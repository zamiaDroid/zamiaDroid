package uni.projecte.Activities.Thesaurus;

import java.util.ArrayList;
import java.util.HashMap;

import uni.projecte.R;
import uni.projecte.R.array;
import uni.projecte.R.id;
import uni.projecte.R.layout;
import uni.projecte.R.string;
import uni.projecte.controler.PreferencesControler;
import uni.projecte.controler.ThesaurusControler;
import uni.projecte.dataLayer.ThesaurusManager.RemoteThHandler;
import uni.projecte.dataLayer.ThesaurusManager.RemoteThesaurus;
import uni.projecte.dataLayer.ThesaurusManager.RemoteThesaurusListAdapter;
import uni.projecte.dataLayer.ThesaurusManager.ThesaurusDownloader.ThAsyncDownloader;
import uni.projecte.dataTypes.Utilities;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Spinner;


public class ThesaurusRemoteImport extends Activity {
	

	private Spinner spThGroups;
	private ListView lvThPool;
	private ProgressBar progBar;
	private ProgressDialog pd;

	private ArrayList<RemoteThesaurus> thPoolList;
	private RemoteThesaurusListAdapter rtAdapter;
	
	private String thName;
	private long thId;
		
	private String thPoolLocale;
	
	private PreferencesControler pC;
	
	private RemoteThesaurus rTh;
	private ThesaurusControler thCnt;
	
	private HashMap<String, String> remoteLocalTh;
	private boolean dontAskThName=false;
	private String lang;
	
	
	
	   @Override
	public void onCreate(Bundle state) {
	        super.onCreate(state);
	        
	        Utilities.setLocale(this);

	        setContentView(R.layout.thesaurus_import_remote);
	       
	        spThGroups=(Spinner)findViewById(R.id.spThGroups);
	        progBar=(ProgressBar)findViewById(R.id.progBarLoading);
	        lvThPool=(ListView)findViewById(R.id.lvRemoteThPool);
	        
			spThGroups.setOnItemSelectedListener(spListener);
			
			pC=new PreferencesControler(this);
			thCnt=new ThesaurusControler(this);
			
			/* pair list thRemoteId and lastUpdate */
			remoteLocalTh=thCnt.getRemoteThUpdatedStatus();
			
			PreferencesControler prefCnt=new PreferencesControler(this);
			
			lang=prefCnt.getLang();
			   
	   }
	   
	   
	   private void loadThPool(String poolId){
		   
		   progBar.setVisibility(View.VISIBLE);
		   
				RemoteThHandler rThHandler=new RemoteThHandler();
				thPoolList=rThHandler.getThPoolList(poolId,lang);
				   
				rtAdapter=new RemoteThesaurusListAdapter(this, thPoolLocale,thPoolList, downloadThListener,remoteLocalTh);
				lvThPool.setAdapter(rtAdapter);	
			
			progBar.setVisibility(View.INVISIBLE);
		   
	   }
	   
	   
	    private OnClickListener downloadThListener = new OnClickListener() {
			
			public void onClick(View v) {
				
				rTh=thPoolList.get(v.getId());
				
				if(v.getTag().equals("update")){
					
					thName=thCnt.removeThOverwrite(rTh.getThId());
					
					if(thName!=null){
						
						dontAskThName=true;
				        
					}
					
				}
					
			        new ThAsyncDownloader(v.getContext(),postThDownloadHandler).execute(rTh.getUrl(),Environment.getExternalStorageDirectory()+"/"+pC.getDefaultPath()+"/Thesaurus/",rTh.getThId()+".xml");
					
				
			}
	
	    };
	   

