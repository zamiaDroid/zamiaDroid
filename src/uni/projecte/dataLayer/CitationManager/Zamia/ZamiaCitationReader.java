package uni.projecte.dataLayer.CitationManager.Zamia;

import uni.projecte.controler.ProjectSecondLevelControler;
import uni.projecte.controler.CitationSecondLevelControler;
import uni.projecte.dataLayer.CitationManager.Fagus.FagusReader;
import android.content.Context;
import android.util.Log;

public class ZamiaCitationReader extends FagusReader {
	
	protected CitationSecondLevelControler slSC;
	protected ProjectSecondLevelControler slPC;
	protected boolean secondLevelFields=false;
	protected String lastFieldValue;
	protected long secondLevelSampleId;
	
	
	public ZamiaCitationReader(Context c, long projectId){
		
		super(c, projectId);
		
		slSC= new CitationSecondLevelControler(c);
		slPC= new ProjectSecondLevelControler(c);
		slSC.startTransaction();

		
	}
	
	public ZamiaCitationReader(Context c, long projectId,boolean createFields){
		
		super(c, projectId);
	
		slSC= new CitationSecondLevelControler(c);
		slPC= new ProjectSecondLevelControler(c);
		slSC.startTransaction();

		createFields=false;

		
	}
	

	public void createNewSample() {

		
		if(!secondLevelFields){
			

			this.sampleId=citCnt.createEmptyCitation(projectId);
			
			Log.i("Cit",this.sampleId+" : create Empty Citation:");

				
		}
		else{
			
			
			this.secondLevelSampleId=slSC.createEmptyCitation(lastFieldValue);
			
			Log.i("Cit",this.secondLevelSampleId+" : create Empty Citation SL:");
		}
		
		numSamples++;
				
	}
	
	public void setSecondLevelFields(boolean sLFields){
		
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
			
			//Log.i("Cit",sampleId+"Date: "+date);

			repeated=citCnt.updateCitationDate(sampleId,date);

				
		}
		else{
			
			
			//Log.i("Cit",secondLevelSampleId+"Date: "+date);

			repeated=slSC.updateCitationDate(secondLevelSampleId,date);
		
		}
		
		return repeated;
		
	}
	
	@Override
	public void createDatumFields(String tempVal, String name, String label,String category) {
	
		if(!tempVal.equals("      ") && !tempVal.equals("        ")){

			lastFieldValue=tempVal;
			
			if(!secondLevelFields){
	
				//Log.i("Cit","Create Datum Fields "+name+" "+tempVal+" ");
				
				super.createDatumFields(tempVal, name, label, category);
	
					
			}
			else{
				
				/*
				 * It's necessary to create the field if it doesn't exist.
				 * 
				 */
				
			/*	if(name.equals("photo")) {
					
					if(numSamples<2) rC.createField(projectId,name,label,category,"photo",visible);
	
					
				}
				else{
	
					if(numSamples<2) rC.createField(projectId,name,label,category,"simple",visible);
				
				}*/
				
	
				
				//obtenir identificador camp
				
			
				long fieldId=slPC.getSLId(projectId, lastFieldValue);
				
				//Log.i("Cit","Create SL "+name+" "+tempVal+" with field: "+fieldId);
	
				
				slSC.addCitationField(fieldId,this.secondLevelSampleId, this.projectId, name, tempVal);
				
				
			}
		
		}
		else lastFieldValue="";
	
		
	
	}

	
	

}
