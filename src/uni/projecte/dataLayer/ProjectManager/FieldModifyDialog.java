package uni.projecte.dataLayer.ProjectManager;

import java.util.ArrayList;
import java.util.Arrays;

import uni.projecte.R;
import uni.projecte.controler.DataTypeControler;
import uni.projecte.controler.ProjectControler;
import uni.projecte.dataTypes.ProjectField;
import uni.projecte.dataTypes.Utilities;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;



public class FieldModifyDialog extends Dialog{
	
	
	private Context context;
	private long projId;

	private ProjectControler projCnt;
	private DataTypeControler dtH;
	
	private LinearLayout fieldModDialog;
	private ProjectField projField;
	private ArrayAdapter<String> adapter;
	private Spinner spValuesList;
	
	private boolean modifiedList;

	
	
	/*
	 * This class creates a dialog that allows us to modified the field's label.
	 * It's also possible to modify the items when the field is complex (has a list of items)
	 * 
	 */
	
	public FieldModifyDialog(Context context,long projId,ProjectField projField,ProjectControler projCnt) {
		  
	    super(context);

		this.context=context;
		this.projId=projId;
		this.projField=projField;
		this.projCnt=projCnt;
		
	
	}
	  
	@Override
	 protected void onCreate(final Bundle savedInstanceState){

		super.onCreate(savedInstanceState);

		fieldModDialog=(LinearLayout) LayoutInflater.from(context).inflate(R.layout.field_edit, null);
		setContentView(fieldModDialog);
		
		dtH=new DataTypeControler(context);
		
		modifiedList=false;
		
		bindEvents();
		
		
	}
	
	  private void bindEvents() {
		  
		  final EditText etFieldName = (EditText)fieldModDialog.findViewById(R.id.etFieldName);
		  final Button btModifyField = (Button)fieldModDialog.findViewById(R.id.btModifyField);
		  
		  spValuesList = (Spinner)fieldModDialog.findViewById(R.id.sPrefFields);
		  
		  final Button removeValue = (Button)fieldModDialog.findViewById(R.id.bRemovePredField);
		  final Button addValue = (Button)fieldModDialog.findViewById(R.id.bAddPredField);
		  
		  
		  if(projField.isPredefined()){
			  
			  fillAdapter();			  
			  
			
			addValue.setOnClickListener(new Button.OnClickListener() {
				
				public void onClick(View v) {

					EditText et=(EditText)fieldModDialog.findViewById(R.id.etPredValue);
	                 
	                 String text=et.getText().toString();

	                 if(!text.equals("")){
	                	 
	                	 addItem(text);
	                	 modifiedList=true;
	                	 et.setText("");
	                	 
	                 }
	                 				
				}
			});
		  
		  
			removeValue.setOnClickListener(new Button.OnClickListener() {
			
				public void onClick(View v) {
		  
					removeSelectedItem();
               	 	modifiedList=true;

				}
			});
			  
			  
		  }
		  else ((LinearLayout)fieldModDialog.findViewById(R.id.llPredefinedValues)).setVisibility(View.GONE);
		  
		  
		  etFieldName.setText(projField.getLabel());
		  
		 		  
		  btModifyField.setOnClickListener(new Button.OnClickListener() {
			
			public void onClick(View v) {

				String labelName=etFieldName.getText().toString();
								
				projCnt.changeLabelName(projId, projField.getId(), labelName);
				projField.setLabel(labelName);
				
				if(projField.isPredefined() && modifiedList){
					
					dtH.removeItemsFromField(projField.getId());
					addNewItems();
					
				}
				
				Utilities.showToast(context.getString(R.string.fieldLabelChanged)+": "+labelName, context);
				dismiss();
				
			}
		
		  });
		  
	  }
	  
	  
	  /*
	   * Inserting new items to database (previously has been cleared)
	   * 
	   */
	  
		private void addNewItems() {

			int n=adapter.getCount();
			
			for(int i=0; i<n; i++){
				
				projCnt.addFieldItem(projField.getId(),adapter.getItem(i));
				
			}
			
		}
	
	/*
	 * 
	 * Populating spinner with items' list
	 *    
	 */
	  private void fillAdapter() {

		  String[] items=dtH.getItemsbyFieldId(projField.getId());
		  
		  ArrayList<String> values = new ArrayList<String>();
		  values.addAll(Arrays.asList(items));
			
		  adapter = new ArrayAdapter<String>(context,
			        android.R.layout.simple_spinner_item, values);
		  
		  adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		
		  spValuesList.setAdapter(adapter);
		  
	}

	private void addItem(String itemValue){
		
		if(adapter.getCount()>0) {
			
			int pos=spValuesList.getSelectedItemPosition();
			
			adapter.insert(itemValue, pos+1);
			spValuesList.setSelection(pos+1);

		}
		else adapter.insert(itemValue, 0);
			
		adapter.notifyDataSetChanged();
		
	  }

	  private void removeSelectedItem(){
		  
		adapter.remove((String)spValuesList.getSelectedItem());
		adapter.notifyDataSetChanged();
		
	  }
	  
}
