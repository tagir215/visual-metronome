package com.example.artmetronome;

import static org.junit.Assert.*;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TestYIN {
    @Test
    public void testAutoCorrelationMethod(){
        YIN yin = new YIN();
        Object[] samples = new Object[] {1.0,2.0,3.0,4.0,5.0,6.0};
        double r = yin.autoCorrelation(samples,1,2);
        assertEquals(0.057,r,1);
    }
    @Test
    public void testGetPitch(){
        YIN yin = new YIN();
        List<Object> samples = new ArrayList<>();
        for (int x=0; x<200; x++){
            samples.add(Math.sin(x));
        }
       // double freq = yin.getPitch(samples,500);
        //assertEquals(0.98,freq,4);
    }
}
