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

import java.util.HashMap;

import edu.ub.bio.biogeolib.CoordConverter;
import edu.ub.bio.biogeolib.CoordinateLatLon;
import edu.ub.bio.biogeolib.CoordinateUTM;
import edu.ub.bio.biogeolib.LatLonCoordsBox;
import android.content.Context;
import android.util.Log;
import uni.projecte.R;
import uni.projecte.controler.ProjectControler;
import uni.projecte.controler.CitationControler;
import uni.projecte.dataLayer.utils.StringUtils;
import uni.projecte.dataLayer.utils.UTMUtils;


public class FagusReader {

	
	protected CitationControler citCnt;
	protected ProjectControler projCnt;
	protected Context c;
	protected long projectId;
	protected long sampleId;
	protected int numSamples=0;
	protected boolean visible=true;
	protected boolean createFields=true;
	protected HashMap<String, Long> createdFields;
	
	

	public FagusReader(Context c, long projectId){
		
		this.c=c;
		projCnt=new ProjectControler(c);
		
		createdFields=projCnt.getProjectFieldsHash(projectId);

		citCnt=new CitationControler(c);
		citCnt.startTransaction();
		
		this.projectId=projectId;
		
		
	}
	
	
	public void startTransaction(){
		
		citCnt.startTransaction();
		
	}
	
	
	public void createNewSample(String origin){
		
		this.sampleId=citCnt.createEmptyCitation(projectId);
		String label = c.getString(R.string.origin);

		if(createdFields.get("Origin")==null){
		
			long fieldId=projCnt.createField(projectId,"Origin",label,"ECO","simple",visible);
			citCnt.addCitationFieldNoCheck(projectId,this.sampleId, fieldId,"Origin",origin);

		}
		
		numSamples++;

	}
	
	public int getNumSamples() {
		return numSamples;
	}
	

	public void createOriginalTaxonName(String taxonName, String sureness){
		
		String label = c.getString(R.string.OriginalTaxonName);

		if(createdFields.get("OriginalTaxonName")==null){ 
			
			long fieldId=projCnt.createField(projectId,"OriginalTaxonName",label,"A","thesaurus",visible);
			if(fieldId>0) createdFields.put("OriginalTaxonName", fieldId);

		}
		
		citCnt.addCitationFieldNoCheck(projectId,this.sampleId, createdFields.get("OriginalTaxonName"),"OriginalTaxonName",taxonName);
		
		label = c.getString(R.string.sureness);

		if(createdFields.get("Sureness")==null){ 
			
			long fieldId=projCnt.createField(projectId,"Sureness",label,"A","simple",visible);
			if(fieldId>0) createdFields.put("Sureness", fieldId);
			
		}
		
		citCnt.addCitationFieldNoCheck(projectId,this.sampleId, createdFields.get("Sureness"),"Sureness",sureness.trim());
		
	}
	
	public void createCorrectedTaxonName(String taxonName, String sureness){
		
		/*String label = c.getString(R.string.CorrectedTaxonName);

		if(numSamples<2) rC.createField(projectId,"CorrectedTaxonName",label,"A","thesaurus",visible);
		
		sC.addCitationField(projectId,this.sampleId, this.projectId,"CorrectedTaxonName",taxonName);
		
		
		label = c.getString(R.string.correctedSureness);

		if(numSamples<2) rC.createField(projectId,"correctedSureness",label,"A","simple",visible);
		
		sC.addCitationField(projectId,this.sampleId, this.projectId,"correctedSureness",sureness); */
		
	}
	

	public void createInformatisationDate(String day,String month, String year, String hours, String minutes,String seconds){
		
		//Date format: yyyy-MM-dd hh:mm:ss
		
		String date=year+"-"+month+"-"+day+" "+hours+":"+minutes+":"+seconds;
		
		String label = c.getString(R.string.InformatisationDate);
		
		if(numSamples<2) projCnt.createField(projectId,"InformatisationDate",label,"ECO","simple",visible);
		
		citCnt.addCitationField(projectId,this.sampleId, this.projectId, "InformatisationDate",date);
		
		
	}
	
	public void createObservationDate(String day,String month, String year, String hours, String minutes,String seconds){
		
		//Date format: yyyy-MM-dd hh:mm:ss
		
		String date=year+"-"+month+"-"+day+" "+hours+":"+minutes+":"+seconds;
		
		if(date.contains("null")) date=date.replace("null", "00");
		
		citCnt.updateCitationDate(this.sampleId,date);
		
	}


	public void createSecondaryCitationCoordinate(String location){
		
		/*String label = c.g				if(fieldId>0) createdFields.put(name, fieldId);
				etString(R.string.SecondaryCitationCoordinate);
		
		if(numSamples<2) rC.createField(projectId,"SecondaryCitationCoordinate",label,"ECO","simple",visible);
		
		sC.addCitationField(projectId,this.sampleId, this.projectId, "SecondaryCitationCoordinate",location);
		
		*/
		
		
	}
	
