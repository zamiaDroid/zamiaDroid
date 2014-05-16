package uni.projecte.dataLayer.ProjectManager.objects;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import uni.projecte.controler.ProjectConfigControler;
import android.text.format.DateFormat;

public class Project {

	private long projId;
	private String projName;
	private String projType;
	private String thName;
	private int server_unsyncro;
	private String server_last_mod;
	



	public Project(long projId,String projName, String projType, String thName) {
		super();
		this.projId=projId;
		this.projName = projName;
		this.projType = projType;
		this.thName = thName;
	}
	
	public Project(long projId) {

		if(projId==0) this.projId=-1;
		else this.projId=projId;
	
	}
	

	public String getServer_last_mod() {
		return server_last_mod;
	}

	public void setServer_last_mod(String server_last_mod) {
		this.server_last_mod = server_last_mod;
	}
	
	public boolean isCreated(){
		
		return projId>=0;
		
	}

	public String getProjName() {
		return projName;
	}
	public String getProjType() {
		return projType;
	}
	public String getThName() {
		return thName;
	}
	public void setProjName(String projName) {
		this.projName = projName;
	}
	public void setProjType(String projType) {
		this.projType = projType;
	}
	public void setThName(String thName) {
		this.thName = thName;
	}

	public long getProjId() {
		return projId;
	}

	public void setProjId(long projId) {
		this.projId = projId;
	}
	
	public int getServer_unsyncro() {
		return server_unsyncro;
	}

	public void setServer_unsyncro(int server_unsyncro) {
		this.server_unsyncro = server_unsyncro;
	}
	
	public boolean isUpdated(String remote){
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd kk:mm:ss");

		Date date1 = null;
		Date date2 = null;
		
		if(server_last_mod.equals("")){
			
			return true;
			
		}
		else{
		
			try {
				date1 = sdf.parse(server_last_mod);
		    	date2 = sdf.parse(remote);
	
			} catch (ParseException e) {
	
				e.printStackTrace();
			}
		}
		return date1.compareTo(date2)>0;	
		
	}
	
}
