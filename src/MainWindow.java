import vector.Vector2;

import javax.swing.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class MainWindow extends JFrame{
    private Game game;
    private GamePanel gamePanel;
    private Timer timer;

    public MainWindow(){
        setTitle(Config.GAME_TITLE);
        setSize(1600, 900);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        game = new Game();
        gamePanel = new GamePanel(game);
        add(gamePanel);

        timer = new Timer(10, e -> {
            game.update();
            gamePanel.repaint();
        });
        timer.start();

        Timer chunkTimer = new Timer(10, f -> {
           game.chunkLoader.update();
        });
        chunkTimer.start();

        gamePanel.setFocusable(true);
        gamePanel.requestFocusInWindow();
        gamePanel.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                game.keyPressed(e); // Forward key events to the game class
            }
            @Override
            public void keyReleased(KeyEvent e){
                game.keyReleased(e);
            }
        });
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            MainWindow window = new MainWindow();
            window.setVisible(true);
        });
    }
}
