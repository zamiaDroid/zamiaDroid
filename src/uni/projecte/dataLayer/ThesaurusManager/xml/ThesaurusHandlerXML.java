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

package uni.projecte.dataLayer.ThesaurusManager.xml;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import uni.projecte.controler.ThesaurusControler;
import uni.projecte.dataTypes.ParsedDataSet;


public class ThesaurusHandlerXML extends DefaultHandler{

     // ===========================================================
     // Fields
     // ===========================================================
     
	 private String tempVal;
	 private String genus;
	 private String specie;
	 private String subEpitet="";
	 private String iCode;
	 private String nameCode;
	 private String filum;
	 
	 private String iCodeSpec;
	 private String nameCodeSpec;
	 
	 private String author;
	 private String subAuthor="";
	 private String level;
	 private int total;
	  
	 private ThesaurusControler thCont;
     
     
     public ThesaurusHandlerXML(ThesaurusControler thCont) {
		super();
		total=0;
		this.thCont=thCont;
		
	}

	private ParsedDataSet myParsedExampleDataSet = new ParsedDataSet();
	//private boolean in_innertag;
	//private boolean in_outertag;

     // ===========================================================
     // Getter & Setter
     // ===========================================================

     public ParsedDataSet getParsedData() {
          return this.myParsedExampleDataSet;
     }
     
     public int getTotalItems(){
    	  	 
    	 return this.total;
    	 
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
      
    	 
    	 if (localName.equals("taxon")) {
    		 
               String category=atts.getValue("name");
               level=atts.getValue("level");
               
               if(level.equals("Species")){
            	   
            	   iCodeSpec=atts.getValue("icode");;
            	   nameCodeSpec=atts.getValue("namecode");

               }
               else{
            	   
            	   iCode=atts.getValue("icode");
                   nameCode=atts.getValue("namecode");
            	   
               }
            
            
	            
	            if (level.equals("Species")){

	            	specie=category;

	            }
	            else if(level.equals("Genus")){
	              	
	            	genus=category;
	            	
	            	
	            }      
	            else if (level.equals("Subspecies")){
  	
	            	subEpitet=category;
	            	
	            } 
		        else if (level.equals("Variety")){
		              	
		        	subEpitet=category;
		         
		        } 
		        else if (level.equals("Subvariety")){
		        	
		        	subEpitet=category;
		        	
		        }
		        else if (level.equals("Form")){
		              	
		           subEpitet=category;
		         
		        } 
	          
                           
          }
    	 
         else if(localName.equals("author")){
         	
         	
         }
      
      else if (localName.equals("taxon_pool")) {
          // this.in_outertag = true;
   	   
    	  filum=atts.getValue("filum");
    	  thCont.updateType(filum);

    	  
    	  thCont.startTransaction();
    	  
    	  
      }
     }
     
     /** Gets be called on closing tags like:
      * </tag> */
     @Override
     public void endElement(String namespaceURI, String localName, String qName) throws SAXException {
    	 
    	 
         if (localName.equals("taxon")) {
        	 
	          if (level.equals("Species")){
	        	  
	          	thCont.addElement(genus, specie,"",iCodeSpec, nameCodeSpec, author,"","");
	          	level="Genus";
	          	subAuthor="";
	          	
	          }
	          else if (level.equals("Subspecies")){
		          	
	          	thCont.addElement(genus,specie,subEpitet,iCode, nameCode, author,subAuthor,"subsp.");
	          	subEpitet="";
	          	level="Species";
	          	subAuthor="";
	
	          }
	          else if (level.equals("Variety")){
		          	
		          	thCont.addElement(genus,specie,subEpitet,iCode, nameCode,author,subAuthor,"var.");
		          	subEpitet="";
		          	level="Species";
		          	subAuthor="";
		
		      }
	          else if (level.equals("Subvariety")){
		          	
		          	thCont.addElement(genus,specie,subEpitet,iCode, nameCode,author,subAuthor,"subvar.");
		          	subEpitet="";
		          	level="Species";
		          	subAuthor="";
		
		      }
	          else if (level.equals("Form")){
		          	
		          	thCont.addElement(genus,specie,subEpitet,iCode, nameCode,author,subAuthor,"form.");
		          	subEpitet="";
		          	level="Species";
		          	subAuthor="";
		
		      }
	        /*  else if (level.equals("Form")){
		          	
		          	thCont.addElement(genus,specie,subEpitet,iCode, nameCode,author,subAuthor,"subvar.");
		          	subEpitet="";
		          	level="Species";
		          	subAuthor="";
		
		      }*/
          
	          
          }
          else if (localName.equals("author")) {
  
        	  if(tempVal.equals("null")) {
        		  
        		  if(subEpitet.equals("")){
        			  
            		  author="";
        			  subAuthor="";
        			  tempVal="";

        		  }
        		  
        		  //author inside subspecie tag
        		  else{
        			  
        			  subAuthor="";
        			  tempVal="";
        			  
        		  }
        		  
        	  }
        	  else{ 
        		  
        		 //subAuthor
        		  if(subEpitet.equals("")){
        			  
        			  if(tempVal.equals("\n") || tempVal.equals("\n")) author="";
        			  else author=tempVal.trim();
        			  subAuthor="";
        			  tempVal="";

        			  
        		  }
        		  
        		  //author inside subspecie tag
        		  else{
        			  
        			  if(tempVal.equals("") || tempVal.equals("\n") ) subAuthor="";
        			  else subAuthor=tempVal.trim();
        			  tempVal="";

        		  }
        		  
        	  }
              
          }
         
          else if (localName.equals("taxon_pool")) {

        	  
        	  
         }
     }
     

     @Override
    public void characters(char ch[], int start, int length) {

    	 if(tempVal==null || tempVal.equals("") || tempVal.equals("\t") || tempVal.equals("\n")){
    		 
      		 tempVal = new String(ch,start,length);

    	 }
    	 
    	 else{
    		 
      		 tempVal = tempVal.concat(new String(ch,start,length));

    	 }

    }
}