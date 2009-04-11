package maciek.fyproject.tests;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import android.content.Context;

public class FileDownloader {
	
	public FileDownloader (Context context) {
		mContext = context;
	}
	
	public boolean getFile() throws IOException {
		URL u;
        HttpURLConnection c;
        FileOutputStream f;
        
		try {
			String state = android.os.Environment.getExternalStorageState(); 
		    if(!state.equals(android.os.Environment.MEDIA_MOUNTED))  {
		        throw new IOException("SD Card must be mounted. It is " + state + ".");
		    }
		    
		    String xmlPath = mContext.getResources().getString(R.string.xmlPath);
			String xmlUrl = mContext.getResources().getString(R.string.xmlUrl);

		    // make sure the directory we plan to store the recording in exists
		    File directory = new File(xmlPath).getParentFile();
		    if (!directory.exists() && !directory.mkdirs()) {
		      throw new IOException("Path to file could not be created.");
		    }
		    
			u = new URL(xmlUrl);
			c = (HttpURLConnection)u.openConnection();
			c.setRequestMethod("GET");
	        c.setDoOutput(true);
	        c.connect();
			f = new FileOutputStream(new File(xmlPath));
	        InputStream in = c.getInputStream();
	
	        byte[] buffer = new byte[1024];
		    int len1 = 0;
		    while ( (len1 = in.read(buffer)) > 0 ) {
		        f.write(buffer,0, len1);
		    }
		    f.close();
		    return true;
		} catch (IOException e) {
			throw e;
		} catch (Exception e) {
			return false;
		}
	}
	
	Context mContext;
}