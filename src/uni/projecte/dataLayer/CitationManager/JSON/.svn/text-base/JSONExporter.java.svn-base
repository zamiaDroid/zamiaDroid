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


package uni.projecte.dataLayer.CitationManager.JSON;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import uni.projecte.dataLayer.CitationManager.CitationExporter;


public class JSONExporter extends CitationExporter {


	private JSONArray citationsList;
	private JSONObject sample;
	private JSONArray fieldList;
	
	
	public JSONExporter(String projectName, String thesaurusName,
			String projectType) {
		
		super(projectName, thesaurusName, projectType);
		
		citationsList=new JSONArray();
	
	}
	
	@Override
	public void openCitation(){
		
		sample=new JSONObject();
		fieldList=new JSONArray();
		
	}
	
	@Override
	public void closeCitation(){
	
		try {
			
			sample.put("fields", fieldList);
			citationsList.put(sample);
			
		} catch (JSONException e) {
			
			e.printStackTrace();
			
		}

	}
	
	@Override
	public void createCitationField(String attName, String label, String value,String category){
		
		JSONObject field=new JSONObject();
		
		try {
			
			field.put(label, value);
			fieldList.put(field);
			
		} catch (JSONException e) {

			e.printStackTrace();
			
		}

		
	}
	
	@Override
	public void writeCitationCoordinateLatLong(double latitude, double longitude) {

		
		try {
			
			sample.put("X", latitude);
			sample.put("Y", longitude);
			
		} catch (JSONException e) {
			
			e.printStackTrace();
			
		}
		
	}


	@Override
	public void writeCitationCoordinateUTM(String utmShortForm) {

	
	}


	@Override
	public void writeCitationDate(String date) {
		
		try {
			
			sample.put("date", date);
			
		} catch (JSONException e) {

			e.printStackTrace();
		}

		
	}
	
	@Override
	public void openDocument(){
		
		
		
	}
	
	@Override
	public void closeDocument(){
		
		setFormat(".json");
		setResult(citationsList.toString());
		
	}
	

}
