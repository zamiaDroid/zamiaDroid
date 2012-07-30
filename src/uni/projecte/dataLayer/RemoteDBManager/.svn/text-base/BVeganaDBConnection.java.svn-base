package uni.projecte.dataLayer.RemoteDBManager;

import edu.ub.bio.biogeolib.CoordConverter;
import edu.ub.bio.biogeolib.CoordinateLatLon;
import edu.ub.bio.biogeolib.CoordinateUTM;
import uni.projecte.R;
import uni.projecte.dataLayer.RemoteDBManager.dataParsers.BiocatDBResponseHandler;
import uni.projecte.dataTypes.RemoteCitationSet;
import uni.projecte.dataTypes.RemoteTaxonSet;
import uni.projecte.dataTypes.Utilities;
import uni.projecte.maps.UTMDisplay;
import android.content.Context;

public abstract class BVeganaDBConnection extends AbstractDBConnection {
	

	protected BiocatDBResponseHandler bioResp;
	protected String utm;
	protected String utm1x1;
	
	protected String filumLetter;
	protected String serviceTaxonList;
	
	
	public BVeganaDBConnection(Context baseContext,String filum,String language) {
		
		super(baseContext,filum,language);
		
		bioResp=new BiocatDBResponseHandler();
		
		filumLetter=getFilumLetter(filum);
		


	}
	
	@Override
	public void setLocation(double latitude, double longitude, boolean utm1x1){

		super.setLocation(latitude, longitude, false);
		 
  		CoordinateUTM utmConverter = CoordConverter.getInstance().toUTM(new CoordinateLatLon(latitude,longitude));
  		
  		if(!utm1x1)utm=UTMDisplay.getBdbcUTM10x10(utmConverter.getShortForm());
  		else utm=UTMDisplay.get1x1UTM(utmConverter.getShortForm());
		
	}
	
	
	@Override
	public int serviceGetTaxonList(){
		
        projList=new RemoteTaxonSet(utm);
		
		String url=getServiceTaxonListURL();
		
		bioResp.loadTaxons(url, projList);
		
	    return projList.numElements();

	} 
	
	
	protected abstract String getServiceTaxonListURL();
	
	
	
	@Override
	public int serviceGetTaxonCitations(String codiOrc){
		
		String url=getServiceTaxonList()+filumLetter+"6.b@"+utm+"%25codi_e_orc%3D"+codiOrc;
		
		citList=new RemoteCitationSet(utm);

		int result=bioResp.loadCitations(url, citList);
		
		
		  
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
		
		
		return result;
		
	}
	

	
	@Override
	public String getPrettyLocation(){
		
		return utm.replace("_", "");
	
		
	}
	
	
	

	protected abstract String getServiceTaxonList();
	
	
	/*
	 * 
	 * Utilities
	 * 
	 */
	  
	private String getFilumLetter(String currentLanguageName){
		  
		  String[] biocatNamesEnglish = baseContext.getResources().getStringArray(R.array.thesaurusFilumsEnglish);
		  int pos=Utilities.findString(biocatNamesEnglish, currentLanguageName);
		   	
		  if(pos>-1) {
			  
			  String[] biocatFilumsLetters = baseContext.getResources().getStringArray(R.array.thesaurusFilumsLetters);
			  
			  return biocatFilumsLetters[pos];
			  
		  }
		  else return "";
		  
		  
	  }
	
	@Override
	public boolean useThId() {

		return false;
		
	}


}
