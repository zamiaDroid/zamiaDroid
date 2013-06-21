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


package uni.projecte.controler;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import edu.ub.bio.biogeolib.CoordConverter;
import edu.ub.bio.biogeolib.CoordinateLatLon;
import edu.ub.bio.biogeolib.CoordinateUTM;

import uni.projecte.dataLayer.CitationManager.Fagus.FagusExporter;
import uni.projecte.dataLayer.CitationManager.JSON.JSONExporter;
import uni.projecte.dataLayer.CitationManager.KML.KMLExporter;
import uni.projecte.dataLayer.CitationManager.Quercus.QuercusWriter;
import uni.projecte.dataLayer.CitationManager.Tab.TABExporter;
import uni.projecte.dataLayer.CitationManager.Zamia.ZamiaCitationExporter;
import uni.projecte.dataLayer.bd.CitacionDbAdapter;
import uni.projecte.dataLayer.bd.ProjectDbAdapter;
import uni.projecte.dataLayer.bd.SampleDbAdapter;
import uni.projecte.dataLayer.bd.SecondLevelCitacionDbAdapter;
import uni.projecte.dataTypes.CitationPhoto;
import uni.projecte.dataTypes.ProjectField;
import uni.projecte.maps.utils.CoordinateUtils;
import android.content.Context;
import android.database.Cursor;
import android.os.Handler;
import android.util.Log;



public class CitationSecondLevelControler extends CitationControler {
	

	private SecondLevelCitacionDbAdapter mDbAttributes;
	
	private ArrayList<String> taxonList;

	

	public CitationSecondLevelControler(Context context) {
		super(context);
		
		this.baseContext=context;
		

	}
	
	/*
	 * This method stores a citation with latitude, longitude provided for a concrete secondLevelField
	 * 
	 */
	
	
	public long createCitation(String secondLevelFieldId, double latPoint, double longPoint, String comment,long projId, String subFieldType,long parentCitationId){
		
		SecondLevelCitacionDbAdapter mDbSample=new SecondLevelCitacionDbAdapter(baseContext);
		mDbSample.open();
			
		long idSample=  mDbSample.createCitation(secondLevelFieldId, latPoint, longPoint,projId,subFieldType,parentCitationId);
			
		mDbSample.close();
		
		return idSample;		
		
	}
	
	/*
	 * This methods remove all citations related to a @secondLevelId 
	 * and returns the number of deleted citations.
	 * 
	 */
	
	public int removeCitationsBySLId(String secondLevelFieldId){
		
		SecondLevelCitacionDbAdapter mDbSample=new SecondLevelCitacionDbAdapter(baseContext);
		mDbSample.open();
			
		Cursor citationList=mDbSample.fetchSecondLevelValuesGrid(secondLevelFieldId);		
		citationList.moveToFirst();
		
		int elements=citationList.getCount();
		
		while(!citationList.isAfterLast()){
			
			long citationId=citationList.getLong(0);
			
			mDbSample.deleteCitationFields(citationId);
			mDbSample.deleteCitation(citationId);
			
			citationList.moveToNext();
			
		}
			
		mDbSample.close();
		
		return elements;		
		
	}
	
	/* 
	 * 
	 * Method used by Sampling Activity
	 * 
	 * It's required to start and finish DB transaction
	 * 
	 * 
	 * */
	
	@Override
	public long addCitationField(long fieldId,long idSample, long idRs,String attName, String value){
		
		
		long attId;
		long citationValueId=-1;
		
		ProjectDbAdapter aTypes=new ProjectDbAdapter(baseContext);
		aTypes.open();
		

		Cursor att= aTypes.fetchSecondLevelFieldFromProject(fieldId, attName);
		
		att.moveToFirst();
		
		//attExists
		
		if(att.getCount()>0){ 
			
			attId=att.getLong(0);
		
			citationValueId=mDbAttributes.createCitationField(idSample,attId,value,attName);  

			
		}
		
		//we have to create a new Attribute
		else{
			
			//aTypes.createAttribute(idRs, attName, label, desc, value, type, cat);
			
			
		} 
		
		att.close();		
		aTypes.close();
	
		return citationValueId;
		
	}
	
	
	
