package uni.projecte.dataLayer.ThesaurusManager.ThesaurusDownloader;

import java.util.ArrayList;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

public class ThDownloader {
	
	private ArrayList <NameValuePair> params;
	
	
	public ThDownloader(){
        
		params = new ArrayList<NameValuePair>();
        headers = new ArrayList<NameValuePair>();
		
	}
	
	
	public ArrayList<NameValuePair> getParams() {
		return params;
	}


	public ArrayList<NameValuePair> getHeaders() {
		return headers;
	}
	private ArrayList <NameValuePair> headers;
	
	
	protected int getPosition(String[] list,String element){
		
		boolean found=false;
		int position=-1;
		
		int n=list.length;
		
		for(int i=0;i<n&&!found;i++){
			
			if(list[i].equals(element)) {
				found=true;
				position=i;
				
			}
			
		}
		
		return position;

	}
	
	
	  public void addParam(String name, String value)
	    {
	        params.add(new BasicNameValuePair(name, value));
	    }
	    public void addHeader(String name, String value)
	    {
	        headers.add(new BasicNameValuePair(name, value));
	    }

}
