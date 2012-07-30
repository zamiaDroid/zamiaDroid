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


package uni.projecte.Activities.Projects;

import java.io.File;
import java.util.ArrayList;

import uni.projecte.R;
import uni.projecte.R.id;
import uni.projecte.R.layout;
import uni.projecte.R.string;
import uni.projecte.controler.PreferencesControler;
import uni.projecte.controler.ThesaurusControler;
import uni.projecte.controler.ProjectZamiaControler;
import uni.projecte.dataLayer.ProjectManager.ListAdapters.RemoteProjectListAdapter;
import uni.projecte.dataLayer.ThesaurusManager.RemoteThHandler;
import uni.projecte.dataLayer.ThesaurusManager.ThesaurusDownloader.ThAsyncDownloader;
import uni.projecte.dataTypes.ProjectRepositoryType;
import uni.projecte.dataTypes.Utilities;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.text.Html;
import android.view.View;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;



public class ProjectRepositoryList extends Activity {
	
	private ProjectZamiaControler zpC;
	private ArrayList<ProjectRepositoryType> list;
	
	private ProgressDialog pd;
	private Dialog dialog;
	
	private ListView lV;
	private ListView localLV;
	
    public static final int DIALOG_DOWNLOAD_PROGRESS = 0;
	    
    private String projNameGlob;
    private String thNameGlob;
    private String prNameGlob;
    private String prType;
    private String thFilum;
    private String thSource;
    private String thName;
	
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
       
        Utilities.setLocale(this);
	    setContentView(R.layout.projectrepositorylist);	
     
	    //filling list with remote and local projects
	    lV= (ListView) findViewById(R.id.repProjList);
	    localLV=(ListView) findViewById(R.id.repProjListLocal);
	    
		zpC= new ProjectZamiaControler(this);
	
		loadRemoteProjects();
		loadLocalProjects();
		
