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
    private LinkedList<BufferedImage>  escenarios = new LinkedList<BufferedImage>();
    private LinkedList<Figure3D> figures = new LinkedList<Figure3D>();
    public long TIME = 0;
    Thread thread;
    private Graphics2D graphics;
    int condicional = 0;
    Avion avion;

    public Animacion() {
        setTitle("Practica");
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

        avion =  new Avion(new Point3D(0,200,0),bufferSec);
        avion.setTimes(0,25);
        //avion.setRotationVector(new int[]{0,200,300});
        //avion.setTranslation(800,300,100);
        figures.add(avion);

        setVisible(true);
    }

    @Override
    public void paint(Graphics g) {
        if (buffer == null) {
            buffer = new BufferedImage(getWidth(),getHeight(),BufferedImage.TYPE_INT_ARGB);
            buffer.getGraphics().setColor(Color.white);
            buffer.getGraphics().fillRect(0,0,getWidth(),getHeight());
            new Rectangulo(new strdatos.Point(0,0), new strdatos.Point(getWidth(),getHeight()),buffer,Color.decode("#61C9E5")).draw();
            new Nube(new Point(50,110),new Point(115,130),buffer,Color.white).draw();
            new Nube(new Point(250,210),new Point(325,230),buffer,Color.white).draw();
            new Nube(new Point(450,90),new Point(525,110),buffer,Color.white).draw();
            new Nube(new Point(0,320),new Point(80,350),buffer,Color.white).draw();
            new Nube(new Point(30,610),new Point(105,630),buffer,Color.white).draw();
            new Nube(new Point(530,510),new Point(605,530),buffer,Color.white).draw();
            new Nube(new Point(580,310),new Point(655,330),buffer,Color.white).draw();
            new Nube(new Point(880,250),new Point(955,270),buffer,Color.white).draw();
            thread = new Thread(new Transforms(figures));
            thread.start();
        }

        if (TIME == 5000 || TIME == 15000){
            avion.setRotationVector(new int[]{-580,0,0});
        }
        if (TIME == 9000 || TIME == 14050 || TIME == 19000){
            avion.setRotationVector(new int[]{0,0,0});
        }
        if (TIME == 10000 || TIME == 20000){
            avion.setRotationVector(new int[]{580,0,0});
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
            int tiempo = (int) ( figure.getDuracion() * 1000 )/ DELAY;
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
            if (figure.getFinishTime() <= (TIME / 1000)) {
                return true;
            }
            return false;
        }

        public boolean startTime(){
            if (figure.getStartTime()*1000 <= TIME) {
                return true;
            }
            return false;
        }

        public void removeAnimation(int i) {
            animate.remove(figure);
            if (!figure.isSteps()) {
                //figure.draw();
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
                                    if (i != animate.size() - 1 && animate.size() != 0){
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
                                System.out.println("" + TIME / 1000);
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
