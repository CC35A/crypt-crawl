import vector.Vector2;

import java.io.File;
import java.util.ArrayList;
import java.util.Scanner;

public class Chunk {
    public final Vector2 pos;
    ArrayList<Tile> mapEnv = new ArrayList<>();
    ArrayList<Tile> mapFor = new ArrayList<>();

    public Chunk(Vector2 pos, ArrayList<Tile> mapEnv, ArrayList<Tile> mapFor) {
        this.pos = pos;
        this.mapEnv = mapEnv;
        this.mapFor = mapFor;
    }
}