	public void createCitationCoordinate(String location){
		
		//empty location
		if(location.equals("")){
			
			citCnt.updateCitationLocation(this.sampleId,100, 190);
			
		}
		else if(UTMUtils.isLatLong(location)){
			
			location=location.replace(",", ".");
			String [] loc=location.split(" ");
			String lat=(String) loc[0].subSequence(0, loc[0].length()-1);
			
			citCnt.updateCitationLocation(this.sampleId,Double.valueOf(lat), Double.valueOf(loc[1]));
			
		}
		else{
			
			//Not working at all.....shhhhhhhhhhhh!			
			if(UTMUtils.isUTMCoordDigraph(location)){
				
				CoordinateLatLon latLong=CoordConverter.getInstance().toLatLon(new CoordinateUTM(true,31,location));
	            citCnt.updateCitationLocation(this.sampleId,latLong.getLat(), latLong.getLon());
				
			}
			//UTM: 31 429425 4635963
			else if(UTMUtils.isUTMCoordNum(location)){
				
				String[] spUTM=location.split(" ");
				
				CoordinateUTM coordUTM=new CoordinateUTM(true,Integer.valueOf(spUTM[0]),Double.valueOf(spUTM[1]),Double.valueOf(spUTM[2]));
				CoordinateLatLon latLong=CoordConverter.getInstance().toLatLon(coordUTM);
	            citCnt.updateCitationLocation(this.sampleId,latLong.getLat(), latLong.getLon());
	            
			}
			else{
				
				//not implemented
				
			}
	
			
		}
		
	}
	
	public void createDefaultFields(String tagName,String value){
		
		String label="";
		
		if(tagName.equals("LifeCycleStatus")){
			
			label=c.getString(R.string.LifeCycleStatus);
			
		}
		else if(tagName.equals("Natureness")){
			
			label=c.getString(R.string.Natureness);
			
		}
		else if(tagName.equals("Accepted")){
			
			label=c.getString(R.string.Accepted);
			
		}
		else if(tagName.equals("Informatiser")){
			
			label=c.getString(R.string.Informatiser);
			
		}
		else if(tagName.equals("ObservationAuthor")){
			
			label=c.getString(R.string.ObservationAuthor);			
			
		}
		else if(tagName.equals("biological_record_type")){
			
			label=c.getString(R.string.biological_record_type);
			
		}
		else if(tagName.equals("CitationNotes")){
			
			label=c.getString(R.string.CitationNotes);
			
		}

		//get Field and if it doesn't exists 
		if(!label.equals("")){
		
			if(!value.equals("") && createdFields.get(tagName)==null){
				
				long fieldId=projCnt.createField(projectId,tagName,label,"ECO","simple",visible);
				if(fieldId>0) createdFields.put(tagName, fieldId);

			}
			
			citCnt.addCitationFieldNoCheck(projectId,this.sampleId, createdFields.get(tagName), tagName, value);
		
		}
		
	}

	public void createDatumFields(String tempVal, String name, String label,String category) {
		
		//fixing whitespaces etc...fieldValue need to have chars or numbers
		if(tempVal.matches(".[a-zA-Z0-9]+.")){
		
			if(createdFields.get(name)==null) {
					
				long fieldId=-1;
					
				if(name.equals("photo")){
						
					fieldId=projCnt.createField(projectId,name,label,category,"photo",visible);

				}
				else{
						
					fieldId=projCnt.createField(projectId,name,label,category,"simple",visible);
					
				}
					
				Log.i("Citation","FagusImport: (Action)--> Creating field "+label+" | Id: "+fieldId);
					
				if(fieldId>0) createdFields.put(name, fieldId);
					
			}

			citCnt.addCitationFieldNoCheck(projectId,this.sampleId, createdFields.get(name), name, tempVal.trim());
		
		}
		
		
	}
	
	public void finishReader(){
		
		citCnt.EndTransaction();
		
	}


	
	/*
	 * 
	 * 	<string name="LifeCycleStatus">Estadi del cicle vital</string>
	<string name="Natureness">Natura</string>
	<string name="Accepted">Acceptat</string>
	<string name="Informatiser">Informatitzador</string>
	<string name="ObservationAuthor">Autor</string>
	<string name="biological_record_type">Botànica</string>
	<string name="CitationNotes">Comentaris</string>
	<string name="OriginalTaxonName">Nom Original</string>
	<string name="CorrectedTaxonName">Nom Corregit</string>
	<string name="sureness">Certesa</string>
	 * 
	 * 
	 * 
	 */
	

	
	/*
	 * 
	 *  <ObservationAuthor>David Martí Pino</ObservationAuthor>
    	<ObservationDate day="10" month="10" year="2010" />
    	<Informatiser>David Martí</Informatiser>
    	<InformatisationDate day="14" hours="9" mins="37" month="09" secs="16" year="2010" />
    	<LifeCycleStatus>Flowering and fructification</LifeCycleStatus>
    	<Natureness>Accidental</Natureness>
    	<Accepted>true</Accepted>
    	<CitationCoordinate code="42,12, 1,2" precision="1.0" type="UTM alphanum" units="1m" />
    	<SecondaryCitationCoordinate code="42,12, 1,2" precision="0.0" type="UTM num" units="1m" />
	 * 
	 * 
	 */
	
}
