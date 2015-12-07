package andriy.krupych.andenginegame.entity;

import org.andengine.entity.sprite.TiledSprite;
import org.andengine.input.touch.TouchEvent;
import org.andengine.opengl.texture.region.ITiledTextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

import andriy.krupych.andenginegame.ResourceManager;

public class Player extends TiledSprite {

    boolean dead = false;

    public Player(float pX, float pY, ITiledTextureRegion pTiledTextureRegion, VertexBufferObjectManager pVertexBufferObjectManager) {
        super(pX, pY, pTiledTextureRegion, pVertexBufferObjectManager);
    }

    public boolean isDead() {
        return dead;
    }

    public void setDead(boolean dead) {
        this.dead = dead;
    }

    public void turnLeft() {
        setFlippedHorizontal(true);
    }

    public void turnRight() {
        setFlippedHorizontal(false);
    }

    public void stand() {
        setCurrentTileIndex(0);
    }

    public void fly() {
        setCurrentTileIndex(1);
    }

    public void fall() {
        setCurrentTileIndex(2);
    }

    public void die() {
        setDead(true);
        setCurrentTileIndex(2);
    }

    @Override
    public boolean onAreaTouched(TouchEvent pSceneTouchEvent, float pTouchAreaLocalX, float pTouchAreaLocalY) {
        if (pSceneTouchEvent.isActionDown()) {
            clearEntityModifiers();
            ResourceManager.getInstance().soundJump.play();
            return true;
        } else if (pSceneTouchEvent.isActionMove()) {
            setPosition(pSceneTouchEvent.getX(), pSceneTouchEvent.getY());
            return true;
        }
        return false;
    }
}
