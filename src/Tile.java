import vector.Vector2;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;

public class Tile extends GameObject{
    private final String path = "./src/images/textures/";
    public Tile(Vector2 pos, int id) {
        super(pos);
        String imageName = null;
        if(id == 0) imageName = "DungeonFloorClean.png";
        if(id == 1) imageName = "RedChest.png";
        if(imageName == null) return;
        try {
            this.img = ImageIO.read(new File(path + imageName));
        } catch (IOException e) {
            throw new RuntimeException("Failed to load image: " + imageName, e);
        }
    }
}
