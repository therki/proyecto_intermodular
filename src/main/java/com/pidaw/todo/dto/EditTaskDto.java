package com.pidaw.todo.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.pidaw.todo.model.Task;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public record EditTaskDto(
        String title,
        String description,
        boolean completed,
        @JsonFormat(pattern="yyyy-MM-dd") LocalDate deadline,
        Task.Priority priority,
        Long categoryId,
        List<Long> tagsId
) {

}
