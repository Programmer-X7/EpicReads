package com.ardent.epicreads.repository;

import com.ardent.epicreads.entity.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {

    @Query("SELECT b FROM Book b WHERE " +
            "LOWER(b.name) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
            "LOWER(b.author) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
            "LOWER(b.publisher) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
            "LOWER(b.isbn) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
            "LOWER(b.language) LIKE LOWER(CONCAT('%', :query, '%'))")
    List<Book> searchBooks(@Param("query") String query);

    List<Book> getBookByCategoryId(long id);

    List<Book> getBooksByTag(String tag);

    @Query("SELECT COUNT(b) FROM Book b WHERE b.quantity = 0")
    long countOutOfStockProducts();

    List<Book> findByQuantity(int quantity);
}
