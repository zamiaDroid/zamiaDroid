package uni.projecte.dataTypes;

import java.util.ArrayList;
import java.util.HashMap;

import uni.projecte.dataLayer.RemoteDBManager.RemoteTaxon;

public class TotalTaxonSet {
	
	ArrayList<RemoteTaxon> taxonList;
	HashMap<String, String> uniqueNameList;
	private String utm;
		
 	public TotalTaxonSet(String utm){
 		
 		this.utm=utm;
 		taxonList=new ArrayList<RemoteTaxon>();
 		uniqueNameList= new HashMap<String, String>();
 		
 		
 		
 	}
 	
 	public ArrayList<RemoteTaxon> getTaxonList(){
 		
 		return taxonList;
 		
 	}
 	
 	public void addTaxon(String taxon, String taxonId){
 		
 		taxon=taxon.replace("  "," ");
 		
 		taxonList.add(new RemoteTaxon(taxon, taxonId));

 		uniqueNameList.put(taxon, taxonId);
 		
 	}
 	
 	public boolean existsTaxon(String taxon){
 		
 		if(uniqueNameList.get(taxon) != null) return true;
 		else return false;
 		
 	}
 	
 	public HashMap<String, String> getUniqueNameList() {
		return uniqueNameList;
	}

	public void setUniqueNameList(HashMap<String, String> uniqueNameList) {
		this.uniqueNameList = uniqueNameList;
	}

	public String getTaxonId(String taxon){
 		
 		String taxonId=uniqueNameList.get(taxon);
 		
 		if( taxonId!= null) return taxonId;
 		else return "";
 		
 	}
 	

}
