package uni.projecte.maps;

import uni.projecte.R;
import uni.projecte.controler.PreferencesControler;
import uni.projecte.dataTypes.Utilities;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Color;
import android.os.Handler;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.apps.mytracks.content.MyTracksProviderUtils;
import com.google.android.apps.mytracks.content.Track;
import com.google.android.apps.mytracks.services.ITrackRecordingService;

public class MyTracksService {
	
	 private ITrackRecordingService myTracksService;
	 private MyTracksProviderUtils myTracksProviderUtils;
	 private Intent myTracksIntent;
	 
	 private static String tagName="MyTracks";
	 
	 private Context baseContext;
	 private long trackId=-1;
	 private Track lastTrack;
	 
	 private static String trackPrefix="zdt_";
	 private String projName;
	 
	 private Handler handlerAfterInit;
	 
	 
	 public MyTracksService(Context baseContext){
		 
		 this.baseContext=baseContext;
		 
		 myTracksProviderUtils = MyTracksProviderUtils.Factory.get(baseContext);
		    
		 myTracksIntent = new Intent();
		    
		 myTracksIntent.setComponent(new ComponentName(
		        baseContext.getString(R.string.mytracks_service_package),
		        baseContext.getString(R.string.mytracks_service_class)));

	 
	 }
	 
		public boolean initMyTracksService(Handler handlerAfterInit) {
			
			this.handlerAfterInit=handlerAfterInit;
			
			try{
			
				baseContext.startService(myTracksIntent);
				boolean activeService=baseContext.bindService(myTracksIntent, serviceConnection, 0);
				
				Log.i(tagName, "Init MyTracks Service "+activeService);
				
				return activeService;
			
			}
			catch(SecurityException e){
				
				return false;
			}
			
		
			
		}
		
		public boolean endMyTracksService(){
			
			if(myTracksService != null) baseContext.unbindService(serviceConnection);
			
			
			try{
				
				boolean stopped=baseContext.stopService(myTracksIntent);
				Log.i(tagName, "Stopped MyTracks Service "+stopped);
				   
				return stopped;
			
			}
			catch(SecurityException e){
				
				Log.i(tagName, "You do not have permission to stop the given service");
				
				return false;
			}
			
			
		}
		
		public long startTraking(){
			
			  try {
				  
				//if(myTracksService==null) initMyTracksService();  
	     		  
				if(myTracksService!=null){
					
					trackId=myTracksService.startNewTrack();
					Log.i(tagName, "Starting track "+trackId);
					
					return trackId;
					
				}
				else return -1;
	    
				
			} catch (RemoteException e) {

				e.printStackTrace();
				
				return -1;
				
			}
		}
		
		
		
		public boolean endTracking(String projName){
			
			try {
	  		  
				myTracksService.endCurrentTrack();
				
				Log.i(tagName, "Ending track "+trackId);

				lastTrack = myTracksProviderUtils.getLastTrack();
				
				if(lastTrack==null){
					
					return false;
					
				}
				else{
					
					storeTrackDialog(projName);
					return true;
					
				}
				
				
			}	 catch (RemoteException e) {

					e.printStackTrace();
				
				return false;
			}
			
		}
		
		public String getLoadedTrackName(long trackId){
			
			Track loadedTrack = myTracksProviderUtils.getTrack(trackId);
			
			if(loadedTrack!=null){
			
				String trackName=loadedTrack.getName();
				String projName=loadedTrack.getCategory();
				trackName=trackName.replace(trackPrefix+projName+"_", "");
				
				return trackName;

			}
			else{
				
				return "";
				
			}
			

			
		}
		
