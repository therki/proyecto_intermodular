package com.pidaw.todo.dto;

import com.pidaw.todo.model.User;

public record NewUserCommand(String username, String fullname, String email, String password, User.RoleType role) {
}