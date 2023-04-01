package com.example.artmetronome;

import android.util.Log;

public class CrossCorrelation {
    double maxr = 0;
    public static double crossCorrelation(ComplexNumber[] xBins, ComplexNumber[] yBins){
        return dotProduct(xBins,yBins);
    }

    private static double dotProduct(ComplexNumber[] xBins, ComplexNumber[] yBins){
        double sum = 0;
        for (int i=0; i<xBins.length; i++){
            double magX = Math.sqrt(xBins[i].getReal()*xBins[i].getReal() + xBins[i].getImg()*xBins[i].getImg());
            double magY = Math.sqrt(yBins[i].getReal()*yBins[i].getReal() + yBins[i].getImg()*yBins[i].getImg());
            sum += magX * magY;
        }
        return sum;
    }

}
