package com.deadvikingstudios.norsetown.utils;

import com.deadvikingstudios.norsetown.controller.GameContainer;

public class Logger
{
    public static void debug(Object obj)
    {
        if(GameContainer.MODE == GameContainer.DEBUG)
        {
            System.out.println(obj.toString());
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
