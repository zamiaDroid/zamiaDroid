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

import java.util.ArrayList;
import java.util.Iterator;

import android.content.Context;
import android.util.Log;

import uni.projecte.dataLayer.ProjectManager.xml.ZamiaProjectJSON;
import uni.projecte.dataLayer.ProjectManager.xml.ZamiaProjectXMLparser;
import uni.projecte.dataTypes.ProjectField;
import uni.projecte.dataTypes.ProjectRepositoryType;

public class ProjectZamiaControler {
	
	private String language;
	private String biologicalRecordType;
	private String projectName;
	private boolean hasLocation=false;
	private ProjectSecondLevelControler pC;

	private long projId;
	private long fieldId;
	
	private long polygonFieldId=-1;
	private long multiPhotoFieldId=-1;

	/* -------- */
	
	private ArrayList<ProjectField> subFieldsList;
		
	private Context baseContext;
	private boolean secondLevelField;
	
	private boolean complexType=false;
	
	private boolean hasRemoteTh=false;
	
	private String thServer;
	private String thName;

	private String thType;
	
	private boolean setPreviousTh=true;
	
	private PreferencesControler prefCnt;

	private static String baseURL="http://biodiver.bio.ub.es/ZamiaProjectProvider/GetZamiaProject?&remote_th=true&lang=";
	
	private String repositoryURL;
	private boolean autoFields;

	public ProjectZamiaControler(Context context){
		
		this.baseContext=context;

		pC=new ProjectSecondLevelControler(baseContext);
		prefCnt=new PreferencesControler(baseContext);
		
		String lang=prefCnt.getLang();
		repositoryURL=baseURL+lang;
		
		subFieldsList=new ArrayList<ProjectField>();
		
	}
	
	public ArrayList<ProjectRepositoryType> getZamiaProject(){
		
		ZamiaProjectJSON zpJSON=new ZamiaProjectJSON();
		zpJSON.connect(repositoryURL);
		
		ArrayList<ProjectRepositoryType> projectList=zpJSON.getList();
				
		return projectList;
		
	}
	
	public void downloadProject(String projName, boolean remote){
		
		ZamiaProjectXMLparser zpP=new ZamiaProjectXMLparser(this);
		
		if(remote){
			
			zpP.readXML(baseContext,repositoryURL+"&proj_id="+projName, remote);
			
		}
		else{			
			
			zpP.readXML(baseContext,projName, remote);
		}
		
	}

	public void addAutoFields(){
		
		if(autoFields) pC.addAutoFields(projId);
		
	}
	
	
	public void addProjectField(String fieldName, String fieldLabel, String fieldDesc, String fieldType, String value,String cat) {
		
		Log.e("ZPparser",fieldName+" : "+fieldLabel+" : "+value);
		
		if(cat.equals("")) cat="ADDED";
		
		
		if(fieldType.equals("text")){

			fieldId=pC.addProjectField(projId, fieldName, fieldLabel, fieldDesc, value, "simple", cat);
			
		}
		else if(fieldType.equals("thesaurus")){
			
			fieldId=pC.addProjectField(projId, fieldName, fieldLabel, fieldDesc, value, fieldType, cat);
			
		}
		else if(fieldType.equals("polygon")){
			
			fieldId=pC.addProjectField(projId, fieldName, fieldLabel, fieldDesc, value, fieldType, cat);
			polygonFieldId=fieldId;
			
		}
		else if(fieldType.equals("multiPhoto")){
			
			fieldId=pC.addProjectField(projId, fieldName, fieldLabel, fieldDesc, value, fieldType, cat);
			multiPhotoFieldId=fieldId;

			
		}
		else if(fieldType.equals("photo")){
			
			fieldId=pC.addProjectField(projId, fieldName, fieldLabel, fieldDesc, value, fieldType, cat);
			
		}
		else{
			
			fieldId=pC.addProjectField(projId, fieldName, fieldLabel, fieldDesc, value, fieldType, cat);
			
		}

		complexType=false;
		
	}
	
	public void createSecondLevelFields() {

		createSubProjectFields();
		createPolygonField();
		createMultiPhotoField();
		
	}
	
