package uni.projecte.maps;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapView;

public class MapDrawUtils {
	
	private static String units="m";
	
	
	  public static void drawUTMCross(Canvas canvas, GeoPoint center,String label,MapView mapView) {

		 	Paint paint=new Paint();
			paint.setAntiAlias(true);
	        paint.setStrokeWidth(4);
	        paint.setStrokeCap(Paint.Cap.ROUND);
	        paint.setStyle(Paint.Style.STROKE);
	       
	        
	        Point centerP = new Point();	  	
	        mapView.getProjection().toPixels(center, centerP);
		
			paint.setColor(Color.WHITE);
	        
			canvas.drawLine(centerP.x-10, centerP.y, centerP.x+10, centerP.y, paint);

			canvas.drawLine(centerP.x, centerP.y+10, centerP.x, centerP.y-10, paint);

			if(!label.equals("")){
				
				paint.setStyle(Paint.Style.FILL);
				paint.setAntiAlias(true);
				paint.setTextSize(18);				
				canvas.drawText(label, (centerP.x) +10, centerP.y-2, paint);
				
			}   
	}
	  
	  /*
	   * Really "cutre" method that draw a scale ruler on the bottom-right part
	   * of the screen 
	   * 
	   */
	  
	  public static void drawMapZoom(Canvas canvas,MapView mapView, int screenWidth, int screenHeight) {

	    	Paint paint=new Paint();
			paint.setColor(Color.WHITE);
			paint.setAntiAlias(true);
			paint.setTextSize((float) 16.0);
			paint.setStrokeCap(Paint.Cap.ROUND);
			paint.setStyle(Paint.Style.STROKE);
			
			String scaleValue="";
			
			Double pot=25*(Math.pow(2,(20-mapView.getZoomLevel())));
         
			if(pot>1000) {
			
				units="km";
				pot=pot/1000;
				
				scaleValue=pot.toString();
          
			}
			else{
				
				units="m";
				
				if(pot>=25.0) scaleValue=pot.intValue()+"";
				else scaleValue=pot.toString();

			}

			canvas.drawText(scaleValue+" "+units+" ZL: "+mapView.getZoomLevel(), screenWidth-120,screenHeight-65, paint);
			
				paint.setColor(Color.WHITE);
				paint.setStrokeWidth(5);
				paint.setStyle(Paint.Style.FILL);
             
			canvas.drawLine(screenWidth-20, screenHeight-60, screenWidth-120, screenHeight-60, paint);

				paint.setColor(Color.BLACK);
				canvas.drawLine(screenWidth-120, screenHeight-60,screenWidth-95, screenHeight-60,  paint);
				canvas.drawLine(screenWidth-70, screenHeight-60,screenWidth-45, screenHeight-60,  paint);

	    	
		}

}
