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

import android.content.Context;
import uni.projecte.dataLayer.CitationManager.CitationExporter;


public class KMLExporter extends CitationExporter {
	
	public static final int PLACEMARK_POINT=1;
	public static final int PLACEMARK_POLYGON=2;
	
	public int PLACEMARK_TYPE = PLACEMARK_POINT;
		
	private KMLWriter fc;
	private String description="<h1>Fields</h1>";
	private String citationName="";
	private String date;
	private String coordinates="";
	
	private String lastValue="";
	
	

	public KMLExporter(String projectName, String thesaurusName, String projectType) {
		
		super(projectName, thesaurusName, projectType);
		
		fc= new KMLWriter();
	
	}
	
	@Override
	public void openCitation(){
		
		fc.openCitation();
		
	}
	
	@Override
	public void closeCitation(){
	
		switch (PLACEMARK_TYPE) {
		
			case PLACEMARK_POINT:
				
				fc.writeCitationCoordinatePoint(coordinates);
				
				break;
				
			case PLACEMARK_POLYGON:
				
				fc.writeCitationCoordinatePolygon(coordinates.replace("] [", "\n").replace("[","").replace("]",""));
				
				break;
	
			default:
				break;
		}
		
		if(citationName.equals("")) fc.closeCitation(date,description);
		else fc.closeCitation(citationName,description);
				
		description="<h1>Fields</h1>";
		citationName="";
		
	}
	
	@Override
	public void createCitationField(String attName, String label, String value,String category){
		
		if(attName.equals("OriginalTaxonName")) citationName=value;
		
		description=description+"<p><b>"+label+"</b> "+value+"</p>";
	
		lastValue=value;
		
	}
	
	@Override
	public void setFieldType(long fieldId, String type,Context c) {

		super.setFieldType(fieldId, type, c);

		if(type.equals("polygon")){ 
			
			PLACEMARK_TYPE=PLACEMARK_POLYGON;
			coordinates=lastValue;
		}
		
		
	}
	
	@Override
	public void writeCitationCoordinateLatLong(double latitude, double longitude) {
		
		if(latitude>90 || longitude>180) coordinates="";

		else coordinates=longitude+", "+latitude;

	}


	@Override
	public void writeCitationCoordinateUTM(String utmShortForm) {

	//	fc.writeSecondaryCitationCoordinate(utmShortForm);

	}


	@Override
	public void writeCitationDate(String date) {
		
		this.date=date;
		
	}
	
	@Override
	public void openDocument(){
		
		fc.openDocument();
		
		
	}
	
	@Override
	public void closeDocument(){
		

		
		
		fc.closeDocument();
		setFormat(".kml");
		setResult(fc.convertXML2String());
		
	}
	

}
