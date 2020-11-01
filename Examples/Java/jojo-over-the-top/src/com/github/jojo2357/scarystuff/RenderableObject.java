package src.com.github.jojo2357.scarystuff;

import org.lwjgl.opengl.GL;
import src.com.github.jojo2357.scarystuff.events.EventManager;
import src.com.github.jojo2357.scarystuff.events.GameTimes;
import src.com.github.jojo2357.scarystuff.graphics.Dimensions;
import src.com.github.jojo2357.scarystuff.graphics.Point;
import src.com.github.jojo2357.scarystuff.graphics.ScreenManager;
import src.com.github.jojo2357.scarystuff.graphics.Texture;

public class RenderableObject {
    protected Texture image;
    protected Dimensions imageSize;
    protected boolean isVisible = true;
    protected double rotation = 0; //degrees
    protected boolean canRotate = false;

    protected RenderableObject(String filename) {
        GL.createCapabilities();
        this.image = new Texture(filename);
        this.imageSize = new Dimensions(image.getWidth(), image.getHeight());
        this.registerToRender();
    }

    protected RenderableObject(){

    }

    protected void reInit(String filename){
        GL.createCapabilities();
        this.image = new Texture(filename);
        this.imageSize = new Dimensions(image.getWidth(), image.getHeight());
        this.registerToRender();
    }

    protected void render(Point location, Dimensions dimensions) {
        if (EventManager.currentPhase == GameTimes.SECOND_RENDER)
            ScreenManager.renderTexture(this.image, location, 1, this.rotation, dimensions);
    }

    protected boolean render(Point location) {
        if (EventManager.currentPhase == GameTimes.SECOND_RENDER)
            ScreenManager.renderTexture(this.image, location, 1, this.rotation, this.imageSize);
        return false;
    }

    public boolean render() {
        if (EventManager.currentPhase == GameTimes.SECOND_RENDER)
            this.render(new Point(0, 0));
        return false;
    }

    protected void registerToRender() {
        EventManager.addRenderingObject(this);
    }

    public void disable(boolean visible) {
        this.isVisible = visible;
    }

    public boolean getVisibility() {
        return this.isVisible;
    }
}
