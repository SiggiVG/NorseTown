package com.deadvikingstudios.norsetown.model.world;

import com.deadvikingstudios.norsetown.controller.GameContainer;
import com.deadvikingstudios.norsetown.model.entities.Entity;
import com.deadvikingstudios.norsetown.model.entities.EntityLiving;
import com.deadvikingstudios.norsetown.model.entities.EntityStructure;
import com.deadvikingstudios.norsetown.model.entities.ai.Task;
import com.deadvikingstudios.norsetown.model.entities.humanoids.EntityHumanoid;
import com.deadvikingstudios.norsetown.model.tiles.Tile;
import com.deadvikingstudios.norsetown.model.world.structures.Structure;
import com.deadvikingstudios.norsetown.model.world.structures.StructureIsland;
import com.deadvikingstudios.norsetown.model.world.structures.StructureTree;
import com.deadvikingstudios.norsetown.utils.vector.Vector3i;
import org.lwjgl.util.vector.Vector3f;

import java.util.*;

public class World
{
    private List<Entity> entities;
    private List<Entity> entitiesToRemove = new ArrayList<Entity>();
    private List<EntityStructure> structures;

    private List<Task> tasks;

    private long seed;

    private static Random genRandom;
    private static Random updateRandom;

    private CalendarNorse calendar;
    private static World currentWorld;
    public Structure currentIsland;

    //TODO: replace with dedicated class for managing job queues (plural)
    public Queue<Task> jobQueue;

    public World(long seed)
    {
        currentWorld = this;

        jobQueue = new PriorityQueue<Task>();

        this.seed = seed;
        this.entities = new ArrayList<Entity>();
        this.structures = new ArrayList<EntityStructure>();
        genRandom = new Random(seed);
        updateRandom = new Random();
        init();
    }



    public static Random getUpdateRandom()
    {
        return updateRandom;
    }

    public static World getCurrentWorld()
    {
        return currentWorld;
    }

    public void init()
    {
        addStructure(this.currentIsland = new StructureIsland(0,0,0));
//        addStructure(new StructureTree(new Vector3i(-40,0,-40), island));

        for (int i = 0; i < 20; i++)
        {
            int x = updateRandom.nextInt(64) -32;
            int z = updateRandom.nextInt(64) -32;
            if(currentIsland.getTile(x,0,z) != Tile.Tiles.tileAir)
            {
                i--;
            }
            else
            {
                this.addEntity(new EntityLiving("cow", x, 0, z));
            }
        }
        addEntity(new EntityHumanoid(0,0,0,0,0,0));
    }

    public void addStructure(Structure structure)
    {
        this.structures.add(new EntityStructure(structure));
    }

    public void addStructure(Structure structure, Vector3i pos)
    {
        this.structures.add(new EntityStructure(structure, pos.x, pos.y, pos.z));
    }

    public void addEntity(Entity entity) {
        this.entities.add(entity);
        GameContainer.addEntity(entity);
    }

    public void removeEntity(Entity entity)
    {
        this.entitiesToRemove.add(entity);
        GameContainer.removeEntity(entity);
    }

    /**
     * Testing only
     */
    public void cutDownTrees()
    {
//        Logger.debug("fizz");
        List<EntityStructure> ents = new ArrayList<>();
        for (EntityStructure structure : structures)
        {
            if(structure.getStructure() instanceof StructureTree)
            {
                ents.add(structure);
                ((StructureTree) structure.getStructure()).destroy(null,true);
            }
        }
        this.structures.removeAll(ents);
    }

    public void update()
    {
        for (EntityStructure struct : structures)
        {
            struct.update();
        }

        entities.removeAll(entitiesToRemove);
        entitiesToRemove.clear();

        for (Entity ent : entities)
        {
            ent.update();
            if(ent instanceof EntityLiving)
            {
                if(!((EntityLiving) ent).isAlive)
                {
                    this.removeEntity(ent);
                }
            }
        }
    }

    public List<EntityStructure> getStructures()
    {
        return structures;
    }

    public List<Entity> getEntities()
    {
        return entities;
    }

    public Tile getTileAt(int x, int y, int z)
    {
        return Tile.Tiles.tileAir;
    }
    //TODO: finish this method and it's calls in other classes
    private List<Structure> getStructuresAt(Vector3f point)
    {
        List<Structure> structs = new ArrayList<Structure>();
        for (EntityStructure struct : this.structures)
        {
            if(struct.getStructure().containsPoint(new Vector3f(point).translate(-struct.getPosX(), -struct.getPosY(), -struct.getPosZ())))
            {
                structs.add(struct.getStructure());
            }
        }
        return structs;
    }

    public void addTask(Task task)
    {
        this.tasks.add(task);
    }
}
