package com.deadvikingstudios.bygul.view;

import com.deadvikingstudios.bygul.utils.Logger;
import org.lwjgl.util.vector.Vector2f;
import org.newdawn.slick.opengl.PNGDecoder;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class TextureAtlas
{
    public static final TextureAtlas instance = new TextureAtlas();
    public static int atlasID;

    public static final int TEXTURE_DIMENSIONS = 32;

    public static int getAtlasID()
    {
        return atlasID;
    }

    public void CreateAtlas()
    {
        int rgba = PNGDecoder.RGBA.getNumComponents();

        List<File> files = null;
        HashMap<String, Vector2f> uvs = new HashMap<String, Vector2f>();

        try(Stream<Path> filePathStream=Files.walk(Paths.get("src\\main\\resources\\norsetown.textures\\tiles")))
        {
            files = filePathStream
                    .filter(Files::isRegularFile)
                    .map(Path::toFile)
                    .collect(Collectors.toList());

            int atlasWidth = (int)Math.ceil((Math.sqrt(files.size())+1) * TEXTURE_DIMENSIONS);
            ByteBuffer atlas = ByteBuffer.allocateDirect(rgba*atlasWidth*atlasWidth);
            float textureStep = 1 / (atlasWidth/TEXTURE_DIMENSIONS);


            for (File file : files)
            {
                try(BufferedInputStream in = new BufferedInputStream(this.getClass().getResourceAsStream("/norsetown/textures/tiles/" + file.getName())))
                {
                    PNGDecoder decoder = new PNGDecoder(in);
                    ByteBuffer buf = ByteBuffer.allocateDirect(rgba*decoder.getWidth()*decoder.getHeight());
                    decoder.decode(buf, decoder.getWidth()*rgba, PNGDecoder.RGBA);
                    buf.flip();
                    Logger.debug(buf);

                    //Combine to atlas

                } catch (IOException e)
                {
                    Logger.debug("Unable to load texture: " + file.getName());
                }

            }
        } catch (IOException e)
        {
            e.printStackTrace();
        }

//        BufferedInputStream in = null;
//        try
//        {
//            in = new BufferedInputStream(this.getClass().getResourceAsStream("/" +  filePath + ".png"));
//            PNGDecoder decoder = new PNGDecoder(in);
//            // assuming RGB here but should allow for RGB and RGBA (changing wall.png to RGBA will crash this!)
//            ByteBuffer buf = ByteBuffer.allocateDirect(4*decoder.getWidth()*decoder.getHeight());
//            decoder.decode(buf, decoder.getWidth()*4, PNGDecoder.Format.RGBA);
//            buf.flip();
//            System.out.println(buf);
////            texture=glGenTextures();
////            glBindTexture(GL_TEXTURE_2D, texture);
////            glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, decoder.getWidth(), decoder.getHeight(), 0,
////                    GL_RGBA, GL_UNSIGNED_BYTE, buf);
////            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
////            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
//            in.close();
//        } catch (FileNotFoundException e)
//        {
//            System.out.println("Texture not found: " + filePath);
//            e.printStackTrace();
//        } catch (IOException e)
//        {
//            e.printStackTrace();
//        }


    }
}
