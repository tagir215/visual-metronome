package com.example.artmetronome;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class DFT {
    private static final float pi2 =2f* (float)Math.PI;
    public static List<ComplexNumber>frequencyBins;
    public static float calculateFrequency(List<Float>samples){
        frequencyBins = new ArrayList<>();
        StringBuilder stringBuilder = new StringBuilder();

        for(int i = 0; i< samples.size(); i++){
            stringBuilder.append("\n"+Math.abs(samples.get(i))+",");
        }

        for(float k = 0; k< samples.size(); k++){
            ComplexNumber sum = transformFourier(k,samples);
            frequencyBins.add(sum);
        }
        for(int i=0; i<frequencyBins.size(); i++){
            stringBuilder.append("\n"+frequencyBins.get(i).getReal()+","+frequencyBins.get(i).getImg()+"i");
        }


        return calculateEquivalentFrequency(findPeakMagnitudePosition(),samples.size());
    }

    private static ComplexNumber transformFourier(float k,List<Float>samples){
        ComplexNumber sum = new ComplexNumber(0,0);
        for (float n = 0; n< samples.size(); n++){
            float angle = -(pi2*k*n) / samples.size();
            ComplexNumber c = new ComplexNumber((float)Math.cos(angle),(float)Math.sin(angle));
            float x = samples.get((int)n);
            sum = sum.plus(c.times(x));
        }
        return sum;
    }

    private static int findPeakMagnitudePosition(){
        float magnitude = 0;
        int k = 0;
        for(int i=1; i<frequencyBins.size()/2; i++){
            ComplexNumber c = frequencyBins.get(i);
            float m = (float)Math.sqrt(c.getReal()*c.getReal() + c.getImg() * c.getImg());
            if(m>magnitude) {
                magnitude = m;
                k = i;
            }
        }
        return k;
    }

    private static float calculateEquivalentFrequency(int k,int size){
        return (k / size) * Settings.SAMPLE_RATE;
    }




}



