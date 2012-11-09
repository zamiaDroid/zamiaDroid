package uni.projecte.dataLayer.ThesaurusManager.ThesaurusDownloader;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;


public class ThDownloaderBiocat extends ThDownloader{
	
			
	private String baseURL="http://biodiver.bio.ub.es/biocat/servlet/biocat.ExportaThesaurusServlet?";
	private String url=" ";
	private String params="12.0%25tesa=tax%25downloadthesa=true%25format=XML%25sinonims=true";

	
	
	public String getURL(String filum){
		
	
		if(filum.contains(":")){
			
			String[] subRegne=filum.split(":");
			
			
			url=baseURL+subRegne[0]+params+"%subgrup="+subRegne[1];
			//url=baseURL+subRegne[0];

			
		}
		else{
			
	        String dataP="";
	         
	         
			try {
				
				dataP = "&" + URLEncoder.encode("downloadthesa", "UTF-8") + "=" + URLEncoder.encode("true", "UTF-8");
	            dataP += "&" + URLEncoder.encode("format", "UTF-8") + "=" + URLEncoder.encode("XML", "UTF-8");
	            dataP += "&" + URLEncoder.encode("sinonims", "UTF-8") + "=" + URLEncoder.encode("true", "UTF-8");
	            dataP += "&" + URLEncoder.encode("tesa", "UTF-8") + "=" + URLEncoder.encode("true", "UTF-8");
				
			} catch (UnsupportedEncodingException e) {

				e.printStackTrace();
			}

			url=baseURL+filum+params;

			//url=baseURL+filum+"12.0"+dataP;

			
		}
		

	return url;
	
	}


}
