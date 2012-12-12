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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import uni.projecte.R;
import uni.projecte.R.id;
import uni.projecte.R.layout;
import uni.projecte.R.string;
import uni.projecte.controler.ProjectControler;
import uni.projecte.controler.ThesaurusControler;
import uni.projecte.dataLayer.CitationManager.Zamia.ZamiaCitationXMLparser;
import uni.projecte.dataLayer.CitationManager.Zamia.ZamiaExportCitationReader;
import uni.projecte.dataLayer.ProjectManager.ListAdapters.NewFieldsListAdapter;
import uni.projecte.dataLayer.ProjectManager.objects.Project;
import uni.projecte.dataTypes.FieldsList;
import uni.projecte.dataTypes.ProjectField;
import uni.projecte.dataTypes.Utilities;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import android.widget.CompoundButton.OnCheckedChangeListener;
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

public class CitationImportZamia extends Activity {
	
	public static int SUCCESSFUL_IMPORT =1;
	
	private Project projObj;	
	private FieldsList fieldsList;
	
	private ArrayList<String> newFields;
	private NewFieldsListAdapter newFieldListAdapter;
	private long projId;
	private ProjectControler projCnt;
	private static String fileName;
	
	private ProgressDialog pdRemove;
	private Dialog dialog;

	
	private int newCitations;

	
    @Override
	public void onCreate(Bundle savedInstanceState) {
    	
    	super.onCreate(savedInstanceState);

    	setContentView(R.layout.zamiaimport);
    	
    	ListView lvNewFields=(ListView)findViewById(R.id.lvNewFields);
    	TextView tvCitationsInfo=(TextView)findViewById(R.id.tvCitations);
    	TextView tvFieldsInfo=(TextView)findViewById(R.id.tvFields);
    	Button btImport=(Button)findViewById(R.id.btZamiaImport);
    	LinearLayout llBottomPanel=(LinearLayout)findViewById(R.id.llBottomPanel);

    	//when no projId    	
    	projId=getIntent().getExtras().getLong("id");
    	    	    	
       	fileName=getIntent().getExtras().getString("file");

    	ZamiaCitationXMLparser zCP= new ZamiaCitationXMLparser();
    	
    	projObj= new Project(projId);
    	fieldsList = new FieldsList();
    	
    	projCnt=new ProjectControler(this);
    	  
    	// pre-reading file Structure
       	newCitations=zCP.preReadXML(this, fileName, projObj,fieldsList);    	

       	//Well formatted with citations
       	if(newCitations>0){
       		
       		loadProjectInfo();

           	tvCitationsInfo.setText(String.format(getString(R.string.zamiaImportCitationsCount),newCitations));

           	if(projObj.isCreated()){
		    	
	    		//creating list of new Fields (fields that not exists in the project)
		    	checkNewFields();
		    	tvFieldsInfo.setText(Html.fromHtml(String.format(getString(R.string.zamiaImportAddFields),newFields.size(),projObj.getProjName())));

	    	}
	    	else{
	    		
	    		newFields=fieldsList.getFieldsNames();
	    		
		    	tvFieldsInfo.setText(Html.fromHtml(String.format(getString(R.string.zamiaImportProjAddFields),newFields.size())));
		    	btImport.setText(getString(R.string.projCreation));
		    			    	
	    	}
	    	
	
	    	newFieldListAdapter=new NewFieldsListAdapter(this, newFields);
	    	lvNewFields.setAdapter(newFieldListAdapter);
	    	
	    	btImport.setOnClickListener(btZamiaImportListener);
	    	
       	}
       	else{
       		
       		llBottomPanel.setVisibility(View.GONE);
       		tvFieldsInfo.setVisibility(View.GONE);
           	tvCitationsInfo.setText(getString(R.string.zamiaImportWrongFormat));
       		
       	}
    	
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
        	
        	
        	if(projObj.isCreated()){
        		
            	importCitationsDialog();

        	}
        	else{
        		
                    	
        		//createProject Dialog
        		createProjectDialog();
                		
        	}
        	
        }
        
	};
    
	private void importCitationsDialog(){


		String progressMessage = String.format(getString(R.string.zamiaImportTitle), projObj.getProjName());

		pdRemove = new ProgressDialog(this);
		pdRemove.setCancelable(true);
		pdRemove.setMessage(progressMessage);
		pdRemove.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
		pdRemove.setProgress(0);
		pdRemove.setMax(newCitations);
		pdRemove.show();

		Thread thread = new Thread(){

			@Override
			public void run() {

				importCitationsThread();

			}

		};


		thread.start();
		
	}
	
    private void importCitationsThread() {

		Log.i("Import","Format:Zamia | (A) Action: Importing Citations ("+newCitations+")");

		addProjectFields();
		
      	//importing citations
    	importCitations();        	
    	        	
    	handlerUpdateProcessDialog.sendEmptyMessage(1);
    	
    	
	}
	
	private void createProjectDialog() {


		ThesaurusControler thCont= new ThesaurusControler(this);

		//Context mContext = getApplicationContext();
		dialog = new Dialog(this);

		dialog.setContentView(R.layout.projectcreation);
		dialog.setTitle("Introdueixi les dades");

		Button createProject = (Button)dialog.findViewById(R.id.bAddItem);
		EditText name=(EditText)dialog.findViewById(R.id.etNameItem);


		Spinner thList=(Spinner)dialog.findViewById(R.id.thList);


		ArrayAdapter<String> dtAdapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, thCont.getThList());


		dtAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		thList.setAdapter(dtAdapter);

		name.setText(projObj.getProjName());

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
					
					dialog.dismiss();
					importCitationsDialog();

				}


			}

		});

		dialog.show();



	}
	
	private int addProjectFields(){

		boolean[] selectedNewFields=newFieldListAdapter.getItemSelection();
		
		Iterator<String> it=newFields.iterator();
		int i=0;

		//creating fields that not exists and we have chosen to create them    	
		while(it.hasNext()){

			String fieldName=it.next();

			if(selectedNewFields[i]){

				createNewField(fieldsList.getProjectField(fieldName));

			}

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

					if(msg.what==1){
						
						pdRemove.dismiss();
						finishActivity();
									
					}
					else{
						
						pdRemove.incrementProgressBy(1);
						
					}


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
	
	protected void finishActivity() {
		
		Intent intent = new Intent();
    	
		Bundle b = new Bundle();
		b.putInt("numCitations", newCitations);
					
			intent.putExtras(b);
			setResult(1, intent);
		
		finish();
		
	}

	protected void importCitations() {


		ZamiaExportCitationReader zR= new ZamiaExportCitationReader(this, projId,handlerUpdateProcessDialog);
		
		ZamiaCitationXMLparser zCP= new ZamiaCitationXMLparser(zR);
		zCP.readXML(this, fileName, false);
	
		boolean error=zCP.isError();
		
		if(error) Utilities.showToast(getString(R.string.zamiaImportError), this);	
		
		Log.i("Import","Format:Zamia | (A) Action: Citations Imported (error: "+error+")");

		
	}

	protected void createNewField(ProjectField projectField) {

		Log.i("Import","Format:Zamia | (B) Action: Field Added -> "+projectField.getName()+" : "+projectField.getLabel());

		long fieldId=projCnt.createField(projId, projectField.getName(), projectField.getLabel(),"ECO", projectField.getType(), true);
		
		if(projectField.getPredValuesList().size()>0) projCnt.addFieldItemList(projId,fieldId,projectField.getPredValuesList());
				
		
	}
	
}



