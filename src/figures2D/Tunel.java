package figures2D;

import figures.FloodFill;
import strdatos.Point;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Tunel extends Figure{
    private BufferedImage bufferMain;
    public Tunel(Point startPoint, BufferedImage buffer) {
        super(startPoint);
        bufferMain = buffer;
    }

    @Override
    public void draw() {
        if (buffer == null){
            buffer = new BufferedImage(810, 500, BufferedImage.TYPE_INT_ARGB);
            color = Color.gray;
            drawRec(new Point(0,0),new Point(30,500));
            new FloodFill(new Point(15, 250), color, buffer);
            drawRec(new Point(780,0),new Point(810,500));
            new FloodFill(new Point(795, 250), color, buffer);
            color = Color.black;
            drawRec(new Point(30,0),new Point(780,500));
            new FloodFill(new Point(450, 250), color, buffer);
        }
        bufferMain.getGraphics().drawImage(buffer,startPoint.x,startPoint.y,null);
    }
}
