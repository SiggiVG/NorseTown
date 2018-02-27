package com.deadvikingstudios.norsetown.model.entities.ai.tasks;

import com.deadvikingstudios.norsetown.utils.Logger;
import com.deadvikingstudios.norsetown.utils.vector.Vector3i;
import org.lwjgl.util.vector.Vector3f;

public abstract class Task implements Comparable<Task>
{
    protected Vector3i position;
    private boolean canceled = false;
    private int priority = 0;
    public boolean retry = false;
    public boolean isComplete;

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
    protected abstract boolean execute();

    public boolean complete()
    {
        if(!canceled)
        {
            if(this.isComplete = this.execute())
            {
//                Logger.debug(this + " completed");
                return true;
            }
            else
            {
                this.canceled = true;
//                Logger.debug(this + " failed");
                return false;
            }
        }
        Logger.debug(this + " was canceled");
        return false;
    }

    public void cancel()
    {
        this.canceled = true;
    }

    public int compareTo(Task task)
    {
        return Integer.compare(this.priority, task.priority);
    }

    public boolean isCanceled()
    {
        return canceled;
    }

    public abstract boolean areResourcesAvailable();

    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (!(o instanceof Task)) return false;

        Task task = (Task) o;

        if (retry != task.retry) return false;
        return getPosition().equals(task.getPosition());
    }

    @Override
    public int hashCode()
    {
        int result = getPosition().hashCode();
        result = 31 * result + (retry ? 1 : 0);
        return result;
    }
}
