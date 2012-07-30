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
import java.util.ArrayList;
import java.util.Iterator;

import org.xmlpull.v1.XmlSerializer;

import android.content.Context;
import android.database.Cursor;
import android.os.Environment;
import android.util.Log;

import uni.projecte.dataLayer.CitationManager.Zamia.ZamiaCitationReader;
import uni.projecte.dataLayer.CitationManager.Zamia.ZamiaCitationXMLparser;
import uni.projecte.dataLayer.ProjectManager.ZamiaProjectWriter;
import uni.projecte.dataLayer.ProjectManager.xml.ZamiaProjectXMLparser;
import uni.projecte.dataLayer.ThesaurusManager.ThesaurusElement;
import uni.projecte.dataLayer.bd.AttributeDbAdapter;
import uni.projecte.dataLayer.bd.ProjectDbAdapter;
import uni.projecte.dataLayer.bd.ResearchDbAdapter;
import uni.projecte.dataLayer.utils.FileUtils;
import uni.projecte.dataLayer.utils.IoUtils;
import uni.projecte.dataTypes.CitationPhoto;
import uni.projecte.dataTypes.Compress;
import uni.projecte.dataTypes.ProjectField;



public class BackupControler {
	
	private static String photoPath="Backups/photos/";
	private String zamiaDroidPath;
	
	
	private ResearchDbAdapter rsDB;
	private AttributeDbAdapter atDB;
	private ProjectDbAdapter prDB;
	
	private Context baseContext;
	private PreferencesControler prefC;
	
	
	
	

	
	public BackupControler(Context context){
		
		this.baseContext=context;
		prefC= new PreferencesControler(baseContext);
		zamiaDroidPath=prefC.getDefaultPath();
		
	}
	
		
	
	/*
	 * 
	 * copy db's projects from version2 to version3
	 * 
	 */	
	public void copyProjects(){
		
		rsDB= new ResearchDbAdapter(baseContext);
		prDB= new ProjectDbAdapter(baseContext);	
		atDB= new AttributeDbAdapter(baseContext);
		
		
		rsDB.open();
		atDB.open();
		prDB.open();
		
		
		Cursor projects=rsDB.fetchAllResearches();
		
		projects.moveToFirst();
		
		while(!projects.isAfterLast()){
			
			long projId=prDB.createProject(projects.getString(1), projects.getString(2),"");
			
			Cursor attributes=atDB.fetchAllFieldsFromProjectWithDefaultOrder(projId);
			attributes.moveToFirst();
			
			prDB.startTransaction();
	
				while(!attributes.isAfterLast()){
				
					boolean visible=true;
					int intVis=attributes.getInt(7);
					if(intVis==0) visible=false;
					
					prDB.createField(projId, attributes.getString(2), attributes.getString(4), attributes.getString(8), attributes.getString(5), attributes.getString(3),attributes.getString(6),visible);
				
					attributes.moveToNext();
			
				}
				
				
			prDB.endTransaction();
			
			attributes.close();
			
			projects.moveToNext();
			
		}
		
		projects.close();
		
		rsDB.deleteDatabase();
		
		atDB.deleteDatabase();
		
		rsDB.close();
		atDB.close();
		
		prDB.close();
		
				
	}
	
