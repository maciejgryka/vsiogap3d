package com.maciejgryka.fyproject;

import java.util.ArrayList;

import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

/**
 * DBWorker does all the low-level database jobs like read/write
 * and query execution.
 *
 */
public class DBWorker {
	
	private static SQLiteDatabase mDb;
	
	public DBWorker(SQLiteDatabase db) {
		try {
			mDb = db;
			db.execSQL("DROP TABLE IF EXISTS Measurement");
			createMeasurementTable();
			createRoomTable();
			db.execSQL("DROP TABLE IF EXISTS Indicator");
			createIndicatorTable();
		} catch (SQLException ex) {
			// TODO write handle
			return;
		}
	}
	
	public boolean close()
	{
		try {
			mDb.close();
			return true;
		} catch (Exception e) {
			return false;
		}
	}
	
	private boolean createIndicatorTable() throws SQLException
	{
		mDb.execSQL(
				"CREATE TABLE IF NOT EXISTS " +
				"Indicator(" +
				//"indicatorId integer primary key, " +
				"sensorId text not null, " +
				"type text not null, " +
				"translationX float not null, " +
				"translationY float not null, " +
				"translationZ float not null, " +
				"rotationAngle float not null, " +
				"rotationX float not null, " +
				"rotationY float not null, " +
				"rotationZ float not null, " +
				"CONSTRAINT pk PRIMARY KEY (sensorId, type));"
		);
		return true;
	}
	
//	public void insertRecord(XmlDataObject dataObject, String type)
//	{
//		if (type.equals("measurement")) {
////			insertMeasurement((MeasurementData)dataObject);
//		} else if (type.equals("indicator")) {
//			insertIndicator((IndicatorData)dataObject);
//		}
//	}
	
	private boolean createMeasurementTable() throws SQLException
	{
		mDb.execSQL(
				"CREATE TABLE IF NOT EXISTS " +
				"Measurement(" +
				"sensorId integer not null, " +
				"temperature integer, " +
				"light integer, " +
				"movement integer, " +
				"time text not null," +
				"PRIMARY KEY (sensorId, time));"
		);

		return true;
	}
	
//	public void insertMeasurement(MeasurementData measurement)
//	{
//		try {
//			// TODO: check if there is a newer measurement in the table already - if so, don't write this one
//			boolean isNewest = isNewest(measurement); 
//			if (!isNewest) return;
//			
//			mDb.execSQL("DELETE FROM Measurement WHERE sensorId = '" + measurement.getSensorId() + "';");
//			String q = "INSERT INTO Measurement (sensorId, temperature, light, movement, time) VALUES ('" +
//			measurement.getSensorId() + "', '" +
//			measurement.getTemperature() + "', '" +
//			measurement.getLight() + "', '" +
//			measurement.getMovement() + "', '" +
//			measurement.getTime() + "');";
//			
//			mDb.execSQL(q);
//		} catch (SQLException ex) {
//			// TODO: write handler
//			return;
//		}
//	}
	
	private boolean createRoomTable() throws SQLException
	{
		mDb.execSQL(
				"CREATE TABLE IF NOT EXISTS " +
				"Room(" +
				"sensorId text primary key, " +
				"name text not null, " +
				"floor text);"
		);
		return true;
	}
	
