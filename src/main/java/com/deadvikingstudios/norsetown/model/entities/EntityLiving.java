package com.deadvikingstudios.norsetown.model.entities;

import com.deadvikingstudios.norsetown.model.entities.ai.tasks.Task;
import com.deadvikingstudios.norsetown.model.entities.ai.pathfinding.Pathfinder;
import com.deadvikingstudios.norsetown.model.entities.ai.pathfinding.PathNode;
import com.deadvikingstudios.norsetown.model.entities.ai.tasks.TaskManager;
import com.deadvikingstudios.norsetown.model.events.TaskEventHandler;
import com.deadvikingstudios.norsetown.model.physics.AxisAlignedBoundingBox;
import com.deadvikingstudios.norsetown.model.tiles.Tile;
import com.deadvikingstudios.norsetown.model.world.World;
import com.deadvikingstudios.norsetown.utils.Logger;
import com.deadvikingstudios.norsetown.utils.Maths;
import com.deadvikingstudios.norsetown.utils.vector.Vector3i;
import org.lwjgl.util.vector.Vector3f;

import java.util.ArrayList;
import java.util.List;

public class EntityLiving extends Entity
{
    private String name = "";

    public boolean isAlive = true;
    private int maxHealth = 100;
    private int currentHealth = maxHealth;
    public int height = 2;

    private Faction faction;

    private Task currentTask;

    protected AxisAlignedBoundingBox hitBox;

    public EntityLiving(Faction faction, String name, float x, float y, float z)
    {
        super(x,y,z);
        this.faction = faction;
        hitBox = new AxisAlignedBoundingBox(0,0,0,1,this.height,1);
        this.name = name;
    }

    public EntityLiving(String faction, String name, float x, float y, float z)
    {
        this(Faction.get(faction), name, x,y,z);
    }

    /**
     * The position that it is lerping from
     */
    private Vector3i travelFrom;
    /**
     * the destination that it is lerping to
     */
    protected Vector3i destination;
    /**
     * it's progress in it's current lerp
     */
    private float moved = 0;
    /**
     * The path it is traveling
     */
    protected List<PathNode> path;

    /**
     * Adjacent coordinate, only x xor y xor z is different
     */
    private static final float VELOCITY_ADJ = 0.1f;
    /**
     * First Order Diagonal
     */
    private static final float VELOCITY_DIAG_1 = VELOCITY_ADJ / (float) Math.sqrt(2);
    /**
     * Second Order Diagonal
     */
    private static final float VELOCITY_DIAG_2 = VELOCITY_ADJ / (float) Math.sqrt(3);

    @Override
    public void update()
    {
        if(currentHealth <= 0)
        {
            this.isAlive = false;
            Logger.debug(this.name + " has perished");
        }

        if(this.isAlive)
        {
            if(this.currentTask == null)
            {
                acquireTask();
            }
            else if(Maths.distanceSquared(this.position, this.currentTask.getPosition()) <= 3)
            {
                performTask();

                this.path = null;
                this.destination = null;
                this.pathThread = null;
            }
//            else
            {
//                checkInsideWall();
                doMovement();
            }

        }
        else
        {
            World.getCurrentWorld().removeEntity(this);
        }
    }

    private void checkInsideWall()
    {

    }

    private void performTask()
    {
        TaskEventHandler.onTaskExecute(this.currentTask, this);
    }

    private void acquireTask()
    {
        this.setTask(TaskEventHandler.getTask(this));
    }

    @Override
    public List<AxisAlignedBoundingBox> getAxisAlignedBoundingBox()
    {
        List<AxisAlignedBoundingBox> list = new ArrayList<AxisAlignedBoundingBox>();
        list.add(hitBox.offset(this.position));
        return list;
    }

    int counter = 0;

