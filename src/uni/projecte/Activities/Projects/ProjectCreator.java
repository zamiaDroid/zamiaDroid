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

import java.util.ArrayList;
import java.util.Iterator;

import uni.projecte.R;
import uni.projecte.controler.ProjectSecondLevelControler;
import uni.projecte.controler.ThesaurusControler;
import uni.projecte.dataLayer.ProjectManager.FieldCreator;
import uni.projecte.dataTypes.ProjectField;
import uni.projecte.dataTypes.Utilities;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;


public class ProjectCreator extends Activity{
	
	   private ListView listAttributesPres;
	   private EditText projName;
	   private Spinner spinnerTh;
	   private CheckBox checkBox;
	   private ProjectSecondLevelControler rsCont;
	   private long projId;
	   
	   private long subPojId=0;
	   

	   private FieldCreator fc;
	   
	   private ProgressDialog pd;

		
		static final int IMPORT_FAGUS = Menu.FIRST;
		static final int IMPORT_CITACIONS = Menu.FIRST+1;
		static final int CREATE_EXAMPLE=Menu.FIRST+2;


	
	   @Override
	public void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        
	        Utilities.setLocale(this);
	        setContentView(R.layout.researchcreation);

	        ThesaurusControler thCont= new ThesaurusControler(this);
	        

	        //getting view items     
	        Button bAfegirAtribut = (Button)findViewById(R.id.bAfegirAtribut);
	        bAfegirAtribut.setOnClickListener(addAttributeListener);

	        
	        Button bProjectCreation = (Button)findViewById(R.id.bCrearEstudi);
	        bProjectCreation.setOnClickListener(createProjListener);
	       
	        projName=(EditText)findViewById(R.id.eTextEstudi);
	        listAttributesPres = (ListView)findViewById(R.id.llistaAtributs);
	        spinnerTh = (Spinner) findViewById(R.id.thList);
	        
	        checkBox = (CheckBox) findViewById(R.id.chNoTh);
	        checkBox.setOnClickListener(new View.OnClickListener() 
	        {
	            public void onClick(View v) {
	            	
	                if (((CheckBox)v).isChecked()) {
	                	
	                	spinnerTh.setVisibility(View.VISIBLE);
	                	
	                	addTaxThesaurusField();
	                	
	                	
	                }	                	
	                else{
	                	
	                	spinnerTh.setVisibility(View.INVISIBLE);
	                	
	                	removeTaxThesaurusField();

	                }
	            }
	        });


	        //binding Thesaurus List to Spinner view
	        
	    	ArrayAdapter<String> dtAdapter = new ArrayAdapter<String>(this,
                    android.R.layout.simple_spinner_item, thCont.getThList());
        
        
 		   dtAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
 		   
 		   spinnerTh.setAdapter(dtAdapter);

 		   fc=new FieldCreator(this, listAttributesPres, projId);
 		   
