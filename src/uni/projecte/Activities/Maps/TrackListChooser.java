package uni.projecte.Activities.Maps;

import java.util.ArrayList;
import java.util.List;

import uni.projecte.R;
import uni.projecte.R.id;
import uni.projecte.R.layout;
import uni.projecte.R.string;
import uni.projecte.controler.ProjectControler;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

import com.google.android.apps.mytracks.content.MyTracksProviderUtils;
import com.google.android.apps.mytracks.content.Track;

public class TrackListChooser extends Activity{

	 private MyTracksProviderUtils myTracksProviderUtils;
	 private long projId=-1;
	 
	 private String projName="proj_botanic";
	 
	 private ListView lvTrackList;
	 private TrackListAdapter tkListAdap;
	 
	 private Button allTracks;
	 private Button projectTracks;
	 
	 private boolean allTracksEnabled=false;

	 
	  @Override
	public void onCreate(Bundle savedInstanceState) {
		  
		   super.onCreate(savedInstanceState);
	       setContentView(R.layout.tracklistchooser);
	       
	       lvTrackList=(ListView)findViewById(R.id.lvTrackList);
	       allTracks=(Button)findViewById(R.id.btTracksAll);
	       projectTracks=(Button)findViewById(R.id.btTracksByProj);
	       
	       allTracks.setOnClickListener(allTracksListener);
	       projectTracks.setOnClickListener(concreteProjectListener);

	       myTracksProviderUtils = MyTracksProviderUtils.Factory.get(this);
	       
	       projId=getIntent().getExtras().getLong("projId");

	       ProjectControler rC=new ProjectControler(this);
	       rC.loadProjectInfoById(projId);
	       projName=rC.getCleanProjectName();
	       
	       projectTracks.setText(rC.getName());

	       loadTrackList(allTracksEnabled);
	       
	       lvTrackList.setOnItemClickListener(theListListener);
	       lvTrackList.setOnItemLongClickListener(theLongListListener);

		    
	  }
	  
	  private void loadTrackList(boolean completeList) {
		  
		  String filter="zdt_";
		  
		  if(!completeList) filter=filter+projName+"_";

		  List<Track> tracks = myTracksProviderUtils.getAllTracks();
	       ArrayList<Track> selectedTracks=new ArrayList<Track>();
  	    
	       for (Track track : tracks) {
	   	    
	    	   if(track.getName().startsWith(filter)) selectedTracks.add(track);
	   	 	
	   	 	}

	       tkListAdap=new TrackListAdapter(this, selectedTracks);
	       lvTrackList.setAdapter(tkListAdap);

	}
	  
	  private String getTrackProject(long trackId){
		  
		  
		  Track track=myTracksProviderUtils.getTrack(trackId);
		  return track.getCategory();
		  
		  
	  }
	  
		private OnClickListener allTracksListener = new OnClickListener()
	    {
	        public void onClick(View v)
	        {                        

	        	allTracksEnabled=true;
	        	loadTrackList(allTracksEnabled);
	           
	        }
	    };
	    
		private OnClickListener concreteProjectListener = new OnClickListener()
	    {
	        public void onClick(View v)
	        {                        
	        	    	
        	
	        	allTracksEnabled=false;
	        	loadTrackList(allTracksEnabled);
	        	
	        	
	        }
	    };
	
	

	public OnItemClickListener theListListener = new OnItemClickListener() {
		    
		    public void onItemClick(android.widget.AdapterView<?> parent, View v, int position, long id) {

		    	long trackId=Long.valueOf(v.getId());
		    	
		    	Intent intent = new Intent();
                intent.putExtra("trackId",trackId);
	       
	            setResult(RESULT_OK,intent);
	            
	            finish();
		   	
		    	
		    }
		    
	};
		    

		    
		    
		    public OnItemLongClickListener theLongListListener= new OnItemLongClickListener() {
			    
			    public boolean onItemLongClick(android.widget.AdapterView<?> parent, final View v, int position, long id) {
			        
			    	final long trackId=Long.valueOf(v.getId());
			    	
			    	String trackName=myTracksProviderUtils.getTrack(trackId).getName();
			    	
			    	AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
	 		    	
	 		    	builder.setMessage(getString(R.string.trackRemoveDialog)+" "+trackName+"?")
	 		    	       .setCancelable(false)
	 		    	       .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
	 		    	           public void onClick(DialogInterface dialog, int id) {
	 		    
	 		    	        	   
	 		    	        	   myTracksProviderUtils.deleteTrack(trackId);
	 		    	        	   loadTrackList(allTracksEnabled);
	 		    	        		        	   
	 		    	           }

	 						
	 		    	       })
	 		    	       .setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
	 		    	           public void onClick(DialogInterface dialog, int id) {
	 		    	                
	 		    
	 		    	                
	 		    	           }

	 						
	 		    	       });
	 		    	
	 		    	AlertDialog alert = builder.create();
	 		    	
	 		    	alert.show();

		        	
		        	return true;

			    	
			    }
			    
			    
		    };
		    
	  
	  
	  private static class TrackListAdapter extends BaseAdapter{
			 
			private LayoutInflater mInflater;
			ArrayList<Track> trackList;

		    
			 public TrackListAdapter(Context context,ArrayList<Track> trackList) {
			 
				 mInflater = LayoutInflater.from(context);
			 
				 this.trackList=trackList;
		         
			 }

		
			public int getCount() {
				 return trackList.size();
			 }
		
			 public Object getItem(int position) {
			 return position;
			 }
		
			 public long getItemId(int position) {
			 return position;
			 }
		
			 public View getView(int position, View convertView, ViewGroup parent) {
			 ViewHolder holder;
			 if (convertView == null) {
		    	 
				 convertView = mInflater.inflate(R.layout.trackelementlist, null);
				 
		    	 holder = new ViewHolder();
		    	 holder.tvTrackDistance = (TextView) convertView.findViewById(R.id.tvTrackDistance);
		    	 holder.tvTrackTime= (TextView) convertView.findViewById(R.id.tvTrackTime);
		    	 holder.tvTrackName= (TextView) convertView.findViewById(R.id.tvTrackName);
		    	 holder.tvTrackDesc= (TextView) convertView.findViewById(R.id.tvTrackDesc);
		
		    	 convertView.setTag(holder);
		    	 
		    	 
			 } 
			 else {
				 
				 holder = (ViewHolder) convertView.getTag();
				 
			 }
		   	 
			 
			 	Double distance=trackList.get(position).getTripStatistics().getTotalDistance();
			 	holder.tvTrackDistance.setText((int)distance.doubleValue()+" m");
			 	
			 	holder.tvTrackTime.setText(trackList.get(position).getTripStatistics().getTotalTime()+" ms");

			 
			 	holder.tvTrackDesc.setText(trackList.get(position).getDescription());
			 	holder.tvTrackName.setText(trackList.get(position).getName());

			 	convertView.setId((int) trackList.get(position).getId());
		
		    	 return convertView;
			 
			 }
		
			 static class ViewHolder {
		    	 TextView tvTrackDistance;
		    	 TextView tvTrackTime;
		    	 TextView tvTrackDesc;
		    	 TextView tvTrackName;
		
			 }
		
		
		
			 
	}  
	
	
}

	
