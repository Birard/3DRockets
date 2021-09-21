//package engine.Threads;
//
//import engine.texture.TextureManager;
//
//import java.awt.image.BufferedImage;
//
//public class GetTextPictureThread extends Thread{
//    private final BufferedImage bufferedImage;
//    private String s1;
//
//    public GetTextPictureThread(String s, BufferedImage out) {
//        bufferedImage = out;
//        this.s1 = s;
//    }
//
//    @Override
//    public void run() {
//        Thread.currentThread().setPriority(Thread.MAX_PRIORITY);
//        TextureManager.getTextImage(s1, bufferedImage);
//    }
//}
