package engine.texture;

import engine.assets.Texture;
import engine.io.Timer;

public class Animation {
    private Texture[] frames;
    private int pointer;

    private double elapsedTime;
    private double currentTime;
    private double lastTime;
    private double fps;

    public Animation(int amount, int fps, String filename) {
        pointer = 0;
        elapsedTime = 0;
        currentTime = 0;
        lastTime = Timer.getTime();
        this.fps = 1.0/((double)fps);

        this.frames = new Texture[amount];
        for(int i = 0; i < amount; i++) {
            try {
                this.frames[i] = new Texture("anim/" + filename + i + ".png");
            } catch (Exception e) {
                e.printStackTrace();
            }
            ////////  this.frames[i] = TextureManager.getTexture("src/main/resources/player/scraps/" + filename + i + ".png");
        }
    }

    public void bind() { bind(0);}

    public void bind(int sampler) {
        currentTime = Timer.getTime();
        elapsedTime += currentTime - lastTime;

        if(elapsedTime >= fps) {
            elapsedTime -= fps;
            pointer++;
        }

        if(pointer >= frames.length) pointer = 0;

        lastTime = currentTime;

        frames[pointer].bind(sampler);
    }
}
