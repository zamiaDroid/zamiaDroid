package uni.projecte.dataLayer.utils;

public class UTMUtils {
	
	//41.59711401204716, 1.744415346292667
	public static boolean isLatLong(String baseString){
		
		return baseString.matches("^(-)*[0-9]*.[0-9]*, (-)*[0-9]*.[0-9]*$");
		
	}
	
	public static boolean isUTMCoordDigraph(String baseString){
		
		return baseString.matches("^[0-9]*[a-zA-Z]{3}+[0-9]+$");
		
	}
	
	
	//UTM: 31 429425 4635963
	public static boolean isUTMCoordNum(String baseString){
		
		return baseString.matches("^[0-9]* [0-9]+ [0-9]+$");
		
	}

}
