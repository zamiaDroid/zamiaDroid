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

package uni.projecte.Activities.Maps;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import uni.projecte.R;
import uni.projecte.Activities.Citations.CitationEditor;
import uni.projecte.Activities.Citations.Sampling;
import uni.projecte.Activities.RemoteDBs.RemoteDBConfig;
import uni.projecte.Activities.RemoteDBs.TaxonListExplorer;
import uni.projecte.R.drawable;
import uni.projecte.controler.PreferencesControler;
import uni.projecte.controler.ProjectControler;
import uni.projecte.controler.CitationControler;
import uni.projecte.controler.ThesaurusControler;
import uni.projecte.dataLayer.RemoteDBManager.DBManager;
import uni.projecte.dataTypes.LocationCoord;
import uni.projecte.dataTypes.Utilities;
import uni.projecte.maps.CitationMapState;
import uni.projecte.maps.MapDrawUtils;
import uni.projecte.maps.MapConfigurationDialog;
import uni.projecte.maps.MapLocation;
import uni.projecte.maps.MyTracksService;
import uni.projecte.maps.UTMDisplay;
import uni.projecte.maps.overlays.MyTracksOverlay;
import uni.projecte.maps.overlays.PolygonOverlay;
import uni.projecte.maps.overlays.UTMOverlay;
import uni.projecte.maps.overlays.UserLocationOverlay;
import uni.projecte.maps.utils.LatLonParcel;
import uni.projecte.ui.TransparentPanel;
import uni.projecte.ui.polygon.PolygonField;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Point;
import android.graphics.RectF;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Vibrator;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.MyLocationOverlay;
import com.google.android.maps.Overlay;

import edu.ub.bio.biogeolib.CoordConverter;
import edu.ub.bio.biogeolib.CoordinateLatLon;
import edu.ub.bio.biogeolib.CoordinateUTM;



public class CitationMap extends MapActivity implements LocationListener {
    /** Called when the activity is first created. */
	
	/* Map modes */
	public static final int VIEW_CITATIONS=1;
	public static final int VIEW_POLYGON=2;
	
	public static final String MAP_MODE="MAP_MODE";
	
	 /* Menu Options  */
	 private static final int MY_LOCATION=Menu.FIRST;
	 private static final int MAP_TYPE=Menu.FIRST+1;
	 private static final int CHOOSE_TRACK=Menu.FIRST+2;
	 private static final int MAP_OPTIONS=Menu.FIRST+3;
	 private static final int CLEAR_MAP=Menu.FIRST+4;
	 private static final int DB_CONFIG=Menu.FIRST+5;


	/* On Result actions */ 
	 public final static int LOAD_TRACKS = 1;
	 public final static int MAP_MARKERS_UPDATED = 2;
	 public final static int GPS_CONFIG = 3;
	 public final static int GPS_CONFIG_MYTRACKS = 4;
	 
		
	 /* Center location when no samples */
	 private static double lat = 41.692;
	 private  static double lng = 1.620;

	 
	 /* MapView main objects */
	 private MapController mc;
	 private MapView mapView;
	 
	 
	 /* Pairs of fieldName and fieldLabel */
	 private HashMap<String, String> fieldsLabelNames;
	 
	 /* MyTracksService & LocationManager (GPS) */
	 private MyTracksService tracksService;
	 private LocationManager lm;
	 
	 /* Overlays */
	 private List<Overlay> listOfOverlays;
	 private UserLocationOverlay myLocationOverlay;
	 private MyTracksOverlay myTracks;
	 private MyLocationOverlay myLocation;
	 private CitationsOverlay mapOverlay;
	 private UTMOverlay utmOverlay;

	 
	 /* ImageButtons  */
	 private ImageButton connectDBButton;
	 private ImageButton gridModeButton;
	 private ImageButton createCitationButton;
	 private ImageButton myTracksButton;
	 private ImageButton gpsButton;
	 private ImageButton editModeButton;
	 private ImageButton editCitation;
	 private ImageButton moveCitation;
	 private ImageButton photoCitation;

	 /* Info Layer: TextViews  */
	 private TextView tvCenteredLocation;
	 private TextView tvInfoTrackName;
	 private TextView tvAltitude;
	 private TextView tvCitationInfo;
	 
	 /* Info Layer: Layouts */
	 private TransparentPanel controlPanel;
	 private TransparentPanel transparentPanel;

	 private TableRow trLocation;
	 private TableRow tableRowTrack;
	 private TableRow tableRowAltitude;
	 private ImageView ivLocation;
	 private LinearLayout llCitationInfo;
	 private LinearLayout llCitationWithPhoto;
	 private TransparentPanel llPolygon;

	 // Project Id
	 private long projId;
	 
	 /* MapMode*/
	 private int map_mode=VIEW_CITATIONS;
	 
	 /* String with a list of selected citationsIds that will be loaded */
	 private String preSettedLoc;
	 
	 /* Only one citation shown in the map */
	 private long idCitation;
	 private ArrayList<LocationCoord> coordinates;
	 

	 /* Current location [userLocation || trackLastPoint] */
	 private GeoPoint lastKnownLocation;	 
	 private double elevation;
	 private PolygonField polygonField;
	 
	 
	 /* State Class */
	 private CitationMapState mapState = new CitationMapState(false, false, false,
			false, false, false, false, false, false, false, true,false,false);
	 
	 
	private String utmPrec="10km";

	 
	 // Data Providers
	 private PreferencesControler pC;
	 private CitationControler sC;
	 private ProjectControler projCnt;

	 // Data Storage
	 private ArrayList<MapLocation> mapLocations;

	 
	 /* Current track Info */
	 private long trackId=-1;
	
	
    @Override
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Utilities.setLocale(this);

        setContentView(R.layout.samplesmap);
        
        gpsButton = (ImageButton)findViewById(R.id.myLocationButton);
        gpsButton.setOnClickListener(activateGPSListener);
        gpsButton.setBackgroundResource(drawable.position_icon_off);
        
        trLocation=(TableRow)findViewById(R.id.tableRowLocation);
        tableRowTrack=(TableRow)findViewById(R.id.tableRowTrack);
        tableRowAltitude=(TableRow)findViewById(R.id.tableRowAltitude);
        ivLocation=(ImageView)findViewById(R.id.ivLocation);

        controlPanel=(TransparentPanel)findViewById(R.id.mapControlPanel);
        transparentPanel=(TransparentPanel)findViewById(R.id.transparent_panel);
        llCitationInfo=(LinearLayout)findViewById(R.id.llCitationInfo);
        llCitationWithPhoto=(LinearLayout)findViewById(R.id.llCitationWithPhoto);
    	llPolygon=(TransparentPanel)findViewById(R.id.llPolygonMapMenu);
    	
        
        ImageButton showLabelsButton = (ImageButton)findViewById(R.id.myShowLabels);
        showLabelsButton.setOnClickListener(showLabelsListener);
        
        connectDBButton = (ImageButton)findViewById(R.id.myShowDBInfo);
        connectDBButton.setOnClickListener(showDBListener);
        connectDBButton.setVisibility(View.GONE);

        createCitationButton = (ImageButton)findViewById(R.id.myCreateCitationCenter);
        createCitationButton.setOnClickListener(createCenteredCitationListener);
        createCitationButton.setVisibility(View.GONE);
        
        myTracksButton = (ImageButton)findViewById(R.id.myTracksButton);
        myTracksButton.setOnClickListener(myTracksListener);
        
        editModeButton = (ImageButton)findViewById(R.id.myEditMode);
        editModeButton.setOnClickListener(editModeListener);
        
        gridModeButton = (ImageButton)findViewById(R.id.myShowGrid);
        gridModeButton.setOnClickListener(showGridListener);
        
        moveCitation=(ImageButton)findViewById(R.id.ibMoveCit);
        photoCitation=(ImageButton)findViewById(R.id.ibCitMapPhoto);
        
        tvCenteredLocation = (TextView)findViewById(R.id.locationTV);  
        tvInfoTrackName= (TextView)findViewById(R.id.tvInfoTrackName);
        tvAltitude= (TextView)findViewById(R.id.tvAltitudeInfo);
        tvCitationInfo=(TextView)findViewById(R.id.tvCitMapInfoExtended);
		   