	private void createPolygonField() {

		if(polygonFieldId>=0) pC.createField(polygonFieldId, "polygonAltitude", "polygonAltitude", "", "", "text");
		
	}
	
	private void createMultiPhotoField() {

		if(multiPhotoFieldId>=0) pC.createField(multiPhotoFieldId,  "Photo", "photo", "", "", "text");
		
	}

	public void createSubProjectFields(){
		
		Iterator<ProjectField> itr = subFieldsList.iterator();
		
		while(itr.hasNext()){
		
			ProjectField pF=itr.next();
			
			long subFieldId=pC.createField(pF.getId(),pF.getName(),pF.getLabel(),pF.getDesc(), pF.getValue(),pF.getType());
			
			if(pF.getPredValuesList().size()>0){
				
				Iterator <String> itratorItems=pF.getPredValuesList().iterator();
				
				while(itratorItems.hasNext()){
					
					pC.addSecondLevelFieldItem(subFieldId, itratorItems.next());
				
				}
				
				pC.startTransaction();
				pC.updateSecondLevelComplexType(subFieldId);
				pC.endTransaction();
				
			}

		}
		
		
	}
	
	public void addSecondLevelFieldList(ArrayList<String> subFieldItems){
		
		subFieldsList.get(subFieldsList.size()-1).setPredValues(subFieldItems);

	}

	public void addSecondLevelProjectField(String fieldName, String fieldLabel, String fieldDesc, String fieldType, String value) {
		
		Log.e("ZPparser"," SL: "+fieldName+" : "+fieldLabel+" : "+value);
	
		ProjectField pF=new ProjectField(fieldId,fieldName,fieldDesc,fieldLabel,value,fieldType);
		subFieldsList.add(pF);
	
	}
	
	public void startFieldTransaction(){
		
		pC.startTransaction();
		
	}
	
	public void endFieldTransaction(){
		
		pC.endTransaction();
		
	}
	
	public long createProject(String projectUserName,String thName, String projType){
		
		Log.e("ZPparser","ProjectByUser "+projectName);
		
		if(thName.equals("")) this.setPreviousTh=false;
		
		projId=pC.createProject(projectUserName,thName,projType);
		
		return projId;
		
	}

	public void addPredefinedValue(String predValue) {

		Log.e("ZPparser",predValue);
		pC.addFieldItem(fieldId, predValue);
		
		complexType=true;
				
	}
	
	public void updateInfo(){
		
		if(!setPreviousTh) pC.changeTh(projId, thName);
		
		pC.setProjectType(projId, biologicalRecordType);
		//afegir tipus
		
	}
	
	public void updateComplexType(){
		
		if(complexType) pC.updateComplexType(fieldId);
		
	}

	public boolean isSecondLevelField() {
		return secondLevelField;
	}

	public void setSecondLevelField(boolean secondLevelField) {
		this.secondLevelField = secondLevelField;
	}
	
	
	public void setHasLocation(boolean hasLocation) {
		this.hasLocation = hasLocation;
	}

	public void setLanguage(String language) {
		this.language = language;
	}

	public void setBiologicalRecordType(String biologicalRecordType) {
		this.biologicalRecordType = biologicalRecordType;
	}

	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}

	public void setRepositoryURL(String repositoryURL) {
		this.repositoryURL = repositoryURL;
	}

	public void setRemoteTh(String thName,String thServer, String thType) {

		this.thName=thName;
		this.thServer=thServer;
		this.thType=thType;
		
	}

	public boolean isHasRemoteTh() {
		return hasRemoteTh;
	}

	public void setHasRemoteTh(boolean hasRemoteTh) {
		
		this.hasRemoteTh = hasRemoteTh;
		
	}
	public String getThServer() {
		return thServer;
	}

	public String getThType() {
		return thType;
	}

	public void setThServer(String thServer) {
		this.thServer = thServer;
	}

	public void setThType(String thType) {
		this.thType = thType;
	}

	public long getProjId() {
		return projId;
	}

	public void setAutoFields(boolean autoFields) {
		this.autoFields = autoFields;
	}

}
