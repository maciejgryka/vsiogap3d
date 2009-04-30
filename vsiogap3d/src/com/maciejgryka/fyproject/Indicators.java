package com.maciejgryka.fyproject;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;

import javax.microedition.khronos.opengles.GL10;

import android.content.Context;
import android.content.SharedPreferences;

public class Indicators {
	public Indicators()
	{
		mIndicatorIcons = new ArrayList<IndicatorIcon>();
//		indicatorDataList = new ArrayList<IndicatorData>();
	}
	
	public void populateIndicatorIcons(Context context, int textureNames[])
	{
		if (!mIndicatorIcons.isEmpty()) return;
		
		DataHandler dh = new DataHandler(context);
		mTextureNames = textureNames;
		
		SharedPreferences preferences = context.getSharedPreferences("com.maciejgryka.fyproject_preferences", Context.MODE_WORLD_WRITEABLE);
		String url = preferences.getString("dataSource_preference", null);
		
//		dh.readMeasurementData(context.getResources().getString(R.string.xmlUrl));
		dh.readMeasurementData(url);
		
		
		// TODO: should be much faster to check if XML exists (and is valid) before writing to the database!
		ArrayList<Measurement> measurementList = new ArrayList<Measurement>();
		measurementList = dh.getMeasurementList(null);
		for (Measurement measurement : measurementList) {
			
			int sensorId = measurement.getSensorId();
			/* 
			 * For each Measurement add corresponding indicators.
			 * Texture names (indices of mTextureNames) are as follows:
			 * 0 - temperature
			 * 1 - light
			 * 2 - occupancy  
			 */
			if (measurement.getTemperature() != 0) {
				float location[];
				float translation[] = new float[3];
				float rotation[] = new float[4];
				location = dh.getIndicatorLocation(sensorId, "0");
				if (location != null) {
					translation[0] = location[0];
					translation[1] = location[1];
					translation[2] = location[2];
					rotation[0] = location[3];
					rotation[1] = location[4];
					rotation[2] = location[5];
					rotation[3] = location[6];
					addIndicatorIcon(mTextureNames[0], measurement.getTemperature(), translation, rotation);
				}
			} 
			if (measurement.getLight() != 0) {
				float location[];
				float translation[] = new float[3];
				float rotation[] = new float[4];
				location = dh.getIndicatorLocation(sensorId, "1");
				if (location != null) {
					translation[0] = location[0];
					translation[1] = location[1];
					translation[2] = location[2];
					rotation[0] = location[3];
					rotation[1] = location[4];
					rotation[2] = location[5];
					rotation[3] = location[6];
					addIndicatorIcon(mTextureNames[1], measurement.getLight(), translation, rotation);
				}
			} 
			if (measurement.getMovement() != 0) {
				float location[];
				float translation[] = new float[3];
				float rotation[] = new float[4];
				location = dh.getIndicatorLocation(sensorId, "2");
				if (location != null) {
					translation[0] = location[0];
					translation[1] = location[1];
					translation[2] = location[2];
					rotation[0] = location[3];
					rotation[1] = location[4];
					rotation[2] = location[5];
					rotation[3] = location[6];
					addIndicatorIcon(mTextureNames[2], measurement.getMovement(), translation, rotation);
				}
			}
		}
	}	
	
	public void addIndicatorIcon(int type, int value, float translation[], float rotation[])
	{
		IndicatorIcon indIcon = new IndicatorIcon(type, value, translation, rotation);
		mIndicatorIcons.add(indIcon);
	}

	public void draw(GL10 gl)
	{
		for (IndicatorIcon indIcon: mIndicatorIcons) {
			indIcon.draw(gl);
		}
	}
	
	public boolean isEmpty()
	{
		if (mIndicatorIcons.isEmpty()) {
			return true;
		} else {
			return false;
		}
	}
	
	private static ArrayList<IndicatorIcon> mIndicatorIcons;
	public static int[] mTextureNames;
}