		public long getWorkingTrackId() {

			Track loadedTrack = myTracksProviderUtils.getLastTrack();
			
			if(loadedTrack==null) return -1;
			else return loadedTrack.getId();
			
		}
		
		
		  private void storeTrackDialog(final String projName) {

			  	this.projName=projName;
			  	
			  	String trackName=lastTrack.getName();
				
				if(lastTrack.getNumberOfPoints()==0){
					
					Utilities.showToast(baseContext.getString(R.string.trackWithoutLocations), baseContext);
					myTracksProviderUtils.deleteTrack(lastTrack.getId());
					
					handlerAfterInit.sendEmptyMessage(2);

				}
				else{
				
				  
					AlertDialog.Builder alert = new AlertDialog.Builder(baseContext);
	
					alert.setTitle(R.string.trackSaveQuestion);
					alert.setMessage(baseContext.getString(R.string.trackProjectNameDialog)+" "+trackPrefix+projName+"_"+trackName);
	
					// Set an EditText view to get user input 
					final EditText input = new EditText(baseContext);
					input.setText(trackName);
					
					alert.setView(input);
	
					alert.setPositiveButton(baseContext.getString(R.string.yes), new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int whichButton) {
					  
						String value = input.getText().toString();
					  	
						lastTrack.setName(trackPrefix+projName+"_"+value);
						lastTrack.setDescription(baseContext.getString(R.string.trackProjectDescription)+" "+projName);
						lastTrack.setCategory(projName);
					  	
					  	myTracksProviderUtils.updateTrack(lastTrack);
					  	
						  handlerAfterInit.sendEmptyMessage(3);

						
						
					  }
					});
	
					alert.setNegativeButton(baseContext.getString(R.string.no), new DialogInterface.OnClickListener() {
					  public void onClick(DialogInterface dialog, int whichButton) {
	
						  myTracksProviderUtils.deleteTrack(lastTrack.getId());
						  handlerAfterInit.sendEmptyMessage(2);

						  
					  }
					});
	
					alert.show();
				
				}
				    
			  
		}


		private ServiceConnection serviceConnection = new ServiceConnection() {
			    
			    public void onServiceConnected(ComponentName className, IBinder service) {
			    	 
			    	myTracksService = ITrackRecordingService.Stub.asInterface(service);
					
			    	Log.i(tagName, "On Service Connected");
					
					handlerAfterInit.sendEmptyMessage(1);
			    	
			    }

			    public void onServiceDisconnected(ComponentName className) {
			    	
			    	myTracksService = null;
					Log.i(tagName, "On Service Disconnected");
			    	
			    }
			
			  };


		public void showInfoDialog(boolean succed, final PreferencesControler pC) {

			    final Dialog dialog = new Dialog(baseContext);
			    
			    String lang=pC.getLang();

				dialog.setContentView(R.layout.map_mytracks_dialog);
				dialog.setTitle(baseContext.getString(R.string.tracksInfo));

				dialog.show();
					
				WebView webview = (WebView) dialog.findViewById(R.id.webviewMytracks);
				webview.loadUrl("file:///android_asset/my_tracks_"+lang+".html");
				final CheckBox cbMyTracks=(CheckBox)dialog.findViewById(R.id.cbMyTracksRepeatDialog);
				
				//myTracksMissing
				if(!succed){
					
					TextView tv= (TextView) dialog.findViewById(R.id.tvMyTracksInstalled);
					tv.setText(baseContext.getString(R.string.tvMyTracksNotInstalled));
					tv.setTextColor(Color.RED);
					
					ImageView iv=(ImageView)dialog.findViewById(R.id.ivMytracksInstalled);
					iv.setImageResource(R.drawable.cross);
					
					
				}
				
				Button accept=(Button) dialog.findViewById(R.id.btAccept);
				
		        accept.setOnClickListener(new View.OnClickListener() {
		             public void onClick(View v) {
		            	 
		            	 dialog.dismiss();
		            	 pC.setShownMyTracksDialog(cbMyTracks.isChecked());

		             }
		         });
		        
			
		}

		public String getProjName() {
			return projName;
		}

		public void setProjName(String projName) {
			this.projName = projName;
		}


	    

	

}