	@Override
	public void startTransaction() {

		mDbAttributes = new SecondLevelCitacionDbAdapter(baseContext);
		mDbAttributes.open();

		mDbAttributes.startTransaction();
		
		
	}

	@Override
	public void EndTransaction() {
		
		mDbAttributes.endTransaction();
		mDbAttributes.close();
		
	}
	
	public Cursor getFieldValuesBySLId(String secondLevelFieldId){
		
		SecondLevelCitacionDbAdapter mDbSample=new SecondLevelCitacionDbAdapter(baseContext);
		mDbSample.open();
			
			Cursor list=  mDbSample.fetchSecondLevelValuesGrid(secondLevelFieldId);

		mDbSample.close();

		return list;
		
		
	}
	
	public String getMultiPhotosValues(String secondLevelFieldId){
		
		String multiPhotoValue="";
		
		SecondLevelCitacionDbAdapter mDbSample=new SecondLevelCitacionDbAdapter(baseContext);
		mDbSample.open();
			
			Cursor list=  mDbSample.fetchMultiPhotoValues(secondLevelFieldId);
			list.moveToFirst();
			
			if(list!=null || list.getCount()>0){
				
				if(list.getColumnCount()>3) multiPhotoValue=list.getString(3);
				
			}
			
		list.close();	
			
		mDbSample.close();

		return multiPhotoValue;
		
		
	}
	
	public CitationPhoto getMultiPhotoByValue(String photoValue){
		
		CitationPhoto citationPhoto=null;
				
		SecondLevelCitacionDbAdapter mDbSample=new SecondLevelCitacionDbAdapter(baseContext);
		mDbSample.open();
			
			Cursor list= mDbSample.getMultiPhotoByValue(photoValue);
			list.moveToFirst();
			
			if(list!=null && list.getCount()>0){
				
				citationPhoto= new CitationPhoto(photoValue, list.getLong(2), list.getLong(1), "multiPhoto");
				
			}
			
		list.close();	
			
		mDbSample.close();

		return citationPhoto;
		
		
	}
	

	/*
	 * 
	 * Method used by CitationEditor
	 * 
	 * It requires a started transaction
	 * 
	 */
	
	public void updateCitationField(long sampleId, long idAtt, String newValue) {
		
		mDbAttributes.updateSampleFieldValue(sampleId, idAtt, newValue);

	}
	

