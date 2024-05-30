package game;

import vector.Vector2;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class GamePanel extends JPanel {
    private Game game;
    public boolean debug = true;

    public GamePanel(Game game){
        this.game = game;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        drawGraphics(g);
    }

    private void drawCollider(Graphics2D g2d, GameObject obj) {
        if (obj.collider == null) return;
        g2d.setColor(Color.GREEN);
        for (int i = 0; i < obj.collider.vertices.size(); i++) {
            Vector2 drawPosition1 = obj.pos.add(obj.collider.vertices.get(i)).scale(Config.TILE_SIZE);
            Vector2 drawPosition2 = new Vector2(0, 0);
            if (i + 1 < obj.collider.vertices.size()) {
                drawPosition2 = obj.pos.add(obj.collider.vertices.get(i+1)).scale(Config.TILE_SIZE);
            } else {
                drawPosition2 = obj.pos.add(obj.collider.vertices.get(0)).scale(Config.TILE_SIZE);
            }
            //TODO fix next line (+ Config.TILE_SIZE / 2 should be revised)
            g2d.drawLine((int) drawPosition1.x + Config.TILE_SIZE / 2, (int) drawPosition1.y + Config.TILE_SIZE / 2, (int) drawPosition2.x + Config.TILE_SIZE / 2, (int) drawPosition2.y + Config.TILE_SIZE / 2);
        }
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

        // draw debug info
        if (debug) {
            // Draw loaded chunks
            g2d.setColor(Color.GRAY);
            ArrayList<Vector2> loadedChunkPositions = new ArrayList<>(game.chunkLoader.loadedChunks.values());
            for (Vector2 lpos : loadedChunkPositions) {
                //System.out.println(lpos);
                if(lpos == null) continue;
                if (camera.chunkPos.subtract(lpos).magnitude() > Config.RENDER_DISTANCE) continue;
                for (int x = 0; x < Config.CHUNK_SIZE; x++) {
                    for (int y = 0; y < Config.CHUNK_SIZE; y++) {
                        Vector2 drawPosition = new Vector2(x, y).add(lpos.scale(Config.CHUNK_SIZE)).scale(Config.TILE_SIZE);
                        g2d.drawRect((int) drawPosition.x, (int) drawPosition.y, Config.TILE_SIZE, Config.TILE_SIZE);
                    }
                }
            }

            // Draw environment tiles colliders
            for (Tile tile : game.getTilesEnv()) {
                drawCollider(g2d, tile);
            }

            // Draw foreground tiles colliders
            for (Tile tile : game.getTilesFor()) {
                drawCollider(g2d, tile);
            }

            // Draw player collider
            drawCollider(g2d, game.player);

            // Draw the camera position marker
            g2d.setColor(Color.RED);
            Vector2 cameraDrawPosition = camera.pos.scale(Config.TILE_SIZE);
            g2d.drawRect((int) cameraDrawPosition.x, (int) cameraDrawPosition.y, Config.TILE_SIZE, Config.TILE_SIZE);
        }

        Vector2 drawPosition2 = game.player.pos.scale(Config.TILE_SIZE);
        g2d.drawImage(game.player.img, (int) drawPosition2.x, (int) drawPosition2.y, null);

        // Reset translation to draw text
        g2d.translate((cameraTopLeft.x + Config.X_OFFSET), (cameraTopLeft.y + Config.Y_OFFSET));

        g2d.setColor(Color.BLACK);
        g2d.drawString("FPS: " + (int) (1 / game.deltaTime), 50, 50);
        g2d.drawString("MAX DT: " + game.longestDeltaTime, 50, 75);
        g2d.drawString("Camera Pos: " + camera.pos.round(), 50, 100);
        g2d.drawString("Player Pos: " + game.player.pos.round(), 50, 125);
    }
}
