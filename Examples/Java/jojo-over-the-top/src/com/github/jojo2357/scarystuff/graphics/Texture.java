package src.com.github.jojo2357.scarystuff.graphics;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.stb.STBImage.*;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;

import org.lwjgl.BufferUtils;

public class Texture {
    private final int id;
    private final int width;
    private final int heigth;

    public Texture(String filename) {
        IntBuffer width = BufferUtils.createIntBuffer(1);
        IntBuffer heigth = BufferUtils.createIntBuffer(1);
        IntBuffer comp = BufferUtils.createIntBuffer(1);
        ByteBuffer data = stbi_load("./src/images/" + filename + ".png", width, heigth, comp, 4);

        this.width = width.get();
        this.heigth = heigth.get();
        this.id = glGenTextures(); // generate texture name

        glBindTexture(GL_TEXTURE_2D, this.id);
        glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
        glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
        glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, this.width, this.heigth, 0, GL_RGBA, GL_UNSIGNED_BYTE, data);

        if (data == null)
            throw new NullPointerException("Image at " + "./src/images/" + filename + ".png" + " may not exist");
        stbi_image_free(data);
    }

    public int getWidth() {
        return this.width;
    }

    public int getHeight() {
        return this.heigth;
    }

    public int getId() {
        return this.id;
    }

    public void bind() {
        glBindTexture(GL_TEXTURE_2D, this.id);
    }
}