package src.com.github.jojo2357.scarystuff.events;

public abstract class EventBase {
    private final EventTypes eventType;

    public EventBase(EventTypes eventType) {
        this.eventType = eventType;
    }

    public EventBase() {
        this(EventTypes.EmptyEvent);
    }

    public EventTypes getEventType() {
        return this.eventType;
    }
}
