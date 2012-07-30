package uni.projecte.dataLayer.ThesaurusManager.xml;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.InputStreamReader;

import android.util.Log;

import uni.projecte.controler.ThesaurusControler;

public class PlainThesaurusReader {
	
	/*
	 * Fields contained in the Thesaurus - Darwin Core 2 -> 
	 * http://wiki.tdwg.org/twiki/bin/view/DarwinCore/DarwinCoreDraftStandard
	 * 
	 * 
		 * 	Genus | SpecificEpithet |          | InfraspecificRank | InfraspecificEpithet |			| primaryKey | foreignKey
		 * 					| authorSpecies |	     							| authorSubspecies |
	 *
	 */

	public static String GENUS="Genus";  													/* 0 */
	
	public static String SPECIFIC_EPITHET="SpecificEpithet"; 								/* 1 */
	public static String SPECIFIC_EPITHET_AUTHOR="SpecificEpithetAuthor"; 				/* 2 */
	
	public static String INFRA_SPECIFIC_RANK="InfraspecificRank"; 						/* 3 */
	public static String INFRA_SPECIFIC_EPITHET="InfraspecificEpithet"; 					/* 4 */
	public static String INFRA_SPECIFIC_EPITHET_AUTHOR="InfraspecificEpithetAuthor";		/* 5 */
	
	public static String PRIMARY_KEY="PrimaryKey";		/* 6 */
	public static String SECONDARY_KEY="SecondaryKey";		/* 7 */

	
	//private String taxonSeparator;
	private String fieldSeparator="\t";
	
	
	private ThesaurusControler thCont;
	private String[] fields;
	private int[] fieldsPosition;
	private String fileName;
	
	private boolean omitFirstLine;
	private boolean scape;
	
	
	/* 
	 * 
	 * InfraspecificRank: {"subsp.", "var.", "forma."} 
	 * 
	 * Taxon separator -> taxonSeparator: { ";" , "\n", .... }
	 * Field separator->  fieldSeparator: { ";" , " ", ......}
	 *  
	 */

	
	public PlainThesaurusReader(String fileName,String fieldSeparator){
		
		this.fieldSeparator=fieldSeparator;
		this.fileName=fileName;


	}
	
	
	private int[] getFieldsCorrespondase(String[] fields) {

		int n=fields.length;
		int[] fieldsOrder=new int[n];
	
		
		for(int i=0; i<n; i++){
			
			if(fields[i].equals(GENUS)) fieldsOrder[i]=0;
			else if(fields[i].equals(SPECIFIC_EPITHET)) fieldsOrder[i]=1;
			else if(fields[i].equals(SPECIFIC_EPITHET_AUTHOR)) fieldsOrder[i]=2;
			else if(fields[i].equals(INFRA_SPECIFIC_RANK)) fieldsOrder[i]=3;
			else if(fields[i].equals(INFRA_SPECIFIC_EPITHET)) fieldsOrder[i]=4;
			else if(fields[i].equals(INFRA_SPECIFIC_EPITHET_AUTHOR)) fieldsOrder[i]=5;
			else if(fields[i].equals(PRIMARY_KEY)) fieldsOrder[i]=6;
			else if(fields[i].equals(SECONDARY_KEY)) fieldsOrder[i]=7;

			else fieldsOrder[i]=-1;
		}
		
		
		return fieldsOrder;
	}


	public String readFileFirstLine(){
		
		String strLine="";

		try{
		
	        FileInputStream fstream = new FileInputStream(fileName);
	        // Get the object of DataInputStream
	        DataInputStream in = new DataInputStream(fstream);
	        BufferedReader br = new BufferedReader(new InputStreamReader(in));
	
	        strLine = br.readLine();
	
	        in.close();
        	        
        }catch (Exception e){
          
        	System.err.println("Error: " + e.getMessage());
        	
        	return "";
        	
        }

        
        return strLine;		
		
		
		
	}
	
	public boolean readFile(String[] fields, ThesaurusControler thCont){
		
		this.fields=fields;
		this.thCont=thCont;
		this.fieldsPosition=getFieldsCorrespondase(fields);
		boolean omitFirst=omitFirstLine;

		
	    try{
	 
	        FileInputStream fstream = new FileInputStream(fileName);
	        // Get the object of DataInputStream
	        DataInputStream in = new DataInputStream(fstream);
	           
	        BufferedReader br = new BufferedReader(new InputStreamReader(in));

	        String strLine;
	        

	    	thCont.startTransaction();
	        
	        
		        while ((strLine = br.readLine()) != null)   {
	
		        	if(omitFirst) omitFirst=false;
		        	else readTaxon(strLine);
		          
		        }
	
		        in.close();
		        

	        	        
	        }catch (Exception e){
	          	        	
	        	return true;
	        	
	        }
	  

	        
	        return false;		
		
	}
		
	/*
	 * Example:
	 * 
	 * 	taxonSeparator="\n"
	 * 	fieldSeparator="\t"
	 * 
	 * Tadorna"\t"tadorna\n
	 * Plectropterus\tgambensis\n
	 * Nettapus\tcoromandelianus\n
	 *
	 * 
	 */
	
	public long readTaxon(String line){
		
		String[] splittedItems=line.split(fieldSeparator);
		
		if(splittedItems.length == fields.length){
			
			insertIntoDataBase(splittedItems);
			
		}
		/* Wrong format */
		else{
			
			
			
		}
		
		return -1;
		
		
	}
	
	private void insertIntoDataBase(String[] values){
		
		ThesaurusItem thItem= new ThesaurusItem();
		thItem.mapThItem(values,fieldsPosition,scape);

		long thItemId=thCont.addThElement(thItem);
		
		Log.i("ThReader","ThId: "+thItemId+" "+thItem.printElement());

		
	}


	public boolean isOmitFirstLine() {
		return omitFirstLine;
	}


	public boolean isScape() {
		return scape;
	}


	public void setOmitFirstLine(boolean omitFirstLine) {
		this.omitFirstLine = omitFirstLine;
	}


	public void setScape(boolean scape) {
		this.scape = scape;
	}


	public String getFieldSeparator() {
		return fieldSeparator;
	}


	public void setFieldSeparator(String fieldSeparator) {
		this.fieldSeparator = fieldSeparator;
	}
	

}
