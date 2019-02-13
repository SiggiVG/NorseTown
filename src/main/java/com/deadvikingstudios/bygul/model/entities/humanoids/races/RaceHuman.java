package com.deadvikingstudios.bygul.model.entities.humanoids.races;

/**
 * Created by SiggiVG on 7/13/2017.
 */
public class RaceHuman extends Race
{
    /*//Start Mood
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

    protected RaceHuman(String name, int id)
    {
        super("human_" + name, id);
    }
}
