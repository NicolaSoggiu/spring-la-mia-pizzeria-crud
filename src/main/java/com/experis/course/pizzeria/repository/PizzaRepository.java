package com.experis.course.pizzeria.repository;

import com.experis.course.pizzeria.model.Pizza;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PizzaRepository extends JpaRepository<Pizza, Integer> {
}
