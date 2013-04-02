package uni.projecte.dataLayer.RemoteDBManager;

import uni.projecte.dataTypes.RemoteCitationSet;
import uni.projecte.dataTypes.RemoteTaxonSet;
import android.content.Context;

public abstract class AbstractDBConnection {
	
	protected Context baseContext;
	
	protected String dbName;
	protected String filum;
	protected String location;
	
	protected String systemLanguage="ca";
	
	protected RemoteTaxonSet projList;
	protected RemoteCitationSet citList;
	
	protected double latitude;
	protected double longitude;
	
	public static final int DB_UNAVAILABLE=-1;
	public static final int DB_SERVER_ERROR=-2;

	
	public AbstractDBConnection(Context baseContext,String filum,String language){
		
		this.systemLanguage=language;
		this.baseContext=baseContext;
		this.filum=filum;

	}
	
	public abstract int serviceGetTaxonList();
	
	public abstract String serviceGetTaxonInfoUrl(String codiSpec);
	
	public abstract int serviceGetTaxonCitations(String codiOrc);


	
	public void setLocation(double latitude, double longitude, boolean utm1x1){
		
		this.latitude=latitude;
		this.longitude=longitude;
		
	}

	
	public String getDbName() {
		
		return dbName;
	}
	
	public String getPrettyLocation(){
		
		return location;
		
	}
	
	public RemoteTaxonSet getList(){
	    	
		return projList;
	    	
	}
	 
	public RemoteCitationSet getCitList() {
	
		return citList;
		
	}

	public boolean hasUTM1x1() {

		return false;
	}

	public boolean useThId() {

		return true;
		
	}



}
