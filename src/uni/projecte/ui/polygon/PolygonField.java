package uni.projecte.ui.polygon;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import uni.projecte.R;
import uni.projecte.Activities.Citations.Sampling;
import uni.projecte.Activities.Maps.CitationMap;
import uni.projecte.controler.PolygonControler;
import uni.projecte.dataTypes.ProjectField;
import uni.projecte.dataTypes.Utilities;
import uni.projecte.maps.utils.LatLon;
import uni.projecte.maps.utils.LatLonParcel;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import android.app.Activity;


import com.google.android.maps.MapView;

public class PolygonField {
	
	public static final int CREATE_MODE = 1;
	public static final int EDIT_MODE = 2;
	public static final int MAP_MODE = 3;

	private int POLYGON_FIELD_MODE;
	
	protected Context baseContext;

	private PolygonControler polygonCnt;
	
	/* ProjectField object */
	protected ProjectField field;
	
	protected long projId;
	private long citationId;
	private String secondLevelId;

	private ArrayList<LatLon> path;
	private boolean waitingGPS=false;
	private boolean modified=false;
	
	private MapView mapView;
	
	/* Main container */
	protected LinearLayout llField;
	
	private TextView tvCounter;
	private ImageButton ibAddPoint;
	
	private ImageButton btRemovePolygon;
	private ImageButton btRemovePoint;
	private ImageButton btShowPolygon;
	private ImageButton btClosePolygon;
	private ImageButton ibQuitEdit;
	
	private LinearLayout llWaitingGPS;
	
	private TextView tvAddPoint;

	
	
	public PolygonField(Context baseContext,long projId, ProjectField field, LinearLayout llField, int mode){
		
		this.baseContext=baseContext;
		this.field=field;
		this.llField=llField;		
		this.projId=projId;
		
		POLYGON_FIELD_MODE=mode;
		
		LayoutInflater inflater = (LayoutInflater) baseContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE); 
		View llPolygon=(View) inflater.inflate(R.layout.polygon_field,null);		
		
		loadBasicUi((LinearLayout)llPolygon);
		
		llField.addView(llPolygon);
		
		((LinearLayout) btRemovePolygon.getParent()).setVisibility(View.GONE);
		
