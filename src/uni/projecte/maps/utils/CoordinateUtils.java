package uni.projecte.maps.utils;

public class CoordinateUtils {


	public static boolean isCorrectCoordinate(double latitude, double longitude){
		
		return (latitude<=90) && (longitude <=180);

		
	}

}
