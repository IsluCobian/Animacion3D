package figures;

import strdatos.Point;
import strdatos.Point3D;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Misil extends Figure3D{
    private double scale;
    private Color mainColor;


    public Misil(Point3D startPoint, BufferedImage buffer) {
        super(startPoint, null);
        auxBuffer = buffer;
        scale = .4;
        mainColor = Color.RED;
    }

    @Override
    public void draw() {
        //        if (buffer == null || rotating()){
        buffer = new BufferedImage(300,300, BufferedImage.TYPE_INT_ARGB);
        int centerX = buffer.getWidth()/2;
        int centerY = buffer.getHeight()/2;
        setRotationPoint(new Point3D((int) (centerX * scale), (int) (centerY * scale), (int) (100 * scale)));

        // Punta
        color = mainColor;
        drawRectangularPyramid(new Point3D((int) ((centerX + 50) * scale), (int) ((centerY + 25) * scale), (int) (75 * scale)),
                new Point3D((int) ((centerX + 50) * scale), (int) ((centerY - 25) * scale), (int) (120 * scale)),
                new Point3D((int) ((centerX + 120) * scale), (int) ((centerY) * scale), (int) ( 100 * scale)));
        // Cuerpo Principal
        color = Color.lightGray;
        drawQuadPrism(new Point3D((int) ((centerX + 50) * scale), (int) ((centerY - 25) * scale), (int) (75 * scale)),
                new Point3D((int) ((centerX - 100) * scale), (int) ((centerY + 25) * scale), (int) (75 * scale)),
                new Point3D((int) ((centerX + 50) * scale), (int) ((centerY - 25) * scale), (int) (125 * scale)));
        // Cola
        color = Color.darkGray;
        drawQuadPrism(new Point3D((int) ((centerX - 100) * scale), (int) ((centerY - 25) * scale), (int) (75 * scale)),
                new Point3D((int) ((centerX - 120) * scale), (int) ((centerY + 25) * scale), (int) (75 * scale)),
                new Point3D((int) ((centerX - 100) * scale), (int) ((centerY - 25) * scale), (int) (125 * scale)));

        startPoint = translate(startPoint);
        auxBuffer.getGraphics().drawImage(buffer,startPoint.x,startPoint.y,null);
    }

    public void setScale(double scale) {
        this.scale = scale;
    }

    public void setMainColor(Color mainColor) {
        this.mainColor = mainColor;
    }
}
