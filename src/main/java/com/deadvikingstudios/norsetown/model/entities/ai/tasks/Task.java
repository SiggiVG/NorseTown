package com.deadvikingstudios.norsetown.model.entities.ai.tasks;

import com.deadvikingstudios.norsetown.utils.Logger;
import com.deadvikingstudios.norsetown.utils.vector.Vector3i;
import org.lwjgl.util.vector.Vector3f;

public abstract class Task implements Comparable<Task>
{
    protected Vector3i position;
    private boolean jobCanceled = false;
    private int priority = 0;
    public boolean retry = false;

    public Task (Vector3f position)
    {
        this.position = new Vector3i(position.x, position.y, position.z);
    }
    public Task (Vector3i position) { this.position = new Vector3i(position);}

    public Vector3i getPosition()
    {
        return position;
    }

    public void setPosition(Vector3i position)
    {
        this.position = position;
    }

    /**
     * performs the task's operation
     */
    protected abstract void execute();

    public boolean complete()
    {
        if(!jobCanceled)
        {
            this.execute();
            Logger.debug(this + " completed");
            return true;
        }
        Logger.debug(this + " failed");
        return false;
    }

    public void cancel()
    {
        this.jobCanceled = true;
    }

    public int compareTo(Task task)
    {
        return Integer.compare(this.priority, task.priority);
    }
}
