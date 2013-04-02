package uni.projecte.dataLayer.RemoteDBManager;

import uni.projecte.R;
import uni.projecte.dataTypes.Utilities;
import android.content.Context;

public class SivimDBConnection extends BVeganaDBConnection {

	
	protected static String serviceTaxonList="http://www.sivim.info/sivi/ZamiaLlistaDeTaxonsUTM?";


	public SivimDBConnection(Context baseContext, String filum, String language) {
		
		super(baseContext, filum, language);

		dbName=baseContext.getString(R.string.dbNameSivim);
		filumLetter="f";
		
	}
	

	@Override
	protected String getServiceTaxonListURL() {

		return serviceTaxonList+filumLetter+"2.1@"+utm;
		
	}
	
	
	@Override
	protected String getServiceTaxonList() {
		
		return serviceTaxonList;
		
	}


	@Override
	public String serviceGetTaxonInfoUrl(String codiOrc){
		
		String langBiocat=Utilities.translateLangBiocat(systemLanguage);
   
		String url="http://biodiver.bio.ub.es/biocat/servlet/biocat.SSBPBTServlet?"+filumLetter.toLowerCase()+"4.05%nomestab=1%idioma="+langBiocat+"%mobile=1%taxon=%40taxon%40%25codi_e_orc%3D"+codiOrc+"%25nivell%3DSP";
	   		
		return url;
	 	
		
	}

	@Override
	public boolean useThId() {

		return true;
		
	}
	

	

}
