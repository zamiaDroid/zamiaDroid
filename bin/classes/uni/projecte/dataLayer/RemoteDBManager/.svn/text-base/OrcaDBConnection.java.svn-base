package uni.projecte.dataLayer.RemoteDBManager;

import uni.projecte.R;
import android.content.Context;

public class OrcaDBConnection extends BVeganaDBConnection {

	
	protected static String serviceTaxonList="http://biodiver.bio.ub.es/biocat/servlet/biocat.ZamiaLlistaDeTaxonsUTM?";
	

	public OrcaDBConnection(Context baseContext, String filum, String language) {
		
		
		
		super(baseContext, filum, language);
		dbName=baseContext.getString(R.string.dBNameOrca);
		filumLetter="f";

		
		
	}
	
	
	@Override
	protected String getServiceTaxonList() {
		
		return serviceTaxonList;
		
	}


	@Override
	public String serviceGetTaxonInfoUrl(String codiSpec) {

		
		
		return null;
	}


	@Override
	protected String getServiceTaxonListURL() {

		return serviceTaxonList+filumLetter+"2.4@"+utm;
		
	}


	

}
