package engine.render.Cameras;

import engine.assets.GameItem;
import org.joml.Quaternionf;
import org.joml.Vector3f;

public class ThirdPersonCamera extends Camera {

    private boolean focused = false, hardFocused = true;
    private GameItem focus;
    private final Vector3f cameraPosOnFocus = new Vector3f(30, 0, 0);


        public void updateCamPos() {
        if(!focused) return;
        if(hardFocused) {
            Vector3f posF = focus.getPosition();
            Quaternionf quarS = new Quaternionf(focus.getQuatRotation());
            Vector3f vectorF = new Vector3f(-cameraPosOnFocus.x,10,0);
            vectorF.rotate(quarS);
            Vector3f posC = new Vector3f((posF.x + vectorF.x), (posF.y + vectorF.y), (posF.z + vectorF.z));
            setPosition(posC);

            Quaternionf qe = new Quaternionf();
            qe.rotateAxis((float) Math.toRadians(90), new Vector3f(0,1,0));
            qe.div(quarS);
            setRotation(qe);

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

    public void setHardFocusedOpposite() {
        hardFocused = !hardFocused;
    }

    public void addLengthToFocus() {
        cameraPosOnFocus.x += 1;
    }


    public void subtractLengthToFocus() {
            if(cameraPosOnFocus.x > 2)
        cameraPosOnFocus.x -= 1;
    }
}
