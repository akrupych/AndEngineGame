package andriy.krupych.andenginegame.scene;

import android.hardware.SensorManager;

import com.badlogic.gdx.math.Vector2;

import org.andengine.engine.camera.hud.HUD;
import org.andengine.entity.Entity;
import org.andengine.entity.scene.background.EntityBackground;
import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.text.Text;
import org.andengine.entity.text.TextOptions;
import org.andengine.extension.physics.box2d.PhysicsWorld;
import org.andengine.extension.physics.box2d.util.Vector2Pool;
import org.andengine.input.sensor.acceleration.AccelerationData;
import org.andengine.input.sensor.acceleration.IAccelerationListener;
import org.andengine.util.adt.align.HorizontalAlign;
import org.andengine.util.debug.Debug;

import java.util.LinkedList;
import java.util.Random;

import andriy.krupych.andenginegame.ResourceManager;
import andriy.krupych.andenginegame.entity.Platform;
import andriy.krupych.andenginegame.entity.Player;
import andriy.krupych.andenginegame.factory.PlatformFactory;
import andriy.krupych.andenginegame.factory.PlayerFactory;

public class GameScene extends AbstractScene implements IAccelerationListener {

    private Player player;
    private Text scoreText;
    private float lastX = 0;
    private PhysicsWorld physicsWorld;
    private Random rand = new Random();
    private LinkedList<Platform> platforms = new LinkedList<>();

    public GameScene() {
        physicsWorld = new PhysicsWorld(new Vector2(0, -SensorManager.GRAVITY_EARTH * 4), false);
        PlayerFactory.getInstance().create(physicsWorld, vbom);
        PlatformFactory.getInstance().create(physicsWorld, vbom);
    }

    @Override
    public void populate() {
        createBackground();
        createPlayer();
        createHUD();
        addPlatform(240, 50, false);
    }

    @Override
    public void onPause() {
        Debug.d("GameScene.onPause");
        engine.disableAccelerationSensor(activity);
        ResourceManager.getInstance().stopMusic();
        unregisterUpdateHandler(physicsWorld);
    }

    @Override
    public void onResume() {
        Debug.d("GameScene.onResume");
        engine.enableAccelerationSensor(activity, this);
        ResourceManager.getInstance().playMusic();
        registerUpdateHandler(physicsWorld);
    }

    @Override
    public void onAccelerationAccuracyChanged(AccelerationData pAccelerationData) {

    }

    @Override
    public void onAccelerationChanged(AccelerationData pAccelerationData) {
        if (Math.abs(pAccelerationData.getX() - lastX) > 0.5) {
            if (pAccelerationData.getX() > 0) {
                player.turnRight();
            } else {
                player.turnLeft();
            }
            lastX = pAccelerationData.getX();
        }
        Vector2 gravity = Vector2Pool.obtain(pAccelerationData.getX() * 8, -SensorManager.GRAVITY_EARTH * 4);
        physicsWorld.setGravity(gravity);
        Vector2Pool.recycle(gravity);
    }

    private void createBackground() {
        Entity background = new Entity();
        Sprite cloud1 = new Sprite(200, 300, res.cloud1TextureRegion, vbom);
        Sprite cloud2 = new Sprite(300, 600, res.cloud2TextureRegion, vbom);
        background.attachChild(cloud1);
        background.attachChild(cloud2);
        setBackground(new EntityBackground(213/255f, 245/255f, 248/255f, background));
    }

    private void createPlayer() {
        player = PlayerFactory.getInstance().createPlayer(240, 400);
        attachChild(player);
    }

    private void createHUD() {
        HUD hud = new HUD();
        scoreText = new Text(16, 784, res.font, "0123456789", new TextOptions(HorizontalAlign.LEFT), vbom);
        scoreText.setAnchorCenter(0, 1);
        hud.attachChild(scoreText);
        camera.setHUD(hud);
    }

    private void addPlatform(float tx, float ty, boolean moving) {
        Platform platform;
        if (moving) {
            platform = PlatformFactory.getInstance().createMovingPlatform(tx, ty, (rand.nextFloat() - 0.5f) * 10f);
        } else {
            platform = PlatformFactory.getInstance().createPlatform(tx, ty);
        }
        attachChild(platform);
        platforms.add(platform);
    }
}
