package uni.projecte.controler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import uni.projecte.dataLayer.CitationManager.Zamia.ZamiaCitationExporter;
import uni.projecte.dataLayer.CitationManager.objects.Citation;
import uni.projecte.dataLayer.bd.CitacionDbAdapter;
import uni.projecte.dataLayer.bd.SecondLevelCitacionDbAdapter;
import uni.projecte.dataLayer.utils.PhotoUtils;
import uni.projecte.dataTypes.CitationPhoto;
import uni.projecte.dataTypes.ProjectField;
import uni.projecte.ui.multiphoto.MultiPhotoFieldForm;
import android.content.Context;
import android.database.Cursor;
import android.os.Handler;
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


	public void addPhotosList(MultiPhotoFieldForm photoFieldForm, long subFieldId, long projId, long parentId) {
   			
        	citSLCnt=new CitationSecondLevelControler(baseContext);
			        
	    	ArrayList<String> photoList=photoFieldForm.getPhotoList();
	    	Iterator<String> photoIt=photoList.iterator();
	    	
	    	while(photoIt.hasNext()){
	    		
	    		String photoValue=photoIt.next();
	    		
				// subProjId (0) || fieldId inside subproject (1)				
		        long citationId=citSLCnt.createCitation(photoFieldForm.getSecondLevelId(), 100, 190, "",projId,FIELD_NAME,parentId);

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
	
	public void exportSubCitationsZamia(long fieldId, String citationValue,ZamiaCitationExporter zamiaCitExp) {

		citSLCnt = new CitationSecondLevelControler(baseContext);

		zamiaCitExp.createPhotoList();
		
		String multiPhotoValues= citSLCnt.getMultiPhotosValues(citationValue);

		if(multiPhotoValues!=null) {

			String[] splitted= multiPhotoValues.split("; ");

			for(int i=0; i<splitted.length; i++){

				zamiaCitExp.addPhoto(splitted[i]);

			}
			
		}
		
		zamiaCitExp.closePhotoList();
		
	}

	public CitationPhoto getCitationByPhotoPath(String fileName, boolean hasMultiPhoto, HashMap<String, String> fieldsLabelNames) {
		
		CitationControler citCnt=new CitationControler(baseContext);
		CitationPhoto citationPhoto=null;

		//trying if multiPhotoValue
		if(hasMultiPhoto){
		
        	citSLCnt=new CitationSecondLevelControler(baseContext);
        	citationPhoto=citSLCnt.getMultiPhotoByValue(fileName);
			
		}
		
		//trying simple Photo
		if(citationPhoto==null){
			
			citationPhoto=citCnt.getCitationPhoto(fileName);
			
		}
		
		
		if(citationPhoto!=null){
			
			String[] citLabel=citCnt.getCitationValues(citationPhoto.getCitationId(), fieldsLabelNames);
			citationPhoto.setLabel(citLabel[0]);
						
		}
	
		return citationPhoto;
		
	}

	public void updatePhotoField(long projId, ProjectField att, Handler handlerChangePhotos) {


		ProjectSecondLevelControler projSLCnt= new ProjectSecondLevelControler(baseContext);
		projSLCnt.createField(att.getId(), "Photo", "photo", "", "", "text");

		 //for each: create citationFieldValue (newId) & create subCitationFieldValue with photoValue		

		 citCnt= new CitationControler(baseContext);
		 citSLCnt=new CitationSecondLevelControler(baseContext);
		 
		 //citCnt.
		 Cursor photos=citCnt.getPhotoValuesByProjectId(projId,att.getId());
		 photos.moveToFirst();
		 
		 if(photos!=null){
			 
		      handlerChangePhotos.sendEmptyMessage(photos.getCount());
		 
			 //KEY_ROWID,KEY_SAMPLE_ID, KEY_TIPUS_ATRIB,VALUE
			 while(!photos.isAfterLast()){
						 
				 String photoValue=photos.getString(1).trim();
				 long parentCitationId=photos.getLong(0);
				 
				 if(!photoValue.equals("")){ 
					 
					 String secondLevelId=PhotoUtils.getFileName(photoValue);
	
					 long newCitationId=citSLCnt.createCitation(secondLevelId, 100, 190, "",projId,FIELD_NAME,parentCitationId);
					 
					 citSLCnt.startTransaction();
				     	long lala=citSLCnt.addCitationField(att.getId(),newCitationId,projId,"Photo",photoValue);
				     citSLCnt.EndTransaction();
				     
				     //changing photo id's
				     citCnt.startTransaction();
				     citCnt.updateCitationField(parentCitationId, att.getId(), secondLevelId,att.getName());
				     citCnt.EndTransaction();
				     
				 }
				 
				 handlerChangePhotos.sendEmptyMessage(-1);

				 
				 photos.moveToNext();
				 
			 }
			 
			 photos.close();
		 
		 }

		 //updatePhoto2MultiPhoto
		ProjectControler projCnt=new ProjectControler(baseContext);
        projCnt.updatePhotoField(projId,att.getId());				
			
       handlerChangePhotos.sendEmptyMessage(0);
		
	}
	
	
	
	

}
