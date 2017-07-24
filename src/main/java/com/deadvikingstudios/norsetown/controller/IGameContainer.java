package com.deadvikingstudios.norsetown.controller;

/**
 * Created by SiggiVG on 6/22/2017.
 */
public interface IGameContainer
{
    void init();
    void input() throws Exception;
    void update(float dt);
    void render();
}