	/*  
	 * Concrete Quercus exporter
	 * It creates a list of releveés which their entries and a list of taxons
	 * Only with the citations included in the @citationSet
	 * 
	 */

	
	public int exportProjectQuercus(long projId, Context co,Set<Long> citationSet, String fileName, String exportFormat, Handler handlerExportProcessDialog){
		
		Log.d("Citacions","Exportant Citacions Start "+exportFormat);
		
		ProjectSecondLevelControler rC= new ProjectSecondLevelControler(co);
		
		CitationControler sC=new CitationControler(co);
		
		QuercusWriter qW= new QuercusWriter();
		
		/* ArrayList that maintains a list of taxons with each own substrat level */ 
		taxonList= new ArrayList<String>();
		
		//checking: only one secondLevelField and secondLevel has a field called OriginalTaxonName
		long secondLevelFieldId=rC.isQuercusExportable(projId);

		
		if (secondLevelFieldId>0){
			
			// iterate over each field Value
			
			qW.openDocument(fileName);
			
			
			/* iterating over each citation */ 
			
			Iterator<Long> itCitations=citationSet.iterator();
			
			
			while (itCitations.hasNext()){
				
				handlerExportProcessDialog.sendMessage(handlerExportProcessDialog.obtainMessage());

				
				long citationId=itCitations.next();
				
				Cursor secondLevelfieldList=getCitationFromFieldAndCitationId(citationId,secondLevelFieldId);
					
				String secLevId=secondLevelfieldList.getString(3);
				
				sC.loadCitation(citationId);
				
				qW.openReleve(secLevId, "BB");			
				qW.addDate(sC.getDate());
				
				if(CoordinateUtils.isCorrectCoordinate(sC.latitude, sC.longitude)){ 
					
					qW.writeReleveCoordinate(sC.latitude+", "+sC.longitude);
					
					CoordinateUTM utm = CoordConverter.getInstance().toUTM(new CoordinateLatLon(sC.latitude,sC.longitude));
					qW.writeSecondaryCitationCoordinate(utm.getShortForm().replace("_",""));
				
				}
			    
				Cursor subProjItems=getFieldValuesBySLId(secLevId);
				
				
				//iterating over secondLevelCitationFields
				  				   		   
				  Cursor fieldList=rC.getProjectFieldsCursor(secondLevelFieldId);
				  fieldList.moveToFirst();
				  
			    //value, layer, sureness, comment
			  
			    while(subProjItems.isAfterLast() == false){
			    
			    	String attributes=subProjItems.getString(5);
			    	String[] splittedFields=attributes.split(":");
			    	
			    	//check fieldNames
			    	
			    	createReleveEntry(qW,splittedFields,fieldList);
			       
			    	fieldList.moveToFirst();
			    	
			    	subProjItems.moveToNext();
			     
			       
			    	
			    }
			    
			    addCitationFields(projId,citationId,rC,sC,qW);
			    
			    fieldList.close();
				
				qW.closeReleve();

				secondLevelfieldList.moveToNext();
				
			}		
			
			
			addTaxonList(qW);	

			qW.closeDocument();
			
			String returnS=qW.convertXML2String();
			
			Log.i("Citations","Export "+returnS);
			
			qW.stringToFile(fileName, co);
			
			
		}
		else{
			
			//project can't be exported
			
		}
	
		
		Log.d("Citacions","Exportant Citacions End "+exportFormat);

		
		
		return 0;
		
	}
	
	
	
	/*  
	 * Concrete Quercus exporter
	 * It creates a list of releveés which their entries and a list of taxons
	 * 
	 */

	
	@Override
	public int exportProject(long projId, Context co,String fileName, String exportFormat){
		
		Log.d("Citacions","Exportant Citacions Start "+exportFormat);
		
		ProjectSecondLevelControler rC= new ProjectSecondLevelControler(co);
		
		CitationControler sC=new CitationControler(co);
		
		QuercusWriter qW= new QuercusWriter();
		
		/* ArrayList that maintains a list of taxons with each own substrat level */ 
		taxonList= new ArrayList<String>();
		
		//checking: only one secondLevelField and secondLevel has a field called OriginalTaxonName
		long secondLevelFieldId=rC.isQuercusExportable(projId);

		
		if (secondLevelFieldId>0){
			
			// iterate over each field Value
			
			qW.openDocument(fileName);
			
			
			/* iterating over each citation */ 
			Cursor secondLevelfieldList=getCitationValuesFromField(secondLevelFieldId);			
			int n= secondLevelfieldList.getCount();
		
			
			for (int i=0; i<n; i++){
				
				String secLevId=secondLevelfieldList.getString(3);
				long citationId=secondLevelfieldList.getLong(1);
				
				sC.loadCitation(citationId);
				
				qW.openReleve(secLevId, "BB");			
				qW.addDate(sC.getDate());
			    
			    
				Cursor subProjItems=getFieldValuesBySLId(secLevId);
				
				
				//iterating over secondLevelCitationFields
				  				   		   
				  Cursor fieldList=rC.getProjectFieldsCursor(secondLevelFieldId);
				  fieldList.moveToFirst();
				  
			    //value, layer, sureness, comment
				  
				  
			    while(subProjItems.isAfterLast() == false){
			    
			    	String attributes=subProjItems.getString(5);
			    	String[] splittedFields=attributes.split(":");
			    	
			    	//check fieldNames
			    	
			    	createReleveEntry(qW,splittedFields,fieldList);
			       
			    	fieldList.moveToFirst();
			    	
			    	subProjItems.moveToNext();
			     
			       
			    	
			    }
			    
			    addCitationFields(projId,citationId,rC,sC,qW);
			    
			    fieldList.close();
				
				qW.closeReleve();

				secondLevelfieldList.moveToNext();
				
			}		
			
			addTaxonList(qW);	

			qW.closeDocument();
			
			String returnS=qW.convertXML2String();
			
			Log.i("Citations","Export "+returnS);
			
			qW.stringToFile(fileName, co);
			
			
		}
		else{
			
			//project can't be exported
			
		}
	
		
		Log.d("Citacions","Exportant Citacions End "+exportFormat);

		
		
		return 0;
		
	}
	
	
	public int exportSubCitationsZamia(long fieldId, String subProjId, ZamiaCitationExporter zce){
		
		
		ProjectSecondLevelControler rsC=new ProjectSecondLevelControler(baseContext);
		SecondLevelCitacionDbAdapter slCitations= new SecondLevelCitacionDbAdapter(baseContext);
		slCitations.open();

		HashMap<Long, ProjectField> projFieldList=rsC.getProjectFieldsMap(fieldId);
		
		Cursor citations=this.getFieldValuesBySLId(subProjId);
		citations.moveToFirst();
		
		setcExporter(zce);
		
		zce.createSecondLevel();
		
		
		while(!citations.isAfterLast()){
			
			Log.i("ZC Export","F: "+fieldId+" SubProjId: "+subProjId);
			
	
			exportCitation(citations, projFieldList);
			
			citations.moveToNext();
		
		}
			
		zce.closeSecondLevel();
		
		citations.close();
		slCitations.close();

		
		return -1;
		
		
	}
	
