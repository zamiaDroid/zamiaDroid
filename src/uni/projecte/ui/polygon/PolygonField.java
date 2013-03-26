package uni.projecte.ui.polygon;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.zip.Inflater;

import uni.projecte.R;
import uni.projecte.Activities.Maps.CitationMap;
import uni.projecte.dataTypes.ProjectField;
import uni.projecte.maps.utils.LatLon;
import uni.projecte.maps.utils.LatLonParcel;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

public class PolygonField {
	
	public static int CREATE_MODE = 1;
	public static int EDIT_MODE = 2;
	
	private int POLYGON_FIELD_MODE;
	
	protected Context baseContext;

	/* Main container */
	protected LinearLayout llField;

	/* ProjectField object */
	protected ProjectField field;
	
	private TextView tvCounter;
	private ImageButton ibAddPoint;
	
	protected long projId;
	
	private int points;
	
	private boolean waitingGPS=false;
	private ArrayList<LatLon> path;
	
	private Button btRemovePolygon;
	private Button btRemovePoint;
	private Button btShowPolygon;
	
	public PolygonField(Context baseContext,long projId, ProjectField field, LinearLayout llField){
		
		this.baseContext=baseContext;
		this.field=field;
		this.llField=llField;		
		this.projId=projId;
		
		LayoutInflater inflater = (LayoutInflater) baseContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE); 
		View llPolygon=(View) inflater.inflate(R.layout.polygon_field,null);

		tvCounter=(TextView) llPolygon.findViewById(R.id.tvPolygonCount);
		ibAddPoint=(ImageButton) llPolygon.findViewById(R.id.ibAddPoint);
		
		btShowPolygon=(Button) llPolygon.findViewById(R.id.btShowPolygon);
		btShowPolygon.setOnClickListener(showPolygonListener);
		
		btRemovePolygon=(Button) llPolygon.findViewById(R.id.btPolygonRemove);
		btRemovePolygon.setOnClickListener(showPolygonListener);
		
		btRemovePoint=(Button) llPolygon.findViewById(R.id.btPolygonRemovePoint);
		btRemovePoint.setOnClickListener(removePointListener);
		
		llField.addView(llPolygon);
		
		path= new ArrayList<LatLon>();
		
	}
	
	public void setAddPointListener(OnClickListener addPointListener){
		
		ibAddPoint.setOnClickListener(addPointListener);

	}
	
	
	public void addPoint(double latitude, double longitude){
		
		path.add(new LatLon(latitude, longitude));
		
		
		waitingGPS=false;
		
		updateUI();
		
	}
	
	private void updateUI() {

		if(path.size()>0){
			
			btRemovePoint.setVisibility(View.VISIBLE);
			btRemovePolygon.setVisibility(View.VISIBLE);
			btShowPolygon.setVisibility(View.VISIBLE);
			
		}
		else{
			
			btRemovePoint.setVisibility(View.GONE);
			btRemovePolygon.setVisibility(View.GONE);
			btShowPolygon.setVisibility(View.GONE);
		}
		
		tvCounter.setText(path.size()+"");

	}

	private OnClickListener showPolygonListener=new OnClickListener() {
		
		public void onClick(View v) {

			ArrayList<LatLonParcel> pointsExtra = new ArrayList<LatLonParcel>();
						
			for(LatLon point: path){
			   
				pointsExtra.add(new LatLonParcel(point));
				
			}
			
			Intent myIntent = new Intent(baseContext, CitationMap.class);
	    	myIntent.putExtra("id", projId);
	    	myIntent.putExtra(CitationMap.MAP_MODE,CitationMap.VIEW_POLYGON);
	    	myIntent.putExtra("polygon_path", pointsExtra);
			
	    	baseContext.startActivity(myIntent);
			
		}
	};
	

	
	private OnClickListener removePolygonListener=new OnClickListener() {
		
		public void onClick(View v) {

			path= new ArrayList<LatLon>();
			updateUI();

		}
	};
	
	private OnClickListener removePointListener=new OnClickListener() {
		
		public void onClick(View v) {

			path.remove(path.size()-1);
			updateUI();		
			
		}
	};

	public boolean isWaitingGPS() {
		return waitingGPS;
	}

	public void setWaitingGPS(boolean waitingGPS) {
		this.waitingGPS = waitingGPS;
	}
	
	
	
}