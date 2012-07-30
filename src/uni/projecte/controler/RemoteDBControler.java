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

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;
import uni.projecte.dataLayer.RemoteDBManager.RemoteDBFilumList;
import uni.projecte.dataLayer.bd.DataBaseDbAdapter;
import uni.projecte.dataLayer.utils.StringUtils;
import uni.projecte.dataTypes.Utilities;

public class RemoteDBControler {
	
	private DataBaseDbAdapter remoteDBAdapter;
	private Context baseContext;
	
	private HashMap<String, ArrayList<String>> dbTerrritories;
	private String[] allDBs;

	
	
	public RemoteDBControler (Context baseContext){
		
		this.baseContext=baseContext;
		remoteDBAdapter=new DataBaseDbAdapter(baseContext);
		dbTerrritories= new HashMap<String, ArrayList<String>>();
		//initialData();
		checkDBConfig();
		
	
	}
	
	/*
	 * Initial dbLoad
	 * 
	 */
	
	public void initialData(){
		
		
		remoteDBAdapter.open();
		
			int id=(int) remoteDBAdapter.createRemoteDB("gbif");
				remoteDBAdapter.createRemoteDBFilum(id, "Flora", 0);
				remoteDBAdapter.createRemoteDBFilum(id, "Bryophytes", 0);
				remoteDBAdapter.createRemoteDBFilum(id, "Algae", 0);
				remoteDBAdapter.createRemoteDBFilum(id, "Fungi", 0);
				remoteDBAdapter.createRemoteDBFilum(id, "Lichens", 0);
				remoteDBAdapter.createRemoteDBFilum(id, "Vertebrates", 0);
					remoteDBAdapter.createRemoteDBFilum(id, "Aves", 0);
					remoteDBAdapter.createRemoteDBFilum(id, "Amphibia", 0);
					remoteDBAdapter.createRemoteDBFilum(id, "Mammalia", 0);
					remoteDBAdapter.createRemoteDBFilum(id, "Reptilia", 0);
				remoteDBAdapter.createRemoteDBFilum(id, "Invertebrate", 0);
				remoteDBAdapter.createRemoteDBFilum(id, "Reptilia", 0);
				remoteDBAdapter.createRemoteDBFilum(id, "Arthropods", 0);

	
		/*	id=(int) remoteDBAdapter.createRemoteDB("sifib");
				remoteDBAdapter.createRemoteDBFilum(id, "Flora", 1);

			id=(int) remoteDBAdapter.createRemoteDB("siba");
				remoteDBAdapter.createRemoteDBFilum(id, "Flora", 2);

			id=(int)remoteDBAdapter.createRemoteDB("orca");
				remoteDBAdapter.createRemoteDBFilum(id, "Flora", 3);

			id=(int)remoteDBAdapter.createRemoteDB("sivim");	
				remoteDBAdapter.createRemoteDBFilum(id, "Flora", 4); */
	
		
		remoteDBAdapter.close();
		
	}
	
	
	public ArrayList<String> getAvailableFilumsIds(String filum){
		
		ArrayList<String> filums=new ArrayList<String>();
		
		remoteDBAdapter.open();
		
			Cursor filumList=remoteDBAdapter.getAvailableFilumsList(filum);
			
			if(filumList!=null){
				
				filumList.moveToFirst();
				
			
				while(!filumList.isAfterLast()){
					
					filums.add(filumList.getString(2));
					filumList.moveToNext();
					
				}
				
				filumList.close();
				
			}
		
			
		remoteDBAdapter.close();
		
		return filums;
		
	}
	
	
	/*
	 * 
	 * Checks if remoteDB with @dBId works with @dbFilum
	 * 
	 */
	
	public boolean checkDBAvailability(long dbId, String dbFilum){
		
		remoteDBAdapter.open();
		
			boolean result=remoteDBAdapter.checkDataBaseAvailable(dbId, dbFilum);
		
		remoteDBAdapter.close();
		
		return result;
		
	}
	
	
	public String getDBTagdByDBId(int dbId){
		
		String dbTag="";
		
		if(allDBs!=null && dbId<allDBs.length+1) dbTag=allDBs[dbId-1]; 
		
		return dbTag;
		
	}
	
	public String[] getAvailableDBs() {

		remoteDBAdapter.open();
		Cursor list=remoteDBAdapter.getAllDBList();
		
		
		String[] dbArray=new String[list.getCount()];
		int i=0;
		
		while (!list.isAfterLast()) {

			dbArray[i]=list.getString(1);
			list.moveToNext();
			i++;
			
		}

		list.close();
		
		remoteDBAdapter.close();
			
		
		
		return dbArray;
	}
	
	
	public ArrayList<Integer> getDbsListByFilum(String filum) {

		ArrayList<Integer> dbList=new ArrayList<Integer>();
		
		remoteDBAdapter.open();

			Cursor list=remoteDBAdapter.getDataBaseAvailable(filum);
			
			while (!list.isAfterLast()) {

				if(list.getInt(1)>0) dbList.add(list.getInt(1));
				list.moveToNext();
				
			}
	
			list.close();
			
		remoteDBAdapter.close();
	
		return dbList;
	}

