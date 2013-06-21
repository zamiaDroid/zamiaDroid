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
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;


import uni.projecte.dataLayer.ProjectManager.objects.Project;
import uni.projecte.dataLayer.RemoteDBManager.DBManager;
import uni.projecte.dataLayer.bd.FieldItemAdapter;
import uni.projecte.dataLayer.bd.ProjectDbAdapter;
import uni.projecte.dataTypes.ProjectField;
import uni.projecte.dataTypes.Research;
import android.content.Context;
import android.database.Cursor;
import android.text.format.DateFormat;


public class ProjectControler {
		
	protected ProjectDbAdapter fieldTransactionDb;
	
	protected ProjectDbAdapter projDbAdapter;

	protected Context baseContext;
	
	private boolean mandatory;


	private String desc;
	private long idRs;
	private String name;
	private String citationType;
	private String projType;
	private String filum;

	
	
	public ProjectControler (Context c) {
		
		super();
		
		this.baseContext=c;
		
	}
	
	public String getCitationType() {
		return citationType;
	}
	
	
	public void changeTh(long rsId,String thName){
		
		
		projDbAdapter = new ProjectDbAdapter(baseContext);
		projDbAdapter.open();
		
		projDbAdapter.updateProjectTh(rsId,thName);

		projDbAdapter.close();

		
	}
	
	public void changeLabelName(long projId,long fieldId,String fieldLabel){
		
		
		projDbAdapter = new ProjectDbAdapter(baseContext);
		projDbAdapter.open();
		
		projDbAdapter.setFieldLabel(projId, fieldId, fieldLabel);

		projDbAdapter.close();

		
	}
	
	public void updatePhotoField(long projId,long fieldId) {
		
		projDbAdapter = new ProjectDbAdapter(baseContext);
		projDbAdapter.open();
		
		projDbAdapter.updatePhotoType(projId, fieldId);

		projDbAdapter.close();
		
		
	}


	
	public void setProjectType(long rsId,String projectType){
		
		
		projDbAdapter = new ProjectDbAdapter(baseContext);
		projDbAdapter.open();
		
		projDbAdapter.updateProjectType(rsId,projectType);

		projDbAdapter.close();

		
	}
	
	public boolean projectExists(String name){
		
		boolean exists=false;
		
		projDbAdapter = new ProjectDbAdapter(baseContext);
		projDbAdapter.open();
		
		Cursor c=projDbAdapter.fetchProjectByName(name);
		c.moveToFirst();

		if(c.getCount()>0) {
			
			exists=true;
			this.idRs=c.getLong(0);

		}
		
		c.close();	
		
		projDbAdapter.close();

		return exists;
		
		
	}
	
	public void loadResearchInfoByName(String name){
		
		
		projDbAdapter = new ProjectDbAdapter(baseContext);
		projDbAdapter.open();
		
		Cursor c=projDbAdapter.fetchProjectByName(name);
		c.moveToFirst();
		this.desc=c.getString(2);
		this.idRs=c.getLong(0);
		this.name=name;
		this.projType=c.getString(3);
		
		c.close();	
		
		projDbAdapter.close();

		
		
	}
	
	public String getFieldNameByLabel(long projId,String label){
		
		String name="";
		
		ProjectDbAdapter mDbAttribute = new ProjectDbAdapter(baseContext);
		mDbAttribute.open();
		
		Cursor cur=mDbAttribute.fetchFieldNameByLabel(projId,label);
		cur.moveToFirst();
		
		if(cur.getCount()>0) name=cur.getString(2);
		
		cur.close();
		
		mDbAttribute.close();
		
		return name;
		
	
	}
	
	public String getFieldLabelByName(long projId,String name){
		
		
		ProjectDbAdapter mDbAttribute = new ProjectDbAdapter(baseContext);
		mDbAttribute.open();
		
		Cursor cur=mDbAttribute.fetchFieldLabelByName(projId,name);
		cur.moveToFirst();
		
			String label=cur.getString(4);
		
		cur.close();
		
		mDbAttribute.close();
		
		return label;
		
	
	}
	
	
	
