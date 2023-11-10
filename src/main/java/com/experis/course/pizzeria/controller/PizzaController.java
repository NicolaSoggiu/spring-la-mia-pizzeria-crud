package com.experis.course.pizzeria.controller;

import com.experis.course.pizzeria.model.Pizza;
import com.experis.course.pizzeria.repository.PizzaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/pizzas")
public class PizzaController {
    @Autowired
    private PizzaRepository pizzaRepository;

    @GetMapping
    public String index(@RequestParam Optional<String> search, Model model) {
        List<Pizza> pizzaList;
        if (search.isPresent()) {
            pizzaList = pizzaRepository.findByNameContainingIgnoreCase(search.get());
        } else {
            pizzaList = pizzaRepository.findAll();
        }
        model.addAttribute("pizzaList", pizzaList);
        return "pizzas/index";
    }

    @GetMapping("/show/{id}")
    public String show(@PathVariable Integer id, Model model) {
        model.addAttribute("pizza", pizzaRepository.findById(id).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "The pizza with id " + id + " doesn't exist!")));
        return "pizzas/show";
    }
}
