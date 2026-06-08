package com.pidaw.todo.controller;

import com.pidaw.todo.model.User;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ViewController {
    /* PPUBLICO */
    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/register")
    public String registerPage() {
        return "register";
    }

    @GetMapping("/")
    public String home(@AuthenticationPrincipal User user, Model model) {
        if (user == null) {
            return "redirect:/login";
        }

        model.addAttribute("user", user);

        switch (user.getRole()) {
            case ADMIN:
                return "redirect:/admin/dashboard";
            case GESTOR:
                return "redirect:/gestor/dashboard";
            case USUARIO:
                return "redirect:/user/dashboard";
            default:
                return "redirect:/login";
        }
    }

    /* ADMIN  */

    @GetMapping("/admin/dashboard")
    public String adminDashboard(@AuthenticationPrincipal User user, Model model) {
        model.addAttribute("user", user);
        return "admin/dashboard";
    }
    // Listado de usuarios
    @GetMapping("/admin/users")
    public String adminUsersList(@AuthenticationPrincipal User user, Model model) {
        model.addAttribute("user", user);
        return "admin/users-list";
    }
    // Listado de categorias
    @GetMapping("/admin/categories")
    public String adminCategoriesList(@AuthenticationPrincipal User user, Model model) {
        model.addAttribute("user", user);
        return "admin/categories-list";
    }

    /*  GESTOR  */
    //Dashboard gestor
    @GetMapping("/gestor/dashboard")
    public String gestorDashboard(@AuthenticationPrincipal User user, Model model) {
        model.addAttribute("user", user);
        return "gestor/dashboard";
    }
    // Listado categorias
    @GetMapping("/gestor/categories")
    public String gestorCategoriesList(@AuthenticationPrincipal User user, Model model) {
        model.addAttribute("user", user);
        return "gestor/categories-list";
    }

    /*  USUARIO  */
    // Dashboard
    @GetMapping("/user/dashboard")
    public String userDashboard(@AuthenticationPrincipal User user, Model model) {
        model.addAttribute("user", user);
        return "user/dashboard";
    }

    // Tareas
    @GetMapping("/user/tasks")
    public String userTasks(@AuthenticationPrincipal User user, Model model) {
        model.addAttribute("user", user);
        return "user/tasks";
    }

    // Etiquetas
    @GetMapping("/user/tags")
    public String userTags(@AuthenticationPrincipal User user, Model model) {
        model.addAttribute("user", user);
        return "user/tags";
    }

    /* ERROR*/

    @GetMapping("/access-denied")
    public String accessDenied() {
        return "error/access-denied";
    }
}