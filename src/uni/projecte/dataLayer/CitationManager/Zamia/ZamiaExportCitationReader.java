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
	
}
