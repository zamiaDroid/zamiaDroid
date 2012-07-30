package uni.projecte.dataLayer.RemoteDBManager;

import java.util.ArrayList;

import uni.projecte.dataLayer.RemoteDBManager.objects.RemoteDB;



public class RemoteDBFilumList{
	
	private String filumId;
	private ArrayList<RemoteDB> availableDb;
	
	public RemoteDBFilumList(String filumId){
		
		this.filumId=filumId;
		availableDb=new ArrayList<RemoteDB>();
		
	}
		
	public void addDB(int dbId, String dbName, String dbDesc,String dbFilum, int order, boolean enabled){
		
		availableDb.add(new RemoteDB(dbId, dbName, dbDesc, dbFilum, order, enabled));
		
	}
	
	public RemoteDB getDB(int pos){
		
		return availableDb.get(pos);
		
	}
	
	public int getDBCount(){
		
		return availableDb.size();
		
	}

	public void remove(RemoteDB rmDBCurrent) {

		availableDb.remove(rmDBCurrent);
		
	}

	public void add(int i, RemoteDB rmDBCurrent) {

		availableDb.add(i, rmDBCurrent);
		
	}
	
	
	
	
}





