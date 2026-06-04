package com.pidaw.todo.repos;

import com.pidaw.todo.model.Category;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CategoryRepository extends JpaRepository<Category, Long> {
    List<Category> findByUserId(Long id, Sort title);
    
    Category findByTitle(String title);

    @Query("SELECT DISTINCT c FROM Category c LEFT JOIN FETCH c.user")
    List<Category> findAllWithUser(Sort sort);
}
