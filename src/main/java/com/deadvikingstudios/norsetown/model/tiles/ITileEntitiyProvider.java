package com.deadvikingstudios.norsetown.model.tiles;

import com.deadvikingstudios.norsetown.model.tileenitites.TileEntity;

/**
 * Created by SiggiVG on 7/14/2017.
 *
 * flags a tile as one that creates a tileEntity
 */
public interface ITileEntitiyProvider
{
    TileEntity createTileEntity();
}
