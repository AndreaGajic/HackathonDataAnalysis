package src.com.github.jojo2357.scarystuff.events;

public enum EventPriorities {
    HIGHEST(2),
    MIDDLE(1),
    LOWEST(0);

    public int id = -1;

    EventPriorities(int id){
        this.id = id;
    }
}
