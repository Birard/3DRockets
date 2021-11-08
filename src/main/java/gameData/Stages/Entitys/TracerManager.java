package gameData.Stages.Entitys;

import engine.assets.GameItem;
import engine.assets.NewMesh;
import engine.assets.NewStaticMeshesLoader;
import org.joml.Vector3f;

import java.util.ArrayList;

public class TracerManager {

    public ArrayList<GameItem> gameItems = new ArrayList<>();

    public TracerManager() {
        NewMesh[] mesh = new NewMesh[0];
        try {
            mesh = NewStaticMeshesLoader.load("untitled/untitled.obj", "untitled/whiteBox");
        } catch (Exception e) {
            e.printStackTrace();
        }
        for(int i = 0; i <500; i++) {
            GameItem skyBox = new GameItem(mesh);
            skyBox.setPosition(new Vector3f(3*i, 3, 3));
            gameItems.add(skyBox);
        }
    }
}
