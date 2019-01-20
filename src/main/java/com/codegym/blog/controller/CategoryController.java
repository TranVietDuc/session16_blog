package com.codegym.blog.controller;

import com.codegym.blog.model.Category;
import com.codegym.blog.service.CategorySevice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class CategoryController {

    @Autowired
    private CategorySevice categorySevice;

   @GetMapping("/categories")
    public ModelAndView listCategories(){
       Iterable<Category> categories = categorySevice.findAll();
       ModelAndView modelAndView = new ModelAndView("/category/list");
       modelAndView.addObject("categories",categories);
       return modelAndView;
    }

    @GetMapping("/create-category")
    public ModelAndView showCreatCategoryForm(){
       ModelAndView modelAndView = new ModelAndView("/category/create");
       modelAndView.addObject("category", new Category());
       return modelAndView;
    }
    @PostMapping("/create-category")
    public ModelAndView creatCategory(@ModelAttribute("category") Category category){
       categorySevice.save(category);

       ModelAndView modelAndView = new ModelAndView("/category/create");
       modelAndView.addObject("category",new Category());
       modelAndView.addObject("message","New Category was created");
       return modelAndView;
    }

    @GetMapping("/edit-category/{id}")
    public ModelAndView showEditCategoryForm(@PathVariable Long id){
       Category category = categorySevice.findById(id);

       if (category != null){

           ModelAndView modelAndView = new ModelAndView("/category/edit");
           modelAndView.addObject("category",category);
           return modelAndView;
       }else {
           ModelAndView modelAndView = new ModelAndView("error.404");
           return modelAndView;
       }
    }

    @PostMapping("/edit-category")
    public ModelAndView editCategory(@ModelAttribute("category") Category category){
        categorySevice.save(category);

        ModelAndView modelAndView = new ModelAndView("/category/edit");
        modelAndView.addObject("category", category);
        modelAndView.addObject("message","New Category was edit successfully");
        return modelAndView;
    }

    @GetMapping("/delete-category/{id}")
    public ModelAndView showDeleteForm(@PathVariable Long id) {
        Category category = categorySevice.findById(id);

        if (category != null) {

            ModelAndView modelAndView = new ModelAndView("/category/delete");
            modelAndView.addObject("category", category);
            return modelAndView;
        } else {
            ModelAndView modelAndView = new ModelAndView("error.404");
            return modelAndView;
        }
    }

    @PostMapping("/delete-category")
    public String deleteCategory(@ModelAttribute("category") Category category){
       categorySevice.remove(category.getId());
       return "redirect:/categories";
     }
}
