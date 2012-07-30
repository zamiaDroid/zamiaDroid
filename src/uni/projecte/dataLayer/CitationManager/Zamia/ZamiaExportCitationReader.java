package uni.projecte.dataLayer.CitationManager.Zamia;


import android.content.Context;
import android.os.Handler;
import android.util.Log;

public class ZamiaExportCitationReader extends ZamiaCitationReader {
	

	private Handler handUpdateProcess;
		
	
	public ZamiaExportCitationReader(Context c, long projectId) {
	
		super(c, projectId);
		
		
	}
	
	public ZamiaExportCitationReader(Context c, long projectId, Handler handUpdateProcess) {
		
		super(c, projectId);
		
		this.handUpdateProcess=handUpdateProcess;
		
		
	}


	
	@Override
	public boolean createObservationDate(String date) {
		
		boolean repeated=false;
		
		if(handUpdateProcess!=null) handUpdateProcess.sendMessage(handUpdateProcess.obtainMessage());
	
		if(!secondLevelFields){
			
			Log.i("Cit",sampleId+"Date: "+date);

			repeated=citCnt.updateCitationDate(sampleId,date);

				
		}
		else{
			
			
			Log.i("Cit",secondLevelSampleId+"Date: "+date);

			repeated=slSC.updateCitationDate(secondLevelSampleId,date);
		
		}
		
		return repeated;
		
	}
	
	
	@Override
	public void createDatumFields(String tempVal, String name, String label,String category) {
	

		lastFieldValue=tempVal;
		
		if(!secondLevelFields){

//			Log.i("Cit","Create Datum Fields "+name+" "+tempVal+" ");
//    		super.createDatumFields(tempVal, name, label, category);
//	 		Log.i("Cit","Sample: "+this.sampleId+":"+name+" "+tempVal+" ");
	
			
			long fieldId= projCnt.getFieldIdByName(projectId,name);

			if(fieldId>0) citCnt.addCitationField(projectId,this.sampleId, this.projectId, name, tempVal);
			

				
		}
		else{
					
			//obtenir identificador camp
			long fieldId=slPC.getSLId(projectId, lastFieldValue);
			
			Log.i("Cit","Create SL "+name+" "+tempVal+" with field: "+fieldId);

			slSC.addCitationField(fieldId,this.secondLevelSampleId, this.projectId, name, tempVal);
			
			
		}
		
	
		
	
	}

	
	

}
