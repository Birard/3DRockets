package engine.render;

import engine.assets.GameItem;
import org.joml.Matrix4f;
import org.joml.Quaternionf;
import org.joml.Vector3f;

public class Transformation {

    private final Matrix4f projectionMatrix;
    private final Matrix4f modelViewMatrix;
    private final Matrix4f viewMatrix;
    
    public Transformation() {
        projectionMatrix = new Matrix4f();
        modelViewMatrix = new Matrix4f();
        viewMatrix = new Matrix4f();
    }

    public final Matrix4f getProjectionMatrix(float fov, float width, float height, float zNear, float zFar) {
        return projectionMatrix.setPerspective(fov, width / height, zNear, zFar);
    }

    public Matrix4f getViewMatrix(Camera camera) {
        Vector3f cameraPos = camera.getPosition();
//        Vector3f rotation = camera.getRotation();
        Quaternionf rotationQ = camera.getRotation();

        viewMatrix.identity();
        // First do the rotation so camera rotates over its position
        viewMatrix.rotate(rotationQ);
//        viewMatrix.rotate((float)Math.toRadians(rotation.x), new Vector3f(1, 0, 0))
//                .rotate((float)Math.toRadians(rotation.y), new Vector3f(0, 1, 0));
//             //   .rotate((float)Math.toRadians(rotation.z), new Vector3f(0, 0, 1));

        // Then do the translation
        viewMatrix.translate(-cameraPos.x, -cameraPos.y, -cameraPos.z);
        return viewMatrix;
    }

    public Matrix4f getModelViewMatrix(GameItem gameItem, Matrix4f viewMatrix) {
//        Vector3f rotation = gameItem.getRotation();
        Quaternionf rotation = gameItem.getQuatRotation();
        modelViewMatrix.identity().translate(gameItem.getPosition());

//        modelViewMatrix.rotateLocalZ((float)Math.toRadians(rotation.z));
//        modelViewMatrix.rotateLocalY((float)Math.toRadians(rotation.y));
//        modelViewMatrix.rotateLocalX((float)Math.toRadians(rotation.x));

//        Quaternionf rotationQ = new Quaternionf().
//                rotateAxis((float)Math.toRadians(rotation.z), new Vector3f(0f, 0f, 1f)).
//                rotateAxis((float)Math.toRadians(rotation.y), new Vector3f(0f, 1f, 0f)).
//                rotateAxis((float)Math.toRadians(rotation.x), new Vector3f(1f, 0f, 0f));

        modelViewMatrix.rotate(rotation);

//        modelViewMatrix.rotate((float)Math.toRadians(rotation.z), new Vector3f(0, 0, 1));
//        modelViewMatrix.rotate((float)Math.toRadians(rotation.y), new Vector3f(0, 1, 0));
//        modelViewMatrix.rotate((float)Math.toRadians(rotation.x), new Vector3f(1, 0, 0));
        modelViewMatrix.scale(gameItem.getScale());
        Matrix4f viewCurr = new Matrix4f(viewMatrix);
        return viewCurr.mul(modelViewMatrix);
    }

    public Matrix4f buildModelMatrix(GameItem gameItem) {
        Quaternionf rotation = gameItem.getQuatRotation();
        return modelViewMatrix.translationRotateScale(
                gameItem.getPosition().x, gameItem.getPosition().y, gameItem.getPosition().z,
                rotation.x, rotation.y, rotation.z, rotation.w,
                gameItem.getScale(), gameItem.getScale(), gameItem.getScale());
    }

    public Matrix4f getModelViewMatrix1(GameItem gameItem, Matrix4f viewMatrix) {
        return buildModelViewMatrix(buildModelMatrix(gameItem), viewMatrix);
    }

    public Matrix4f buildModelViewMatrix(Matrix4f modelMatrix, Matrix4f viewMatrix) {
        return viewMatrix.mulAffine(modelMatrix, modelViewMatrix);
    }




}
