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


package uni.projecte.maps.overlays;

import uni.projecte.maps.MapDrawUtils;
import uni.projecte.maps.UTMDisplay;
import uni.projecte.maps.UTMGrid;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.util.Log;
import android.widget.TextView;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;

import edu.ub.bio.biogeolib.CoordConverter;
import edu.ub.bio.biogeolib.CoordinateLatLon;
import edu.ub.bio.biogeolib.CoordinateUTM;


/*
 * This class provides us the utmGrid that will allow 
 * us to connect with the biodiversity data banks 
 *  
 */

public class UTMOverlay extends Overlay {
	
	private MapView mapView;
	private TextView utmTv;
	private String utmPrec;

	
	
	public UTMOverlay(MapView mapView,String utmPrec){
		
		this.mapView=mapView;
		this.utmPrec=utmPrec;
		
	}
	
	public UTMOverlay(MapView mapView,TextView utmTv,String utmPrec){
		
		this.mapView=mapView;
		this.utmTv=utmTv;
		this.utmPrec=utmPrec;

	}
	
	
	@Override
	public void draw(Canvas canvas, MapView mapView, boolean shadow) {

   		GeoPoint center=mapView.getMapCenter();
   		
   		CoordinateUTM utm = CoordConverter.getInstance().toUTM(new CoordinateLatLon(center.getLatitudeE6()/1E6,center.getLongitudeE6()/1E6));
   		String utmShort=utm.getShortForm();	

   		/* determining if we are at northern hemisphere*/
   		boolean hemisNorth=center.getLatitudeE6()/1E6>=0;
   		
   		
   		/* Drawing 10km UTM square */ 
   		if(utmPrec.equals("10km")){
   		
			CoordinateUTM utmBox = calculateUTMCenter(utm, 10000,hemisNorth);	
			UTMGrid grid=utmToLatLonBox(utmBox, 10000,hemisNorth);
			
	   		/* determining if we are on a UTM overlapping zone */			
			isZoneLimit(utmBox,canvas,mapView,grid,hemisNorth);
			
			drawCalculatedUTMSquare(canvas,grid,false,UTMDisplay.convertUTM(utmShort,"10km",false),Color.WHITE);

   		}

   		/* Drawing 1km UTM square */ 		
   		if(utmPrec.equals("1km") || utmPrec.equals("1m") ){
			
   			CoordinateUTM utmBox1x1 = calculateUTMCenter(utm, 1000,hemisNorth);
	   		UTMGrid grid1x1=utmToLatLonBox(utmBox1x1, 1000,hemisNorth);  	
	   		
	   		/* determining if we are on a UTM overlapping zone */
			isZoneLimit(utmBox1x1,canvas,mapView,grid1x1,hemisNorth);
			
			drawCalculatedUTMSquare(canvas, grid1x1,true,UTMDisplay.convertUTM(utmShort,"1km",false),Color.RED);
		
   		}

   		
		/*  when square "doesn\'t fits" on screen we draw a cross at the center of the screen"*/	
		if(mapView.getZoomLevel()>13) MapDrawUtils.drawUTMCross(canvas, center,"",mapView);
		
		
		/* TextView is updated with UTM */
		if(utmTv!=null){
			
			utmTv.setText(UTMDisplay.convertUTM(utmShort,utmPrec,false));
		}
		

	}
	
