package src.com.github.jojo2357;

import src.com.github.jojo2357.analyzers.Analyze;
import src.com.github.jojo2357.analyzers.CompetitionYears;
import src.com.github.jojo2357.scarystuff.events.EventManager;
import src.com.github.jojo2357.scarystuff.events.events.RenderEvent;
import src.com.github.jojo2357.scarystuff.graphics.ScreenManager;
import src.com.github.jojo2357.scarystuff.typeface.JojoFont;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;

class HackathonMain {
    private static double fps = 60;
    private static double frameLength = 1000/fps;

    public static void main(String args[]) throws Exception{
        for (CompetitionYears year : CompetitionYears.values()) {
            String fileData = readFromFile(year.fileName);
            Analyze.analyze(fileData, year);
        }
        long last;
        int totalSkips = 0;
        ScreenManager.init();
        JojoFont.init();
        do{
            last = System.currentTimeMillis();
            //loops++;
            EventManager.sendTickEvent();
            EventManager.sendEvents();
            if (EventManager.addEvent(new RenderEvent()))
                break;
            if ((long) (frameLength - (System.currentTimeMillis() - last)) > 0)
                Thread.sleep((long) (frameLength - (System.currentTimeMillis() - last)));
            else
                System.out.println("SKIPPED FRAME (" + (frameLength - (System.currentTimeMillis() - last)) + ") " + (++totalSkips));
        }while (true);
    }

    private static String readFromFile(String filename) {
        try {
            StringBuilder builder = new StringBuilder();
            File inFile = new File(filename);
            FileReader inReader = new FileReader(inFile);
            char[] inData = new char[1];
            while (inReader.read(inData) != -1) {
                builder.append(inData);
            }
            inReader.close();
            return builder.toString();
        } catch (FileNotFoundException e) {
            System.out.println("Could not find " + filename);
        } catch (Exception e) {
            System.out.println("No idea why but we done muffed up");
        }
        throw new RuntimeException("U can see the error");
    }
}

