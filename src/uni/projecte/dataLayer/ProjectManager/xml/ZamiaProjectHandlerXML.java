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

import java.util.ArrayList;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import uni.projecte.controler.ProjectZamiaControler;
import uni.projecte.dataTypes.ParsedDataSet;


public class ZamiaProjectHandlerXML extends DefaultHandler{

     // ===========================================================
     // Fields
     // ===========================================================
     
     private boolean in_mytag = false;
     private String tempVal;
	 private ProjectZamiaControler zpControler;
	 
	 private String predValue;
	 private String name;
	 private String label;
	 private String desc;
	 private String category;

	 
	 private boolean secondFields;
	 
	 private long attId;
	 
	 private boolean attCreated;
     
	 private ArrayList<String> subFieldItems;
     
     
     public ZamiaProjectHandlerXML(ProjectZamiaControler zpControler) {
		super();
		
		this.zpControler=zpControler;
		
	}

	private ParsedDataSet myParsedExampleDataSet = new ParsedDataSet();

	
	private String fieldName;
	private String fieldLabel;
	private String fieldDesc;
	private String fieldType;
	private String fieldCat="";

	private String thType;
	private String thServer;
	private String thSourceId;
	private String thSourceType;
	
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
    	 
    	if (localName.equals("field")) {

               fieldName=atts.getValue("name");
               fieldLabel=atts.getValue("label");
               fieldDesc=atts.getValue("description");
               fieldType=atts.getValue("type");
               fieldCat=atts.getValue("cat");
                           
          }
    	else if (localName.equals("field_list")) {
        	  
        	  zpControler.startFieldTransaction();
               
         }
         else if (localName.equals("default_value")) {
        	  
    			   
          }
         else if (localName.equals("second_level_fields")) {
         	 
          	 	zpControler.setSecondLevelField(true);
          	 	subFieldItems=new ArrayList<String>();
          	 	
         }
         else if (localName.equals("CitationCoordinate")) {
        	  
              zpControler.setHasLocation(true);
                          
         }
         else if (localName.equals("thesaurus")) {
       	  
             thType=atts.getValue("filum");
             thServer=atts.getValue("server");
             thSourceId=atts.getValue("sourceId");
             thSourceType=atts.getValue("sourceType");
                         
        }
          else if (localName.equals("project_type")) {
        	  
        	  this.in_mytag = true; 
                          
         }
          else if (localName.equals("zamia_project")) {
        	  
        	  zpControler.setLanguage(atts.getValue("language"));
        	  zpControler.setProjectName(atts.getValue("name"));
                          
         }
          
        
     }
     
     /** Gets be called on closing tags like:
      * </tag> */
     @Override
     public void endElement(String namespaceURI, String localName, String qName)
               throws SAXException {
    	 
    	 if (localName.equals("value")) {
    		 
    		 if(zpControler.isSecondLevelField()){
    			 
    			 subFieldItems.add(tempVal);
    			 
    		 }
    		 else{
    			 
        		 zpControler.addPredefinedValue(tempVal);
    			 
    		 }
       
       	  }else if (localName.equals("default_value")) {
        	  
       		  if(zpControler.isSecondLevelField()){
       			  
           		 zpControler.addSecondLevelProjectField(fieldName, fieldLabel, fieldDesc, fieldType,tempVal);
       			  
       		  }
       		  else{
       			  
       			  if(fieldCat==null) fieldCat="";
       			  
          		 zpControler.addProjectField(fieldName, fieldLabel, fieldDesc, fieldType,tempVal,fieldCat);

       		  }
       		  

     		 this.in_mytag=true;
    		  
   			   
          }else if (localName.equals("field")) {
        	  
        	  if(zpControler.isSecondLevelField()){
    			 
        		  zpControler.addSecondLevelFieldList(subFieldItems);
        		  subFieldItems=new ArrayList<String>();
        		  
    		 }
    		 else{
    			 
           	  	zpControler.updateComplexType();
    			 
    		 }

   			   
          }else if (localName.equals("second_level_fields")) {
         	  		
        	 zpControler.setSecondLevelField(false);

			   
          }else if (localName.equals("field_list")) {
             
        	  zpControler.addAutoFields();
        	  
        	  zpControler.endFieldTransaction();
   			   
          }
          else if (localName.equals("thesaurus")) {
              
        	  
        	//before update: sourceId and sourceType didn't exist  
  			if(thServer!=null){
  				
  	         	 zpControler.setRemoteTh(tempVal,thServer,thType);
  				
  			}
  			//after update: 
  				// if: 			sourceType=local
  				// else if:		sourceType=remote	
  			else{
  				
  				if(thSourceType!=null && thSourceType.equals("remote")){
  					
  	  	         	 zpControler.setRemoteTh(tempVal,thSourceId,thSourceType);
  					
  				}
  				
  				else{
  					//zpControler.setLocalTh(tempVal,thSourceId,thSource);
  				}
  				
  			}
         	  
           }
          else if (localName.equals("project_type")) {
       
        	 zpControler.setBiologicalRecordType(tempVal);
        	  
          }
    	 
          else if (localName.equals("description")) {
          
         	  
          }
          else if (localName.equals("zamia_project")) {
        	 
        	  zpControler.createSecondLevelFields();
        	  zpControler.updateInfo();

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