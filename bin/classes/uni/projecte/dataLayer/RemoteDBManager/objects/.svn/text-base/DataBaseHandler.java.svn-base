package uni.projecte.dataLayer.RemoteDBManager.objects;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import uni.projecte.controler.RemoteDBControler;
import uni.projecte.dataLayer.RemoteDBManager.RemoteDBFilumList;
import android.content.Context;

public class DataBaseHandler {
	
	private RemoteDBControler rmDbControler;
	private Context baseContext;
	
	private ArrayList<String> filumsListIds;

	private HashMap<String, RemoteDBFilumList> filumLists;
	
	private String filum="";	
	
	
	public DataBaseHandler(Context baseContext,String filum){
		
		this.baseContext=baseContext;
		this.filum=filum;
		
		rmDbControler=new RemoteDBControler(baseContext);
		filumsListIds=new ArrayList<String>();
		filumLists=new HashMap<String, RemoteDBFilumList>(); 
		
		filumsListIds=rmDbControler.getAvailableFilumsIds(filum);
		
		fillFilumList();
				
	}
	
	
	
	
	public void fillFilumList(){

		Iterator<String> it=filumsListIds.iterator();
		
		while(it.hasNext()){
			
			String filumId=it.next();
			
			RemoteDBFilumList rl=rmDbControler.getAvailableDbByFilum(filumId);
			filumLists.put(filumId, rl);
			
		}
		
	}
	


	public String getFilumAtPostion(int i){
		
		return filumsListIds.get(i);
		
	}
	
	public RemoteDB getDataBase(int i, int j){
		
		String filumId=filumsListIds.get(i);
		 
		return filumLists.get(filumId).getDB(j);
		
		
	}
	
	public void moveDownDBFilum(int order, String filum){
		
		RemoteDB rmDBCurrent=filumLists.get(filum).getDB(order);
		rmDBCurrent.setOrder(order+1);
		
		RemoteDB rmDBNext=filumLists.get(filum).getDB(order+1);
		rmDBNext.setOrder(order);

			
		filumLists.get(filum).remove(rmDBCurrent);
		filumLists.get(filum).add(order+1,rmDBCurrent);
		
		/*
		 * Update DBs
		 * 
		 */
		
		rmDbControler.updateDbFilumOrder(rmDBCurrent.getDbId(),order+1);
		rmDbControler.updateDbFilumOrder(rmDBNext.getDbId(),order);
		
	
		
	}
	
	public void moveUpDBFilum(int order, String filum){
		
		RemoteDB rmDBCurrent=filumLists.get(filum).getDB(order);
		rmDBCurrent.setOrder(order-1);
		
		RemoteDB rmDBPrev=filumLists.get(filum).getDB(order-1);
		rmDBPrev.setOrder(order);

			
		filumLists.get(filum).remove(rmDBCurrent);
		filumLists.get(filum).add(order-1,rmDBCurrent);
		
		/*
		 * Update DBs
		 * 
		 */
		
		rmDbControler.updateDbFilumOrder(rmDBCurrent.getDbId(),order-1);
		rmDbControler.updateDbFilumOrder(rmDBPrev.getDbId(),order);
		
		
		
	}
	
	public int getDataBaseCount(int i){
		
		String filumId=filumsListIds.get(i);
		 
		return filumLists.get(filumId).getDBCount();
		
		
	}
	
	public int getFilumCount(){
		
		return filumsListIds.size();
		
	}




	public boolean changeDBFilumState(int id) {
		
		return rmDbControler.changeDBFilumState(id);
		
	}
	
	

}




