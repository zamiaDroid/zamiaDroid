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

package uni.projecte.dataLayer.ProjectManager.xml;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;

import uni.projecte.controler.ProjectControler;
import uni.projecte.dataTypes.ParsedDataSet;
import android.content.Context;

public class ProjectXMLparser {

	private ProjectControler projCnt;
	private boolean error=false;
	


	public ProjectXMLparser(ProjectControler rsC){
		
		this.projCnt=rsC;
		
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
    
         ProjectHandlerXML myExampleHandler = new ProjectHandlerXML(projCnt);
         xr.setContentHandler(myExampleHandler);
         
         
         if (internet){
     		  
       		URL urlR = new URL(url); 
       		  
       		 xr.parse(new InputSource(urlR.openStream())); 
       		  
       	  }
       	  
       	  else{
        	  
       		  projCnt.setOptional();
       		  
       		  BufferedReader fis = new BufferedReader(new InputStreamReader(new FileInputStream(url), "UTF-8"));

       	  	//  InputStream fis = c.getAssets().open("botanical_project.xml");
       	  	 xr.parse(new InputSource(fis));
       	  	 
       	  	 projCnt.setMandatory();
       	  	 
       	  	 if(projCnt.getCitationType().equals("Botanical")){
       	  		 
	       	   	 InputStream fise = c.getAssets().open("botanical.xml");
	    	  	 xr.parse(new InputSource(fise));
 
       	  	 }
       	  	 else if(projCnt.getCitationType().equals("Ornithological")){
       	  		 
       	  	   	 InputStream fise = c.getAssets().open("ornithological.xml");
	    	  	 xr.parse(new InputSource(fise));
       	  		 
       	  	 }
       	  	 
       	
       		  
       	  }
         
         
         
         /* Parse the xml-data from our URL. */

         /* Parsing has finished. */
         /* Our ExampleHandler now provides the parsed data to us. */
         ParsedDataSet parsedExampleDataSet = myExampleHandler.getParsedData();
         
         System.out.println(parsedExampleDataSet.toString());
         
         /* Set the result to be displayed in our GUI. */
         //tv.setText(parsedExampleDataSet.toString());
         
    } catch (Exception e) {
         /* Display any Error to the GUI. */

    	error=true;

    	
    }
	
	
	}
	
}
