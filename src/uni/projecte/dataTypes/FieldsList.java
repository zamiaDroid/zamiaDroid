package uni.projecte.dataTypes;

import java.util.ArrayList;
import java.util.HashMap;

public class FieldsList {
	
	private HashMap<String, ProjectField> fieldList;
	private ArrayList<String> fieldsNames;


	public FieldsList(){
		
    	fieldList= new HashMap<String, ProjectField>();
    	fieldsNames = new ArrayList<String>();
		
	}
	
	public void addNewField(String fieldName,ProjectField projField){
		
		fieldList.put(fieldName, projField);
		fieldsNames.add(fieldName);
		
	}
	
	public boolean fieldExists(String fieldName){
		
		return fieldList.get(fieldName)!=null;
		
	}
		
	public ProjectField getProjectField(String fieldName){
		
		return fieldList.get(fieldName);
				
	}

	public ArrayList<String> getFieldsNames() {
		return fieldsNames;
	}


}
