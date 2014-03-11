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
import android.text.Html;
import android.view.View;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

/*
 *  @author utoPiC
 *  
 * This activity allows to create a new project with the citations'
 * file chosen from the list.
 * 
 * Formats:
 * 	- Zamia
 *  - Fagus
 *
 */

public class CitationProjectImport extends Activity{
	
	   private static final int ZAMIA_IMPORT = 0;
	   
	   private List<String> elements = null;
	   private ListView fileList;
	   private String url;
	   private Button createProject;

	   private PreferencesControler prefCnt;
	   private ProgressDialog pdCitationImport;
	   
	   private FagusReader fR;
	   
	   private Dialog dialog;
	   
		/* Import format {"Zamia","Fagus"} */
	   private String format;
	   
	   private ProjectControler projCnt;

	   private long projId=-1;
	   private String fileName;


	   	   

	
	  @Override
	public void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        
	        Utilities.setLocale(this);

	        setContentView(R.layout.project_citation_import);
	        
	        prefCnt=new PreferencesControler(this);
	        
	        format=getIntent().getExtras().getString("format");
	        
	        fileList = (ListView)findViewById(R.id.projectList);
	        
	        TextView tvTitle=(TextView)findViewById(R.id.tvImportCitationsTitle);
	        tvTitle.setText(getString(R.string.zamiaImportTitle)+" "+format);
	        
	        TextView tvPath=(TextView)findViewById(R.id.tvImportPath);
	        tvPath.setText("/"+prefCnt.getDefaultPath()+"/Citations/");
	        
	        TextView tvPathText=(TextView)findViewById(R.id.tvImportPathInitial);
	        tvPathText.setText(Html.fromHtml(getString(R.string.citationsAtDir)));
	          

	        
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
		   
		   super.onRestart();
		   
