package GiantsSidescroller.src;

public class World {
    private static World theWorld;
    private Map theMap;

    private World()
    {
        theMap = new Map();
        System.out.println("Map");
    }

    /**
     * Ensures there can only be one world
     * @return
     */
    public static World getWorld() {
        if (theWorld == null)
        {
            theWorld = new World();
        }
        return theWorld;
    }

    public Map getMap() {
        return this.theMap;
    }
}