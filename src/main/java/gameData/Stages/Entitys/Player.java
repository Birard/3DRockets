package gameData.Stages.Entitys;

import engine.assets.GameItem;
import engine.assets.Mesh;
import engine.assets.NewMesh;
import org.joml.Quaternionf;
import org.joml.Vector3f;
import org.joml.Quaternionf;
import org.joml.Vector3d;
import org.joml.Vector3f;


public class Player extends GameItem {

    double speedY = 0, speedX = 0, speedZ = 0;
    float power = 10;

    public Player(NewMesh[] mesh1) {
        super(mesh1);
    }

        public double M1=0, M2=0, M3=0;// включены или нет ускорители
        double J1=50, J2=50, J3=50;// силы ускорителей

        public void setControls(double m1, double m2, double m3) {
            M1=m1;
            M2=m2;
            M3=m3;
        }

        public void updateRotate(double deltaT) {

            double W1 = (M1 * J1)*deltaT;
            double W2 = (M2 * J2)*deltaT;
            double W3 = (M3 * J3)*deltaT;

            super.rotationQ.rotateAxis((float)Math.toRadians(W3), new Vector3f(0f, 0f, 1f)).
                    rotateAxis((float)Math.toRadians(W2), new Vector3f(0f, 1f, 0f)).
                    rotateAxis((float)Math.toRadians(W1), new Vector3f(1f, 0f, 0f));

            M1 = 0; M2 = 0; M3 = 0;

        }


        public Vector3f move(double deltaT) {

            Vector3f vectorF = new Vector3f(power, 0, 0);
            vectorF.rotate(rotationQ);

            float k = (float) 0.1;

            speedY = speedY + ((vectorF.y - speedY*k) * deltaT);
            speedX = speedX + ((vectorF.x - speedX*k) * deltaT);
            speedZ = speedZ + ((vectorF.z - speedZ*k) * deltaT);
            Vector3f change = new Vector3f((float)(speedX * deltaT), (float)(speedY * deltaT), (float)(speedZ * deltaT));
            super.position.add(change);
            return change;
        }

}
