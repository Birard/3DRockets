package gameData.Stages.Entitys;

import org.joml.Quaternionf;
import org.joml.Vector3f;

public class PlayerHandController {
    private double M1=0, M2=0, M3=0;// включены или нет ускорители
    private double J1=250, J2=250, J3=250;// силы ускорителей
    private Quaternionf rotationQ;

    public PlayerHandController(){
        rotationQ = new Quaternionf();
    }

    public void setM1(double m1) {
        M1 = m1;
    }

    public void setM2(double m2) {
        M2 = m2;
    }

    public void setM3(double m3) {
        M3 = m3;
    }

    public void updateRotate(double deltaT) {
        double W1 = (M1 * J1)*deltaT;
        double W2 = (M2 * J2)*deltaT;
        double W3 = (M3 * J3)*deltaT;
        rotationQ.rotateAxis((float)Math.toRadians(W3), new Vector3f(0f, 0f, 1f)).
                rotateAxis((float)Math.toRadians(W2), new Vector3f(0f, 1f, 0f)).
                rotateAxis((float)Math.toRadians(W1), new Vector3f(1f, 0f, 0f));
        M1 = 0; M2 = 0; M3 = 0;
    }

    public Quaternionf getRotationQ() {
        return rotationQ;
    }

    public void setRotationQ(Quaternionf rotationQ) {
        this.rotationQ = rotationQ;
    }
}
