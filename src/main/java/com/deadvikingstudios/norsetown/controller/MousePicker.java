package com.deadvikingstudios.norsetown.controller;

import com.deadvikingstudios.norsetown.model.world.Chunk;
import com.deadvikingstudios.norsetown.model.world.World;
import com.deadvikingstudios.norsetown.view.RenderMath;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;

/**
 * Created by SiggiVG on 7/16/2017.
 *
 * Note: LWJGL calculates Display coordinates from the bottom left rather than the top left
 */
public class MousePicker
{
    private static final int RECURSION_COUNT = 200;
    private static final int RAY_RANGE = 600;

    private Vector3f currentRay = new Vector3f();

    private Matrix4f projMatrix;
    private Matrix4f viewMatrix;
    private CameraController camera;

    private World world;
    private Vector3f currentWorldPoint;

    public MousePicker(CameraController camera, Matrix4f projection, World world)
    {
        this.camera = camera;

        this.projMatrix = new Matrix4f(projection);
        this.viewMatrix = RenderMath.createViewMatrix(camera);
        this.world = world;
    }

    public Vector3f getCurrentWorldPoint()
    {
        return currentWorldPoint;
    }

    public Vector3f getCurrentRay()
    {
        return currentRay;
    }

    public void update()
    {
        viewMatrix = RenderMath.createViewMatrix(camera);
        currentRay = calculateMouseRay();
        //if (intersectionInRange(0, RAY_RANGE, currentRay)) {
        currentWorldPoint = lookForBlock(0, currentRay, 0, RAY_RANGE);
        //} else {
            //currentWorldPoint = null;
        //}
    }

    private Vector3f calculateMouseRay()
    {
        float mouseX;
        float mouseY;

        if(!Mouse.isGrabbed())
        {
            mouseX = Mouse.getX();
            mouseY = Mouse.getY();
        }
        else
        {
            mouseX = Display.getWidth()/2;
            mouseY = Display.getHeight()/2;
        }
        Vector2f normalizedCoords = getNormalizedDeviceCoords(mouseX, mouseY);
        //special case because we want a ray,
        Vector4f clipCoords = new Vector4f(normalizedCoords.x, normalizedCoords.y, -1f, 1f);
        Vector4f eyeCoords = toEyeCoords(clipCoords);

        return toWorldCoords(eyeCoords);
    }

    private Vector3f toWorldCoords(Vector4f eyeCoords)
    {
        Matrix4f invertedView = Matrix4f.invert(viewMatrix, null);
        Vector4f rayWorld = Matrix4f.transform(invertedView, eyeCoords, null);
        Vector3f mouseRay = new Vector3f(rayWorld.x, rayWorld.y, rayWorld.z);
        mouseRay.normalise();
        return mouseRay;
    }

    private Vector4f toEyeCoords(Vector4f clipCoords)
    {
        Matrix4f invertedProjection = Matrix4f.invert(projMatrix, null);
        Vector4f eyeCoords = Matrix4f.transform(invertedProjection, clipCoords, null);
        //we set z to -1 because we want our ray to go into the screen
        return new Vector4f(eyeCoords.x, eyeCoords.y, -1f, 0f);
    }

    private Vector2f getNormalizedDeviceCoords(float mouseX, float mouseY)
    {
        float x = (2f*mouseX) / Display.getWidth() - 1;
        float y = (2f*mouseY) / Display.getHeight() - 1;
        return new Vector2f(x,y);//if not using LWJGL, return (x,-y) instead
    }

    private Vector3f getPointOnRay(Vector3f ray, float distance) {
        Vector3f camPos = camera.getPosition();
        Vector3f start = new Vector3f(camPos.x, camPos.y, camPos.z);
        Vector3f scaledRay = new Vector3f(ray.x * distance, ray.y * distance, ray.z * distance);
        return Vector3f.add(start, scaledRay, null);
    }

    //TODO: look for face
    private Vector3f lookForBlock(int count, Vector3f ray, float start, float finish)
    {
        //exit Case
        if(count >= RECURSION_COUNT || start == finish)
        {
            Vector3f endPoint = getPointOnRay(ray, finish);
            if(world.getTileAt(endPoint) != 0 )
            {
                return world.worldSpaceToTileCoords(endPoint);
            }
            else
            {
                return null;
            }
        }
        if(world.getTileAt(getPointOnRay(ray, start)) != 0)
        {
            return world.worldSpaceToTileCoords(getPointOnRay(ray, start));
        }
        else
        {
            return lookForBlock(count + 1, ray, start + 1f, finish);
        }
    }
}
