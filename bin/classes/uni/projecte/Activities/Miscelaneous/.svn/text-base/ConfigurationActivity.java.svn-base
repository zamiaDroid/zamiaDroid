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


package uni.projecte.Activities.Miscelaneous;

import java.io.File;
import java.util.Locale;

import uni.projecte.R;
import uni.projecte.R.string;
import uni.projecte.R.xml;
import uni.projecte.dataTypes.Utilities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Environment;
import android.preference.CheckBoxPreference;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceChangeListener;
import android.preference.Preference.OnPreferenceClickListener;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.widget.Toast;
 
public class ConfigurationActivity extends PreferenceActivity {
	
	private SharedPreferences prefs;
	
        @Override
        protected void onCreate(Bundle savedInstanceState) {
                super.onCreate(savedInstanceState);
                               
    	        Utilities.setLocale(this);

                prefs = PreferenceManager.getDefaultSharedPreferences(this);
                
                
                addPreferencesFromResource(R.xml.preferences);
                
                PreferenceManager.setDefaultValues(this, R.xml.preferences, false);

                // Get the custom preference
                
                Preference customPref = findPreference("customPref");
                customPref
                                .setOnPreferenceClickListener(new OnPreferenceClickListener() {
 
                                        public boolean onPreferenceClick(Preference preference) {
                                                Toast.makeText(getBaseContext(),
                                                                getString(R.string.reset_values),
                                                                Toast.LENGTH_LONG).show();
                                           
                                                SharedPreferences.Editor editor = prefs
                                                                .edit();
                                                editor.putString("myCustomPref",
                                                                "The preference has been clicked");
                                                editor.commit();

                                                
                                                return true;
                                        }
 
                                });
                
                Preference gpsActivationPred = findPreference("GPSactivationPref");
                gpsActivationPred
                                .setOnPreferenceClickListener(new OnPreferenceClickListener() {
 
                                        public boolean onPreferenceClick(Preference preference) {
                                              
                                                showGPSConfigWindow();
                                                
                                                return true;
                                        }
 
                                });
                
                
                
                Preference langPref = findPreference("listPref");
                langPref.setOnPreferenceChangeListener(new OnPreferenceChangeListener() {

                    public boolean onPreferenceChange(Preference preference, Object newValue) {
                    	
                    	String localeName=(String)newValue;
                    	
                    	if(localeName.equals("ca")){
                    		
                        	preference.setSummary("Català");

                    		
                    	}
                    	else if (localeName.equals("es")){
                        	
                    		preference.setSummary("Castellano");
                    		
                    	}
                    	else if (localeName.equals("en")){
                    		
                        	preference.setSummary("English");

                    	}
                    	
                    	
                      	SharedPreferences.Editor editor = prefs.edit();
                    	editor.putString("listPref",(String)newValue);
                    	editor.commit();
                    
                    	
                    	backActivity();
                    	
                    	return true;
                    	
                    }
                    	
                    });
                
                
                Preference usernamePref = findPreference("usernamePref");
            	//usernamePref.setSummary(usernamePref.get);

                usernamePref.setOnPreferenceChangeListener(new OnPreferenceChangeListener() {

                    public boolean onPreferenceChange(Preference preference, Object newValue) {
                    	
                    	preference.setSummary((String)newValue);
                    	
                     	SharedPreferences.Editor editor = prefs.edit();
                    	editor.putString("usernamePref",(String)newValue);
                    	editor.commit();
                    	
                    	
                    	return false;
                    	
                    }
                    	
                    });
                
                
                Preference geoidGPSPref = findPreference("geoidGPSCorrection");
                geoidGPSPref.setOnPreferenceChangeListener(new OnPreferenceChangeListener() {

                    public boolean onPreferenceChange(Preference preference, Object newValue) {
                    	
                    	
                    	
                    	if(!isNumeric((String)newValue)){
                    		
                    		Utilities.showToast("Double value needed", getBaseContext());
                    		
                    	}
                    	else{
                    		
                          	preference.setSummary((String)newValue+" (m)");
                        	
                         	SharedPreferences.Editor editor = prefs.edit();
                        	editor.putString("geoidGPSCorrection",(String)newValue);
                        	editor.commit();
                    		
                    	}
                    	
              
                    	return true;
                    	
                    }
                    	
                    });
                
                
                Preference pathThPref = findPreference("urlThPref");
                pathThPref.setOnPreferenceChangeListener(new OnPreferenceChangeListener() {

                    public boolean onPreferenceChange(Preference preference, Object newValue) {
                    	
                    	preference.setSummary((String)newValue);
                    	SharedPreferences.Editor editor = prefs.edit();
                    	editor.putString("urlThPref",(String)newValue);
                    	editor.commit();
                    	
                    	return false;
                    	
                    }
                    	
                    });
                
                
                Preference coordPref = findPreference("listPrefCoord");
                
                
                coordPref.setOnPreferenceChangeListener(new OnPreferenceChangeListener() {

                    public boolean onPreferenceChange(Preference preference, Object newValue) {
                    	
                    	preference.setSummary((String)newValue);
                    	SharedPreferences.Editor editor = prefs.edit();
                    	editor.putString("listPrefCoord",(String)newValue);
                    	editor.commit();
                    	
                    	return false;
                    	
                    }
                    	
                    });
                
                
                final CheckBoxPreference pref = (CheckBoxPreference) findPreference("secondaryStorageEnabled");  
                final Preference prefPath = (Preference) findPreference("secondaryExternalStoragePath");
                pref.setOnPreferenceClickListener(new OnPreferenceClickListener() {

                    public boolean onPreferenceClick(Preference preference) {
                    	
                       	prefPath.setEnabled(pref.isChecked());
                       	SharedPreferences.Editor editor = prefs.edit();
                    	editor.putBoolean("secondaryStorageEnabled",pref.isChecked());
                    	editor.commit();
                    	
                        return true; 
                        
                    }

				
                });
                
                
                final Preference secPathPref = findPreference("secondaryExternalStoragePath");
                secPathPref.setOnPreferenceChangeListener(new OnPreferenceChangeListener() {

                    public boolean onPreferenceChange(Preference preference, Object newValue) {

                    	String path=(String)newValue;
                    	
                    	if(pathExists(path)){
                    	
                    		secPathPref.setSummary(path);
	                    	SharedPreferences.Editor editor = prefs.edit();
	                    	editor.putString("secondaryExternalStoragePath",path);
	                    	editor.commit();
	                    	
	                    	return true;

                    	
                    	}
                    	else{
                    		
                    		Utilities.showToast(getString(R.string.photoPathWrong), getBaseContext());
                    		
                    		return false;
                    		
                    	}
                    	
                    	
                    }
                    	
                    });

                
                
        }
        
