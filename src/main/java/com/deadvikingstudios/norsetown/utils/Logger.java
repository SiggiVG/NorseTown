package com.deadvikingstudios.norsetown.utils;

import com.deadvikingstudios.norsetown.controller.GameContainer;

public class Logger
{
    public static void debug(String message)
    {
        if(GameContainer.MODE == GameContainer.DEBUG)
        {
            System.out.println(message);
        }
    }

    public static void info(String message)
    {
        //if(GameContainer.MODE == GameContainer.DEBUG)
        {
            System.out.println(message);
        }
    }
}
