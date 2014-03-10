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


package uni.projecte.dataLayer.CitationManager.Quercus;

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

public class QuercusWriter {
	
	
	private static String relAttXMLNS="http://biodiver.bio.ub.es/vegana/resources/schemas";
	private static String relAttXMLNS_XSI="http://www.w3.org/2001/XMLSchema-instance";
	private static String relAttXMLVersion="1.2";
	private static String relThId="TaxonsAttrib2.xml";
	private static String relXsiSchema="http://biodiver.bio.ub.es/vegana/resources/schemas http://biodiver.bio.ub.es/vegana/resources/schemas/ReleveTable1.2.xsd";
	
	
	private XmlSerializer serializer;
	private StringWriter writer;

	
	private boolean createdSideData=false;
	
	private String lastCategory="";
	private String plotArea="";
	
	String result;
	
	
	
	public void openDocument(String fileName){
		

	    writer = new StringWriter();		
		serializer = Xml.newSerializer();
		
		
		 try {
		        serializer.setOutput(writer);
		        serializer.startDocument("UTF-8", true);
		        serializer.setFeature("http://xmlpull.org/v1/doc/features.html#indent-output", true);
		
		        serializer.startTag("", "ReleveTable");
		        
				serializer.attribute("", "xmlns",relAttXMLNS);
				serializer.attribute("", "xmlns:xsi",relAttXMLNS_XSI);
				
				serializer.attribute("", "code",fileName);
				serializer.attribute("", "releve_table_xml_version",relAttXMLVersion);
				
				serializer.attribute("", "thesaurus_id",relThId);
				serializer.attribute("", "xsi:schemaLocation",relXsiSchema);
		        
		 } catch (Exception e) {
		        throw new RuntimeException(e);
		    } 
		
	}
	
	
	
