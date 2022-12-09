package com.example.artmetronome;

import java.util.List;

public class Autocorrelation {

    static double getFrequencyViaDotProduct(List<Float>samples, double timeStep){
        double r = 0;
        double position = 0;
        for (int i=10; i<samples.size(); i++){
            double rr = dotProduct(samples,i);
            if(rr>r) {
                r = rr;
                position = i;
            }
        }
        return 1 / ( position * timeStep );
    }
    private static double dotProduct(List<Float> x, int k){
        double sum = 0;
        for (int i=0; i<x.size(); i++){
            double y;
            if(i+k>x.size()-1)
                y=0;
            else
                y= (float)x.get(i+k);
            sum += (float)x.get(i)*y;
        }
        return sum;
    }
    private static double[] replaceBiggest(double[] array, double value){
        if(array[0]<value)
            array[0]=value;
        else if(array[1]<value)
            array[1]=value;
        else if(array[2]<value)
            array[2]=value;
        return array;
    }

}
