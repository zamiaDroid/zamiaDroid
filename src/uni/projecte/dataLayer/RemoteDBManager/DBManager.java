package uni.projecte.dataLayer.RemoteDBManager;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Iterator;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import edu.ub.bio.biogeolib.CoordConverter;
import edu.ub.bio.biogeolib.CoordinateLatLon;
import edu.ub.bio.biogeolib.CoordinateUTM;

import uni.projecte.R;
import uni.projecte.controler.RemoteDBControler;
import uni.projecte.maps.UTMDisplay;
import uni.projecte.maps.geocoding.Geocoder;

import android.content.Context;
import android.util.Log;



public class DBManager {
	
	private Context baseContext;
	private static String projTypes="projTypes.cnf";
	private String filumType="";
	private String filumLetter="";
	
	
	/* UTM territory lists  */
	private static String catalunyaUTM="utm_catalunya.tab";
	private static String ppccUTM="utm_ppcc.tab";
	private static String balearsUTM="utm_balears.tab";	
	private static String pyreneesUTM="utm_pyrenees.tab";	

	
	/* DataBase Id's */
	
	/*
		public static final int DB_NULL=0;
		public static final int DB_BIOCAT=1;
		public static final int DB_SIFIB=2;
		public static final int DB_SIBA=3;
		public static final int DB_ORCA=4;
		public static final int DB_SIVIM=5;
		public static final int DB_GBIF=6;
		public static final int DB_SIARE=6;
	*/
	
	

	public static String locCatalunya="catalunya";
	public static String locIberia="iberia";
	public static String locEspaña="españa";
	public static String locGlobal="global";
	public static String locPyrenees="pyrenees";
	public static String locIllesBalears="illesBalears";
	public static String locPPCC="ppcc";

	
	private String locality;
	
	public static int AVAILABLE_DB =0;
	public static int NO_DB_TERRITORY=-1;
	public static int NO_DB_FILUM=-2;
	public static int NO_SERVER_NOT_FOUND=-3;
	
	
	/* -1: no territory ---- -2: no dbFilum  */
	private int errorCode=-2;
	
	/* list of available db's */
	private ArrayList<Integer> availableDbByFilum;
	
	/* Boolean of active DB's */
	private boolean[] allowedDB;
	

	private String countryCode;
	private RemoteDBControler remoteDBCnt;
	
	
	
	public DBManager(Context baseContext, boolean checkAvailableDBs){
		
		this.baseContext=baseContext;
		if(checkAvailableDBs) remoteDBCnt=new RemoteDBControler(baseContext);
		
	}

	
	/* WebService that gets the locality near to @latitude and @longitude provided  */
	
	public String getLocationCountryCode(double latitude,double longitude){
		
		String localityName = Geocoder.reverseGeocode(latitude,longitude);
		String country="";
		
		if(localityName!=null && !localityName.equals(":")) {
			
			country=localityName.split(":")[1];
			locality=localityName.split(":")[0];
			if(locality.equals("null")) locality="";
		
		}
		
		return country;
		
	}
	
	
	/* Countries belonging to the Iberian Peninsula */
	private boolean iberianPeninsula(String contrycode){
		
		String contryCodesList="ES:PT:FR:MA:AD";
		
		return contryCodesList.contains(contrycode);
				
	}
	

	
	/*
	 * Checks if @utm is included on the UTM list for the @territory
	 * 
	 * 	@utm has no zone and letter
	 * 
	 */
	
	private boolean checkUTMList(String utm, String territory){
		
		InputStream is;
		try {

			is = baseContext.getResources().getAssets().open(territory);

		} catch (IOException e) {

			return false;
		}

		String utmList=convertStreamToString(is);

		return utmList.contains(utm);
		
	}
	
	
	
	public int getDefaultDBConnection(double latitude, double longitude,String filum){
		
		countryCode=getLocationCountryCode(latitude, longitude);
		
		if(!countryCode.equals("")){
			
			createAvailableDBArray(latitude, longitude, filum);
			remoteDBCnt.printAvailableDB(allowedDB);
			
		}
		else{
			
			errorCode=NO_SERVER_NOT_FOUND;
			
		}
	
		
		return errorCode;
		
		
	}
	
	
	public String getPresenceTag(int tabId,int dbIdNumber){
		
		String type="";

		switch (tabId) {
		
			case 0:
				
				type=baseContext.getString(R.string.presenceRemoteDB);

				break;
				
			case 1:
				
				type=baseContext.getString(R.string.presenceLocalDB);

				break;
	
			case 2:
			
				type=baseContext.getString(R.string.presenceTotalDB);
				break;

	
			default:
				
				type=baseContext.getString(R.string.presenceRemoteDB);

				
				break;
		}
			
			
			return remoteDBCnt.getDBTagdByDBId(dbIdNumber)+"_"+type;
		
	}

		
	

	public AbstractDBConnection getDBInstance(int dbIdNumber,String filum,String language) {
		
		AbstractDBConnection absDBConnection = null;

		String dbTag=remoteDBCnt.getDBTagdByDBId(dbIdNumber);
		
		
		if(dbTag.equals("bdbc")){
					
			absDBConnection=new BiocatDBConnection(baseContext, filum, language);
		
		}
		else if(dbTag.equals("siba")){
				
			absDBConnection=new OrcaDBConnection(baseContext, filum, language);
				
		}
		else if(dbTag.equals("sifib")){

			absDBConnection=new SifibDBConnection(baseContext, filum, language);
			
		}
		else if(dbTag.equals("sivim")){

			absDBConnection=new SivimDBConnection(baseContext, filum, language);

		}			
		else if(dbTag.equals("siare")){
		
			absDBConnection=new SiareDBConnection(baseContext, filum, language);

		}
		else if(dbTag.equals("gbif")){
			
			absDBConnection=new GBIFDBConnection(baseContext, filum, language);

		}
		else if(dbTag.equals("pyrenees")){
			
			absDBConnection=new FloraPyrenaeaDBConnection(baseContext, filum, language);

		}

		return absDBConnection;
	
	}
	
	
	
