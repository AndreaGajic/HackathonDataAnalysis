package src.com.github.jojo2357.scarystuff.typeface;

import src.com.github.jojo2357.scarystuff.graphics.Texture;

public class FontCharacter {
    private final char stringRep;
    private final Colors color;
    private final Texture image;

    public FontCharacter(char strRepresentation, Colors color) {
        this.stringRep = strRepresentation;
        this.color = color;
        this.image = new Texture(color.getName() + "/" + strRepresentation + "_" + color.getName());
    }

    public FontCharacter(String fileName, char strRepresentation, Colors color) {
        this.stringRep = strRepresentation;
        this.color = color;
        this.image = new Texture(color.getName() + "/" + fileName + "_" + color.getName());
    }

    public char getStringRep() {
        return stringRep;
    }

    public Colors getColor() {
        return color;
    }

    public Texture getImage() {
        return image;
    }
}
