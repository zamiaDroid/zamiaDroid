package uni.projecte.controler;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import edu.ub.bio.biogeolib.CoordConverter;
import edu.ub.bio.biogeolib.CoordinateLatLon;
import edu.ub.bio.biogeolib.CoordinateUTM;

import uni.projecte.dataLayer.CitationManager.Synchro.SyncroUpdater;
import uni.projecte.dataLayer.CitationManager.Synchro.ZamiaCitation;
import uni.projecte.dataLayer.ProjectManager.objects.Project;
import uni.projecte.dataLayer.bd.CitacionDbAdapter;
import uni.projecte.dataLayer.utils.UTMUtils;
import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;



public class SyncControler extends CitationControler {

	public static String REMOVED_CITATION="removed";
	
	
	public SyncControler(Context baseContext) {
	
		super(baseContext);

	}
	
	
	private void updateProgress(Handler updateStateHandler, int progress){
		
		Message msg = updateStateHandler.obtainMessage();
		Bundle bundle = new Bundle();
		
		bundle.putInt("state", SyncroUpdater.STATE_SET_PROGRESS);
		bundle.putInt("progress", progress);
		
		msg.setData(bundle);
		
		updateStateHandler.sendMessage(msg);
		
	}
	
	public void syncroRemoteCitations(long projId, ArrayList<ZamiaCitation> citationList, HashMap<String, Long> fieldList, Handler updateStateHandler) {

		mDbAttributes = new CitacionDbAdapter(baseContext);
		mDbAttributes.open();
		
		int i=1;
		
		for(ZamiaCitation citation: citationList){
			
			//cerquem per zamiaId
			long citationId=mDbAttributes.citationExists(projId,citation.getObservationDate());
			
			System.out.println("Found: "+citationId);

			if(citation.getState().equals(REMOVED_CITATION)){
				
				if(citationId>=0){
					
					System.out.println("Esborrant: "+citationId);
					deleteCitation(citationId);
						
				}
				
			}
			else{
			
				if(citationId<0){
	
					createSyncroCitation(mDbAttributes, projId, citation,fieldList);
	
				}
				else{
					
					System.out.println("Modfify: "+citationId);
					modifySyncroCitation(mDbAttributes, projId, citation,citationId,fieldList);
					
				}
			}
			
			updateProgress(updateStateHandler,i);
			i++;
						
		}
		
		mDbAttributes.close();
		
	}
	
	private void modifySyncroCitation(CitacionDbAdapter citationAdapter,long projId, ZamiaCitation citation, long citationId, HashMap<String, Long> fieldList) {

		//1) FETA: Solució agressiva. overwritte petem tots els camps!
		//2) Mirem els canvis de Citation i CitationValue

		//startTransaction();
	
		mDbAttributes.updateLocation(citationId, citation.getLatitude(), citation.getLongitude());
		
		//EndTransaction();

		//startTransaction();

		mDbAttributes.updateSampleFieldValue(citationId, fieldList.get("OriginalTaxonName"), citation.getOriginalTaxonName());
		mDbAttributes.updateSampleFieldValue(citationId, fieldList.get("Sureness"), citation.getSureness());
		if(fieldList.get("Natureness")!=null) mDbAttributes.updateSampleFieldValue(citationId, fieldList.get("Natureness"), citation.getNatureness());
		if(fieldList.get("Phenology")!=null) mDbAttributes.updateSampleFieldValue(citationId, fieldList.get("Phenology"), citation.getPhenology());
		if(fieldList.get("CitationNotes")!=null) updateCitationField(citationId, fieldList.get("CitationNotes"), citation.getCitationNotes(),"CitationNotes");
		if(fieldList.get("altitude")!=null) mDbAttributes.updateSampleFieldValue(citationId, fieldList.get("altitude"), citation.getAltitude());
		if(fieldList.get("ObservationAuthor")!=null) updateCitationField(citationId, fieldList.get("ObservationAuthor"), citation.getObservationAuthor(),"ObservationAuthor");
		if(fieldList.get("Locality")!=null) updateCitationField(citationId, fieldList.get("Locality"), citation.getLocality(), "Locality");

		
		
		mDbAttributes.updateLastModDate(citationId);

		//EndTransaction();
		
	}


