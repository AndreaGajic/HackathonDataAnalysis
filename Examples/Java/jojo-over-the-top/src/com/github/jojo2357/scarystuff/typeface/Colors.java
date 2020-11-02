package src.com.github.jojo2357.scarystuff.typeface;

public enum Colors {
    YELLOW("yellow", 0),
    WHITE("white", 1),
    GREEN("green", 2),
    BLUE("blue", 3);

    private final String name;
    private final int valueOf;

    public static int size = 0;

    Colors(String name, int val){
        this.name = name;
        this.valueOf = val;
    }

    public String getName(){
        return this.name;
    }

    //@return ID for this color
    public int valueOf() {
        return this.valueOf;
    }
}