	/*
	 * 
	 * 
	 *  
	 */
	
	public int exportSubCitations(long projId, Set<Long> citationSet){
		
		
		ProjectSecondLevelControler slP= new ProjectSecondLevelControler(baseContext);

		SecondLevelCitacionDbAdapter slCitationsDB= new SecondLevelCitacionDbAdapter(baseContext);
		slCitationsDB.open();

		//list of project's secondLevelFields
		Cursor secondLevelFields=slP.getSecondLevelFieldsByProjId(projId);
		
		
		while(!secondLevelFields.isAfterLast()){
			
			//getID
			long fieldId=secondLevelFields.getLong(0);

			Iterator<Long> itCitations=citationSet.iterator();
			
					
			while(itCitations.hasNext()){
				
				Long citationId=itCitations.next();
				
				Cursor citationsSL=this.getCitationFromFieldAndCitationId(citationId,fieldId);
				
				String subFieldId=citationsSL.getString(3);
				
				exportProject(subFieldId,fieldId,baseContext, subFieldId, "TAB");
								
				citationsSL.close();

			}
			
			
			secondLevelFields.moveToNext();
			
		}
		
		secondLevelFields.close();
		
		slCitationsDB.close();
		
		return -1;
		
		
	}
	
	
	public int exportProject(String secondLevelFieldId, long fieldId, Context co,String fileName, String exportFormat){
		
			
		ProjectSecondLevelControler rC= new ProjectSecondLevelControler(co);
		CitationSecondLevelControler sC= new CitationSecondLevelControler(co);
	
		Cursor citations= sC.getFieldValuesBySLId(secondLevelFieldId);
		KEY_DATA=citations.getColumnIndex(SampleDbAdapter.DATE);
		
		int n= citations.getCount();
		
		rC.loadProjectInfoById(fieldId);
		
		//Depending on the chosen type of file we'll instantiate the concrete exporter subclass
		
		if(exportFormat.equals("Fagus")){
			
			cExporter=new FagusExporter(rC.getName(),rC.getThName(),rC.getCitationType());
			
		}
		else if (exportFormat.equals("TAB")){
			
			cExporter=new TABExporter(rC.getName(),rC.getThName(),rC.getCitationType());

		}
		else if (exportFormat.equals("JSON")){
			
			cExporter=new JSONExporter(rC.getName(),rC.getThName(),rC.getCitationType());

		}
		else if (exportFormat.equals("KML")){
			
			cExporter=new KMLExporter(rC.getName(),rC.getThName(),rC.getCitationType());

		}
		
		cExporter.openDocument();

		
		//c= list of types
		HashMap<Long, ProjectField> projectFields=rC.getProjectFieldsMap(fieldId);

		Log.d("Citacions","Exportant Citacions Start "+exportFormat);
				
		for (int i=0; i<n; i++){
			
			Log.d("Citacions","Citacio "+i);

			exportCitation(citations, projectFields);
			cExporter.setLast(false); 
			citations.moveToNext();

		}
		
		Log.d("Citacions","Exportant Citacions End "+exportFormat);

		cExporter.closeDocument();
		
		fileName=fileName.replace(":", "_");
		
		cExporter.stringToFile(fileName,baseContext);
		
		
		return 0;
		
	}
	
