package uni.projecte.maps;

import uni.projecte.R;
import uni.projecte.controler.PreferencesControler;
import android.app.Dialog;
import android.content.Context;
import android.os.Handler;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.RadioButton;
import android.widget.RadioGroup;



public class MapConfigurationDialog {
	
	private static Dialog mapConfigDialog;
	
	
	  public static Dialog initDialog(Context context,PreferencesControler pC, boolean compassEnabled, Handler handlerUpdateConf) {
		  
		    mapConfigDialog  = new Dialog(context);
		    mapConfigDialog.setCancelable(true);
		    mapConfigDialog.setTitle(R.string.mapConfigTitle);
		 
		    mapConfigDialog.setContentView(R.layout.citationmapconfig);
		    mapConfigDialog.show();
		    
		    		    
		    handleEvents(context,pC,compassEnabled,handlerUpdateConf);

		    return mapConfigDialog;
		    
		  }
	  
	  private static void handleEvents(final Context context, final PreferencesControler pC, boolean compassEnabled, final Handler handlerUpdateConf) {
		  
		  RadioGroup rdgCategory = (RadioGroup)mapConfigDialog.findViewById(R.id.radioGroupUTMPrec);
		  CheckBox cbShowCompass = (CheckBox)mapConfigDialog.findViewById(R.id.cbShowCompass);
		  CheckBox cbShowMyTracks = (CheckBox)mapConfigDialog.findViewById(R.id.cbShowMyTracks);
		  CheckBox cbShowElevation = (CheckBox)mapConfigDialog.findViewById(R.id.cbShowAltitude);
		  CheckBox cbChangeMarker = (CheckBox)mapConfigDialog.findViewById(R.id.cbChangeMapMarkers);
		  
		  cbShowCompass.setChecked(compassEnabled);
		  cbShowElevation.setChecked(pC.isMapElevationShown());
		  
		  setChosenUTMPrec(rdgCategory,pC.getUTMDisplayPrec());
		  

          RadioGroup.OnCheckedChangeListener rdGrpCheckedListener = new RadioGroup.OnCheckedChangeListener(){

           public void onCheckedChanged(RadioGroup group, int checkedId) {

                  switch (checkedId){
                  
	                  case R.id.rb10km:
	                	  
	                	  pC.setUTMDisplayPrec("10km");
	                      break;
	                      
	                  case R.id.rb1km:
	
	                	  pC.setUTMDisplayPrec("1km");
	                      break;
	                      
	                  case R.id.rb1m:
	
	                	  pC.setUTMDisplayPrec("1m");
	                      break;
	                      
	                  default:
	
	                      break;
	                  }
                  
                  handlerUpdateConf.sendEmptyMessage(0);
                  
                  
              }
          };

          rdgCategory.setOnCheckedChangeListener(rdGrpCheckedListener);

          
          
          cbShowCompass.setOnCheckedChangeListener(new OnCheckedChangeListener() {

				public void onCheckedChanged(CompoundButton buttonView,boolean isChecked) {
	
					
	               if(isChecked) handlerUpdateConf.sendEmptyMessage(1);
	               else handlerUpdateConf.sendEmptyMessage(0);
	               
	               mapConfigDialog.dismiss();
					
				}
				
				
        	  }); 
          
          cbShowElevation.setOnCheckedChangeListener(new OnCheckedChangeListener() {

				public void onCheckedChanged(CompoundButton buttonView,boolean isChecked) {
	
					
	               if(isChecked) handlerUpdateConf.sendEmptyMessage(3);
	               else handlerUpdateConf.sendEmptyMessage(4);
	               
	               mapConfigDialog.dismiss();
					
				}
				
				
      	  }); 
          
          cbShowMyTracks.setOnCheckedChangeListener(new OnCheckedChangeListener() {

				public void onCheckedChanged(CompoundButton buttonView,boolean isChecked) {
	
					handlerUpdateConf.sendEmptyMessage(2);
	               
	               mapConfigDialog.dismiss();
					
				}
				
				
      	  }); 

          cbChangeMarker.setOnCheckedChangeListener(new OnCheckedChangeListener() {

 				public void onCheckedChanged(CompoundButton buttonView,boolean isChecked) {
 	
 					handlerUpdateConf.sendEmptyMessage(5);
 	               
 	               mapConfigDialog.dismiss();
 					
 				}
 				
 				
       	  });
		  
	  }

	private static void setChosenUTMPrec(RadioGroup rdgCategory,String utmDisplayPrec) {

		if(utmDisplayPrec.equals("10km")){
			
			RadioButton rb=(RadioButton) rdgCategory.findViewById(R.id.rb10km);
			rb.setChecked(true);
			
			
		}
		else if(utmDisplayPrec.equals("1km")){
			
			RadioButton rb=(RadioButton) rdgCategory.findViewById(R.id.rb1km);
			rb.setChecked(true);
			
		}
		else{
			
			RadioButton rb=(RadioButton) rdgCategory.findViewById(R.id.rb1m);
			rb.setChecked(true);
			
		}
		
		
	}

}
