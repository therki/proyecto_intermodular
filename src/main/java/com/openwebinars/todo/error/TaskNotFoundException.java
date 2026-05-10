package com.openwebinars.todo.error;

public class TaskNotFoundException extends  RuntimeException{

    public TaskNotFoundException(String message){
        super(message);
    }

    public TaskNotFoundException(Long id){
        super("No hay ninguna tarea con el ID %d".formatted(id));
    }

    public TaskNotFoundException(){
        super("No hay tareas con esos requisitos de busqeda");
    }
}
