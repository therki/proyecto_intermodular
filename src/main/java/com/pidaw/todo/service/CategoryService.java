package com.pidaw.todo.service;

import com.pidaw.todo.model.Category;
import com.pidaw.todo.repos.CategoryRepository;
import com.pidaw.todo.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;
    private final TaskService taskService;
/* LISTAR CATEGORIAS */
    public List<Category> listCategories(User user) {
        // Admin ver todas las categorias
        List<Category> userCategories;
        if (user.getRole() != User.RoleType.USUARIO) {
            return categoryRepository.findAllWithUser(Sort.by("title").ascending());
        } else {
            // Rol usuario puede ver las propias más las del admin
            // Categorias del usuario
            userCategories = categoryRepository.findByUserId(user.getId(), Sort.by("title").ascending());
            // Categorias del administrador
            List<Category> adminCategories = categoryRepository.findByUserId(1L, Sort.by("title").ascending());

            // Combinar todas en un listado
            List<Category> allCategories = new ArrayList<>();
            allCategories.addAll(userCategories);
            for (Category adminCat : adminCategories) {
                if (!allCategories.contains(adminCat)) {
                    allCategories.add(adminCat);
                }
            }

            return allCategories;
        }

    }

/* OBTENER CATEGORIA POR ID */
    public Category findByID(Long id, User user){
        Category category =  categoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Categoría no encontrada con ID: " + id));

        if (user.getRole() != User.RoleType.ADMIN && 
                !category.getUser().getId().equals(user.getId())) {
                throw new SecurityException("No tienes permiso para ver esta categoría");
            }
            
            return category;
    }

/* CREAR NUEVA CATGORIA */
    public Category save(Category category, User user) {
        category.setUser(user);
        return categoryRepository.save(category);
    }

/* EDITAR CATEGORIA */
    public Category edit(Long id, Category category, User user) {
    return categoryRepository.findById(id)
            .map(cat -> {
                if (user.getRole() != User.RoleType.ADMIN &&
                        !cat.getUser().getId().equals(user.getId())) {
                    throw new SecurityException("No tienes permiso para editar esta categoría");
                }
                cat.setTitle(category.getTitle());
                return categoryRepository.save(cat);
            })
            .orElseThrow(() -> new RuntimeException("Categoría no encontrada"));
    }
/* ELIMINAR CATEGORIA */
    public void deleteById(Long id, User user) {
        Category category = findByID(id, user);
        // No borrar categoria por defecto
        if (id == 1L) {
            throw new RuntimeException("No está permitido eliminar la categoría principal.");
        }

        taskService.updateCategoryBeforeDelete(id, 1L);
        categoryRepository.deleteById(id);
}
}
