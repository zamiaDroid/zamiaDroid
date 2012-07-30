package uni.projecte.dataLayer.RemoteDBManager.objects;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

import android.content.Context;
import android.os.Environment;
import android.util.Log;
import uni.projecte.R;
import uni.projecte.controler.PreferencesControler;
import uni.projecte.dataLayer.RemoteDBManager.RemoteTaxon;
import uni.projecte.dataTypes.LocalTaxon;
import uni.projecte.dataTypes.LocalTaxonSet;
import uni.projecte.dataTypes.RemoteTaxonSet;
import uni.projecte.dataTypes.TotalTaxonSet;

public class RemoteTaxonsExport {
	
	private RemoteTaxonSet remoteList;
	private LocalTaxonSet localTaxonList;
	private ArrayList<RemoteTaxon> total;
	
	private String result;
	
	public RemoteTaxonsExport(RemoteTaxonSet remoteList,LocalTaxonSet localTaxonList, ArrayList<RemoteTaxon> totalTaxonList) {
		
		super();
		
		this.remoteList = remoteList;
		this.localTaxonList = localTaxonList;
		this.total = totalTaxonList;
		this.result="";
		
	}
	
	public void exportRemoteList(){
	
		Iterator<RemoteTaxon> it=remoteList.getTaxonList().iterator();
 		
 		while(it.hasNext()){
 			
 			RemoteTaxon rt=it.next();
 			
 			boolean taxonExists=localTaxonList.existsTaxon(rt.getCleanTaxon());
 			
 			if(taxonExists) result=result+  rt.getTaxon()+"\t"+"X\n";
 			else result=result+ rt.getTaxon()+"\t"+" \n";
		
 		}
 		
	}
	
	public void exportLocalList(){
		
		Iterator<LocalTaxon> it=localTaxonList.getTaxonList().iterator();
 		
 		while(it.hasNext()){
 			
 			LocalTaxon lt=it.next();
 			
 			boolean taxonExists=remoteList.existsTaxon(lt.getCleanTaxon());
 			
 			if(taxonExists) result=result+ lt.getTaxon()+"\t"+"X\n";
 			else result=result+ lt.getTaxon()+"\t"+" \n";
		
 		}
		
	}
	
	public void exportTotalList(){
		
		Iterator<RemoteTaxon> it=total.iterator();
 		
 		while(it.hasNext()){
 			
 			RemoteTaxon lt=it.next();
 			
 			boolean taxonExists=localTaxonList.existsTaxon(lt.getCleanTaxon());
			
 			if(lt.getTaxonId().equals("local") && localTaxonList.existsTaxon(lt.getCleanTaxon())) result=result+lt.getTaxon()+"\t"+" "+"\t"+"X"+"\n";
 			else if(taxonExists)result=result+lt.getTaxon()+"\t"+"X"+"\t"+"X"+"\n"; 
 			else result=result+ lt.getTaxon()+"\t"+"X"+"\t"+" "+"\n";
		
 		}
 
		
	}
	
	public void stringToFile(String fileName, Context c){
		
		
		try {
			
		    File root = Environment.getExternalStorageDirectory();
		    PreferencesControler pC=new PreferencesControler(c);
		    
		    if (root.canWrite()){
		    	
		        File gpxfile = new File(pC.getReportPath(), fileName+".tab");
		      
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
