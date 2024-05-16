package game;

import animator.Animation;
import vector.Vector2;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;

public class Player extends GameObject{
    private Game game;
    private Animation anim;
    public Player(Vector2 pos, Game game) {
        super(pos);
        this.game = game;
        this.anim = new Animation("./src/images/Player.png", this, 16, 4, 1, 15);
    }

    @Override
    protected void update(double deltaTime) {
        move(game.currentMovement.normalize().scale(Config.SPEED * deltaTime));
        game.camera.pos = this.pos;
        game.camera.move(new Vector2(0, 0));
    }

    public void move(Vector2 offset) {
        anim.update();
        this.pos.x += offset.x;
        this.pos.y += offset.y;


        if(this.pos.x < 0) this.pos.x = 0;
        if(this.pos.y < 0) this.pos.y = 0;
        this.chunkPos = pos.scale(1f / Config.CHUNK_SIZE).round();
        //System.out.println(this.chunkPos + "; " + this.pos);

        anim.resume();
        if (offset.x > 0) {
            anim.setAnim(2);
        } else if (offset.x < 0) {
            anim.setAnim(1);
        } else if (offset.y > 0) {
            anim.setAnim(0);
        } else if (offset.y < 0) {
            anim.setAnim(3);
        } else anim.stop();
    }
}
