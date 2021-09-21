package engine.texture;


import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;

public class TextureManager {
    public static final TextureManager textureManager = new TextureManager();
    private static BufferedImage font;

    private TextureManager() {
    try{
        File file = new File("src/main/resources/ExportedFont.bmp");
        font = ImageIO.read(file);

    } catch (IOException e) {

        System.out.println("Файл не найден или не удалось загрузить");
    }
    }



// использовать чтобы создать файлик с символами
//        Graphics2D g = result.createGraphics();
////        graphics2D.drawString("іїЇіІ",50,50);
//
//        g.setColor(Color.white);
//
//        // determine the parameters of the new font
//
//        int nStyle = Font.PLAIN;
//        int nSize = 64;
//        Font f = new Font("Arial", nStyle, nSize);
//
//        g.setFont(f);
//        g.setColor(Color.black);
//        g.drawString(text, 10, 100);
        // Созраняем результат в новый файл
//            File output = new File("src/main/resources/ExportedFont_grey"+ text +".png");
//        try {
//            ImageIO.write(result, "png", output);
//        } catch (IOException e) {
//            System.out.println("Файл не найден или не удалось сохранить");
//        }
//
//    }

    public static void getTextImage(String text, BufferedImage out) {
        int now_line = 0, now_column = 0;
        for(int i = 0; i<text.length();i++) {
            int sign = text.charAt(i);
            if (sign == '\n') {
                now_line++;
                now_column =0;
                continue;
            }
            if (sign > 255) sign -=848;
            if(sign > 255) sign=0;
            int x = sign%16,  y = sign/16;
            for (int i0 = 0; i0 < 64; i0++) {
                for (int j0 = 0; j0 < 64; j0++) {
                    Color rgb = new Color(font.getRGB(x*64+j0, y*64+i0));

                    if(rgb.getBlue() == 0) {
                        rgb = new Color(30,30,30,255);
                    }

                    out.setRGB( now_column*34+j0, i0+now_line*64, rgb.getRGB());
                }
            }
            now_column++;
        }
    }

    public static BufferedImage getTextImage(String text) {
        int count=1;
        for (char element : text.toCharArray()){
            if (element == '\n') count++;
        }
        BufferedImage result = new BufferedImage((text.length()+1)*34, count*64, font.getType());
        getTextImage(text, result);
        return result;
    }
}
