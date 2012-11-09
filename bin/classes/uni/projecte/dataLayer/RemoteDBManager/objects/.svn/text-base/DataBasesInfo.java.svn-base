package uni.projecte.dataLayer.RemoteDBManager.objects;

import android.content.Context;
import uni.projecte.R;

public class DataBasesInfo {
	
		
	private String bdbcName;
	private String sibaName;
	private String sifibName;
	private String orcaName;
	private String sivimName;
	private String gbifName;
	private String siocName;
	private String floraCatalanaName;
	private String herbariName;
	private String naturaDigitalName;
	private String encVerteEsp;

	
	private String catalunya;
	private String illesBalears;
	private String andorra;
	private String ppcc;
	private String penIberica;
	private String global;
		
	
	
	public DataBasesInfo(Context baseContext){
		
		sivimName=baseContext.getString(R.string.dbNameSivim);
		sibaName="Andorra";
		sifibName=baseContext.getString(R.string.dbNameSifib);
		orcaName=baseContext.getString(R.string.dBNameOrca);
		bdbcName=baseContext.getString(R.string.dbNameBiocat);
		gbifName=baseContext.getString(R.string.dbNameGbif);
		
		siocName=baseContext.getString(R.string.dbNameSioc);
		floraCatalanaName=baseContext.getString(R.string.dbNameFloraCat);
		herbariName=baseContext.getString(R.string.dbNameHerbari);
		naturaDigitalName=baseContext.getString(R.string.dbNameNaturaDigital);
		encVerteEsp=baseContext.getString(R.string.dbNameEncVertebrados);


		/* Territories */
		catalunya=baseContext.getString(R.string.dBCatalunya);
		illesBalears=baseContext.getString(R.string.dbIllesBalears);
		andorra=baseContext.getString(R.string.dbAndorra);
		ppcc=baseContext.getString(R.string.dbPPCC);
		penIberica=baseContext.getString(R.string.dbIberia);
		global=baseContext.getString(R.string.dbGlobal);

		
	}
	
	
	public String getDataBaseName(String dbId){
		
		String dbName="";
		
			if(dbId.equals("bdbc")) dbName=bdbcName;
			else if(dbId.equals("sifib")) dbName=sifibName;
			else if(dbId.equals("siba")) dbName=sibaName;
			else if(dbId.equals("orca")) dbName=orcaName;
			else if(dbId.equals("sivim")) dbName=sivimName;
			else if(dbId.equals("gbif")) dbName=gbifName;
			else if(dbId.equals("sioc")) dbName=siocName;
			else if(dbId.equals("floraCatalana")) dbName=floraCatalanaName;
			else if(dbId.equals("herbari")) dbName=herbariName;
			else if(dbId.equals("naturdigit")) dbName=naturaDigitalName;
			else if(dbId.equals("enc_vert_iber")) dbName=encVerteEsp;

		return dbName;
			
	}
	
	public String getDataBaseTerritory(String dbId){
		
		String dbTerritory="Undefined";
		
			if(dbId.equals("bdbc")) dbTerritory=catalunya;
			else if(dbId.equals("sifib")) dbTerritory=illesBalears;
			else if(dbId.equals("siba")) dbTerritory=andorra;
			else if(dbId.equals("orca")) dbTerritory=ppcc;
			else if(dbId.equals("sivim")) dbTerritory=penIberica;
			else if(dbId.equals("gbif")) dbTerritory=global;
		
		return dbTerritory;
			
	}
	
	

}
