package andriy.krupych.andenginegame.entity;

import com.badlogic.gdx.physics.box2d.Body;

import org.andengine.entity.IEntity;

public interface CollidableEntity extends IEntity {
    public void setBody(Body body);
    public Body getBody();
    public String getType();
}
