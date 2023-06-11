package figures;

import strdatos.AnimationFeatures;
import strdatos.Point;
import strdatos.Point3D;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.awt.image.BufferedImage;

public abstract class Figure3D {
    protected double[][] translateMatrix;
    protected Point3D rotationPoint,startPoint;
    protected Point3D pointOfView;
    protected BufferedImage buffer, auxBuffer;
    protected Color color;
    protected boolean steps = false;
    protected AnimationFeatures animationFeatures;
    protected List<Edge> scannedEdges;
    protected List<Point3D> facesPoints;
    protected int[] vector ,rotationVector;
    protected Point3D[] points;
    boolean projectParallel, projectPerspective;

    public Figure3D() {
        this.vector = new int[]{0,0,0};
        animationFeatures = new AnimationFeatures();
        rotationVector = new int[]{0,0,0};
        translateMatrix = new double[][]{{1, 0, 0, 0},{0, 1, 0, 0},{0, 0, 1, 0},{0, 0, 0, 1}};
        projectParallel = true;
        projectPerspective = false;
    }
    public Figure3D(Point3D[] points, BufferedImage buffer) {
        this();
        this.buffer = buffer;
        this.color = Color.black;
    }

    //Constructor para Objetos complejos
    public Figure3D(Point3D startPoint, BufferedImage buffer) {
        this();
        this.startPoint = startPoint;
        this.buffer = buffer;
    }

    public abstract void draw();

    protected void putPixel(int x, int y, Color c) {

        if (x < 0 || x >= buffer.getWidth() || y < 0 || y >= buffer.getHeight()) {
            return;
        }
        buffer.setRGB(x, y, c.getRGB());
    }

    //Drawing Methods
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

        scannedEdges = new ArrayList<>();
        // Defino qué aristas se conectarán
        scannedEdges.add(new Edge(points[0], points[1]));
        scannedEdges.add(new Edge(points[1], points[2]));
        scannedEdges.add(new Edge(points[2], points[3]));
        scannedEdges.add(new Edge(points[3], points[0]));
        // Uniones entre caras frontal y trasera
        scannedEdges.add(new Edge(points[0], points[4]));
        scannedEdges.add(new Edge(points[1], points[5]));
        scannedEdges.add(new Edge(points[2], points[6]));
        scannedEdges.add(new Edge(points[3], points[7]));
        // Cara trasera
        scannedEdges.add(new Edge(points[4], points[5]));
        scannedEdges.add(new Edge(points[5], points[6]));
        scannedEdges.add(new Edge(points[6], points[7]));
        scannedEdges.add(new Edge(points[7], points[4]));

        facesPoints = new ArrayList<>();

        //cara frontal
        facesPoints.add(new Point3D((int) ((points[0].x + points[2].x) / 2.0), (int) ((points[0].y + points[2].y) / 2.0), (int) ((points[0].z + points[2].z) / 2.0)));

        //cara trasera
        facesPoints.add(new Point3D((int) ((points[4].x + points[6].x) / 2.0), (int) ((points[4].y + points[6].y) / 2.0), (int) ((points[4].z + points[6].z) / 2.0)));

        //cara inferior
        facesPoints.add(new Point3D((int) ((points[0].x + points[1].x) / 2.0), (int) (((points[0].y + points[1].y) / 2.0)), ((points[0].z + points[4].z) / 2)));

        //cara superior
        facesPoints.add(new Point3D((int) ((points[2].x + points[3].x) / 2.0), (int) (((points[2].y + points[3].y) / 2.0) ), ((points[0].z + points[4].z) / 2)));

        //cara lateral izquierda
        facesPoints.add(new Point3D(points[0].x - 1, (int) ((points[0].y + points[6].y) / 2.0), (int) ((points[0].z + points[6].z) / 2.0)));

        //cara lateral derecha
        facesPoints.add(new Point3D(points[1].x + 1, (int) ((points[1].y + points[7].y) / 2.0), (int) ((points[1].z + points[7].z) / 2.0)));

