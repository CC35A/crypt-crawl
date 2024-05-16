package animator;

import game.GameObject;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import static animator.ImageUtility.scale;

public class Animation {

    private BufferedImage animSheet;

    private int frameSize;
    private int scale;
    private int animFrameCount;
    private int baseImg;
    private String sheetPath;

    private int counter;
    private int currentAnim;
    private GameObject actor;
    private BufferedImage[][] frames;

    private HashMap<String, BufferedImage[][]> framesColor = new HashMap<String, BufferedImage[][]>();
    public int frameCount;
    private boolean isRunning;
    private List<String> colors;
    private String currentColor = "Default";
    private long nextUpdate;
    private double timeBetweenUpdates;

    public Animation(String animSheetPath, GameObject actor, int frameSize, int scale, int baseImg, int FPS, List<String> colors) {
        try {
            this.timeBetweenUpdates = (1f / FPS) * 1e3;
            System.out.println(timeBetweenUpdates);
            this.actor = actor;
            this.sheetPath = animSheetPath;
            this.baseImg = baseImg;
            this.frameSize = frameSize;
            this.scale = scale;
            System.out.println("Reading Sheet" + animSheetPath);
            this.animSheet = ImageIO.read(new File(animSheetPath));
            this.animFrameCount = this.animSheet.getWidth() / frameSize;
            createFrames(this.animSheet, "Default");

            if (colors != null) {
                for (String color : colors) {
                    createFrames(this.animSheet, color);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Animation(String animSheetPath, GameObject actor, int frameSize, int scale, int baseImg, int FPS) {
        this(animSheetPath, actor, frameSize, scale, baseImg, FPS, null);
    }

    private void createFrames(BufferedImage sheet, String color) throws IOException {
        if (framesColor.containsKey(color)) return;
        this.frameCount = this.animSheet.getWidth() / this.frameSize;
        frames = new BufferedImage[this.animSheet.getHeight() / this.frameSize][frameCount];
        String[] keys = sheetPath.replace(".png", "").split("/");
        String key = keys[keys.length - 1];
        for (int c = 0; c < sheet.getHeight() / this.frameSize; c++) {
            for (int i = 0; i < sheet.getWidth() / this.frameSize; i++) {
                BufferedImage subImg = sheet.getSubimage(i * this.frameSize, c * this.frameSize, this.frameSize, this.frameSize);
                if (!color.equals("Default")) {
                    subImg = Text.changeImageColor(subImg, Text.getColorFromHex(color));
                }
                frames[c][i] = subImg;
            }
        }
        framesColor.putIfAbsent(color, frames);
    }

    public void update() {
        if(System.currentTimeMillis() < this.nextUpdate) return;
        this.nextUpdate = (long) (System.currentTimeMillis() + timeBetweenUpdates);
        if (this.counter >= this.animFrameCount) this.counter = 0;
        BufferedImage img = framesColor.get(currentColor)[currentAnim][this.isRunning ? this.counter : this.baseImg];
        img = scale(img, this.frameSize * this.scale, this.frameSize * this.scale);
        this.counter++;
        actor.img = img;
    }

    public void setImage(int id) {
        BufferedImage img = framesColor.get(currentColor)[currentAnim][id];
        img = scale(img, this.frameSize * this.scale, this.frameSize * this.scale);
        actor.img = img;
    }

    public void setAnim(int animId) {
        this.currentAnim = animId;
    }

    public void stop() {
        this.isRunning = false;
        update();
    }

    public void resume() {
        this.isRunning = true;
    }

    public void resetCounter() {
        this.counter = 0;
    }

    public void setColor(String colorCode) {
        this.currentColor = colorCode;
    }
}
