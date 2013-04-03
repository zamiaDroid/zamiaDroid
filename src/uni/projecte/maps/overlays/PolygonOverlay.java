package uni.projecte.maps.overlays;

import java.util.ArrayList;

import uni.projecte.maps.utils.LatLon;
import uni.projecte.maps.utils.LatLonParcel;
import uni.projecte.maps.utils.LatLonPoint;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;
import com.google.android.maps.Projection;

public class PolygonOverlay extends Overlay {

    private ArrayList<LatLon> route;
    private MapView mapView;
    private Point firstPoint;
    

    /*public PolygonOverlay(ArrayList<ParcelableGeoPoint> r) {
            route = new ArrayList<GeoPoint>();
            
            for (ParcelableGeoPoint p: r) {
                    route.add(p.getGeoPoint());
            }
    }*/
    
    
    /*public PolygonOverlay(ArrayList<GeoPoint> route){
    	
    	this.route=route;
    	
    }*/

 
	public PolygonOverlay(MapView mapView, ArrayList<LatLon> polygonPath) {

		this.route=polygonPath;
		this.mapView=mapView;
	
		//centrer to lastPoint
		if(route.size()>0) mapView.getController().animateTo(new LatLonPoint(route.get(route.size()-1)));
		
	}

	public void draw(Canvas canvas, MapView mapv, boolean shadow) {
            super.draw(canvas, mapv, shadow);

            
          if(route!=null && route.size()>0){  
            
            
	            Paint mPaint = new Paint();
	            mPaint.setDither(true);
	            mPaint.setAlpha(100);
	            mPaint.setStyle(Paint.Style.FILL_AND_STROKE);
	            mPaint.setStrokeJoin(Paint.Join.ROUND);
	            mPaint.setStrokeCap(Paint.Cap.ROUND);
	            mPaint.setStrokeWidth(5);
	
	            Path path = new Path();
	
	            GeoPoint start = new LatLonPoint(route.get(0));
	            firstPoint = new Point();
	
	            Projection projection = mapv.getProjection();
	            projection.toPixels(start, firstPoint);
	
	            mPaint.setColor(Color.rgb(208, 221, 154));
	            
	            for (int i = 1; i < route.size(); ++i) {
	                    
	            		Point p1 = new Point();
	                    Point p2 = new Point();
	
	                    projection.toPixels(start, p1);
	                    projection.toPixels(new LatLonPoint(route.get(i)), p2);
	
	                    path.moveTo(p2.x, p2.y);
	                    path.lineTo(p1.x, p1.y);
	
	                    canvas.drawCircle((float)p2.x, (float)p2.y, (float) 4.5, mPaint);
	                    
	                    start = new LatLonPoint(route.get(i));
	            }
	
	            mPaint.setAntiAlias(true);
	            
	            mPaint.setColor(Color.rgb(208, 221, 154));
	
	            canvas.drawPath(path, mPaint);
	            
	            //drawing firstPoint
	            mPaint.setColor(Color.rgb(136, 170, 0));
	            canvas.drawCircle((float)firstPoint.x, (float)firstPoint.y, (float) 4.5, mPaint);
	            
	            
          }
    }
}