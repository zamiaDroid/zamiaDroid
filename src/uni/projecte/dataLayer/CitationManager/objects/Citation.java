package uni.projecte.dataLayer.CitationManager.objects;

import edu.ub.bio.biogeolib.CoordConverter;
import edu.ub.bio.biogeolib.CoordinateLatLon;
import edu.ub.bio.biogeolib.CoordinateUTM;

public class Citation {

	
	private long citationId;
	private String date;
	private double longitude;
	private double latitude;
	
	private int filterLevel;
	
	
	/*  UTM data */
	private String zonaFus;
	private String quadrant;
	private String x;
	private String y;
	
	/*in case of biological data it'll be the taxonName*/
	private String tag;

	
	private boolean checked;
	private int position;
	
	private boolean convertUTM=true;
	
	
	
	public Citation(long citationId,String tag, String date, double latitude, double longitude,int position){
		
		this.citationId=citationId;
		this.latitude=latitude;
		this.longitude=longitude;
		this.tag=tag;
		this.date=date;
		this.position=position;
		
		if(convertUTM) {
			
	        CoordinateUTM utm = CoordConverter.getInstance().toUTM(new CoordinateLatLon(latitude,longitude));
 	       	String utmS=utm.getShortForm();

 	       	String[] utmArray=utmS.split("_");
 	       	
	        //zonaFus=utmS.substring(0, 3);
 	        //quadrant=utmS.substring(3, 5);
 	        //x=utmS.substring(5,utmS.length()-5);
 	        //y=utmS.substring(utmS.length()-5);
 	        zonaFus=utmArray[0];
 	        quadrant=utmArray[1];
 	        x=utmArray[2];
 	        y=utmArray[3];
		}
		
	}
	
	public Citation(long citationId,String tag, String date, double latitude, double longitude,int position,int level){

		this(citationId,tag,date,latitude,longitude,position);
		
		this.filterLevel=level;
		
	}



	public long getCitationId() {
		return citationId;
	}



	public String getDate() {
		return date;
	}



	public double getLongitude() {
		return longitude;
	}



	public double getLatitude() {
		return latitude;
	}



	public String getTag() {
		return tag;
	}



	public boolean isChecked() {
		return checked;
	}



	public void setCitationId(long citationId) {
		this.citationId = citationId;
	}



	public void setDate(String date) {
		this.date = date;
	}



	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}



	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}



	public void setTag(String tag) {
		this.tag = tag;
	}



	public void setChecked(boolean checked) {
		this.checked = checked;
	}



	public int getPosition() {
		return position;
	}



	public void setPosition(int position) {
		this.position = position;
	}



	public boolean belongsToUTM(String utm, String x1,String y1) {

		if(utm.substring(0, 3).equals(zonaFus)){
			
			if(utm.substring(3, 5).equals(quadrant)){
				
				int prec=x1.length();
				
				if(x1.equals(x.substring(0, prec)) && y1.equals(y.substring(0, prec))) return true;
				
			}
						
		}
		
		return false;
		
	}



	public boolean belongsToLatLong(double latitude, int precLat, double longitude, int precLong) {

		double latitudeMax=((1/Math.pow(10, precLat-1))+latitude);
		double longitudeMax=((1/Math.pow(10, precLong-1))+longitude);
		
		return (latitudeMax> this.latitude && this.latitude >= latitude) && (longitudeMax > this.longitude && this.longitude >= longitude);
		
	}

	public int getFilterLevel() {
		return filterLevel;
	}

	public void setFilterLevel(int filterLevel) {
		this.filterLevel = filterLevel;
	}
	
	public String toString(){
		
		return citationId+":"+tag;
		
	}
	
	
}
