package com.deadvikingstudios.norsetown.view;

public interface Text2D
{
    void initText2D(final String texturePath);
    void printText2D(final String text, int x, int y, int size);
    void cleanupText2D();
}