	public long getFieldIdByName(long projId,String name){
		
		
		ProjectDbAdapter mDbAttribute = new ProjectDbAdapter(baseContext);
		
		long id=-1;
		
		mDbAttribute.open();
		
		Cursor cur=mDbAttribute.fetchFieldIdByName(projId,name);
		cur.moveToFirst();
		
		if(cur.getCount()>0) id=cur.getLong(0);
		
		cur.close();
		
		mDbAttribute.close();
		
		return id;
		
	
	}
	
	public Cursor getFieldCursorByName(long projId,String name){
		
		
		ProjectDbAdapter mDbAttribute = new ProjectDbAdapter(baseContext);		
		mDbAttribute.open();
		
		Cursor cur=mDbAttribute.fetchFieldIdByName(projId,name);
		cur.moveToFirst();
		
		
		mDbAttribute.close();
		
		return cur;
		
	
	}
	
	
	public Cursor getProjectFieldsCursor(long rsId){
		
		ProjectDbAdapter fieldAdapter = new ProjectDbAdapter(baseContext);
		
		fieldAdapter.open();
		
			Cursor cur=fieldAdapter.fetchAllFieldsFromProject(rsId);
			cur.moveToFirst();
		
		fieldAdapter.close();
		
		return cur;
	}
	
	public HashMap<Long, ProjectField> getProjectFieldsMap(long projId){
		
		HashMap<Long, ProjectField> projectFields=new HashMap<Long, ProjectField>();
		
		ProjectDbAdapter fieldAdapter = new ProjectDbAdapter(baseContext);
		
		fieldAdapter.open();
		
			Cursor cur=fieldAdapter.fetchAllFieldsFromProject(projId);
			cur.moveToFirst();
			
			while(!cur.isAfterLast()){
				
				//(long fieldId,String nom, String desc, String label,String value,String type)
				
				//ID, Name, categoria, label, tipus
				//ProjectField projField=new ProjectField(cur.getLong(0), cur.getString(2),cur.getString(6),cur.getString(4),cur.getString(3));
				
				ProjectField projField=new ProjectField(cur.getLong(0), cur.getString(2),cur.getString(6),cur.getString(4),"",cur.getString(3));

				
				projectFields.put(projField.getId(), projField);
				
				cur.moveToNext();
			}
			
			cur.close();
			
		
		fieldAdapter.close();
		
		return projectFields;
	}
	
	
	public HashMap<String, String> getProjectFieldsPair(long projId){
		
		HashMap<String, String> fields=new HashMap<String, String>();

		ProjectDbAdapter fieldAdapter = new ProjectDbAdapter(baseContext);		
		fieldAdapter.open();
		
			Cursor cur=fieldAdapter.fetchAllFieldsFromProject(projId);
			cur.moveToFirst();
			
			while (!cur.isAfterLast()) {

				fields.put(cur.getString(2), cur.getString(4));
				
				cur.moveToNext();
				
			}
			
		cur.close();
		fieldAdapter.close();
		
		
		return fields;
		
	}
	
	public CharSequence[] getListProjFields(long rsId){
		
		ProjectDbAdapter mDbAttribute = new ProjectDbAdapter(baseContext);
		
		mDbAttribute.open();
		
		Cursor cu=mDbAttribute.fetchProjectsFromRsNotOrdered(rsId);
		 cu.moveToFirst();
		 int n=cu.getCount();
		 
		 CharSequence[] list=new CharSequence[n];
		  
		  for (int i=0;i<n;i++){
			  
			  list[i]=cu.getString(4);
			  cu.moveToNext();
			  
		  }
		  
		  cu.close();
		
		mDbAttribute.close();
		
		return list;
	}
	
	public int getProjCount(){
		
		ProjectDbAdapter mDbAttribute = new ProjectDbAdapter(baseContext);
		
		mDbAttribute.open();
		
		Cursor rt= mDbAttribute.fetchAllProjects();
		
		   	rt.moveToFirst();
		   	int n=rt.getCount();
	
	   	rt.close();
	   	
		mDbAttribute.close();
		
		return n;
	}
	
