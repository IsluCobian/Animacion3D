package figures;

import strdatos.Point3D;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Avion extends Figure3D{
    public Avion(Point3D startPoint, BufferedImage buffer) {
        super(startPoint, null);
        auxBuffer = buffer;
        //setRotationVector(new int[]{45,0,45});
        //rotate(1);
    }

    @Override
    public void draw() {
        if (buffer == null || rotating()){
            buffer = new BufferedImage(350,350, BufferedImage.TYPE_INT_ARGB);
            translateMatrix = new double[][]{{1, 0, 0, 0},{0, 1, 0, 0},{0, 0, 1, 0},{0, 0, 0, 1}};
            int centerX = buffer.getWidth()/2;
            int centerY = buffer.getHeight()/2;
            setRotationPoint(new Point3D(centerX,centerY,100));
            //Cuerpo Principal
            color = Color.lightGray;
            drawQuadPrism(new Point3D(centerX + 100,centerY - 25,75), new Point3D(centerX - 150,centerY + 25,75), new Point3D(centerX + 100,centerY - 25,125));
            //Alas
            color = Color.red;
            drawQuadPrism(new Point3D(centerX + 40,centerY - 35,0), new Point3D(centerX - 25,centerY - 15,0), new Point3D(centerX + 40,centerY - 35,200));
            //Cola
            color = Color.red;
            drawQuadPrism(new Point3D(55,centerY - 35,20), new Point3D(25,centerY - 25,20), new Point3D(55,centerY - 35,180));
            //Union Aspa-Cuerpo
            color = Color.decode("#606060");
            drawQuadPrism(new Point3D(centerX + 110,centerY - 5,95), new Point3D(centerX + 100,centerY + 5,95), new Point3D(centerX + 110,centerY - 5,105));
            //Aspas
            color = Color.darkGray;
            drawQuadPrism(new Point3D(centerX + 115,centerY - 30,65), new Point3D(centerX + 110,centerY + 30,65), new Point3D(centerX + 115,centerY - 30,135));
        }
        startPoint = translate(startPoint);
        auxBuffer.getGraphics().drawImage(buffer,startPoint.x,startPoint.y,null);
    }
}
