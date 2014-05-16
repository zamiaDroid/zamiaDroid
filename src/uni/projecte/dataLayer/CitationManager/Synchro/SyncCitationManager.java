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



package uni.projecte.dataLayer.CitationManager.Synchro;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import uni.projecte.controler.PreferencesControler;
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
	private PreferencesControler prefCnt;

	private SyncRestApi remoteAPI;
	
	private HashMap<String, Long> hashFieldList;
	
	private long projId;
	private String lastUpdate;
	private String service="";
	
	private User user;
	

	public SyncCitationManager(Context baseContext,String service){
		
		this.baseContext=baseContext;
		this.service=service.replace("remote_","");
		
		syncCnt=new SyncControler(baseContext);
		
		projCnt= new ProjectControler(baseContext);
        projCnf=new ProjectConfigControler(baseContext);
        prefCnt=new PreferencesControler(baseContext);
		
        user=prefCnt.getSyncroUser(this.service);
        
		remoteAPI= new SyncRestApi(baseContext,user);
				
	}
	
	public boolean isConfigured(){
		
		return user.isLogged();
		
	}
	
	
	public int getOutdatedLocalCitations(String projTag) {

		remoteAPI= new SyncRestApi(baseContext,user);
		
		loadProjectInfo(projTag);
		
		List<ZamiaCitation> citationList = syncCnt.syncroLocalCitations(projId, lastUpdate);
		
		for(ZamiaCitation citation: citationList){
			
		    remoteAPI.sendCitations(projTag,citation);

		}
	    
		return citationList.size();
		
	}
	
	public int getOutdatedRemoteCitations(String projectTag){
		
		remoteAPI= new SyncRestApi(baseContext,user);
				
		System.out.println("Remote Citations: Last update: "+lastUpdate);
		
		loadProjectInfo(projectTag);
		
		ArrayList<ZamiaCitation> citationList=remoteAPI.getAllCitations(projectTag,lastUpdate);
		
		syncCnt.syncroRemoteCitations(projId,citationList,hashFieldList);

		updateLastMod(projId);
		
		return citationList.size();
			
	}
	
	

	public ArrayList<Project> getRemoteProjectList() {
		
		remoteAPI= new SyncRestApi(baseContext,user);
		
		return remoteAPI.getProjectList();
		
	}
	


	public boolean checkLogin(String username, String password) {

		remoteAPI= new SyncRestApi(baseContext,user);
		
		boolean success=remoteAPI.checkLogin(username,password);
		
		prefCnt.setSyncroUsername(service,username);
		prefCnt.setSyncroPassword(service,password);
		
		
		return success;
		
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
	
	public String enableSyncroProject(long projId, boolean remote){
		
		remoteAPI= new SyncRestApi(baseContext,user);
		
		Date date = new Date();
		date.getDate();
		
		String lastSyncro=(String) DateFormat.format("yyyy-MM-dd kk:mm:ss", date);
		
		ZamiaProject project=new ZamiaProject();
	
		projCnt.loadProjectInfoById(projId);
		
		String projTag=projCnt.getName();
		
		project.setProject_name(projTag);
		project.setSyncro_date(lastSyncro);
		project.setUser("utoPiC");

		if(!remote) {
			
			remoteAPI.createRemoteProject(project);
			projCnf.setProjectConfig(projId,ProjectConfigControler.LAST_SYNCRO,lastSyncro);
			
		}
		else projCnf.setProjectConfig(projId,ProjectConfigControler.LAST_SYNCRO,"");
		
		return projTag;
		
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

	
	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	
	

}
