package com.maciejgryka.fyproject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

public class LoadingScreen extends Activity {
	
	Context mContext;
	
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TextView tv = new TextView(this);
        tv.setText("Loading...");
        mContext = this;
        setContentView(tv);

        class SleepClass implements Runnable {
            public void run() {
                Log.i("viosgap3d", "loading application");
                Intent myIntent = new Intent(mContext, Vsiogap3d.class);
                startActivity(myIntent);
            }
        }

        Thread t1 = new Thread(new SleepClass());
        t1.start();
    }
}