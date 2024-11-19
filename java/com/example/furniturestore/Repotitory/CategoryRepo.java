package com.example.furniturestore.Repotitory;


import com.example.furniturestore.Entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CategoryRepo extends JpaRepository<Category, Long> {

    //ktra trùng tên hay ko
    Optional<Category> findByName(String name);
}
