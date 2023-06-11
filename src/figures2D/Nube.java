package figures2D;

import figures.FloodFill;
import strdatos.Point;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Nube extends Figure{

    public Nube(Point startPoint, Point endPoint, BufferedImage buffer, Color color) {
        super(startPoint, endPoint, buffer, color);
    }

    @Override
    public void draw() {
        int radioX = endPoint.x - startPoint.x;
        int radioY = endPoint.y - startPoint.y;
        drawEllipse(startPoint,radioX,radioY);
        new FloodFill(startPoint, color, buffer);
        drawEllipse(new Point(startPoint.x + 3*(radioX/2), startPoint.y),radioX,radioY);
        new FloodFill(new Point(startPoint.x + 3*(radioX/2), startPoint.y), color, buffer);
        drawEllipse(new Point(startPoint.x + (radioX/2), startPoint.y - radioY),radioX,radioY);
        new FloodFill(new Point(startPoint.x + (radioX/2), startPoint.y - radioY), color, buffer);
    }
}
