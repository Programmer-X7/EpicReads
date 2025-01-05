package com.ardent.epicreads.service.impl;

import com.ardent.epicreads.entity.Book;
import com.ardent.epicreads.repository.BookRepository;
import com.ardent.epicreads.service.BookService;
import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@Service
public class BookServiceImpl implements BookService {

    private final Cloudinary cloudinary;
    private final BookRepository bookRepository;

    @Autowired
    public BookServiceImpl(BookRepository bookRepository,
                           @Value("${cloudinary.cloud_name}") String cloudName,
                           @Value("${cloudinary.api_key}") String apiKey,
                           @Value("${cloudinary.api_secret}") String apiSecret
    ) {
        this.bookRepository = bookRepository;
        cloudinary = new Cloudinary(ObjectUtils.asMap(
                "cloud_name", cloudName,
                "api_key", apiKey,
                "api_secret", apiSecret
        ));
    }

    public Map uploadImage(MultipartFile file) throws IOException {
        return cloudinary.uploader().upload(file.getBytes(), ObjectUtils.emptyMap());
    }

    @Override
    public Book createBook(Book book) {
        return bookRepository.save(book);
    }

    @Override
    public List<Book> getAllBooks() {
        return bookRepository.findAll();
    }

    @Override
    public List<Book> getBookByCategoryId(long id) {
        return bookRepository.getBookByCategoryId(id);
    }

    @Override
    public List<Book> searchBooks(String query) {
        return bookRepository.searchBooks(query);
    }

    @Override
    public List<Book> getBooksByTag(String tag) {
        return bookRepository.getBooksByTag(tag);
    }

    @Override
    public Book getBookById(long id) {
        return bookRepository.findById(id).orElse(null);
    }

    public List<Book> getOutOfStockBooks() {
        return bookRepository.findByQuantity(0);
    }

    @Override
    public Book updateBook(Book book) {
        return bookRepository.save(book);
    }

    @Override
    public void deleteBook(long id) {
        bookRepository.deleteById(id);
    }
}
