import vector.Vector2;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class ChunkLoader {
    private Camera camera;
    private HashMap<Integer, Vector2> loadedChunks = new HashMap<>();

    public ChunkLoader(Camera camera){
        this.camera = camera;
        this.loadedChunks = new HashMap<>();
    }

    public void update() {
        Vector2 cameraPosition = camera.pos;

        Vector2 cameraChunkPos = cameraPosition.scale(1f / Config.CHUNK_SIZE).round();

        for (int dx = -Config.VISIBILITY_RANGE; dx <= Config.VISIBILITY_RANGE; dx++) {
            for (int dy = -Config.VISIBILITY_RANGE; dy <= Config.VISIBILITY_RANGE; dy++) {
                Vector2 dv = new Vector2(dx, dy);
                Vector2 chunkPosition = cameraChunkPos.round().add(dv);
                if(!loadedChunks.containsKey(chunkPosition.hashCode())){
                    loadChunk(chunkPosition);
                }
            }
        }

        Iterator<Vector2> iterator = loadedChunks.values().iterator();
        while (iterator.hasNext()) {
            Vector2 chunkPosition = iterator.next();
            double distance = cameraPosition.subtract(chunkPosition).magnitude();
            if(distance > Config.VISIBILITY_RANGE * Config.CHUNK_SIZE){
                iterator.remove();
                System.out.println("Unloading chunk" + chunkPosition);
                loadedChunks.remove(chunkPosition.hashCode());
                // TODO unload chunk
            }
        }
    }

    private void loadChunk(Vector2 chunkPosition){
        loadedChunks.put(chunkPosition.hashCode(), chunkPosition);
        System.out.println("Loading chunk" + chunkPosition);
        // TODO load chunks
    }
}
