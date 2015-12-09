package andriy.krupych.andenginegame.scene;

import org.andengine.engine.camera.hud.HUD;
import org.andengine.entity.Entity;
import org.andengine.entity.IEntity;
import org.andengine.entity.modifier.IEntityModifier;
import org.andengine.entity.modifier.LoopEntityModifier;
import org.andengine.entity.modifier.MoveModifier;
import org.andengine.entity.modifier.RotationModifier;
import org.andengine.entity.scene.IOnSceneTouchListener;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.scene.background.EntityBackground;
import org.andengine.entity.sprite.AnimatedSprite;
import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.text.Text;
import org.andengine.entity.text.TextOptions;
import org.andengine.input.sensor.acceleration.AccelerationData;
import org.andengine.input.sensor.acceleration.IAccelerationListener;
import org.andengine.input.touch.TouchEvent;
import org.andengine.util.adt.align.HorizontalAlign;
import org.andengine.util.debug.Debug;
import org.andengine.util.modifier.IModifier;
import org.andengine.util.modifier.ease.EaseExponentialIn;

import andriy.krupych.andenginegame.ResourceManager;
import andriy.krupych.andenginegame.entity.Player;
import andriy.krupych.andenginegame.factory.PlayerFactory;

public class GameScene extends AbstractScene implements IAccelerationListener {

    private Player player;
    private Text scoreText;
    private float lastX = 0;

    public GameScene() {
        PlayerFactory.getInstance().create(vbom);
    }

    @Override
    public void populate() {
        createBackground();
        createPlayer();
        createHUD();
        final AnimatedSprite fly = new AnimatedSprite(240, 200, res.enemyTextureRegion, vbom) {
            @Override
            protected void onManagedUpdate(float pSecondsElapsed) {
                super.onManagedUpdate(pSecondsElapsed);
                if (collidesWith(player)) {
                    setScale(2);
                } else {
                    setScale(1);
                }
            }
        };
        fly.animate(125);
        attachChild(fly);
        fly.registerEntityModifier(new LoopEntityModifier(new RotationModifier(2, 0, 360, EaseExponentialIn.getInstance())));
        setOnSceneTouchListener(new IOnSceneTouchListener() {
            @Override
            public boolean onSceneTouchEvent(Scene pScene, TouchEvent pSceneTouchEvent) {
                if (pSceneTouchEvent.isActionDown()) {
                    player.clearEntityModifiers();
                    player.registerEntityModifier(new MoveModifier(1, player.getX(), player.getY(),
                            pSceneTouchEvent.getX(), pSceneTouchEvent.getY(), new IEntityModifier.IEntityModifierListener() {
                        @Override
                        public void onModifierStarted(IModifier<IEntity> pModifier, IEntity pItem) {

                        }

                        @Override
                        public void onModifierFinished(IModifier<IEntity> pModifier, IEntity pItem) {
//                            activity.runOnUpdateThread(new Runnable() {
//                                @Override
//                                public void run() {
//                                    player.detachSelf();
//                                }
//                            });
                        }
                    }));
                    ResourceManager.getInstance().soundFall.play();
                    return true;
                }
                return false;
            }
        });
        registerTouchArea(player);
        setTouchAreaBindingOnActionDownEnabled(true);
        setTouchAreaBindingOnActionMoveEnabled(true);
    }

    @Override
    public void onPause() {
        Debug.d("GameScene.onPause");
        engine.disableAccelerationSensor(activity);
        ResourceManager.getInstance().stopMusic();
    }

    @Override
    public void onResume() {
        Debug.d("GameScene.onResume");
        engine.enableAccelerationSensor(activity, this);
        ResourceManager.getInstance().playMusic();
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
        player.setX(player.getX() + pAccelerationData.getX());
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
}
