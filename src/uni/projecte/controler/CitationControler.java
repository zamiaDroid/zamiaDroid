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


import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

import uni.projecte.R;
import uni.projecte.dataLayer.CitationManager.CitationExporter;
import uni.projecte.dataLayer.CitationManager.Fagus.FagusExporter;
import uni.projecte.dataLayer.CitationManager.JSON.JSONExporter;
import uni.projecte.dataLayer.CitationManager.KML.KMLExporter;
import uni.projecte.dataLayer.CitationManager.Tab.TABExporter;
import uni.projecte.dataLayer.CitationManager.Xflora.XfloraExporter;
import uni.projecte.dataLayer.CitationManager.Zamia.ZamiaCitationExporter;
import uni.projecte.dataLayer.CitationManager.objects.Citation;
import uni.projecte.dataLayer.bd.CitacionDbAdapter;
import uni.projecte.dataLayer.bd.ProjectDbAdapter;
import uni.projecte.dataLayer.bd.SampleAttributeDbAdapter;
import uni.projecte.dataLayer.bd.SampleDbAdapter;
import uni.projecte.dataLayer.utils.StringUtils;
import uni.projecte.dataTypes.LocalTaxonSet;
import uni.projecte.dataTypes.LocationCoord;
import uni.projecte.dataTypes.ProjectField;
import uni.projecte.maps.MapLocation;
import uni.projecte.maps.UTMDisplay;
import android.content.Context;
import android.content.MutableContextWrapper;
import android.database.Cursor;
import android.os.Handler;
import android.util.Log;
import edu.ub.bio.biogeolib.CoordConverter;
import edu.ub.bio.biogeolib.CoordinateLatLon;
import edu.ub.bio.biogeolib.CoordinateUTM;




public class CitationControler {
	
	protected Context baseContext;
	private CitacionDbAdapter mDbSample;
	private CitacionDbAdapter mDbAttributes;
	protected CitationControler sC;
	protected CitationExporter cExporter;
	
	private CoordinateUTM utm;

	/*
	private int KEY_TIPUS;
	private int LATITUT;
	private int LONGITUT;*/
	protected int KEY_DATA;
	
	private String date;
	protected double latitude;
	protected double longitude;
	
	protected long sampleId;
	private String firstFieldLabel;
	private String firstFieldName;
	private long projId=-1;
	
	private String author;
	
	private boolean citationExists;
	

	
	

	public CitationControler(Context baseContext) {
		super();
		
		this.baseContext=baseContext;
		
		PreferencesControler pc=new PreferencesControler(baseContext);
		author=pc.getUsername();

		citationExists=false;
	
	}
	
	public long createCitation(long idRs, double latPoint, double longPoint, String comment){
	
		CitacionDbAdapter mDbSample=new CitacionDbAdapter(baseContext);
		mDbSample.open();
			
		long idSample=  mDbSample.createCitation(idRs, latPoint, longPoint, comment);
			
		mDbSample.close();
		
		return idSample;		
		
	}
	
	
	public long createCitationWithDate(long idRs, double latPoint, double longPoint, String comment,String date){
		
				
		CitacionDbAdapter mDbSample=new CitacionDbAdapter(baseContext);
		mDbSample.open();
	
		
		long idSample=  mDbSample.createCitationWithDate(idRs, latPoint, longPoint, comment,date);
			
		mDbSample.close();
		
		return idSample;		
		
	}
	
	
	
	
	/** Methods invocated by Fagus Reader **/
	
	public long createEmptyCitation(long idRs){
		
		projId=idRs;
		
		this.sampleId=  mDbAttributes.createEmptyCitation(idRs);
	
		return sampleId;		
		
	}
	

	public boolean updateCitationDate(long citationId,String date){

		//Citation is repeated? --> (date | latitude | longitude) makes unique a citationId
		citationExists = mDbAttributes.checkRepeated(projId,this.sampleId, latitude, longitude, date);
		
		
		if(!citationExists) {
			
			mDbAttributes.updateDate(citationId, date);
		
		}
		
		this.latitude=0;
		this.longitude=0;
		
		return citationExists;
	}
	
	
	public void updateCitationLocation(long citationId,double lat, double longitude){
		
		this.latitude=lat;
		this.longitude=longitude;
		
		mDbAttributes.updateLocation(citationId, lat, longitude);


	}
	
	

	/*
	 * 
	 * Method used by CitationEditor
	 * 
	 * It requires a started transaction
	 * 
	 */
	
	public boolean updateCitationField(long sampleId, long idAtt, String newValue, String fieldName) {
		
		boolean updated=mDbAttributes.updateSampleFieldValue(sampleId, idAtt, newValue);
		
		if(!updated) {
			
			mDbAttributes.createCitationField(sampleId, idAtt, newValue, fieldName);
			
		}
		
		return updated;
		
	}
	
	
	public void loadCitation(long sampleId){
		
		CitacionDbAdapter sa=new CitacionDbAdapter(baseContext);
		sa.open();
		
		Cursor c=sa.fetchSampleBySampleId(sampleId);
		c.moveToFirst();
		
		this.date=c.getString(4);
		this.latitude=c.getDouble(2);
		this.longitude=c.getDouble(3);
		this.projId=c.getLong(1);
		
		c.close();
		sa.close();
		
	}
	
