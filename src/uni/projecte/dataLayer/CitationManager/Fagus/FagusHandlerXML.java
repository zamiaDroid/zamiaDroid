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


public class FagusHandlerXML extends DefaultHandler{

     // ===========================================================
     // Fields
     // ===========================================================
     
     private String tempVal;
	 private FagusReader fReader;
	 
	 private String name;
	 private String label;
	 private String category;
	 
	 private String sureness;
	 
	 private String origin;
	 
	 public FagusHandlerXML(FagusReader fReader) {
		super();
		
		this.fReader=fReader;
		
	}

	private ParsedDataSet myParsedExampleDataSet = new ParsedDataSet();
	//private boolean in_innertag;
	//private boolean in_outertag;
	
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
          // Nothing to do
     }

     /** Gets be called on opening tags like:
      * <tag>
      * Can provide attribute(s), when xml was like:
      * <tag attribute="attributeValue">*/
     @Override
     public void startElement(String namespaceURI, String localName,
               String qName, Attributes atts) throws SAXException {
          
    	 
    	 if (localName.equals("OrganismCitationList")) {
      
    		 
    		 
          }else if (localName.equals("OrganismCitation")) {
        	  
        	  origin=atts.getValue("origin");
              fReader.createNewSample(origin);
        	  
                           
          }else if (localName.equals("Datum")) {
        	  
               name=atts.getValue("name");
               label=atts.getValue("label");


               
          }else if (localName.equals("SideData")) {
        	  
              category=atts.getValue("type");

              
         }else if (localName.equals("value")) {

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
          	
        	  fReader.createObservationDate(atts.getValue("day"), atts.getValue("month"), atts.getValue("year"), atts.getValue("hours"), atts.getValue("mins"), atts.getValue("secs"));

        	  
          }
          else if (localName.equals("CorrectedTaxonName")) {
        	
        	  sureness=atts.getValue("sureness");
        	  	//ho agafem a l'end
        	  
            }
          
          else if (localName.equals("OriginalTaxonName")) {

        	  sureness=atts.getValue("sureness");
        	  //ho agafem a l'end
          
          
          }
          
          else{
        	  
        	  ///no fem res...
        	  
        	  
          }
     }
     
     /** Gets be called on closing tags like:
      * </tag> */
     @Override
     public void endElement(String namespaceURI, String localName, String qName)
               throws SAXException {
          
    	 if (localName.equals("OrganismCitationList")) {
    
    		 fReader.finishReader();

          }else if (localName.equals("OrganismCitation")) {
        	  
    		  
    		  
   			   
          }else if (localName.equals("Datum")) {
        	  
    		  
    		  
   			   
          }else if (localName.equals("SideData")) {
             
        	  
        	  
        	  
        	  
          }else if (localName.equals("value")) {
               
        	  fReader.createDatumFields(tempVal,name.replace("\"", ""),label.replace("\"", ""),category);
        	  tempVal="";

          }
          else if (localName.equals("CitationCoordinate")) {
          	
              
              // startElem does hard work


        	  
          }
          else if (localName.equals("SecondaryCitationCoordinate")) {
          	
              // startElem does hard work
        	  
        	  
          }
          else if (localName.equals("InformatisationDate")) {
          	
              // startElem does hard work


        	  
          }
          else if (localName.equals("ObservationDate")) {
          	
              
              // startElem does hard work

        	  
          }
          else if (localName.equals("CorrectedTaxonName")) {
        	
        
        	  fReader.createCorrectedTaxonName(tempVal, sureness);

        	  
            }
          
          else if (localName.equals("OriginalTaxonName")) {

        	  fReader.createOriginalTaxonName(tempVal,sureness);
          
          
          }
    	 
          else if (localName.equals("CollectionData")) {

     
          
          
          }
          else{

        	  
        	  fReader.createDefaultFields(localName, tempVal);
        	  
        	  
          }
     }
     
     /** Gets be called on the following structure:
      * <tag>characters</tag> */
     @Override
    public void characters(char ch[], int start, int length) {
          
  		 tempVal = new String(ch,start,length);
  		 if(tempVal.contains("\t")) tempVal="";

    }
}