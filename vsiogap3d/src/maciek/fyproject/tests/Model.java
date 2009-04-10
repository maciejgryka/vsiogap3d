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

package maciek.fyproject.tests;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.IntBuffer;
import java.nio.ShortBuffer;

import javax.microedition.khronos.opengles.GL10;

import android.content.Context;

class Model
{
    public Model(Context context)
    {
        //int one = 0x10000;
        
    	int vertices[] = context.getResources().getIntArray(R.array.vertices);
    	int indicesInt[] = context.getResources().getIntArray(R.array.indices);
    	short indices[] = new short[indicesInt.length];
    	for (int i = 0; i < indicesInt.length; i++) {
    		indices[i] = (short)indicesInt[i];
    	}
        
//        int colors[] = {
//	        0,    0,    0,  one,
//	        one,    0,    0,  one,
//	        one,  one,    0,  one,
//	        0,  one,    0,  one,
//	        0,    0,  one,  one,
//	        one,    0,  one,  one,
//	        one,  one,  one,  one,
//	        0,  one,  one,  one,
//        };
        
//        int colors[] = {
//                one, one, 0, one
//        };

        // Buffers to be passed to gl*Pointer() functions
        // must be direct, i.e., they must be placed on the
        // native heap where the garbage collector cannot
        // move them.
        //
        // Buffers with multi-byte datatypes (e.g., short, int, float)
        // must have their byte order set to native order
        //
        // I've changed indices to type short in order to 
        // handle more than 128 indices.

        ByteBuffer vbb = ByteBuffer.allocateDirect(vertices.length*4);
        vbb.order(ByteOrder.nativeOrder());
        mVertexBuffer = vbb.asIntBuffer();
        mVertexBuffer.put(vertices);
        mVertexBuffer.position(0);

//        ByteBuffer cbb = ByteBuffer.allocateDirect(colors.length*4);
//        cbb.order(ByteOrder.nativeOrder());
//        mColorBuffer = cbb.asIntBuffer();
//        mColorBuffer.put(colors);
//        mColorBuffer.position(0);
        
        ByteBuffer ibb = ByteBuffer.allocateDirect(indices.length*4);
        ibb.order(ByteOrder.nativeOrder());
        mIndexBuffer = ibb.asShortBuffer();
        mIndexBuffer.put(indices);
        mIndexBuffer.position(0);
    }

    public void draw(GL10 gl)
    {
    	gl.glTranslatef(0.4f, -0.5f, 4.5f);
    	gl.glRotatef(90, 0, 0, 1);
    	gl.glTranslatef(0.0f, -3.0f, 0.0f);
    	
    	gl.glDisable(GL10.GL_CULL_FACE);
        
    	gl.glVertexPointer(3, GL10.GL_FIXED, 0, mVertexBuffer);
        //gl.glColorPointer(4, GL10.GL_FIXED, 0, mColorBuffer);
        
        gl.glDrawElements(GL10.GL_TRIANGLES, 212*6, GL10.GL_UNSIGNED_SHORT, mIndexBuffer);
        gl.glScalef(0.5f, 0.5f, 0.5f);
    }

    private IntBuffer   mVertexBuffer;
    //private IntBuffer   mColorBuffer;
    private ShortBuffer	mIndexBuffer;
}
