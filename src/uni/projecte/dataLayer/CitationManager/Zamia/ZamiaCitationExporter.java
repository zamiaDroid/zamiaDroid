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

import org.xmlpull.v1.XmlSerializer;

import android.content.Context;
import uni.projecte.controler.BackupControler;
import uni.projecte.controler.CitationSecondLevelControler;
import uni.projecte.controler.PolygonControler;
import uni.projecte.dataLayer.CitationManager.CitationExporter;
import uni.projecte.dataLayer.CitationManager.Zamia.ZamiaCitationWriter;
import uni.projecte.dataLayer.ProjectManager.ZamiaProjectWriter;


public class ZamiaCitationExporter extends CitationExporter {

	private ZamiaCitationWriter zcW;
	private ZamiaProjectWriter zpW;
	
	private String citationValue;
	private CitationSecondLevelControler secSampCont;
	
	private boolean includeProjectStructure=true;
	
	
	
	public ZamiaCitationExporter(String projectName, String thesaurusName, String projectType) {
		
		super(projectName, thesaurusName, projectType);
		
		zcW=new ZamiaCitationWriter();
		
		includeProjectStructure=false;

				
		zcW.openDocument();
	
	}
	
	/*
	 * ZamiaCitation Exporter: 
	 * 
	 */
	public ZamiaCitationExporter(long projId,String projectName, String thesaurusName, String projectType,Context baseContext) {
		
		super(projectName, thesaurusName, projectType);
		
		zcW=new ZamiaCitationWriter();
		zpW=new ZamiaProjectWriter();

		
		includeProjectStructure=true;
		
		BackupControler back=new BackupControler(baseContext);
		XmlSerializer serializer=back.writeZamiaProject(projId, zpW,includeProjectStructure);
				
		zcW.openCitationList(serializer,zpW.getWriter());
		
	
	}
		
		
	
	@Override
	public void openCitation(){
		
		zcW.openCitation();
		
	}
	
	@Override
	public void closeCitation(){
	
		zcW.closeCitation();

	}
	
	public void createSecondLevel() {

		zcW.openSecondLevelCitations();
		
	}

	public void closeSecondLevel() {
		
		zcW.closeSecondLevelCitations();
		
	}
	
	public void createPolygon(){
		
		zcW.openPolygon();
	}
	
	public void closePolygon(){
		
		zcW.closePolygon();
		
	}
	
	
	@Override
	public void createCitationField(String attName, String label, String value,String category){
		
		
		zcW.createCitationField(label, attName, value, category);
		citationValue=value;
		

		
	}
	
	@Override
	public void closeCitationField(){
		
		zcW.closeCitationField();

		
	}
	
	@Override
	public void setFieldType(long fieldId, String type,Context c) {

		super.setFieldType(fieldId, type, c);

		if(type.equals("secondLevel")){
			
			CitationSecondLevelControler slC = new CitationSecondLevelControler(baseContext);
			slC.exportSubCitationsZamia(fieldId, citationValue, this);
			
		}
		else if(type.equals("polygon")){
			
			PolygonControler polygonCnt =new PolygonControler(baseContext);
			polygonCnt.exportSubCitationsZamia(fieldId, citationValue, this);
			
		}
		
		
	}
	
	public void createPolygonPoint(double latitude, double longitude, String date, String altitude) {
	
		zcW.createPolygonPoint(latitude,longitude,date,altitude);
		
	}
	


	@Override
	public void writeCitationCoordinateLatLong(double latitude, double longitude) {

		if(latitude>90 || longitude>180){
	    		
			zcW.writeCitationCoordinate("");
	
	    }
		else{
			
				zcW.writeCitationCoordinate(latitude+", "+longitude);
		}
			
		
	}


	@Override
	public void writeCitationCoordinateUTM(String utmShortForm) {

	
	}


	@Override
	public void writeCitationDate(String date) {
		
		zcW.addDate(date);
			
	}
	
	@Override
	public void openDocument(){
		
		
		
	}
	
	@Override
	public void closeDocument(){
		
		if(includeProjectStructure) zcW.closeCitationDocument();
		else zcW.closeDocument();
		
		setFormat(".xml");
		setResult(zcW.convertXML2String());
		
	}





}
