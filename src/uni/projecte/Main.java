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

package uni.projecte;


import java.io.File;
import java.util.Locale;

import uni.projecte.Activities.Citations.CitationManager;
import uni.projecte.Activities.Citations.Sampling;
import uni.projecte.Activities.Maps.CitationMap;
import uni.projecte.Activities.Miscelaneous.ConfigurationActivity;
import uni.projecte.Activities.Miscelaneous.GalleryGrid;
import uni.projecte.Activities.Projects.ProjectManagement;
import uni.projecte.controler.BackupControler;
import uni.projecte.controler.PreferencesControler;
import uni.projecte.controler.ProjectControler;
import uni.projecte.dataLayer.ThesaurusManager.ThesaurusDownloader.ThUpdater;
import uni.projecte.dataTypes.Utilities;
import uni.projecte.hardware.gps.MainLocationListener;
import uni.projecte.maps.geocoding.Geocoder;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.text.Html;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;



public class Main extends Activity {
    /** Called when the activity is first created. */
		
	long idSample=-1;
	long predPrefId;
	
	
	public static final int CONFIGURATION = Menu.FIRST;
	public static final int PROJECT_CHOOSE = Menu.FIRST+1;
	public static final int ABOUT_US = Menu.FIRST+2;
	public static final int BACKUP = Menu.FIRST+3;

	public static final String PREF_FILE_NAME = "PredProject";
	private SharedPreferences preferences;
	private SharedPreferences configPrefs;
	private PreferencesControler prefCnt;
	
	private String projName;
	private String locality_auto="";
	
	private LocationManager mLocationManager;
	private MainLocationListener mLocationListener;
	
	private LinearLayout llLocality;
	private TextView tvLocality;
	
	private ThUpdater thUpdater;
	private Dialog updateFlora;
	
	
	
	 
    @Override
	public void onCreate(Bundle savedInstanceState) {
    	
    	super.onCreate(savedInstanceState);
    	

		prefCnt= new PreferencesControler(this);
        Utilities.setLocale(this);

    	setContentView(R.layout.main);
        
      
        //we get all 6 buttons and create their listeners
        //each listener will create an Intent for changing the activity
        
        Button btnSampleCreate = (Button)findViewById(R.id.sampling);
        btnSampleCreate.setOnClickListener(createCitationListener);
        
        Button btnSincro = (Button)findViewById(R.id.sincro);
        btnSincro.setOnClickListener(citationManagerListener);
      
        Button btProjCreate = (Button)findViewById(R.id.bCrearEstudi);
        btProjCreate.setOnClickListener(projectManagerListener);

        Button btShowMap = (Button)findViewById(R.id.bShowMapMain);
        btShowMap.setOnClickListener(bShowMapListener);
        
        Button btShowGallery = (Button)findViewById(R.id.btShowGalleryMain);
        btShowGallery.setOnClickListener(showGalleryListener);
        
        Button btShowConfig = (Button)findViewById(R.id.btConfigMain);
        btShowConfig.setOnClickListener(showConfigListener);
        
        tvLocality=(TextView)findViewById(R.id.tvLocality);
        llLocality=(LinearLayout)findViewById(R.id.llLocality);    
        
		configPrefs=PreferenceManager.getDefaultSharedPreferences(this);
   
        createFolderStructure();
        
        if(prefCnt.isFirstRun()) createFistExecutionDialog();
        
        prefCnt= new PreferencesControler(getApplicationContext());
		prefCnt.setAutoField("locality", "");

		if(prefCnt.isFirstUpdate()){
		
			BackupControler bc= new BackupControler(this);
			bc.copyProjects();
			
			
			prefCnt.setFirstUpdate(false);
		
		}
		
		if(prefCnt.isSecondUpdate()){
			
			BackupControler bc= new BackupControler(this);
			
			bc.clearTH();
			
			prefCnt.setSecondUpdate(false);
		
		}
		
		updateFloraThDialog();
		

    }
    

    
    private void loadLocalLocale() {

    	
    	String localName=prefCnt.getLang();
    	Locale locale = new Locale(localName);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        getBaseContext().getResources().updateConfiguration(config, getBaseContext().getResources().getDisplayMetrics());
    
        
	}



