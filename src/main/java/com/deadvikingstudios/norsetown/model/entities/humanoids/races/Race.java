package com.deadvikingstudios.norsetown.model.entities.humanoids.races;

import java.util.ArrayList;

/**
 * Created by SiggiVG on 7/13/2017.
 */
public abstract class Race
{
    private static Race[] races;

    public final String raceName;
    public final int raceNum;

    protected Race(String name, int id)
    {
        this.raceName = name;
        this.raceNum = id;
    }

    //Different races have different cultural likes and dislikes

    public void init()
    {
         races = new Race[16];
         addRace(new RaceHuman("nord", 0));
    }

    public void addRace(Race race)
    {
        races[race.raceNum] = race;
    }
}
