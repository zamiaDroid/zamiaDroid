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

import uni.projecte.R;
import uni.projecte.R.array;
import uni.projecte.R.id;
import uni.projecte.R.layout;
import uni.projecte.R.string;
import uni.projecte.controler.PhotoControler;
import uni.projecte.controler.ProjectControler;
import uni.projecte.controler.ThesaurusControler;
import uni.projecte.dataLayer.ProjectManager.FieldCreator;
import uni.projecte.dataLayer.ProjectManager.ListAdapters.ProjectFieldListAdapter;
import uni.projecte.dataTypes.ProjectField;
import uni.projecte.dataTypes.Utilities;
import uni.projecte.maps.MarkerConfigurationDialog;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.renderscript.ProgramFragmentFixedFunction.Builder.Format;
import android.text.Html;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;



public class ProjectInfo extends Activity{
	
	   private ListView llista;

	   /* project*/
	   private long projId;
	   private String projectName;
	   private String projectTh;
	   
	   private ProjectControler projCnt;   
	   private ThesaurusControler thCnt;
	   private PhotoControler photoCnt;	   
	   
	   private TextView thName;
	   private FieldCreator fc;	   

	   private static final int ADD_FIELD=Menu.FIRST;
	   private static final int CHANGE_TH=Menu.FIRST+1;
	   private static final int ALLOW_SEC_EXTERNAL_STORAGE=Menu.FIRST+2;
	   private static final int CHANGE_MARKER=Menu.FIRST+3;

	   private ArrayList<ProjectField> cFields;
		

	@Override
	public void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        
	        Utilities.setLocale(this);
	        setContentView(R.layout.projectinfo);
	        
	        projectName=getIntent().getExtras().getString("projName");
	        projectTh=getIntent().getExtras().getString("projDescription");
	        projId= getIntent().getExtras().getLong("Id"); 

        	thCnt= new ThesaurusControler(this);
	        projCnt=new ProjectControler(this);
	        photoCnt=new PhotoControler(this);
	        
		    fc=new FieldCreator(this, projId);


	        TextView tip= (TextView)findViewById(R.id.tvRschName);
	        thName= (TextView)findViewById(R.id.tvProjTh);


	        tip.setText(Html.fromHtml("<b>"+getString(R.string.tvProjectName)+"</b> "+projectName));
	        
	        boolean thWorking=thCnt.checkThWorking(projectTh);

	        if(!thWorking) {
	        	
	        	thName.setTextColor(Color.RED);
	        	thName.setText(getString(R.string.projWithoutTh));
	        	
	        }
	        
	        else thName.setText(Html.fromHtml("<b>"+getString(R.string.tvDefaultTh)+"</b> "+projectTh)); 
	        
    
	        llista = (ListView)findViewById(R.id.lFields);
	        llista.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
	        llista.setItemsCanFocus(true);


	        fillFieldList();
	        
	          

