package uni.projecte.dataLayer.CitationManager.Tab;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import android.content.Context;

public class TabReader {
	
	private BufferedReader buffreader;
	
	
	public TabReader(Context c, String url){
		
	
		  try {
		    // open the file for reading
		    InputStream instream = c.openFileInput(url);

			    if (instream != null) {

			      InputStreamReader inputreader = new InputStreamReader(instream);
			      buffreader = new BufferedReader(inputreader);
			 
			    }

				instream.close();
				
			} catch (IOException e) {

				e.printStackTrace();
				
			}

		
	}
	
	public String[] readRegister(){
		
	    
		String line="";
		
		try {
			
			line = buffreader.readLine();
			
		} catch (IOException e) {

			e.printStackTrace();
			
		}
		
		return line.split("\t");
			  
		
	}
	
	

}
