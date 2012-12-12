

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

import java.util.ArrayList;
import java.util.HashMap;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import uni.projecte.dataLayer.ProjectManager.objects.Project;
import uni.projecte.dataTypes.FieldsList;
import uni.projecte.dataTypes.ProjectField;


public class ZamiaCitationFastHandlerXML extends DefaultHandler{

     // ===========================================================
     // Fields
     // ===========================================================
     
     private String tempVal;
	 
 	private String fieldName="";
	private String fieldLabel="";
	private String fieldDesc="";
	private String fieldType="";
	private String fieldCat="";
	private String fieldValue="";
	
	private String projName="";
	private String filum="";
	private String thName="";

	 private int citationCount=0;
	 private FieldsList fieldsList;
	 private Project projObj;
	 private ArrayList<String> fieldItems;


	 
	 public ZamiaCitationFastHandlerXML(Project projObj, FieldsList fieldsList) {
		super();
		
		this.projObj=projObj;
		this.fieldsList=fieldsList;
		fieldItems=new ArrayList<String>();
		
	}


     // ===========================================================
     // Methods
     // ===========================================================
     @Override
     public void startDocument() throws SAXException {

     
     }

     @Override
     public void endDocument() throws SAXException {

     
     }

    
     @Override
	public void startElement(String namespaceURI, String localName,
               String qName, Attributes atts) throws SAXException {
          
    	 
    	 if (localName.equals("ZamiaCitationList")) {
      
    		 
          }
    	 else if (localName.equals("ZamiaCitation")) {
        	  
        	  
                           
          }
          else if (localName.equals("field")) {


                fieldName=atts.getValue("name");
                fieldLabel=atts.getValue("label");
                fieldDesc=atts.getValue("description");
                fieldType=atts.getValue("type");
                fieldCat=atts.getValue("cat");

                            
           }
          else if (localName.equals("zamia_project")) {
          	  
          	  projObj.setProjName(atts.getValue("name"));
                
          }
          else if (localName.equals("thesaurus")) {
          	  
          	  projObj.setProjType(atts.getValue("filum"));
                
          }
    	   else{
        	  
        	  ///no fem res...
        	  
        	  
          }
     }
     

     @Override
	public void endElement(String namespaceURI, String localName, String qName)
               throws SAXException {
          
    	 if (localName.equals("ZamiaCitationList")) {
    

         
    	}
    	 //counting all citations
    	 else if (localName.equals("ZamiaCitation")) {
        	  
    		  citationCount++;
   			   
         }
    	 else if (localName.equals("value")) {
    		 

    		 fieldItems.add(tempVal);
             
       
       	  }else if (localName.equals("default_value")) {

     		 	fieldValue=tempVal;

   			   
          }else if (localName.equals("field")) {
        	  
        	  
        	     if(!fieldsList.fieldExists(fieldName)){

                 	  ProjectField pf= new ProjectField(fieldName, fieldDesc, fieldLabel, fieldValue, fieldType,fieldItems);
                 	  fieldsList.addNewField(fieldName, pf);
                 	  
                   }
       		  
        	     fieldItems=new ArrayList<String>();
  			   	 fieldValue="";
        	     
          }
          else if (localName.equals("thesaurus")) {
          	  
        	  projObj.setThName(tempVal);
                
          } 
          else if (localName.equals("zamia_project")) {
          	  
               
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


	public void setCitationCount(int citationCount) {
		this.citationCount = citationCount;
	}
}



