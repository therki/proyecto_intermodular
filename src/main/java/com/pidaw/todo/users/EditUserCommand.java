package com.pidaw.todo.users;

public record EditUserCommand(String username,String fullname,String email,String password) {
}
