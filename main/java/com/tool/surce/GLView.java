package com.tool.surce;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLSurfaceView;
import android.opengl.GLES20;
import android.opengl.Matrix;
import android.view.MotionEvent;

import java.nio.*;
import java.io.File;

public class GLView extends GLSurfaceView {

    Renderer3D renderer;
    float previousX, previousY;

    public GLView(Context c) {
        super(c);
        setEGLContextClientVersion(2);
        renderer = new Renderer3D();
        setRenderer(renderer);
    }

    public void loadObj(String path) {
        renderer.load(path);
    }

    public void loadTexture(String path) {
        renderer.loadTexture(path);
    }

    public boolean onTouchEvent(MotionEvent e) {
        float x = e.getX();
        float y = e.getY();

        if (e.getAction() == MotionEvent.ACTION_MOVE) {
            float dx = x - previousX;
            float dy = y - previousY;
            renderer.angleX += dy;
            renderer.angleY += dx;
        }

        previousX = x;
        previousY = y;
        return true;
    }

    static class Renderer3D implements GLSurfaceView.Renderer {

        FloatBuffer vertexBuffer;
        int program;
        int textureId = -1;

        float angleX = 0;
        float angleY = 0;

        float[] mvp = new float[16];
        float[] proj = new float[16];
        float[] view = new float[16];
        float[] model = new float[16];

        public void load(String path) {
            float[] data = ObjLoader.load(path);
            ByteBuffer bb = ByteBuffer.allocateDirect(data.length * 4);
            bb.order(ByteOrder.nativeOrder());
            vertexBuffer = bb.asFloatBuffer();
            vertexBuffer.put(data);
            vertexBuffer.position(0);
        }

        public void loadTexture(String path) {
            Bitmap bmp = BitmapFactory.decodeFile(path);
            int[] tex = new int[1];
            GLES20.glGenTextures(1, tex, 0);
            textureId = tex[0];
        }

        public void onSurfaceCreated(javax.microedition.khronos.opengles.GL10 gl,
                                     javax.microedition.khronos.egl.EGLConfig config) {

            String v = "attribute vec4 vPos; uniform mat4 mvp; void main(){gl_Position=mvp*vPos;}";
            String f = "precision mediump float; void main(){gl_FragColor=vec4(0.0,1.0,1.0,1.0);}";

            int vs = GLES20.glCreateShader(GLES20.GL_VERTEX_SHADER);
            GLES20.glShaderSource(vs, v);
            GLES20.glCompileShader(vs);

            int fs = GLES20.glCreateShader(GLES20.GL_FRAGMENT_SHADER);
            GLES20.glShaderSource(fs, f);
            GLES20.glCompileShader(fs);

            program = GLES20.glCreateProgram();
            GLES20.glAttachShader(program, vs);
            GLES20.glAttachShader(program, fs);
            GLES20.glLinkProgram(program);

            GLES20.glClearColor(0,0,0,1);
        }

        public void onSurfaceChanged(javax.microedition.khronos.opengles.GL10 gl, int w, int h) {
            GLES20.glViewport(0,0,w,h);
            Matrix.perspectiveM(proj,0,45,(float)w/h,1,100);
        }

        public void onDrawFrame(javax.microedition.khronos.opengles.GL10 gl) {
            GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT);

            if (vertexBuffer == null) return;

            Matrix.setLookAtM(view,0,0,0,5,0,0,0,0,1,0);
            Matrix.setIdentityM(model,0);
            Matrix.rotateM(model,0,angleX,1,0,0);
            Matrix.rotateM(model,0,angleY,0,1,0);

            Matrix.multiplyMM(mvp,0,view,0,model,0);
            Matrix.multiplyMM(mvp,0,proj,0,mvp,0);

            GLES20.glUseProgram(program);

            int pos = GLES20.glGetAttribLocation(program,"vPos");
            int mat = GLES20.glGetUniformLocation(program,"mvp");

            GLES20.glEnableVertexAttribArray(pos);
            GLES20.glVertexAttribPointer(pos,3,GLES20.GL_FLOAT,false,0,vertexBuffer);
            GLES20.glUniformMatrix4fv(mat,1,false,mvp,0);

            GLES20.glDrawArrays(GLES20.GL_TRIANGLES,0,vertexBuffer.capacity()/3);

            GLES20.glDisableVertexAttribArray(pos);
        }
    }
}