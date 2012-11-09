package uni.projecte.dataLayer.utils;

public class PhotoUtils {
	
	public static String getFileName(String photoPath){
		
		int pos=photoPath.indexOf("/zamiaDroid");
		
		if(pos<=0) return photoPath;
		else return photoPath.substring(pos+19);
				
		
	}
	

}
