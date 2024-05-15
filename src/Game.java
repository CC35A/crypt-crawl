import vector.Vector2;

import java.awt.event.KeyEvent;
import java.util.*;

public class Game {
    public Camera camera;
    private List<Tile> tilesEnv = new ArrayList<Tile>() {};
    private List<Tile> tilesFor = new ArrayList<Tile>() {};
    private Map<Integer, Vector2> keyDirections; // Map key codes to movement directions
    private Vector2 currentMovement;
    private Set<Integer> currentKeys = new HashSet<>();

    private ChunkLoader chunkLoader;

    public Game() {
        genererateWorldMap();
        this.camera = new Camera(new Vector2(0, 0));
        initializeKeyDirections();
        this.currentMovement = new Vector2(0, 0);
        this.chunkLoader = new ChunkLoader(this.camera);
    }

    public void update(){
        updateCameraPosition();
        chunkLoader.update();
    }

    private void genererateWorldMap(){
        this.tilesEnv.add(new Tile(new Vector2(1, 1), 0));
        this.tilesEnv.add(new Tile(new Vector2(0, 1), 0));
        this.tilesEnv.add(new Tile(new Vector2(0, 0), 0));

        this.tilesFor.add(new Tile(new Vector2(1, 1), 1));
    }

    public List<Tile> getTilesEnv() {
        return tilesEnv;
    }

    public List<Tile> getTilesFor() {
        return tilesFor;
    }

    // Initialize key directions map
    private void initializeKeyDirections() {
        keyDirections = new HashMap<>();
        keyDirections.put(KeyEvent.VK_W, new Vector2(0, -1));
        keyDirections.put(KeyEvent.VK_A, new Vector2(-1, 0));
        keyDirections.put(KeyEvent.VK_S, new Vector2(0, 1));
        keyDirections.put(KeyEvent.VK_D, new Vector2(1, 0));
    }

    // Handle keyboard events for camera movement
    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();
        Vector2 direction = keyDirections.get(key);

        // If the pressed key corresponds to a direction and it's not already being pressed, update current movement
        if (direction != null && !currentKeys.contains(key)) {
            currentKeys.add(key);  // Add the key to the list of currently pressed keys
            currentMovement = currentMovement.add(direction);
        }
    }

    public void keyReleased(KeyEvent e) {
        int key = e.getKeyCode();
        Vector2 direction = keyDirections.get(key);

        // If the released key corresponds to a direction, update current movement
        if (direction != null) {
            currentKeys.remove(key);  // Remove the key from the list of currently pressed keys
            currentMovement = currentMovement.subtract(direction);
        }
    }

    // Update camera position based on current movement
    private void updateCameraPosition() {
        camera.move(currentMovement.normalize().scale(Config.SPEED));
    }
}
