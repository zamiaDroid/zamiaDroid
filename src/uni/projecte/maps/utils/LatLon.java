package uni.projecte.maps.utils;

public final class LatLon {
	
	public double latitude;
	public double longitude;
	public double altitude;
	
	public LatLon(double latitude, double longitude, double altitude){
		
		this.latitude=latitude;
		this.longitude=longitude;
		this.altitude=altitude;
		
	}
	
	public boolean equals(LatLon point){
			
		return (latitude == point.latitude) && 
			   (longitude == point.longitude) && 
			   (altitude == point.altitude);
		
	}
	
}