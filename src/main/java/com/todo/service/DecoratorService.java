package com.todo.service;

import com.todo.decorator.TaskDecorator;
import com.todo.decorator.TaskDecorator.PriorityBadgeDecorator;
import com.todo.decorator.TaskDecorator.UrgentNotificationDecorator;
import com.todo.model.Task;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Service qui applique le Pattern Decorator sur les tâches
 * et génère les notifications
 */
@Service
public class DecoratorService {

    /**
     * Applique le décorateur sur une tâche et retourne
     * les informations enrichies
     */
    public Map<String, Object> decorate(Task task) {
        // Chaîne de décorateurs : Priority -> Urgent
        TaskDecorator decorated = new UrgentNotificationDecorator(
                new PriorityBadgeDecorator(task)
        );

        Map<String, Object> result = new HashMap<>();
        result.put("id",           decorated.getId());
        result.put("label",        decorated.getLabel());
        result.put("description",  decorated.getDescription());
        result.put("notification", decorated.getNotification());
        result.put("hasAlert",     decorated.hasAlert());
        result.put("status",       decorated.getStatus());
        result.put("priority",     decorated.getPriority());
        return result;
    }

    /**
     * Retourne uniquement les tâches urgentes (HIGH + non DONE)
     */
    public List<Map<String, Object>> getUrgentTasks(List<Task> tasks) {
        return tasks.stream()
                .map(this::decorate)
                .filter(t -> (Boolean) t.get("hasAlert"))
                .collect(Collectors.toList());
    }
}