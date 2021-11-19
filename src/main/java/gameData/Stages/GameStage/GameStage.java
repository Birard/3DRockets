package gameData.Stages.GameStage;

import engine.assets.*;
import engine.game.MainGameStage;
import engine.io.Input;
import engine.io.Timer;
import engine.io.Window;
import engine.render.Renderer;
import engine.render.Cameras.ThirdPersonCamera;
import gameData.Stages.Entitys.*;
import org.joml.Quaternionf;
import org.joml.Vector2f;
import org.joml.Vector3f;

import java.util.ArrayList;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL11.GL_COLOR_BUFFER_BIT;

public class GameStage extends MainGameStage {
    private static final float MOUSE_SENSITIVITY = 0.2f;
    public static final double frame_cap = 1.0 / 60.0; // в одной секунде 60 кадров
    private static GameGui gui;
    private static boolean again = true, pause = true, closeWindow = false, auto = true, endGame = false, showHelp = false, handControlSecondPlayer = true;
    private static Renderer renderer;
    private static final Window window = Window.windows;
    private static SecondPlayer secondPlayer;
    private static FirstPlayer firstPlayer;
    private static PlayerHandController handController = new PlayerHandController();
    private static TracerManager secondPlayerTraces;
    private static TracerManager firstPlayerTraces;
    private static ArrayList<GameItem> gameItems = new ArrayList<>();

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

        NewMesh[] secondPlayerMesh = NewStaticMeshesLoader.load("Satellite/Satellite.obj", "Satellite/text");
        SecondPlayer satelliteS = new SecondPlayer(secondPlayerMesh);
        satelliteS.setPosition(0,0,0);
        satelliteS.setScale((float) 2);
        gameItems.add(satelliteS);
        secondPlayer = satelliteS;
        ((ThirdPersonCamera)renderer.camera).setFocus(secondPlayer);
        ((ThirdPersonCamera)renderer.camera).setFocused(true);
        renderer.camera.setPosition(1,0,0);


        NewMesh[] firstPlayerMesh = NewStaticMeshesLoader.load("Torpedo1/Torpedo1.obj", "Torpedo1/text");
        FirstPlayer torpedoF = new FirstPlayer(firstPlayerMesh);
        torpedoF.setPosition(10,0,0);
        torpedoF.setScale(1);
        gameItems.add(torpedoF);
        firstPlayer = torpedoF;

        NewMesh[] helpMesh = NewStaticMeshesLoader.load("Plane/Plane.obj", "Plane/text");
        GameItem helpBox = new GameItem(helpMesh);
        helpBox.setScale((float) 1);
        gameItems.add(helpBox);

        NewMesh[] pauseMesh = NewStaticMeshesLoader.load("Plane/Plane.obj", "Plane/pause");
        GameItem pauseBox = new GameItem(pauseMesh);
        pauseBox.setScale((float) 0.1);
        gameItems.add(pauseBox);
        pauseMesh[0].setNeedToRender(false);

        NewMesh[] whiteArrow = NewStaticMeshesLoader.load("arrow.obj", "Box/whiteBox");
        secondPlayerTraces = new TracerManager(whiteArrow, 300);
        gameItems.addAll(secondPlayerTraces.gameItems);

        NewMesh[] blackArrow = NewStaticMeshesLoader.load("arrow.obj", "Box/blackBox");
        firstPlayerTraces = new TracerManager(blackArrow, 600);
        gameItems.addAll(firstPlayerTraces.gameItems);

        double timeOnGame = 0;

