package com.example.artmetronome;

import android.opengl.GLES20;
import android.opengl.Matrix;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.util.LinkedList;
import java.util.Queue;

public class GLLine {
    FloatBuffer vertexBuffer;
    static final int COORDS_PER_VERTEX = 3;
    int program;
    int positionHandle;
    int colorHandle;
    int vertexCount;
    int vertexStride;
    int vPMatrixHandle;
    float width = 3f;
    boolean active = true;
    float[] coords = new float[] {
            -1f,0.5f,0,
            1f,0.5f,0,
    };
    float color[] = { 1f,0.5f,0,1f};

    void moveForward(float speed){
        float x = coords[0]-speed;
        float y = coords[1];
        float z = coords[2];
        float xx = coords[3]-speed;
        float yy = coords[4];
        float zz = coords[5];
        if(x<-1){
            active = false;
            setColor(0,0,0,0);
        }
        setVertices(x,y,z,xx,yy,zz);
    }

    private final String vertexShaderCode =
            "attribute vec4 vPosition;" +
                    "void main() {" +
                    "  gl_Position = vPosition;" +
                    "}";

    private final String fragmentShaderCode =
            "precision mediump float;" +
                    "uniform vec4 vColor;" +
                    "void main() {" +
                    "  gl_FragColor = vColor;" +
                    "}";



    public GLLine(float x, float y, float z, float xx, float yy, float zz){
        setVertices(x,y,z,xx,yy,zz);

        int vertexShader = OpenGLRenderer.loadShader(GLES20.GL_VERTEX_SHADER,vertexShaderCode);
        int fragmentShader = OpenGLRenderer.loadShader(GLES20.GL_FRAGMENT_SHADER,fragmentShaderCode);
        program = GLES20.glCreateProgram();
        GLES20.glAttachShader(program,vertexShader);
        GLES20.glAttachShader(program,fragmentShader);
        GLES20.glLinkProgram(program);

        GLES20.glBlendFunc(GLES20.GL_SRC_ALPHA, GLES20.GL_ONE_MINUS_SRC_ALPHA);
        GLES20.glEnable( GLES20.GL_BLEND );
        vertexCount = coords.length / COORDS_PER_VERTEX;
        vertexStride = COORDS_PER_VERTEX * 4;
    }

    void setVertices(float x, float y, float z, float xx, float yy, float zz){
        coords[0] = x;
        coords[1] = y;
        coords[2] = z;
        coords[3] = xx;
        coords[4] = yy;
        coords[5] = zz;

        createVertexBuffer();
    }


    void createVertexBuffer(){
        ByteBuffer byteBuffer = ByteBuffer.allocateDirect(coords.length*4);
        byteBuffer.order(ByteOrder.nativeOrder());
        vertexBuffer = byteBuffer.asFloatBuffer();
        vertexBuffer.put(coords);
        vertexBuffer.position(0);
    }

    void setColor(float r, float g, float b, float a){
        color[0] = r;
        color[1] = g;
        color[2] = b;
        color[3] = a;

    }

    void setWidth(float w){
        width = w;
    }

    public void draw(){
        GLES20.glUseProgram(program);
        positionHandle = GLES20.glGetAttribLocation(program,"vPosition");
        GLES20.glEnableVertexAttribArray(positionHandle);
        GLES20.glVertexAttribPointer(positionHandle,COORDS_PER_VERTEX,GLES20.GL_FLOAT,
                false,vertexStride,vertexBuffer);
        colorHandle = GLES20.glGetUniformLocation(program,"vColor");

        GLES20.glUniform4fv(colorHandle,1,color,0);
        GLES20.glLineWidth(width);
        GLES20.glDrawArrays(GLES20.GL_LINES,0,vertexCount);
        GLES20.glDisableVertexAttribArray(positionHandle);
    }




}
