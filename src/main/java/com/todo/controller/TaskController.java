package com.todo.controller;

import com.todo.model.Task;
import com.todo.service.TaskService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;
import com.todo.service.DecoratorService;

@RestController
@RequestMapping("/api/tasks")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class TaskController {

    private final TaskService taskService;
    private final DecoratorService decoratorService;
    @GetMapping
    public ResponseEntity<List<Task>> getAll(@RequestParam Long userId) {
        return ResponseEntity.ok(taskService.getAllTasksByUser(userId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Task> getOne(@PathVariable Long id) {
        return ResponseEntity.ok(taskService.getTaskById(id));
    }

    @PostMapping
    public ResponseEntity<Task> create(@Valid @RequestBody Task task,
                                       @RequestParam Long userId) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(taskService.createTask(task, userId));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Task> update(@PathVariable Long id,
                                       @Valid @RequestBody Task task) {
        return ResponseEntity.ok(taskService.updateTask(id, task));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, String>> delete(@PathVariable Long id) {
        taskService.deleteTask(id);
        return ResponseEntity.ok(Map.of("message", "Tâche supprimée"));
    }

    @GetMapping("/filter")
    public ResponseEntity<List<Task>> filter(@RequestParam Long userId,
                                             @RequestParam(defaultValue = "status") String by,
                                             @RequestParam(required = false) String value) {
        return ResponseEntity.ok(taskService.filterTasks(userId, by, value));
    }

    @GetMapping("/search")
    public ResponseEntity<List<Task>> search(@RequestParam Long userId,
                                             @RequestParam String keyword) {
        return ResponseEntity.ok(taskService.searchTasks(userId, keyword));
    }
    @GetMapping("/urgent")
    public ResponseEntity<List<Map<String, Object>>> getUrgentTasks(
            @RequestParam Long userId) {
        List<Task> tasks = taskService.getAllTasksByUser(userId);
        return ResponseEntity.ok(decoratorService.getUrgentTasks(tasks));
    }

    @GetMapping("/{id}/decorated")
    public ResponseEntity<Map<String, Object>> getDecorated(
            @PathVariable Long id) {
        Task task = taskService.getTaskById(id);
        return ResponseEntity.ok(decoratorService.decorate(task));
    }
}