	public void clearTH(){
		
		ThesaurusControler tC= new ThesaurusControler(baseContext);
	
		tC.removeThList();
		
		
	}
	

	
	public XmlSerializer writeZamiaProject(long projId, ZamiaProjectWriter zpW, boolean includeCitationStructure){
		
		
		ProjectControler pC= new ProjectControler(baseContext);
		ProjectSecondLevelControler slP= new ProjectSecondLevelControler(baseContext);
		DataTypeControler dtC= new DataTypeControler(baseContext);
		ThesaurusControler tC=new ThesaurusControler(baseContext);
		
		pC.loadProjectInfoById(projId);
		

		if(includeCitationStructure) zpW.openCitationDocument();
		else zpW.openDocument();
		
		zpW.openProject(pC.getName(), prefC.getLang());
			
		zpW.setDescription("");
		zpW.setProjectType(pC.getProjType());
		
		
		ThesaurusElement th=tC.loadThInfo();
		
		if(th!=null){
				  	
			zpW.setThesaurus(th.getThType(),pC.getThName(),th.getSourceId(),th.getSourceType());
	  	
		}
	
			
		zpW.openFieldList();
	
		ArrayList<ProjectField> fieldList=pC.getProjFields(projId);
		Iterator<ProjectField> fieldIterator=fieldList.iterator();
		
		//for
		while (fieldIterator.hasNext()) {
			
			ProjectField pF=fieldIterator.next();
			zpW.openField(pF.getLabel(), pF.getName(), pF.getDesc(),pF.getType(),pF.getValue());
			
			if(pF.getType().equals("secondLevel")){
				
				ArrayList<ProjectField> slList= slP.getProjFields(pF.getId());
				Iterator<ProjectField> slFieldIterator=slList.iterator();
				
				zpW.openSLFields();
				
				while(slFieldIterator.hasNext()){
										
					ProjectField sPF=slFieldIterator.next();
					zpW.openField(sPF.getLabel(), sPF.getName(), sPF.getDesc(),sPF.getType(),sPF.getValue());
					
					if(sPF.getType().equals("complex")){
						
						zpW.addItemsList(dtC.getItemsbySecondLevelFieldId(sPF.getId()));
						
					}
					
					zpW.closeField();
					
				}
				
				zpW.closeSLFields();
				
			}
			else if(pF.getType().equals("complex")){
				
				zpW.addItemsList(dtC.getItemsbyFieldId(pF.getId()));
				
			}
			
			zpW.closeField();
			
		}
		
		zpW.closeFieldList();
		
		zpW.writeCitationCoordinate();
		zpW.closeProject();
		
		
		if(!includeCitationStructure) zpW.closeDocument();
		
		return zpW.getSerializer();

		
	}
	
	
	public String backupProject(long projId, String fileName){
		
		ZamiaProjectWriter zpW= new ZamiaProjectWriter();	
		
			writeZamiaProject(projId, zpW, false);
	
		zpW.writeToFile(zpW.convertXML2String(), fileName, baseContext);
		
		return zpW.convertXML2String();
		
		
	}
	
	public void backupProjectCitations(long projId, String fileName){
		
		CitationControler sC= new CitationControler(baseContext);
		sC.exportProject(projId, baseContext, fileName, "Zamia");
		
	}
	
	private String getPhotoTempBackupPath(){
		
		return Environment.getExternalStorageDirectory() +"/"+zamiaDroidPath+"/"+photoPath;
		
	}
	
	private String getZippedPhotosPath(){
		
		return Environment.getExternalStorageDirectory() +"/"+zamiaDroidPath+"/Backups/photos.zip";

	}
	
	private int backupProjectPhotos(long projId) {

		String photoLocation = getPhotoTempBackupPath(); 
	 	File f = new File(photoLocation);
		
	 	if(!f.exists())	f.mkdir();
		else{ 
			
			FileUtils.deleteRecursive(f);
			f.mkdir();		
		}
				
		PhotoControler photoCnt= new PhotoControler(baseContext);
		ArrayList<CitationPhoto> photoList=photoCnt.getPhotoCitationPhotos(projId);
		
		Iterator<CitationPhoto> itCitPhoto=photoList.iterator();
		
		while(itCitPhoto.hasNext()){
			
			CitationPhoto tmpPhoto=itCitPhoto.next();
			
			File originFile=new File(tmpPhoto.getPhotoPath());
			FileUtils.copyFileToDir(originFile, new File(photoLocation+originFile.getName()));
						
		}
		
		String[] filesInside=f.list();
		int size=filesInside.length;
		
		return size;
		
	}
	

	
	
	/*
	 * This method creates a Backup of the Project structure and citations contained on a zipFile
	 * 
	 */
	
	public int exportProject(long projId,String fileName,boolean backupPhotos){
		
		backupProject(projId, "zp_"+fileName);
		
		backupProjectCitations(projId, "zc_"+fileName);
		
		if(backupPhotos){ 
			
			int photosMoved=backupProjectPhotos(projId);
			if(photosMoved==0) backupPhotos=false;
				
		}
		
		long size= zipFiles(fileName,backupPhotos);
		
		if(size>0) return 1;
		else return (int) size;
			
		
	}
	
	



	/*
	 *
	 * This method restores a Backup called @fileName. If @projId is < 0 the project contained on the backup will be created
	 * with @projName. When @projId > 0 the citations will be loaded on the project with @projID.   
	 *  
	 */
	
