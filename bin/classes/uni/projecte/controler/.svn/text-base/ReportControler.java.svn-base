package uni.projecte.controler;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

import uni.projecte.dataLayer.CitationManager.Doc.DocExporter;
import uni.projecte.dataLayer.CitationManager.Fagus.FagusExporter;
import uni.projecte.dataLayer.CitationManager.JSON.JSONExporter;
import uni.projecte.dataLayer.CitationManager.KML.KMLExporter;
import uni.projecte.dataLayer.CitationManager.Tab.TABExporter;
import uni.projecte.dataLayer.CitationManager.Zamia.ZamiaCitationExporter;
import uni.projecte.dataLayer.bd.CitacionDbAdapter;
import uni.projecte.dataLayer.bd.SampleDbAdapter;
import uni.projecte.dataTypes.ProjectField;
import android.content.Context;
import android.database.Cursor;
import android.os.Handler;
import android.util.Log;

public class ReportControler extends CitationControler {

	public ReportControler(Context baseContext) {

		super(baseContext);

	}
	
	public int exportProject(long projId,Set<Long> selectionIds,String fileName, String exportFormat, Handler handlerExportProcessDialog, long fieldId){
		
		
		ProjectControler projCnt= new ProjectControler(baseContext);
		sC= new CitationControler(baseContext);
	
		projCnt.loadProjectInfoById(projId);
		
		HashMap<Long, ProjectField> projectFields=projCnt.getProjectFieldsMap(projId);

		
		//Depending on the chosen type of file we'll instantiate the concrete exporter subclass
		if(exportFormat.equals("reportDocumentLabel")){
			
			cExporter=new DocExporter(projCnt.getName(),projCnt.getThName(),projCnt.getCitationType());
			((DocExporter)cExporter).setDefaultTag(projectFields.get(fieldId).getName());
			
		}
		
		cExporter.openDocument();

		Log.d("Citations","Creating Report (Start) "+exportFormat);
		
		CitacionDbAdapter citationAdapter = new CitacionDbAdapter(baseContext);
		citationAdapter.open();

		Iterator<Long> iter = selectionIds.iterator();
				
		 while (iter.hasNext()) {
			
 		  	long citationId=iter.next();
			 
			Cursor citations= citationAdapter.fetchCitationByCitationId(citationId);
			KEY_DATA=citations.getColumnIndex(SampleDbAdapter.DATE);
			
			exportCitation(citations, projectFields);
			
			handlerExportProcessDialog.sendMessage(handlerExportProcessDialog.obtainMessage());
						
			citations.close();
			
		}
		
		citationAdapter.close();
		
		Log.d("Citations","Creating Report (End) "+exportFormat);
		
		
		cExporter.closeDocument();
				
		cExporter.stringToFile(fileName,baseContext);
		
		
		return selectionIds.size();
		
	}
	
	

}
