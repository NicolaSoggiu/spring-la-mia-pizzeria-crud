package com.experis.course.pizzeria.service;

import com.experis.course.pizzeria.exception.NameUniqueException;
import com.experis.course.pizzeria.exception.PizzaNotFoundException;
import com.experis.course.pizzeria.model.Pizza;
import com.experis.course.pizzeria.repository.PizzaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PizzaService {

    @Autowired
    private PizzaRepository pizzaRepository;

    public List<Pizza> getPizzaList(Optional<String> search) {
        if (search.isPresent()) {
            return pizzaRepository.findByNameContainingIgnoreCase(search.get());
        } else {
            return pizzaRepository.findAll();
        }
    }

    public Pizza getPizzaById(Integer id) throws PizzaNotFoundException {
        Optional<Pizza> result = pizzaRepository.findById(id);
        if (result.isPresent()) {
            return result.get();
        } else {
            throw new PizzaNotFoundException("Pizza with id " + id + " not found!");
        }
    }

    public Pizza createPizza(Pizza pizza) throws NameUniqueException {
        try {
            return pizzaRepository.save(pizza);
        } catch (RuntimeException e) {
            throw new NameUniqueException(pizza.getName());
        }
    }

    public Pizza editPizza(Pizza pizza) throws PizzaNotFoundException {
        Pizza pizzaToEdit = getPizzaById(pizza.getId());
        pizzaToEdit.setName(pizza.getName());
        pizzaToEdit.setDescription(pizza.getDescription());
        pizzaToEdit.setImage(pizza.getImage());
        pizzaToEdit.setPrice(pizza.getPrice());

        return pizzaRepository.save(pizzaToEdit);
    }

    public void deletePizza(Integer id) {
        pizzaRepository.deleteById(id);
    }
}
