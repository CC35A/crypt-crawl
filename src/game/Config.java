package game;

public class Config {
    public static final int WINDOW_WIDTH = 1600;
    public static final int WINDOW_HEIGHT = 900;
    public static final int TILE_SIZE = 64;
    public static final String GAME_TITLE = "Cryptcrawl";
    public static final double SPEED = 7;
    public static final int CHUNK_SIZE = 16;
    public static final int SIMULATION_DISTANCE = 2;
    public static final int RENDER_DISTANCE = 2;
    public static final int X_OFFSET = 40; // Calculated for specific resolution
    public static final int Y_OFFSET = 50; // Calculated for specific resolution

    // Prevent instantiation
    private Config() {
        throw new AssertionError("Cannot instantiate Config class");
    }
}
