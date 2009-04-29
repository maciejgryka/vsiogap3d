package com.maciejgryka.fyproject;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.nio.ShortBuffer;

import javax.microedition.khronos.opengles.GL10;

class IndicatorIcon {
	/**
	 * Creates an IndicatorIcon - textured 2D rectangle.
	 * @param type determines the texture (0 - temperature, 1 - light, 2 - occupancy)
	 * @param translation - position of the indicator (preferably somewhere on the wall)
	 * @param rotation - rotation of the indicator
	 */
    public IndicatorIcon(int type, int value, float translation[], float rotation[]) {
    	mType = type;
    	mTranslation = translation;
    	mRotation = rotation;
    	
    	int one = 0x10000;
    	int side = one/4;
    	// A square of (2*side) size sides
    	int coords[] = {
    			-side, -side, 0,
    			side, -side, 0,
    			side, side, 0,
    			-side, side, 0
    	};
    	        
        short[] indices = {
        		1, 2, 3,
        		3, 0, 1
        };
        
        float color[] = {
        		1.0f, 1.0f, 1.0f, 0f
        };
        
        switch (type) {
    	case 1:
    		if (value > 25) {
    			color[0] = 1.0f;
    			color[1] = 0.7f;
    			color[2] = 0.7f;
    			color[3] = 0.0f;
    		} else {
    			color[0] = 0.7f;
    			color[1] = 0.7f;
    			color[2] = 1.0f;
    			color[3] = 0.0f;
    		}
    		break;
    	case 2:
    		if (value > 50) {
    			color[0] = 1.0f;
    			color[1] = 1.0f;
    			color[2] = 0.2f;
    			color[3] = 0.0f;
    		} else {
    			color[0] = 0.5f;
    			color[1] = 0.5f;
    			color[2] = 0.2f;
    			color[3] = 0.0f;
    		}
    		break;
    	}
        
		float colors[] = {
				color[0], color[1], color[2], color[3],
				color[0], color[1], color[2], color[3],
				color[0], color[1], color[2], color[3],
				color[0], color[1], color[2], color[3],
		};

        // Buffers to be passed to gl*Pointer() functions
        // must be direct, i.e., they must be placed on the
        // native heap where the garbage collector cannot
        // move them.
        //
        // Buffers with multi-byte datatypes (e.g., short, int, float)
        // must have their byte order set to native order
        ByteBuffer vbb = ByteBuffer.allocateDirect(VERTS * 3 * 4);
        vbb.order(ByteOrder.nativeOrder());
        mVertexBuffer = vbb.asIntBuffer();
        mVertexBuffer.put(coords);
        mVertexBuffer.position(0);

        ByteBuffer ibb = ByteBuffer.allocateDirect(indices.length*4);
        ibb.order(ByteOrder.nativeOrder());
        mIndexBuffer = ibb.asShortBuffer();
        mIndexBuffer.put(indices);
        mIndexBuffer.position(0);
        
        ByteBuffer cbb = ByteBuffer.allocateDirect(colors.length*4);
		cbb.order(ByteOrder.nativeOrder());
		mColorBuffer = cbb.asFloatBuffer();
		mColorBuffer.put(colors);
		mColorBuffer.position(0);
		
		ByteBuffer tbb = ByteBuffer.allocateDirect(VERTS * 2 * 4);
        tbb.order(ByteOrder.nativeOrder());
        mTexBuffer = tbb.asIntBuffer();
        mTexBuffer.put(0);
        mTexBuffer.put(0);
        mTexBuffer.put(one);
        mTexBuffer.put(0);
        mTexBuffer.put(one);
        mTexBuffer.put(one);
        mTexBuffer.put(0);
        mTexBuffer.put(one);
        mTexBuffer.position(0);
    }

    public void draw(GL10 gl) {
    	gl.glTranslatef(mTranslation[0], mTranslation[1], mTranslation[2]);
    	gl.glRotatef(mRotation[0], mRotation[1], mRotation[2], mRotation[3]);
    	
    	gl.glFrontFace(GL10.GL_CCW);
    	gl.glEnable(GL10.GL_TEXTURE_2D);
    	gl.glEnableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
    	gl.glEnableClientState(GL10.GL_COLOR_ARRAY);

    	gl.glBindTexture(GL10.GL_TEXTURE_2D, mType);
    	gl.glVertexPointer(3, GL10.GL_FIXED, 0, mVertexBuffer);
    	gl.glColorPointer(4, GL10.GL_FLOAT, 0, mColorBuffer);
    	gl.glTexCoordPointer(2, GL10.GL_FIXED, 0, mTexBuffer);
    	gl.glDrawElements(GL10.GL_TRIANGLES, 6, GL10.GL_UNSIGNED_SHORT, mIndexBuffer);

    	gl.glDisableClientState(GL10.GL_COLOR_ARRAY);
    	gl.glDisable(GL10.GL_TEXTURE_2D);

    	gl.glRotatef(-mRotation[0], mRotation[1], mRotation[2], mRotation[3]);
    	gl.glTranslatef(-mTranslation[0], -mTranslation[1], -mTranslation[2]);
    }
    
    private final static int VERTS = 4;

    private IntBuffer mVertexBuffer;
    private IntBuffer mTexBuffer;
    private ShortBuffer mIndexBuffer;
    private FloatBuffer   mColorBuffer;

    private int mType;
    private int mValue;
    private float[] mTranslation;
    private float[] mRotation;
}