	/**
	 * Returns float[] with indicator location data
	 * @param sensorId room identifier
	 * @param type of measurements (0 for temperature, 1 for light, 2 for movement)
	 */
	public float[] getIndicatorLocation(int sensorId, String type)
	{
		try {
			String columns[] = {
					"translationX", "translationY", "translationZ",
					"rotationAngle", "rotationX", "rotationY", "rotationZ"
			};
			//String selectionArgs[] = {sensorId, type};
			Cursor cur = mDb.query("Indicator", columns, "sensorId = '" + sensorId + "' AND type = '" + type + "'", null, null, null, null);
			
			float location[] = new float[7];
			cur.moveToFirst();
			location[0] = cur.getFloat(0);
			location[1] = cur.getFloat(1);
			location[2] = cur.getFloat(2);
			location[3] = cur.getFloat(3);
			location[4] = cur.getFloat(4);
			location[5] = cur.getFloat(5);
			location[6] = cur.getFloat(6);
			
			cur.close();
			return location;
		} catch (Exception e) {
			// TODO write handle
			return null;
		}
	}
	
//	private int insertIndicator(
//			String sensorId, String type, 
//			float translationX, float translationY, float translationZ,
//			float rotationAngle, float rotationX, float rotationY, float rotationZ)
//	{
//		ContentValues cv = new ContentValues();
//		cv.put("sensorId", sensorId);
//		cv.put("type", type);
//		cv.put("translationX", translationX);
//		cv.put("translationY", translationY);
//		cv.put("translationZ", translationZ);
//		cv.put("rotationAngle", rotationAngle);
//		cv.put("rotationX", rotationX);
//		cv.put("rotationY", rotationY);
//		cv.put("rotationZ", rotationZ);
//		int result = (int)mDb.insert("Indicator", "", cv); 
//		return result;
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
		try {
			ArrayList<Measurement> measurements = new ArrayList<Measurement>();
			String q = "SELECT sensorId, temperature, light, movement, time FROM Measurement";
			if (condition != null) {
				q += condition;
			}
			Cursor cur = query(q);
			
			for (int row = 0; row < cur.getCount(); row++) {
				cur.moveToNext();
				Measurement measurement = new Measurement();
				measurement.setSensorId(cur.getInt(0));
				measurement.setTemperature(cur.getInt(1));
				measurement.setLight(cur.getInt(2));
				measurement.setMovement(cur.getInt(3));
				measurement.setTime(cur.getString(4));
				
				measurements.add(measurement);
			}
			cur.close();
			return measurements;
		} catch (Exception e) {
			// TODO write handle
			return null;
		}
	}
	
	public void insertIndicator(IndicatorData indicator)
	{
		try {
			String q = "INSERT INTO Indicator " +
					"(sensorId, type, translationX, translationY, translationZ, rotationAngle, rotationX, rotationY, rotationZ) " +
					"VALUES ('" +
					indicator.getSensorId() + "', '" +
					indicator.getType() + "', '" +
					indicator.getTranslationX() + "', '" +
					indicator.getTranslationY() + "', '" +
					indicator.getTranslationZ() + "', '" +
					indicator.getRotationAngle() + "', '" +
					indicator.getRotationX() + "', '" +
					indicator.getRotationY() + "', '" +
					indicator.getRotationZ() + "');";
			mDb.execSQL(q);
		} catch (SQLException e) {
			// TODO: write handler
			return;
		}
	}
	
	public void insertMeasurement(Measurement measurement)
	{
		// If this is not the most recent record, ignore
		boolean isNewest = isNewest(measurement);
		if (!isNewest) return;
		
		// If it is the most recent record, delete all the older ones from the database
		mDb.execSQL("DELETE FROM Measurement WHERE sensorId = '" + measurement.getSensorId() + "';");
		
		String q = "INSERT INTO Measurement (sensorId, temperature, light, movement, time) VALUES ('" +
		measurement.getSensorId() + "', '" +
		measurement.getTemperature() + "', '" +
		measurement.getLight() + "', '" +
		measurement.getMovement() + "', '" +
		measurement.getTime() + "');";
		
		mDb.execSQL(q);
		return;
	}
	
	private boolean isNewest(Measurement measurement)
	{
		String mQuery = "SELECT sensorID, time FROM Measurement WHERE sensorId = '" + measurement.getSensorId() + "' AND time > datetime('" + measurement.getTime() + "');";
		Cursor cur = mDb.rawQuery(mQuery, null);
		if (cur.getCount() != 0) {
			cur.close();
			return false;
		} else {
			cur.close();
			return true;
		}
	}
	
	public Cursor query(String query)
	{
		return mDb.rawQuery(query, null);
	}
}
