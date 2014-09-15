package uni.projecte.controler;

import java.util.ArrayList;
import java.util.HashMap;

import uni.projecte.dataLayer.ProjectManager.objects.Project;

import android.content.Context;

public class SyncProjectControler  {

	private Context baseContext;
	
	private ProjectConfigControler projCnfCnt;
	private ProjectControler projCnt;
	

	public SyncProjectControler(Context c) {

		this.baseContext=c;
		projCnfCnt= new ProjectConfigControler(baseContext);
		projCnt= new ProjectControler(baseContext);
		
		
	}
	
	public HashMap<String,Project> getAllSyncroProjects(){
		
		ArrayList<Project> projectList=projCnfCnt.getAllProjConfig(ProjectConfigControler.LAST_SYNCRO);
		HashMap<String, Project> projMap=new HashMap<String, Project>();
		
		for(Project projTmp: projectList){
			
			projCnt.loadProjectInfoById(projTmp.getProjId());
			
			String projName=projCnt.getName();
			projTmp.setProjName(projName);

			projMap.put(projName,projTmp);
			
		}
				
		return projMap;
		
	}
	
	
	
	
	public boolean isSyncroProj(){
		
		return true;
	}
	
	public void updateLastSyncro(){
		
		
	}

	


}
