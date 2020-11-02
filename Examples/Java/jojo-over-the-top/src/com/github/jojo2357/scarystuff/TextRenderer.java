package src.com.github.jojo2357.scarystuff;

import src.com.github.jojo2357.scarystuff.graphics.Point;
import src.com.github.jojo2357.scarystuff.graphics.ScreenManager;
import src.com.github.jojo2357.scarystuff.typeface.Colors;
import src.com.github.jojo2357.scarystuff.typeface.JojoFont;

public class TextRenderer {
    public static void render(String charSequenceToPrint, Colors color, Point renderStart, int lineWidth) {
        charSequenceToPrint = charSequenceToPrint.toUpperCase();
        Point currentSpot = renderStart.copy();
        int onThisLine = 0;
        charSequenceToPrint += ' ';
        try {
            for (int i = 0; i < charSequenceToPrint.length(); i++) {
                char renderChar = charSequenceToPrint.charAt(i);
                if (renderChar != ' ') {
                    onThisLine++;
                    ScreenManager.renderTexture(JojoFont.getCharacter(renderChar, color).getImage(), currentSpot, 2.1f);
                    currentSpot.stepX(18);
                } else {
                    if (charSequenceToPrint.indexOf(' ', i + 1) - i + onThisLine >= lineWidth) {
                        currentSpot = new Point(renderStart.getX(), currentSpot.getY() + 40);
                        onThisLine = 0;
                    } else currentSpot.stepX(18);
                }
            }
        }catch (Exception e){
            System.out.println("Tried printing " + charSequenceToPrint);
            throw e;
        }
    }
}