	private boolean isZoneLimit(CoordinateUTM utmBox,Canvas canvas, MapView mapView, UTMGrid grid, boolean hemisNorth) {
		
		int acur=(int) utmBox.getAccuracy();

	/* Determining if Right UTM is from another zone */
		CoordinateUTM utmR=new CoordinateUTM(hemisNorth,utmBox.getZone(),utmBox.getX()+acur+1,utmBox.getY()+(acur/2),utmBox.getAccuracy());
		CoordinateLatLon latLong4=CoordConverter.getInstance().toLatLon(utmR);	
        CoordinateUTM utmRight=CoordConverter.getInstance().toUTM(latLong4);

        
    /* Determining if Left UTM is from another zone */	
		CoordinateUTM utmL=new CoordinateUTM(hemisNorth,utmBox.getZone(),utmBox.getX()-1,utmBox.getY()+(acur/2),utmBox.getAccuracy());
		CoordinateLatLon latLong5=CoordConverter.getInstance().toLatLon(utmL);
        CoordinateUTM utmLeft=CoordConverter.getInstance().toUTM(latLong5);

        	
		 if(utmBox.getZone()!=utmRight.getZone()){
			 
			calculateUTMOverlap(utmBox,canvas,mapView,grid,acur/40,true,acur,hemisNorth);
		 
			return true;
		 
		 }
		 else if(utmBox.getZone()!=utmLeft.getZone()){
			 
			 calculateUTMOverlap(utmBox,canvas,mapView,grid,acur/40,false,acur,hemisNorth);

			 return true;
			 
			 
		 }
		 else return false;
		

	}
	
	
	/*
	 * We know that the @utmBox is overlapped by another UTM from another zone from the @right {right=false -> left} zone
	 * We are calculating the overlapped UTM increasing the distance determined by @nextDistance from the original zone 
	 *  
	 */
	
	private void calculateUTMOverlap(CoordinateUTM utmBox,Canvas canvas, MapView mapView, UTMGrid grid2, int nextDistance,boolean right, int acur,boolean hemisNorth) {

		
		/* Right Square */
		
			CoordinateUTM utmR=new CoordinateUTM(hemisNorth,utmBox.getZone(),utmBox.getX()+nextDistance,utmBox.getY()+(acur/2),utmBox.getAccuracy());
			CoordinateLatLon latLong=CoordConverter.getInstance().toLatLon(utmR);	
			CoordinateUTM utmRight=CoordConverter.getInstance().toUTM(latLong);

		/* Left Square */	
			CoordinateUTM utmL=new CoordinateUTM(hemisNorth,utmBox.getZone(),utmBox.getX()-(nextDistance-acur),utmBox.getY()+(acur/2),utmBox.getAccuracy());
			CoordinateLatLon latLong2=CoordConverter.getInstance().toLatLon(utmL);
			CoordinateUTM utmLeft=CoordConverter.getInstance().toUTM(latLong2);

						        
	        if(right){
	        
	        	//still in the same UTM
	        	if(utmBox.getZone()==utmRight.getZone()){
					
					nextDistance=nextDistance+acur/40;
					calculateUTMOverlap(utmBox, canvas, mapView, grid2,nextDistance,right,acur,hemisNorth);
					
				}
	        	else{	        	
			        
	        		utmRight=calculateUTMCenter(utmRight, acur,hemisNorth);
					UTMGrid grid=utmToLatLonBox(utmRight, acur,hemisNorth);
					
					double distance=distanceBetweenLatLongPoints(grid.upperRight, grid2.upperLeft);
		
					CoordinateUTM topNewModif=new CoordinateUTM(hemisNorth,utmBox.getZone(), (utmBox.getX()+distance*500), utmBox.getY(),utmBox.getAccuracy());
					UTMGrid topGrid=utmToLatLonBox(topNewModif, acur,hemisNorth);
						
		
					//Modifying the bounding box using the intersection in the middle between the overlapping UTM
					grid2.upperRight=topGrid.upperLeft;
					grid2.lowerRight=topGrid.lowerLeft;
					
					
	        	}
				
	        }
	        
	        
	        else {
	        	
	        	//still in the same UTM
	        	if(utmBox.getZone()==utmLeft.getZone()){
					
					nextDistance=nextDistance+acur/40;
					calculateUTMOverlap(utmBox, canvas, mapView, grid2,nextDistance,right,acur,hemisNorth);
					
				}
	        	else{
		       	
			        utmLeft=calculateUTMCenter(utmLeft, acur,hemisNorth);
					UTMGrid grid=utmToLatLonBox(utmLeft, acur,hemisNorth);
					
					double distance=distanceBetweenLatLongPoints(grid.upperLeft, grid2.upperRight);
										
					CoordinateUTM topNewModif=new CoordinateUTM(hemisNorth,utmBox.getZone(), (utmBox.getX()-distance*500), utmBox.getY(),utmBox.getAccuracy());
					UTMGrid topGrid=utmToLatLonBox(topNewModif, acur,hemisNorth);

						
					//Modifying the bounding box using the intersection in the middle between the overlapping UTM
					grid2.upperLeft=topGrid.upperRight;
					grid2.lowerLeft=topGrid.lowerRight;
					
						
	        	}
			
	       }
		
		


	}
	
	
	
