package com.todo.decorator;

import com.todo.model.Task;

/**
 * Pattern Decorator - Notifications et badges sur les tâches
 * Ajoute dynamiquement des comportements sans modifier Task
 */
public abstract class TaskDecorator {

    protected Task task;

    public TaskDecorator(Task task) {
        this.task = task;
    }

    public abstract String getLabel();
    public abstract String getDescription();
    public abstract String getNotification();
    public abstract boolean hasAlert();

    public Long getId()                { return task.getId(); }
    public Task.Status getStatus()     { return task.getStatus(); }
    public Task.Priority getPriority() { return task.getPriority(); }
    public Task getTask()              { return task; }

    // =============================================
    // Décorateur 1 : Badge de priorité
    // =============================================
    public static class PriorityBadgeDecorator extends TaskDecorator {

        public PriorityBadgeDecorator(Task task) { super(task); }

        @Override
        public String getLabel() {
            return switch (task.getPriority()) {
                case HIGH   -> "🔴 [HAUTE] " + task.getTitle();
                case MEDIUM -> "🟡 [MOYENNE] " + task.getTitle();
                case LOW    -> "🟢 [BASSE] " + task.getTitle();
            };
        }

        @Override
        public String getDescription() {
            return task.getDescription() != null ? task.getDescription() : "";
        }

        @Override
        public String getNotification() {
            return switch (task.getPriority()) {
                case HIGH   -> "⚠️ Tâche urgente ! Priorité haute.";
                case MEDIUM -> "📌 Priorité moyenne.";
                case LOW    -> "✅ Priorité basse, pas urgent.";
            };
        }

        @Override
        public boolean hasAlert() {
            return task.getPriority() == Task.Priority.HIGH;
        }
    }

    // =============================================
    // Décorateur 2 : Badge de statut
    // =============================================
    public static class StatusBadgeDecorator extends TaskDecorator {

        public StatusBadgeDecorator(Task task) { super(task); }

        @Override
        public String getLabel() {
            return switch (task.getStatus()) {
                case TODO        -> "📋 [À FAIRE] " + task.getTitle();
                case IN_PROGRESS -> "⏳ [EN COURS] " + task.getTitle();
                case DONE        -> "✅ [TERMINÉ] " + task.getTitle();
            };
        }

        @Override
        public String getDescription() {
            return task.getDescription() != null ? task.getDescription() : "";
        }

        @Override
        public String getNotification() {
            return switch (task.getStatus()) {
                case TODO        -> "📋 Cette tâche n'a pas encore commencé.";
                case IN_PROGRESS -> "⏳ Tâche en cours de traitement.";
                case DONE        -> "✅ Tâche terminée avec succès !";
            };
        }

        @Override
        public boolean hasAlert() {
            return task.getStatus() == Task.Status.IN_PROGRESS
                    && task.getPriority() == Task.Priority.HIGH;
        }
    }

    // =============================================
    // Décorateur 3 : Notification urgente (combiné)
    // =============================================
    public static class UrgentNotificationDecorator extends TaskDecorator {

        private final TaskDecorator wrapped;

        public UrgentNotificationDecorator(TaskDecorator wrapped) {
            super(wrapped.getTask());
            this.wrapped = wrapped;
        }

        @Override
        public String getLabel() {
            if (hasAlert()) {
                return "🚨 URGENT — " + wrapped.getLabel();
            }
            return wrapped.getLabel();
        }

        @Override
        public String getDescription() {
            return wrapped.getDescription();
        }

        @Override
        public String getNotification() {
            if (task.getPriority() == Task.Priority.HIGH
                    && task.getStatus() != Task.Status.DONE) {
                return "🚨 URGENT : Cette tâche est haute priorité et non terminée !";
            }
            return wrapped.getNotification();
        }

        @Override
        public boolean hasAlert() {
            return task.getPriority() == Task.Priority.HIGH
                    && task.getStatus() != Task.Status.DONE;
        }
    }
}