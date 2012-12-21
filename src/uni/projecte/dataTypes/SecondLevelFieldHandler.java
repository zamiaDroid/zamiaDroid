package uni.projecte.dataTypes;

import java.util.Date;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Set;

import uni.projecte.controler.CitationSecondLevelControler;
import android.content.Context;
import android.text.format.DateFormat;
import android.widget.TextView;



public class SecondLevelFieldHandler {
	
	   private Hashtable<Integer, SecondLevelField> secLevFields;
	   private Context baseContext;
	   

	public SecondLevelFieldHandler(Context c){
		
		this.baseContext=c;
        secLevFields = new Hashtable<Integer, SecondLevelField>();
		
	}
	
	public String addSecondLevelField(int id,String fieldName){
		
		String secondLevelId=createSecondLevelIdentifier(fieldName);
		
		SecondLevelField sLF= new SecondLevelField(secondLevelId);
		
		secLevFields.put(id,sLF);
		
		return secondLevelId;
		
	}
	
	public String addSecondLevelField(int id,String fieldName, TextView numElements){
		
		String secondLevelId=createSecondLevelIdentifier(fieldName);
		
		SecondLevelField sLF= new SecondLevelField(secondLevelId,numElements);
		
		secLevFields.put(id,sLF);
		
		return secondLevelId;
		
	}
	
	
	public String replaceSecondLevelField(int id, String fieldName) {

		String secondLevelId=createSecondLevelIdentifier(fieldName);
		
		SecondLevelField scF=secLevFields.get(id);
		
		TextView counter=scF.getCounter();
		
		counter.setText("");
		
    	secLevFields.remove(id);

		SecondLevelField sLF= new SecondLevelField(secondLevelId,counter);
		secLevFields.put(id,sLF);
		
		return secondLevelId;

    	
	}
	
	public String getSecLevelIdByFieldId(int fieldId){
		
		return secLevFields.get(fieldId).getId();
		
	}
	
	public TextView getSecLevelCounterByFieldId(int fieldId){
		
		return secLevFields.get(fieldId).getCounter();
		
	}
	
	public int getSecLevelChildrenByFieldId(int fieldId){
		
		return secLevFields.get(fieldId).getChildren();
		
	}


	public void updateNumCitations(int fieldId, int numCitations){
		
		secLevFields.get(fieldId).incChildren(numCitations);
	
	}
	
	public void setNumCitations(int fieldId, int numCitations){
		
		secLevFields.get(fieldId).setChildren(numCitations);
		
	}
	
	
	private String createSecondLevelIdentifier(String fieldName){
		
			Date date = new Date();
	    	date.getDate();

	       return fieldName.toLowerCase()+"_"+(String) DateFormat.format("yyyy-MM-dd_kk:mm:ss",date);
			
		}
	

	
	
    /*
     * 
     * If we finish the citation Activity and it has secondLevelFields, the filed secondLevelFields have to be removed.
     * 
     */
    
    public void removeSecondLevelFields() {
    	
    	
    	CitationSecondLevelControler slCont=new CitationSecondLevelControler(baseContext);
    	
    	
    	if(secLevFields.size()>0){
    		
    		Set<Integer> set=secLevFields.keySet();
    		Iterator<Integer> iter = set.iterator(); 
    		     
    		while ( iter.hasNext ()){  
    		
    			SecondLevelField secLevelField=secLevFields.get(iter.next());

    			slCont.removeCitationsBySLId(secLevelField.getId());
    			
    			
    		}  	
    		
    	}
    	
		
	}



}



class SecondLevelField{

	private String secondLevelId;
	private int children;
	private TextView counter;
	
	
	public SecondLevelField(String secondLevelId, TextView numElements){
		
		this.secondLevelId=secondLevelId;
		this.children=0;
		this.counter=numElements;
		
	}
	
	public SecondLevelField(String secondLevelId){
		
		this.secondLevelId=secondLevelId;
		this.children=0;
		
	}
	
	
	public String getId() {
		return secondLevelId;
	}
	public void setSecondLevelId(String secondLevelId) {
		this.secondLevelId = secondLevelId;
	}
	public int getChildren() {
		return children;
	}
	public void setChildren(int chuildren) {
		this.children = chuildren;
	}
	
	public void incChildren(int child) {
		this.children = this.children+child;
	}


	public TextView getCounter() {
		return counter;
	}


	public void setCounter(TextView counter) {
		this.counter = counter;
	}
	
	
}
