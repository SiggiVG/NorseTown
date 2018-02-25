package com.deadvikingstudios.norsetown.model.entities.ai;

import com.deadvikingstudios.norsetown.utils.Logger;
import com.deadvikingstudios.norsetown.utils.vector.Vector3i;
import org.lwjgl.util.vector.Vector3f;

public class Task
{
    private Vector3i position;
    private float jobTime = 1f;
    private boolean canDoJob = true;
    private boolean jobCanceled = false;

    public Task (Vector3f position)
    {
        this.position = new Vector3i(position.x, position.y, position.z);
    }

    public Task(Vector3f position, float jobTime)
    {
        this(position);
        this.jobTime = jobTime;
    }

    public Vector3i getPosition()
    {
        return position;
    }

    public void setPosition(Vector3i position)
    {
        this.position = position;
    }

    public float getJobTime()
    {
        return jobTime;
    }

    public void setJobTime(float jobTime)
    {
        this.jobTime = jobTime;
    }

    public boolean doWork(float workTime)
    {
        jobTime -= workTime;
        if(jobTime <= 0)
        {
            return this.complete();
        }
        return false;
    }

    public boolean complete()
    {
        if(!jobCanceled)
        {
            Logger.debug(this + " completed");
            return true;
        }
        Logger.debug(this + " failed");
        return false;
    }

    public void cancel()
    {
        this.jobCanceled = true;
        this.canDoJob = false;
    }
}