		 /*  if(isSdPresent()) {
			   
			   fillFileList(new File(Environment.getExternalStorageDirectory()+"/"+prefCnt.getDefaultPath()+"/Projects/").listFiles(new XMLFilter()));
			   fileList.setOnItemClickListener(theListListener);

		   
		   }
		   else {
	        	
	        	Toast.makeText(getBaseContext(), 
	                    R.string.noSdAlert, 
	                    Toast.LENGTH_SHORT).show();
	        	
	        	
	        }*/
		   
	   }
	   
	  
	  public OnItemClickListener theListListener = new OnItemClickListener() {
		    

			public void onItemClick(android.widget.AdapterView<?> parent, View v, int position, long id) {
		    	

		    	File archivo = new File(elements.get(position));
		    	if (archivo.isDirectory()){
		    		
		    		fillFileList(archivo.listFiles(new XMLFilter()));

		    	}
		    	else{
		    		
		    		if(format.equals("Zamia")){
	            		   
	            		zamiaImporter(prefCnt.getCitationsPath()+archivo.getPath());
	            		   
	            	}
	            	else {
	            		
	            		url= prefCnt.getCitationsPath()+archivo.getPath();
	            		fileName=archivo.getName();
	            		
	            		checkFagusFile();
	            	
	            	}

		    	}

		    }   
		
		    };

	private int citationCount;
		    
		    

		   private void createProjectDialog() {
		        
		        
			  	
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
		    	 
		    	   	name.setText(fileName.replace(".xml", ""));

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
			    	    	                	
			    	    	                	importFagusCitationFile();
			    	    	                	
				    	    	                 
			    	    	                }
		    	    	                
		    	    	                
		    	    	              }
		    	    	             
		    	    });
		    	    
		    	    dialog.show();


		    	 
		    }
		   
		   private void checkFagusFile(){
			   
				 String progressMessage=getString(R.string.pdCheckingCitFile);
			   
				 pdCitationImport = new ProgressDialog(this);
				 pdCitationImport.setCancelable(true);
				 pdCitationImport.setMessage(progressMessage);
				 pdCitationImport.show();
				 
			       Thread thread = new Thread(){
	  	        	   
		                 @Override
						public void run() {
		               	  
						   
						   	FagusXMLparser fagusXMLparser = new FagusXMLparser(null,null);
							 
							citationCount=fagusXMLparser.preReadXML(getBaseContext(), url);
							
							handlerCheckFagusFile.sendEmptyMessage(0);
				
             	  
		                 }
		           };
		           
		           
		           thread.start();
	 
			   
		   }
		   
		   private Handler handlerCheckFagusFile = new Handler() {

				@Override
				public void handleMessage(Message msg) {
					
					pdCitationImport.cancel();

					if(citationCount>0){
						
						createProjectDialog();   
									
						
					}
					 else{

						String message=String.format(getString(R.string.citationWrongFileFormat), format);
						
						Utilities.showToast(message, getBaseContext());
					 
						 				 
						 
					 }
					
					
				}
				
		   };
		   
		   
			 private void importFagusCitationFile() {

				 String progressMessage=getString(R.string.citationLoading);
				 
				 pdCitationImport = new ProgressDialog(this);
				 pdCitationImport.setCancelable(true);
				 pdCitationImport.setMessage(progressMessage);
						
					 pdCitationImport.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
					 pdCitationImport.setProgress(0);
					 pdCitationImport.setMax(citationCount);
					 
					 pdCitationImport.show();

					 
					                 Thread thread = new Thread(){
					  	        	   
						                 @Override
										public void run() {
						        
						                	 importFagusThread(url,projId);
						               	  
						                 }
						           };
						           
						           
					   thread.start();
					 
				 }
			
			 
			    private void zamiaImporter(String fileName) {
			    		    	
			    	Intent myIntent = new Intent(this, CitationImportZamia.class);
		        	myIntent.putExtra("file", fileName);

		            startActivityForResult(myIntent,ZAMIA_IMPORT);
					
			    } 
			 
			 
			   private void importFagusThread(String url,long projId){
				   
				   fR=new FagusReader(this, projId);
				
				   FagusXMLparser fXML=new FagusXMLparser(fR,handler);
				   fXML.readXML(this,url, false);
				   
			   	 	 if(fXML.isError()) handler.sendEmptyMessage(-1);
		       	 	 else handler.sendEmptyMessage(0);
				
	      			

			   }
			   
				 private Handler handler = new Handler() {

						@Override
						public void handleMessage(Message msg) {	
							
													
							if(msg.what<0){
								
								pdCitationImport.dismiss();
								dialog.dismiss();
								
								projCnt.removeProject(projId);
								
								Toast.makeText(getBaseContext(), 
				   	                    getString(R.string.errorCitationFile), 
				   	                    Toast.LENGTH_SHORT).show();	
								
								
							}
							
							
							else if(msg.what==0){
								
								pdCitationImport.dismiss();
								dialog.dismiss();
								
								
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
								
								pdCitationImport.incrementProgressBy(1);
																
							}

						}
					};
					
					
					  public static boolean isSdPresent() {

					      return android.os.Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED);
					
					  }
					  
					  
					  private void fillFileList(File[] listFiles) {
						
						        elements = new ArrayList<String>();
						        //elements.add(getString(R.string.root));
						        
						        for( File archivo: listFiles)
						            elements.add(archivo.getName());
						       
						        ArrayAdapter<String> listaArchivos= new ArrayAdapter<String>(this, R.layout.row, elements);
						        fileList.setAdapter(listaArchivos);
						    
						
					}
					  
					    class XMLFilter implements FilenameFilter {
					    	  
					    	  
				              public boolean accept(File dir, String name) {
			
				                return (name.endsWith(".xml"));
			
				        }
			              
				    }
					    
					    @Override
						protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
					        super.onActivityResult(requestCode, resultCode, intent);
					        
					    
					        switch(requestCode) {          	
					        
						        case ZAMIA_IMPORT:
						
						        	if(intent!=null){
						    		
							        	Intent setProject = new Intent();
							        	Long createdProjectId=intent.getLongExtra("projId",-1);
							        							        	
										Bundle b = new Bundle();
										b.putLong("projId", createdProjectId);
										setProject.putExtras(b);
	
										setResult(1, intent);
																        	
							          	finish();
							          	
						        	}
						                        
						            break;
						                
						        default:
						            	
						     
						    }
					        
					    }					    
				    		    


}
