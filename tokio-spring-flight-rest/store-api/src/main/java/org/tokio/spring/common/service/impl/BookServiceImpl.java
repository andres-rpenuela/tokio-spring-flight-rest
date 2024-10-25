package org.tokio.spring.common.service.impl;

import com.github.javafaker.Faker;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import org.tokio.spring.common.core.exception.NotFoundException;
import org.tokio.spring.common.domain.Author;
import org.tokio.spring.common.domain.Book;
import org.tokio.spring.common.dto.BookDTO;
import org.tokio.spring.common.dto.BookRequestDTO;
import org.tokio.spring.common.dto.BookSearchRequestDTO;
import org.tokio.spring.common.dto.PageDTO;
import org.tokio.spring.common.helper.NumberHelper;
import org.tokio.spring.common.service.BookingService;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.IntStream;

@Service
@RequiredArgsConstructor
public class BookServiceImpl implements BookingService {

    private final Faker faker;

    private List<Book> books;
    private List<Author> authors;

    @PostConstruct
    public void postConstruct() throws NotFoundException {

        authors = new ArrayList<>();
        IntStream.range(0,10)
                .mapToObj(item-> Author.builder().id(item).name(faker.book().author()).build())
                .forEach(authors::add);

        books = new ArrayList<>();
        IntStream.range(0,10)
                .mapToObj(item-> Book.builder().id(item)
                        .title(faker.book().title())
                        .genre(faker.book().genre())
                        .authors( List.of( authors.get(NumberHelper.integerRandom(NumberHelper.THEN)) ) )
                        .build())
                .forEach(books::add);
    }

    @Override
    public Author getAuthorById(int authorId) throws NotFoundException {
        return authors.stream()
                .filter(author -> author.getId() == authorId).
                findFirst().orElseThrow(() -> new NotFoundException("Author id: %d not found.".formatted(authorId)));
    }

    @Override
    public Book getBookById(int bookId) throws NotFoundException {
        return books.stream()
                .filter(book -> book.getId() == bookId)
                .findFirst().orElseThrow(() -> new NotFoundException("Book id: %d not found.".formatted(bookId)));
    }

    @Override
    public BookDTO getBookByBookId(int bookId)throws NotFoundException {
        return toBookDTO(getBookById(bookId));
    }

    @Override
    public PageDTO<BookDTO> searchBookByPageIdAndPageSize(@NonNull BookSearchRequestDTO bookSearchRequestDTO) throws NotFoundException {
        // Se filta la lista de Books, si no devuelve todos
        final List<Book> filteredBooks = Optional.of(bookSearchRequestDTO)
                .map(BookSearchRequestDTO::getGenre)
                .map(StringUtils::trimToNull)
                .map(StringUtils::lowerCase)
                .map(value -> books.stream().filter(book -> {
                    final String genre = book.getGenre();
                    return  genre != null ? genre.toLowerCase().contains(value) : null;
                }).toList()).orElseGet(()->books);

        // inicio
        final int start = bookSearchRequestDTO.getPage() * bookSearchRequestDTO.getPageSize();

        if( start <0 ||  start >= filteredBooks.size() ){ // No hay elementos que mostrar
            return PageDTO.<BookDTO>builder()
                    .items(List.of())
                    .page(bookSearchRequestDTO.getPage())
                    .pageSize(bookSearchRequestDTO.getPageSize())
                    .total(filteredBooks.size()).build();
        }

        final int end = Math.min(start + bookSearchRequestDTO.getPageSize(), filteredBooks.size());

        final List<BookDTO> items =
                IntStream.range(start,end).mapToObj(filteredBooks::get).map(this::toBookDTO).toList();

        return PageDTO.<BookDTO>builder()
                .items(items)
                .page(bookSearchRequestDTO.getPage())
                .pageSize(bookSearchRequestDTO.getPageSize())
                .total(filteredBooks.size()).build();
    }

    @Override
    public void deleteBookById(int bookId) throws NotFoundException {
        final Book maybeBook = Optional.of(this.getBookById(bookId))
                .orElseThrow(()->new NotFoundException("Book id: %d not found.".formatted(bookId)));

        books.remove(maybeBook);
    }

    @Override
    public BookDTO createBook(@NonNull BookRequestDTO bookRequestDTO) throws NotFoundException {
        final Book book = Book.builder()
                .id(this.nextBookId())
                .genre(bookRequestDTO.getGenre())
                .title(bookRequestDTO.getTitle())
                .authors(List.of(getAuthorById(bookRequestDTO.getAuthorId())))
                .build();

        books.add(book);
        return toBookDTO(book);
    }

    @Override
    public BookDTO editBook(int bookId,@NonNull BookRequestDTO bookRequestDTO) throws NotFoundException {
        Book book = getBookById(bookId);
        book.setTitle(bookRequestDTO.getTitle());
        book.setGenre(bookRequestDTO.getGenre());
        book.setAuthors( List.of( getAuthorById(bookRequestDTO.getAuthorId())) );

        // guardar informacion
        books.removeIf(book1 -> book1.getId() == bookId);
        books.add(book);

        return toBookDTO(book);
    }

    protected BookDTO toBookDTO(@NonNull Book book){
        return BookDTO.builder()
                .id(book.getId())
                .genre(book.getGenre())
                .title(book.getTitle())
                .authorIds(book.getAuthors().stream().map(Author::getId).toList())
                .build();
    }

    private synchronized int nextBookId(){
        return books.stream().map(Book::getId).reduce(Math::max).map(id->id + 1).orElse(1);
    }
}