        int skippedFrames = 0;
int i = 0;
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
                endGame = false;
                timeOnGame = 0;
                secondPlayer.setPosition(0,0,0);
                firstPlayer.setPosition(200,200,200);
                secondPlayerTraces.resetPoses();
                firstPlayerTraces.resetPoses();
                ((ThirdPersonCamera)renderer.camera).setFocused(true);
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
                secondPlayer.offAllManeuversRender();
                frames = 0;
            }
            if (can_render) {
                if(showHelp){
                    i++;
                    pauseMesh[0].setNeedToRender(false);
                    helpMesh[0].setNeedToRender(true);
                    Quaternionf qe2 = new Quaternionf();
                    qe2.div(renderer.camera.getRotation());
                    helpBox.setQuatRotation(qe2);
                    Vector3f camPos = renderer.camera.getPosition();
                    Vector3f vectorF = new Vector3f(0, (float) 0,-1);
                    vectorF.rotate(qe2);
                    qe2.rotateY((float) Math.toRadians(-90));
                    qe2.rotateX((float) Math.toRadians(90));
                    qe2.rotateZ((float) Math.toRadians(0));
                    Vector3f newPosC = new Vector3f((camPos.x + vectorF.x), (camPos.y + vectorF.y), (camPos.z + vectorF.z));
                    helpBox.setPosition(newPosC);
                } else {
                    helpMesh[0].setNeedToRender(false);
                    if (!endGame) {
                        Vector3f posF = firstPlayer.getPosition();
                        Vector3f posE = secondPlayer.getPosition();
                        if (timeOnGame == 0 && !pause) {
                            double TStar = Calculator.calculateTStar(secondPlayer, firstPlayer) - 1.2;
                            System.out.println("* " + TStar);
                        }
                        if (posE.distance(posF) < 0.1 && !endGame) {
                            timeOnGame += frame_cap;
                            System.out.println(timeOnGame);
                            endGame = true;
                        }
                        //повертаем догоняющего
                        Quaternionf qe2 = new Quaternionf();
                        qe2.rotateTo(new Vector3f(1, 0, 0), new Vector3f((posE.x - posF.x), (posE.y - posF.y), (posE.z - posF.z)));

                        if(handControlSecondPlayer) {
                            firstPlayer.setQuatRotation(qe2);
                        } else  secondPlayer.setQuatRotation(qe2);

                        if (auto) {
                            if(handControlSecondPlayer) {
                                secondPlayer.setQuatRotation(qe2);
                                handController.setRotationQ(secondPlayer.getQuatRotation());
                            } else  {
                                firstPlayer.setQuatRotation(qe2);
                                handController.setRotationQ(firstPlayer.getQuatRotation());
                            }
                        } else {
                            handController.updateRotate(frame_cap);
                        }
                        if (!pause) {
                            firstPlayer.move(frame_cap);
                            secondPlayer.move(frame_cap);
                            timeOnGame += frame_cap;
                            if (skippedFrames == 3)
                                firstPlayerTraces.createTrace(firstPlayer.getPosition(), firstPlayer.getQuatRotation());
                            if (skippedFrames == 6) {
                                secondPlayerTraces.createTrace(secondPlayer.getPosition(), secondPlayer.getQuatRotation());
                                firstPlayerTraces.createTrace(firstPlayer.getPosition(), firstPlayer.getQuatRotation());
                                skippedFrames = 0;
                            } else skippedFrames++;
                            pauseMesh[0].setNeedToRender(false);
                        }
                        //шоб камера за игроком
//                ((ThirdPersonCamera)renderer.camera).setFocus(player);
                        ((ThirdPersonCamera) renderer.camera).updateCamPos();
                        if(pause) {
                            pauseMesh[0].setNeedToRender(true);
                            Quaternionf qe3 = new Quaternionf();
                            qe3.div(renderer.camera.getRotation());
                            Vector3f camPos = renderer.camera.getPosition();
                            Vector3f vectorF = new Vector3f(0, (float) 0,-1);
                            vectorF.rotate(qe3);
                            qe3.rotateY((float) Math.toRadians(90));
                            Vector3f newPosC = new Vector3f((camPos.x + vectorF.x), (camPos.y + vectorF.y), (camPos.z + vectorF.z));
                            pauseBox.setPosition(newPosC);
//                            qe3.rotateX((float) Math.toRadians(90));
                            pauseBox.setQuatRotation( qe3);
                        }
                    } else {
                        ((ThirdPersonCamera) renderer.camera).setFocused(false);
                    }
                }
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
                if(Input.isKeyPressed(key)) {
                    again = true;
                }
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
                }
                break;
            case GLFW_KEY_F:
                if(Input.isKeyPressed(key)) {
                    ((ThirdPersonCamera) renderer.camera).setFocusedOpposite();
                }
                break;
            case GLFW_KEY_R:
                if(Input.isKeyPressed(key)) {
                    showHelp = !showHelp;
                    pause = true;
                }
                break;
            case GLFW_KEY_V:
                if(Input.isKeyPressed(key)) {
                    ((ThirdPersonCamera) renderer.camera).setHardFocusedOpposite();
                }
                break;
            case GLFW_KEY_S:
                handController.setM3(1);
                if(handControlSecondPlayer) {
                    secondPlayer.onManeuverRender(7);
                    secondPlayer.onManeuverRender(8);
                }
                break;
            case GLFW_KEY_W:
                handController.setM3(-1);
                if(handControlSecondPlayer) {
                    secondPlayer.onManeuverRender(5);
                    secondPlayer.onManeuverRender(6);
                }
                break;
            case GLFW_KEY_D:
                handController.setM1(+1);
                if(handControlSecondPlayer) {
                    secondPlayer.onManeuverRender(13);
                    secondPlayer.onManeuverRender(14);
                }
                break;
            case GLFW_KEY_A:
                handController.setM1(-1);
                if(handControlSecondPlayer) {
                    secondPlayer.onManeuverRender(15);
                    secondPlayer.onManeuverRender(16);
                }
                break;
            case GLFW_KEY_E:
                handController.setM2(-1);
                if(handControlSecondPlayer) {
                    secondPlayer.onManeuverRender(9);
                    secondPlayer.onManeuverRender(10);
                }
                break;
            case GLFW_KEY_Q:
                handController.setM2(1);
                if(handControlSecondPlayer) {
                    secondPlayer.onManeuverRender(11);
                    secondPlayer.onManeuverRender(12);
                }
                break;
            case GLFW_KEY_C:
                if(Input.isKeyPressed(key)) {
                    handControlSecondPlayer = !handControlSecondPlayer;
                    if(handControlSecondPlayer) {
                        handController.setRotationQ(secondPlayer.getQuatRotation());
                        ((ThirdPersonCamera)renderer.camera).setFocus(secondPlayer);
                    } else {
                        handController.setRotationQ(firstPlayer.getQuatRotation());
                        ((ThirdPersonCamera)renderer.camera).setFocus(firstPlayer);
                    }
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
