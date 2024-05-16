import vector.Vector2;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class ChunkLoader{
    private Camera camera;
    private final String chunkPath = "./src/mapdata/";
    private String world;
    public HashMap<Integer, Vector2> loadedChunks = new HashMap<>(); // TODO set private?
    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
    public ArrayList<Chunk> chunks = new ArrayList<>();
    public boolean hasLoadedChunks;
    private Game game;

    public ChunkLoader(Camera camera, Game game){
        this.game = game;
        this.camera = camera;
        this.loadedChunks = new HashMap<>();
        this.world = "overworld"; // TODO do properly
        scheduler.scheduleAtFixedRate(this::update, 0, 16, TimeUnit.MILLISECONDS);
        update();
    }

    //public void run() {
    //    while (isAlive()) {
    //        update();
    //    }
    //}

    public void update() {
        Vector2 camChunkPos = camera.chunkPos;
        hasLoadedChunks = false;

        for (int dx = -Config.SIMULATION_DISTANCE; dx <= Config.SIMULATION_DISTANCE; dx++) {
            for (int dy = -Config.SIMULATION_DISTANCE; dy <= Config.SIMULATION_DISTANCE; dy++) {
                Vector2 dv = new Vector2(dx, dy);
                Vector2 chunkPosition = camera.chunkPos.round().add(dv);
                double distance = camChunkPos.subtract(chunkPosition).magnitude();
                if (distance <= Config.SIMULATION_DISTANCE) {
                    if (!loadedChunks.containsKey(chunkPosition.hashCode())) {
                        System.out.println("Loading chunk" + chunkPosition + " at distance " + distance);
                        loadChunk(chunkPosition);
                        hasLoadedChunks = true;
                    }
                }
            }
        }

        Iterator<Vector2> iterator = loadedChunks.values().iterator();
        while (iterator.hasNext()) {
            Vector2 chunkPosition = iterator.next();
            double distance = camChunkPos.subtract(chunkPosition).magnitude();
            if(Config.SIMULATION_DISTANCE < distance) {
                if (loadedChunks.containsKey(chunkPosition.hashCode())) {
                    iterator.remove();
                    System.out.println("Unloading chunk " + chunkPosition + " at distance " + distance);
                    loadedChunks.remove(chunkPosition.hashCode());
                    chunks.removeIf(c -> c.pos.equals(chunkPosition));
                }
            }
        }
        //System.out.println(loadedChunks.size());
    }

    private void loadChunk(Vector2 chunkPosition) {
        loadedChunks.put(chunkPosition.hashCode(), chunkPosition);

        ArrayList<Tile> mapEnv = new ArrayList<>();
        ArrayList<Tile> mapFor = new ArrayList<>();
        String chunkID = (int) chunkPosition.x + "-" + (int) chunkPosition.y;
        String filePath = chunkPath + this.world + "/chunk-" + chunkID + ".dat";


        File file = new File(filePath);
        if (!file.exists()) {
            System.out.println("File does not exist " + filePath);
            filePath = chunkPath + world + "/default.dat";
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            int counter = 0;
            while ((line = reader.readLine()) != null && counter < 16) {
                String[] tokens = line.split(",");
                for (int i = 0; i < Math.min(tokens.length, Config.CHUNK_SIZE); i++) {
                    int id = Integer.parseInt(tokens[i].trim());
                    Vector2 tilePos = new Vector2((chunkPosition.x * Config.CHUNK_SIZE) + i, (chunkPosition.y * Config.CHUNK_SIZE) + counter);
                    mapEnv.add(new Tile(tilePos, id));
                }
                counter++;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        chunks.add(new Chunk(chunkPosition, mapEnv, mapFor)); // TODO add logic for mapFor
    }



}