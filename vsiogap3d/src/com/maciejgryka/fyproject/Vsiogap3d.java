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

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.widget.TextView;

public class Vsiogap3d extends Activity {
	
	private static final int DIALOG_ERROR = 1;
	private static final int DIALOG_LOADING = 2;
	
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        mGLSurfaceView = new GLSurfaceView(this);
        mGLSurfaceView.setRenderer(new ModelRenderer(this));
        setContentView(mGLSurfaceView);
    }
	
	/* (non-Javadoc)
	 * @see android.app.Activity#onCreateOptionsMenu(android.view.Menu)
	 */
//	@Override
//	public boolean onCreateOptionsMenu(Menu menu) {
//		menu.add(R.string.menuRefreshData);
//		if (mGLSurfaceView.getZoom() == -1) {
//			menu.add(R.string.menuZoomOut);
//			menu.add(R.string.preferences);
//			return true;
//		} else if (mGLSurfaceView.getZoom() == 1) {
//			menu.add(R.string.menuZoomIn);
//			menu.add(R.string.preferences);
//			return true;
//		}
//		return false;
//	}

	/* (non-Javadoc)
	 * @see android.app.Activity#onPrepareOptionsMenu(android.view.Menu)
	 */
	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		// TODO: this is slow - remove one item instead of clearing the whole menu
		menu.clear();
		menu.add(R.string.menuRefreshData);
		
		if (mGLSurfaceView.getZoom() == -1) {
			menu.add(R.string.menuZoomOut);
			menu.add(R.string.preferences);
			return true;
		} else if (mGLSurfaceView.getZoom() == 1) {
			menu.add(R.string.menuZoomIn);
			menu.add(R.string.preferences);
			return true;
		}
		
		return false;
	}

	/* (non-Javadoc)
	 * @see android.app.Activity#onOptionsItemSelected(android.view.MenuItem)
	 */
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		try {
			// TODO: this is slow - would be faster to use item ids not titles
			String title = item.getTitle().toString();
			if (title.equals("Zoom In")) {
				mGLSurfaceView.setZoom(-1);
				return true;
			} else if (title.equals("Zoom Out")) {
				mGLSurfaceView.setZoom(1);
				return true;
			} else if (title.equals("Refresh Data")) {
				mGLSurfaceView = new GLSurfaceView(this);
				ModelRenderer modelRenderer = new ModelRenderer(this);
		        mGLSurfaceView.setRenderer(modelRenderer);
		        setContentView(mGLSurfaceView);
				return true;
			} else if (title.equals("Preferences")) {
				startActivity(new Intent(this, Preferences.class));
				return true;
			}
			return false;
		} catch (Exception e) {
			currentError = e.getMessage();
			showDialog(DIALOG_ERROR);
			return false;
		} 
//		catch (Exception e) {
//			return false;
//		}
	}
		
	/* (non-Javadoc)
	 * @see android.app.Activity#onCreateDialog(int)
	 */
	@Override
	protected Dialog onCreateDialog(int id) {
		switch (id) {
	        case DIALOG_ERROR:
	            return new AlertDialog.Builder(this)
	                .setTitle("Error")
	                .setPositiveButton("OK", null)
	                .setMessage(currentError)
	                .create();
            case DIALOG_LOADING:
            	return new AlertDialog.Builder(this)
                .setTitle("Loading...")
                //.setMessage("Loading...")
                .create();
		}
		return super.onCreateDialog(id);
	}

	@Override
    protected void onPause() {
        // Ideally a game should implement onResume() and onPause()
        // to take appropriate action when the activity looses focus
        super.onPause();
        mGLSurfaceView.onPause();
    }

	@Override
    protected void onResume() {
        // Ideally a game should implement onResume() and onPause()
        // to take appropriate action when the activity looses focus
        super.onResume();
        mGLSurfaceView.onResume();
    }
	
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		return mGLSurfaceView.onTouchEvent(event);
	}
	
	@Override
	public boolean onTrackballEvent(MotionEvent event) {
		return mGLSurfaceView.onTrackballEvent(event);
	}

	private String currentError = "";
	private GLSurfaceView mGLSurfaceView;
}