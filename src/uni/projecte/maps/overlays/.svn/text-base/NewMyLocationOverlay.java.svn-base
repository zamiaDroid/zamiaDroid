package uni.projecte.maps.overlays;

import uni.projecte.R;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import com.google.android.maps.MapView;
import com.google.android.maps.MyLocationOverlay;

public class NewMyLocationOverlay extends MyLocationOverlay {

	private Context context;
	private Bitmap canvasBitmap;
	private Paint paint;
	
	public NewMyLocationOverlay(Context context, MapView mapView) {
		
		super(context, mapView);
		
		this.context=context;
		
	    paint = new Paint();
        paint.setAntiAlias(true);
        paint.setColor(Color.BLACK);
        paint.setStyle(Paint.Style.FILL);


	
	}
	
	@Override
	protected void drawCompass(Canvas canvas, float bearing) {

	   float rotationAngle = -bearing + 360f;/*
	    
		Bitmap arrow = BitmapFactory.decodeResource(context.getResources(), R.drawable.arrow);
		
		Matrix matrix = new Matrix();
        matrix.postRotate(rotationAngle);
        

        Bitmap arrowR = Bitmap.createBitmap(arrow, 0, 0, arrow.getWidth(), arrow.getHeight(), matrix, true);
		canvas.drawBitmap(arrowR, 200, 200, null);*/


	    // don't call super if you don't want the default compass image:
	    //super.drawCompass(canvas, bearing);
		
		 //  Display display = ((Activity) context).getWindowManager().getDefaultDisplay(); 

		
		   int w = 200;
	        int h = 200;
	        int cx = w / 2;
	        int cy = h / 2;
	         
	        if(canvasBitmap == null) {
	        	
	         canvasBitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.compass_arrow);
	         
	        }
	 
	        canvas.save();
	        
	        canvas.translate(cx, cy);
	     // rotate to be aligned with the true north
	        canvas.rotate(rotationAngle);
	        float maxwidth = (float) (canvasBitmap.getWidth() * Math.sqrt(2));
	        float maxheight = (float) (canvasBitmap.getHeight() * Math.sqrt(2));
	        float ratio = 1f;
	        int width = (int) (canvasBitmap.getWidth() * ratio);
	        int height = (int) (canvasBitmap.getHeight() * ratio);
	        // draw the compass
	        canvas.drawBitmap(canvasBitmap, new Rect(0, 0, canvasBitmap.getWidth(), canvasBitmap.getHeight()), new Rect(- width / 2, - height/2, width / 2, height / 2), paint);
	     
	        canvas.restore();
		

	}

	

}
