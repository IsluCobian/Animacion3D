/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package figures2D;

import figures.FloodFill;
import strdatos.Point;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 *
 * Autor: Luis Cobian
 * Registro: 20310016
 */
public class Rectangulo extends Figure{
    public Rectangulo(Point startPoint, Point endPoint, BufferedImage buffer, Color color) {
        super(startPoint, endPoint, buffer, color);
    }
    public Rectangulo(Point startPoint, Point endPoint, BufferedImage buffer, Color color, boolean steps) {
        super(startPoint, endPoint, buffer, color, steps);
    }

    public Rectangulo(Point startPoint, Point endPoint, BufferedImage buffer, Color color, int ang) {
        super(startPoint, endPoint, buffer, color, ang);
    }
    

    @Override
    public void draw() {
        drawRec(startPoint, endPoint);
        new FloodFill(getFloodPoint(), color, buffer);
    }
}
