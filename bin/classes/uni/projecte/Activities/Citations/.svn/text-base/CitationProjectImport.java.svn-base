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

package uni.projecte.Activities.Citations;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.List;

import uni.projecte.R;
import uni.projecte.R.id;
import uni.projecte.R.layout;
import uni.projecte.R.string;
import uni.projecte.controler.PreferencesControler;
import uni.projecte.controler.ProjectControler;
import uni.projecte.controler.ThesaurusControler;
import uni.projecte.dataLayer.CitationManager.Fagus.FagusReader;
import uni.projecte.dataLayer.CitationManager.Fagus.FagusXMLparser;
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
import android.widget.Toast;




public class CitationProjectImport extends Activity{
	
	//   private AutoCompleteTextView txtName;
	  // private  ResearchControler rsCont;
	   private List<String> elements = null;
	   private ListView fileList;
	   private String url;
	   private Button createProject;

	   private PreferencesControler prefCnt;
	   private ProgressDialog pd;
	   
	   private FagusReader fR;
	   
	   private Dialog dialog;
	   
	   private ProjectControler projCnt;

	   private long projId=-1;

	   
	static final int DIALOG_PAUSED_ID = 0;

	
	  @Override
	public void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        
	        Utilities.setLocale(this);

	        setContentView(R.layout.citation_import);
	        
	        prefCnt=new PreferencesControler(this);

	        
	        fileList = (ListView)findViewById(R.id.projectList);

	        
	        if(isSdPresent()) fillFileList(new File(Environment.getExternalStorageDirectory()+"/"+prefCnt.getDefaultPath()+"/Citations/").listFiles(new XMLFilter()));
	        else {
	        	
	        	Toast.makeText(getBaseContext(), 
	                    R.string.noSdAlert, 
	                    Toast.LENGTH_SHORT).show();
	        	
	        	
	        }
	        
	        fileList.setOnItemClickListener(theListListener);
	        
	  }
	  
	   @Override
	protected void onRestart(){
		   
		   
		   if(isSdPresent()) fillFileList(new File(Environment.getExternalStorageDirectory()+"/"+prefCnt.getDefaultPath()+"/Projects/").listFiles(new XMLFilter()));
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
		    	   	
		        	dialog.setContentView(R.layout.projectcreation);
		    	   	dialog.setTitle("Introdueixi les dades");
		    	   	
		    	   	createProject = (Button)dialog.findViewById(R.id.bAddItem);
		    	   	EditText name=(EditText)dialog.findViewById(R.id.etNameItem);
	
		    	   	
		    	   	Spinner thList=(Spinner)dialog.findViewById(R.id.thList);
		    	   	
		    	    
		    	   	ArrayAdapter<String> dtAdapter = new ArrayAdapter<String>(this,
		                    android.R.layout.simple_spinner_item, thCont.getThList());
		        
		        
		 		   dtAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		 		   thList.setAdapter(dtAdapter);
		    	 
		    	   	name.setText(prName.replace(".xml", ""));

		    	    createProject.setOnClickListener(new Button.OnClickListener(){
		    	    	             
		    	    	
		    	    	public void onClick(View v)
		    	    	              {

		    	    	                 EditText et=(EditText)dialog.findViewById(R.id.etNameItem);
		    	    			    	 Spinner thList=(Spinner)dialog.findViewById(R.id.thList);

		    	    			    	 String projName=et.getText().toString();
		    	    			    	 String thName=(String)thList.getSelectedItem();
		    	    			    	 
		    	    					projCnt= new ProjectControler(v.getContext());
		    	    					
		    	    					projId=projCnt.createProject(projName, thName,"");


			    	    	                if(projId<=0) {
			    	    	   	             
			    	    	                	String sameProject=getBaseContext().getString(R.string.sameNameProject);
			    	    	                	Toast.makeText(getBaseContext(), 
			    	    	   		              sameProject+" "+projName, 
			    	    	   		              Toast.LENGTH_LONG).show();
			    	    	                	
			    	    	                }
			    	    	                else{
			    	    	                	
			    	    	                	importFagusCitationFile(url,projId);
			    	    	                	
				    	    	                 
			    	    	                }
		    	    	                
		    	    	                
		    	    	              }
		    	    	             
		    	    });
		    	    
		    	    dialog.show();


		    	 
		    }
		   
		   
			 private void importFagusCitationFile(final String url, final long projId) {
				 
				 pd = ProgressDialog.show(this, getString(R.string.citationLoading), getString(R.string.citationLoadingTxt), true,false);

				                 Thread thread = new Thread(){
				  	        	   
					                 @Override
									public void run() {
					               	  
					                	 importFagusThread(url,projId);
					               	  
					                 }
					           };
					           
					           
				   thread.start();
				}	
			 
			 
			   private void importFagusThread(String url,long projId){
				   
				   fR=new FagusReader(this, projId);
				
				   FagusXMLparser fXML=new FagusXMLparser(fR);
				   fXML.readXML(this,url, false);
				   
			   	 	 if(fXML.isError()) handler.sendEmptyMessage(-1);
		       	 	 else handler.sendEmptyMessage(0);
				
	      			

			   }
			   
				 private Handler handler = new Handler() {

						@Override
						public void handleMessage(Message msg) {	
							
							pd.dismiss();
							dialog.dismiss();

							
							if(msg.what==0){
								
								Toast.makeText(getBaseContext(), 
										String.format(getString(R.string.projSuccesCreatedWithCitations),fR.getNumSamples()), 
				   	                    Toast.LENGTH_SHORT).show();	
								
								Intent intent = new Intent();
						    	
								Bundle b = new Bundle();
								b.putLong("projId", projId);
								intent.putExtras(b);

								setResult(1, intent);
								
								
						        finish();
					        
							}
							
							else{
								
								projCnt.removeProject(projId);
								
								Toast.makeText(getBaseContext(), 
				   	                    getString(R.string.errorCitationFile), 
				   	                    Toast.LENGTH_SHORT).show();	
								
								
							}

						}
					};


}
