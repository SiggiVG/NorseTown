package com.deadvikingstudios.bygul.controller.modloading;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * This defines a Mod to the bygul engine. Any classes found with this annotation applied will be loaded as a Mod.
 * It will be instantiated (with the no-param constructor) and will be sent various subclassed of {@link ModLoaderEvent}
 * at runtime during predefined times during the loading of the game.
 */
//@Target(ElementType.TYPE)
//@Retention(RetentionPolicy.RUNTIME)
public interface Mod
{
    /**
     * a unique identifier for the Mod
     */
    String modid();

    String name();// default "";

    String version();// default "";

    String acceptedEngineVersions();// default "";

    void preInit(PreInitializationEvent event);
    void init(InitializationEvent event);
    void postInit(PostInitializationEvent event);
}
