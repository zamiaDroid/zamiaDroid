package uni.projecte.maps.overlays;

import java.util.ArrayList;
import uni.projecte.maps.utils.LatLon;
import android.graphics.Canvas;
import com.google.android.maps.MapView;


public class PolygonLayerOverlay extends PolygonOverlay{

	private ArrayList<ArrayList<LatLon>> polygon_list;  


	public PolygonLayerOverlay(MapView mapView, ArrayList<ArrayList<LatLon>> polygon_list){
		
		super(mapView, null);

		this.polygon_list=polygon_list;
		
		
	}
	
	public void draw(Canvas canvas, MapView mapv, boolean shadow) {
		
		for(ArrayList<LatLon> polygon : polygon_list ){

			if (polygon != null && polygon.size() > 0) {	
			
				drawPolygon(canvas, mapv,polygon,isPolygon(polygon));
			
			}

		}
	}
	
	public boolean isEmpty(){
		
		return polygon_list==null || polygon_list.size()<1;
		
		
	}
	

}
