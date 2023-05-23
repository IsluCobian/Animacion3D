package figures;

import strdatos.AnimationFeatures;
import strdatos.Point;
import strdatos.Point3D;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.awt.image.BufferedImage;

public abstract class Figure3D {
    protected Point3D startPoint, middlePoint ,endPoint, rotationPoint;
    protected BufferedImage buffer;
    private int ang;
    protected Color color;
    protected boolean steps = false;
    private AnimationFeatures animationFeatures;
    protected BufferedImage bufferMain;

    public Figure3D() {
    }
    public Figure3D(Point3D startPoint, Point3D middlePoint , Point3D endPoint, BufferedImage buffer) {
        this.startPoint = startPoint;
        this.middlePoint = middlePoint;
        this.endPoint = endPoint;
        this.buffer = buffer;
        this.color = Color.black;
        animationFeatures = new AnimationFeatures();
        bufferMain = null;
        rotationPoint = new Point3D(0,0,0);
        ang = 0;
    }
    public abstract void draw();

    protected void putPixel(int x, int y, Color c) {

        if (x < 0 || x >= buffer.getWidth() || y < 0 || y >= buffer.getHeight()) {
            return;
        }
        buffer.setRGB(x, y, c.getRGB());
    }

    //Drawing Methods
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
    /*3D Methods*/
    public void setPerspective(){

    }

    protected void drawQuadPrism(Point3D A, Point3D B, Point3D C) {
        Point3D[] points = new Point3D[8];
        // Puntos cara Frontal
        points[0] = A;
        points[1] = new Point3D(B.x, A.y, A.z);
        points[2] = B;
        points[3] = new Point3D(A.x, B.y, A.z);
        // Puntos cara trasera
        points[4] = new Point3D(A.x - 20, A.y - 20, C.z);
        points[5] = new Point3D(B.x - 20, A.y - 20, C.z);
        points[6] = new Point3D(B.x - 20, B.y - 20, C.z);
        points[7] = new Point3D(C.x - 20, B.y - 20, C.z);

        color = Color.red;
        // Defino qué aristas se conectarán
        List<Edge> edges = classifyEdges(points);

        // Agregar las aristas que unen la cara frontal con la cara trasera
        edges.add(new Edge(points[0], points[4]));
        edges.add(new Edge(points[1], points[5]));
        edges.add(new Edge(points[2], points[6]));
        edges.add(new Edge(points[3], points[7]));
        // Dibujar las aristas del prisma cuadrangular
        for (Edge edge : edges) {
            Point3D p1 = edge.p1;
            Point3D p2 = edge.p2;
            drawLine(p1.x, p1.y, p2.x, p2.y);
        }
    }

    protected void drawPolyPrism(Point3D[] points){
        List<Edge> edges = classifyEdges(points);
        color = Color.red;
        for (Edge edge : edges) {
            Point3D p1 = edge.p1;
            Point3D p2 = edge.p2;
            drawLine(p1.x, p1.y, p2.x, p2.y);
        }
    }

    //Transforms
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

    private Point rotate(int x, int y){
        double[][] rotateMatrix = {{Math.cos(Math.toRadians(ang)), -Math.sin(Math.toRadians(ang)), 0},{Math.sin(Math.toRadians(ang)), Math.cos(Math.toRadians(ang)), 0},{0, 0, 1}};
        double[] figurePosition = {x, y, 1};
        double[] newPosition = matrixMultiply(rotateMatrix, figurePosition);
        return new Point((int)newPosition[0],(int)newPosition[1]);
    }

    //setters
    public void setBuffer(BufferedImage buffer) {this.buffer = buffer;}

    public void setBufferMain(BufferedImage bufferMain) {
        this.bufferMain = bufferMain;
    }

    public Point3D getStart() {return startPoint;}

    public Point3D getEnd() {return endPoint;}

    public BufferedImage getBufferMain() {
        return bufferMain;
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
    public void setRotationPoint(Point3D rotationPoint) {
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

    //Clasificacion de Bordes
    public List<Edge> classifyEdges(Point3D[] points) {
        List<Edge> edges = new ArrayList<>();

        // Genera todas las posibles aristas entre los puntos
        for (int i = 0; i < points.length; i++) {
            for (int j = i + 1; j < points.length; j++) {
                Edge edge = new Edge(points[i], points[j]);
                edges.add(edge);
            }
        }

        // Filtra las aristas para obtener solo las que son bordes
        List<Edge> filteredEdges = new ArrayList<>();
        for (Edge edge : edges) {
            if (edge.p1.x == edge.p2.x || edge.p1.y == edge.p2.y) {
                filteredEdges.add(edge);
            }
        }

        return filteredEdges;
    }

    private boolean edgeSharesPoints(Edge edge1, Edge edge2) {
        return false;
    }
}
