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

package uni.projecte.Activities.Citations;


import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map.Entry;

import uni.projecte.R;
import uni.projecte.Activities.RemoteDBs.TaxonRemoteTab;
import uni.projecte.controler.CitationControler;
import uni.projecte.controler.CitationSecondLevelControler;
import uni.projecte.controler.DataTypeControler;
import uni.projecte.controler.MultiPhotoControler;
import uni.projecte.controler.PreferencesControler;
import uni.projecte.controler.ProjectControler;
import uni.projecte.controler.ThesaurusControler;
import uni.projecte.dataLayer.ThesaurusManager.ListAdapters.ThesaurusAutoCompleteAdapter;
import uni.projecte.dataLayer.utils.PhotoUtils;
import uni.projecte.dataTypes.ProjectField;
import uni.projecte.dataTypes.SecondLevelFieldHandler;
import uni.projecte.dataTypes.Utilities;
import uni.projecte.hardware.gps.MainLocationListener;
import uni.projecte.maps.GoogleMapsAPI;
import uni.projecte.maps.UTMDisplay;
import uni.projecte.ui.multiphoto.MultiPhotoFieldForm;
import uni.projecte.ui.multiphoto.PhotoFieldForm;
import uni.projecte.ui.multiphoto.SimplePhotoFieldForm;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.MutableContextWrapper;
import android.database.Cursor;
import android.graphics.Color;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.os.Vibrator;
import android.provider.MediaStore;
import android.text.Html;
import android.text.InputType;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import edu.ub.bio.biogeolib.CoordConverter;
import edu.ub.bio.biogeolib.CoordinateLatLon;
import edu.ub.bio.biogeolib.CoordinateUTM;

public class Sampling extends Activity {

	
	/*dataBase helpers*/
	   private ProjectControler projCnt;
	   private  ThesaurusControler tC;
	   private CitationControler citCnt;

	   public final static int SUCCESS_RETURN_CODE = 1;
	   
	   public static final int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 3;
	   public static final int LOAD_REMOTE_TAB = 6;

	   public static final int ENABLE_GPS=Menu.FIRST;
	   public static final int REPEATED_VALUES=Menu.FIRST+1;
	   public static final int SHOW_TAXON_INFO=Menu.FIRST+2;
	   public static final int SHOW_CITATIONS=Menu.FIRST+3;


	   private SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd_hhmmss");
	   	   
	   private SecondLevelFieldHandler sCLH;
	   
	   private long projId=-1;
	   
	   private PreferencesControler prefCnt;
	   private TextView tvProjectName;
	   
	   private TextView mLocationDisplay;
	   private TextView mAltitudeDisplay;
	   private TextView mLocationLabel;
	   private TextView mTimeStampDisplay;

	   private String thName;
	   private String projType;
	   private String thType;
	   
	   private boolean hasThesaurusField=false;
	   
	   /*top date Display*/
	   	private TextView mDateDisplay;
	    private int mYear;
	    private int mMonth;
	    private int mDay;

		private ArrayList<View> elementsList;
		private int n;

    	private Double lat;
    	private Double longitude;
    	private Double elevation;
    	private String presettedDate;
    	private long altitudeFieldId=-1;
    	private long citationIdCopy=-1;
    	
    	private boolean thStatus;
    	
    	private String fileName="";
    	private Uri imageUri;
    	private String photoPath;
    	private String lastPhotoField;
    	
    	private Button bUpdateLoc;
    	
    	private boolean predefinedLocation=false;
    	
    	private AutoCompleteTextView thElem;
    	private TextView altitudeTv;
    	private ThesaurusAutoCompleteAdapter elements;
    	 
    	private Hashtable<String, Integer> complexValuesList;
    	private Hashtable<String, Boolean> repetedValuesList;
    	private Hashtable<String, String> repetedValuesLabelList;
    	
    	private Hashtable<String, PhotoFieldForm> photoFieldsList;
    	
    	private LocationManager mLocationManager;
    	private MainLocationListener mLocationListener;

    	private boolean tempGPS=false;
    	private boolean uniqueCitationEntry;



		
    @Override
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        Utilities.setLocale(this);
        setContentView(R.layout.sampling);
        
        /*DB access*/
        
        projCnt=new ProjectControler(this);
        tC= new ThesaurusControler(this);
        prefCnt= new PreferencesControler(this);
        citCnt= new CitationControler(this);
        
        /* button Listeners*/ 
        
        Button bCreateSample = (Button)findViewById(R.id.bCreateSample);
        bCreateSample.setOnClickListener(bCreateCitationListener);
        
        bUpdateLoc = (Button)findViewById(R.id.bUpdateLocation);
        bUpdateLoc.setOnClickListener(bUpdateLocationListener);
        
        /* instances of main view elements */
        
        mDateDisplay = (TextView) findViewById(R.id.dateDisplay);
        mLocationDisplay = (TextView) findViewById(R.id.tvLocation);
        mLocationLabel = (TextView) findViewById(R.id.tvCoordinatesLabel);
        mAltitudeDisplay = (TextView) findViewById(R.id.tvAltitude);
        mTimeStampDisplay = (TextView) findViewById(R.id.tvSettedDate);

        tvProjectName = (TextView)findViewById(R.id.projectName);

        projId=getIntent().getExtras().getLong("id");
        
        projCnt.loadProjectInfoById(projId);
        	
        tvProjectName.setText(projCnt.getName());

        lat=getIntent().getExtras().getDouble("latitude");
        longitude=getIntent().getExtras().getDouble("longitude");
        elevation=getIntent().getExtras().getDouble("altitude");
        presettedDate=getIntent().getExtras().getString("timestamp");
        citationIdCopy=getIntent().getExtras().getLong("citationId");
        uniqueCitationEntry=getIntent().getExtras().getBoolean("uniqueCitation");
        

        if(lat==0.0 && longitude==0.0){
        	
        	lat=null;
        	longitude=null;
        }
        
        
        //we'll create a citation with a pre-setted location and/or date
        if(lat!=null && longitude!=null){
        	
        	predefinedLocation=true;
        	uniqueCitationEntry=true;
        	bUpdateLoc.setVisibility(View.INVISIBLE);
        	
        	if(elevation==null || elevation==0.0){
    
	        	GoogleMapsAPI googleAP= new GoogleMapsAPI();
 	        	elevation=googleAP.getElevationFromGoogleMaps(longitude, lat);
	        	
        	}
        	if(presettedDate!=null){
        		
        		 CitationControler citCnt=new CitationControler(this);
        		 presettedDate=citCnt.getAvailableTimestamp(projId,presettedDate);
        	
        	}
        	
        	updateDisplay();
        	
        }
        else{
        	
        	  if(prefCnt.gpsNeeded()){
        	        
              	callGPS();
              
              }
        
        }
        
