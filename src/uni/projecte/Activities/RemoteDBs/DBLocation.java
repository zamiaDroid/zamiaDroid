package uni.projecte.Activities.RemoteDBs;

import edu.ub.bio.biogeolib.CoordConverter;
import edu.ub.bio.biogeolib.CoordinateLatLon;
import edu.ub.bio.biogeolib.CoordinateUTM;
import uni.projecte.R;
import uni.projecte.R.id;
import uni.projecte.R.layout;
import uni.projecte.R.string;
import uni.projecte.maps.UTMDisplay;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class DBLocation extends Activity implements LocationListener {
	
	private String coorSystem;
	
	private TextView myLocation;
	private TextView myLocationUTM;
	private TextView gpsState;
	private Button btConnect;

	private boolean noGPS;
	private String utmLocation;

	@Override
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    
        setContentView(R.layout.remote_db_location);
        
        /* URL: @from resource  */
        //String url=getIntent().getExtras().getString("url");
        
        
        myLocation = (TextView)findViewById(R.id.tvMyLocation);
        gpsState = (TextView) findViewById(R.id.gpsState);
        myLocationUTM = (TextView)findViewById(R.id.myLocationUTM);
        btConnect = (Button)findViewById(R.id.btConnect);
     
        btConnect.setOnClickListener(connect2DB);

  	     gpsManagement();
  	     
    }
	
	
	private OnClickListener connect2DB = new OnClickListener()
    {
        public void onClick(View v)
        {                        
        	    	       		
        		Intent intent = new Intent(v.getContext(), TaxonListExplorer.class);
        		intent.putExtra("utm",utmLocation);
        		
        		startActivity(intent);

        }
    };
    

    private void gpsManagement(){
    	
   	 LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		
		if (lm.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
	
			lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 10000, this);
			gpsState.setText("Buscant GPS");
	
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
		    	                
		    	        	setNoGPS();
			    	        dialog.dismiss();
		    	                
		    	           }

						
		    	       });
		    	
		    	AlertDialog alert = builder.create();
		    	
		    	alert.show();


		}
   	
   	
   	
   }
   
   private void setNoGPS() {
	
   		this.noGPS=true;
		
	}

	public void onLocationChanged(Location location) {
		if (location != null) {
			
			Double lat = location.getLatitude();
			Double lng = location.getLongitude();
			//String currentLocation = "Lat: " + lat + " Lng: " + lng;
			
		       CoordinateUTM utm = CoordConverter.getInstance().toUTM(new CoordinateLatLon(lat,lng));
		       myLocationUTM.setText(UTMDisplay.getBdbcUTM10x10(utm.getShortForm()));
	
	
			   myLocation.setText(lat.toString().subSequence(0, 7)+"\n"+lng.toString().subSequence(0, 7));
	
			   utmLocation=UTMDisplay.getBdbcUTM10x10(utm.getShortForm());
			   	
				gpsState.setText("GPS Trobat");

		    			
		}
	}
	
	public void onProviderDisabled(String provider) {
		// required for interface, not used
	}
	
	public void onProviderEnabled(String provider) {
		// required for interface, not used
	}
	
	public void onStatusChanged(String provider, int status, Bundle extras) {
		// required for interface, not used
	}
	
	protected boolean isRouteDisplayed() {
		return false;
	}
}
