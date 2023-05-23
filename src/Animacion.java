import figures.*;
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
    private LinkedList<Figure> figures = new LinkedList<Figure>();
    public int TIME = 0;
    Thread thread;
    private Graphics2D graphics;
    int condicional = 0;

    public Animacion() {
        setTitle("Practica");
        setSize(750, 500);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        
        bufferSec = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_ARGB);

        setVisible(true);
    }

    @Override
    public void paint(Graphics g) {
        if (buffer == null) {
            buffer = new BufferedImage(getWidth(),getHeight(),BufferedImage.TYPE_INT_ARGB);
            new QuadPrism(new Point3D(200,180,20),new Point3D(50,90,20), new Point3D(200,180,100),buffer).draw();
            new QuadPrism(new Point3D(400,300,20),new Point3D(150,250,20), new Point3D(400,300,100),buffer).draw();
            Point3D[] points = {
                    // Base de la T
                    new Point3D(200, 450, 250),
                    new Point3D(300, 450, 250),
                    new Point3D(200, 200, 250),
                    new Point3D(300, 200, 150),

                    // Parte superior de la T
                    new Point3D(200, 275, 250),
                    new Point3D(200, 375, 250),

                    new Point3D(100, 275, 250),
                    new Point3D(100, 375, 250),

                    // Base de la T
                    new Point3D(200  - 30, 450  - 30, 250),
                    new Point3D(300  - 30, 450  - 30, 250),
                    new Point3D(200  - 30, 200  - 30, 250),
                    new Point3D(300  - 30, 200  - 30, 150),

                    // Parte superior de la T
                    new Point3D(200  - 30, 275  - 30, 250),
                    new Point3D(200  - 30, 375  - 30, 250),

                    new Point3D(100  - 30, 275  - 30, 250),
                    new Point3D(100  - 30, 375  - 30, 250)
            };
            //new PolyPrism(points,buffer).draw();
            //thread = new Thread(new Transforms(figures));

            //thread.start();
        }


        this.getGraphics().drawImage(buffer, 0, 0, this);
        //this.getGraphics().drawImage(bufferSec, 0, 0, this);
    }

    public static void main(String[] args) {
        new Animacion();   
    }
    
    public class Transforms implements Runnable {
        LinkedList<Figure> animate;
        int DELAY = 50;
        Figure figure;
        int c = 0;

        public Transforms(LinkedList<Figure> animate) {
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
            if (figure.rotate()){
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