        if(prefCnt.isUTM()){
        	
        	mLocationLabel.setText(Html.fromHtml("<b>UTM:</b>"));
        	
        }
        
        
        createFieldForm(projId);
        updateDisplay();

        
    }

    
    @Override
	protected void onStop(){
		  
		  super.onStop();
		  
		  tC.closeCursors();
		  
	  }

    
    
	private Handler handler = new Handler() {
		
		@Override
		public void handleMessage(Message msg) {
			
		
			lat= mLocationListener.getLatitude();
	   	    longitude= mLocationListener.getLongitude();
	   	    elevation= mLocationListener.getElevation();
	   	    
	   	    if(lat==0.0 && longitude==0.0){
	   	    	
	   	    	lat=null;
	   	    	longitude=null;
	   	    	
	   	    }
	   	    else{
	   	    	
				mLocationManager.removeUpdates(mLocationListener);
	    		tempGPS=false;
	    		 
		   	    updateDisplay();
		   	    
	   	    }
		    	
	    	
		}
	};
    
    private void callGPS(){
    	
	            		
	    	mLocationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE); 
			
			if (mLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
		
				mLocationListener = new MainLocationListener(mLocationManager,handler,prefCnt.getGeoidCorrection());
					
				mLocationManager.requestLocationUpdates(
			                LocationManager.GPS_PROVIDER, 0, 0, (LocationListener) mLocationListener);
				
				tempGPS=true;
				
				
				updateDisplay();
			
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
		    	                
		    	        	  // finish();
		    	        	   
		    	        	   prefCnt.setGPSNeeded(false);
		    	                
		    	           }
		    	       });
		    	
		    	AlertDialog alert = builder.create();
		    	alert.show();

		}
				   

    }
    
    
	public boolean onCreateOptionsMenu(Menu menu) {
    	
		if(prefCnt.gpsNeeded()) menu.add(0, ENABLE_GPS, 0,R.string.mDesactivateGPS).setIcon(android.R.drawable.ic_menu_mylocation);
		else menu.add(0, ENABLE_GPS, 0,R.string.mActivateGPS).setIcon(android.R.drawable.ic_menu_mylocation);
		
		menu.add(0, REPEATED_VALUES, 0,R.string.mRepeatedField).setIcon(android.R.drawable.ic_menu_preferences);
		if(hasThesaurusField) menu.add(0, SHOW_TAXON_INFO, 0,R.string.mShowTaxonInfo).setIcon(android.R.drawable.ic_dialog_info);
		menu.add(0, SHOW_CITATIONS, 0,R.string.bSampleManagement).setIcon(android.R.drawable.ic_menu_agenda);


    	return super.onCreateOptionsMenu(menu);
    }

	

    
    @Override
	public boolean onOptionsItemSelected(MenuItem item) {
    	
    	
		switch (item.getItemId()) {
			case ENABLE_GPS:
				
				if(prefCnt.gpsNeeded()){
					
					prefCnt.setGPSNeeded(false);
					
					
				} 
				else{
					
					prefCnt.setGPSNeeded(true);
					callGPS();
					
				}
				 			 
			break;
			
			case REPEATED_VALUES:
				
				showRepetedValuesList();
				 			 
			break;
			
			case SHOW_CITATIONS:
				
				Intent intent = new Intent(getBaseContext(), CitationManager.class);        	       
	 			Bundle b = new Bundle();
	 			b = new Bundle();
	 			b.putLong("id", projId);
	 			intent.putExtras(b);

	            startActivity(intent);
				
				
			break;
			
			case SHOW_TAXON_INFO:
				
				if (!emptyThesaurus()){
	        	    
	       	    	 Toast.makeText(getBaseContext(), 
	       	                R.string.fieldMissing, 
	       	                 Toast.LENGTH_SHORT).show();
       	    	
				}
				else{
					
				
										 
					 if(thStatus){
						 
						 String taxonName=thElem.getText().toString();
						 
						 if(tC.checkTaxonBelongs(taxonName)){
						 
							 loadBiologicalRemoteCard(taxonName);
							
						 }
						 
						 else{
							 
							 Toast.makeText(getBaseContext(), 
				       	               R.string.toastTaxonNotExists, 
				       	                 Toast.LENGTH_SHORT).show();
							 
							 
						 }
						 
						 
					 }
					 else{
						 
						 Toast.makeText(getBaseContext(), 
			       	               R.string.toastNotThesarus, 
			       	                 Toast.LENGTH_SHORT).show();
						 
					 }
					 
					
					
				}

				
				
			break;
				
				
		}
		
	
		return super.onOptionsItemSelected(item);
	}
    
    
   private void showRepetedValuesList() {
	   
	   int elements=repetedValuesList.size();
	   boolean[] states= new boolean[elements];
	   final CharSequence[] items= new CharSequence[elements];
	   
	   int i=0;

	   
	   for (Entry<String, Boolean> s : repetedValuesList.entrySet())
		{
		   states[i]=s.getValue();
		   items[i]=s.getKey();
		   i++;
		  
		}  
	   
       AlertDialog.Builder builder = new AlertDialog.Builder(this);
       
       builder.setTitle(getString(R.string.repeatedFieldTitle));
       
       builder.setMultiChoiceItems(items, states, new DialogInterface.OnMultiChoiceClickListener(){
           public void onClick(DialogInterface dialogInterface, int item, boolean state) {
      	   
        	   repetedValuesList.remove(items[item]);
               repetedValuesList.put(items[item].toString(),state);
               
           }
       });
       builder.create().show();
	   
	}
   
   
   /*
    * 
    *  
    */
   
   private void loadBiologicalRemoteCard(String taxonName){
	  
	   
	   String filum=Utilities.translateThTypeToFilumLetter(this, thType);
	   
	   if(filum!=null){
	   		 
			Intent intent = new Intent(getBaseContext(), TaxonRemoteTab.class);
			intent.putExtra("filumLetter", filum);
			intent.putExtra("taxon",taxonName);
			intent.putExtra("projId",projId);

			startActivityForResult(intent,LOAD_REMOTE_TAB);
			
		}

	   
   }

   
   
    
    /*
     * When backButton is pressed a dialog is shown showing an alert.
     * The alert advises the user that it will lose data if it finishes the activity.
     * 
     * @see android.app.Activity#onKeyDown(int, android.view.KeyEvent)
     */

 
 

	public boolean onKeyDown(int keyCode, KeyEvent event) {
    	
    	
        if ((keyCode == KeyEvent.KEYCODE_BACK)) {
        	
        	
        	if(!filledFields()){
        		
        		endActivity();
        		
        		
        	} 
        	else{
    	
	        	AlertDialog.Builder builder = new AlertDialog.Builder(this);
		    	
		    	builder.setMessage(R.string.backFromCitationMessage)
		    	       .setCancelable(false)
		    	       .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
		    	           public void onClick(DialogInterface dialog, int id) {
		    
		    	        	   endActivity();
	        	   
		    	           }
	
						
		    	       })
		    	       .setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
		    	           public void onClick(DialogInterface dialog, int id) {
		    	                
		    	        	   
		    	        	   dialog.dismiss();
	 
		    	           }
		    	       });
		    	
		    	AlertDialog alert = builder.create();
		    	
		    	alert.show();
        		
        	}
	    	
	    	
	    	
	        return true;
	        
	        
	        
        	
        }
        
        return false;

    }

	
	public void endActivity(){
		
		   prefCnt.setGPSNeeded(true);
    	   
    	   if(tempGPS && prefCnt.gpsNeeded() && mLocationManager!=null && mLocationListener !=null) mLocationManager.removeUpdates(mLocationListener);
    	   
    	   sCLH.removeSecondLevelFields();
    	       	   
    	   //removing stored multiPhotos
    	   Iterator<String> photoFields=photoFieldsList.keySet().iterator();
	
			while(photoFields.hasNext()){
				
				PhotoFieldForm tmpField=photoFieldsList.get(photoFields.next());
				
				if(tmpField instanceof MultiPhotoFieldForm) ((MultiPhotoFieldForm) tmpField).removeAllPhotos();
				
			}
    	   
    	   tC.closeThReader();
    	   
    	   finish();
		
	}
    



	/*
     * It takes the date of the system and location and shows it.
     * 
     */
    
    
    private void updateDisplay() {
    	
        final Calendar c = Calendar.getInstance();

    	 mYear = c.get(Calendar.YEAR);
         mMonth = c.get(Calendar.MONTH);
         mDay = c.get(Calendar.DAY_OF_MONTH);

         
        mDateDisplay.setText(
            new StringBuilder()
                    // Month is 0 based so add 1
            		.append(mDay).append("-")
                    .append(mMonth + 1).append("-")
                    .append(mYear).append(" ")
                   /* .append(" - ")
                    .append(pad(mHour)).append(":")
                    .append(pad(mMinute))*/);
        
        if(presettedDate!=null) {
        	
        	mTimeStampDisplay.setVisibility(View.VISIBLE);
        	mTimeStampDisplay.setText(" | "+presettedDate);
        	mTimeStampDisplay.setTextColor(Color.GREEN);
        	
        }
        else mTimeStampDisplay.setVisibility(View.GONE);
        
        
        if(lat==null || longitude==null){
        	
    		//pm.setGPSNeeded(false);

 	    	 //mLocationDisplay.setText(R.string.gps_disabled);
 	    	 
    		mLocationDisplay.setText("");
    		
 	    	 bUpdateLoc.setVisibility(Button.INVISIBLE);

        	
        }
        else{
        	
        	   if(prefCnt.isUTM()){
      	    	 
      	         CoordinateUTM utm = CoordConverter.getInstance().toUTM(new CoordinateLatLon(lat,longitude));
      	         
      	         
      	         if(utm.getShortForm().length()<15){ 
      	
      	        	 mLocationDisplay.setText(utm.getShortForm());
      	   	    	      	        	 
      	         }
      	         else{
      	        	 
      	           String utmP=UTMDisplay.convertUTM(utm.getShortForm(),"1m",true);
          		   mLocationDisplay.setText(utmP);
     	        	 
      	         }

      	     }
      	     else {
      	    	 
      	    	 mLocationDisplay.setText(lat.toString().subSequence(0, 7)+" - "+longitude.toString().subSequence(0, 7));

      	     }
        	   
        	   
        	mAltitudeDisplay.setText(""+(int)elevation.doubleValue());
        	   
        	if(altitudeTv!=null) altitudeTv.setText(""+(int)elevation.doubleValue());
        	
   	    	if(!predefinedLocation) bUpdateLoc.setVisibility(Button.VISIBLE);
   	    	
   	    	if(tempGPS){ 
   	    		
   	    		mLocationDisplay.setTextColor(Color.RED);
   	    		mAltitudeDisplay.setTextColor(Color.RED);
   	    		
   	    	}
   	    	else {
   	    		
   	    		mLocationDisplay.setTextColor(Color.GREEN);
   	    		mAltitudeDisplay.setTextColor(Color.GREEN);
   	    		
   	    	}
   	    	
        }

	
    
    }
    

 
    /*
     * Listener of the citation creation button.
     * 
     * The thesaurus is mandatory. When no geo-location button is check, GPS activity won't be called. 
     * 
     */
    



	private OnClickListener bCreateCitationListener = new OnClickListener()
    {
        public void onClick(View v)
        {                        
            
        	//if the thesaurus is not chosen when can't create a citation
        	
        	if (!emptyThesaurus()){
        	    
        	    	 Toast.makeText(getBaseContext(), 
        	                R.string.fieldMissing, 
        	                 Toast.LENGTH_SHORT).show();
        	    	
        	    }
        	  
        	  else{
        		 
        			if(tvProjectName.length()==0){
        	    		
        	    		Toast.makeText(getBaseContext(), 
        	                    R.string.projNameMissing, 
        	                    Toast.LENGTH_SHORT).show();
        	    	}
        	    	
        	    	else  {
        	    		
        	    			if(prefCnt.gpsNeeded()&& tempGPS){
        	    				
        	    				
        	    				if(lat==null && longitude==null){
        	    					
            	    				Utilities.showToast(getString(R.string.toastGPSWorking), getBaseContext());

        	    					
        	    				}
        	    				else{
        	    					
        	    					AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
            	    		    	
            	    		    	builder.setMessage(R.string.waitingForGPS)
            	    		    	       .setCancelable(false)
            	    		    	       .setPositiveButton(R.string.lastKnownLocation, new DialogInterface.OnClickListener() {
            	    		    	           public void onClick(DialogInterface dialog, int id) {
            	    		    
            	    		    	        	   dialog.dismiss(); 
            	    		    	        	   createSample(lat, longitude);
             	    		    	        	       	    		    	        	

            	    	        	   
            	    		    	           }

            	    						
            	    		    	       })
            	    		    	       .setNegativeButton(R.string.waitForLocation, new DialogInterface.OnClickListener() {
            	    		    	           public void onClick(DialogInterface dialog, int id) {
            	    		    	              
            	    		    	        	   dialog.dismiss();        	    		    	        	
            	    	 
            	    		    	           }
            	    		    	       });
            	    		    	
            	    		    	AlertDialog alert = builder.create();
            	    		    	
            	    		    	alert.show();
        	    					
        	    					
        	    				}
        	    				
        	    				
        	    				
        	    			}
        	    			else{
        	    				
	        	    			if(lat==null && longitude==null){
	            	    				
	                 	    			 createSample(100, 190);

	           	    			}
	           	    			else{
	           	    				
	           	    				//GPS Activity that will give back the latitude and longitude
	              	    			 	createSample(lat, longitude);

	           	    			}
           	    		        
        	    			}
        	    		
        	    		
        	    			    	
        	    	}

        	  }
        	
        }
    }; 
    
    
    /*
     * Method called when finish button is pressed.
     * It only finishes the Activity
     * 
     */
    
    private void finishActivity(){
    	
    	
        Intent intent = new Intent();
        setResult(0, intent);
   			
        finish();
    	
    	
    }
    
    private OnClickListener bUpdateLocationListener = new OnClickListener()
    {
        public void onClick(View v)
        {                        
             
            if(prefCnt.gpsNeeded()){
                
            	callGPS();
            
            }
        	
        }
    };
    
    
    /*
     * 
     * This listener is similar to BCreateCitationListener.
     * There is only one difference. When the citation is stored the activity is finished.
     * 
     */
    
    @SuppressWarnings("unused")
	private OnClickListener bFinishListener = new OnClickListener()
    {
        public void onClick(View v)
        {                        
            
        	if (!emptyThesaurus()){
        	    	
        	    	 Toast.makeText(getBaseContext(), 
        	                R.string.fieldMissing, 
        	                 Toast.LENGTH_SHORT).show();
        	    	
        	    }
        	  
        	  else{
        		 
        			if(tvProjectName.length()==0){
        	    		
        	    		Toast.makeText(getBaseContext(), 
        	                    R.string.projNameMissing, 
        	                    Toast.LENGTH_SHORT).show();
        	    	}
        	    	
        	    	else  {

        	    			 createSample(lat, longitude);
        	        		 finishActivity();
 	    	
        	    	}

        	  }
        	
        }
    };
    
   
    
    /*
     * It checks if the thesaurus field is filled
     * 
     */
    
    private boolean emptyThesaurus(){
    	
    	
    	boolean notEmpty=true;

    	
    	for (int i=0;i<n;i++){
    		
    		View vi=elementsList.get(i);

    		
    		if((vi instanceof AutoCompleteTextView)){
    			
    			if (((AutoCompleteTextView)vi).length()==0) notEmpty=false;	

    		}

    		
    	}
    	
    	return notEmpty;
    	
    }
    
    
    private boolean filledFields(){
    	
    	boolean filled=false;
    	
    	for (int i=0;i<n&&!filled;i++){
    		
    		View vi=elementsList.get(i);

    		
    		if((vi instanceof TextView)){
    			
    			if (((TextView)vi).length()!=0) filled=true;	

    		}

    		
    	}
    	
    	return filled;
    	
    	
    }
    
    
    @SuppressWarnings("unused")
    
	private boolean emptyAttributes(){
    	
    	boolean notEmpty=true;
    	

    	//are all the attributes filled?
    	
    	for (int i=0;i<n;i++){
    		
    		View vi=elementsList.get(i);
    		
    		if (vi instanceof EditText){
				
    			if (((EditText)vi).length()==0) notEmpty=false;	
			}
    		
    		else if((vi instanceof AutoCompleteTextView)){
    			
    			if (((AutoCompleteTextView)vi).length()==0) notEmpty=false;	

    		}
			
    		
    		else if((vi instanceof CheckBox)){
    			
    			
    			//rubish
    		}
			
			else {
				
				if (((Spinner)vi).getSelectedItem().toString().length()==0) notEmpty=false;	
				
			}
    		
    		
    	}

    	return notEmpty;  
    	    	
    }
    
    
    /*
     * 
     * This method clears the form for the next citation
     * 
     */
    private void removeAttributes(){


			n=elementsList.size();
			
			for (int i=0;i<n;i++){
				
				
				View et=elementsList.get(i);
								
				if (et instanceof EditText){
					
					Boolean repeat=false;
					
					String label=repetedValuesLabelList.get(et.getTag().toString());
					
					if(label!=null) repeat=repetedValuesList.get(label);
					
					if(repeat==null || !repeat) ((TextView) et).setText("");
					
					
				}
				else if(et instanceof CheckBox){
					
					((CheckBox) et).setChecked(false);
					
				}
				else if(et instanceof Spinner){
					
					int pos=complexValuesList.get(et.getTag());
					
					if(pos>-1) ((Spinner) et).setSelection(pos);
					
				}
				else if(et instanceof TextView){
				
					createRandomIdSecondLevelField(et);
					
				}
			
				
			}
			
			//iterate over all photos
			Iterator<String> photoFields=photoFieldsList.keySet().iterator();
			
			while(photoFields.hasNext()){
				
				PhotoFieldForm tmpField=photoFieldsList.get(photoFields.next());
				
				tmpField.clearForm();
				
			}
   		    
    }
    
    
    private void createRandomIdSecondLevelField(View et) {
		
    	String secLevId=sCLH.replaceSecondLevelField(et.getId(), (String) et.getTag());

    	((TextView) et).setText(secLevId);
		
	}

    /*
     * This method takes the values filled by the user in the form and store them into the DB
     * For each type of field the retrieval of the value will be different
     * 
     */

	private void addFieldValues(long idSample, CitationControler smplCntr){
    			
			boolean isMultiPhotoField=false;
				
			n=elementsList.size();
			
			smplCntr.startTransaction();
			

			
			for (int i=0;i<n;i++){
				
				String value="";
				String label="";
				long citationValueId=-1;
				
				View et=elementsList.get(i);
				
				if(et instanceof CheckBox){
					
					if (((CheckBox) et).isChecked()) value="true";
					else value="false";
					
					label=et.getTag().toString();
					
				}
				else if (et instanceof EditText){
					
					value= ((EditText) et).getText().toString();
					label= ((EditText) et).getTag().toString();
					
				}
				
				else if(et instanceof TextView){
					
					value= ((TextView) et).getText().toString();
					label= ((TextView) et).getTag().toString();
					
				}
				else if(et instanceof HorizontalScrollView){
					
					//multiPhotoElement
					
					MultiPhotoFieldForm multiPhoto=(MultiPhotoFieldForm) photoFieldsList.get(et.getTag());

					value=multiPhoto.getSecondLevelId();
					label=(String) et.getTag();
					
					isMultiPhotoField=true;
					
				}
				else {
					
					Spinner sp=(Spinner)et;
					
						if(sp.getSelectedItem()==null) value="";
						else value =((Spinner) et).getSelectedItem().toString();
					
					label= ((Spinner) et).getTag().toString();

					
				}
				
					/*if value is empty we don't add the value into the database*/
					if(value.compareTo("")==0){
						
					}
					
					else{
						
						Log.i("Citation","Action-> created citationValue : Label: "+label+" Value: "+value);
						citationValueId=smplCntr.addCitationField(projId,idSample,et.getId(),label, value);
							
						if(isMultiPhotoField){
						
							MultiPhotoFieldForm multiPhoto=(MultiPhotoFieldForm) photoFieldsList.get(et.getTag());
							multiPhoto.setCitationId(citationValueId);
							
							isMultiPhotoField=false;
						} 
						
					}
					
				
			}
			
			smplCntr.addObservationAuthor(projId,idSample);	
			
			if(altitudeFieldId>0 && elevation!=0.0) smplCntr.addCitationField(projId,idSample,altitudeFieldId,"altitude", String.valueOf((int)elevation.doubleValue()));

			smplCntr.EndTransaction();
			

    	
    }
	
    
    private void addCitationSubFields() {
    	
    	//Adding MultiPhoto: photoList
    	
    	MultiPhotoControler multiProjCnt= new MultiPhotoControler(this);
    	   	   	
    	Iterator<String> photoFields=photoFieldsList.keySet().iterator();
		
		while(photoFields.hasNext()){
			
			PhotoFieldForm tmpField=photoFieldsList.get(photoFields.next());
							
			if(tmpField instanceof MultiPhotoFieldForm){
				
				long subFieldId=multiProjCnt.getMultiPhotoSubFieldId(((MultiPhotoFieldForm) tmpField).getFieldId());
				
				multiProjCnt.addPhotosList((MultiPhotoFieldForm) tmpField,subFieldId);				
				
			}						
		}

	}

    
    
    /*
     * 
     * This method stores the citation in the DB. Getting location and projectId
     * It also calls the addFildValues method for the insertion of the filled fields.
     * 
     */
    
    private long createSample(double latPoint, double longPoint){
    	
    
	    CitationControler sampleCntr=new CitationControler(this);

	    long idSample=-1;
        		
    	// Sample Creation with lat,long, comment and rsId		
        if(presettedDate==null) idSample=sampleCntr.createCitation(projId, latPoint, longPoint, "");
        else idSample=sampleCntr.createCitationWithDate(projId, latPoint, longPoint, "",presettedDate);
        
	    Log.d("Citation","Action -> Create New Citation");

        
        ((Vibrator)getSystemService(VIBRATOR_SERVICE)).vibrate(300);
        
        addFieldValues(idSample, sampleCntr);
          
	    Log.d("Citation","Action -> Fields Added");
	    
		addCitationSubFields();
	    
		Log.d("Citation","Action -> MutliFields Added");

		
       	Toast.makeText(getBaseContext(), 
                getString(R.string.bSuccessCitationCreation), 
                Toast.LENGTH_SHORT).show();
       	
	    Log.d("Citation","Action -> Citation Created: "+tvProjectName.getText().toString()+":"+idSample+"|"+latPoint+":"+longPoint);

	  	tempGPS=true;

       	removeAttributes();
       	
       	if(uniqueCitationEntry) {
		    	
		    	finish();
		    	
		}		   
		else{

			   if(prefCnt.gpsNeeded()){
			        
		           	updateDisplay();
		        	callGPS();
		        
		        }
  
			 elementsList.get(0).requestFocus();
		 
		 }
       	
       	
	    
       	return idSample;
   
    
    }
    
    private OnItemClickListener synonimThListener = new OnItemClickListener()
    {
        public void onItemClick(AdapterView<?> parent, View view, int position, long id)
        {
        	
        	if(prefCnt.getSynonymCheck()){
        	
	        	View syno=((LinearLayout) view).getChildAt(0);
	        	View name=((LinearLayout) view).getChildAt(1);
	        	final View icodeView=((LinearLayout) view).getChildAt(2);
	        	
	
	        	
	        	int visible=syno.getVisibility();
	        	
	        	if(visible>0){
	        		
	        			AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
	
	        			String question = String.format(getString(R.string.synonymQuestion), ((TextView)name).getText());
			    	
			    	builder.setMessage(question)
			    	       .setCancelable(false)
			    	       .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
			    	           public void onClick(DialogInterface dialog, int id) {
			    	        	   
			    	        	   String icode=((TextView) icodeView).getText().toString();
			    
			    	        
			    	        	   thElem.setText(tC.fetchThesaurusSynonymous(icode));
			    	        		        	   
			    	           }
	
							
			    	       })
			    	       .setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
			    	           public void onClick(DialogInterface dialog, int id) {
			    	                
			    	        	   dialog.dismiss();
			    	                
			    	           }
			    	       });
			    	
			    	AlertDialog alert = builder.create();
			    	alert.show();
	        		
	        	}
	       
	        
	        }
        	
        	// update gps status
        	if(prefCnt.gpsNeeded() && prefCnt.isTaxonUpdate()){
                
            	callGPS();
            
            }
        	
        	
	        	
        }

	
}; 

    
    /*
     * This method creates the project fields form.
     * For each field it creates a TextView with the field label and also another 
     * view that will be field with data provided by the user.
     * 
     * Type: 
     * 		simple -> EditText
     * 		thesaurus -> AutoCompleteEditText
     * 		bool -> CheckBox
     * 		complex -> Spinner or (dropdowList in other languages)
     * 
     */
    
    private void createFieldForm(final long projId){
    	
    	
    	//list where items will the hold
    	
		   elementsList=new ArrayList<View>();
		   complexValuesList= new Hashtable<String, Integer>();
		   repetedValuesList= new Hashtable<String, Boolean>();
		   repetedValuesLabelList= new Hashtable<String, String>();
		   
		   photoFieldsList = new Hashtable<String, PhotoFieldForm>();

		   
		   sCLH=new SecondLevelFieldHandler(this);
		   
		   
		  DataTypeControler dtH=new DataTypeControler(this);

		  LinearLayout lPhoto=null;
		  LinearLayout llsL=null;
		   
		   //main layout that will include the form
		   LinearLayout l= (LinearLayout)findViewById(R.id.fieldsLay);

		   
		   ProjectControler rsC=new ProjectControler(this);
		   citCnt.startTransaction();

		   
		   rsC.loadProjectInfoById(projId);
		   
		   
		   //we load the Th reader that will help to create the autocomplete view with the items of the provided thName.

		   thName=rsC.getThName();
		   projType=rsC.getProjType();
		   
		   thStatus=tC.initThReader(thName);
		   
		  	Cursor thInfo=tC.getThInfo(thName);
		  	thInfo.moveToFirst();
		  	
		  	if(thInfo.getCount()>0){
		  		
		  	   thType=Utilities.translateThTypeToCurrentLanguage(this, thInfo.getString(4));	        
		  		
		  	}
		   
		   ArrayList<ProjectField> projFieldList=rsC.getProjFields(projId);
		   
		   Iterator<ProjectField> it=projFieldList.iterator();
		   
		   int i=0;
  
		   
		   //for each field we will create the TextView with the label and an "special" view where the user will provide the field value
		  while(it.hasNext()){
			  
			   LinearLayout llField=new LinearLayout(this);
			   TextView t=new TextView(getBaseContext());
			   
			   ProjectField att=it.next();
			   
			   String fieldType =att.getType();
			   String fieldLabel= att.getLabel();
			   
			   t.setText(fieldLabel);

			   llField.addView(t);
			   llField.setPadding(4, 4, 4, 4);

			   
			   //when the field is Simple we create an EditText		   
			   if (fieldType.equals("simple") || fieldType.equals("number")){
				   
				   EditText etFieldValue=(EditText) new EditText(getBaseContext());	   
				   
				   int idD= (int) att.getId();
				   String tag=att.getName();
				   
				   checkAutoField(att);
				   
				   if(att.getValue()!=null && att.getValue().length()>0) {
					   
					   etFieldValue.setText(att.getValue());
				   
				   }
				   
				   if(citationIdCopy>0) {
					   
					   String pred=citCnt.getFieldValue(citationIdCopy,att.getId());
					   etFieldValue.setText(pred);
					   
				   }

				   etFieldValue.setTag(tag);
				   etFieldValue.setId(idD);
				   
				   if(fieldType.equals("number")){
					   
					   etFieldValue.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
					   
				   }
				   else{
					   
					   etFieldValue.setImeOptions(EditorInfo.IME_ACTION_NEXT);
					   
				   }
				   
				   if(att.getName().equals("altitude")) altitudeTv=etFieldValue;


				   etFieldValue.setLayoutParams(new LayoutParams
					        (ViewGroup.LayoutParams.FILL_PARENT,ViewGroup.LayoutParams.
					                WRAP_CONTENT));
				   
				   llField.addView(etFieldValue);
				   elementsList.add(etFieldValue);
				   
				   repetedValuesList.put(att.getLabel(), false);
				   repetedValuesLabelList.put(att.getName(), att.getLabel());
				   
			   }
			   
			   //when the field is boolean we create a CheckBox
			   else if (fieldType.equals("boolean")){
				   
				   CheckBox e=(CheckBox) new CheckBox(getBaseContext());	   		   
				   int idD= (int) att.getId();
				   e.setId(idD);
				   e.setTag(att.getName());

				   llField.addView(e);
				   elementsList.add(e);
				   
				   if(citationIdCopy>0) {
					   
					   String pred=citCnt.getFieldValue(citationIdCopy,att.getId());
					   if(pred.equals("true")) e.setChecked(true);
					   
				   }
				   
			   }
			   
			   //when the field is photo we show photo Interface
			   else if (fieldType.equals("photo")){
				   
				   SimplePhotoFieldForm photoFieldForm = new SimplePhotoFieldForm(this,projId,att,llField);
				   
				   elementsList.add(photoFieldForm.getEtPhotoPath());

				   lPhoto=photoFieldForm.getlPhoto();
				   llField=photoFieldForm.getLlField();
				   
				   photoFieldForm.setAddPhotoEvent(takePicture);
				   photoFieldForm.setRemoveEvent(removePicture);
				   
				   photoFieldsList.put(att.getName(), photoFieldForm);
			
			   
			   }
			   else if(fieldType.equals("multiPhoto")){
				   
				   MultiPhotoFieldForm multiPhotoFieldForm = new MultiPhotoFieldForm(this, projId, att, llField,MultiPhotoFieldForm.CREATE_MODE);
				   
				   multiPhotoFieldForm.setAddPhotoEvent(takePicture);
				   
				   
				   photoFieldsList.put(att.getName(), multiPhotoFieldForm);

				   elementsList.add(multiPhotoFieldForm.getImageScroll());

				   
			   }
			   
			   //when the field is a thesaurus we create an AutoCompleteView and we fill it with the items provided by the ThesaurusControler
			   
			   else if(fieldType.equals("thesaurus")){
				   
				   String tag=att.getName();
				   
				   View e;
				   
				   if(thStatus){
					   
					   e=(AutoCompleteTextView) new AutoCompleteTextView(getBaseContext());	   
					   
					   elements = tC.fillData((AutoCompleteTextView) e);

					  ((AutoCompleteTextView) e).setAdapter(elements);
					  
					  ((AutoCompleteTextView) e).setOnItemClickListener(synonimThListener);
					  
					  thElem=((AutoCompleteTextView) e);
					  
					  hasThesaurusField=true;

					   
				   }
				   else{
					   
					   e=(EditText) new EditText(getBaseContext());
					   
				   }
				   
				   ((TextView) e).setImeOptions(EditorInfo.IME_ACTION_NEXT);
				   ((TextView) e).setHint(getString(R.string.thHint));
	  
				   e.setTag(tag);

				      
				   int idD= (int) att.getId();
				   e.setId(idD);
					   
				      
				   e.setLayoutParams(new LayoutParams
						        (ViewGroup.LayoutParams.FILL_PARENT,ViewGroup.LayoutParams.
						                WRAP_CONTENT));
					   
				   llField.addView(e);
				   elementsList.add(e);
					  
					  
			   }
			   
			   else if(fieldType.equals("secondLevel")){
				   
				   
				   Button showList=(Button) new Button(getBaseContext());
				   showList.setText(this.getString(R.string.slShowElem));
				   
				   Button addSubFields=(Button) new Button(getBaseContext());
				   addSubFields.setText(this.getString(R.string.slAddElem));

				   TextView et= (TextView) new TextView(getBaseContext());
				   
				   TextView numElements=new TextView(this);

				   int idD= (int) att.getId();
				   et.setId(idD);
				   et.setTag(att.getName());
				   
				   String subLevelIdentifiear=sCLH.addSecondLevelField(idD, att.getName(),numElements);

				   addSubFields.setTag(subLevelIdentifiear);
				  
				   et.setText(Html.fromHtml("<b>"+subLevelIdentifiear+"</b>"));
			
				   
				   addSubFields.setId(idD);
				   showList.setId(idD);

				   
				   et.setPadding(3,3,3,10);
				   
				   
				  et.setImeOptions(EditorInfo.IME_ACTION_NEXT);
				 

				   showList.setOnClickListener(new OnClickListener() {

		                public void onClick(View v) {
		                 
		                Intent myIntent = new Intent(v.getContext(), SecondLevelList.class);
		            				            		
		    	        	myIntent.putExtra("id", projId);
		    	        	
		    	        	myIntent.putExtra("subProjId", (long)v.getId());
		    	        	
		    	        	String subLevelTag=sCLH.getSecLevelIdByFieldId(v.getId());

		    	        	myIntent.putExtra("subLevelTag", subLevelTag);
		    	
		    	            startActivityForResult(myIntent, 5);

		                }
				   });
				   
				   addSubFields.setOnClickListener(new OnClickListener() {

		                public void onClick(View v) {
		                  		                	
		            		Intent myIntent = new Intent(v.getContext(), SecondLevelSampling.class);
		            		
		    	        	myIntent.putExtra("id", projId); 
		    	        	myIntent.putExtra("subProjId", (long)v.getId());
		    	        	
		    	        	String subLevelTag=sCLH.getSecLevelIdByFieldId(v.getId());
		    	        	myIntent.putExtra("subLevelTag", subLevelTag);
		    	
		    	            startActivityForResult(myIntent, 4);
		                	
		                }
				   });
				   
				   
				   
				   numElements.setText("0");
				   
				   llField.addView(et);
				   
				   llsL= new LinearLayout(this);
				   llsL.setOrientation(LinearLayout.HORIZONTAL);
				   
				   llsL.addView(showList);
				   llsL.addView(addSubFields);
				   llsL.addView(numElements);
				   
				   //l.addView(ll);
		
				   elementsList.add(et);
				   
			
			   }
			   
			 else{
				   
					
				 String[] items=dtH.getItemsbyFieldId(att.getId());
				 
				 Spinner e=(Spinner)new Spinner(this);
				 e.setPrompt(getString(R.string.chooseItem));
				 
				 
				  e.setLayoutParams(new LayoutParams
					        (ViewGroup.LayoutParams.FILL_PARENT,ViewGroup.LayoutParams.
					                WRAP_CONTENT));

						 ArrayAdapter<CharSequence> adapter = new ArrayAdapter<CharSequence>(this,
				                    android.R.layout.simple_spinner_item,items);
				
						  
						   adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
						   
						  
						   e.setAdapter(adapter);
						   
						   if(att.getValue()!=null && att.getValue().length()>0) complexValuesList.put(att.getName(),setDefaultSpinnerItem(e,att.getValue(),items));
						   else complexValuesList.put(att.getName(), 0);
						   
						   int idD= (int) att.getId();
						   e.setId(idD);
						   e.setTag(att.getName());
						   
						   if(citationIdCopy>0) {
							   
							   String pred=citCnt.getFieldValue(citationIdCopy,att.getId());
							   Utilities.setDefaultSpinnerItem(e, pred, items);
							   
						   }

						   
						  llField.addView(e);
						   elementsList.add(e);


	   
			   }
			   

			   
			   if (fieldType.equals("photo")){ 
				   
				   
				   LinearLayout ll= new LinearLayout(this);
				   ll.setOrientation(LinearLayout.VERTICAL);
				   ll.setPadding(3,3,3,3);

				   ll.addView(llField);
				   ll.addView(lPhoto);
				   ll.setBackgroundColor(Color.parseColor("#ff333333"));
				  
				   l.addView(ll);

				   
			   }
			   else if(fieldType.equals("secondLevel")){
				   
				   LinearLayout ll= new LinearLayout(this);
				   ll.setOrientation(LinearLayout.VERTICAL);
				   ll.setPadding(3,3,3,3);
	

				   ll.addView(llField);
				   ll.addView(llsL);
				   
				   ll.setBackgroundColor(Color.parseColor("#ff333333"));

				   
				   l.addView(ll);
				   
			   }
			   else l.addView(llField,i);
			   
			   i++;
				
			   
			   
		   }
		  

		   
		  n=i;
		  
		if(elementsList.size()>1){
		  
			if(elementsList.get(n-1) instanceof TextView) {
				
				((TextView) elementsList.get(n-1)).setImeOptions(EditorInfo.IME_ACTION_DONE);
				
			
			}
			
		}
		
		//if altitude is not visible get need fieldId
		if(altitudeTv==null){
			
			altitudeFieldId=rsC.getFieldIdByName(projId, "altitude");
			
		}
		
		   citCnt.EndTransaction();
		   
	   }
    
    
    
    private OnClickListener removePicture = new OnClickListener() {

        public void onClick(View v) {

        	PhotoFieldForm photoField=photoFieldsList.get(v.getTag());
        	photoField.removePhoto();
 
        	PhotoUtils.removePhoto(Environment.getExternalStorageDirectory()+"/"+prefCnt.getDefaultPath()+"/Photos/"+fileName);
        	  
        	        	
        }
    };
    
    
    
    private OnClickListener takePicture = new OnClickListener() {

        public void onClick(View v) {
          
 	       	String projName=tvProjectName.getText().toString();
 	       	String currentTime = formatter.format(new Date());
 	       	projName=projName.replace(" ", "_");
 	       	
 	       	fileName = projName + currentTime + ".jpg";

 	       	prefCnt.setLastPhotoPath(fileName);
 	       	
 	       	lastPhotoField=(String) v.getTag();		
 	       	 	       	
 	       	//create parameters for Intent with filename
 	       	ContentValues values = new ContentValues();
 	       	values.put(MediaStore.Images.Media.TITLE, fileName);
 	       	
 	       	String imageDesc=String.format(getString(R.string.photoDescription),projName);
 	       	values.put(MediaStore.Images.Media.DESCRIPTION,imageDesc);
 	       	
 	       	//imageUri is the current activity attribute, define and save it for later usage (also in onSaveInstanceState)
 	       	photoPath=Environment.getExternalStorageDirectory() + "/zamiaDroid/Photos/"+fileName;
 	       	 	       	
         	File file = new File( photoPath );
 	   	    imageUri = Uri.fromFile( file );
 	       	
 	       	//create new Intent
 	       	Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
 	
 	   	    intent.putExtra( MediaStore.EXTRA_OUTPUT, imageUri );		                	
 	       	intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1);
 	       	startActivityForResult(intent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);

        }
        
    };
    
    
    private void checkAutoField(ProjectField att) {
    	
    	if(prefCnt.isAutoFieldEnabled(att.getName().toLowerCase())){
    		
    		att.setValue(prefCnt.getAutoFieldEnabled(att.getName().toLowerCase()));
    		
    	}	
    	
	}
    
    /*
     * This method sets the default spinner item using defValue parameter provided.
     * If the spinner doesn't contain the value, spinner is not modified. 
     * 
     */

	private int setDefaultSpinnerItem(Spinner e, String defaultValue, String[] items){
    
    	int n=items.length;
    	boolean found=false;
    	int pos=-1;
    	int i;
    	
    	for(i=0; i<n && !found;i++){
    		
    		if (items[i].compareTo(defaultValue)==0){ found=true; pos=i;}
    		
    	}
    	
    	if(found) e.setSelection(pos);
    	
    	return pos;
    	
    }
    

    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
       // super.onActivityResult(requestCode, resultCode, intent);
   	
       	
    	switch(requestCode) {

	    	case LOAD_REMOTE_TAB :
	
	    		elements = tC.fillData((AutoCompleteTextView) thElem);
	
	    		((AutoCompleteTextView) thElem).setAdapter(elements);
	
	    		((AutoCompleteTextView) thElem).setOnItemClickListener(synonimThListener);
	
	
	    		break;
	
	
	    	case 2 :
	
	    		//back from GPS Activity
	
	    		if(intent!=null){
	
	    			Bundle ext = intent.getExtras();
	
	    			lat= ext.getDouble("latitude");
	    			longitude= ext.getDouble("longitude");
	    			elevation= ext.getDouble("elevation");
	
	    			//it can be 0 and 0 when it's located in the south of nigeria. It has to be improved.
	    			if (lat==0 || longitude ==0){
	
	    				Toast.makeText(getBaseContext(), 
	    						getBaseContext().getString(R.string.gps_missing), 
	    						Toast.LENGTH_SHORT).show();
	
	
	    			}
	
	    			else{
	
	    				prefCnt.setGPSNeeded(true);
	    				updateDisplay();
	
	    			}
	
	    		}
	
	
	    		break;
	
	    	case 4 :
	
	    		//back from GPS Activity
	
	    		if(intent!=null){
	
	    			Bundle ext = intent.getExtras();
	
	    			int numSecCitations=ext.getInt("numSecCit");
	    			Long subProjId=ext.getLong("subProjId");
	
	    			sCLH.updateNumCitations(subProjId.intValue(), numSecCitations);
	
	    			TextView counter=sCLH.getSecLevelCounterByFieldId(subProjId.intValue());
	
	    			int numSec= sCLH.getSecLevelChildrenByFieldId(subProjId.intValue());
	
	    			counter.setText(""+numSec);
	
	
	    		}
	
	
	    		break;
	
	    	case 5 :
	
	    		//back from GPS Activity
	
	    		if(intent!=null){
	
	    			Bundle ext = intent.getExtras();
	
	    			int numSecCitations=ext.getInt("numSecCit");
	    			Long subProjId=ext.getLong("subProjId");
	
	    			sCLH.setNumCitations(subProjId.intValue(), numSecCitations);
	
	    			TextView counter=sCLH.getSecLevelCounterByFieldId(subProjId.intValue());
	
	    			int numSec= sCLH.getSecLevelChildrenByFieldId(subProjId.intValue());
	
	    			counter.setText(""+numSec);
	
	    		}
	
	
	    		break;
	
	    	case CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE :
	
	    		if (resultCode == RESULT_OK) {
	
	    			if(photoPath==null){
	
	    				String fileName=prefCnt.getLastPhotoPath();
	
	    				photoPath=Environment.getExternalStorageDirectory().toString();
	    				photoPath=photoPath + "/zamiaDroid/Photos/"+ fileName;
	
	    			}
	    			
	    			PhotoFieldForm photoFieldForm=photoFieldsList.get(lastPhotoField);
	    			photoFieldForm.addPhoto(photoPath);
   			
	    			
	    			break;
	
	
	    		} else if (resultCode == RESULT_CANCELED) {
	    			
	    			Utilities.showToast("Picture was not taken", this);
	    			
	    		} else {
	    			
	    			Utilities.showToast("Picture was not taken", this);
	    		}
	
	
	    		break;
	
	    	default:


  
        }
        

    }
    
}

    

    