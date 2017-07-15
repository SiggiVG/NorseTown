package com.deadvikingstudios.norsetown.model.entities.humanoids;

import com.deadvikingstudios.norsetown.model.entities.humanoids.races.Race;

/**
 * Created by SiggiVG on 7/13/2017.
 */
public class Humanoid
{
    /*//Start Mood TODO: move to Race
    //How they feel about their sleeping environments and what kind of comfort mod they get from it.
    // 0: wont do; 1: hates; 2: dislikes; 3: doesnt mind; 4: likes; 5: loves
    private byte sleepOutside = 1;
    private byte cramptInteriors = 1;
    private byte sleepHardSurfaces = 2;
    private byte sleepSolfSurfaces = 4;
    private byte sleepShareRooms = 3;
    private byte sleepOwnRoom = 5;

    //Food comforts
    private byte ateRaw = 2;
    private byte ateVeggie = 3;
    private byte ateMeat = 4;
    private byte drankWater = 3;
    private byte drankBooze = 5;
    private byte ateAtTable = 4;
    private byte ateStanding = 2;
    //End Mood*/

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
