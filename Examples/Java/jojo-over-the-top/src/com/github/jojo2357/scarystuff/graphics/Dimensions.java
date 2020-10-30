package src.com.github.jojo2357.scarystuff.graphics;

public class Dimensions {

    private final int width;
    private final int height;

    public Dimensions(int xWidth, int yHeight){
        this.width = xWidth;
        this.height = yHeight;
    }

    public int getWidth(){
        return this.width;
    }

    public int getHeight(){
        return this.height;
    }

    public double getDiagonal(){
        return Math.sqrt(width * width + height *height)/2.0;
    }

    @Override
    public String toString(){
        return "Dimension (" + this.width + ", " + this.height + ")";
    }
}
