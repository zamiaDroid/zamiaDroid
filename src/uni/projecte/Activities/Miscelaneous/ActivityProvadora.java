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

import java.util.ArrayList;

import uni.projecte.R;
import uni.projecte.Activities.Projects.ProjectRepositoryList;
import uni.projecte.Activities.Syncro.SyncroConfig;
import uni.projecte.controler.SyncProjectControler;
import uni.projecte.dataLayer.CitationManager.Synchro.ProjectSyncListAdapter;
import uni.projecte.dataLayer.CitationManager.Synchro.SyncCitationManager;
import uni.projecte.dataLayer.ProjectManager.objects.Project;
import uni.projecte.dataTypes.Utilities;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;


public class ActivityProvadora extends Activity {
	
	private Spinner sp;
	private SyncCitationManager synchroManager;
	private SyncProjectControler syncCnt;

	
	private ListView lstClientes;
	private Button btConfigSyncro;
	private Button btCreateSyncroProj;

	  @Override
	  public void onCreate(Bundle savedInstanceState) {
		  
	       super.onCreate(savedInstanceState);
	        
	       Utilities.setLocale(this);
		   
	       setContentView(R.layout.project_sync);
	       
	       syncCnt=new SyncProjectControler(this);
	       
	       lstClientes = (ListView)findViewById(R.id.lvRemoteThPool);
	      
	       btConfigSyncro=(Button)findViewById(R.id.btConfigSyncro);
	       btCreateSyncroProj=(Button)findViewById(R.id.btCreateSyncroProj);
	       
	       btCreateSyncroProj.setOnClickListener(createSyncroProjectClick);
	       btConfigSyncro.setOnClickListener(btSyncroConfig);

	       
	  }
	  
		 @Override
		protected void onResume(){
			 super.onResume();
		     
		      synchroManager = new SyncCitationManager(this,"remote_orca");
			 
			 if(synchroManager.isConfigured()){ 
				 
				 loadSyncroProjectList();
				 
			 }
			 else callConfigActivity();
		    	
		 }
		    
	  
	  
	  private void loadSyncroProjectList(){
		  
	       ArrayList<Project> remoteProjectList=synchroManager.getRemoteProjectList();
	       
	       ProjectSyncListAdapter adapter=new ProjectSyncListAdapter(this, remoteProjectList,syncCnt.getAllSyncroProjects(), listLocalClick);
	       lstClientes.setAdapter(adapter);	

	  }
	  
	  
	  public OnClickListener listLocalClick = new OnClickListener() {

			public void onClick(View v) {

				if(v.getId()==-1){

					Intent intent = new Intent(getBaseContext(), ProjectRepositoryList.class);
					intent.putExtra("filter","remote_orca");
					intent.putExtra("projName",(String)v.getTag());

		            startActivityForResult(intent,1);
		            
				}
				else syncroProject((String)v.getTag());
	
			}
		  
		};

		public OnClickListener btSyncroConfig = new OnClickListener() {

				public void onClick(View v) {

					callConfigActivity();
		
				}
			  
			};
		
		private void syncroProject(String projectTag){
			
			//int updated=synchroManager.getOutdatedLocalCitations(projectTag);
			//Utilities.showToast("Dades remotes actualitzades "+updated, this);
			//updated=synchroManager.getOutdatedRemoteCitations(projectTag);
			//Utilities.showToast("Dades locals actualitzades "+updated, this);		
			
		}
		
		private void callConfigActivity(){
			
			Intent intent = new Intent(this, SyncroConfig.class);

            startActivity(intent);
		}	
			
		
	  public OnClickListener createSyncroProjectClick = new OnClickListener() {
		
		@Override
		public void onClick(View v) {

			Intent intent = new Intent(getBaseContext(), ProjectRepositoryList.class);
			intent.putExtra("filter","remote_orca");
            startActivityForResult(intent,1);
			
		}
	};
	
	
	   @Override
		protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
		   	
		        switch(requestCode) {
  
		        case 1 :
		        	
		        	if(intent!=null){
		        		
		        	 	 Bundle ext = intent.getExtras();
		        	 	 long projId=ext.getLong("projId");
		        	 	 
		        	 	 boolean remoteProj=ext.getBoolean("syncro");
		        	 	 
			        	 String projTag=synchroManager.enableSyncroProject(projId,remoteProj);
			        	 
			        	 if(remoteProj) syncroProject(projTag);
			        	 
			        	 loadSyncroProjectList();
		        				        	 
		        	}
		       
		            break;
		            
		            default:
		  
		        }

		    }
	
}

