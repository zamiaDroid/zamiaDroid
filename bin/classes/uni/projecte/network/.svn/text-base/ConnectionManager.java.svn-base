package uni.projecte.network;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URLEncoder;
import java.util.ArrayList;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;

import android.content.Context;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

public class ConnectionManager {
	
	Context baseContext;

	private ArrayList <NameValuePair> params;
	private ArrayList <NameValuePair> headers;
	
	
    private int responseCode;
    private String message;
 
    private String response;
    
    private String url="";

	private ResponseMethod responseMethod;

	private static Handler h;
	
	
	public enum RequestMethod
	{
		GET,
		POST
	}
	
	public enum ResponseMethod
	{
		FILE,
		STRING
	}
	

	public void downLoadFile(String url, ArrayList<NameValuePair> params,ArrayList<NameValuePair> headers, ResponseMethod responseMethod, Handler mHandler) {
		
		this.url=url;
        this.params = params;
        this.headers = headers;
        this.responseMethod= responseMethod;
        ConnectionManager.h=mHandler;
        
        try {
			Execute(RequestMethod.POST);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	

	
	public ConnectionManager(String url){

		this.url=url;
        params = new ArrayList<NameValuePair>();
        headers = new ArrayList<NameValuePair>();

	}
	
	public ConnectionManager() {

		

	}



	public String getResponse() {
	        return response;
	}
	
    public String getErrorMessage() {
        return message;
    }
 
    public int getResponseCode() {
        return responseCode;
    }
	
	  
	public void AddParam(String name, String value){
		
	        params.add(new BasicNameValuePair(name, value));
	        
	}
	 
	 public void AddHeader(String name, String value){
	    	
	        headers.add(new BasicNameValuePair(name, value));
	  }

	
	 public void Execute(RequestMethod method) throws Exception
	    {
	        switch(method) {
	            case GET:
	            {
	                //add parameters
	                String combinedParams = "";
	                if(!params.isEmpty()){
	                    combinedParams += "?";
	                    for(NameValuePair p : params)
	                    {
	                        String paramString = p.getName() + "=" + URLEncoder.encode(p.getValue(),"UTF-8");
	                        if(combinedParams.length() > 1)
	                        {
	                            combinedParams  +=  "&" + paramString;
	                        }
	                        else
	                        {
	                            combinedParams += paramString;
	                        }
	                    }
	                }
	 
	                HttpGet request = new HttpGet(url + combinedParams);
	 
	                //add headers
	                for(NameValuePair h : headers)
	                {
	                    request.addHeader(h.getName(), h.getValue());
	                }
	 
	                executeRequest(request, url);
	                break;
	            }
	            case POST:
	            {
	                HttpPost request = new HttpPost(url);
	 
	                //add headers
	                for(NameValuePair h : headers)
	                {
	                    request.addHeader(h.getName(), h.getValue());
	                }
	 
	                if(!params.isEmpty()){
	                    request.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));
	                }
	 
	                executeRequest(request, url);
	                break;
	            }
	        }
	    }
	 
	    private void executeRequest(HttpUriRequest request, String url)
	    {
	        HttpClient client = new DefaultHttpClient();
	 
	        HttpResponse httpResponse;
	 
	        try {
	            httpResponse = client.execute(request);
	            responseCode = httpResponse.getStatusLine().getStatusCode();
	            message = httpResponse.getStatusLine().getReasonPhrase();
	 
	            HttpEntity entity = httpResponse.getEntity();
	 
	            if (entity != null) {
	 
	                InputStream instream = entity.getContent();
	                
	                switch(responseMethod) {
			            case FILE:{
			            	
			                convertStreamToFile(instream, 132);

			            }
			            case STRING: {
			            	
			                response = convertStreamToString(instream);
 	
			            }
		            }

	 
	                // Closing the input stream will trigger connection release
	                instream.close();
	            }
	 
	        } catch (ClientProtocolException e)  {
	            client.getConnectionManager().shutdown();
	            
	            Log.e("CM","Error Client Protocol: "+e.getMessage());
	            
	            e.printStackTrace();
	        } catch (IOException e) {
	            client.getConnectionManager().shutdown();
	            
	            Log.e("CM","Error IO: "+e.getMessage());

	            e.printStackTrace();
	        }
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
	    
	    private static void convertStreamToFile(InputStream inputStream, long totalSize){
	    	
	    	
	        File SDCardRoot = Environment.getExternalStorageDirectory();
	        //create a new file, specifying the path, and the filename
	        //which we want to save the file as.
	        File file = new File(SDCardRoot,"zamiaDroid/Thesaurus/vertebrats.xml");

	        
	        
	        //this will be used to write the downloaded data into the file we created
	        FileOutputStream fileOutput;
			try {
				fileOutput = new FileOutputStream(file);
			

	        //this will be used in reading the data from the internet

	        //variable to store total downloaded bytes
	        int downloadedSize = 0;

	        //create a buffer...
	        byte[] buffer = new byte[1024];
	        int bufferLength = 0; //used to store a temporary size of the buffer

	        //now, read through the input buffer and write the contents to the file
	     
				while ( (bufferLength = inputStream.read(buffer)) > 0 ) {
				        //add the data in the buffer to the file in the file output stream (the file on the sd card
				        fileOutput.write(buffer, 0, bufferLength);
				        //add up the size so we know how much is downloaded
				        downloadedSize += bufferLength;
				        //this is where you would do something to report the prgress, like this maybe
				        //updateProgress(downloadedSize, totalSize);
				        
		                
		                Message msg = h.obtainMessage();
		                msg.arg1 = downloadedSize;
		                h.sendMessage(msg);

				}
		
	        //close the output stream when done
	        fileOutput.close();
	        
			} catch (FileNotFoundException e) {
				
	            Log.e("CM","Error File Not Found: "+e.getMessage());

				e.printStackTrace();
			}
			
			catch (IOException e) {
				
	            Log.e("CM","Error IO File Creation: "+e.getMessage());

				e.printStackTrace();
			}
	    	
	    	
	    }

	

}
