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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import uni.projecte.dataLayer.CitationManager.CitationExporter;
import uni.projecte.dataTypes.FagusUtils;
import uni.projecte.dataTypes.ProjectField;
import uni.projecte.dataTypes.TaxonUtils;


public class FagusExporter extends CitationExporter {
	
	private FagusWriter fc;
	private boolean sheet=false;
	
	private ArrayList<ProjectField> geoFields;	  /* GEO */	
	private ArrayList<ProjectField> ecoFields;	  /* ECO */
	private ArrayList<ProjectField> structFields; /* STRUCT */
	private ArrayList<ProjectField> organism_attFields; /* ORGANISM_ATT */
	

	public FagusExporter(String projectName, String thesaurusName, String projectType) {
		
		super(projectName, thesaurusName, projectType);
		fc= new FagusWriter();
	
	}
	
	@Override
	public void openCitation(){
		
		geoFields=new ArrayList<ProjectField>();
		ecoFields=new ArrayList<ProjectField>();
		structFields=new ArrayList<ProjectField>();
		organism_attFields=new ArrayList<ProjectField>();
		
		fc.openCitation(sheet);
		
	}
	
	@Override
	public void closeCitation(){
	
		addSideData();
		
		sheet=false;
		fc.closeCitation();
		
	}
	
	

	@Override
	public void createCitationField(String attName, String label, String value,String category){
		
		if(attName.compareTo("OriginalTaxonName")==0 || attName.compareTo("OriginalTaxonNames")==0){
						
			fc.setTaxon(TaxonUtils.cleanTaxon(value));
			
		}
		else if(attName.compareTo("origin")==0){
			
			fc.writeCitation(value, "Botanical");
			
		}
		
		else if(attName.compareTo("CitationNotes")==0){
			
			fc.addComment(value);
			
		}
		else if(attName.compareTo("Accepted")==0){
			
			
			
		}
		else if(attName.compareTo("CorrectedTaxonName")==0){
			
			
			
		}
		else if(attName.compareTo("SecondaryCitationCoordinate")==0){
			
			
		}
		else if(attName.compareTo("ObservationAuthor")==0){
			
			fc.setAuthor(value);
			
		}
		else if(attName.compareTo("Natureness")==0){
			
			fc.setNatureness(FagusUtils.translateFagusNatures(baseContext, value));
			
		}
		else if(attName.compareTo("Phenology")==0){
			
			fc.setNatureness(FagusUtils.translateFagusPhenology(baseContext, value));
			
		}
		else if(attName.compareTo("Sureness")==0){
			
			fc.addTaxon(value);
			
		}
		else if(attName.compareTo("Locality")==0){
			
			geoFields.add(new ProjectField(attName.toLowerCase(), category, label, value));
			
		}
		else{
			
			if(value!=null && !value.equals("")) storeCitaionField(label, attName, value,category);
			//afegir al seu container {label, fieldName, value, isLast, category}
			
		}
		
		
		
	}
	
	private void storeCitaionField(String fieldLabel, String fieldName, String fieldValue, String category) {

		/* category field is mapped to description field */
		
		if(category.equals("ECO")){
			
			ecoFields.add(new ProjectField(fieldName, category, fieldLabel, fieldValue));
						
		}
		else if(category.equals("GEO")){
			
			geoFields.add(new ProjectField(fieldName.toLowerCase(), category, fieldLabel, fieldValue));

		}
		else if(category.equals("STRUCT")){
			
			structFields.add(new ProjectField(fieldName, category, fieldLabel, fieldValue));

		}
		else if(category.equals("ORGANISM_ATT")){
			
			organism_attFields.add(new ProjectField(fieldName, category, fieldLabel, fieldValue));
			
		}
		else if(category.equals("ADDED")){
			
			ecoFields.add(new ProjectField(fieldName, category, fieldLabel, fieldValue));
			
		}
		else{
			
			ecoFields.add(new ProjectField(fieldName, category, fieldLabel, fieldValue));
		
		}
		
		
	}
	
	private void addSideData() {

		createCategory("ECO", ecoFields);
		createCategory("GEO", geoFields);
		createCategory("STRUCT", structFields);
		createCategory("ORGANISM_ATT", organism_attFields);
		
	}
	
	private void createCategory(String category,ArrayList<ProjectField> fieldCategoryList){
		
		Iterator<ProjectField> listIt=fieldCategoryList.iterator();
		
		if(fieldCategoryList.size()>0){
		
			fc.createSideDataCategory(category);
						
			while(listIt.hasNext()){
				
				ProjectField projFieldTmp=listIt.next();
				fc.createSideData(projFieldTmp.getLabel(), projFieldTmp.getName(), projFieldTmp.getValue(),category);
				
			}
			
			fc.closeSideDataCategory();
		
		}
		
		
		
	}

	@Override
	public void writeCitationCoordinateLatLong(double latitude, double longitude) {
		
		if(latitude>90 || longitude>180){
    		
			fc.writeCitationCoordinate("");

    	}
		else fc.writeCitationCoordinate(latitude+", "+longitude);

	}


	@Override
	public void writeCitationCoordinateUTM(String utmShortForm) {

		fc.writeSecondaryCitationCoordinate(utmShortForm.replace("_", ""));

	}


	@Override
	public void writeCitationDate(String date) {
		
		fc.addDate(date);

		
	}
	
	@Override
	public void openDocument(){
		
		fc.openDocument();
		
		
	}
	
	@Override
	public void closeDocument(){
		
		fc.closeDocument();
		setFormat(".xml");
		setResult(fc.convertXML2String());
		
	}

	public void forceSpecimen(String value) {

		if(value.equals("true")) sheet=true;
		
	}
	

}