	public long getNextCitationId(long citationId) {
		
		long nextCitationId=-1;
		
		mDbSample=new CitacionDbAdapter(baseContext);
		mDbSample.open();
		
		Cursor citation=mDbSample.getNextCitationId(citationId);
			
		if(citation!=null && citation.getCount()>0){
				
			nextCitationId=citation.getLong(0);
			citation.close();
				
		}
		
		mDbSample.close();
		
		return nextCitationId;
		
	}

	public long getPreviousCitationId(long citationId) {

		long prevCitationId=-1;
		
		mDbSample=new CitacionDbAdapter(baseContext);
		mDbSample.open();
		
		Cursor citation=mDbSample.getPreviousCitationId(citationId);
			
		if(citation!=null && citation.getCount()>0){
				
			prevCitationId=citation.getLong(0);
			citation.close();
				
		}
		
		mDbSample.close();
		
		return prevCitationId;
		
	}
	
	
	public ArrayList<LocationCoord> getSamplesLocationByProjectId(long projectId, ArrayList<MapLocation> mapLocations){
		
		mDbSample=new CitacionDbAdapter(baseContext);
		mDbSample.open();
		
		ProjectControler rsC= new ProjectControler(baseContext);
		long idField=rsC.getFieldIdByName(projectId, "OriginalTaxonName");
		
		Cursor cursor =null;
		
		/* Hi ha original taxon Name */
		if(idField>0) {
			
			firstFieldLabel=rsC.getFieldLabelByName(projectId, "OriginalTaxonName");
			cursor= mDbSample.fetchSamplesByField(projectId,"OriginalTaxonName",false);

			
		} 
		else{
			
			Cursor first=mDbSample.fetchSamplesByResearchId(projectId);
			first.moveToFirst();
			
			
			
			if(first.getCount()>0) {

				String firstFieldName=first.getString(4);
				
				//firstFieldName="OriginalTaxonName";
				
				firstFieldLabel=rsC.getFieldLabelByName(projectId, firstFieldName);
				
				
				cursor= mDbSample.fetchSamplesByField(projectId,firstFieldName,false);
				
			}
			else{
				
				cursor=mDbSample.fetchSamples(projectId);
				
			}
			
			first.close();
			
			
			
		}
		
		
		int n=cursor.getCount();
		
		ArrayList<LocationCoord> coordinates=new ArrayList<LocationCoord>();
		
		cursor.moveToFirst();
		
		for(int i=0;i<n;i++){
			
			Double lat=cursor.getDouble(4);
			Double longitude=cursor.getDouble(5);
			
		 	if(lat<=90 && longitude<=180){
		 		
		 		coordinates.add(new LocationCoord(lat,longitude));
		 		mapLocations.add(new MapLocation(cursor.getLong(0),cursor.getString(1),cursor.getDouble(4),cursor.getDouble(5)));
			
		 	}
			cursor.moveToNext();
			
		}
		
		cursor.close();
		
		mDbSample.close();
		
		return coordinates;
		
		
	}
	
	
	public ArrayList<LocationCoord> getSampleLocationBySampleId(long projId,long sampleId, ArrayList<MapLocation> mapLocations){
		
		mDbSample=new CitacionDbAdapter(baseContext);
		mDbSample.open();
		
		ProjectControler rsC= new ProjectControler(baseContext);
		long idField=rsC.getFieldIdByName(projId, "OriginalTaxonName");
		
		Cursor cursor =null;
		
		/* Hi ha original taxon Name */
		if(idField>0) {
			
			firstFieldLabel=rsC.getFieldLabelByName(projId, "OriginalTaxonName");
			cursor= mDbSample.fetchSampleByField(projId,sampleId,"OriginalTaxonName");

			
		} 
		else{
		
			cursor=mDbSample.fetchSampleBySampleIdWithFirstField(sampleId);
			
		}
		

		cursor.moveToFirst();
		int n=cursor.getCount();

		ArrayList<LocationCoord> coordinates=new ArrayList<LocationCoord>();

		
		for(int i=0;i<n;i++){
			
			Double lat=cursor.getDouble(4);
			Double longitude=cursor.getDouble(5);
			
		 	if(lat<=90 && longitude<=180){
		 		
		 		coordinates.add(new LocationCoord(lat,longitude));
		 		mapLocations.add(new MapLocation(cursor.getLong(0),cursor.getString(1),cursor.getDouble(4),cursor.getDouble(5)));
			
		 	}

			cursor.moveToNext();
		
		
		
		}
		
		cursor.close();
		
		mDbSample.close();
		
		return coordinates;
		
		
	}
	
	
	public LocalTaxonSet getLocalTaxon(long projId,String utm){
		
		
		LocalTaxonSet taxonList=new LocalTaxonSet(utm);
		
		CitacionDbAdapter citationAdapter = new CitacionDbAdapter(baseContext);
		citationAdapter.open();
		
		
		Cursor taxonListCursor=citationAdapter.fetchSamplesByFieldOrdered(projId,"OriginalTaxonName");
		taxonListCursor.moveToFirst();
		
		while(!taxonListCursor.isAfterLast()){
			
			taxonList.addTaxon(taxonListCursor.getLong(0),taxonListCursor.getString(1), taxonListCursor.getDouble(4), taxonListCursor.getDouble(5), taxonListCursor.getString(2));
		
			taxonListCursor.moveToNext();
			
		}
		
		taxonListCursor.close();
		
		citationAdapter.close();
		
		return taxonList;
		
	}
	
	
	public LocalTaxonSet getLocalWrongTaxon(long projId, String thName){
	
		LocalTaxonSet taxonList=new LocalTaxonSet();
		
		CitacionDbAdapter citationAdapter = new CitacionDbAdapter(baseContext);
		citationAdapter.open();
		
		Cursor taxonListCursor=citationAdapter.fetchSamplesByFieldOrdered(projId,"OriginalTaxonName");
		citationAdapter.close();
		
		ThesaurusControler thCnt=new ThesaurusControler(baseContext);
		boolean status=thCnt.initThReader(thName);
		
		taxonListCursor.moveToFirst();
		
		while(!taxonListCursor.isAfterLast()){
			
			String taxonName=taxonListCursor.getString(1);
			
			if(!belongsToThesaurus(taxonName,thCnt)){

				taxonList.insertTaxon(taxonListCursor.getLong(0),taxonName, taxonListCursor.getDouble(4), taxonListCursor.getDouble(5), taxonListCursor.getString(2));
				
			}
		
			taxonListCursor.moveToNext();
			
		}
		
		taxonListCursor.close();
		
		return taxonList;
		
	}
	
	
	