	public CharSequence[] getProjectListCS(){
		
		ProjectDbAdapter mDbAttribute = new ProjectDbAdapter(baseContext);
		
		mDbAttribute.open();
		
		Cursor rt= mDbAttribute.fetchAllProjects();
		
	   	rt.moveToFirst();
	   	int n=rt.getCount();
	
		CharSequence[] list=new CharSequence[n];

		   	
		   	for (int i=0;i<n;i++){
		   		
		   		list[i]=rt.getString(1);
		   		rt.moveToNext();
		   		
		   	}
		
		mDbAttribute.close();
		
		return list;
	}
	
	public ArrayList<ProjectField> getProjFields(long rsId){
		
		ArrayList<ProjectField> attList=new ArrayList<ProjectField>();
		
		projDbAdapter = new ProjectDbAdapter(baseContext);
		
		projDbAdapter.open();
		
		Cursor c=projDbAdapter.fetchOrderedFieldsFromProject(rsId); 
		c.moveToFirst();
		
		int n=c.getCount();
		  
		  for (int i=0;i<n;i++){
			  
			  ProjectField atV=new ProjectField(c.getLong(0),c.getString(2), c.getString(3),c.getString(4),c.getString(5));
			  
			  attList.add(atV);
			  
			  c.moveToNext();
			  
		  }
		  
		c.close();
		
		projDbAdapter.close();
		
		return attList;
	}
	
	
	public ArrayList<ProjectField> getProjectFields(long rsId){
		
		idRs=rsId;
		
		ArrayList<ProjectField> attList=new ArrayList<ProjectField>();
		
		projDbAdapter = new ProjectDbAdapter(baseContext);
		
		projDbAdapter.open();
		
		Cursor c=projDbAdapter.fetchProjectsFromProjectOrdered(rsId); 
		c.moveToFirst();
		
		int n=c.getCount();
		  
		  for (int i=0;i<n;i++){
			  
			  if(c.getInt(7)==0 && i!=0) projDbAdapter.setFieldOrder(rsId,c.getLong(0),i);
 
			  ProjectField atV=new ProjectField(c.getLong(0),c.getString(2), c.getString(3),c.getString(4),c.getString(5),c.getInt(7),c.getInt(8));
			  
			  attList.add(atV);
			  
			  c.moveToNext();
			  
		  }
		  
		c.close();
		
		projDbAdapter.close();
		
		return attList;
	}
	
	public long loadProjectInfoById(long id){

		 ProjectDbAdapter mDdResearchTypes = new ProjectDbAdapter(baseContext);
		 mDdResearchTypes.open();
		
		Cursor c=mDdResearchTypes.fetchProjectById(id);
		c.moveToFirst();
		
		
		if(c.getCount()>0){
			
			this.desc=c.getString(2);
			this.idRs=c.getLong(0);
			this.name=c.getString(1);
			this.projType=c.getString(3);

		
		}
		else{
			
			this.idRs=-1;
			
		}
		
		/*
		 * Determine filum
		 * 
		 * 
		 */
		
		DBManager dbL=new DBManager(baseContext,false);
		ThesaurusControler tC=new ThesaurusControler(baseContext);
		String thType=tC.getTHType(getThName());
		
    	if(thType.equals("")) dbL.getFilum(getProjType());			
    	else dbL.getFilum(thType);
		
    	filum="";
		
		/* Check filumLetter  */
			
			if(	!dbL.getFilumLetter().toLowerCase().equals("")){
				
				filum=dbL.getFilumType();
				
			}
	
		
		
		c.close();			
		mDdResearchTypes.close();

		return idRs;
		
	}
	


	public Cursor getResearchListCursor(){
		
		
		projDbAdapter = new ProjectDbAdapter(baseContext);
		projDbAdapter.open();
	        
	    Cursor rt= projDbAdapter.fetchAllProjects();
	    rt.moveToFirst();

	    projDbAdapter.close();
    	
    	return rt;

		
	}
	
