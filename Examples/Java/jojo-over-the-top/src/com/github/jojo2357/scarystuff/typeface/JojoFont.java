package src.com.github.jojo2357.scarystuff.typeface;

public class JojoFont {

    private static final FontCharacter[][] fontCharacters = new FontCharacter[Colors.values().length][42];

    public static void init() {
        loadNumbers();
        loadLetters();
    }

    public static void loadNumbers() {
        for (Colors color : Colors.values()) {
            for (char asciiValue = 48; asciiValue < 58; asciiValue++) {
                fontCharacters[color.valueOf()][asciiValue - 48] = new FontCharacter(asciiValue, color);
            }
            fontCharacters[color.valueOf()][36] = new FontCharacter('-', color);
        }
        fontCharacters[Colors.WHITE.valueOf()][37] = new FontCharacter('#', Colors.WHITE);
        fontCharacters[Colors.WHITE.valueOf()][38] = new FontCharacter("slash", '/', Colors.WHITE);
        fontCharacters[Colors.WHITE.valueOf()][39] = new FontCharacter('[', Colors.WHITE);
        fontCharacters[Colors.WHITE.valueOf()][40] = new FontCharacter(']', Colors.WHITE);
        fontCharacters[Colors.WHITE.valueOf()][41] = new FontCharacter("questionMark", '?', Colors.WHITE);
    }

    private static void loadLetters() {
        for (Colors color : Colors.values()) {
            for (char asciiValue = 65; asciiValue < 91; asciiValue++) {
                fontCharacters[color.valueOf()][asciiValue - 55] = new FontCharacter(asciiValue, color);
            }
        }
    }

    public static FontCharacter getCharacter(Colors color, char charRepresentation) {
        return getCharacter(charRepresentation, color);
    }

    public static FontCharacter getCharacter(char charRepresentation, Colors color) {
        if (charRepresentation <= '9' && charRepresentation >= '0')
            if (fontCharacters[color.valueOf()][charRepresentation - 48] == null)
                throw new IllegalStateException("Probs wrong color. " + charRepresentation + "_" + color.getName() + " DNE");
            else
                return fontCharacters[color.valueOf()][charRepresentation - 48];
        if (charRepresentation <= 'Z' && charRepresentation >= 'A')
            if (fontCharacters[color.valueOf()][charRepresentation - 55] == null)
                throw new IllegalStateException("Probs wrong color. " + charRepresentation + "_" + color.getName() + " DNE");
            else
                return fontCharacters[color.valueOf()][charRepresentation - 55];
        if (charRepresentation == '-')
            if (fontCharacters[color.valueOf()][36] == null)
                throw new IllegalStateException("Probs wrong color. " + charRepresentation + "_" + color.getName() + " DNE");
            else
                return fontCharacters[color.valueOf()][36];
        if (charRepresentation == '#')
            if (fontCharacters[color.valueOf()][37] == null)
                throw new IllegalStateException("Probs wrong color. " + charRepresentation + "_" + color.getName() + " DNE");
            else
                return fontCharacters[color.valueOf()][37];
        if (charRepresentation == '/' || charRepresentation == '\\')
            if (fontCharacters[color.valueOf()][38] == null)
                throw new IllegalStateException("Probs wrong color. " + charRepresentation + "_" + color.getName() + " DNE");
            else
                return fontCharacters[color.valueOf()][38];
        if (charRepresentation == '[')
            if (fontCharacters[color.valueOf()][39] == null)
                throw new IllegalStateException("Probs wrong color. " + charRepresentation + "_" + color.getName() + " DNE");
            else
                return fontCharacters[color.valueOf()][39];
        if (charRepresentation == ']')
            if (fontCharacters[color.valueOf()][40] == null)
                throw new IllegalStateException("Probs wrong color. " + charRepresentation + "_" + color.getName() + " DNE");
            else
                return fontCharacters[color.valueOf()][40];
        if (charRepresentation == '?')
            if (fontCharacters[color.valueOf()][41] == null)
                throw new IllegalStateException("Probs wrong color. " + charRepresentation + "_" + color.getName() + " DNE");
            else
                return fontCharacters[color.valueOf()][41];

        throw new IndexOutOfBoundsException("font type for " + charRepresentation + "_" + color.getName() + " not found");
    }
}
