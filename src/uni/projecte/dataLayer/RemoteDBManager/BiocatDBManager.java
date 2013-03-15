package uni.projecte.dataLayer.RemoteDBManager;

import android.content.Context;

public class BiocatDBManager extends DBManager {

	public BiocatDBManager(Context baseContext) {
		super(baseContext,true);

	}
	
	public String getBiologicalCardUrl(String filum, String taxon){
		
		String url;
		
		if(filum.contains(":")) filum=filum.split(":")[0];
		
		taxon=taxon.replace(" ","+");
		
		
		 if(filum.toLowerCase().equals("l")){
   			 
   			 //url="http://biodiver.bio.ub.es/biocat/servlet/biocat.FitxaLiquensServlet?"+filum.toLowerCase()+"4.%nomestab=1%mobile=1%taxon=%40taxon%40%25codi_e_orc%3D"+codiOrc+"%25nivell%3DSP";
   			 url="http://biodiver.bio.ub.es/biocat/servlet/biocat.FitxaLiquensServlet?"+filum.toLowerCase()+"4.%nomestab=1%mobile=1%25stringfied_taxon="+taxon;

   		 }
		 else  if(filum.toLowerCase().equals("m")){
   			 
   			// url="http://biodiver.bio.ub.es/biocat/servlet/biocat.FitxaFongsServlet?"+filum.toLowerCase()+"4.%nomestab=1%mobile=1%taxon=%40taxon%40%25codi_e_orc%3D"+codiOrc+"%25nivell%3DSP";
   			 url="http://biodiver.bio.ub.es/biocat/servlet/biocat.FitxaFongsServlet?"+filum.toLowerCase()+"4.%nomestab=1%mobile=1%25stringfied_taxon="+taxon;

   		 }
   		 else if(filum.toLowerCase().equals("t")){
   			 
			 //  url="http://biodiver.bio.ub.es/biocat/servlet/biocat.FitxaVertebratsServlet?"+filum.toLowerCase()+"4.%nomestab=1%mobile=1%taxon=%40taxon%40%25codi_e_orc%3D"+codiOrc+"%25nivell%3DSP";
			   url="http://biodiver.bio.ub.es/biocat/servlet/biocat.FitxaVertebratsServlet?"+filum.toLowerCase()+"4.%nomestab=1%mobile=1%25stringfied_taxon="+taxon;

   		 }
   		 else{
			    
			   //url="http://biodiver.bio.ub.es/biocat/servlet/biocat.SSBPBTServlet?"+filum.toLowerCase()+"4.05%nomestab=1%mobile=1%taxon=%40taxon%40%25codi_e_orc%3D"+codiOrc+"%25nivell%3DSP";
			   url="http://biodiver.bio.ub.es/biocat/servlet/biocat.SSBPBTServlet?"+filum.toLowerCase()+"4.05%nomestab=1%mobile=1%25stringfied_taxon="+taxon;

   		 }
		 
		 
		 
		return url;
		
	}
	
	

}
