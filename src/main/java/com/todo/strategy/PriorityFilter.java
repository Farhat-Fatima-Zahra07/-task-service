package com.todo.strategy;

import com.todo.model.Task;
import org.springframework.stereotype.Component;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Component("priorityFilter")
public class PriorityFilter implements FilterStrategy {

    @Override
    public List<Task> filter(List<Task> tasks, String value) {
        if (value == null || value.isBlank()) {
            return tasks.stream()
                    .sorted(Comparator.comparingInt(t -> -order(t.getPriority())))
                    .collect(Collectors.toList());
        }
        try {
            Task.Priority priority = Task.Priority.valueOf(value.toUpperCase());
            return tasks.stream()
                    .filter(t -> t.getPriority() == priority)
                    .collect(Collectors.toList());
        } catch (IllegalArgumentException e) {
            return tasks;
        }
    }

    private int order(Task.Priority p) {
        return switch (p) {
            case HIGH   -> 3;
            case MEDIUM -> 2;
            case LOW    -> 1;
        };
    }
}