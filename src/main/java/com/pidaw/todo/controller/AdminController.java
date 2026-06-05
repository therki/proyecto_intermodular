package com.pidaw.todo.controller;

import com.pidaw.todo.model.Category;
import com.pidaw.todo.service.CategoryService;
import com.pidaw.todo.dto.NewUserCommand;
import com.pidaw.todo.dto.NewUserResponse;
import com.pidaw.todo.model.User;
import com.pidaw.todo.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
@PreAuthorize("hasRole('ADMIN')")
@SecurityRequirement(name="basicAuth")
@Tag(name = "Admin", description = "Endpoints de administración")
@AllArgsConstructor
public class AdminController {
    private final UserService userService;

    private final CategoryService categoryService;

    /* Cambiar Usuario - Gestor */
    @Operation(summary = "Promocionar usuario a gestor", description = "Cambia el rol de un usuario a GESTOR")
    @PutMapping("/user/{id}/promote")
    public ResponseEntity<NewUserResponse> promoteToGestor(
            @Parameter(description = "ID del usuario a promocionar", required = true)
            @PathVariable Long id) {
        User user = userService.changeRole(id, User.RoleType.GESTOR);
        return ResponseEntity.ok(NewUserResponse.of(user));
    }
    /* Cambiar Gestor - Usuario */
    @Operation(summary = "Degradar usuario gestor a usuario", description = "Cambia el rol de un usuario a USUARIO")
    @PutMapping("/user/{id}/degradate")
    @RequestBody(description = "Datos del usuario")
    public ResponseEntity<NewUserResponse> degradateToUser(
            @Parameter(description = "ID del usuario a degradar", required = true)
            @PathVariable Long id) {
        User user = userService.changeRole(id, User.RoleType.USUARIO);
        return ResponseEntity.ok(NewUserResponse.of(user));       }
    /****  CRUD USUARIO ****/
    /* Listar usuarios */
    @Operation(summary = "Listar usuarios", description = "Listar todos los usuarios de la aplicación")
    @GetMapping("/users")
    public List<NewUserResponse> getAllUsers() {
        return userService.listUsers()
                .stream()
                .map(NewUserResponse::of) // Convertimos cada User en NewUserResponse
                .toList();
    }
    /* Obtener usuario */
    @Operation(summary = "Obtener usuario por ID", description = "Buscar usuario por su identificador")
    @GetMapping("/user/{id}")
    public NewUserResponse getUser(
            @Parameter(description = "ID del usuario", required = true)
            @PathVariable Long id) {
        return NewUserResponse.of(userService.getUser(id));
    }

    @Operation(summary = "Obtener usuario por Email", description = "Buscar usuario por su correo electrónico")
    @GetMapping("/user/email/{email}")
    public NewUserResponse getUser(
            @Parameter(description = "Email del usuario", required = true)
            @PathVariable String email) {
        return NewUserResponse.of(userService.getUser(email));
    }

    /* Crear usuario */
    @PostMapping("/user")
    public ResponseEntity<NewUserResponse> createUser(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Datos del nuevo usuario",
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                schema = @Schema(implementation = NewUserCommand.class),
                            examples = @ExampleObject(
                    value = "{ \"username\": \"nuevoUser\", \"password\": \"1234\", \"email\": \"correo@example.com\", \"fullname\": \"Nombre Completo\", \"role\": \"USUARIO\" }"
                )
                            )
                    )
        @org.springframework.web.bind.annotation.RequestBody NewUserCommand cmd) {  // ✅ Añade esta anotación
        User user = userService.register(cmd);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(NewUserResponse.of(user));
    }


    /* Editar usuario */
    @Operation(summary = "Actualizar usuario", description = "Actualizar datos del usuario")
    @PutMapping("/user/{id}")
    public ResponseEntity<NewUserResponse> editUser(
            @Parameter(description = "ID del usuario", required = true)
            @PathVariable Long id,
            @Parameter(description = "Datos actualizados del usuario", required = true)
            @org.springframework.web.bind.annotation.RequestBody NewUserCommand cmd) {

        User user = userService.updateUser(id, cmd);
        return ResponseEntity.ok(NewUserResponse.of(user));
    }

    /* Eliminar usuario */
    @Operation(summary = "Eliminar usuario", description = "Eliminar información de un usuario")
    @DeleteMapping("/user/{id}")
    public ResponseEntity<?> deleteUser(
            @Parameter(description = "IS del usuario a eliminar", required = true)
            @PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }

    /**** CRUD CATEGORIA *****/

    /* Listar categorias */
    @Operation(summary = "Listar categorias", description = "Listar todas las categorias de la aplicación")
    @GetMapping("/categories")
    public List<Category> getAllCategories(@AuthenticationPrincipal User user) {
        return categoryService.listCategories(user);
    }

    /* Obtener categoria */
    @Operation(summary = "Obtener categoria", description = "Obtener información de una categoría")
    @GetMapping("/category/{id}")
    public Category getCategory(
            @Parameter(description = "ID de la categoria", required = true)
            @PathVariable Long id,
            @AuthenticationPrincipal User user) {
        return categoryService.findByID(id, user);
    }

    /* Crear categoría */
    @Operation(summary = "Crear categoria", description = "OCrear una nueva categoría")
    @PostMapping("/category")
    public ResponseEntity<Category> createCategory(
            @Parameter(description = "Datos de la categoría", required = true)
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Datos de la nueva categoría",
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    value = "{ \"title\": \"Nueva Categoría\" }"
                            )
                    )
            ) @org.springframework.web.bind.annotation.RequestBody Category category,
            @AuthenticationPrincipal User user) {
        return ResponseEntity.status(HttpStatus.CREATED).body(categoryService.save(category,user));
    }

    /* Editar categoria */
    @Operation(summary = "Editar categoria", description = "Editar una categoría existente")
    @PutMapping("/category/{id}")
    public ResponseEntity<Category> editCategory(
            @Parameter(description = "ID de la categoría a editar", required = true)
            @PathVariable Long id,
            @Parameter(description = "Datos modificados de la categoria", required = true)
            @org.springframework.web.bind.annotation.RequestBody Category  category,
            @AuthenticationPrincipal User user) {
        return ResponseEntity.ok(categoryService.edit(id, category, user));
    }
    /* Borrar categoria */
    @Operation(summary = "Eliminar categoria", description = "Borrar una categoría por su ID")
    @DeleteMapping("/category/{id}")
    public ResponseEntity<?> deleteCategory(
            @Parameter(description = "ID de la categoría a eliminar", required = true)
            @PathVariable Long id,
            @AuthenticationPrincipal User user) {
        categoryService.deleteById(id,user);
        return ResponseEntity.noContent().build();
    }

}
