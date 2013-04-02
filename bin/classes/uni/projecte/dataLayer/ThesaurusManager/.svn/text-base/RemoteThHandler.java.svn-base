package uni.projecte.dataLayer.ThesaurusManager;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

public class RemoteThHandler {
	
	private static String url="http://biodiver.bio.ub.es/ZamiaProjectProvider/ThesaurusProvider?lang=";
	private ArrayList<RemoteThesaurus> remoteThPool;
		
	
	public String getThUrl(String poolId, String thId,String lang){
		
		String thUrl="";
		
		String result=connect(url+lang+"&poolId="+poolId+"&thId="+thId);
		
	     JSONObject json;
	     
			try {
				
				json = new JSONObject(result);
				
		    	JSONArray nameArray=json.names();
		    	JSONArray valArray=json.toJSONArray(nameArray);
		
				for(int i=0; i<nameArray.length(); i++){
					
					String name=nameArray.getString(i);
					
					if(name.equals("url")){
						
						thUrl=valArray.getString(i);
						
					}
				
				} 
		       		    	  
	    	} 
	        catch (JSONException e) {

	        }
	        
	        return thUrl;  
		
		
	}
	
	
	public ArrayList<RemoteThesaurus> getThPoolList(String poolId,String lang){
		
		remoteThPool=new ArrayList<RemoteThesaurus>();
		
		String result=connect(url+lang+"&poolId="+poolId);
		
        JSONObject json;
		try {
			json = new JSONObject(result);
	
	        JSONArray jsonProj=json.getJSONArray("thList");
	        
	        for(int i=0;i<jsonProj.length();i++){
	    	  
	    	  JSONObject proj=jsonProj.getJSONObject(i);
	    	  
	    	  JSONArray nameArray=proj.names();
	    	  JSONArray valArray=proj.toJSONArray(nameArray);
	
	    	  RemoteThesaurus rmTh=getJSONValues(nameArray, valArray);
	    	  remoteThPool.add(rmTh);
	    	  
	        }
	    	  
    	} 
        catch (JSONException e) {

        }
        
        return remoteThPool;       
		
		
	}
	
	
	public RemoteThesaurus getJSONValues(JSONArray nameArray,JSONArray valArray) {
		
		String thId="";
		String thSource="";
		String thDesc="";
		String lastUpdate="";
		String url="";
		
		
		try {
					
			for(int i=0; i<nameArray.length(); i++){
						
				String name=nameArray.getString(i);
				
				if(name.equals("id")){
					
					thId=valArray.getString(i);
					
				}
				else if(name.equals("source")){
					
					thSource=valArray.getString(i);
					
				}
				else if(name.equals("description")){
				
					thDesc=valArray.getString(i);
				
				}
				else if(name.equals("last_update")){
				
					lastUpdate=valArray.getString(i);
				
				}
				else if(name.equals("url")){
			
					url=valArray.getString(i);
							
				}
			
			} 
		
			} catch (JSONException e) {


			}
			
			return new RemoteThesaurus(thId, thSource, thDesc, lastUpdate,url);
			
		}

	
	
    private String connect(String url){
 
    	Log.i("Thesaurus","Connecting TH server: "+url);
    	
        HttpClient httpclient = new DefaultHttpClient();
        HttpGet httpget = new HttpGet(url); 
 
        HttpResponse response;
 
        String result="";
        
        try {
            response = httpclient.execute(httpget);
            // Examine the response status
            Log.i("ZP",response.getStatusLine().toString());
 
            // Get hold of the response entity
            HttpEntity entity = response.getEntity();
            // If the response does not enclose an entity, there is no need
            // to worry about connection release
 
            if (entity != null) {
 
                // A Simple JSON Response Read
                InputStream instream = entity.getContent();
                result= convertStreamToString(instream);
          
                instream.close();
                
                
            }
 
 
        } catch (ClientProtocolException e) {
        	
        	Log.i("Thesaurus","Connecting TH server | Error: "+e.getMessage());

            e.printStackTrace();
            
        } catch (IOException e) {
        	
        	Log.i("Thesaurus","Connecting TH server | Error: "+e.getMessage());
        	e.printStackTrace();
        	
        } 
        
        return result;
        
    }
    
    
    private static String convertStreamToString(InputStream is) {

    	
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();
 
        String line = null;
        try {
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        
        return sb.toString();
        
    }
	
	
	

}
