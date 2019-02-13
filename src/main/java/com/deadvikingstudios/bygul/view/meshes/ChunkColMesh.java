package com.deadvikingstudios.bygul.view.meshes;

import com.deadvikingstudios.bygul.model.tiles.EnumTileFace;
import com.deadvikingstudios.bygul.model.tiles.Tile;
import com.deadvikingstudios.bygul.model.world.structures.Chunk;
import com.deadvikingstudios.bygul.model.world.structures.ChunkColumn;
import com.deadvikingstudios.bygul.model.world.structures.Structure;
import com.deadvikingstudios.bygul.utils.ArrayUtils;
import org.lwjgl.util.vector.Vector3f;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.deadvikingstudios.bygul.controller.GameContainer.loader;

public class ChunkColMesh extends EntityMesh
{
    public ChunkColMesh(ChunkColumn entity, MeshTexture texture)
    {
        super(entity, texture);

        List<Float> vertices = new ArrayList<Float>();
        List<Integer> indices = new ArrayList<Integer>();
        List<Float> uvs = new ArrayList<Float>();
        List<Float> norms = new ArrayList<Float>();

        createMesh(vertices, indices, uvs, norms);

        rawMesh = loader.loadToVAO(ArrayUtils.floatFromFloat(vertices),
                ArrayUtils.intFromInteger(indices), ArrayUtils.floatFromFloat(uvs), ArrayUtils.floatFromFloat(norms));
    }

    public void reloadMesh()
    {
        List<Float> vertices = new ArrayList<Float>();
        List<Integer> indices = new ArrayList<Integer>();
        List<Float> uvs = new ArrayList<Float>();
        List<Float> norms = new ArrayList<Float>();

        createMesh(vertices, indices, uvs, norms);

        rawMesh = loader.reloadToVAO(rawMesh, ArrayUtils.floatFromFloat(vertices),
                ArrayUtils.intFromInteger(indices), ArrayUtils.floatFromFloat(uvs), ArrayUtils.floatFromFloat(norms));
    }

    public Vector3f getPosition()
    {
        return ((ChunkColumn)entity).getPosition();
    }

    public ChunkColumn getChunkColumn()
    {
        return ((ChunkColumn)entity);
    }

    protected boolean[] getCullNeighbors(int x, int y, int z)
    {
        //Is currently always pulling from the first chunk
        Structure structure = this.getChunkColumn().getStructure();
        boolean[] bools = new boolean[6];

        for (int i = 0; i < 6; i++)
        {
            bools[i] = structure.getTile(EnumTileFace.get(i).getOffset(x,y,z)).isSolidCuboid();//.isSideSolid(EnumTileFace.get(i));
        }
        return bools;
    }

    //TODO: NOTE: don't do offsets here, do the column transform elsewhere

    protected void createMesh(List<Float> vertices, List<Integer> indices, List<Float> uvs, List<Float> norms)
    {
        for (Map.Entry<Integer, Chunk> entry : getChunkColumn().getChunks().entrySet())
        {
            Chunk chunk = entry.getValue();
            Vector3f renderChunkPos = chunk.getRenderPosition(this.getChunkColumn().position.x * Chunk.SIZE, 0, this.getChunkColumn().position.y * Chunk.SIZE);
            int x = (int) renderChunkPos.x;
            int y = (int) renderChunkPos.y;
            int z = (int) renderChunkPos.z;

            //Offset by structure's position
            int m = this.getChunkColumn().getStructure().getPosition().x;
            int n = this.getChunkColumn().getStructure().getPosition().y;
            int o = this.getChunkColumn().getStructure().getPosition().z;

            for (byte i = 0; i < Chunk.SIZE; ++i)
            {
                for (byte j = 0; j < Chunk.SIZE; ++j)
                {
                    for (byte k = 0; k < Chunk.SIZE; ++k)
                    {
                        Tile tile = this.getChunkColumn().getTile(i, j + y, k);
                        int metadata = this.getChunkColumn().getMetadata(i, j + y, k);

                        if (tile.isAir()) continue;

                        boolean[] bools = getCullNeighbors(i + x, j + y, k + z);
                        List<TileMesh.Face> faces = TileMesh.drawTile(tile, bools, metadata, i+x+m,j+y+n,k+z+o);
                        if (faces != null)
                        {
                            for (TileMesh.Face face : faces)
                            {
                                face.draw(vertices, indices, uvs, norms, new Vector3f(i + x + m, j + y + n, k + z + o));
                            }
                        }
                    }
                }
            }
        }
    }
}
