package uni.projecte.dataLayer.RemoteDBManager;

import uni.projecte.R;
import uni.projecte.dataLayer.RemoteDBManager.dataParsers.GBIFDBResponseHandler;
import uni.projecte.dataLayer.RemoteDBManager.utils.GbifFilumConverter;
import uni.projecte.dataTypes.RemoteCitationSet;
import uni.projecte.dataTypes.RemoteTaxonSet;
import uni.projecte.maps.UTMDisplay;
import uni.projecte.maps.UTMGrid;
import uni.projecte.maps.utils.UTMUtils;
import edu.ub.bio.biogeolib.CoordConverter;
import edu.ub.bio.biogeolib.CoordinateLatLon;
import edu.ub.bio.biogeolib.CoordinateUTM;

import android.content.Context;

public class GBIFDBConnection extends AbstractDBConnection {
	
	private CoordinateLatLon upperLeft;
	private CoordinateLatLon lowerRight;
	
	private String restBaseURL="http://data.gbif.org/ws/rest/occurrence/list";
	private String formatBrief="?format=brief";
	private String formatDarwin="?format=darwin";
	
	private GBIFDBResponseHandler gbifResponse;
	
	private String utm;
	
	private String location;
	
	
	
	

	public GBIFDBConnection(Context baseContext, String filum, String language) {
		
		super(baseContext, filum, language);
		
		dbName=baseContext.getString(R.string.dbNameGbif);

		gbifResponse=new GBIFDBResponseHandler();
		
	}
	
	
	/*
	 * 
	 * 	If we want a list of taxon occurrences:
	 *  GBIF requires a bounding box determined by a maximum and minimum latitude and longitude.
	 *  
	 *  We'll create a correspondence between that bounding box and the UTM grids:
	 *  
	 *  	(10x10 km) or (1x1 km)
	 *  
	 *  With the latitude and longitude we determine the center of the utmGrid and upperLeft 
	 *  and lowerRight points are stored.
	 * 
	 */
	
	@Override
	public void setLocation(double latitude, double longitude, boolean utm1x1){

		super.setLocation(latitude, longitude, false);
			 
  		CoordinateUTM utm = CoordConverter.getInstance().toUTM(new CoordinateLatLon(latitude,longitude));
  		
  		if(! utm1x1) this.utm=UTMDisplay.getBdbcUTM10x10(utm.getShortForm());
  		else this.utm=UTMDisplay.get1x1UTM(utm.getShortForm());
  		
  		UTMGrid grid=null;
  		
  		if(!utm1x1) {
  			
  			grid=UTMUtils.getUTMBoundingBox(utm,latitude>=0, 10000.0);
  		}
  		else{
  			
  			grid=UTMUtils.getUTMBoundingBox(utm,latitude>=0, 1000.0);
  			
  		}
  		
  		upperLeft=grid.upperLeft;
  		lowerRight=grid.lowerRight;
  		
		// locations bounded by latitudes and longitudes provided 
	
  		location="&minlatitude="+lowerRight.getLat()+
  		"&maxlatitude="+upperLeft.getLat()+
  		"&maxlongitude="+lowerRight.getLon()+
  		"&minlongitude="+upperLeft.getLon();
		
	}
	
	
	/*
	 * 	----- REST interface -- (ocurrences) ---
	 * 
	 * 	http://data.gbif.org/ws/rest/occurrence/list?format=darwin
	 * 	&minlatitude=41.0874&maxlatitude=41.179
	 * 	&minlongitude=1.32969&maxlongitude=1.45099
	 *
	 */
	
	@Override
	public int serviceGetTaxonList() {
				
		//String kingdom="&taxonconceptkey=5";
		String kingdom=GbifFilumConverter.getGbifFilumCorresondance(filum);
		
		//citList=new RemoteCitationSet(utm);
		
		projList=new RemoteTaxonSet(utm);

		loadRemoteTaxons(restBaseURL+formatBrief+kingdom+location, projList);
		
		projList.sort();
		
		return projList.numElements();
	}
	
	private void loadRemoteTaxons(String url,RemoteTaxonSet projList){
		
		int result=gbifResponse.loadTaxons(url, projList);

		if(result==GBIFDBResponseHandler.NEXT_URL) loadRemoteTaxons(gbifResponse.getNextURL(), projList);

		
	}


	/*
	 *  ---- REST interface ---- (taxon)
	 *  
	 *  http://data.gbif.org/ws/rest/taxon/get/50782904
	 *  	
	 */
	
	@Override
	public String serviceGetTaxonInfoUrl(String taxonKey) {

		return "";
	}


	/*
	 *  ---- REST interface ---- (occurence)
	 *  
	 *  Info: http://data.gbif.org/ws/rest/occurrence/
	 *  http://data.gbif.org/ws/rest/occurrence/get/occurrenceKeyGBIF
	 *  	
	 */
		
	@Override
	public int serviceGetTaxonCitations(String scientificname) {
		
		citList=new RemoteCitationSet(utm);
		
		scientificname=scientificname.replace(" ","+");
		
		int result=gbifResponse.loadCitations(restBaseURL+formatDarwin+"&scientificname="+scientificname+location, citList);

		return citList.numElements();
	}
	
	
	
	
	
	@Override
	public boolean hasUTM1x1() {

		return true;
	}
	
	@Override
	public String getPrettyLocation(){
		
		return utm.replace("_", "");
		
	}
	
	@Override
	public boolean useThId() {

		return false;
		
	}
	
	
	

}
