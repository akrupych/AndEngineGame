package andriy.krupych.andenginegame;

import android.graphics.Typeface;

import org.andengine.audio.music.Music;
import org.andengine.audio.music.MusicFactory;
import org.andengine.audio.sound.Sound;
import org.andengine.audio.sound.SoundFactory;
import org.andengine.engine.Engine;
import org.andengine.engine.camera.Camera;
import org.andengine.opengl.font.Font;
import org.andengine.opengl.font.FontFactory;
import org.andengine.opengl.texture.TextureOptions;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.andengine.opengl.texture.atlas.bitmap.BuildableBitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.source.IBitmapTextureAtlasSource;
import org.andengine.opengl.texture.atlas.buildable.builder.BlackPawnTextureAtlasBuilder;
import org.andengine.opengl.texture.atlas.buildable.builder.ITextureAtlasBuilder;
import org.andengine.opengl.texture.bitmap.BitmapTextureFormat;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.texture.region.ITiledTextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.util.adt.color.Color;

public class ResourceManager {

    // single instance is created only
    private static final ResourceManager INSTANCE = new ResourceManager();

    //common objects
    public GameActivity activity;
    public Engine engine;
    public Camera camera;
    public VertexBufferObjectManager vbom;

    //game textures
    public ITiledTextureRegion playerTextureRegion;
    public ITiledTextureRegion enemyTextureRegion;
    public ITextureRegion platformTextureRegion;
    public ITextureRegion cloud1TextureRegion;
    public ITextureRegion cloud2TextureRegion;
    private BuildableBitmapTextureAtlas gameTextureAtlas;

    //sounds
    public Sound soundFall;
    public Sound soundJump;
    //music
    public Music music;

    //font
    public Font font;

    public static ResourceManager getInstance() {
        return INSTANCE;
    }

    // constructor is private to ensure nobody can call it from outside
    private ResourceManager() {
    }

    public void create(GameActivity activity, Engine engine, Camera camera, VertexBufferObjectManager vbom) {
        this.activity = activity;
        this.engine = engine;
        this.camera = camera;
        this.vbom = vbom;
    }

    public void loadGameGraphics() {
        BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/");
        gameTextureAtlas = new BuildableBitmapTextureAtlas(activity.getTextureManager(),
                1024, 512, BitmapTextureFormat.RGBA_8888, TextureOptions.BILINEAR_PREMULTIPLYALPHA);
        playerTextureRegion = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(
                gameTextureAtlas, activity.getAssets(), "player.png", 3, 1);
        enemyTextureRegion = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(
                gameTextureAtlas, activity.getAssets(), "enemy.png", 1, 2);
        platformTextureRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(
                gameTextureAtlas, activity.getAssets(), "platform.png");
        cloud1TextureRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(
                gameTextureAtlas, activity.getAssets(), "cloud1.png");
        cloud2TextureRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(
                gameTextureAtlas, activity.getAssets(), "cloud2.png");
        try {
            gameTextureAtlas.build(new BlackPawnTextureAtlasBuilder<IBitmapTextureAtlasSource, BitmapTextureAtlas>(2, 0, 2));
            gameTextureAtlas.load();
        } catch (final ITextureAtlasBuilder.TextureAtlasBuilderException e) {
            throw new RuntimeException("Error while loading game textures", e);
        }
    }

    public void loadGameAudio() {
        try {
            SoundFactory.setAssetBasePath("sfx/");
            soundJump = SoundFactory.createSoundFromAsset(activity.getSoundManager(), activity, "jump.wav");
            soundFall = SoundFactory.createSoundFromAsset(activity.getSoundManager(), activity, "fall.wav");
            MusicFactory.setAssetBasePath("mfx/");
            music = MusicFactory.createMusicFromAsset(activity.getMusicManager(), activity, "music.mp3");
        } catch (Exception e) {
            throw new RuntimeException("Error while loading audio", e);
        }
    }

    public void loadFont() {
        font = FontFactory.createStroke(activity.getFontManager(), activity.getTextureManager(), 256, 256, Typeface.create(
                Typeface.SANS_SERIF, Typeface.BOLD), 50, true, Color.WHITE_ABGR_PACKED_INT, 2, Color.BLACK_ABGR_PACKED_INT);
        font.prepareLetters("01234567890ABCDEFGHIJKLMNOPQRSTUVWXYZ.,!?".toCharArray());
        font.load();
    }

    public void playMusic() {
        if (music != null && !music.isPlaying()) {
            music.play();
        }
    }

    public void stopMusic() {
        if (music != null && music.isPlaying()) {
            music.stop();
        }
    }
}
