package com.deadvikingstudios.norsetown.view;

import com.deadvikingstudios.norsetown.model.entities.Camera;
import com.deadvikingstudios.norsetown.model.world.Chunk;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;

import java.util.Arrays;

/**
 * Created by SiggiVG on 6/20/2017.
 *
 * Various Utility functions to do with rendering, mostly matrices
 */
public class RenderMath
{
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

    public static Matrix4f createViewMatrix(Camera camera)
    {
        Matrix4f matrix = new Matrix4f();

        Matrix4f.rotate((float)Math.toRadians(camera.getRotationX()), new Vector3f(1,0,0), matrix, matrix);
        Matrix4f.rotate((float)Math.toRadians(camera.getRotationY()), new Vector3f(0,1,0), matrix, matrix);
        Matrix4f.rotate((float)Math.toRadians(camera.getRotationZ()), new Vector3f(0,0,1), matrix, matrix);
        Matrix4f.translate(new Vector3f(-camera.getPosX(), -camera.getPosY(), -camera.getPosZ()), matrix, matrix);

        return matrix;
    }

    public static Matrix4f createPerspectiveMatrix(float aspectRatio, float fov, float near, float far)
    {
        Matrix4f matrix = new Matrix4f();
        matrix.setIdentity();

        float yScale = 1.0f / (float)Math.tan(Math.toRadians(fov / 2.0f));
        float xScale = yScale / aspectRatio;
        float zp = far + near;
        float zm = far - near;

        matrix.m00 = xScale;
        matrix.m11 = yScale;
        matrix.m22 = -zp/zm;
        matrix.m23 = -1;
        matrix.m32 = -(2*far*near)/zm;
        matrix.m33 = 0;

        return matrix;
    }

    /*public static Matrix4f createPerspectiveMatrix(float left, float right, float bottom, float top, float near, float far)
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
