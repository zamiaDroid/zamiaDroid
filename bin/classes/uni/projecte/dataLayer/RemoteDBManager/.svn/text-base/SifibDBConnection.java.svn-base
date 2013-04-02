package uni.projecte.dataLayer.RemoteDBManager;

import uni.projecte.R;
import uni.projecte.dataTypes.Utilities;
import android.content.Context;

public class SifibDBConnection extends BVeganaDBConnection {

	
	protected static String serviceTaxonList="http://www.sifib.es/sifib/servlet/biocat.ZamiaLlistaDeTaxonsUTM?";
	

	public SifibDBConnection(Context baseContext, String filum, String language) {
		
		
		
		super(baseContext, filum, language);
		dbName=baseContext.getString(R.string.dbNameSifib);
		filumLetter="f";
	
		
	}
	
	
	@Override
	protected String getServiceTaxonList() {
		
		return serviceTaxonList;
		
	}


	@Override
	public String serviceGetTaxonInfoUrl(String codiSpec) {

		String langBiocat=Utilities.translateLangBiocat(systemLanguage);
		
		String  url="http://www.sifib.es/sifib/servlet/biocat.SSBPBTServlet?"+filumLetter.toLowerCase()+"4.05%nomestab=1%idioma="+langBiocat+"%mobile=1%taxon=%40taxon%40%25codi_e_orc%3D"+codiSpec+"%25nivell%3DSP";

		return url;
	}


	@Override
	protected String getServiceTaxonListURL() {

		return serviceTaxonList+filumLetter+"2.1@"+utm;
		
	}
	
	@Override
	public boolean hasUTM1x1() {

		return true;
	}	


	

}
