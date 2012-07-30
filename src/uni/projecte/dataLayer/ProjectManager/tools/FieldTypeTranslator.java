package uni.projecte.dataLayer.ProjectManager.tools;

import uni.projecte.R;
import android.content.Context;

public class FieldTypeTranslator {
	
	private String text;
	private String bool;
	private String number;
	
	private String thesaurus;
	
	private String predefined;
	private String subProject;
	private String photo;
	
	private Context baseContext;
	
	public FieldTypeTranslator(Context baseContext){
		
		this.baseContext=baseContext;
		
		text=baseContext.getString(R.string.fieldTypeText);
		bool=baseContext.getString(R.string.fieldTypeBool);
		number=baseContext.getString(R.string.fieldTypeNum);
		thesaurus=baseContext.getString(R.string.fieldTypeTh);
		photo=baseContext.getString(R.string.fieldTypePhoto);
		subProject=baseContext.getString(R.string.fieldTypeSubProject);
		predefined=baseContext.getString(R.string.fieldTypePredefined);
				
	}
	
	public String getFieldTypeTranslated(String fieldType){
		
		if(fieldType.equals("simple")) return text;
		
		else if(fieldType.equals("boolean")) return bool;
		
		else if(fieldType.equals("number")) return number;
		
		else if(fieldType.equals("thesaurus")) return thesaurus;
		
		else if(fieldType.equals("photo")) return photo;
		
		else if(fieldType.equals("secondLevel")) return subProject; 
		
		else if(fieldType.equals("text")) return predefined;
		
		else if(fieldType.equals("complex")) return predefined;
		
		else return "";
			
		
	}
	
	

}
