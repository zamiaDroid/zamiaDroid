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
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.List;

import uni.projecte.R;
import uni.projecte.R.id;
import uni.projecte.R.layout;
import uni.projecte.R.string;
import uni.projecte.controler.BackupControler;
import uni.projecte.controler.PreferencesControler;
import uni.projecte.controler.ProjectControler_needToBeRenamed;
import uni.projecte.controler.ThesaurusControler;
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
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;




public class ProjectImport extends Activity{
	
	   private List<String> elements = null;
	   private ListView fileList;
	   private String url;
	   private Button createProject;
	   private TextView tvImportTitle;

	   private PreferencesControler pC;
	   private ProgressDialog pd;
	   private Dialog dialog=null;

	   private String projName;
	   private String format;
	   
	   
	   private static final int DIALOG_PAUSED_ID = 0;

	
	  @Override
	public void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        
	        Utilities.setLocale(this);
	        setContentView(R.layout.project_citation_import);
	        
	        pC=new PreferencesControler(this);
	        
	        fileList = (ListView)findViewById(R.id.projectList);
	        tvImportTitle = (TextView)findViewById(R.id.tvImportCitationsTitle);
	        
	        format=getIntent().getExtras().getString("format");
	        
	        tvImportTitle.setText(String.format(getString(R.string.chooseFile),format));
	        
	        
	        if(isSdPresent()) fillFileList(new File(Environment.getExternalStorageDirectory()+"/"+pC.getDefaultPath()+"/Projects/").listFiles(new XMLFilter()));
	        else {
	        	
	        	Toast.makeText(getBaseContext(), 
	                    R.string.noSdAlert, 
	                    Toast.LENGTH_SHORT).show();
	        	
	        	
	        }
	        
