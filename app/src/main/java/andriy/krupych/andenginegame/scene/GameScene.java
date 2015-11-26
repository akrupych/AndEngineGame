package andriy.krupych.andenginegame.scene;

import org.andengine.entity.Entity;
import org.andengine.entity.scene.background.EntityBackground;
import org.andengine.entity.sprite.Sprite;

import andriy.krupych.andenginegame.entity.Player;
import andriy.krupych.andenginegame.factory.PlayerFactory;

public class GameScene extends AbstractScene {

    private Player player;

    public GameScene() {
        PlayerFactory.getInstance().create(vbom);
    }

    @Override
    public void populate() {
        createBackground();
        createPlayer();
    }

    @Override
    public void onPause() {

    }

    @Override
    public void onResume() {

    }

    private void createBackground() {
        Entity background = new Entity();
        Sprite cloud1 = new Sprite(200, 300, res.cloud1TextureRegion, vbom);
        Sprite cloud2 = new Sprite(300, 600, res.cloud2TextureRegion, vbom);
        background.attachChild(cloud1);
        background.attachChild(cloud2);
        setBackground(new EntityBackground(0.059f, 0.059f, 0.561f, background));
    }

    private void createPlayer() {
        player = PlayerFactory.getInstance().createPlayer(240, 400);
        attachChild(player);
    }
}
