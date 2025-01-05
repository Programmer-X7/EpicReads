package com.ardent.epicreads.controller;

import com.ardent.epicreads.entity.Book;
import com.ardent.epicreads.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Arrays;
import java.util.List;

@Controller
public class DemoController {

    private final BookRepository bookRepository;

    @Autowired
    public DemoController(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    @GetMapping("/")
    public String home(@RequestParam(value = "logoutSuccess", required = false) String logoutSuccess, Model model) {

        if (logoutSuccess != null) {
            model.addAttribute("logoutSuccessMessage", "You have successfully logged out.");
        }

        List<Book> featuredBooks = bookRepository.getBooksByTag("featured-book");
        List<Book> bestSellers = bookRepository.getBooksByTag("best-seller");

        // Limit to 6 books for featuredBooks
        if (featuredBooks.size() > 6) {
            featuredBooks = featuredBooks.subList(0, 6);
        }

        // Limit to 12 books for bestSellers
        if (bestSellers.size() > 12) {
            bestSellers = bestSellers.subList(0, 12);
        }

        model.addAttribute("featuredBooks", featuredBooks);
        model.addAttribute("bestSellers", bestSellers);

        return "home";
    }

    @GetMapping("/about")
    public String aboutPage() {
        return "about";
    }

    @GetMapping("/contact")
    public String contactPage() {
        return "contact";
    }
}
