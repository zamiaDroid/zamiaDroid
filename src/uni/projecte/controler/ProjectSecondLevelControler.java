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
import java.util.HashMap;

import uni.projecte.R;
import uni.projecte.dataLayer.bd.FieldItemAdapter;
import uni.projecte.dataLayer.bd.ProjectDbAdapter;
import uni.projecte.dataTypes.ProjectField;
import android.content.Context;
import android.database.Cursor;

public class ProjectSecondLevelControler extends ProjectControler {

	protected ProjectDbAdapter projDbAdapter;
	protected Context baseContext;
	
	
	public ProjectSecondLevelControler(Context c) {
		
		super(c);
		this.baseContext=c;
		
	}
	
	public long getTwoLevelFieldByProjId(long projId){
		
		projDbAdapter = new ProjectDbAdapter(baseContext);
		
		Cursor fieldList=projDbAdapter.findSecondLevelField(projId);
		
		fieldList.moveToFirst();
		
		if (fieldList.getCount()>0) return fieldList.getLong(0);
		else return -1;
		
	}
	
	public Cursor getSecondLevelFieldsByProjId(long projId){
		
		projDbAdapter = new ProjectDbAdapter(baseContext);
		projDbAdapter.open();
		
		Cursor fieldList=projDbAdapter.findSecondLevelField(projId);
		
		fieldList.moveToFirst();
		
		return fieldList;
		
	}
	
	public long[] getTwoLevelFieldListByProjId(long projId){
		
		long[] list;
		
		projDbAdapter = new ProjectDbAdapter(baseContext);
		
		projDbAdapter.open();
		
		Cursor fieldList=projDbAdapter.findSecondLevelField(projId);
		
		fieldList.moveToFirst();
		
		list=new long[fieldList.getCount()];
		
		int i=0;
		
		
		while(!fieldList.isAfterLast()){
			
			list[i]=fieldList.getLong(0);
			
			fieldList.moveToNext();
			
			i++;
		}
		
		projDbAdapter.close();
		
		return list;
		
	}
	
	
	/*
	 * This method removes the project and all each fields and secondFields if there's no citations
	 * 
	 * @return: -1 when project has citations | 0 when success
	 * 
	 */
	
	@Override
	public int removeProject(long projId){
		
		int error=0;
		
		CitationControler sC= new CitationControler(baseContext);
		ProjectDbAdapter projDbAdapter = new ProjectDbAdapter(baseContext);
		projDbAdapter.open();
		
		FieldItemAdapter fi=new FieldItemAdapter(baseContext);
	 	fi.open();
		
		//checking if project has citations
		Cursor numSamp=sC.getCitationsWithFirstFieldByProjectId(projId,false,false);
	 	 	   	
	   numSamp.moveToFirst();
	 	   	
	 	 	if (numSamp.getCount()<=0){
	 		
	 	 		//finding secondLevel Fields
	 			Cursor fieldList=projDbAdapter.findSecondLevelField(projId);
	 			fieldList.moveToFirst();
	 			
	 			while(!fieldList.isAfterLast()){
	 					 		
	 				//finding complexSecondLevelFields
	 				Cursor complexFieldList=projDbAdapter.findComplexSecondLevelFields(fieldList.getLong(0));
		 			complexFieldList.moveToFirst();
		 			
	 	 			
		 			while(!complexFieldList.isAfterLast()){
		 					 		
			 	 		//removing secondLevelFields
		 				fi.deleteItemsFromSecondLevelField(complexFieldList.getLong(0));
		 	 			complexFieldList.moveToNext();
		 				
		 			}
		 			
		 			complexFieldList.close(); 			
		 			
	 				
		 	 		//removing secondLevelFields
	 	 			projDbAdapter.removeSLFieldsFromSLField(fieldList.getLong(0));
	 				fieldList.moveToNext();
	 				
	 			}
	 			fieldList.close();
	 			
	 			
	 			//removeComplexTypes
	 			Cursor complexFieldList=projDbAdapter.findComplexFields(projId);
	 			complexFieldList.moveToFirst();
 			

 	 			
	 			while(!complexFieldList.isAfterLast()){
	 					 		
		 	 		//removing secondLevelFields
	 				fi.deleteItemsFromField(complexFieldList.getLong(0));
	 	 			complexFieldList.moveToNext();
	 				
	 			}
	 			
	 			complexFieldList.close(); 			
	 			fi.close();
	 			
	 			
	 	 		//remove Project and Fields	 	 		
	 	   		projDbAdapter.deleteProject(projId);
 	   	  	   	projDbAdapter.deleteFieldsFromProjectId(projId);
	 	   	}
	 	   	else{
	 	   		
	 	   		error= -1;
	 	   		
	 	   	}

	 	 	projDbAdapter.close();
	 	 	numSamp.close();
 			fi.close();

		
		return error;
		
	}
	
	
	