		if(POLYGON_FIELD_MODE==CREATE_MODE){
			
			path= new ArrayList<LatLon>();			
			secondLevelId=createSecondLevelIdentifier(field.getName());

		}
		else{

			((LinearLayout) ibAddPoint.getParent()).setVisibility(View.GONE);

		}
		
	}
	
	public PolygonField(Context baseContext,long projId, View llPolygon, ArrayList<LatLonParcel> pointsExtra, MapView mapView){

		this.baseContext=baseContext;
		this.projId=projId;
		this.mapView=mapView;
		
		POLYGON_FIELD_MODE=MAP_MODE;

		loadBasicUi(llPolygon);
		
		btRemovePoint=(ImageButton) llPolygon.findViewById(R.id.ibPolygonRemovePoint);
		btRemovePoint.setOnClickListener(removePointListener);
		
		btClosePolygon=(ImageButton) llPolygon.findViewById(R.id.ibClosePolygon);
		btClosePolygon.setOnClickListener(closePolygonListener);
		
		ibQuitEdit=(ImageButton)llPolygon.findViewById(R.id.ibCreatePolygon);
		ibQuitEdit.setOnClickListener(savePolygonListener);

		path=new ArrayList<LatLon>();
		
		for(LatLonParcel point: pointsExtra) {
    		
    		path.add(point.getGeoPoint());
    	   
    	}
		
		if(path.size()<1){
			
			((LinearLayout) btRemovePoint.getParent()).setVisibility(View.GONE);
			((LinearLayout) btRemovePolygon.getParent()).setVisibility(View.GONE);
			((LinearLayout) btClosePolygon.getParent()).setVisibility(View.GONE);
			
		}
		
	}
	
	public void updatePath(ArrayList<LatLonParcel> pointsExtra){
		
		path=new ArrayList<LatLon>();
		
		for(LatLonParcel point: pointsExtra) {
    		
    		path.add(point.getGeoPoint());
    	   
    	}

		updateUI();
		
	}
	
	
	private void loadBasicUi(View llPolygon) {

		tvCounter=(TextView) llPolygon.findViewById(R.id.tvPolygonCount);
		ibAddPoint=(ImageButton) llPolygon.findViewById(R.id.ibAddPoint);
		
		btShowPolygon=(ImageButton) llPolygon.findViewById(R.id.ibShowPolygon);
		if(btShowPolygon!=null)btShowPolygon.setOnClickListener(showPolygonListener);
		
		btRemovePolygon=(ImageButton) llPolygon.findViewById(R.id.ibPolygonRemove);
		btRemovePolygon.setOnClickListener(removePolygonListener);
		
		llWaitingGPS=(LinearLayout) llPolygon.findViewById(R.id.llgpsWaiting);
		
		tvAddPoint=(TextView) llPolygon.findViewById(R.id.tvAddPoint);		
		
	}

	private void loadPolygonValues() {

		polygonCnt= new PolygonControler(baseContext);
		
		path=polygonCnt.getPolygonPath(secondLevelId);			
	
		if(isClosed()) tvCounter.setText(path.size()-1+"");
		else tvCounter.setText(path.size()+"");

		
	}

	public void setAddPointListener(OnClickListener addPointListener){
		
		ibAddPoint.setOnClickListener(addPointListener);

	}
	
	
	public void addPoint(double latitude, double longitude, double altitude){
		
		path.add(new LatLon(latitude, longitude,altitude));
		
		waitingGPS=false;
		
		((LinearLayout) ibAddPoint.getParent()).setVisibility(View.VISIBLE);
		llWaitingGPS.setVisibility(View.GONE);
		
		tvAddPoint.setText("Afegir punt");
		
		updateUI();
		
		modified=true;
		
	}
	
	public ArrayList<LatLon> getPolygonPath() {
		
		return path;
		
	}
	
	private void updateUI() {

		switch (POLYGON_FIELD_MODE) {
		
			case CREATE_MODE:
			
				if(path.size()>0){
					
					((LinearLayout) btRemovePolygon.getParent()).setVisibility(View.VISIBLE);
					
				}
				else{
					
					((LinearLayout) btRemovePolygon.getParent()).setVisibility(View.GONE);
					
				}
				
			break;
			
			case MAP_MODE:
			
				if(path.size()>0){
					
					((LinearLayout) btRemovePolygon.getParent()).setVisibility(View.VISIBLE);
					((LinearLayout) btRemovePoint.getParent()).setVisibility(View.VISIBLE);
					((LinearLayout) btClosePolygon.getParent()).setVisibility(View.VISIBLE);
				}
				else{
					
					((LinearLayout) btRemovePolygon.getParent()).setVisibility(View.GONE);
					((LinearLayout) btRemovePoint.getParent()).setVisibility(View.GONE);
					((LinearLayout) btClosePolygon.getParent()).setVisibility(View.GONE);
				}
				
				mapView.invalidate();
				
				
			break;

		default:
			break;
			
			
		}
	
		if(isClosed()) tvCounter.setText(path.size()-1+"");
		else tvCounter.setText(path.size()+"");

	}
	
	public void clearForm(){
		
		path=new ArrayList<LatLon>();

		secondLevelId=createSecondLevelIdentifier(field.getName());
		
		updateUI();
		
		modified=false;
		
	}
	
	public void loadPoints(String secondLevelId) {
		
		this.secondLevelId = secondLevelId;
		loadPolygonValues();

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

	    	
	    	((Activity)baseContext).startActivityForResult(myIntent,Sampling.POLYGON_EDIT);
	    	
			
		}
	};
	

	private OnClickListener savePolygonListener=new OnClickListener() {
		
		public void onClick(View v) {

			ArrayList<LatLonParcel> pointsExtra = new ArrayList<LatLonParcel>();
			
			for(LatLon point: path){
			   
				pointsExtra.add(new LatLonParcel(point));
				
			}
			
			Intent myIntent = new Intent();
	    	myIntent.putExtra("polygon_path", pointsExtra);
	    	myIntent.putExtra("polygon_modified", modified);
	    	
	    	((Activity)baseContext).setResult(Sampling.SUCCESS_RETURN_CODE, myIntent);

	    	((Activity)baseContext).finish();
              

		}
	};
	
	
	private OnClickListener removePolygonListener=new OnClickListener() {
		
		public void onClick(View v) {

			path.clear();
			updateUI();
			
			modified=true;

		}
	};
	
	private OnClickListener closePolygonListener=new OnClickListener() {
		
		public void onClick(View v) {

			if(path.size()>2){
				
				LatLon firstLatLon=path.get(0);
				LatLon lastLatLon=path.get(path.size()-1);
				
				if(firstLatLon.equals(lastLatLon)){
					
					Utilities.showToast("El polígon ja està tancat", baseContext);
					
				}
				else {
					
					path.add(firstLatLon);
					modified=true;
					
				}
				
			}
			else{
				
				Utilities.showToast("Es necessiten més de 2 punts per a tancar el polígon", baseContext);
				
			}

			updateUI();

		}
	};
	
	
	private OnClickListener removePointListener=new OnClickListener() {
		
		public void onClick(View v) {

			path.remove(path.size()-1);
			
			modified=true;

			updateUI();	


		}
	};

	public boolean isClosed(){
		
		if(path.size()<=1) return false;
		else return getFirstPoint().equals(getLastPoint());
		
	}
	
	private LatLon getLastPoint(){
		
		return path.get(path.size()-1);
		
	}
	
	private LatLon getFirstPoint(){
		
		return path.get(0);
		
	}
	
	
	public boolean isWaitingGPS() {
		
		return waitingGPS;
		
	}

	public void setWaitingGPS(boolean waitingGPS) {
		
		this.waitingGPS = waitingGPS;
		
		if(waitingGPS) {
			
			((LinearLayout) ibAddPoint.getParent()).setVisibility(View.GONE);
			llWaitingGPS.setVisibility(View.VISIBLE);
						
		}
		
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



	public void setCitationId(long citationValueId) {

		this.citationId=citationValueId;
		
	}
	
	public long getCitationid(){
		
		return citationId;
		
	}

	public String getFieldLabel() {

		return field.getLabel();

	}

	public boolean canAddPoint() {

		if(path.size()>2 && isClosed()) return false;
		else return true;
	
	}

	public boolean isModified() {
		return modified;
	}

	public void setModified(boolean modified) {
		this.modified = modified;
	}
	
	
}