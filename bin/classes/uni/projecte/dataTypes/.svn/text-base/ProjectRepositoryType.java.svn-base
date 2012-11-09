package uni.projecte.dataTypes;

import org.json.JSONArray;
import org.json.JSONException;

public class ProjectRepositoryType {
	
	private String projId;
	private String projName;
	private String projType;
	private String projDescription;
	private String thName;
	private String thFilum;
	private String thSource;
	


	public ProjectRepositoryType(String projId,String projName, String projType, String projDesc,String thName,String thFilum,String thSource){
		
		this.projId=projId;
		this.projName=projName;
		this.projType=projType;
		this.projDescription=projDesc;
		this.thName=thName;
		this.thFilum=thFilum;
		this.thSource=thSource;
	}
	
	public ProjectRepositoryType(JSONArray nameArray,JSONArray valArray) {
		
		for(int i=0; i<nameArray.length(); i++){
			
			try {
			
				
			String name=nameArray.getString(i);
			
			
			if(name.equals("projType")){
				
				projType=valArray.getString(i);
				
			}
			else if(name.equals("projId")){
				
				projId=valArray.getString(i).replace(".xml", "");
				
			}
			else if(name.equals("projectName")){
			
				projName=valArray.getString(i);
			
			}
			else if(name.equals("projDescription")){
			
				projDescription=valArray.getString(i);
			
			}
			else if(name.equals("thId")){
		
				thName=valArray.getString(i);
		
			}
			else if(name.equals("thCat")){
				
				thFilum=valArray.getString(i);
				
			}
			else if(name.equals("source")){
				
				thSource=valArray.getString(i);
				
			}
			
			
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		
	}
	
	public String getProjName() {
		return projName;
	}
	public void setProjName(String projName) {
		this.projName = projName;
	}
	public String getProjType() {
		return projType;
	}
	public void setProjType(String projType) {
		this.projType = projType;
	}
	public String getProjDescription() {
		return projDescription;
	}

	public void setProjDescription(String projDescription) {
		this.projDescription = projDescription;
	}
	
	public String getProjId() {
		return projId;
	}

	public void setProjId(String projId) {
		this.projId = projId;
	}

	public String getThName() {
		return thName;
	}

	public void setThName(String thName) {
		this.thName = thName;
	}

	public void setThFilum(String thFilum) {
		this.thFilum = thFilum;
	}

	public String getThFilum() {
		return thFilum;
	}

	public String getThSource() {
		return thSource;
	}

	public void setThSource(String thSource) {
		this.thSource = thSource;
	}

}
