package uni.projecte.dataLayer.RemoteDBManager;

import uni.projecte.dataLayer.RemoteDBManager.dataParsers.SiareResponseHandler;
import uni.projecte.dataTypes.RemoteTaxonSet;
import uni.projecte.maps.UTMDisplay;
import edu.ub.bio.biogeolib.CoordConverter;
import edu.ub.bio.biogeolib.CoordinateLatLon;
import edu.ub.bio.biogeolib.CoordinateUTM;
import android.content.Context;

public class SiareDBConnection extends AbstractDBConnection {
	
	private String utmSpain;
	private String utm;
	private String serviceTaxonList="http://siare.herpetologica.es/bdh/especiesxutm10_ajax/";
	private SiareResponseHandler siareResp;
	private String dbName="Servidor de Información de Anfibios y Reptiles de España";



	public SiareDBConnection(Context baseContext, String filum, String language) {
		super(baseContext, filum, language);

		siareResp=new SiareResponseHandler();
		
	}

	
	@Override
	public void setLocation(double latitude, double longitude, boolean utm1x1){

		super.setLocation(latitude, longitude, false);
		 
  		CoordinateUTM utmConverter = CoordConverter.getInstance().toUTM(new CoordinateLatLon(latitude,longitude));
  		
  		utm=UTMDisplay.convertUTM(utmConverter.getShortForm(),"10km",false);
  		
  		utmSpain=utm.substring(3).replace(" ", "");
		
	}

	@Override
	public String serviceGetTaxonInfoUrl(String codiSpec) {

		return serviceTaxonList+utmSpain;
		
	}
	
	@Override
	public int serviceGetTaxonList() {

		projList=new RemoteTaxonSet(utm);
		
		String url=serviceTaxonList+utmSpain;
		
		siareResp.loadTaxons(url, projList);
		
	    return projList.numElements();
		
	}
	


	private String getServiceTaxonListURL() {

		return null;
		
	}
	
	@Override
	public String getPrettyLocation(){
		
		return utm;
		
	}


	@Override
	public String getDbName() {
		return dbName;
	}


	@Override
	public int serviceGetTaxonCitations(String codiOrc) {

		return 0;
	}

}
