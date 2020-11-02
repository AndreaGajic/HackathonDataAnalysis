package src.com.github.jojo2357.scarystuff.events;

import src.com.github.jojo2357.analyzers.BaseTeamData;
import src.com.github.jojo2357.scarystuff.RenderableObject;
import src.com.github.jojo2357.scarystuff.TextRenderer;
import src.com.github.jojo2357.scarystuff.events.events.RenderEvent;
import src.com.github.jojo2357.scarystuff.events.events.TickEvent;
import src.com.github.jojo2357.scarystuff.graphics.IRecievesEvent;
import src.com.github.jojo2357.scarystuff.graphics.Point;
import src.com.github.jojo2357.scarystuff.graphics.ScreenManager;

import java.util.ArrayList;
import java.util.List;

public class EventManager {
    private static final List<EventBase> events = new ArrayList<EventBase>();
    private static final List<List<IRecievesEvent>> registeredListeners = new ArrayList<List<IRecievesEvent>>();
    private static final List<RenderableObject> renderableObjects = new ArrayList<RenderableObject>();

    public static GameTimes currentPhase = GameTimes.WAITING;
    private static final TickEvent staticTickEvent = new TickEvent();

    private static boolean configured = false;

    public static void init() {
        for (int fillRegisteredListener = 0; fillRegisteredListener < EventTypes.values().length; fillRegisteredListener++) {
            registeredListeners.add(new ArrayList<IRecievesEvent>());
        }
        configured = true;
    }

    public static <T extends EventBase> boolean addEvent(T event) {
        //System.out.println(event.getEventType().getName() + " gotten!");
        if (event instanceof RenderEvent) {
            boolean toClose = ScreenManager.tick();
            ScreenManager.drawBox(new Point(10, 10), new Point(30, 30), 255, 255, 255);
            for (GameTimes gameTime : GameTimes.values()) {
                if (gameTime.name().contains("RENDER")) {
                    currentPhase = gameTime;
                    events.add(event);
                    EventManager.sendEvents();
                }
            }
            ScreenManager.finishRender();
            currentPhase = GameTimes.WAITING;
            return toClose;
        } else {
            events.add(event);
        }
        return false;
    }

    public static <T extends IRecievesEvent> void addListeningObject(T listeningObject, EventTypes eventToListenFor) {
        if (!configured) init();
        registeredListeners.get(eventToListenFor.getId()).add(listeningObject);
    }

    public static <T extends IRecievesEvent> void addListeningObject(T listeningObject, EventBase eventToListenFor) {
        registeredListeners.get(eventToListenFor.getEventType().getId()).add(listeningObject);
    }

    public static void sendEvents() {
        int maxLoops = 10 * events.size();
        int loopsMade = 0;
        boolean terminateLoops = false;
        while (events.size() > 0) {
            if (events.get(0).getEventType() != EventTypes.RenderEvent) {
                allFors:
                {// cant use for...each because events.remove(0); crashes it
                    for (int eventLooper = 0; eventLooper < EventPriorities.values().length; eventLooper++) {
                        EventPriorities prio = EventPriorities.values()[eventLooper];
                        for (int roomLooper = 0; roomLooper < registeredListeners.get(events.get(0).getEventType().getId()).size(); roomLooper++) {
                            IRecievesEvent room = registeredListeners.get(events.get(0).getEventType().getId()).get(roomLooper);
                            if (room.getPrio(events.get(0).getEventType().getId()) == prio && room.notify(events.get(0))) {
                                break allFors;
                            }
                        }
                    }
                }
            } else {
                for (RenderableObject obj : renderableObjects) {
                    if (obj.render()) break;
                }
                drawNumbers();
            }
            events.remove(0);
            loopsMade++;
            if (loopsMade > maxLoops)// debug because reasons
            {
                throw new RuntimeException("Infinite loop?");
            }
        }
    }

    public static void sendTickEvent() {
        if (!configured) EventManager.init();
        currentPhase = GameTimes.TICK;
        for (IRecievesEvent room : registeredListeners.get(EventTypes.TickEvent.getId())) {
            room.notify(staticTickEvent);
        }
    }

    public static <T extends RenderableObject> void addRenderingObject(T object) {
        EventManager.renderableObjects.add(object);
    }

    private static void drawNumbers() {
        Point drawSpot = new Point(50, 50);
        for (EventPriorities prio : EventPriorities.values()) {
            for (BaseTeamData team : BaseTeamData.teamRegistrar.values()) {
                if (team.getPrio(EventTypes.RenderEvent.getId()) == prio) {
                    if (prio == EventPriorities.MIDDLE){
                        team.printAllGames();
                        return;
                    }
                    TextRenderer.render(team.teamNumber, team.color, drawSpot, 1000);
                    team.location = drawSpot.copy();
                    drawSpot.stepX(team.teamNumber.length() * 20 + 10);
                    if (drawSpot.getX() > 1100) {
                        drawSpot.stepX(50 - (int) drawSpot.getX());
                        drawSpot.stepY(50);
                    }
                }
            }
        }
    }
}