	public ArrayList<Project> getProjectArrayList(){
		
		ArrayList<Project> projList=new ArrayList<Project>();
		
		projDbAdapter = new ProjectDbAdapter(baseContext);
		projDbAdapter.open();
	        
	    Cursor rt= projDbAdapter.fetchAllProjects();
	    rt.moveToFirst();
	    
	    	if(rt!=null && rt.getCount()>0){
	    		
	    		while (!rt.isAfterLast()) {
				
	    			projList.add(new Project(rt.getLong(0), rt.getString(1), "", rt.getString(2)));
	    			rt.moveToNext();
					
				}
	    		
	    		rt.close();
	    		
	    	}
	    

	    projDbAdapter.close();
    	
    	return projList;

		
	}
	



	public void addItem(String itemName, String selectedItem, Research rs) {
		
		rs.addAtribbute(itemName, selectedItem);
		
	}


	
	/*
	 * 
	 * This method creates a Project with the name rsName that uses the thesaurus thName
	 * 
	 */
	
	public long createProject(String rsName, String thName, String projType){
		
		projDbAdapter = new ProjectDbAdapter(baseContext);
		projDbAdapter.open();
		
		long idRs= projDbAdapter.createProject(rsName, thName,projType);
		
		this.idRs=idRs;
		
		projDbAdapter.close();
		
		
		return idRs;
	}
	
	public int removeProject(long projId){
		
		projDbAdapter = new ProjectDbAdapter(baseContext);
		projDbAdapter.open();
		
		projDbAdapter.deleteProject(projId);
		
		projDbAdapter.close();
	
		return 0;
	}
	
	
	public long addFieldItem(long fieldId, String value){
		
		FieldItemAdapter fIBd=new FieldItemAdapter(baseContext);
		fIBd.open();
		
			long idItem=fIBd.addFieldItem(fieldId, value);
		
		fIBd.close();
		
		return idItem;

	}
	
	public void addFieldItemList(long projId, long fieldId,ArrayList<String> predValuesList) {
		
		FieldItemAdapter fIBd=new FieldItemAdapter(baseContext);
		fIBd.open();
		
			Iterator<String> it=predValuesList.iterator();
			
			while(it.hasNext()){
				
				String fieldValue=it.next();
				fIBd.addFieldItem(fieldId, fieldValue);
				
			}
		
		
		fIBd.close();
	}

	
	
	public void startTransaction(){
		
		fieldTransactionDb=new ProjectDbAdapter(baseContext);
		fieldTransactionDb.open();
		fieldTransactionDb.startTransaction();
		
	}
	
	public void endTransaction(){
		
		fieldTransactionDb.endTransaction();
		fieldTransactionDb.close();
		
	}
	
	/*
	 * 
	 * This method creates a Field for the project with projId provided. 
	 * 
	 * @param projectId project's identifier
	 * @param fieldName field's name
	 * @param label field's label or long name
	 * @param fieldType field's type
	 * @param fieldCategory field's category
	 * 
	 */
	
	public long addProjectField(long projId,String fieldName, String label, String desc,String value, String fieldType,String fieldCategory){
		
	    long projFieldId;
	    		
	    
	    if(mandatory){
	    
	    	if(fieldName.equals("OriginalTaxonName")){
	    		
				projFieldId=fieldTransactionDb.createDefField(projId, fieldName,label,desc,value, "thesaurus");
   		
	    		
	    	}
	    	else{
	    		
	    		projFieldId=fieldTransactionDb.createDefField(projId, fieldName,label,desc,value, fieldType);
	    		
	    	}
	    	
	    }
	    
	    else{
	    	
			projFieldId=fieldTransactionDb.createField(projId,fieldName,label,desc,value,fieldType,fieldCategory,true);

	    }

    	    		
		
		return projFieldId;
		
		
	}
	
	public void updateComplexType(long fieldId) {
	
		fieldTransactionDb.updateComplexType(fieldId);
		
	}
	
	/*
	 * 
	 * This method creates a Field for the project with projId provided. 
	 * 
	 * @param projectId project's identifier
	 * @param fieldName field's name
	 * @param label field's label or long name
	 * @param fieldType field's type
	 * @param fieldCategory field's category
	 * 
	 */
	
