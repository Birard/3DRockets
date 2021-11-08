package gameData.Stages.Entitys;

import engine.assets.GameItem;
import engine.assets.NewMesh;
import org.joml.Vector3f;


public class Player extends GameItem {

    public double M1=0, M2=0, M3=0;// включены или нет ускорители
    double J1=250, J2=250, J3=250;// силы ускорителей
    float power = 30;


    public Player(NewMesh[] mesh1) {
        super(mesh1);
    }


    public void setControls(double m1, double m2, double m3) {
        M1=m1;
        M2=m2;
        M3=m3;
    }


    public float getPower() {
        return power;
    }


    public void updateRotate(double deltaT) {
            double W1 = (M1 * J1)*deltaT;
            double W2 = (M2 * J2)*deltaT;
            double W3 = (M3 * J3)*deltaT;
            super.rotationQ.rotateAxis((float)Math.toRadians(W3), new Vector3f(0f, 0f, 1f)).
                    rotateAxis((float)Math.toRadians(W2), new Vector3f(0f, 1f, 0f)).
                    rotateAxis((float)Math.toRadians(W1), new Vector3f(1f, 0f, 0f));

//                rotationQ.rotateLocalZ((float)Math.toRadians(W3));
//        rotationQ.rotateLocalY((float)Math.toRadians(W2));
//        rotationQ.rotateLocalX((float)Math.toRadians(W1));

            M1 = 0; M2 = 0; M3 = 0;
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
