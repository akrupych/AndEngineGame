package andriy.krupych.andenginegame;

import org.andengine.engine.camera.Camera;
import org.andengine.engine.options.EngineOptions;
import org.andengine.engine.options.ScreenOrientation;
import org.andengine.engine.options.WakeLockOptions;
import org.andengine.engine.options.resolutionpolicy.FillResolutionPolicy;
import org.andengine.engine.options.resolutionpolicy.IResolutionPolicy;
import org.andengine.entity.scene.Scene;
import org.andengine.ui.activity.BaseGameActivity;
import org.andengine.util.debug.Debug;

import java.io.IOException;

import andriy.krupych.andenginegame.scene.AbstractScene;
import andriy.krupych.andenginegame.scene.GameScene;

public class GameActivity extends BaseGameActivity {

    public static final int CAMERA_WIDTH = 480;
    public static final int CAMERA_HEIGHT = 800;
    private GameScene mScene;

    @Override
    public EngineOptions onCreateEngineOptions() {
        Camera camera = new Camera(0, 0, CAMERA_WIDTH, CAMERA_HEIGHT);
        IResolutionPolicy resolutionPolicy = new FillResolutionPolicy();
        EngineOptions engineOptions = new EngineOptions(true, ScreenOrientation.PORTRAIT_FIXED, resolutionPolicy, camera);
        engineOptions.getAudioOptions().setNeedsMusic(true).setNeedsSound(true);
        engineOptions.setWakeLockOptions(WakeLockOptions.SCREEN_ON);
        engineOptions.getRenderOptions().setDithering(true);
        Debug.i("Engine configured");
        return engineOptions;
    }

    @Override
    public void onCreateResources(OnCreateResourcesCallback pOnCreateResourcesCallback) throws IOException {
        ResourceManager.getInstance().create(this, getEngine(), getEngine().getCamera(), getVertexBufferObjectManager());
        ResourceManager.getInstance().loadFont();
        ResourceManager.getInstance().loadGameAudio();
        ResourceManager.getInstance().loadGameGraphics();
        pOnCreateResourcesCallback.onCreateResourcesFinished();
        ResourceManager.getInstance().playMusic();
    }

    @Override
    public void onCreateScene(OnCreateSceneCallback pOnCreateSceneCallback) throws IOException {
        mScene = new GameScene();
        pOnCreateSceneCallback.onCreateSceneFinished(mScene);
        mScene.onResume();
    }

    @Override
    public void onPopulateScene(Scene pScene, OnPopulateSceneCallback pOnPopulateSceneCallback) throws IOException {
        AbstractScene scene = (AbstractScene) pScene;
        scene.populate();
        pOnPopulateSceneCallback.onPopulateSceneFinished();
    }

    @Override
    protected synchronized void onResume() {
        super.onResume();
        if (mScene!= null) {
            mScene.onResume();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mScene!= null) {
            mScene.onPause();
        }
    }
}