	public long addProjectNotEditableField(long projId,String fieldName, String label, String desc,String value, String fieldType,String fieldCategory){
		
	    long projFieldId;   
		

	    if(mandatory){
	    
	    	if(fieldName.equals("OriginalTaxonName")){
	    		
				projFieldId=fieldTransactionDb.createDefField(projId, fieldName,label,desc,value, "thesaurus");
   		
	    		
	    	}
	    	else{
	    		
	    		projFieldId=fieldTransactionDb.createNotEdDefField(projId, fieldName,label,desc,value, fieldType);
	    		
	    	}
	    	
	    }
	    
	    else{
	    	
			projFieldId=fieldTransactionDb.createField(projId,fieldName,label,desc,value,fieldType,fieldCategory,false);

	    }

    	    		

		return projFieldId;
		
		
	}
	
	/*
	 * This method provides us a list of the projects in the system
	 * 
	 * @return an ArrayList with the project names
	 * 
	 */
	
	public ArrayList<String> getProjectList(){
		
		
		projDbAdapter = new ProjectDbAdapter(baseContext);

		projDbAdapter.open();
	        
	    Cursor rt= projDbAdapter.fetchAllProjects();
		
		ArrayList<String> list = new ArrayList<String>();
   	
	   	rt.moveToFirst();
	   	int n=rt.getCount();
	   	
	   	
	   	for (int i=0;i<n;i++){
	   		
	   		list.add(rt.getString(1));
	   		rt.moveToNext();
	   		
	   	}
	   	
	   	rt.close();
	   	
	   	
	   	return list;
		
		
	}

	
	public String getProjNameByTh(String thName) {

		projDbAdapter = new ProjectDbAdapter(baseContext);
		projDbAdapter.open();
		
		Cursor c=projDbAdapter.fetchProjectByTh(thName);
		c.moveToFirst();
		
			String projName=c.getString(1);
		
		c.close();
			
		projDbAdapter.close();
		
		return projName;
	}

	
	
	/*
	 * 
	 * This method gets the projects that uses the provided thesaurus name. Returns thesaurus count
	 * 
	 */

	public int isUsed(String thName) {
		
		projDbAdapter = new ProjectDbAdapter(baseContext);
		projDbAdapter.open();
		
		Cursor c=projDbAdapter.fetchProjectByTh(thName);
		c.moveToFirst();
		
			int used=c.getCount();
		
		c.close();
			
		projDbAdapter.close();
		
		return used;
	}

	
	/*
	 * 
	 * This method helps us to create a field for a concrete project with the parameters provided.
	 * 
	 * @param projectId project's identifier
	 * @param fieldName field's name
	 * @param label field's label or long name
	 * @param category field's category
	 * @param type field's type
	 * 
	 * @return gives back the recently created fieldId
	 * 
	 */

	public long createField(long projectId, String fieldName, String label, String category,String type, boolean visible) {
		
		
		long fieldId;

		  projDbAdapter = new ProjectDbAdapter(baseContext);
		  projDbAdapter.open();
		  
		  Cursor fieldC=projDbAdapter.fetchFieldsFromProject(projectId, fieldName);
		  fieldC.moveToFirst();
		  
		  
		  //it exists
		  if(fieldC.getCount()>0){
			  
			 fieldId=fieldC.getLong(0);
			  
		  }
		  else{
			  
			  
			  fieldId=projDbAdapter.createField(projectId, fieldName, label, "", "", type, category,visible);
			  
			  setFieldOrder(projDbAdapter,projectId,fieldId);
			  
			  
			  
		  }
		  
		   fieldC.close();
		
		  projDbAdapter.close();
		  
		  return fieldId;
		
		
	}
	
	private void setFieldOrder(ProjectDbAdapter projDbAdapter, long projectId, long fieldId) {

		Cursor fieldList=projDbAdapter.fetchAllFieldsFromProject(projectId);
		
		if(fieldList!=null) {
		
			fieldList.moveToFirst();
			
			int numElements=fieldList.getCount();
			projDbAdapter.setFieldOrder(projectId, fieldId, numElements-1);
			
		}
		
		
	}