	        //listener for item cliked more than 3 seconds
	       llista.setOnItemLongClickListener(theListLongListener);

	        
	        
	   }
   
	   public void fillFieldList(){
	
		   cFields=projCnt.getProjectFields(projId);	   
	       
		   // Now create an array adapter and set it to display using our row
	       ProjectFieldListAdapter fieldsAdapter = new ProjectFieldListAdapter(this, cFields,projCnt);
	        
	       llista.setAdapter(fieldsAdapter);
		   
	   }
	   
	   
	    @Override
		protected void onResume(){
			  
			  super.onResume();		  
			
			  fillFieldList();

		  }
	   
	   public OnItemLongClickListener theListLongListener = new OnItemLongClickListener() {
		    
		    public boolean onItemLongClick(android.widget.AdapterView<?> parent, final View v, int position, long id) {
		        
		    	AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
		    	
		    	
		    	builder.setMessage(R.string.deleteProjQuestion)
		    	       .setCancelable(false)
		    	       .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
		    	           public void onClick(DialogInterface dialog, int id) {
		    
		    	        	// removeResearch(rsName);
		    	        
		    	        	 //loadResearches();

		    	        	   
		    	           }
		    	       })
		    	       .setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
		    	           public void onClick(DialogInterface dialog, int id) {
		    	                
		    	        	   dialog.cancel();
		    	                
		    	           }
		    	       });
		    	AlertDialog alert = builder.create();
		    	
		    	alert.show();
		    	   
		    	   return true;
		    	
		    }
		    
		    
		    };
	    
	   
	   
		@Override
		public boolean onCreateOptionsMenu(Menu menu) {

	    	menu.add(0, ADD_FIELD, 0,R.string.mAddField).setIcon(android.R.drawable.ic_menu_add);
	    	menu.add(0, CHANGE_TH, 0,R.string.mChangeTh).setIcon(android.R.drawable.ic_menu_agenda);
	    	menu.add(0, CHANGE_MARKER, 0,R.string.changeMapMarker).setIcon(android.R.drawable.ic_menu_myplaces);

	    	if(photoCnt.hasSecondaryStorage()) {
	    		
	    		if(photoCnt.isSecondaryExternalStorageDefault(projId)){
	    			
		    		menu.add(0, ALLOW_SEC_EXTERNAL_STORAGE, 0,R.string.mUseExternalStorageDisabled).setIcon(android.R.drawable.ic_menu_set_as);

	    		}
	    		
	    		else{
	    			
		    		menu.add(0, ALLOW_SEC_EXTERNAL_STORAGE, 0,R.string.mUseExternalStorage).setIcon(android.R.drawable.ic_menu_set_as);

	    		}
	    		
	    		
	    	
	    	}
	    	
	    	return super.onCreateOptionsMenu(menu);
	    }
		
		@Override
		public boolean onPrepareOptionsMenu(Menu menu){
			
			if(photoCnt.hasSecondaryStorage()){
			
			
				if(photoCnt.isSecondaryExternalStorageDefault(projId)){
					
					menu.findItem(ALLOW_SEC_EXTERNAL_STORAGE).setTitle(R.string.mUseExternalStorageDisabled);
				}
				else{
					
					menu.findItem(ALLOW_SEC_EXTERNAL_STORAGE).setTitle(R.string.mUseExternalStorage);

				}
			
			}
	
			return super.onPrepareOptionsMenu(menu);
			
		}

		
		public void createFieldDialogType(){
			
			final CharSequence[] items = getBaseContext().getResources().getStringArray(R.array.newFieldTypes);

        	AlertDialog.Builder builder = new AlertDialog.Builder(this);
        	builder.setTitle(getBaseContext().getString(R.string.fieldTypeMessage));
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
	        	       /* else if(items[item].equals(items[2])){
	        	        	
	        	        	fc.createPredFieldDialog("photo",messageHandler);
	        	        	
	        	        }*/
	        	        else if(items[item].equals(items[2])){
	        	        	
	        	        	if(repeatedFieldType("multiPhoto")) fc.repeatedToast("multiPhoto");
	        	        	else fc.createPredFieldDialog("multiPhoto",messageHandler);
	        	        	
	        	        }
	        	        else if(items[item].equals(items[3])){
	        	        	
	        	        	if(repeatedFieldType("polygon")) fc.repeatedToast("polygon");
	        	        	else fc.createPredFieldDialog("polygon",messageHandler);
	        	        	
	        	        }
	        	        else{
	        	        	
	        	        	if(repeatedFieldType("secondLevel")) fc.repeatedToast("secondLevel");
	        	        	else fc.createPredFieldDialog("secondLevel",messageHandler);
	        	        	
	        	        }
        	    	 
        	    }
        	});
        	AlertDialog alert = builder.create();
        	alert.show();
			
			
		}

	    
	    @Override
		public boolean onOptionsItemSelected(MenuItem item) {
	    	
	    	
			switch (item.getItemId()) {
			case ADD_FIELD:
				
				createFieldDialogType();
				 			 
				break;
				
			case CHANGE_TH:
				
				changeTh();
				 			 
				break;
				
			case CHANGE_MARKER:
				
				changeMapMarker();

				
				break;
								
			case ALLOW_SEC_EXTERNAL_STORAGE:

				if(photoCnt.isSecondaryExternalStorageDefault(projId)){
					
					photoCnt.setSecondaryExternalStorageAsDefault(projId, "false");

				}
				else {
					
					photoCnt.setSecondaryExternalStorageAsDefault(projId, "true");
				}
				
				break;
				
				
			}
			
		
			return super.onOptionsItemSelected(item);
		}
	
	    
	    
	 private void changeMapMarker(){
		 
		MarkerConfigurationDialog dialog=new MarkerConfigurationDialog(this, projId, null,MarkerConfigurationDialog.UPDATE_PROJECT_MARKER);
		dialog.show();
			 
		 
	 }
	 
	 private boolean repeatedFieldType(String fieldType){
		 
		 for(ProjectField field: cFields){
			 
			 if(field.getType().equals(fieldType)) return true;
			 			 
		 }
		 
		 return false;
		 
	 }
	 

	    
	 
	   private void changeTh(){
		   
		   final String [] thList=thCnt.getThList();

       	
       	if(thList.length==0){
       		

   			Toast.makeText(getBaseContext(), 
	                    R.string.emptyThList, 
	                    Toast.LENGTH_SHORT).show();
   		   
       		
       	}
       	else{
       	
	        	AlertDialog.Builder builder;
	        	
	        	builder= new AlertDialog.Builder(this);
	        	
	        	
	        	builder.setTitle(R.string.thChooseIntro);
	        	
	        	
	        	builder.setSingleChoiceItems(thList, -1, new DialogInterface.OnClickListener() {
	        	    
	        		
	        		public void onClick(DialogInterface dialog, int item) {
	        	        
	        			String thChanged=getApplicationContext().getString(R.string.thChangedText);
	        			
	        			Toast.makeText(getApplicationContext(),thChanged+" "+thList[item], Toast.LENGTH_SHORT).show();
	        	        
	        	        thCnt.changeProjectTh(projId,thList[item]);
	        	        
	        	        String defTh=getApplicationContext().getString(R.string.tvDefaultTh);
	        	        String thChangedShort=getApplicationContext().getString(R.string.thChangedShort);
	        	        thName.setText(Html.fromHtml("<b>"+defTh+"</b>"+thList[item]+" ("+thChangedShort+")")); 
	        	        thName.setTextColor(Color.GREEN);
	        	        
	        	        dialog.dismiss();
	        	        
	        	        
	        	    }
	        	});
	        	AlertDialog alert = builder.create();
	        	
	        	alert.show();
		   
		   
	   } 
   
	   }
	   
	   private Handler messageHandler = new Handler() {

		      @Override
		      public void handleMessage(Message msg) {  
		          switch(msg.what) {
		            
		          case 0:
		        	  fillFieldList();
		          	
		          
		          }
		      }

		  };
	   
	   
}