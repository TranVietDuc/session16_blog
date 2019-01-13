package com.codegym.blog.controller;


import com.codegym.blog.model.Blog;
import com.codegym.blog.service.BlogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller
public class BlogController {

    @Autowired
    private BlogService blogService;

 @GetMapping("/")
     public ModelAndView getStart(){
     List<Blog> blogs = blogService.findAll();
     ModelAndView modelAndView = new ModelAndView("/list");
     modelAndView.addObject("blogs",blogs);
     return modelAndView;
     }

 @GetMapping("/create-blog")
    public ModelAndView showFormCreate(){
     ModelAndView modelAndView = new ModelAndView("/create");
     modelAndView.addObject("blog",new Blog());
     return modelAndView;
 }

 @PostMapping("/create-blog")
    public ModelAndView creatBlog(@ModelAttribute("newBlog") Blog blog){
     blogService.save(blog);

     ModelAndView modelAndView = new ModelAndView("/create");
     modelAndView.addObject("blog",new Blog());
     modelAndView.addObject("message","New Blog was created");
     return modelAndView;
 }
 @GetMapping("/view-blog/{id}")
    public ModelAndView showContent(@PathVariable Long id) {
     Blog blog = blogService.findById(id);
     ModelAndView modelAndView = new ModelAndView("/viewDetail");
     modelAndView.addObject("blog",blog);
     return modelAndView;
 }
}
