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
import uni.projecte.dataLayer.CitationManager.Fagus.FagusReader;
import uni.projecte.dataLayer.CitationManager.Fagus.FagusXMLparser;
import uni.projecte.dataTypes.Utilities;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;




public class CitationImport extends Activity{
	
	
	public final static int ZAMIA_IMPORT = 1;

	
	private List<String> elements = null;
	private ListView fileList;
	private FagusReader fR;
	   
	private long projectId;
	private PreferencesControler prefCnt;
	private ProgressDialog pd;
    private ProgressDialog pdCitationImport;
	  
	
	/* Import format {"Zamia","Fagus"} */
	private String format;
	

	private int citationCount;
	private String filePath;
		    

	   
	
	  @Override
	public void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);

	        Utilities.setLocale(this);
	        setContentView(R.layout.project_citation_import);
	        
	        
	        prefCnt=new PreferencesControler(this);

	        projectId=getIntent().getExtras().getLong("id");
	        format=getIntent().getExtras().getString("format");

	        fileList = (ListView)findViewById(R.id.projectList);
	        TextView tvTitle=(TextView)findViewById(R.id.tvImportCitationsTitle);
	        tvTitle.setText(getString(R.string.zamiaImportTitle)+" "+format);
	        
	        TextView tvPath=(TextView)findViewById(R.id.tvImportPath);
	        tvPath.setText("/"+prefCnt.getDefaultPath()+"/Citations/");
	        
	        TextView tvPathText=(TextView)findViewById(R.id.tvImportPathInitial);
	        tvPathText.setText(Html.fromHtml(getString(R.string.citationsAtDir)));
	   
	        	        
	        
	        if(isSdPresent()) fillFileList(new File(prefCnt.getCitationsPath()).listFiles(new XMLFilter()));
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
		   
		   if(isSdPresent()) fillFileList(new File(Environment.getExternalStorageDirectory()+"/"+prefCnt.getDefaultPath()+"/Citations/").listFiles(new XMLFilter()));
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
		        //elements.add(getString(R.string.root));
		        
		        for( File archivo: listFiles)
		            elements.add(archivo.getName());
		       
		        ArrayAdapter<String> listaArchivos= new ArrayAdapter<String>(this, R.layout.row, elements);
		        fileList.setAdapter(listaArchivos);
		    
		
	}
	   
	    private void zamiaImporter(String fileName) {
	    	
	    	
	    	Intent myIntent = new Intent(this, CitationImportZamia.class);
        	myIntent.putExtra("id", projectId);
        	myIntent.putExtra("file", fileName);

            startActivityForResult(myIntent,ZAMIA_IMPORT);
			
	    } 
	
	  
	  public OnItemClickListener theListListener = new OnItemClickListener() {
		    
		    public void onItemClick(android.widget.AdapterView<?> parent, View v, int position, long id) {
		    		
		    	  int IDFilaSeleccionada = position;
		
		    	  File archivo = new File(elements.get(IDFilaSeleccionada));
		           if (archivo.isDirectory()){
		            	  
		        	   fillFileList(archivo.listFiles(new XMLFilter()));
		            	  
		            }
		            else{
		            
		            	if(format.equals("Zamia")){
		            		   
		            		zamiaImporter(prefCnt.getCitationsPath()+archivo.getPath());
		            		   
		            	}
		            	else {
		            		
		            		filePath=prefCnt.getCitationsPath()+archivo.getPath();
		            		checkFagusFile();
		            		
		            	}
		            }
		    

		    	}   
		
		    };


		    
	 
		    class XMLFilter implements FilenameFilter {
		    	  
		    	  
		              public boolean accept(File dir, String name) {
	
		                return (name.endsWith(".xml"));
	
		        }
	              
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
								 
								citationCount=fagusXMLparser.preReadXML(getBaseContext(), filePath);
								
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
							
		            		importFagus();
										
							
						}
						 else{

							String message=String.format(getString(R.string.citationWrongFileFormat), format);
							
							Utilities.showToast(message, getBaseContext());
						 
							 				 
							 
						 }
						
						
					}
					
			   };
		    
		    
		    private void importFagusThread(){
		    	
         	   Log.d("Import","Format: Fagus (A) Action: Importing citations");

         	   fR=new FagusReader(this,projectId);

         	   FagusXMLparser fXML=new FagusXMLparser(fR,handler);
	       	   fXML.readXML(this, filePath, false);
	       	 	 
	       	   if(fXML.isError()) handler.sendEmptyMessage(-1);
	       	   else handler.sendEmptyMessage(0);
	       	 	 
          	   Log.d("Citations","Format: Fagus (A) Action: Citations Imported (error:"+fXML.isError()+")");


    		 
			 }
			 
			 private Handler handler = new Handler() {

					@Override
					public void handleMessage(Message msg) {
						
						
						if(msg.what<0){
							
							pdCitationImport.dismiss();

							Toast.makeText(getBaseContext(), 
			   	                    "Error a l'arxiu de citacions", 
			   	                    Toast.LENGTH_SHORT).show();	
							
							
						}
						else if(msg.what==0){
							
							pdCitationImport.dismiss();

							String numCit= getString(R.string.numCitationsImported);
								
							Toast.makeText(getBaseContext(), 
			   	                    fR.getNumSamples()+" "+numCit, 
			   	                    Toast.LENGTH_SHORT).show();	
							finish();
							
						}
						
						else{
							
							pdCitationImport.incrementProgressBy(1);
															
						}
					
				        


					}
				};
		    
			 private void importFagus() {
				 
				String progressMessage=getString(R.string.citationLoadingTxt);
				 
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
					               	  
					                	 importFagusThread();
					               	  
					                 }
					           };
					           
					           
				   thread.start();
				 
				}
		    
			 
			 @Override
			protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
			        // super.onActivityResult(requestCode, resultCode, intent);
			    	
			        	
			         switch(requestCode) {
			             
			                 
			         case ZAMIA_IMPORT :
			        	 
			        	 if(intent!=null){
			        		 
			        		int numCitations=intent.getExtras().getInt("numCitations");
			        		
			        		if(numCitations>0){
				        	
			        			finish();
				        		
			        		}
			        		 
			        		 
			        	 }
			            
			             break;
			             
	

			           default:
			             	
			             	
			   
			         }
			         

			     }	 


}
