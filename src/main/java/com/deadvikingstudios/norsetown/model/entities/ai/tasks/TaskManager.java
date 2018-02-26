package com.deadvikingstudios.norsetown.model.entities.ai.tasks;

import com.deadvikingstudios.norsetown.utils.Maths;
import com.deadvikingstudios.norsetown.utils.vector.Vector3i;
import org.lwjgl.util.vector.Vector3f;

import java.util.ArrayList;
import java.util.List;

public class TaskManager
{
    private static List<Task> taskList = new ArrayList<>();

    public static Task getClosestTask(Vector3f position)
    {
        return getClosestTask(new Vector3i(position));
    }

    public static Task getClosestTask(Vector3i position)
    {
        Task closest = null;
        if(!taskList.isEmpty())
        {
            double min = Double.MAX_VALUE;
            for(Task task : taskList)
            {
                final double diff = Maths.distanceSquared(position, task.position);

                if(diff < min)
                {
                    min = diff;
                    closest = task;
                }
            }
        }
        return pollTask(closest);
    }

    private static Task pollTask(Task task)
    {
        if(taskList.remove(task))
        {
            return task;
        }
        return null;
    }

    public static boolean add(Task task)
    {
        return taskList.add(task);
    }


}
