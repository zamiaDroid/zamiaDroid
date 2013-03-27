package uni.projecte.ui.polygon;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.zip.Inflater;

import uni.projecte.R;
import uni.projecte.Activities.Maps.CitationMap;
import uni.projecte.controler.CitationSecondLevelControler;
import uni.projecte.controler.PolygonControler;
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
	
	private boolean waitingGPS=false;
	private ArrayList<LatLon> path;
	
	private Button btRemovePolygon;
	private Button btRemovePoint;
	private Button btShowPolygon;
	private Button btClosePolygon;
	
	private String secondLevelId;

	private PolygonControler polygonCnt;
	
	public PolygonField(Context baseContext,long projId, ProjectField field, LinearLayout llField, int mode){
		
		this.baseContext=baseContext;
		this.field=field;
		this.llField=llField;		
		this.projId=projId;
		
		POLYGON_FIELD_MODE=mode;
		
		LayoutInflater inflater = (LayoutInflater) baseContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE); 
		View llPolygon=(View) inflater.inflate(R.layout.polygon_field,null);

		tvCounter=(TextView) llPolygon.findViewById(R.id.tvPolygonCount);
		ibAddPoint=(ImageButton) llPolygon.findViewById(R.id.ibAddPoint);
		
		btShowPolygon=(Button) llPolygon.findViewById(R.id.btShowPolygon);
		btShowPolygon.setOnClickListener(showPolygonListener);
		
		btRemovePolygon=(Button) llPolygon.findViewById(R.id.btPolygonRemove);
		btRemovePolygon.setOnClickListener(removePolygonListener);
		
		btRemovePoint=(Button) llPolygon.findViewById(R.id.btPolygonRemovePoint);
		btRemovePoint.setOnClickListener(removePointListener);
		
		btClosePolygon=(Button) llPolygon.findViewById(R.id.btClosePolygon);
		btClosePolygon.setOnClickListener(closePolygonListener);
		
		llField.addView(llPolygon);
		
		btRemovePoint.setVisibility(View.GONE);
		btRemovePolygon.setVisibility(View.GONE);
		btClosePolygon.setVisibility(View.GONE);
		
		
		if(POLYGON_FIELD_MODE==CREATE_MODE){
			
			path= new ArrayList<LatLon>();			
			secondLevelId=createSecondLevelIdentifier(field.getName());
			btShowPolygon.setVisibility(View.GONE);

		}
		else{

			ibAddPoint.setVisibility(View.GONE);
		}
		
	}
	
	private void loadPolygonValues() {

		polygonCnt= new PolygonControler(baseContext);
		
		path=polygonCnt.getPolygonPath(secondLevelId);			
	
		
	}

	public void setAddPointListener(OnClickListener addPointListener){
		
		ibAddPoint.setOnClickListener(addPointListener);

	}
	
	
	public void addPoint(double latitude, double longitude, double altitude){
		
		path.add(new LatLon(latitude, longitude,altitude));
		
		waitingGPS=false;
		
		updateUI();
		
	}
	
	public ArrayList<LatLon> getPolygonPath() {
		
		return path;
		
	}
	
	private void updateUI() {

		if(path.size()>0 && POLYGON_FIELD_MODE==CREATE_MODE){
			
			btRemovePoint.setVisibility(View.VISIBLE);
			btRemovePolygon.setVisibility(View.VISIBLE);
			btShowPolygon.setVisibility(View.VISIBLE);
			btClosePolygon.setVisibility(View.VISIBLE);
			
		}
		else{
			
			btRemovePoint.setVisibility(View.GONE);
			btRemovePolygon.setVisibility(View.GONE);
			btShowPolygon.setVisibility(View.GONE);
			btClosePolygon.setVisibility(View.GONE);

		}
		
		tvCounter.setText(path.size()+"");

	}

	private OnClickListener showPolygonListener=new OnClickListener() {
		
		public void onClick(View v) {

			if(POLYGON_FIELD_MODE==EDIT_MODE){
				
				loadPolygonValues();
				
			}
			
			
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
	
	private OnClickListener closePolygonListener=new OnClickListener() {
		
		public void onClick(View v) {

			LatLon tmpLatLon=path.get(0);
			path.add(tmpLatLon);
			
			
			updateUI();

		}
	};
	
	
	private OnClickListener removePointListener=new OnClickListener() {
		
		public void onClick(View v) {

			path.remove(path.size()-1);
			updateUI();		
			
		}
	};
	private long citationId;

	public boolean isWaitingGPS() {
		return waitingGPS;
	}

	public void setWaitingGPS(boolean waitingGPS) {
		this.waitingGPS = waitingGPS;
	}

	public long getFieldId(){
		
		return field.getId();
		
	}
	
	private String createSecondLevelIdentifier(String fieldName){
		
    	SimpleDateFormat dfDate  = new SimpleDateFormat("yyyy-MM-dd_kk:mm:ss");
    	Calendar c = Calendar.getInstance(); 
    	String date=dfDate.format(c.getTime());
    	
    	return fieldName.toLowerCase()+"_"+date;
		
	}

	public String getSecondLevelId() {
		return secondLevelId;
	}

	public void setSecondLevelId(String secondLevelId) {
		this.secondLevelId = secondLevelId;
	}

	public void setCitationId(long citationValueId) {

		this.citationId=citationValueId;
		
	}
	
	public long getCitationid(){
		
		return citationId;
		
	}

	public String getFieldLabel() {

		return field.getLabel();

	}
	
	
}