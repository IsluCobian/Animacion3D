package figures;

import strdatos.AnimationFeatures;
import strdatos.Point;
import strdatos.Point3D;

import java.awt.*;
import java.awt.image.BufferedImage;

public class QuadPrism extends Figure3D{

    public QuadPrism(Point3D startPoint, Point3D middlePoint, Point3D endPoint, BufferedImage buffer) {
        super(startPoint, middlePoint, endPoint, buffer);
    }

    @Override
    public void draw() {
        drawQuadPrism(startPoint,middlePoint,endPoint);
    }


}
