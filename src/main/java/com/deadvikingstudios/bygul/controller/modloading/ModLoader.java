package com.deadvikingstudios.bygul.controller.modloading;

import com.deadvikingstudios.bygul.controller.GameContainer;
import com.deadvikingstudios.bygul.utils.Logger;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class ModLoader
{
    private static final String modFileLoc = "/ModManifest.txt";

    private static List<Mod> modList;

    public static void preInitMods(GameContainer game)
    {
        if(modList == null)
            modList = getMods();
        for (Mod mod : modList)
        {

            mod.preInit(null);
        }
    }

    public static void initMods(GameContainer game)
    {
        if(modList == null)
            modList = getMods();
        for (Mod mod : modList)
        {
            mod.init(null);
        }
    }

    public static void postInitMods(GameContainer game)
    {
        if(modList == null)
            modList = getMods();
        for (Mod mod : modList)
        {
            mod.postInit(null);
        }
    }

    public static List<Mod> getMods()
    {

        List<String> modFiles = new ArrayList<>();
        try(InputStream stream = ModLoader.class.getResourceAsStream(modFileLoc))
        {
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(stream));
            String line;

            while((line = bufferedReader.readLine()) != null)
            {
                modFiles.add(line);
                Logger.info("Found mod " + line + " in " + modFileLoc + ", will attempt to load.");
            }
            stream.close();

        } catch (IOException e)
        {
            e.printStackTrace();
        }

        List<Mod> mods = new ArrayList<>();

        for (String file : modFiles)
        {
            Logger.info("Attempting to load " + file);
            Mod mod = getMod(file);
            if(mod != null)
            {
                Logger.info("Successfully found Mod " + file);
                mods.add(mod);
            }
            else
            {
                Logger.info("Could not find Mod " + file);
            }
        }
        return mods;
    }

    private static Mod getMod(String string)
    {
        try
        {
            Class<? extends Mod> clazz = (Class<? extends Mod>) Class.forName(string);
            return clazz.newInstance();
        } catch (ClassNotFoundException | ClassCastException | IllegalAccessException | InstantiationException e)
        {
            e.printStackTrace();
        }
        return null;
    }
}
