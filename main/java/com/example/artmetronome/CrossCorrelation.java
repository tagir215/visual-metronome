package com.example.artmetronome;

import android.util.Log;

public class CrossCorrelation {
    double maxr = 0;
    static double crossCorrelation(ComplexNumber[] xBins, ComplexNumber[] yBins){
        return dotProduct(xBins,yBins);
    }

    private static double dotProduct(ComplexNumber[] xBins, ComplexNumber[] yBins){
        double sum = 0;
        for (int i=0; i<xBins.length; i++){
            double magX = Math.sqrt(xBins[i].real*xBins[i].real + xBins[i].img*xBins[i].img);
            double magY = Math.sqrt(yBins[i].real*yBins[i].real + yBins[i].img*yBins[i].img);
            sum += magX * magY;
        }
        return sum;
    }

}
