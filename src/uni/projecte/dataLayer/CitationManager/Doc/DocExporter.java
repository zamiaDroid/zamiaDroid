package uni.projecte.dataLayer.CitationManager.Doc;


import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import android.content.Context;
import android.os.Environment;
import android.util.Log;
import uni.projecte.controler.PreferencesControler;
import uni.projecte.dataLayer.CitationManager.CitationExporter;

public class DocExporter extends CitationExporter {
	
	private String document;
	private String citation;
	private String defaultTag="OriginalTaxonName";
	private String defaultLabel;
	private String defaultTagValue;

	
	public DocExporter(String projectName, String thesaurusName,String projectType) {

		super(projectName, thesaurusName, projectType);
		document="";
		
	}
	
	@Override
	public void openCitation(){

		citation="";
		
	}
	
	
	private void createSummary(){
		
		//Project: projectName
		//ProjectType: projectType
		//Data Creació: 
		
	}
	
	@Override
	public void closeCitation(){

		document+=defaultLabel+": "+defaultTagValue+"\n"+citation+"\n\n";
		
	}
	
	@Override
	public void createCitationField(String attName, String label, String value,String category){
		
		if(attName.equals(defaultTag)) {
			
			defaultTagValue=value;
			defaultLabel=label;
		}
		else{
			
			citation+=label+": "+value+"\n";			

		}
		
				
	}
	
	@Override
	public void writeCitationCoordinateLatLong(double latitude, double longitude) {

		citation+="Lat/Long: "+latitude+" "+longitude+"\n";
	
	}

	

	@Override
	public void writeCitationCoordinateUTM(String utmShortForm) {

		citation+="UTM: "+utmShortForm.replace("_"," ")+"\n\n";
	
	}


	@Override
	public void writeCitationDate(String date) {
		
		citation+=date+"\n";
			
	}
	
	@Override
	public void openDocument(){
		
		
	}
	
	@Override
	public void closeDocument(){
		
		setFormat(".txt");
		setResult(document);
		
	}

	public String getDefaultTag() {
		return defaultTag;
	}

	public void setDefaultTag(String defaultTag) {
		this.defaultTag = defaultTag;
	}
	
	public void stringToFile(String fileName, Context c){
		
		
		try {
			
		    File root = Environment.getExternalStorageDirectory();
		    PreferencesControler pC=new PreferencesControler(c);
		    
		    if (root.canWrite()){
		    	
		        File gpxfile = new File(Environment.getExternalStorageDirectory()+"/"+pC.getDefaultPath()+"/Reports/", fileName+format);
		      
		        FileWriter gpxwriter = new FileWriter(gpxfile);
		        BufferedWriter out = new BufferedWriter(gpxwriter);
		        out.write(result);
		        out.close();
		        
		    }
		} catch (IOException e) {
			
		    Log.e("File EXPORT", "Could not write file " + e.getMessage());
		}
		
		
		
	}
	

}