	@Override
	public void onResume(){
    	
    	super.onResume();
    	
    	preferences = getSharedPreferences(PREF_FILE_NAME, MODE_PRIVATE);
        predPrefId = preferences.getLong("predProjectId", -1);
    	
        TextView tvProjName= (TextView) findViewById(R.id.projectNameM);       
        tvProjName.setOnClickListener(changeProjListener);
        
        if(predPrefId==-1) {	
        	
        	tvProjName.setText(getString(R.string.noProjectChosen));
        
        }
        else {

        	ProjectControler projCnt= new ProjectControler(this);
        	long result=projCnt.loadProjectInfoById(predPrefId);
        	
        	if(result>-1){
        		
        		projName=projCnt.getName();
        		
            	tvProjName.setText(projName);
            	
            	Log.i("Project","Working with project: "+projName);
            		
        	}
        	
        	else{
        		
            	tvProjName.setText(getString(R.string.noProjectChosen));
                
    	        SharedPreferences.Editor editor = preferences.edit();
    	        editor.putLong("predProjectId", -1);
    	        editor.commit();
        		
        	}
        	
        }
        
		determineLocality();
    	
    	
    }
    
    
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
	
		menu.add(0, CONFIGURATION, 0,R.string.mConfiguration).setIcon(android.R.drawable.ic_menu_preferences);
		menu.add(0, PROJECT_CHOOSE, 0,R.string.mChooseProject).setIcon(android.R.drawable.ic_menu_agenda);
		menu.add(0, ABOUT_US, 0,R.string.mAboutUs).setIcon(android.R.drawable.ic_dialog_info);

