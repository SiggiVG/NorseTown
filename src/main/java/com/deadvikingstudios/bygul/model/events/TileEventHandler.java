package com.deadvikingstudios.bygul.model.events;

import com.deadvikingstudios.bygul.model.tiles.Tile;

public class TileEventHandler
{
    public static boolean onTilePlaced(Tile tile, int x, int y, int z, int metadata, boolean byPlayer)
    {
        return tile.onTilePlaced(x,y,z,metadata,byPlayer);
        //play sound effects
        //update mesh?
    }

    public static boolean onTileDestroyed(Tile tile, int x, int y, int z, int metadata, boolean byPlayer)
    {
        return tile.onTileDestroyed(x,y,z,metadata,byPlayer);
    }
}
