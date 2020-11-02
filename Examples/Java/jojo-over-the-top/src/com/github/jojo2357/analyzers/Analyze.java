package src.com.github.jojo2357.analyzers;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;

public class Analyze {
    public static final int autoHatchPointValue = 2;
    public static final int autoCargoPointValue = 3;
    public static final int manualHatchPointValue = 2;
    public static final int manualCargoPointValue = 3;
    public static final int foulPenalty = 3;
    public static final int habClimbMult = 3; //per level

    public static ArrayList<GameData> allGames = new ArrayList<Analyze.GameData>();

    public static HashMap<CompetitionYears, HashMap<Integer, ArrayList<Analyze.GameData>>> gameData = new HashMap<CompetitionYears,HashMap<Integer, ArrayList<Analyze.GameData>>>();
    public static HashMap<CompetitionYears,HashMap<String, ArrayList<Analyze.GameData>>> tempTeamData = new HashMap<CompetitionYears,HashMap<String, ArrayList<Analyze.GameData>>>();
	/*
	1 Team Number,
	2 Match Number,
	3 Starting Position,
	4 Hab Line 1/0,
	5 Auto # H Ship Side,
	6 Auto # H Ship Front,
	7 Auto # C Ship Side,
	8 Auto C Rkt Lvl,
	9 Auto H Rkt Lvl,
	10 Auto H Rkt Lvl [2],
	11 Auto # C Dropped,
	12 Auto # H Dropped,
	13 Tele H Ship,
	14 Tele C Ship,
	15 Tele Rocket H L1,
	16 Tele Rocket H L2,
	17 Tele Rocket H L3,
	18 Tele Rocket C L1,
	19 Tele Rocket C L2,
	20 Tele Rocket C L3,
	21 Tele Dropped C,
	22 Tele Dropped H,
	23 Def pl amt,
	24 Def pl quality,
	25 Def rec amt,
	26 Who Played D,
	27 Fouls? 0/1,
	28 Dead 0-3,
	29 Highest success,
	30 Highest attempt,
	31 Was assisted?
	*/

    public static void analyze(String rawData, CompetitionYears year) {
        //HashMap<Integer, GameData2019> teamData = new HashMap<Integer, GameData2019>();

        getGamesFromRaw(rawData, allGames, year);

        gameData.put(year, new HashMap<>());
        tempTeamData.put(year, new HashMap<>());
        fillCategories(allGames, gameData.get(year), tempTeamData.get(year));

        for (int i = gameData.get(year).keySet().size() - 1; i >= 0; i--) {
            int id = (int) gameData.get(year).keySet().toArray()[i];
            if (gameData.get(year).get(id).size() != 6) {
                for (GameData game : gameData.get(year).get(id)) {
                    tempTeamData.get(year).get(game.teamNumber).remove(game);
                    if (tempTeamData.get(year).get(game.teamNumber).size() == 0) tempTeamData.get(year).remove(game.teamNumber);
                }
                gameData.get(year).remove(id);
            }
        }

        HashMap<String, Analyze.TeamData2019> teamData = new HashMap<String, Analyze.TeamData2019>();
        for (String teamNumber : tempTeamData.get(year).keySet()) {
            teamData.put(teamNumber, new Analyze.TeamData2019(teamNumber, tempTeamData.get(year).get(teamNumber)));
        }

        HashMap<String, ArrayList<Analyze.GameData>> defendGames = new HashMap<String, ArrayList<GameData>>();

        //TODO: implement show games defended

        for (String teamNumber : teamData.keySet()) {
            double[] pointsScored = new double[teamData.get(teamNumber).gamesPlayed.size()];
            for (int gameNumber = 0; gameNumber < pointsScored.length; gameNumber++) {
                pointsScored[gameNumber] = teamData.get(teamNumber).gamesPlayed.get(gameNumber).pointsEarned;
                teamData.get(teamNumber).totalPoints += teamData.get(teamNumber).gamesPlayed.get(gameNumber).pointsEarned;
            }
            teamData.get(teamNumber).pointsSD = calculateSD(pointsScored) / Math.sqrt(teamData.get(teamNumber).totalPoints);
        }

        allGames.sort(new Comparator<Analyze.GameData>() {
            @Override
            public int compare(Analyze.GameData o1, Analyze.GameData o2) {
                return -Integer.compare(o1.pointsEarned, o2.pointsEarned);
            }
        });

        for (Analyze.TeamData2019 team : teamData.values()) {
            new BaseTeamData(team.teamNumber, year, team.allGameDatas);
        }

        System.out.println(allGames.size());
    }

    private static void getGamesFromRaw(String rawData, ArrayList<GameData> allGames, CompetitionYears year) {
        String[] eachLine = rawData.split("[\r\n]"); //.split looks for regex and not string literal so just know this line splits the string into an array where each index is a single line
        for (String line : eachLine) {
            if (line.contains("Team") || line.equals("")) continue;
            String[] values = line.split(",");
            allGames.add(new GameData(values, year));
        }
    }

    private static void fillCategories(final ArrayList<Analyze.GameData> allGames, HashMap<Integer, ArrayList<GameData>> gameData,  HashMap<String, ArrayList<GameData>> teamData) {
        boolean duplicate;
        for (Analyze.GameData game : allGames) {
            if (!gameData.containsKey(game.matchNumber)) {
                gameData.put(game.matchNumber, new ArrayList<GameData>());
            }
            duplicate = false;
            for (GameData otherGame : gameData.get(game.matchNumber)) {
                duplicate |= otherGame.teamNumber.equals(game.teamNumber) && (otherGame.matchNumber == game.matchNumber);
            }
            if (!duplicate)
                gameData.get(game.matchNumber).add(game);

            if (!teamData.containsKey(game.teamNumber)) {
                teamData.put(game.teamNumber, new ArrayList<GameData>());
            }
            duplicate = false;
            for (Analyze.GameData otherGame : teamData.get(game.teamNumber)) {
                duplicate |= otherGame.teamNumber.equals(game.teamNumber) && (otherGame.matchNumber == game.matchNumber);
            }
            if (!duplicate)
                teamData.get(game.teamNumber).add(game);
        }
    }

