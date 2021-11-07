package engine.render.Cameras;

import engine.assets.GameItem;
import org.joml.Quaternionf;
import org.joml.Vector3f;

public class ThirdPersonCamera extends Camera {

    boolean focused;
    private GameItem focus;
    public Vector3f cameraPosOnFocus = new Vector3f(-30, 0, 0);
    private Quaternionf rotationOnFocus = new Quaternionf();

    public ThirdPersonCamera () {
        super();
        focused = false;
    }

    public ThirdPersonCamera(Vector3f position, Quaternionf rotation) {
        super(position, rotation);
        focused = false;
    }


        public void updateRotation() {
            Vector3f posF = this.getPosition();
            Vector3f posE = focus.getPosition();
            Quaternionf qe2 = new Quaternionf();
            qe2.rotateTo(new Vector3f(0,0,1),new Vector3f((posE.x - posF.x), (posE.y - posF.y), (posF.z - posE.z)));
            this.setRotation(qe2);

            Vector3f vectorF = new Vector3f(cameraPosOnFocus);
            vectorF.rotate(rotationOnFocus);
            Vector3f posC = new Vector3f((posE.x + vectorF.x), (posE.y + vectorF.y), (posE.z + vectorF.z));
            this.setPosition(posC);
    }


    public void setFocus(GameItem focus) {
        this.focus = focus;
    }

    public void setFocused(boolean focused) {
        this.focused = focused;
    }

    public boolean isFocused() {
        return focused;
    }

}
