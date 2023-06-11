package figures2D;

import strdatos.Point;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Polygon{
    private Color color;
    private BufferedImage buffer;
    private Point[] points;

    public Polygon(Point[] points, BufferedImage buffer, Color color) {
        this.points = points;
        this.buffer = buffer;
        this.color = color;
    }

    public void draw() {
        for (int i = 0; i < points.length; i++){
            if ((i + 1) == points.length){
                drawLine(points[i], points[0],color);
            } else {
                drawLine(points[i], points[i + 1], color);
            }
        }
        //new FloodFill(new Point((points[0].x - 1),(points[0].y - 1)),color,buffer);
    }

    private void drawLine(Point startPoint, Point endPoint, Color c) {
        float steps;
        int dy = endPoint.y - startPoint.y;
        int dx = endPoint.x - startPoint.x;
        if (Math.abs(dx) > Math.abs(dy)) {
            steps = Math.abs(dx);
        } else {
            steps = Math.abs(dy);
        }
        float x = startPoint.x, y = startPoint.y;
        putPixel(Math.round(x), Math.round(y), c);
        for (int k = 0; k < steps; k++) {
            x = x + dx/steps;
            y = y + dy/steps;
            putPixel(Math.round(x), Math.round(y), c);
        }
    }

    private void putPixel(int x, int y, Color c) {
        if(x < 0 || x >= buffer.getWidth() || y < 0 || y >= buffer.getHeight()) {
            return;
        }
        buffer.setRGB(x, y, c.getRGB());
    }
}
