package uni.projecte.dataLayer.utils;

import java.io.File;

public class PhotoUtils {
	
	public static String getFileName(String photoPath){
		
		int pos=photoPath.indexOf("/zamiaDroid");
		
		if(pos<=0) return photoPath;
		else return photoPath.substring(pos+19);
				
		
	}
	
	public static boolean removePhoto(String path){
		
    	File file = new File(path); 
    	boolean deleted = file.delete();

		return deleted;
    	
	}
	

}
