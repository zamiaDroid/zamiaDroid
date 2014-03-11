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
import java.util.Date;

import uni.projecte.Main;
import uni.projecte.R;
import uni.projecte.Activities.Citations.Sampling;
import uni.projecte.controler.BackupControler;
import uni.projecte.controler.PhotoControler;
import uni.projecte.controler.ProjectControler;
import uni.projecte.controler.ProjectSecondLevelControler;
import uni.projecte.controler.ThesaurusControler;
import uni.projecte.dataLayer.CitationManager.FileExporter;
import uni.projecte.dataLayer.ProjectManager.ListAdapters.ProjectBaseListAdapter;
import uni.projecte.dataLayer.ProjectManager.objects.Project;
import uni.projecte.dataLayer.bd.ProjectDbAdapter;
import uni.projecte.dataTypes.Utilities;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Html;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.ResourceCursorAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class ProjectList extends Activity {
	
		private ListView projectList;

		private static final int BACKUP_PROJ=Menu.FIRST;
		private static final int LOAD_BACKUP_PROJ=Menu.FIRST+1;
		private static final int REMOVE_PROJECT=Menu.FIRST+2;
		private static final int EXPORT_PROJECT=Menu.FIRST+3;

		private String name;
		private String desc;
		
		private SharedPreferences preferences;
		
		private String defaultProject="";
		private long projId;

		private ProgressDialog pd;
		private Dialog exportProjdialog;
		
		private ProjectBaseListAdapter projectsAdapter;

		private boolean changeProject=false;

 
	    @Override
		public void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        
	        Utilities.setLocale(this);
	        
	        setContentView(R.layout.project_list);

	        projectList = (ListView)findViewById(R.id.listResearches);
	 	          
	        
	        //simple click listener
	        projectList.setOnItemClickListener(theListListener);
	        
	        loadProjects();
          
	        if(getIntent().getExtras()!=null){
	        
	        	changeProject=getIntent().getExtras().getBoolean("changeProject");
	        	
	        }
	       
	    }
	    
	    @Override
		public void onResume()
	    {
	             super.onResume();

	             loadProjects();
	     }
	    
	    @Override
		public void onStop(){
	    	
	    	super.onStop();
	    	
	    	
	    }
	    
	    /*
	     * 
	     * Project Removal providing its name
	     * 
	     */
	    
	    private int removeProject(String projName){
	    	
	    	int status=0;
	    	
	    	ProjectSecondLevelControler projCnt= new ProjectSecondLevelControler(this);
	    	projCnt.loadResearchInfoByName(projName);
	    	
	    	status=projCnt.removeProject(projCnt.getProjectId());
	    	
	 	   	
	 	   	if (status==-1){
	 	   		
		 	   	 Toast.makeText(getBaseContext(), 
		 	              R.string.hasCitations, 
		 	              Toast.LENGTH_LONG).show();
		 	   	 
		 	   	 status=-1;
	 	   		
	 	   	}
	 	   	 
	 	   	return status;
	    	
	    	
	    }
	    
	    
	    @Override
		public boolean onCreateOptionsMenu(Menu menu) {
	    	
	    	
	    	menu.add(0, BACKUP_PROJ, 0,R.string.mCreateBackup).setIcon(android.R.drawable.ic_menu_save);
	    	menu.add(0, LOAD_BACKUP_PROJ, 0,R.string.mLoadBackup).setIcon(android.R.drawable.ic_menu_upload);
	    	menu.add(0, EXPORT_PROJECT, 0,R.string.dialogProjectExport).setIcon(android.R.drawable.ic_menu_save);
	    	menu.add(0, REMOVE_PROJECT, 0,R.string.mRemoveProject).setIcon(android.R.drawable.ic_menu_delete);

	    	
	    	return super.onCreateOptionsMenu(menu);
	    }
	    
	   @Override
	public boolean onPrepareOptionsMenu(Menu menu){
		   
		   if(projId<0){
			   
			   menu.findItem(BACKUP_PROJ).setVisible(false);
			   menu.findItem(REMOVE_PROJECT).setVisible(false);
			   menu.findItem(EXPORT_PROJECT).setVisible(false);

		   }
		   else{
			   
			   
			 String createBackup=getString(R.string.mCreateBackup);
			 String removeProject=getString(R.string.mRemoveProject);

			 defaultProject=projectsAdapter.getDefaultProject();
			   
			 menu.findItem(BACKUP_PROJ).setVisible(true);
			 menu.findItem(BACKUP_PROJ).setTitle(createBackup+": "+defaultProject);
			   
			 menu.findItem(REMOVE_PROJECT).setVisible(true);
			 menu.findItem(REMOVE_PROJECT).setTitle(removeProject+": "+defaultProject);
				   
		   }
		   
		   
		   return super.onPrepareOptionsMenu(menu);
		  
		   
	   }

	    
	    @Override
		public boolean onOptionsItemSelected(MenuItem item) {
	    	
	    	
			switch (item.getItemId()) {
				
			case BACKUP_PROJ:
				
				  if(Utilities.isSdPresent()){ 
					  
					  if(projId>-1){
						
						createBackupDialog();
					}
				  
				  }
				   else {
			        	
			        	Toast.makeText(getBaseContext(), 
			                    R.string.noSdAlert, 
			                    Toast.LENGTH_SHORT).show();
			        	
			        }
				
				
		 
				break;
				
			case LOAD_BACKUP_PROJ:
				
				Intent projBackActivity = new Intent(getBaseContext(),ProjectBackupList.class);
	    		startActivityForResult(projBackActivity,1);

				
				break;
				
			case EXPORT_PROJECT:
				
				
				exportProjectDialog(projectsAdapter.getDefaultProject());
				
				break;
				
			case REMOVE_PROJECT:
				
				removeProject();
				
				break;
				
				
			}
			
		
			return super.onOptionsItemSelected(item);
		}
	    
	    
	    private void exportProjectDialog(String projName){
	    	
	    	final BackupControler bc= new BackupControler(this);
	    	
	    	exportProjdialog=new Dialog(this);
	    	exportProjdialog.setContentView(R.layout.projectexportdialog);
	    	exportProjdialog.setTitle(R.string.dialogProjectExport);
    	   	
	    	final EditText etProjName=(EditText) exportProjdialog.findViewById(R.id.etProjectName);
	    	etProjName.setText("zp_"+projName);	 
	    	
	    	Button btExportProject=(Button) exportProjdialog.findViewById(R.id.btExportProject);
	    	
	    	btExportProject.setOnClickListener(new OnClickListener() {
				
				public void onClick(View v) {

					 if(Utilities.isSdPresent()){ 
						 
						 exportProjectCheck(etProjName.getText().toString());						 
						
					 }

					else {
				        	
				        	Toast.makeText(getBaseContext(), 
				                    R.string.noSdAlert, 
				                    Toast.LENGTH_SHORT).show();
				        	
				        }
					
			
					

					
				}
			});
	    	
    	   	
    	   	exportProjdialog.show();
	    	
	    	
	    }
	    
	    private void exportProjectCheck(final String fileName){
			
			//checking that file exists
	    	final BackupControler bc= new BackupControler(this);
	    	String filePath=bc.getProjectsPath()+fileName+".xml";
	    	
	    	File f= new File(filePath);
	        
	        if(f.exists()) {
	        	
	        	AlertDialog.Builder builder = new AlertDialog.Builder(this);
	        	
				    builder.setMessage(getString(R.string.projFileExists))
				           .setCancelable(false)
				           .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
				               public void onClick(DialogInterface dialog, int id) {
				   
					   				dialog.dismiss();
					   				
					   				bc.exportProjectStructure(projId,fileName);
					   				
					   				exportProjdialog.dismiss();
					   				
									Utilities.showToast(getString(R.string.projExportSuccess), getBaseContext());

				               }
				           })
				           .setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
				               public void onClick(DialogInterface dialog, int id) {
				            	   	
					   				dialog.dismiss();
					
				               }
				           });
				   
				    
					 AlertDialog alert = builder.create();
					 alert.show();
	        	
	        }
	        else{
	
				bc.exportProjectStructure(projId,fileName);
				exportProjdialog.dismiss();
				Utilities.showToast(getString(R.string.projExportSuccess), this);

	        }
			
		}
	    
	    private void exportProjectDialog2222(String projName, String thName){
	    	
	  		ThesaurusControler thCont= new ThesaurusControler(this);

	    	
	    	final Dialog dialog=new Dialog(this);
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

    	 
    	   	name.setText(projName);
    	   	
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
    	    	             
    	    	
    	    	public void onClick(View v){
    	    		

    	    	                 EditText et=(EditText)dialog.findViewById(R.id.etNameItem);
    	    			    	 Spinner thList=(Spinner)dialog.findViewById(R.id.thList);

    	    			    	 String projName=et.getText().toString();
    	    			    
    	    			    	 String thNameFinal="";
    	    			    	 
    	    			    	// BackupControler bc= new BackupControler(this);
    	    					//bc.exportProjectStructure(projId, );
    	    						
    	    			    	 
    	    			    	/* if(rbRemoteTh.isChecked()){
    	    			    		 
    	    			    		 thNameFinal=thName;projName
    	    			    		 
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
    	    			    	 */
    	    	                
    	    	              }
    	    	             
    	    });
    	    

    	    
    	    dialog.show();
	    	
	    	
	    }
	    
	    private void createBackupDialog() {

	    	PhotoControler photoCnt=new PhotoControler(this);
	    	long photFieldId=photoCnt.getProjectPhotoFieldId(projId);
	    	
	    	String dialogTitle=String.format(getString(R.string.backupCreateTitle), defaultProject);
	    	
			final Dialog dialog=new Dialog(this);
			dialog.setContentView(R.layout.createbackup);
			
			dialog.setTitle(dialogTitle);
			
			final Date date = new Date();
	        date.getDate();
			
	        final EditText etBackName=(EditText) dialog.findViewById(R.id.etBackupName);
	        final CheckBox cbAddPhotos=(CheckBox) dialog.findViewById(R.id.cbBackupAddPhotos);
	        TextView tvBackupInfo=(TextView) dialog.findViewById(R.id.tvBackupInfo);
	        Button btCreateBackup=(Button) dialog.findViewById(R.id.btBackupCreate);
	      
	        if(photFieldId<0) {
	        	
	        	cbAddPhotos.setVisibility(View.GONE);
	        }
	        else{
	        	
	        	tvBackupInfo.setText(getString(R.string.backupInfo)+". "+getString(R.string.backupInfoPhotos));
	        	
	        }
	        
			String defaultBackupName=defaultProject+"_"+(String) DateFormat.format("yyyy-MM-dd-kk-mm-ss", date);
			etBackName.setText(defaultBackupName);
			etBackName.setEnabled(false);

			dialog.show();
			
			btCreateBackup.setOnClickListener(new OnClickListener() {
				
				public void onClick(View v) {
					

					if(etBackName.getText().length()>0){
						
						dialog.dismiss();
						createBackupProgressDialog(etBackName.getText().toString(),cbAddPhotos.isChecked());

					}
					else{
						
						Utilities.showToast(getString(R.string.backupEmptyName), v.getContext());
						
					}
					
				}
			});
	    	
			
		}

		protected void createBackupProgressDialog(final String backupName, final boolean addPhotos) {

			final BackupControler bc= new BackupControler(this);
			
			 pd = ProgressDialog.show(this, getBaseContext().getString(R.string.pdBackupCreating), getBaseContext().getString(R.string.projCreationTxt), true,false);

             Thread thread = new Thread(){
	        	   
                 @Override
				public void run() {
               	  
 					bc.exportProject(projId,backupName,addPhotos);
 			  		handler.sendEmptyMessage(0);
               	  
                 }
           };
           
           
           thread.start();

		}
		
		

		private void removeProject() {

	    	AlertDialog.Builder builder = new AlertDialog.Builder(this);
	    	
	    	
	    	builder.setMessage(R.string.deleteProjQuestion)
	    	       .setCancelable(false)
	    	       .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
	    	           public void onClick(DialogInterface dialog, int id) {
	    
	    	        	   
	    	        	 int status=removeProject(defaultProject);
	    	        	 
	    	        	 if(status>=0){
	    	        		 
	    	        		 if(defaultProject!=null) {
	    	        		 
		    	        		 if(defaultProject.equals(defaultProject)) {
		    	        			 
	    	        			
		    	        			projectsAdapter.setDefaultRadioButton(null);
		    	        		
		    	        	        SharedPreferences.Editor editor = preferences.edit();
			            	        editor.putLong("predProjectId", -1);
			            	        editor.putString("predField", null);
			            	        editor.commit();
			            	        
			            	        defaultProject="";
			            	        projId=-1;
		    	        	 
		    	        		 }
	    	        		 
	    	        		 }
	    	        
	    	        	 	loadProjects();
	    	           }

	    	        	   
	    	           }
	    	       })
	    	       .setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
	    	           public void onClick(DialogInterface dialog, int id) {
	    	                
	    	        	   dialog.cancel();
	    	                
	    	           }
	    	       });
	    	AlertDialog alert = builder.create();
	    	
	    	alert.show();
		}

		private Handler handler = new Handler() {
	    	
			@Override
			public void handleMessage(Message msg) {	
				
				pd.dismiss();

				
				if(msg.what==0){
					
					Utilities.showToast("Projecte backupejat", getBaseContext());
			    
				}
				
		

			}
		};
	    
		
		private Handler handlerUpdatePref = new Handler() {
	    	
			@Override
			public void handleMessage(Message msg) {
				
				Bundle b=msg.getData();
				
		        SharedPreferences.Editor editor = preferences.edit();
		        editor.putLong("predProjectId", b.getLong("projId"));
		        editor.putString("predField", null);
		        editor.commit();
		        
		        if(changeProject) {
		        	            
		        	finish();
		        	
		        }
		
		        if(b.getBoolean("removeProject")){
		        	
		        	loadProjects();
		        	
		        }
		        	

			}
		};
	    
		
		

			

	    
	    /*
	     * It fills the listAdapter with the list of project Names
	     * 
	     */

	    private void loadProjects (){
	    	
        	ProjectControler projCnt= new ProjectControler(this);

	    	preferences = getSharedPreferences(Main.PREF_FILE_NAME, MODE_PRIVATE);

		     projId = preferences.getLong("predProjectId", -1);
		        
		     if(projId!=-1) {
		        	
		        	projCnt.loadProjectInfoById(projId);
		        	defaultProject=projCnt.getName();
		        	
		     }
	    	

		     ArrayList<Project> projList=projCnt.getProjectArrayList();
		     
		     
		     projectsAdapter=new ProjectBaseListAdapter(this, projList, projId, defaultProject, handlerUpdatePref);
		     projectList.setAdapter(projectsAdapter);

	    	
	    }
	    
	    
	  /*
	   * 
	   * This Listener is triggered when a list item is clicked.
	   * 
	   * The method fetchs projFields and shows them in a Dialog.
	   * 
	   */
	  
	  
	  public OnItemClickListener theListListener = new OnItemClickListener() {
	    
		    public void onItemClick(android.widget.AdapterView<?> parent, View v, int position, long id) {
		        
			   	 TextView tv=(TextView)v;
			   	 String projName=(String)tv.getText();
			   	 
			   	 ProjectControler rsC=new ProjectControler(parent.getContext());
		     
			     rsC.loadResearchInfoByName(projName);
	 
			   	 name=rsC.getName();
			   	 desc=rsC.getThName();
			   	 
			   	 
			   	Intent intent = new Intent(v.getContext(), ProjectInfo.class);
			       
	 			Bundle b = new Bundle();
	 			b.putLong("Id", rsC.getProjectId());
	 			intent.putExtras(b);
	 			
	 			b.putString("projName", name);
	 			intent.putExtras(b);
	 			intent.putExtras(b);
	 			b = new Bundle();
	 			b.putString("projDescription", desc);
	 			intent.putExtras(b);
	 		
		 		startActivityForResult(intent, 1);   
		 		
		 		
		 		
		    	
		    }
	    };
	    
	    
	    public boolean isDefaultProject(String projName){
	    	
	    	return projName.equals(defaultProject);
	    	
	    }
	    
	    
	    
	    private void setDefaultProject(long projId){
			   
			SharedPreferences.Editor editor = preferences.edit();
    	    editor.putLong("predProjectId", projId);
    	    editor.putString("predField", null);
    	    editor.commit();
		   
	   }
  		
  		
 	   @Override
	protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
	       // super.onActivityResult(requestCode, resultCode, intent);
	   	
	       	
	        switch(requestCode) {
	        case 0 :
	   
	            break;
	            
	         /* when we're back from a project creator, we'll set it as a default project*/   
	        case 1 :
	        	
	        	if(intent!=null){
	        		
	        	 	 Bundle ext = intent.getExtras();
		        	 setDefaultProject(ext.getLong("projId"));
		        	 loadProjects();
	        				        	 
	        	}
	       
	            break;
	            
	            default:
	            	
	            	
	  
	        }
	        

	    }
	    
}