        // Dibujar las aristas del prisma cuadrangular
        drawEdges();
    }

    protected void drawPolyPrism(Point3D[] points){
        scannedEdges = classifyEdges(points);
        color = Color.red;
        calculateCubeCenter();
        drawEdges();
    }

    //Transforms
    private double[] matrixMultiply(double[][] matrix, double[] vector) {
        double[] result = new double[vector.length];
        for (int i = 0; i < vector.length; i++) {
            double sum = 0;
            for (int j = 0; j < vector.length; j++) {
                sum += matrix[i][j] * vector[j];
            }
            result[i] = sum;
        }
        return result;
    }

    public void translate(int tiempo){
        int [] trans = animationFeatures.getTrasValues();

        translateMatrix = new double[][]{{1, 0, 0, (double) trans[0]/tiempo},{0, 1, 0, (double) trans[1]/tiempo},{0, 0, 1, (double) trans[2]/tiempo},{0, 0, 0, 1}};

        for (Point3D p : points) {
            double[] figurePosition = {p.x, p.y, p.z, 1};
            double[] newPosition = matrixMultiply(translateMatrix, figurePosition);
            p.x = (int) newPosition[0];
            p.y = (int) newPosition[1];
            p.z = (int) newPosition[2];
        }

    }

    protected Point3D translate(Point3D point3D){
        double[] figurePosition = {point3D.x, point3D.y, point3D.z, 1};
        double[] newPosition = matrixMultiply(translateMatrix, figurePosition);

        return new Point3D((int) newPosition[0],(int) newPosition[1],(int) newPosition[2]);
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

        return new Point3D((int) rotatedX, (int) rotatedY, (int) rotatedZ);
    }

    //setters
    public void setBuffer(BufferedImage buffer) {this.buffer = buffer;}

    public void setAuxBuffer(BufferedImage auxBuffer) {
        this.auxBuffer = auxBuffer;
    }

    public BufferedImage getAuxBuffer() {
        return auxBuffer;
    }

    //Set Features
    public void setTimes(double inicio, double fin){
        animationFeatures.setTiempos(new double[]{inicio,fin});
    }

    public void setTranslation(int dx, int dy, int dz){
        animationFeatures.setTrasValues(new int[]{dx,dy,dz});
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
            /*if (edge.p1.equals(smallestZPoint)|| edge.p2.equals(smallestZPoint)) {
                continue;
            }*/
            Point3D p1 = translate(rotate(edge.p1));
            Point3D p2 = translate(rotate(edge.p2));
            Point A, B;
            if (projectParallel){
                A = parallelProjection(p1);
                B = parallelProjection(p2);
            } else {
                A = perspectiveProjection(p1);
                B = perspectiveProjection(p2);
            }


            drawLine(A.x, A.y, B.x, B.y);
        }
        int i =0;
        for (Point3D point : facesPoints){
            Point3D p = translate(rotate(point));
            facesPoints.set(facesPoints.indexOf(point), translate(point));
            i+=2;
            Point floodPoint;
            if (projectParallel){
                floodPoint = parallelProjection(p);
            } else {
                floodPoint = perspectiveProjection(p);
            }
            //drawCircle(new Point(x, y),4 + i);
            new FloodFill(floodPoint,color ,buffer);
        }
        color = color.darker();
        for (Edge edge : scannedEdges) {
            if (edge.p1.equals(smallestZPoint)|| edge.p2.equals(smallestZPoint)) {
                continue;
            }
            Point3D p1 = translate(rotate(edge.p1));
            Point3D p2 = translate(rotate(edge.p2));
            Point A, B;

            if (projectParallel){
                A = parallelProjection(p1);
                B = parallelProjection(p2);
            } else {
                A = perspectiveProjection(p1);
                B = perspectiveProjection(p2);
            }
            drawLine(A.x, A.y, B.x, B.y);
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

    private void calculateCubeCenter() {
        double centerX = 0.0;
        double centerY = 0.0;
        double centerZ = 0.0;

        for (Point3D point : points) {
            centerX += point.x;
            centerY += point.y;
            centerZ += point.z;
        }
        centerX /= points.length;
        centerY /= points.length;
        centerZ /= points.length;

        rotationPoint = new Point3D((int) centerX, (int) centerY, (int) centerZ);
    }
    //Metodos de Proyeccion
    private Point parallelProjection(Point3D point3D){
        double u = (vector[2] == 0) ? 0 : (double) point3D.z / vector[2];

        // Proyecta las Coordenadas en plano XY
        int x = (int) (point3D.x + vector[0] * u);
        int y = (int) (point3D.y + vector[1] * u);

        return new Point(x,y);
    }

    private Point perspectiveProjection(Point3D point3D){
        double u = (point3D.z - pointOfView.z) == 0 ? 0 : (double) -pointOfView.z / (point3D.z - pointOfView.z);

        // Proyecta las coordenadas en el plano XY
        int x = (int) (pointOfView.x + (point3D.x - pointOfView.x) * u);
        int y = (int) (pointOfView.y + (point3D.y - pointOfView.y) * u);

        return new Point(x, y);
    }

    public void setProjectParallel(boolean projectParallel) {
        if (projectParallel){
            projectPerspective = false;
        }
        this.projectParallel = projectParallel;
    }

    public void setProjectPerspective(boolean projectPerspective) {
        if (projectPerspective){
            projectParallel = false;
        }
        this.projectPerspective = projectPerspective;
    }

    public void setPointOfView(Point3D pointOfView) {
        this.pointOfView = pointOfView;
    }

    public int[] getRotationVector() {
        return rotationVector;
    }
}