    	return super.onCreateOptionsMenu(menu);
    }

    
    @Override
	public boolean onOptionsItemSelected(MenuItem item) {
    	
		switch (item.getItemId()) {
			
			case CONFIGURATION:
				
			       Intent settingsActivity = new Intent(getBaseContext(),
                           ConfigurationActivity.class);
			       startActivityForResult(settingsActivity,3);
				
				 			 
			break;
			
			case PROJECT_CHOOSE:
				
				int tabId;
	        	if(predPrefId<0) tabId=1;
	        	else tabId=0;
	        	
	    		Intent projActivity = new Intent(getBaseContext(),ProjectManagement.class);
	    		projActivity.putExtra("tab", tabId);
	        	startActivity(projActivity);
				
				
			break;
			
			case ABOUT_US:
				
				createAboutUsDialog();
				
			break;
			

				
		}
		
	
		return super.onOptionsItemSelected(item);
	}
    
   
   
	private OnClickListener changeProjListener = new OnClickListener()
    {
        public void onClick(View v)
        {                        
        	    	
        	int tabId;
        	if(predPrefId<0) tabId=1;
        	else tabId=0;
        	
    		Intent projActivity = new Intent(getBaseContext(),ProjectManagement.class);
    		projActivity.putExtra("tab", tabId);
        	startActivity(projActivity);
        	
        }
    };
    
    private OnClickListener showConfigListener = new OnClickListener() {
		
		public void onClick(View v) {

		       Intent settingsActivity = new Intent(getBaseContext(),
                       ConfigurationActivity.class);
		       startActivityForResult(settingsActivity,3);			
		}
	};
    
    private OnClickListener bShowMapListener = new OnClickListener()
    {
        public void onClick(View v)
        {                        
        	
        	if (predPrefId==-1){
        		
        		Intent projActivity = new Intent(getBaseContext(),ProjectManagement.class);
        		projActivity.putExtra("tab", 1);
            	startActivity(projActivity);
        		
        		
        	}
        	
        	else{
	        	
        		Intent myIntent = new Intent(v.getContext(), CitationMap.class);
	        	myIntent.putExtra("id", predPrefId);
	
	            startActivityForResult(myIntent, 0);
            
        	}
        	
        }
    };
    
    
    private OnClickListener showGalleryListener = new OnClickListener()
    {
        public void onClick(View v)
        {                        
        	
        	if (predPrefId==-1){
        		
        		Intent projActivity = new Intent(getBaseContext(),ProjectManagement.class);
        		projActivity.putExtra("tab", 1);
            	startActivity(projActivity);
        		
        		
        	}
        	
        	else{
	        	

     		   if(Utilities.isSdPresent()){
     			   
	       		    	Intent intent = new Intent(v.getContext(), GalleryGrid.class);
	 		 	       
	    	 			Bundle b = new Bundle();
	    	 			b = new Bundle();
	    	 			b.putLong("id", predPrefId);
	    	 			intent.putExtras(b);
    
	    	 			startActivity(intent);
     		   } 
     		   else {
			        	
			        	Toast.makeText(getBaseContext(), 
			                    R.string.noSdAlert, 
			                    Toast.LENGTH_SHORT).show();
			        	
			   }
        	}
            
        	
        }
    };
    
    private OnClickListener createCitationListener = new OnClickListener()
    {
        public void onClick(View v)
        {        
      
        	if(predPrefId==-1){
        		
        		Intent projActivity = new Intent(getBaseContext(),ProjectManagement.class);
        		projActivity.putExtra("tab", 1);
            	startActivity(projActivity);
        		
        		
        	}
        	else {
        		
        		Intent intent = new Intent(v.getContext(), Sampling.class);
        	       
	 			Bundle b = new Bundle();
	 			b = new Bundle();
	 			b.putLong("id", predPrefId);
	 			intent.putExtras(b);
	 			
	            startActivity(intent);

        	}
            
        	       	
                	
        }
    };
    
   
    
    private OnClickListener citationManagerListener = new OnClickListener()
    {
        public void onClick(View v)
        {                        
      
        	if(predPrefId==-1){
        		
        		Intent projActivity = new Intent(getBaseContext(),ProjectManagement.class);
        		projActivity.putExtra("tab", 1);
            	startActivity(projActivity);
        		
        		
        	}
        	else {
        		
        		Intent intent = new Intent(v.getContext(), CitationManager.class);        	       
	 			Bundle b = new Bundle();
	 			b = new Bundle();
	 			b.putLong("id", predPrefId);
	 			intent.putExtras(b);

	            startActivity(intent);

        	}
            
        	
        }
    }; 
    
    private OnClickListener projectManagerListener = new OnClickListener()
    {
        public void onClick(View v)
        {                        
        	
        	int tabId;
        	if(predPrefId<0) tabId=1;
        	else tabId=0;
        
        	Intent myIntent1 = new Intent(v.getContext(), ProjectManagement.class);
        	myIntent1.putExtra("tab", tabId);
        	myIntent1.setAction(Intent.ACTION_CONFIGURATION_CHANGED);
            startActivity(myIntent1);
        	
        }
    }; 
    
    
    private PackageInfo getPackageInfo() {
        PackageInfo pi = null;
        try {
             pi = getPackageManager().getPackageInfo(getPackageName(), PackageManager.GET_ACTIVITIES);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return pi;
    }
    
        
    private void determineLocality(){
    	
    	mLocationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE); 

    	if (mLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) && Utilities.isNetworkConnected(this)) {
    		
			mLocationListener = new MainLocationListener(mLocationManager,handler,prefCnt.getGeoidCorrection());
				
			mLocationManager.requestLocationUpdates(
		                LocationManager.GPS_PROVIDER, 0, 0, (LocationListener) mLocationListener);
			
    	}
    	else{
    		
    		llLocality.setVisibility(View.GONE);
    		
    	}
    	
    }
    
    
	private Handler handler = new Handler() {

	@Override
		public void handleMessage(Message msg) {
		
			double latitude = mLocationListener.getLatitude();
	   	    double longitude = mLocationListener.getLongitude();
	   	
			mLocationManager.removeUpdates(mLocationListener);
			
			String locality=Geocoder.reverseGeocode(latitude, longitude);
			
			locality_auto=locality.split(":")[0];
			tvLocality.setText(locality_auto);

			prefCnt.setAutoField("locality", locality_auto);

	    	
		}
	
	};

    
    private void createAboutUsDialog() {

    	final Dialog dialog;
    	PackageInfo versionInfo = getPackageInfo();
    	
    	dialog = new Dialog(this);
 	   	
    	dialog.setContentView(R.layout.main_aboutus);
    	dialog.setTitle(getString(R.string.aboutUs)+": ZamiaDroid");
   	  
    	TextView tvAboutUsExt=(TextView) dialog.findViewById(R.id.tvAboutUsExtended);
    	tvAboutUsExt.setText(Html.fromHtml(getString(R.string.aboutUsExtended)));
    	TextView tvVersion=(TextView) dialog.findViewById(R.id.tvZamiaDroidVersion);
    	tvVersion.setText(Html.fromHtml(String.format(getString(R.string.appVersion), versionInfo.versionName)));
    	
	    dialog.show();

	}

    
    private void updateFloraThDialog(){
    	
    	//check taxFlora
    	thUpdater= new ThUpdater(this, "bdbc_Flora");

    	String thName=thUpdater.getThName();

    	if( !thName.equals("") && thUpdater.isOutdated() ){

	    	    	
		    	updateFlora = new Dialog(this);
		    	updateFlora.requestWindowFeature(Window.FEATURE_NO_TITLE);
		    	updateFlora.setContentView(R.layout.thesaurus_update_dialog);
		    	
		    	TextView tv_info=(TextView) updateFlora.findViewById(R.id.tvThChangeTitle);
		    	
		    	String title_message=String.format(getString(R.string.thChangeTitle), thName,thUpdater.getLastUpdated());
		    	tv_info.setText(title_message);
		    	
		    	Button btThUpdate=(Button) updateFlora.findViewById(R.id.btUpdateTh);
		    	btThUpdate.setOnClickListener(updateTh);
		    	
		    	updateFlora.show();
		    	
	    	}
    	else{
    		
    		
    		
    	}
    	
    }
    
    public OnClickListener updateTh = new OnClickListener() {
		
		public void onClick(View v) {

			if(!Utilities.isNetworkConnected(v.getContext())){
				
				Utilities.showToast(v.getContext().getString(R.string.noThConnection), v.getContext());
				
			}
			else{
			
				thUpdater.update_bdb_Flora_Th(postThDownloadHandler);
			
			}
								
		}
	};
	
	 private Handler postThDownloadHandler = new Handler(){
		 
		 @Override
		public void handleMessage(Message msg){
			 
			 updateFlora.dismiss();
			 
		 }
		 
	 };
    
	private void createFistExecutionDialog() {

		final Dialog dialog;
		dialog = new Dialog(this);

		dialog.setContentView(R.layout.main_welcome_dialog);
		dialog.setTitle("ZamiaDroid");

		final SharedPreferences.Editor editor = configPrefs.edit();

		Button bStart = (Button) dialog.findViewById(R.id.bStart);
		Button languageList = (Button) dialog.findViewById(R.id.bChlanguage);

		final EditText etUserName = (EditText) dialog
				.findViewById(R.id.userName);
		etUserName.setImeOptions(EditorInfo.IME_ACTION_DONE);

		languageList.setOnClickListener(new Button.OnClickListener() {

			public void onClick(View v) {

				AlertDialog.Builder builder = new AlertDialog.Builder(v
						.getContext());

				builder.setTitle("Idiomes | Languages");

				final String[] languages = getResources().getStringArray(
						R.array.languages);

				builder.setSingleChoiceItems(languages, -1,
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int item) {

								String language = languages[item];

								if (language.equals("Castellano")) {

									editor.putString("listPref", "es");

								} else if (language.equals("English")) {

									editor.putString("listPref", "en");

								}

								else {

									editor.putString("listPref", "ca");

								}

								editor.commit();

								loadLocalLocale();

								refreshWelcomeScreen();

								dialog.dismiss();
							}
						});

				AlertDialog alert = builder.create();

				alert.show();

			}
		});

		bStart.setOnClickListener(new Button.OnClickListener() {

			public void onClick(View v) {

				String userName = etUserName.getText().toString();

				if (userName.equals("")) {

					Toast.makeText(getBaseContext(),
							v.getResources().getString(R.string.noUserName),
							Toast.LENGTH_SHORT).show();

				}

				else {

					editor.putString("usernamePref", userName);
					editor.commit();
					dialog.dismiss();
					prefCnt.setFirstRun(false);

				}

			}

		});

		dialog.show();

	}

	private void refreshWelcomeScreen() {

		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR);

	}
  
    private void createFolderStructure(){
    	
    	String path= prefCnt.getDefaultPath();

    	File f;

    	f = new File("/sdcard/"+path+"/");
		if(!f.exists())	f.mkdir();
		
		f = new File("/sdcard/"+path+"/Citations");
		if(!f.exists())	f.mkdir();
		
		f = new File("/sdcard/"+path+"/Thesaurus");
		if(!f.exists())	f.mkdir();

		f = new File("/sdcard/"+path+"/Projects");
		if(!f.exists())	f.mkdir();

		f = new File("/sdcard/"+path+"/Photos");
		if(!f.exists())	f.mkdir();

		f = new File("/sdcard/"+path+"/Backups");
		if(!f.exists())	f.mkdir();
		
		f = new File("/sdcard/"+path+"/Reports");
		if(!f.exists())	f.mkdir();
    }
    
    
    @Override
	protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        
    
        switch(requestCode) {
        
        case 0:
        	
        
        case 3 :

          	setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
      	    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR);
                        
            break;
            
                
            default:
            	
            	System.out.println("default");
     
        }
    }
    
    
}

    

    