		lV.setOnItemClickListener(theListListener);
        localLV.setOnItemClickListener(theLocalListListener);

	}
	
		private void loadLocalProjects() {

			ArrayList<ProjectRepositoryType> localProjects=new ArrayList<ProjectRepositoryType>();
						
			localProjects.add(new ProjectRepositoryType(getResources().getString(R.string.sampleProjectId), getResources().getString(R.string.sampleProjectName), getResources().getString(R.string.sampleProjectType), getResources().getString(R.string.sampleProjectDesc),"","","local"));

			RemoteProjectListAdapter eA=new RemoteProjectListAdapter(this,localProjects);
			localLV.setAdapter(eA);
			
	}


		private void loadRemoteProjects(){
			
			list=zpC.getZamiaProject();
			
			RemoteProjectListAdapter eA=new RemoteProjectListAdapter(this,list);		
			lV.setAdapter(eA);
					
		}
		
	/*
	 *	Remote Projects' ListListener  
	 * 
	 */
		
	  public OnItemClickListener theListListener = new OnItemClickListener() {
		    
		    public void onItemClick(android.widget.AdapterView<?> parent, View v, int position, long id) {
		        
			   	 TextView tvType=(TextView) v.findViewById(R.id.tvProjRepType);
			   	 TextView tvProjDesc=(TextView) v.findViewById(R.id.tvProjRepDesc);
			   	 TextView tvProjName=(TextView) v.findViewById(R.id.tvProjRepName);
  	 
			   	 String projName=(String)tvProjName.getTag();
			   	 thName=(String)tvType.getTag();
			   	 thFilum=(String)tvProjDesc.getTag();
			   	 
			   	 prType=(String)tvType.getText();
			   	 
			   	 thSource=list.get(position).getThSource();
			   	 
			   	 createProjectDialog(projName,thName,true);
			   	
		    }
	    };
	    
	    
		  public OnItemClickListener theLocalListListener = new OnItemClickListener() {
			    
			    public void onItemClick(android.widget.AdapterView<?> parent, View v, int position, long id) {
			        
				   	 TextView tv=(TextView) v.findViewById(R.id.tvProjRepName);
				   	 TextView tvProjType=(TextView) v.findViewById(R.id.tvProjRepType);
				   	
				   	 String projName=(String)tv.getTag();
				   	 prType=tvProjType.getText().toString();
				   	
				   	 createProjectDialog(projName,"",false);
				   				    	
			    }
		    };
		    
	    
	    private void createProjectDialog(final String prName, final String thName, final boolean remote) {
	        
	  	
	  		ThesaurusControler thCont= new ThesaurusControler(this);
  		        	
    	   	dialog = new Dialog(this);
        	dialog.setContentView(R.layout.projectcreationremoteth);
    	   	dialog.setTitle(R.string.insert_data);
    	   	
    	   	Button createProject = (Button)dialog.findViewById(R.id.bAddItem);
    	   	EditText name=(EditText)dialog.findViewById(R.id.etNameItem);

    	   	
    	   	final Spinner thList=(Spinner)dialog.findViewById(R.id.thList);
    	   	
    	   	String[] thArrayList=thCont.getThList();
    	   	
    	   	ArrayAdapter<String> dtAdapter = new ArrayAdapter<String>(this,
                    android.R.layout.simple_spinner_item,thArrayList);
        
 		   dtAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
 		   thList.setAdapter(dtAdapter);
 		   thList.setVisibility(View.GONE); 

    	 
    	   	name.setText(prName);
    	   	
    	    final RadioButton rbRemoteTh = (RadioButton) dialog.findViewById(R.id.rbRemoteTh);
    	    final RadioButton rbLocalTh = (RadioButton) dialog.findViewById(R.id.rbLocalTh);

    	    
    	    if(thName.equals("")){
    	    	
    	    	rbRemoteTh.setVisibility(View.GONE);
    	    	rbLocalTh.setVisibility(View.GONE);
    	    	
    	    }
    	    else{
    	    
    	    	//Linked thesaurus exists on the system
    	    	int found=Utilities.findString(thArrayList,thName);
    	    	
    	    	if(found>=0) {
    	    		
    	    		Utilities.setDefaultSpinnerItem(thList, thName, thArrayList);
        	    	rbRemoteTh.setEnabled(false);
        	    	rbLocalTh.setChecked(true);
        	    	thList.setVisibility(View.VISIBLE);

    	    	}
    	    	else{
    	    		
        	    	rbRemoteTh.setText(Html.fromHtml(rbRemoteTh.getText()+" <b>"+thName+"</b> "));
        	    	
        	    	if(thList.getChildCount()>0){
        	    		
            	    	rbLocalTh.setChecked(true);

        	    	}
        	    	else{
        	    		
            	    	rbRemoteTh.setChecked(true);
            	    	rbLocalTh.setEnabled(false);

        	    	}

    	    	}
    	    	
    	    }
    	    
    	    rbRemoteTh.setOnClickListener(new RadioButton.OnClickListener(){
	             
    	    	public void onClick(View v){

	    	         thList.setVisibility(View.GONE); 

    	    	                
    	    	    }
    	    	             
    	    });
    	    
    	    rbLocalTh.setOnClickListener(new RadioButton.OnClickListener(){
	             
    	    	public void onClick(View v){

	    	         thList.setVisibility(View.VISIBLE); 

    	    	                
    	    	    }
    	    	             
    	    });
    	    
    

    	    createProject.setOnClickListener(new Button.OnClickListener(){
    	    	             
    	    	
    	    	public void onClick(View v)
    	    	              {

    	    	                 EditText et=(EditText)dialog.findViewById(R.id.etNameItem);
    	    			    	 Spinner thList=(Spinner)dialog.findViewById(R.id.thList);

    	    			    	 String projName=et.getText().toString();
    	    			    
    	    			    	 String thNameFinal="";
    	    			    	 
    	    			    	 if(rbRemoteTh.isChecked()){
    	    			    		 
    	    			    		 thNameFinal=thName;
    	    			    		 
    	    			    		 projNameGlob=projName;
    	    			    		 prNameGlob=prName;
    	    			    		 thNameGlob=thNameFinal;
    	    			    		 
    	    			    		 //poolId, thId
    	    			    		 startDownload(thFilum,thName);
    	    			    		 
    	    			    	 }
    	    			    	 else if(rbLocalTh.isChecked()){
    	    			    	
    	    			    		 thNameFinal=(String)thList.getSelectedItem();
    	    			    		 createProject(projName,prName,thNameFinal, remote);
    	    			    		 
    	    			    	 }
    	    			    	 else{
    	    			    		 
    	    			    		 createProject(projName,prName,"",remote);
    	    			    		     	    			    		 
    	    			    	 }
    	    			    	 
    	    	                
    	    	              }
    	    	             
    	    });
    	    

    	    
    	    dialog.show();


    	 
    }
	    
	    
	   private void createProject(String projName,String prName, String thName, boolean remote){
		   
		   
			long projId=zpC.createProject(projName,thName,prType);

            if(projId<=0) {
	             
            	String sameProject=getBaseContext().getString(R.string.sameNameProject);
            	Toast.makeText(getBaseContext(),sameProject+" "+projName,Toast.LENGTH_LONG).show();
            	
            }
            else{
            	
            	zpC.setAutoFields(true);
            	zpC.downloadProject(prName,remote);
            	
            	dialog.dismiss();
            	
            	Intent intent = new Intent();
		    	
				Bundle b = new Bundle();
				b.putLong("projId", projId);
				intent.putExtras(b);

				setResult(1, intent);
		        finish();
                 
            }
		   
	   }
	    
	  
	    
	    
	    
	    private void startDownload(String poolId, String thId) {
	    	
	    	ThesaurusControler thC=new ThesaurusControler(this);
	    	poolId=thC.determineThType(poolId);
	       	
	    	PreferencesControler pC= new PreferencesControler(this);
	 	   
			RemoteThHandler rThHandler=new RemoteThHandler();
			String url=rThHandler.getThUrl(poolId, thId,pC.getLang());
	    	
			// new DownloadFileAsync().execute(url,"/sdcard/"+pC.getDefaultPath()+"/Thesaurus/",thName+".xml",filumLetter);

			new ThAsyncDownloader(this,postThDownloadHandler).execute(url,Environment.getExternalStorageDirectory()+"/"+pC.getDefaultPath()+"/Thesaurus/",thId+".xml");
		   
	           
	     }
	    
     	 private Handler thPostImportHandler = new Handler() {

   			@Override
			public void handleMessage(Message msg) {
   				
   				pd.dismiss();
   				
   				if (msg.what==0){
   					
   					createProject(projNameGlob, prNameGlob, thNameGlob, true);
   					
   				}
   				
   				else{
   					
   				  	Toast.makeText(getBaseContext(), 
     	   		              R.string.toastWrongFile, 
     	   		              Toast.LENGTH_LONG).show();
   					
   				}
   				
   		        finish();

   			}
   		};
	    
	    
		 private Handler postThDownloadHandler = new Handler(){
			 
			 @Override
			public void handleMessage(Message msg){
				 
				 Bundle b=msg.getData();
				 
				 if(b!=null){
					 
					/* String path="/sdcard/"+pC.getDefaultPath()+"/Thesaurus/"+b.getString("filePath");
					 
					 if(!dontAskThName) createThDialog(path,rTh.getThId(),rTh.getThSource()); 
					 else{
						 
						createThWithoutDialog(path,rTh.getThId(),rTh.getThSource());
						 
					 }*/
					 
					 	String thFileName=b.getString("filePath");
					 
					 	File f= new File(Environment.getExternalStorageDirectory()+"/zamiaDroid/Thesaurus/"+thFileName);
		             		             	
		              	ThesaurusControler thCntr= new ThesaurusControler(getBaseContext());
		              	
		              	//thName can't be chosen
		               	long thId=thCntr.createThesaurus(thName,thName,thFilum,thSource,"remote");
   	                 
		               	final String url= f.getAbsolutePath();
		               	
		               	if(thId>0){
   	                	 
		               		importTh(thId,thName,url);
		               	
		               	}
					 
				 }
				 
			 }
			 
		 };
	    
	           
	           private void importTh(final long thId,final String thName, final String url) {
	      		 
	      		 pd = ProgressDialog.show(this, getString(R.string.thLoading), getString(R.string.thLoadingTxt), true,false);
	      		 
	      		                 Thread thread = new Thread(){
	      		  	        	   	      		  	        	   
	      			                 @Override
									public void run() {
	      			               	 
	      			                	importThThread(thId,thName, url); 
	      			               	  
	      			                 }
	      			           };
	      			           
	      			           
	      		   thread.start();
	      		 
	      		}
	      	 
	      private void importThThread(long thId,String thName, String url){
	      		 
	    	  ThesaurusControler thCntr= new ThesaurusControler(getBaseContext());
	      	  thName=thName.replace(".", "_");

	      	  boolean error=thCntr.addThItems(thId,thName, url);

	      	  if(!error) thPostImportHandler.sendEmptyMessage(0);
	      	  else thPostImportHandler.sendEmptyMessage(1);
	      		 
	      	 }
	      	    	 
	      	 
	

}
