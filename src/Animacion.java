import figures.*;
import figures2D.Nube;
import figures2D.Rectangulo;
import strdatos.Point;
import strdatos.Point3D;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.LinkedList;

/**
 * Autor: Luis Cobian Registro: 20310016
 */

public class Animacion extends JFrame {

    private static BufferedImage buffer;
    public BufferedImage bufferSec;
    private final LinkedList<BufferedImage>  escenarios = new LinkedList<BufferedImage>();
    private final LinkedList<Figure3D> figures = new LinkedList<Figure3D>();
    public long TIME = 0;
    Thread thread;
    private Graphics2D graphics;
    int condicional = 0;
    Avion avion, escolta01, escolta02;
    Misil misil;

    public Animacion() {
        setTitle("Traición");
        setSize(1080, 700);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        
        bufferSec = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_ARGB);

        QuadPrism quadPrism =  new QuadPrism(new Point3D(150,190,20),new Point3D(50,90,20), new Point3D(150,190,120),bufferSec);
        quadPrism.setTimes(0,15);
        quadPrism.setRotationVector(new int[]{900,720,500});
        quadPrism.setTranslation(500,500,100);
        //figures.add(quadPrism);

        Avion avioneta = new Avion(new Point3D(1080,500,0),bufferSec);
        avioneta.setRotation(45,180,0);
        avioneta.setTimes(30,45);
        avioneta.setTranslation(-1500,0,0);
        avioneta.setMainColor(Color.YELLOW);
        avioneta.setScale(0.45);
        figures.add(avioneta);

        //↑ Cosas Debajo del Avion
        avion =  new Avion(new Point3D(0,200,0),bufferSec);
        avion.setTimes(0,25.0);
        avion.setRotationVector(new int[]{-920,0,0});
        figures.add(avion);
        // ↓ Cosas Arriba del Avion

        escolta01 =  new Avion(new Point3D(0,100,0),bufferSec);
        escolta01.setScale(0.5);
        escolta01.setTimes(0,25.0);
        escolta01.setRotationVector(new int[]{-600,0,0});
        //avion.setTranslation(800,500,100);
        figures.add(escolta01);

        escolta02 =  new Avion(new Point3D(50,470,0),bufferSec);
        escolta02.setScale(0.5);
        escolta02.setTimes(0,25.0);
        escolta02.setRotationVector(new int[]{-600,0,0});
        //avion.setTranslation(800,500,100);
        figures.add(escolta02);

        avioneta = new Avion(new Point3D(400,-450,0),bufferSec);
        avioneta.setRotation(80,180,-52);
        avioneta.setTimes(24,32);
        avioneta.setTranslation(-500,1000,0);
        avioneta.setRotationVector(new int[]{2000,0,0});
        avioneta.setMainColor(Color.YELLOW);
        avioneta.setScale(1.1);
        avioneta.setSteps(true);
        figures.add(avioneta);

        avioneta = new Avion(new Point3D(1080,90,0),bufferSec);
        avioneta.setRotation(45,180,0);
        avioneta.setTimes(0,15);
        avioneta.setTranslation(-1500,0,0);
        avioneta.setMainColor(Color.YELLOW);
        avioneta.setScale(0.5);
        figures.add(avioneta);

        misil = new Misil(new Point3D(570,150,0),bufferSec);
        misil.setTimes(4,25.0);
        misil.setRotation(0,180,0);
        misil.setScale(0.35);
        misil.setMainColor(Color.YELLOW);
        misil.setRotationVector(new int[]{4100,0,0});
        misil.setTranslation(-3000,0,100);
        figures.add(misil);

        avioneta = new Avion(new Point3D(1080,150,0),bufferSec);
        avioneta.setRotation(45,180,0);
        avioneta.setTimes(15,30);
        avioneta.setTranslation(-1500,0,0);
        avioneta.setMainColor(Color.YELLOW);
        avioneta.setScale(0.6);
        figures.add(avioneta);

        avioneta = new Avion(new Point3D(1080,90,0),bufferSec);
        avioneta.setRotation(45,180,0);
        avioneta.setTimes(38,45);
        avioneta.setTranslation(-1500,0,0);
        avioneta.setMainColor(Color.YELLOW);
        avioneta.setScale(0.7);
        figures.add(avioneta);

        //Pelea 51 - 70
        /*Bando amarillo*/
        avioneta = new Avion(new Point3D(1080,320,0),bufferSec);
        avioneta.setRotation(55,180,0);
        avioneta.setTimes(49,70);
        avioneta.setTranslation(-2000,0,0);
        avioneta.setMainColor(Color.YELLOW);
        avioneta.setScale(0.4);
        figures.add(avioneta);

