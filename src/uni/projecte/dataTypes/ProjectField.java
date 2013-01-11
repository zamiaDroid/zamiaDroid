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

package uni.projecte.dataTypes;

import java.util.ArrayList;

public class ProjectField{
	
	private String name;
	private String type;
	private String desc;
	private int visible;
	private String category;


	private long id;
	private String label;
	private String value;
	private int order;
	private ArrayList<String> predValues;

	
	public long getId() {
		return id;
	}

	public ProjectField (String nom, String tipus){
		
		this.name=nom;
		this.type=tipus;
		predValues=new ArrayList<String>();

		
}
	public ProjectField (String nom, String desc, String label,String value){
		
		this.name=nom;
		this.desc=desc;
		this.label=label;
		this.value=value;
		predValues=new ArrayList<String>();

		
	}
	
	public ProjectField (long fieldId,String nom, String type, String label,String value,int order,int visible){
		
		this.id=fieldId;
		this.name=nom;
		//this.desc=desc;
		this.label=label;
		this.value=value;
		this.type=type;
		this.order=order;
		this.visible=visible;
		predValues=new ArrayList<String>();

		
	}


	
	public ProjectField (String nom, String desc, String label,String value,String type){
		
		this.name=nom;
		this.desc=desc;
		this.label=label;
		this.value=value;
		this.type=type;
		predValues=new ArrayList<String>();

		
	}
	
	public ProjectField (String nom, String desc, String label,String value,String type, ArrayList<String> predValues){
		
		this.name=nom;
		this.desc=desc;
		this.label=label;
		this.value=value;
		this.type=type;
		this.predValues=predValues;

		
	}

	
	public void insertPredValue(String value){
		
		predValues.add(value);
		
	}
	
	public ArrayList<String> getPredValuesList(){
		
		return predValues;
		
		
	}
	
	public void setPredValues(ArrayList <String> predValuesList){
		
		predValues=predValuesList;
		
	}
	
	public ProjectField (long id,String nom, String tipus, String label,String value){
		
		this.id=id;
		this.name=nom;
		this.type=tipus;
		this.label=label;
		
	}
	
	public ProjectField (long fieldId,String nom, String desc, String label,String value,String type){
		
		this.id=fieldId;
		this.name=nom;
		this.desc=desc;
		this.label=label;
		this.value=value;
		this.type=type;
		predValues=new ArrayList<String>();

		
	}


	public String getName() {
		return name;
	}
	
	public String getValue() {
		return value;
	}
	
	public String getLabel() {
		return label;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}



	public void setName(String nom) {
		this.name = nom;
	}

	
	public void setValue(String value) {
		this.value = value;
	}


	public String getType() {
		return type;
	}


	public void setType(String tipus) {
		this.type = tipus;
	}

	public int getVisible() {
		return visible;
	}

	public void setVisible(int visible) {
		this.visible = visible;
	}

	public int getOrder() {
		return order;
	}

	public void setOrder(int order) {
		this.order = order;
	}
	
	public boolean isSubFieldExport(){
		
		return isMultiPhoto();
		
	}
	
	public boolean isMultiPhoto(){
		
		return type.equals("multiPhoto");		
	}
	
	public boolean isSecondLevel(){
		
		return type.equals("secondLevel");		
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}
	
}

