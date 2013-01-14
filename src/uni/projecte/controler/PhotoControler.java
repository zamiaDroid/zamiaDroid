package uni.projecte.controler;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import uni.projecte.dataLayer.bd.CitacionDbAdapter;
import uni.projecte.dataLayer.utils.FileUtils;
import uni.projecte.dataLayer.utils.PhotoUtils;
import uni.projecte.dataTypes.CitationPhoto;
import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

public class PhotoControler {
	
	private Context baseContext;
	private PreferencesControler prefCnt;
	private ProjectConfigControler projCnfCnt;
	
	private String mainPhotoFieldName;
	private String mainMultiPhotoFieldName;
	
	
	
	/*
	 * External storage this can be:
	 * 	a) a removable storage media (such as an SD card)
	 * 	b) an internal (non-removable) storage.
	 * 
	 * 
	 * Environment.getExternalStorageDirectory() refers to whatever the device manufacturer considered to 
	 * be "external storage". On some devices, this is removable media, like an SD card. 
	 * On some devices, this is a portion of on-device flash. Here, "external storage" means 
	 * "the stuff accessible via USB Mass Storage mode when mounted on a host machine", at least for Android 1.x and 2.x.
	 * 
	 */
	
	
	public PhotoControler(Context baseContext){
		
		this.baseContext=baseContext;
		prefCnt=new PreferencesControler(baseContext);
		projCnfCnt=new ProjectConfigControler(baseContext);
		
	}
	
	
	public String getMainPhotoPath() {

		return Environment.getExternalStorageDirectory()+"/"+prefCnt.getDefaultPath()+"/Photos/";
		
	}
	
	public String getSecondayExternalStoragePath(){
		
		return prefCnt.getSecondaryExternalStoragePath()+"/"+prefCnt.getDefaultPath()+"/Photos/";
		
	}
	
	public String getWorkingPhotoPath(long projId){
		
		if(isSecondaryExternalStorageDefault(projId)) return getSecondayExternalStoragePath();
		else return getMainPhotoPath();
		
	}
	
	
	public boolean isSecondaryExternalStorageDefault(long projId){
		
		return projCnfCnt.getProjectConfig(projId, ProjectConfigControler.SEC_STORAGE_ENABLED).equals("true") && hasSecondaryStorage() ;
		
	}
	
	public String setSecondaryExternalStorageAsDefault(long projId,String secStorageDefault){
		
		return projCnfCnt.changeProjectConfig(projId,ProjectConfigControler.SEC_STORAGE_ENABLED, secStorageDefault);	
		
	}
	
	public boolean hasSecondaryStorage(){
		
		return prefCnt.getSecondaryExternalStorage();
		
	}

	
	/*
	 * This method allows to movePhotos between paths and it also updates citation path
	 * 
	 * 	@projId
	 * 	@secondaryStorage moving to secondaryStorage?
	 * 	@copyPhoto leaves a photo copy at the original path
	 * 	@selectedPhotos hashWith with the subset of selected photos
	 * 	@handlerMove it handles the progress bar status
	 * 
	 */
	public int movePhotosToSecondaryStorage(long projId,String storagePath, HashMap<String, Long> selectedPhotos, boolean secondaryStorage,boolean copyPhoto, ArrayList<String> citPhotoList, Handler handlerMove){
		
		/*
		 * a) Selected photos -> hash
		 * b) All photos -> cursor
		 */
		
		String destStoragePath="";
		
		if(secondaryStorage) destStoragePath=getSecondayExternalStoragePath();
		else destStoragePath=getMainPhotoPath();
		
		
		/* Determining destination Path: primary -> secondary || secondary -> primary */		
		File destination=new File(destStoragePath);
		
		/* When destination doesn't exist */
		if(!destination.exists()) destination.mkdirs();
		
		
		CitationControler citCnt=new CitationControler(baseContext);
		
		/* ArrayList with all citationWithPhoto */
		
		if(selectedPhotos==null) selectedPhotos = getPhotoCitationList(projId,citPhotoList);
		//ArrayList<CitationPhoto> citPhotoList = getPhotoCitationsGroup(projId,arrayList,selectedPhotos);
		
		long photoFieldId=getProjectPhotoFieldId(projId);
		
		Iterator<String> itPhoto=citPhotoList.iterator();
		
		int count=0;
		
		while(itPhoto.hasNext()){
			
			String photoPath=itPhoto.next();
			File origin=new File(photoPath);

			/* Method tries to move photos to destination Path */
			boolean success=movePhotoPysically(citCnt,origin, destination,copyPhoto,selectedPhotos,photoFieldId);
			
			if (success) count++;
			
			/* Sending fileName to ProgressDialog */
			Message msg=new Message();
			Bundle b=new Bundle();
			b.putString("fileName", origin.getName());
			msg.setData(b);
			handlerMove.sendMessage(msg);
			
		}
		
			Message msg=new Message();
			Bundle b=new Bundle();
			b.putBoolean("secondaryStorage", secondaryStorage);
			msg.setData(b);
			handlerMove.sendMessage(msg);
		
		return count;
		
	}
	

	