	@SuppressWarnings("unused")
	private void drawLine(Canvas canvas, MapView mapView, int color,CoordinateLatLon upperRight, CoordinateLatLon upperLeft, String string) {
		

		
		 	Paint mPaint = new Paint();
	        mPaint.setDither(true);
	        mPaint.setColor(color);
	        mPaint.setStyle(Paint.Style.STROKE);
	        mPaint.setStrokeWidth(4);

	        GeoPoint gP1 = new GeoPoint((int) (upperRight.getLat()*1E6),(int) (upperRight.getLon()*1E6));
	        
	        GeoPoint gP2 = new GeoPoint((int)(upperLeft.getLat()*1E6),(int) (upperLeft.getLon()*1E6));

	        Point p1 = new Point();
	        Point p2 = new Point();

	        mapView.getProjection().toPixels(gP1, p1);
	        mapView.getProjection().toPixels(gP2, p2);


	        canvas.drawLine(p1.x, p1.y, p2.x, p2.y, mPaint);
	        canvas.drawLine(p1.x, p1.y+5, p1.x, p1.y-5, mPaint);
	        
	    	mPaint.setStyle(Paint.Style.FILL);
			mPaint.setAntiAlias(true);
			mPaint.setTextSize(18);				
			canvas.drawText(string, p1.x,  p1.y-4, mPaint);
		
	}
	
	

