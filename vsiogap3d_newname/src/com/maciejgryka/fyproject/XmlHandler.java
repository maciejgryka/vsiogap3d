package com.maciejgryka.fyproject;

import java.io.IOException;
import java.net.URL;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;

import android.util.Log;

public class XmlHandler extends DefaultHandler {
	
	DBWorker mDbWorker;
	Measurement currentMeasurement;
	String currentTag = "";
 
    public void startElement(String uri, String name, String qName, Attributes atts) {
    	currentTag = name.trim();
        if (name.trim().equals("measurement")) {
        	currentMeasurement.clear();
        }
    }
 
    public void endElement(String uri, String name, String qName) throws SAXException {
        if (name.trim().equals("measurement")) {
        	mDbWorker.insertMeasurement(currentMeasurement);
        }
    }
 
    public void characters(char ch[], int start, int length) {
    	String characters = (new String(ch).substring(start, start + length).trim());
    	
    	if (characters.equals("\n") || characters.equals("")) return;
    	
    	if (currentTag.equals("sensorId")) {
			currentMeasurement.setSensorId(Integer.valueOf(characters));
		} else if (currentTag.equals("temperature")) {
			currentMeasurement.setTemperature(Integer.valueOf(characters));
		} else if (currentTag.equals("light")) {
			currentMeasurement.setLight(Integer.valueOf(characters));
		} else if (currentTag.equals("movement")) {
			currentMeasurement.setMovement(Integer.valueOf(characters));
		} else if (currentTag.equals("time")) {
			currentMeasurement.setTime(characters);
		}
    }
 
    public void getMeasurements(URL url, DBWorker dbw) {
        try {
        	mDbWorker = dbw;
        	currentMeasurement = new Measurement();
 
            SAXParserFactory spf = SAXParserFactory.newInstance();
            SAXParser sp = spf.newSAXParser();
            XMLReader xr = sp.getXMLReader();
            xr.setContentHandler(this);
            xr.parse(new InputSource(url.openStream()));
           
        } catch (IOException e) {
            Log.e("vsiogap3d - XmlHandler", e.toString());
        } catch (SAXException e) {
            Log.e("vsiogap3d - XmlHandler", e.toString());
        } catch (ParserConfigurationException e) {
            Log.e("vsiogap3d - XmlHandler", e.toString());
        }
    }
}