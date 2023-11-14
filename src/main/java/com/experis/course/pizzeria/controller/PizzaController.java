package com.experis.course.pizzeria.controller;

import com.experis.course.pizzeria.exception.NameUniqueException;
import com.experis.course.pizzeria.exception.PizzaNotFoundException;
import com.experis.course.pizzeria.model.Pizza;
import com.experis.course.pizzeria.service.PizzaService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Optional;

@Controller
@RequestMapping("/pizzas")
public class PizzaController {

    @Autowired
    private PizzaService pizzaService;

    @GetMapping
    public String index(@RequestParam Optional<String> search, Model model) {
        model.addAttribute("pizzaList", pizzaService.getPizzaList(search));
        return "pizzas/index";
    }

    @GetMapping("/show/{id}")
    public String show(@PathVariable Integer id, Model model) {
        try {
            Pizza pizza = pizzaService.getPizzaById(id);
            model.addAttribute("pizza", pizza);
            return "pizzas/show";
        } catch (PizzaNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    @GetMapping("/create")
    public String create(Model model) {
        model.addAttribute("pizza", new Pizza());
        return "pizzas/form";
    }

    @PostMapping("/create")
    public String doCreate(@Valid @ModelAttribute("pizza") Pizza formPizza, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "pizzas/form";
        }
        try {
            Pizza savedPizza = pizzaService.createPizza(formPizza);
            return "redirect:/pizzas/show/" + savedPizza.getId();
        } catch (NameUniqueException e) {
            bindingResult.addError(new FieldError("pizza", "name", formPizza.getName(),
                    false, null, null,
                    "Already exist a pizza with this name!"));
            return "pizzas/form";
        }
    }

    @GetMapping("/edit/{id}")
    public String edit(@PathVariable Integer id, Model model) {
        try {
            model.addAttribute("pizza", pizzaService.getPizzaById(id));
            return "pizzas/form";
        } catch (PizzaNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    @PostMapping("/edit/{id}")
    public String doEdit(@PathVariable Integer id, @Valid @ModelAttribute("pizza") Pizza formPizza, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "pizzas/form";
        }
        try {
            Pizza savedPizza = pizzaService.editPizza(formPizza);
            return "redirect:/pizzas/show/" + savedPizza.getId();
        } catch (PizzaNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    @PostMapping("/delete/{id}")
    public String delete(@PathVariable Integer id, RedirectAttributes redirectAttributes) {
        try {
            Pizza pizzaToDelete = pizzaService.getPizzaById(id);
            pizzaService.deletePizza(id);
            redirectAttributes.addFlashAttribute("message", "Pizza " + pizzaToDelete.getName() + " deleted!");
            return "redirect:/pizzas";
        } catch (PizzaNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }
}
