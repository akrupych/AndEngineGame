package andriy.krupych.andenginegame.scene;

import org.andengine.engine.Engine;
import org.andengine.engine.camera.Camera;
import org.andengine.entity.scene.Scene;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.util.debug.Debug;

import andriy.krupych.andenginegame.GameActivity;
import andriy.krupych.andenginegame.ResourceManager;

public abstract class AbstractScene extends Scene {

    protected ResourceManager res = ResourceManager.getInstance();
    protected Engine engine = res.engine;
    protected GameActivity activity = res.activity;
    protected VertexBufferObjectManager vbom = res.vbom;
    protected Camera camera = res.camera;

    public abstract void populate();

    public void destroy() {
    }

    public void onBackKeyPressed() {
        Debug.d("Back key pressed");
    }

    public abstract void onPause();

    public abstract void onResume();
}