        protected boolean pathExists(String path) {

        	File f=new File(path);
        	return f.exists();
        	
		}

		private void backActivity(){
        	
        	finishActivity(3);
        	
        	
        }
        
        public static boolean isNumeric(String str)  
        {  
          try{  
            
        	  double d = Double.parseDouble(str);  
          }  
          catch(NumberFormatException nfe){  
        	  
            return false;  
            
          }  
          return true;  
        }

        
        protected void refreshWindow(String localName) {
        	

        	Locale locale = new Locale(localName);
            Locale.setDefault(locale);
            Configuration config = new Configuration();
            config.locale = locale;
            getBaseContext().getResources().updateConfiguration(config, getBaseContext().getResources().getDisplayMetrics());
				
        	setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
      	    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR);

		}

		@Override
		public void onResume() {
    		super.onResume();
    		
    		
    		SharedPreferences prefs=PreferenceManager.getDefaultSharedPreferences(this);
    		
    		Preference pathStoragePref = findPreference("secondaryExternalStoragePath");

    		if(prefs.getBoolean("secondaryStorageEnabled", false)){
    			
    			pathStoragePref.setSummary(prefs.getString("secondaryExternalStoragePath", Environment.getExternalStorageDirectory().toString()));
    			pathStoragePref.setEnabled(true);

    		}
    		else{
    			
    			pathStoragePref.setEnabled(false);
    			
    		}
    		
    		if(prefs.getString("listPrefCoord", "UTM") != null){
    			
   			 Preference coordPref = findPreference("listPrefCoord");
   			 coordPref.setSummary(prefs.getString("listPrefCoord", "UTM"));
   			
    		}
        		
    		if(prefs.getString("geoidGPSCorrection", "49") != null){
    			
   			 	Preference geoidPref = findPreference("geoidGPSCorrection");
   			 	geoidPref.setSummary(prefs.getString("geoidGPSCorrection", "49")+" (m)");
   			
    		}
    		
    		if(prefs.getString("listPref", "ca")!=null){
    			
    			String localeName=prefs.getString("listPref", "ca");
    			
    			 Preference listPref = findPreference("listPref");
    			
    			if(localeName.equals("ca")){
            		
                	listPref.setSummary("Català");

            		
            	}
            	else if (localeName.equals("es")){
                	
            		listPref.setSummary("Castellano");
            		
            	}
            	else if (localeName.equals("en")){
            		
            		listPref.setSummary("English");

            	}
    			
    			
    		}
    		
    		if(prefs.getString("urlThPref", "zamiaDroid") != null){
    			
    			 Preference pathPref = findPreference("urlThPref");
    			 pathPref.setSummary(prefs.getString("urlThPref", "zamiaDroid"));
    			
    		}
    		
    		if(prefs.getString("usernamePref", "empty") != null){
    			
	   			 Preference usernamePref = findPreference("usernamePref");
	   			 usernamePref.setSummary(prefs.getString("usernamePref", "default"));
   			
    		}
    		
    		
    	}
        
        protected void showGPSConfigWindow() {
        	
        	Intent settingsIntent = new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            settingsIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
            this.startActivityForResult(settingsIntent, 0);
			
		}

		public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
            Preference pref = findPreference(key);

            if (pref instanceof ListPreference) {
                ListPreference listPref = (ListPreference) pref;
                pref.setSummary(listPref.getEntry());
            }
        }

}

	 
   
 