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
public class Circulo extends Figure{

    public Circulo(Point startPoint, Point endPoint, BufferedImage buffer, Color color) {
        super(startPoint, endPoint, buffer, color);
    }

    public Circulo(Point startPoint, Point endPoint, BufferedImage buffer, Color color, int ang) {
        super(startPoint, endPoint, buffer, color, ang);
    }

    @Override
    public void draw() {
        drawEllipse(startPoint,endPoint.x - startPoint.x,endPoint.y - startPoint.y);
        new FloodFill(startPoint, color, buffer);
    }
}
