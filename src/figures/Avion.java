package figures;

import strdatos.Point3D;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Avion extends Figure3D{
    private double scale;
    private Color mainColor;
    public Avion(Point3D startPoint, BufferedImage buffer) {
        super(startPoint, null);
        auxBuffer = buffer;
        scale = 1;
        mainColor = Color.RED;
        //setRotationVector(new int[]{45,0,45});
        //rotate(1);
    }

    @Override
    public void draw() {
//        if (buffer == null || rotating()){
            buffer = new BufferedImage(400,400, BufferedImage.TYPE_INT_ARGB);
            int centerX = buffer.getWidth()/2;
            int centerY = buffer.getHeight()/2;
            setRotationPoint(new Point3D((int) (centerX * scale), (int) (centerY * scale), (int) (100 * scale)));

            // Cuerpo Principal
            color = Color.lightGray;
            drawQuadPrism(new Point3D((int) ((centerX + 100) * scale), (int) ((centerY - 25) * scale), (int) (75 * scale)),
                    new Point3D((int) ((centerX - 150) * scale), (int) ((centerY + 25) * scale), (int) (75 * scale)),
                    new Point3D((int) ((centerX + 100) * scale), (int) ((centerY - 25) * scale), (int) (125 * scale)));

            // Alas
            color = mainColor;
            drawQuadPrism(new Point3D((int) ((centerX + 40) * scale), (int) ((centerY - 35) * scale), 0),
                    new Point3D((int) ((centerX - 25) * scale), (int) ((centerY - 15) * scale), 0),
                    new Point3D((int) ((centerX + 40) * scale), (int) ((centerY - 35) * scale), (int) (200 * scale)));

            // Cola
            color = mainColor;
            drawQuadPrism(new Point3D((int) ((centerX - 120) * scale), (int) ((centerY - 35) * scale), (int) (40 * scale)),
                    new Point3D((int) ((centerX - 150) * scale), (int) ((centerY - 25) * scale), (int) (40 * scale)),
                    new Point3D((int) ((centerX - 120) * scale), (int) ((centerY - 35) * scale), (int) (160 * scale)));

            // Union Aspa-Cuerpo
            color = Color.decode("#606060");
            drawQuadPrism(new Point3D((int) ((centerX + 110) * scale), (int) ((centerY - 5) * scale), (int) (95 * scale)),
                    new Point3D((int) ((centerX + 100) * scale), (int) ((centerY + 5) * scale), (int) (95 * scale)),
                    new Point3D((int) ((centerX + 110) * scale), (int) ((centerY - 5) * scale), (int) (105 * scale)));

            // Aspas
            color = Color.darkGray;
            drawQuadPrism(new Point3D((int) ((centerX + 120) * scale), (int) ((centerY - 30) * scale), (int) (65 * scale)),
                    new Point3D((int) ((centerX + 110) * scale), (int) ((centerY + 30) * scale), (int) (65 * scale)),
                    new Point3D((int) ((centerX + 120) * scale), (int) ((centerY - 30) * scale), (int) (135 * scale)));
//        }
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
