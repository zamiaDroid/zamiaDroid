package uni.projecte.dataLayer.CitationManager.Synchro;

import uni.projecte.R;
import uni.projecte.Activities.Syncro.SyncroConfig;
import uni.projecte.dataTypes.Utilities;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.LinearLayout;
import android.widget.TextView;

public class SyncroUpdater {

	private Context baseContext;
	
	private LinearLayout llSyncro;
	private TextView tvSyncroInfo;

	private ProgressDialog progressBar;
    
	private SyncCitationManager synchroManager;
	
	private Handler parentHandler;
	
	
	public static final int STATE_SET_PROGRESS_MAX=0;
	public static final int STATE_SET_PROGRESS=1;
	public static final int STATE_FINISH_PROGRESS=2;


	public SyncroUpdater(Context baseContext, String service, LinearLayout llSyncro){
		
		this.baseContext=baseContext;
		this.llSyncro=llSyncro;
		
	    tvSyncroInfo=(TextView)llSyncro.findViewById(R.id.tvSyncroInfo);

        
		synchroManager = new SyncCitationManager(baseContext,service);

		
	}
	
	public SyncroUpdater(Context baseContext,  SyncCitationManager synchroManager){
		
		this.baseContext=baseContext;
	
		this.synchroManager = synchroManager;

		
	}
	
	/*
	 * 
	 * 
	 */

	public void startUpdate(final String projTag, Handler reloadListHandler){
		
		this.parentHandler=reloadListHandler;
		
		createProgressBar();

		if(synchroManager.isConfigured()){

			 new Thread(new Runnable() {
		            public void run() {
		            	
		            	synchroManager.getOutdatedLocalCitations(projTag,updateStateHandler);
		            	synchroManager.getOutdatedRemoteCitations(projTag,updateStateHandler);
		        
		            }
		        }).start();
			
		}
		else{
			
			Utilities.showToast(baseContext.getString(R.string.syncroUserPassword), baseContext);
				
			Intent intent = new Intent(baseContext, SyncroConfig.class);
            baseContext.startActivity(intent);
			
		}
		
	}
	
	
	
	private void createProgressBar(){
		
	      // prepare for a progress bar dialog
        progressBar = new ProgressDialog(baseContext);
        progressBar.setCancelable(true);
        progressBar.setMessage(baseContext.getString(R.string.syncroProjectProcess));
        progressBar.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progressBar.setProgress(0);
        progressBar.setMax(100);
        progressBar.show();
        
		
	}
	
	private Handler updateStateHandler = new Handler() {
        
		public void handleMessage(Message msg) {
			
			Bundle bundle = msg.getData();
            
			int state=bundle.getInt("state");
					
			
			switch(state) {
			
				case STATE_SET_PROGRESS:
					
					int progress=bundle.getInt("progress");
					progressBar.setProgress(progress);
					
					System.out.println("Progress "+progress);
					
					break;
					
				case STATE_SET_PROGRESS_MAX:
					
					int max=bundle.getInt("max");
					progressBar.setMax(max);
					
					System.out.println("Max "+max);
					
					break;	
					
				case STATE_FINISH_PROGRESS:
						
					progressBar.dismiss();
					
					if(tvSyncroInfo!=null) tvSyncroInfo.setText(baseContext.getString(R.string.syncroStatusUpdated)); 
					
					parentHandler.sendEmptyMessage(0);
					
					break;
						
				default:
					break;
				}
			
			}
           
    };

	
}