	private boolean belongsToThesaurus(String taxonName, ThesaurusControler tC) {
		
		Cursor element=tC.fetchThesaurusItembyName(taxonName);
		
		if(element!=null && element.getCount()>0){ 
			
			element.close();
			return true;
			
		}
		else{ 
			
			if(element!=null) element.close();
			return false;
			
		}
		
	}
	
	
	public Cursor getCitationListCursorByProjIdUnsyncro(long projId){
		
		CitacionDbAdapter citationAdapter = new CitacionDbAdapter(baseContext);
		
		citationAdapter.open();
		   
		Cursor cursor= citationAdapter.fetchUnsyncronisedSamples(projId);

		citationAdapter.close();
	   
	   return cursor;
		
		
	}
	
	public Cursor getCitationListCursorByProjId(long projId){
		
		CitacionDbAdapter mDbAttributes = new CitacionDbAdapter(baseContext);
		
		mDbAttributes.open();
		   
		Cursor cursor= mDbAttributes.fetchSamplesByResearchId(projId);
		

	   mDbAttributes.close();
	   
	   return cursor;
		
		
	}
	
	private long getMainField(long projId){
		
		long fieldId=-1;
		
		ProjectControler rsC=new ProjectControler(baseContext);
		fieldId=rsC.getFieldIdByName(projId, "OriginalTaxonName");
		
		if(fieldId<0){
			
			Cursor fieldCursor =rsC.getSecondLevelFieldId(projId);
			
			if(fieldCursor.getCount()>0){
				
				fieldId=fieldCursor.getLong(0);
				firstFieldName=fieldCursor.getString(2);

			}
			
		}
		else{
			
			firstFieldName="OriginalTaxonName";
			
			
		}
		
		return fieldId;
		
		
	}
	
	public Cursor getCitationsWithFirstFieldByProjectId(long projId, boolean alphaOrder, boolean timestampAsc){
		
		CitacionDbAdapter mDbAttributes = new CitacionDbAdapter(baseContext);
		
		mDbAttributes.open();
		
		long idField=getMainField(projId);
		
		Cursor cursor =null;
		
		/* Hi ha original taxon Name */
		if(idField>0) {
			
			
			if(alphaOrder) cursor= mDbAttributes.fetchSamplesByFieldOrdered(projId,firstFieldName);
			else cursor= mDbAttributes.fetchSamplesByField(projId,firstFieldName,timestampAsc);
			
			
		} 
		/*No hi ha original*/
		else{
		
					
			Cursor first=mDbAttributes.fetchSamplesByResearchId(projId);
			first.moveToFirst();
			
			if(first.getCount()>0) {

				String firstFieldName=first.getString(4);
				cursor= mDbAttributes.fetchSamplesByField(projId,firstFieldName,timestampAsc);
				
			}
			else{
				
				cursor=mDbAttributes.fetchSamples(projId);
				
			}
			
			first.close();
			
			
		}
		
		
		mDbAttributes.close();
		
		return cursor;
	   
		
		
	}
	
