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
import gameData.Stages.Entitys.TracerManager;
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
    private boolean again = true, pause = true, closeWindow = false, auto = true, firsTime = true;;
    private Renderer renderer;
    private static final Window window = Window.windows;
    private static Player player;
    public static Enemy enemy;
    private static TracerManager playerTraces;
    private static TracerManager enemyTraces;
    public ArrayList<GameItem> gameItems = new ArrayList<>();

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
        renderer.setCamera(new ThirdPersonCamera());
        renderer.init(window);

        NewMesh[] playerMesh = NewStaticMeshesLoader.load("Satellite.obj", "Satellite/text");
        Player satelliteP = new Player(playerMesh);
        satelliteP.setPosition(0,0,0);
        satelliteP.setScale((float) 2);
        gameItems.add(satelliteP);
        player = satelliteP;
        ((ThirdPersonCamera)renderer.camera).setFocus(player);
        ((ThirdPersonCamera)renderer.camera).setFocusedOpposite();
        renderer.camera.setPosition(1,0,0);


        NewMesh[] enemyMesh = NewStaticMeshesLoader.load("Torpedo1/Torpedo1.obj", "Torpedo1/text");
        Enemy torpedoEn = new Enemy(enemyMesh);
        torpedoEn.setPosition(10,0,0);
        torpedoEn.setScale(1);
        gameItems.add(torpedoEn);
        enemy = torpedoEn;

//        NewMesh[] mesh = NewStaticMeshesLoader.load("untitled/untitled.obj", "untitled/skyBox");
//        GameItem skyBox = new GameItem(mesh);
//        skyBox.setPosition(renderer.camera.getPosition());
//        skyBox.setScale((float) 400);
//        gameItems.add(skyBox);

        NewMesh[] mesh = NewStaticMeshesLoader.load("arrow.obj", "untitled/whiteBox");
        playerTraces = new TracerManager(mesh);
        gameItems.addAll(playerTraces.gameItems);

        NewMesh[] mesh2 = NewStaticMeshesLoader.load("arrow.obj", "untitled/blackBox");
        enemyTraces = new TracerManager(mesh2);
        gameItems.addAll(enemyTraces.gameItems);

        double timeOnGame = 0;

        int skippedFrames = 0;

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
                player.offAllManeuversRender();
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
                    if(skippedFrames == 3)enemyTraces.createTrace(enemy.getPosition(), enemy.getQuatRotation());
                    if(skippedFrames == 6) {
                        playerTraces.createTrace(player.getPosition(), player.getQuatRotation());
                        enemyTraces.createTrace(enemy.getPosition(), enemy.getQuatRotation());
                        skippedFrames = 0;
                    } else skippedFrames++;
                }
//////////////////////////////////////////////////////////////////////
                //шоб камера за игроком
//                ((ThirdPersonCamera)renderer.camera).setFocus(player);
                ((ThirdPersonCamera)renderer.camera).updateCamPos();
//                skyBox.setPosition(renderer.camera.getPosition());
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
                enemy.setPosition(100,100,100);
                playerTraces.resetPoses();
                enemyTraces.resetPoses();
                firsTime = true;
                break;
            case GLFW_KEY_KP_ADD:
            case GLFW_KEY_EQUAL:
                ((ThirdPersonCamera) renderer.camera).addLengthToFocus();
                break;
            case GLFW_KEY_MINUS:
            case GLFW_KEY_KP_SUBTRACT:
                ((ThirdPersonCamera) renderer.camera).subtractLengthToFocus();
                break;
            case GLFW_KEY_I:
                renderer.camera.move(0, (float) +0.1,0);
                break;
            case GLFW_KEY_K:
                renderer.camera.move(0, (float) -0.1,0);
                break;
            case GLFW_KEY_Y:
                renderer.camera.move(0,0, (float) -0.1);
                break;
            case GLFW_KEY_H:
                renderer.camera.move(0,0, (float) 0.1);
                break;
            case GLFW_KEY_J:
                renderer.camera.move((float) 0.1,0,0);
                break;
            case GLFW_KEY_G:
                renderer.camera.move((float) -0.1,0,0);
                break;
            case GLFW_KEY_T:
                renderer.camera.moveRotation(0, 0, -1);
                break;
            case GLFW_KEY_U:
                renderer.camera.moveRotation(0, 0, 1);
                break;
            case GLFW_KEY_Z:
                if(Input.isKeyPressed(key)) {
                auto = !auto;
                }
                break;
            case GLFW_KEY_P:
                if(Input.isKeyPressed(key)) {
                    pause = !pause;
                    System.out.println(pause);
                }
                break;
            case GLFW_KEY_F:
                if(Input.isKeyPressed(key)) {
                    ((ThirdPersonCamera) renderer.camera).setFocusedOpposite();
                }
                break;
            case GLFW_KEY_V:
                if(Input.isKeyPressed(key)) {
                    ((ThirdPersonCamera) renderer.camera).setHardFocusedOpposite();
                }
                break;
            case GLFW_KEY_S:
                player.setControls(player.M1,player.M2,1);
                player.onManeuverRender(9-2);
                player.onManeuverRender(12-2);
                break;
            case GLFW_KEY_W:
                player.setControls(player.M1,player.M2,-1);
                player.onManeuverRender(10-2);
                player.onManeuverRender(11-2);
                break;
            case GLFW_KEY_D:
                player.setControls(+1,player.M2,player.M3);
                player.onManeuverRender(5-2);
                player.onManeuverRender(6-2);
                break;
            case GLFW_KEY_A:
                player.setControls(-1,player.M2,player.M3);
                player.onManeuverRender(7-2);
                player.onManeuverRender(8-2);
                break;
            case GLFW_KEY_E:
                player.setControls(player.M1,-1,player.M3);
                player.onManeuverRender(13-2);
                player.onManeuverRender(15-2);
                break;
            case GLFW_KEY_Q:
                player.setControls(player.M1,1,player.M3);
                player.onManeuverRender(14-2);
                player.onManeuverRender(16-2);
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
