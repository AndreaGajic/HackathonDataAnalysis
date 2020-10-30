package src.com.github.jojo2357.scarystuff.events.events;

import src.com.github.jojo2357.scarystuff.events.EventBase;
import src.com.github.jojo2357.scarystuff.events.EventTypes;

public class TickEvent extends EventBase {
    private static int tickNumber = 0;
    private final int myTickNumber;

    public TickEvent(){
        super(EventTypes.TickEvent);
        this.myTickNumber = ++tickNumber;
    }
}
