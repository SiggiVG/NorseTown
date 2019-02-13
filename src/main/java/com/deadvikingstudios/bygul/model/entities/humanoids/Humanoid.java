package com.deadvikingstudios.bygul.model.entities.humanoids;

import com.deadvikingstudios.bygul.model.entities.humanoids.races.Race;

/**
 * Created by SiggiVG on 7/13/2017.
 */
public class Humanoid
{
    private Race race;

    private final String baseName; //game named
    private String nickName; //player named

    private BirthDate birthdate;

    private float carryCapacity;

    public Humanoid(Race race, String name, BirthDate bdate)
    {
        this.race = race;
        this.baseName = name;
        this.nickName = name;
        this.birthdate = new BirthDate(bdate);

    }

}

class BirthDate
{
    public final int year, month, day;

    public BirthDate(int month, int day, int year)
    {
        this.year = year;
        this.month = month;
        this.day = day;
    }

    public BirthDate(BirthDate bdate)
    {
        this.year = bdate.year;
        this.month = bdate.month;
        this.day = bdate.day;
    }
}