        editCitation=(ImageButton)findViewById(R.id.ibCitMapEditCit);
        
        
        mapView =(MapView)findViewById(R.id.mapview);
        mapView.setBuiltInZoomControls(true);
        mapView.setSatellite(true);      
        
        
        mc = mapView.getController();
        
        projId=getIntent().getExtras().getLong("id");
        map_mode=getIntent().getExtras().getInt(MAP_MODE);

        sC=new CitationControler(this);
        pC=new PreferencesControler(this);
        projCnt=new ProjectControler(this);
        
        listOfOverlays = mapView.getOverlays();

        
        if(map_mode==VIEW_POLYGON){
        	
        	mapState.polygonMode=true;
        	
        	ArrayList<LatLonParcel> pointsExtra =  getIntent().getParcelableArrayListExtra("polygon_path");

        	polygonField=new PolygonField(this, projId, llPolygon,pointsExtra,mapView);
        	polygonField.setAddPointListener(capturePolygonPoint);
        	
        	PolygonOverlay polygonOverlay=new PolygonOverlay(mapView,polygonField.getPolygonPath());
        	listOfOverlays.add(polygonOverlay);
        	
        }
        else{
        	
            projCnt.loadProjectInfoById(projId);
            fieldsLabelNames=projCnt.getProjectFieldsPair(projId);
        	
            /* Getting instance of MyTracksService */
            tracksService=new MyTracksService(this);
            tracksService.setProjName(projCnt.getCleanProjectName());
                
        	// User location overlay (green marker)
            myLocationOverlay = new UserLocationOverlay(this, mapView,lastKnownLocation);
            
            myLocation = new MyLocationOverlay(getApplicationContext(), mapView);
            mapView.getOverlays().add(myLocation);
        	
            	
	        if(pC.getTrackingService()) myTracksIsWorkingInBackground();       
	        if(mapState.compassEnabled) myLocation.enableCompass();
	        
	        preSettedLoc=getIntent().getExtras().getString("idSelection");
	        
	        loadCitations();
	          
	        loadUTMGrid();
	        
	        utmPrec=pC.getUTMDisplayPrec();
	        mapState.elevationEnabled=pC.isMapElevationShown();
        
        }

