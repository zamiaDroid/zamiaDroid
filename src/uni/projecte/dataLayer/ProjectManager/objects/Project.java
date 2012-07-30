package uni.projecte.dataLayer.ProjectManager.objects;

public class Project {

	private long projId;
	private String projName;
	private String projType;
	private String thName;
	
	public Project(long projId,String projName, String projType, String thName) {
		super();
		this.projId=projId;
		this.projName = projName;
		this.projType = projType;
		this.thName = thName;
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
	
}
