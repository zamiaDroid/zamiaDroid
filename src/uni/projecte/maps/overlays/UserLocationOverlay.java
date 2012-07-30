package uni.projecte.maps.overlays;

import uni.projecte.R;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.location.Location;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapView;


	/* Class overload draw method which actually plot a marker with the user current location on Map */
	public class UserLocationOverlay extends com.google.android.maps.MyLocationOverlay {
		
		private Context context;
		private GeoPoint current;
		

		public UserLocationOverlay(Context context, MapView mapView,GeoPoint current) {
			super(context, mapView);
	
			this.context=context;
			this.current=current;

		}
		
	
		@Override
		public boolean draw(Canvas canvas, MapView mapView, boolean shadow, long when) {
			
			super.draw(canvas, mapView, shadow);
			
			Paint paint = new Paint();
						
			// Converts lat/lng-Point to OUR coordinates on the screen.
			Point myScreenCoords = new Point();
			
			if(current!=null){
			
				mapView.getProjection().toPixels(current, myScreenCoords);
				
				paint.setStrokeWidth(1);
				paint.setARGB(255, 255, 255, 255);
				paint.setStyle(Paint.Style.STROKE);
	
				Bitmap bmp = BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_maps_indicator_verd);
				canvas.drawBitmap(bmp, myScreenCoords.x-bmp.getWidth()/2, myScreenCoords.y-bmp.getHeight()/2, paint);
	        
			}
			
			
			return true;
		}
		
	
	
		public Point location2Point(Location aLocation){
			return new Point((int) (aLocation.getLongitude() * 1E6),
							(int) (aLocation.getLatitude() * 1E6));
		}


		public void setCurrent(GeoPoint current) {
			this.current = current;
		}



		
	}
