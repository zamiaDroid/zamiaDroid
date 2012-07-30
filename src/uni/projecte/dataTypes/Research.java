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


public class Research{
	
	private String name;
	private String description;
	private ArrayList<ProjectField> atributtes;
	
	
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public ArrayList<ProjectField> getAtributtes() {
		return atributtes;
	}

	public void setAtributtes(ArrayList<ProjectField> atributtes) {
		this.atributtes = atributtes;
	}

	public Research(String name, String description) {
		super();
		this.name = name;
		this.description = description;
		atributtes=new ArrayList<ProjectField>();
	}
	
	public void addAtribbute(String name, String type){
		
		ProjectField tmpAt=new ProjectField(name,type);
		atributtes.add(tmpAt);
		
	}
	
public void printElement() {
		

		System.out.println(name+" : "+description);
		int n=atributtes.size();
		
		for(int i=0;i<n;i++){
			
			System.out.println("->"+atributtes.get(i).getName()+" : "+atributtes.get(i).getType());
			
		}
	
		
	}

}