package src.com.github.jojo2357.analyzers;

import src.com.github.jojo2357.scarystuff.RenderableObject;
import src.com.github.jojo2357.scarystuff.TextRenderer;
import src.com.github.jojo2357.scarystuff.events.EventBase;
import src.com.github.jojo2357.scarystuff.events.EventPriorities;
import src.com.github.jojo2357.scarystuff.events.events.MouseInputEvent;
import src.com.github.jojo2357.scarystuff.graphics.Dimensions;
import src.com.github.jojo2357.scarystuff.graphics.IRecievesEvent;
import src.com.github.jojo2357.scarystuff.graphics.Point;
import src.com.github.jojo2357.scarystuff.typeface.Colors;

import java.util.ArrayList;
import java.util.HashMap;

public class BaseTeamData extends RenderableObject implements IRecievesEvent {
    public static HashMap<String, HashMap<CompetitionYears, HashMap<String, HashMap<String, String>>>> allTeamData = new HashMap<>();
    public static HashMap<String, BaseTeamData> teamRegistrar = new HashMap<>();
    //get(team#).get(year).get(gameNumber).get(item)

    public String teamNumber;
    public Point location = new Point();
    public Colors color = Colors.WHITE;
    public Dimensions dimensions;

    public BaseTeamData(String teamNumber, CompetitionYears year, HashMap<String, HashMap<String, String>> data){
        if (teamRegistrar.containsKey(teamNumber)) {
            teamRegistrar.get(teamNumber).addData(year, data);
            return;
        }
        this.registerAllListeners();
        this.dimensions = new Dimensions(teamNumber.length() * 20 + 10, 20);
        this.teamNumber = teamNumber;
        allTeamData.put(teamNumber, new HashMap<>());
        allTeamData.get(teamNumber).put(year, data);
        if (!teamRegistrar.containsKey(teamNumber))
            teamRegistrar.put(teamNumber, this);
    }

    public void addData(CompetitionYears year, HashMap<String, HashMap<String, String>> data){
        allTeamData.get(this.teamNumber).put(year, data);
    }

    public void render(){
        this.render(this.location);
    }

    public void render(Point point){
        TextRenderer.render(this.teamNumber, this.color, point, 1000);
    }

    @Override
    public void tick() {

    }

    @Override
    public boolean handleMouseInput(MouseInputEvent event) {
        if (event.getPosition().isInBoundingBox(this.location.copy().stepX(this.dimensions.getWidth()/2).stepY(/*-this.dimensions.getHeight()/2*/ 0), this.dimensions, 0.5f)) {
            this.color = Colors.YELLOW;
            System.out.println(this.location.toString() + ": " + this.dimensions.toString());
        }else
            this.color = Colors.WHITE;
        return false;
    }

    @Override
    public EventPriorities getPrio(int id) {
        return EventPriorities.LOWEST;
    }
}
