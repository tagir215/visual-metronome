package com.example.artmetronome;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class FFT {

    private static final double pi = Math.PI;
    public static ComplexNumber[] frequencyBins;
    int sampleRate;

    private static double calculateFrequency(ComplexNumber[] frequencyBins){
        double frequency = calculateEquivalentFrequency(findPeakMagnitudePosition(),frequencyBins.length,Settings.SAMPLE_RATE);
        return frequency;
    }

    public static ComplexNumber[]getFFT(List<Float>samples){
        frequencyBins = fft(inputToComplexNumbers(samples));
        //double freq = calculateEquivalentFrequency(findPeakMagnitudePosition(),samples.size(),sampleRate);
        //Log.e("freq","frequency is "+freq+"Hz");
        return frequencyBins;
    }

    public static ComplexNumber[] inputToComplexNumbers(List<Float> samples){
        ComplexNumber[] complexNumbers = new ComplexNumber[samples.size()];
        for (int i=0; i<samples.size(); i++){
            float s = Math.abs(samples.get(i));
            complexNumbers[i] = new ComplexNumber(s,0);
        }
        return complexNumbers;
    }

    public static ComplexNumber[] fft(ComplexNumber[] complexNumbers){
        int N = complexNumbers.length;
        if(N==1) {
            return new ComplexNumber[]{complexNumbers[0]};
        }

        ComplexNumber[] evenArray = new ComplexNumber[N/2];
        ComplexNumber[] oddArray = new ComplexNumber[N/2];
        for (int i=0; i<N/2; i++){
            evenArray[i] = complexNumbers[i*2];
            oddArray[i] = complexNumbers[i*2+1];
        }

        ComplexNumber[] transformedEvenArray = fft(evenArray);
        ComplexNumber[] transformedOddArray = fft(oddArray);

        ComplexNumber[] fftArray = new ComplexNumber[N];
        for(int k=0; k<N/2; k++){
            double angle = -(2*pi*k) / N;
            ComplexNumber constant = new ComplexNumber((float)Math.cos(angle),(float)Math.sin(angle));
            constant = constant.times(transformedOddArray[k]);
            fftArray[k] = transformedEvenArray[k].plus(constant);
            fftArray[k+(N/2)] = transformedEvenArray[k].minus(constant);
        }
        return fftArray;
    }


    public static int findPeakMagnitudePosition(){
        double magnitude = 0;
        int k = 0;;
        for(int i=3; i<frequencyBins.length/2; i++){
            ComplexNumber c = frequencyBins[i];
            double m = Math.sqrt(c.getReal()*c.getReal() + c.getImg() * c.getImg());
            if(m>magnitude) {
                magnitude = m;
                k = i;
            }

        }

       // Log.e("freq","k "+k+" magnitude is "+magnitude);
        return k;
    }

    private static double calculateEquivalentFrequency(double k, double sampleSize, double sampleRate){
        return (k / sampleSize) * sampleRate;
    }



}
