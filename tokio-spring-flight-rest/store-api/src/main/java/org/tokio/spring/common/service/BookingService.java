package org.tokio.spring.common.service;

import org.tokio.spring.common.core.exception.NotFoundException;
import org.tokio.spring.common.domain.Author;
import org.tokio.spring.common.domain.Book;
import org.tokio.spring.common.dto.BookDTO;
import org.tokio.spring.common.dto.BookRequestDTO;
import org.tokio.spring.common.dto.BookSearchRequestDTO;
import org.tokio.spring.common.dto.PageDTO;

public interface BookingService {

    // metodos que simulan llamadas a un repositorio
    Author getAuthorById(int authorId) throws NotFoundException;
    Book getBookById(int bookId) throws NotFoundException ;

    // metodos de la lógica de negocio
    BookDTO getBookByBookId(int bookId) throws NotFoundException ;
    PageDTO<BookDTO> searchBookByPageIdAndPageSize(BookSearchRequestDTO bookSearchRequestDTO) throws NotFoundException ;
    void deleteBookById(int bookId) throws NotFoundException;
    BookDTO createBook(BookRequestDTO bookRequestDTO) throws NotFoundException;
    BookDTO editBook(int bookId, BookRequestDTO bookRequestDTO) throws NotFoundException;
}

