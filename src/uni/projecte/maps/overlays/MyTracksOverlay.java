package uni.projecte.maps.overlays;

import java.util.ArrayList;
import java.util.Iterator;

import uni.projecte.R;
import uni.projecte.maps.UTMDisplay;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Point;
import android.location.Location;
import android.os.Handler;
import android.widget.TextView;

import com.google.android.apps.mytracks.content.MyTracksProviderUtils;
import com.google.android.apps.mytracks.content.MyTracksProviderUtils.LocationIterator;
import com.google.android.apps.mytracks.content.Track;
import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapView;

import edu.ub.bio.biogeolib.CoordConverter;
import edu.ub.bio.biogeolib.CoordinateLatLon;
import edu.ub.bio.biogeolib.CoordinateUTM;


public class MyTracksOverlay extends com.google.android.maps.Overlay {

		private MapView mapView;
		private MyTracksProviderUtils myTracksProviderUtils;
		private Context baseContext;
		private Track track;
		private Bitmap bubbleIconExt;
		private long trackId;
		private TextView infoUTM;
		private String infoPrec;
		
		private ArrayList<GeoPoint> trackPointsList;
		
		private boolean cache=true;
		private Bitmap lastMarker;
		private Handler handler;
		
		private GeoPoint lastPoint;
		private double elevation;
		

		public MyTracksOverlay(Context context,MapView mapView,long trackId,boolean cache,Handler handler){
						
			this.mapView=mapView;
			this.baseContext=context;
			this.handler=handler;
			
		    myTracksProviderUtils = MyTracksProviderUtils.Factory.get(baseContext);
		    track=myTracksProviderUtils.getTrack(trackId);
		    
			bubbleIconExt = BitmapFactory.decodeResource(baseContext.getResources(),R.drawable.blue_dot);
 			lastMarker = BitmapFactory.decodeResource(context.getResources(), R.drawable.arrow_blue);

			this.trackId=trackId;
			this.cache=cache;
			
			if(cache) loadTrackPoints();
			
		}
		
		private void getLastAltitude(){
			
//			Location lastLoc=myTracksProviderUtils.getLastLocation();
			//if(lastLoc!=null) elevation=lastLoc.getAltitude();
			elevation=0.0;
		
		}
		
		public GeoPoint getLastLocation(){
			
			return lastPoint;

		}
		
		private void loadTrackPoints(){

			
			trackPointsList=new ArrayList<GeoPoint>();
			LocationIterator it = myTracksProviderUtils.getTrackPointLocationIterator(trackId, -1,false, MyTracksProviderUtils.DEFAULT_LOCATION_FACTORY);
			
			getLastAltitude();

			GeoPoint geo=null;
			
		      try {
		    	  
		      
		       while (it.hasNext()) {
	            	 
	            	Location location = it.next();
		    		
		 	    	geo=new GeoPoint((int)(location.getLatitude()*1E6),(int)(location.getLongitude()*1E6));	    		 
		 	    	trackPointsList.add(geo);
	
	             }
		       
		       if(geo!=null) lastPoint=geo;	    		 


		      }
		      finally{
		    	  
		    	  it.close();
		    	  
		      }
			
		}
		
		@Override
		public void draw(Canvas canvas, MapView mapView, boolean shadow) {

			if(!cache) loadTrackPoints();
			
			Iterator<GeoPoint> it=trackPointsList.iterator();
			
			Point screenCoords = new Point();
			Point previousScreenCoords = new Point();

			GeoPoint previousPoint = null;
			GeoPoint geo=null;
			
			Paint paint=new Paint();
			paint.setColor(Color.argb(255, 87, 129, 252));
	        paint.setAntiAlias(true);
	        paint.setStrokeWidth(4);
	        paint.setStrokeCap(Paint.Cap.ROUND);
	        paint.setStyle(Paint.Style.STROKE);
			

			if(it.hasNext()){
				
				geo=it.next();	    		

				mapView.getProjection().toPixels(geo,screenCoords);
				
				/* First point */
				canvas.drawBitmap(bubbleIconExt, screenCoords.x - bubbleIconExt.getWidth()/2, screenCoords.y - bubbleIconExt.getHeight(),null);
				previousPoint=geo;
	
		
		             while (it.hasNext()) {

		 	    		geo= it.next();	    		
		 		        mapView.getProjection().toPixels(geo,screenCoords);
		 		        mapView.getProjection().toPixels(previousPoint, previousScreenCoords);
		 		
		 		    	canvas.drawLine(previousScreenCoords.x, previousScreenCoords.y, screenCoords.x, screenCoords.y, paint);

						previousPoint=geo;
						
		            	 
		             }
		             
		             
		         /* Last Point */  
		 		if(previousScreenCoords!=null)	{

					/* Last Point and Previous Point: calculating  arrow Rotation */
		 			float bearing=calculateBearing(screenCoords,previousScreenCoords);
		 			
	 		    	paint.setColor(Color.argb(255, 87, 129, 252));

					Matrix matrix = new Matrix();
			        matrix.postRotate(360-bearing);
  
					Bitmap arrowR = Bitmap.createBitmap(lastMarker,0, 0, lastMarker.getWidth(), lastMarker.getHeight(), matrix, true);
					canvas.drawBitmap(arrowR, screenCoords.x -arrowR.getWidth()/2, screenCoords.y -arrowR.getHeight()/2, paint);

					
			   		if(!cache){
			   			
			   			CoordinateUTM utm = CoordConverter.getInstance().toUTM(new CoordinateLatLon(geo.getLatitudeE6()/1E6,geo.getLongitudeE6()/1E6));					
			   			infoUTM.setText(UTMDisplay.convertUTM(utm.getShortForm(),infoPrec,false));
			   			handler.sendEmptyMessage(0);
			   			lastPoint=geo;

						getLastAltitude();
			   			
			   			
			   		}
					
					
		 		}			               

			}
			
			
		}
		
		private float calculateBearing(Point pBefore, Point pAfter) {

			float res = -(float) (Math.atan2(pAfter.y - pBefore.y, pAfter.x - pBefore.x) * 180 / 3.1416) + 90.0f;
						
			if (res < 0)
				return res + 360.0f;
			else
				return res;
		}


		public void setInfoUTM(TextView infoUTM) {
			this.infoUTM = infoUTM;
						
		}


		public void setInfoPrec(String infoPrec) {
			this.infoPrec = infoPrec;
		}


		public GeoPoint getLastPoint() {
			return lastPoint;
		}


		public double getElevation() {
			return elevation;
		}
		
	

}
