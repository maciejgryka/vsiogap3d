package maciek.fyproject.tests;

import java.util.ArrayList;

import javax.microedition.khronos.opengles.GL10;

import android.content.Context;

public class Indicators {
	public Indicators()
	{
		mIndicatorIcons = new ArrayList<IndicatorIcon>();
	}
	
	public void populateIndicatorIcons(Context context, int textureNames[])
	{
		if (!mIndicatorIcons.isEmpty()) return;
		
		DataHandler dh = new DataHandler(context);
		mTextureNames = textureNames;
		
		dh.readMeasurementData(R.xml.sensor);
		dh.readIndicatorData(R.xml.indicators);
		
		// TODO: would be much faster to check if XML exists (and is valid) before writing to the database!
		ArrayList<MeasurementData> measurementList = new ArrayList<MeasurementData>();
		measurementList = dh.getMeasurement(null);
		for (MeasurementData measurement : measurementList) {
			
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
					addIndicatorIcon(mTextureNames[0], translation, rotation);
				}
			} if (measurement.getLight() != 0) {
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
					addIndicatorIcon(mTextureNames[1], translation, rotation);
				}
			} if (measurement.getMovement() != 0) {
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
					addIndicatorIcon(mTextureNames[2], translation, rotation);
				}
			}
		}
	}
	
	public void addIndicatorIcon(int type, float translation[], float rotation[])
	{
		IndicatorIcon indIcon = new IndicatorIcon(type, translation, rotation);
		mIndicatorIcons.add(indIcon);
	}

	public void draw(GL10 gl)
	{
		for (IndicatorIcon indIcon: mIndicatorIcons) {
			indIcon.draw(gl);
		}
	}
	
	private ArrayList<IndicatorIcon> mIndicatorIcons;
	private int[] mTextureNames;
}