	@Override
	public String getFieldValueNoTrans(long sampleId,long attId){

		
		SecondLevelCitacionDbAdapter mDbAttributes = new SecondLevelCitacionDbAdapter(baseContext);
		mDbAttributes.open();
		
		Cursor c=mDbAttributes.fetchSampleAttributeBySampleAttId(sampleId,attId);
		c.moveToFirst();
		
		String value;
	
		if(c.getCount()>0) value=c.getString(3);
		else value="";
		
		c.close();
		mDbAttributes.close();
		
		
		return value;
		
		
	}

	
	/*
	 * 
	 * Create a ReleveEntry for Quercus for a concrete subField instance
	 * 
	 * @splittedFields: has a list of FieldValues
	 * @fieldList Iterator over ProjectFields
	 * 
	 */


	private void createReleveEntry(QuercusWriter qW, String[] splittedFields, Cursor fieldList) {

		String releveComment="";
		String releveSureness="";
		String releveLayer="";
		String releveTaxon="";
		String releveValue="";
		
		int i=0;
		
		
		while(!fieldList.isAfterLast()){
			

			String fieldName=fieldList.getString(2);
			
			if(fieldName.equals("OriginalTaxonName")){
				
				if(splittedFields[i] != null) releveTaxon=splittedFields[i];
				
			}
			else if(fieldName.equals("value")){
				
				if(splittedFields[i] != null) releveValue=splittedFields[i];

				
			}
			else if(fieldName.equals("layer")){
				
				if(splittedFields[i] != null){ 
					
					releveLayer=splittedFields[i];
					
					String layer=releveLayer.substring(0,1);
					
					if(layer.equals("0")){
						
						
						releveLayer="";
						
					}
					
					else{
						
						releveLayer=layer;
						
					}

				}
				
				
				

				
			}
			else if(fieldName.equals("sureness")){
				
				if(i < splittedFields.length) releveSureness=splittedFields[i];

				
			}
			else if(fieldName.equals("comments")){
				
				if(i < splittedFields.length) releveComment=splittedFields[i];

			}
			
			fieldList.moveToNext();
			
			i++;
			
		}
		
	
		qW.addReleveEntry(releveTaxon,releveValue,releveSureness,releveLayer,releveComment);

		taxonList.add(releveTaxon+":"+releveLayer);

		
	}

	private void addTaxonList(QuercusWriter qW) {

		for (Object y : GetUniqueValues(taxonList)){

			String[] splitted=((String)y).split(":");
			String taxon=splitted[0];
			String level="";
			
			if(splitted.length>1) level=splitted[1];
			
			qW.createReleveTableEntry(taxon,level);
			
		}
		
	}


