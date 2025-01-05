package com.ardent.epicreads.service;

import com.ardent.epicreads.entity.Category;

import java.util.List;

public interface CategoryService {
    Category createCategory(Category category);
    List<Category> getAllCategory();
    Category getCategoryById(long id);
    Category updateCategory(Category category);
    void deleteCategory(long id);
}
