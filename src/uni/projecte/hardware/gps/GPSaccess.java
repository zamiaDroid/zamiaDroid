/*    	This file is part of ZamiaDroid.
*
*	ZamiaDroid is free software: you can redistribute it and/or modify
*	it under the terms of the GNU General Public License as published by
*	the Free Software Foundation, either version 3 of the License, or
*	(at your option) any later version.
*
*    	ZamiaDroid is distributed in the hope that it will be useful,
*    	but WITHOUT ANY WARRANTY; without even the implied warranty of
*    	MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
*    	GNU General Public License for more details.
*
*    	You should have received a copy of the GNU General Public License
*    	along with ZamiaDroid.  If not, see <http://www.gnu.org/licenses/>.
*/

package uni.projecte.hardware.gps;

import uni.projecte.R;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.GpsSatellite;
import android.location.GpsStatus;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.widget.Toast;

public class GPSaccess extends Activity{
	
	private ProgressDialog pd;
    
	private LocationManager mLocationManager;
	private GpsStatus gpsStatus = null;


	private MyLocationListener mLocationListener;
	
	private Thread t;
	
	double lat;
	double lon;
	float elevation;
	
	Context context;	
	Location currentLocation = null;
	
	
	 
    @Override
	public void onCreate(Bundle icicle) {
     super.onCreate(icicle);
     
     	setContentView(R.layout.gps);
     
    }

	
    @Override
	public void onResume(){
    	
    	super.onResume();
    	
       	try {
       			
       		writeSignalGPS();
       			
       			
		} catch (Exception e) {
			
			e.printStackTrace();
			
		}
    	
    	
    	
    }
    
    private void setCurrentLocation(Location loc) {
    	currentLocation = loc;
    }
    
	public double getLat() {
		return lat;
	}


	public double getLon() {
		return lon;
	}

    
    public boolean writeSignalGPS() throws Exception {
    	
    	context=this;
        	
    	DialogInterface.OnCancelListener dialogCancel = new DialogInterface.OnCancelListener() {

            public void onCancel(DialogInterface dialog) {
                Toast.makeText(context, 
                		context.getResources().getString(R.string.gps_signal_not_found), 
                        Toast.LENGTH_LONG).show();
                handler.sendEmptyMessage(0);
            }
          
        };
    	
	
		
		mLocationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE); 
		
		if (mLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
			
			

			
			pd = ProgressDialog.show(context, context.getResources().getString(R.string.search), 
					
			context.getResources().getString(R.string.search_signal_gps), true, true, dialogCancel);
			
			
			t=new Thread() {
	      	  
	            
				@Override
				public void run() {
	
					Looper.prepare();
						
					mLocationListener = new MyLocationListener();
					//MyStatusListener mGpsStatusListner = new MyStatusListener();
						
					mLocationManager.requestLocationUpdates(
				                LocationManager.GPS_PROVIDER, 0, 0, mLocationListener);
					
                   // mLocationManager.addGpsStatusListener(mGpsStatusListner);

	
					Looper.loop(); 
					Looper.myLooper().quit(); 
 
				}

				
	      };
			
	      
	      t.start();
      
	}
		
