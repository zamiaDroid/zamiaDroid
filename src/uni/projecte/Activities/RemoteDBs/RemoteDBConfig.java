package uni.projecte.Activities.RemoteDBs;

import uni.projecte.R;
import uni.projecte.R.id;
import uni.projecte.R.layout;
import uni.projecte.dataLayer.RemoteDBManager.objects.DataBaseHandler;
import uni.projecte.dataLayer.RemoteDBManager.objects.DatabaseListAdapter;
import android.app.Activity;
import android.os.Bundle;
import android.view.Display;
import android.widget.ExpandableListView;

public class RemoteDBConfig extends Activity{
	
	private String filum;

	
	  @Override
	  public void onCreate(Bundle savedInstanceState) {
		  
		   super.onCreate(savedInstanceState);
	       setContentView(R.layout.remote_db_config);
	       
	       
	       ExpandableListView list= (ExpandableListView)findViewById(R.id.eLVremoteDB);
		   Display newDisplay = getWindowManager().getDefaultDisplay(); 
    	   int width = newDisplay.getWidth();
  
    	   filum=getIntent().getExtras().getString("filum");
    		
    	   if(filum==null) filum="";
  
    	   DataBaseHandler dbH= new DataBaseHandler(this,filum);
    	    
    	   list.setIndicatorBounds(width-50, width-10);
       
	       list.setAdapter(new DatabaseListAdapter(this, dbH));
	       
	       if(dbH.getFilumCount()==1) list.expandGroup(0);

		    
	  }
	  
}
