package com.deadvikingstudios.norsetown.view;

import com.deadvikingstudios.norsetown.controller.GameContainer;
import com.deadvikingstudios.norsetown.view.lwjgl.Loader;
import com.deadvikingstudios.norsetown.view.meshes.RawMesh;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by SiggiVG on 6/27/2017.
 */
public class OBJLoader
{
    public static RawMesh loadObjModel(String filePath, Loader loader)
    {
        //FileReader fr = null;
        InputStream in;
        BufferedReader reader;

        //fr = new FileReader(new File("/" + filePath + ".obj"));
        in = OBJLoader.class.getResourceAsStream("/" + filePath + ".obj");
        reader = new BufferedReader(new InputStreamReader(in));
        String line;
        List<Vector3f> vertices = new ArrayList<Vector3f>();
        List<Vector2f> uvs = new ArrayList<Vector2f>();
        List<Vector3f> normals = new ArrayList<Vector3f>();
        List<Integer> indices = new ArrayList<Integer>();
        float[] verticesArray = null;
        float[] uvsArray = null;
        float[] normalsArray = null;
        int[] indicessArray = null;
        try
        {
            while(true)
            {
                line = reader.readLine();
                String[] currentLine = line.split(" ");
                if(line.startsWith("v "))//vertex
                {
                    Vector3f vertex = new Vector3f(Float.parseFloat(currentLine[1]), Float.parseFloat(currentLine[2]),
                            Float.parseFloat(currentLine[3]));
                    vertices.add(vertex);
                }else if(line.startsWith("vt "))//uvs
                {
                    Vector2f uv = new Vector2f(Float.parseFloat(currentLine[1]), Float.parseFloat(currentLine[2]));
                    uvs.add(uv);
                }else if(line.startsWith("vn "))//normals
                {
                    Vector3f normal = new Vector3f(Float.parseFloat(currentLine[1]), Float.parseFloat(currentLine[2]),
                            Float.parseFloat(currentLine[3]));
                    normals.add(normal);
                }else if(line.startsWith("f "))
                {
                    uvsArray = new float[vertices.size() * 2];
                    normalsArray = new float[vertices.size() * 3];
                    break;
                }
            }
            while(line != null)
            {
                if(!line.startsWith("f "))
                {
                    line = reader.readLine();
                    continue;
                }
                String[] currentLine = line.split(" ");
                String[] vertex1 = currentLine[1].split("/");
                String[] vertex2 = currentLine[2].split("/");
                String[] vertex3 = currentLine[3].split("/");

                processVertex(vertex1, indices, uvs, normals, uvsArray, normalsArray);
                processVertex(vertex2, indices, uvs, normals, uvsArray, normalsArray);
                processVertex(vertex3, indices, uvs, normals, uvsArray, normalsArray);
                line = reader.readLine();
            }
            reader.close();
        } catch(Exception e)
        {
            e.printStackTrace();
        }

        verticesArray = new float[vertices.size()*3];
        indicessArray = new int[indices.size()];

        int vertexPointer = 0;
        for(Vector3f vertex : vertices)
        {
            verticesArray[vertexPointer++] = vertex.x;
            verticesArray[vertexPointer++] = vertex.y;
            verticesArray[vertexPointer++] = vertex.z;
        }

        for (int i = 0; i < indices.size(); i++)
        {
            indicessArray[i] = indices.get(i);
        }
        return loader.loadToVAO(verticesArray, indicessArray, uvsArray, normalsArray);
    }

    private static void processVertex(String[] vertexData,
                                      List<Integer> indices, List<Vector2f> uvs, List<Vector3f> normals,
                                      float[] uvsArray, float[] normalsArray)
    {
        int currentVertexPointer = Integer.parseInt(vertexData[0]) - 1;
        indices.add(currentVertexPointer);
        Vector2f currentUV = uvs.get(Integer.parseInt(vertexData[1]) - 1);
        uvsArray[currentVertexPointer*2] = currentUV.x;
        uvsArray[currentVertexPointer*2+1] = 1 - currentUV.y;//blender does something odd, so -1
        Vector3f currentNorm = normals.get(Integer.parseInt(vertexData[2]) - 1);
        normalsArray[currentVertexPointer*3] = currentNorm.x;
        normalsArray[currentVertexPointer*3+1] = currentNorm.y;
        normalsArray[currentVertexPointer*3+2] = currentNorm.z;
    }
}
