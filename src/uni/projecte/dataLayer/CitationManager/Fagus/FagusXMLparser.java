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

package uni.projecte.dataLayer.CitationManager.Fagus;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.net.URL;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;

import uni.projecte.dataLayer.CitationManager.Zamia.ZamiaCitationFastHandlerXML;
import uni.projecte.dataLayer.ProjectManager.objects.Project;
import uni.projecte.dataTypes.FieldsList;
import uni.projecte.dataTypes.ParsedDataSet;
import android.content.Context;
import android.os.Handler;
import android.util.Log;

public class FagusXMLparser {

	private FagusReader fR;
	private boolean error=false;
	private static int WRONG_FORMAT=-1;
	private Handler handlerUpdateProcess;
	
	
	public FagusXMLparser(FagusReader fR, Handler handler){
		
		this.fR=fR;
		this.handlerUpdateProcess=handler;
		
	}
	
	public boolean isError() {
		return error;
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
    
         FagusHandlerXML myExampleHandler = new FagusHandlerXML(fR,handlerUpdateProcess);
         xr.setContentHandler(myExampleHandler);
         
         
         if (internet){
     		  
       		URL urlR = new URL(url); 
       		  
       		 xr.parse(new InputSource(urlR.openStream())); 
       		  
       	  }
       	  
       	  else{
        	  
       		 BufferedReader fis = new BufferedReader(new InputStreamReader(new FileInputStream(url), "UTF-8"));
       	  	 xr.parse(new InputSource(fis));
       	  	        		  
       	  }
        
        
         
    } catch (Exception e) {
         /* Display any Error to the GUI. */
      //   tv.setText("Error: " + e.getMessage());
        error=true;
    }
	
	
	}
	
	
	public int preReadXML(Context c, String url){
		
		
		try {

			
	         /* Get a SAXParser from the SAXPArserFactory. */
	         SAXParserFactory spf = SAXParserFactory.newInstance();
	         SAXParser sp = spf.newSAXParser();

	         XMLReader xr = sp.getXMLReader();
	    
	         	FagusFastHandlerXML fagusFastXMLHandler = new FagusFastHandlerXML();
	         	xr.setContentHandler(fagusFastXMLHandler);
	     		  
	      	  BufferedReader fis = new BufferedReader(new InputStreamReader(new FileInputStream(url), "UTF-8"));
       		  xr.parse(new InputSource(fis));
	 
	         return fagusFastXMLHandler.getCitationCount();
	         
	    } 
		catch (Exception e) {

			Log.e("Info",e.getCause()+" : "+e.getMessage());
			
	    	return WRONG_FORMAT;
	    	
	    }
		
		
	}
	
}
