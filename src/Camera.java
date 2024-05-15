import vector.Vector2;

public class Camera {
    public Vector2 pos;

    public Camera(Vector2 pos) {
        this.pos = pos;
    }

    public void move(Vector2 dv) {
        this.pos = this.pos.subtract(dv); // TODO change to add to mimic player movement
    }
}
