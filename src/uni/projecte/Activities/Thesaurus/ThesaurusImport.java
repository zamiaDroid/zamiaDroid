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

package uni.projecte.Activities.Thesaurus;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.List;

import uni.projecte.R;
import uni.projecte.R.id;
import uni.projecte.R.layout;
import uni.projecte.R.string;
import uni.projecte.controler.PreferencesControler;
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
import android.widget.Toast;

public class ThesaurusImport extends Activity {
	
	
	   private PreferencesControler pC;
	  
	   private List<String> elementsBVegana = null;
	   private List<String> elementsPlain = null;

	   private ListView fileList;
	   private ListView plainList;
	   
	   
	   private String url;
	   private long thId;
	   
	   private ProgressDialog pd;
	   
	   private String thName;
	   private String fileNameComp;
	   
	    public static final int DIALOG_DOWNLOAD_PROGRESS = 0;
	   
	
	    @Override
	   public void onCreate(Bundle state) {
	        super.onCreate(state);
	        
	        Utilities.setLocale(this);

	        setContentView(R.layout.thesaurus_import_local);
	
	        
	        pC=new PreferencesControler(this);
	        
	        fileList = (ListView)findViewById(R.id.thList);
	        plainList=(ListView)findViewById(R.id.thListPlain);

	        
	        if(isSdPresent()){ 
	        	
	        	fillXMLFileList(new File(Environment.getExternalStorageDirectory()+"/"+pC.getDefaultPath()+"/Thesaurus/").listFiles(new XMLFilter()),fileList);

	        	fillFileList(new File(Environment.getExternalStorageDirectory()+"/"+pC.getDefaultPath()+"/Thesaurus/").listFiles(new EXCLUDE_XMLFilter()),plainList);
 
	        }
	        else {
	        	
	        	Toast.makeText(getBaseContext(), R.string.noSdAlert, Toast.LENGTH_SHORT).show();
	        	
	        }
	        
	        fileList.setOnItemClickListener(theListListener);
	        plainList.setOnItemClickListener(plainListListener);
	       
	        
	        
	   }
	
	@Override
	    protected void onStop(){
	       super.onStop();
	    
	      // Save user preferences. We need an Editor object to
	      // make changes. All objects are from android.context.Context
	    
	    }
	   
	   @Override
	protected void onRestart(){
		   
		   super.onRestart();
		   reloadThList();
		   
	   }
	   
