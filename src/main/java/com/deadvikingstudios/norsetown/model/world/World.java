package com.deadvikingstudios.norsetown.model.world;

import com.deadvikingstudios.norsetown.model.entities.Entity;
import com.deadvikingstudios.norsetown.model.entities.EntityStructure;
import com.deadvikingstudios.norsetown.model.world.structures.Structure;
import com.deadvikingstudios.norsetown.model.world.structures.StructureIsland;
import com.deadvikingstudios.norsetown.model.world.structures.StructureTree;
import com.deadvikingstudios.norsetown.utils.Logger;
import com.deadvikingstudios.norsetown.utils.vector.Vector3i;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class World
{
    private List<Entity> entities;
    private List<EntityStructure> structures;

    private long seed;

    private CalendarNorse calendar;

    public World(long seed)
    {
        this.seed = seed;
        this.entities = new ArrayList<Entity>();
        this.structures = new ArrayList<EntityStructure>();

        init();
    }

    public void init()
    {
        addStructure(new StructureIsland(0,0,0));
        Random rand = new Random(seed);
        for (int i = 0; i < 16; i++)
        {
            addStructure(new StructureTree(new Vector3i(rand.nextInt(64)-32, rand.nextInt(64), rand.nextInt(64)-32 )));
        }
    }

    private void addStructure(Structure structure)
    {
        this.structures.add(new EntityStructure(structure));
    }

    private void addStructure(Structure structure, Vector3i pos)
    {
        this.structures.add(new EntityStructure(structure, pos.x, pos.y, pos.z));
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
        for (Entity ent : entities)
        {
            ent.update();
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
}
