package uni.projecte.dataLayer.RemoteDBManager;

import uni.projecte.R;
import uni.projecte.dataTypes.RemoteCitationSet;
import uni.projecte.dataTypes.Utilities;
import android.content.Context;
import android.util.Log;

public class FloraPyrenaeaDBConnection extends BVeganaDBConnection {
	

	protected static String serviceTaxonList="http://atlasflorapyrenaea.org/florapyrenaea/Zamia?";
	//private static String serviceTaxonInfo="";
	
	
	public FloraPyrenaeaDBConnection(Context baseContext,String filum,String language) {
		
		super(baseContext,filum,language);

		dbName=baseContext.getString(R.string.dbNameFloraPyrenaea);

		
	}
	
	@Override
	protected String getServiceTaxonListURL() {

		return serviceTaxonList+"action=taxon_utm&utm="+utm.replace("_", "");
		
	}
		

	
	@Override
	public int serviceGetTaxonCitations(String codiOrc){
		
		String url=serviceTaxonList+"action=taxon_bib&utm="+utm.replace("_", "")+"&codi_e_poc="+codiOrc.replace(" ","+");
		
		citList=new RemoteCitationSet(utm);

		Log.i("DB","Connectant: "+url);
		
		bioResp.loadCitations(url, citList);
		
		
		  
		/*  BiocatRemoteTaxonList rTL= new BiocatRemoteTaxonList(utm);
		  
	      if(level==2) {
	    	  
	    	  rTL.connect("http://biodiver.bio.ub.es/biocat/servlet/biocat.ZamiaLlistaDeTaxonsUTM?"+filum+"6.b@"+utm+"%25codi_e_orc%3D"+codiOrc,false);
	    	  
	      }
	      if(level==3){
	    	  
	    	  rTL.connect("http://www.sivim.info/sivi/ZamiaLlistaDeTaxonsUTM?"+filum+"6.b@"+utm+"%25codi_e_orc%3D"+codiOrc,false);
	    	  
	      }
	      else rTL.connect("http://biodiver.bio.ub.es/biocat/servlet/biocat.ZamiaLlistaDeTaxonsUTM?"+filum+"6.b@"+utm+"%25codi_e_orc%3D"+codiOrc,false);
	        
	       remoteList=rTL.getCitList();
	       
		  
		  */
		
		
		return citList.numElements();
		
	}
	
	@Override
	public String serviceGetTaxonInfoUrl(String codiOrc){
		
		String url="";
		String langBiocat=Utilities.translateLangBiocat(systemLanguage);
			   	
		url="http://http://atlasflorapyrenaea.org/florapyrenaea/Zamia?action=taxon_tab&taxon="+codiOrc.replace(" ","+");
	   			 
	   		 
	 	return url;
	 	
		
	}



	@Override
	protected String getServiceTaxonList() {
		
		return serviceTaxonList;
		
	}
	
	@Override
	public boolean hasUTM1x1() {

		return true;
	}





	
	

}