	public Cursor getSamplesByField(long projId, String label){
		
		CitacionDbAdapter citationAdapter = new CitacionDbAdapter(baseContext);
		ProjectControler rC= new ProjectControler(baseContext);
		
		String fieldName=rC.getFieldNameByLabel(projId,label);
		Cursor cursor=null;
		
		if(fieldName.equals("")){
			
		
			
		}
		else{
			
			citationAdapter.open();
			
			cursor= citationAdapter.fetchSamplesByField(projId,fieldName,false);

			citationAdapter.close();
			
		}
	
	
	   
		return cursor;
		
		
	}



/*	public void addSampleAttribute(long idSample, int id, String value) {
		
		mDbAttributes = new CitacionDbAdapter(c);
		
		mDbAttributes.open();
		   
		mDbAttributes.createSampleAttribute(idSample,id, value,"");  

	   mDbAttributes.close();
  
		
	}*/
	

	
	/* 
	 * 
	 * Method used by Sampling Activity
	 * 
	 * It's required to start and finish DB transaction
	 * 
	 * 
	 * */
	
	public long addCitationField(long projId,long idSample, long idRs,String attName, String value){
		
		long citationFieldId=-1;
		
		if(!citationExists){	
		
			long attId;
			
			ProjectDbAdapter aTypes=new ProjectDbAdapter(baseContext);
			aTypes.open();
			
	
			Cursor att= aTypes.fetchFieldsFromProject(projId, attName);
			
			att.moveToFirst();
			
			//attExists
			
			if(att.getCount()>0){
				
				attId=att.getLong(0);
				citationFieldId=mDbAttributes.createCitationField(idSample,attId,value,attName);  
	
				
			}
			
			//we have to create a new Attribute
			else{
				
				//aTypes.createAttribute(idRs, attName, label, desc, value, type, cat);
				
				
			}
			
			att.close();		
			aTypes.close();
			
		}
		
		return citationFieldId;
		
	}
	
	public void addCitationFieldNoCheck(long projId,long idSample, long idField,String attName, String value){
		
		if(!citationExists){	
		
			mDbAttributes.createCitationField(idSample,idField,value,attName);  

		}
		
	}
	
	public void addObservationAuthor(long projId,long idSample){
		
		
		
		ProjectDbAdapter aTypes=new ProjectDbAdapter(baseContext);
		
		aTypes.open();

		Cursor att= aTypes.fetchFieldsFromProject(projId,"ObservationAuthor");
		
		att.moveToFirst();
		
		//attExists
		
		if(att.getCount()>0){
			
			long id=att.getLong(0);
			
			//mDbAttributes.updateSampleFieldValue(idSample, id, author);
			
			mDbAttributes.createCitationField(idSample,id,author,"ObservationAuthor");  

			
		}
		
		//we have to create a new Attribute
		else{
			
			long id=aTypes.createField(projId, "ObservationAuthor", baseContext.getString(R.string.ObservationAuthor), "", "", "simple", "ADDED",false);
			mDbAttributes.createCitationField(idSample,id,author,"ObservationAuthor");  

			
		}
	
		
		att.close();

		aTypes.close();

		
		
	}
	
	/*
	 * Global citation exporter. This method will create a file @fileName on a concret @exportFormat 
	 * with all project @projId citations. 
	 * 
	 */
	

	public int exportProject(long projId, Context co,String fileName, String exportFormat){
		
				
		ProjectControler projCnt= new ProjectControler(co);
		sC= new CitationControler(co);
	
		Cursor citations= sC.getCitationListCursorByProjIdUnsyncro(projId);
		KEY_DATA=citations.getColumnIndex(SampleDbAdapter.DATE);
		
		int n= citations.getCount();
		
		projCnt.loadProjectInfoById(projId);
		
		//Depending on the chosen type of file we'll instantiate the concrete exporter subclass
		
		if(exportFormat.equals("Fagus")){
			
			cExporter=new FagusExporter(projCnt.getName(),projCnt.getThName(),projCnt.getCitationType());
			
		}
		else if (exportFormat.equals("TAB")){
			
			cExporter=new TABExporter(projCnt.getName(),projCnt.getThName(),projCnt.getCitationType());

		}
		else if (exportFormat.equals("JSON")){
			
			cExporter=new JSONExporter(projCnt.getName(),projCnt.getThName(),projCnt.getCitationType());

		}
		else if (exportFormat.equals("Zamia")){
			
			cExporter=new ZamiaCitationExporter(projCnt.getProjectId(),projCnt.getName(),projCnt.getThName(),projCnt.getCitationType(),baseContext);

		}
		else if (exportFormat.equals("KML")){
			
			cExporter=new KMLExporter(projCnt.getName(),projCnt.getThName(),projCnt.getCitationType());

		}
		
		cExporter.openDocument();

		
		HashMap<Long, ProjectField> projectFields=projCnt.getProjectFieldsMap(projId);

		Log.d("Citations","Exportant Citacions Start "+exportFormat);
				
		for (int i=0; i<n; i++){
			
			exportCitation(citations, projectFields);
			
			//fagus needs to know which is the last citation
			cExporter.setLast(false); 
			
			citations.moveToNext();

		}
		
		Log.d("Citations","Exportant Citacions End "+exportFormat);

		citations.close();
		
		
		cExporter.closeDocument();
				
		cExporter.stringToFile(fileName,baseContext);
		
		
		return 0;
		
	}
	
