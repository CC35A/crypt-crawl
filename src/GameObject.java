import vector.Vector2;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class GameObject {
    public BufferedImage img;
    public Vector2 pos;


    public GameObject(Vector2 pos){
        this.pos = pos;
    }
}
