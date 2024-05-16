package animator;

import game.GameObject;
import vector.Vector2;

/**
 * Used to hold elements from the user interface
 */
public class UI extends GameObject {
    public boolean active;  // whether this element is active or not

    public UI(Vector2 pos) {
        super(pos);
        this.active = false;
    }
}