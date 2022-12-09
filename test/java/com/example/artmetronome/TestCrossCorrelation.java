package com.example.artmetronome;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import java.util.Arrays;
import java.util.List;

public class TestCrossCorrelation {
    @Test
    public void testCrossCorrelation(){
        ComplexNumber[] freqBins1 = new ComplexNumber[] {
                new ComplexNumber(0.1f,0f),
                new ComplexNumber(0.2f,0f),
                new ComplexNumber(0.3f,0f),
                new ComplexNumber(0.4f,0f),
                new ComplexNumber(0.5f,0f),
                new ComplexNumber(0.6f,0f),
                new ComplexNumber(0.7f,0f),
        };

        ComplexNumber[] freqBins2 = new ComplexNumber[] {
                new ComplexNumber(0.7f,0f),
                new ComplexNumber(0.6f,0f),
                new ComplexNumber(0.5f,0f),
                new ComplexNumber(0.4f,0f),
                new ComplexNumber(0.3f,0f),
                new ComplexNumber(0.2f,0f),
                new ComplexNumber(0.1f,0f),
        };

        double r = CrossCorrelation.crossCorrelation(freqBins1,freqBins2);
        assertEquals(-1,r,0.001);
    }


    @Test
    public void testDotProductCorrelation(){
        //Autocorrelation autocorrelation = new Autocorrelation();
        //double freq = autocorrelation.getFrequencyViaDotProduct(createMags(),1,new OpenGLRenderer());
        //assertEquals(14,freq,1);
    }

    List<Float> createMags(){
        return Arrays.asList(
                4f,
                5f,
                6f,
                1f,
                2f,
                1f,
                1f,
                1f,
                1f,
                1f,
                1f,
                1f,
                1f,
                1f,
                4f,
                5f,
                6f,
                1f
                );
    }
}
