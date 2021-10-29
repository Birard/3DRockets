package engine.game;

import engine.io.Input;
import engine.io.Window;
import gameData.Stages.GameStage.GameStage;

import static org.lwjgl.glfw.GLFW.glfwTerminate;

public class Main {
    public static MainGameStage main;
    public static final double frame_cap = 1.0 / 60.0;

    private Main() {
    }

    public static void main(String[] args) throws Exception {


        Window.windows.setFullscreen(false);
        Window.windows.setSize(1376/2, 768/2);
        Window.windows.createWindow("Game");


        main = new GameStage();
        Input.input.start();
        gameLoop();
    }

    public static void gameLoop() throws Exception {
       while (!Window.windows.windowShouldClose())main.main();
       Input.setAlive(false);
        glfwTerminate();
    }

    public static void keyIsPressed(int key) {
        main.keyIsPressed(key);
    }


    public static void mouseButtonIsPressed(int mouseButton) {
        main.mouseButtonIsPressed(mouseButton);
    }

    public static void setGameStage(MainGameStage stage) {
        main.stop();
        main = stage;
    }
}