		private void createThDialog(final String path,final String fileName,final String source) {
	        
		  	final Dialog dialog = new Dialog(this);
	    	   	
	        dialog.setContentView(R.layout.thesaurus_creation_dialog);
	    	dialog.setTitle(R.string.thName);
	    	   	
	    	Button createProject = (Button)dialog.findViewById(R.id.bCreateTh);
	    		
	    	EditText name=(EditText)dialog.findViewById(R.id.etNameItem);
	    	name.setText(fileName);
	    	   	
	    	createProject.setOnClickListener(new Button.OnClickListener(){
	    	    	             
	    	    	
	    	   	public void onClick(View v)
	    	          {

	    	                 EditText et=(EditText)dialog.findViewById(R.id.etNameItem);
	    	    	                 
	    	                 thName=et.getText().toString();
	    	                 
	    	    	           
   	    	                 ThesaurusControler thCntr= new ThesaurusControler(v.getContext());
  	    	                 thId=thCntr.createThesaurus(thName,fileName,getPoolId(),source,"remote");
	    	    	                 
	    	    	                 if(thId>0){
	    	    	                	 
	    	    	                     importTh(path);
		    	    	                 dialog.dismiss();
		    	    	                 
	    	    	                 }
	    	    	                 else{
	    	    	                	 
		    	    	                String sameTh=getBaseContext().getString(R.string.sameThName);

	    	    	                 	Toast.makeText(getBaseContext(), 
			    	    	   		              sameTh+" "+thName, 
			    	    	   		              Toast.LENGTH_LONG).show();
	    	    	                	 
	    	    	                 }
	    	    	                 
	    	    	              }
	    	    	             
	    	    });
	    	    
	    	    dialog.show();


	    	 
	    } 
	   
		 private void importTh(final String filePath) {
			 
			 pd = ProgressDialog.show(this, getString(R.string.thLoading), getString(R.string.thLoadingTxt), true,false);
			 
			                 Thread thread = new Thread(){
			  	        	   
			  	        	   
				                 @Override
								public void run() {
				               	  				               	  
				                	importThThread(filePath); 
				               	  
				                 }
				           };
				           
				           
			   thread.start();
			 
			}
		 
		 private void importThThread(String filePath){
			 	 
			 ThesaurusControler thCntr= new ThesaurusControler(this);
			 
			 thName=thName.replace(".", "_");

			 boolean error=thCntr.addThItems(thId,thName, filePath);

			 if(!error) handler.sendEmptyMessage(0);
			 else handler.sendEmptyMessage(1);

			 
		 }
		 
		 private Handler postThDownloadHandler = new Handler(){
			 
			 @Override
			public void handleMessage(Message msg){
				 
				 Bundle b=msg.getData();
				 
				 if(b!=null){
					 
					 String path=Environment.getExternalStorageDirectory()+"/"+pC.getDefaultPath()+"/Thesaurus/"+b.getString("filePath");
					 
					 if(!dontAskThName) createThDialog(path,rTh.getThId(),rTh.getThSource()); 
					 else{
						 
						createThWithoutDialog(path,rTh.getThId(),rTh.getThSource());
						 
					 }
					 
				 }
				 
			 }
			 
		 };
		 
		 private Handler handler = new Handler() {

				@Override
				public void handleMessage(Message msg) {
					
					
					
					pd.dismiss();
					
					if (msg.what==0){
						
						Intent intent = new Intent();
				    	
						Bundle b = new Bundle();
						b.putString("thName", thName);
									
						intent.putExtras(b);

						setResult(1, intent);
						
					}
					
					else{
						
					  	Toast.makeText(getBaseContext(), 
	  	   		              getBaseContext().getString(R.string.thWrongFile), 
	  	   		              Toast.LENGTH_LONG).show();
						
					}
					
			
			        finish();


				}
			};
		 
		 	   
	   
	   private OnItemSelectedListener spListener = new OnItemSelectedListener() {

			public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,long arg3) {
	
				loadThPool(getPoolId());
			}
	
			public void onNothingSelected(AdapterView<?> arg0) {
				
				
				
			}
		   
	};


	protected String getPoolId() {

		String poolId = getResources().getStringArray(R.array.thesaurusFilumsEnglish)[spThGroups.getSelectedItemPosition()];
		thPoolLocale=spThGroups.getSelectedItem().toString().replace("+ ", "");
		
		return poolId;
		
	}


	protected void createThWithoutDialog(String path, String thRemoteId,String thSource) {
		
        ThesaurusControler thCntr= new ThesaurusControler(this);
        
          thId=thCntr.createThesaurus(thName,thRemoteId,getPoolId(),thSource,"remote");
                 
                 if(thId>0){
                	 
                     importTh(path);
                 }
                 
	}
	
	
}
