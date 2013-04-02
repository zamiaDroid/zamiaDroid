package uni.projecte.dataLayer.CitationManager;

import java.io.File;

import uni.projecte.controler.PreferencesControler;
import uni.projecte.dataLayer.utils.MimeTypes;
import android.content.Context;
import android.os.Environment;

public class FileExporter {
	
	private String exportMimeType="text/xml";
	private Context baseContext;
	private PreferencesControler pC;
	private File file;
	private String fileName;
	private String format;
	
	
	public FileExporter(Context context){
		
		this.baseContext=context;
		pC=new PreferencesControler(baseContext);
		
	}
	

	
	
	/*
	 * Creates a file according to @format provided and @fileName
	 * 
	 * @return true if file exists
	 * 
	 */
	
	public boolean createFile(String format, String fileName){
		
		
		if(format.equals("Fagus") || format.equals("Zamia")){
			
	        file = new File(Environment.getExternalStorageDirectory()+"/"+pC.getDefaultPath()+"/Citations/", fileName+".xml");
	        exportMimeType=MimeTypes.xmlMimeType;

			
		}
		else if(format.equals("KML")){
			
		       file = new File(Environment.getExternalStorageDirectory()+"/"+pC.getDefaultPath()+"/Citations/", fileName+".kml");
		       exportMimeType=MimeTypes.kmlMimeType;
			
		}
		else if(format.equals("JSON")){
			
		       file = new File(Environment.getExternalStorageDirectory()+"/"+pC.getDefaultPath()+"/Citations/", fileName+".json");
		        exportMimeType=MimeTypes.jsonMimeType;
			
		}
		else if(format.equals("presenceReport")){
			
		       file = new File(pC.getReportPath(),fileName+".tab");
		        exportMimeType=MimeTypes.tabMimeType;
			
		}
		else if(format.equals("reportDocumentLabel")){
			
		       file = new File(pC.getReportPath(),fileName+".txt");
		        exportMimeType=MimeTypes.txtMimeType;
			
		}
		else{
			
	        file = new File(Environment.getExternalStorageDirectory()+"/"+pC.getDefaultPath()+"/Citations/", fileName+".tab");
	        exportMimeType=MimeTypes.tabMimeType;
			
		}
		
		this.fileName=fileName;
		this.format=format;
		
		return file.exists();
		
		
	}


	public void setExportMimeType(String exportMimeType) {
		this.exportMimeType = exportMimeType;
	}


	public String getExportMimeType() {
		return exportMimeType;
	}


	public File getFile() {
		return file;
	}


	public void setFile(File file) {
		this.file = file;
	}


	public void setFileName(String fileName) {
		this.fileName = fileName;
	}


	public String getFileName() {
		return fileName;
	}


	public String getFormat() {
		return format;
	}


	public void setFormat(String format) {
		this.format = format;
	}
	
	
	
	

}
