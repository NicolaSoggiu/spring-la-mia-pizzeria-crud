package com.experis.course.pizzeria.repository;

import com.experis.course.pizzeria.model.Pizza;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PizzaRepository extends JpaRepository<Pizza, Integer> {
    List<Pizza> findByNameContainingIgnoreCase(String nameKeyword);
}
