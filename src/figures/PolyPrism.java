package figures;

import strdatos.Point3D;

import java.awt.image.BufferedImage;

public class PolyPrism extends Figure3D{
    Point3D[] points;
    public PolyPrism(Point3D[] points, BufferedImage buffer) {
        this.points = points;
        this.buffer = buffer;
    }

    @Override
    public void draw() {
        drawPolyPrism(points);
    }

}
