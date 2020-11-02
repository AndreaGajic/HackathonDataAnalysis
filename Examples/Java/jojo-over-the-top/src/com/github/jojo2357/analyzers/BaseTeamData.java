package src.com.github.jojo2357.analyzers;

import src.com.github.jojo2357.scarystuff.RenderableObject;
import src.com.github.jojo2357.scarystuff.TextRenderer;
import src.com.github.jojo2357.scarystuff.events.EventPriorities;
import src.com.github.jojo2357.scarystuff.events.EventTypes;
import src.com.github.jojo2357.scarystuff.events.events.MouseInputEvent;
import src.com.github.jojo2357.scarystuff.graphics.Dimensions;
import src.com.github.jojo2357.scarystuff.graphics.IRecievesEvent;
import src.com.github.jojo2357.scarystuff.graphics.Point;
import src.com.github.jojo2357.scarystuff.typeface.Colors;

import java.util.HashMap;

public class BaseTeamData extends RenderableObject implements IRecievesEvent {
    //get(team#).get(year).get(gameNumber).get(item)
    public static HashMap<String, HashMap<CompetitionYears, HashMap<String, HashMap<String, String>>>> allTeamData = new HashMap<>();
    public static HashMap<String, BaseTeamData> teamRegistrar = new HashMap<>();
    public static MouseInputEvent lastMouseEvent = new MouseInputEvent();

    public String teamNumber;
    public Point location = new Point();
    public Colors color = Colors.WHITE;
    public Dimensions dimensions;

    public boolean menuOpen = false;
    private CompetitionYears yearSelected = null;
    private String selectedCategory = null;

    public BaseTeamData(String teamNumber, CompetitionYears year, HashMap<String, HashMap<String, String>> data) {
        if (teamRegistrar.containsKey(teamNumber)) {
            teamRegistrar.get(teamNumber).addData(year, data);
            return;
        }
        this.registerAllListeners();
        this.dimensions = new Dimensions(teamNumber.length() * 20 + 10, 20);
        this.teamNumber = teamNumber;
        allTeamData.put(teamNumber, new HashMap<>());
        allTeamData.get(teamNumber).put(year, data);
        if (!teamRegistrar.containsKey(teamNumber)) teamRegistrar.put(teamNumber, this);
    }

    public void addData(CompetitionYears year, HashMap<String, HashMap<String, String>> data) {
        allTeamData.get(this.teamNumber).put(year, data);
    }

    public boolean render() {
        return this.render(this.location);
    }

    public boolean render(Point point) {
        TextRenderer.render(this.teamNumber, this.color, point, 1000);
        return false;
    }

    private void promptYearSelect() {
        Point startingPoint = new Point(50, 50);
        TextRenderer.render("Select a year", Colors.WHITE, startingPoint, 1000);
        for (CompetitionYears year : CompetitionYears.values()) {
            if (!allTeamData.get(this.teamNumber).containsKey(year)) continue;
            startingPoint.stepY(50);
            TextRenderer.render(year.yearNumber, Colors.WHITE, startingPoint, 1000);
        }
    }

    private void promptCategorySelect() {
        Point startingPoint = new Point(50, 50);
        TextRenderer.render("Select a category", Colors.WHITE, startingPoint, 1000);
        for (String category : this.yearSelected.keyData) {
            startingPoint.stepY(50);
            if (startingPoint.getY() > 1100) {
                startingPoint.setY(50);
                startingPoint.stepX(600);
            }
            TextRenderer.render(category, Colors.WHITE, startingPoint, 1000);
        }
    }

    public void printAllGames() {
        if (yearSelected == null) {
            promptYearSelect();
            return;
        }
        if (selectedCategory == null) {
            promptCategorySelect();
            return;
        }
        Point startingPoint = new Point(50, 50);
        TextRenderer.render(selectedCategory, Colors.WHITE, startingPoint, 1000);
        for (HashMap<String, String> game : allTeamData.get(this.teamNumber).get(yearSelected).values()) {
            startingPoint.stepY(50);
            try {
                TextRenderer.render(game.get(selectedCategory), Colors.WHITE, startingPoint, 1000);
            }catch(Exception e){
                throw new RuntimeException("Error occured with " + selectedCategory);
            }
        }
    }

    @Override
    public void tick() {

    }

    @Override
    public boolean handleMouseInput(MouseInputEvent event) {
        int lastClick = -1;
        for (int mouseChecker = 0; mouseChecker <= 2; mouseChecker++) {
            if ((event.getRawData() & (4 >> mouseChecker)) == 4 >> mouseChecker) {
                lastClick = 2 - mouseChecker;
            }
        }
        if (this.menuOpen) {
            if (this.yearSelected == null && this.selectedCategory == null && lastClick == 1 && lastMouseEvent.getRawData() == 0) {
                lastMouseEvent = event;
                this.menuOpen = false;
                return true;
            }
            if (this.yearSelected != null && this.selectedCategory == null && lastClick == 1 && lastMouseEvent.getRawData() == 0) {
                lastMouseEvent = event;
                this.yearSelected = null;
                return true;
            }
            if (this.yearSelected == null && lastClick == 0 && lastMouseEvent.getRawData() == 0) {
                lastMouseEvent = event;
                Point checkPos = new Point(50, 100);
                for (CompetitionYears year : CompetitionYears.values()) {
                    if (!allTeamData.get(this.teamNumber).containsKey(year))
                        continue;
                    if (event.getPosition().isInBoundingBox(checkPos.copy().stepX(year.yearNumber.length() * 10), new Dimensions(year.yearNumber.length() * 20 + 10, 30), 0.5f)) {
                        this.yearSelected = year;
                    }
                    checkPos.stepY(50);
                    if (checkPos.getY() > 1100) {
                        checkPos.setY(50);
                        checkPos.stepX(600);
                    }
                }
                return true;
            }
            if (this.selectedCategory != null && lastClick == 1 && lastMouseEvent.getRawData() == 0) {
                lastMouseEvent = event;
                this.selectedCategory = null;
                return true;
            }
            if (this.selectedCategory == null && lastClick == 0 && lastMouseEvent.getRawData() == 0) {
                lastMouseEvent = event;
                Point checkPos = new Point(50, 100);
                for (String category : this.yearSelected.keyData) {
                    if (event.getPosition().isInBoundingBox(checkPos.copy().stepX(category.length() * 10), new Dimensions(category.length() * 20 + 10, 30), 0.5f)) {
                        this.selectedCategory = category;
                    }
                    checkPos.stepY(50);
                    if (checkPos.getY() > 1100) {
                        checkPos.setY(50);
                        checkPos.stepX(600);
                    }
                }
                return true;
            }
            this.printAllGames();
            lastMouseEvent = event;
            return true;
        }
        lastMouseEvent = event;
        if (event.getPosition().isInBoundingBox(this.location.copy().stepX(this.dimensions.getWidth() / 2).stepY(/*-this.dimensions.getHeight()/2*/ 0), this.dimensions, 0.5f)) {
            this.color = Colors.YELLOW;
            System.out.println(this.location.toString() + ": " + this.dimensions.toString());
            if (event.getRawData() == 0) return false;
            if (lastClick == 0) return this.menuOpen = true;
        } else {
            this.color = Colors.WHITE;
        }
        return false;
    }

    @Override
    public EventPriorities getPrio(int id) {
        if ((EventTypes.MouseInputEvent.getId() == id || EventTypes.RenderEvent.getId() == id) && this.menuOpen) {
            return EventPriorities.MIDDLE;
        }
        return EventPriorities.LOWEST;
    }
}
