package com.deadvikingstudios.bygul.model.entities.ai.pathfinding;

import com.deadvikingstudios.bygul.model.entities.EntityLiving;
import com.deadvikingstudios.bygul.model.world.World;
import com.deadvikingstudios.bygul.model.world.structures.Structure;
import com.deadvikingstudios.bygul.utils.Logger;
import com.deadvikingstudios.bygul.utils.Maths;
import com.deadvikingstudios.bygul.utils.vector.Vector3i;
import com.sun.istack.internal.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 *
 */
public class Pathfinder
{
    private List<PathNode> path = null;


    public static List<PathNode> findPathAStar(EntityLiving entity, @NotNull Vector3i start, @NotNull Vector3i goal, boolean useDiagonals)
    {
        if(start == null || goal == null)
        {
            Logger.debug(start + "," + goal);
            return null;
        }
        //TODO: run this in a thread.
        List<PathNode> openList = new ArrayList<PathNode>();
        List<PathNode> closeList = new ArrayList<PathNode>();

        //TODO: speed up by using distanceSquared
        PathNode current = new PathNode(new Vector3i(start), null, 0, Maths.distanceSquared(start, goal));
        openList.add(current);


        while(openList.size() > 0)
        {
            Collections.sort(openList, PathNode.NODE_COMPARATOR);

            current = openList.get(0);
            if(current.position.equals(goal))
            {
                List<PathNode> path = new ArrayList<PathNode>();
                while(current.parent != null)
                {
                    path.add(current);
                    current = current.parent;
                }
                openList.clear();
                closeList.clear();
                return path;
            }
            openList.remove(current);
            closeList.add(current);

            //in 2D, add the 8 surrounding nodes, if traversable
            //in 3D, add the 26 surrounding nodes, if traversable
            for (int i = -1; i < 2; i++)
            {
                for (int k = -1; k < 2; k++)
                {
                    //3D
                    for (int j = -1; j < 2; j++)
                    {
                        int x = current.position.x;
                        int y = current.position.y;
                        int z = current.position.z;

                        //TODO: have it look at multiple structures by getting the world
                        //is the current tile
                        if(i == 0 && j == 0 && k == 0) continue;
                        if(!useDiagonals)
                            if(i != 0 && k != 0) continue;
                        //if it's not solid
                        if(World.getCurrentWorld().getCurrentBuildStructure().getTile(x + i, y + j, z + k).isSolidCuboid()) continue;
                        //if the block below it is not solid
                        if(!World.getCurrentWorld().getCurrentBuildStructure().getTile(x + i, y + j-1, z + k).isSolidCuboid()) continue;
                        //if the space is too small for the entity to traverse
                        boolean tooSmall = false;
                        for (int l = 1; l <= entity.height; l++)
                        {
                            if(World.getCurrentWorld().getCurrentBuildStructure().getTile(x + i, y + j+l, z + k).isSolidCuboid())
                            {
                                tooSmall = true;
                                break;
                            }
                        }
                        if(tooSmall) continue;

                        //TODO: add support for differing tile sizes
                        Vector3i a = new Vector3i(x + i, y + j, z + k);
                        PathNode node = new PathNode(new Vector3i(x + i, y + j, z + k), current,
                                current.travelSoFarCost + Maths.distanceSquared(current.position, a), Maths.distanceSquared(a, goal));

                        if(vecInList(closeList, node.position) && node.travelSoFarCost >= current.travelSoFarCost) continue;
                        if(!vecInList(openList, node.position) || node.travelSoFarCost < current.travelSoFarCost) openList.add(node);
                    }
                }
            }
        }
        closeList.clear();
//        Logger.debug("Path from " + start + " to " + goal + " not an attainable destination");
//        World.getCurrentWorld().currentIsland.setTile(Tile.NorseTiles.tileClay, goal.x, goal.y, goal.z);
        if(entity.hasTask()) entity.cancelTask();
        return null;
    }

    public List<PathNode> findPathJumpPoint(Structure structure, Vector3i start, Vector3i goal)
    {
        return null;
    }

    private static boolean vecInList(List<PathNode> list, Vector3i vector)
    {
        for (PathNode node : list)
        {
            if(node.position.equals(vector)) return true;
        }
        return false;
    }
}