        avioneta = new Avion(new Point3D(1080,400,0),bufferSec);
        avioneta.setRotation(40,180,0);
        avioneta.setTimes(49,70);
        avioneta.setTranslation(-2000,0,0);
        avioneta.setMainColor(Color.YELLOW);
        avioneta.setScale(0.6);
        figures.add(avioneta);

        avioneta = new Avion(new Point3D(1080,550,0),bufferSec);
        avioneta.setRotation(40,180,0);
        avioneta.setTimes(49,70);
        avioneta.setTranslation(-2000,0,0);
        avioneta.setMainColor(Color.YELLOW);
        avioneta.setScale(0.5);
        figures.add(avioneta);

        Misil traicion = new Misil(new Point3D(300,800,100),bufferSec);
        traicion.setRotation(15,0,-90);
        traicion.setTimes(65,70);
        traicion.setTranslation(0,-600,0);
        traicion.setRotationVector(new int[]{2280,0,0});
        traicion.setMainColor(Color.red);
        traicion.setScale(0.6);
        traicion.setSteps(true);
        figures.add(traicion);

        //Los caidos
        avioneta = new Avion(new Point3D(100,-300,0),bufferSec);
        avioneta.setRotation(0,0,90);
        avioneta.setTimes(89,99);
        avioneta.setTranslation(0,850,0);
        avioneta.setRotationVector(new int[]{3500,0,0});
        avioneta.setScale(0.5);
        figures.add(avioneta);

        avioneta = new Avion(new Point3D(300,-200,0),bufferSec);
        avioneta.setRotation(0,0,90);
        avioneta.setTimes(89,100);
        avioneta.setTranslation(0,850,0);
        avioneta.setRotationVector(new int[]{3000,0,0});
        avioneta.setScale(0.7);
        figures.add(avioneta);

        avioneta = new Avion(new Point3D(600,-250,0),bufferSec);
        avioneta.setRotation(0,0,90);
        avioneta.setTimes(89,99.5);
        avioneta.setTranslation(0,830,0);
        avioneta.setRotationVector(new int[]{3800,0,0});
        avioneta.setScale(0.65);
        figures.add(avioneta);

        avioneta = new Avion(new Point3D(800,-180,0),bufferSec);
        avioneta.setRotation(0,0,90);
        avioneta.setTimes(89,99.8);
        avioneta.setTranslation(0,850,0);
        avioneta.setRotationVector(new int[]{3200,0,0});
        avioneta.setScale(0.85);
        figures.add(avioneta);