    public static double calculateSD(double[] numArray) {
        double sum = 0.0, standardDeviation = 0.0;
        int length = numArray.length;

        for (double num : numArray) {
            sum += num;
        }

        double mean = sum / length;

        for (double num : numArray) {
            standardDeviation += Math.pow(num - mean, 2);
        }

        return Math.sqrt(standardDeviation / length);
    }

    static class TeamData2019 {
        public String teamNumber;
        public ArrayList<GameData> gamesPlayed;
        public int totalPoints = 0;
        public double pointsSD = 0;
        public ArrayList<GameData> gamesIDefended;
        public HashMap<String, HashMap<String, String>> allGameDatas = new HashMap<String, HashMap<String, String>>();

        public TeamData2019(String teamNumber, ArrayList<GameData> gamesPlayed) {
            this.teamNumber = teamNumber;
            this.gamesPlayed = gamesPlayed;
            for (GameData game : gamesPlayed)
                this.allGameDatas.put("" + game.matchNumber, game.gameData);
        }
    }

    static class GameData {
        public String rawData;
        public CompetitionYears year;
        public HashMap<String, String> gameData = new HashMap<String, String>();
        public int pointsEarned = 0;

        public String teamNumber;
        public int matchNumber;
        /*public Analyze2019.StartingPosition2019 startingPosition;
        public boolean habLine;
        public int[] autoShipHatches;
        public int autoShipCargoSide;
        public int autoCargoLevel;
        public int[] autoRocketHatchLevels;
        public int autoDroppedHatches;
        public int autoDroppedCargo;
        public int manualShipHatches;
        public int manualShipCargo;
        public int[] manualRocketHatches;
        public int[] manualRocketCargo;
        public int manualDroppedCargo;
        public int manualDroppedHatches;
        public int defendAmt;
        public int defendEfficiency;
        public int pressureFelt;
        public String defendingTeam;
        public boolean foul;
        public int deadTime;
        public int highestMake;
        public int highestTry;


        public int pointsEarned = 0;

        public GameData2019(String[] values) {
            this.rawData = String.join(",", values);
            for (int i = 0; i < CompetitionYears.y2019.keyData.length; i++){
                gameData.put(CompetitionYears.y2019.keyData[i], values[i]);
            }
            this.teamNumber = values[0];
            this.matchNumber = (int) Math.ceil(Double.parseDouble(values[1]));
            this.startingPosition = Analyze2019.StartingPosition2019.valueOf(values[2]);
            this.habLine = values[3].equals("1");
            this.autoShipHatches = new int[]{(int) Math.ceil(Double.parseDouble(values[4])), (int) Math.ceil(Double.parseDouble(values[5]))};
            this.autoShipCargoSide = (int) Math.ceil(Double.parseDouble(values[6]));
            this.autoCargoLevel = (int) Math.ceil(Double.parseDouble(values[7]));
            this.autoRocketHatchLevels = new int[]{(int) Math.ceil(Double.parseDouble(values[8])), (int) Math.ceil(Double.parseDouble(values[9]))};
            this.autoDroppedCargo = (int) Math.ceil(Double.parseDouble(values[10]));
            this.autoDroppedHatches = (int) Math.ceil(Double.parseDouble(values[11]));
            this.manualShipHatches = (int) Math.ceil(Double.parseDouble(values[12]));
            this.manualShipCargo = (int) Math.ceil(Double.parseDouble(values[13]));
            this.manualRocketHatches = new int[]{(int) Math.ceil(Double.parseDouble(values[14])), (int) Math.ceil(Double.parseDouble(values[15])), (int) Math.ceil(Double.parseDouble(values[16]))};
            this.manualRocketCargo = new int[]{(int) Math.ceil(Double.parseDouble(values[17])), (int) Math.ceil(Double.parseDouble(values[18])), (int) Math.ceil(Double.parseDouble(values[19]))};
            this.manualDroppedCargo = (int) Math.ceil(Double.parseDouble(values[20]));
            this.manualDroppedHatches = (int) Math.ceil(Double.parseDouble(values[21]));
            this.defendAmt = (int) Math.ceil(Double.parseDouble(values[22]));
            this.defendEfficiency = (int) Math.ceil(Double.parseDouble(values[23]));
            this.pressureFelt = (int) Math.ceil(Double.parseDouble(values[24]));
            this.defendingTeam = values[25];
            this.foul = values[26].equals("1");
            this.deadTime = (int) Math.ceil(Double.parseDouble(values[27]));
            this.highestMake = (int) Math.ceil(Double.parseDouble(values[28]));
            this.highestTry = (int) Math.ceil(Double.parseDouble(values[29]));

            evaluateScore();
        }*/

        public GameData(String[] values, CompetitionYears year) {
            this.year = year;
            this.rawData = String.join(",", values);
            this.matchNumber = (int)Math.ceil(Double.parseDouble(values[1]));
            this.teamNumber = values[0];
            for (int i = 0; i < year.keyData.length; i++) {
                gameData.put(year.keyData[i], values[i]);
            }
        }

        private void evaluateScore() {

        }
    }



    enum StartingPosition2019 {
        L1(3), L2(6), R1(3), R2(6), M1(3), M(0);

        public final int pointValue;

        StartingPosition2019(int pointValue) {
            this.pointValue = pointValue;
        }
    }
}
