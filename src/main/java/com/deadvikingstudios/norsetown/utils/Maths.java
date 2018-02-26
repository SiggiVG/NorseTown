package com.deadvikingstudios.norsetown.utils;

import com.deadvikingstudios.norsetown.controller.CameraController;
import com.deadvikingstudios.norsetown.utils.vector.Vector3i;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

/**
 * Created by SiggiVG on 6/20/2017.
 *
 * Various Utility functions to do with rendering, mostly matrices
 */
public class Maths
{
    public static float lerp(float start, float end, float percent)
    {
        return start * (1.0f - percent) + end * percent;
    }

    public static Vector2f lerp(Vector2f start, Vector2f end, float percent)
    {
        return new Vector2f(lerp(start.x, end.x, percent), lerp(start.y, end.y, percent));
    }

    public static float lerp(float x1, float y1, float x2, float y2, float percent)
    {
        return lerp(lerp(x1, x2, percent), lerp(y1, y2, percent), percent);
    }

    public static Vector3f lerp(Vector3f start, Vector3f end, float percent)
    {
        return new Vector3f(lerp(start.x, end.x, percent), lerp(start.y, end.y, percent), lerp(start.z, end.z, percent));
    }

    public static float lerp(float x1, float y1, float z1, float x2, float y2, float z2, float percent)
    {
        return lerp(lerp(x1, y1, x2, y2, percent), lerp(y1, z1, y2, z2, percent), percent);
    }

    public static Vector3f lerp(Vector3i startMove, Vector3i target, float percent)
    {
        return lerp(new Vector3f(startMove.x, startMove.y, startMove.z), new Vector3f(target.x, target.y, target.z), percent);
    }

    public static float distance(float n1, float n2)
    {
        return (float) Math.sqrt(distanceSquared(n1,n2));
    }

    public static float distanceSquared(float n1, float n2)
    {
        return (float) Math.pow(n1-n2,2);
    }

    public static float distance(Vector2f start, Vector2f end)
    {
        return (float) Math.sqrt(distanceSquared(start,end));
    }

    public static float distanceSquared(Vector2f start, Vector2f end)
    {
        return distanceSquared(start.x, end.x) + distanceSquared(start.x, end.x);
    }

    public static float distance(Vector3f start, Vector3f end)
    {
        return (float) Math.sqrt(distanceSquared(start,end));
    }

    public static float distanceSquared(Vector3f start, Vector3f end)
    {
        return distanceSquared(start.x, end.x) + distanceSquared(start.y, end.y) + distanceSquared(start.z, end.z);
    }

    public static float distance(Vector3i start, Vector3i end)
    {
        return (float) Math.sqrt(distanceSquared(start,end));
    }

    public static float distanceSquared(Vector3i start, Vector3i end)
    {
        return distanceSquared(start.x, end.x) + distanceSquared(start.y, end.y) + distanceSquared(start.z, end.z);
    }

    public static float distanceSquared(Vector3f start, Vector3i end)
    {
        return distanceSquared(start.x, end.x) + distanceSquared(start.y, end.y) + distanceSquared(start.z, end.z);
    }

    public static float distanceSquared(Vector3i start, Vector3f end)
    {
        return distanceSquared(start.x, end.x) + distanceSquared(start.y, end.y) + distanceSquared(start.z, end.z);
    }

