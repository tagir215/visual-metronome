package com.example.artmetronome;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.util.Log;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

public class Recording {

    AudioRecord record;
    MainActivity main;
    OpenGLRenderer renderer;
    ComplexNumber[] oldFreqBins;
    List<Integer> crossingsList = new ArrayList<>();
    double timeStep;
    private List<Float>oldSamples;
    int bufferSize;
    boolean interrupted;
    final int SAMPLE_SIZE = 1024;
    final int SAMPLE_RATE_DIVIDER = 1;
    private double maxAmplitude;
    private double oldMaxAmplitude;
    int sampleRate;
    int gain = 1;
    int oldCrossingMean;
    Queue<Float> drawQueue = new ConcurrentLinkedQueue<>();
    List<Float> samples = Collections.synchronizedList(new ArrayList<>());
    final int DRAW_QUEUE_LENGTH = 50;
    List<Integer>hillsForMedian = Collections.synchronizedList(new ArrayList<>());
    int oldHills;
    FFT fft = new FFT(sampleRate);
    PlaybackAction playbackAction;

    Recording(PlaybackAction playbackAction, OpenGLRenderer renderer, int sampleRate, int bufferSize){
        this.renderer = renderer;
        this.sampleRate = sampleRate;
        this.bufferSize = bufferSize;
        this.playbackAction = playbackAction;
        this.main  = playbackAction.main;

    }
    void record() {

        createAudioRecord();
        record.startRecording();

        renderer.setIsRecording(true);
        timeStep = 1 / (double)sampleRate * SAMPLE_RATE_DIVIDER;
        while(!interrupted){
            short[] audioBuffer = new short[bufferSize];
            record.read(audioBuffer,0, bufferSize);

            int gap=0;
            for(int i=0; i<audioBuffer.length; i++){
                audioBuffer[i] =(short) (audioBuffer[i] *gain);


                float mag = audioBuffer[i]*(1/32767f);
                if(gap>=100){
                    buildDrawQueue(mag);
                    gap = 0;
                }
                samples.add(mag);

                gap++;
                if(Math.abs(mag)>maxAmplitude)
                    maxAmplitude = Math.abs(mag);

                if(samples.size()>=SAMPLE_SIZE) {
                    renderZeroCrossing();
                    samples.clear();
                    maxAmplitude = 0;
                }
            }
            //playbackAction.bufferQueue.add(audioBuffer);
            //playbackAction.amplitudeQueue.add(maxAmplitude);

        }
    }
    private void renderFFT(){
        if(maxAmplitude>0.01f) {
            ComplexNumber[] freqBins = fft.getFFT(samples);
            if(oldFreqBins == null)
                oldFreqBins = freqBins;
            double r = CrossCorrelation.crossCorrelation(oldFreqBins,freqBins);
            oldFreqBins = freqBins;
            if(r<300) {
                renderer.soundExists = true;
                renderer.magnitude = (float)maxAmplitude;
            }
        }
    }
    private void renderAutocorrelation(){
        if(maxAmplitude>0.01f){
            renderer.frequency = Autocorrelation.getFrequencyViaDotProduct(samples,timeStep);
            renderer.soundExists = true;
            renderer.magnitude = (float)maxAmplitude;
        }
    }
    private void renderHills(){
        if(maxAmplitude>0.005f){
            int hills = ZeroCrossing.countHills(samples);
            if(hillsForMedian.size()<10){
                hillsForMedian.add(hills);
            }else{
                int median = ZeroCrossing.getMedian(hillsForMedian);
                Log.e("hills",median+" hills");
                int difference = Math.abs(oldHills-median);
                if(difference>5){
                    renderer.soundExists = true;
                    renderer.magnitude = difference/200f;
                    oldHills = median;
                }
                hillsForMedian.clear();
            }

        }

    }
    private void renderZeroCrossing(){
        if(maxAmplitude>0.001f) {
            List<Float> crossings = ZeroCrossing.getZeroCrossings(samples, sampleRate);
            crossingsList.add(crossings.size());
            if(crossingsList.size()>=10){
                int sum=0;
                for(int i=0; i<crossingsList.size(); i++){
                    sum+=crossingsList.get(i);
                }
                int mean = sum/crossingsList.size();
                int difference = Math.abs(mean- oldCrossingMean);

                if(difference>2) {
                    renderer.soundExists = true;
                    renderer.colorRhythm = new float[]{1, (float)maxAmplitude*10, 0};
                    renderer.magnitude = (float)maxAmplitude*2;
                    renderer.difference = difference;
                    oldCrossingMean = mean;
                }
                crossingsList.clear();

            }
            oldMaxAmplitude = maxAmplitude;
        }
    }



    private void createAudioRecord() {
        if (ActivityCompat.checkSelfPermission(main, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
            getPermission();
            return;
        }
        record = new AudioRecord(MediaRecorder.AudioSource.VOICE_RECOGNITION, sampleRate,
                AudioFormat.CHANNEL_IN_STEREO, AudioFormat.ENCODING_PCM_16BIT,bufferSize);
    }

    private void buildDrawQueue(float mag){

        drawQueue.add(mag);
        if(drawQueue.size()> DRAW_QUEUE_LENGTH) {
            drawQueue.remove();
            main.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Object[] drawArray = drawQueue.toArray();
                    renderer.updateAmplitudeLines(drawArray);
                }
            });
        }
    }

    void getPermission() {
        if (ContextCompat.checkSelfPermission(main, Manifest.permission.RECORD_AUDIO)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions((Activity) main, new String[]{
                    Manifest.permission.RECORD_AUDIO
            }, 0);
        }
    }

}
