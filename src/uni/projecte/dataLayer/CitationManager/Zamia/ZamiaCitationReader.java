package uni.projecte.dataLayer.CitationManager.Zamia;

import uni.projecte.controler.PolygonControler;
import uni.projecte.controler.ProjectSecondLevelControler;
import uni.projecte.controler.CitationSecondLevelControler;
import uni.projecte.dataLayer.CitationManager.Fagus.FagusReader;
import uni.projecte.dataLayer.utils.PhotoUtils;
import uni.projecte.dataLayer.utils.UTMUtils;
import uni.projecte.maps.overlays.PolygonOverlay;
import android.content.Context;
import android.util.Log;

public class ZamiaCitationReader extends FagusReader {
	
	protected CitationSecondLevelControler slSC;
	protected ProjectSecondLevelControler slPC;
	protected PolygonControler polyCnt;
	protected boolean secondLevelFields=false;
	protected String secondLevelTag="";
	protected boolean polygon=false;
	protected String lastFieldValue;
	protected long secondLevelSampleId;
	protected String secondLevelType;
	
	private boolean multiPhoto=false;
	private long multiPhotoFieldId;
	private boolean hasOldPhotoField=false;
	
	
	public ZamiaCitationReader(Context c, long projectId){
		
		super(c, projectId);
		
		slSC= new CitationSecondLevelControler(c);
		slPC= new ProjectSecondLevelControler(c);
		polyCnt= new PolygonControler(c);
		
		multiPhoto=projCnt.hasMultiPhotoField(projectId);
		hasOldPhotoField=projCnt.hasOldPhotoField(projectId);
		multiPhotoFieldId=projCnt.getMultiPhotoFieldId();
		
		slSC.startTransaction();

		
	}
	
	public ZamiaCitationReader(Context c, long projectId,boolean createFields){
		
		super(c, projectId);
	
		slSC= new CitationSecondLevelControler(c);
		slPC= new ProjectSecondLevelControler(c);
		
		multiPhoto=projCnt.hasOldPhotoField(projectId);

		slSC.startTransaction();

		createFields=false;

		
	}
	

	public void createNewSample() {
		
		if(!secondLevelFields){
			
			this.sampleId=citCnt.createEmptyCitation(projectId);
			Log.i("Cit",this.sampleId+" : create Empty Citation:");
				
		}
		else{
			
			this.secondLevelSampleId=slSC.createEmptyCitation(secondLevelTag,projectId,secondLevelType,sampleId);
			Log.i("Cit",this.secondLevelSampleId+" : create Empty Citation SL:");
			
		}
		
		numSamples++;
				
	}
	
	public void createCitationCoordinate(String location){
		
		if(!secondLevelFields){

			super.createCitationCoordinate(location);
				
		}
		else{
			
			if(UTMUtils.isLatLong(location)){
				
				location=location.replace(",", ".");
				String [] loc=location.split(" ");
				String lat=(String) loc[0].subSequence(0, loc[0].length()-1);
				
				slSC.updateCitationLocation(this.secondLevelSampleId,Double.valueOf(lat), Double.valueOf(loc[1]));
			}
			
		}
		
	}
	
	public void setSecondLevelFields(boolean sLFields){
		
		if(sLFields) secondLevelTag=lastFieldValue;
		else secondLevelTag="";
		
		Log.i("Cit","SetSecLevel "+sLFields);
		this.secondLevelFields=sLFields;
				
	}
	
	@Override
	public void finishReader(){
		
		slSC.EndTransaction();
		
		super.finishReader();
		
	}

	public boolean createObservationDate(String date) {
	
		boolean repeated=false;
		
		if(!secondLevelFields){
			
			repeated=citCnt.updateCitationDate(sampleId,date);
				
		}
		else{
			
			repeated=slSC.updateCitationDate(secondLevelSampleId,date);
		
		}
		
		return repeated;
		
	}
	
	@Override
	public void createDatumFields(String tempVal, String name, String label,String category) {
	

		lastFieldValue=tempVal;
		
		if(!secondLevelFields){

	
			long fieldId= projCnt.getFieldIdByName(projectId,name);
			
			if(fieldId>0){ 

				if(fieldId==multiPhotoFieldId && !hasOldPhotoField) {
				
					tempVal=photoToMultiPhoto(tempVal,name);
				
				}
				
				citCnt.addCitationField(projectId,this.sampleId, this.projectId, name, tempVal);
				
			}
				
		}
		else{
					
			//obtenir identificador camp
			
			long fieldId=-1;
			
			if(secondLevelType.equals("polygon")) fieldId=slPC.getSLId(projectId, "polygonAltitude");
			else if(secondLevelType.equals("multiPhoto")) fieldId=slPC.getSLId(projectId, "photo");
			else fieldId=slPC.getSLId(projectId, lastFieldValue);
			
			Log.i("Cit","Create SL "+name+" "+tempVal+" with field: "+fieldId);

			slSC.addCitationField(fieldId,this.secondLevelSampleId, this.projectId, name, tempVal);
			
			
		}
		
	
	}
	
	private String photoToMultiPhoto(String tempVal, String name) {

		String multiPhotoId=PhotoUtils.getFileName(tempVal);		
		long fieldId=slPC.getSLId(projectId, "Photo");

		long secondLevelSampleId=slSC.createEmptyCitation(multiPhotoId,projectId,"multiPhoto",sampleId);
		slSC.addCitationField(fieldId,secondLevelSampleId, this.projectId, "Photo", tempVal);

		
		return multiPhotoId;
	}

	public String getSecondLevelType() {
		return secondLevelType;
	}

	public void setSecondLevelType(String secondLevelType) {
		this.secondLevelType = secondLevelType;
	}
	

}
