package com.deadvikingstudios.norsetown.model.events;

import com.deadvikingstudios.norsetown.model.entities.Entity;
import com.deadvikingstudios.norsetown.model.entities.EntityLiving;
import com.deadvikingstudios.norsetown.model.entities.ai.tasks.Task;
import com.deadvikingstudios.norsetown.model.entities.ai.tasks.TaskManager;
import com.deadvikingstudios.norsetown.model.world.World;

public class TaskEventHandler
{
    //puts a task onto the task queue
    public static Task createTask(Task task)
    {
        TaskManager.add(task);
        return task;
    }

    //retrieves a task from the task queue
    public static Task getTask(Entity entity)
    {
        return TaskManager.getClosestTask(entity.getPosition());
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
        return true;
    }

    //
    public static boolean onTaskCanceled(Task task, EntityLiving entity)
    {
        entity.setTask(null);
        if(task.retry)
        {
            return TaskManager.add(task);
        }
        task.cancel();
        return TaskEventHandler.onTaskComplete(task, entity);
    }
}

