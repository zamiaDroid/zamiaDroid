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

import uni.projecte.R;
import uni.projecte.Activities.RemoteDBs.TaxonExplorer;
import uni.projecte.R.id;
import uni.projecte.R.layout;
import uni.projecte.R.string;
import uni.projecte.controler.DataTypeControler;
import uni.projecte.controler.PreferencesControler;
import uni.projecte.controler.ProjectControler;
import uni.projecte.controler.CitationSecondLevelControler;
import uni.projecte.controler.ThesaurusControler;
import uni.projecte.controler.ProjectSecondLevelControler;
import uni.projecte.dataLayer.RemoteDBManager.BiocatDBManager;
import uni.projecte.dataLayer.ThesaurusManager.ListAdapters.ThesaurusAutoCompleteAdapter;
import uni.projecte.dataLayer.utils.PhotoUtils;
import uni.projecte.dataTypes.ProjectField;
import uni.projecte.dataTypes.Utilities;
import uni.projecte.hardware.gps.MainLocationListener;
import uni.projecte.maps.UTMDisplay;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
import android.provider.MediaStore.Images.ImageColumns;
import android.provider.MediaStore.MediaColumns;
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
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import edu.ub.bio.biogeolib.CoordConverter;
import edu.ub.bio.biogeolib.CoordinateLatLon;
import edu.ub.bio.biogeolib.CoordinateUTM;

public class SecondLevelSampling extends Activity {

	
	/*dataBase helpers*/
	   private ProjectControler projCnt;
	   private  ThesaurusControler tC;
	   private PreferencesControler pC;

	   public final static int SUCCESS_RETURN_CODE = 1;
	   public static final int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 3;
	   public static final int ENABLE_GPS=Menu.FIRST;
	   public static final int SHOW_TAXON_INFO=Menu.FIRST+1;

	   
	   private SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd_hhmmss");
	   
	   private long projId=-1;
	   private long subProjId=-1;
	   private String subLevelTag;
	   
	   private int subFieldsCount=0;
	   
	   private  PreferencesControler pm;
	   private TextView rsNameTV;
	   
	   private TextView mLocationDisplay;
	   private TextView mAltitudeDisplay;
	   private TextView mLocationLabel;

	   
	   private String thName;
	   private String projType;
	   
	   
	   /*top date Display*/
	   private TextView mDateDisplay;
	    private int mYear;
	    private int mMonth;
	    private int mDay;


		private ArrayList<View> elementsList;
		private int n;

		
    	private Double lat;
    	private Double longitude;
    	private double elevation;
    	
    	private ImageView photoTV;
    	private ImageButton rmPhotoButton;
    	private EditText etPhotoPath;
    	private String fileName;
    	private Uri imageUri;
    	private String _path;
    	
    	private ImageButton photoButton;
    	private Button bUpdateLoc;
    	
    	private boolean predefinedLocation=false;
    	
    	private TextView altitudeTv;
    	private AutoCompleteTextView thElem;
    	
    	int registeredCitations;
    	
    	private Hashtable<String, Integer> complexValuesList;
    	
    	private LocationManager mLocationManager;
    	private MainLocationListener mLocationListener;

    	private boolean tempGPS=false;

    	

		
    @Override
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        Utilities.setLocale(this);
        setContentView(R.layout.secondlevelsampling);
        
        /*
         * 
         * Paràmetres necessaris
         * 
         */
        
        
        /* projId  */
        
        projId=getIntent().getExtras().getLong("id");
        
        /* fieldId */
        
        subProjId=getIntent().getExtras().getLong("subProjId");
        
        
        
        subLevelTag=getIntent().getExtras().getString("subLevelTag");
        
        
        Utilities.showToast(subLevelTag, this);
        
        /*
         * 
         * Funcionalitats
         * 
         */
        
        /* comptar iteracions */
        
        
        
        /*DB access*/
        
        projCnt=new ProjectControler(this);
        tC= new ThesaurusControler(this);
        pC=new PreferencesControler(this);

        
        
        /* button Listeners*/ 
        
        Button bCreateSample = (Button)findViewById(R.id.bCreateSample);
        bCreateSample.setOnClickListener(bCreateCitationListener);
        
