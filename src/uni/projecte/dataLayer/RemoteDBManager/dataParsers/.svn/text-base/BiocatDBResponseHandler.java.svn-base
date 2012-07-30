package uni.projecte.dataLayer.RemoteDBManager.dataParsers;

import java.io.IOException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import uni.projecte.dataTypes.RemoteCitationSet;
import uni.projecte.dataTypes.RemoteTaxonSet;

import android.util.Log;

public class BiocatDBResponseHandler {
	

	 public int loadTaxons(String url,RemoteTaxonSet projList)
	    {
	 
	        HttpClient httpclient = new DefaultHttpClient();
	        
	        System.out.println(url);
	 
	        HttpGet httpget = new HttpGet(url); 
	 
	        // Execute the request
	        HttpResponse response;
	        try {
	            response = httpclient.execute(httpget);

	            Log.i("ZP",response.getStatusLine().toString());
	 
	            // Get hold of the response entity
	            HttpEntity entity = response.getEntity();
	            
	            
	            if (entity != null) {
	               
	                String result = EntityUtils.toString(entity, HTTP.UTF_8);
	                result=result.replace("<html>", "");
	 
	                // A Simple JSONObject Creation
	                JSONObject json=new JSONObject(result);
	                
	                JSONArray jsonProj=json.getJSONArray("llistat");
	                
	              for(int i=0;i<jsonProj.length();i++){
	            	  
	            	  JSONObject proj=jsonProj.getJSONObject(i);
	            	  
	            	 // ["projType","projId","projectName","projDescription","thName"]
	            	  JSONArray nameArray=proj.names();
	            	  JSONArray valArray=proj.toJSONArray(nameArray);
	     
	            	  projList.addTaxon(valArray.getString(0),valArray.getString(1));
	                  
	                    
	                } 
	 
	            }
	            
	            return projList.numElements();
	 
	 
	        } catch (ClientProtocolException e) {
	       	
	            e.printStackTrace();
	            
	            return -1;
	            
	            
	        } catch (IOException e) {
	        	
	            e.printStackTrace();
	            
	            return -2;
	            
	            
	        } catch (JSONException e) {
	        	
	            e.printStackTrace();
	            
	            return -3;
	        }
	    }
	
	
	
	 public int loadCitations(String url,RemoteCitationSet citList)
	    {
	 
	        HttpClient httpclient = new DefaultHttpClient();
	        
	        System.out.println(url);
	 
	        HttpGet httpget = new HttpGet(url); 
	 
	        // Execute the request
	        HttpResponse response;
	        try {
	            response = httpclient.execute(httpget);

	            Log.i("ZP",response.getStatusLine().toString());
	 
	            // Get hold of the response entity
	            HttpEntity entity = response.getEntity();
	            
	            
	            if (entity != null) {
	               
	                String result = EntityUtils.toString(entity, HTTP.UTF_8);
	                result=result.replace("<html>", "");
	 
	                // A Simple JSONObject Creation
	                JSONObject json=new JSONObject(result);
	                
	                JSONArray jsonProj=json.getJSONArray("llistat");
	                
	              for(int i=0;i<jsonProj.length();i++){
	            	  
	            	  JSONObject proj=jsonProj.getJSONObject(i);
	            	  
	            	 // ["projType","projId","projectName","projDescription","thName"]
	            	  JSONArray nameArray=proj.names();
	            	  JSONArray valArray=proj.toJSONArray(nameArray);
	     
	            	  citList.addCitation(valArray.getString(0),valArray.getString(1));
	                  
	                    
	                } 
	              
	 
	            }
	 
	              return citList.numElements();

	 
	        } catch (ClientProtocolException e) {
	       	
	            e.printStackTrace();
	            
	            return -1;
	            
	        } catch (IOException e) {
	        	
	            e.printStackTrace();
	            return -2;
	            
	        } catch (JSONException e) {
	        	
	            e.printStackTrace();
	            return -1;

	        }
	     
	    }


}
