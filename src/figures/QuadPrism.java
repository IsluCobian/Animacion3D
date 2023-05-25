package figures;

import strdatos.AnimationFeatures;
import strdatos.Point;
import strdatos.Point3D;

import java.awt.*;
import java.awt.image.BufferedImage;

public class QuadPrism extends Figure3D{
    Point3D A,B,C;

    public QuadPrism(Point3D a, Point3D b, Point3D c, BufferedImage buffer) {
        A = a;
        B = b;
        C = c;
        this.buffer = buffer;
    }

    @Override
    public void draw() {
        drawQuadPrism(A,B,C);
    }


}
