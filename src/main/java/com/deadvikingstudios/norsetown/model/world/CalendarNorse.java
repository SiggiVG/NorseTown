package com.deadvikingstudios.norsetown.model.world;

public class CalendarNorse
{
    public static final int numOfDaysInYear = 365;

    private int currentDay = EnumSeason.SPRING.getStartDay();
    private EnumMoon currentMoon = EnumMoon.WAXING_CRESCENT;
    private int currentMoonDay = 0;

    /**
     * The current tick number
     */
    private int time = 0;
    public static final int DAY_LENGTH = 10800;

    public int getCurrentDay()
    {
        return currentDay;
    }

    public void update()
    {
        time++;
        if(!(time < DAY_LENGTH))
        {
            time = 0;
            advanceDay();
        }
    }

    public void advanceDay()
    {
        currentDay++;
        currentMoonDay++;
        if(currentMoonDay >= currentMoon.getLength())
        {
            currentMoon = currentMoon.getNextPhase();
            currentMoonDay = 0;
        }
    }

    public EnumMoon getCurrentMoon()
    {
        return currentMoon;
    }

    public int getCurrentMoonDay()
    {
        return currentMoonDay;
    }

    public int getTime()
    {
        return time;
    }

    public EnumSeason getCurrentSeason()
    {
        if(currentDay < EnumSeason.SPRING.getStartDay())
        {
            return EnumSeason.WINTER;
        }
        else if(currentDay < EnumSeason.SUMMER.getStartDay())
        {
            return EnumSeason.SPRING;
        }
        else if(currentDay < EnumSeason.AUTUMN.getStartDay())
        {
            return EnumSeason.SUMMER;
        }
        else// if(currentDay < EnumSeason.WINTER.getStartDay())
        {
            return EnumSeason.AUTUMN;
        }
    }

    public enum EnumSeason
    {
        SPRING(79), SUMMER(172), AUTUMN(265), WINTER(355);

        private int startDay;
        EnumSeason(int startDay)
        {
            this.startDay = startDay;
        }

        public int getStartDay()
        {
            return startDay;
        }
    }

    public enum EnumMoon
    {
        NEW("new_moon", 0f, 4),
        WAXING_CRESCENT("wax_crescent", 0.25f, 3),
        WAXING_QUARTER("wax_quarter", 0.5f, 4),
        WAXING_GIBBOUS("wax_gibbous", 0.75f, 3),
        FULL("full_moon", 1f, 4),
        WANING_GIBBOUS("wan_gibbous", 0.75f, 3),
        WANING_QUARTER("wan_quarter", 0.5f, 4),
        WANING_CRESCENT("wan_crescent", 0.25f, 3);

        String unlocalizedName;
        private float moonLight;
        private float length;

        EnumMoon(String unlocalizedName, float moonLight, int length)
        {
            this.unlocalizedName = unlocalizedName;
            this.moonLight = moonLight;
            this.length = length;
        }

        public float getMoonLight()
        {
            return moonLight;
        }

        public float getLength()
        {
            return length;
        }

        public EnumMoon getNextPhase()
        {
            switch (this)
            {
                case NEW: return WAXING_CRESCENT;
                case WAXING_CRESCENT: return WAXING_QUARTER;
                case WAXING_QUARTER: return WAXING_GIBBOUS;
                case WAXING_GIBBOUS: return FULL;
                case FULL: return WANING_CRESCENT;
                case WANING_CRESCENT: return WANING_QUARTER;
                case WANING_QUARTER: return WANING_GIBBOUS;
                case WANING_GIBBOUS: return NEW;
            }
            return null;
        }
    }

    public enum EnumWeekDay
    {
        SUNDAY("sunday", "Solsdagr", "Day of Sun"),
        MONDAY("munday", "Manisdagr", "Day of Moon"),
        TUESDAY("tuesday", "Tyrsdagr", "Day of Tyr"),
        WEDNESDAY("wednesday", "Odinsdagr", "Day of Odin"),
        THURSDAY("thursday", "Thorsdagr", "Day of Thor"),
        FRIDAY("friday", "Friggsdagr", "Day of Frigg"),
        SATURDAY("saturday", "Laugurdagr", "Day of Bathing");

        private String unlocalizedName;
        private String norseName;
        private String norseNameMeaning;

        EnumWeekDay(String unlocalizedName, String norseName, String norseNameMeaning)
        {
            this.unlocalizedName = unlocalizedName;
            this.norseName = norseName;
            this.norseNameMeaning = norseNameMeaning;
        }

        public String getUnlocalizedName()
        {
            return unlocalizedName;
        }

        public String getNorseName()
        {
            return norseName;
        }

        public String getNorseNameMeaning()
        {
            return norseNameMeaning;
        }
    }
}