	private void addCitationFields(long projId,long citationId, ProjectSecondLevelControler rC, CitationControler sC, QuercusWriter qW) {

		boolean last=false;
		
		String authorValue="";
		boolean authorLast=false;
		
		//Cursor fieldList
		//KEY_ROWID, PROJ_ID,PROJ_NAME,TYPE,LABEL,PREVALUE,DESC,CAT
		
		
		///KEY_ROWID,KEY_SAMPLE_ID, KEY_TIPUS_ATRIB,VALUE,FIELD_NAME
		Cursor fieldValuesList=sC.getCitationsValuesFromCitationId(citationId);
		
		while(!fieldValuesList.isAfterLast()){
		
			String fieldName=fieldValuesList.getString(4);
		
	
				Cursor element=rC.getFieldCursorByName(projId, fieldName);
				element.moveToFirst();
				
				
				if(fieldValuesList.isLast()) last=true;
				
				if(fieldName.equals("ObservationAuthor")){
					
					authorValue=fieldValuesList.getString(3);
					
					if(last) authorLast=true;
					
				}
				else{
					
					String type=element.getString(3);
					
					Log.d("Export",fieldName+" : "+fieldValuesList.getString(3));
					
					if(!type.equals("secondLevel")){ 
						
						createCitationField(qW,fieldName, element.getString(4), fieldValuesList.getString(3), element.getString(7),last);
					}
				}
				
			
				element.close();
				
				fieldValuesList.moveToNext();
			

		}
		
		
			if(!authorValue.equals("")) createCitationField(qW,"ObservationAuthor","", authorValue, "", authorLast);
		
	}
	
	
	/**
	 * 
	 * Method that creates the QuercusField depending on:
	 * @param qW
	 * @param attName
	 * @param label
	 * @param value
	 * @param category
	 * @param last
	 * 
	 *
	 */
	
	
	public void createCitationField(QuercusWriter qW,String attName, String label, String value,String category,boolean last){
		
		
		
		if(attName.compareTo("OriginalTaxonName")==0){
			
			//qW.setTaxon(value);
			
		}
		else if(attName.compareTo("origin")==0){
			
			//qW.writeCitation(value, "Botanical");
			
		}
		
		else if(attName.compareTo("OriginalSyntaxonName")==0){
			
			qW.addReleveSintaxon(value);
			
		}
		else if(attName.compareTo("ReleveComments")==0){
			
			qW.addReleveComment(value);

			
		}
		else if(attName.compareTo("PlotArea")==0){
			
			qW.addReleveArea(value);
			
		}
		else if(attName.compareTo("ObservationAuthor")==0){
			
			qW.writeAuthor(value,last);
			
		}
		else if(attName.compareTo("PlotForm")==0){
			
			
		}		
		else{

			qW.createSideData(label, attName, value,last,category);
			
		}
		

	}
	
	
	/** Methods invocated by Fagus Reader **/
	
	public long createEmptyCitation(String secondFieldId,long projId, String subFieldType,long parentId){
		
		
		this.sampleId= mDbAttributes.createEmptyCitation(secondFieldId,projId, subFieldType,parentId);
		
		return sampleId;		
		
	}
	

	@Override
	public boolean updateCitationDate(long citationId,String date){

		//citationExists = mDbAttributes.checkRepeated(this.sampleId, latitude, longitude, date);				
	//	if(!citationExists) 
			
			mDbAttributes.updateDate(citationId, date);

		this.latitude=0;
		this.longitude=0;
		
		return false;
		
	}
	
	
	@Override
	public void updateCitationLocation(long citationId,double lat, double longitude){
		
		this.latitude=lat;
		this.longitude=longitude;
		
		mDbAttributes.updateLocation(citationId, lat, longitude);


	}

	public void deleteAllCitationsFromProject(String projectName,long[] slIds) {
				
		//obtenir els valors dels camps secondaris
		//esborrar mostres amb el idDelsNoms secundaris
		
		CitacionDbAdapter cAdapt = new CitacionDbAdapter(baseContext);
		SecondLevelCitacionDbAdapter slcAdapt= new SecondLevelCitacionDbAdapter(baseContext);
		slcAdapt.open();
		
		cAdapt.open();
		
		int n=slIds.length;
		
		
		//iteration over all secondLevelFieldsIDs
		for(int i=0;i<n;i++){
			
			
			//list of all secondLevelIdentifiers
			Cursor citationFields=cAdapt.fetchSamplesByFieldId(slIds[i]);
			citationFields.moveToFirst();
			
			
			while(!citationFields.isAfterLast()){
			
				String secondLevelId=citationFields.getString(3);
				
				Cursor sCitationsList=slcAdapt.fetchCitationBySubProjIdValue(secondLevelId);
				sCitationsList.moveToFirst();
				
				while(!sCitationsList.isAfterLast()){
					
					long citationId=sCitationsList.getLong(0);
					
					slcAdapt.deleteCitation(citationId);
					slcAdapt.deleteCitationFields(citationId);
					
					sCitationsList.moveToNext();
				}
				
				sCitationsList.close();
				
				citationFields.moveToNext();
			
			}
			
			citationFields.close();
		}
		
		
		cAdapt.close();
		slcAdapt.close();

		
	}
	
	
	private static Collection<String> Union(Collection<String> coll1, Collection<String> coll2)
	{
	    Set<String> union = new HashSet<String>(coll1);
	    union.addAll(new HashSet<String>(coll2));
	    return new ArrayList<String>(union);
	}
	 
