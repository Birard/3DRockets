package gameData.Stages.MenuStage;

import engine.assets.*;
import engine.game.MainGameStage;
import engine.io.Input;
import engine.io.Timer;
import engine.io.Window;
import engine.render.Camera;
import engine.render.Renderer;
import gameData.Stages.Entitys.Player;
import org.joml.Quaternionf;
import org.joml.Vector2f;
import org.joml.Vector3f;

import java.util.ArrayList;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL11.GL_COLOR_BUFFER_BIT;

public class MenuStage extends MainGameStage {
    private static final float MOUSE_SENSITIVITY = 0.2f;
    public final double frame_cap = 1.0 / 60.0; // в одной секунде 60 кадров
    private MenuGui gui;
    private boolean again = true, pause = true , stop = false;
    private double startTime;
    private ArrayList<GameItem> gameItems;
    private Renderer renderer;
    private static final Window window = Window.windows;
    private static Player satellit;///////////////////////////////////////////////////////
    NewMesh[] satelliteMesh;

    public MenuStage() {

    }

    public void main() throws Exception {
        gui = new MenuGui();
        gui.setSize(window.getWidth(), window.getHeight());

        double frame_time = 0;
        int frames = 0;

        double time_2;
        double passed;

        double timeWastLogik = 0, timeWastRender = 0, savedtime;


        double time = Timer.getTime(), unprocessed = 0;
        startTime = Timer.getTime();
        boolean can_render;

        Thread.currentThread().setPriority(Thread.MAX_PRIORITY);

        float[] positions = new float[] {
                // V0
                -0.5f, 0.5f, 0.5f,
                // V1
                -0.5f, -0.5f, 0.5f,
                // V2
                0.5f, -0.5f, 0.5f,
                // V3
                0.5f, 0.5f, 0.5f,
                // V4
                -0.5f, 0.5f, -0.5f,
                // V5
                0.5f, 0.5f, -0.5f,
                // V6
                -0.5f, -0.5f, -0.5f,
                // V7
                0.5f, -0.5f, -0.5f,

                // For text coords in top face
                // V8: V4 repeated
                -0.5f, 0.5f, -0.5f,
                // V9: V5 repeated
                0.5f, 0.5f, -0.5f,
                // V10: V0 repeated
                -0.5f, 0.5f, 0.5f,
                // V11: V3 repeated
                0.5f, 0.5f, 0.5f,

                // For text coords in right face
                // V12: V3 repeated
                0.5f, 0.5f, 0.5f,
                // V13: V2 repeated
                0.5f, -0.5f, 0.5f,

                // For text coords in left face
                // V14: V0 repeated
                -0.5f, 0.5f, 0.5f,
                // V15: V1 repeated
                -0.5f, -0.5f, 0.5f,

                // For text coords in bottom face
                // V16: V6 repeated
                -0.5f, -0.5f, -0.5f,
                // V17: V7 repeated
                0.5f, -0.5f, -0.5f,
                // V18: V1 repeated
                -0.5f, -0.5f, 0.5f,
                // V19: V2 repeated
                0.5f, -0.5f, 0.5f,
        };

        float[] textCoords = new float[]{
                0.0f, 0.0f,
                0.0f, 0.5f,
                0.5f, 0.5f,
                0.5f, 0.0f,

                0.0f, 0.0f,
                0.5f, 0.0f,
                0.0f, 0.5f,
                0.5f, 0.5f,

                // For text coords in top face
                0.0f, 0.5f,
                0.5f, 0.5f,
                0.0f, 1.0f,
                0.5f, 1.0f,

                // For text coords in right face
                0.0f, 0.0f,
                0.0f, 0.5f,

                // For text coords in left face
                0.5f, 0.0f,
                0.5f, 0.5f,

                // For text coords in bottom face
                0.5f, 0.0f,
                1.0f, 0.0f,
                0.5f, 0.5f,
                1.0f, 0.5f,
        };

        int[] indices = new int[]{
                // Front face
                0, 1, 3, 3, 1, 2,
                // Top Face
                8, 10, 11, 9, 8, 11,
                // Right face
                12, 13, 7, 5, 12, 7,
                // Left face
                14, 15, 6, 4, 14, 6,
                // Bottom face
                16, 18, 19, 17, 16, 19,
                // Back face
                4, 6, 7, 5, 4, 7,};

        renderer = new Renderer();
        renderer.init(window);

        gameItems = new ArrayList<>();

//        Texture texture = new Texture("src/main/resources/8k_stars.jpg");
//        Mesh mesh = new Mesh(positions, textCoords, indices, texture);

        satelliteMesh = NewStaticMeshesLoader.load("src/main/resources/Satelite/Satellite.obj", "src/main/resources/Satelite/text");
        Player satellite0 = new Player(satelliteMesh);
        satellite0.setPosition(0,0,0);
        satellite0.setScale(2);
        gameItems.add(satellite0);
        satellit = satellite0;//////////////////////////////////////////////////

        GameItem satellite1 = new Player(satelliteMesh);
        satellite1.setPosition(0,0,0);
        satellite1.setScale(2);
        gameItems.add(satellite1);//////////////////////////////////////////////////

        NewMesh[] mesh = NewStaticMeshesLoader.load("src/main/resources/untitled.obj", "src/main/resources/");
        GameItem skyBox = new GameItem(mesh);
        skyBox.setPosition(0,-10, -20);
        skyBox.setScale((float) 1);
        gameItems.add(skyBox);
        ////////////////////////////////////////////////////////////////////////
//        renderer.camera.setPosition(0,10,20);
//        renderer.camera.setFocus(gameItems.get(0).getPosition());

        int hidenConus = 5, timerrr = 0;
        satelliteMesh[5].setNeedToRender(false);
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
                satellit.move(frame_cap);

                Vector3f posS = satellit.getPosition();
                Quaternionf quarS = satellit.getQuatRotation();
                Vector3f vectorF = new Vector3f(-20, 10, 0);
                vectorF.rotate(quarS);
                Vector3f posC = new Vector3f((posS.x + vectorF.x), (posS.y + vectorF.y), (posS.z + vectorF.z));
                renderer.camera.setPosition(posC);
                Quaternionf qe = new Quaternionf();
                qe.rotateAxis((float) Math.toRadians(90), new Vector3f(0,1,0));
                qe.div(quarS);
                renderer.camera.setRotation(qe);

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
//            case GLFW_KEY_W:
//                renderer.camera.movePosition(0, 0,(float) -0.1);
//                break;
//            case GLFW_KEY_S:
//                renderer.camera.movePosition(0, 0,(float) 0.1);
//                break;
//            case GLFW_KEY_A:
//                renderer.camera.movePosition((float) -0.1, 0,0);
//                break;
//            case GLFW_KEY_D:
//                renderer.camera.movePosition((float) 0.1, 0,0);
//                break;
//            case GLFW_KEY_LEFT_SHIFT:
//                renderer.camera.movePosition(0, (float) -0.1,0);
//                break;
//            case GLFW_KEY_SPACE:
//                renderer.camera.movePosition(0, (float) 0.1,0);
//                break;
            case GLFW_KEY_N:
//                window.swapBuffers();
//                again = true;
                break;
//            case GLFW_KEY_F:
//                if(renderer.camera.isFocused()) renderer.camera.setFocused(false);
//                else
//                renderer.camera.setFocus(gameItems.get(0).getPosition());
//
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
