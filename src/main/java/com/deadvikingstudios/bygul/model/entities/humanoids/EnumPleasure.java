package com.deadvikingstudios.bygul.model.entities.humanoids;

/**
 * Used as descriptors for how certain actions effect the Humanoid
 */
public enum EnumPleasure
{
    /**
     * Won't even attempt it
     */
    REFUSE,
    /**
     * Avoids doing it, big mood penalty
     */
    HATE,
    /**
     * Mood penalty
     */
    DISLIKE,
    /**
     * Has no affect on mood
     */
    INDIFFERENT,
    /**
     * Mood benefit
     */
    ENJOY,
    /**
     * Will do if able, big mood benefit
     */
    ADORE;

    private EnumPleasure()
    {

    }

    public EnumPleasure getWorse()
    {
        //if(this == REFUSE) return this;
        switch (this)
        {
            case HATE: return REFUSE;
            case DISLIKE: return HATE;
            case INDIFFERENT: return DISLIKE;
            case ENJOY: return INDIFFERENT;
            case ADORE: return ENJOY;
            default: return REFUSE;
        }
    }

    public EnumPleasure getBetter()
    {
        //if(this == REFUSE) return this;
        switch (this)
        {
            case REFUSE: return HATE;
            case HATE: return DISLIKE;
            case DISLIKE: return INDIFFERENT;
            case INDIFFERENT: return ENJOY;
            case ENJOY: return ADORE;
            default: return ADORE;
        }
    }

    public EnumPleasure get(int ordinal)
    {
        return values()[ordinal];
    }
}
