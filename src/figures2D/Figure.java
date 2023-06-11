/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package figures2D;

import strdatos.AnimationFeatures2D;
import strdatos.Point;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * Autor: Luis Cobian
 * Registro: 20310016
 */

public abstract class Figure {
    protected Point startPoint, endPoint, rotationPoint;
    protected BufferedImage buffer;
    private int ang;
    protected Color color;
    protected boolean steps = false;
    private AnimationFeatures2D animationFeatures;
    protected BufferedImage bufferMain;
    

    public Figure(Point startPoint, Point endPoint, BufferedImage buffer, Color color) {
        this.startPoint = startPoint;
        this.endPoint = endPoint;
        this.buffer = buffer;
        this.color = color;
        animationFeatures = new AnimationFeatures2D();
        bufferMain = null;
        rotationPoint = new Point(0,0);
        ang = 0;
    }

    public Figure(Point startPoint, BufferedImage buffer) {
        this.startPoint = startPoint;
        this.buffer = buffer;
        this.endPoint = new Point(0,0);
        animationFeatures = new AnimationFeatures2D();
        rotationPoint = new Point(0,0);
        ang = 0;
    }
    public Figure(Point startPoint) {
        this.startPoint = startPoint;
        this.buffer = null;
        this.endPoint = new Point(0,0);
        animationFeatures = new AnimationFeatures2D();
        rotationPoint = new Point(0,0);
        ang = 0;
    }

    public Figure(Point startPoint, Point endPoint, BufferedImage buffer, Color color, boolean steps) {
        this(startPoint, endPoint, buffer, color);
        this.steps = steps;
    }

    public Figure(Point startPoint, Point endPoint, BufferedImage buffer, Color color, int ang) {
        this(startPoint, endPoint, buffer, color);
        this.ang = ang;
    }
    
    public abstract void draw();
    
    public void translate(int tiempo){
        int [] trans = animationFeatures.getTrasValues();
        
        double[][] translateMatrix = {{1, 0, trans[0]/tiempo},{0, 1, trans[1]/tiempo},{0, 0, 1}};
    
        Point[] points = {getStart(), getEnd()};
        for (Point p : points) {
            double[] figurePosition = {p.x, p.y, 1};
            double[] newPosition = matrixMultiply(translateMatrix, figurePosition);
            p.x = (int) newPosition[0];
            p.y = (int) newPosition[1];
        }
    }
    
    public void scalate(int tiempo){
        double [] scale = animationFeatures.getScaleValues();
   
        double centerX = (getStart().x + getEnd().x) / 2.0;
        double centerY = (getStart().y + getEnd().y) / 2.0;

        double[][] scalateMatrix = {{1+scale[0]/tiempo, 0, 0}, {0, 1+scale[1]/tiempo, 0}, {0, 0, 1}};
        double[] start = {getStart().x - centerX, getStart().y - centerY, 1};
        double[] end = {getEnd().x - centerX, getEnd().y - centerY, 1};
        double[] newStart = matrixMultiply(scalateMatrix, start);
        double[] newEnd = matrixMultiply(scalateMatrix, end);

       
        startPoint.x = (int) (newStart[0] + centerX);
        startPoint.y = (int) (newStart[1] + centerY);
        endPoint.x = (int) (newEnd[0] + centerX);
        endPoint.y = (int) (newEnd[1] + centerY);
    }

    public void rotate(int tiempo){
        this.ang += animationFeatures.getAng()/tiempo;
    }
    
    private Point rotate(int x, int y){
        double[][] rotateMatrix = {{Math.cos(Math.toRadians(ang)), -Math.sin(Math.toRadians(ang)), 0},{Math.sin(Math.toRadians(ang)), Math.cos(Math.toRadians(ang)), 0},{0, 0, 1}};
        double[] figurePosition = {x, y, 1};
        double[] newPosition = matrixMultiply(rotateMatrix, figurePosition);
        return new Point((int)newPosition[0],(int)newPosition[1]);
    }
    
    protected void putPixel(int x, int y, Color c) {
        //calculo centro de la figura
        int centerX = ((getStart().x + getEnd().x) / 2) + rotationPoint.x;
        int centerY = ((getStart().y + getEnd().y) / 2) + rotationPoint.y;
        //se trasladan las coordenadas al centro
        int nx = x - centerX;
        int ny = y - centerY;
        //se calcula la rotacion
        Point newPoint = rotate(nx,ny);
        //Se regresan las x a su posicion ya con rotacion
        int newX = newPoint.x + centerX;
        int newY = newPoint.y + centerY;
        if (newX < 0 || newX >= buffer.getWidth() || newY < 0 || newY >= buffer.getHeight()) {
            return;
        }
        buffer.setRGB(newX, newY, c.getRGB());
    }
        
    private double[] matrixMultiply(double[][] matrix, double[] vector) {
        double[] result = new double[3];
        for (int i = 0; i < 3; i++) {
            double sum = 0;
            for (int j = 0; j < 3; j++) {
                sum += matrix[i][j] * vector[j];
            }
            result[i] = sum;
        }
        return result;
    }

    public void setBuffer(BufferedImage buffer) {this.buffer = buffer;}

    public void setBufferMain(BufferedImage bufferMain) {
        this.bufferMain = bufferMain;
    }

    public Point getStart() {return startPoint;}

    public Point getEnd() {return endPoint;}

