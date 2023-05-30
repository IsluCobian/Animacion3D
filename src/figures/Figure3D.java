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
    protected Point3D rotationPoint;
    protected BufferedImage buffer;
    protected Color color;
    protected boolean steps = false;
    private AnimationFeatures animationFeatures;
    protected BufferedImage bufferMain;

    protected List<Edge> scannedEdges;
    protected int[] vector, rotationVector;
    protected Point3D[] points;

    public Figure3D() {
        this.vector = new int[]{0,0,0};
        animationFeatures = new AnimationFeatures();
        rotationVector = new int[]{0,0,0};
    }
    public Figure3D(Point3D[] points, BufferedImage buffer) {
        this.buffer = buffer;
        this.color = Color.black;
        this.vector = new int[]{0,0,0};
        animationFeatures = new AnimationFeatures();
        bufferMain = null;
        rotationPoint = new Point3D(0,0,0);
        rotationVector = new int[]{0,0,0};
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
    public void setVector(int[] vector){

    }

    protected void drawQuadPrism(Point3D A, Point3D B, Point3D C) {
        if (scannedEdges == null) {
            points = new Point3D[8];
            // Puntos cara Frontal
            points[0] = A;
            points[1] = new Point3D(B.x, A.y, A.z);
            points[2] = B;
            points[3] = new Point3D(A.x, B.y, A.z);
            // Puntos cara trasera
            points[4] = new Point3D(A.x, A.y, C.z);
            points[5] = new Point3D(B.x, A.y, C.z);
            points[6] = new Point3D(B.x, B.y, C.z);
            points[7] = new Point3D(C.x, B.y, C.z);

            color = Color.red;
            // Defino qué aristas se conectarán
            scannedEdges = classifyEdges(points);
            calculateCubeCenter();
        }
        // Dibujar las aristas del prisma cuadrangular
        drawEdges();
    }

    protected void drawPolyPrism(Point3D[] points){
        scannedEdges = classifyEdges(points);
        color = Color.red;
        drawEdges();
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

    public void translate(int tiempo){
        int [] trans = animationFeatures.getTrasValues();

        double[][] translateMatrix = {{1, 0, trans[0]/tiempo},{0, 1, trans[1]/tiempo},{0, 0, 1}};

        /*Point[] points = {getStart(), getEnd()};
        for (Point p : points) {
            double[] figurePosition = {p.x, p.y, 1};
            double[] newPosition = matrixMultiply(translateMatrix, figurePosition);
            p.x = (int) newPosition[0];
            p.y = (int) newPosition[1];
        }*/
    }

    public void scalate(int tiempo){
        double [] scale = animationFeatures.getScaleValues();

        /*double centerX = (getStart().x + getEnd().x) / 2.0;
        double centerY = (getStart().y + getEnd().y) / 2.0;

        double[][] scalateMatrix = {{1+scale[0]/tiempo, 0, 0}, {0, 1+scale[1]/tiempo, 0}, {0, 0, 1}};
        double[] start = {getStart().x - centerX, getStart().y - centerY, 1};
        double[] end = {getEnd().x - centerX, getEnd().y - centerY, 1};
        double[] newStart = matrixMultiply(scalateMatrix, start);
        double[] newEnd = matrixMultiply(scalateMatrix, end);


        startPoint.x = (int) (newStart[0] + centerX);
        startPoint.y = (int) (newStart[1] + centerY);
        endPoint.x = (int) (newEnd[0] + centerX);
        endPoint.y = (int) (newEnd[1] + centerY);*/
    }

    public void rotate(int tiempo){
        rotationVector[0] += animationFeatures.getAng()[0]/tiempo;
        rotationVector[1] += animationFeatures.getAng()[1]/tiempo;
        rotationVector[2] += animationFeatures.getAng()[2]/tiempo;
    }

    public Point3D rotate(Point3D point3D) {
        double angX = rotationVector[0];
        double angY = rotationVector[1];
        double angZ = rotationVector[2];

        // Calcular las coordenadas relativas al centro de rotación
        double relativeX = point3D.x - rotationPoint.x;
        double relativeY = point3D.y - rotationPoint.y;
        double relativeZ = point3D.z - rotationPoint.z;

        double[][] rotateMatrixX = {
                {1, 0, 0},
                {0, Math.cos(Math.toRadians(angX)), -Math.sin(Math.toRadians(angX))},
                {0, Math.sin(Math.toRadians(angX)), Math.cos(Math.toRadians(angX))}
        };

        double[][] rotateMatrixY = {
                {Math.cos(Math.toRadians(angY)), 0, Math.sin(Math.toRadians(angY))},
                {0, 1, 0},
                {-Math.sin(Math.toRadians(angY)), 0, Math.cos(Math.toRadians(angY))}
        };

        double[][] rotateMatrixZ = {
                {Math.cos(Math.toRadians(angZ)), -Math.sin(Math.toRadians(angZ)), 0},
                {Math.sin(Math.toRadians(angZ)), Math.cos(Math.toRadians(angZ)), 0},
                {0, 0, 1}
        };


        double[] figurePosition = {relativeX, relativeY, relativeZ};

        double[] newPosition = matrixMultiply(rotateMatrixX, figurePosition);
        newPosition = matrixMultiply(rotateMatrixY, newPosition);
        newPosition = matrixMultiply(rotateMatrixZ, newPosition);

        // Calcular las coordenadas absolutas después de la rotación y agregar el desplazamiento del centro de rotación
        double rotatedX = newPosition[0] + rotationPoint.x;
        double rotatedY = newPosition[1] + rotationPoint.y;
        double rotatedZ = newPosition[2] + rotationPoint.z;

        Point3D rotatedPoint = new Point3D((int) rotatedX, (int) rotatedY, (int) rotatedZ);

        return rotatedPoint;
    }

    //setters
    public void setBuffer(BufferedImage buffer) {this.buffer = buffer;}

    public void setBufferMain(BufferedImage bufferMain) {
        this.bufferMain = bufferMain;
    }

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

    public void setRotationVector(int[] rotVector){
        animationFeatures.setAng(rotVector);
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

    public boolean rotating(){
        if (animationFeatures.getAng()[0] != 0 || animationFeatures.getAng()[1] != 0 || animationFeatures.getAng()[2] != 0){
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
            if ((edge.p1.x == edge.p2.x || edge.p1.y == edge.p2.y) && edge.p1.z == edge.p2.z) {
                filteredEdges.add(edge);
            } else if (edge.p1.x == edge.p2.x && edge.p1.y == edge.p2.y) {
                filteredEdges.add(edge);
            }
        }

        return filteredEdges;
    }

    protected void drawEdges(){
        double smallestZ = Double.MAX_VALUE;
        Point3D smallestZPoint = null;

        for (int i = 0; i < points.length; i++){
            Point3D p = rotate(points[i]);
            if (p.z < smallestZ) {
                smallestZ = p.z;
                smallestZPoint = points[i];
            }
        }


        for (Edge edge : scannedEdges) {
            Point3D p1 = rotate(edge.p1);
            Point3D p2 = rotate(edge.p2);

            if (edge.p1.equals(smallestZPoint)|| edge.p2.equals(smallestZPoint)) {
                continue;
            }

            double u = (vector[2] == 0) ? 0 : (double) p1.z / vector[2];

            // Proyecta las Coordenadas en plano XY
            int x1 = (int) (p1.x + vector[0] * u);
            int y1 = (int) (p1.y + vector[1] * u);

            u = (vector[2] == 0) ? 0 : (double) p2.z / vector[2];
            int x2 = (int) (p2.x + vector[0] * u);
            int y2 = (int) (p2.y + vector[1] * u);

            // Dibuja las aristas ya con los puntos proyectados
            drawLine(x1, y1, x2, y2);
        }
    }

    private void calculateCubeCenter() {
        double sumX = 0.0;
        double sumY = 0.0;
        double sumZ = 0.0;

        for (int i = 0; i < points.length; i++) {
            sumX += points[i].x;
            sumY += points[i].y;
            sumZ += points[i].z;
        }

        int numVertices = points.length;
        double centerX = sumX / numVertices;
        double centerY = sumY / numVertices;
        double centerZ = sumZ / numVertices;

        rotationPoint = new Point3D((int) centerX, (int) centerY, (int) centerZ);
    }
}
