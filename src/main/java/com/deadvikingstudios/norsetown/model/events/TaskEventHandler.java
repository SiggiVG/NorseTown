package com.deadvikingstudios.norsetown.model.events;

import com.deadvikingstudios.norsetown.model.entities.Entity;
import com.deadvikingstudios.norsetown.model.entities.EntityLiving;
import com.deadvikingstudios.norsetown.model.entities.Faction;
import com.deadvikingstudios.norsetown.model.entities.ai.tasks.Task;
import com.deadvikingstudios.norsetown.model.entities.ai.tasks.TaskManager;
import com.sun.istack.internal.NotNull;

public class TaskEventHandler
{
    //puts a task onto the task queue
    public static Task createTask(@NotNull Faction faction, @NotNull Task task)
    {
        if(faction == null || task == null) return null;
        if(faction.getTaskManager().add(task)) return task;
        return null;
    }

    //retrieves a task from the task queue
    public static Task getTask(EntityLiving entity)
    {
        Faction faction = entity.getFaction();
        if(faction != null)
        {
            TaskManager taskManager = faction.getTaskManager();
            if(taskManager != null)
            {
                return taskManager.getClosestAvailableTask(entity.getPosition());
            }
        }
        return null;
    }

    //executes the task
    public static boolean onTaskExecute(Task task, EntityLiving entity)
    {
        return task.complete() && onTaskComplete(task, entity);
    }

    /**
     *
     * @return true if the task is successful
     */
    public static boolean onTaskComplete(Task task, EntityLiving entity)
    {
        entity.removeTask();
        entity.getFaction().getTaskManager().completeTask(task);
        return true;
    }

    //
    public static boolean onTaskCanceled(@NotNull Task task, EntityLiving entity)
    {
        if(task == null) return false;
        entity.removeTask();
        if(task.retry)
        {
            return entity.getFaction().getTaskManager().retry(task);
        }
        task.cancel();
        return TaskEventHandler.onTaskComplete(task, entity);
    }
}