	/*
	 * Concrete citation exporter. This method will create a file @fileName on a concrete @exportFormat 
	 * with all project @projId citations. 
	 * 
	 */
	public int exportProject(long projId,Set<Long> selectionIds,String fileName, String exportFormat, Handler handlerExportProcessDialog){
		
				
		ProjectControler rC= new ProjectControler(baseContext);
		sC= new CitationControler(baseContext);
	
		
		rC.loadProjectInfoById(projId);
		
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
		else if (exportFormat.equals("Zamia")){
			
			cExporter=new ZamiaCitationExporter(rC.getProjectId(),rC.getName(),rC.getThName(),rC.getCitationType(),baseContext);

		}
		else if (exportFormat.equals("KML")){
			
			cExporter=new KMLExporter(rC.getName(),rC.getThName(),rC.getCitationType());

		}
		else if (exportFormat.equals("Xflora")){
			
			cExporter=new XfloraExporter(rC.getName(),rC.getThName(),rC.getCitationType());

		}
		
		cExporter.openDocument();

		
		//c= list of Fields
		HashMap<Long, ProjectField> projectFields=rC.getProjectFieldsMap(projId);
		
		cExporter.setProjFieldsList(projectFields);

		Log.d("Citations","Exportant Citacions Start "+exportFormat);
		
		CitacionDbAdapter citationAdapter = new CitacionDbAdapter(baseContext);
		citationAdapter.open();

		Iterator<Long> iter = selectionIds.iterator();

				
		 while (iter.hasNext()) {
			
 		  	long citationId=iter.next();
			 
			Cursor citations= citationAdapter.fetchCitationByCitationId(citationId);
			KEY_DATA=citations.getColumnIndex(SampleDbAdapter.DATE);
			
			exportCitation(citations, projectFields);
			
			handlerExportProcessDialog.sendMessage(handlerExportProcessDialog.obtainMessage());
						
			//Fagus Exporter needs to know which is the last citation
			cExporter.setLast(false); 
			
			citations.close();
			
		}
		
		citationAdapter.close();
		
		Log.d("Citations","Exportant Citacions End "+exportFormat);
		
		
		cExporter.closeDocument();
				
		cExporter.stringToFile(fileName,baseContext);
		
		
		return selectionIds.size();
		
	}
	
	
	protected void exportCitation(Cursor citations, HashMap<Long, ProjectField> projectFields){
		
		// When exporting to KML if there's no location citation is not added to overlay 
		if(cExporter instanceof KMLExporter && (citations.getDouble(2)>90 || citations.getDouble(3)>180)){
			
			
		}
		else{
			
			if(cExporter instanceof FagusExporter){

				String value=sC.getSheetFieldValue(citations.getLong(0));
				if(!value.equals("") && value.equals("true") ) ((FagusExporter)cExporter).forceSpecimen(value);

			}

			long citationId=citations.getLong(0);

			cExporter.setCitationId(citationId);

			cExporter.openCitation();
						
			cExporter.writeCitationCoordinateLatLong(citations.getDouble(2),citations.getDouble(3));
			
			if(citations.getDouble(2)>90 || citations.getDouble(3)>180){
	    		
				cExporter.writeCitationCoordinateUTM("");
					
	    	}
			
			else {
				
				utm = CoordConverter.getInstance().toUTM(new CoordinateLatLon(citations.getDouble(2),citations.getDouble(3)));
				cExporter.writeCitationCoordinateUTM(utm.getShortForm());
				cExporter.writeCitationCoordinateXY(utm.getX(),utm.getY());
				
			}
				
			cExporter.writeCitationDate(citations.getString(4));
				
			Cursor citationFieldValue=getCitationFieldValues(citations.getLong(0));

			int j=0;
			int m=citationFieldValue.getCount();
			
			//iterating over each field value
			while(!citationFieldValue.isAfterLast()){
				
				if(j==m-1) cExporter.setLast(true); 
						
				ProjectField projField=projectFields.get(citationFieldValue.getLong(2));
						
				
				
				if(projField.isSubFieldExport()){ 
					
					cExporter.createCitationField(projField.getName(), projField.getLabel(), getSubCitationValue(citationFieldValue.getString(3)), projField.getDesc());
					cExporter.setFieldType(projField.getId(),projField.getType(),baseContext);
					
				}
				else{
					
					cExporter.createCitationField(projField.getName(), projField.getLabel(), citationFieldValue.getString(3), projField.getDesc());
					cExporter.setFieldType(projField.getId(),projField.getType(),baseContext);
					
				}
				
				cExporter.closeCitationField();
						
				citationFieldValue.moveToNext();
				j++;
							
			}
					
			citationFieldValue.close();		
			
			cExporter.closeCitation();
		
		}
		
	}
		
		
	