	/*
	 * 
	 * This method provides all the field elements from a concrete project
	 * 
	 * @param projectId is the id of our project
	 * @return gives an array list of ProjectFields
	 * 
	 */
	
	


	public ArrayList<ProjectField> getAllProjectFields(long projectId) {
		
		
		ArrayList<ProjectField> attList=new ArrayList<ProjectField>();
		
		projDbAdapter = new ProjectDbAdapter(baseContext);
		
		projDbAdapter.open();
		
		Cursor c=projDbAdapter.fetchAllFieldsFromProject(projectId);
		c.moveToFirst();
		 
		 
		 int n=c.getCount();
		  
		  for (int i=0;i<n;i++){
			  
			  ProjectField atV=new ProjectField(c.getLong(0),c.getString(2), c.getString(3),c.getString(4),c.getString(5));
			  
			  attList.add(atV);
			  
			  c.moveToNext();
			  
		  }
		  
		  c.close();
		
		projDbAdapter.close();
		
		return attList;
	}
	
	
	public HashMap<String,Long> getProjectFieldsHash(long projectId) {
		
		HashMap<String, Long> attList=new HashMap<String, Long>();
		
		projDbAdapter = new ProjectDbAdapter(baseContext);
		projDbAdapter.open();
		
		Cursor c=projDbAdapter.fetchAllFieldsFromProject(projectId);
		c.moveToFirst();
		 
		while(!c.isAfterLast()){
			  			  
			  attList.put(c.getString(2),c.getLong(0));
			  c.moveToNext();
			  
		}
		  
		 c.close();
		
		projDbAdapter.close();
		
		return attList;
		
	}

	
	public void removeField(long idField) {

		ProjectDbAdapter projDbAdapter = new ProjectDbAdapter(baseContext);
		
		projDbAdapter.open();
		
		projDbAdapter.removeProjectField(idField);

		projDbAdapter.close();
		
		
	}

	/*
	 * This method helps us to change the visibility of a field.
	 * 
	 * @param projectId is the id of the project that contains the field
	 * @param fieldName is the name of which we want to change the visibility
	 * @param visible says if the field has to be visible or not
	 * 
	 */
	
	public void changeFieldVisibility(long projectId, String attName, boolean visible) {

		ProjectDbAdapter projDbAdapter = new ProjectDbAdapter(baseContext);
		
		projDbAdapter.open();
		
		projDbAdapter.setFieldVisibilty(projectId,attName,visible);

		projDbAdapter.close();
		
		
	}
	
	
	public void setFieldOrder(long fieldId, int order) {
		
		
		ProjectDbAdapter projDbAdapter = new ProjectDbAdapter(baseContext);
		
		projDbAdapter.open();
		
			projDbAdapter.setFieldOrder(idRs,fieldId,order);

		projDbAdapter.close();

		
		
	}
	
	public String getThName() {
		return desc;
	}


	public long getProjectId() {
		return idRs;
	}


	public String getName() {
		return name;
	}
	
	public String getCleanProjectName() {
		
		return name.replace(" ", "_");
		
	}
	
	public void setOptional() {
		
		mandatory=false;
	}


	public void setMandatory() {
		

		mandatory=true;
		
	}
	
	public void setCitationType(String type){
		
		citationType=type;
		
	}
	
public String createSecondLevelIdentifier(String fieldName){
		
	 /*   final Calendar c = Calendar.getInstance();

   	 int mYear = c.get(Calendar.YEAR);
        int mMonth = c.get(Calendar.MONTH);
        int mDay = c.get(Calendar.DAY_OF_MONTH);
        int mMinute = c.get(Calendar.MINUTE);
        int mHour = c.get(Calendar.HOUR_OF_DAY);

        String timestap=new StringBuilder()
        // Month is 0 based so add 1
        .append(mYear)
        .append(mMonth + 1)
        .append(mDay)
        .append(" - ")
        .append(mHour).append(":")
        .append(mMinute).toString(); */
	
	
		Date date = new Date();
    	date.getDate();

       return fieldName.toLowerCase()+"_"+(String) DateFormat.format("yyyy-MM-dd_kk:mm:ss",date);
		
		
	}

