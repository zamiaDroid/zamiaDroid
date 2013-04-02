package uni.projecte.dataLayer.ThesaurusManager.xml;

public class ThesaurusItem {
	
	@SuppressWarnings("unused")
	private static String InfraspecificRank_FORM = "forma.";
	private static String InfraspecificRank_SUBSP = "subsp.";
	@SuppressWarnings("unused")
	private static String InfraspecificRank_VAR = "var.";
	
	
	private String genus="";  						/* 0 */
	
	private String specificEpithet=""; 				/* 1 */
	private String specificEpithetAuthor=""; 		/* 2 */
	
	private String infraspecificRank=InfraspecificRank_SUBSP; 			/* 3 */
	
	private String infraspecificEpithet=""; 		/* 4 */
	private String infraspecificEpithetAuthor=""; 	/* 5 */
	
	private String primaryKey=""; 					/* 6 */
	private String secondaryKey=""; 				/* 7 */
	

	
	
	
	public void mapThItem(String[] values, int[] fields,boolean scape) {
	
		int n=values.length;
		
		for (int i=0; i<n; i++){
			
			if(scape) values[i]=values[i].replace("\"","");
			
			if(fields[i]==0) genus=values[i].replace(" ", "");
			else if(fields[i]==1) specificEpithet=values[i].replace(" ", "");	
			else if(fields[i]==2) specificEpithetAuthor=values[i];
			else if(fields[i]==3) infraspecificRank=values[i];
			else if(fields[i]==4) {
				
				if(values[i].equals("null")) infraspecificEpithet="";
				else infraspecificEpithet=values[i].replace(" ", "");
			
			}
			else if(fields[i]==5) {
				
				if(values[i].equals("null")) infraspecificEpithetAuthor="";
				else infraspecificEpithetAuthor=values[i];
				
			}
			else if(fields[i]==6) primaryKey=values[i];
			else if(fields[i]==7) secondaryKey=values[i];
						
		}

		
	}


	public String getGenus() {
		return genus;
	}



	public String getSpecificEpithet() {
		return specificEpithet;
	}



	public String getSpecificEpithetAuthor() {
		return specificEpithetAuthor;
	}



	public String getInfraspecificRank() {
		return infraspecificRank;
	}



	public String getInfraspecificEpithet() {
		return infraspecificEpithet;
	}



	public String getInfraspecificEpithetAuthor() {
		return infraspecificEpithetAuthor;
	}



	public void setGenus(String genus) {
		this.genus = genus;
	}



	public void setSpecificEpithet(String specificEpithet) {
		this.specificEpithet = specificEpithet;
	}



	public void setSpecificEpithetAuthor(String specificEpithetAuthor) {
		this.specificEpithetAuthor = specificEpithetAuthor;
	}



	public void setInfraspecificRank(String infraspecificRank) {
		this.infraspecificRank = infraspecificRank;
	}



	public void setInfraspecificEpithet(String infraspecificEpithet) {
		this.infraspecificEpithet = infraspecificEpithet;
	}



	public void setInfraspecificEpithetAuthor(String infraspecificEpithetAuthor) {
		this.infraspecificEpithetAuthor = infraspecificEpithetAuthor;
	}


	public String printElement() {
		
		String result="Genus: "+ genus+" SpecificEpithet: "+specificEpithet+" SpecificEpithetAuthor: "+
		getSpecificEpithetAuthor()+" InfraspecificRank: "+infraspecificRank+" InfraspecificEpithet: "+infraspecificEpithet+
		" InfraspecificEpithetAuthor: "+infraspecificEpithetAuthor+" PK: "+primaryKey+" SK: "+secondaryKey;
		
		return result;
	}


	public String getPrimaryKey() {
		return primaryKey;
	}


	public String getSecondaryKey() {
		return secondaryKey;
	}


	public void setPrimaryKey(String primaryKey) {
		this.primaryKey = primaryKey;
	}


	public void setSecondaryKey(String secondaryKey) {
		this.secondaryKey = secondaryKey;
	}

	
}
