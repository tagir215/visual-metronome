package com.example.artmetronome;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioRecord;
import android.media.audiofx.AcousticEchoCanceler;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Queue;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ConcurrentLinkedQueue;


public class PlaybackAction {
    private static int MICROPHONE_PERMISSION_CODE = 200;
    MainActivity main;
    AcousticEchoCanceler acousticEchoCanceler;
    List<Float>samples = Collections.synchronizedList(new ArrayList<>());
    final int DRAW_QUEUE_LENGTH = 50;
    int audioEncoding;
    Queue<short[]>bufferQueue = new ConcurrentLinkedQueue<>();
    Queue<Double> amplitudeQueue = new ConcurrentLinkedQueue<>();
    Thread threadRecord;
    boolean soundHasExisted;
    int soundNeverExistedCount =0;
    ImageButton buttonMic;
    Timer timer;
    TimerTask timerTask;
    Settings settings;
    Autocorrelation autocorrelation;
    Recording recording;
    PlayAudio playAudio;

    PlaybackAction(MainActivity main) {
        this.main = main;
        setButton();
        audioSettings();
        settings = new Settings(main);
        Metronome.start();
    }

    void setTimer(){
        soundHasExisted = false;
        soundNeverExistedCount = 0;
        timer = new Timer();
        timerTask = new TimerTask() {
            @Override
            public void run() {
                boolean soundExists = false;
                int size = amplitudeQueue.size();
                for (int i=0; i<size; i++){
                    double amp = amplitudeQueue.poll();
                    if(amp>0.005f){
                        soundExists = true;
                        soundHasExisted = true;
                    }
                    if(!soundHasExisted){
                        soundNeverExistedCount++;
                        if(soundNeverExistedCount > 10)
                            bufferQueue.remove();
                    }
                }

                if(!soundExists && soundHasExisted){
                    endRecordingAndPlay();
                }
            }
        };
        timer.schedule(timerTask,(int)settings.waitTime,(int)settings.waitTime);
    }

    void endTimer(){
        if(timer == null)
            return;
        timer.cancel();
        timerTask.cancel();
        timer = null;
        timerTask = null;
    }

    void getPermission() {
        if (ContextCompat.checkSelfPermission(main, Manifest.permission.RECORD_AUDIO)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions((Activity) main, new String[]{
                    Manifest.permission.RECORD_AUDIO
            }, MICROPHONE_PERMISSION_CODE);
        }
    }

    boolean checkMicrophone() {
        if (main.getPackageManager().hasSystemFeature(PackageManager.FEATURE_MICROPHONE))
            return true;
        else
            return false;
    }

    void setButton() {
        buttonMic = main.findViewById(R.id.micBtn);
        buttonMic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checkMicrophone())
                    getPermission();
                if(Recording.interrupted) {
                    Recording.interrupted = false;
                    startRecording();
                }
                else{
                    Recording.interrupted = true;
                    stopAll();
                }
                PlayAudio.interrupted = true;
            }
        });
    }

    void startRecording(){
        setTimer();
        buttonMic.setImageResource(R.drawable.ic_baseline_mic_24);
        buttonMic.setColorFilter(Color.RED);
        recording.interrupted = false;
        threadRecord = new Thread(new Runnable() {
            @Override
            public void run() {
                Recording.record(main,OpenGLView.renderer,PlaybackAction.this);
            }
        });
        threadRecord.start();

    }

    void stopAll(){
        if(threadRecord !=null)
            threadRecord.interrupt();
        threadRecord = null;
        endTimer();
        OpenGLView.renderer.recording=false;
        buttonMic.setImageResource(R.drawable.ic_baseline_mic_24);
        buttonMic.setColorFilter(Color.WHITE);
    }

    void audioSettings(){
        AudioManager audioManager = (AudioManager) main.getSystemService(Context.AUDIO_SERVICE);
        audioManager.setMode(AudioManager.MODE_NORMAL);
        audioEncoding = AudioFormat.ENCODING_PCM_16BIT;
        Settings.bufferSize = AudioRecord.getMinBufferSize(Settings.SAMPLE_RATE, AudioFormat.CHANNEL_IN_MONO, audioEncoding);
    }







    void endRecordingAndPlay(){
        threadRecord.interrupt();
        threadRecord = null;
        Recording.interrupted = true;
        endTimer();
        buttonMic.setImageResource(R.drawable.ic_baseline_play_arrow_24);
        buttonMic.setColorFilter(Color.WHITE);
        playAudio.playAudio(bufferQueue,OpenGLView.renderer,this);
    }






}
