package controllers.utils;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;

public class ImageUtils {
    public static BufferedImage scaleImageKeepRelations(BufferedImage img, int newWidth) {
        int oriWidth = img.getWidth();
        int oriHeight = img.getHeight();

        double scale = newWidth / (float) oriWidth;
        int newHeight = (int)(oriHeight * scale);

        Image scaledImg = img.getScaledInstance(newWidth,newHeight,Image.SCALE_SMOOTH);
        
        BufferedImage fImage = new BufferedImage(newWidth, newHeight, BufferedImage.TYPE_INT_ARGB);

        Graphics g = fImage.createGraphics();
        g.drawImage(scaledImg, 0, 0, new Color(0,0,0), null);

        return fImage;
    }
}
