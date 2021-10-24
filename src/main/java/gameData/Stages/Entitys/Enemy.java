package gameData.Stages.Entitys;


import engine.assets.GameItem;
import engine.assets.NewMesh;
import org.joml.Vector3f;
import org.joml.Vector3d;


public class Enemy extends GameItem {

//    отета с формул
//  private Vector3d speed = new Vector3d(0,0,0);
    private final float power = 100;
    private final float k = 1;



    public Enemy(NewMesh[] mesh1) {
        super(mesh1);
    }


    public float getK() {
        return k;
    }


    public float getPower() {
        return power;
    }


//  отета ленивий варіант
    public void move(double deltaT) {
        Vector3f vectorF = new Vector3f(power, 0, 0);
        vectorF.rotate(rotationQ);
        super.position.add(new Vector3f((float)(vectorF.x * deltaT), (float)(vectorF.y * deltaT), (float)(vectorF.z * deltaT)));
    }


////    отета с формул
//    public void move(double deltaT) {
//
//        Vector3f vectorF = new Vector3f(power, 0, 0);
//        vectorF.rotate(rotationQ);
//
//        speed.y = speed.y + ((vectorF.y - speed.y*k) * deltaT);
//        speed.x = speed.x + ((vectorF.x - speed.x*k) * deltaT);
//        speed.z = speed.z + ((vectorF.z - speed.z*k) * deltaT);
//        super.position.add(new Vector3f((float)(speed.x * deltaT), (float)(speed.y * deltaT), (float)(speed.z * deltaT)));
//    }


    public Vector3d getSpeed() { return new Vector3d(power,0,0); //return speed;
         }

}
