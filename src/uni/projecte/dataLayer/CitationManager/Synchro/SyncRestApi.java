/*    	This file is part of ZamiaDroid.
*
*	ZamiaDroid is free software: you can redistribute it and/or modify
*	it under the terms of the GNU General Public License as published by
*	the Free Software Foundation, either version 3 of the License, or
*	(at your option) any later version.
*
*    	ZamiaDroid is distributed in the hope that it will be useful,
*    	but WITHOUT ANY WARRANTY; without even the implied warranty of
*    	MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
*    	GNU General Public License for more details.
*
*    	You should have received a copy of the GNU General Public License
*    	along with ZamiaDroid.  If not, see <http://www.gnu.org/licenses/>.
*/



package uni.projecte.dataLayer.CitationManager.Synchro;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import uni.projecte.dataLayer.ProjectManager.objects.Project;
import uni.projecte.dataLayer.utils.StringUtils;
import android.content.Context;
import android.util.Base64;
import android.util.Log;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;



public class SyncRestApi {

	private static String BASE_URL="http://biodiver.bio.ub.es/orcanew/api";
	//private static String BASE_URL="http://biodiver.bio.ub.es/orcanew/api";
	
	private static String ALL_PROJECTS_API="/projects/";
	
	private static String CHECK_USER="/users/";


	/*POST*/
	private static String CREATE_PROJECT_API="/projects/";
	
	private static String PROJECT_API="/projects/%s/synchro/%s";

	private static String ALL_CITATIONS_API="/citations/%s";
	
	/*POST*/
	private static String SEND_CITATIONS_API="/citations/%s";
	
	private static String SYNCRO_CITATIONS_API="/citations/%s/synchro/%s";
	
	private static String TAG="SynchroService";
	private String lastUpdate="";
	
	private Context baseContext;	
	private User user;
	
	public SyncRestApi(Context baseContext, User user){

		this.baseContext=baseContext;
		this.lastUpdate="2014-04-07";
		this.user=user;
		
		//projCnt= new ProjectControler(baseContext);
		//projCnt.loadProjectInfoById(projId);
		
	}
	
	private String getAuthorization (String usermane, String password) {
		   
		String source=usermane+":"+password;
		  
		return "Basic "+Base64.encodeToString(source.getBytes(),Base64.URL_SAFE|Base64.NO_WRAP);
		   
	}
	
	
	public ArrayList<Project> getProjectList(){
		
		ArrayList<Project> projectList= new ArrayList<Project>();
		
		HttpClient httpClient = new DefaultHttpClient();
		 
		HttpGet del = new HttpGet(BASE_URL+ALL_PROJECTS_API);
		 
		del.setHeader("content-type", "application/json");
		 
		try
		{
				System.out.println(getAuthorization(user.getUsername(),user.getPassword()));
			
				del.addHeader("Authorization", getAuthorization(user.getUsername(),user.getPassword()));
				
		        HttpResponse resp = httpClient.execute(del);
		        String respStr = EntityUtils.toString(resp.getEntity());
		 
		        JSONArray respJSON= new JSONArray(respStr);
		 
		        for(int i=0; i<respJSON.length(); i++){
		        	
		        	
		            JSONObject obj = respJSON.getJSONObject(i);
		 
		            String nombCli = obj.getString("project_name");
		            
		            String unsyncro= obj.getString("mod_date");
		            int count=obj.getInt("count_all");
		 
		            Log.i(TAG,"#"+i+" "+nombCli);
		            
		            Project proj=new Project(0);
		            proj.setProjName(nombCli);
		            proj.setServer_last_mod(unsyncro);
		            proj.setServer_unsyncro(count);
		            
		            projectList.add(proj);
		            
		        }
		 
			        	
		}
		catch(Exception ex)
		{
		        Log.e("ServicioRest","Error!", ex);
		}
		
		
		return projectList;
		
	}

	
	
	
	public void getProjectInfo(String projectId, TextView tvCount){
		
		HttpClient httpClient = new DefaultHttpClient();
		 
		HttpGet del = new HttpGet(String.format(BASE_URL+PROJECT_API,getProject(projectId),lastUpdate));
		del.addHeader("Authorization", getAuthorization(user.getUsername(),user.getPassword()));

		del.setHeader("content-type", "application/json");
		 
		try
		{
		        HttpResponse resp = httpClient.execute(del);
		        String respStr = EntityUtils.toString(resp.getEntity());
		 
		        JSONObject object= new JSONObject(respStr);
		        
		        tvCount.setText(object.getString("count_unsynchro"));
		        	
		}
		catch(Exception ex)
		{
		        Log.e("ServicioRest","Error!", ex);
		}
		
		
	}
	
