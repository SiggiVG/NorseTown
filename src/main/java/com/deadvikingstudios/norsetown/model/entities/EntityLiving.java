package com.deadvikingstudios.norsetown.model.entities;

import com.deadvikingstudios.norsetown.model.entities.ai.pathfinding.Pathfinder;
import com.deadvikingstudios.norsetown.model.entities.ai.pathfinding.Node;
import com.deadvikingstudios.norsetown.model.physics.AxisAlignedBoundingBox;
import com.deadvikingstudios.norsetown.model.tiles.Tile;
import com.deadvikingstudios.norsetown.model.world.World;
import com.deadvikingstudios.norsetown.utils.Logger;
import com.deadvikingstudios.norsetown.utils.Maths;
import com.deadvikingstudios.norsetown.utils.vector.Vector3i;

import java.util.ArrayList;
import java.util.List;

public class EntityLiving extends Entity
{
    private String name = "";

    public boolean isAlive = true;
    private int maxHealth = 100;
    private int currentHealth = maxHealth;
    public int height = 2;

    protected AxisAlignedBoundingBox hitBox;

    public EntityLiving(String name, float x, float y, float z)
    {
        super(x,y,z);
        hitBox = new AxisAlignedBoundingBox(0,0,0,1,this.height,1);
        this.name = name;
    }



    private Vector3i treavelFrom;
    private Vector3i destination;
    private float velocity = VELOCITY_ADJ;
    private float moved = 0;
    private List<Node> path;

    private static final float VELOCITY_ADJ = 0.1f;
    private static final float VELOCITY_DIAG = VELOCITY_ADJ / (float) Math.sqrt(2);

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
            doMovement();
        }
        else
        {
            World.getCurrentWorld().removeEntity(this);
        }
    }

    @Override
    public List<AxisAlignedBoundingBox> getAxisAlignedBoundingBox()
    {
        List<AxisAlignedBoundingBox> list = new ArrayList<AxisAlignedBoundingBox>();
        list.add(hitBox.offset(this.position));
        return list;
    }

    protected void doMovement()
    {
        if (path != null)
        {
            if (path.size() > 0)
            {
                Vector3i vec = path.get(path.size() - 1).position;
                if(World.getCurrentWorld().currentIsland.getTile(vec.x, vec.y, vec.z) != Tile.Tiles.tileAir)
                {
                    this.position.x = this.treavelFrom.x;
                    this.position.y = this.treavelFrom.y;
                    this.position.z = this.treavelFrom.z;
                    path = Pathfinder.findPathAStar(World.getCurrentWorld().currentIsland, this, this.treavelFrom, this.destination, true);
                    return;
                }
                this.position = Maths.lerp(treavelFrom, vec, moved);
                moved += velocity;

                if (moved >= 1f)
                {
                    moved = 0;
                    this.treavelFrom = vec;
                    path.remove(path.size() - 1);
                }
            }
            if (this.destination.equals(this.treavelFrom))
            {
                World.getCurrentWorld().currentIsland.setTile(Tile.Tiles.tileSoil, destination.x, destination.y, destination.z);
                this.path = null;
                this.destination = null;
            }
        } else
        {

            this.destination = new Vector3i(
                    World.getUpdateRandom().nextInt(64) - 32,
                    World.getUpdateRandom().nextInt(32),
                    World.getUpdateRandom().nextInt(64) - 32);
            if (World.getCurrentWorld().currentIsland.getTile(destination.x, destination.y - 1, destination.z) == Tile.Tiles.tileAir)
                return;
            for (int i = 0; i <= this.height; i++)
            {
                if (World.getCurrentWorld().currentIsland.getTile(destination.x, destination.y + i, destination.z) != Tile.Tiles.tileAir)
                    return;
            }
            if (this.destination != null)
            {
                this.treavelFrom = new Vector3i(Math.round(this.position.x), Math.round(this.position.y), Math.round(this.position.z));
                path = Pathfinder.findPathAStar(World.getCurrentWorld().currentIsland, this, this.treavelFrom, this.destination, true);
            }
        }
    }
}
