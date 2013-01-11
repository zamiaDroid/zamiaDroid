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

package uni.projecte.dataLayer.CitationManager;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;

import uni.projecte.controler.PreferencesControler;
import uni.projecte.dataTypes.ProjectField;
import android.content.Context;
import android.os.Environment;
import android.util.Log;

public abstract class CitationExporter {
	
	/* main project fields */
	
	private String projectName;
	private String thesaurusName;
	private String projectType;
	
	
	/* Small hack for Fagus export */
	
	private boolean last;
	
	/* output fields*/

	protected String result;
	protected String format;

	protected Context baseContext;
	protected HashMap<Long, ProjectField> projectFields;

	public CitationExporter(String projectName, String thesaurusName, String projectType) {
		
		this.projectName=projectName;
		this.thesaurusName=thesaurusName;
		this.projectType=projectType;
		
	}
	

	public void openCitation(){
		
		
	}
	
	public void closeCitation(){
		
		
	}
	
	public void createCitationField(String name, String label, String value,String category){
		
		
	}
	
	public void closeCitationField(){

	}
	
	public void stringToFile(String fileName, Context c){
		
		
		try {
			
		    File root = Environment.getExternalStorageDirectory();
		    PreferencesControler pC=new PreferencesControler(c);
		    
		    if (root.canWrite()){
		    	
		        File gpxfile = new File(Environment.getExternalStorageDirectory()+"/"+pC.getDefaultPath()+"/Citations/", fileName+format);
		      
		        FileWriter gpxwriter = new FileWriter(gpxfile);
		        BufferedWriter out = new BufferedWriter(gpxwriter);
		        out.write(result);
		        out.close();
		        
		    }
		} catch (IOException e) {
			
		    Log.e("File EXPORT", "Could not write file " + e.getMessage());
		}
		
		
		
	}
	
	public boolean isLast() {
		return last;
	}




	public void setLast(boolean last) {
		this.last = last;
	}


	public void writeCitationCoordinateLatLong(double double1, double double2) {

		
	}


	public void writeCitationCoordinateUTM(String shortForm) {

		
	}


	public void writeCitationDate(String string) {
		
	}
	
	public void setFormat(String format){
		
		this.format=format;
		
	}
	
	public void openDocument(){
		
		
	}
	
	public void closeDocument(){
		
		
	}
	
	
	
	public String getResult() {
		return result;
	}


	public void setResult(String result) {
		this.result = result;
	}


	public void setFieldType(long fieldId, String type, Context c) {

		this.baseContext=c;
		
	}


	public void setProjFieldsList(HashMap<Long, ProjectField> projectFields) {

		this.projectFields=projectFields;
						
		
	}


	public void writeCitationCoordinateXY(double x, double y) {
		// TODO Auto-generated method stub
		
	}


	public void createCitationSubField() {

		
		
	}
	
	

}
