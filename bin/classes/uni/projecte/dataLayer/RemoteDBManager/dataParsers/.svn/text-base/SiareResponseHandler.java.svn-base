package uni.projecte.dataLayer.RemoteDBManager.dataParsers;

import java.io.IOException;
import java.io.StringReader;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import android.util.Log;

import uni.projecte.dataTypes.RemoteTaxonSet;

public class SiareResponseHandler {


	public int loadTaxons(String url, RemoteTaxonSet projList) {

		DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();	
		DocumentBuilder builder;
		
		try {
			
			
		builder = builderFactory.newDocumentBuilder();
		
	       HttpClient httpclient = new DefaultHttpClient();
	        System.out.println(url);
	 
	        HttpGet httpget = new HttpGet(url); 
	 
	        // Execute the request
	        HttpResponse response;
	    
	            response = httpclient.execute(httpget);

	            Log.i("ZP",response.getStatusLine().toString());
	 
	            // Get hold of the response entity
	            HttpEntity entity = response.getEntity();
	            
	            
	            if (entity != null) {
	               
	                String result = EntityUtils.toString(entity, HTTP.UTF_8);
	                
	                if(!result.equals("ERROR")){
	                
		    		    Document document = builder.parse(new InputSource(new StringReader(result)));
		    		    Element rootElement = document.getDocumentElement();	
		    		    
		    		    NodeList nodes = rootElement.getChildNodes();
	
		    		    for(int i=0; i<nodes.getLength(); i++){
		    		      Node node = nodes.item(i);
	
		    		      if(node instanceof Element){
		    		        //a child element to process
		    		        Element child = (Element) node;
		    		        
		    		        String taxon = child.getAttribute("nombre");
		    		        String code= child.getAttribute("codigo"); 
		    		        
		    		        projList.addTaxon(taxon, code);
	
		    		      }
		    		    }

		    			
	                }
	                else{
	                	
	                	return 0;
	                	
	                }
	    		    

	            }
	            
	        } 
	        catch (ClientProtocolException e) {
	       	
	        e.printStackTrace();
	        
	        return -1;
	        
	        
	        } 
	       catch (IOException e) {
	    	
	        e.printStackTrace();
	        
	        return -2;
	        
	        
	       } 
	  
	       catch (ParserConfigurationException e1) {
	    	   
	    	   return -3;
	    	   
	       }
	       catch (SAXException e) {
	    	   
				return -1;
			}
	       
	       
	       return projList.numElements();
		    

		
	}

}
