package gameData.Stages.GameStage;

import engine.assets.*;
import engine.game.MainGameStage;
import engine.io.Input;
import engine.io.Timer;
import engine.io.Window;
import engine.render.Renderer;
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
    private boolean again = true, pause = true , stop = false;
    private double startTime;
    private double S7=0, S8=0;
    private ArrayList<GameItem> gameItems;
    private Renderer renderer;
    private static final Window window = Window.windows;
    private static Player satellit;///////////////////////////////////////////////////////
    private static Enemy satellit2;
    NewMesh[] satelliteMesh;
    private static Vector3f cameraPosOnSatellite = new Vector3f(-20, 10, 0);
    private static double cameraRotation = 90;
    private static boolean auto = true;

    public GameStage() {

    }

    public void main() throws Exception {
        gui = new GameGui();
        gui.setSize(window.getWidth(), window.getHeight());

        double frame_time = 0;
        int frames = 0;

        double time_2;
        double passed;

        double timeWastLogik = 0, timeWastRender = 0, savedtime;


        double time = Timer.getTime(), unprocessed = 0;
//        startTime = Timer.getTime();
        boolean can_render;

        Thread.currentThread().setPriority(Thread.MAX_PRIORITY);

        renderer = new Renderer();
        renderer.init(window);

        gameItems = new ArrayList<>();

//        Texture texture = new Texture("src/main/resources/8k_stars.jpg");
//        Mesh mesh = new Mesh(positions, textCoords, indices, texture);

        satelliteMesh = NewStaticMeshesLoader.load("Satelite/Satellite.obj", "Satelite/text");
        Player satellite0 = new Player(satelliteMesh);
        satellite0.setPosition(-1000,0,0);
        satellite0.setScale(2);
        gameItems.add(satellite0);
        satellit = satellite0;//////////////////////////////////////////////////

        Enemy satellite1 = new Enemy(satelliteMesh);
        satellite1.setPosition(100,1000,100);
        satellite1.setScale(2);
        gameItems.add(satellite1);
        satellit2 = satellite1;//////////////////////////////////////////////////

        NewMesh[] mesh = NewStaticMeshesLoader.load("untitled/untitled.obj", "untitled");
        GameItem skyBox = new GameItem(mesh);
        skyBox.setPosition(0,-10, -20);
        skyBox.setScale((float) 0.01);
        gameItems.add(skyBox);
        ////////////////////////////////////////////////////////////////////////
//        renderer.camera.setPosition(0,10,20);
//        renderer.camera.setFocus(gameItems.get(0).getPosition());
        boolean firsTime = true;

        int hidenConus = 5, timerrr = 0;
        satelliteMesh[5].setNeedToRender(false);
        startTime = Timer.getTime();
        double starticTime = Timer.getTime();
        //while (!window.shouldClose()) {
        while (!stop && !window.windowShouldClose()) {
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
                startTime = Timer.getTime();
            }



            while (unprocessed >= frame_cap) {
                savedtime = Timer.getTime();
                unprocessed -= frame_cap;
                can_render = true;
                if (!pause) {
                    gui.update();
                    startTime = Timer.getTime();
                } else startTime = Timer.getTime();
                window.update();
                timeWastLogik += Timer.getTime() - savedtime;
            }

            if (frame_time >= 1.0) {
                frame_time = 0;
                System.out.println("FPS: " + frames);
                for(int i = 5; i <= 16; i++) {
                    satelliteMesh[i].setNeedToRender(false);
                }
                frames = 0;
            }
            if (can_render) {
                savedtime = Timer.getTime();




                //////////////////////////////////////////////////////////////////////

                satellit.updateRotate(frame_cap);
//                satellit.move(frame_cap);

                /////////////////////////////////////////////////////////////////////
                Vector3f posF = satellit2.getPosition();
                Vector3f posE = satellit.getPosition();
                double TStar = Calculator.calculateTStar(satellit, satellit2);
                if (firsTime || posE.distance(posF) < 1) {
                    firsTime = false;
                    System.out.println("* " + TStar);
                    System.out.println(Timer.getTime() - starticTime);
//                System.out.println(posE);
                }
                //повертаем догоняющего
                Quaternionf qe2 = new Quaternionf();
//                qe2 = Calculator.calculateQuaternion(TStar, satellit, satellit2);
                qe2.rotateTo(new Vector3f(1,0,0),new Vector3f((posE.x - posF.x), (posE.y - posF.y), (posE.z - posF.z)));
                satellit2.setQuatRotation(qe2);
                if(auto) satellit.setQuatRotation(qe2);// для идеаль страт
                if (pause) {
                    satellit2.move(frame_cap);
                    satellit.move(frame_cap);
                }
//////////////////////////////////////////////////////////////////////
                //шоб камера за игроком
                Quaternionf quarS = new Quaternionf(satellit.getQuatRotation());
                Vector3f vectorF = new Vector3f(cameraPosOnSatellite);
                vectorF.rotate(quarS);
                Vector3f posC = new Vector3f((posE.x + vectorF.x), (posE.y + vectorF.y), (posE.z + vectorF.z));
                renderer.camera.setPosition(posC);
                Quaternionf qe = new Quaternionf();
                qe.rotateAxis((float) Math.toRadians(cameraRotation), new Vector3f(0,1,0));
                qe.div(quarS);
                renderer.camera.setRotation(qe);
//////////////////////////////////////////////////////////////////////
                /////////////////////////////////////////////////////////////////////
                glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

//                if(renderer.camera.isFocused())
//                renderer.camera.setFocus(gameItems.get(0).getPosition());

                // для скайблока щоб бути разом з камерою
//                gameItems.get(gameItems.size()-1).setPosition(renderer.camera.getPosition());

                renderer.render(window, gameItems);

                gui.render();
                window.swapBuffers();

                frames++;
                timeWastRender += Timer.getTime() - savedtime;
            }

        }

    }

    public void keyIsPressed(int key) {
        switch (key) {
            case GLFW_KEY_ESCAPE:
                glfwSetWindowShouldClose(window.getWindow(), true);
                stop = true;
                break;
            case GLFW_KEY_KP_5:
            case GLFW_KEY_5:
                satellit.setPosition(-1000,0,0);
                satellit2.setPosition(100,1000,100);
                cameraPosOnSatellite = new Vector3f(-20, 10, 0);
                cameraRotation = 90;
                break;
            case GLFW_KEY_KP_8:
            case GLFW_KEY_8:
                cameraPosOnSatellite = new Vector3f(30, 10, 0);
                cameraRotation = -90;
                break;
            case GLFW_KEY_KP_2:
            case GLFW_KEY_2:
                cameraPosOnSatellite = new Vector3f(-30, 10, 0);
                cameraRotation = 90;
                break;
            case GLFW_KEY_KP_6:
            case GLFW_KEY_6:
                cameraPosOnSatellite = new Vector3f(0, 10, 30);
                cameraRotation = 0;
                break;
            case GLFW_KEY_KP_4:
            case GLFW_KEY_4:
                cameraPosOnSatellite = new Vector3f(0, 10, -30);
                cameraRotation = 180;
                break;
            case GLFW_KEY_KP_3:
            case GLFW_KEY_3:
                if(Input.isKeyPressed(key)) {
                auto = !auto;
                }
                break;
//            case GLFW_KEY_KP_ADD:
//                cameraPosOnSatellite = new Vector3f(-20, 10, 0);
//                break;
//            case GLFW_KEY_SPACE:
////                renderer.camera.movePosition(0, (float) 0.1,0);
//                satellit.move(frame_cap);
////                System.out.println("dsdsdsd");
//                break;
//            case GLFW_KEY_O:
//                S7+=0.1;
////                renderer.camera.movePosition(0, 0,(float) -0.1);
//                break;
//            case GLFW_KEY_L:
//                S8+=0.1;
////                renderer.camera.movePosition(0, 0,(float) -0.1);
//                break;
//            case GLFW_KEY_N:
////                window.swapBuffers();
////                again = true;
//                break;
//            case GLFW_KEY_F:
////                if(renderer.camera.isFocused()) renderer.camera.setFocused(false);
////                else
////                renderer.camera.setFocus(gameItems.get(0).getPosition());
//                satellit2.move(frame_cap);
//                break;
            case GLFW_KEY_S:
                satelliteMesh[9].setNeedToRender(true);
                satelliteMesh[12].setNeedToRender(true);
                satellit.setControls(satellit.M1,satellit.M2,1);
                break;
            case GLFW_KEY_W:
                satelliteMesh[10].setNeedToRender(true);
                satelliteMesh[11].setNeedToRender(true);
                satellit.setControls(satellit.M1,satellit.M2,-1);
                break;
            case GLFW_KEY_D:
                satelliteMesh[5].setNeedToRender(true);
                satelliteMesh[6].setNeedToRender(true);
                satellit.setControls(+1,satellit.M2,satellit.M3);
                break;
            case GLFW_KEY_A:
                satelliteMesh[7].setNeedToRender(true);
                satelliteMesh[8].setNeedToRender(true);
                satellit.setControls(-1,satellit.M2,satellit.M3);
                break;
            case GLFW_KEY_E:
                satelliteMesh[13].setNeedToRender(true);
                satelliteMesh[15].setNeedToRender(true);
                satellit.setControls(satellit.M1,-1,satellit.M3);
                break;
            case GLFW_KEY_Q:
                satelliteMesh[14].setNeedToRender(true);
                satelliteMesh[16].setNeedToRender(true);
                satellit.setControls(satellit.M1,1,satellit.M3);
                break;

//            case GLFW_KEY_KP_5:
//            case GLFW_KEY_5:
//                if(Input.isKeyPressed(key)) {
//                    satelliteMesh[5].setNeedToRender(false);
//                    satelliteMesh[6].setNeedToRender(false); satelliteMesh[11].setNeedToRender(false);
//                    satelliteMesh[7].setNeedToRender(false); satelliteMesh[13].setNeedToRender(false);
//                    satelliteMesh[8].setNeedToRender(false); satelliteMesh[15].setNeedToRender(false);
//                    satelliteMesh[9].setNeedToRender(false); satelliteMesh[14].setNeedToRender(false);
//                    satelliteMesh[12].setNeedToRender(false); satelliteMesh[16].setNeedToRender(false);
//                    satelliteMesh[10].setNeedToRender(false);
//                    gameItems.get(0).setQuatRotation(new Quaternionf(0,0,0,1));
//                }
//                break;
            case GLFW_KEY_1:
            case GLFW_KEY_KP_1:
                if(Input.isKeyPressed(key)) {
                    pause = !pause;
                    System.out.println(pause);
                }
                break;
        }
    }

    public void stop(){
        stop = true;
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
