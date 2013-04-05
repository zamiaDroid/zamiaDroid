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


package uni.projecte.dataLayer.CitationManager.KML;

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

public class KMLWriter {
	
	private XmlSerializer serializer;
	private StringWriter writer;
	
	

	private String taxon;
	private boolean createdSideData=false;
	
	private String lastCategory="";
	

	
	public String convertXML2String(){
		
		//s'han de fer més macoooooooooooo
		
		return writer.toString();

		
	}
	
	public void setTaxon(String taxon) {
		this.taxon = taxon;
	}

	
	public void openDocument(){
		

	    writer = new StringWriter();		
		serializer = Xml.newSerializer();
		
		
		 try {
		        serializer.setOutput(writer);
		        serializer.startDocument("UTF-8", true);
		        serializer.setFeature("http://xmlpull.org/v1/doc/features.html#indent-output", true);
		
		        serializer.startTag("", "kml");
		        serializer.attribute("", "xmlns", "http://www.opengis.net/kml/2.2");
		        
		        serializer.startTag("", "Document");
		 
		        
		 } catch (Exception e) {
		        throw new RuntimeException(e);
		    } 
		
	}
	
	public void closeDocument(){
		
		  try {
			
			  
		  serializer.endTag("", "Document");
			serializer.endTag("", "kml");
			serializer.endDocument();
			
			
			
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalStateException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	        
		
	}
	
	
	public void addDate(String date){

		
	}
	
	
	public void addComment(String comment){
		
		 try {
        	
			 
	            serializer.startTag("", "CitationNotes");
	            serializer.text(comment);
	            serializer.endTag("", "CitationNotes");
		
			 

			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (IllegalStateException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
        
		
		
	}
	
	
	private boolean newCategory(String category){
		
		
		return (category.compareTo(this.lastCategory)!=0);
		
		
	}
	
	
	public void createSideData(String label, String name, String value, boolean last,String category){
		
	
		
        try {
			
        	
        	if (!createdSideData || newCategory(category)){ 
        		
        		
        		if(newCategory(category) && lastCategory.compareTo("")!=0) 	serializer.endTag("", "SideData"); 

        		
	        		if(category==null) category="merda";
	        		serializer.startTag("","SideData"); 
	
	        		serializer.attribute("","type",category);
	        		this.createdSideData=true;
	        		this.lastCategory=category;
        		
        	}

        	if(value!=null && !value.equals("")){
        	
				serializer.startTag("","Datum");
				
				serializer.attribute("", "label", label);
				serializer.attribute("", "name", name);
					
					serializer.startTag("", "value");
					
						if(value!=null) serializer.text(value);

							
					serializer.endTag("","value");
				
					serializer.endTag("","Datum");
				
        	}

			if (last){  
				
				serializer.endTag("", "SideData"); 
				createdSideData=false;
				
			
			}
			
			
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalStateException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		
	}
	
	
	public void openCitation(){
		
		try {
			
			serializer.startTag("","Placemark");

			
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalStateException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
	
	public void closeCitation(String title,String description){
		
        try {
        	
			serializer.startTag("", "name");
			
				serializer.text(title);
            
			serializer.endTag("", "name");
			
			serializer.startTag("","description");
        	
        		serializer.text("<![CDATA["+description+"]]>");
        	
			serializer.endTag("","description");

			serializer.endTag("","Placemark");

			
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalStateException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}


		
	}
	
	
	public void createCitationField(String label, String name, String value,String category){
		
		
		try{
		
		serializer.startTag("","CitationField");

		serializer.attribute("", "label", label);
		serializer.attribute("", "name", name);
		serializer.attribute("", "category", category);
			
			if(value!=null) serializer.text(value);
			else  serializer.text("");
			
		serializer.endTag("","CitationField");

			
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalStateException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}


	}

	
	public void writeCitation(String origin, String bio_type){

		        	
		try {
		            	
		//	serializer.attribute("", "origin", origin);
			            
	        serializer.startTag("", "biological_record_type");
				serializer.text(bio_type);
			serializer.endTag("", "biological_record_type");
	        
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
  	 *	<Point>
     *   <coordinates>102.595626,14.996729</coordinates>
     * </Point>
	 * 
	 * 
	 * */
	
	public void writeCitationCoordinatePoint(String code){
    	
		try {
		            	
	        serializer.startTag("", "Point");
	        
	        	serializer.startTag("", "coordinates");

					serializer.text(code);
				
				serializer.endTag("", "coordinates");
	
			serializer.endTag("", "Point");
	        
		} catch (IllegalArgumentException e) {
						
			e.printStackTrace();
			
		} catch (IllegalStateException e) {
						
			e.printStackTrace();
			
		} catch (IOException e) {
						
			e.printStackTrace();
		}	    
	}
	
	/*
	 * <Polygon> 
	 * 	<outerBoundaryIs>  
	 * 		<LinearRing>  
  	 *  		<coordinates>
   	 *				135.2, 35.4, 0. 
     *				135.4, 35.6, 0.
   	 *				135.2, 35.6, 0.
   	 *				135.2, 35.4, 0. 
   	 *  		</coordinates>
 	 *		</LinearRing> 
 	 *	</outerBoundaryIs> 
 	 * </Polygon>	  
	 * 
	 */
	
	public void writeCitationCoordinatePolygon(String coords){
    	
		try {
		            	
	        serializer.startTag("", "Polygon");
	        	serializer.startTag("", "outerBoundaryIs");
	        		serializer.startTag("", "LinearRing");

	        			serializer.startTag("", "coordinates");

	        				serializer.text(coords);
				
						serializer.endTag("", "coordinates");
				
					serializer.endTag("", "LinearRing");
				serializer.endTag("", "outerBoundaryIs");
			serializer.endTag("", "Polygon");
	        
		} catch (IllegalArgumentException e) {
						
			e.printStackTrace();
			
		} catch (IllegalStateException e) {
						
			e.printStackTrace();
			
		} catch (IOException e) {
						
			e.printStackTrace();
		}	    
	}
	
	public void writeSecondaryCitationCoordinate(String code){

    	
		try {
		            	
		//	serializer.attribute("", "origin", origin);
			            
	        serializer.startTag("", "SecondaryCitationCoordinate");
				serializer.attribute("", "code",code);
				serializer.attribute("", "precision","1.0");
				serializer.attribute("", "type","UTM num");
				serializer.attribute("", "units","1m");
			serializer.endTag("", "SecondaryCitationCoordinate");
	        
		} catch (IllegalArgumentException e) {
						
			e.printStackTrace();
			
		} catch (IllegalStateException e) {
						
			e.printStackTrace();
			
		} catch (IOException e) {
						
			e.printStackTrace();
		}
		            
		      
		    
	}
	
	public void writeAuthor(String author){

    	
		try {
		            	
		//	serializer.attribute("", "origin", origin);
			            
	        serializer.startTag("", "ObservationAuthor");
		        if(author==null)serializer.text("");
		        else serializer.text(author);
			serializer.endTag("", "ObservationAuthor");
	        
		} catch (IllegalArgumentException e) {
						
			e.printStackTrace();
			
		} catch (IllegalStateException e) {
						
			e.printStackTrace();
			
		} catch (IOException e) {
						
			e.printStackTrace();
		}
		            
		      
		    
	}


	public void writeToFile(String mostra, String fileName, Context c){
	
	
	try {
	    File root = Environment.getExternalStorageDirectory();
	    PreferencesControler pC=new PreferencesControler(c);
	    
	    if (root.canWrite()){
	        File gpxfile = new File(Environment.getExternalStorageDirectory()+"/"+pC.getDefaultPath()+"/Citations/", fileName);
	        FileWriter gpxwriter = new FileWriter(gpxfile);
	        BufferedWriter out = new BufferedWriter(gpxwriter);
	        out.write(mostra);
	        out.close();
	    }
	} catch (IOException e) {
	    Log.e("sample", "Could not write file " + e.getMessage());
	}
	
	
	
}



}
