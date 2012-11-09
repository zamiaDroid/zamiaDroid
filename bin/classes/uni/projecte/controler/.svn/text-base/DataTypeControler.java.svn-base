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
import java.util.List;

import android.content.Context;
import android.database.Cursor;

import uni.projecte.dataLayer.bd.DataTypeDbAdapter;
import uni.projecte.dataLayer.bd.FieldItemAdapter;
import uni.projecte.dataLayer.bd.ItemDTDbAdapter;


public class DataTypeControler {
	
	Context c;
	
	int imported;
	
	
	
	
	public DataTypeControler(Context c) {
		super();
		
		this.c=c;

		
		
		     	
		
	}
	
	
	
	private void clearDTdb(){
		
		  DataTypeDbAdapter dtHnd= new DataTypeDbAdapter(c);
			
		  dtHnd.open();
		  
		  Cursor llistat=dtHnd.fetchAllDTs();
		  llistat.moveToFirst();
		  int n=llistat.getCount();
		  
		  for (int i=0;i<n;i++){
			  
			  dtHnd.deleteDT(llistat.getLong(0));
			  
			  llistat.moveToNext();
			  
		  }
		  
		  dtHnd.close();
		  
		  
		  ItemDTDbAdapter itemHnd= new ItemDTDbAdapter(c);
			
		  itemHnd.open();
		  
		  llistat=itemHnd.fetchAllDtItems();
		  llistat.moveToFirst();
		  
		  n=llistat.getCount();
		  
		  for (int i=0;i<n;i++){
			  
			  itemHnd.deleteItemDT(llistat.getLong(0));
			  
			  llistat.moveToNext();
			  
		  }
		  
		  dtHnd.close();
		
	}
	
	public long addDT(String name, String description, String type){
		
		  DataTypeDbAdapter dtHnd= new DataTypeDbAdapter(c);
			
		  dtHnd.open();
		  
		  long result;
			  
		  
		  if (type.equals("simple")){
			  
			  result =dtHnd.createDT(1, name,description);
			    
			  
		  }
		  
		  else if (type.equals("complex")){
			  
			  result =dtHnd.createDT(2, name,description);
			  
			  
		  }
		  		  
		  else{
			  
			  //wrong type
			  
			  result=-2;
			  
		  }
		  
		  
		  
			
		  dtHnd.close();
		
		return result;
		
		
		
	}
	
	
	public long addItemsDTbyDTid(long dtId, String name, String moreInfo){
		
		ItemDTDbAdapter itemsHnd= new ItemDTDbAdapter(c);
		
		itemsHnd.open();

			long result=itemsHnd.addItemDT((int)dtId, name, moreInfo);
		
		itemsHnd.close();

		return result;
		
	}
	
	
	
	
	/*public void loadDataTypesFromDB(){
		
		DataTypesDbAdapter dtHnd= new DataTypesDbAdapter(c);
		
		dtHnd.open();
		
		ItemsDTDbAdapter itemsHnd= new ItemsDTDbAdapter(c);
		
		itemsHnd.open();
		
		Cursor dataTyesC=dtHnd.fetchAllDTs();
		
		dataTyesC.moveToFirst();
		
		int n=dataTyesC.getCount();
		
		for (int i=0;i<n;i++){
			
			String dtName=dataTyesC.getString(2);
			DataType dt=new DataType(dtName, dataTyesC.getString(3));
					
				Cursor items=itemsHnd.fetchDtItembyType(dataTyesC.getInt(0));
				items.moveToFirst();
				
				int m=items.getCount();
				
				ArrayList<String> itemsArray=new ArrayList<String>();
				
				for(int j=0;j<m;j++){
					
					itemsArray.add(items.getString(2));
					items.moveToNext();
					
				}
				
				
			dt.setItems(itemsArray);
			
			
			dataTyesC.moveToNext();
			
		}
		
		dtHnd.close();
		itemsHnd.close();


		
	}*/


    
    
