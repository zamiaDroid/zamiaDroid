package uni.projecte.dataLayer.RemoteDBManager.dataParsers;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;

import uni.projecte.dataTypes.RemoteCitationSet;
import uni.projecte.dataTypes.RemoteTaxonSet;
import android.util.Log;



public class GBIFDBResponseHandler {
	
	private String nextURL="";
	public static int NEXT_URL=-2;
	
	 public int loadTaxons(String url,RemoteTaxonSet projList){
		 
			try {
	  	  
			   
		        /* Get a SAXParser from the SAXPArserFactory. */
		        SAXParserFactory spf = SAXParserFactory.newInstance();
				
				SAXParser sp = spf.newSAXParser();
				XMLReader xr = sp.getXMLReader();
				
				GBIFHandlerXML myExampleHandler = new GBIFHandlerXML(projList);
		        xr.setContentHandler(myExampleHandler);

		  
		        URL urlR = new URL(url); 
		        
				Log.i("BD","Gbif URL Taxons: "+urlR);

	
		        InputSource iS=new InputSource(urlR.openStream());
		       	xr.parse(iS);

	       	  	
		       	nextURL=myExampleHandler.getNextURL();
		       	
	       	  	if(!nextURL.equals("")){
	       	  		
	       	  		return NEXT_URL;
	       	  		
	       	  	}
	       	  	else return projList.numElements();
	       	  	
	  		  
			}
			catch (Exception e) {
			
				Log.i("BD","Gbif exception: "+e.getMessage());
				

				
			}
			return 0;

			 

	 }
	 
	 public static int copy(InputStream input, OutputStream output) throws IOException{
	     byte[] buffer = new byte[512];
	     int count = 0;
	     int n = 0;
	     while (-1 != (n = input.read(buffer))) {
	         output.write(buffer, 0, n);
	         count += n;
	     }
	     return count;
	 }


	public void setNextURL(String nextURL) {
		this.nextURL = nextURL;
	}

	public String getNextURL() {
		return nextURL;
	}

	public int loadCitations(String url, RemoteCitationSet citList) {

		try {
		  	  
			   
		   
        /* Get a SAXParser from the SAXPArserFactory. */
        SAXParserFactory spf = SAXParserFactory.newInstance();
		
		SAXParser sp = spf.newSAXParser();
		XMLReader xr = sp.getXMLReader();
		
		GBIFHandlerXML myExampleHandler = new GBIFHandlerXML(citList);
        xr.setContentHandler(myExampleHandler);

  
        URL urlR = new URL(url); 
        
		Log.i("BD","Gbif URL Citations: "+urlR);


        InputSource iS=new InputSource(urlR.openStream());
        
       // 	BufferedReader fis = new BufferedReader(new InputStreamReader(new FileInputStream(url.substring(1))));

      //  iS.setEncoding("ISO-8859-1");
          	 
       	xr.parse(iS);
   	  	
       	nextURL=myExampleHandler.getNextURL();
       	
   	  	if(!nextURL.equals("")){
   	  		
   	  		return NEXT_URL;
   	  		
   	  	}
   	  	else return citList.numElements();
   	  	
	        //URL urlR = new URL(url); 
	   	  	//xr.parse(new InputSource(urlR.openStream())); 
			  
		}
		catch (Exception e) {
		
			Log.i("BD","Gbif exception: "+e.getMessage());
			
		}
		
		return 0;
	}

}
