package com.deadvikingstudios.norsetown.model.entities.ai.pathfinding;

import com.deadvikingstudios.norsetown.utils.vector.Vector3i;

import java.util.Comparator;

public class PathNode
{
    public Vector3i position;
    public PathNode parent;
    public double finalCost, travelSoFarCost, heuristicCost;

    public static final Comparator<PathNode> NODE_COMPARATOR = (n0, n1) -> {
        if(n1.finalCost < n0.finalCost) return +1;
        if(n1.finalCost > n0.finalCost) return -1;
        return 0;
    };

    public PathNode(Vector3i position, PathNode parent, double travelSoFarCost, double hueristicCost)
    {
        this.position = position;
        this.parent = parent;
        this.travelSoFarCost = travelSoFarCost;
        this.heuristicCost = hueristicCost;
        this.finalCost = this.travelSoFarCost + this.heuristicCost;
    }
}
