package GiantsSidescroller.src;

public class World {
    private static World theWorld;
    private Controller theController;

    private World()
    {
        theController = new Controller();
        System.out.println("Controller");
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

    public Controller getController() {
        return this.theController;
    }
}