	   private void reloadThList(){
		   
		   if(isSdPresent()){ 
	        	
	        	fillXMLFileList(new File(Environment.getExternalStorageDirectory()+"/"+pC.getDefaultPath()+"/Thesaurus/").listFiles(new XMLFilter()),fileList);
	        	fillFileList(new File(Environment.getExternalStorageDirectory()+"/"+pC.getDefaultPath()+"/Thesaurus/").listFiles(new EXCLUDE_XMLFilter()),plainList);

		  } 
	      else {
	        	
	        	Toast.makeText(getBaseContext(), 
	                    R.string.noSdAlert, 
	                    Toast.LENGTH_SHORT).show();
	        	
	        	
	        }
		   
	   }
	   
	 
	  public static boolean isSdPresent() {

	      return android.os.Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED);
	
	  }

	   
	   private void fillXMLFileList(File[] listFiles, ListView lv) {
			
	        elementsBVegana = new ArrayList<String>();
	      //  elements.add(getString(R.string.root));
	        
	        for( File archivo: listFiles)
	        	elementsBVegana.add(archivo.getName());
	       
	        ArrayAdapter<String> listaArchivos= new ArrayAdapter<String>(this, R.layout.row, elementsBVegana);
	        lv.setAdapter(listaArchivos);
	    

	        
}
	   
	   private void fillFileList(File[] listFiles, ListView lv) {
			
	        elementsPlain = new ArrayList<String>();
	      //  elements.add(getString(R.string.root));
	        
	        for( File archivo: listFiles)
	        	elementsPlain.add(archivo.getName());
	       
	        ArrayAdapter<String> listaArchivos= new ArrayAdapter<String>(this, R.layout.row, elementsPlain);
	        lv.setAdapter(listaArchivos);
	    
	
}
	   
	 
	      class XMLFilter implements FilenameFilter {
	    	  
	    	  
	  	              public boolean accept(File dir, String name) {
	  
	                      return (name.endsWith(".xml"));
	  
	              }
	  
	      }
	      
	      class EXCLUDE_XMLFilter implements FilenameFilter {
	    	  
	    	      public boolean accept(File dir, String name) {

                  return !(name.endsWith(".xml"));

          }

  }

	   
	 public OnItemClickListener plainListListener = new OnItemClickListener() {
	  	    
	  	    public void onItemClick(android.widget.AdapterView<?> parent, View v, int position, long id) {
	  	    		
	  	    	  int IDFilaSeleccionada = position;

	  	              File archivo = new File(Environment.getExternalStorageDirectory()+"/"+pC.getDefaultPath()+"/Thesaurus/"+elementsPlain.get(IDFilaSeleccionada));
	  	              if (archivo.isDirectory()){
	  	            	  fillFileList(archivo.listFiles(new XMLFilter()),fileList);
	  	            	  
	  	              }
	  	               else{
	  	            	   
	      		            url= archivo.getAbsolutePath();
	      		            String fileName=archivo.getName();
	      		            fileName=fileName.substring(0, fileName.lastIndexOf('.'));
	      		            
	      		            Intent intent = new Intent(getBaseContext(), ThesaurusPlainImport.class);
	      		            
	      		            Bundle b = new Bundle();
	      		       
	      		            b.putString("filePath", url);
	      		            intent.putExtras(b);
	      		            
	      		            b = new Bundle();
	      		          
	      		            b.putString("fileName", fileName);
	      		            intent.putExtras(b);
	      		        
	      		            startActivityForResult(intent,1);
	  	    
	  	               }
	  	    }   
	  	
	  	    };
	  	    
	      
 
	  	    public OnItemClickListener theListListener = new OnItemClickListener() {
	    
	     	
	  	    	public void onItemClick(android.widget.AdapterView<?> parent, View v, int position, long id) {
	    		
	  	    		int IDFilaSeleccionada = position;
	
		              File archivo = new File(Environment.getExternalStorageDirectory()+"/"+pC.getDefaultPath()+"/Thesaurus/"+elementsBVegana.get(IDFilaSeleccionada));
		              if (archivo.isDirectory()){
		            	  fillFileList(archivo.listFiles(new XMLFilter()),fileList);
		            	  
		              }
		               else{
		            	   
	    		            url= archivo.getAbsolutePath();
	    		            
	    		            fileNameComp=archivo.getName();            
	    		            String name=fileNameComp.replace(".xml", "");
	
	    		            createThDialog(name,"");   
		    
		               }
		    }   
	
	    };
	    
	    
	   
		private void createThDialog(String fileName,final String filumLetter) {
	        
		  	final Dialog dialog;
	        
	  		        	
	        	//Context mContext = getApplicationContext();
	    	   	dialog = new Dialog(this);
	    	   	
	        	dialog.setContentView(R.layout.thesaurus_creation_dialog);
	    	   	dialog.setTitle(R.string.thName);
	    	   	
	    	   	Button createProject = (Button)dialog.findViewById(R.id.bCreateTh);
	    		
	    	   	EditText name=(EditText)dialog.findViewById(R.id.etNameItem);
	    	   	name.setText(fileName);
	    	   	
	    	   	
	    	    createProject.setOnClickListener(new Button.OnClickListener(){
	    	    	             
	    	    	
	    	    	public void onClick(View v)
	    	    	              {

	    	    	                 EditText et=(EditText)dialog.findViewById(R.id.etNameItem);
	    	    	                 
	    	    	                 thName=et.getText().toString();
	    	    	           
	    	    	                 ThesaurusControler thCntr= new ThesaurusControler(v.getContext());
	    	    	                 thId=thCntr.createThesaurus(thName,"",filumLetter,fileNameComp,"localBvegana");
	    	    	                 
	    	    	                 if(thId>0){
	    	    	                	 
	    	    	                     importTh();
		    	    	                 dialog.dismiss();
		    	    	                 
	    	    	                	 
	    	    	                 }
	    	    	                 else{
	    	    	                	 
		    	    	                String sameTh=getBaseContext().getString(R.string.sameThName);

	    	    	                 	Toast.makeText(getBaseContext(), 
			    	    	   		              sameTh+" "+thName, 
			    	    	   		              Toast.LENGTH_LONG).show();
			    	    	                	
	    	    	                	 
	    	    	                 }
	    	    	                 
	    	    	      
	    	    	                 
	    	    	        
	    	    	            	 
	    	    	              }
	    	    	             
	    	    });
	    	    
	    	    dialog.show();


	    	 
	    } 

	 private void importTh() {
		 
		 pd = ProgressDialog.show(this, getString(R.string.thLoading), getString(R.string.thLoadingTxt), true,false);
		 
		                 Thread thread = new Thread(){
		  	        	   
		  	        	   
			                 @Override
							public void run() {
			               	  
			               	  
			                	importThThread(); 
			               	  
			                 }
			           };
			           
			           
		   thread.start();
		 
		}
	 
	 private void importThThread(){
		 
		 
		 ThesaurusControler thCntr= new ThesaurusControler(this);
		 
		 thName=thName.replace(".", "_");

		 boolean error=thCntr.addThItems(thId,thName, url);

		if(!error) handler.sendEmptyMessage(0);
		else handler.sendEmptyMessage(1);
		 


		 
	 }
	 
	 
	 
	 private Handler handler = new Handler() {

			@Override
			public void handleMessage(Message msg) {
				
				
				pd.dismiss();
				
				if (msg.what==0){
					
					Intent intent = new Intent();
			    	
					Bundle b = new Bundle();
					b.putString("thName", thName);
								
					intent.putExtras(b);

					setResult(1, intent);
					
				}
				
				else{
					
				  	Toast.makeText(getBaseContext(), 
  	   		              "Wrong File", 
  	   		              Toast.LENGTH_LONG).show();
					
				}
				
		
		        finish();


			}
		};
	 
	 
		       
		       @Override
			protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
		           
		    	   
		    	   if(intent!=null){
		    	   
		           	
			           switch(requestCode) {
			           case 0 :
			           	
			    
			           	break;
			               
			           case 1 :
			           	
			        	   if(resultCode==1){
			        		   
			        		   setResult(1, intent);
				           	   finish();
			        		   
			        	   }
			        	  
			            
			            default:
	
	
			           }
			           
			           
			       }
		    	   
		    	   super.onActivityResult(requestCode, resultCode, intent);
		    	   
		       }
		       

	        
	    }
	    
	        
