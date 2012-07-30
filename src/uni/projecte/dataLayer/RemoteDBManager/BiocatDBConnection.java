package uni.projecte.dataLayer.RemoteDBManager;

import uni.projecte.R;
import uni.projecte.dataTypes.RemoteCitationSet;
import uni.projecte.dataTypes.Utilities;
import android.content.Context;
import android.util.Log;

public class BiocatDBConnection extends BVeganaDBConnection {
	

	protected static String serviceTaxonList="http://biodiver.bio.ub.es/biocat/servlet/biocat.ZamiaLlistaDeTaxonsUTM?";
	//private static String serviceTaxonInfo="";
	
	
	public BiocatDBConnection(Context baseContext,String filum,String language) {
		
		super(baseContext,filum,language);

		dbName=baseContext.getString(R.string.dbNameBiocat);

		
	}
	
	@Override
	protected String getServiceTaxonListURL() {

		return serviceTaxonList+filumLetter+"2.1@"+utm;

	}
		

	
	@Override
	public int serviceGetTaxonCitations(String codiOrc){
		
		String url=serviceTaxonList+filumLetter+"6.b@"+utm+"%25codi_e_orc%3D"+codiOrc;
		
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
		

	   		 if(filum.toLowerCase().equals("l")){
	   			 
	   			 url="http://biodiver.bio.ub.es/biocat/servlet/biocat.FitxaLiquensServlet?"+filumLetter.toLowerCase()+"4.%nomestab=1%idioma="+langBiocat+"%mobile=1%taxon=%40taxon%40%25codi_e_orc%3D"+codiOrc+"%25nivell%3DSP";

	   		 }
	   		 else  if(filum.toLowerCase().equals("m")){
	   			 
	   			 url="http://biodiver.bio.ub.es/biocat/servlet/biocat.FitxaFongsServlet?"+filumLetter.toLowerCase()+"4.%nomestab=1%idioma="+langBiocat+"%mobile=1%taxon=%40taxon%40%25codi_e_orc%3D"+codiOrc+"%25nivell%3DSP";

	   		 }
	   		 else if(filum.toLowerCase().equals("t")){
	   			 
				   url="http://biodiver.bio.ub.es/biocat/servlet/biocat.FitxaVertebratsServlet?"+filumLetter.toLowerCase()+"4.%nomestab=1%idioma="+langBiocat+"%mobile=1%taxon=%40taxon%40%25codi_e_orc%3D"+codiOrc+"%25nivell%3DSP";

	   		 }
	   		 else{
				    
				   url="http://biodiver.bio.ub.es/biocat/servlet/biocat.SSBPBTServlet?"+filumLetter.toLowerCase()+"4.05%nomestab=1%idioma="+langBiocat+"%mobile=1%taxon=%40taxon%40%25codi_e_orc%3D"+codiOrc+"%25nivell%3DSP";
	   			 
	   		 }
			 
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
