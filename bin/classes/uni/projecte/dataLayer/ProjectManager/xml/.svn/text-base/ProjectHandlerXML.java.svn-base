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

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import uni.projecte.controler.ProjectControler;
import uni.projecte.dataTypes.ParsedDataSet;


public class ProjectHandlerXML extends DefaultHandler{

     // ===========================================================
     // Fields
     // ===========================================================
     
     private boolean in_mytag = false;
     private String tempVal;
	 private ProjectControler projCnt;
	 
	 private String predValue;
	 private String name;
	 private String label;
	 private String desc;
	 private String category;
	 
	 private long attId;
	 
	 private boolean attCreated;
     
     
     
     public ProjectHandlerXML(ProjectControler rsCntrl) {
		super();
		
		projCnt=rsCntrl;
		
	}

	private ParsedDataSet myParsedExampleDataSet = new ParsedDataSet();
	//private boolean in_innertag;
	//private boolean in_outertag;
	
	private boolean isDefined=false;

     // ===========================================================
     // Getter & Setter
     // ===========================================================

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
    	 
    	if (localName.equals("SideData")) {
              // this.in_innertag = true;
               category=atts.getValue("type");
               
                           
          }else if (localName.equals("Datum")) {
        	  
               this.in_mytag = true;
               
               name=atts.getValue("name");
               label=atts.getValue("label");

               desc=atts.getValue("tip");

               
          }else if (localName.equals("predefined_values")) {
               isDefined=true;
   			   
          }else if (localName.equals("biological_record_type")) {
        	  
              this.in_mytag = true;
             
                          
         }
          else if (localName.equals("fagus_profile")) {
        	  
                          
         }
          
        
     }
     
     /** Gets be called on closing tags like:
      * </tag> */
     @Override
     public void endElement(String namespaceURI, String localName, String qName)
               throws SAXException {
    	 
    	 if (localName.equals("value")) {
             
       	  if(isDefined){ 
       		  
       		
       		  if (!attCreated) {
       			  
       			  	attId=projCnt.addProjectField(projCnt.getProjectId(),name, label,desc,predValue,"complex",category);
       			           			  	
       			  	attCreated=true;
       			  	
       			  
       			  	projCnt.addFieldItem(attId, tempVal);
       			  	//afegir item amb attId
       		  
       		  }
       		  else{
       			 
     			  	
   			  	projCnt.addFieldItem(attId, tempVal);
     			  	//afegir item amb attId
       			  
       		  }
       	  
       	  }
       	  else {
       		  
       		  
       		  predValue=tempVal;
       		  


       	  }
  			   
         }
          else if (localName.equals("SideData")) {
        	  
        	  
              
          }
          else if (localName.equals("Datum")) {
        	  


    		  if (!attCreated && !isDefined)  {
    			  
    			  if(name.equals("photo")){
    				  
        			  attId=projCnt.addProjectField(projCnt.getProjectId(),name, label,desc,predValue,"photo",category);

    			  }
    			  else if(name.equals("ObservationAuthor")){
    				  
        			  attId=projCnt.addProjectNotEditableField(projCnt.getProjectId(),name, label,desc,"","simple",category);

    			  }
    			  
    			  
    			  else{
    				  
        			  attId=projCnt.addProjectField(projCnt.getProjectId(),name, label,desc,predValue,"simple",category);

    				  
    			  }
    			  
    		  }

           
        	  isDefined=false;

    		  
    		  
   			   
          }else if (localName.equals("predefined_values")) {
               
        	  attCreated=false;
        	  predValue="";
   			   
          }
          else if (localName.equals("biological_record_type")) {
       
        	  projCnt.setCitationType(tempVal);
        	 
        	  
            }
    	
          
          else if (localName.equals("fagus_profile")) {
        	 
        	  

          }
          
        
     }
     
     /** Gets be called on the following structure:
      * <tag>characters</tag> */
     @Override
    public void characters(char ch[], int start, int length) {
          if(this.in_mytag){
          myParsedExampleDataSet.setExtractedString(new String(ch, start, length));
  		 tempVal = new String(ch,start,length);
  		 if(tempVal.contains("\t")) tempVal="";

     }
    }
}