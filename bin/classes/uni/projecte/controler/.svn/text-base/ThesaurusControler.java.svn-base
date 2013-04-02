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
import uni.projecte.dataLayer.ThesaurusManager.ThesaurusElement;
import uni.projecte.dataLayer.ThesaurusManager.ListAdapters.ThesaurusAutoCompleteAdapter;
import uni.projecte.dataLayer.ThesaurusManager.ListAdapters.ThesaurusGenusAutoCompleteAdapter;
import uni.projecte.dataLayer.ThesaurusManager.xml.PlainThesaurusReader;
import uni.projecte.dataLayer.ThesaurusManager.xml.ThesaurusItem;
import uni.projecte.dataLayer.ThesaurusManager.xml.ThesaurusXMLparser;
import uni.projecte.dataLayer.bd.ThesaurusIndexDbAdapter;
import uni.projecte.dataLayer.bd.ThesaurusItemsDbAdapter;
import uni.projecte.dataTypes.Utilities;
import android.content.Context;
import android.database.Cursor;
import android.widget.AutoCompleteTextView;


public class ThesaurusControler {
	
	public final String defaultTH = "Flora";

	private Context c;
	
	private static ThesaurusItemsDbAdapter thDbAdapter;
	private ThesaurusIndexDbAdapter thIndexAdapter;
	private ThesaurusXMLparser thXMLp;
	public int numElem;
	public Cursor allTc;
	
	private String thType;
	
	
	public ThesaurusControler(Context c){
		
		this.c=c;
		
	}
	
	public void closeCursors(){
		
		if(allTc!=null) allTc.close();
		
	}
	
	
	

	public boolean initThReader(String thName){

		
		if (thName==null || thName.compareTo("")==0) {
			
			return false;
																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																						
		}
		
		else{
			

			thIndexAdapter= new ThesaurusIndexDbAdapter(c);
			thIndexAdapter.open();
		
			Cursor cursor=thIndexAdapter.fetchThbyName(thName);
			cursor.moveToFirst();
			
			if(cursor.getCount()>0){
			
			String tableName=cursor.getString(2);
				
				thIndexAdapter.close();
	
				cursor.close();
				
				thDbAdapter= new ThesaurusItemsDbAdapter(c);
				thDbAdapter.open(tableName);

				return true;

				
			}
			else{
				
				thIndexAdapter.close();
				
				cursor.close();
				
				return false;
				
			}
			
		
		}
		
	}
	
	public boolean checkThWorking(String thName){
		
		if (thName==null || thName.equals("") || thName.equals("null")) {
			
			return false;
																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																						
		}
		
		else{
			
			thIndexAdapter= new ThesaurusIndexDbAdapter(c);
			thIndexAdapter.open();
		
			Cursor cursor=thIndexAdapter.fetchThbyName(thName);
			cursor.moveToFirst();
			
			if(cursor.getCount()>0){
							
				thIndexAdapter.close();

				return true;

			}
			else return false;
						
		}
		
		
	}
	
	
	
	public int removeTh(String thName){
		
		//its necessary to check if any project needs this Th
		ProjectControler rsC= new ProjectControler(c);
		
		int projUsesTh=rsC.isUsed(thName);
		
		if(projUsesTh<1){ 

			thIndexAdapter= new ThesaurusIndexDbAdapter(c);
			thIndexAdapter.open();
			
				Cursor index=thIndexAdapter.fetchThbyName(thName);
				index.moveToFirst();
			
				String dbName=index.getString(2);
				index.close();
			
				thIndexAdapter.removeThbyName(thName);
	
			thIndexAdapter.close();
			
			
			thDbAdapter= new ThesaurusItemsDbAdapter(c);
	    	thDbAdapter.open(dbName);
	    	
	    		thDbAdapter.dropTable(dbName);
		    	
			thDbAdapter.close();
			
			return 0;
		
		}
		
		else {
			
			return projUsesTh;
			
		}
		
		
	}
	
	public String removeThOverwrite(String thRemoteId) {

		//its not necessary to check if any project needs it 
		//because thesaurus will be reloaded again
		
		thIndexAdapter= new ThesaurusIndexDbAdapter(c);
		thIndexAdapter.open();
			
		Cursor index=thIndexAdapter.fetchRemoteTh(thRemoteId);
		index.moveToFirst();
			
			String thName=index.getString(1);
			String dbName=index.getString(2);
			
		index.close();
			
		thIndexAdapter.removeThbyName(thName);
		thIndexAdapter.close();
			
		thDbAdapter= new ThesaurusItemsDbAdapter(c);
	   	thDbAdapter.open(dbName);
	    	
	   	thDbAdapter.dropTable(dbName);
		thDbAdapter.close();
	
		return thName;
	}

	
	public void closeThReader(){
		
		if(thDbAdapter!=null) thDbAdapter.close();
		
	}
	
