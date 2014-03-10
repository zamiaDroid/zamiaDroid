package uni.projecte.Activities.Projects;

import uni.projecte.Main;
import uni.projecte.R;
import uni.projecte.Activities.Citations.CitationProjectImport;
import uni.projecte.controler.PreferencesControler;
import uni.projecte.dataLayer.ProjectManager.examples.ExampleProjectCreator;
import uni.projecte.dataTypes.Utilities;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

public class ProjectTemplateCreator extends Activity{
	
	private PreferencesControler pC;
	private ExampleProjectCreator epC;
	
	private RadioButton rbRepository;
	private RadioButton rbCitations;
	private RadioButton rbProjects;
	private RadioButton rbFree;

	private CheckBox cbAddAuthor;
	private CheckBox cbAddAltitude;

	private SharedPreferences preferences;

	private long projId;
	   

		@Override
		public void onCreate(Bundle savedInstanceState) {
		    super.onCreate(savedInstanceState);
	        
	        Utilities.setLocale(this);
		    setContentView(R.layout.projcreator);
	        
	        rbRepository=(RadioButton)findViewById(R.id.rbRepository);
	        rbCitations=(RadioButton)findViewById(R.id.rbCitationsFile);
	        rbProjects=(RadioButton)findViewById(R.id.rbProject);
	        rbFree=(RadioButton)findViewById(R.id.rbFree);
	        
	        cbAddAuthor=(CheckBox)findViewById(R.id.cbAddAuthor);
	        cbAddAltitude=(CheckBox)findViewById(R.id.cbAddAlsada);
	        
	        preferences = getSharedPreferences(Main.PREF_FILE_NAME, MODE_PRIVATE);
	        
	        cbAddAuthor.setOnClickListener(onCheckedAddAuthorListener);
	        cbAddAltitude.setOnClickListener(onCheckedAddAlsadaListener);

	        pC= new PreferencesControler(this);
	        
	        cbAddAuthor.setChecked(pC.isAddAuthor());
	        cbAddAltitude.setChecked(pC.isAddAltitude());
	        
		}
		
		
		private OnClickListener onCheckedAddAuthorListener = new OnClickListener(){


			public void onClick(View check) {

				if(cbAddAuthor.isChecked()){ 
					
					pC.setAddAuthor(true);
					
				}
				else{
					
					pC.setAddAuthor(false);

				}
				
			}


	    };
	    
	    
		private OnClickListener onCheckedAddAlsadaListener = new OnClickListener(){


			public void onClick(View check) {

				if(cbAddAltitude.isChecked()){ 
					
					pC.setAddAltitude(true);
					
				}
				else{
					
					pC.setAddAltitude(false);

				}
				
			}


	    };
		
		
	
		public void bCreatePredProject(View v){                        
	        	
	        	if(rbRepository.isChecked()){
	        		
	        		Intent intent = new Intent(getBaseContext(), ProjectRepositoryList.class);
		            startActivityForResult(intent,1);
	       
	        		
	        	}
	        	else if(rbFree.isChecked()){
	        		
	    			Intent myIntent = new Intent(getBaseContext(), ProjectCreator.class);
	                startActivityForResult(myIntent,1);
	        		
	        	}
	        	else if(rbCitations.isChecked()){
	        		
	        		 AlertDialog.Builder builder2 = new AlertDialog.Builder(this);
					   builder2.setTitle(getString(R.string.chooseImportFormatCit));
					   
					   final String[] formats2=getBaseContext().getResources().getStringArray(R.array.importCitFormats);
					   
					   builder2.setSingleChoiceItems(formats2,-1, new DialogInterface.OnClickListener() {
					       public void onClick(DialogInterface dialog, int item) {
					           
						    	dialog.dismiss();
					    		Intent intent = new Intent(getBaseContext(), CitationProjectImport.class);
					    		intent.putExtra("format", formats2[item]);
					            startActivityForResult(intent,1);
					    	   
					       }
					   });
					   AlertDialog alert2 = builder2.create();
					   alert2.show();
					
	        		
	        		
	        	}
	        	else{
	        		
	       		 AlertDialog.Builder builder = new AlertDialog.Builder(this);
				   builder.setTitle(getString(R.string.chooseImportFormatProj));
				   
				   final String[] formats=v.getContext().getResources().getStringArray(R.array.importProjFormats);
				   
				   builder.setSingleChoiceItems(formats,-1, new DialogInterface.OnClickListener() {
				       public void onClick(DialogInterface dialog, int item) {
				           
				    	dialog.dismiss();
				   		Intent myIntent = new Intent(getBaseContext(), ProjectImport.class);
				   		
				   		Bundle b= new Bundle();
				   		b.putString("format", formats[item]);
				   		
				   		myIntent.putExtras(b);
				   		
			            startActivityForResult(myIntent,1);
			            
				    	   
				       }
				   });
				   AlertDialog alert = builder.create();
				   alert.show();
	        		
	        		
	        	}
	        	
	        	
	     }
		
		 private void createProjectDialog(String prName) {
		        
	        	
	        	//Context mContext = getApplicationContext();
	    	   final Dialog dialog = new Dialog(this);
	    	   	
	        	dialog.setContentView(R.layout.projectcreationsimple);
	    	   	dialog.setTitle(getString(R.string.insert_data));
	    	   	
	    	   	Button createProject = (Button)dialog.findViewById(R.id.bAddItemS);
	    	   	EditText name=(EditText)dialog.findViewById(R.id.etNameItemS);

	    	   	name.setText(prName);

	    	    createProject.setOnClickListener(new Button.OnClickListener(){
	    	    	             
	    	    	
	    	    	public void onClick(View v)
	    	    	              {

	    	    	                 EditText et=(EditText)dialog.findViewById(R.id.etNameItemS);

	    	    			    	 String projName=et.getText().toString();
	    	    			    	
	    	    			    	 
	    	    			    	 projId=epC.createBasicProject(projName);
	    	    						
	    	    					 if(projId>0){
	    	    						 
	    	    						 Toast.makeText(getBaseContext(), String.format(getString(R.string.projSuccesCreated), epC.getProjectName()),Toast.LENGTH_LONG).show();
	    	    						 dialog.dismiss();
	    	    						 
	    	    						setDefaultProject();
	    	    						finish();
	    	    						
	    	    					 }
	    	    					 
	    	    					 else{
	    	    						 
	    	    							String sameProject=getBaseContext().getString(R.string.sameNameProject);
	    	    	    	    	      	Toast.makeText(getBaseContext(), 
	    	    		    	   		              sameProject+" "+epC.getProjectName().toString(), 
	    	    		    	   		              Toast.LENGTH_LONG).show();
	    	    						 
	    	    					 }
	    	    	                
	    	    	              }
	    	    	             
	    	    });
	    	    
	    	    dialog.show();


	    	 
	    }
		 
		   private void setDefaultProject(){
			   
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
				        	 projId= ext.getLong("projId");
				        	 setDefaultProject();
			        		
				        	 finish();
				        	 
			        	}
			       
			            break;
			            
			            default:
		            	
		            	
		  
		        }
		        

		    }
	   

		

}
