package com.deadvikingstudios.bygul.model.events;

import com.deadvikingstudios.bygul.controller.GameContainer;
import com.deadvikingstudios.bygul.model.world.World;
import com.deadvikingstudios.bygul.model.world.structures.ChunkColumn;
import com.deadvikingstudios.bygul.model.world.structures.Structure;
import com.deadvikingstudios.bygul.utils.vector.Vector2i;

import java.util.Map;

public class StructureEventHandler
{
    public static void onStructureCreated(Structure structure)
    {
        World.getCurrentWorld().addStructure(structure);
    }

    public static boolean onStructureParented(Structure structure, Structure parent)
    {

        return false;
    }

    public static void onStructureDestroyed(Structure structure)
    {
        structure.setParent(null);
        World.getCurrentWorld().removeStructure(structure);
        for (Map.Entry<Vector2i, ChunkColumn> chunkCol : structure.getChunks().entrySet())
        {
            GameContainer.removeStructureMesh(chunkCol.getValue());
        }
    }


    public static void onChunkColumnCreated(Structure structure, ChunkColumn column)
    {
        GameContainer.addStructureMesh(column);
    }



}