    public BufferedImage getBufferMain() {
        return bufferMain;
    }

    //Sets the middle point to apply floodfill
    protected Point getFloodPoint() {
        int centerX = ((getStart().x + getEnd().x) / 2) + rotationPoint.x;
        int centerY = ((getStart().y + getEnd().y) / 2) + rotationPoint.y;

        int nx = - rotationPoint.x;
        int ny = - rotationPoint.y;

        Point newPoint = rotate(nx,ny);

        int newX = newPoint.x + centerX;
        int newY = newPoint.y + centerY;

        return new Point(newX,newY);
    }

    //Set Features
    public void setTimes(double inicio, double fin){
        animationFeatures.setTiempos(new double[]{inicio,fin});
    }

    public void setTraslation(int dx, int dy){
        animationFeatures.setTrasValues(new int[]{dx,dy});
    }

    public void setScalation(double sx, double sy){
        animationFeatures.setScaleValues(new double[]{sx,sy});
    }

    public void setAng(int ang){
        animationFeatures.setAng(ang);
    }

    public void setSteps(boolean steps) {
        this.steps = steps;
    }

    //Traslate the rotation/pivot point
    public void setRotationPoint(Point rotationPoint) {
        this.rotationPoint = rotationPoint;
    }

    //Get Features
    public boolean isSteps() {
        return steps;
    }
    public double getDuracion(){
        return animationFeatures.getTiempos()[1] - animationFeatures.getTiempos()[0];
    }

    public double getStartTime(){
        return animationFeatures.getTiempos()[0];
    }

    public double getFinishTime(){
        return animationFeatures.getTiempos()[1];
    }

    //Get active features
    public boolean translate(){
        if (animationFeatures.getTrasValues() != null){
            return true;
        }
        return false;
    }

    public boolean scale(){
        if (animationFeatures.getScaleValues() != null){
            return true;
        }
        return false;
    }

    public boolean rotate(){
        if (animationFeatures.getAng() != 0){
            return true;
        }
        return false;
    }

    //Metodos de Figuras
    protected void drawRec(Point startPoint, Point endPoint){
        for (int i = startPoint.x; i <= endPoint.x; i++) {
            putPixel(i, startPoint.y, color);
            putPixel(i, endPoint.y, color);
        }

        for (int i = startPoint.y; i <= endPoint.y; i++) {
            putPixel(startPoint.x, i, color);
            putPixel(endPoint.x, i, color);
        }
    }

    protected void drawCircle(Point centerPoint, int radio){
        int x = 0;
        int y = radio;
        int pk = 1 - radio;
        while (x <= y) {
            putPixel(centerPoint.x + x, centerPoint.y + y, color);
            putPixel(centerPoint.x + y, centerPoint.y + x, color);
            putPixel(centerPoint.x - x, centerPoint.y + y, color);
            putPixel(centerPoint.x - y, centerPoint.y + x, color);
            putPixel(centerPoint.x - x, centerPoint.y - y, color);
            putPixel(centerPoint.x - y, centerPoint.y - x, color);
            putPixel(centerPoint.x + x, centerPoint.y - y, color);
            putPixel(centerPoint.x + y, centerPoint.y - x, color);

            if (pk < 0) {
                pk += 2 * x + 3;
            } else {
                pk += 2 * (x - y) + 5;
                y--;
            }
            x++;
        }
    }

    protected void drawEllipse(Point centerPoint, int radioX, int radioY){
        double x,y;
        for (double i = 0; i <= 2 * Math.PI; i+=0.01) {
            x = centerPoint.x + radioX*Math.cos(i);
            y = centerPoint.y + radioY*Math.sin(i);
            putPixel((int) x, (int) y, color);
        }
    }

    protected void drawSol(Point centerPoint){
        double x,y;
        double y2 = centerPoint.y, x2 = centerPoint.x;
        int Puntos = 200;
        for (double t = 0; t <= (14*Math.PI); t+=(14*Math.PI)/Puntos) {
            x = centerPoint.x + (17*Math.cos(t) + 7*Math.cos((17.0/7)*t)) * 3;
            y = centerPoint.y - (17*Math.sin(t) - 7*Math.sin((17.0/7)*t)) * 3;
            if (t != 0) {
                drawLine((int) x, (int) y, (int) x2, (int) y2);
            }
            y2 = y;
            x2 = x;
        }
    }

    public void drawHumo(Point centerPoint){
        double x,y;
        double y2 = centerPoint.y, x2 = centerPoint.x;
        int Puntos = 40;
        for (double i = 0; i <= (2*Math.PI); i+=(2*Math.PI)/Puntos) {
            y = centerPoint.y - i * 8;
            x = centerPoint.x - (i * Math.cos(3 * i)) * 2;
            drawLine((int) x, (int) y, (int) x2, (int) y2);
            y2 = y;
            x2 = x;
        }
    }

    protected void drawLine(int x1, int y1, int x2, int y2) {
        float steps;
        int dy = y2 - y1;
        int dx = x2 - x1;
        if (Math.abs(dx) > Math.abs(dy)) {
            steps = Math.abs(dx);
        } else {
            steps = Math.abs(dy);
        }
        float x = x1, y = y1;
        putPixel(Math.round(x), Math.round(y), color);
        for (int k = 0; k < steps; k++) {
            x = x + dx/steps;
            y = y + dy/steps;
            putPixel(Math.round(x), Math.round(y), color);
        }
    }
    
}
