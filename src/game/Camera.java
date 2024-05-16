package game;

import vector.Vector2;

public class Camera {
    public Vector2 pos;
    public Vector2 chunkPos;

    public Camera(Vector2 pos) {
        this.pos = pos;
        move(new Vector2(0, 0)); // initialize chunkPos
    }

    public void move(Vector2 offset) {
        this.pos.x += offset.x;
        this.pos.y += offset.y;


        if(this.pos.x < 0) this.pos.x = 0;
        if(this.pos.y < 0) this.pos.y = 0;
        this.chunkPos = pos.scale(1f / Config.CHUNK_SIZE).round();
        //System.out.println(this.chunkPos + "; " + this.pos);
    }

    public Vector2 getTopLeft() {
        double screenWidthInTiles = Config.WINDOW_WIDTH / Config.TILE_SIZE;
        double screenHeightInTiles = Config.WINDOW_HEIGHT / Config.TILE_SIZE;
        return pos.subtract(new Vector2(screenWidthInTiles / 2, screenHeightInTiles / 2));
    }
}
