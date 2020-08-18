package com.ceti.backend.web.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;


import java.util.List;


@Controller
public class HomeController {


    @GetMapping(value = "/")
    public String doShowHomePage(Model model) {

        Pageable page = PageRequest.of(0, 20, Sort.by("id").descending());

        return "index";
    }
}
