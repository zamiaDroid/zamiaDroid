package uni.projecte.controler;

import java.util.ArrayList;
import java.util.Iterator;

import uni.projecte.dataTypes.ProjectField;
import uni.projecte.ui.multiphoto.MultiPhotoFieldForm;
import android.content.Context;
import android.util.Log;

public class MultiPhotoControler{
	
	private Context baseContext;
	private ProjectField projField;
	private ProjectSecondLevelControler projSLCnt; 
	private CitationSecondLevelControler citSLCnt;
	

	public MultiPhotoControler(Context baseContext) {

		this.baseContext=baseContext;

		
	}

	public long getMultiPhotoSubFieldId(long fieldId){
		
		projSLCnt=new ProjectSecondLevelControler(baseContext);
		
		projField=projSLCnt.getMultiPhotoSubFieldId(fieldId);
	
		return projField.getId();
		
	}
	

	public String getMultiPhotoFieldName() {
		
		return projField.getName();
		
	}


	public void addPhotosList(MultiPhotoFieldForm photoFieldForm, long subFieldId) {
   			
        	citSLCnt=new CitationSecondLevelControler(baseContext);
			        
	    	ArrayList<String> photoList=photoFieldForm.getPhotoList();
	    	Iterator<String> photoIt=photoList.iterator();
	    	
	    	while(photoIt.hasNext()){
	    		
	    		String photoValue=photoIt.next();
	    		
				// subProjId (0) || fieldId inside subproject (1)				
		        long citationId=citSLCnt.createCitation(photoFieldForm.getSecondLevelId(), 100, 190, "");

		        citSLCnt.startTransaction();
		        	citSLCnt.addCitationField(photoFieldForm.getFieldId(),citationId,subFieldId,projField.getName(),photoValue);
		    	citSLCnt.EndTransaction();

				Log.i("Citation","Action-> created citation[Photo]Value : Label: "+photoFieldForm.getSecondLevelId()+" Value: "+photoValue);

	    	}
    	    	
	}

	
}