	public long importProject(String fileName, long projId, String projName){
		
		Log.i("Backup","(A) Action: Import Backup | Info: filename: "+fileName+ " projName: "+projName);

		
		boolean error=false;	
		unZipFilesToTemp(fileName);
		
		
		if(projId<0){
			
			Log.i("Backup","(B) Action: Creating Project: zp_"+fileName+".xml");

			
			ProjectZamiaControler zpControler= new ProjectZamiaControler(baseContext);
		
			zpControler.createProject(projName,"", "");
			zpControler.setAutoFields(false);
			
			ZamiaProjectXMLparser zpXM= new ZamiaProjectXMLparser(zpControler);
			zpXM.readXML(baseContext,prefC.getBackupPath()+"unzipped/zp_"+fileName+".xml",false);
			error=zpXM.isError();
			
			projId=zpControler.getProjId();
			
			Log.i("Backup","(B) Action: Project created | Info: error="+error);


		}
		
			if(!error){
		
				Log.i("Backup","(B) Action: Creating Citations: zc_"+fileName+".xml");
		
			  	ZamiaCitationReader zR= new ZamiaCitationReader(baseContext, projId);
				ZamiaCitationXMLparser zCP= new ZamiaCitationXMLparser(zR);
				zCP.readXML(baseContext, prefC.getBackupPath()+"unzipped/zc_"+fileName+".xml", false);
			
				error=zCP.isError();
			
			}
			
		if(error) return -1;
		else{ 
			
	
			
			String unzippedPath=prefC.getBackupPath() + "unzipped/";
			
			File f=new File(unzippedPath+"photos.zip");
			
			if(f.exists()){
				
				Log.i("Backup","(C) Action: Extracting photos: photos.zip");

				
				importPhotos(f);
				f.delete();
								
			}
			
			else{
				
				Log.i("Backup","(C) Action: Extracting photos: no photos' file found");
				
			}
			
			
			return projId;
			
		
		}

		
	}
	
	
	private void importPhotos(File f) {

		//Unzip photos.zip
		//move photos to default Path
		
		PhotoControler photoCnt=new PhotoControler(baseContext);
		
		IoUtils.unpackZip(f.getAbsolutePath(), photoCnt.getMainPhotoPath());
	
		
	}



	/*
	 *  This method encapsulates citation and project backup file inside a unique compressed ZIP file
	 *  with the name @fileName  
	 * 
	 */
	private long zipFiles(String fileName, boolean backupPhotos){
		

		String zipName = prefC.getBackupPath()+fileName+".zip";
		
		String[] files;

		
		if(backupPhotos) {
			
			Compress c=new Compress(new File(getPhotoTempBackupPath()).list(), getZippedPhotosPath());
			c.zipWithRelativePath(getPhotoTempBackupPath());
			
			files = new String[] {prefC.getCitationsPath()+"zc_"+fileName+".xml",prefC.getBackupPath()+"zp_"+fileName+".xml",getZippedPhotosPath()};
		
		}
		else{
			
			files = new String[] {prefC.getCitationsPath()+"zc_"+fileName+".xml", prefC.getBackupPath()+"zp_"+fileName+".xml"};
			
		}
		
		Compress c = new Compress(files, zipName);
		c.zip();
		
		
		File file = new File(files[0]);
		file.delete();
		
		file = new File(files[1]);
		file.delete();
		
		if(backupPhotos){ 
			
			file=new File(getZippedPhotosPath());
			file.delete();
			
			FileUtils.deleteRecursive(new File(getPhotoTempBackupPath()));
			
		}

		
		file = new File(zipName);
		
		return file.length();

		
		
	}
	
	/*
	 *  This method extracts the citation and project backup file with the name @fileName  
	 * 
	 */
	
	private void unZipFilesToTemp(String fileName){
		
		String zipFile = Environment.getExternalStorageDirectory() +"/ZamiaDroid/Backups/"+fileName+".zip"; 
		String unzipLocation = getUnzippedPath(); 
		 	
		IoUtils.unpackZip(zipFile,unzipLocation);
		
		Log.i("Backup","(B) Action: Unzipping Project "+fileName+".zip");
			
	}
	
	private String getUnzippedPath(){
		
		return Environment.getExternalStorageDirectory() + "/ZamiaDroid/Backups/unzipped/";
		
	}


}
