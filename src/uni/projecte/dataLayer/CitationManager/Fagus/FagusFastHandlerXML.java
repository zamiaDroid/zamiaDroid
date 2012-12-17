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

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import uni.projecte.dataTypes.ParsedDataSet;


public class FagusFastHandlerXML extends DefaultHandler{

     // ===========================================================
     // Fields
     // ===========================================================
     
     private String tempVal;
	 
     private int citationCount=0;
	 
	 
	 public FagusFastHandlerXML() {
		super();
		
	}

     @Override
     public void startDocument() throws SAXException {
     
     }

     @Override
     public void endDocument() throws SAXException {


     }


     @Override
     public void startElement(String namespaceURI, String localName,
               String qName, Attributes atts) throws SAXException {
          
    	 
    	 if (localName.equals("OrganismCitationList")) {
      
    		 
    		 
          }else if (localName.equals("OrganismCitation")) {
        	  
        	  
                           
          }          
          else{
      	  
        	  
          }
     }


     @Override
     public void endElement(String namespaceURI, String localName, String qName)
               throws SAXException {
          
    	 if (localName.equals("OrganismCitationList")) {
    

          }else if (localName.equals("OrganismCitation")) {
        	  
        	  citationCount++;
   			   
          }
          else{

        	        	  
          }
     }
     

     @Override
    public void characters(char ch[], int start, int length) {
    	 
  		 tempVal = new String(ch,start,length);
  		 if(tempVal.contains("\t")) tempVal="";

    }


	public int getCitationCount() {

		return citationCount;
		
	}
}