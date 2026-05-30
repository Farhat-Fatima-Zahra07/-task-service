package com.todo;

import com.todo.decorator.TaskDecorator;
import com.todo.model.Task;
import com.todo.model.User;
import com.todo.strategy.PriorityFilter;
import com.todo.strategy.StatusFilter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests unitaires
 * Auteurs : Farhat Fatima Zahra & Elfahli Khadija
 * Module : Ingénierie Logicielle Avancée ISI_S6
 */
class TaskServiceTest {

    private List<Task> tasks;
    private StatusFilter statusFilter;
    private PriorityFilter priorityFilter;
    private Task taskHigh;
    private Task taskMedium;
    private Task taskLow;

    @BeforeEach
    void setUp() {
        statusFilter   = new StatusFilter();
        priorityFilter = new PriorityFilter();

        taskHigh = new Task();
        taskHigh.setTitle("Tâche urgente");
        taskHigh.setStatus(Task.Status.TODO);
        taskHigh.setPriority(Task.Priority.HIGH);

        taskMedium = new Task();
        taskMedium.setTitle("Tâche normale");
        taskMedium.setStatus(Task.Status.IN_PROGRESS);
        taskMedium.setPriority(Task.Priority.MEDIUM);

        taskLow = new Task();
        taskLow.setTitle("Tâche basse");
        taskLow.setStatus(Task.Status.DONE);
        taskLow.setPriority(Task.Priority.LOW);

        tasks = Arrays.asList(taskHigh, taskMedium, taskLow);
    }

    // ===== MODÈLE =====

    @Test
    @DisplayName("Task : valeurs par défaut")
    void testTaskDefaults() {
        Task t = new Task();
        assertEquals(Task.Status.TODO, t.getStatus());
        assertEquals(Task.Priority.MEDIUM, t.getPriority());
        assertNotNull(t.getCreatedAt());
    }

    @Test
    @DisplayName("User : liste tâches vide par défaut")
    void testUserDefaults() {
        User u = new User();
        assertNotNull(u.getTasks());
        assertEquals(0, u.getTasks().size());
    }



    @Test
    @DisplayName("StatusFilter : filtrer TODO")
    void testStatusTodo() {
        List<Task> result = statusFilter.filter(tasks, "TODO");
        assertEquals(1, result.size());
        assertEquals(Task.Status.TODO, result.get(0).getStatus());
    }

    @Test
    @DisplayName("StatusFilter : filtrer DONE")
    void testStatusDone() {
        List<Task> result = statusFilter.filter(tasks, "DONE");
        assertEquals(1, result.size());
        assertEquals(Task.Status.DONE, result.get(0).getStatus());
    }

    @Test
    @DisplayName("StatusFilter : null retourne tout")
    void testStatusNull() {
        assertEquals(3, statusFilter.filter(tasks, null).size());
    }

    @Test
    @DisplayName("StatusFilter : valeur invalide retourne tout")
    void testStatusInvalid() {
        assertEquals(3, statusFilter.filter(tasks, "INVALIDE").size());
    }



    @Test
    @DisplayName("PriorityFilter : filtrer HIGH")
    void testPriorityHigh() {
        List<Task> result = priorityFilter.filter(tasks, "HIGH");
        assertEquals(1, result.size());
        assertEquals(Task.Priority.HIGH, result.get(0).getPriority());
    }

    @Test
    @DisplayName("PriorityFilter : filtrer LOW")
    void testPriorityLow() {
        List<Task> result = priorityFilter.filter(tasks, "LOW");
        assertEquals(1, result.size());
        assertEquals(Task.Priority.LOW, result.get(0).getPriority());
    }

    @Test
    @DisplayName("PriorityFilter : tri décroissant sans valeur")
    void testPrioritySort() {
        List<Task> result = priorityFilter.filter(tasks, null);
        assertEquals(Task.Priority.HIGH, result.get(0).getPriority());
        assertEquals(Task.Priority.LOW, result.get(result.size() - 1).getPriority());
    }



    @Test
    @DisplayName("PriorityBadgeDecorator : label HIGH contient HAUTE")
    void testDecoratorLabelHigh() {
        TaskDecorator d = new TaskDecorator.PriorityBadgeDecorator(taskHigh);
        assertTrue(d.getLabel().contains("HAUTE"));
    }

    @Test
    @DisplayName("PriorityBadgeDecorator : hasAlert true pour HIGH")
    void testDecoratorAlert() {
        TaskDecorator d = new TaskDecorator.PriorityBadgeDecorator(taskHigh);
        assertTrue(d.hasAlert());
    }

    @Test
    @DisplayName("PriorityBadgeDecorator : hasAlert false pour LOW")
    void testDecoratorNoAlert() {
        TaskDecorator d = new TaskDecorator.PriorityBadgeDecorator(taskLow);
        assertFalse(d.hasAlert());
    }

    @Test
    @DisplayName("UrgentNotificationDecorator : label contient URGENT")
    void testUrgentLabel() {
        TaskDecorator d = new TaskDecorator.UrgentNotificationDecorator(
                new TaskDecorator.PriorityBadgeDecorator(taskHigh)
        );
        assertTrue(d.getLabel().contains("URGENT"));
    }
}