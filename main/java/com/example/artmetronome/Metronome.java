package com.example.artmetronome;

import java.util.Timer;
import java.util.TimerTask;

public class Metronome {
    Timer timer;
    TimerTask task;
    OpenGLRenderer renderer;
    double bpm = 100;
    int tick = 1;
    Metronome(OpenGLRenderer renderer){
        this.renderer = renderer;
    }
    void start(){
        double time = 60/bpm/4 * 1000;
        timer = new Timer();
        task = new TimerTask() {
            @Override
            public void run() {


                if(tick==1)
                    renderer.metronomeTick=1;
                else if(tick==3)
                    renderer.metronomeTick=3;
                else
                    renderer.metronomeTick=2;

                if (tick == 4)
                    tick = 1;
                else
                    tick++;


            }
        };
        timer.schedule(task,(int)time,(int)time);
    }
    void setBpm(int bpm){
        this.bpm = bpm;
    }
    void stop(){
        timer.cancel();
        task.cancel();
        timer = null;
        task = null;
    }
}