	public double distanceBetweenLatLongPoints(CoordinateLatLon x, CoordinateLatLon y){
		
		double lat1=x.getLat();
		double lon1=x.getLon();
		
		double lat2=y.getLat();
		double lon2=y.getLon();
		
		int R = 6371; // km
		double dLat = Math.toRadians(lat2-lat1);
		double dLon = Math.toRadians(lon2-lon1);
		lat1 = Math.toRadians(lat1);
		lat2 = Math.toRadians(lat2);

		double a = Math.sin(dLat/2) * Math.sin(dLat/2) +
		        Math.sin(dLon/2) * Math.sin(dLon/2) * Math.cos(lat1) * Math.cos(lat2); 
		
		double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a)); 
		double d = R * c;
		
		return d;
		
		
	}

	public CoordinateUTM calculateUTMCenter(CoordinateUTM utm, double prec, boolean hemisNorth){
		
		double xMinus=utm.getX()%prec;
		double yMinus=utm.getY()%prec;
		
		CoordinateUTM utmBox=new CoordinateUTM(hemisNorth,utm.getZone(),utm.getX()-xMinus,utm.getY()-yMinus,prec);

		return utmBox;
		
	} 


	public float calculateBearing(Point pBefore, Point pAfter) {

		float res = -(float) (Math.atan2(pAfter.y - pBefore.y, pAfter.x - pBefore.x) * 180 / 3.1416) + 90.0f;
		
		Log.d("MapView", "Bearing: " + res);
		
		if (res < 0)
			return res + 360.0f;
		else
			return res;
		
		
	}

	private void drawCalculatedUTMSquare(Canvas canvas, UTMGrid box, boolean prec1x1, String label, int color) {

		   Paint paint=new Paint();
			paint.setAntiAlias(true);
	        paint.setStrokeWidth(4);
	        paint.setStrokeCap(Paint.Cap.ROUND);
	        paint.setStyle(Paint.Style.STROKE);
		
	    	paint.setColor(color);
	    	
	    	if(prec1x1) paint.setColor(Color.GREEN);
	        paint.setAlpha(128);
	        
	        /*
	         * UpperLeft --> c******d
	         * (lat/long)	 ******** 
	         * 				 ********
	         * 				 b******a <--- LowerRight (lat,long)
	         *  
	         */
	        
	        
	        GeoPoint a=new GeoPoint((int) (box.lowerRight.getLat() * 1E6), (int) (box.lowerRight.getLon() * 1E6));
			GeoPoint c=new GeoPoint((int) (box.upperLeft.getLat() * 1E6), (int) (box.upperLeft.getLon() * 1E6));

			
		    GeoPoint b=new GeoPoint((int) (box.lowerLeft.getLat() * 1E6), (int) (box.lowerLeft.getLon() * 1E6));
		    GeoPoint d=new GeoPoint((int) (box.upperRight.getLat() * 1E6), (int) (box.upperRight.getLon() * 1E6));
		    
	        
	    	if(!prec1x1) drawUTMSquare(canvas, a, b, c, d, label, color);
	    	else drawUTMSquare(canvas, a, b, c, d, label, Color.GREEN);
		
	}
	
	   public UTMGrid utmToLatLonBox(CoordinateUTM utm, double accuracy, boolean hemisNorth) {
		   
		   /*
	         * UpperLeft --> c******d
	         * (lat/long)	 ******** 
	         * 				 ********
	         * 				 b******a <--- LowerRight (lat,long)
	         *  
	         */
		   
		   
	        CoordinateUTM utmLR = new CoordinateUTM(hemisNorth,utm.getZone(), utm.getX() + utm.getAccuracy(), utm.getY());

	        CoordinateUTM utmLL = new CoordinateUTM(hemisNorth,utm.getZone(), utm.getX(), utm.getY());

	        CoordinateUTM utmUL = new CoordinateUTM(hemisNorth,utm.getZone(), utm.getX(), utm.getY() + accuracy);
	        
	        CoordinateUTM utmUR = new CoordinateUTM(hemisNorth,utm.getZone(), utm.getX() + utm.getAccuracy(), utm.getY()+ accuracy);

	        CoordConverter conv=CoordConverter.getInstance();
	        
	   		CoordinateLatLon lowerRight=conv.toLatLon(utmLR);
	   		CoordinateLatLon lowerLeft=conv.toLatLon(utmLL);
	   		CoordinateLatLon upperLeft=conv.toLatLon(utmUL);
	   		CoordinateLatLon upperRight=conv.toLatLon(utmUR);

	        	        
	        return new UTMGrid(lowerRight,lowerLeft,upperLeft,upperRight);
	        
	        
	    }


	private void drawUTMSquare(Canvas canvas,GeoPoint a,GeoPoint b, GeoPoint c, GeoPoint d, String utm,int color){
		   
		    Paint paint=new Paint();
			paint.setAntiAlias(true);
	        paint.setStrokeWidth(4);
	        paint.setStrokeCap(Paint.Cap.ROUND);
	        paint.setStyle(Paint.Style.STROKE);
	        
	      	Point aP = new Point();	  	
			mapView.getProjection().toPixels(a, aP);
			
	      	Point bP = new Point();	  	
			mapView.getProjection().toPixels(b, bP);
			
	      	Point cP = new Point();	  	
			mapView.getProjection().toPixels(c, cP);
			    			
	      	Point dP = new Point();	  	
			mapView.getProjection().toPixels(d, dP);
				
			paint.setColor(color);
	        paint.setAlpha(128);

			
				canvas.drawLine(aP.x, aP.y, bP.x, bP.y, paint);
				canvas.drawLine(bP.x, bP.y, cP.x, cP.y, paint);
				canvas.drawLine(cP.x, cP.y, dP.x, dP.y, paint);		
				canvas.drawLine(dP.x, dP.y, aP.x, aP.y, paint);
		   
	
			paint.setStyle(Paint.Style.FILL);
			paint.setAntiAlias(true);
			paint.setTextSize(18);				
			canvas.drawText(utm, aP.x +10, aP.y-15, paint);
		

			
	   }

	public void setUtmPrec(String utmPrec) {
		this.utmPrec = utmPrec;
	}
	

	
}
