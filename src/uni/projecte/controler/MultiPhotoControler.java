package uni.projecte.controler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import uni.projecte.dataLayer.utils.PhotoUtils;
import uni.projecte.dataTypes.ProjectField;
import uni.projecte.ui.multiphoto.MultiPhotoFieldForm;
import android.content.Context;
import android.util.Log;

public class MultiPhotoControler{
	
	public static String FIELD_NAME="multiPhoto";

	
	private Context baseContext;
	private ProjectField projField;
	private ProjectSecondLevelControler projSLCnt;
	private CitationControler citCnt;
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
	
	
	public boolean getMultiPhotoValuesByCitationId(long citationId, long multiPhotoFieldId, HashMap<String,Long> selectedPhotos){
		
		citCnt = new CitationControler(baseContext);
		citSLCnt = new CitationSecondLevelControler(baseContext);

		String citationTag=citCnt.getMultiPhotoFieldTag(citationId, multiPhotoFieldId);

		if(!citationTag.equals("")){

			String multiPhotoValues= citSLCnt.getMultiPhotosValues(citationTag);

			if(multiPhotoValues!=null) {

				String[] splitted= multiPhotoValues.split("; ");

				for(int i=0; i<splitted.length; i++){

					selectedPhotos.put(PhotoUtils.getFileName(splitted[i]),citationId);

				}

				return true;

			}
			
			return false;
			
		}
		else return false;
	} 


	public void addPhotosList(MultiPhotoFieldForm photoFieldForm, long subFieldId, long projId) {
   			
        	citSLCnt=new CitationSecondLevelControler(baseContext);
			        
	    	ArrayList<String> photoList=photoFieldForm.getPhotoList();
	    	Iterator<String> photoIt=photoList.iterator();
	    	
	    	while(photoIt.hasNext()){
	    		
	    		String photoValue=photoIt.next();
	    		
				// subProjId (0) || fieldId inside subproject (1)				
		        long citationId=citSLCnt.createCitation(photoFieldForm.getSecondLevelId(), 100, 190, "",projId,FIELD_NAME);

		        citSLCnt.startTransaction();
		        	citSLCnt.addCitationField(photoFieldForm.getFieldId(),citationId,subFieldId,projField.getName(),photoValue);
		    	citSLCnt.EndTransaction();

				Log.i("Citation","Action-> created citation[Photo]Value : Label: "+photoFieldForm.getSecondLevelId()+" Value: "+photoValue);

	    	}
    	    	
	}

	public boolean removeMultiPhoto(String imagePath) {

    	citSLCnt=new CitationSecondLevelControler(baseContext);

    	return citSLCnt.removeMultiPhoto(imagePath);
    			
		
	}

	
}