	public ArrayList<ZamiaCitation> getAllCitations(String projectTag, String lastUpdate){
		
		ArrayList<ZamiaCitation> citationList=new ArrayList<ZamiaCitation>();
		
		HttpClient httpClient = new DefaultHttpClient();
		 
		HttpGet del=null;
		
		if(lastUpdate.equals("")){
			
			System.out.println(String.format(BASE_URL+ALL_CITATIONS_API,getProject(projectTag)));
			
			del = new HttpGet(String.format(BASE_URL+ALL_CITATIONS_API,getProject(projectTag)));

		}
		else{
			
			System.out.println(String.format(BASE_URL+SYNCRO_CITATIONS_API,getProject(projectTag),lastUpdate.replace(" ","%20")));

			del = new HttpGet(String.format(BASE_URL+SYNCRO_CITATIONS_API,getProject(projectTag),lastUpdate.replace(" ","%20")));

		}
		
		del.addHeader("Authorization", getAuthorization(user.getUsername(),user.getPassword()));
		del.setHeader("content-type", "application/json");
		 
		try
		{
		        HttpResponse resp = httpClient.execute(del);
		        String respStr = EntityUtils.toString(resp.getEntity());
		 
		        JSONArray respJSON= new JSONArray(respStr);
		 
		        for(int i=0; i<respJSON.length(); i++)
		        {
		            JSONObject jsonCitation = respJSON.getJSONObject(i);
		 
		            citationList.add(getCitation(jsonCitation));
		        }
		 		        	
		        	
		}
		catch(Exception ex)
		{
		        Log.e("ServicioRest","Error!", ex);
		}
		
		return citationList;
		
	}
	
	
	private ZamiaCitation getCitation(JSONObject citation){
		
		ZamiaCitation citationObj=new ZamiaCitation();
		
		try {
			
			citationObj.setId(citation.getString("id"));
			citationObj.setLatitude(citation.getDouble("latitude"));
			citationObj.setLongitude(citation.getDouble("longitude"));
			citationObj.setObservationDate(citation.getString("observationDate"));	
			citationObj.setOriginalTaxonName(citation.getString("originalTaxonName"));
			
			citationObj.setCitationNotes(citation.getString("citationNotes"));
			citationObj.setSureness(citation.getString("sureness"));
			citationObj.setPhenology(citation.getString("phenology"));
			citationObj.setNatureness(citation.getString("natureness"));
			
			citationObj.setAltitude(citation.getString("altitude"));
			citationObj.setObservationAuthor(citation.getString("observationAuthor"));
			
			citationObj.setLocality(citation.getString("locality"));
			
			citationObj.setState(citation.getString("state"));
			
			
			//System.out.println(citationObj.getTaxon());

			
		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return citationObj;

	}
	
	
	public String getProject(String projectId){
		
		return projectId.replace(" ","%20");
		
	}

	

	public void createRemoteProject(ZamiaProject project) {


		Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
		String result=gson.toJson(project);
		
		System.out.println(result);		
				
	    HttpClient httpclient = new DefaultHttpClient();
	    
		HttpPost httpPost = new HttpPost(BASE_URL+CREATE_PROJECT_API);

		    try {
		        // Add your data
	
		    	 httpPost.setEntity(new StringEntity(result));
		    	 httpPost.addHeader("Authorization", getAuthorization(user.getUsername(),user.getPassword()));

		    	 httpPost.setHeader("Accept", "application/json; charset=utf-8");
		         httpPost.setHeader("Content-type", "application/json; charset=utf-8");
	    	    
		        // Execute HTTP Post Request
		        HttpResponse response = httpclient.execute(httpPost);
			    HttpEntity httpEntity = response.getEntity();

			    if (httpEntity != null) {
			    	
			        InputStream is = httpEntity.getContent();
			        result = StringUtils.convertStreamToString(is);
			        Log.i(TAG, "Result: " + result);
			        
			    }
			    
		    } catch (ClientProtocolException e) {

		    } catch (IOException e) {

		    	
		    	
		 }
		
		
	}
	
	
	public void sendCitations(String projectTag,ZamiaCitation citation) {

		Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
		String result=gson.toJson(citation);
		
				
	    HttpClient httpclient = new DefaultHttpClient();
	    
		System.out.println(String.format(BASE_URL+SEND_CITATIONS_API,getProject(projectTag)));
		System.out.println(result);

	    
		HttpPost httpPost = new HttpPost(String.format(BASE_URL+SEND_CITATIONS_API,getProject(projectTag)));

		    try {
		        // Add your data
	
		    	 httpPost.setEntity(new StringEntity(result));
		    	 httpPost.addHeader("Authorization", getAuthorization(user.getUsername(),user.getPassword()));
		    	 httpPost.setHeader("Accept", "application/json; charset=utf-8");
		         httpPost.setHeader("Content-type", "application/json; charset=utf-8");
	    	    
		        // Execute HTTP Post Request
		        HttpResponse response = httpclient.execute(httpPost);
			    HttpEntity httpEntity = response.getEntity();

			    if (httpEntity != null) {
			    	
			        InputStream is = httpEntity.getContent();
			        result = StringUtils.convertStreamToString(is);
			        Log.i(TAG, "Result: " + result);
			        
			      
			        
			    }
			    
		    } catch (ClientProtocolException e) {

		    } catch (IOException e) {

		    	
		    }
	        
	}

	public boolean checkLogin(String username, String password) {

		boolean success=false;

		
		HttpClient httpClient = new DefaultHttpClient();
		 
		HttpGet del= new HttpGet(BASE_URL+CHECK_USER);
		
		del.addHeader("Authorization", getAuthorization(username,password));
		del.setHeader("content-type", "application/json");
		 
		try
		{
		        HttpResponse resp = httpClient.execute(del);
		        HttpEntity httpEntity =resp.getEntity();
		        
		        if (httpEntity != null) {
			    	
			        InputStream is = httpEntity.getContent();
			        String result = StringUtils.convertStreamToString(is);
			        Log.i(TAG, "Result: " + result);
			        
			        try{
			        	
			        	 JSONObject object= new JSONObject(result);
					     result=object.getString("user");
					     
					     if(result.equals(username)) success=true;
			        	
			        }
			        catch (JSONException e){
			        	
			        	
			        	
			        }
			        
			        
			    }
		 		        	
		        	
		}
		catch(Exception ex)
		{
		        Log.e(TAG,"Error!", ex);
		}
		
		return success;
	}

	
}
