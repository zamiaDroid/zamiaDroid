package uni.projecte.dataTypes;

import java.util.ArrayList;
import java.util.HashMap;

public class RemoteCitationSet {
	
	ArrayList<RemoteCitation> citationList;
	HashMap<String, String> uniqueNameList;
	private String utm;
		
 	public RemoteCitationSet(String utm){
 		
 		this.utm=utm;
 		citationList=new ArrayList<RemoteCitation>();
 		uniqueNameList= new HashMap<String, String>();
 		 		
 		
 	}
 	
 	public ArrayList<RemoteCitation> getCitationList(){
 		
 		return citationList;
 		
 	}
 	
 	public int numElements(){
 		
 		return citationList.size();
 		
 	}
 	
 	public void addCitation(String locality, String bib){

 		locality=locality.replace("; ; ","");
 		citationList.add(new RemoteCitation(locality,bib));
  		
 		//uniqueNameList.put(taxon, taxonId);
 		
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


