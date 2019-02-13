package com.deadvikingstudios.bygul.utils;

import com.deadvikingstudios.bygul.controller.GameContainer;

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
