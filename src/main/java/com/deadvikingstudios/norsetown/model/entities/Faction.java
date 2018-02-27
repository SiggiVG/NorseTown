package com.deadvikingstudios.norsetown.model.entities;

import com.deadvikingstudios.norsetown.model.entities.ai.tasks.TaskManager;
import com.sun.istack.internal.NotNull;

import java.util.HashMap;
import java.util.Map;

/**
 * Factions define which task list the entities can pull tasks from and how they react to other entities based on faction
 */
public class Faction
{
    private static HashMap<String, Faction> factions = new HashMap<>();

    private final String unlocalizedName;
    private final TaskManager taskManager;

    public Faction(String name)
    {
        this.unlocalizedName = name;
        this.taskManager = new TaskManager();
    }

    public String getName()
    {
        return this.unlocalizedName;
    }

    public TaskManager getTaskManager()
    {
        return taskManager;
    }

    public static void init()
    {
        registerFaction(new Faction("player"));
    }

    public static Faction registerFaction(@NotNull Faction faction)
    {
        if(faction == null) return null;
        factions.put(faction.getName(), faction);
        return faction;
    }

    public static Faction get(String key)
    {
        return factions.get(key);
    }

    public static void update()
    {
        for (Map.Entry<String, Faction> faction : factions.entrySet())
        {
            faction.getValue().getTaskManager().update();
        }
    }
}
