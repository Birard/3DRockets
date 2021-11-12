package gameData.Stages.Entitys;

import engine.assets.GameItem;
import engine.assets.NewMesh;
import engine.assets.NewStaticMeshesLoader;
import org.joml.Quaternionf;
import org.joml.Vector3f;

import java.util.ArrayList;

public class TracerManager {

    public ArrayList<GameItem> gameItems = new ArrayList<>();
    private int nowTrace = 0;

    public TracerManager(NewMesh[] mesh1, int n) {
        for(int i = 0; i<n ; i++) {
            GameItem box = new GameItem(mesh1);
            box.setPosition(new Vector3f(0, 0, 0));
            box.setScale(1);
            gameItems.add(box);
        }
    }

    public void createTrace(Vector3f pos, Quaternionf quaternionf) {
        if(nowTrace == gameItems.size()) nowTrace = 0;
        gameItems.get(nowTrace).setPosition(pos);
        gameItems.get(nowTrace).setQuatRotation(new Quaternionf(quaternionf));
        nowTrace++;
    }

    public void resetPoses() {
        for (GameItem gameItem : gameItems) {
            gameItem.setPosition(new Vector3f(0, 0, 0));
            gameItem.setQuatRotation(new Quaternionf());
        }
    }
}
