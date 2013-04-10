package uni.projecte.maps.overlays;

import java.util.ArrayList;
import java.util.Iterator;

import uni.projecte.maps.utils.LatLon;
import uni.projecte.maps.utils.LatLonPoint;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapView;
import com.google.android.maps.Projection;

public class PolygonLayerOverlay extends PolygonOverlay{

	private ArrayList<ArrayList<LatLon>> polygon_list;  


	
	/*public PolygonLayerOverlay(MapView mapView, ArrayList<LatLon> polygonPath) {
		
		super(mapView, polygonPath);
		
	}*/
	
	
	public PolygonLayerOverlay(MapView mapView, ArrayList<ArrayList<LatLon>> polygon_list){
		
		super(mapView, polygon_list.get(0));

		this.polygon_list=polygon_list;
		
		
	}
	
	public void draw(Canvas canvas, MapView mapv, boolean shadow) {
		
		for(ArrayList<LatLon> polygon : polygon_list ){

			drawPolygon(canvas, mapv,polygon);

		}
	}
	

}
