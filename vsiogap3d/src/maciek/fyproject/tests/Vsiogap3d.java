package maciek.fyproject.tests;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;

public class Vsiogap3d extends Activity {
	
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
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		if (mGLSurfaceView.getZoom() == -1) {
			menu.add(R.string.menuZoomOut);
			return true;
		} else if (mGLSurfaceView.getZoom() == 1) {
			menu.add(R.string.menuZoomIn);
			return true;
		}
		return false;
	}

	/* (non-Javadoc)
	 * @see android.app.Activity#onPrepareOptionsMenu(android.view.Menu)
	 */
	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		if (mGLSurfaceView.getZoom() == -1) {
			menu.clear();
			menu.add(R.string.menuZoomOut);
			return true;
		} else if (mGLSurfaceView.getZoom() == 1) {
			menu.clear();
			menu.add(R.string.menuZoomIn);
			return true;
		}
		return false;
	}

	/* (non-Javadoc)
	 * @see android.app.Activity#onOptionsItemSelected(android.view.MenuItem)
	 */
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (mGLSurfaceView.getZoom() == -1) {
			mGLSurfaceView.setZoom(1);
			return true;
		} else if (mGLSurfaceView.getZoom() == 1) {
			mGLSurfaceView.setZoom(-1);
			return true;
		}
		return false;
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

	private GLSurfaceView mGLSurfaceView;
}