package src.com.github.jojo2357.scarystuff.graphics;

import src.com.github.jojo2357.scarystuff.events.EventBase;
import src.com.github.jojo2357.scarystuff.events.EventManager;
import src.com.github.jojo2357.scarystuff.events.EventPriorities;
import src.com.github.jojo2357.scarystuff.events.EventTypes;
import src.com.github.jojo2357.scarystuff.events.events.MouseInputEvent;

import java.util.ArrayList;

public interface IRecievesEvent {
    default <T extends EventBase> boolean notify(T event) {
        switch (event.getEventType()) {
            case TickEvent:
                this.tick();
                break;
            case MouseInputEvent:
                return this.handleMouseInput((MouseInputEvent) event);
        }
        return false;
    }

    void tick();
    boolean handleMouseInput(MouseInputEvent event);

    default void registerAllListeners(){
        for (EventTypes event : EventTypes.values())
            registerListener(event);
    }

    default void registerListeners(ArrayList<EventBase> events){
        for (EventBase event : events)
            registerListener(event.getEventType());
    }

    default void registerListener(EventTypes event){
        EventManager.addListeningObject(this, event);
    }

    EventPriorities getPrio(int id);
}
