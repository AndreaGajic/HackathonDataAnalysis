package src.com.github.jojo2357.scarystuff.events.events;

import src.com.github.jojo2357.scarystuff.events.EventBase;
import src.com.github.jojo2357.scarystuff.events.EventTypes;
import src.com.github.jojo2357.scarystuff.graphics.Point;

public class MouseInputEvent extends EventBase {
    private final Point mouseLocation;
    private final byte buttonsData;

    public MouseInputEvent(Point point, byte mouseButtonData) {
        super(EventTypes.MouseInputEvent);
        this.mouseLocation = point;
        this.buttonsData = mouseButtonData;
    }

    public MouseInputEvent() {
        this(new Point(0, 0), (byte)0);
    }

    public byte getRawData(){
        return this.buttonsData;
    }

    public Point getPosition(){
        return this.mouseLocation;
    }

    @Override
    public boolean equals(Object other) {
        if (other == null || !(other instanceof MouseInputEvent))
            return false;
        return ((MouseInputEvent) other).mouseLocation.equals(this.mouseLocation) && ((MouseInputEvent) other).buttonsData == this.buttonsData;
    }
}
