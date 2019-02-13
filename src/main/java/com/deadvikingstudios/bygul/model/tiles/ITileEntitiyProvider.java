package com.deadvikingstudios.bygul.model.tiles;

import com.deadvikingstudios.bygul.model.tileenitites.TileEntity;

/**
 * Created by SiggiVG on 7/14/2017.
 *
 * flags a tile as one that creates a tileEntity
 */
public interface ITileEntitiyProvider
{
    TileEntity createTileEntity();
}