	/*
	 * 
	 * This method returns a boolean map with the db's available for the 
	 * provided location with @latitude and @longitude and chosen @filum
	 * 
	 */
	
	private void createAvailableDBArray(double latitude, double longitude,String filum){
		
		
			allowedDB=new boolean[remoteDBCnt.getDBCount()];
			availableDbByFilum=remoteDBCnt.getDbsListByFilum(filum);
			
    		CoordinateUTM utmConv = CoordConverter.getInstance().toUTM(new CoordinateLatLon(latitude,longitude));
    		String utm=UTMDisplay.getBdbcUTM10x10(utmConv.getShortForm());
    		    		
    		determineTerritory(utm);
    		determineAvailableDB();   		
    		    		
		
	}
	
	private void determineAvailableDB() {

		Iterator<Integer> it=availableDbByFilum.iterator();
		boolean found=false;
		
		while(it.hasNext() && !found){
			
			int dbId=it.next();
			
			if(allowedDB[dbId-1]){ 
				
				found=true;
				errorCode=dbId;
				
			}		
		}
		
	}


	private void determineTerritory(String utm){
		
		remoteDBCnt.filterDbByTerritory(locGlobal,allowedDB);
		
		/* Inside Iberian Peninsula  */
		if(iberianPeninsula(countryCode)){
		
			remoteDBCnt.filterDbByTerritory(locIberia,allowedDB);
			
			
			/* UTM zone 31 */
			if(utm.startsWith("31") || utm.startsWith("30")){
				
				/* Pyrenees */
				
				if(checkUTMList(utm.replace("_", ""),pyreneesUTM)){
					
					remoteDBCnt.filterDbByTerritory(locPyrenees,allowedDB);
				
				}
				
				
				/* Països Catalans */
				
				if(checkUTMList(utm.substring(4,6)+utm.substring(7,9),ppccUTM)){
					
					remoteDBCnt.filterDbByTerritory(locPPCC,allowedDB);
				
				}
				
				/* Catalunya */
				if(checkUTMList(utm.substring(4,6)+utm.substring(7,9), catalunyaUTM)){
					
					remoteDBCnt.filterDbByTerritory(locCatalunya,allowedDB);
					
				}
				
				/* Illes Balears */	
				if(checkUTMList(utm.replace("_", ""), balearsUTM)){
					
					remoteDBCnt.filterDbByTerritory(locIllesBalears,allowedDB);
					
				}
				
				/* Andorra */
				
				/* País Valencià */
				
				

				
			}
			/* Aprox: outside Països Catalans */
			else{
				
				
				
			}
		
		}
			
		/* Outside Iberian Peninsula */
		else{
			
			//errorCode=-1;

			
		}
		
		
		
		
	}
	

	
	
	
	public void getFilum(String projectType){
		
		InputStream is;
		
		boolean found=false;
		
		if(projectType==null){
			
			filumType="Flora";
			filumLetter="F";
			
		}
		
		else{
		
			try {
				
				is = baseContext.getResources().getAssets().open(projTypes);
				String jsonTypes=convertStreamToString(is);
	
		        JSONObject json = new JSONObject(jsonTypes);
	              
	            JSONArray jsonTypesList=json.getJSONArray("mainTypes");
	            
	            for(int i=0;i<jsonTypesList.length()&& !found;i++){
	          	  
	          	  JSONObject proj=jsonTypesList.getJSONObject(i);
	          	  
	          	 // ["projType","projId","projectName","projDescription","thName"]
	          	  JSONArray nameArray=proj.names();
	          	  JSONArray valArray=proj.toJSONArray(nameArray);
	          	  	                  
	                  String types=valArray.getString(0);
	                  
	                  if(types.contains(projectType)) {
	                	  
	                	  filumType=valArray.getString(2);
	                	  found=true;
	                	  filumLetter=valArray.getString(1);
	                	  
	                  }
	                  //projId, projName, projType, projDesc, projTh
	                 Log.i("JParser",nameArray.getString(0));
	                  
	              } 
	            	
				
			} catch (IOException e) {
				e.printStackTrace();
			}
			catch (JSONException e) {
				e.printStackTrace();
			}
		}
		
	}
	
	
	
	   private static String convertStreamToString(InputStream is) {
	        /*
	         * To convert the InputStream to String we use the BufferedReader.readLine()
	         * method. We iterate until the BufferedReader return null which means
	         * there's no more data to read. Each line will appended to a StringBuilder
	         * and returned as String.
	         */
	        BufferedReader reader;
			StringBuilder sb = new StringBuilder();

			try {
				reader = new BufferedReader(new InputStreamReader(is,"UTF-8"));
			
	 	        String line = null;
		
	            while ((line = reader.readLine()) != null) {
	                sb.append(line + "\n");
	            }
	            
	        } catch (IOException e) {
	            e.printStackTrace();
	        }finally {
	            try {
	                is.close();
	            } catch (IOException e) {
	                e.printStackTrace();
	            }
	         	        }
	        return sb.toString();
	    }


	public String getFilumType() {
		return filumType;
	}


	public void setFilumType(String filumType) {
		this.filumType = filumType;
	}


	public String getFilumLetter() {
		return this.filumLetter;
	}


	public String getLocality() {
		return locality;
	}


	public int getErrorCode() {
		return errorCode;
	}


	

}
