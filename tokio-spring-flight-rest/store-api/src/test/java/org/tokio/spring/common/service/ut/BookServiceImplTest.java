package org.tokio.spring.common.service.ut;

import com.github.javafaker.Book;
import com.github.javafaker.Faker;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;
import org.tokio.spring.common.core.exception.NotFoundException;
import org.tokio.spring.common.domain.Author;
import org.tokio.spring.common.dto.BookDTO;
import org.tokio.spring.common.dto.BookRequestDTO;
import org.tokio.spring.common.dto.BookSearchRequestDTO;
import org.tokio.spring.common.dto.PageDTO;
import org.tokio.spring.common.service.impl.BookServiceImpl;

import java.util.List;

@ExtendWith(MockitoExtension.class)
@ActiveProfiles("test")
class BookServiceImplTest {

    @InjectMocks
    private BookServiceImpl bookService;

    @Mock
    private Faker faker;

    @Mock
    private Book bookMock; // Simulación del objeto Book

    @BeforeEach
    void setUp() {
        // Simulación de llamadas internas en el servicio
        Mockito.when(faker.book()).thenReturn(bookMock);
        Mockito.when(bookMock.author()).thenReturn("Author");
        Mockito.when(bookMock.title()).thenReturn("Title");
        Mockito.when(bookMock.genre()).thenReturn("Genre");

        // Inicializar cualquier lógica necesaria en postConstruct
        bookService.postConstruct();
    }

    @Test
    void givenIdAuthor_whenGetAuthorById_thenReturnSuccessAuthor() {
        // Obtener el autor por ID
        Author author = bookService.getAuthorById(1);

        // Verificar que el autor no sea nulo y que el nombre sea el esperado
        Assertions.assertThat(author)
                .isNotNull()
                .returns("Author", Author::getName)
                .returns(1, Author::getId);
    }

    @Test
    void givenIdAuthorUnknown_whenGetAuthorById_thenReturnNotFoundException() {
        // En este caso, no se precisa simular el comportamiento que se quiere comprobar
        // pero en caso de querer hacerlo, sería tal que así:
        //Mockito.when(bookService.getAuthorById(60)).thenThrow(new AuthorNotFoundException("Author id: %d not found.".formatted(60));

        // Verificar que se lance la excepción correcta
        Assertions.assertThatThrownBy(() -> bookService.getAuthorById(60))
                .isInstanceOf(NotFoundException.class)
                .hasMessage("Author id: %d not found.".formatted(60));
    }

    @Test
    void givenIdBook_whenGetBookByBookId_thenReturnSuccessBookDTO() {

        BookDTO bookDTO = bookService.getBookByBookId(1);

        Assertions.assertThat(bookDTO)
                .isNotNull()
                .returns(1, BookDTO::getId)
                .returns("Title", BookDTO::getTitle)
                .satisfies(bookDTO1 -> Assertions.assertThat(bookDTO1.getAuthorIds()).isNotNull());
    }

    @Test
    void givenIdBook_whenGetBookByBookId_thenReturnNotFoundException() {

        Assertions.assertThatThrownBy(() -> bookService.getBookByBookId(60))
                .isInstanceOf(NotFoundException.class)
                .hasMessage("Book id: %d not found.".formatted(60));
    }

    @Test
    void givenBookSearchRequestDtoWithOutGenreAndPageSizeTwo_whenSearchBookByPageIdAndPageSize_thenReturnPageBooDTOWithPageSizeGiven() {
        final BookSearchRequestDTO bookSearchRequestDTO = BookSearchRequestDTO.builder()
                .page(0)
                .pageSize(2)
                .build();
        PageDTO<BookDTO> bookDTOPageDTO = bookService.searchBookByPageIdAndPageSize(bookSearchRequestDTO);

        Assertions.assertThat(bookDTOPageDTO)
                .isNotNull()
                .returns(0,PageDTO::getPage)
                .returns(2,PageDTO::getPageSize)
                .extracting(PageDTO::getItems)
                .satisfies(bookDTOS -> Assertions.assertThat(bookDTOS).isNotEmpty())
                .extracting(List::getFirst)
                .returns("Title", BookDTO::getTitle)
                .returns(0, BookDTO::getId)
                .extracting(BookDTO::getAuthorIds)
                .extracting(List::getFirst)
                .satisfies(idAuthor -> Assertions.assertThat(idAuthor).isNotNull());
    }


