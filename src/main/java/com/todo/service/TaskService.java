package com.todo.service;

import com.todo.model.Task;
import com.todo.model.User;
import com.todo.repository.TaskRepository;
import com.todo.repository.UserRepository;
import com.todo.strategy.FilterStrategy;
import com.todo.strategy.PriorityFilter;
import com.todo.strategy.StatusFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class TaskService {

    private final TaskRepository taskRepository;
    private final UserRepository userRepository;
    private final StatusFilter statusFilter;
    private final PriorityFilter priorityFilter;

    public Task createTask(Task task, Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Utilisateur introuvable"));
        task.setUser(user);
        return taskRepository.save(task);
    }

    @Transactional(readOnly = true)
    public List<Task> getAllTasksByUser(Long userId) {
        return taskRepository.findByUserId(userId);
    }

    @Transactional(readOnly = true)
    public Task getTaskById(Long id) {
        return taskRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Tâche introuvable"));
    }

    public Task updateTask(Long id, Task updated) {
        Task existing = getTaskById(id);
        existing.setTitle(updated.getTitle());
        existing.setDescription(updated.getDescription());
        existing.setStatus(updated.getStatus());
        existing.setPriority(updated.getPriority());
        return taskRepository.save(existing);
    }

    public void deleteTask(Long id) {
        if (!taskRepository.existsById(id))
            throw new RuntimeException("Tâche introuvable");
        taskRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public List<Task> filterTasks(Long userId, String filterBy, String value) {
        List<Task> tasks = taskRepository.findByUserId(userId);
        FilterStrategy strategy = "priority".equalsIgnoreCase(filterBy)
                ? priorityFilter : statusFilter;
        return strategy.filter(tasks, value);
    }

    @Transactional(readOnly = true)
    public List<Task> searchTasks(Long userId, String keyword) {
        return taskRepository.searchByTitle(userId, keyword);
    }
}