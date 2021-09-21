package engine.collision;

import org.joml.Vector3f;

public class AABB {
    private Vector3f center, half_extent;

    public AABB(Vector3f center, Vector3f half_extent) {
        this.center = center;
        this.half_extent = half_extent;
    }

    public Collision getCollision(AABB box3){
        Vector3f distance = box3.center.sub(center, new Vector3f());
        distance.x = Math.abs(distance.x);
        distance.y = Math.abs(distance.y);

        distance.sub(half_extent.add(box3.half_extent, new Vector3f()));

        return new Collision(distance, distance.x < 0 && distance.y < 0);
    }

    public void correctPosition(AABB box3, Collision data) {
        Vector3f correction = box3.center.sub(center, new Vector3f());
        if(data.distance.x > data.distance.y) {
            if(correction.x > 0) {
                center.add(data.distance.x, 0,0);
            } else {
                center.add(-data.distance.x, 0,0);
            }
        } else {
            if(correction.y > 0) {
                center.add(0,  data.distance.y,0);
            } else {
                center.add(0, -data.distance.y,0);
            }
        }
    }

    public Vector3f getCenter() {
        return center;
    }

    public Vector3f getHalf_extent() {
        return half_extent;
    }
}
