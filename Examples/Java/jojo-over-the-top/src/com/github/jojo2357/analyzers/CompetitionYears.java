package src.com.github.jojo2357.analyzers;

import java.io.File;
import java.util.Scanner;

public enum CompetitionYears {
    y2015("Data Set/csv/3476_2015cc.csv", "2015"),
    y2019("Data Set/csv/2791_2019dar.csv", "2019");


    public String yearNumber;
    public String fileName;
    public String[] keyData;

    CompetitionYears(String fileName, String yearNumber) {
        this.yearNumber = yearNumber;
        this.fileName = fileName;
        File inputFile = new File(fileName);
        Scanner reader;
        try {
            reader = new Scanner(inputFile);
        } catch (Exception e) {
            throw new RuntimeException(inputFile + " could not be found");
        }
        keyData = reader.nextLine().split(",");
        reader.close();
    }
}