	public void openReleve(String releveName, String releveType){
		
		
		
		try {

			Log.d("XML", "<Releve ");

			serializer.startTag("","Releve");
			serializer.attribute("", "name",releveName);
			serializer.attribute("", "type",releveType);

			
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalStateException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
	
	
	
	public String convertXML2String(){
		
		//s'han de fer més macoooooooooooo
		
		result=writer.toString();
		
		return result;

		
	}
	

	
	public void closeDocument(){
		
		  try {
			
			serializer.startTag("", "TableVisualOptions");
			serializer.endTag("", "TableVisualOptions");

			serializer.endTag("", "ReleveTable");
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
			
			}
			
			catch (ParseException e)
			        {
		            e.printStackTrace();

			        }
			serializer.startTag("", "SurveyDate");
			
	            serializer.attribute("", "day", String.valueOf(today.getDate()));
	            serializer.attribute("", "hours", String.valueOf(today.getHours()));
	            serializer.attribute("", "mins", String.valueOf(today.getMinutes()));
	            serializer.attribute("", "month", String.valueOf(today.getMonth()));
	            serializer.attribute("", "secs",String.valueOf(today.getSeconds()));
	            serializer.attribute("", "year", String.valueOf(today.getYear()+1900));
            
			serializer.endTag("", "SurveyDate");
			
			
			
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
        		
        		
        		if(newCategory(category) && lastCategory.compareTo("")!=0) {	
        			
        			serializer.endTag("", "SideData"); 
            		Log.d("XML", "SideData > ");

        		
        		}

        		
	        		if(category==null) category="merda";
	        		serializer.startTag("","SideData"); 
	        		
	        		Log.d("XML", "< SideData  "+category+" ");
	
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

		        		Log.d("XML", "Datum <  > "+name+": "+value+" ");

							
					serializer.endTag("","value");
				
					serializer.endTag("","Datum");
				
        	}

			if (last){  
				
        		Log.d("XML", "SideData > ");

				serializer.endTag("", "SideData"); 
				createdSideData=false;
				lastCategory="";
				
			
			}
			
			
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalStateException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		
	}
	
	
	
	

	
	public void closeReleve(){
		
        try {
        	
			Log.d("XML", "Releve > ");
			
			createReleveArea();

			serializer.endTag("","Releve");
			lastCategory="";
			
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalStateException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}


		
	}
	
	public void addReleveDate(String day, String month, String year){
		
    	
		try {
			
        serializer.startTag("", "SurveyDate");
		serializer.attribute("", "day",day);
		serializer.attribute("", "month",month);
		serializer.attribute("", "year",year);

        serializer.endTag("", "SurveyDate");
		
		}catch (IllegalArgumentException e) {
						
			e.printStackTrace();
			
		} catch (IllegalStateException e) {
						
			e.printStackTrace();
			
		} catch (IOException e) {
						
			e.printStackTrace();
		}
		
		
		
	}
	
	public void addReleveArea(String area){
		
		plotArea=area;
		
	}
	
	private void createReleveArea(){
		
    	
		try {

			
			if(!plotArea.equals("")) {
			
	        serializer.startTag("", "PlotArea");
	        
	        	try {
	        
	        		float areaF=Float.parseFloat(plotArea);
	        		serializer.text(areaF+"");
	        	
	        	}
	        	catch (NumberFormatException e) {
					
	    			e.printStackTrace();
	    			
	    		}
	        	
	        serializer.endTag("", "PlotArea");
        
	        plotArea="";
	        
		}
		
		}catch (IllegalArgumentException e) {
						
			e.printStackTrace();
			
		} catch (IllegalStateException e) {
						
			e.printStackTrace();
			
		} catch (IOException e) {
						
			e.printStackTrace();
		}
		
		
		
	}
	
	public void addReleveComment(String comment){
		
    	
		try {
			
        serializer.startTag("", "ReleveComments");
        serializer.text(comment);
        serializer.endTag("", "ReleveComments");
		
		}catch (IllegalArgumentException e) {
						
			e.printStackTrace();
			
		} catch (IllegalStateException e) {
						
			e.printStackTrace();
			
		} catch (IOException e) {
						
			e.printStackTrace();
		}
		
		
		
	}
	
	public void addReleveSintaxon(String sintaxon){
		
    	
		try {
			
        serializer.startTag("", "OriginalSyntaxonName");
        serializer.text(sintaxon);
        serializer.endTag("", "OriginalSyntaxonName");
		
		}catch (IllegalArgumentException e) {
						
			e.printStackTrace();
			
		} catch (IllegalStateException e) {
						
			e.printStackTrace();
			
		} catch (IOException e) {
						
			e.printStackTrace();
		}
		
		
		
	}
	
	public void addReleveEntry(String originalName,String value, String sureness,String level, String releveComment){
		
    	
		try {
			
        serializer.startTag("", "ReleveEntry");

        	if(!level.equals(""))serializer.attribute("", "layer",level);
			serializer.attribute("", "original_name",originalName);
			serializer.attribute("", "value",value);
			if(!sureness.equals(""))serializer.attribute("", "sureness",sureness);

        
        
        	if(!releveComment.equals("")){
        		
                serializer.startTag("", "comments");
                serializer.text(releveComment);
                serializer.endTag("", "comments");
        		
        	}
        	
            serializer.endTag("", "ReleveEntry");

		
		}catch (IllegalArgumentException e) {
						
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
	
	public void writeReleveCoordinate(String code){

		try {
			            
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
	
	public void writeAuthor(String author,boolean authorLast){

    	
		try {
		            	
		//	serializer.attribute("", "origin", origin);
			
			if(authorLast && createdSideData){
				
				Log.d("XML", "SideData > ");

				serializer.endTag("", "SideData"); 
				createdSideData=false;
				lastCategory="";

			}
			            
	        serializer.startTag("", "SurveyAuthor");
		        if(author==null)serializer.text("");
		        else serializer.text(author);
			serializer.endTag("", "SurveyAuthor");
	        
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
	
	
	public void stringToFile(String fileName, Context c){
		
		
		try {
			
		    File root = Environment.getExternalStorageDirectory();
		    PreferencesControler pC=new PreferencesControler(c);
		    
		    if (root.canWrite()){
		    	
		        File gpxfile = new File(Environment.getExternalStorageDirectory()+"/"+pC.getDefaultPath()+"/Citations/", fileName+".xml");
		      
		        FileWriter gpxwriter = new FileWriter(gpxfile);
		        BufferedWriter out = new BufferedWriter(gpxwriter);
		        out.write(result);
		        out.close();
		        
		    }
		} catch (IOException e) {
			
		    Log.e("File EXPORT", "Could not write file " + e.getMessage());
		}
		
		
		
	}

	public void createReleveTableEntry(String taxon2, String level) {


	    try {
		
	    serializer.startTag("", "ReleveTableEntry");
		
			if(!level.equals(""))serializer.attribute("", "layer",level);
	    	serializer.attribute("", "original_name",taxon2);
        
        serializer.endTag("", "ReleveTableEntry");
        
        
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalStateException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	    
		
		
		
	}
	
	




}
