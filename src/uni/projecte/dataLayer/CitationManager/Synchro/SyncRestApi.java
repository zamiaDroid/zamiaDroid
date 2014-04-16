package uni.projecte.dataLayer.CitationManager.Synchro;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import uni.projecte.controler.ProjectControler;
import uni.projecte.dataLayer.ProjectManager.objects.Project;
import uni.projecte.dataLayer.utils.StringUtils;

import android.content.Context;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;



public class SyncRestApi {

	private static String ALL_PROJECTS_API="http://161.116.68.40:8080/orcanew/api/projects/";
	private static String PROJECT_API="http://161.116.68.40:8080/orcanew/api/projects/%s/synchro/%s";
	private static String SYNCRO_CITATIONS_API="http://161.116.68.40:8080/orcanew/api/citations/%s/synchro/%s";
	private static String ALL_CITATIONS_API="http://161.116.68.40:8080/orcanew/api/citations/%s";

	private static String SEND_CITATIONS_API="http://161.116.68.40:8080/orcanew/api/citations/%s";
	
	private static String TAG="SynchroService";
	private String lastUpdate="";

	private ProjectControler projCnt;
	
	private Context baseContext;

	
	
	
	public SyncRestApi(Context baseContext){

		this.baseContext=baseContext;
		this.lastUpdate="2014-04-07";
		
		//projCnt= new ProjectControler(baseContext);
		//projCnt.loadProjectInfoById(projId);
		
	}

	public ArrayList<Project> getProjectList(){
		
		ArrayList<Project> projectList= new ArrayList<Project>();
		
		HttpClient httpClient = new DefaultHttpClient();
		 
		HttpGet del = new HttpGet(ALL_PROJECTS_API);
		 
		del.setHeader("content-type", "application/json");
		 
		try
		{
		        HttpResponse resp = httpClient.execute(del);
		        String respStr = EntityUtils.toString(resp.getEntity());
		 
		        JSONArray respJSON= new JSONArray(respStr);
		 
		        String[] clientes = new String[respJSON.length()];
		 
		        for(int i=0; i<respJSON.length(); i++)
		        {
		            JSONObject obj = respJSON.getJSONObject(i);
		 
		            String nombCli = obj.getString("project_name");
		            String unsyncro= obj.getString("mod_date");
		 
		            clientes[i] = nombCli;
		            
		            Log.i(TAG,clientes[i]);
		            
		            Project proj=new Project(0);
		            proj.setProjName(nombCli);
		            proj.setServer_last_mod(unsyncro);
		            
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
		 
		HttpGet del = new HttpGet(String.format(PROJECT_API,getProject(projectId),lastUpdate));
		 
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
			
			System.out.println(String.format(ALL_CITATIONS_API,getProject(projectTag)));
			
			del = new HttpGet(String.format(ALL_CITATIONS_API,getProject(projectTag)));

		}
		else{
			
			System.out.println(String.format(SYNCRO_CITATIONS_API,getProject(projectTag),lastUpdate.replace(" ","%20")));

			del = new HttpGet(String.format(SYNCRO_CITATIONS_API,getProject(projectTag),lastUpdate.replace(" ","%20")));

		}
		
		 
		del.setHeader("content-type", "application/json");
		 
		try
		{
		        HttpResponse resp = httpClient.execute(del);
		        String respStr = EntityUtils.toString(resp.getEntity());
		 
		        JSONArray respJSON= new JSONArray(respStr);
		 
		        String[] clientes = new String[respJSON.length()];
		 
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

	public void sendCitations(String projectTag,ZamiaCitation citation) {

		Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
		String result=gson.toJson(citation);
		
				
	    HttpClient httpclient = new DefaultHttpClient();
	    
		System.out.println(String.format(SEND_CITATIONS_API,getProject(projectTag)));
		System.out.println(result);

	    
		HttpPost httpPost = new HttpPost(String.format(SEND_CITATIONS_API,getProject(projectTag)));

		    try {
		        // Add your data
	
		    	 httpPost.setEntity(new StringEntity(result));
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
	
}
