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
import uni.projecte.dataLayer.CitationManager.Zamia.ZamiaCitationXMLparser;
import uni.projecte.dataLayer.CitationManager.Zamia.ZamiaExportCitationReader;
import uni.projecte.dataTypes.ProjectField;
import uni.projecte.dataTypes.Utilities;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
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
 * Checked fields will be created inside project {getIntent().getExtras().getLong("id")} and then
 * citations will be imported to the project provided
 *
 * 
 */

public class CitationImportZamia extends Activity {
	
	public static int SUCCESSFUL_IMPORT =1;
	
	private HashMap<String, ProjectField> fieldList;
	private ArrayList<String> newFields;
	private FieldListAdapter newFieldListAdapter;
	private long projId;
	private ProjectControler projCnt;
	private static String fileName;
	private ProgressDialog pdRemove;
	
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

    	projId=getIntent().getExtras().getLong("id");
       	fileName=getIntent().getExtras().getString("file");

    	ZamiaCitationXMLparser zCP= new ZamiaCitationXMLparser();
    	
    	fieldList= new HashMap<String, ProjectField>();
    	projCnt=new ProjectControler(this);
    	projCnt.loadProjectInfoById(projId);
    	
    	// pre-reading file Structure
       	newCitations=zCP.preReadXML(this, fileName, fieldList);    	

       	//Well formatted with citations
       	if(newCitations>0){

           	tvCitationsInfo.setText(String.format(getString(R.string.zamiaImportCitationsCount),newCitations));

	    	//creating list of new Fields (fields that not exists in the project)
	    	checkNewFields();
	    	
	    	tvFieldsInfo.setText(Html.fromHtml(String.format(getString(R.string.zamiaImportAddFields),newFields.size(),projCnt.getName())));
	
	    	newFieldListAdapter=new FieldListAdapter(this, newFields);
	    	lvNewFields.setAdapter(newFieldListAdapter);
	    	
	    	btImport.setOnClickListener(btZamiaImportListener);
	    	
       	}
       	else{
       		
       		llBottomPanel.setVisibility(View.GONE);
       		tvFieldsInfo.setVisibility(View.GONE);
           	tvCitationsInfo.setText(getString(R.string.zamiaImportWrongFormat));
       		
       	}
    	
    }

    
	private OnClickListener btZamiaImportListener = new OnClickListener()
    {
        public void onClick(View v)
        {        
        	
        	

        	String progressMessage = String.format(v.getContext().getString(R.string.zamiaImportTitle), projCnt.getName());
        	
            pdRemove = new ProgressDialog(v.getContext());
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
	};
    
    
    private void importCitationsThread() {

		Log.i("Import","Format:Zamia | (A) Action: Importing Citations ("+newCitations+")");

        
        boolean[] selectedNewFields=newFieldListAdapter.getItemSelection();
    	
    	Iterator<String> it=newFields.iterator();
    	int i=0;
    	
    	//creating fields that not exists and we have chosen to create them    	
    	while(it.hasNext()){
    		
    		String fieldName=it.next();
    		
    		if(selectedNewFields[i]){
    			
    			createNewField(fieldList.get(fieldName));
    			
    		}
    		
    		i++;
    		
    	}
    	
    	//importing citations
    	importCitations();        	
    	        	
    	handlerUpdateProcessDialog.sendEmptyMessage(1);
    	
    	
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
		
		for (String key : fieldList.keySet()) {
			
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

	/*
	 * 	BaseAdapter that includes a list of newFields and a Checkbox that will 
	 * 	allow us to decide if we want to create the newField to the current project
	 * 
	 */

    private class FieldListAdapter extends BaseAdapter {
        
    	private ArrayList<String> newFields;
        private Context mContext;
        
        private LayoutInflater inflater;
        
        // array of items selection
        private boolean[] itemSelection;

    	
    	public FieldListAdapter(Context context,ArrayList<String> newFields){
    		
            mContext = context;
            this.newFields=newFields;
            inflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            itemSelection=new boolean[getCount()];
            
        }


        public int getCount() {
        	
            return newFields.size();
            
        }


        public String getItem(int position) {
            return newFields.get(position);
        }

  
        public long getItemId(int position) {
            return position;
        }

      
 
        public View getView(final int position, View convertView, ViewGroup parent) {

        	convertView = inflater.inflate(R.layout.zamiaimportfield, null);
        	final ViewHolder holder = new ViewHolder();
        	
        	holder.chkItem = (CheckBox)convertView.findViewById(R.id.cbField);
        	
        	holder.chkItem.setOnCheckedChangeListener(new OnCheckedChangeListener(){

        		public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        			
        			itemSelection[position] = holder.chkItem.isChecked();
        			
        		}
        	});
        	 
        	holder.chkItem.setChecked(itemSelection[position]);
        	convertView.setTag(holder);
        	
        	holder.chkItem.setText(getItem(position));
        	
        	return convertView;
        	}


		public boolean[] getItemSelection() {
			return itemSelection;
		}

    }

    public static class ViewHolder {
    	
    	CheckBox chkItem;
    
    }
    
}


