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


package uni.projecte.dataLayer.ProjectManager;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringWriter;
import org.xmlpull.v1.XmlSerializer;

import uni.projecte.controler.PreferencesControler;
import android.content.Context;
import android.os.Environment;
import android.util.Log;
import android.util.Xml;

public class ZamiaProjectWriter {
	
	private XmlSerializer serializer;
	private StringWriter writer;
	private String extension=".xml";

	
	
	public String convertXML2String(){
		
		return writer.toString();
		
	}
	
	public void openCitationDocument(){
		
	    writer = new StringWriter();		
		serializer = Xml.newSerializer();
		
		 try {
		        serializer.setOutput(writer);
		        serializer.startDocument("UTF-8", true);
		        serializer.setFeature("http://xmlpull.org/v1/doc/features.html#indent-output", true);
		        
		        serializer.startTag("","zamia");
		
		        
		 } catch (Exception e) {
		        throw new RuntimeException(e);
		    } 
		
		
	}
	
	
	public void openDocument(){

	    writer = new StringWriter();		
		serializer = Xml.newSerializer();
		
		 try {
		        serializer.setOutput(writer);
		        serializer.startDocument("UTF-8", true);
		        serializer.setFeature("http://xmlpull.org/v1/doc/features.html#indent-output", true);
		
		        
		 } catch (Exception e) {
		        throw new RuntimeException(e);
		    } 
		
	}
	
