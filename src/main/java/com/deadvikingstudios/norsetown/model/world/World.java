package com.deadvikingstudios.norsetown.model.world;

import com.deadvikingstudios.norsetown.controller.GameContainer;
import com.deadvikingstudios.norsetown.model.entities.Entity;
import com.deadvikingstudios.norsetown.model.entities.EntityLiving;
import com.deadvikingstudios.norsetown.model.entities.EntityStructure;
import com.deadvikingstudios.norsetown.model.entities.Faction;
import com.deadvikingstudios.norsetown.model.entities.ai.tasks.TaskManager;
import com.deadvikingstudios.norsetown.model.entities.ai.tasks.TaskPlaceTile;
import com.deadvikingstudios.norsetown.model.entities.humanoids.EntityHumanoid;
import com.deadvikingstudios.norsetown.model.events.TaskEventHandler;
import com.deadvikingstudios.norsetown.model.tiles.Tile;
import com.deadvikingstudios.norsetown.model.world.structures.Structure;
import com.deadvikingstudios.norsetown.model.world.structures.StructureIsland;
import com.deadvikingstudios.norsetown.model.world.structures.StructureTree;
import com.deadvikingstudios.norsetown.utils.vector.Vector3i;
import org.lwjgl.util.vector.Vector3f;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class World
{
    private List<Entity> entities;
    private List<Entity> entitiesToRemove = new ArrayList<Entity>();
    private List<EntityStructure> structures;

    private Structure currentBuildStructure = null;

    private long seed;

    private static Random genRandom;
    private static Random updateRandom;

    private CalendarNorse calendar;
    private static World currentWorld;

    public World(long seed)
    {
        currentWorld = this;

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
        addStructure(this.currentBuildStructure = new StructureIsland(0,0,0));
//        addStructure(new StructureTree(new Vector3i(-40,0,-40), island));

//        this.addEntity(new EntityLiving("grunt", 0, 0, 0));
//        for (int i = 0; i < 20; i++)
//        {
//            int x = updateRandom.nextInt(64) -32;
//            int z = updateRandom.nextInt(64) -32;
//            if(currentIsland.getTile(x,0,z) != Tile.Tiles.tileAir)
//            {
//                i--;
//            }
//            else
//            {
//                this.addEntity(new EntityLiving("cow", x, 0, z));
//            }
//        }
        addEntity(new EntityHumanoid(0,0,0,0,0,0));

//        for (int i = -30; i < 30; i++)
//        {
//            TaskEventHandler.createTask(Faction.get("player"), new TaskPlaceTile(currentBuildStructure, Tile.Tiles.tileStoneCliff, new Vector3i(i,0,-15), i));
//        }
        int n = 20;
        for (int j = 0; j < 30; j++)
        {
            for (int i = -n; i < n; i++)
            {
                TaskEventHandler.createTask(Faction.get("player"),new TaskPlaceTile(this.currentBuildStructure, Tile.Tiles.tileStoneCliff, new Vector3i(i,j,-15), 0));
            }
            n--;
        }
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

    public Tile getTileAt(Vector3f vec)
    {
        return getTileAt((int)vec.x, (int)vec.y, (int)vec.z);
    }

    public Tile getTileAt(Vector3i vec)
    {
        return getTileAt(vec.x, vec.y, vec.z);
    }

    //TODO: this is broken
    public Tile getTileAt(int x, int y, int z)
    {
        Structure struct = getStructureAt(x,y,z);
        if(struct != null)
        {
            Vector3i sPos = struct.getPosition();
            return struct.getTile(x-sPos.x, y-sPos.y, z-sPos.z);
        }
        return Tile.Tiles.tileAir;
    }

    public boolean isTileAir(int x, int y, int z)
    {
        return getTileAt(x,y,z) == Tile.Tiles.tileAir;
    }

    public boolean setAirAt(int x, int y, int z)
    {
        return this.setTileAt(null, Tile.Tiles.tileAir, x,y,z, 0, false);
    }

    /**
     * @param struct
     * @param tile
     * @param x
     * @param y
     * @param z
     * @param metadata
     * @param byPlayer
     */
    public boolean setTileAt(Structure struct, Tile tile, int x, int y, int z, int metadata, boolean byPlayer)
    {
        //TODO: If I know the structuure, I should use that structure's coordinates
        boolean knownStructure = true;

        Structure structureAt = getStructureAt(x,y,z);
        //if @param struct is null, work on the currently selected structure
        if(struct == null)
        {
            if(this.currentBuildStructure != null)
            {
                //work on the current structure
                struct = currentBuildStructure;
                byPlayer = true;
                //Actually, I should override playerBuiltStructures to always return true
            }
            else if(structureAt != null)
            {
                //work on the structure at that location
                struct = structureAt;
            } //no structure to work on
            else return false;
            knownStructure = false;
        }

        if(tile == null || tile == Tile.Tiles.tileAir)
        {
            tile = Tile.Tiles.tileAir;
            //air doesnt use metadata and can't be player placed
            metadata = 0;
            byPlayer = false;
        }
        //there exists a tile there
        if(structureAt != null)
        {
            //the tile there belongs to a different structure
            if(structureAt != struct)
            {
                if(tile == Tile.Tiles.tileAir)
                {
                    //Tile is air, I dont care
                    struct = structureAt;
                }
                else return false;
            }
            //the two structures are the same, go ahead

        }

        //if not placed by player
        if(!byPlayer)
        {
            Vector3i sPos = struct.getPosition();
            return struct.setTile(tile, x-sPos.x, y-sPos.y, z-sPos.z, metadata, false);
        }
        else if(this.currentBuildStructure != null)
        {
            Vector3i sPos = this.currentBuildStructure.getPosition();
            //place it in the player's building structure
             return this.currentBuildStructure.setTile(tile,x-sPos.x, y-sPos.y, z-sPos.z, metadata, true);
        }
        //else it does nothing
        return false;
    }


    //TODO: finish this method and it's calls in other classes
    private List<Structure> getRoughStructureCollision(Vector3f point)
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

    public Structure getStructureAt(int x, int y, int z)
    {
        List<Structure> structs = getRoughStructureCollision(new Vector3f(x,y,z));
        //there is no structure there
        if(structs.isEmpty()) return null;

        for (Structure struct : structs)
        {
            Vector3i sPos = struct.getPosition();
            if(struct.getTile(x-sPos.x, y-sPos.y, z-sPos.z) != Tile.Tiles.tileAir)
            {
                return struct;
            }
        }
        return null;
    }

    public Structure getCurrentBuildStructure()
    {
        return currentBuildStructure;
    }
}
