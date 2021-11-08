package engine.render.Cameras;

import org.joml.Quaternionf;
import org.joml.Vector3f;

public class Camera {

    private Vector3f position;
    public Quaternionf rotationQ;


    public Camera() {
        position = new Vector3f();
        rotationQ = new Quaternionf();
    }

    public Camera(Vector3f position, Quaternionf rotation) {
        this.position = position;
        this.rotationQ = rotation;
    }


    public void setPosition(float x, float y, float z) {
        position.x = x;
        position.y = y;
        position.z = z;
    }


    public void setPosition(Vector3f vector3f) {
        position.x = vector3f.x;
        position.y = vector3f.y;
        position.z = vector3f.z;
    }


    public void setRotation(Quaternionf rotatQ) {
        rotationQ = rotatQ;
    }


    public void moveRotation(float offsetX, float offsetY, float offsetZ) {
        rotationQ.rotateLocalZ((float)Math.toRadians(offsetZ));
        rotationQ.rotateLocalY((float)Math.toRadians(offsetY));
        rotationQ.rotateLocalX((float)Math.toRadians(offsetX));
}


    public void move(float X, float Y, float Z) {
        Vector3f vectorF = new Vector3f(X, Y, Z);
        Quaternionf q = new Quaternionf();
        q.div(rotationQ);
        vectorF.rotate(q);
        position.add(new Vector3f((vectorF.x), (vectorF.y), (vectorF.z)));
    }


    public Quaternionf getRotation() {
        return rotationQ;
    }


    public Vector3f getPosition() {
        return position;
    }

}