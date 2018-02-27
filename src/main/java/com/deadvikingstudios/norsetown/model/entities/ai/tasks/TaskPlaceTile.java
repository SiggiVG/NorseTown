package com.deadvikingstudios.norsetown.model.entities.ai.tasks;

import com.deadvikingstudios.norsetown.model.events.TileEventHandler;
import com.deadvikingstudios.norsetown.model.tiles.Tile;
import com.deadvikingstudios.norsetown.model.world.World;
import com.deadvikingstudios.norsetown.model.world.structures.Structure;
import com.deadvikingstudios.norsetown.utils.Logger;
import com.deadvikingstudios.norsetown.utils.vector.Vector3i;
import com.sun.istack.internal.NotNull;

public class TaskPlaceTile extends Task
{
    private Structure structure;
    private Tile tile;
    private int metadata;
    public TaskPlaceTile(@NotNull Structure structure, Tile tile, @NotNull Vector3i position, int metadata)
    {
        super(position);
        this.structure = structure;
        this.tile = (tile != null ? tile : Tile.Tiles.tileAir);
        this.metadata = metadata;
        this.retry = true;
    }

    @Override
    protected boolean execute()
    {
//        Logger.debug("fizz");
        return World.getCurrentWorld().setTileAt(this.structure, this.tile, this.position.x, this.position.y, this.position.z, metadata, true) &&
         TileEventHandler.onTilePlaced(this.tile, this.position.x, this.position.y, this.position.z, metadata, true);
    }

    @Override
    public boolean areResourcesAvailable()
    {
        //TODO: make this check stockpiles for resources required to build the tile
        return true;
    }


}
