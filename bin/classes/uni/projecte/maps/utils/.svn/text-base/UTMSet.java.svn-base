package uni.projecte.maps.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;

import android.content.Context;
import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapView;

public class UTMSet {
	
	private ArrayList<UTMSquare> utmList;
	private HashMap<String, UTMSquare> utmHashList;
	private Context baseContext;
	
	
	public UTMSet(Context baseContext){
		
		this.baseContext=baseContext;
		
		
	}
	
	public UTMSquare getUTMCoords(String utm){
		
		UTMSquare utmS=utmHashList.get(utm);
	
		return utmS;
		
		
	}
	
	
	public void loadUTMIberianGrid(MapView mapView){
		
		InputStream is;
		utmList=new ArrayList<UTMSquare>();
		utmHashList=new HashMap<String, UTMSquare>();

		try {
			
			is = baseContext.getResources().getAssets().open("utmIberic.json");

			 BufferedReader reader;
			
			reader = new BufferedReader(new InputStreamReader(is,"UTF-8"));
		
 	        String line = null;
 	        String utm;
	
            while ((line = reader.readLine()) != null) {
               
            	utm=line;
            			            	
            	line = reader.readLine();
            	
            	
            	if(line.equals("")){
            		
	            	line = reader.readLine();
            		
            	}
            	
            	String [] elements=line.split(" ");
            	
            	if(elements.length>1){
            	
	            	String [] subElement=elements[0].split(",");
	            		            	
	            	double lat=Double.valueOf(subElement[1]);
	                double longi=Double.valueOf(subElement[0]);
	            	
	    			GeoPoint a=new GeoPoint((int) (lat * 1E6), (int) (longi * 1E6));

	    			
	    			subElement=elements[1].split(",");
	            	
	    		  	lat=Double.valueOf(subElement[1]);
	                longi=Double.valueOf(subElement[0]);
	            	
	    			GeoPoint b=new GeoPoint((int) (lat * 1E6), (int) (longi * 1E6));
	   		  			
	    			subElement=elements[2].split(",");

	    		  	lat=Double.valueOf(subElement[1]);
	                longi=Double.valueOf(subElement[0]);
	            	
	    			GeoPoint c=new GeoPoint((int) (lat * 1E6), (int) (longi * 1E6));
	   			
	    			subElement=elements[3].split(",");

	    		  	lat=Double.valueOf(subElement[1]);
	                longi=Double.valueOf(subElement[0]);
	            	
	    			GeoPoint d=new GeoPoint((int) (lat * 1E6), (int) (longi * 1E6));
	    			
	    			UTMSquare utmS= new UTMSquare(utm, a, b, c, d);
	    			
	    			utmList.add(utmS);
	    			utmHashList.put(utm, utmS);
	    			
    			
            	}
            	
            	
            }
            


            
        } catch (IOException e) {
            e.printStackTrace();
        }

		
	}
	

}



