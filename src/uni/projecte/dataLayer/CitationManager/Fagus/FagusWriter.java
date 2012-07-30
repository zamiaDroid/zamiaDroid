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

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.xmlpull.v1.XmlSerializer;

import uni.projecte.controler.PreferencesControler;
import android.content.Context;
import android.os.Environment;
import android.util.Log;
import android.util.Xml;

public class FagusWriter {
	
	private XmlSerializer serializer;
	private StringWriter writer;
	
	

	private String taxon;
	private String author;
	private String comment;
	private boolean taxonAdded=false;
	private boolean createdSideData=false;
	
	private String lastCategory="";
	private String natureness="";
	

	
	public String convertXML2String(){
		
		//s'han de fer més macoooooooooooo
		
		return writer.toString();

		
	}
	
	public void setTaxon(String taxon) {
		this.taxon = taxon;
	}
	
	public void setAuthor(String author){
		
		this.author=author;
		
	}

	
	public void openDocument(){
		

	    writer = new StringWriter();		
		serializer = Xml.newSerializer();
		
		
		 try {
		        serializer.setOutput(writer);
		        serializer.startDocument("UTF-8", true);
		        serializer.setFeature("http://xmlpull.org/v1/doc/features.html#indent-output", true);
		
		        serializer.startTag("", "OrganismCitationList");
		        
		 } catch (Exception e) {
		        throw new RuntimeException(e);
		    } 
		
	}
	
	public void closeDocument(){
		
		  try {
			
			  
			serializer.endTag("", "OrganismCitationList");
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
		
		try {
			
			// <InformatisationDate day="02" hours="13" mins="55" month="09" secs="37" year="2010" />
			
			SimpleDateFormat df=new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
			Date today = null;
			
			try{
		
			
				today = df.parse(date);           
			
			
			
			serializer.startTag("", "ObservationDate");
			
	            serializer.attribute("", "day", String.valueOf(today.getDate()));
	            serializer.attribute("", "hours", String.valueOf(today.getHours()));
	            serializer.attribute("", "mins", String.valueOf(today.getMinutes()));
	            serializer.attribute("", "month", String.valueOf(today.getMonth()+1));
	            serializer.attribute("", "secs",String.valueOf(today.getSeconds()));
	            serializer.attribute("", "year", String.valueOf(today.getYear()+1900));
            
			serializer.endTag("", "ObservationDate");
			
			
			}
			
			catch (ParseException e){
			        
		            
				e.printStackTrace();

			}
			
			
			
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalStateException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		
		

		
		
		
	}
	
	
	public void addComment(String comment){
        	
			 this.comment=comment;
		
	
		
		
	}
	

	
	public void createSideDataCategory(String category){
		
        try {
        	
        	serializer.startTag("","SideData"); 
			serializer.attribute("","type",category);
		
        }
        catch (IllegalArgumentException e) {
		e.printStackTrace();
        } catch (IllegalStateException e) {
		e.printStackTrace();
        } catch (IOException e) {
		e.printStackTrace();
        }
	}
	
	public void closeSideDataCategory(){
		
        try {
        	
        	serializer.endTag("", "SideData"); 
		
        }
        catch (IllegalArgumentException e) {
        	e.printStackTrace();
        } 
        catch (IllegalStateException e) {
        	e.printStackTrace();
        } 
        catch (IOException e) {
        	e.printStackTrace();
        }
		
	}
	
	public void createSideData(String label, String name, String value, String category){

	     try {
		
			if(value!=null && !value.equals("")){
	        	
				serializer.startTag("","Datum");
				
				serializer.attribute("", "label", label);
				serializer.attribute("", "name", name);
					
					serializer.startTag("", "value");
					
						if(value!=null) serializer.text(value);
	
					serializer.endTag("","value");
				
				serializer.endTag("","Datum");
			
		     }
	     }
	        
	     catch (IllegalArgumentException e) {
	        
	    	 e.printStackTrace();
	     } 
	     catch (IllegalStateException e) {
	        
	    	 e.printStackTrace();
	     } 
	     catch (IOException e) {
	     
	    	 e.printStackTrace();
	     }
		
		
	}

	
	public void addTaxon(String sureness){
		
		 try {
         	
	            serializer.startTag("", "OriginalTaxonName");
				serializer.attribute("", "sureness",sureness);
				
				if(taxon==null) serializer.text(""); 
				else serializer.text(taxon);
	            	            
	            serializer.endTag("", "OriginalTaxonName");
	            
	            
	            serializer.startTag("", "CorrectedTaxonName");
				serializer.attribute("", "sureness",sureness);
				
				if(taxon==null) serializer.text(""); 
				else serializer.text(taxon);
	            	            
	            serializer.endTag("", "CorrectedTaxonName");
	            
	            serializer.startTag("", "Accepted");
	            serializer.text("true");
	            serializer.endTag("", "Accepted");

		
	            taxonAdded=true;

			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (IllegalStateException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
         
		
		
	}
	
	public void openCitation(boolean sheet){
		
		
		try {
			
			serializer.startTag("","OrganismCitation");
			
			if(sheet) serializer.attribute("", "origin","Specimen");
			
			
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalStateException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
	
	public void closeCitation(){
		
        try {
        	
        	if(!taxonAdded) addTaxon("");
        	
        	taxonAdded=false;
        	
        	writeAuthor(author);

        	//serializer.attribute("", "origin","Specimen");
			serializer.endTag("","OrganismCitation");
			lastCategory="";
			
			
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
	 *     <CitationCoordinate code="42,12, 1,2" precision="1.0" type="UTM alphanum" units="1m" />
    <SecondaryCitationCoordinate code="42,12, 1,2" precision="0.0" type="UTM num" units="1m" />
	 * 
	 * 
	 * */
	
	public void writeCitationCoordinate(String code){

    	
		try {
		            	
		//	serializer.attribute("", "origin", origin);
			            
	        serializer.startTag("", "CitationCoordinate");
				serializer.attribute("", "code",code);
				serializer.attribute("", "precision","1.0");
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
		            	
			
	        serializer.startTag("", "ObservationAuthor");
		        if(author==null)serializer.text("");
		        else serializer.text(author);
			serializer.endTag("", "ObservationAuthor");
			
            serializer.startTag("", "CitationNotes");
            	if(comment!=null)serializer.text(comment);
            serializer.endTag("", "CitationNotes");
	        
         serializer.startTag("", "Natureness");
        	if(!natureness.equals(""))serializer.text(natureness);
        serializer.endTag("", "Natureness");
            
	        natureness="";
	        comment="";
        
            
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

	public void setNatureness(String value) {

		this.natureness=value;
		
	}



}