	public void removeThList(){
		
		thIndexAdapter= new ThesaurusIndexDbAdapter(c);
		thIndexAdapter.open();
		
		thIndexAdapter.deleteDatabase();
		
		thIndexAdapter.close();
		
		thDbAdapter= new ThesaurusItemsDbAdapter(c);
		thDbAdapter.open("dbProva");
		
		thDbAdapter.deleteDatabase();
		
		thDbAdapter.close();
		
	}
	
	public Cursor fetchAllThesaurus(){
		
		thIndexAdapter= new ThesaurusIndexDbAdapter(c);
		thIndexAdapter.open();
		
		Cursor c=thIndexAdapter.fetchAllTH();
		c.moveToFirst();
		
		thIndexAdapter.close();
		
		return c;
		
	}
	
	public Cursor fetchThesaurusItembyName(String name){
		
		String[] spec=name.toString().split(" ");
 
		Cursor c=null;
 	    
 	    if(spec.length>3){
 	    
 	    	String[] sub=name.split("subsp. ");
 	    	String subL=null;
 	    	
 	    	if(sub.length>1){
 	    		subL=sub[1].split(" ")[0];
 	    	}
 	    	else{
 	    		
 	    		subL="";
 	    	}

 	    	c=thDbAdapter.fetchTbhItem(spec[0], spec[1], subL);
 			c.moveToFirst();

 	    }
 	    else if(spec.length>1){
 	    	
 	    	c=thDbAdapter.fetchTbhItem(spec[0], spec[1], "");
			c.moveToFirst();
 	    	
 	    }
 	    
 	    else {
 	     	    	
 	    }
 	    
		
		
		return c;
		
	}
	
	public String fetchThesaurusSynonymous(String icode){
		
		Cursor synon;
 	    
		synon=thDbAdapter.fetchSynonymous(icode);

		synon.moveToFirst();
		
		String completeName="";
		
				
		if(synon.getString(3).equals("")){
  			
  			completeName=synon.getString(1)+" "+synon.getString(2)+" "+synon.getString(4);
  			
  		}
  		else{
  			
  			completeName=synon.getString(1)+" "+synon.getString(2)+" "+synon.getString(4)+" "+synon.getString(6)+" "+synon.getString(3)+" "+synon.getString(4);

  			
  		}
		
		synon.close();
		
		return completeName;
		
	}
	
	public HashMap<String, String> getRemoteThUpdatedStatus(){
		
		HashMap<String, String> remoteThList=new HashMap<String, String>();
		
		thIndexAdapter= new ThesaurusIndexDbAdapter(c);
		thIndexAdapter.open();
		
			Cursor list=thIndexAdapter.fetchRemoteTh();
			
			if(list!=null){
				
				list.moveToFirst();
				
				while(!list.isAfterLast()){
					
					//<thRemoteId,lastUpdate> 
					remoteThList.put(list.getString(3), list.getString(2));
					list.moveToNext();
					
				}
				
			}
		
		thIndexAdapter.close();
		
		return remoteThList;

	}
	
	
	public Cursor getThInfo(String thName){
		
		thIndexAdapter= new ThesaurusIndexDbAdapter(c);
		thIndexAdapter.open();
		
		Cursor c=thIndexAdapter.fetchThbyName(thName);
		c.moveToFirst();
		
		thIndexAdapter.close();
		
		return c;
		
		
	}
	
	
	public ThesaurusElement loadThInfo(){
		
		ThesaurusElement tmpThElem=null;
		
		thIndexAdapter = new ThesaurusIndexDbAdapter(c);
		thIndexAdapter.open();

	
		Cursor list=thIndexAdapter.fetchAllTH();
		list.moveToFirst();

		if(list!=null){
			
			tmpThElem=new ThesaurusElement(list.getLong(0),list.getString(1), list.getString(4),list.getInt(3),list.getString(5),list.getString(6));
			
		}
		
		
		list.close();
		
		thIndexAdapter.close();
		
		return tmpThElem;
		
	}

	
	public long createThesaurus(String name,String thRemoteId,String filum,String sourceId,String sourceType){
		
		String dbName=name.replace(" ", "_");
		
		String thType=determineThType(filum);
		
		thIndexAdapter = new ThesaurusIndexDbAdapter(c);
		thIndexAdapter.open();
	    		
		long thId=thIndexAdapter.createThWithType(name,dbName,thRemoteId, 0,thType,sourceId,sourceType);
		
		thIndexAdapter.close();  
		
		return thId;
		
		
	}
	