	public Cursor getSecondLevelFieldId(long projId){
			
		projDbAdapter = new ProjectDbAdapter(baseContext);
		projDbAdapter.open();
		
			Cursor slFields=projDbAdapter.findSecondLevelField(projId);
			slFields.moveToNext();
			
		projDbAdapter.close();
		
		return slFields;
		
	}
	
	public long getThesaurusFieldId(long projId){
		
		long fieldId=-1;
		
		projDbAdapter = new ProjectDbAdapter(baseContext);
		projDbAdapter.open();
		
			Cursor slFields=projDbAdapter.findThesaurusField(projId);
			slFields.moveToNext();
			
		projDbAdapter.close();
		
		if(slFields!=null && slFields.getCount()>0) fieldId=slFields.getLong(0);
		
		slFields.close();
		
		return fieldId;
		
	}
	
	

	public Cursor getPhotoFieldsFromProject(long projId){
	
		projDbAdapter = new ProjectDbAdapter(baseContext);
		projDbAdapter.open();
		
			Cursor list=projDbAdapter.getPhotoFieldsFromProject(projId);
			list.moveToFirst();
		
		projDbAdapter.close();
		
		return list;
		
	}
	
	
	public ProjectField getOldPhotoField(long projId){
		
		ProjectField projField=null;
		
		projDbAdapter = new ProjectDbAdapter(baseContext);
		projDbAdapter.open();
		
			Cursor list=projDbAdapter.getPhotoFieldsFromProject(projId);
			list.moveToFirst();
		
			if(list!=null && list.getCount()>0){
				
				//KEY_ROWID, PROJ_ID,PROJ_NAME,TYPE,LABEL,PREVALUE,CAT,VISIBLE
				projField=new ProjectField(list.getLong(0),list.getString(2),"photo",list.getString(4),"");
			
				list.close();
			}
			
			
		projDbAdapter.close();
		
		return projField;
		
	} 
	
	
	public Cursor getMultiPhotoFieldsFromProject(long projId){
		
		projDbAdapter = new ProjectDbAdapter(baseContext);
		projDbAdapter.open();
		
			Cursor list=projDbAdapter.getMultiPhotoFieldsFromProject(projId);
			list.moveToFirst();
		
		projDbAdapter.close();
		
		return list;
		
	}
	
	
	public String getPhotoFieldName(long projId){
		
		String fieldName="";
		
		projDbAdapter = new ProjectDbAdapter(baseContext);
		projDbAdapter.open();
		
			Cursor list=projDbAdapter.getPhotoFieldsFromProject(projId);
			list.moveToFirst();

			if(list!=null && list.getCount()>0){
				
				fieldName=list.getString(2);
				
				list.close();

			}
			
		projDbAdapter.close();
		
		
		return fieldName;
		
	}
	
	
	public long hasSurenessField(long projId) {

		long surenessFieldId=-1;
		
		projDbAdapter = new ProjectDbAdapter(baseContext);
		projDbAdapter.open();
		
			Cursor list=projDbAdapter.fetchFieldIdByName(projId, "Sureness");
			list.moveToFirst();

			if(list!=null && list.getCount()>0){
				
				surenessFieldId=list.getLong(0);
				
				list.close();

			}
			
		projDbAdapter.close();
				
		return surenessFieldId;
		
	}
	
	public boolean hasOldPhotoField(long projId){
		
		boolean photoField=false;
		
		projDbAdapter = new ProjectDbAdapter(baseContext);
		projDbAdapter.open();
		
			Cursor list=projDbAdapter.getPhotoFieldsFromProject(projId);
			list.moveToFirst();

			if(list!=null && list.getCount()>0){
				
				photoField=true;
				
				list.close();

			}
			
		projDbAdapter.close();
		
		return photoField;
	}


	public String getProjType() {
		return projType;
	}

	public String getFilum() {
		return filum;
	}

	public void setFilum(String filum) {
		this.filum = filum;
	}

	
	
	






}
