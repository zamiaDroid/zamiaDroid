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

package uni.projecte.controler;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Environment;

public class PreferencesControler {

    private SharedPreferences settings;
    private Context baseContext;
	
	public PreferencesControler(Context c) {
		
		settings = c.getSharedPreferences("uni.projecte_preferences", 0);
		baseContext=c;
		
	}
	public boolean isUTM(){
		
	     String coorSystem = settings.getString("listPrefCoord", "UTM");
	     
	     if(coorSystem.equals("UTM")) return true;
	     else return false;
		
	}
	
	public boolean isLatLong(){
		
	   return !isUTM();
		
	}
	
	public void setTrackingService(boolean needed){
		
	    SharedPreferences.Editor editor = settings.edit();

	    editor.putBoolean("trackingService",needed);

	    editor.commit();
	   	    
		
		
	}
	
	public boolean getTrackingService(){
		
	   return settings.getBoolean("trackingService", false);
		
	}
	
	public void setShownMyTracksDialog(boolean shown){
		
	    SharedPreferences.Editor editor = settings.edit();

	    editor.putBoolean("shownMyTracksDialog",shown);

	    editor.commit();
		
	}
	
	public boolean isShownMyTracksDialog(){
		
		   return settings.getBoolean("shownMyTracksDialog", false);
			
	}
	
	
	public void setGPSNeeded(boolean needed){
		
	    SharedPreferences.Editor editor = settings.edit();

	    editor.putBoolean("gpsNeeded",needed);

	    editor.commit();
	   	    
		
		
	}
	
	public boolean gpsNeeded(){
		
	   return settings.getBoolean("gpsNeeded", true);
		
	}
	
	public void setFirstRun(boolean firstRun){
		
	    SharedPreferences.Editor editor = settings.edit();

	    editor.putBoolean("firstRun",firstRun);

	    editor.commit();
		
		
	}
	
	public boolean isFirstRun(){
		
	   return settings.getBoolean("firstRun", true);
		
	}
	
	public void setFirstUpdate(boolean firstUpdate){
		
	    SharedPreferences.Editor editor = settings.edit();

	    editor.putBoolean("firstUpdate",firstUpdate);

	    editor.commit();
		
		
	}
	
	public boolean isFirstUpdate(){
		
	   return settings.getBoolean("firstUpdate", true);
		
	}
	
	
	public void setSecondUpdate(boolean firstUpdate){
		
	    SharedPreferences.Editor editor = settings.edit();

	    editor.putBoolean("secondUpdate",firstUpdate);

	    editor.commit();
		
		
	}
	
	
	public boolean isSecondUpdate(){
		
	   return settings.getBoolean("secondUpdate", true);
		
	}
	
	public void setAddAuthor(boolean author){
		
	    SharedPreferences.Editor editor = settings.edit();

	    editor.putBoolean("author",author);

	    editor.commit();
		
		
	}
	
	public boolean isAddAltitude(){
		
	   return settings.getBoolean("altitude", true);
		
	}
	
	public void setAddAltitude(boolean altitude){
		
	    SharedPreferences.Editor editor = settings.edit();

	    editor.putBoolean("altitude",altitude);

	    editor.commit();
		
		
	}
	
	public double getGeoidCorrection(){
		
		double geoidCorrection=Double.valueOf(settings.getString("geoidGPSCorrection", "49"));
		
		   return geoidCorrection;
			
		}
		
		
	public void setGeoidCorrection(double geoidCorrection){
			
			String geoidCorrectionString=String.valueOf(geoidCorrection);
			
		    SharedPreferences.Editor editor = settings.edit();

		    editor.putString("geoidGPSCorrection",geoidCorrectionString);

		    editor.commit();
			
			
	}
	
	
	public void setUTMDisplayPrec(String prec){
		
		
	    SharedPreferences.Editor editor = settings.edit();

	    editor.putString("utmDispPrec",prec);

	    editor.commit();
		
		
	}
	
	public String getUTMDisplayPrec(){
		
		   return settings.getString("utmDispPrec", "1m");

		
	}
	
	public boolean isAddAuthor(){
		
	   return settings.getBoolean("author", true);
		
	}
	
	
	public String getDefaultPath(){
		
	     return settings.getString("urlThPref", "zamiaDroid");

		
	}
	
	public String getUsername(){
		
	     return settings.getString("usernamePref", "Unknown User");

		
	}
	
	public boolean getSynonymCheck(){
		
	     return settings.getBoolean("checkSynonymsPref", true);

		
	}
	
	public boolean isTaxonUpdate(){
		
	     return settings.getBoolean("updateGPSPref", true);

		
	}
	
	
	public String getLang(){
		
	     return settings.getString("listPref", "ca");

		
	}
	
	
	public void setAutoField(String fieldName, String value){

	    SharedPreferences.Editor editor = settings.edit();

		
	    if(fieldName.equals("locality")){
			
	    	editor.putString("prefAutoFieldLocalityValue",value);
			
		}
		else if(fieldName.equals("altitude")){
		    
			editor.putString("prefAutoFieldAltitudeValue",value);

		}
			
	    editor.commit();
	
	}
	
	public String getAutoFieldEnabled(String fieldName){
		
		if(fieldName.equals("locality")){
			
			return settings.getString("prefAutoFieldLocalityValue", ""); 
			
		}
		else if(fieldName.equals("altitude")){
			
			return settings.getString("prefAutoFieldAltitudeValue", ""); 

		}
		
		return "";
		
	}
	
	public boolean isAutoFieldEnabled(String fieldName){
		
		if(fieldName.equals("locality")){
			
			return settings.getBoolean("prefAutoFieldLocality", false); 
			
		}
		else if(fieldName.equals("altitude")){
			
			return settings.getBoolean("prefAutoFieldAltitude", false); 

		}
		
		return false;
		
	}
	public void setLastPhotoPath(String _path) {

	    SharedPreferences.Editor editor = settings.edit();
	    editor.putString("lastPhotoPath",_path);
	    		
	    editor.commit();
	}
	
	public String getLastPhotoPath(){
		
		return settings.getString("lastPhotoPath", ""); 

	}
	
	public void setMapElevationShown(boolean enabled) {

	    SharedPreferences.Editor editor = settings.edit();
	    editor.putBoolean("elevationShown",enabled);

	    editor.commit();
		
	}
	
	public boolean isMapElevationShown(){
		
		return settings.getBoolean("elevationShown", false); 

	}
	public boolean getSecondaryExternalStorage() {

		return settings.getBoolean("secondaryStorageEnabled",false); 

	}
	public String getSecondaryExternalStoragePath() {

		return settings.getString("secondaryExternalStoragePath", Environment.getExternalStorageDirectory().toString()); 
		
	}
	public String getReportPath() {

		return Environment.getExternalStorageDirectory()+"/"+getDefaultPath()+"/Reports/";
	}
	public String getBackupPath() {

		return Environment.getExternalStorageDirectory()+"/"+getDefaultPath()+"/Backups/";
	}
	
	public String getCitationsPath() {

		return Environment.getExternalStorageDirectory()+"/"+getDefaultPath()+"/Citations/";
	}

}
