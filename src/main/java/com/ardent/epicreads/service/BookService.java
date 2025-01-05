package com.ardent.epicreads.service;

import com.ardent.epicreads.entity.Book;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public interface BookService {

    Book createBook(Book book);

    Map uploadImage(MultipartFile file) throws IOException;

    List<Book> getAllBooks();

    List<Book> getBookByCategoryId(long id);

    List<Book> searchBooks(String query);

    List<Book> getBooksByTag(String tag);

    List<Book> getOutOfStockBooks();

    Book getBookById(long id);

    Book updateBook(Book book);

    void deleteBook(long id);
}
