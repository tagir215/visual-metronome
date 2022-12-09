package com.example.artmetronome;

import android.graphics.Color;
import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioTrack;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

public class PlayAudio {

    AudioTrack track;
    OpenGLRenderer renderer;
    PlaybackAction playbackAction;

    PlayAudio(PlaybackAction playbackAction, OpenGLRenderer renderer){
        this.renderer = renderer;
        this.playbackAction = playbackAction;

    }
    void createAudioTrack(int sampleRate, int bufferSize){
        track = new AudioTrack(AudioManager.STREAM_MUSIC,sampleRate,
                AudioFormat.CHANNEL_IN_STEREO,AudioFormat.ENCODING_PCM_16BIT,bufferSize,AudioTrack.MODE_STREAM);
        track.setPlaybackRate(sampleRate);

    }

    void playAudio(Queue<short[]>bufferQueue){
        track.play();
        Thread threadPlayer = new Thread(new Runnable() {
            @Override
            public void run() {
                renderer.setIsRecording(false);
                float totalSize = bufferQueue.size();
                while (bufferQueue.size()>0 && !playbackAction.interrupted){
                    short[] data = bufferQueue.poll();
                    track.write(data,0,data.length);
                    renderer.setPlayAudioPosition((float) bufferQueue.size(), totalSize);
                }
                if(!playbackAction.interrupted)
                    playbackAction.startRecording();
                else
                    bufferQueue.clear();
            }
        });
        threadPlayer.start();

    }
}
