package uni.projecte.dataLayer.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.LinkedList;
import java.util.List;

import uni.projecte.R;

import android.content.Context;

public class StringUtils {
	
	public static String[] removeElements(String[] input, String deleteMe) {
	    
		List<String> result = new LinkedList<String>();

	    for(String item : input){
	        
	    	if(!deleteMe.equals(item)) result.add(item);
	    	
	    }

	    return result.toArray(new String[0]);
	}
	
    /*
     * To convert the InputStream to String we use the BufferedReader.readLine()
     * method. We iterate until the BufferedReader return null which means
     * there's no more data to read. Each line will appended to a StringBuilder
     * and returned as String.
     */
	
	   public static String convertStreamToString(InputStream is) {

	        BufferedReader reader;
			StringBuilder sb = new StringBuilder();

			try {
				reader = new BufferedReader(new InputStreamReader(is,"UTF-8"));
			
	 	        String line = null;
		
	            while ((line = reader.readLine()) != null) {
	                sb.append(line + "\n");
	            }
	            
	        } 
			catch (IOException e) {
				
	            e.printStackTrace();
	            
	        }
	        finally {
	        	
	            try {
	            	
	                is.close();
	                
	            } catch (IOException e) {
	            	
	                e.printStackTrace();
	                
	            }
	        }
	        
	        return sb.toString();
	    }


	public static boolean emptyValue(String fieldValue) {

		if(fieldValue.equals("") || fieldValue.equals("      ")) return true;
		else return false;
		
	}


	public static String getBooleanValue(Context c, String fieldValue) {

		String newValue="";
		
			if(fieldValue.equals("true")) newValue=c.getResources().getString(R.string.yes);
			else newValue=c.getResources().getString(R.string.no);
			
		return newValue;
	}


}