	public long createField(long projectId, String fieldName, String label, String desc,String value,String type) {
		
		
		 long fieldId;
		 

		  projDbAdapter = new ProjectDbAdapter(baseContext);
		  projDbAdapter.open();
		  
			  
		  if(type.equals("text")){
				
				  fieldId=projDbAdapter.createSecondLevelField(projectId, fieldName, label, desc, value,"simple");

			}
		  else{
			  
			  fieldId=projDbAdapter.createSecondLevelField(projectId, fieldName, label, desc, value, type);
			  
		  }
			  
		
		  projDbAdapter.close();
		  
		  return fieldId;
		
		
	}
	
	
	@Override
	public ArrayList<ProjectField> getProjFields(long rsId){
		
		ArrayList<ProjectField> attList=new ArrayList<ProjectField>();
		
		projDbAdapter = new ProjectDbAdapter(baseContext);
		
		projDbAdapter.open();
		
		Cursor c=projDbAdapter.fetchSLFieldsFromProject(rsId); 
		c.moveToFirst();
		
		int n=c.getCount();
		  
		  for (int i=0;i<n;i++){
			 
			  
			  long subProjId=c.getLong(1);
			  
			  ProjectField atV=new ProjectField(c.getLong(0),c.getString(2), c.getString(3),c.getString(4),c.getString(5));
			  
			  attList.add(atV);
			  
			  c.moveToNext();
			  
		  }
		  
		  c.close();
		
		projDbAdapter.close();
		
		return attList;
	}
	
	@Override
	public Cursor getProjectFieldsCursor(long rsId){
		
		ProjectDbAdapter fieldAdapter = new ProjectDbAdapter(baseContext);
		fieldAdapter.open();
		
			Cursor cur=fieldAdapter.fetchSLFieldsFromProject(rsId);
			cur.moveToFirst();
		
		fieldAdapter.close();
		
		return cur;
	}
	
	public HashMap<Long, ProjectField> getProjectFieldsMap(long projId){

		HashMap<Long, ProjectField> projectFields=new HashMap<Long, ProjectField>();
		
		ProjectDbAdapter fieldAdapter = new ProjectDbAdapter(baseContext);
		fieldAdapter.open();
		
			Cursor cur=fieldAdapter.fetchSLFieldsFromProject(projId);
			cur.moveToFirst();
			
			while(!cur.isAfterLast()){
				
				ProjectField projField=new ProjectField(cur.getLong(0), cur.getString(2),cur.getString(3),cur.getString(4), cur.getString(6));
				projectFields.put(projField.getId(), projField);
				
				cur.moveToNext();
			}
			
			cur.close();
			
		fieldAdapter.close();
		
		return projectFields;
	}
	
	
	public long addSecondLevelFieldItem(long fieldId, String value){
		
		FieldItemAdapter fIBd=new FieldItemAdapter(baseContext);
		
		fIBd.open();
		
			long idItem=fIBd.addSecondLevelFieldItem(fieldId, value);
		
		fIBd.close();
		
		return idItem;

	}
	
	
	/*
	 * This method helps us to change the visibility of a field.
	 * 
	 * @param projectId is the id of the project that contains the field
	 * @param fieldName is the name of which we want to change the visibility
	 * @param visible says if the field has to be visible or not
	 * 
	 */
	
	@Override
	public void changeFieldVisibility(long projectId, String attName, boolean visible) {

		ProjectDbAdapter projDbAdapter = new ProjectDbAdapter(baseContext);
		
		projDbAdapter.open();
		
		projDbAdapter.setSecondLevelFieldVisibilty(projectId,attName,visible);

		projDbAdapter.close();
		
		
	}
	
