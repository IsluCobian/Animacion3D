package figures;/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import strdatos.Point;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Stack;

/**
 * Autor: Luis Cobian
 * Registro: 20310016
 */
public class FloodFill {
        
        private BufferedImage buffer;
        private Point startPoint;
        private int fill, c;

        public FloodFill(Point startPoint, Color c, BufferedImage buffer) {
            this.startPoint = startPoint;
            this.c = c.getRGB();
            this.buffer = buffer;
            if (startPoint.x < 0) {
                floodFill(0, startPoint.y);
            } else if (startPoint.x >= buffer.getWidth()) {
                floodFill(buffer.getWidth() - 1, startPoint.y);
            } else if (startPoint.y < 0) {
                floodFill(startPoint.x, 0);
            } else if (startPoint.y >= buffer.getHeight()) {
                floodFill(startPoint.x, buffer.getHeight() - 1);
            } else {
                floodFill(startPoint.x, startPoint.y);
            }
        }
        
        //Flood Fill con Stack
        /*Permite que en vez de estar revisando cada pixel*/
        private void floodFill(int x, int y){
            Stack<Point> stack = new Stack<>();
            stack.push(new Point(x, y));

            while(!stack.isEmpty()) {
                Point p = stack.pop();

                //Revisa que no se pasen las dimesiones del canvas
                if(p.x < 0 || p.x >= buffer.getWidth() || p.y < 0 || p.y >= buffer.getHeight()) {
                    continue;
                }

                //Revisa que el pixel si el pixel ya es del color tanto que sea del color a rellenar
                if(buffer.getRGB(p.x, p.y) != c) {

                    buffer.setRGB(p.x, p.y, c);

                    stack.push(new Point(p.x - 1, p.y));
                    stack.push(new Point(p.x + 1, p.y));
                    stack.push(new Point(p.x, p.y - 1));
                    stack.push(new Point(p.x, p.y + 1));
                }
            }
        }
    }
