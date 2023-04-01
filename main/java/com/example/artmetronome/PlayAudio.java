package com.example.artmetronome;

import android.graphics.Color;
import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioTrack;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

public class PlayAudio {
    public static boolean interrupted = false;

    private static AudioTrack createAudioTrack(int sampleRate, int bufferSize){
        AudioTrack track = new AudioTrack(AudioManager.STREAM_MUSIC,sampleRate,
                AudioFormat.CHANNEL_IN_STEREO,AudioFormat.ENCODING_PCM_16BIT,bufferSize,AudioTrack.MODE_STREAM);
        track.setPlaybackRate(sampleRate);
        return track;
    }

    public static void playAudio(Queue<short[]>bufferQueue,OpenGLRenderer renderer,PlaybackAction playbackAction){
        AudioTrack track = createAudioTrack(Settings.SAMPLE_RATE,Settings.bufferSize);
        interrupted = false;
        track.play();
        Thread threadPlayer = new Thread(new Runnable() {
            @Override
            public void run() {
                renderer.setIsRecording(false);
                float totalSize = bufferQueue.size();
                while (bufferQueue.size()>0 && !interrupted){
                    short[] data = bufferQueue.poll();
                    track.write(data,0,data.length);
                    renderer.setPlayAudioPosition((float) bufferQueue.size(), totalSize);
                }
                if(!interrupted)
                    playbackAction.startRecording();
                else
                    bufferQueue.clear();
            }
        });
        threadPlayer.start();

    }
}
