package com.example.artmetronome;

import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import static com.example.artmetronome.Metronome.metronomeTick;
import java.util.ArrayList;
import java.util.List;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

public class OpenGLRenderer implements GLSurfaceView.Renderer {
    private GLLine[] audioLines;
    private GLLine[] borderLines;
    private GLLine playerUnderLine;
    private GLLine finishedUnerLine;
    private List<GLLine>metronomeList = new ArrayList<>();
    private List<GLLine>rhythmList = new ArrayList<>();
    private MainActivity main;
    private boolean silenceHasExisted;
    private float offsetX = 0.005f;
    private float graph1Y = 0.3f;
    private float graph2Y = -0.3f;
    private float speed = 0.01f;
    private final float MAX_MAGNITUDE =0.3f;
    private float oldMagnitude = 0.1f;
    private boolean onTick;
    private int DRAW_LIST_SIZE = 50;
    private final int DRAW_FFT_SIZE = 1024/2;
    private final int RHYTM_LIST_SIZE = 400;
    private final float GD = 2f;
    private final float FFTGD = 3f;
    public boolean soundExists;
    public double frequency;
    public float magnitude = 0.1f;
    public float difference = 0;
    public float[] colorRhythm = new float[] {0,1,0,1};
    public boolean recording;
    OpenGLRenderer(MainActivity main){
        this.main = main;
    }

    @Override
    public void onSurfaceCreated(GL10 gl10, EGLConfig eglConfig) {
        createAmplitudeLines();
        createBorderLines();
        createPlayerLines();
    }



    @Override
    public void onSurfaceChanged(GL10 gl10, int i, int i1) {

    }


    @Override
    public void onDrawFrame(GL10 gl10) {
        drawBorders();
        drawPlayer();
        drawMagnitude();

        drawMetronomeLinePosition();
        drawMetronomeLineType();
        drawRhythm();

        onTick = false;

    }
    private void drawMagnitude(){
        for (int i = 0; i< audioLines.length-1; i++){
            if(i!=0)
                audioLines[i].draw();
        }
    }

    private void drawPlayer(){
        playerUnderLine.draw();
        finishedUnerLine.draw();
    }

    private void drawBorders(){

        for(int i=0; i<borderLines.length; i++){
            borderLines[i].draw();
        }
    }

    private void drawMetronomeLineType(){
        if(metronomeTick>0){
            GLLine line = null;
            if(metronomeTick==1)
                line = new GLLine(1,-0.2f+graph2Y,0,1,0.2f+graph2Y,0);
            if(metronomeTick==3)
                line = new GLLine(1, -0.06f + graph2Y, 0, 1, 0.06f + graph2Y, 0);
            if(metronomeTick==2)
                line = new GLLine(1,-0.015f+graph2Y,0,1,0.015f+graph2Y,0);


            line.setWidth(1f);
            metronomeList.add(line);
            metronomeTick = 0;
        }


    }

    private void drawMetronomeLinePosition(){
        if(metronomeList.size()>0 && !metronomeList.get(0).active) {
            metronomeList.remove(0);
        }
        if(rhythmList.size()>0 && !rhythmList.get(0).active) {
            rhythmList.remove(0);
        }
        for(int i=0; i<metronomeList.size(); i++){
            GLLine line = metronomeList.get(i);
            line.moveForward(speed);
            float alpha = 1;
            if(!recording)
                alpha = 0;
            line.setColor(0.5f,0.5f,0.5f,alpha);
            if(Math.abs(metronomeList.get(i).coords[0]) < 0.025)
                onTick = true;
            metronomeList.get(i).draw();
        }
        for(int i=0; i<rhythmList.size(); i++){
            rhythmList.get(i).moveForward(speed);
            rhythmList.get(i).draw();
        }
    }

    void drawRhythm(){
        if(soundExists){
            float y = difference / 150f;
            float offsetY = 0;
            GLLine line = new GLLine(0,graph2Y-y +offsetY,0,0,graph2Y+y+offsetY,0);
            line.setColor(colorRhythm[0],colorRhythm[1],colorRhythm[2],(float)magnitude/MAX_MAGNITUDE*10);
            line.setWidth(4f);
            rhythmList.add(line);
            soundExists = false;
        }
    }



    void createPlayerLines(){
        playerUnderLine = new GLLine(-1f,-1,0,1f,-1,0);
        playerUnderLine.setColor(1,1,1,0);
        playerUnderLine.setWidth(1f);
        finishedUnerLine = new GLLine(-1,-1,0,-1,-1,0);
        finishedUnerLine.setColor(0,0,1,0);
        finishedUnerLine.setWidth(6f);
    }
    void createAmplitudeLines(){
        int size = DRAW_LIST_SIZE;
        audioLines = new GLLine[size];
        float travelX = 2f/(float)(size-1);
        float x = -1f ;
        float y = 0.005f;
        for(int i=0; i<size; i++){
            audioLines[i] = new GLLine(x, graph1Y -y,0,x, graph1Y +y,0);
            x += travelX;
        }
    }
    void createBorderLines(){
        borderLines = new GLLine[]{

                new GLLine(0,0.3f+graph2Y,0,0,-0.3f+graph2Y,0),
        };
        setColorAndWidth(borderLines,1,1,1,1,3f);
    }



    void setIsRecording(boolean bool){
        recording = bool;
        if(bool){
            playerUnderLine.setColor(1,1,1,0);
            finishedUnerLine.setColor(0,0,1,0);
            setColorAndWidth(audioLines,1,0.5f,0,1,3f);
        }
        else{
            playerUnderLine.setColor(1,1,1,1f);
            finishedUnerLine.setColor(0,0,1,1f);
            setColorAndWidth(audioLines,1,0.5f,1,0,3f);
        }
    }

    void setColorAndWidth(GLLine[] lines, float r, float g, float b, float a, float w){
        for(int i = 0; i< lines.length; i++){
            lines[i].setColor(r,g,b,a);
            lines[i].setWidth(w);
        }
    }

    void updateAmplitudeLines(Object[] drawArray){
        int size = drawArray.length;
        float travelX = 2f/(float)(size-1);
        float x = -1f ;
        float y = 0;
        for(int i = 0; i< DRAW_LIST_SIZE; i++){
            float rawy =(float)drawArray[i];
            y = (float)rawy * GD;
            if(y<0.005f)
                y=0.005f;

            audioLines[i].setVertices(x, graph1Y -y,0,x, graph1Y +y,0);
            x += travelX;

        }

    }

    void updateFTTGraph(ComplexNumber[] frequencyBins){
        float travelX = 2f/(float)(DRAW_FFT_SIZE-1);
        float x = -1f ;
        for (int i=0; i<DRAW_FFT_SIZE; i++){
            ComplexNumber c = frequencyBins[i];
            double magnitude =Math.sqrt( c.getReal()*c.getReal() + c.getImg()*c.getImg());
            audioLines[i].setVertices(x,graph1Y,0,x,graph1Y+(float)magnitude/FFTGD,0);
            x += travelX;
        }
    }




    void setPlayAudioPosition(float currentSize, float totalSize){
        float percentage =1f- currentSize / totalSize;
        float position = percentage*2 -1;
        finishedUnerLine.setVertices(-1+offsetX,-1f,-0.1f,position-offsetX,-1f,-0.1f);
    }

    public static int loadShader(int type, String shaderCode){
        int shader = GLES20.glCreateShader(type);
        GLES20.glShaderSource(shader,shaderCode);
        GLES20.glCompileShader(shader);
        return shader;
    }
}