	public String determineThType(String filum) {
		
		String thType="";
		
		String[] biocatFilumsNames = c.getResources().getStringArray(R.array.thesaurusFilumsEnglish);
		
		if (Utilities.findString(biocatFilumsNames, filum)>-1) thType=filum;
		else{
		
			String[] biocatLetters = c.getResources().getStringArray(R.array.thesaurusFilumsLetters);
		   	int pos=Utilities.findString(biocatLetters, filum);
			if(pos>-1) thType=biocatFilumsNames[pos];
		
		}

		return thType;
	}

	public boolean addThItemsPlainTh(long thId,String name, String[] fieldsMapping, PlainThesaurusReader ptR){
		
		String dbName=name.replace(" ", "_");

		thDbAdapter= new ThesaurusItemsDbAdapter(c);
	    thDbAdapter.open(dbName);
	 
	    boolean error= ptR.readFile(fieldsMapping, this);
		    
	
		    if(!error){
		    	
					thDbAdapter.endTransaction();
										
					Cursor curItems= thDbAdapter.fetchNumAllItems();
			    	curItems.moveToFirst();
			    	numElem=curItems.getCount();
			    	curItems.close();
	
			thDbAdapter.close();
			
			thIndexAdapter = new ThesaurusIndexDbAdapter(c);
			thIndexAdapter.open();
		
			thIndexAdapter.updateThCount(thId,numElem);
			if(thType!=null && !thType.equals("")) thIndexAdapter.updateThType(thId,thType);
	
				thIndexAdapter.close();  
			
		    }
		    
		    else{
		    	
		    	thDbAdapter.dropTable(dbName);
		    	
				thDbAdapter.close();
		    	
		    	
		    }

		    return error;
		
	}
	
	
	public boolean addThItems(long thId,String name, String url){
		
		String dbName=name.replace(" ", "_");

		thDbAdapter= new ThesaurusItemsDbAdapter(c);
	    thDbAdapter.open(dbName);
	 
				thXMLp= new ThesaurusXMLparser(this);
		    	thXMLp.readXML(c, url, false);
		    	
		    	boolean error=thXMLp.isError();
		    	
		    if(!error){
		    	
					thDbAdapter.endTransaction();
					
					
					
					Cursor curItems= thDbAdapter.fetchNumAllItems();
			    	curItems.moveToFirst();
			    	numElem=curItems.getCount();
			    	curItems.close();
	
			thDbAdapter.close();
			
			thIndexAdapter = new ThesaurusIndexDbAdapter(c);
			thIndexAdapter.open();
		
			thIndexAdapter.updateThCount(thId,numElem);
			if(!thType.equals("")) thIndexAdapter.updateThType(thId,thType);
	
				thIndexAdapter.close();  
			
		    }
		    
		    else{
		    	
		    	thDbAdapter.dropTable(dbName);
		    	
				thDbAdapter.close();
		    	
		    	
		    }

		    return error;
		
	}
	
	
	public ArrayList<ThesaurusElement> fetchAllTh(){
		
		ArrayList<ThesaurusElement> thList=new ArrayList<ThesaurusElement>();
		
		thIndexAdapter = new ThesaurusIndexDbAdapter(c);
		thIndexAdapter.open();

	
		Cursor list=thIndexAdapter.fetchAllTH();
		list.moveToFirst();
		
		while(!list.isAfterLast()){
			
			ThesaurusElement tmpThElem=new ThesaurusElement(list.getLong(0),list.getString(1), list.getString(4),list.getInt(3),list.getString(5),list.getString(6));
			
			thList.add(tmpThElem);
			
			list.moveToNext();
		}
		
		list.close();
		
		thIndexAdapter.close();
		
		return thList;
		
	}
	
	
	public String[] getThList(){
		
		thIndexAdapter= new ThesaurusIndexDbAdapter(c);
		thIndexAdapter.open();
		
		Cursor c=thIndexAdapter.fetchAllTH();
		c.moveToFirst();
		
		int n= c.getCount();
		
		String[] resultList=new String[n];
		
		for(int i=0; i<n;i++){
			
			
			resultList[i]=c.getString(1);
			c.moveToNext();
			
			
		}
		
		c.close();
		thIndexAdapter.close();
		
		
		return resultList;
			
		
		
	}
	
	
	public void startTransaction(){
		
		thDbAdapter.startTransaction();
		
	}
    
	
	public void endTransaction(){
		
		thDbAdapter.endTransaction();
		
	}
	
	
    public void addElement(String genus, String specie, String subspecie, String iCode,String nameCode, String author,String subAuthor,String rank){
    	
    	thDbAdapter.addThesaurusItem(genus, specie, subspecie, iCode, nameCode, author,subAuthor,rank);
    	
    }
    
    