	        fileList.setOnItemClickListener(theListListener);
	        
	  }
	  
	   @Override
	protected void onRestart(){
		   
		   
		   if(isSdPresent()) fillFileList(new File(Environment.getExternalStorageDirectory()+"/"+pC.getDefaultPath()+"/Projects/").listFiles(new XMLFilter()));
		   else {
	        	
	        	Toast.makeText(getBaseContext(), 
	                    R.string.noSdAlert, 
	                    Toast.LENGTH_SHORT).show();
	        	
	        	
	        }
		   
	   }
	   
	 
	  public static boolean isSdPresent() {

	      return android.os.Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED);
	
	  }
	  
	  
	  private void fillFileList(File[] listFiles) {
		
		        elements = new ArrayList<String>();
		        elements.add(getString(R.string.root));
		        
		        for( File archivo: listFiles)
		            elements.add(archivo.getPath());
		       
		        ArrayAdapter<String> listaArchivos= new ArrayAdapter<String>(this, R.layout.row, elements);
		        fileList.setAdapter(listaArchivos);
		    
		
	  }
	  
	  private void rellenarConElRaiz() {
		   
	        fillFileList(new File("/").listFiles());
	        
	  } 
	
	  
	  public OnItemClickListener theListListener = new OnItemClickListener() {
		    
		    public void onItemClick(android.widget.AdapterView<?> parent, View v, int position, long id) {
		    		
		    	  int IDFilaSeleccionada = position;
		          if (IDFilaSeleccionada==0){
		              rellenarConElRaiz();
		          } else {
		              File archivo = new File(elements.get(IDFilaSeleccionada));
		              if (archivo.isDirectory()){
		            	  fillFileList(archivo.listFiles(new XMLFilter()));
		            	  
		              }
		               else{
		            
	     		            url= archivo.getAbsolutePath();
	                    	createProjectDialog(archivo.getName());   
		    	
		    }
		    
		    
		    }
		    }   
		
		    };
		    
		    
	 
		    class XMLFilter implements FilenameFilter {
		    	  
		    	  
		              public boolean accept(File dir, String name) {
	
		                return (name.endsWith(".xml"));
	
		        }
	              
		    }
		    
		    
		   private void createProjectDialog(String prName) {
		        
			  		ThesaurusControler thCont= new ThesaurusControler(this);
		  		        	
		        	//Context mContext = getApplicationContext();
		    	   	dialog = new Dialog(this);
		    	   	
		        	dialog.setContentView(R.layout.project_creator_dialog);
		    	   	dialog.setTitle(getString(R.string.insert_data));
		    	   	
		    	   	createProject = (Button)dialog.findViewById(R.id.bAddItem);
		    	   	EditText name = (EditText)dialog.findViewById(R.id.etNameItem);
	
		    	   	
		    	   	Spinner thList=(Spinner)dialog.findViewById(R.id.thList);
		    	   	
		    	    
		    	   	ArrayAdapter<String> dtAdapter = new ArrayAdapter<String>(this,
		                    android.R.layout.simple_spinner_item, thCont.getThList());
		        
		        
		 		   dtAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		 		   thList.setAdapter(dtAdapter);
		    	 

		 		   	prName=prName.replace(".xml", "");
		    	   	name.setText(prName);

		    	    createProject.setOnClickListener(new Button.OnClickListener(){
		    	    	             
		    	    	
		    	    	public void onClick(View v)
		    	    	              {

		    	    	                 EditText et=(EditText)dialog.findViewById(R.id.etNameItem);
		    	    			    	 Spinner thList=(Spinner)dialog.findViewById(R.id.thList);

		    	    			    	 projName=et.getText().toString();
		    	    			    	 String thName=(String)thList.getSelectedItem();
		    	    			    	
		    	    	            
			    	    	             importProject2(projName,thName);
		    	    	                
		    	    	                
		    	    	              }
		    	    	             
		    	    });
		    	    
		    	    dialog.show();


		    	 
		    }
		   
		   
		   
			 private void importProject2(final String projName,final String thName) {
				 
				 pd = ProgressDialog.show(this, getString(R.string.projLoading), getString(R.string.projLoadingTxt), true,false);
				
				                 Thread thread = new Thread(){
				  	        	   
				  	        	   
					                 @Override
									public void run() {
					               	  
					               	  
			    	    	               importProjectThread(projName,thName);
					               	  
					                 }
					           };
					           
					           
				   thread.start();
				 
				}	
			 
			 
			 private void importProjectThread(String name, String thName){
				 
				 
				if(format.equals("Fagus")){ 
				 
					ProjectControler_needToBeRenamed pC= new ProjectControler_needToBeRenamed(this);
			    	long id=pC.importProject(name,thName,url);
					handler.sendEmptyMessage((int)id);
				
				}
				else{
					
					BackupControler backCnt=new BackupControler(this);
					long id = backCnt.importProjectStructure(name, url, thName);
					handler.sendEmptyMessage((int)id);
					
				}
				 
			 }
			 
			 
			 
			 private Handler handler = new Handler() {

					@Override
					public void handleMessage(Message msg) {
						
					 	pd.dismiss();

						
						switch (msg.what) {
						case -2:
							
							String message=String.format(getBaseContext().getString(R.string.wrongProjectFile), format);
							
						 	Toast.makeText(getBaseContext(), 
		    	   		              message, 
		    	   		              Toast.LENGTH_LONG).show();
						 	
						 	dialog.dismiss();
							
							break;
						case -1:
							
							String sameProject=getBaseContext().getString(R.string.sameNameProject);
							
    	                	Toast.makeText(getBaseContext(), 
    	   		              sameProject+" "+projName, 
    	   		              Toast.LENGTH_LONG).show();
    	                	
							break;


						default:
							
							Intent intent = new Intent();
					    	
							Bundle b = new Bundle();
							b.putLong("projId", msg.what);
							intent.putExtras(b);

							setResult(1, intent);
		
					        finish();
							
							break;
						}
						

						


					}
				}; 
			 

}
