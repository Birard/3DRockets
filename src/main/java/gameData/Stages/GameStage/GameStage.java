package gameData.Stages.GameStage;

import engine.assets.*;
import engine.game.MainGameStage;
import engine.io.Input;
import engine.io.Timer;
import engine.io.Window;
import engine.render.Renderer;
import engine.render.Cameras.ThirdPersonCamera;
import gameData.Stages.Entitys.Calculator;
import gameData.Stages.Entitys.Enemy;
import gameData.Stages.Entitys.Player;
import org.joml.Quaternionf;
import org.joml.Vector2f;
import org.joml.Vector3f;

import java.util.ArrayList;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL11.GL_COLOR_BUFFER_BIT;

public class GameStage extends MainGameStage {
    private static final float MOUSE_SENSITIVITY = 0.2f;
    public final double frame_cap = 1.0 / 60.0; // в одной секунде 60 кадров
    private GameGui gui;
    private boolean again = true, pause = true, closeWindow = false, auto = true;
    private Renderer renderer;
    private static final Window window = Window.windows;
    private static Player player;
    private static Enemy enemy;
    NewMesh[] playerMesh;

    public GameStage() {
    }

    public void main() throws Exception {
        gui = new GameGui();
        gui.setSize(window.getWidth(), window.getHeight());

        double frame_time = 0;
        int frames = 0;

        double time_2;
        double passed;
        double time = Timer.getTime(), unprocessed = 0;
        boolean can_render;

        Thread.currentThread().setPriority(Thread.MAX_PRIORITY);

        renderer = new Renderer();
//        renderer.setCamera(new ThirdPersonCamera());
        renderer.init(window);

        ArrayList<GameItem> gameItems = new ArrayList<>();

        playerMesh = NewStaticMeshesLoader.load("Satellite/Satellite.obj", "Satellite/text");
        Player satellite0 = new Player(playerMesh);
        satellite0.setPosition(0,0,0);
        satellite0.setScale((float) 2);
        gameItems.add(satellite0);
        player = satellite0;

        NewMesh[] satelliteMesh2 = NewStaticMeshesLoader.load("Torpedo1/Torpedo1.obj", "Torpedo1/text");
        Enemy satellite1 = new Enemy(satelliteMesh2);
        satellite1.setPosition(10,0,0);
        satellite1.setScale(1);
        gameItems.add(satellite1);
        enemy = satellite1;

        NewMesh[] mesh = NewStaticMeshesLoader.load("untitled/untitled.obj", "untitled");
        GameItem skyBox = new GameItem(mesh);
        skyBox.setPosition(renderer.camera.getPosition());
        skyBox.setScale((float) 200);
        gameItems.add(skyBox);

        boolean firsTime = true;

        double timeOnGame = 0;

        while (!closeWindow && !window.windowShouldClose()) {
            can_render = false;
            time_2 = Timer.getTime();
            passed = time_2 - time;
            unprocessed += passed;
            frame_time += passed;
            time = time_2;

            if(window.isResized()){
                glViewport(0,0,window.getWidth(), window.getHeight());
                window.setResized(false);
                gui.setSize(window.getWidth(), window.getHeight());
            }

            if (again) {
                again = false;
                firsTime=true;
                timeOnGame = 0;
            }

            while (unprocessed >= frame_cap) {
                unprocessed -= frame_cap;
                can_render = true;
                gui.update();
                window.update();
            }

            if (frame_time >= 1.0) {
                frame_time = 0;
                System.out.println("FPS: " + frames);
                for(int i = 5; i <= 16; i++) {
                    playerMesh[i].setNeedToRender(false);
                }
                frames = 0;
            }
            if (can_render) {

                /////////////////////////////////////////////////////////////////////
                Vector3f posF = enemy.getPosition();
                Vector3f posE = player.getPosition();
                double TStar = Calculator.calculateTStar(player, enemy);
                if ((firsTime || posE.distance(posF) < 0.5) && !pause) {
                    if(firsTime)System.out.println("* " + TStar);
                    System.out.println(timeOnGame);
                    firsTime = false;
                }
                //повертаем догоняющего
                Quaternionf qe2 = new Quaternionf();
//                qe2 = Calculator.calculateQuaternion(TStar, satellit, satellit2);
                qe2.rotateTo(new Vector3f(1,0,0),new Vector3f((posE.x - posF.x), (posE.y - posF.y), (posE.z - posF.z)));
                enemy.setQuatRotation(qe2);
                if(auto) {player.setQuatRotation(qe2);// для идеаль страт
                     } else {player.updateRotate(frame_cap);}
                if (!pause) {
                    enemy.move(frame_cap);
                    player.move(frame_cap);
                    timeOnGame += frame_cap;
                }
//////////////////////////////////////////////////////////////////////
                //шоб камера за игроком
/////                ((ThirdPersonCamera)renderer.camera).setFocus(satellit);
/////                ((ThirdPersonCamera)renderer.camera).updateRotation();
/////                skyBox.setPosition(renderer.camera.getPosition());
//////////////////////////////////////////////////////////////////////
                glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
                renderer.render(window, gameItems);
                gui.render();
                window.swapBuffers();
                frames++;
            }

        }

    }

