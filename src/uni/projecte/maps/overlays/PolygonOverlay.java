package uni.projecte.maps.overlays;

import java.util.ArrayList;

import uni.projecte.maps.utils.LatLon;
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

	//protected int baseColor = Color.rgb(208, 221, 154);
	//protected int emphasisColor = Color.rgb(136, 170, 0);

	public PolygonOverlay(MapView mapView, ArrayList<LatLon> polygonPath) {

		this.route = polygonPath;
		this.mapView = mapView;

		// centrer to lastPoint
		if (route != null && route.size() > 0)
			mapView.getController().animateTo(
					new LatLonPoint(route.get(route.size() - 1)));

	}

	public void draw(Canvas canvas, MapView mapv, boolean shadow) {

		super.draw(canvas, mapv, shadow);

		if (route != null && route.size() > 0) {

			drawPolygon(canvas, mapv,route,isPolygon(route));

		}
	}
	
	protected boolean isPolygon(ArrayList<LatLon> route){
		
		return route.get(0).equals(route.get(route.size()-1));
		
	}

	protected void drawPolygon(Canvas canvas, MapView mapv,ArrayList<LatLon> route, boolean closed) {

		
		Paint verPaint = new Paint();
		verPaint.setColor(Color.rgb(208, 221, 154));
		verPaint.setStrokeWidth(4);
		verPaint.setStyle(Paint.Style.FILL_AND_STROKE);
		verPaint.setStrokeJoin(Paint.Join.ROUND);
		verPaint.setStrokeCap(Paint.Cap.ROUND);

		Path path = new Path();
	    path.setFillType(Path.FillType.EVEN_ODD);
	    
	    
		GeoPoint start = new LatLonPoint(route.get(0));
		firstPoint = new Point();

		Projection projection = mapv.getProjection();
		projection.toPixels(start, firstPoint);

		path.moveTo(firstPoint.x,firstPoint.y);
		path.lineTo(firstPoint.x, firstPoint.y);

		for (int i = 1; i < route.size(); ++i) {

			Point p1 = new Point();
			Point p2 = new Point();

			projection.toPixels(start, p1);
			projection.toPixels(new LatLonPoint(route.get(i)), p2);

			path.lineTo(p2.x, p2.y);

			if(!closed) canvas.drawCircle((float) p2.x, (float) p2.y, (float) 4.5, verPaint);

			start = new LatLonPoint(route.get(i));
		}

		
		Paint mPaint = new Paint();
		
		if(closed){
			
			path.close();
			
			mPaint.setColor(Color.rgb(208, 221, 154));
			mPaint.setAlpha(120);
			
			mPaint.setStyle(Paint.Style.FILL);
			mPaint.setAntiAlias(true);
			
			canvas.drawPath(path, mPaint);
		
		}
		
		mPaint.setColor(Color.rgb(136, 170, 0));
		mPaint.setStyle(Paint.Style.STROKE);
		mPaint.setStrokeWidth(3);

		canvas.drawPath(path, mPaint);

		// drawing firstPoint
		verPaint.setColor(Color.rgb(136, 170, 0));
		canvas.drawCircle((float) firstPoint.x, (float) firstPoint.y,(float) 4.5, verPaint);


	}

}