    public static Matrix4f createTransformationMatrix(Vector3f translation, float rx, float ry, float rz, float scale)
    {
        Matrix4f matrix = new Matrix4f();
        //1,0,0
        //0,1,0
        //0,0,1
        matrix.setIdentity();
        //x,0,0
        //0,y,0
        //0,0,z
        Matrix4f.translate(translation, matrix, matrix);
        //x,0,0
        //0,ycos(rx),-ysin(rx)
        //0,zsin(rx),zcos(rx)
        Matrix4f.rotate((float)Math.toRadians(rx), new Vector3f(1,0,0), matrix, matrix);
        //xcos(ry),0,xsin(ry)
        //0,ycos(rx),-ysin(rx)
        //-zsin(ry),zsin(rx),zcos(rx)cos(ry)
        Matrix4f.rotate((float)Math.toRadians(ry), new Vector3f(0,1,0), matrix, matrix);
        //xcos(ry)cos(rz),-xsin(rz),xsin(ry)
        //ysin(rz),ycos(rx)cos(rz),-ysin(rx)
        //-zsin(ry),zsin(rx),zcos(rx)cos(ry)
        Matrix4f.rotate((float)Math.toRadians(rz), new Vector3f(0,0,1), matrix, matrix);
        //sxcos(ry)cos(rz),-sxsin(rz),sxsin(ry)
        //sysin(rz),sycos(rx)cos(rz),-sysin(rx)
        //-szsin(ry),szsin(rx),szcos(rx)cos(ry)
        Matrix4f.scale(new Vector3f(scale,scale,scale), matrix, matrix);
        return matrix;
    }

    public static Matrix4f createViewMatrix(CameraController camera)
    {
        Matrix4f matrix = new Matrix4f();
        matrix.setIdentity();

        Matrix4f.rotate((float)Math.toRadians(camera.getPitch()), new Vector3f(1,0,0), matrix, matrix);
        Matrix4f.rotate((float)Math.toRadians(camera.getYaw()), new Vector3f(0,1,0), matrix, matrix);
        Matrix4f.rotate((float)Math.toRadians(camera.getRoll()), new Vector3f(0,0,1), matrix, matrix);
        Matrix4f.translate(new Vector3f(-camera.getPosition().x, -camera.getPosition().y, -camera.getPosition().z), matrix, matrix);

        return matrix;
    }

    public static Matrix4f createProjectionMatrix(int displayWidth, int displayHeight, float fov, float near, float far)
    {
        float aspectRatio = (float) displayWidth / (float) displayHeight;
        float xScale = (float) (1.0f / Math.tan(Math.toRadians(fov / 2.0f)));
        float yScale = xScale * aspectRatio;
        float frustum_length = far - near;

        Matrix4f matrix = new Matrix4f();
        matrix.m00 = xScale;
        matrix.m11 = yScale;
        matrix.m22 = -(far+near)/frustum_length;
        matrix.m23 = -1;
        matrix.m32 = -(2*far*near)/frustum_length;
        matrix.m33 = 0;

        return matrix;
    }

    public static Matrix4f createTransformationMatrix(Vector2f translation, Vector2f scale) {
        Matrix4f matrix = new Matrix4f();
        matrix.setIdentity();
        Matrix4f.translate(translation, matrix, matrix);
        Matrix4f.scale(new Vector3f(scale.x, scale.y, 1f), matrix, matrix);
        return matrix;
    }

    /*public static Matrix4f createProjectionMatrix(float left, float right, float bottom, float top, float near, float far)
    {
        Matrix4f matrix = new Matrix4f();
        matrix.setIdentity();

        matrix.m00 = 2 * near / (right - left);
        matrix.m11 = 2 * near / (top - bottom);
        matrix.m20 = (right + left) / (right -left);
        matrix.m21 = (top + bottom) / (top - bottom);
        matrix.m22 = -(far + near)/(far - near);
        matrix.m23 = -1;
        matrix.m32 = -2 * far * near / (far - near);
        matrix.m33 = 0;

        return matrix;
    }

    public static Matrix4f createOrthagonalMatrix(float left, float right, float bottom, float top, float near, float far)
    {
        Matrix4f matrix = new Matrix4f();

        matrix.setIdentity();

        matrix.m00 = 2 / (right - left);
        matrix.m11 = 2 / (top - bottom);
        matrix.m22 = -2 / (far - near);
        matrix.m32 = (far + near) / (far - near);
        matrix.m30 = (right + left) / (right - left);
        matrix.m31 = (top + bottom) / (top - bottom);

        return matrix;
    }*/


}
