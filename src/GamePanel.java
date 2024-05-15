import vector.Vector2;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

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

        g2d.translate((int) -camera.pos.x, (int) -camera.pos.y);

        for (Tile tile : game.getTilesEnv()) {
            g2d.drawImage(tile.img, (int) tile.pos.x * Config.TILE_SIZE, (int) tile.pos.y * Config.TILE_SIZE, Config.TILE_SIZE, Config.TILE_SIZE, null);
        }

        for (Tile tile : game.getTilesFor()) {
            g2d.drawImage(tile.img, (int) tile.pos.x * Config.TILE_SIZE, (int) tile.pos.y * Config.TILE_SIZE, Config.TILE_SIZE, Config.TILE_SIZE, null);
        }

        g2d.translate((int) camera.pos.x, (int) camera.pos.y);
    }
}
