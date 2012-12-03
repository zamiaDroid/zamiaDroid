package uni.projecte.dataLayer.utils;

import android.util.Log;
import android.util.Pair;
import uni.projecte.dataLayer.ThesaurusManager.ThesaurusElement;
import uni.projecte.dataTypes.TaxonElement;

public class TaxonUtils {
	
	public static String removeAuthors(String taxon){
		
		String[] splittedTaxon=taxon.split(" ");
		
		if(splittedTaxon.length>=2){
			
			String genus=splittedTaxon[0];
			String specie=splittedTaxon[1];
			
			int rankPos=containsSubEpitet(taxon);
			
			if(rankPos>-1){
	
				String sub=taxon.substring(rankPos);
				
				String[] split=sub.split(" ");
				
				if(split.length>=2) return genus+" "+specie+" "+split[1];
				else return genus+" "+specie;
				
			}
			
			else return genus+" "+specie;
		
		}
		else return taxon;
		
		
	}

	private static int containsSubEpitet(String taxon) {

		int i=taxon.indexOf("subsp.");
		
		if(i<0){
			
			i=taxon.indexOf("form.");
			
			if(i<0){
				
				i=taxon.indexOf("var.");
				
				if(i<0){
					
					return -1;
					
				}
				else{
					
					return i+4;
					
				}
				
			}
			else{
				
				return i+5;

			}
		}
		else{
			
			return i+6;
			
		}
		
	//	if(taxon.contains("subsp.") || taxon.contains("form.") || taxon.contains("var.") || taxon.contains("subvar.") ) return true;
	//	else return false;
		
	}
	
	
	private static Pair<Integer, Integer> rankPosition(String taxon) {

		int i=taxon.indexOf("subsp.");
		
		if(i<0){
			
			i=taxon.indexOf("form.");
			
			if(i<0){
				
				i=taxon.indexOf("var.");
				
				if(i<0){
					
					return new Pair<Integer,Integer>(-1, -1);
					
				}
				else{
					
					return new Pair<Integer,Integer>(i, 4);
					
				}
				
			}
			else{
				
				return new Pair<Integer,Integer>(i, 5);

			}
		}
		else{
			
			return new Pair<Integer,Integer>(i, 6);
			
		}
		
	//	if(taxon.contains("subsp.") || taxon.contains("form.") || taxon.contains("var.") || taxon.contains("subvar.") ) return true;
	//	else return false;
		
	}


	
	public static TaxonElement mapThesaurusElement(String taxonName) {

	//genus, specificEpitet, specificEpitetAuthor, [ {rank: subsp., form., var. } infraSpecEpitet, infraSpecEpitetAuthor ]

		if(taxonName!=null) taxonName=taxonName.trim();
		
		//Log.i("Thesaurus","MapTh: "+taxonName);
		
		String genus="";
		String specificEpithet="";
		String specificEpithetAuthor="";
		String rank="";
		String infraspecEpithet="";
		String infraspecEpithetAuthor="";
		
		String[] taxonSplitet=taxonName.split(" ");
		
		if(taxonSplitet.length>0) {
			
			genus=taxonSplitet[0];
			
			if(taxonSplitet.length>1){ 
				
				specificEpithet=taxonSplitet[1];
				
				if(taxonSplitet.length>2){ 
				
					Pair<Integer,Integer> subEpPos=rankPosition(taxonName);
					
					if(subEpPos.first<0){
					
						specificEpithetAuthor=taxonName.substring(taxonName.indexOf(taxonSplitet[2]));

					}
					else{
						
						//No specific Epithet Autor
						if(taxonName.indexOf(taxonSplitet[2])>=subEpPos.first-1){
							
							specificEpithetAuthor="";
							
						}
						else{
						
							specificEpithetAuthor=taxonName.substring(taxonName.indexOf(taxonSplitet[2]),subEpPos.first-1);
						
						}
						
						String infraSpec=taxonName.substring(subEpPos.first);
						String[] infraSpecSplited=infraSpec.split(" ");
						
						if(infraSpecSplited.length>1){
							
							rank=infraSpecSplited[0];
							infraspecEpithet=infraSpecSplited[1];
							
							if(infraSpecSplited.length>2){
								
								infraspecEpithetAuthor=infraSpec.substring(infraSpec.indexOf(infraSpecSplited[2]));																
							}
							
						}
						
					}
				}
			}
		
		}

		TaxonElement taxElem=new TaxonElement(genus, specificEpithet, specificEpithetAuthor, 
				rank, infraspecEpithet, infraspecEpithetAuthor, "","");
		

		return taxElem;
	}

}