	@Override
	public void updateComplexType(long fieldId) {

		
		fieldTransactionDb.updateComplexType(fieldId);

		
	}
	
	public void updateSecondLevelComplexType(long fieldId) {

		
		fieldTransactionDb.updateSecondLevelComplexType(fieldId);

		
	}
	
	
	
	public void cloneFieldToSubField(long projId,long subProjId, String name, String label) {
		
		projDbAdapter = new ProjectDbAdapter(baseContext);
		projDbAdapter.open();
			
		Cursor cursor= projDbAdapter.fetchFieldIdByName(projId, name);
		cursor.moveToFirst();
		
		if(cursor.getCount()>0){ 
			
			String fieldType=cursor.getString(3);
		
			long fieldId=projDbAdapter.createSecondLevelField(subProjId, name, label,cursor.getString(6),cursor.getString(5),cursor.getString(3));
		
			if(fieldType.equals("complex")){
				
				cloneComplexData(cursor.getLong(0),fieldId);
				
			}
			
		}

		projDbAdapter.close();

		//afegir items
		
		cursor.close();
	}
	
	private void cloneComplexData(long oldFieldId, long newfieldId) {


			FieldItemAdapter itemsHnd= new FieldItemAdapter(baseContext);
					itemsHnd.open();
			
			
			Cursor itemsC=itemsHnd.fetchItemsbyFieldId(oldFieldId);
			itemsC.moveToFirst();
			
			
			while(!itemsC.isAfterLast()){
				
				
				addSecondLevelFieldItem(newfieldId, itemsC.getString(2));
				
				itemsC.moveToNext();
				
				
			}
			
			itemsC.close();
			itemsHnd.close();
			
		
		
	}

	public void updateSubFieldId(long oldId, long newId){
		
		fieldTransactionDb.updateSecondLevelFieldId(oldId,newId);	
		
	}
	
	public Cursor getProjectFieldsNSCursor(long rsId){
		
		ProjectDbAdapter fieldAdapter = new ProjectDbAdapter(baseContext);
		
		fieldAdapter.open();
		
		Cursor cur=fieldAdapter.fetchFieldsNoSecondLevelFromProject(rsId);
		cur.moveToFirst();
		
		fieldAdapter.close();
		
		return cur;
	}
	
	
	public long isQuercusExportable(long projId){
		
		projDbAdapter = new ProjectDbAdapter(baseContext);
		
		projDbAdapter.open();
		
		long result=projDbAdapter.isQuercusExportable(projId);
		
		projDbAdapter.close();
		
		return result;
		
		
	}
	
	public long getSLId(long projId,String fieldName){
		
		long fieldId=-1;
		
		projDbAdapter = new ProjectDbAdapter(baseContext);
		
		projDbAdapter.open();
		
			Cursor result=projDbAdapter.fetchSecondLevelFieldFromProject(projId,fieldName);
			result.moveToFirst();
			
			if(result.getCount()>0) fieldId=result.getLong(0);
			
			result.close();
		
		projDbAdapter.close();
		
		return fieldId;
		
		
	}

	public void addAutoFields(long projId) {
		
		PreferencesControler pC= new PreferencesControler(baseContext);
		
		//altitude field
		
		if(pC.isAddAltitude()){
			
			addProjectField(projId,"altitude", baseContext.getString(R.string.altitudeLabel), "", "","simple", "ADDED");

		}
		
		//author field
		
		if(pC.isAddAuthor()){
			
			addProjectNotEditableField(projId,"ObservationAuthor", "Autor", "", "","simple", "ADDED");
			
		}
	
}

  
	public ProjectField getMultiPhotoSubFieldId(long fieldId){
	
		projDbAdapter = new ProjectDbAdapter(baseContext);
		projDbAdapter.open();
		
		Cursor fieldList=projDbAdapter.fetchSLFieldsFromProject(fieldId);
		
		fieldList.moveToFirst();
		
		ProjectField projField= new ProjectField(fieldList.getLong(0), fieldList.getString(2), "", fieldList.getString(4), "", "multiPhoto");

		
		fieldList.close();
		projDbAdapter.close();
				
		return projField;
		
	}
	

}
