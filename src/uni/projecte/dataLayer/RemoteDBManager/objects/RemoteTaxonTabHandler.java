package uni.projecte.dataLayer.RemoteDBManager.objects;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.util.Log;

public class RemoteTaxonTabHandler {
	
	private String url="http://biodiver.bio.ub.es/biocat/servlet/biocat.ProveidorFitxesBiologiques?{filum}{action}%25nomestab=1%25mobile=1%25stringfied_taxon=";
	private HashMap<String, String> tabProviderList;
	private RemoteProviderPair[] providers;
	private DataBasesInfo dbInfo;

	
	public RemoteTaxonTabHandler(Context baseContext,String filumLetter){
		
		url=url.replace("{filum}",filumLetter.toLowerCase());
		
		if(filumLetter.toLowerCase().equals("f")) url=url.replace("{action}", "4.05");
		else url=url.replace("{action}", "4.");
		
		dbInfo=new DataBasesInfo(baseContext);
				
	}
	
	public RemoteProviderPair[] getAvailableTaxonTabs(String stringfiedTaxon,String lang){
		
		tabProviderList=new HashMap<String, String>();
		String result=connect(url+stringfiedTaxon+"%25lang="+lang);
		
        JSONObject json;
		try {
			
			json = new JSONObject(result);
	        JSONArray jsonProj=json.getJSONArray("serviceList");
	        
	        providers=new RemoteProviderPair[jsonProj.length()];
	        	        
	        for(int i=0;i<jsonProj.length();i++){
	    	  
	    	  JSONObject proj=jsonProj.getJSONObject(i);
	    	  
	    	  JSONArray nameArray=proj.names();
	    	  JSONArray valArray=proj.toJSONArray(nameArray);
	
	    	  String provId=getJSONValues(nameArray, valArray);
	    	  providers[i]=new RemoteProviderPair(dbInfo.getDataBaseName(provId), provId);
	    	  
	        }
	    	  
    	} 
        catch (JSONException e) {

        	String lala=e.getMessage();
        	
        }
        
        return providers;       
		
	}
	
	public String getJSONValues(JSONArray nameArray,JSONArray valArray) {
		
		
		String service="";
		String url="";
	
		
		try {
					
			for(int i=0; i<nameArray.length(); i++){
						
				String name=nameArray.getString(i);
				
				if(name.equals("service")){
					
					service=valArray.getString(i);
					
				}
				else if(name.equals("url")){
					
					url=valArray.getString(i);
					
				}
							
			} 
		
			} catch (JSONException e) {


			}
			
			tabProviderList.put(service, url);
			
			return service;
			
		}

	
	private String connect(String url){
		 
    	Log.i("Thesaurus","Connecting TH server: "+url);
    	
        HttpClient httpclient = new DefaultHttpClient();
        HttpGet httpget = new HttpGet(url); 
 
        HttpResponse response;
 
        String result="";
        
        try {
            response = httpclient.execute(httpget);

            Log.i("ZP",response.getStatusLine().toString());
 
            HttpEntity entity = response.getEntity();
 
            if (entity != null) {
 
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

	public HashMap<String, String> getTabProviderList() {
		return tabProviderList;
	}

	public void setTabProviderList(HashMap<String, String> tabProviderList) {
		this.tabProviderList = tabProviderList;
	}

	

}