        Button bFinishSample = (Button)findViewById(R.id.bFinishSampling);
        bFinishSample.setOnClickListener(bFinishSamplingListener);
        
        
        bUpdateLoc = (Button)findViewById(R.id.bUpdateLocation);
        bUpdateLoc.setOnClickListener(bUpdateLocationListener);
        
        /* instances of main view elements */
        
        mDateDisplay = (TextView) findViewById(R.id.dateDisplay);
        mLocationDisplay = (TextView) findViewById(R.id.tvLocation);
        mLocationLabel = (TextView) findViewById(R.id.tvCoordinatesLabel);
        rsNameTV = (TextView)findViewById(R.id.projectName);

        mAltitudeDisplay = (TextView) findViewById(R.id.tvAltitude);
       
        
        projCnt.loadProjectInfoById(projId);
        	
      //  rsNameTV.setText(rsCont.getName());
        
        rsNameTV.setText(subLevelTag);
        
        
        pm= new PreferencesControler(this);


        lat=getIntent().getExtras().getDouble("latitude");
        longitude=getIntent().getExtras().getDouble("longitude");

        if(lat==0.0 && longitude==0.0){
        	
        	lat=null;
        	longitude=null;
        }
        
        
        //we'll create a citation with a pre-setted location
        if(lat!=null && longitude!=null){
        	
        	predefinedLocation=true;
        	bUpdateLoc.setVisibility(View.INVISIBLE);
        	
        	
        }
        else{
        	
        	  if(pm.gpsNeeded()){
        	        
              	callGPS();
              
              }
        
        }
        
        if(pm.isUTM()){
        	
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
	
			mLocationListener = new MainLocationListener(mLocationManager,handler,pC.getGeoidCorrection());
				
			mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, mLocationListener);
			
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
	    	                
