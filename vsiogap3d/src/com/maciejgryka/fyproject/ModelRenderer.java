/*
 * Copyright (C) 2007 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

/* 
 * Modified by Maciej Gryka (2009)
 * maciej.gryka@gmail.com
 */

package com.maciejgryka.fyproject;

import java.io.IOException;
import java.io.InputStream;

import javax.microedition.khronos.egl.EGL10;
import javax.microedition.khronos.opengles.GL10;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLUtils;

/**
 * Render a building model with all the indicators.
 */
public class ModelRenderer implements GLSurfaceView.Renderer {
    public ModelRenderer(Context context) {
    	mContext = context;
        mModel = new Model(mContext);
        mIndicators = new Indicators();
    }

    public void drawFrame(GL10 gl) {
        gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);
        gl.glClearColor(0.25f, 0.25f, 0.25f, 1.0f);
        
        gl.glMatrixMode(GL10.GL_MODELVIEW);
        gl.glLoadIdentity();

        gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
        //gl.glEnableClientState(GL10.GL_COLOR_ARRAY);
        
        
        if (zoom != 0) changeDistance((float)zoom/50);
        polarView(gl, panX, panY, distance, elevation, rotation);

//        gl.glScalef(1.1f, 1.1f, 1.1f);

        mIndicators.draw(gl);
        mModel.draw(gl);
    }

    public int[] getConfigSpec() { 
		/*
		 *  We want a depth buffer, don't care about the
		 *  details of the color buffer.
		 */
		int[] configSpec = {
		        EGL10.EGL_DEPTH_SIZE,   16,
		        EGL10.EGL_NONE
		};
		return configSpec;
    }

    public void sizeChanged(GL10 gl, int width, int height) {
         gl.glViewport(0, 0, width, height);

         /*
          * Set our projection matrix. This doesn't have to be done
          * each time we draw, but usually a new projection needs to
          * be set when the viewport is resized.
          */

         float ratio = (float) width / height;
         gl.glMatrixMode(GL10.GL_PROJECTION);
         gl.glLoadIdentity();
         gl.glFrustumf(-ratio, ratio, -1, 1, 1, 10);
    }

    public void surfaceCreated(GL10 gl) {
        
    	gl.glDisable(GL10.GL_DITHER);
    	gl.glHint(GL10.GL_PERSPECTIVE_CORRECTION_HINT, GL10.GL_FASTEST);
    	gl.glShadeModel(GL10.GL_SMOOTH);
    	gl.glEnable(GL10.GL_DEPTH_TEST);
    	gl.glEnable(GL10.GL_TEXTURE_2D);
    	
    	gl.glClearColor(0,0,0,0);
    	bindTextures(gl);
    	mIndicators.populateIndicatorIcons(mContext, mTextureNames);
    }
    
    private void bindTextures(GL10 gl)
    {
    	mTextureNames = new int[3];
		gl.glGenTextures(3, mTextureNames, 0);
		
		bind(gl, mTextureNames[0], R.drawable.thermometer);
		bind(gl, mTextureNames[1], R.drawable.lightbulb);
        bind(gl, mTextureNames[2], R.drawable.occupied);
    }
    
    private void bind(GL10 gl, int type, int id)
    {
    	gl.glBindTexture(GL10.GL_TEXTURE_2D, type);
    	
    	gl.glTexEnvx(GL10.GL_TEXTURE_ENV, GL10.GL_TEXTURE_ENV_MODE, GL10.GL_MODULATE);
        gl.glTexParameterx(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MAG_FILTER, GL10.GL_NEAREST);
        gl.glTexParameterx(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MIN_FILTER, GL10.GL_NEAREST);
        gl.glTexParameterx(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_WRAP_S, GL10.GL_REPEAT);
        gl.glTexParameterx(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_WRAP_T, GL10.GL_REPEAT);
        
        InputStream is = mContext.getResources().openRawResource(id);
		Bitmap bitmap = BitmapFactory.decodeStream(is);
        GLUtils.texImage2D(GL10.GL_TEXTURE_2D, 0, bitmap, 0);
        bitmap.recycle();
        
        try {
        	is.close();
        } catch (IOException e) {
        	// Ignore
        }
    }
    
    private void polarView(GL10 gl, float panX, float panY, float distance, float elevation, float rotation) {
		gl.glTranslatef(panX, 0, 0);
		gl.glTranslatef(0, panY, 0);
    	gl.glTranslatef(0.0f, 0.0f, -distance);
		gl.glRotatef(elevation, 1.0f, 0.0f, 0.0f);
		gl.glRotatef(rotation, 0.0f, 1.0f, 0.0f);
	}
    
    public void changeElevation(float elevation) {
    	if (this.elevation + elevation >= 0 && this.elevation + elevation <= 90) {
    		this.elevation += elevation;
    	}
	}
    
    public void changeRotation(float rotation) {
    	this.rotation += rotation;
    }
    
    public void changeDistance(float distance) {
    	if (this.distance + distance >= 0 && this.distance + distance <= 9) {
        	this.distance += distance;	
    	}
    }
    
    public void changePanX(float panX) {
    	this.panX += panX;
    }
    
    public void changePanY(float panY) {
    	this.panY += panY;
    }
    
    public void setZoom(int zoom)
    {
    	this.zoom = zoom;
    }
    
    private Context mContext;
	
    private Model mModel;
    private Indicators mIndicators;
    private int[] mTextureNames;
    
    private int zoom = 0;
    private float panX = 0.0f;
    private float panY = 0.0f;
    private float distance = 4;
    private float elevation = 45;
    private float rotation = 90;
}