	private String getSubCitationValue(String subCitId) {

		CitationSecondLevelControler citSLCnt= new CitationSecondLevelControler(baseContext);

		String value=citSLCnt.getMultiPhotosValues(subCitId);
		
		return value;
		
		
	}

	private String getSheetFieldValue(long sampleId) {
		
		mDbAttributes = new CitacionDbAdapter(baseContext);
		mDbAttributes.open();
		
		
		Cursor c=mDbAttributes.fetchSheetField(sampleId);
		c.moveToFirst();
		
		String value;
	
		if(c.getCount()>0) value=c.getString(3);
		else value="";
		
		c.close();
		
		mDbAttributes.close();
		
		return value;
		
	}

	public String getFieldValue(long sampleId,long attId){

		
		Cursor c=mDbAttributes.fetchSampleAttributeBySampleAttId(sampleId,attId);
		c.moveToFirst();
		
		String value;
	
		if(c.getCount()>0) value=c.getString(3);
		else value="";
		
		c.close();
		
		return value;
		
		
	}
	
	public Cursor getCitationFieldValues(long citationId){
		
		mDbAttributes = new CitacionDbAdapter(baseContext);
		mDbAttributes.open();
		
			Cursor c=mDbAttributes.fetchSamplesByCitationId(citationId);
			c.moveToFirst();
		
		mDbAttributes.close();	
	
		return c;
		
	}	
	
	public String getMultiPhotoFieldTag(long citationId, long multiPhotoFieldId){
		
		String multiPhotoTag="";
		
		mDbAttributes = new CitacionDbAdapter(baseContext);
		mDbAttributes.open();
		
			Cursor c=mDbAttributes.fetchCitationsByMultiPhoto(citationId, multiPhotoFieldId);
			c.moveToFirst();
			
			if(c!=null && c.getCount()>0){
			
				multiPhotoTag=c.getString(1);
				
				c.close();
			}
			
		mDbAttributes.close();	
	
		return multiPhotoTag;
		
	}	
	
