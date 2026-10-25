package com.crb.seaturtlewebservice.repository;

import com.crb.seaturtlewebservice.domain.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
    Optional<Category> getCategoryByUrlName(String urlName);
    Optional<Category>getCategoryByName(String keyword);
}
