package engine.render;

import org.joml.Quaternionf;
import org.joml.Vector3f;

public class Camera {

    boolean focused;
    private Vector3f focus;

    private Vector3f position;
    private Quaternionf rotationQ;
//    private Vector3f rotation;

    public Camera() {
        position = new Vector3f();
        rotationQ = new Quaternionf();
        focused = false;
    }

    public Camera(Vector3f position, Quaternionf rotation) {
        this.position = position;
        this.rotationQ = rotation;
        focused = false;
    }

//    public void setFocus(Vector3f newFocus) {
//        focus = new Vector3f(newFocus);
//        Vector3f focusPosition = new Vector3f(0,0,-1);
//        focus.x = focus.x - position.x;
//        focus.y = focus.y - position.y;
//        focus.z = focus.z - position.z;
//
//        float savedf = focus.z;
//
//        if(focus.z<0) {focus.z = focus.z - Math.abs(focus.x);}
//        else {focus.z = focus.z + Math.abs(focus.x);}
//
//        boolean changeSign = false;
//        if(focus.y > 0) {
//            changeSign = true;
//        }
//        // Получим косинус угла по формуле
//        double cos = (focusPosition.y*focus.y + focusPosition.z*focus.z) /
//                (Math.sqrt(focusPosition.y*focusPosition.y + focusPosition.z*focusPosition.z) *
//                        Math.sqrt(focus.y*focus.y + focus.z*focus.z));
//        // Вернем arccos полученного значения (в радианах!)
//        Float angl =  (float) Math.acos(cos);
//        angl = (float)(angl*180/3.14);
//        if(!angl.isNaN()) {
//                for(rotation.x = angl; rotation.x > 360; rotation.x = rotation.x - 360) {}
//               if(changeSign) {
//                   rotation.x = -rotation.x;
//               }
//            rotation.y = 0;
//            if(rotation.x < -90 | rotation.x > 90) {
//                rotation.x = (180 - rotation.x);
//            }
//        }
//        changeSign = false;
//        focus.z = savedf;
//
//        if(focus.x < 0) {
//            changeSign = true;
//        }
//        // Получим косинус угла по формуле
//        cos = (focusPosition.x*focus.x + focusPosition.z*focus.z) /
//                (Math.sqrt(focusPosition.x*focusPosition.x + focusPosition.z*focusPosition.z) *
//                        Math.sqrt(focus.x*focus.x + focus.z*focus.z));
//        // Вернем arccos полученного значения (в радианах!)
//        angl =  (float) Math.acos(cos);
//        angl = (float)(angl*180/3.14);
//        if(!angl.isNaN()) {
//            for(rotation.y += angl; rotation.y > 360; rotation.y = rotation.y - 360) {
//            }
//            if(changeSign) rotation.y = 360 - rotation.y;
//        }
//
//        focused = true;
//    }

    public void setFocused(boolean focused) {
        this.focused = focused;
    }

    public boolean isFocused() {
        return focused;
    }

    public Vector3f getPosition() {
        return position;
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

//    public void movePosition(float offsetX, float offsetY, float offsetZ) {
//        if ( offsetZ != 0 ) {
//            position.x += (float)Math.sin(Math.toRadians(rotation.y)) * -1.0f * offsetZ;
//            position.z += (float)Math.cos(Math.toRadians(rotation.y)) * offsetZ;
//        }
//        if ( offsetX != 0) {
//            position.x += (float)Math.sin(Math.toRadians(rotation.y - 90)) * -1.0f * offsetX;
//            position.z += (float)Math.cos(Math.toRadians(rotation.y - 90)) * offsetX;
//        }
//        position.y += offsetY;
////
////        position.x += offsetX;
////        position.y += offsetY;
////        position.z += offsetZ;
//
//    }

    public Quaternionf getRotation() {
        return rotationQ;
    }

    public void setRotation(Quaternionf rotatQ) {
        rotationQ = rotatQ;
    }

//    public void setRotation(float x, float y, float z) {
//        rotation.x = x;
//        rotation.y = y;
//        rotation.z = z;
//    }
//
//    public void moveRotation(float offsetX, float offsetY, float offsetZ) {
//        rotation.x += offsetX;
//        rotation.y += offsetY;
//        rotation.z += offsetZ;
//    }

    public void moveRotation(float offsetX, float offsetY, float offsetZ) {
        rotationQ.rotateAxis((float) Math.toRadians(offsetZ), new Vector3f(0f, 0f, 1f)).
            rotateAxis((float) Math.toRadians(offsetY), new Vector3f(0f, 1f, 0f)).
            rotateAxis((float) Math.toRadians(offsetX), new Vector3f(1f, 0f, 0f));
}
}