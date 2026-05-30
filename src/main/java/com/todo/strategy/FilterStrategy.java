package com.todo.strategy;

import com.todo.model.Task;
import java.util.List;

public interface FilterStrategy {
    List<Task> filter(List<Task> tasks, String value);
}