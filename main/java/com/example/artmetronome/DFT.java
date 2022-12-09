package com.example.artmetronome;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class DFT {
    List<Float> sampleList;
    List<ComplexNumber>frequencyBins;
    StringBuilder stringBuilder;
    int sampleRate;
    float frequency;
    DFT(int sampleRate){
        this.sampleRate = sampleRate;
    }
    float sampleSize;
    final float pi2 =2f* (float)Math.PI;

    void calculateFrequency(List<Float>samples){
        this.sampleList = samples;
        sampleSize = samples.size();
        frequencyBins = new ArrayList<>();
        stringBuilder = new StringBuilder();

        for(int i = 0; i< sampleSize; i++){
            stringBuilder.append("\n"+Math.abs(sampleList.get(i))+",");
        }

        for(float k = 0; k< sampleSize; k++){
            ComplexNumber sum = transformFourier(k);
            frequencyBins.add(sum);
        }
        for(int i=0; i<frequencyBins.size(); i++){
            stringBuilder.append("\n"+frequencyBins.get(i).real+","+frequencyBins.get(i).img+"i");
        }


        frequency = calculateEquivalentFrequency(findPeakMagnitudePosition());
    }

    ComplexNumber transformFourier(float k){
        ComplexNumber sum = new ComplexNumber(0,0);
        for (float n = 0; n< sampleSize; n++){
            float angle = -(pi2*k*n) / sampleSize;
            ComplexNumber c = new ComplexNumber((float)Math.cos(angle),(float)Math.sin(angle));
            float x = sampleList.get((int)n);
            sum = sum.plus(c.times(x));
        }
        return sum;
    }

    int findPeakMagnitudePosition(){
        float magnitude = 0;
        int k = 0;
        for(int i=1; i<frequencyBins.size()/2; i++){
            ComplexNumber c = frequencyBins.get(i);
            float m = (float)Math.sqrt(c.real*c.real + c.img * c.img);
            if(m>magnitude) {
                magnitude = m;
                k = i;
            }
        }
        return k;
    }

    float calculateEquivalentFrequency(int k){
        return (k / sampleSize) * sampleRate;
    }




}