	public RemoteDBFilumList getAvailableDbByFilum(String filumId) {

		RemoteDBFilumList rl=new RemoteDBFilumList(filumId);
		
		remoteDBAdapter.open();
	
		Cursor dbList=remoteDBAdapter.getAvailableDbByFilum(filumId);
		
		if(dbList!=null){
			
			dbList.moveToFirst();
			
			while(!dbList.isAfterLast()){
				
				//DB_FILUM_ID,DB_ID_FK, FILUM,ORDER,,ACTIVE_FILUM
				
				rl.addDB(dbList.getInt(0),dbList.getString(6), dbList.getInt(5)+":", filumId, dbList.getInt(3), dbList.getInt(4)>0);

				dbList.moveToNext();
				
			}
			
			dbList.close();
			
		}

		remoteDBAdapter.close();
		
		return rl;
	}
	
	

	

	public boolean changeDBFilumState(int id) {

		remoteDBAdapter.open();
		
		boolean result=remoteDBAdapter.getDBFilumState(id);

		remoteDBAdapter.updateDBState(id, !result);
	
		remoteDBAdapter.close();
		
		return !result;
	}

	public void updateDbFilumOrder(int dbId, int order) {
		
		remoteDBAdapter.open();

		remoteDBAdapter.updateDbFilumOrder(dbId, order);
	
		remoteDBAdapter.close();
		
		
	}
	
	public void filterDbByTerritory(String locIberia, boolean[] allowedDB) {

		ArrayList<String> dbTags=dbTerrritories.get(locIberia);
		
		if(dbTags!=null){
			
			Iterator<String> it=dbTags.iterator();
			
			while(it.hasNext()){
				
				String dbTag=it.next();
				int pos=Utilities.findString(allDBs, dbTag);
				
				allowedDB[pos]=true;
				
				
			}
			
		}
		
	}

	
	public void checkDBConfig(){
		
		InputStream is;
		
		allDBs=getAvailableDBs();		
		
		
		remoteDBAdapter.open();
			
			try {
				
				is = baseContext.getResources().getAssets().open("dbConfig.cnf");
				String jsonTypes=StringUtils.convertStreamToString(is);
	
		        JSONObject json = new JSONObject(jsonTypes);
	              
	            JSONArray jsonTypesList=json.getJSONArray("dbList");
	            
	            for(int i=0;i<jsonTypesList.length();i++){
	          	  
	            	JSONObject proj=jsonTypesList.getJSONObject(i);
	          	  
	          	  	JSONArray nameArray=proj.names();
	          	  	JSONArray valArray=proj.toJSONArray(nameArray);
	          	  	
	          	  	checkDBExists(nameArray,valArray);
	                  
	            } 
				
			} 
			
			catch (IOException e) {
				
				e.printStackTrace();
			}
			catch (JSONException e) {
				
				e.printStackTrace();
			}
			
		remoteDBAdapter.close();

		}
	
	public void checkDBExists(JSONArray nameArray,JSONArray valArray) {
	
		try {
			
			String dbTag="";
			String territory="";
			//String[] filumList = null;
			JSONArray filumList=null;
			
			for(int i=0; i<nameArray.length(); i++){
				
					String name=nameArray.getString(i);
					
					if(name.equals("dbTag")){
						
						dbTag=valArray.getString(i);
						
					}
					else if(name.equals("type")){
	
						filumList=valArray.getJSONArray(i);
						
					}
					else if(name.equals("territory")){
						
						territory=valArray.getString(i);						
					}
				
			}
			
			
			long dbId=remoteDBAdapter.createRemoteDB(dbTag);
			
			if(dbId > -1){
				
				
				
				for(int i=0; i<filumList.length(); i++){
					
					String filum=filumList.getString(i);
					
					int count=remoteDBAdapter.getFilumsCount(filum);			
					remoteDBAdapter.createRemoteDBFilum((int)dbId, filumList.getString(i), count);	

				}
				
			}
			
			ArrayList<String> territoryDBs=dbTerrritories.get(territory);
			
			//territory not created
			if(territoryDBs==null){
				
				ArrayList<String> dbTags=new ArrayList<String>();
				dbTags.add(dbTag);
				dbTerrritories.put(territory, dbTags);
				
			}
			else{
				
				territoryDBs.add(dbTag);
				
				
			}
			
			
			
		
		} 
		
		catch (JSONException e) {
			
			e.printStackTrace();
							
		}
		
	}

	public int getDBCount() {

		return allDBs.length;
		
	}

	public void printAvailableDB(boolean[] allowedDB) {

        Log.i("CitationMap","--------- Available DB's  by Territory-----------");

		for(int i=0; i<allowedDB.length ; i++){
			
			Log.i("CitationMap",allDBs[i] +" : "+allowedDB[i]);

		}
		
        Log.i("CitationMap","----------------------------");
	     
	}

	


}
