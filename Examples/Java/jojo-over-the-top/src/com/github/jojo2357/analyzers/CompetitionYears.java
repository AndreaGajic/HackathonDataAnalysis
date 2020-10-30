package src.com.github.jojo2357.analyzers;

public enum CompetitionYears {
    y2019(new String[]{"Team Number","Match Number","Starting Position","Hab Line","Auto # H Ship Side","Auto # H Ship Front","Auto # C Ship Side","Auto C Rkt Lvl","Auto H Rkt Lvl","Auto H Rkt Lvl [2]","Auto # C Dropped","Auto # H Dropped","Tele H Ship","Tele C Ship","Tele Rocket H L1","Tele Rocket H L2","Tele Rocket H L3","Tele Rocket C L1","Tele Rocket C L2","Tele Rocket C L3","Tele Dropped C","Tele Dropped H","Def pl amt","Def pl quality","Def rec amt","Who Played D","Fouls","Dead","Highest success","Highest attempt","Was assisted"});

    public String[] keyData;

    CompetitionYears(String[] gameKey){
        this.keyData = gameKey;
    }
}
