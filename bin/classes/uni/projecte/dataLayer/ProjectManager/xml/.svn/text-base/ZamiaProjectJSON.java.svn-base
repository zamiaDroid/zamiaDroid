package uni.projecte.dataLayer.ProjectManager.xml;

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

import uni.projecte.dataTypes.ProjectRepositoryType;

import android.util.Log;

public class ZamiaProjectJSON {
	
	ArrayList<ProjectRepositoryType> projList;
	
		public ZamiaProjectJSON(){
			
            projList=new ArrayList<ProjectRepositoryType>();

			
		}


	    private static String convertStreamToString(InputStream is) {
	        /*
	         * To convert the InputStream to String we use the BufferedReader.readLine()
	         * method. We iterate until the BufferedReader return null which means
	         * there's no more data to read. Each line will appended to a StringBuilder
	         * and returned as String.
	         */
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
	    
	    public ArrayList<ProjectRepositoryType> getList(){
	    	
	    	return projList;
	    	
	    }
	 
	    /* This is a test function which will connects to a given
	     * rest service and prints it's response to Android Log with
	     * labels "Praeda".
	     */
	    
	    public void connect(String url)
	    {
	 
	        HttpClient httpclient = new DefaultHttpClient();
	 
	        // Prepare a request object
	        HttpGet httpget = new HttpGet(url); 
	 
	        // Execute the request
	        HttpResponse response;
	        try {
	            response = httpclient.execute(httpget);
	            // Examine the response status
	            Log.i("ZP",response.getStatusLine().toString());
	 
	            HttpEntity entity = response.getEntity();
	 
	            if (entity != null) {
	 
	                // A Simple JSON Response Read
	                InputStream instream = entity.getContent();
	                String result= convertStreamToString(instream);
	 
	                Log.i("JSON",result);
	                
	                // A Simple JSONObject Creation
	                JSONObject json=new JSONObject(result);
	 
	                // A Simple JSONObject Parsing
	                
	                JSONArray jsonProj=json.getJSONArray("projects");
	                              
	                
	              for(int i=0;i<jsonProj.length();i++){
	            	  
	            	  JSONObject proj=jsonProj.getJSONObject(i);
	            	  
	            	 // ["projType","projId","projectName","projDescription","thName"]
	            	  JSONArray nameArray=proj.names();
	            	  JSONArray valArray=proj.toJSONArray(nameArray);
	            	  
	                    //projId, projName, projType, projDesc, projTh
	                    projList.add(new ProjectRepositoryType(nameArray,valArray));
	                    
	                } 
	 
	                // Closing the input stream will trigger connection release
	                instream.close();
	            }
	 
	 
	        } catch (ClientProtocolException e) {
	        	
	        	
	            e.printStackTrace();
	        } catch (IOException e) {
	            e.printStackTrace();
	        } catch (JSONException e) {
	            e.printStackTrace();
	        }
	    }


	 
	}
