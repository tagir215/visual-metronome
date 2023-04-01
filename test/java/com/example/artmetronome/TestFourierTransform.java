package com.example.artmetronome;
import static org.junit.Assert.*;

import org.junit.Test;

import java.util.Arrays;
import java.util.List;

public class TestFourierTransform {


    private static List<Float> createList(){
        return Arrays.asList(
                0.3f,0.3f,0.4f,0.5f,0.1f,0.2f,0.1f,0.8f
        );
    }

    @Test
    public void testFastFourierTransform(){
        ComplexNumber[] freqBins =  FFT.getFFT(createList());
        assertEquals(2.7f,freqBins[0].getReal(),0.1f);
        assertEquals(0.483f,freqBins[1].getReal(),0.1f);
        assertEquals(-0.1f,freqBins[2].getReal(),0.1f);
        assertEquals(-0.083,freqBins[3].getReal(),0.1f);
        assertEquals(-0.9,freqBins[4].getReal(),0.1f);
        assertEquals(0.483f,freqBins[7].getReal(),0.1f);
    }

    @Test
    public static void testDiscreteFourierTransform(){
        DFT.calculateFrequency(createList());
        List<ComplexNumber>freqBins = DFT.frequencyBins;
        assertEquals(2.7f,freqBins.get(0).getReal(),0.1f);
        assertEquals(0.483f,freqBins.get(1).getReal(),0.1f);
        assertEquals(-0.1f,freqBins.get(2).getReal(),0.1f);
        assertEquals(-0.083,freqBins.get(3).getReal(),0.1f);
        assertEquals(-0.9,freqBins.get(4).getReal(),0.1f);
        assertEquals(0.483f,freqBins.get(7).getReal(),0.1f);
    }

}

