package uni.projecte.dataTypes;

import java.util.ArrayList;
import java.util.HashMap;

import uni.projecte.dataLayer.utils.TaxonUtils;
import uni.projecte.maps.UTMDisplay;

import edu.ub.bio.biogeolib.CoordConverter;
import edu.ub.bio.biogeolib.CoordinateLatLon;
import edu.ub.bio.biogeolib.CoordinateUTM;

public class LocalTaxonSet {
	
	ArrayList<LocalTaxon> taxonList;
	HashMap<String, String> uniqueNameList;
	private String utm;
		
 	public LocalTaxonSet(String utm){
 		
 		this.utm=utm;
 		taxonList=new ArrayList<LocalTaxon>();
 		uniqueNameList= new HashMap<String, String>();
 		
 	}
 	
 	public LocalTaxonSet(){
 		
 		taxonList=new ArrayList<LocalTaxon>();
 		uniqueNameList= new HashMap<String, String>();
 		 		
 	}
 	
 	public ArrayList<LocalTaxon> getTaxonList(){
 		
 		return taxonList;
 		
 	}
 	 	
 	public HashMap<String, String> getUniqueNameList() {
		
 		return uniqueNameList;
 		
	}

	public String getUtm() {
		return utm;
	}

	public void addTaxon(long id,String taxon, double latitude, double longitude, String date){
 		
 		taxon=taxon.replace("  "," ");
 		
 		String taxonWOAuthor=TaxonUtils.removeAuthors(taxon);
 		
		CoordinateUTM utmC = CoordConverter.getInstance().toUTM(new CoordinateLatLon(latitude,longitude));
		String utmS=UTMDisplay.getBdbcUTM10x10(utmC.getShortForm());
 		
		if(latitude<100 && longitude<190 && utm.equals(utmS)){
		 		
	 		if(uniqueNameList.get(taxonWOAuthor+":"+utmS) == null){
	 			
	 			taxonList.add(new LocalTaxon(id,taxon, latitude, longitude, date));
	
	 	 		uniqueNameList.put(taxonWOAuthor+":"+utmS, date);
	 			
	 			
	 		}
 		
		}
 	
 		
 	}
	
	public void insertTaxon(long id,String taxon, double latitude, double longitude, String date){
 		
 		taxon=taxon.replace("  "," ");
 		
 		String taxonWOAuthor=TaxonUtils.removeAuthors(taxon);
		
		if(uniqueNameList.get(taxonWOAuthor) == null){
	 			
	 			taxonList.add(new LocalTaxon(id,taxon, latitude, longitude, date));
	 	 		uniqueNameList.put(taxonWOAuthor, date);
	 			
		}

 	
 		
 	}
 	
 	public boolean existsTaxon(String taxon){
 		
 		if(uniqueNameList.get(taxon+":"+utm) != null) return true;
 		else return false;
 		
 	}

}
