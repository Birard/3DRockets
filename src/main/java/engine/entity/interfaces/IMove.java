package engine.entity.interfaces;

import org.joml.Vector3f;

public interface IMove {

    void move();
    float getSpeedX();
    float getSpeedY();
    Vector3f getPosition();
}
