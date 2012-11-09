package uni.projecte.hardware.gps;

import android.location.GpsSatellite;
import android.location.GpsStatus;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;


public class MainLocationListener implements LocationListener {
	
	private LocationManager mLocationManager;
	private GpsStatus gpsStatus = null;
	
	private double latitude;
	private double longitude;
	private double elevation;
	
	private Handler handler;
	
	private Location lastKnownLocation;
	
	private double geoidCorrection=0.0;
	
	
	public MainLocationListener(LocationManager locationManager,Handler hand,double geoidCorrection){
		
		this.mLocationManager=locationManager;
		this.handler=hand;
		this.geoidCorrection=geoidCorrection;
		
	}
	
	
    public void onLocationChanged(Location loc) {
       
    	if (loc != null) {

        	gpsStatus = mLocationManager.getGpsStatus(gpsStatus);

			   
			   for (GpsSatellite s: gpsStatus.getSatellites()){
				
				    elevation = s.getElevation() - 90.0f;
				    				   
				    if (elevation > 90 || s.getAzimuth() < 0 || s.getPrn() < 0){
				            continue;
				    }
			    
			   } 
              
			 
			updateLocation(loc);  
			   
            handler.sendEmptyMessage(0);
            
        }
    }

    private void updateLocation(Location loc) {

    	lastKnownLocation=loc;
    	
    	
    	if (lastKnownLocation!=null) {
    		
    		latitude=lastKnownLocation.getLatitude();
    		longitude=lastKnownLocation.getLongitude();
    		
    		elevation=lastKnownLocation.getAltitude()-geoidCorrection;
    		
    	}
    	
	}


	public void onProviderDisabled(String provider) {
    }

    public void onProviderEnabled(String provider) {
    }


	public void onStatusChanged(String provider, int status, Bundle extras) {
		
	}


	public double getLatitude() {
		return latitude;
	}


	public double getLongitude() {
		return longitude;
	}


	public double getElevation() {
		return elevation;
	}


	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}


	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}


	public void setElevation(double elevation) {
		this.elevation = elevation;
	}
	
	
} 

