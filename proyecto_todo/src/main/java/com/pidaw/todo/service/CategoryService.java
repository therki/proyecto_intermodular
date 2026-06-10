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
        // Si es Admin o Gestor, ven todas las categorías del sistema
        if (user.getRole() != User.RoleType.USUARIO) {
            return categoryRepository.findAll(Sort.by("title").ascending()); 
            // Nota: Usa findAll estándar de Spring Data para evitar fallos si findAllWithUser daba problemas
        } else {
            // Si es un Usuario normal, debe ver sus categorías propias MÁS las categorías creadas por los ADMIN o GESTORES
            List<Category> allCategories = categoryRepository.findAll(Sort.by("title").ascending());
            List<Category> filteredCategories = new ArrayList<>();

            for (Category cat : allCategories) {
                // Añadimos la categoría si:
                // 1. Le pertenece al propio usuario logueado
                // 2. O si fue creada por un ADMIN o GESTOR (categorías del sistema para todos)
                if (cat.getUser() == null || 
                    cat.getUser().getId().equals(user.getId()) || 
                    cat.getUser().getRole() == User.RoleType.ADMIN || 
                    cat.getUser().getRole() == User.RoleType.GESTOR) {
                    
                    filteredCategories.add(cat);
                }
            }
            return filteredCategories;
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
