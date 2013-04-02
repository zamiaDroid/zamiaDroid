package uni.projecte.dataLayer.ThesaurusManager.ThesaurusDownloader;


public class ThDownloaderSivim extends ThDownloader{
	
			
	private String baseURL="http://www.sivim.info/sivi/ExportaThesaurusServlet?";
	private String url=" ";
	private String params="12.0%25tesa=tax%25downloadthesa=true%25format=XML%25sinonims=true";

	
	
	public String getURL(String filum){
		
	
		if(filum.contains(":")){
			
			String[] subRegne=filum.split(":");
			
			
			url=baseURL+subRegne[0]+params+"%subgrup="+subRegne[1];

			
		}
		else{
			
			url=baseURL+filum+params;

			
		}
		

	return url;
	
	}


}
