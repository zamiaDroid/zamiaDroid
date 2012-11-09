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

import uni.projecte.dataLayer.CitationManager.CitationExporter;
import uni.projecte.dataLayer.CitationManager.Quercus.QuercusWriter;


public class QuercusExporter extends CitationExporter {
	
	private QuercusWriter qExporter;

	public QuercusExporter(String projectName, String thesaurusName, String projectType) {
		
		super(projectName, thesaurusName, projectType);
		
		qExporter= new QuercusWriter();
	
	
	}
	
	@Override
	public void openCitation(){
		
		//
		
	}
	
	@Override
	public void closeCitation(){
	
	//	fc.closeCitation();
		
	}
	
	@Override
	public void createCitationField(String attName, String label, String value,String category){
		
		if(attName.compareTo("OriginalTaxonName")==0){
			
			//qExporter.setTaxon(value);
			
		}
		else if(attName.compareTo("origin")==0){
			
		//	fc.writeCitation(value, "Botanical");
			
		}
		
		else if(attName.compareTo("CitationNotes")==0){
			
		//	fc.addComment(value);
			
		}
		else if(attName.compareTo("ObservationAuthor")==0){
			
			//qExporter.writeAuthor(value);
			
		}
		
		else if(attName.compareTo("Sureness")==0){
			
			//fc.addTaxon(value);
			
		}
		else{
			

			qExporter.createSideData(label, attName, value,isLast(),category);

			
		}
		
		
		
	}
	
	@Override
	public void writeCitationCoordinateLatLong(double latitude, double longitude) {
		
		if(latitude>90 || longitude>180){
    		
			//fc.writeCitationCoordinate("");

    	}
		//else fc.writeCitationCoordinate(latitude+", "+longitude);

	}


	@Override
	public void writeCitationCoordinateUTM(String utmShortForm) {

		qExporter.writeSecondaryCitationCoordinate(utmShortForm);

	}


	@Override
	public void writeCitationDate(String date) {
		
		qExporter.addDate(date);

		
	}
	
	@Override
	public void openDocument(){
		
		qExporter.openDocument("lolo");
		
		
	}
	
	@Override
	public void closeDocument(){
		
		qExporter.closeDocument();
		setFormat(".xml");
		setResult(qExporter.convertXML2String());
		
	}
	

}
