package com.example.furniturestore.Repotitory;

import com.example.furniturestore.Entity.OurUsers;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface OurUserRepo extends JpaRepository<OurUsers, Long> {
    OurUsers findByEmail(String email);

}
