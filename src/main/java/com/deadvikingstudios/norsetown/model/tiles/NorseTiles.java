package com.deadvikingstudios.norsetown.model.tiles;

import com.deadvikingstudios.bygul.model.tiles.*;
import com.deadvikingstudios.bygul.model.world.structures.Structure;
import com.deadvikingstudios.bygul.view.meshes.TileMesh;

public class NorseTiles
{
    public static Tile tileGrass;
    public static Tile tileSoil;
    public static Tile tileTreeBase;
    public static Tile tileTrunkFir;
    public static Tile tilePlank;
    public static Tile tileStoneCliff;
    public static Tile tileStoneCobble;
    public static Tile tileClay;
    public static Tile tileWattleDaub;
    public static Tile tileLeaves;
    public static Tile tileGrassTall;

    public static void init()
    {
        Tile.Tiles.register(tileGrass = new TileSod("sod", Tile.EnumMaterial.EARTH));
        Tile.Tiles.register(tileSoil = new TileSoil("soil", Tile.EnumMaterial.EARTH));
        Tile.Tiles.register(tileTreeBase = new TileLog("tree_base"));
        Tile.Tiles.register(tileTrunkFir = new TileLog("log"));
        Tile.Tiles.register(tilePlank = new TileWood("plank", Tile.EnumMaterial.WOOD));
        Tile.Tiles.register(tileStoneCliff = new TileStone("stone_cliff", Tile.EnumMaterial.STONE));
        Tile.Tiles.register(tileStoneCobble = new TileStone("stone_cobble", Tile.EnumMaterial.STONE));
        Tile.Tiles.register(tileClay = new TileSoil("clay", Tile.EnumMaterial.EARTH));
        Tile.Tiles.register(tileWattleDaub = new TileWood("wattledaub", Tile.EnumMaterial.EARTH));
        Tile.Tiles.register(tileLeaves = new TileLeaves("leaves", Tile.EnumMaterial.PLANT).setOpaque(false));
        Tile.Tiles.register(tileGrassTall = new Tile( "grass_tall", Tile.EnumMaterial.PLANT)
        {
            @Override
            public TileMesh getTileMesh(int metadata, int x, int y, int z)
            {
                return null;
            }

            @Override
            public void update(Structure structure, int x, int y, int z)
            {

            }

            @Override
            public boolean isReplacable()
            {
                return true;
            }
        }.setOpaque(false));
    }
}
