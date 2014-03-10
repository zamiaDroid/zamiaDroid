package uni.projecte.dataTypes;

import uni.projecte.R;
import android.content.Context;

public class FagusUtils {
	
	  public static String translateFagusNatures(Context c, String currentValue){
		  
		  String[] natureness = c.getResources().getStringArray(R.array.natureness);
		  int pos=findString(natureness, currentValue);
		   	
		  if(pos>-1) {
			  
			  String[] naturenessEnglish = c.getResources().getStringArray(R.array.naturenessEnglish);
			  
			  return naturenessEnglish[pos];
			  
		  }
		  else return "";
		  
			
		  
		  
	  }
	  
	  public static String translateFagusPhenology(Context c, String currentValue){
		  
		  String[] natureness = c.getResources().getStringArray(R.array.phenology);
		  int pos=findString(natureness, currentValue);
		   	
		  if(pos>-1) {
			  
			  String[] naturenessEnglish = c.getResources().getStringArray(R.array.phenologyEnglish);
			  
			  return naturenessEnglish[pos];
			  
		  }
		  else return "";
		  
			
		  
		  
	  }
	  
	  
	  
		public static int findString(String[] items, String item){
		    
	    	int pos=-1;

			
			if(item!=null && !item.equals("")){
				
		    	int n=items.length;
		    	boolean found=false;
		    	int i;
		    	
		    	for(i=0; i<n && !found;i++){
		    		
		    		if (items[i].compareTo(item)==0){ found=true; pos=i;}
		    		
		    	}

		    	
			}
	    	
	    	return pos;

	    	
	    }

}
