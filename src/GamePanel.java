import vector.Vector2;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;

public class GamePanel extends JPanel {
    private Game game;

    public GamePanel(Game game){
        this.game = game;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        drawGraphics(g);
    }

    private void drawGraphics(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        Camera camera = game.camera;

        // Translate the graphics context based on the camera's top-left position
        Vector2 cameraTopLeft = game.camera.getTopLeft().scale(Config.TILE_SIZE);
        g2d.translate(-(cameraTopLeft.x + Config.X_OFFSET), -(cameraTopLeft.y + Config.Y_OFFSET));

        // Draw environment tiles
        for (Tile tile : game.getTilesEnv()) {
            Vector2 drawPosition = tile.pos.scale(Config.TILE_SIZE);
            g2d.drawImage(tile.img, (int) drawPosition.x, (int) drawPosition.y, Config.TILE_SIZE, Config.TILE_SIZE, null);
        }

        // Draw foreground tiles
        for (Tile tile : game.getTilesFor()) {
            Vector2 drawPosition = tile.pos.scale(Config.TILE_SIZE);
            g2d.drawImage(tile.img, (int) drawPosition.x, (int) drawPosition.y, Config.TILE_SIZE, Config.TILE_SIZE, null);
        }

        // Draw loaded chunks
        g2d.setColor(Color.GRAY);
        ArrayList<Vector2> loadedChunkPositions = new ArrayList<>(game.chunkLoader.loadedChunks.values());
        for (Vector2 lpos : loadedChunkPositions) {
            //System.out.println(lpos);
            if (camera.chunkPos.subtract(lpos).magnitude() > Config.RENDER_DISTANCE) continue;
            for (int x = 0; x < Config.CHUNK_SIZE; x++) {
                for (int y = 0; y < Config.CHUNK_SIZE; y++) {
                    Vector2 drawPosition = new Vector2(x, y).add(lpos.scale(Config.CHUNK_SIZE)).scale(Config.TILE_SIZE);
                    g2d.drawRect((int) drawPosition.x, (int) drawPosition.y, Config.TILE_SIZE, Config.TILE_SIZE);
                }
            }
        }


        // Draw the camera position marker
        g2d.setColor(Color.RED);
        Vector2 cameraDrawPosition = camera.pos.scale(Config.TILE_SIZE);
        g2d.drawRect((int) cameraDrawPosition.x, (int) cameraDrawPosition.y, Config.TILE_SIZE, Config.TILE_SIZE);

        // Reset translation to draw the player visual
        g2d.translate((cameraTopLeft.x + Config.X_OFFSET), (cameraTopLeft.y + Config.Y_OFFSET));

        // Draw the player visual
        g2d.setColor(Color.GREEN);
        int playerX = (this.getWidth() - Config.TILE_SIZE) / 2;
        int playerY = (this.getHeight() - Config.TILE_SIZE) / 2;
        g2d.drawRect(playerX, playerY, Config.TILE_SIZE, Config.TILE_SIZE);

        g2d.setColor(Color.BLACK);
        g2d.drawString("FPS: " + (int) (1 / game.deltaTime), 50, 50);
        g2d.drawString("MAX DT: " + game.longestDeltaTime, 50, 75);
        g2d.drawString("Camera Pos: " + camera.pos.round(), 50, 100);
    }
}
