package uni.projecte.dataLayer.CitationManager.Synchro;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import uni.projecte.controler.ProjectConfigControler;
import uni.projecte.controler.ProjectControler;
import uni.projecte.controler.SyncControler;
import uni.projecte.dataLayer.ProjectManager.objects.Project;
import android.content.Context;
import android.text.format.DateFormat;

public class SyncCitationManager {

	private Context baseContext;
	private SyncControler syncCnt;
	private ProjectControler projCnt;
	private ProjectConfigControler projCnf;

	private SyncRestApi remoteAPI;
	
	//private ArrayList<ProjectField> fieldList;
	private HashMap<String, Long> hashFieldList;
	
	private long projId;
	private String lastUpdate;
	
	
	public SyncCitationManager(Context baseContext){
		
		this.baseContext=baseContext;
		syncCnt=new SyncControler(baseContext);
		
		projCnt= new ProjectControler(baseContext);
        projCnf=new ProjectConfigControler(baseContext);
		
		remoteAPI= new SyncRestApi(baseContext);
		
		
		//hashFieldList=projCnt.getProjectFieldsHash(projId);
		//fieldList=projCnt.getAllProjectFields(projId);		   
				
	}
	
	
	public int getOutdatedLocalCitations(String projTag) {

		remoteAPI= new SyncRestApi(baseContext);
		
		loadProjectInfo(projTag);
		
		List<ZamiaCitation> citationList = syncCnt.syncroLocalCitations(projId, lastUpdate);
		
		for(ZamiaCitation citation: citationList){
			
		    remoteAPI.sendCitations(projTag,citation);

		}
	    
		return citationList.size();
		
	}
	
	public int getOutdatedRemoteCitations(String projectTag){
		
		remoteAPI= new SyncRestApi(baseContext);
				
		System.out.println("Remote Citations: Last update: "+lastUpdate);
		
		loadProjectInfo(projectTag);
		
		ArrayList<ZamiaCitation> citationList=remoteAPI.getAllCitations(projectTag,lastUpdate);
		
		syncCnt.syncroRemoteCitations(projId,citationList,hashFieldList);

		updateLastMod(projId);
		
		return citationList.size();
			
	}
	
	

	public ArrayList<Project> getRemoteProjectList() {
		
		remoteAPI= new SyncRestApi(baseContext);
		
		return remoteAPI.getProjectList();
		
	}

	private void loadProjectInfo(String projectTag){
		
		projCnt.loadResearchInfoByName(projectTag);
		
		projId=projCnt.getProjectId();
		
		hashFieldList=projCnt.getProjectFieldsHash(projId);
		
		lastUpdate=getProjLastMod(projId);
		
		
	}
	
	private void loadProjectInfo(long projId){
		
		hashFieldList=projCnt.getProjectFieldsHash(projId);
		lastUpdate=getProjLastMod(projId);
		
	}
	
	public void enableSyncroProject(long projId){
		
		remoteAPI= new SyncRestApi(baseContext);
		
		Date date = new Date();
		date.getDate();
		
		String lastSyncro=(String) DateFormat.format("yyyy-MM-dd kk:mm:ss", date);
		
		ZamiaProject project=new ZamiaProject();
	
		projCnt.loadProjectInfoById(projId);
		
		project.setProject_name(projCnt.getName());
		project.setSyncro_date(lastSyncro);
		project.setUser("utoPiC");

		remoteAPI.createRemoteProject(project);
		
		projCnf.setProjectConfig(projId,ProjectConfigControler.LAST_SYNCRO,lastSyncro);
		
	}
	
	
	private void updateLastMod(long projId){
		
	   Date date = new Date();
	   date.getDate();
		
	   projCnf.changeProjectConfig(projId,ProjectConfigControler.LAST_SYNCRO,(String) DateFormat.format("yyyy-MM-dd kk:mm:ss", date));
		
	}

	
	private String getProjLastMod(long projId){
		
		String lastUpdate=projCnf.getProjectConfig(projId,ProjectConfigControler.LAST_SYNCRO);
		
		return lastUpdate;
				
	}
	
	
	

}
