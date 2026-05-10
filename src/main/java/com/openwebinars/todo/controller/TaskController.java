package com.openwebinars.todo.controller;

import com.openwebinars.todo.dto.EditTaskDto;
import com.openwebinars.todo.dto.GetTaskDto;
import com.openwebinars.todo.service.TaskService;
import com.openwebinars.todo.users.User;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/task/")
@RequiredArgsConstructor
@SecurityRequirement(name="basicAuth")
public class TaskController {

    private final TaskService taskService;

//    @GetMapping
//    public List<GetTaskDto> getAll(){return taskService.findAll()
//            .stream()
//            .map(GetTaskDto::of)
//            .toList();}
    @Operation(
        summary = "Obtner todas las tareas del usuario",
        description = "Permite obtener todas las tares del usuario")
    @ApiResponse(description = "Lista de tareas del usuario",
        responseCode = "200",
        content = @Content(
                mediaType = "application/json",
                array=@ArraySchema(schema = @Schema(implementation = GetTaskDto.class))
        ))
    @GetMapping
    public List<GetTaskDto> getAll(@AuthenticationPrincipal User author){
        return taskService.findByAuthor(author)
            .stream()
            .map(GetTaskDto::of)
            .toList();}

    @Operation(
            summary = "Obtner todas las tareas por ID",
            description = "Permite buscar todas las tareas por ID")
    @GetMapping
    public GetTaskDto getById(@PathVariable Long id){

        return GetTaskDto.of(taskService.findById(id));
    }

    @Operation(
            summary = "Crear una nueva tarea",
            description = "Crear una nueva tarea")
    @PostAuthorize("""
            returnObject.author.username == authentication.principal.username
            """)
    @PostMapping
    public ResponseEntity<GetTaskDto> create(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "tarea a crear", required = true,
                    content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation=EditTaskDto.class))
            )
            @RequestBody EditTaskDto cmd,
            @AuthenticationPrincipal User author){
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(GetTaskDto.of(taskService.save(cmd, author)));
    }

    @Operation(
            summary = "Editar una tarea",
            description = "Editar una tarea existente")
    @PreAuthorize("""
            @OwnerCheck.check(#id, authentication.principal.getId())
            """)
    @PutMapping("/{id}")
    public GetTaskDto edit(
            @RequestBody EditTaskDto cmd,
            @PathVariable Long id){
        return GetTaskDto.of(taskService.edit(cmd, id));
    }
    @Operation(
            summary = "Elimnar una tarea",
            description = "Eliminar una tarea a partir de su ID")
    @PreAuthorize("""
            @OwnerCheck.check(#id, authentication.principal.getId())
            """)
    @DeleteMapping("/{id}")
    public ResponseEntity<?>  delete( @PathVariable Long id){
        taskService.delete(id);
        return  ResponseEntity.noContent().build();
    }


}