	public long addThElement(ThesaurusItem thItem) {

		long thItemId=thDbAdapter.addThesaurusItem(thItem.getGenus(), thItem.getSpecificEpithet(), thItem.getInfraspecificEpithet(), thItem.getPrimaryKey(), thItem.getSecondaryKey(), thItem.getSpecificEpithetAuthor(),thItem.getInfraspecificEpithetAuthor(),thItem.getInfraspecificRank());
		
		return thItemId;
		
	}
	
	public void removeThElement(long thItemId) {

		thDbAdapter.removeThesaurusItem(thItemId);
		
	}
    
    public void close(){
    	
    	thDbAdapter.close();
    	
    }

	public Cursor getNextItems(String selection) {
		
		allTc = thDbAdapter.fetchNext(selection);
		allTc.moveToFirst();

		return allTc;
	}
	
	public Cursor getGenusNextItems(String selection) {
		
		allTc = thDbAdapter.fetchGenusNext(selection);
		allTc.moveToFirst();

		return allTc;
	}
    

	
	
	public ThesaurusAutoCompleteAdapter fillData(AutoCompleteTextView auto)  {
		
		allTc= fetchAllTc();	 
		allTc.moveToFirst();
    	
		ThesaurusAutoCompleteAdapter thList = new ThesaurusAutoCompleteAdapter(c, allTc,auto,this);

		return thList;
  }
	
	public ThesaurusGenusAutoCompleteAdapter fillGenusData(AutoCompleteTextView auto)  {
		
		allTc= fetchAllTc();	 
		allTc.moveToFirst();
    	
		ThesaurusGenusAutoCompleteAdapter thList = new ThesaurusGenusAutoCompleteAdapter(c, allTc,auto,this);

		return thList;
  }
	
	
	
	private Cursor fetchAllTc() {

	    return thDbAdapter.fetchAllItems();

	}




	public void changeProjectTh(long rsId,String thName) {

		ProjectControler rc=new ProjectControler(c);
		rc.changeTh(rsId,thName);
		
	} 
	
	
	/*
	 * Load codiOrc from @taxonName
	 * 
	 */
	
	public String loadRemoteNameCode(String taxonName){
		
		String codiOrc="";
		 
		Cursor c=fetchThesaurusItembyName(taxonName);
		 if(c!=null && c.getCount()>0 && c.getString(5)!=null){ 
			 
			 codiOrc=c.getString(5);
			 c.close();

		 }
		 
		 return codiOrc;

		
	}

	public void updateType(String filum) {

		this.thType=determineThType(filum);
		
	}

	public String getTHType(String thName) {
		
		String thType="";

		thIndexAdapter= new ThesaurusIndexDbAdapter(c);
		thIndexAdapter.open();
		
		Cursor c=thIndexAdapter.fetchThbyName(thName);
		c.moveToFirst();
		
		
			if(c.getCount()>0 && c.getString(4)!=null) thType=c.getString(4);
		
		thIndexAdapter.close();
	
		c.close();
		
		return thType;
	}

	public void updateThCount(long thId, int newNumElem) {
		
		
		thIndexAdapter = new ThesaurusIndexDbAdapter(c);
		thIndexAdapter.open();
	
		thIndexAdapter.updateThCount(thId,newNumElem);
	
		thIndexAdapter.close();  
	
			
	}
	
	
	public void unlinkTh(long thId) {

		thIndexAdapter = new ThesaurusIndexDbAdapter(c);
		thIndexAdapter.open();
	
		thIndexAdapter.unlinkTh(thId);
	
		thIndexAdapter.close();  
		
	}

	public boolean checkTaxonBelongs(String taxonName) {

		 String codiOrc=loadRemoteNameCode(taxonName);
		 
		 if(codiOrc!=null && !codiOrc.equals("")) return true;
		 else return false;
		 
		 
	}



	
}
	
	
   	 
   	 