 		  fc.createTaxonField();
	   }
	   
	
	   
	protected void removeTaxThesaurusField() {

		fc.removeTaxonField();

		
	}



	protected void addTaxThesaurusField() {
	
		fc.createTaxonField(); 
	}



	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
	        if ((keyCode == KeyEvent.KEYCODE_BACK)) {
	    		
	        	
	        	AlertDialog.Builder builder = new AlertDialog.Builder(this);
		    	
		    	builder.setMessage(R.string.backFromProjectCreation)
		    	       .setCancelable(false)
		    	       .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
		    	           public void onClick(DialogInterface dialog, int id) {
		    
		    	        	   finish();
	        	   
		    	           }

						
		    	       })
		    	       .setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
		    	           public void onClick(DialogInterface dialog, int id) {
		    	                
		    	        	   
		    	        	   dialog.dismiss();
	 
		    	           }
		    	       });
		    	
		    	AlertDialog alert = builder.create();
		    	
		    	alert.show();
	        		
		        return true;

	        	
	        }
	        
	        return false;

	    }

	   
	    private OnClickListener addAttributeListener = new OnClickListener()
	    {
	        public void onClick(View v)
	        {                     
	        	
	        	final CharSequence[] items = getBaseContext().getResources().getStringArray(R.array.newFieldTypes);
	        	
	        	

	        	AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
	        	builder.setTitle(R.string.fieldTypeMessage);
	        	builder.setSingleChoiceItems(items, -1, new DialogInterface.OnClickListener() {
	        		
	        	    public void onClick(DialogInterface dialog, int item) {
	        	    	
	        	    	dialog.dismiss();
	        	    	
	        	        if(items[item].equals(items[0])){
	        	        	
	        	        	// camps lliures o simples
	        	        	fc.createPredFieldDialog("simple",messageHandler);
	        	        	
	        	        }
	        	        else if(items[item].equals(items[1])){
	        	        	
	        	        	// camps pred-field
	        	        	fc.createComplexFieldDialog(messageHandler);
	        	        	
	        	        }
	        	        /*else if(items[item].equals(items[2])){
	        	        	
	        	        	//camp fotografia
	        	        	fc.createPredFieldDialog("photo",messageHandler);

	        	        }*/
	        	        else if(items[item].equals(items[2])){
	        	        	
	        	        	//Photo field
	        	        	if(fc.repeatedFieldType("multiPhoto")) fc.repeatedToast("multiPhoto");
	        	        	else fc.createPredFieldDialog("multiPhoto",messageHandler);

	        	        }
	        	        else if(items[item].equals(items[3])){
	        	        	
	        	        	//camp polygon
	        	        	if(fc.repeatedFieldType("polygon")) fc.repeatedToast("polygon");
	        	        	else fc.createPredFieldDialog("polygon",messageHandler);

	        	        }
	        	        else{
	        	        	
	        	        	//camps de dos nivells
	        	        	//fc.createPredFieldDialog("secondLevel");
	        	        	if(fc.repeatedFieldType("secondLevel")) fc.repeatedToast("secondLevel");
	        	        	else fc.createPredFieldDialog("secondLevel",messageHandler);

	        	        }
	        	    }
	        	});
	        	AlertDialog alert = builder.create();
	        	alert.show();
	        	
	     

	        }
	    };
	    
		   private Handler messageHandler = new Handler() {

			      @Override
			      public void handleMessage(Message msg) {  
			          switch(msg.what) {
			            
			          case 0:
			          	
			          
			          }
			      }

			  };
		
		protected boolean fieldNameExists(String fieldName,
				ArrayList<ProjectField> objFieldList) {
			
			return false;
		}
		
		
		/*
		 * This Method gets the project name and the created Attributes or Fields and creates a new Project in the system
		 * 
		 * It also takes into account the chosen Thesaurus
		 * 
		 */
	    
	    private OnClickListener createProjListener = new OnClickListener()
	    {
	        public void onClick(View v)
	        {                    
	        	
	        		String projNameTxt=projName.getText().toString();
	        	 	
	        	    if (!projNameTxt.equals("")){
	        	
	        	    	if (fc.getFieldList().isEmpty()){
	        	    		
	        	    		 Toast.makeText(getBaseContext(), R.string.projFieldsMissing, 
	        		   	              Toast.LENGTH_LONG).show();
	        	    		
	        	    	}   
	        	    	
	        	    	else{
	        	    		
	        	    		rsCont=new ProjectSecondLevelControler(v.getContext());
	        	    		

	        	    		if(checkBox.isShown()){
	        	    			
	        	    			Spinner thList=(Spinner)findViewById(R.id.thList);
	        	    			
	        	    			if(thList.getCount()==0){
	        	    				
	        	    				projId=rsCont.createProject(projName.getText().toString(), "", "");

	        	    			}
	        	    			else{
	        	    				
	        	    				projId=rsCont.createProject(projName.getText().toString(), thList.getSelectedItem().toString(), "");
	        	    			}

	        	    		}
	        	    		else{
	        	    			
		        	    		projId=rsCont.createProject(projName.getText().toString(), "", "");

	        	    		}
	        	    		
    	    				
	        	    		if (projId>0){
	        	    			
	        	    			createProjectThreadCreator(projId);
	        	    		
	        	    		}
	        	    		
	        	    		else{
	        	    			
	    	                	String sameProject=getBaseContext().getString(R.string.sameNameProject);

	        	    	      	Toast.makeText(getBaseContext(), 
	    	    	   		              sameProject+" "+projName.getText().toString(), 
	    	    	   		              Toast.LENGTH_LONG).show();
	        	    		}
	        	    		
	        	    	}
	        	    	
	        	    }
	        	    
	        	    else {
	        	    	
		        	    Toast.makeText(getBaseContext(), R.string.projNameMissing, 
		   	              Toast.LENGTH_LONG).show();
	        	    	
	        	    }

	          
	        }
	    };
	    
	    
	    private void createProjectThreadCreator(final long projId) {
			 
			 pd = ProgressDialog.show(this, getBaseContext().getString(R.string.projCreationLoading), getBaseContext().getString(R.string.projCreationTxt), true,false);

			                 Thread thread = new Thread(){
			  	        	   
				                 @Override
								public void run() {
				               	  
				                	 createProjectThread(projId);
				               	  
				                 }
				           };
				           
				           
			   thread.start();
			}	
		 
		 
		   private void createProjectThread(long projId){
			   
			   Iterator<ProjectField> itr = fc.getObjFieldList().iterator();
	    		rsCont.startTransaction();
	    		
	    		while(itr.hasNext()){
	    		
	    			ProjectField at=itr.next();
	    			
	    			ArrayList <String> predValues=at.getPredValuesList();
	    			
	    			Iterator<String> itrPreValues = predValues.iterator();
	    			
	    			int numPred=predValues.size();
	    			
	    			long fieldId;
	    			
	    			if(numPred<1){
	    				
	    				
	    				if(at.getType()==null){
	    					
	    					fieldId=rsCont.addProjectField(projId, at.getName(),at.getLabel(), at.getType(),at.getValue(),"simple","ECO");
	    					
	    				}
	    				else if(at.getType().equals("thesaurus")){
	    					
	    					fieldId=rsCont.addProjectField(projId, at.getName(),at.getLabel(), at.getDesc(),at.getValue(),"thesaurus","A");

	    				}
	    				else if(at.getType().equals("CitationNotes")){
	    					
	    					fieldId=rsCont.addProjectField(projId, at.getName(),at.getLabel(), at.getDesc(),at.getValue(),"simple","A");

	    				}
	    				else if (at.getType().equals("photo")){
	    					
	    					fieldId=rsCont.addProjectField(projId, at.getName(),at.getLabel(), at.getDesc(),at.getValue(),"photo","ECO");
	    					
	    				}
	    				else if (at.getType().equals("multiPhoto")){
	    					
	    					fieldId=rsCont.addProjectField(projId, at.getName(),at.getLabel(), at.getDesc(),at.getValue(),"multiPhoto","ECO");
	    					
	    					subPojId++;
	    					
	    					rsCont.updateSubFieldId(-subPojId, fieldId);
	    					
	    				}
	    				else if (at.getType().equals("polygon")){
	    					
	    					fieldId=rsCont.addProjectField(projId, at.getName(),at.getLabel(), at.getDesc(),at.getValue(),"polygon","ECO");
	    					
	    					subPojId++;
	    					
	    					rsCont.updateSubFieldId(-subPojId, fieldId);
	    					
	    				}
	    				else if (at.getType().equals("number")){
	    					
	    					fieldId=rsCont.addProjectField(projId, at.getName(),at.getLabel(), at.getDesc(),at.getValue(),"number","ECO");

	    				}
	    				else if (at.getType().equals("boolean")){
	    					
	    					fieldId=rsCont.addProjectField(projId, at.getName(),at.getLabel(), at.getDesc(),at.getValue(),"boolean","ECO");

	    				}
	    				else if (at.getType().equals("secondLevel")){
	    					
	    					fieldId=rsCont.addProjectField(projId, at.getName(),at.getLabel(), at.getDesc(),at.getValue(),"secondLevel","ECO");
	    					
	    					subPojId++;
	    						    					
	    					rsCont.updateSubFieldId(-subPojId, fieldId);
	    					
	    				}
	    				else{
	    					
	    					fieldId=rsCont.addProjectField(projId, at.getName(),at.getLabel(), at.getType(),at.getValue(),"simple","ECO");
	    				}
	    			}
	  
	    			else{
	    				
   	    			fieldId=rsCont.addProjectField(projId, at.getName(),at.getLabel(), at.getDesc(),at.getValue(),"complex","ECO");

	    				
	    			}


	   	    		while(itrPreValues.hasNext()){
	       	    		
	   	    			rsCont.addFieldItem(fieldId, itrPreValues.next());
	   	    			
	   	    		}

	    	
	    		}
	    		
	    		
	    		rsCont.addAutoFields(projId);
	    		

	    		rsCont.endTransaction();
	    		
	    		 
	
    	 	
			   handlerCreate.sendEmptyMessage(0);
    			

		   }
		   
			 private Handler handlerCreate = new Handler() {

					@Override
					public void handleMessage(Message msg) {	
			
						pd.dismiss();
						
						Toast.makeText(getBaseContext(), String.format(getString(R.string.projSuccesCreated), projName.getText().toString()), 
				   	              Toast.LENGTH_LONG).show();
						   
		        	    
						Intent intent = new Intent();
				    	
						Bundle b = new Bundle();
						b.putLong("projId", projId);
						intent.putExtras(b);

						setResult(1, intent);
	
				        finish();
	
				

					}
				};

	 
}
