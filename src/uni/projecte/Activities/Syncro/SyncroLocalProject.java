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


	package uni.projecte.Activities.Syncro;

	import java.util.ArrayList;
	import java.util.Iterator;

import uni.projecte.R;
import uni.projecte.controler.ProjectControler;
import uni.projecte.controler.ProjectSecondLevelControler;
import uni.projecte.dataLayer.CitationManager.Synchro.SyncCitationManager;
import uni.projecte.dataLayer.CitationManager.Synchro.SyncroUpdater;
import uni.projecte.dataLayer.CitationManager.Zamia.ZamiaCitationXMLparser;
import uni.projecte.dataLayer.ProjectManager.ListAdapters.NewFieldsListAdapter;
import uni.projecte.dataLayer.ProjectManager.objects.Project;
import uni.projecte.dataTypes.FieldsList;
import uni.projecte.dataTypes.ProjectField;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;


	/*
	 * 
	 * This activity allows us to importCitations in Zamia Format
	 * 
	 * Firstly, citation file provided {getIntent().getExtras().getString("file")} is pre-read and new Fields are shown
	 * inside a listView with checkBoxes.
	 * 
	 * a) Checked fields will be created inside project {getIntent().getExtras().getLong("id")} and then
	 * citations will be imported to the project provided
	 * 
	 * or
	 * 
	 * b) when no project is provided a dialog will be shown to create a new project with a selected thesaurus.
	 * In that case, the list of new fields will contain all fields belonging to citation file.
	 * 
	 */

	public class SyncroLocalProject extends Activity {
		
		public static int SUCCESSFUL_IMPORT =1;
		
		private SyncCitationManager synchroManager;
		
		private Project projObj;	
		private FieldsList fieldsList;
		
		private ArrayList<String> newFields;
		private ArrayList<String> notRemoteFields;
		
		
		private NewFieldsListAdapter newFieldListAdapter;
		private long projId;
		
		private ProjectControler projCnt;
		private static String fileName;
		
		
		private int newCitations;

		
	    @Override
	    public void onCreate(Bundle savedInstanceState) {

	    	super.onCreate(savedInstanceState);

	    	setContentView(R.layout.project_sync_local);

	    	ListView lvNewFields=(ListView)findViewById(R.id.lvNewFields);
	    	ListView lvNotRemoteFields=(ListView)findViewById(R.id.lvNotRemoteFields);


	    	TextView tvFieldsInfo=(TextView)findViewById(R.id.tvFields);
	    	Button btImport=(Button)findViewById(R.id.btZamiaImport);
	    	LinearLayout llBottomPanel=(LinearLayout)findViewById(R.id.llBottomPanel);

	    	//when no projId    	
	    	projId=getIntent().getExtras().getLong("id");

	    	fileName="http://biodiver.bio.ub.es/ZamiaProjectProvider/GetZamiaProject?&remote_th=true&lang=ca&proj_id=proj_botanic_orca";

	    	ZamiaCitationXMLparser zCP= new ZamiaCitationXMLparser();

	    	projObj= new Project(projId);
	    	fieldsList = new FieldsList();

	    	projCnt=new ProjectControler(this);
	    	

	    	// pre-reading file Structure
	    	newCitations=zCP.preReadXML(this, fileName, projObj,fieldsList);    	

	    	//Well formatted with citations
	    	loadProjectInfo();
	    	
	    	
	    	if(!projCnt.isBotanical()){
	    	
	           		llBottomPanel.setVisibility(View.GONE);
	           		tvFieldsInfo.setText(String.format(getString(R.string.syncroCannot),"orca.cat"));
	    		
	    	}
	    	else{
	    	
	
		    	//creating list of new Fields (fields that not exists in the project)
		    	checkNewFields();
		    	checkNotRemoteFields();
	
		    	tvFieldsInfo.setText(Html.fromHtml(String.format(getString(R.string.syncroLocalAddFields),newFields.size())));
	
	
		    	newFieldListAdapter=new NewFieldsListAdapter(this, newFields,true);
		    	lvNewFields.setAdapter(newFieldListAdapter);
	
		    	NewFieldsListAdapter newNotRemoteFieldListAdapter=new NewFieldsListAdapter(this, notRemoteFields,true);
		    	lvNotRemoteFields.setAdapter(newNotRemoteFieldListAdapter);
	
		    	btImport.setOnClickListener(btZamiaImportListener);
		    	
	    	}

	    }

	 
		 @Override
			protected void onResume(){
				 super.onResume();
			     
			      synchroManager = new SyncCitationManager(this,"remote_orca");
				 
				 if(synchroManager.isConfigured()){ 
					 
					 
				 }
				 else callConfigActivity();
			    	
			 }
		
		 
		 private void callConfigActivity(){
				
				Intent intent = new Intent(this, SyncroConfig.class);
	            startActivityForResult(intent,0);
	            
		 }	
				    

		private void loadProjectInfo() {

			if(projObj.isCreated()){
	    		
	        	projCnt.loadProjectInfoById(projId);
	        	projObj.setProjName(projCnt.getName());
	        	    		
			}
					
		}


		private OnClickListener btZamiaImportListener = new OnClickListener()
	    {
	        public void onClick(View v){        
	        	
	        	syncroProject();
	        }
	        
		};
	    
		private void syncroProject(){

			addProjectFields();
			
       	 	String projTag=synchroManager.enableSyncroProject(projId,false);
       	 	
    		SyncroUpdater syncUpdater= new SyncroUpdater(this, synchroManager);
			syncUpdater.startUpdate(projTag, handlerUpdateProcessDialog);
			
		}
			
		private int addProjectFields(){
			
			Iterator<String> it=newFields.iterator();
			int i=0;

			//creating fields that not exists and we have chosen to create them    	
			while(it.hasNext()){

				String fieldName=it.next();

				createNewField(fieldsList.getProjectField(fieldName));

				i++;

			}

			return i;

		}

	    
		   /*
		    * This handler handles the result of the import dialog:
		    * 
		    * 	+ Incrementing progress bar msg.what==0
		    * 	+ Ending the process msg.what==1
		    * 
		    */
		   
			 private Handler handlerUpdateProcessDialog = new Handler() {

					@Override
					public void handleMessage(Message msg) {	

			
						finishActivity();
										

					}
				};
	    

		/*
		 * 
		 * @newFields ArrayList will contain all fields not belonging to current project 
		 * but belonging to citations file 
		 * 
		 */

		private void checkNewFields() {

			ProjectControler pC= new ProjectControler(this);
			newFields=new ArrayList<String>();
			
			Iterator<String> it=fieldsList.getFieldsNames().iterator();
					
			while (it.hasNext()) {
				
				String key=it.next();
				
				long fieldId=pC.getFieldIdByName(projId,key);
				if(fieldId<0) newFields.add(key);
							
			}

		}
		
		   
		private void checkNotRemoteFields() {

			ProjectControler pC= new ProjectControler(this);
			ArrayList<ProjectField> fields=pC.getProjectFields(projId);
			notRemoteFields=new ArrayList<String>();
			
			for(ProjectField field : fields) {
				
				if(!fieldsList.fieldExists(field.getName())) notRemoteFields.add(field.getName());
				
			}
			
		}
		
		protected void finishActivity() {
			
			Intent intent = new Intent();
	    	
			Bundle b = new Bundle();
			b.putInt("numCitations", newCitations);
			intent.putExtras(b);
	   
			
			b = new Bundle();
			b.putLong("projId", projId);
						
			intent.putExtras(b);
			setResult(1, intent);
			
			finish();
			
		}

		protected void createNewField(ProjectField projectField) {

			Log.i("Import","Format:Zamia | (B) Action: Field Added -> "+projectField.getName()+" : "+projectField.getLabel());
				
			long fieldId=projCnt.createField(projId, projectField.getName(), projectField.getLabel(),"ECO", projectField.getType(), true);
			
			if(projectField.getPredValuesList().size()>0) projCnt.addFieldItemList(projId,fieldId,projectField.getPredValuesList());

			
			//creating subProjectFields
			if(projectField.isPolygon()){
				
				 ProjectSecondLevelControler projSLCnt= new ProjectSecondLevelControler(this);
				 projSLCnt.createField(fieldId, "polygonAltitude", "polygonAltitude", "", "", "text");
				
			}
			
			if(projectField.isMultiPhoto()){
				
				 ProjectSecondLevelControler projSLCnt= new ProjectSecondLevelControler(this);
				 projSLCnt.createField(fieldId, "Photo", "photo", "", "", "text");
			}
				
		}
		
	}