		else{

			 
				AlertDialog.Builder builder = new AlertDialog.Builder(this);
		    	
		    	
		    	builder.setMessage(R.string.enableGPSQuestion)
		    	       .setCancelable(false)
		    	       .setPositiveButton(R.string.enableGPS, new DialogInterface.OnClickListener() {
		    	           public void onClick(DialogInterface dialog, int id) {
		    
		    	        	   Intent callGPSSettingIntent = new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
		    	        	   startActivity(callGPSSettingIntent);
		    	        		        	   
		    	           }

						
		    	       })
		    	       .setNegativeButton(R.string.noGPS, new DialogInterface.OnClickListener() {
		    	           public void onClick(DialogInterface dialog, int id) {
		    	                
		    	        	   finish();
		    	                
		    	           }
		    	       });
		    	
		    	AlertDialog alert = builder.create();
		    	alert.show();

		}
      
																										
		return true;

    }
    
    /*private void createGpsDisabledAlert(){  
    	
    	
    	   AlertDialog.Builder builder = new AlertDialog.Builder(this);  
    	    builder.setMessage("Your GPS is disabled! Would you like to enable it?")  
    	         .setCancelable(false)  
    	        .setPositiveButton("Enable GPS",  
    	              new DialogInterface.OnClickListener(){  
    	              public void onClick(DialogInterface dialog, int id){  
    	                  try {
							writeSignalGPS();
						} catch (Exception e) {
							// 
							e.printStackTrace();
						}  
    	             }  
    	        });  
    	       builder.setNegativeButton("Do nothing",  
    	             new DialogInterface.OnClickListener(){  
    	             public void onClick(DialogInterface dialog, int id){  
    	                 dialog.cancel();  
    	            }  
    	        });  
    	   AlertDialog alert = builder.create();  
    	   alert.show();  
    	   }  */


    
	private Handler handler = new Handler() {
		
		@Override
		public void handleMessage(Message msg) {
			

			pd.dismiss();
			mLocationManager.removeUpdates(mLocationListener);
			
	    	if (currentLocation!=null) {
	    		
	    		
	    		double lat= currentLocation.getLatitude();
	    		double lon= currentLocation.getLongitude();
	    		double elevation=currentLocation.getAltitude();
	    		
	    		Intent intent = new Intent();
	    	
				Bundle b = new Bundle();
				b.putDouble("latitude", lat);
				
				intent.putExtras(b);
				
				b = new Bundle();
				b.putDouble("longitude", lon);
							
				intent.putExtras(b);
				
				b = new Bundle();
				b.putDouble("elevation", elevation);
							
				intent.putExtras(b);

				
				setResult(2, intent);
		    		
		    		
				finish();


	    		
	    	}
	    		    	
	    	
		}
	};
	
	@SuppressWarnings("unused")
	private class MyStatusListener implements GpsStatus.Listener{
		
	
		   public synchronized void onGpsStatusChanged(int event) {
		    switch( event )
		             {
		                case GpsStatus.GPS_EVENT_SATELLITE_STATUS: {
		                 GpsStatus status = mLocationManager.getGpsStatus( null );

		                    Iterable<GpsSatellite> list = status.getSatellites();
		                    for( GpsSatellite satellite : list )
		                    {
		                       if( satellite.usedInFix() )
		                       {
		                         elevation = satellite.getElevation() - 90.0f;
		                         
		                         if (elevation > 90 || satellite.getAzimuth() < 0 || satellite.getPrn() < 0){
		  				            continue;
		                         }
		                         
		                       }
		                    }

		                    break;
		                }

		                default:
		                   break;
		             }
		   }
		
	}

	
    private class MyLocationListener implements LocationListener 
    {
        public void onLocationChanged(Location loc) {
            if (loc != null) {
               /* Toast.makeText(context, 
                		context.getResources().getString(R.string.gps_signal_found), 
                    Toast.LENGTH_LONG).show();*/
            	
            	gpsStatus = mLocationManager.getGpsStatus(gpsStatus);

 			   
 			   for (GpsSatellite s: gpsStatus.getSatellites()){
 	
 	//			   float theta = - (s.getAzimuth() + 90);
 				  // float rad = (float) (theta * Math.PI/180.0f);
 				   
 				 
 				   
 				    elevation = s.getElevation() - 90.0f;
 				    
 				   Toast.makeText(context, 
	                		"Elevation: "+elevation, 
	                    Toast.LENGTH_LONG).show();
 				   
 				    if (elevation > 90 || s.getAzimuth() < 0 || s.getPrn() < 0){
 				            continue;
 				    }
 			    
 			   } 
            	
            	
            	
                setCurrentLocation(loc);
                handler.sendEmptyMessage(0);
            }
        }

        public void onProviderDisabled(String provider) {
        }

        public void onProviderEnabled(String provider) {
        }


		public void onStatusChanged(String provider, int status, Bundle extras) {
			
		}
    } 
    
}