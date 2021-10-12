package engine.assets;

import org.joml.Quaternionf;
import org.joml.Vector3f;



public class GameItem {

    private  Mesh mesh;
    private  NewMesh[] newMesh;
    protected final Vector3f position;
    private float scale;
    protected Quaternionf rotationQ;
    private boolean hasNewMesh;


    public GameItem() {
        position = new Vector3f();
        scale = 1;
        rotationQ = new Quaternionf(0,0,0,1);
    }

    public GameItem(Mesh mesh) {
        this();
        this.mesh = mesh;
        newMesh = null;
        hasNewMesh = false;
    }

    public GameItem(NewMesh[] mesh1) {
        this();
        this.newMesh = mesh1;
        mesh = null;
        hasNewMesh = true;
    }



    public Vector3f getPosition() {
        return position;
    }

    public void setPosition(float x, float y, float z) {
        this.position.x = x;
        this.position.y = y;
        this.position.z = z;
    }
    public void setPosition(Vector3f newPosition) {
        this.position.x = newPosition.x;
        this.position.y = newPosition.y;
        this.position.z = newPosition.z;
    }
    public float getScale() {
        return scale;
    }

    public void setScale(float scale) {
        this.scale = scale;
    }

    public Quaternionf getQuatRotation() {
        return rotationQ;
    }

    public void setQuatRotation(Quaternionf quaternionf) {
        rotationQ = quaternionf;
    }

    public void rotate(Quaternionf quat) {
        rotationQ.mul(quat);
        Vector3f vector3f = new Vector3f(0,0,0);
    }

    public void setRotation(float x, float y, float z) {
        rotationQ.rotateAxis((float)Math.toRadians(z), new Vector3f(0f, 0f, 1f)).
        rotateAxis((float)Math.toRadians(y), new Vector3f(0f, 1f, 0f)).
        rotateAxis((float)Math.toRadians(x), new Vector3f(1f, 0f, 0f));
    }

    public Mesh getMesh() {
        return mesh;
    }
    public Mesh[] getMeshes() {
       return newMesh;
    }

    public boolean isHasNewMesh() {
        return hasNewMesh;
    }


}