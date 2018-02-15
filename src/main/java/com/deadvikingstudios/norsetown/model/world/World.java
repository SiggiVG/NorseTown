package com.deadvikingstudios.norsetown.model.world;

import com.deadvikingstudios.norsetown.model.entities.Entity;
import com.deadvikingstudios.norsetown.model.entities.EntityStructure;

import java.util.List;

public class World
{
    public static List<Entity> entities;
    public static  List<EntityStructure> structures;

    public void init()
    {

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

}
