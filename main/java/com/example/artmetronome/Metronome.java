package com.example.artmetronome;

import java.util.Timer;
import java.util.TimerTask;

public class Metronome {
    private static Timer timer;
    private static TimerTask task;
    public static double bpm = 100;
    private static int tick = 1;
    public static int metronomeTick = 0;
    public static void start(){
        double time = 60/bpm/4 * 1000;
        timer = new Timer();
        task = new TimerTask() {
            @Override
            public void run() {


                if(tick==1)
                    metronomeTick=1;
                else if(tick==3)
                    metronomeTick=3;
                else
                    metronomeTick=2;

                if (tick == 4)
                    tick = 1;
                else
                    tick++;


            }
        };
        timer.schedule(task,(int)time,(int)time);
    }

    public static void stop(){
        timer.cancel();
        task.cancel();
        timer = null;
        task = null;
    }
    public static void reset(){
        stop();
        start();
    }
}
