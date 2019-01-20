package com.codegym.blog.service;

import com.codegym.blog.model.Category;

public interface CategorySevice {
    Iterable<Category> findAll();

    Category findById(Long id);

    void save(Category category);

    void remove(Long id);
}
