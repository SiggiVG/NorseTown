package com.deadvikingstudios.norsetown.model.entities.ai.tasks;

import com.deadvikingstudios.norsetown.model.tiles.Tile;
import com.deadvikingstudios.norsetown.model.world.World;
import com.deadvikingstudios.norsetown.utils.vector.Vector3i;

public class TaskPlaceTile extends Task
{
    private Tile tile;
    public TaskPlaceTile(Tile tile, Vector3i position, int priority)
    {
        super(position);
        this.tile = (tile != null ? tile : Tile.Tiles.tileAir);
        this.retry = true;
    }

    @Override
    protected void execute()
    {
        World.getCurrentWorld().currentIsland.setTile(this.tile, this.position.x, this.position.y, this.position.z);
    }
}
