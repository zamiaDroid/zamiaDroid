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

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import android.util.Log;

import uni.projecte.dataTypes.ParsedDataSet;


public class ZamiaCitationHandlerXML extends DefaultHandler{

     // ===========================================================
     // Fields
     // ===========================================================
     
     private String tempVal;
	 private ZamiaCitationReader fReader;
	 
	 private String name;
	 private String label;
	 private String category;
	 
	 
	 private boolean repeatedCitation=false;
	 private boolean insideCitationField=false;
	 
	 private ParsedDataSet myParsedExampleDataSet = new ParsedDataSet();

	 
	 
	 public ZamiaCitationHandlerXML(ZamiaCitationReader fReader) {
		super();
		
		this.fReader=fReader;
		
	}

	public ParsedDataSet getParsedData() {
          return this.myParsedExampleDataSet;
     }

     // ===========================================================
     // Methods
     // ===========================================================
     @Override
     public void startDocument() throws SAXException {
          this.myParsedExampleDataSet = new ParsedDataSet();
     }

     @Override
     public void endDocument() throws SAXException {

     
     }

    
     @Override
	public void startElement(String namespaceURI, String localName,
               String qName, Attributes atts) throws SAXException {
          
    	 
    	 if (localName.equals("ZamiaCitationList")) {
      
    		 tempVal="";
    		 
    		 
          }else if (localName.equals("ZamiaCitation")) {
        	  
              fReader.createNewSample();
        	  
                           
          }else if (localName.equals("CitationField")) {
        	  
              category=atts.getValue("category");
              name=atts.getValue("name");
              label=atts.getValue("label");
              
              insideCitationField=true;

              
         }else if(localName.equals("value")){
        	 
       	  
         }else if(localName.equals("SecondaryCitationList")){
        	 
        	 fReader.setSecondLevelFields(true);
         	 fReader.setSecondLevelType("secondLevel");
       	 
         }
         else if (localName.equals("CitationCoordinate")) {
          	
        	  fReader.createCitationCoordinate(atts.getValue("code"));

          }
          else if (localName.equals("SecondaryCitationCoordinate")) {
          	
        	  fReader.createSecondaryCitationCoordinate(atts.getValue("code"));
        	  
          }
          else if (localName.equals("InformatisationDate")) {
  
        	  fReader.createInformatisationDate(atts.getValue("day"), atts.getValue("month"), atts.getValue("year"), atts.getValue("hours"), atts.getValue("mins"), atts.getValue("secs"));
        	  
          }
          else if (localName.equals("ObservationDate")) {
        	  
          }
          else if(localName.equals("Polygon")){
        	  
         	 fReader.setSecondLevelFields(true);
         	 fReader.setSecondLevelType("polygon");
        	  
          }
          else if(localName.equals("PhotoList")){
        	  
          	 fReader.setSecondLevelFields(true);
          	 fReader.setSecondLevelType("multiPhoto");
         	  
           }
          else if(localName.equals("PolygonPoint")){
        	  
          	 fReader.createNewSample();
         	  
           }
          else if(localName.equals("Photo")){
        	  
           	 fReader.createNewSample();
          	  
            }  
          else{
        	  
        	  ///no fem res...
        	  
          }
     }
     

     @Override
	public void endElement(String namespaceURI, String localName, String qName)
               throws SAXException {
          
    	 if (localName.equals("ZamiaCitationList")) {
    
    		 fReader.finishReader();

          }
    	 else if (localName.equals("ZamiaCitation")) {
        	  
    		  repeatedCitation=false;
   			   
          }
          else if(localName.equals("value")){
       	  
        	  Log.i("Zamia","Inside: "+insideCitationField+" Repeated: "+repeatedCitation);
        	  
        	  if(insideCitationField && !repeatedCitation) {
        		  
        		  fReader.createDatumFields(tempVal,name,label,category);    
        		  
        	  }   	  
        	  tempVal="";
        	  
          }
          else if (localName.equals("CitationField")) {
               
        	  insideCitationField=false;
        	  
        	  
          }
          else if(localName.equals("SecondaryCitationList")){
         	 
         	 fReader.setSecondLevelFields(false);       	 
         	 
          }
          else if (localName.equals("CitationCoordinate")) {
          	

        	  
          }
          else if (localName.equals("SecondaryCitationCoordinate")) {

        	  
          }
          else if (localName.equals("InformatisationDate")) {
          	
        	  
          }
          else if (localName.equals("ObservationDate")) {
          	
        	  tempVal=tempVal.trim();
        	  //cutreeeeeeeeeeeeeeeeee
              repeatedCitation=fReader.createObservationDate(tempVal);
              tempVal="";

          }
          else if(localName.equals("Polygon")){
        	  
          	 fReader.setSecondLevelFields(false);       	 
        	  
          }
          else if(localName.equals("PhotoList")){

           	 fReader.setSecondLevelFields(false);       	 
        	  
          }
          else{

        	  
          }
     }
     

    @Override
	public void characters(char ch[], int start, int length) {
          
        // myParsedExampleDataSet.setExtractedString(new String(ch, start, length));
       //	 tempVal = new String(ch,start,length);
       //	 if(tempVal.equals("      ") || tempVal.equals("\n")) tempVal="";
    	
    	
    	if(tempVal==null || tempVal.equals("\\") || tempVal.equals("\t") || tempVal.equals("\n")){
    		 
      		 tempVal = "";

    	 }
    	 
    	 else{
    		 
      		 tempVal = tempVal.concat(new String(ch,start,length));

    	 }
    	

    }
}