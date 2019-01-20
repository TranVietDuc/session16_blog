package com.codegym.blog.formatter;

import com.codegym.blog.model.Category;
import com.codegym.blog.service.CategorySevice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.Formatter;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.util.Locale;

@Component
public class CategoryFormatter implements Formatter<Category> {

    private CategorySevice categorySevice;

    @Autowired
    public CategoryFormatter(CategorySevice categorySevice){
        this.categorySevice=categorySevice;
    }

    @Override
    public Category parse(String text, Locale locale) throws ParseException {
        return categorySevice.findById(Long.parseLong(text));
    }

    @Override
    public String print(Category object, Locale locale) {
        return "[" + object.getId() + ", " +object.getName() + "]";
    }
}
