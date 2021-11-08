package engine.render.Cameras;

import engine.assets.GameItem;
import org.joml.Quaternionf;
import org.joml.Vector3f;

public class ThirdPersonCamera extends Camera {

    private boolean focused = false, hardFocused = false;
    private GameItem focus;
    private final Vector3f cameraPosOnFocus = new Vector3f(30, 0, 0);


        public void updateCamPos() {
        if(!focused) return;
        if(hardFocused) {
            Vector3f posC = this.getPosition();
            Vector3f posF = focus.getPosition();
            Quaternionf qe1 = new Quaternionf();
//            qe1.div(focus.getQuatRotation());
            qe1 = focus.getQuatRotation();
            Vector3f vectorF = new Vector3f(-cameraPosOnFocus.x,10,0);
            vectorF.rotate(qe1);
            Vector3f newPosC = new Vector3f((posF.x + vectorF.x), (posF.y + vectorF.y), (posF.z + vectorF.z));
            this.setPosition(newPosC);

            Quaternionf qe2 = new Quaternionf(focus.getQuatRotation());
            qe2.rotateLocalY(1F);
//            qe2.rotateTo(new Vector3f(0,0,1),new Vector3f((posF.x - newPosC.x), (posF.y - newPosC.y), (newPosC.z - posF.z)));
            this.setRotation(qe2);
            return;
        }
            Vector3f posC = this.getPosition();
            Vector3f posF = focus.getPosition();
            Quaternionf qe1 = new Quaternionf();
            qe1.rotateTo(new Vector3f(1,0,0),new Vector3f((posC.x - posF.x), (posC.y - posF.y), (posC.z - posF.z)));
            Vector3f vectorF = new Vector3f(cameraPosOnFocus);
            vectorF.rotate(qe1);
            Vector3f newPosC = new Vector3f((posF.x + vectorF.x), (posF.y + vectorF.y), (posF.z + vectorF.z));
            this.setPosition(newPosC);

            Quaternionf qe2 = new Quaternionf();
            qe2.rotateTo(new Vector3f(0,0,1),new Vector3f((posF.x - newPosC.x), (posF.y - newPosC.y), (newPosC.z - posF.z)));
            this.setRotation(qe2);

    }


    public void setFocus(GameItem focus) {
        this.focus = focus;
    }




    public void setFocusedOpposite() {
        focused = !focused;
    }
    public boolean isFocused() {
        return focused;
    }
    public void setFocused(boolean focused) {
        this.focused = focused;
    }

    public void setHardFocusedOpposite() {
        hardFocused = !hardFocused;
    }
    public boolean isHardFocused() {
        return hardFocused;
    }
    public void setHardFocused(boolean hardFocused) {
        this.hardFocused = ThirdPersonCamera.this.hardFocused;
    }


    public void addLengthToFocus() {
        cameraPosOnFocus.x += 1;
    }


    public void subtractLengthToFocus() {
            if(cameraPosOnFocus.x > 2)
        cameraPosOnFocus.x -= 1;
    }
}
