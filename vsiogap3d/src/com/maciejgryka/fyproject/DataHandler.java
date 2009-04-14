package com.maciejgryka.fyproject;

import java.net.URL;
import java.util.ArrayList;

import android.content.Context;
import android.content.res.XmlResourceParser;

/**
 * DataHandler takes care of all the data-related tasks.
 * It can read/write XML files and read/write to the database accordingly.
 *
 */
public class DataHandler {

	/**
	 * Constructor
	 * @param context to get access to XML resource and database
	 */
	public DataHandler(Context context)
	{
		mContext = context;
		mDbWorker = new DBWorker(context.openOrCreateDatabase("vsiogapDB", 0, null));
	}
	
	public void readMeasurementData(int resourceId)
	{
		try {
			XmlResourceParser xrp = mContext.getResources().getXml(resourceId);
			String currentTag = "";
			
			int eventType = xrp.getEventType();
			Measurement currentMeasurement = new Measurement();
			
			while (eventType != XmlResourceParser.END_DOCUMENT) {
				if (eventType == XmlResourceParser.START_TAG) {
					currentTag = xrp.getName();
					if (currentTag.equals("measurement")) {
						currentMeasurement.clear();
					}
				} else if (eventType == XmlResourceParser.TEXT) {
					if (currentTag.equals("sensorId")) {
						currentMeasurement.setSensorId(Integer.valueOf(xrp.getText()));
					} else if (currentTag.equals("temperature")) {
						currentMeasurement.setTemperature(Integer.valueOf(xrp.getText()));
					} else if (currentTag.equals("light")) {
						currentMeasurement.setLight(Integer.valueOf(xrp.getText()));
					} else if (currentTag.equals("movement")) {
						currentMeasurement.setMovement(Integer.valueOf(xrp.getText()));
					} else if (currentTag.equals("time")) {
						currentMeasurement.setTime(xrp.getText());
					}
				} else if (eventType == XmlResourceParser.END_TAG) {
					if (xrp.getName().equals("measurement")) {
						mDbWorker.insertMeasurement(currentMeasurement);
					}
				}
				eventType = xrp.next();
			}
		} catch (Exception e) {
			return;
		}
	}
	
	public void readMeasurementData(String stringUrl)
	{
		try {
			URL url = new URL(stringUrl);
			XmlHandler xh = new XmlHandler();
			xh.getMeasurements(url, mDbWorker);
		} catch (Exception e) {
			// TODO: handle exception
//			String s = "";
			return;
		}
	}
	
	public void readIndicatorData(int resourceId)
	{
		IndicatorData currentIndicator = new IndicatorData();
		readXml(resourceId, currentIndicator, "indicator");
	}
	
	// TODO DataHandler should know what datatype it's reading (string, date or int)
	// and decide which measurement.set function to call. We need nested if statement here
	private void readXml(int resourceId, XmlDataObject dataObject, String type)
	{
		//Measurement currentMeasurement = new Measurement();
		try {
			XmlResourceParser xrp = mContext.getResources().getXml(resourceId);
			String currentTag = "";
			
			int eventType = xrp.getEventType();
			while (eventType != XmlResourceParser.END_DOCUMENT) {
				if (eventType == XmlResourceParser.START_TAG) {
					currentTag = xrp.getName();
					if (currentTag.equals(type)) {
						dataObject.clear();
					}
				} else if (eventType == XmlResourceParser.TEXT) {
					// no need to check what the currentTag is - if it's invalid nothing will be written
					dataObject.setAttribute(currentTag, xrp.getText());
				} else if (eventType == XmlResourceParser.END_TAG) {
					if (xrp.getName().equals(type)) {
						//dbw.insertMeasurement(currentMeasurement);
						mDbWorker.insertRecord(dataObject, type);
					}
				}
				eventType = xrp.next();
			}
		} catch (Exception e) {
			return;
		}
	}
	
//	public Cursor getMeasurements(String condition)
//	{
//		return dbw.getMeasurements(condition);
//	}

	/**
	 * Gets all columns of the matching records from the Measurement table 
	 * (sensorId, temperature, light, movement, time).
	 * If condition is null, every record is returned.
	 * @param condition - SQL condition statement (including "WHERE" clause); enter null for no condition
	 * @return ArrayList of all the Measurements
	 */
	public ArrayList<Measurement> getMeasurementList(String condition)
	{
		return mDbWorker.getMeasurementList(condition);
	}
	
	/**
	 * Returns float[] with indicator location data
	 * @param sensorId room identifier
	 * @param type of measurements (0 for temperature, 1 for light, 2 for movement)
	 */
	public float[] getIndicatorLocation(int sensorId, String type)
	{
		return mDbWorker.getIndicatorLocation(sensorId, type);
	}
	
	public float[] getTranslation(String sensorId, int type) {
		float translation[] = {-2.0f, 0.0f, 1.19f - change};
		change += 0.5f;
		return translation;
		// TODO change to retrieve location from the database
	}
	
	public float[] getRotation(String sensorId, int type) {
		float rotation[] = {0.0f, 0.0f, 1.0f, 0.0f};
		return rotation;
		// TODO change to retrieve location from the database
	}
	
	public boolean close()
	{
		try {
			mContext.deleteDatabase("vsiogapDB");
			mDbWorker.close();
			return true;
		} catch (Exception e) {
			return false;
		}
	}
	
	private Context mContext;
	private static DBWorker mDbWorker;
	
	private float change = 0.0f;
}