	private boolean createSyncroCitation(CitacionDbAdapter citationAdapter, long projId, ZamiaCitation citation, HashMap<String, Long> fieldList){
		
		String zamiaId=generateZamiaID("utoPiC", citation.getObservationDate().trim());
		System.out.println(citation.getId()+" ]...[ "+zamiaId);

		long citationId=citationAdapter.createCitationWithDate(projId, citation.getLatitude(), citation.getLongitude(), zamiaId,citation.getObservationDate());
				
		mDbAttributes.startTransaction();

			mDbAttributes.createCitationField(citationId, fieldList.get("OriginalTaxonName"), citation.getOriginalTaxonName(),"OriginalTaxonName");
			mDbAttributes.createCitationField(citationId, fieldList.get("Sureness"), citation.getSureness(), "Sureness");
			if(fieldList.get("Natureness")!=null) mDbAttributes.createCitationField(citationId, fieldList.get("Natureness"), citation.getNatureness(), "Natureness");
			if(fieldList.get("Phenology")!=null) mDbAttributes.createCitationField(citationId, fieldList.get("Phenology"), citation.getPhenology(), "Phenology");
			mDbAttributes.createCitationField(citationId, fieldList.get("CitationNotes"), citation.getCitationNotes(), "CitationNotes");
			if(fieldList.get("altitude")!=null) mDbAttributes.createCitationField(citationId, fieldList.get("altitude"), citation.getAltitude(), "altitude");
			if(fieldList.get("ObservationAuthor")!=null) mDbAttributes.createCitationField(citationId, fieldList.get("ObservationAuthor"), citation.getObservationAuthor(), "ObservationAuthor");
			if(fieldList.get("Locality")!=null) mDbAttributes.createCitationField(citationId, fieldList.get("Locality"), citation.getLocality(), "Locality");

			
			mDbAttributes.endTransaction();

		return true;
		
	}
	
	public List<ZamiaCitation> syncroLocalCitations(long projId, String lastUpdate, String userName){
		
		List<ZamiaCitation> citationList= new ArrayList<ZamiaCitation>();
		
		CitacionDbAdapter citationAdapter = new CitacionDbAdapter(baseContext);
		citationAdapter.open();
		   
		Cursor cursor= citationAdapter.fetchOutdatedCitations(projId,lastUpdate);

		while(!cursor.isAfterLast()){
			
			ZamiaCitation citation= new ZamiaCitation();
			
			citation.setInternalId(cursor.getLong(0));
			citation.setLatitude(cursor.getDouble(2));
			citation.setLongitude(cursor.getDouble(3));
			citation.setObservationDate(cursor.getString(4));
			citation.setId(generateZamiaID(userName, cursor.getString(4)));
			
			cursor.moveToNext();
		
			citationList.add(citation);
		}
		
		cursor.close();
		
		

		
		getOutdatedCitationsValues(citationAdapter,citationList);
		
		citationAdapter.close();
		
		
		
		return citationList;
		
	}

	private void getOutdatedCitationsValues(CitacionDbAdapter citationAdapter, List<ZamiaCitation> citationList) {
		
			
		for(ZamiaCitation citation: citationList){
		
			Cursor cursor= citationAdapter.fetchSampleAttributesBySampleId(citation.getInternalId());
			cursor.moveToFirst();
			
			while(!cursor.isAfterLast()){
				
				mapCitationFields(citation, cursor.getLong(2),cursor.getString(3),cursor.getString(4));								
				
				cursor.moveToNext();
			}
			
			cursor.close();
			
			CoordinateUTM utm = CoordConverter.getInstance().toUTM(new CoordinateLatLon(citation.getLatitude(),citation.getLongitude()));
			citation.setUtm(utm.getShortForm().replace("_", ""));
			
		}
		
		
	}

	private void mapCitationFields(ZamiaCitation citation, long projectFieldId,String value, String fieldName) {
		
		//System.out.println("\tField: "+fieldName+"="+value);
		
		if(fieldName.equals("OriginalTaxonName")){
			
			citation.setOriginalTaxonName(value);
			
		}
		else if(fieldName.equals("Sureness")){
			
			citation.setSureness(value);
			
		}
		else if(fieldName.equals("Natureness")){
				
			citation.setNatureness(value);
				
		}
		else if(fieldName.equals("Phenology")){
			
			citation.setPhenology(value);
			
		}
		else if(fieldName.equals("CitationNotes")){
			
			citation.setCitationNotes(value);
			
		}
		else if(fieldName.equals("Locality")){
			
			citation.setLocality(value);
			
		}
		else if(fieldName.equals("ObservationAuthor")){
			
			citation.setObservationAuthor(value);
			
		}
		else if(fieldName.equals("altitude")){
			
			citation.setAltitude(value);
			
		}
		
		
				
	}
	
	/*
     * SHA1 Function with userId and timestap creates our citation unique ID
     */
     
    public static String generateZamiaID(String userId,String timestamp) {

        String input=userId+"_"+timestamp;
        String output = "";

        MessageDigest mDigest;
        try {
            mDigest = MessageDigest.getInstance("SHA1");

            byte[] result = mDigest.digest(input.getBytes());
            StringBuffer sb = new StringBuffer();
            for (int i = 0; i < result.length; i++) {
                sb.append(Integer.toString((result[i] & 0xff) + 0x100, 16).substring(1));
            }

            output = sb.toString();

        } catch (NoSuchAlgorithmException e) {
            
            e.printStackTrace();
            
        }

        return output;
    }




	

}
