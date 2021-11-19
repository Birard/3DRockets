package gameData.Stages.Entitys;

import engine.assets.GameItem;
import engine.assets.NewMesh;
import org.joml.Vector3f;


public class SecondPlayer extends GameItem {

    private final float power = 10;


    public SecondPlayer(NewMesh[] mesh1) {
        super(mesh1);
    }


    public float getPower() {
        return power;
    }


        public void move(double deltaT) {
            Vector3f vectorF = new Vector3f(power, 0, 0);
            vectorF.rotate(rotationQ);
            super.position.add(new Vector3f((float)(vectorF.x * deltaT), (float)(vectorF.y * deltaT), (float)(vectorF.z * deltaT)));
        }


        public void offAllManeuversRender () {
            for(int i = 5; i <= 16; i++) {
                newMesh[i].setNeedToRender(false);
            }
        }


        public void onManeuverRender(int i) {
            newMesh[i].setNeedToRender(true);
        }
}