	    	        	   pm.setGPSNeeded(false);

	    	                
	    	           }
	    	       });
	    	
	    	AlertDialog alert = builder.create();
	    	alert.show();

	}

    }
    
    
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
    	
		if(pm.gpsNeeded()) menu.add(0, ENABLE_GPS, 0,R.string.mDesactivateGPS).setIcon(android.R.drawable.ic_menu_mylocation);
		else menu.add(0, ENABLE_GPS, 0,R.string.mActivateGPS).setIcon(android.R.drawable.ic_menu_mylocation);

		menu.add(0, SHOW_TAXON_INFO, 0,R.string.mShowTaxonInfo).setIcon(android.R.drawable.ic_dialog_info);
		
    	return super.onCreateOptionsMenu(menu);
    }

	

    
    @Override
	public boolean onOptionsItemSelected(MenuItem item) {
    	
    	
		switch (item.getItemId()) {
			case ENABLE_GPS:
				
				if(pm.gpsNeeded()){
					
					pm.setGPSNeeded(false);
					
					
				} 
				else{
					
					pm.setGPSNeeded(true);
					callGPS();
					
				}
				 			 
			break;
			
			
			case SHOW_TAXON_INFO:
				
				if (!emptyThesaurus()){
	        	    
	       	    	 Toast.makeText(getBaseContext(), 
	       	                R.string.fieldMissing, 
	       	                 Toast.LENGTH_SHORT).show();
       	    	
				}
				else{
					
				
					
					 boolean thStatus=tC.initThReader(thName);
					 
					 if(thStatus){
						 
						 String codiOrc=tC.loadRemoteNameCode(thElem.getText().toString());
						 
						 if(codiOrc!=null && !codiOrc.equals("")){
							 
							 
							 loadBiologicalRemoteCard(thElem.getText().toString());
						 
						 }
						 
						 else{
							 
							 Toast.makeText(getBaseContext(), 
				       	               R.string.toastTaxonNotExists, 
				       	                 Toast.LENGTH_SHORT).show();
							 
							 
						 }
						 
						 
						 tC.closeThReader();
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
    
    
    /*
     * When backButton is pressed a dialog is shown showing an alert.
     * The alert advices the user that it will lose data if it finishes the activity.
     * 
     * @see android.app.Activity#onKeyDown(int, android.view.KeyEvent)
     */

 
    @Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK)) {
    		
        	
        	AlertDialog.Builder builder = new AlertDialog.Builder(this);
	    	
	    	builder.setMessage(R.string.backFromCitationMessage)
	    	       .setCancelable(false)
	    	       .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
	    	           public void onClick(DialogInterface dialog, int id) {
	    
	    	        	   pm.setGPSNeeded(true);
	    	        	   
	    	        	   Intent intent = new Intent();
	    	   	    	
	    					Bundle b = new Bundle();
	    					b.putInt("numSecCit", registeredCitations);
	    					
	    					intent.putExtras(b);
	    					
	    					b = new Bundle();
	    					b.putLong("subProjId", subProjId);
	    								
	    					intent.putExtras(b);

	    					setResult(4, intent);
	    	        	   
	    					finish();
        	   
	    	           }

					
	    	       })
	    	       .setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
	    	           public void onClick(DialogInterface dialog, int id) {
	    	                
	    	        	   
	    	        	   dialog.dismiss();
 
	    	           }
	    	       });
	    	
	    	AlertDialog alert = builder.create();
	    	
	    	alert.show();
        		
	        return true;

        	
        }
        
        return false;

    }
    
    
    private void loadBiologicalRemoteCard(String codiOrc){

    	
 		BiocatDBManager dbL=new BiocatDBManager(this);
 		dbL.getFilum(projType);
 		String filum=dbL.getFilumLetter();

 	   
 	   if(codiOrc!=null && !codiOrc.equals("") && filum!=null){

 			    
	  			String url=dbL.getBiologicalCardUrl(filum, codiOrc);
		   		 
				Intent intent = new Intent(getBaseContext(), TaxonExplorer.class);
				intent.putExtra("url", url);
				
				startActivityForResult(intent,0);
 		   		 
 			
 		}

 	   
 	   
    }


    
    /*
     * It takes the date of the system and location and shows it.
     * 
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
                                    .append(mMonth + 1).append("-")
                                    .append(mDay).append("-")
                                    .append(mYear).append(" ")
                                   /* .append(" - ")
                                    .append(pad(mHour)).append(":")
                                    .append(pad(mMinute))*/);
                        
                        
                        
                        
                        if(lat==null || longitude==null){
                        	
                    	//	pm.setGPSNeeded(false);

                 	    	 //mLocationDisplay.setText(R.string.gps_disabled);
                 	    	 
                    		mLocationDisplay.setText("");
                    		
                 	    	 bUpdateLoc.setVisibility(View.INVISIBLE);

                        	
                        }
                        else{
                        	
                        	   if(pm.isUTM()){
                      	    	 
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
                        	   
                        	   mAltitudeDisplay.setText(""+(int)elevation);
                        	   
                        	 if(altitudeTv!=null) altitudeTv.setText(""+(int)elevation);
                        	
                   	    	if(!predefinedLocation) bUpdateLoc.setVisibility(View.VISIBLE);
                   	    	
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
        		 
        			if(rsNameTV.length()==0){
        	    		
        	    		Toast.makeText(getBaseContext(), 
        	                    R.string.projNameMissing, 
        	                    Toast.LENGTH_SHORT).show();
        	    	}
        	    	
        	    	else  {
        	    		
        	    		if(pC.gpsNeeded()&& tempGPS){
    	    				
    	    				
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
     * Listener of the citation creation button.
     * 
     * The thesaurus is mandatory. When no geo-location button is check, GPS activity won't be called. 
     * 
     */
    
    private OnClickListener bFinishSamplingListener = new OnClickListener()
    {
        public void onClick(View v)
        {                        
            
        	
        	finishActivity();
        	  
        	
        }
    }; 
    
    
    /*
     * Method called when finish button is pressed.
     * It only finishes the Activity
     * 
     */
    
    private void finishActivity(){
    	
        
   	   Intent intent = new Intent();
	    	
		Bundle b = new Bundle();
		b.putInt("numSecCit", registeredCitations);
		
		intent.putExtras(b);
		
		b = new Bundle();
		b.putLong("subProjId", subProjId);
					
		intent.putExtras(b);

        setResult(4, intent);
   			
        finish();
    	
    	
    }
    
    private OnClickListener bUpdateLocationListener = new OnClickListener()
    {
        public void onClick(View v)
        {                        
             
            if(pm.gpsNeeded()){
                
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
        		 
        			if(rsNameTV.length()==0){
        	    		
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
					
					((TextView) et).setText("");
				}
				else if(et instanceof Spinner){
					
					int pos=complexValuesList.get(et.getTag());
					
					if(pos>-1) ((Spinner) et).setSelection(pos);
					
				}
				
				else if(et instanceof CheckBox){

					((CheckBox) et).setChecked(false);

				}
				
			}
			
			 if(photoTV!=null) {
				 
		         	photoTV.setVisibility(View.GONE);

                	photoButton.setEnabled(true);
                	rmPhotoButton.setEnabled(false);
                					 

			 }
			
   		   

    }
    
    /*
     * This method takes the values filled by the user in the form and store them into the DB
     * For each type of field the retrieval of the value will be different
     * 
     */
    
    private void addFieldValues(long idSample, CitationSecondLevelControler smplCntr){
    			
			n=elementsList.size();
			
			smplCntr.startTransaction();
			
			
			for (int i=0;i<n;i++){
				
				String value="";
				String label="";
				
				View et=elementsList.get(i);
								
				if (et instanceof EditText){
					
					value= ((TextView) et).getText().toString();
					label= ((TextView) et).getTag().toString();
					
				}
				
				else if(et instanceof CheckBox){
					
					if (((CheckBox) et).isChecked()) value="true";
					else value="false";
					
				}
				
				else {
					
					value =((Spinner) et).getSelectedItem().toString();
					label= ((Spinner) et).getTag().toString();

					
				}
				
				
					/*if value is empty we don't add the value into the database*/
			/*		if(value.compareTo("")==0){
						
					}
					
					else{
						*/
					
				smplCntr.addCitationField(subProjId,idSample,et.getId(),label, value);
	
						
/*					} */

			}
			
			
		//	smplCntr.addObservationAuthor(projId,idSample);

			smplCntr.EndTransaction();

    	
    }
    
    /*
     * 
     * This method stores the citation in the DB. Getting location and projectId
     * It also calls the addFildValues method for the insertion of the filled fields.
     * 
     */
    
    
    private long createSample(double latPoint, double longPoint){
    	
    
	    CitationSecondLevelControler sampleCntr=new CitationSecondLevelControler(this);
	    Log.d("SecondLevelCitations","New SC");

        		
    	// Sample Creation with lat,long, comment and rsId		
       long idSample=sampleCntr.createCitation(subLevelTag, latPoint, longPoint, "",projId,"secondLevel");
        
	    Log.d("SecondLevelCitations","CreateCitation");

        
        ((Vibrator)getSystemService(VIBRATOR_SERVICE)).vibrate(300);
        
        addFieldValues(idSample, sampleCntr);
          
	    Log.d("SecondLevelCitations","Fields Added to "+subLevelTag);

        
       	Toast.makeText(getBaseContext(), 
                getString(R.string.bSuccessCitationCreation), 
                Toast.LENGTH_SHORT).show();
       	
	//    Log.d("SecondLevelCitations","CitationCreated:"+rsNameTV.getText().toString()+":"+idSample+"|"+latPoint+":"+longPoint);

       	
       	tempGPS=true;
	  	
       	removeAttributes();
       	
        if(predefinedLocation) {
		    	
		    	finish();
		    	
		    }
		    
		    else{


			   if(pm.gpsNeeded()){
			        
		           	updateDisplay();

		        	callGPS();
		        
		        }

		
			 
			   
			 elementsList.get(0).requestFocus();
		 
		 }
       	       	
       	registeredCitations++;
	    
	    return -1;
      // 	return idSample;
   
    
    }
    
    private OnItemClickListener synonimThListener = new OnItemClickListener()
    {
        public void onItemClick(AdapterView<?> parent, View view, int position, long id)
        {
        	
        	if(pm.getSynonymCheck()){
        	
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
    
    private void createFieldForm(long projId){
    	
    	
    	//list where items will the hold
    	
		  elementsList=new ArrayList<View>();
		  complexValuesList= new Hashtable<String, Integer>();

		   
		  DataTypeControler dtH=new DataTypeControler(this);

		  LinearLayout lPhoto=null;
		   
		   //main layout that will include the form
		   LinearLayout l= (LinearLayout)findViewById(R.id.fieldsLay);

		   
		   ProjectSecondLevelControler rsC=new ProjectSecondLevelControler(this);
		   
		   
		   rsC.loadProjectInfoById(projId);
		   
		   thName=rsC.getThName();
		   projType=rsC.getProjType();
		   
		   //we load the Th reader that will help to create the autocomplete view with the items of the provided thName.

		   boolean thStatus=tC.initThReader(thName);
		   
		   ArrayList<ProjectField> projFieldList=rsC.getProjFields(subProjId);
		   
		   Iterator<ProjectField> it=projFieldList.iterator();
		   
		   int i=0;
  
		   
		   //for each field we will create the TextView with the label and an "special" view where the user will provide the field value
		   

		   
		  while(it.hasNext()){
			  

			   LinearLayout lp=new LinearLayout(this);

			   TextView t=new TextView(getBaseContext());
			   
			   ProjectField att=it.next();
			   
			   String fieldType =att.getType();
			   
			   String fieldLabel= att.getLabel();
			   t.setText(fieldLabel);

			   lp.addView(t);
			   lp.setPadding(4, 4, 4, 4);

			   Log.i("SLS",att.getId()+":"+"attName"+att.getName()+":attLabel"+att.getLabel());

			   //when the field is Simple we create an EditText		   
			   
			   if (/*fieldType.equals("Real") || fieldType.equals("Enter") |*/ fieldType.equals("simple") || fieldType.equals("number") /*|| fieldType.equals("string") || fieldType.equals("enter") || fieldType.equals("Text") ||  fieldType.equals("text") || fieldType.equals("String")*/){
				   
				   EditText e=new EditText(getBaseContext());	   
				   
				   int idD= (int) att.getId();
				   String tag=att.getName();
				   
				   if(att.getValue()!=null && att.getValue().length()>0) {
					   
					   e.setText(att.getValue());
					
				  }

				   e.setTag(tag);
				   e.setId(idD);
				   
				   if(fieldType.equals("number")){
					   
					   e.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
					   
				   }
				   else{
					   
					   e.setImeOptions(EditorInfo.IME_ACTION_NEXT);

					   
				   }


				   e.setLayoutParams(new LayoutParams
					        (ViewGroup.LayoutParams.FILL_PARENT,ViewGroup.LayoutParams.
					                WRAP_CONTENT));
				  lp.addView(e);
				   elementsList.add(e);

				   
			   }
			   
			   //when the field is boolean we create a CheckBox
			   
			   else if (fieldType.equals("boolean")){
				   
				   CheckBox e=new CheckBox(getBaseContext());	   		   
				   int idD= (int) att.getId();
				   e.setId(idD);
				   
				   lp.addView(e);
				   elementsList.add(e);
			   }
			   
			   	//when the field is photo we show photo Interface
			   
			   else if (fieldType.equals("photo")){
				   
				   ImageView e=new ImageView(getBaseContext());
				   EditText et= new EditText(getBaseContext());
				   
				   
				  // et.setText(getText(R.string.photoMissing));
				   
				   int idD= (int) att.getId();
				   
				   photoTV=e;
				   
				   et.setId(idD);
				 
				   et.setTag(att.getName());
				   				   
				   etPhotoPath=et;
				   
				  et.setImeOptions(EditorInfo.IME_ACTION_NEXT);
				  
				  et.setEnabled(false);

				 
				   photoButton=new ImageButton(getBaseContext());
				   photoButton.setImageResource(android.R.drawable.ic_menu_camera);
				   
				   rmPhotoButton=new ImageButton(getBaseContext());
				   rmPhotoButton.setImageResource(android.R.drawable.ic_menu_delete);
				   
				   rmPhotoButton.setEnabled(false);

				   
				   rmPhotoButton.setOnClickListener(new OnClickListener() {

		                public void onClick(View v) {
		                  
		                	etPhotoPath.setText("");
		         
		                	boolean deleted=PhotoUtils.removePhoto(Environment.getExternalStorageDirectory()+"/"+pm.getDefaultPath()+"/Photos/"+fileName);
		                	  
		                	if(deleted) Utilities.showToast("Fotos esborrada", v.getContext());
		                	
		                	photoTV.setVisibility(View.GONE);

		                	photoButton.setEnabled(true);
		                	rmPhotoButton.setEnabled(false);

		                }
				   });

				   photoButton.setOnClickListener(new OnClickListener() {

		                public void onClick(View v) {
		                  
		                	String rsName=rsNameTV.getText().toString();
		                	String currentTime = formatter.format(new Date());
		                	rsName=rsName.replace(" ", "_");
		                	
		                	fileName = rsName+currentTime+".jpg";
		                	
		                	//create parameters for Intent with filename
		                	ContentValues values = new ContentValues();
		                	values.put(MediaColumns.TITLE, fileName);
		                	values.put(ImageColumns.DESCRIPTION,"Image capture by camera");
		                	
		                	//imageUri is the current activity attribute, define and save it for later usage (also in onSaveInstanceState)
		                	_path=Environment.getExternalStorageDirectory() + "/zamiaDroid/Photos/"+fileName;

		                  	File file = new File( _path );
	                	    imageUri = Uri.fromFile( file );
		                	
		                	//create new Intent
		                	Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
		 
	                	    intent.putExtra( MediaStore.EXTRA_OUTPUT, imageUri );		                	
		                	intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1);
		                	startActivityForResult(intent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);

		                }
		        });
				   
				   lp.addView(et);

				   lPhoto=new LinearLayout(this);
				   lPhoto.setOrientation(LinearLayout.HORIZONTAL);
				   
				   LinearLayout lButtons=new LinearLayout(this);
				   lButtons.setOrientation(LinearLayout.VERTICAL);
				   
				   lButtons.addView(photoButton);
				   lButtons.addView(rmPhotoButton);
				   
				   lPhoto.addView(lButtons);

				   lPhoto.addView(e);
		
				  // lp.addView(e);
				   elementsList.add(et);
			   
			   }
			   
			   //when the field is a thesaurus we create an AutoCompleteView and we fill it with the items provided by the ThesaurusControler
			   
			   else if(fieldType.equals("thesaurus")){
				   
				   String tag=att.getName();
				   
				   View e;
				   
				   if(thStatus){
					   
					   e=new AutoCompleteTextView(getBaseContext());	   

					   ThesaurusAutoCompleteAdapter elements = tC.fillData((AutoCompleteTextView)e);

					  ((AutoCompleteTextView) e).setAdapter(elements);
					  
					  ((AutoCompleteTextView) e).setOnItemClickListener(synonimThListener);
					  thElem=((AutoCompleteTextView) e);

					   
				   }
				   else{
					   
					   e=new EditText(getBaseContext());
					   
					   
				   }
				   
				   ((TextView) e).setImeOptions(EditorInfo.IME_ACTION_NEXT);

				   
				   
					  ((TextView) e).setHint(getString(R.string.thHint));
	  
					  e.setTag(tag);

				      
				      int idD= (int) att.getId();
					   e.setId(idD);
					   
				      
					   e.setLayoutParams(new LayoutParams
						        (ViewGroup.LayoutParams.FILL_PARENT,ViewGroup.LayoutParams.
						                WRAP_CONTENT));
					   
					  lp.addView(e);
					   elementsList.add(e);
			   }
			   
			   
			   /* ojuuuuuu secondLevel no hauria de funcionar */
			   else if(fieldType.equals("secondLevel")){
				   
				   
				   Button showList=new Button(getBaseContext());
				   showList.setText(this.getString(R.string.slShowElem));
				   
				   Button addSubFields=new Button(getBaseContext());
				   addSubFields.setText(this.getString(R.string.slAddElem));

				   EditText et= new EditText(getBaseContext());
				   et.setText("amb # ítems");
				   
				   int idD= (int) att.getId();
				   et.setId(idD);
				   et.setTag(att.getName());
				   
				  et.setImeOptions(EditorInfo.IME_ACTION_NEXT);
				 

				   showList.setOnClickListener(new OnClickListener() {

		                public void onClick(View v) {
		                 
		                	Utilities.showToast("ShowList", v.getContext());

		                }
				   });
				   
				   addSubFields.setOnClickListener(new OnClickListener() {

		                public void onClick(View v) {
		                  
		                	Utilities.showToast("Add Subfields", v.getContext());

		                }
				   });
				   
				   
				   lp.addView(et);
				   
				   LinearLayout ll= new LinearLayout(this);
				   ll.setOrientation(LinearLayout.HORIZONTAL);
				   
				   ll.addView(showList);
				   ll.addView(addSubFields);
				  
				   l.addView(ll);
		
				  // lp.addView(e);
				   elementsList.add(et);
				   
			
			   }
			   
			 else{
				   
					
				 String[] items=dtH.getItemsbySecondLevelFieldId(att.getId());
				 
				 Spinner e=new Spinner(this);
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

						   
						  lp.addView(e);
						   elementsList.add(e);


	   
			   }
			   
			   if (fieldType.equals("photo")){ 
				   
				   LinearLayout ll= new LinearLayout(this);
				   ll.setOrientation(LinearLayout.VERTICAL);
				   ll.setPadding(3,3,3,3);

				   ll.addView(lp);
				   ll.addView(lPhoto);
				   ll.setBackgroundColor(0xff333333);
				   l.addView(ll);
			   }
			   else l.addView(lp,i);
			   
			   i++;
				
			   
			   
		   }
		  

		   
		  n=i;
		  
		if(elementsList.size()>1){
		  
			if(elementsList.get(n-1) instanceof TextView) ((TextView) elementsList.get(n-1)).setImeOptions(EditorInfo.IME_ACTION_DONE);
			
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
    

    @Override
	protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
       // super.onActivityResult(requestCode, resultCode, intent);
   	
       	
        switch(requestCode) {
        case 0 :
            
        	//back from research choice
        	
        /*	nomEstudi = extras.getString(KEY_NOM);
            idRs=extras.getLong(KEY_ID);
            desc=extras.getString(DESCRIPCIO);

            txtName.setText(nomEstudi);*/
            
            break;
            
            
        case 1 :
        	
        	//this case is not used because the UI was redesigned and attribute creation was mixed with sample capturing
        	//back from filled attributes
        	
           
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
		        		
		        		pm.setGPSNeeded(true);
		
		        		 updateDisplay();
		        		
		        		
		        	}
        	
        	   }
        	
        	
        	break;
        	
        	
       //back from Picture 	
        case CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE :
        	
        	 if (resultCode == RESULT_OK) {
        		   
        		 
        		 etPhotoPath.setText(_path);
        		 
        		  BitmapFactory.Options options = new BitmapFactory.Options();
        		    options.inSampleSize = 5;
        		    


        		    Bitmap PhotoFromCamera = BitmapFactory.decodeFile( _path, options );

    				MediaStore.Images.Media.insertImage(getContentResolver(), PhotoFromCamera, "fileName", "");


        		 
        		/* Bundle b = intent.getExtras();
        			if (b != null && b.containsKey(MediaStore.EXTRA_OUTPUT)) { // large image?
        		        
        		       	Toast.makeText(getBaseContext(), 
        		                "Large Image", 
        		                Toast.LENGTH_SHORT).show();
        		       	
        			        // Shouldn't have to do this ... but
        				
        				
        			} else {
        				
        				Toast.makeText(getBaseContext(), 
        		                "Small Image", 
        		                Toast.LENGTH_SHORT).show();
        				
        				
        				MediaStore.Images.Media.insertImage(getContentResolver(), thumbnail, timestamp, timestamp);
        			}*/
        			
    				photoButton.setVisibility(View.INVISIBLE);
        			photoTV.setImageBitmap(PhotoFromCamera);
        		
        	        photoTV.setScaleType(ScaleType.CENTER);
        	        
        
        			
        			
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

    

    