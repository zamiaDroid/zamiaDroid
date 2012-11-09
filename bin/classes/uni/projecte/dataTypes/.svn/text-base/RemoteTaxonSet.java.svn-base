package uni.projecte.dataTypes;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;

import android.util.Log;

import uni.projecte.dataLayer.RemoteDBManager.RemoteTaxon;
import uni.projecte.dataLayer.utils.TaxonUtils;

public class RemoteTaxonSet {
	
	private ArrayList<RemoteTaxon> taxonList;
	private ArrayList<RemoteTaxon> taxonListOrdered;

	
	/* <taxon,taxonId> */
	private HashMap<String, RemoteTaxon> uniqueNameList;
	private String utm;
	
	private ArrayList<String> citationTags;
	
	private boolean ordered=true;

		
 	public RemoteTaxonSet(String utm){
 		
 		this.utm=utm;
 		taxonList=new ArrayList<RemoteTaxon>();
 		taxonListOrdered=new ArrayList<RemoteTaxon>();
 		uniqueNameList= new HashMap<String, RemoteTaxon>();
 		citationTags = new ArrayList<String>();
 		
 	}
 	
 	public ArrayList<RemoteTaxon> getTaxonList(){
 		
 		if(ordered) return taxonList;
 		else return taxonListOrdered;
 		
 	}
 	
 	public void printList(){
 		
 		Iterator<RemoteTaxon> it=taxonList.iterator();
 		
 		while(it.hasNext()){
 			
 			RemoteTaxon rt=it.next();
 			
 			Log.i("BD", rt.getTaxon()+"\t");
 		}
 		
 	}
 	
 	public int numElements(){
 		
 		return taxonList.size();
 		
 	}
 	
 	public void addTaxon(String taxon, String taxonId){
 		
 		taxon=taxon.replace("  "," ");
 		
 		RemoteTaxon rt= new RemoteTaxon(taxon, taxonId);
 		
 		RemoteTaxon old=uniqueNameList.put(rt.getCleanTaxon(), rt);
 		
 		if(old==null){
 			
	 		taxonList.add(rt);
	 		citationTags.add(rt.getCleanTaxon());
	 		
 		}
 		
 	}
 	
 	
 	
	private void createAlphaOrderedList() {
		
		Collections.sort(citationTags);
			
		Iterator<String> itOrder=citationTags.iterator();
			
		taxonListOrdered= new ArrayList<RemoteTaxon>();
			
			while(itOrder.hasNext()){
				
				String citation=itOrder.next();
				
				RemoteTaxon tmp=uniqueNameList.get(citation);
				
				taxonListOrdered.add(tmp);
		
				
			}
		
	}
 	
 	public boolean existsTaxon(String taxon){
 		
 		if(uniqueNameList.get(taxon) != null) return true;
 		else return false;
 		
 	}
 	
 	public HashMap<String, RemoteTaxon> getUniqueNameList() {
		return uniqueNameList;
	}

	public void setUniqueNameList(HashMap<String, RemoteTaxon> uniqueNameList) {
		this.uniqueNameList = uniqueNameList;
	}

	public String getTaxonId(String taxon){
 		
		RemoteTaxon rt=uniqueNameList.get(TaxonUtils.removeAuthors(taxon));
		
 		if(rt!=null){
 			
 			String taxonId=rt.getTaxonId();
 			return taxonId;

 		}
 		
 		else return "";
 		
 	}

	public void setOrdered(boolean ordered) {
		this.ordered = ordered;
	}

	public boolean isOrdered() {
		return ordered;
	}

	public void sort() {

		createAlphaOrderedList();
		ordered=false;
		
	}
 	

}
