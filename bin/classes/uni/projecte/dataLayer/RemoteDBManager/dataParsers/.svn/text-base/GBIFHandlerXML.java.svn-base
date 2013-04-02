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


package uni.projecte.dataLayer.RemoteDBManager.dataParsers;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import android.util.Log;

import uni.projecte.dataTypes.RemoteCitationSet;
import uni.projecte.dataTypes.RemoteTaxonSet;


public class GBIFHandlerXML extends DefaultHandler{

     // ===========================================================
     // Fields
     // ===========================================================
     
     private String tempVal="";
	 private String occurenceId;
	 
	 private String locality="";
	 private String country="";
	 private String collector="";
	 private String institutionCode="";
	 
	 private String origin="";
	 
	 private boolean insideDataResource=false;
	 
	 private String maxAltitude="";
	 private String minAltitude="";
	 	 
	 private RemoteTaxonSet projList;
	 private RemoteCitationSet citList;
	 
	 private boolean taxonExplorer=true;
	 
	 private int i=0;
	 
	 private String nextURL="";
	 
	 public GBIFHandlerXML(RemoteTaxonSet projList) {
		super();
		
		this.projList=projList;
		
	}


     public GBIFHandlerXML(RemoteCitationSet citList) {

    	 super();
    	 
    	 this.citList=citList;
    	 taxonExplorer=false;

     }


	// =========
     // Methods
    // =========
     @Override
     public void startDocument() throws SAXException {


     }

     @Override
     public void endDocument() throws SAXException {

     
     }

    
     @Override
	public void startElement(String namespaceURI, String localName,
               String qName, Attributes atts) throws SAXException {
          
    	 
    	 if(taxonExplorer){
    	 
	    	 if (localName.equals("TaxonOccurrence")) {
	    		 
	    		 occurenceId=atts.getValue("gbifKey");
		 
	         }
	    	 else if (localName.equals("taxon")) {
	        	  
	        	  tempVal="";
	         	  
	         }
	    	 else if (localName.equals("nameComplete")) {
	        	  
	        	  
	         }
	          else if(localName.equals("nextRequestUrl")){
	     		 
	     		 
	     		 
	     	 }
	          else{
	        	  
	        	  ///no fem res...
	        	  
	        	  
	          }
    	 
    	 }
    	 else{
    		 
    		 
        	 if (localName.equals("locality")) {
	    		 

        			
	    	 }
	    	 else if (localName.equals("country")) {

	                          
	         }
	    	 else if (localName.equals("dataResource")) {
	    		 
	    		 insideDataResource=true;
                 
	         }
	     	 else if (localName.equals("institutionCode")) {
		       	  

                 
	         }
	     	 else if (localName.equals("TaxonOccurrence")) {
		       	  
                 
	         }
    		 
    	 }
     }
     

     @Override
	public void endElement(String namespaceURI, String localName, String qName)
               throws SAXException {
          
      	 
    	 if(taxonExplorer){

    	 
	    	 if (localName.equals("TaxonOccurrence")) {
	    		 
	
	    	 }
	    	 else if (localName.equals("taxon")) {
	       	  
	       	  
	                          
	         }
	    	 else if (localName.equals("nameComplete")) {
	     
	    		tempVal=tempVal.replace("          \n           \n            \n             ","");
	    
	    		Log.i("DataBase","GBID: "+tempVal);
	       	  	projList.addTaxon(tempVal, tempVal);
	       	  	
	       	  	tempVal="";
	       	  	
	       	  	i++;
	       	  
	        }
	    	 else if(localName.equals("nextRequestUrl")){
	    		 
	    		 nextURL=tempVal;
	    		 tempVal="";
	    		 
	    	 }
	        else{
	       	 
	       	  ///no fem res...
	       	  	tempVal="";
	        	
	       	  
	         }
	    	 
    	 }
    	 else{
    		 
    		 
        	 if (localName.equals("locality")) {
	    		 
        		 locality=tempVal;
        		 tempVal="";
        			
	    	 }
	    	 else if (localName.equals("country")) {
	       	  
	    		 country=tempVal;
        		 tempVal="";
	                          
	         }
	    	 else if (localName.equals("dataResource")) {

	    		 insideDataResource=false;
	    		 tempVal="";
	    		 
	         }
	    	 else if (localName.equals("name")) {

                 origin=origin+tempVal+" ; ";

                 tempVal="";
	    		 
	         }
	    	 else if (localName.equals("collector")) {

                 origin=origin+tempVal+" ; ";

                 tempVal="";
	    		 
	         }        	 
	     	 else if (localName.equals("institutionCode")) {
		       	  
	     		 institutionCode=tempVal;
        		 tempVal="";
                 
	         }
	     	 else if(localName.equals("maximumElevationInMeters")){
	     		 
	     		 maxAltitude=tempVal;
        		 tempVal="";
     		 
	     		 
	     	 }
	     	 else if(localName.equals("minimumElevationInMeters")){
	     		 
	     		 minAltitude=tempVal;
        		 tempVal="";

		     		
	     	 }
	     	 else if (localName.equals("TaxonOccurrence")) {
	     		 
	     		 
		       	  
	     		 if(country.equals("")){
	     			
	     			 citList.addCitation(locality+" ; "+getAltitude(), institutionCode);
	     			 
	     		 }
	     		 else citList.addCitation(locality+" ; "+getAltitude()+" ; ("+country+")", origin+" [ "+institutionCode+" ]");
	     		 
		       	tempVal="";
		    	origin="";

	         }
    		 
    		 
    	 }
    	 
    	 
     }
     
     
     private String getAltitude(){
    	 
   		 String altitude="";
    		 
    		 if(maxAltitude.equals("") && minAltitude.equals("")){
    			 
    	    	 return altitude;

    			 
    		 }
    		 else if(maxAltitude.equals("")){
    			 
    			 return minAltitude;
    			 
    		 }
    		 else if(minAltitude.equals("")){
    			 
    			 return maxAltitude;
    			     			 
    		 }
    		 else{
    			 
    			 int max=Integer.parseInt(maxAltitude);
    			 int min=Integer.parseInt(minAltitude);
    			 
    			 if(max!=min) return minAltitude+" - "+maxAltitude ;
    			 else return maxAltitude;
    	    	     			 
    		 }
    	
     }
     

    @Override
	public void characters(char ch[], int start, int length) {
          
     	 
   	 if(taxonExplorer){

    	
	   	 if(tempVal==null || tempVal.equals("") || tempVal.equals("\t") || tempVal.equals("\n")){
			 
	  		 tempVal = new String(ch,start,length);
	
		 }
		 
		 else{
			 
	  		 tempVal = tempVal.concat(new String(ch,start,length));
	
		 }
	   	 
   	 }
   	 else{
   		 
  		 tempVal = new String(ch,start,length);
   		 
   	 }

    }


	public void setNextURL(String nextURL) {
		this.nextURL = nextURL;
	}


	public String getNextURL() {
		return nextURL;
	}
}