	@SuppressWarnings("rawtypes")
	private static ArrayList<?> GetUniqueValues(Collection<String> values)
	{
	    return (ArrayList<?>)Union(values, values);
	}

	public void deleteCitationFromProject(long idSample, long[] slIds) {

		//obtenir els valors dels camps secondaris
		//esborrar mostres amb el idDelsNoms secundaris
		
		CitacionDbAdapter cAdapt = new CitacionDbAdapter(baseContext);
		SecondLevelCitacionDbAdapter slcAdapt= new SecondLevelCitacionDbAdapter(baseContext);
		slcAdapt.open();
		
		cAdapt.open();
		
		int n=slIds.length;
		
		
		//iteration over all secondLevelFieldsIDs
		for(int i=0;i<n;i++){
			
			
			//list of all secondLevelIdentifiers
			Cursor citationFields=cAdapt.fetchSamplesByFieldIdAndCitationId(idSample,slIds[i]);
			citationFields.moveToFirst();
			
			
			while(!citationFields.isAfterLast()){
			
				String secondLevelId=citationFields.getString(3);
				
				Cursor sCitationsList=slcAdapt.fetchCitationBySubProjIdValue(secondLevelId);
				sCitationsList.moveToFirst();
				
				while(!sCitationsList.isAfterLast()){
					
					long citationId=sCitationsList.getLong(0);
					
					slcAdapt.deleteCitation(citationId);
					slcAdapt.deleteCitationFields(citationId);
					
					sCitationsList.moveToNext();
				}
				
				sCitationsList.close();
				
				citationFields.moveToNext();
			
			}
			
			citationFields.close();
		}
		
		
		cAdapt.close();
		slcAdapt.close();
		
		
		
	}

	public boolean removeMultiPhoto(String imagePath) {
		
		
		mDbAttributes = new SecondLevelCitacionDbAdapter(baseContext);
		mDbAttributes.open();
		
			Cursor c=mDbAttributes.fetchCitationIdByMultiPhoto(imagePath);
			c.moveToFirst();
			
			if(c!=null && c.getCount()>0){
				
				long subCitId=c.getLong(1);
				
				long subCitFieldId=c.getLong(0);
					
				
				c.close();
				
				
				boolean result= mDbAttributes.deleteCitation(subCitId);
				result= mDbAttributes.deleteCitationFields(subCitFieldId);
				
				
				mDbAttributes.close();

				return result;
				
			}
			else{
				
				mDbAttributes.close();

				return false;
			}
			
			
		
	}

	public Cursor getPolygonPoints(long projId) {

		mDbAttributes = new SecondLevelCitacionDbAdapter(baseContext);
		mDbAttributes.open();
		
		Cursor polygons=mDbAttributes.getPolygonPointByProjectId(projId);
		
		mDbAttributes.close();
		
		return polygons;
		
	}

	public Cursor getPolygonPoints(long projId, Long parentId) {
		
		mDbAttributes = new SecondLevelCitacionDbAdapter(baseContext);
		mDbAttributes.open();
		
		Cursor polygons=mDbAttributes.getPolygonPointByParentId(projId,parentId);
		
		mDbAttributes.close();
		
		return polygons;
		
		
		
		
	}


}
