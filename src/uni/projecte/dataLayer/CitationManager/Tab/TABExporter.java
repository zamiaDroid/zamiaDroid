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

package uni.projecte.dataLayer.CitationManager.Tab;

import java.util.HashMap;

import uni.projecte.dataLayer.CitationManager.CitationExporter;
import uni.projecte.dataTypes.ProjectField;


public class TABExporter extends CitationExporter {
	

	private String result;
	private String[] citation;

	
	/* Fixed Field */
	private static int COORDINATE_POS = 0;
	private static int UTM_POS = 1;
	private static int UTM_X = 2;
	private static int UTM_Y = 3;	
	private static int DATE_POS = 4;
	
	/* Maximum number of fields */
	private int fieldsCount;
	
	/* Field labels */
	private String[] citationLabels;
	private int lastField;
	private HashMap<String, Integer> insertedFieldsHash;
	
	
	
	public TABExporter(String projectName, String thesaurusName,String projectType) {
		
		super(projectName, thesaurusName, projectType);
		
		result="";
		
		insertedFieldsHash=new HashMap<String, Integer>();
	
	
	}
	

	@Override
	public void openCitation(){
		
		
		
	}
	
	@Override
	public void closeCitation(){
	
		result=result+citationToString();
		citation=new String[fieldsCount];

	}
	
	
	@Override
	public void createCitationField(String fieldName, String fieldLabel, String value,String category){
		
		int fieldPos=addNewCitationField(fieldName, fieldLabel);

		citation[fieldPos]=value;
		
	}
	


	@Override
	public void writeCitationCoordinateLatLong(double latitude, double longitude) {

		if(latitude>90 || longitude>180){
    		
			citation[COORDINATE_POS]=" ";

    	}
		else citation[COORDINATE_POS]=latitude+" "+longitude;

		
	}


	@Override
	public void writeCitationCoordinateUTM(String utmShortForm) {
	
		citation[UTM_POS]=utmShortForm;

	}
	
	public void writeCitationCoordinateXY(double x, double y) {
		
		citation[UTM_X]=String.valueOf((int)x);
		citation[UTM_Y]=String.valueOf((int)y);			
		
	}


	@Override
	public void writeCitationDate(String date) {
		
		citation[DATE_POS]=date;
		
	}
	
	@Override
	public void openDocument(){
		
		
		
	}
	
	public void setProjFieldsList(HashMap<Long, ProjectField> projectFields){
		
		fieldsCount=projectFields.size()+5;
		
		citation=new String[fieldsCount];
		citationLabels=new String[fieldsCount];
				
		setDefaultFields();
		
	}
	
	@Override
	public void closeDocument(){
		
		result=labelsToString()+result;		
		
		setFormat(".tab");
		setResult(result);
		
	}
	

	public void createCitationSubField() {

		
				
	}
	
	private String labelsToString() {

		String labelString="";
		
		for(int i=0; i<fieldsCount; i++){
		
			String citationLabel=citationLabels[i];
			
			if(citationLabel==null) citationLabel="";
			
			if(fieldsCount==i+1) labelString=labelString+citationLabels[i];
			else labelString=labelString+citationLabels[i]+"\t";
			
		}
		
		return labelString+"\n";
	}


	private String citationToString() {

		String citationValues="";
		
		for(int i=0; i<fieldsCount; i++){
			
			String citationVal=citation[i];
			
			if(citationVal==null)  citationVal="";
			
			citationVal=citationVal.replace("\t", " ");
			citationVal=citationVal.trim();
			
			if(fieldsCount==i+1) citationValues=citationValues+citationVal;
			else citationValues=citationValues+citationVal+"\t";
						
		}	
		
		return citationValues+"\n";
		
	}
	
	private int addNewCitationField(String attName, String label) {

		Integer pos=insertedFieldsHash.get(attName);
		
		if(pos==null){
			
			int fieldPos=lastField;
			
			insertedFieldsHash.put(attName, fieldPos);
			citationLabels[fieldPos]=label;
			
			lastField++;
			
			return fieldPos;
			
		}
			
		else return pos;	
		
	}
	
	private void setDefaultFields() {
		
		citationLabels[COORDINATE_POS]="CitationCoordinates";
		citationLabels[UTM_POS]="SecondaryCitationCoordinates";
		citationLabels[UTM_X]="X";
		citationLabels[UTM_Y]="Y";
		citationLabels[DATE_POS]="Date";
	
		lastField=5;

	}
	

}
