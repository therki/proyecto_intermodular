package com.openwebinars.todo.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.openwebinars.todo.model.Task;

import java.time.LocalDateTime;
import java.util.List;

public record EditTaskDto(
        String title,
        String description,
        boolean completed,
        @JsonFormat(pattern="yyyy-MM-dd'T'HH:mm") LocalDateTime deadline,
        Task.Priority priority,
        Long categoryId,
        List<Long> tagsId
) {

}
