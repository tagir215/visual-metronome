package com.example.artmetronome;

import static com.example.artmetronome.Settings.SAMPLE_RATE;
import static com.example.artmetronome.Settings.bufferSize;
import android.Manifest;
import android.app.Activity;
import android.content.Context;
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
import java.util.Set;
import java.util.concurrent.ConcurrentLinkedQueue;

public class Recording {

    private static ComplexNumber[] oldFreqBins;
    private static List<Integer> crossingsList = new ArrayList<>();
    private static List<Float> oldSamples;
    public static boolean interrupted = true;
    private final static int SAMPLE_RATE_DIVIDER = 1;
    private static double maxAmplitude;
    private static double oldMaxAmplitude;
    private static final int SAMPLE_SIZE = 1024;
    private static int gain = 1;
    private static double timeStep = 0;
    private static int oldCrossingMean;
    private static Queue<Float> drawQueue = new ConcurrentLinkedQueue<>();
    private static List<Float> samples = Collections.synchronizedList(new ArrayList<>());
    final static int DRAW_QUEUE_LENGTH = 50;
    private static List<Integer> hillsForMedian = Collections.synchronizedList(new ArrayList<>());
    private static int oldHills;

    public static void record(Context context,OpenGLRenderer renderer,PlaybackAction playbackAction) {

        AudioRecord record = createAudioRecord(context);
        record.startRecording();

        renderer.setIsRecording(true);
        timeStep = 1 / (double) SAMPLE_RATE * SAMPLE_RATE_DIVIDER;
        while (!interrupted) {
            short[] audioBuffer = new short[bufferSize];
            record.read(audioBuffer, 0, bufferSize);

            int gap = 0;
            for (int i = 0; i < audioBuffer.length; i++) {
                audioBuffer[i] = (short) (audioBuffer[i] * gain);

                float mag = audioBuffer[i] * (1 / 32767f);
                if (gap >= 100) {
                    buildDrawQueue(((MainActivity) context),renderer,mag);
                    gap = 0;
                }
                samples.add(mag);

                gap++;
                if (Math.abs(mag) > maxAmplitude)
                    maxAmplitude = Math.abs(mag);

                if (samples.size() >= SAMPLE_SIZE) {
                    renderZeroCrossing(renderer);
                    samples.clear();
                    maxAmplitude = 0;
                }
            }
            playbackAction.bufferQueue.add(audioBuffer);
            playbackAction.amplitudeQueue.add(maxAmplitude);

        }
    }

    private void renderFFT() {
       // if (maxAmplitude > 0.01f) {
       //     ComplexNumber[] freqBins = FFT.getFFT(samples);
       //     if (oldFreqBins == null)
       //         oldFreqBins = freqBins;
       //     double r = CrossCorrelation.crossCorrelation(oldFreqBins, freqBins);
       //     oldFreqBins = freqBins;
       //     if (r < 300) {
       //         renderer.soundExists = true;
       //         renderer.magnitude = (float) maxAmplitude;
       //     }
       // }
    }

    private void renderAutocorrelation(OpenGLRenderer renderer) {
        if (maxAmplitude > 0.01f) {
            renderer.frequency = Autocorrelation.getFrequencyViaDotProduct(samples, timeStep);
            renderer.soundExists = true;
            renderer.magnitude = (float) maxAmplitude;
        }
    }

    private void renderHills(OpenGLRenderer renderer) {
        if (maxAmplitude > 0.005f) {
            int hills = ZeroCrossing.countHills(samples);
            if (hillsForMedian.size() < 10) {
                hillsForMedian.add(hills);
            } else {
                int median = ZeroCrossing.getMedian(hillsForMedian);
                Log.e("hills", median + " hills");
                int difference = Math.abs(oldHills - median);
                if (difference > 5) {
                    renderer.soundExists = true;
                    renderer.magnitude = difference / 200f;
                    oldHills = median;
                }
                hillsForMedian.clear();
            }

        }

    }

    private static void renderZeroCrossing(OpenGLRenderer renderer) {
        if (maxAmplitude > 0.001f) {
            List<Float> crossings = ZeroCrossing.getZeroCrossings(samples, SAMPLE_RATE);
            crossingsList.add(crossings.size());
            if (crossingsList.size() >= 10) {
                int sum = 0;
                for (int i = 0; i < crossingsList.size(); i++) {
                    sum += crossingsList.get(i);
                }
                int mean = sum / crossingsList.size();
                int difference = Math.abs(mean - oldCrossingMean);

                if (difference > 2) {
                    renderer.soundExists = true;
                    renderer.colorRhythm = new float[]{1, (float) maxAmplitude * 10, 0};
                    renderer.magnitude = (float) maxAmplitude * 2;
                    renderer.difference = difference;
                    oldCrossingMean = mean;
                }
                crossingsList.clear();

            }
            oldMaxAmplitude = maxAmplitude;
        }
    }


    private static AudioRecord createAudioRecord(Context context) {

        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {

            return null;
        }
        return new AudioRecord(MediaRecorder.AudioSource.VOICE_RECOGNITION, SAMPLE_RATE,
                AudioFormat.CHANNEL_IN_STEREO, AudioFormat.ENCODING_PCM_16BIT, Settings.bufferSize);
    }

    private static void buildDrawQueue(MainActivity main,OpenGLRenderer renderer,float mag){

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

    void getPermission(MainActivity main) {
        if (ContextCompat.checkSelfPermission(main, Manifest.permission.RECORD_AUDIO)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions((Activity) main, new String[]{
                    Manifest.permission.RECORD_AUDIO
            }, 0);
        }
    }

}
