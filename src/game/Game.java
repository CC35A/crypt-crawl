package game;

import vector.Vector2;

import java.awt.event.KeyEvent;
import java.util.*;

public class Game {
    public Camera camera;

    public double deltaTime;
    public double longestDeltaTime;
    private List<Tile> tilesEnv = new ArrayList<Tile>() {};
    private List<Tile> tilesFor = new ArrayList<Tile>() {};
    private Map<Integer, Vector2> keyDirections; // Map key codes to movement directions
    public Vector2 currentMovement; // TODO make private
    private Set<Integer> currentKeys = new HashSet<>();

    public ChunkLoader chunkLoader; // TODO set private
    public final Player player;
    private long lastUpdateTime;
    public ArrayList<GameObject> gameObjects = new ArrayList<>();

    public Game() {
        this.player = new Player(new Vector2(0, 0), this);
        this.gameObjects.add(this.player);
        this.camera = new Camera(new Vector2(0, 0));
        initializeKeyDirections();
        this.currentMovement = new Vector2(0, 0);
        this.chunkLoader = new ChunkLoader(this.camera, this);
        this.lastUpdateTime = System.nanoTime(); // Initialize the last update time
        //HeapDump.dumpHeap("heapdump.hprof", true); // TODO for testing, remove in prod
        updateWorldMap();
    }

    public void update(){
        long currentTime = System.nanoTime();
        deltaTime = (currentTime - lastUpdateTime) / 1e9; // Convert nanoseconds to seconds
        if(deltaTime > longestDeltaTime) longestDeltaTime = deltaTime;
        lastUpdateTime = currentTime;
        //updateCameraPosition(deltaTime);
        updateWorldMap();

        for (GameObject obj : gameObjects) {
            obj.update(deltaTime);
        }

        handleCollision();
    }

    public boolean checkForCollision(GameObject actor) {
        List<GameObject> sourceObjects = new ArrayList<>(gameObjects);
        sourceObjects.addAll(tilesEnv);
        sourceObjects.addAll(tilesFor);
        sourceObjects.removeIf(obj -> obj.collider == null);

        for (GameObject other : sourceObjects) {
            if (actor == other || actor.collider == null || other.collider == null) continue;
            Vector2 mtv = CollisionDetection.checkCollision(actor, other);
            System.out.println(mtv);
            if(mtv != null) {
                return true;
            }
        }
        return false;
    }

    private void handleCollision() {
        List<List<GameObject>> combinations = getAllCombinations(gameObjects);
        for (List<GameObject> objs : combinations) {
            GameObject actor = objs.get(0);
            GameObject other = objs.get(1);
            if (actor == other || actor.collider == null || other.collider == null) continue;
            Vector2 mtv = CollisionDetection.checkCollision(actor, other);
            if (mtv != null) {
                actor.onCollision(other, mtv);
                other.onCollision(actor, mtv);
            }
        }
    }

    private List<List<GameObject>> getAllCombinations(List<GameObject> gameObjects) {
        List<List<GameObject>> combinations = new ArrayList<>();
        List<GameObject> sourceObjects = new ArrayList<>(gameObjects);
        sourceObjects.addAll(tilesEnv);
        sourceObjects.addAll(tilesFor);
        sourceObjects.removeIf(obj -> obj.collider == null);

        // Iterate through each pair of GameObjects
        for (int i = 0; i < sourceObjects.size(); i++) {
            for (int j = i + 1; j < sourceObjects.size(); j++) {
                List<GameObject> pair = new ArrayList<>();
                pair.add(sourceObjects.get(i));
                pair.add(sourceObjects.get(j));
                if (pair.get(0).pos.subtract(pair.get(1).pos).magnitude() > 4) continue; // TODO to properly
                combinations.add(pair);
            }
        }

        return combinations;
    }

    public void updateWorldMap() {
        if(!chunkLoader.hasLoadedChunks) return;
        System.out.println("updating map"); // TODO optimize, to make less calls
        List<Tile> tmpEnv = new ArrayList<Tile>() {};
        ArrayList<Chunk> tmpChunks = new ArrayList<>(chunkLoader.chunks);
        for (Chunk chunk : tmpChunks) {
            tmpEnv.addAll(chunk.mapEnv);
        }
        this.tilesEnv = tmpEnv;
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
    private void updateCameraPosition(double deltaTime) {
        camera.move(currentMovement.normalize().scale(Config.SPEED * deltaTime));
    }
}
