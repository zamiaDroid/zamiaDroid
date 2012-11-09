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
import android.text.format.DateFormat;
import android.util.Log;
import android.util.Xml;

public class ZamiaCitationWriter {
	
	private XmlSerializer serializer;
	private StringWriter writer;
	
	
	public String convertXML2String(){
			
		return writer.toString();
	}

	public void openCitationList(XmlSerializer serializer,StringWriter writer){
		
	    this.writer = writer;		
		this.serializer=serializer;
		
		
        try {
        	
			serializer.startTag("", "ZamiaCitationList");
			
			
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalStateException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		
		
	}

	public void openDocument(){
		
	    writer = new StringWriter();		
		serializer = Xml.newSerializer();
		
		 try {
		        serializer.setOutput(writer);
		        serializer.startDocument("UTF-8", true);
		        serializer.setFeature("http://xmlpull.org/v1/doc/features.html#indent-output", true);
		
		        serializer.startTag("", "ZamiaCitationList");
		        
		 } catch (Exception e) {
		        throw new RuntimeException(e);
		    } 
		
	}
	
	public void closeDocument(){
		
		  try {
			  
			serializer.endTag("", "ZamiaCitationList");
			serializer.endDocument();
			
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalStateException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	        
		
	}
	
	public void closeCitationDocument(){
		
		  try {
			  
			serializer.endTag("", "ZamiaCitationList");
			serializer.endTag("", "zamia");

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
			System.out.println("Today = " + DateFormat.format("dd-MM-yyyy hh:mm:ss",today));
			
			}
			
			catch (ParseException e){
				
		            e.printStackTrace();

			}
			
			serializer.startTag("", "ObservationDate");
			
				serializer.text(date);
			            
			serializer.endTag("", "ObservationDate");
			
			
			
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
			
			serializer.startTag("","value");
			if(value!=null) serializer.text(value);
			//else  serializer.text("");
			serializer.endTag("","value");

			

			
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalStateException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}


	}
	
	public void closeCitationField(){
		
		   try {
			      
			serializer.endTag("","CitationField");
			
		} catch (IllegalArgumentException e) {

			e.printStackTrace();
		} catch (IllegalStateException e) {

			e.printStackTrace();
		} catch (IOException e) {

			e.printStackTrace();
		}
		
	}
	
	public void closeCitation(){
		
		
		try{
		
			
		serializer.endTag("","ZamiaCitation");

			
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalStateException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}


	}

	
	public void openCitation(){
		
	
		try{
		
		serializer.startTag("","ZamiaCitation");



			
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
	
	

	public void openSecondLevelCitations() {
		
        try {
			serializer.startTag("", "SecondaryCitationList");
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalStateException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		
		
	}
	
	public void closeSecondLevelCitations() {
		
        try {
        	
			serializer.endTag("", "SecondaryCitationList");
			
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
	
	public XmlSerializer getSerializer() {
		return serializer;
	}

	public void setSerializer(XmlSerializer serializer) {
		this.serializer = serializer;
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
