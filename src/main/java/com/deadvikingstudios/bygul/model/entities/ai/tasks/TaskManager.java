package com.deadvikingstudios.bygul.model.entities.ai.tasks;

import com.deadvikingstudios.bygul.model.entities.EntityLiving;
import com.deadvikingstudios.bygul.model.entities.ai.pathfinding.PathNode;
import com.deadvikingstudios.bygul.model.entities.ai.pathfinding.Pathfinder;
import com.deadvikingstudios.bygul.model.world.World;
import com.deadvikingstudios.bygul.utils.Maths;
import com.deadvikingstudios.bygul.utils.vector.Vector3i;
import com.sun.istack.internal.NotNull;
import org.lwjgl.util.vector.Vector3f;

import java.util.ArrayList;
import java.util.List;

public class TaskManager
{
    /**
     * holds a list of all tasks that are planned but impossible to do at the moment
     */
    private List<Task> plannedTasks = new ArrayList<>();
    private List<Task> availableTasks = new ArrayList<>();
    private List<Task> workingTasks = new ArrayList<>();
    private List<Task> completedTasks = new ArrayList<>();

    public void update()
    {
        //remove all tasks that have been reserved since last tick
        availableTasks.removeAll(workingTasks);

        for (Task task : workingTasks)
        {
            if(task.isComplete)
            {
                completedTasks.add(task);
                continue;
            }
            if(task.isCanceled())
            {
                if(task.retry)
                {
                    plannedTasks.add(task);
                }
                else
                {
                    completedTasks.add(task);
                }
            }
        }
        workingTasks.removeAll(plannedTasks);
        //remove all finished tasks
        workingTasks.removeAll(completedTasks);
        //clear all finished tasks
        completedTasks.clear();
        //add all enqueued tasks
        for (Task task : plannedTasks)
        {
            //if the task has no accessibility whatsoever or there arent enough resources to do it, pass on it
            if(isTaskAccessible(task, 2) != null && task.areResourcesAvailable())
            {
                availableTasks.add(task);
            }
        }
        //clear all tasks that would be added
        plannedTasks.removeAll(availableTasks);

    }

    public Task getClosestAvailableTask(Vector3f position)
    {
        return getClosestAvailableTask(new Vector3i(position));
    }

    public Task getClosestAvailableTask(Vector3i position)
    {
        Task closest = null;
        if(!availableTasks.isEmpty())
        {
            double min = Double.MAX_VALUE;
            for(Task task : availableTasks)
            {
                if(isTaskAccessible(task, 2) == null) continue;

                final double diff = Maths.distanceSquared(position, task.position);

                if(diff < min)
                {
                    min = diff;
                    closest = task;
                }
            }
        }
        if(closest != null) workingTasks.add(closest);
        return closest;
    }

    public boolean add(Task task)
    {
        return plannedTasks.add(task);
    }

    /**
     * @param task
     * @param entityHeight The height of the entity being tested
     * @return true if there is a spot for someone to stand on
     */
    public Vector3i isTaskAccessible(@NotNull Task task, int entityHeight)
    {
        if(task == null) return null;
        for (int i = -1; i < 2; i++)
        {
            for (int j = -2; j < 2; j++)
            {
                for (int k = -1; k < 2; k++)
                {
                    //cant stand in same tile as work
                    if(i==0&&j==0&&k==0) continue;

                    Vector3i pos = new Vector3i(i,j,k);
                    if(World.getCurrentWorld().getCurrentBuildStructure().getTile(task.position.x+i, task.position.y+j-1, task.position.z+k).isSolidCuboid())
                    {
                        boolean flag = true;
                        for (int l = 0; l < entityHeight; l++)
                        {
                            if(World.getCurrentWorld().getCurrentBuildStructure().getTile(task.position.x+i, task.position.y+j+l, task.position.z+k).isAir())
                            {
                                flag = false;
                            }
                        }
                        if(flag)
                        {
                            return pos;
                        }
                    }
                }
            }
        }
        return null;
    }

    public List<PathNode> isTaskAccessibleFrom(@NotNull Task task, EntityLiving entityLiving, Vector3i start)
    {
        if(task == null) return null;
        Vector3i taskAccessPosition = this.isTaskAccessible(task, entityLiving.height);
        if(taskAccessPosition == null) return null;

        return Pathfinder.findPathAStar(entityLiving, start, task.position, true);
    }


    public boolean retry(Task task)
    {
        return workingTasks.remove(task) && plannedTasks.add(task);

    }

    public void completeTask(Task task)
    {
        this.completedTasks.add(task);
    }
}