	private boolean movePhotoPysically(CitationControler citCnt, File origin, File destination, boolean copyPhoto, HashMap<String, Long> selectedPhotos, long photoFieldId){
		
		
		boolean success=FileUtils.copyFile(origin, destination);
		if(success && !copyPhoto) origin.delete();
			
		Long citationId=selectedPhotos.get(origin.getName());
		
		if(citationId!=null){
			
			citCnt.startTransaction();
			
				String newPhotoPathValue=destination.getPath()+"/"+origin.getName();
				boolean updated=citCnt.updateCitationField(citationId, photoFieldId, newPhotoPathValue,mainPhotoFieldName);
				
			citCnt.EndTransaction();

			Log.i("Images", "Moving photo: Id->"+selectedPhotos.get(origin.getName())+" New Path value-> "+newPhotoPathValue+" Updated? -> "+updated);

		}
		
		return success;
		//return false;
		
	}
	
	
	public HashMap<String, Long> getPhotoCitationList(long projId,ArrayList<String> citPhotoList){
		
		HashMap<String, Long> photoInfoList=new HashMap<String, Long>();
		
		long photoFielId=getProjectPhotoFieldId(projId);
		
		if(photoFielId>=0){
			
			Iterator<String> itPhoto=citPhotoList.iterator();
			CitacionDbAdapter citDbHand=new CitacionDbAdapter(baseContext);
			citDbHand.open();
			
			while(itPhoto.hasNext()){
				
				String physicalPath=itPhoto.next();
			
				Cursor citPhotoField=citDbHand.fetchCitationIdByPhotoName(PhotoUtils.getFileName(physicalPath));
				citPhotoField.moveToNext();
					
				if(citPhotoField!=null & citPhotoField.getCount()>0){
						
					Log.i("Photo","CitatioId: "+citPhotoField.getLong(1)+" Value: "+citPhotoField.getString(3)+" FieldType: "+citPhotoField.getLong(2));
					photoInfoList.put(physicalPath,citPhotoField.getLong(1));
					
				}
					
				citPhotoField.close();
			
			}
			
			
			citDbHand.close();
		
		}
				
		return photoInfoList;
		
	}
	
	
	/*
	 * 
	 * This method gives a list of citatiton's photo fields (into CitationPhoto objects)
	 * belonging to the project @projId 
	 * 
	 */
	
	public ArrayList<CitationPhoto> getPhotoCitationPhotos(long projId){
		
		ArrayList<CitationPhoto> citPhotoList= new ArrayList<CitationPhoto>();
		
		long photoFielId=getProjectPhotoFieldId(projId);
		
		if(photoFielId>=0){
				
			CitacionDbAdapter citDbHand=new CitacionDbAdapter(baseContext);
			citDbHand.open();
			
				Cursor citPhotoField=citDbHand.fetchCitationWithPhoto(photoFielId);
				citPhotoField.moveToNext();
				
				if(citPhotoField!=null & citPhotoField.getCount()>0){
					
					while(!citPhotoField.isAfterLast()){
						
						CitationPhoto citPhotoTmp= new CitationPhoto(citPhotoField.getString(3), citPhotoField.getLong(1), citPhotoField.getLong(0),citPhotoField.getLong(2));
						if(!citPhotoTmp.getPhotoPath().equals("")) citPhotoList.add(citPhotoTmp);
						
						citPhotoField.moveToNext();
					}
					
				
				}
				
			citPhotoField.close();
			citDbHand.close();
		
		}
				
		return citPhotoList;
	}
	
	

	
	/*
	 * It removes the citationPhotoValue of @citationId and value= @photo
	 * 
	 */
	
