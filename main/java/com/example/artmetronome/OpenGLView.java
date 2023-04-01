package com.example.artmetronome;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.util.AttributeSet;

public class OpenGLView extends GLSurfaceView {
    public static OpenGLRenderer renderer;

    public OpenGLView(Context context) {
        super(context);
        init();
    }

    public OpenGLView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    void init(){
        setEGLContextClientVersion(2);
        setPreserveEGLContextOnPause(true);
    }

    void setup(MainActivity main){
        renderer = new OpenGLRenderer(main);
        setRenderer(renderer);
    }
}