    private Thread pathThread;
    //TODO: Neaten this up
    protected void doMovement()
    {
        if(pathThread == null && path == null)
        {
            this.travelFrom = new Vector3i((int) Math.round(this.position.x), (int) Math.round(this.position.y), (int) Math.round(this.position.z));
            this.acquireTarget();
            if (this.destination != null)
            {
                if (World.getCurrentWorld().getTileAt(destination.x, destination.y - 1, destination.z) == Tile.Tiles.tileAir)
                    return;
                for (int i = 0; i <= this.height; i++)
                {
                    if (World.getCurrentWorld().getTileAt(destination.x, destination.y + i, destination.z) != Tile.Tiles.tileAir)
                        return;
                }

                //launch a new Pathfinder Thread
                this.recalcPath();
            }
        }
        else if(pathThread != null && pathThread.isAlive())
        {
            //wait for the thread to resolve
            return;
        }
        else if(path == null && (!pathThread.isAlive()))
        {
            //pathThread returned null, there is no path
            this.pathThread = null;
            TaskEventHandler.onTaskCanceled(this.currentTask, this);
        }
        else if(path != null)
        {
            //there is a path found, so reset the pathThread
            this.pathThread = null;

            //has not finished the path
            if (path.size() > 0)
            {
                counter++;
                //gets the current node on the path as a vector
                Vector3i vec = path.get(path.size() - 1).position;
//                if(World.getCurrentWorld().currentIsland.getTile(vec.x, vec.y, vec.z) != Tile.Tiles.tileAir)
                if(counter % 10 == 0)
                {
                    this.recalcPath();

                    counter = 0;
                }
                //Changes rotation based on movement direction
                this.rotateToFace(new Vector3f(vec.x, vec.y, vec.z));

                //Changes movespeed to keep the lerp constant
                float velocity;
                if(this.rotation.y % 90 != 0)
                {
                    velocity = VELOCITY_DIAG_1;
                    if(this.travelFrom.y != vec.y)
                    {
                        velocity = VELOCITY_DIAG_2;
                    }
                }
                else
                {
                    velocity = VELOCITY_ADJ;
                    if(this.travelFrom.y != vec.y)
                    {
                        velocity = VELOCITY_DIAG_1;
                    }
                }
                //actually moves
                this.position = Maths.lerp(travelFrom, vec, moved);
                //updates movement progress
                moved += velocity;

                //sets up for next move if it's completed this move
                if (moved >= 1f)
                {
                    moved = 0;
                    this.travelFrom = vec;
                    path.remove(path.size() - 1);
                }
            }
            //if has a destination and path has completed
            if(this.destination != null)
            {
                //if is at the destination
                if (this.destination.equals(this.travelFrom))
                {
//                    World.getCurrentWorld().setTileAt(null, Tile.Tiles.tileLeaves, this.destination.x, this.destination.y, this.destination.z, 0, true);
                    //reset the paththread just in case
                    if(pathThread != null)
                    {
                        this.pathThread.interrupt();
                        this.pathThread = null;
                    }
                    //reset the path and the destination. now the entity will become aimless
                    this.path = null;
                    this.destination = null;
                }
            }
            //path is finished, redundancy
            else
            {
                if(pathThread != null)
                {
                    this.pathThread.interrupt();
                    this.pathThread = null;
                }
                path = null;
            }
        }
    }

    protected void recalcPath()
    {
        //if it has a place to go from and a final destination in mind
        if(this.travelFrom != null && this.destination != null)
        {
            //if it is following a task
            if (this.currentTask != null)
            {
                this.pathThread = new Thread(() -> this.path = this.faction.getTaskManager().isTaskAccessibleFrom(this.currentTask, this, this.travelFrom));
                this.pathThread.start();
            }
            else //is idling
            {
                Vector3i tVec = new Vector3i(this.travelFrom);
                Vector3i dVec = new Vector3i(this.destination);
                this.pathThread = new Thread(() -> this.path = Pathfinder.findPathAStar(this, tVec, dVec, true));
                this.pathThread.start();
            }
        }
    }

    private int wanderRange = 16;

    /**
     * Will wander to any position within their wander range of their wander position
     * for most creatures, the wander position is their own position
     *
     * for herd animals, the wander position is their herd's center
     */
    protected void idle()
    {
        //TODO: check if their trait or profession has an idle task generator
        if(false)
        {

        }
        else //wander
        {
            this.destination = new Vector3i(
                    (int) this.position.x + World.getUpdateRandom().nextInt(wanderRange) - wanderRange / 2,
                    (int) this.position.y + World.getUpdateRandom().nextInt(wanderRange) - wanderRange / 2,
                    (int) this.position.z + World.getUpdateRandom().nextInt(wanderRange) - wanderRange / 2
            );
        }
    }

    private void acquireTarget()
    {
        //if has a task
        if(this.currentTask != null)
        {
            //set destination to closest accessible side of task's target position
            this.destination = currentTask.getPosition();
            return;
        }
        //has nothing to do, so idle
        idle();
    }

    protected void rotateToFace(Vector3f vec)
    {
        float x = this.position.x - vec.x;
        float z = this.position.z - vec.z;

        this.rotation.y = (float) Math.toDegrees(Math.atan2(x, z));
    }

    public void setTask(Task task)
    {
        this.currentTask = task;
    }

    public void removeTask()
    {
        this.currentTask = null;
    }

    public void cancelTask()
    {
        TaskEventHandler.onTaskCanceled(currentTask, this);
    }

    public boolean hasTask()
    {
        return this.currentTask != null;
    }

    public Faction getFaction()
    {
        return faction;
    }
}
