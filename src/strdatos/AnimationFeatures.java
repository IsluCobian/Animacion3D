/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package strdatos;

/**
 * Autor: Luis Cobian
 * Registro: 20310016
 */
public class AnimationFeatures {
    int[] trasValues; //dx,dy
    double[] tiempos; //Inicio,Final
    double[] scaleValues; //sx,sy
    int ang; //En Grados sexagesimales

    public AnimationFeatures() {
        trasValues = null;
        scaleValues = null;
        ang = 0;
    }
    
    //getters

    public int[] getTrasValues() {return trasValues;}

    public double[] getTiempos() {return tiempos;}

    public double[] getScaleValues() {return scaleValues;}

    public int getAng() {return ang;}
    
    //setters

    public void setTrasValues(int[] trasValues) {this.trasValues = trasValues;}

    public void setTiempos(double[] tiempos) {this.tiempos = tiempos;}

    public void setScaleValues(double[] scaleValues) {this.scaleValues = scaleValues;}

    public void setAng(int ang) {this.ang = ang;}
   
}