	public List<CharSequence> getItemsArrayListbyFieldId(long fieldId){
		
		FieldItemAdapter itemsHnd= new FieldItemAdapter(c);
	
		itemsHnd.open();
		
		Cursor itemsC=itemsHnd.fetchItemsbyFieldId(fieldId);
		itemsC.moveToFirst();
		
		int n=itemsC.getCount();
		
	    List<CharSequence> values = new ArrayList<CharSequence>();  
		
		for(int i=0; i<n;i++){
			
			values.add(itemsC.getString(2));
			itemsC.moveToNext();
			
		}
		
		itemsC.close();
		itemsHnd.close();
		
		return values;
	
}
    
	
	public String[] getItemsbyFieldId(long fieldId){
		
		FieldItemAdapter itemsHnd= new FieldItemAdapter(c);
	
		itemsHnd.open();
		
		Cursor itemsC=itemsHnd.fetchItemsbyFieldId(fieldId);
		itemsC.moveToFirst();
		
		int n=itemsC.getCount();
		
		String[] itemList=new String[n];
		
		for(int i=0; i<n;i++){
			
			itemList[i]=itemsC.getString(2);
			itemsC.moveToNext();
			
		}
		
		itemsC.close();
		itemsHnd.close();
		
		return itemList;
	
}
	
	public String[] getItemsbySecondLevelFieldId(long fieldId){
		
		FieldItemAdapter itemsHnd= new FieldItemAdapter(c);
	
		itemsHnd.open();
		
		
		Cursor itemsC=itemsHnd.fetchItemsbySecondLevelFieldId(fieldId);
		itemsC.moveToFirst();
		
		int n=itemsC.getCount();
		
		String[] itemList=new String[n];
		
		for(int i=0; i<n;i++){
			
			
			itemList[i]=itemsC.getString(2);
			itemsC.moveToNext();
			
			
		}
		
		itemsC.close();
		itemsHnd.close();
		
		return itemList;
	
	
	
}
	

	public String[] getItemsbyDTId(long dtId){
		
		ItemDTDbAdapter itemsHnd= new ItemDTDbAdapter(c);
	
		itemsHnd.open();
		
		
		Cursor itemsC=itemsHnd.fetchDtItembyType(dtId);
		itemsC.moveToFirst();
		
		int n=itemsC.getCount();
		
		String[] itemList=new String[n];
		
		for(int i=0; i<n;i++){
			
			
			itemList[i]=itemsC.getString(2);
			itemsC.moveToNext();
			
			
		}
		
		itemsC.close();
		
		return itemList;
	
	
	
}

	public String[] getItemsbyDTName(String nameDt){
		
		DataTypeDbAdapter dtHnd= new DataTypeDbAdapter(c);
		ItemDTDbAdapter itemsHnd= new ItemDTDbAdapter(c);

		dtHnd.open();
		itemsHnd.open();
		
		Cursor cDT=dtHnd.fetchDTbyName(nameDt);
		cDT.moveToFirst();
		long id=cDT.getLong(0); 
		
		dtHnd.close();
		
		Cursor itemsC=itemsHnd.fetchDtItembyType(id);
		itemsC.moveToFirst();
		
		int n=itemsC.getCount();
		
		String[] itemList=new String[n];
		
		for(int i=0; i<n;i++){
			
			
			itemList[i]=itemsC.getString(2);
			itemsC.moveToNext();
			
			
		}
		
		itemsHnd.close();
		
		return itemList;
		
		
		
	}
	
	public int getDtType(String dtName){
		
		DataTypeDbAdapter dtHnd= new DataTypeDbAdapter(c);
		
		dtHnd.open();
		
		Cursor dtC=dtHnd.fetchDTbyName(dtName);
		dtC.moveToFirst();
		
		int type=dtC.getInt(1);
		
		dtHnd.close();
		
		return type;
		
	}


	public String[] getDTList(){
		
		DataTypeDbAdapter dtHnd= new DataTypeDbAdapter(c);
		
		dtHnd.open();
			
		Cursor dtC=dtHnd.fetchAllDTs();
		dtC.moveToFirst();
		
		int n= dtC.getCount();
		
		String[] resultList=new String[n];
		
		for(int i=0; i<n;i++){
			
			
			resultList[i]=dtC.getString(2);
			dtC.moveToNext();
			
			
		}
				
		
		dtC.close();
		
		dtHnd.close();
		
		return resultList;
		
	}

	public int getImportedItems() {

		return this.imported;
		
	}






}