    public void keyIsPressed(int key) {
        switch (key) {
            case GLFW_KEY_ESCAPE:
                glfwSetWindowShouldClose(window.getWindow(), true);
                closeWindow = true;
                break;
            case GLFW_KEY_KP_5:
            case GLFW_KEY_5:
                player.setPosition(0,0,0);
                enemy.setPosition(100,1000,100);
/////                ((ThirdPersonCamera)renderer.camera).cameraPosOnFocus = new Vector3f(-20, 10, 0);
                break;
            case GLFW_KEY_KP_ADD:
            case GLFW_KEY_EQUAL:
                renderer.camera.move(0, (float) +0.1,0);
                break;
            case GLFW_KEY_MINUS:
            case GLFW_KEY_KP_SUBTRACT:
                renderer.camera.move(0, (float) -0.1,0);
                break;
            case GLFW_KEY_KP_8:
            case GLFW_KEY_8:
                renderer.camera.move(0,0, (float) -0.1);
                break;
            case GLFW_KEY_KP_2:
            case GLFW_KEY_2:
                renderer.camera.move(0,0, (float) 0.1);
                break;
            case GLFW_KEY_KP_6:
            case GLFW_KEY_6:
                renderer.camera.move((float) 0.1,0,0);
                break;
            case GLFW_KEY_KP_4:
            case GLFW_KEY_4:
                renderer.camera.move((float) -0.1,0,0);
                break;
            case GLFW_KEY_KP_3:
            case GLFW_KEY_3:
                if(Input.isKeyPressed(key)) {
                auto = !auto;
                }
                break;
//            case GLFW_KEY_F:
////                if(renderer.camera.isFocused()) renderer.camera.setFocused(false);
////                else
////                renderer.camera.setFocus(gameItems.get(0).getPosition());
//                satellit2.move(frame_cap);
//                break;
            case GLFW_KEY_S:
                player.setControls(player.M1,player.M2,1);
                playerMesh[9].setNeedToRender(true);
                playerMesh[12].setNeedToRender(true);
                break;
            case GLFW_KEY_W:
                player.setControls(player.M1,player.M2,-1);
                playerMesh[10].setNeedToRender(true);
                playerMesh[11].setNeedToRender(true);
                break;
            case GLFW_KEY_D:
                player.setControls(+1,player.M2,player.M3);
                playerMesh[5].setNeedToRender(true);
                playerMesh[6].setNeedToRender(true);
                break;
            case GLFW_KEY_A:
                player.setControls(-1,player.M2,player.M3);
                playerMesh[7].setNeedToRender(true);
                playerMesh[8].setNeedToRender(true);
                break;
            case GLFW_KEY_E:
                player.setControls(player.M1,-1,player.M3);
                playerMesh[13].setNeedToRender(true);
                playerMesh[15].setNeedToRender(true);
                break;
            case GLFW_KEY_Q:
                player.setControls(player.M1,1,player.M3);
                playerMesh[14].setNeedToRender(true);
                playerMesh[16].setNeedToRender(true);
                break;
            case GLFW_KEY_1:
            case GLFW_KEY_KP_1:
                if(Input.isKeyPressed(key)) {
                    pause = !pause;
                    System.out.println(pause);
                }
                break;
        }
    }


    public void mouseButtonIsPressed(int mouseButton) {
        switch (mouseButton) {
            case GLFW_MOUSE_BUTTON_LEFT:
                if(Input.isMouseButtonPressed(mouseButton)) {
                    int buttonId = gui.updateLeftMouseButtonPress(Input.getMousePos());
                    if (buttonId > -1) {
                        switch (buttonId){
                            case 0:

                                break;
                            case 1:

                                break;
                        }
                    }
                }
                break;
            case GLFW_MOUSE_BUTTON_RIGHT:
                Vector2f rotVec = Input.getDisplVec();
                renderer.camera.moveRotation(rotVec.y * MOUSE_SENSITIVITY, rotVec.x * MOUSE_SENSITIVITY, 0);
            break;
        }
    }

}
