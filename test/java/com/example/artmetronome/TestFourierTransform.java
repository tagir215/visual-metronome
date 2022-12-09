package com.example.artmetronome;
import static org.junit.Assert.*;

import org.junit.Test;

import java.util.Arrays;
import java.util.List;

public class TestFourierTransform {


    List<Float> createList(){
        return Arrays.asList(
                0.3f,0.3f,0.4f,0.5f,0.1f,0.2f,0.1f,0.8f
        );
    }

    @Test
    public void testFastFourierTransform(){
        FFT fft = new FFT(48000);
        fft.getFFT(createList());
        ComplexNumber[] freqBins = fft.frequencyBins;
        assertEquals(2.7f,freqBins[0].real,0.1f);
        assertEquals(0.483f,freqBins[1].real,0.1f);
        assertEquals(-0.1f,freqBins[2].real,0.1f);
        assertEquals(-0.083,freqBins[3].real,0.1f);
        assertEquals(-0.9,freqBins[4].real,0.1f);
        assertEquals(0.483f,freqBins[7].real,0.1f);
    }

    @Test
    public void testDiscreteFourierTransform(){
        DFT dft = new DFT(48000);
        dft.calculateFrequency(createList());
        List<ComplexNumber>freqBins = dft.frequencyBins;
        assertEquals(2.7f,freqBins.get(0).real,0.1f);
        assertEquals(0.483f,freqBins.get(1).real,0.1f);
        assertEquals(-0.1f,freqBins.get(2).real,0.1f);
        assertEquals(-0.083,freqBins.get(3).real,0.1f);
        assertEquals(-0.9,freqBins.get(4).real,0.1f);
        assertEquals(0.483f,freqBins.get(7).real,0.1f);
    }

}

