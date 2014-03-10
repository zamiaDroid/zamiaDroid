package uni.projecte.dataLayer.ThesaurusManager.ThesaurusDownloader;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import uni.projecte.R;
import uni.projecte.controler.PreferencesControler;
import uni.projecte.controler.ThesaurusControler;
import uni.projecte.dataTypes.Utilities;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.text.format.DateFormat;
import android.util.Log;
import android.widget.Toast;

public class ThUpdater {

	private Context baseContext;
	private String thTag;
	private long thId;
	private String thName;
	private String lastUpdated;
	private ThesaurusControler thCnt;
	private PreferencesControler pC;
	
	private static String urlBDBCFlora="http://biodiver.bio.ub.es/biocat/servlet/biocat.ExportaThesaurusServlet?F12.0%25tesa=tax%25downloadthesa=true%25format=XML%25sinonims=true";
    private static String bdbcFloraUpdated="2013-03-26 10:14:00";
            	
	private ProgressDialog pd;
	private Handler removeUpdateDialog;
	
	public ThUpdater(Context context, String thTag){
		
		this.baseContext=context;
		this.thTag=thTag;
		
		thCnt=new ThesaurusControler(baseContext);
		pC= new PreferencesControler(baseContext);
		
		thName=thCnt.getThByType("bdbc_Flora");
		
		if(!thName.equals("")) lastUpdated=thCnt.getLastUpdate();
				
	}


	
	public void update_bdb_Flora_Th(Handler removeUpdateDialog){
		
		this.removeUpdateDialog=removeUpdateDialog;
		
		thName=thCnt.removeThOverwrite(thTag);
	
		new ThAsyncDownloader(baseContext,postThDownloadHandler).
		
		execute(urlBDBCFlora,Environment.getExternalStorageDirectory()+"/"+pC.getDefaultPath()+"/Thesaurus/",
				thId+".xml");
		
	}

	 private Handler postThDownloadHandler = new Handler(){
		 
		 @Override
		public void handleMessage(Message msg){
			 
			 Bundle b=msg.getData();
			 
			 if(b!=null){
				 
				 String path=Environment.getExternalStorageDirectory()+"/"+pC.getDefaultPath()+"/Thesaurus/"+b.getString("filePath");
			
				 createThWithoutDialog(path,"bdbc_Flora","bdbc");
				 
			 }
			 
		 }
		 
	 };
	 
		private void createThWithoutDialog(final String path, String thRemoteId,String thSource) {
			
	         thId=thCnt.createThesaurus(thName,thRemoteId,"Flora",thSource,"remote");
	            
	         
	         if(thId>0){
	               	 
	        	 pd = ProgressDialog.show(baseContext, baseContext.getString(R.string.thLoading), baseContext.getString(R.string.thLoadingTxt), true,false);
				 
                 Thread thread = new Thread(){
  	        	   
  	        	   
	                 @Override
					public void run() {
	               	  				               	  
	                	importThThread(path); 
	               	  
	                 }
	           };
	           
	           
	           thread.start();
	        	 
	        	 
	         }
	                 
		} 

		 
		 private void importThThread(String filePath){
			 	 
			 ThesaurusControler thCntr= new ThesaurusControler(baseContext);
			 
			 thName=thName.replace(".", "_");

			 boolean error=thCntr.addThItems(thId,thName, filePath);

			 if(error) {
				 
					Toast.makeText(baseContext, 
	  	   		              baseContext.getString(R.string.thWrongFile), 
	  	   		              Toast.LENGTH_LONG).show();
				 
			 }
			 
			 pd.dismiss();
			 removeUpdateDialog.sendEmptyMessage(0);
			 
		 }
		 
	
		 
	public String getThTag() {
		return thTag;
	}



	public void setThTag(String thTag) {
		this.thTag = thTag;
	}



	public String getThName() {
		return thName;
	}



	public void setThName(String thName) {
		this.thName = thName;
	}



	public String getLastUpdated() {
		return lastUpdated;
	}



	public void setLastUpdated(String lastUpdated) {
		this.lastUpdated = lastUpdated;
	}



	public boolean isOutdated() {
	    
	    try {
	    	
	    	Date currentDate =new SimpleDateFormat("yyyy-MM-dd kk:mm:ss",Locale.ENGLISH).parse(bdbcFloraUpdated);
			Date dateCreated=new SimpleDateFormat("yyyy-MM-dd kk:mm:ss",Locale.ENGLISH).parse(lastUpdated);
						
			Log.i("Th","ThDate: "+dateCreated.toLocaleString()+" UpdateDate: "+currentDate.toLocaleString());
			
			if(currentDate.after(dateCreated)) return true;
			
			
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    
	
		
		return false;
	}
	
	
}
