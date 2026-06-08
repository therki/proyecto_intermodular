package com.pidaw.todo.dto;

public record EditUserCommand(String username,String fullname,String email,String password) {
}
