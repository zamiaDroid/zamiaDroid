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

package uni.projecte.dataLayer.CitationManager.Zamia;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.HashMap;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;

import uni.projecte.dataLayer.ProjectManager.objects.Project;
import uni.projecte.dataTypes.FieldsList;
import uni.projecte.dataTypes.ProjectField;
import android.content.Context;
import android.util.Log;



public class ZamiaCitationXMLparser {

	private ZamiaCitationReader fR;
	private boolean error=false;
	private static int WRONG_FORMAT=-1;
	

	public ZamiaCitationXMLparser(ZamiaCitationReader fR){
		
		this.fR=fR;
		
	}
	
	public ZamiaCitationXMLparser() {


	}

	public boolean isError() {
		return error;
	}
	
	
	
	public int preReadXML(Context c, String url,Project projObj, FieldsList fieldsList){
		
		
		try {

			
	         /* Get a SAXParser from the SAXPArserFactory. */
	         SAXParserFactory spf = SAXParserFactory.newInstance();
	         SAXParser sp = spf.newSAXParser();

	         XMLReader xr = sp.getXMLReader();
	    
	         	ZamiaCitationFastHandlerXML zcFastXMLHandler = new ZamiaCitationFastHandlerXML(projObj,fieldsList);
	         	xr.setContentHandler(zcFastXMLHandler);
	     		  
	         	 if (url.startsWith("http://")){
	        		  
	            		URL urlR = new URL(url); 
	         		 xr.parse(new InputSource(urlR.openStream())); 
	            		  
	            	  }
	            	  
	            	  else{
	             	  
	            		  BufferedReader fis = new BufferedReader(new InputStreamReader(new FileInputStream(url), "UTF-8"));
	            		  xr.parse(new InputSource(fis));
	            		  
	            	  }
	         	
	         
	 
	         return zcFastXMLHandler.getCitationCount();
	         
	    } 
		catch (Exception e) {

			Log.e("Info",e.getCause()+" : "+e.getMessage());
			
	    	return WRONG_FORMAT;
	    	
	    }
		
		
	}
	

	public void readXML(Context c, String url ,boolean internet){
		
	try {
         /* Create a URL we want to load some xml-data from. */
  	  
         /* Get a SAXParser from the SAXPArserFactory. */
         SAXParserFactory spf = SAXParserFactory.newInstance();
         SAXParser sp = spf.newSAXParser();

         /* Get the XMLReader of the SAXParser we created. */
         XMLReader xr = sp.getXMLReader();
         /* Create a new ContentHandler and apply it to the XML-Reader*/
    
         ZamiaCitationHandlerXML myExampleHandler = new ZamiaCitationHandlerXML(fR);
         xr.setContentHandler(myExampleHandler);
         
         
         if (internet){
     		  
       		URL urlR = new URL(url); 
    		 xr.parse(new InputSource(urlR.openStream())); 
       		  
       	  }
       	  
       	  else{
        	  
       		  BufferedReader fis = new BufferedReader(new InputStreamReader(new FileInputStream(url), "UTF-8"));
       		  xr.parse(new InputSource(fis));
       		  
       	  }
         
         
    } 
	catch (Exception e) {

		Log.e("Info",e.getCause()+" : "+e.getMessage());
		
    	error=true;
    	
    }
	
	
	}
	
}
