public class Config {
    public static final int TILE_SIZE = 64;
    public static final String GAME_TITLE = "Cryptcrawl";
    public static final double SPEED = 7;
    public static final int CHUNK_SIZE = 16;
    public static final int VISIBILITY_RANGE = 2;

    // Prevent instantiation
    private Config() {
        throw new AssertionError("Cannot instantiate Config class");
    }
}
