package com.deadvikingstudios.norsetown;

import com.deadvikingstudios.bygul.controller.modloading.InitializationEvent;
import com.deadvikingstudios.bygul.controller.modloading.Mod;
import com.deadvikingstudios.bygul.controller.modloading.PostInitializationEvent;
import com.deadvikingstudios.bygul.controller.modloading.PreInitializationEvent;
import com.deadvikingstudios.norsetown.model.tiles.NorseTiles;

//@Mod(modid = "norsetown", name="NorseTown", version = "Indev-0.04")
public class NorseTown implements Mod
{
    @Override
    public String modid()
    {
        return "norsetown";
    }

    @Override
    public String name()
    {
        return "NorseTown";
    }

    @Override
    public String version()
    {
        return "Indev-0.04";
    }

    @Override
    public String acceptedEngineVersions()
    {
        return "Indev-0.04";
    }

    public void preInit(PreInitializationEvent event)
    {
        NorseTiles.init();
    }

    public void init(InitializationEvent event)
    {

    }

    public void postInit(PostInitializationEvent event)
    {

    }
}