        setVisible(true);
    }

    @Override
    public void paint(Graphics g) {
        if (buffer == null) {
            buffer = new BufferedImage(getWidth(),getHeight(),BufferedImage.TYPE_INT_ARGB);
            buffer.getGraphics().setColor(Color.white);
            buffer.getGraphics().fillRect(0,0,getWidth(),getHeight());
            new Rectangulo(new Point(0,0), new Point(getWidth(),getHeight()),buffer,Color.decode("#61C9E5")).draw();
            new Nube(new Point(50,110),new Point(115,130),buffer,Color.white).draw();
            new Nube(new Point(250,210),new Point(325,230),buffer,Color.white).draw();
            new Nube(new Point(450,90),new Point(525,110),buffer,Color.white).draw();
            new Nube(new Point(0,320),new Point(80,350),buffer,Color.white).draw();
            new Nube(new Point(30,610),new Point(105,630),buffer,Color.white).draw();
            new Nube(new Point(530,510),new Point(605,530),buffer,Color.white).draw();
            new Nube(new Point(580,310),new Point(655,330),buffer,Color.white).draw();
            new Nube(new Point(880,250),new Point(955,270),buffer,Color.white).draw();
            Misil misil = new Misil(new Point3D(0,200,0),buffer);
            misil.setRotation(15,0,0);
//            misil.draw();
//            Avion avioneta = new Avion(new Point3D(0,200,0),buffer);
//            avioneta.setRotation(80,180,-50);
//            avioneta.setMainColor(Color.YELLOW);
//            avioneta.setScale(1.1);
//            avioneta.draw();
            thread = new Thread(new Transforms(figures));
            thread.start();
        }

        if (avion.getRotationVector()[0] >= 15 && TIME < 70000){
            avion.setRotationVector(new int[]{-920,0,0});
            escolta01.setRotationVector(new int[]{-890,0,0});
            escolta02.setRotationVector(new int[]{-890,0,0});
        }
        if (TIME == 24150){
            avion.setTimes(24,50);
            escolta02.setTimes(24,50);
            avion.setRotationVector(new int[]{0,0,0});
            //avion.setScale(0.90);
            avion.setTranslation(1600,950,100);
        }
        if (TIME == 46000){
            avion.setTimes(44,75);
            avion.setScale(1.05);
            avion.setRotationVector(new int[]{0,0,0});
            avion.setTranslation(-2350,-2350,100);
        }
        if (TIME == 32000 || TIME == 50000){
            avion.setRotationVector(new int[]{-920,0,0});
            avion.setTranslation(0,0,0);
        }
        if (avion.getRotationVector()[0] <= -35 && TIME < 70000){
            avion.setRotationVector(new int[]{920,0,0});
            escolta01.setRotationVector(new int[]{920,0,0});
            escolta02.setRotationVector(new int[]{920,0,0});
        }
        if (TIME == 6800){
            figures.remove(escolta01);
            figures.remove(misil);
        }
        if (TIME == 29500){
            figures.remove(escolta02);
        }
        if (TIME == 70000){
            avion.setTimes(70,90);
            //avion.setRotation(0,0,60);
            avion.setScale(.95);
            avion.setRotationVector(new int[]{5800,0,1120});
            avion.setTranslation(1500,1700,100);
        }
        if (avion.getRotationVector()[2] >= 90){
            avion.setRotationVector(new int[]{5800,0,0});
            avion.setTranslation(0,1700,100);
        }
        if (TIME == 79000){
            buffer.getGraphics().setColor(Color.white);
            buffer.getGraphics().fillRect(0,0,getWidth(),getHeight());
            new Rectangulo(new Point(0,0), new Point(getWidth(),getHeight()),buffer,Color.decode("#61C9E5")).draw();
            new Rectangulo(new Point(0,650), new Point(getWidth(),getHeight()),buffer,Color.decode("#9ECD6C")).draw();
            new Nube(new Point(50,110),new Point(120,130),buffer,Color.white).draw();
            new Nube(new Point(250,190),new Point(320,210),buffer,Color.white).draw();
            new Nube(new Point(550,100),new Point(620,120),buffer,Color.white).draw();
            avion.setStartPoint(new Point3D(400, -480, 0));
        }

//        this.getGraphics().drawImage(buffer, 0, 0, this);
        this.getGraphics().drawImage(bufferSec, 0, 0, this);
    }

    public static void main(String[] args) {
        new Animacion();   
    }
    
    public class Transforms implements Runnable {
        LinkedList<Figure3D> animate;
        int DELAY = 50;
        Figure3D figure;
        int c = 0;

        public Transforms(LinkedList<Figure3D> animate) {
            this.animate = animate;
        }

        public void transform(int i) {
            long tiempo = (long) (( figure.getDuracion() * 1000 )/ DELAY);
            if (figure.translate()){
                figure.translate(tiempo);
            }
            if (figure.scale()){
                figure.scalate(tiempo);
            }
            if (figure.rotating()){
                figure.rotate(tiempo);
            }
        }

        public boolean isComplete() {
            return figure.getFinishTime() <= ((double) TIME / 1000);
        }

        public boolean startTime(){
            return (figure.getStartTime() * 1000) <= TIME;
        }

        public void removeAnimation(int i) {
            animate.remove(figure);
            if (!figure.isSteps()) {
                figure.setAuxBuffer(buffer);
                figure.draw();
            }
        }

        @Override
        public void run() {
                while (true) {
                    if (!animate.isEmpty()) {
                        bufferSec.getGraphics().drawImage(buffer, 0, 0, null);
                        for (int i = 0; i < animate.size(); i++) {
                            figure = animate.get(i);
                            if (startTime()) {
                                figure.draw();
                                transform(i);
                                if (isComplete()) {
                                    removeAnimation(i);
                                    if (i != animate.size() && animate.size() != 0){
                                        figure = animate.get(i);
                                        figure.draw();
                                        transform(i);
                                    }
                                }
                            }
                        }
                        repaint();
                        try {
                            if (c == (1000 / DELAY)) {
                                System.out.println(String.valueOf(TIME / 1000));
                                c = 0;
                            }
                            c++;
                            TIME += DELAY;
                            Thread.sleep(DELAY);
                        } catch (InterruptedException e) {e.printStackTrace();}
                    }
                }
        }

}


}
