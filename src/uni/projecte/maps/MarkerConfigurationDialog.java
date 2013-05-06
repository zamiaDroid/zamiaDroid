package uni.projecte.maps;

import uni.projecte.R;
import uni.projecte.controler.MapConfigControler;
import uni.projecte.controler.ProjectConfigControler;
import android.app.Dialog;
import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;


public class MarkerConfigurationDialog extends Dialog{

	public static final int UPDATE_PROJECT_MARKER=1;
	public static final int UPDATE_CITATION_MARKER=2;
	
	
	private Context mContext ;
	private LinearLayout ll;
	
	private long id;
	private String marker_id;
	private int mode;
	
	//private ProjectConfigControler projCnf;
	private MapConfigControler mapConfig;
	
	private Handler handler;
	
	private String[] idOfButtons = { 
			   "marker_bubble","marker_butterfly", "marker_snake", "marker_bee", 
			  "marker_turtle", "marker_algae", "marker_spider", "marker_clear",
			  "marker_birds", "marker_plant", "marker_fungus", "marker_mammal"
	};
	

	public MarkerConfigurationDialog(Context context,long id, Handler updateMarkers,int mode) {
		
	    super(context);
	    
	    mContext=context; 
	    this.id=id;
	    this.handler=updateMarkers;
	    this.mode=mode;	    
	    
	}
	

	@Override
	 protected void onCreate(final Bundle savedInstanceState){
		
	  super.onCreate(savedInstanceState);
	  
	  mapConfig = new MapConfigControler(mContext);
	  
	  ll=(LinearLayout) LayoutInflater.from(mContext).inflate(R.layout.mapchangemarker, null);
	  setContentView(ll);
	  
	  bindOnClickEvents();

	  loadSelectedMarker();
	  	
	  if(!marker_id.equals("marker")) setSelectedMarker();
	  
	 }

	private void loadSelectedMarker() {
  
		if(mode == UPDATE_CITATION_MARKER) marker_id=mapConfig.getCitationMapMarker(id);
		else marker_id=mapConfig.getProjectMapMarker(id);
		
	}


	private void bindOnClickEvents() {

		Resources mRes = mContext.getResources();
		  
		  for (int pos = 0; pos < idOfButtons.length; pos++) {
			  
		     Integer btnId = mRes.getIdentifier(idOfButtons[pos], "id",mContext.getPackageName());
		      
		     ImageButton ib = (ImageButton) ll.findViewById(btnId);
		     ib.setTag(idOfButtons[pos]);
		     
		     ib.setOnClickListener(markerClick);
		     
		  }
		
	}


	private void setSelectedMarker(){
		
  	  Resources mRes = mContext.getResources();
		
  	  ImageButton ib=(ImageButton)ll.findViewById(R.id.ibMarkerChosen);
		
  	  int resID = mRes.getIdentifier(marker_id, "drawable", mContext.getPackageName());      
		
  	  ib.setBackgroundResource(resID);
  	  
  	  updateMarkerId();
		
	}

	
	private void updateMarkerId() {

		if(mode == UPDATE_CITATION_MARKER) mapConfig.setCitationMapMarker(id,marker_id);
		else mapConfig.setProjectMapMarker(id,marker_id);

	}

	public android.view.View.OnClickListener markerClick = new android.view.View.OnClickListener() {
		
		public void onClick(View v) {
			
			marker_id=(String)v.getTag();
			
			setSelectedMarker();
			
			if(handler!=null){
				
				Message msg = Message.obtain();
				msg.what = 0;
				msg.obj = marker_id;		
				handler.sendMessage(msg);
				
			}
			
		}
	};


}