    @Test
    void givenBookSearchRequestDtoWithGenreAndPageSizeTwo_whenSearchBookByPageIdAndPageSize_thenReturnPageBooDTOWithPageSizeGiven() {
        final BookSearchRequestDTO bookSearchRequestDTO = BookSearchRequestDTO.builder()
                .page(0)
                .pageSize(2)
                .genre("Genre")
                .build();
        PageDTO<BookDTO> bookDTOPageDTO = bookService.searchBookByPageIdAndPageSize(bookSearchRequestDTO);

        Assertions.assertThat(bookDTOPageDTO)
                .isNotNull()
                .returns(0,PageDTO::getPage)
                .returns(2,PageDTO::getPageSize)
                .extracting(PageDTO::getItems)
                .satisfies(bookDTOS -> Assertions.assertThat(bookDTOS).isNotEmpty())
                .extracting(List::getFirst)
                .returns("Title", BookDTO::getTitle)
                .returns(0, BookDTO::getId)
                .extracting(BookDTO::getAuthorIds)
                .extracting(List::getFirst)
                .satisfies(idAuthor -> Assertions.assertThat(idAuthor).isNotNull());
    }

    @Test
    void givenBookSearchRequestDtoWithGenreUnknownAndPageSizeTwo_whenSearchBookByPageIdAndPageSize_thenReturnPageBooDTOWithItemEmpty() {
        final BookSearchRequestDTO bookSearchRequestDTO = BookSearchRequestDTO.builder()
                .page(0)
                .pageSize(2)
                .genre("Thriller")
                .build();
        PageDTO<BookDTO> bookDTOPageDTO = bookService.searchBookByPageIdAndPageSize(bookSearchRequestDTO);

        Assertions.assertThat(bookDTOPageDTO)
                .isNotNull()
                .returns(0,PageDTO::getPage)
                .returns(2,PageDTO::getPageSize)
                .extracting(PageDTO::getItems)
                .satisfies(bookDTOS -> Assertions.assertThat(bookDTOS).isEmpty());
    }

    @Test
    void givenBookSearchRequestDtoWithPageOverflow_whenSearchBookByPageIdAndPageSize_thenReturnPageBooDTOWithItemEmpty() {
        final BookSearchRequestDTO bookSearchRequestDTO = BookSearchRequestDTO.builder()
                .page(6)
                .pageSize(2)
                .genre("Genre")
                .build();
        PageDTO<BookDTO> bookDTOPageDTO = bookService.searchBookByPageIdAndPageSize(bookSearchRequestDTO);

        Assertions.assertThat(bookDTOPageDTO)
                .isNotNull()
                .returns(6,PageDTO::getPage)
                .returns(2,PageDTO::getPageSize)
                .extracting(PageDTO::getItems)
                .satisfies(bookDTOS -> Assertions.assertThat(bookDTOS).isEmpty());
    }

    @Test
    void givenBookId_whenDeleteByIdBook_thenReturnSuccess(){

        bookService.deleteBookById(1);
        Assertions.assertThatThrownBy(() -> bookService.getBookByBookId(1))
                .isInstanceOf(NotFoundException.class)
                .hasMessage("Book id: %d not found.".formatted(1));
    }

    @Test
    void givenBookIdUnknown_whenDeleteByIdBook_thenReturnExceptionNotFound(){

        Assertions.assertThatThrownBy(() -> bookService.getBookByBookId(99))
                .isInstanceOf(NotFoundException.class)
                .hasMessage("Book id: %d not found.".formatted(99));
    }

    @Test
    void givenBookRequest_whenCreateBook_thenReturnBookDTO(){
        final BookRequestDTO bookRequestDTO = BookRequestDTO.builder()
                .authorId(1)
                .genre("Thriller")
                .title("Sherlock").build();

        final BookDTO bookDTO = bookService.createBook(bookRequestDTO);

        Assertions.assertThat(bookDTO).isNotNull()
                .returns(10,BookDTO::getId)
                .returns("Thriller", BookDTO::getGenre)
                .returns("Sherlock", BookDTO::getTitle);
    }

    @Test
    void givenBookRequest_whenEditBook_thenReturnBookDTO(){
        final BookRequestDTO bookRequestDTO = BookRequestDTO.builder()
                .authorId(1)
                .genre("Thriller")
                .title("Sherlock").build();

        final BookDTO bookDTO = bookService.editBook(1,bookRequestDTO);

        Assertions.assertThat(bookDTO).isNotNull()
                .returns(1,BookDTO::getId)
                .returns("Thriller", BookDTO::getGenre)
                .returns("Sherlock", BookDTO::getTitle);
    }
}