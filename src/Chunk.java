import vector.Vector2;

import java.io.File;
import java.util.Scanner;

public class Chunk {
    public final Vector2 pos;
    private final String chunkPath = "./src/mapdata/";
    private final Game game;
    private final String world;
    public Tile[][][] map = new Tile[16][16][2];

    public Chunk(Vector2 pos, Tile[][][] map, Game game, String world) {
        this.pos = pos;
        this.world = world;
        this.game = game;
        this.map = map;
    }

    private Chunk readChunk(String chunkID) {
        try {
            File file = new File(chunkPath + "/chunk-" + chunkID + ".dat");
            if (!file.exists()) {
                file = new File(chunkPath + world + "/default.dat");
            }
            Scanner reader = new Scanner(file);
            int counter = 0;
            while (reader.hasNextLine()) {
                String data = reader.nextLine();
                String[] string = data.replaceAll(" ", "").split(",");
                for (int i = 0; i < string.length; i++) {
                    int id = Integer.parseInt(string[i]);
                    //System.out.println(id);
                    Vector2 tilePos = new Vector2((this.pos.x * 16) + i, (this.pos.y * 16) + counter);
                    map[counter][i][0] = new Tile(tilePos, id);
                }
                counter++;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
