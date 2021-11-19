package gameData.Stages.Entitys;


import org.joml.Quaternionf;
import org.joml.Vector3d;
import org.joml.Vector3f;

public class Calculator {

    public static double calculateTStar(SecondPlayer satellit, FirstPlayer satellit2) {
        Vector3f posF = satellit2.getPosition();
        Vector3f posE = satellit.getPosition();
        Vector3d speedF = satellit2.getSpeed();
        double k = satellit2.getK();
        return Math.sqrt(Math.pow(posE.x - posF.x, 2) +
                Math.pow(posE.y-posF.y, 2) +
                Math.pow(posE.z-posF.z, 2) +
                ( Math.pow(speedF.x,2) +
                        Math.pow(speedF.y,2) +
                        Math.pow(speedF.z,2)
                ) /Math.pow(k,2) -
                2/k * ((posE.x - posF.x)*speedF.x +
                        (posE.y - posF.y)*speedF.y +
                        (posE.z - posF.z)*speedF.z
                ))/ ((satellit2.getPower()/k)-satellit.getPower());
    }


    public static Quaternionf calculateQuaternion(double TStar, SecondPlayer satellit, FirstPlayer satellit2) {
        Vector3f posF = satellit2.getPosition();
        Vector3f posE = satellit.getPosition();
        Vector3d speedF = satellit2.getSpeed();
        double k = satellit2.getK();
        //считаем Q(T*)
                double Q = TStar * ((satellit2.getPower()/k)-satellit.getPower());
                /////////////////////////////////////////////////////////////////////
                //считаем SC
                double SC = ((posE.x - posF.x) - (speedF.x/k)) / Q;
                //считаем C
                double C = ((posE.y - posF.y) - (speedF.y/k)) / Q;
                //считаем SS
                double SS = ((posE.z - posF.z) - (speedF.z/k)) / Q;
                /////////////////////////////////////////////////////////////////////
                //считаем s7 вместо 180 пі
                double s7;
                    if(C < 0) {
                        s7 = 3.1415 - Math.asin(Math.sqrt(Math.pow(SC,2) + Math.pow(SS,2)));
                    } else {
                        s7 = Math.asin(Math.sqrt(Math.pow(SC,2) + Math.pow(SS,2)));
                    }
                //считаем s8
                double s8 = 1;
                if(s7 != 0) {
                    if(SC<0) {
                        s8 = 3.1415 - Math.asin(SS/Math.sin(s7));
                    } else {
                        s8 = Math.asin(SS/Math.sin(s7));
                    }
                }
                /////////////////////////////////////////////////////////////////////
                //повертаем догоняющего
                Quaternionf qe2 = new Quaternionf();
                if (Double.isNaN(s8)) s8 = 0;
                if (Double.isNaN(s7)) s7 = 0;
                qe2.rotateAxis((float)s7, new Vector3f(0,1,0));
                qe2.rotateAxis((float)s8, new Vector3f(1,0,0));
                return qe2;
    }


}
