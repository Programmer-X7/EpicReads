package com.ardent.epicreads.controller;

import com.ardent.epicreads.entity.Book;
import com.ardent.epicreads.entity.Category;
import com.ardent.epicreads.service.BookService;
import com.ardent.epicreads.service.CategoryService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@Controller
public class CategoryController {

    private final CategoryService categoryService;
    private final BookService bookService;

    @Autowired
    public CategoryController(CategoryService categoryService, BookService bookService) {
        this.categoryService = categoryService;
        this.bookService = bookService;
    }

    // Get Create Category From
    @GetMapping("/category/add")
    public String getAddCategoryForm(HttpServletRequest request, Model model) {
        model.addAttribute("currentPath", request.getRequestURI());
        return "admin/add-category";
    }

    // Create Category
    @PostMapping("/category/add")
    public String addCategory(@RequestParam("name") String categoryName,
                              @RequestParam("categoryImage") MultipartFile categoryImage,
                              RedirectAttributes redirectAttributes
    ) {
        Category category = new Category();
        category.setName(categoryName);
        try {
            if (categoryImage != null && !categoryImage.isEmpty()) {
                System.out.println("Received file: " + categoryImage.getOriginalFilename());

                // Upload the image to Cloudinary and get the URL
                Map uploadResult = bookService.uploadImage(categoryImage);
                String imageUrl = uploadResult.get("url").toString();
                category.setImage(imageUrl);
            }
            categoryService.createCategory(category);
            redirectAttributes.addFlashAttribute("successMessage", "Category added successfully!");
        } catch (IOException e) {
            e.printStackTrace();
        }

        return "redirect:/category";
    }

    // Get All Categories
    @GetMapping("/category")
    public String getAllCategories(Model model, Authentication authentication, HttpServletRequest request) {
        List<Category> categories = categoryService.getAllCategory();
        model.addAttribute("categories", categories);
        model.addAttribute("currentPath", request.getRequestURI());

        if (authentication != null && authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .anyMatch(role -> role.equals("ROLE_ADMIN"))) {
            return "admin/all-categories";
        } else {
            return "categories";
        }
    }

    // Update Category
    @GetMapping("/category/edit/{categoryId}")
    public String getUpdateCategoryForm (@PathVariable() long categoryId, HttpServletRequest request, Model model) {
        Category category = categoryService.getCategoryById(categoryId);
        model.addAttribute("category", category);
        model.addAttribute("currentPath", request.getRequestURI());
        model.addAttribute(request.getRequestURI());
        return "admin/edit-category";
    }

    @PostMapping("/category/edit")
    public String updateCategory (@RequestParam("id") Long categoryId,
                                  @RequestParam("name") String categoryName,
                                  @RequestParam("categoryImage") MultipartFile categoryImage,
                                  RedirectAttributes redirectAttributes) {


        Category category = categoryService.getCategoryById(categoryId);
        category.setName(categoryName);

        if (!categoryImage.isEmpty()) {
            try {
                Map uploadResult = bookService.uploadImage(categoryImage);
                String imageUrl = uploadResult.get("url").toString();

                category.setImage(imageUrl);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        categoryService.updateCategory(category);
        redirectAttributes.addFlashAttribute("successMessage", "Category updated successfully!");
        return "redirect:/category";

    }

    @PostMapping("/category/delete/{categoryId}")
    public String deleteCategory(@PathVariable("categoryId") long categoryId, RedirectAttributes redirectAttributes) {
        categoryService.deleteCategory(categoryId);
        redirectAttributes.addFlashAttribute("successMessage", "Category deleted successfully!");
        return "redirect:/category";
    }

}