	public String getFieldValueNoTrans(long sampleId,long attId){

		
		mDbAttributes = new CitacionDbAdapter(baseContext);
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
	 * Creates a list of fields and each values 
	 * 
	 */
	
	public String getCitationHTMLValues(long citationId, HashMap<String, String> fieldsLabelNames) {
	
		
		CitacionDbAdapter citations = new CitacionDbAdapter(baseContext);
		citations.open();
		
		
		Cursor citInfo=citations.fetchSampleBySampleId(citationId);
		Cursor citList=citations.fetchSampleAttributesBySampleId(citationId);
		
		
		citInfo.moveToFirst();
		
		Double latitude=citInfo.getDouble(2);
		Double longitude=citInfo.getDouble(3);
		
		String result="";
		
		if(latitude<100 && longitude <190){
			
			 CoordinateUTM utm = CoordConverter.getInstance().toUTM(new CoordinateLatLon(latitude,longitude));
		
			result="<b>Lat/long: </b> "+latitude.toString()+" "+longitude.toString()+"<br/>";
			result=result+"<b>UTM: </b>"+UTMDisplay.convertUTM(utm.getShortForm(),"1m",true)+"<br/>";
			
		}
		else{
			
			result="<b>"+baseContext.getString(R.string.citationWithoutLocation)+"</b> <br/>";
			
		}
		
	  
		
		citInfo.close();
		
		citList.moveToFirst();
		
		int n=citList.getCount();
		


		
		for(int i=0;i<n;i++){
			
			String fieldName=citList.getString(4);
			String fieldValue=citList.getString(3);
			
			if(fieldValue==null) fieldValue="";

			if(fieldName.equals("Sheet")) fieldValue=StringUtils.getBooleanValue(baseContext,fieldValue);
			
			result=result+"<br/><b>"+fieldsLabelNames.get(fieldName)+"</b> : "+fieldValue;
			
			citList.moveToNext();
	
			
		}
		
		result=result+"<br/>";
		
		citList.close();

		citations.close();
		
		return result;
		
	}
	


	public String getAllCitationValues(long citationId, HashMap<String, String> fieldsLabelNames, String citationFieldName, HashMap<Long, String> citationsWithPhoto) {
	
		
		CitacionDbAdapter citations = new CitacionDbAdapter(baseContext);
		citations.open();
		
			Cursor citList=citations.fetchSampleAttributesBySampleId(citationId);
			citList.moveToFirst();
			
			int n=citList.getCount();
			
			String result="";
	
			for(int i=0;i<n;i++){
				
				String fieldName=citList.getString(4);
				String fieldLabel=fieldsLabelNames.get(fieldName);
				
				String fieldValue=citList.getString(3);
				
				if(fieldName.equals(citationFieldName) && !StringUtils.emptyValue(fieldValue)){ 
					
					citationsWithPhoto.put(citationId, fieldValue);
					
				}
				
				if(fieldValue==null) fieldValue="";
				
				result+=fieldLabel+" : "+fieldValue+"\n";
				citList.moveToNext();
				
			}
			
			
			citList.close();
		
		citations.close();
		
		return result;
		
	}
	
	public int getCitationsWithField(long idField) {
	
		mDbAttributes = new CitacionDbAdapter(baseContext);
		mDbAttributes.open();
		
		Cursor fields=mDbAttributes.fetchSamplesByFieldId(idField);
		fields.moveToFirst();
		
		int numFields=fields.getCount();
		fields.close();
		
		mDbAttributes.close();

		return numFields;
		
	}
	
	public Cursor getCitationValuesFromField(long idField) {
		
		mDbAttributes = new CitacionDbAdapter(baseContext);
		mDbAttributes.open();
		
		Cursor fields=mDbAttributes.fetchSamplesByFieldId(idField);
		fields.moveToFirst();
				
		mDbAttributes.close();

		return fields;
		
	}
	

	public Cursor getCitationFromFieldAndCitationId(long citationId, long fieldId){
		
		mDbAttributes = new CitacionDbAdapter(baseContext);
		mDbAttributes.open();
		
		Cursor citationFields=mDbAttributes.fetchSamplesByFieldIdAndCitationId(citationId,fieldId);

		citationFields.moveToFirst();
		
		mDbAttributes.close();

		return citationFields;
	}
	


	public void startTransaction() {

		mDbAttributes = new CitacionDbAdapter(baseContext);
		mDbAttributes.open();

		mDbAttributes.startTransaction();
		
		
	}

	public void EndTransaction() {
		
		mDbAttributes.endTransaction();
		mDbAttributes.close();
		
	}
	
	public Cursor getCitationsValuesFromCitationId(long citationId) {
	
		
		CitacionDbAdapter citations = new CitacionDbAdapter(baseContext);
		citations.open();
		
		Cursor citList=citations.fetchSampleAttributesBySampleId(citationId);
		
		citList.moveToFirst();
		
		citations.close();
		
		return citList;
		
	}
	
	
	public void deleteCitation(long sampleId){
		
		CitacionDbAdapter sa=new CitacionDbAdapter(baseContext);
	
		sa.open();
		
		Cursor attribC=sa.fetchSampleAttributesBySampleId(sampleId);
		attribC.moveToFirst();
			
		while(!attribC.isAfterLast()){
				
			sa.deleteSampleAttribute(attribC.getLong(0));
			
			attribC.moveToNext();
				
		}
			
		sa.deleteCitation(sampleId);
		
		
		
		attribC.close();
		
		sa.close();
			
	}
	
	
	public boolean removePhoto(long citationId, String photo){
		
		boolean result=false;
		
		CitacionDbAdapter sa=new CitacionDbAdapter(baseContext);
		sa.open();

		Cursor cursor=sa.fetchCitationIdByPhotoField(photo);
		cursor.moveToFirst();
		
		if(cursor.getCount()>0) {
			
			long fieldCitationId=cursor.getLong(0);
			result=sa.updateCitationFieldValue(cursor.getLong(1), fieldCitationId, "");

		}
		
		cursor.close();
		sa.close();
		
			if(result){
				
				File f= new File(photo);
				result=result && f.delete();
			
			}
	
		return result;
		
	}
	

	public String[] getCitationInfoByPhoto(String photo, HashMap<String, String> fieldsLabelNames){
		
		CitacionDbAdapter sa=new CitacionDbAdapter(baseContext);
		
		sa.open();
		long citationId=0;
		String[] values=null;
		
		Cursor cursor=sa.fetchCitationIdByPhotoField(photo);
		cursor.moveToFirst();
		
		if(cursor.getCount()>0) {
			
			citationId=cursor.getLong(1);
		}
		
		sa.close();
		
		if(cursor.getCount()>0) values=getCitationValues(citationId,fieldsLabelNames);
		
		cursor.close();
		
		return values;
		
	}
	
	
	public String getLastAvailableDate(long projectId, String timeStamp) {

		String lastTimeStamp="";

		Cursor lastCitations=mDbAttributes.getLastTimeStamp(projectId,timeStamp);
		
		if(lastCitations!=null && lastCitations.getCount()>0) {
			
			lastTimeStamp=lastCitations.getString(4);
			
		}
		else lastTimeStamp=timeStamp+" 01:00:00";
		
		lastCitations.close();
		
		return lastTimeStamp;
		
		
	}
	
	public long getCitationIdByPhoto(String photo){
		
		CitacionDbAdapter sa=new CitacionDbAdapter(baseContext);
		
		sa.open();
		long citationId=0;
		
			Cursor cursor=sa.fetchCitationIdByPhotoField(photo);
			cursor.moveToFirst();
			
			if(cursor.getCount()>0) {
				
				citationId=cursor.getLong(1);
			}
			
			cursor.close();
		
		sa.close();
		
		return citationId;
		
	}
	
	public String[] getCitationValues(long citationId, HashMap<String, String> fieldsLabelNames) {
	
		
		CitacionDbAdapter citations = new CitacionDbAdapter(baseContext);
		citations.open();
		
		Cursor citList=citations.fetchSampleAttributesBySampleId(citationId);
		
		citList.moveToFirst();
		
		int n=citList.getCount();
		
		String[] result = new String[n];
		
		
		for(int i=0;i<n;i++){
			
			String fieldName=citList.getString(4);
			String fieldValue=citList.getString(3);
			
			if(fieldValue==null) fieldValue="";
			
			result[i]=fieldsLabelNames.get(fieldName)+" : "+fieldValue;
			citList.moveToNext();
			
			
		}
		
		
		citList.close();

		citations.close();
		
		return result;
		
	}

	
	public int deleteAllCitations(Cursor c, Context co){
		
		int n= c.getCount();
		long idMostra;
		SampleAttributeDbAdapter sa=new SampleAttributeDbAdapter(co);
		SampleDbAdapter sampl=new SampleDbAdapter(co);
		sampl.open();

		sa.open();
		
		
		for(int i=0; i<n; i++){
			
			idMostra=(long)c.getDouble(0);
			
			Cursor attribC=sa.fetchSampleAttributesBySampleId(idMostra);
			attribC.moveToFirst();
			
			int m=attribC.getCount();
			
			for(int j=0; j<m; j++){
				
				sa.deleteSampleAttribute((long)attribC.getDouble(0));
				
			}
				sampl.deleteSample(idMostra);
				c.moveToNext();
		
		}
		
		return n;
		
	}
	
	
	public void deleteCitationField(long idField) {

		mDbAttributes = new CitacionDbAdapter(baseContext);
		mDbAttributes.open();
		
		mDbAttributes.deleteField(idField);
		mDbAttributes.close();

		ProjectControler rC= new ProjectControler(baseContext);
		rC.removeField(idField);
			
	}
	
	
	public void deleteAllCitationsFromProject(long projId) {
		
		CitacionDbAdapter cAdapt = new CitacionDbAdapter(baseContext);
		cAdapt.open();
	
		
		Cursor citations=cAdapt.fetchSamples(projId);
		citations.moveToFirst();
		
		while (!citations.isAfterLast()) {

			long citationId=citations.getLong(0);
			
			cAdapt.deleteCitationFields(citationId);
			cAdapt.deleteCitation(citationId);
			
       	    citations.moveToNext();
        }

		cAdapt.close();		
		
	}
	
	public String getAvailableTimestamp(long projId,String presettedDate) {

		String availableTimestamp=nextTimeStampAvailable(presettedDate);
			
		if(!availableTimestamp.equals("")){
			
			boolean available=isTimestampAvailable(projId, availableTimestamp);
			if(!available) availableTimestamp=getAvailableTimestamp(projId, availableTimestamp);
		
		}
		
		return availableTimestamp;
		
	}
	
	private String nextTimeStampAvailable(String presettedDate){
		
		String availableTimestamp="";
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd kk:mm:ss");
		Calendar c = Calendar.getInstance();
		try {
			
			c.setTime(sdf.parse(presettedDate));
			c.add(Calendar.SECOND, 1);
								
			availableTimestamp = sdf.format(c.getTime()); 
					
    		
		} 
		catch (ParseException e) {

			e.printStackTrace();
		}
		
		return availableTimestamp;
		
		
	}
	



	
	private boolean isTimestampAvailable(long projId, String presettedDate){
		
		CitacionDbAdapter cAdapt = new CitacionDbAdapter(baseContext);
		cAdapt.open();
	
	
		boolean available=cAdapt.isTimestampAvailable(projId, presettedDate);

		cAdapt.close();
		
		return available;
	
		
	}
	
	

	
	
	
	public String getDate() {
		return date;
	}

	public double getLatitude() {
		return latitude;
	}

	public double getLongitude() {
		return longitude;
	}
	
	public String getFirstFieldLabel() {
		return firstFieldLabel;
	}


	public void setcExporter(CitationExporter cExporter) {
		this.cExporter = cExporter;
	}

	public long getProjId() {
		return projId;
	}

	public void setProjId(long projId) {
		this.projId = projId;
	}

	public Context getBaseContext() {
		return baseContext;
	}






	
	

}