	public void closeDocument(){
		
		  try {
			  

			serializer.endDocument();
			
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalStateException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	        
		
	}
	
	
	public void setDescription(String description){
		
		 try {
       	
			 
			 serializer.startTag("", "description");
	           	
			 	serializer.text(description);
	         
	         serializer.endTag("", "description");
	         
		
			} catch (IllegalArgumentException e) {
				
				e.printStackTrace();
				
			} catch (IllegalStateException e) {

				e.printStackTrace();
				
			} catch (IOException e) {

				e.printStackTrace();
			}
      
		
	}
	
	//  <thesaurus type="Flora" filum="F" server="Biocat">Flora BDBC</thesaurus>

	
	public void setThesaurus(String filum, String thName, String server, String type){
		
		 try {
      	
			 
	        serializer.startTag("", "thesaurus");
					
	            if(thName!=null) {
	            	
	            	if(type!=null || server!=null){
	            		
	            		serializer.attribute("", "sourceType", type);
	            		serializer.attribute("", "sourceId", server);
					}
	            	else{
	            		
	            		serializer.attribute("", "sourceType", "remote");
	            		serializer.attribute("", "sourceId", "bdbc");
	            		
	            	}
					
					if(filum!=null) serializer.attribute("", "filum",filum);
				
					serializer.text(thName);
					
	            }
				
	        serializer.endTag("", "thesaurus");
		
			} catch (IllegalArgumentException e) {
				
				e.printStackTrace();
				
			} catch (IllegalStateException e) {

				e.printStackTrace();
				
			} catch (IOException e) {

				e.printStackTrace();
			}
     
		
	}
	
	
	public void setProjectType(String projectType){
		
		 try {
        	
			 
	            serializer.startTag("", "project_type");
	            	if(projectType!=null)serializer.text(projectType);
	            serializer.endTag("", "project_type");
		
			} catch (IllegalArgumentException e) {
				
				e.printStackTrace();
				
			} catch (IllegalStateException e) {

				e.printStackTrace();
				
			} catch (IOException e) {

				e.printStackTrace();
			}
			
			
       
		
	}
	
	public void openProject(String name, String language){
				
		try {

			serializer.startTag("","zamia_project");
			serializer.attribute("", "name", name);
			serializer.attribute("", "language", language);
			
			
		} catch (IllegalArgumentException e) {
			
			e.printStackTrace();
			
		} catch (IllegalStateException e) {
			
			e.printStackTrace();
			
		} catch (IOException e) {

			e.printStackTrace();
		}

	}
	
	public void closeProject(){
		
        try {
        	
      	
			serializer.endTag("","zamia_project");
			
			
		} catch (IllegalArgumentException e) {

			e.printStackTrace();
			
		} catch (IllegalStateException e) {
			
			e.printStackTrace();
			
		} catch (IOException e) {
			
			e.printStackTrace();
		}


		
	}
	
	public void openFieldList(){
		
		try {

			serializer.startTag("","field_list");
			
			
		} catch (IllegalArgumentException e) {
			
			e.printStackTrace();
			
		} catch (IllegalStateException e) {
			
			e.printStackTrace();
			
		} catch (IOException e) {

			e.printStackTrace();
		}

	}
	
	public void closeFieldList(){
		
		try {

			serializer.endTag("","field_list");
			
			
		} catch (IllegalArgumentException e) {
			
			e.printStackTrace();
			
		} catch (IllegalStateException e) {
			
			e.printStackTrace();
			
		} catch (IOException e) {

			e.printStackTrace();
		}

	}

	
	public void openField(String label, String name, String description, String type,String defValue){

		        	
		try {
		       
			serializer.startTag("", "field");
			
			serializer.attribute("", "label", label);
				if (name!=null) serializer.attribute("", "name", name);
				if (description!=null) serializer.attribute("", "description", description);
			serializer.attribute("", "type", type);

			serializer.startTag("", "default_value");
				if (defValue!=null) serializer.text(defValue);
        	serializer.endTag("", "default_value");
	        
		} catch (IllegalArgumentException e) {
						
			e.printStackTrace();
			
		} catch (IllegalStateException e) {
						
			e.printStackTrace();
			
		} catch (IOException e) {
						
			e.printStackTrace();
		}
		            
		      
		    
		}
	
	public void closeField(){

    	
		try {
		            	
			serializer.endTag("", "field");
	        
		} catch (IllegalArgumentException e) {
						
			e.printStackTrace();
			
		} catch (IllegalStateException e) {
						
			e.printStackTrace();
			
		} catch (IOException e) {
						
			e.printStackTrace();
		}
		            
		      
		    
		}
	
	
	public void openSLFields(){

    	
		try {
		       
			serializer.startTag("", "second_level_fields");
			        
		} catch (IllegalArgumentException e) {
						
			e.printStackTrace();
			
		} catch (IllegalStateException e) {
						
			e.printStackTrace();
			
		} catch (IOException e) {
						
			e.printStackTrace();
		}
		            
		      
		    
		}
	
	public void closeSLFields(){

    	
		try {
		            	
			serializer.endTag("", "second_level_fields");
	        
		} catch (IllegalArgumentException e) {
						
			e.printStackTrace();
			
		} catch (IllegalStateException e) {
						
			e.printStackTrace();
			
		} catch (IOException e) {
						
			e.printStackTrace();
		}
		            
		      
		    
		}
	
	/*
	 * 
	 *     <CitationCoordinate code="42,12, 1,2" precision="1.0" type="UTM alphanum" units="1m" />
    <SecondaryCitationCoordinate code="42,12, 1,2" precision="0.0" type="UTM num" units="1m" />
	 * 
	 * 
	 * */
	
	public void writeCitationCoordinate(){

    	
		try {
		            		            
	        serializer.startTag("", "CitationCoordinate");
				serializer.attribute("", "code","");
				serializer.attribute("", "precision","0.0");
				serializer.attribute("", "type","UTM alphanum");
				serializer.attribute("", "units","1m");
			serializer.endTag("", "CitationCoordinate");
	        
		} catch (IllegalArgumentException e) {
						
			e.printStackTrace();
			
		} catch (IllegalStateException e) {
						
			e.printStackTrace();
			
		} catch (IOException e) {
						
			e.printStackTrace();
		}
		            
		      
		    
	}
	

	public void writeToFile(String mostra, String fileName, String exportMode, Context c){
	
		try {
			
			
		    File root = Environment.getExternalStorageDirectory();
		    PreferencesControler pC=new PreferencesControler(c);
		    
		    if (root.canWrite()){
		        File gpxfile = new File(Environment.getExternalStorageDirectory()+"/"+pC.getDefaultPath()+"/"+exportMode+"/",fileName+extension);
		        FileWriter gpxwriter = new FileWriter(gpxfile);
		        BufferedWriter out = new BufferedWriter(gpxwriter);
		        out.write(mostra);
		        out.close();
		    }
		} catch (IOException e) {
		    Log.e("sample", "Could not write file " + e.getMessage());
		}
	
}



	public void addItemsList(String[] itemsbyFieldId) {

	try{
		
        serializer.startTag("", "predefined_values");
		
		for (int i=0; i< itemsbyFieldId.length; i++){
			
			serializer.startTag("", "value");
				serializer.text(itemsbyFieldId[i]);
			serializer.endTag("", "value");
		}

		serializer.endTag("", "predefined_values");
		
		
		} catch (IllegalArgumentException e) {
			
			e.printStackTrace();
			
		} catch (IllegalStateException e) {
						
			e.printStackTrace();
			
		} catch (IOException e) {
						
			e.printStackTrace();
		}
		
		
	}

	public XmlSerializer getSerializer() {
		return serializer;
	}

	public void setSerializer(XmlSerializer serializer) {
		this.serializer = serializer;
	}

	public StringWriter getWriter() {
		return writer;
	}


	    

}
