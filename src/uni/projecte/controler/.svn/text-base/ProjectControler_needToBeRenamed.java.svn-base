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

import uni.projecte.dataLayer.ProjectManager.xml.ProjectXMLparser;
import android.content.Context;
import android.util.Log;

public class ProjectControler_needToBeRenamed {
		
	/*category field types*/
	
	public static int ECO;
	public static int GEO;
	public static int TAXON_ATT;
	
	public static int ORGANISM_ATT;
	public static int HERB;
	
	private ProjectControler rsCnt;
	
	private ProjectXMLparser prXMLparser;
	
	private Context context;
	
	
	public ProjectControler_needToBeRenamed(Context c){
		
		context=c;
		
		rsCnt=new ProjectControler(c);
		
	}
	
	public long importProject(String name, String desc, String url){
		
		Log.d("Project","Import Project Start");
		
		long projId=rsCnt.createProject(name, desc,"");
		
		if(projId>0){
			
			rsCnt.startTransaction();

			prXMLparser= new ProjectXMLparser(rsCnt);
			prXMLparser.readXML(context, url, false);
			
			rsCnt.endTransaction();
			
			if(prXMLparser.isError()){
				
				rsCnt.removeProject(projId);
				Log.d("Project","Import Project | Error | Wrong file ");
				projId=-2;

			}
			else{
				
				Log.d("Project","Import Project End");

				
			}
			
		}
		
		
	
		return projId;
		
		
	}
	
	public void addThesaurus(String thName){
		
		
		
	}
	
	public void addField(String catName,String fieldName){
		
		
		
		
	}
	
	
	

}
