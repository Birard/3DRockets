package gameData.Stages.MenuStage;

import org.joml.Quaternionf;
import org.joml.Vector3d;
import org.joml.Vector3f;

public class Satellit {

    Quaternionf quaternionf = new Quaternionf(0,0,0,1);

    double M1=0, M2=0, M3=0;// включены или нет ускорители

    double W1=0, W2=0, W3=0;// угловая скорость

    double J1=0.5, J2=0.5, J3=0.5;// силы ускорителей


    public void setControls(double m1, double m2, double m3) {
        M1=m1;
        M2=m2;
        M3=m3;
    }

    public Quaternionf calculation(double deltaT) {

        double W01 = W1 + (M1/J1 + ((J2-J3)/J1)*W2*W3)*deltaT;
        double W02 = W2 + (M2/J2 + ((J3-J1)/J2)*W3*W1)*deltaT;
        double W03 = W3 + (M3/J3 + ((J1-J2)/J3)*W1*W2)*deltaT;

        W1=W01; W2=W02; W3=W03;

        quaternionf.x = 0;
        quaternionf.y = 0;
        quaternionf.z = 0;
        quaternionf.w = 1;

        quaternionf.rotateAxis((float)Math.toRadians(W3), new Vector3f(0f, 0f, 1f)).
                rotateAxis((float)Math.toRadians(W2), new Vector3f(0f, 1f, 0f)).
                rotateAxis((float)Math.toRadians(W1), new Vector3f(1f, 0f, 0f));

        M1 = 0; M2 = 0; M3 = 0;

        return quaternionf;
    }

}
