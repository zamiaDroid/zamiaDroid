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

package uni.projecte.dataLayer.CitationManager.Xflora;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import uni.projecte.dataLayer.CitationManager.CitationExporter;
import android.util.Log;


public class XfloraExporter extends CitationExporter {

	private String result;

	private String taxonLine;
	private String sureness;
	private String naturenessLine;
	private String fieldsLine;
	
	private XfloraTags XFloraParser;
	
	
	public XfloraExporter(String projectName, String thesaurusName,String projectType) {
		
		super(projectName, thesaurusName, projectType);
		
		XFloraParser= new XfloraTags();

		result="";
	
	
	}
	

	@Override
	public void openCitation(){
		
		taxonLine="";
		naturenessLine="";
		sureness="";
		fieldsLine="";
		
		
	}
	
	@Override
	public void closeCitation(){
			
		if(naturenessLine.equals("")) naturenessLine="{"+XfloraTags.NATURENESS+"} Espontània";
		
		result=result+ 
				XFloraParser.getCitationInit()+sureness+
				taxonLine+"\n"+
				naturenessLine+"\n"+
				XFloraParser.getFieldsInit()+fieldsLine+"\n\n";

	}
	
	
	@Override
	public void createCitationField(String fieldName, String fieldLabel, String value,String category){
		
		if(fieldName.equals("OriginalTaxonName")){
			
			taxonLine=XFloraParser.createTaxonLine(value);
									
		}
		else if(fieldName.equals("Natureness")){
			
			naturenessLine="{"+XfloraTags.NATURENESS+"} "+value;
			
		}
		else if(fieldName.equals("CitationNotes")){
			
			addFieldValue(XFloraParser.getObservationComment(value));
			
		}
		else if(fieldName.equals("Sureness")){
			
			sureness=XFloraParser.getSurenessCod(value);
			
		}
		else if(fieldName.equals("ObservationAuthor")){
			
			addFieldValue(XFloraParser.getObservationAuthor(value));
			
		}
		else if(fieldName.equals("Locality")){
			
			addFieldValue(value);
			
		}
		else if(fieldName.equals("altitude")){
			
			addFieldValue(XFloraParser.getAltitude(value));
		}
		else{
			
			
		}
		
		
	}
	

	@Override
	public void writeCitationCoordinateLatLong(double latitude, double longitude) {


		
	}


	@Override
	public void writeCitationCoordinateUTM(String utmShortForm) {
	
		addFieldValue(XFloraParser.getUTMCod(utmShortForm.replace("_","")));

	}
	
	@Override
	public void writeCitationDate(String dateString) {
		
		SimpleDateFormat inFormat=new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
	    SimpleDateFormat outputFormatter = new SimpleDateFormat("dd/MM/yyyy");

		    try {
		        
		    	Date date = inFormat.parse(dateString);
		        String XfloraDate = outputFormatter.format(date);
		        
				addFieldValue(XFloraParser.getDateCod(XfloraDate));


		    } catch (ParseException e) {
		        e.printStackTrace();
		    }
				
	}


	@Override
	public void openDocument(){
		
		
		
	}
	
	private void addFieldValue(String value) {

		fieldsLine=fieldsLine+"    "+value;
		
	}
	
	@Override
	public void closeDocument(){
				
		setFormat(".txt");
		setResult(result);
		
	}
	

}