        handleInfoDialog();

    }
    
	@Override
	protected void onStart() {
		    super.onStart();

	     	handleInfoDialog();
	
	    
	} 
	
	
	
    /*
     * Called when CitationMap Activity has been destroyed and myTracks instance seems to be working in Background 
  	 *
     */
    
	  private void myTracksIsWorkingInBackground() {
        	
		  
	        trackId=tracksService.getWorkingTrackId();
	        	
	        if(trackId>0){
	        	
	        	Log.i("MyTracks", "has been brought to top because the Citation Activity has been destroyed");
	        	
		        myTracksButton.setBackgroundResource(drawable.appwidget_button_enabled);
		
		        mapState.myTracksOn=true;
		        mapState.gpsMode=true;
		
		        gpsButton.setVisibility(View.GONE);
		        editModeButton.setVisibility(View.GONE);
		        	
		        myTracks=new MyTracksOverlay(getBaseContext(),mapView,trackId,false,myTracksResultHandler);
		        	
		        mapState.myTrackLoaded=true;
					
				myTracks.setInfoUTM(tvCenteredLocation);
				myTracks.setInfoPrec(utmPrec);
					
				listOfOverlays.add(myTracks);
				
	        } 
	        else{
	        		
	        	pC.setTrackingService(false);
	        		
	        }
		  
	}

    
	@Override
	protected void onResume() {
     super.onResume();
     
	    /* Enabling trackService */
 	
     
     	Log.i("MyTracks", "--------------- onResume");
     
     	 if(map_mode!=VIEW_POLYGON){
     	
		  	mapState.myTracksWorking=tracksService.initMyTracksService(handlerMyTrackUpdates);
	
	
			if(!pC.isShownMyTracksDialog()) tracksService.showInfoDialog(mapState.myTracksWorking,pC);
			if(!mapState.myTracksWorking) myTracksButton.setVisibility(View.GONE);
		
			
	     	myLocationOverlay.enableCompass();
     	
     	 }
     
    }
    
    @Override
	protected void onPause() {
     
    	super.onPause();

    	
    	 if(map_mode!=VIEW_POLYGON){

	     	Log.i("MyTracks", "--------------- onPause");
	
	    	/* When tracking is not running we can remove the myTracks Service */
	    	
	    	if(!pC.getTrackingService()){
	    		
	    		if(tracksService!=null) tracksService.endMyTracksService();
	    		
	    	}
	    	
	    	myLocationOverlay.disableCompass();
    	
    	 }
     
    }
    
    @Override
	protected void onStop() {
		
    	super.onStop();
    	
    	
    	
	}
    
    
    private void handleInfoDialog(){
    	    	
    	
    	Log.i("CitationMap","gpsOn:trackId:myTracksOn:myTracksLoaded:editModeOn:gridModeOn");
    	Log.i("CitationMap",mapState.gpsOn+":"+trackId+":"+mapState.myTracksOn+":"+mapState.myTrackLoaded+":"+mapState.editModeOn+":"+mapState.gridModeOn);
	
	   	
	   	if(mapState.polygonMode){
	   		
	   		controlPanel.setVisibility(View.GONE);
	   		transparentPanel.setVisibility(View.GONE);
	   		
	   	}
	   	
	   	else{
		   	
	   		llCitationInfo.setVisibility(View.GONE);

	   		llPolygon.setVisibility(View.GONE);

	    	//Location bar Info
	    	if(mapState.gpsOn || mapState.myTrackLoaded || trackId>0){
	    		
	    		
	    		if(mapState.gpsOn || mapState.myTracksOn) mapState.gpsMode=true;
	    		else mapState.gpsMode=false;
	    		
	    		ivLocation.setImageResource(R.drawable.ic_maps_indicator_verd_pet);
				
				if(mapState.gpsOn) {
	    			
	    			if(lastKnownLocation==null) tvCenteredLocation.setText(getString(R.string.waitingGPS));
	    			myTracksButton.setVisibility(View.GONE);
	    			
	  
	    			
	    		}
	    		else{
	    			
	    			myTracksButton.setVisibility(View.VISIBLE);
	    			
	    		}
				
	    		if(mapState.gpsMode){ 
	    			
	    			trLocation.setVisibility(View.VISIBLE);
	    			createCitationButton.setVisibility(View.VISIBLE);
	    		   	connectDBButton.setVisibility(View.VISIBLE);
	    		   	editModeButton.setVisibility(View.GONE);
	    		   	
	          		if(mapState.elevationEnabled) tableRowAltitude.setVisibility(View.VISIBLE);
	        		else tableRowAltitude.setVisibility(View.GONE);
	    		   	
	    		}
	    		else{
	    			
	    			trLocation.setVisibility(View.GONE);
	        		tableRowAltitude.setVisibility(View.GONE);
	
	    		}
	
	    		
	    	}
	    	//Position mode
	    	else if(mapState.editModeOn || mapState.gridModeOn){
	    		
	    		mapState.gpsMode=false;
	    		
	    		trLocation.setVisibility(View.VISIBLE);
	    		ivLocation.setImageResource(R.drawable.mini_location);
	    		connectDBButton.setVisibility(View.VISIBLE);
			   	editModeButton.setVisibility(View.VISIBLE);
	    		
	
			   	
	    		if(mapState.editModeOn) createCitationButton.setVisibility(View.VISIBLE);
	    		else  createCitationButton.setVisibility(View.GONE);
	    		
	    	}
	    	//Nothing chosen mode
	    	else{
	    		
	    		
	    		trLocation.setVisibility(View.GONE);
	    		tableRowAltitude.setVisibility(View.GONE);
	    		
	    		connectDBButton.setVisibility(View.GONE);
			   	editModeButton.setVisibility(View.VISIBLE);
	    		createCitationButton.setVisibility(View.GONE);
	    		
				if(mapState.myTracksWorking) myTracksButton.setVisibility(View.VISIBLE);
	
	    		
	    	}
	    	
	    	
	    	//Track bar Info
	    	if(mapState.myTrackLoaded && !tracksService.getLoadedTrackName(trackId).equals("")){
	    		
	    		tableRowTrack.setVisibility(View.VISIBLE);
	    		
	    		if(mapState.myTracksOn) {
	    			
	    			tvInfoTrackName.setText(getString(R.string.trackRegistering));
	    			gpsButton.setVisibility(View.GONE);
	    			
	    		}
	    		else{
	    			
	        		tvInfoTrackName.setText(tracksService.getLoadedTrackName(trackId));
	    			gpsButton.setVisibility(View.VISIBLE);
	
	    		}    		
	    		
	    	}
	    	else{
	    		
				gpsButton.setVisibility(View.VISIBLE);
	    		tableRowTrack.setVisibility(View.GONE);
	    		
	    	}
          	 
	   	}
  	
    }
    
    /*
     * Thread that loads onBackground the chosen track with @trackId
     * 
     */

	private void loadTrack(final long trackId) {

		
	       new Thread(new Runnable() {

				public void run() {
						
					if(myTracks!=null) listOfOverlays.remove(myTracks);

					myTracks=new MyTracksOverlay(getBaseContext(),mapView,trackId, true,myTracksResultHandler);
					listOfOverlays.add(myTracks);
					mapState.myTrackLoaded=true;
					
					/* Centering map to last location */
					GeoPoint lastPoint=myTracks.getLastLocation();
					if(lastPoint!=null) mc.animateTo(lastPoint);
					
					handlerMyTrackUpdates.sendEmptyMessage(4);

	
				}
	              
            	
	    	}).start();
	       
	       
	       handleInfoDialog();
	       
	}
    
  
    /*
     * Thread that loads onBackground the list of UTM's to the UTMSet
     * 
     */
    

	private void loadUTMGrid() {

       utmOverlay= new UTMOverlay(mapView,tvCenteredLocation,utmPrec);
		
       handleInfoDialog();
    	
	}
	
	private void loadCitations(){
		
        mapLocations=new ArrayList<MapLocation>();
		
		
        new Thread(new Runnable() {

			public void run() {
				
			      if(preSettedLoc!=null){
			        	
			            coordinates= loadProjectCitations(preSettedLoc);

			        }
			 
			        else{
			        	
			            coordinates= loadProjectCitations();

			        }

			     centerLastLocation.sendEmptyMessage(0);
			     
			}
              
            	
    	}).start();
        
        
	
		
	}
	

    
    private Handler centerLastLocation = new Handler() {

        @Override
        public void handleMessage(Message msg) {
        	
		     mapOverlay = new CitationsOverlay(mapLocations,sC,projId,getBaseContext());
		     listOfOverlays.add(mapOverlay); 
 
            
        	if(coordinates.isEmpty()) {
            	
                Toast.makeText(getBaseContext(), 
        	                R.string.noLocationsCitations, 
        	                 Toast.LENGTH_SHORT).show();
                
                

        	 
        	    GeoPoint unique = new GeoPoint(
        	            (int) (lat * 1E6), 
        	            (int) (lng * 1E6));

        	      mc.animateTo(unique);
        	      mc.setZoom(4);

            	
            }
            else{
            	
        	    Log.d("CitationsMap",coordinates.size()+" citations Loaded");

                centerUniqueSample();
            	
            	
            }        	
        }
    };
    
    
    private OnClickListener capturePolygonPoint = new OnClickListener() {

        public void onClick(View v) {

        	if(polygonField.canAddPoint()){
        	
	        	polygonField.setWaitingGPS(true);
	
	        	myLocationGPSManager();
        	
        	}
        	else{
        		
        		Utilities.showToast("El polígon ja està tancat", v.getContext());
        		
        	}
        	
        	
        }
    };
    
    private Handler handlerMyTrackUpdates = new Handler() {

        @Override
        public void handleMessage(Message msg) {
 
        	/* myTracks service started */
        	if(msg.what==1){
        		
        		   if(mapState.askedGPSUpdate){ 
                   	
                   	mapState.askedGPSUpdate=false;
           			mapState.myTracksOn=true;
       	    		myTracksButton.setBackgroundResource(drawable.appwidget_button_enabled);
                   	startTraking();
                   	
                   }
        		
        	}
        	/* track empty not saved */
        	else if(msg.what==2){
        		
        		trackId=-1;
        		mapState.myTracksOn=false;
        		
        		if(myTracks!=null){ 
        			
        			listOfOverlays.remove(myTracks);
        			mapState.myTrackLoaded=false;
               	 	mapView.invalidate();

        		}
        		
        		handleInfoDialog();
        		
        		
        	}
        	/* track saved */
        	else if(msg.what==3){
        		
        		mapState.myTracksOn=false;
        		loadTrack(trackId);
        		
        	}
        	/* update track Info Window */
        	else if(msg.what==4){
        		
        		handleInfoDialog();    
        		
        	}

        }
    };
    
    
    
    private Handler handlerUpdateConf = new Handler() {

        @Override
        public void handleMessage(Message msg) {
 
	        utmPrec=pC.getUTMDisplayPrec();
	        
	        if(utmOverlay!=null) utmOverlay.setUtmPrec(utmPrec);
	        if(myTracks!=null) myTracks.setInfoPrec(utmPrec);

	        
	        if(msg.what==1){

	        		controlPanel.setVisibility(View.GONE);
		        	myLocation.enableCompass();
		        	
		        }
		        else if(msg.what==0){
	        	
	        		controlPanel.setVisibility(View.VISIBLE);
		        	myLocation.disableCompass();

		       }
		        else if(msg.what==2){
		        	
		        	tracksService.showInfoDialog(mapState.myTracksWorking,pC);
		        	
		        }
		        else if(msg.what==3){
		        	
		        	pC.setMapElevationShown(true);
		        	mapState.elevationEnabled=true;
		        	handleInfoDialog();
		        	
		       }
			   else if(msg.what==4){
			   	
		        	pC.setMapElevationShown(false);
		        	mapState.elevationEnabled=false;
		        	handleInfoDialog();

			   }
	        
        }
    };
    

    private Handler myTracksResultHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
 
        	lastKnownLocation=myTracks.getLastPoint();
        	elevation=myTracks.getElevation()-pC.getGeoidCorrection();
        	
		    tvAltitude.setText(" "+(int)elevation+" (m)");

        	if(lastKnownLocation!=null) {
        		
        		mc.animateTo(lastKnownLocation);
                mapView.invalidate();
                
        	}
        	
	        
        }
    };
    
        
    private OnClickListener activateGPSListener = new OnClickListener()
    {
        public void onClick(View v)
        {                        
       	
        	if(mapState.gpsOn){
        		
        		activateGPS(false);
        		        		
        	}
        	else{
        		
            	myLocationGPSManager();
        		
        	}
        	
        }
    };
    
    private OnClickListener createCenteredCitationListener = new OnClickListener()
    {
        public void onClick(View v)
        {                        
       	
        	GeoPoint citationPoint;
        	
        	if(!mapState.gpsMode) citationPoint=mapView.getMapCenter();
        	else{ 
        		
        		citationPoint=lastKnownLocation;
        		
        	        	
        	}
        	
        	if(citationPoint==null){
        		
        		Utilities.showToast(getString(R.string.toastGPSWorking), v.getContext());
        		
        	}
        	else{
        	
        		mc.animateTo(citationPoint);
	        	final double latitude=citationPoint.getLatitudeE6()/1E6;
				final double longitude=citationPoint.getLongitudeE6()/1E6;
	        	
	        	createCitationDialog(latitude, longitude);
        	}
        }
    };
    
    private OnClickListener myTracksListener = new OnClickListener()
    {
        public void onClick(View v)
        {                        
         	
          ((Vibrator)getSystemService(VIBRATOR_SERVICE)).vibrate(30);

        	
	        	if(mapState.myTracksOn) {
	        		
	        		mapState.myTracksOn=false;
	        		
	        		v.setBackgroundResource(drawable.appwidget_button_disabled);
	        		
	        		endTracking();           
	
	        	}
	        	else { 
	
	        		myTracksGPSManager();
	        	
	        }
        	
     
      }
        
    };
    
   
    
	
	private void startTraking(){
		
			//starting tracking service through myTracks
		
			pC.setTrackingService(true);
		
			trackId=tracksService.startTraking();	
			
			if(trackId<0){
				
				
				Utilities.showToast(getString(R.string.myTracksThirdPartyAccessDisabled), this);

				mapState.myTrackLoaded=false;
				mapState.myTracksOn=false;
   	    		myTracksButton.setBackgroundResource(drawable.appwidget_button_disabled);
				handleInfoDialog();

				
			}
			else{
				
				//adding myTracksOverlay with the track
				
				if(myTracks!=null) listOfOverlays.remove(myTracks);
				
				myTracks=new MyTracksOverlay(getBaseContext(),mapView,trackId,false,myTracksResultHandler);	
				mapState.myTrackLoaded=true;
					
				tvCenteredLocation.setText(getString(R.string.waitingGPS));
				myTracks.setInfoUTM(tvCenteredLocation);
				myTracks.setInfoPrec(utmPrec);
				
				listOfOverlays.add(myTracks);
				
				handleInfoDialog();
				
			}




	}
	
	private void endTracking(){

		//ending tracking service through myTracks

		pC.setTrackingService(false);

		boolean success=tracksService.endTracking(projCnt.getCleanProjectName());
		
		mapState.gpsMode=false;
		mapState.myTracksWorking=false;
		
		if(success){
		
			if(myTracks!=null){ 
				
				listOfOverlays.remove(myTracks);
				mapState.myTrackLoaded=false;
				
			}
			
		}
		else{
			
			Utilities.showToast(getString(R.string.myTracksInternalError), this);
			trackId=-1;
			
		}
   	 		
		tracksService.initMyTracksService(handlerMyTrackUpdates);
		
		handleInfoDialog();

  		  
	}
    
    private OnClickListener showGridListener = new OnClickListener()
    {
        public void onClick(View v)
        {                        
         	
          ((Vibrator)getSystemService(VIBRATOR_SERVICE)).vibrate(30);

        	
        	if(utmOverlay!=null){
        	
	        	if(mapState.gridModeOn) {
	        		
	        		mapState.gridModeOn=false;
	        		v.setBackgroundResource(drawable.grid_icon_off);
	        		
	                listOfOverlays.remove(utmOverlay);
	                
	                connectDBButton.setVisibility(View.GONE);
	
	        		v.invalidate();
	
	        	}
	        	else { 
	        		
	        		mapState.gridModeOn=true;
	        		
	        		v.setBackgroundResource(drawable.grid_icon);
	
	        		listOfOverlays.add(utmOverlay);
	
	        		if(!mapState.editModeOn){
	        			
		                connectDBButton.setVisibility(View.VISIBLE);

	        		}
	        		

	        		v.invalidate();
	        	}
	        	
	            handleInfoDialog();
	        	
	        	mapView.invalidate();
	        	
	        }
        	
       
        else{
        	
        	Utilities.showToast(getString(R.string.loadingUTM), v.getContext());
        	
        }
      }
        
    };
    
    private OnClickListener showDBListener = new OnClickListener()
    {
        public void onClick(View v)
        {                        

        	GeoPoint lastPointCenter=null;
 		
        		if(mapState.gpsMode && lastKnownLocation==null){
        			
                 		Utilities.showToast(getString(R.string.toastGPSWorking), v.getContext());
            		
            	}
        		else {
        			
        		//	if(Utilities.availableInternetConnection(v.getContext())){
        			
	                    ((Vibrator)getSystemService(VIBRATOR_SERVICE)).vibrate(30);
	
	        			
	        			if(mapState.gpsMode && lastKnownLocation!=null){
	        			
	        				lastPointCenter=lastKnownLocation;
	        		
	                		mc.animateTo(lastPointCenter);
	        			
	        			}
	        			
	        			else if(lastKnownLocation!=null) lastPointCenter=lastKnownLocation;
		        		else lastPointCenter=mapView.getMapCenter();
	        		
		        		
		    			DBManager dbL=new DBManager(v.getContext(),true);
	
		    			ProjectControler rsC=new ProjectControler(v.getContext());
		    			rsC.loadProjectInfoById(projId);
		
		    			ThesaurusControler tC=new ThesaurusControler(v.getContext());
		    			String thType=tC.getTHType(rsC.getThName());
		    			
		    	    	if(thType.equals("")) dbL.getFilum(rsC.getProjType());			
		    	    	else dbL.getFilum(thType);
		    			
		        		
		        		/* Check filumLetter  */
		        		
		        		if(	!dbL.getFilumLetter().toLowerCase().equals("")){
		        		       		
		        			Intent intent = new Intent(getBaseContext(), TaxonListExplorer.class);
		            		
		        			if(lastKnownLocation!=null) intent.putExtra("userLocation",true);
		            		
		        			intent.putExtra("projId",projId);
		            		intent.putExtra("type", dbL.getFilumType());
		            		
		            		intent.putExtra("latitude",lastPointCenter.getLatitudeE6()/1E6);
		            		intent.putExtra("longitude",lastPointCenter.getLongitudeE6()/1E6);
	
		
		            		startActivity(intent);
		            		
		        			//Utilities.showToast(getString(R.string.noFloraDataBase), getBaseContext());
	
		        			
		        		}
		        		else{
		        			
		        			Utilities.showToast(getString(R.string.outOfDataBaseRange), getBaseContext());
		        		}
	        		
		        		/*	}
        			
        			else{
        				
        				Utilities.showToast("No Internet Connection",v.getContext());
        				
        			}*/
        		
            	}
      
        	
        	
        }
    };
    
    
    
    
    private OnClickListener showLabelsListener = new OnClickListener()
    {
        public void onClick(View v)
        {       
        	
            ((Vibrator)getSystemService(VIBRATOR_SERVICE)).vibrate(30);

         
        	if(mapState.labelsOn) {
        		
        		mapState.labelsOn=false;
        		v.setBackgroundResource(drawable.info_icon_off);
        		v.invalidate();

        	}
        	else { 
        		
        		mapState.labelsOn=true;
        		
        		v.setBackgroundResource(drawable.info_icon);
        		v.invalidate();
        	}
        	
        	mapView.invalidate();
        	
        }
    };
    
    private OnClickListener editModeListener = new OnClickListener()
    {
        public void onClick(View v)
        {      
        	
            ((Vibrator)getSystemService(VIBRATOR_SERVICE)).vibrate(30);

         
        	if(mapState.editModeOn) {
        		
        		mapState.editModeOn=false;
        		
        		v.setBackgroundResource(drawable.edit_icon_off);
        		
        		
        	}
        	else {
      
        		mapState.editModeOn=true;
        		v.setBackgroundResource(drawable.edit_icon);

        	
        	}
        	
            handleInfoDialog();

        	
        }
    };
    
 
    private void myLocationGPSManager(){
    	
    	 lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
 		
 		if (lm.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
 	
 			lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 10, this);
 			
 			if(!mapState.polygonMode) listOfOverlays.add(myLocationOverlay);
 			
      	   	activateGPS(true);

 	
 		}
 		else{

 			AlertDialog.Builder builder = new AlertDialog.Builder(this);
 		    	
 		    	
 		    	builder.setMessage(R.string.enableGPSQuestion)
 		    	       .setCancelable(false)
 		    	       .setPositiveButton(R.string.enableGPS, new DialogInterface.OnClickListener() {
 		    	           public void onClick(DialogInterface dialog, int id) {
 		    
 		    	        	   Intent callGPSSettingIntent = new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
 		    	        	   startActivityForResult(callGPSSettingIntent,GPS_CONFIG);
 		    	        		        	   
 		    	           }

 		    	       })
 		    	       .setNegativeButton(R.string.noGPS, new DialogInterface.OnClickListener() {
 		    	           public void onClick(DialogInterface dialog, int id) {
 		    	                
 		    	        	activateGPS(false);
 		    	        	centerUniqueSample();
 			    	        dialog.dismiss();
 		    	                
 		    	           }

 		    	       });
 		    	
 		    	AlertDialog alert = builder.create();
 		    	
 		    	alert.show();


 		}
    	
    	
    	
    }
    
    private void myTracksGPSManager(){
    	
   	 lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		
		if (lm.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
	
			
			if(!pC.getTrackingService()){
				
				mapState.myTracksOn=true;
	    		myTracksButton.setBackgroundResource(drawable.appwidget_button_enabled);
	    		gpsButton.setVisibility(View.GONE);
				startTraking();
			
			}

	
		}
		else{

			AlertDialog.Builder builder = new AlertDialog.Builder(this);
		    	
		    	
		    	builder.setMessage(R.string.enableGPSQuestion)
		    	       .setCancelable(false)
		    	       .setPositiveButton(R.string.enableGPS, new DialogInterface.OnClickListener() {
		    	           public void onClick(DialogInterface dialog, int id) {
		    
		    	        	   Intent callGPSSettingIntent = new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
		    	        	   startActivityForResult(callGPSSettingIntent,GPS_CONFIG_MYTRACKS);
		    	        		        	   
		    	           }

		    	       })
		    	       .setNegativeButton(R.string.noGPS, new DialogInterface.OnClickListener() {
		    	           public void onClick(DialogInterface dialog, int id) {
		    	              
		    	        	   dialog.dismiss();
		    	                
		    	           }

		    	       });
		    	
		    	AlertDialog alert = builder.create();
		    	
		    	alert.show();


		}
   	
   	
   	
   }
    

    public void createCitationDialog(final double latitude, final double longitude) {


		   AlertDialog.Builder builder = new AlertDialog.Builder(this);
		    
			   
		   	String createCitationQuestion= String.format(getString(R.string.mapCreateCitation), latitude,longitude);
		   	

		    
		    builder.setMessage(createCitationQuestion)
		           .setCancelable(false)
		           .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
		               public void onClick(DialogInterface dialog, int id) {
		            	   
		            	   createCitation(latitude,longitude);
		            	   dialog.dismiss();

		               }
		           })
		           .setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
		               public void onClick(DialogInterface dialog, int id) {
		                    
		            	   dialog.dismiss();
		            	   
		            	   
		               }
		           });
		   
		    
			 AlertDialog alert = builder.create();
			 alert.getWindow().setGravity(Gravity.BOTTOM);

			 alert.show();
	} 
	
	/*
	 * 
	 * 	This method calls Sampling Activity with a predefined latitude and longitude
	 * 
	 */
	
	  private void createCitation(double latitude, double longitude){
		  
			Intent myIntent=new Intent(this, Sampling.class);
			myIntent.putExtra("latitude", latitude);
			myIntent.putExtra("longitude", longitude);
			
			if(mapState.gpsMode) myIntent.putExtra("altitude", elevation);
			
			Bundle b = new Bundle();
	 		b = new Bundle();
			b.putLong("id",projId);
			myIntent.putExtras(b);
			
	        startActivityForResult(myIntent,MAP_MARKERS_UPDATED);
		  
	  }
	  
    
    private void activateGPS(boolean active){
    	
    	if(active){
    		
    		this.mapState.gpsOn=true;
        	mapState.gpsMode=true;
            gpsButton.setBackgroundResource(drawable.position_icon);
    		
    	}
    	else{
    		
        	this.mapState.noGPS=true;
        	this.mapState.gpsOn=false;
        	mapState.gpsMode=false;
        	
        	
            gpsButton.setBackgroundResource(drawable.position_icon_off);
            if(mapState.myTracksWorking) myTracksButton.setVisibility(View.VISIBLE);
            
            if(lm.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            	
            	lm.removeUpdates(this);
            	
            }
            
            listOfOverlays.remove(myLocationOverlay);
            
            lastKnownLocation=null;
    		
    	}
    	
        handleInfoDialog();

   	
    }
 
    	
    
    /*
     * 
     * This method get's all citations from the provided project with its location
     * 
     * @return string list with pairs of latitude and longitude
     * 
     */


	private void centerUniqueSample() {
		
		if(idCitation!=0 || mapState.noGPS){
			
			if(idCitation!=0){
				
	    	    Log.d("CitationsMap","B. Centering to unique Citations");
				
			}
			else{

	    	    Log.d("CitationsMap","A. Centering to last Citation");

			}

			
			if(coordinates.size()>0){
			
				 double lat = coordinates.get(0).getLatitude();
			     double lng = coordinates.get(0).getLongitude();
			 
			     GeoPoint unique = new GeoPoint(
			            (int) (lat * 1E6), 
			            (int) (lng * 1E6));
	
			      mc.animateTo(unique);
			}
		        
		}
		
		
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		if(!mapState.polygonMode){
		
			menu.add(0,CHOOSE_TRACK, 0,R.string.mLoadTrack).setIcon(android.R.drawable.ic_menu_directions);
			menu.add(0,MAP_TYPE, 0,R.string.mapShowNormalView).setIcon(android.R.drawable.ic_menu_mapmode);
			menu.add(0,MAP_OPTIONS, 0,R.string.mMapsOptions).setIcon(android.R.drawable.ic_menu_preferences);
			menu.add(0,DB_CONFIG, 0,R.string.mConfigDB).setIcon(getResources().getDrawable(R.drawable.ic_menu_db));

		}

    	return super.onCreateOptionsMenu(menu);
    }

	@Override
	public boolean onPrepareOptionsMenu(Menu menu){
		
		if(!mapState.polygonMode){

			if(mapView.isTraffic())  menu.findItem(MAP_TYPE).setTitle(R.string.mapShowNormalView);
	    	else  menu.findItem(MAP_TYPE).setTitle(getString(R.string.mapShowTransitView));
			
			if(lastKnownLocation==null){
				
				menu.removeItem(MY_LOCATION);
				
			}
			else{
				
		    	if(menu.findItem(MY_LOCATION)==null)menu.add(0, MY_LOCATION, 1,R.string.mCenterLocation).setIcon(android.R.drawable.ic_menu_mapmode);
	
			}
			
			if(!mapState.myTracksOn && trackId>-1){
				
		    	if(menu.findItem(CLEAR_MAP)==null) menu.add(0, CLEAR_MAP, (menu.findItem(CHOOSE_TRACK).getOrder())+1,R.string.mCleanTrackLoaded).setIcon(android.R.drawable.ic_menu_close_clear_cancel);
	
				
			}
			else {
				
				if(menu.findItem(CLEAR_MAP)!=null) menu.removeItem(CLEAR_MAP);
			}

		}

		return super.onPrepareOptionsMenu(menu);
		
	}

    
    @Override
	public boolean onOptionsItemSelected(MenuItem item) {
    	
    	
		switch (item.getItemId()) {
	
		case MY_LOCATION:
			
			if(lastKnownLocation!=null) mc.animateTo(lastKnownLocation);
			
			break;
		
		case CLEAR_MAP:
			
			trackId=-1;
			
			listOfOverlays.remove(myTracks);
			mapState.myTrackLoaded=false;
			mapView.invalidate();
			
			handleInfoDialog();
			
			break;
			
		case CHOOSE_TRACK:
			
     		Intent trackListActivity = new Intent(this,TrackListChooser.class);
       		trackListActivity.putExtra("projId", projId);
        	startActivityForResult(trackListActivity,1);
		 			 
        	break;
		case DB_CONFIG:
			
			
			String filum=projCnt.getFilum();
			
			if(filum.equals("")){
				
				Utilities.showToast(getBaseContext().getString(R.string.projWithoutTh), this);
			
				
			}
			else{
				
				Intent dbConfigActivity = new Intent(this,RemoteDBConfig.class);
				dbConfigActivity.putExtra("filum", filum);
	        	startActivity(dbConfigActivity);
	        	
			}
			
     	
		 			 
        	break;
		case MAP_OPTIONS:
			
			MapConfigurationDialog.initDialog(this,pC,myLocation.isCompassEnabled(),handlerUpdateConf);
			
		break;
		
		case MAP_TYPE:

			if(mapView.isSatellite()){
				
				mapView.setSatellite(false);
				mapView.setTraffic(true);
				
			}
			else{
				
				mapView.setSatellite(true);
				mapView.setTraffic(false);
				
			}

			
		 			 
		break;
			
		}
	
		return super.onOptionsItemSelected(item);
	}


	private ArrayList<LocationCoord> loadProjectCitations(String preSettedLoc) {

		ArrayList<LocationCoord> coordinates = new ArrayList<LocationCoord>();

		String[] ids=preSettedLoc.split(":");
		
		for(int i=1;i<ids.length;i++){
			
			ArrayList<LocationCoord> coordinatesTmp= sC.getSampleLocationBySampleId(projId,Long.valueOf(ids[i]),mapLocations);
			
			coordinates.addAll(coordinatesTmp);
			
		}		
    	
    	return coordinates;
	}
    

	private ArrayList<LocationCoord> loadProjectCitations() {
		
		projId=getIntent().getExtras().getLong("id");
		idCitation=getIntent().getExtras().getLong("idSample");
		
		ArrayList<LocationCoord> coordinates;
		
		if(idCitation==0){
			
			 coordinates= sC.getSamplesLocationByProjectId(projId, mapLocations);

		}
		else{
			
			coordinates= sC.getSampleLocationBySampleId(projId,idCitation,mapLocations);
			
		}
	
		return coordinates;
	}
	
	
	

	public void onLocationChanged(Location location) {
		if (location != null) {
			
			Double lat = location.getLatitude();
			Double lng = location.getLongitude();
			
		/*	if(coorSystem.equals("UTM")){
		    */	 
		         CoordinateUTM utm = CoordConverter.getInstance().toUTM(new CoordinateLatLon(lat,lng));
		         tvCenteredLocation.setText(UTMDisplay.convertUTM(utm.getShortForm(),utmPrec,false));
	
			     
		         elevation=location.getAltitude()-pC.getGeoidCorrection();
			     tvAltitude.setText(" "+(int)elevation+" (m)");
			     
			     
		 /*    }
		     else {
	
		    	 pos.setText(lat.toString().subSequence(0, 7)+"\n"+lng.toString().subSequence(0, 7));
	
		     }*/

			
			lastKnownLocation = new GeoPoint((int) (lat * 1E6), (int) (lng * 1E6));
			if(myLocationOverlay!=null) myLocationOverlay.setCurrent(lastKnownLocation);
			mc.animateTo(lastKnownLocation);
			
			if(polygonField!=null && polygonField.isWaitingGPS()) polygonField.addPoint(lat, lng, elevation);
			
		}
	}
	
	public void onProviderDisabled(String provider) {
		// required for interface, not used
	}
	
	public void onProviderEnabled(String provider) {
		// required for interface, not used
	}
	
	public void onStatusChanged(String provider, int status, Bundle extras) {
		// required for interface, not used
	}
	
	@Override
	protected boolean isRouteDisplayed() {
		return false;
	}
	
	
	public Point location2Point(Location aLocation){
		return new Point((int) (aLocation.getLongitude() * 1E6),
						(int) (aLocation.getLatitude() * 1E6));
	}
	
	public void onConfigurationChanged (Configuration newConfig){
		
		super.onConfigurationChanged(newConfig);
		
		if(mapOverlay!=null) mapOverlay.updateWidthHeight();
		
	}


	
    @Override
	protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        
    	 super.onActivityResult(requestCode, resultCode, intent);
    	

         switch(requestCode) {
         
         
         case MAP_MARKERS_UPDATED :
        	       	 
        	 listOfOverlays.remove(mapOverlay);
        	 //mapView.invalidate();
        	 
             loadCitations();


             
             break;
             
         case LOAD_TRACKS :
             
           	 if(intent!=null){
	         	 
           		 Bundle ext = intent.getExtras();
	        	 trackId=ext.getLong("trackId");
	        	 String trackProjName=ext.getString("trackProject");
	        	 loadTrack(trackId);
	        	 
           	 }
             
             break;
             
         case GPS_CONFIG :
             
           		myLocationGPSManager();
             
             break;
         		
         case GPS_CONFIG_MYTRACKS :
                
        	 mapState.askedGPSUpdate=true;
             
             break;
          
         	default:
             
         }
         
    }
	
	

	class CitationsOverlay extends com.google.android.maps.Overlay
    {
    
	 	ArrayList<MapLocation> mapLocations = new ArrayList<MapLocation>();
	 	
	    private Bitmap bubbleIcon,bubbleIconExt, shadowIcon, greenIcon;
	    
	    private Paint innerPaint, borderPaint, textPaint;
	    
	    private ImageView dragImage=null;
	    
	    private MapLocation selectedMapLocation;
		
		private Context c;
		private CitationControler sC;
		private long projId;
		
		private int screenWidth;
		private int screenHeight;
		
		private MapLocation lastMapLocation ;
		//private MapLocation previousMapLocation;

		private int xDragTouchOffset;

		private int yDragTouchOffset;

		private MapLocation inDrag;

		private int xDragImageOffset=8;
		private int yDragImageOffset=36;
		
		private MapLocation oldMapLoc;
		private Context baseContext;
		
		private HashMap<Long, String> citationsExtendedInfo;
		private HashMap<Long, String> citationsWithPhoto;
		
		private String citationFieldName;
		
		

	   public CitationsOverlay(ArrayList<MapLocation> mapLocations,CitationControler sC, long projId, Context context) {
	 	
		   this.mapLocations=mapLocations;
		   this.baseContext=context;
		 
		   dragImage=(ImageView)findViewById(R.id.drag);
		   
		   llCitationInfo.setVisibility(View.GONE);
		   selectedMapLocation=null;
		   
		   bubbleIcon = BitmapFactory.decodeResource(getResources(),R.drawable.bubble);
		   bubbleIconExt = BitmapFactory.decodeResource(getResources(),R.drawable.bubble_point);
		   shadowIcon = BitmapFactory.decodeResource(getResources(),R.drawable.shadoww);
		   greenIcon = BitmapFactory.decodeResource(getResources(),R.drawable.bubble_verd);
		   
		   this.sC=sC;
		   this.projId=projId;
		   this.c=sC.getBaseContext();
		   
		   citationsExtendedInfo=new HashMap<Long, String>();
		   citationsWithPhoto=new HashMap<Long, String>();
		   
		   
		   updateWidthHeight();
		   
		   getCitationFieldPhotoName();

		   
	    }
	   
	   
	   public void updateWidthHeight() {
		
		   Display display = getWindowManager().getDefaultDisplay(); 
		   screenWidth = display.getWidth();
		   screenHeight= display.getHeight();
		   
	   }


	private void getCitationFieldPhotoName() {

		   ProjectControler pC=new ProjectControler(baseContext);
		   citationFieldName=pC.getPhotoFieldName(projId);
		   
	   }


	@Override
	public boolean onTouchEvent(MotionEvent event, final MapView mapView) 
       {   
		   //when not edit mode we can move the marker
		   if(!mapState.movCitationEnabled) return false;
		   
		   final int action=event.getAction();
		   final int x=(int)event.getX();
		   final int y=(int)event.getY();
		   boolean result=false;
		      
		   
		   if(event.getAction() == MotionEvent.ACTION_DOWN){
			   
			   		 Point p= new Point(0,0);
			          
			          GeoPoint point=mapView.getProjection().fromPixels(x,y);
			          inDrag=getHitMapLocation(mapView,point);

			          
			          if (inDrag!=null && inDrag==lastMapLocation) {
			            
			        	  GeoPoint selected=inDrag.getPoint();
			        	  
			        	  oldMapLoc=inDrag.copy();
			        	  
			        	  mapView.getProjection().toPixels(selected,p);
	
			        	  result=true;
			        
			        	  mapLocations.remove(inDrag);
			           
			        	  // populate();

			        	  xDragTouchOffset=0;
			        	  yDragTouchOffset=0;
			            
			        	  setDragImagePosition(p.x, p.y);
			        	  dragImage.setVisibility(View.VISIBLE);

			        	  xDragTouchOffset=x-p.x;
			        	  yDragTouchOffset=y-p.y;

			        	  ((Vibrator)getSystemService(VIBRATOR_SERVICE)).vibrate(30);
		      
		   
			          }
			          else inDrag=null;
			   
		   }
			else if (event.getAction() == MotionEvent.ACTION_MOVE && inDrag!=null) {
			              
			   setDragImagePosition(x, y);
			   result=true;
			              
			}
			else if (event.getAction() == MotionEvent.ACTION_UP && inDrag!=null) {
			              
				dragImage.setVisibility(View.GONE);
			              
			    final GeoPoint pt=mapView.getProjection().fromPixels(x-xDragTouchOffset+6,y-yDragTouchOffset+14);
			   // OverlayItem toDrop=new OverlayItem(pt, inDrag.getTitle(),inDrag.getSnippet());
			    final MapLocation newMapLoc=inDrag;
			    newMapLoc.setPoint(pt);
			    
			              
				((Vibrator)getSystemService(VIBRATOR_SERVICE)).vibrate(30);
				
				AlertDialog.Builder builder = new AlertDialog.Builder(c);
					    	
			   	String moveCitationQuestion= String.format(getString(R.string.mapMoveCitation), pt.getLatitudeE6()/1e6,pt.getLongitudeE6()/1e6);

					    	
					   builder.setMessage(moveCitationQuestion)
					    	       .setCancelable(false)
					    	       .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
					    	           public void onClick(DialogInterface dialog, int id) {
					    
					    	        	   	updateCitationLocation(pt.getLatitudeE6(),pt.getLongitudeE6(),inDrag);
								              
					    				    mapLocations.add(newMapLoc); //afegim 
					    				    //populate();
					    				              
					    				    inDrag=null;
					    				    
					    				    invalidateMoveButton();
					    				    
					    				    lastMapLocation=newMapLoc;
					    				    lastMapLocation.setChosen(true);

					    				    
					    				    mapView.invalidate();
			
					    	        	   
					    	           }
					    	       })
					    	       .setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
					    	           public void onClick(DialogInterface dialog, int id) {
					    	                
					    	        	   dialog.cancel();
					    	        	   mapLocations.add(oldMapLoc);
					    	        	   inDrag=null;
					    				   
					    				   lastMapLocation=oldMapLoc;
					    				   lastMapLocation.setChosen(true);
					    	        	 
					    	        	   invalidateMoveButton();

					    	        	   
					    	        	   mapView.invalidate();

					    	        	  					    	                
					    	           }
					    	       });
					    	AlertDialog alert = builder.create();
					    	
					    	alert.show();

	    				    result=true;

				

			 }          
   
   			return(result || super.onTouchEvent(event, mapView));
   
       }   
	   
	   
	   
	    private OnClickListener editCitationListener = new OnClickListener()
	    {
	        public void onClick(View v)
	        {                        
	       	
	        	ProjectControler rc= new ProjectControler(c);
      			rc.loadProjectInfoById(projId);
   	 
    	 		Intent intent = new Intent(v.getContext(), CitationEditor.class);
   
	 			Bundle b = new Bundle();
	 			b.putString("rsName", rc.getName());
	 			intent.putExtras(b);
	 			b = new Bundle();
	 			b.putLong("id", projId);
	 			intent.putExtras(b);
	 			b = new Bundle();
	 			b.putString("rsDescription", rc.getThName());
	 			intent.putExtras(b);
	 			b = new Bundle();
	 			b.putLong("idSample", v.getId());
	 			intent.putExtras(b);
	 		
	 			
	 		   ((Activity) c).startActivityForResult(intent,MAP_MARKERS_UPDATED);   	        	
	        }
	    };
	    
	    
	    private OnClickListener callGalleryCitationListener = new OnClickListener()
	    {
	        public void onClick(View v)
	        {                        
   	 
    	 		Intent intent = new Intent(v.getContext(), uni.projecte.Activities.Miscelaneous.ImageView.class);
   
	 			Bundle b = new Bundle();
	 			b.putString("photoPath", (String) v.getTag());
	 			intent.putExtras(b);
	 	
	 			b = new Bundle();
	 			b.putLong("projId", projId);
	 			intent.putExtras(b);
	 			
	 			
	 		   ((Activity) c).startActivity(intent);   	        	
	        }
	    };
	    
	    
	    private OnClickListener moveCitationListener = new OnClickListener()
	    {
	        public void onClick(View v)
	        {                        
	         	
	        	if(mapState.movCitationEnabled){ 
	        		
	        		mapState.movCitationEnabled=false;
	        		v.setBackgroundResource(drawable.move_cit);
	        	
	        	}
	        	else{ 
	        		
	        		mapState.movCitationEnabled=true;
	        		v.setBackgroundResource(drawable.move_cit_on);

	        	}
	        	
	        	mapView.invalidate();
	     
	      }
	        
	    };
	   
		private void updateCitationLocation(int latitudeE6, int longitudeE6, MapLocation point) {

			CitationControler sC= new CitationControler(getBaseContext());
			
			sC.startTransaction();
			sC.updateCitationLocation(point.getCitationId(), latitudeE6/1e6, longitudeE6/1e6);
			sC.EndTransaction();
			
		}

		/*
		 * 	This method controls when a map marker is tapped;
		 * 	previous and selected Map location are stored
		 *  
		 */

		@Override
		public boolean onTap(GeoPoint p, MapView	mapView)  {
		
			
			if(!mapState.movCitationEnabled){
			
			//  Next test whether a new popup should be displayed
				selectedMapLocation = getHitMapLocation(mapView,p);


				// no marker tRapped
				if (selectedMapLocation != null) {

					selectedMapLocation.setChosen(true);
					
					//controlling secondTap
					if(lastMapLocation!=null && selectedMapLocation==lastMapLocation) {
						
	   					if(selectedMapLocation.isMoreInfo()) selectedMapLocation.setMoreInfo(false); 
	   					else selectedMapLocation.setMoreInfo(true);
	   					
	   				}
	   				else{
	   					
	   					if(lastMapLocation!=null) {
	   						
	   						lastMapLocation.setMoreInfo(false);
	   						lastMapLocation.setChosen(false);
	   						
	   					}
	   					
   						lastMapLocation=selectedMapLocation;
	   					
	   				}
					
				}
				//marker tRapped
				else{
					
					//lastMapLocation will be null -> region without marker has been tapped
					
					if(lastMapLocation!=null){ 
						
						lastMapLocation.setMoreInfo(false);
						lastMapLocation.setChosen(false);
						
					}
					
					lastMapLocation=null;
				
				}
				
				mapView.invalidate();
			
			}
		
			
			//  Lastly return true if we handled this onTap()
			return selectedMapLocation != null;
		}
		
		
		
		
	    private void invalidateMoveButton() {

			mapState.movCitationEnabled=false;
		    moveCitation.setBackgroundResource(drawable.move_cit);
	    	
		}

		private void setDragImagePosition(int x, int y) {
	        
	    	RelativeLayout.LayoutParams lp=(RelativeLayout.LayoutParams)dragImage.getLayoutParams();
	              
	        lp.setMargins(x-xDragImageOffset-xDragTouchOffset,y-yDragImageOffset-yDragTouchOffset, 0, 0);
	        
	        dragImage.setLayoutParams(lp);
	        
	      }
		
	    private MapLocation getHitMapLocation(MapView	mapView, GeoPoint	tapPoint) {
	    	
	    	//  Track which MapLocation was hit...if any
	    	MapLocation hitMapLocation = null;
			
	    	RectF hitTestRecr = new RectF();
			Point screenCoords = new Point();
	    	Iterator<MapLocation> iterator = mapLocations.iterator();
	    	while(iterator.hasNext()) {
	    		
	    		
	    		MapLocation testLocation = iterator.next();
	    		
	    		//  Translate the MapLocation's lat/long coordinates to screen coordinates
	    		mapView.getProjection().toPixels(testLocation.getPoint(), screenCoords);

		    	// Create a 'hit' testing Rectangle w/size and coordinates of our icon
		    	// Set the 'hit' testing Rectangle with the size and coordinates of our on screen icon
	    		hitTestRecr.set(-bubbleIcon.getWidth()/2,-bubbleIcon.getHeight(),bubbleIcon.getWidth()/2,0);
	    		hitTestRecr.offset(screenCoords.x,screenCoords.y);

		    	//  Finally test for a match between our 'hit' Rectangle and the location clicked by the user
	    		mapView.getProjection().toPixels(tapPoint, screenCoords);
	    		
	    		
	    		if (hitTestRecr.contains(screenCoords.x,screenCoords.y)) {
	    			
	    			hitMapLocation = testLocation;
	    			
	    			break;
	    		}
	    	}
	    	
	    	//  Lastly clear the newMouseSelection as it has now been processed
	    	tapPoint = null;
	    	
	    	return hitMapLocation; 
	    }
	    
	    /*
	     * Main citationMap layer draw method that draws:
	     * 	- Map Locations markers 
	     * 	- Map locations info when a marker is chosen
	     * 
	     * 	- Optionally:
	     * 		- When gps is not working a centered cross is shown and also the UTM central location. 
	     * 
	     */
	    
		@Override
		public void draw(Canvas canvas, MapView	mapView, boolean shadow) {
	    	
	   		drawMapLocations(canvas, mapView, shadow);
	   		drawInfoWindow(canvas, mapView, shadow);
	   		
	   		/* It will be shown when Gps is not working */
	   		if(mapState.editModeOn && !mapState.gpsMode) {
				
				GeoPoint center=mapView.getMapCenter();
				MapDrawUtils.drawUTMCross(canvas, center, "",mapView);
				updateUTM(center);
				
			}
	   		
	    }
		
	
	    
	
		private void drawMapLocations(Canvas canvas, MapView mapView, boolean shadow) {
	    	
			Iterator<MapLocation> iterator = mapLocations.iterator();
			Point screenCoords = new Point();
			
			
	    	while(iterator.hasNext()) {	   
	    		
	    		MapLocation location = iterator.next();
	    		mapView.getProjection().toPixels(location.getPoint(), screenCoords);
				
		    	if (shadow) {
		    		
		    		//  Only offset the shadow in the y-axis as the shadow is angled so the base is at x=0; 
		    		canvas.drawBitmap(shadowIcon, screenCoords.x, screenCoords.y - shadowIcon.getHeight(),null);
		    		
		    		
		    	} else {
	    			
		    		if(location.isChosen()){
		    			
		    			if(mapState.movCitationEnabled) canvas.drawBitmap(greenIcon, screenCoords.x - bubbleIconExt.getWidth()/2, screenCoords.y - bubbleIconExt.getHeight(),null);
		    			else canvas.drawBitmap(bubbleIconExt, screenCoords.x - bubbleIconExt.getWidth()/2, screenCoords.y - bubbleIconExt.getHeight(),null);
		    				
		    		}
		    		else{
		    		
		    			canvas.drawBitmap(bubbleIcon, screenCoords.x - bubbleIcon.getWidth()/2, screenCoords.y - bubbleIcon.getHeight(),null);
		    		
		    		}
		    		
	    			if (mapState.labelsOn) drawBubbles(canvas, location);
	    			
				
		    	}
	    	}
	    	
			MapDrawUtils.drawMapZoom(canvas,mapView,screenWidth,screenHeight);

	    }
	    



		private void drawBubbles(Canvas canvas, MapLocation point){
	    	
	    	String citationInfo="";
			int INFO_WINDOW_WIDTH;
			int INFO_WINDOW_HEIGHT;
			//  Draw the MapLocation's name
			int TEXT_OFFSET_X = 10;
			int TEXT_OFFSET_Y = 15;
			
			Point selDestinationOffset = new Point();
			mapView.getProjection().toPixels(point.getPoint(), selDestinationOffset);
	    	
	    	//  Setup the info window with the right size & location
			
			RectF infoWindowRect;
			
			citationInfo=point.getName();
			
				if(point.getName()!=null){
					
					INFO_WINDOW_WIDTH = 12+6*point.getName().length();

				}
				else{
					
					citationInfo="";
					INFO_WINDOW_WIDTH = 12;

				}
				INFO_WINDOW_HEIGHT = 25;
				
				infoWindowRect= new RectF(0,0,INFO_WINDOW_WIDTH,INFO_WINDOW_HEIGHT);				
				int infoWindowOffsetX = selDestinationOffset.x-INFO_WINDOW_WIDTH/2;
				int infoWindowOffsetY = selDestinationOffset.y-INFO_WINDOW_HEIGHT-bubbleIcon.getHeight();
				infoWindowRect.offset(infoWindowOffsetX,infoWindowOffsetY);
				
				//  Draw inner info window
				canvas.drawRoundRect(infoWindowRect, 5, 5, getInnerPaint());
				
				//  Draw border for info window
				//canvas.drawRoundRect(infoWindowRect, 5, 5, getBorderPaint());
				
				canvas.drawText(citationInfo,infoWindowOffsetX+TEXT_OFFSET_X,infoWindowOffsetY+TEXT_OFFSET_Y,getTextPaint());

			
	    	
	    }
	   
	    private void drawInfoWindow(Canvas canvas, MapView	mapView, boolean shadow) {
	    	
	    	if ( selectedMapLocation != null) {
	    		if ( shadow) {
	    			//  Skip painting a shadow in this tutorial
	    		} else {
			
	    			
					if(selectedMapLocation.isMoreInfo()) {
						
					   	llCitationInfo.setVisibility(View.VISIBLE);

					   	long citationId=selectedMapLocation.getCitationId();
					   	
					   	String citationInfoText=citationsExtendedInfo.get(citationId);
					   	
					   	if(citationInfoText==null){ 
					   		
					   		citationInfoText=sC.getAllCitationValues(citationId,fieldsLabelNames,citationFieldName,citationsWithPhoto);
					   		citationsExtendedInfo.put(citationId, citationInfoText);
					   		
					   	}
					   	
					   	
					   	
						   	if(citationsWithPhoto.get(citationId)!=null){
						   							   		
						   		llCitationWithPhoto.setVisibility(View.VISIBLE);
						   		photoCitation.setOnClickListener(callGalleryCitationListener);
						   		photoCitation.setTag(citationsWithPhoto.get(citationId));
						   							   		
						   		
						   	}
						   	else llCitationWithPhoto.setVisibility(View.GONE);
						   	
						   tvCitationInfo.setText(citationInfoText);
								
						   editCitation.setId((int) citationId);
						   editCitation.setOnClickListener(editCitationListener);
								
						   moveCitation.setOnClickListener(moveCitationListener);
							
					   	}
					   	
					
					else{
						
						String citationInfo="";

						int INFO_WINDOW_WIDTH;
		    			//  Draw the MapLocation's name
						int TEXT_OFFSET_X = 10;
						int TEXT_OFFSET_Y = 15;
						
						RectF infoWindowRect;
						Point selDestinationOffset = new Point();

						
					   	llCitationInfo.setVisibility(View.GONE);

					   	
						mapView.getProjection().toPixels(selectedMapLocation.getPoint(), selDestinationOffset);
						
						citationInfo=selectedMapLocation.getName();
						
						if(selectedMapLocation.getName()!=null){
							
							INFO_WINDOW_WIDTH = 12+6*selectedMapLocation.getName().length();

						}
						else{
							
							citationInfo="";
							INFO_WINDOW_WIDTH = 12;

						}
						
						int INFO_WINDOW_HEIGHT = 25;
						
						infoWindowRect= new RectF(0,0,INFO_WINDOW_WIDTH,INFO_WINDOW_HEIGHT);				
						int infoWindowOffsetX = selDestinationOffset.x-INFO_WINDOW_WIDTH/2;
						int infoWindowOffsetY = selDestinationOffset.y-INFO_WINDOW_HEIGHT-bubbleIcon.getHeight();
						infoWindowRect.offset(infoWindowOffsetX,infoWindowOffsetY);
						
						//  Draw inner info window
						canvas.drawRoundRect(infoWindowRect, 5, 5, getInnerPaint());
						
						//  Draw border for info window
						canvas.drawRoundRect(infoWindowRect, 5, 5, getBorderPaint());
						
						canvas.drawText(citationInfo,infoWindowOffsetX+TEXT_OFFSET_X,infoWindowOffsetY+TEXT_OFFSET_Y,getTextPaint());
						
						mapState.movCitationEnabled=false;

					}
	    			

	    		}
	    	}
	    	else{
	    		
			   	llCitationInfo.setVisibility(View.GONE);
				mapState.movCitationEnabled=false;

	    	}
	    }
	    

	    
	    public Paint getInnerPaint() {
			if ( innerPaint == null) {
				innerPaint = new Paint();
				innerPaint.setARGB(225, 75, 75, 75); //gray
				innerPaint.setAntiAlias(true);
			}
			return innerPaint;
		}
	    
	    public Paint getTextPaint() {
			if ( textPaint == null) {
				textPaint = new Paint();
				textPaint.setARGB(255, 255, 255, 255);
				textPaint.setAntiAlias(true);
			}
			return textPaint;
		}
	    
	    public Paint getBorderPaint() {
			if ( borderPaint == null) {
				borderPaint = new Paint();
				borderPaint.setARGB(255, 255, 255, 255);
				borderPaint.setAntiAlias(true);
				borderPaint.setStyle(Style.STROKE);
				borderPaint.setStrokeWidth(1);
			}
			return borderPaint;
		}
	    
	    private void updateUTM(GeoPoint center){
			
			CoordinateUTM utm = CoordConverter.getInstance().toUTM(new CoordinateLatLon(center.getLatitudeE6()/1E6,center.getLongitudeE6()/1E6));
			tvCenteredLocation.setText(UTMDisplay.convertUTM(utm.getShortForm(),utmPrec,false));
			
		}
   
        
    }

	


	

    
}

