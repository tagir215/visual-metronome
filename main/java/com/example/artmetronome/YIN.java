package com.example.artmetronome;

import java.util.List;

public class YIN {

    double getPitch(List<Byte>samples, double sampleRate){
        double t = 1;
        Object[] samples1 = samples.toArray();
        double[] acfArray = new double[samples.size()];
        for (int lag=0; lag<samples.size(); lag++){
            acfArray[lag] = doStuffBeforeOtherStuff(samples1,t,lag);
        }

        double min_r = 1;
        int minInd = 0;
        for(int i=20; i<acfArray.length; i++){
            if(Math.abs(acfArray[i])<min_r) {
                min_r = Math.abs(acfArray[i]);
                minInd = i;
            }
        }
        return sampleRate / minInd;
    }

    double doStuffBeforeOtherStuff(Object[] samples, double t, double lag){
        if(lag==0)
            return 1;
        double a = doStuff(samples,t,lag);
        double sum = 0;
        for(int j=0; j<lag; j++){
            sum += doStuff(samples,t,j+1);
        }
        return a*sum * lag;

    }

    double doStuff(Object[] samples,double t, double lag){
        double a = autoCorrelation(samples,t,0);
        double b = autoCorrelation(samples,t+lag,0);
        double c = autoCorrelation(samples,t,lag);
        return a+b-2*c;
    }

    double autoCorrelation(Object[] samples, double t, double lag){
        double r;
        double xy =0;
        double sX = 0;
        double sY = 0;
        double n = samples.length;
        double sXX = 0;
        double sYY = 0;

        for(int i=0; i<samples.length; i++){
            byte x = (byte)samples[i];
            double y = x + lag*t;
            xy += x*y;
            sX +=x;
            sY +=y;
            sXX += x*x;
            sYY += y*y;
        }
        r = (n*xy-sX*sY) / Math.sqrt((n*sXX-sX*sX)*(n*sYY-sY*sY));

        return r;
    }
}
