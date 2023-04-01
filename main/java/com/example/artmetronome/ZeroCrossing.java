package com.example.artmetronome;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ZeroCrossing {
    public static List<Float> getZeroCrossings(List<Float> samples, int sampleRate){
        float timeStep = 1 / (float)sampleRate;
        Object[] sampleArray = samples.toArray();
        List<Float>zeroCrossings = new ArrayList<>();
        float previousY = 0;
        int k=0;
        for(int i=0; i<sampleArray.length; i++){
            k++;
            float y = (float)sampleArray[i];
            if(y>0 && previousY<0){
                zeroCrossings.add(k*timeStep);
                k=0;
            }
            else if(y<0 && previousY>0){
                zeroCrossings.add(k*timeStep);
                k=0;
            }
            previousY = y;
        }
        return zeroCrossings;
    }

    public static int countHills(List<Float>samples){
        float oldY = 0;
        int sum= 0;
        for (int i=0; i<samples.size(); i++){
            float y = samples.get(i);
            if(y>oldY){
                sum++;
            }
            oldY = y;
        }
        return sum;
    }
   public static int getMedian(List<Integer>hills){
       HashMap<Integer,Integer>valuemap = new HashMap<>();
        for (int i=0; i<hills.size(); i++){
            int num = 0;
            if(valuemap.get(hills.get(i))!=null)
                num = valuemap.get(hills.get(i));
            num++;
            valuemap.put(hills.get(i),num);
        }
        int biggest = 0;
        int median = 0;
        for (Integer key : valuemap.keySet()){
            if(valuemap.get(key)>biggest){
                biggest = valuemap.get(key);
                median = key;
            }
        }
        return median;
   }
}
