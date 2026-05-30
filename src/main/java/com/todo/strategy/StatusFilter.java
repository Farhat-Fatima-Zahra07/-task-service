package com.todo.strategy;

import com.todo.model.Task;
import org.springframework.stereotype.Component;
import java.util.List;
import java.util.stream.Collectors;

@Component("statusFilter")
public class StatusFilter implements FilterStrategy {

    @Override
    public List<Task> filter(List<Task> tasks, String value) {
        if (value == null || value.isBlank()) return tasks;
        try {
            Task.Status status = Task.Status.valueOf(value.toUpperCase());
            return tasks.stream()
                    .filter(t -> t.getStatus() == status)
                    .collect(Collectors.toList());
        } catch (IllegalArgumentException e) {
            return tasks;
        }
    }
}