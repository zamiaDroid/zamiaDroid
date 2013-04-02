package uni.projecte.controler;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;

import uni.projecte.dataLayer.bd.CitacionDbAdapter;
import uni.projecte.dataLayer.utils.FileUtils;
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
		
		return projCnfCnt.getProjectConfig(projId, ProjectConfigControler.SEC_STORAGE_ENABLED).equals("true") &&
			hasSecondaryStorage() ;
		
	}
	
	public String setSecondaryExternalStorageAsDefault(long projId,String secStorageDefault){
		
		return projCnfCnt.changeProjectConfig(projId,ProjectConfigControler.SEC_STORAGE_ENABLED,  secStorageDefault);	
		
	}
	
	public boolean hasSecondaryStorage(){
		
		return prefCnt.getSecondaryExternalStorage();
		
	}

	
	public int movePhotosToSecondaryStorage(long projId,boolean secondaryStorage,boolean copyPhoto, Handler handlerMove){
		
		CitationControler citCnt=new CitationControler(baseContext);
		
		ArrayList<CitationPhoto> citPhotoList = getPhotoCitationPhotos(projId);
		Iterator<CitationPhoto> itPhoto=citPhotoList.iterator();
		
		int count=0;
		
		while(itPhoto.hasNext()){
			
			CitationPhoto citPhotoTmp=itPhoto.next();
			
			File origin=new File(citPhotoTmp.getPhotoPath());
			File destination;			
			
			if(secondaryStorage) destination=new File(getSecondayExternalStoragePath());		
			else destination=new File(getMainPhotoPath());	
			
				boolean success=movePhotoPysically(citCnt,citPhotoTmp,origin, destination,copyPhoto);
			
			if (success) count++;
			
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
	
	
	private boolean movePhotoPysically(CitationControler citCnt, CitationPhoto citPhotoTmp, File origin, File destination, boolean copyPhoto){
		
		boolean success=false;
		
		if(copyPhoto) success=FileUtils.copyFile(origin, destination);
		else success=FileUtils.moveFileToDir(origin, destination);
		
		String newPhotoPathValue=destination.getPath()+"/"+origin.getName();
		
		if(success) {
			
			citCnt.startTransaction();
			
				Log.i("Photos", citPhotoTmp.getCitationId()+" : "+newPhotoPathValue);
				citCnt.updateCitationField(citPhotoTmp.getCitationId(), citPhotoTmp.getFieldType(), newPhotoPathValue,mainPhotoFieldName);
				
			citCnt.EndTransaction();
			
		}
		
		return success;
		
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