	public boolean removePhoto(long citationId, String photo){
		
		boolean result=false;
		
		CitacionDbAdapter citDbHand=new CitacionDbAdapter(baseContext);
		citDbHand.open();

		Cursor cursor=citDbHand.fetchCitationIdByPhotoField(photo);
		cursor.moveToFirst();
		
		if(cursor!=null && cursor.getCount()>0) {
			
			long fieldCitationId=cursor.getLong(0);
			result=citDbHand.updateCitationFieldValue(cursor.getLong(1), fieldCitationId, "");

		}
		
		cursor.close();
		
		citDbHand.close();

		//remove file
		if(result){
				
			File f= new File(photo);
			result=result && f.delete();
			
		}
	
		return result;
		
	}
	
	
	/*
	 * 
	 * This method gives the fieldId of the photo field inside project with @projId
	 *  
	 */
	
	public long getProjectPhotoFieldId(long projId) {
		
		ProjectControler projCnt=new ProjectControler(baseContext);
		
		long mainPhotoFieldId=-1;
		
		Cursor photoFields=projCnt.getPhotoFieldsFromProject(projId);
		
		if(photoFields.getCount()>0){
			
			mainPhotoFieldId=photoFields.getLong(0);
			mainPhotoFieldName=photoFields.getString(2);
			
		}
		
		photoFields.close();
		
		return mainPhotoFieldId;
	}

	
	
	/*
	 * This method gives the fieldId of the photo field inside project with @projId
	 * 
	 */
	
	public long getMultiPhotoFieldId(long projId) {
		
		ProjectControler projCnt=new ProjectControler(baseContext);
		
		long mainMultiPhotoFieldId=-1;
		
		Cursor photoFields=projCnt.getMultiPhotoFieldsFromProject(projId);
		
		if(photoFields.getCount()>0){

			mainMultiPhotoFieldId=photoFields.getLong(0);
			mainMultiPhotoFieldName=photoFields.getString(2);
			
		}
		
		photoFields.close();
		
		return mainMultiPhotoFieldId;
		
		
	}
	
	
	public HashMap<String,Long> getSelectedPhotos(long projId, String[] ids){
		
		MultiPhotoControler multiPhotoCnt= new MultiPhotoControler(baseContext);
		
		HashMap<String,Long> selectedPhotos=new HashMap<String,Long>();
		
		
		long photoFieldId=getProjectPhotoFieldId(projId);
		long multiPhotoFieldId=getMultiPhotoFieldId(projId);
	

		for(int i=1;i<ids.length;i++){
			
			long citationId=Long.valueOf(ids[i]);
			
			//simplePhoto
			String photoPath=getPhotoPathByCitationId(citationId,photoFieldId);
			
			if(!photoPath.equals("")){ 
				
				selectedPhotos.put(PhotoUtils.getFileName(photoPath),citationId);
				
			}
			
			//multiPhoto
			if(multiPhotoFieldId>0) multiPhotoCnt.getMultiPhotoValuesByCitationId(citationId, multiPhotoFieldId, selectedPhotos);
			

		}	
		
		return selectedPhotos;
		
	}
	
	
	
	/*
	 * @return PhotoPath (if it exists) of @citationId and photoFieldValue -> @photoField
	 * 
	 */

	public String getPhotoPathByCitationId(long citationId, long photoFieldId) {
		
		String photoPath="";
		
		CitacionDbAdapter citDbHand=new CitacionDbAdapter(baseContext);
		citDbHand.open();
		
			Cursor photoValue=citDbHand.fetchCitationPhotoValue(citationId, photoFieldId);
			photoValue.moveToFirst();
			
			if(photoValue!=null && photoValue.getCount()>0) {
				
				photoPath=photoValue.getString(3);
				photoValue.close();
				
			}

			
		citDbHand.close();
		
		return photoPath;
	}


	

}
