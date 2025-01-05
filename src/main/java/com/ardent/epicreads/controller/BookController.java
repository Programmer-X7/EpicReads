package com.ardent.epicreads.controller;

import com.ardent.epicreads.entity.Book;
import com.ardent.epicreads.entity.Category;
import com.ardent.epicreads.service.BookService;
import com.ardent.epicreads.service.CategoryService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/books")
public class BookController {

    private final BookService bookService;
    private final CategoryService categoryService;

    @Autowired
    public BookController(BookService bookService, CategoryService categoryService) {
        this.bookService = bookService;
        this.categoryService = categoryService;
    }

    @GetMapping("/add")
    public String showAddBookForm(Model model, HttpServletRequest request) {
        List<Category> allCategory = categoryService.getAllCategory();

//        model.addAttribute("book", new Book());
        model.addAttribute("categories", allCategory);
        model.addAttribute("currentPath", request.getRequestURI());

        return "admin/add-book";
    }

    @PostMapping("/add")
    public String addBook(@ModelAttribute Book book, @RequestParam("bookImage") MultipartFile bookImage, RedirectAttributes redirectAttributes) {
        try {
            // Log the received file info for debugging
            if (bookImage != null && !bookImage.isEmpty()) {
                System.out.println("Received file: " + bookImage.getOriginalFilename());

                // Upload the image to Cloudinary and get the URL
                Map uploadResult = bookService.uploadImage(bookImage);
                String imageUrl = uploadResult.get("url").toString();

                // Set the thumbnail URL and in the Book entity
                book.setThumbnail(imageUrl);
            }

            // Save the book to the database
            bookService.createBook(book);
            redirectAttributes.addFlashAttribute("successMessage", "Book added successfully!");

        } catch (IOException e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error uploading image: " + e.getMessage());
            e.printStackTrace();
        }

        return "redirect:/books";
    }

    @GetMapping
    public String getAllBooks(Model model, HttpServletRequest request) {
        List<Book> books = bookService.getAllBooks();
        model.addAttribute("books", books);
        model.addAttribute("currentPath", request.getRequestURI());

//        return "book-list";
        return "admin/all-books";
    }

    @GetMapping("/category/{categoryId}")
    public String getBookByCategory(@PathVariable long categoryId, Model model) {
        List<Book> books = bookService.getBookByCategoryId(categoryId);
        Category category = categoryService.getCategoryById(categoryId);
        model.addAttribute("books", books);
        model.addAttribute("category", category);
        return "book-list";
    }

    @GetMapping("/search")
    public String searchBooks(@RequestParam String query, Model model) {
        List<Book> books = bookService.searchBooks(query);
        model.addAttribute("books", books);
        return "search-results";
    }

    @GetMapping("/{id}")
    public String getBookDetails(@PathVariable("id") Long bookId, Model model, HttpServletRequest request) {
        Book book = bookService.getBookById(bookId);
        model.addAttribute("book", book);
        model.addAttribute("currentPath", request.getRequestURI());
        return "book-details";
    }

    @GetMapping("/out-of-stock")
    public String getOutOfStockBooks (Model model) {
        List<Book> outOfStockBooks = bookService.getOutOfStockBooks();
        model.addAttribute("books", outOfStockBooks);
        model.addAttribute("currentPath", "/books");
        return "admin/out-of-stocks";
    }

    @GetMapping("/update/{bookId}")
    public String showUpdateBookForm(@PathVariable long bookId, Model model, HttpServletRequest request) {
        Book existingBook = bookService.getBookById(bookId);
        List<Category> allCategory = categoryService.getAllCategory();
        Category existingCategory = categoryService.getCategoryById(existingBook.getCategoryId());

        model.addAttribute("book", existingBook);
        model.addAttribute("existingCategory", existingCategory);
        model.addAttribute("categories", allCategory);
        model.addAttribute("currentPath", request.getRequestURI());

        return "admin/update-book";
    }

    @PostMapping("/update")
    public String updateBook(
            @RequestParam("id") Long id,
            @RequestParam("name") String name,
            @RequestParam("author") String author,
            @RequestParam("categoryId") Long categoryId,
            @RequestParam("description") String description,
            @RequestParam("isbn") String isbn,
            @RequestParam("language") String language,
            @RequestParam("pageCount") int pageCount,
            @RequestParam("price") double price,
            @RequestParam("publisher") String publisher,
            @RequestParam("quantity") int quantity,
            @RequestParam("bookImage") MultipartFile bookImage,
            RedirectAttributes redirectAttributes
    ) {
        Book book = bookService.getBookById(id);
        book.setName(name);
        book.setAuthor(author);
        book.setCategoryId(categoryId);
        book.setDescription(description);
        book.setIsbn(isbn);
        book.setLanguage(language);
        book.setPageCount(pageCount);
        book.setPrice(price);
        book.setLanguage(publisher);
        book.setQuantity(quantity);

        if (!bookImage.isEmpty()) {
            try {
                Map uploadResult = bookService.uploadImage(bookImage);
                String imageUrl = uploadResult.get("url").toString();

                book.setThumbnail(imageUrl);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        bookService.updateBook(book);
        redirectAttributes.addFlashAttribute("successMessage", "Book updated successfully!");
        return "redirect:/books";
    }

    @PostMapping("/delete/{bookId}")
    public String deleteBook(@PathVariable("bookId") long bookId, RedirectAttributes redirectAttributes) {
        bookService.deleteBook(bookId);
        redirectAttributes.addFlashAttribute("successMessage", "Book deleted successfully!");
        return "redirect:/books";
    }
}
