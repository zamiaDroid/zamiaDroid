package uni.projecte.maps.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapView;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.util.Log;

public class UTM {
			
		private static String catalunyaUTM="utm_catalunya.tab";
		private static String ppccUTM="utm_ppcc.tab";

		private String utmCatalunya;
		private String utmPPCC;
		
		private Context baseContext;
		
		public UTM(Context baseContext){
			
			this.baseContext=baseContext;
			
		}
		
		
		public int checkUTM(Context baseContext,String utm,String country){
			
			try {
				
				InputStream is= baseContext.getResources().getAssets().open(catalunyaUTM);
				utmCatalunya=convertStreamToString(is);
				
				if(utmCatalunya.contains(utm)) return 1;
								
					is= baseContext.getResources().getAssets().open(ppccUTM);
					utmPPCC=convertStreamToString(is);
				
				}			 
			
			catch (IOException e) {
				
				e.printStackTrace();
			}
				
				if(utmPPCC.contains(utm)) return 2;
				
				else{
					
					if(country.equals("ES")|| country.equals("PT") || country.equals("FR") || country.equals("MA")) return 3;
					else return 4;
				}

			
		}
		
		   private static String convertStreamToString(InputStream is) {
		        /*
		         * To convert the InputStream to String we use the BufferedReader.readLine()
		         * method. We iterate until the BufferedReader return null which means
		         * there's no more data to read. Each line will appended to a StringBuilder
		         * and returned as String.
		         */
		        BufferedReader reader;
				StringBuilder sb = new StringBuilder();

				try {
					reader = new BufferedReader(new InputStreamReader(is,"UTF-8"));
				
		 	        String line = null;
			
		            while ((line = reader.readLine()) != null) {
		                sb.append(line + "\n");
		            }
		            
		        } catch (IOException e) {
		            e.printStackTrace();
		        }finally {
		            try {
		                is.close();
		            } catch (IOException e) {
		                e.printStackTrace();
		            }
		         	        }
		        return sb.toString();
		    }

		   
		 
		   public void drawUTMSquare(Canvas canvas,Point aP,Point bP, Point cP, Point dP){
			   
			    Paint paint=new Paint();
				paint.setColor(Color.WHITE);
				paint.setAntiAlias(true);
		        paint.setStrokeWidth(4);
		        paint.setStrokeCap(Paint.Cap.ROUND);
		        paint.setStyle(Paint.Style.STROKE);
							
				canvas.drawLine(aP.x, aP.y, bP.x, bP.y, paint);
				paint.setColor(Color.YELLOW);

				canvas.drawLine(bP.x, bP.y, cP.x, cP.y, paint);
				paint.setColor(Color.RED);

				canvas.drawLine(cP.x, cP.y, dP.x, dP.y, paint);		
				paint.setColor(Color.GREEN);

				canvas.drawLine(dP.x, dP.y, aP.x, aP.y, paint);
			   
			   
			   
		   }
		   
		   
		public void drawUTMGrid(Canvas canvas, MapView mapView) {

			
			InputStream is;

					try {
						
						is = baseContext.getResources().getAssets().open("utmIberic.json");

						 BufferedReader reader;
						
						reader = new BufferedReader(new InputStreamReader(is,"UTF-8"));
					
			 	        String line = null;
			 	        String utm;
				
			            while ((line = reader.readLine()) != null) {
			               
			            	utm=line;
			            			            	
			            	line = reader.readLine();
			            	
			            	
			            	if(line.equals("")){
			            		
				            	line = reader.readLine();
			            		
			            	}
			            	
			            	String [] elements=line.split(" ");
			            	
			            	if(elements.length>1){
			            	
				            	String [] subElement=elements[0].split(",");
				            	
				            	Log.i("Error","L/L: "+subElement[0]+" : "+utm);
				            	
				            	double lat=Double.valueOf(subElement[0]);
				                double longi=Double.valueOf(subElement[1]);
				            	
				    			GeoPoint a=new GeoPoint((int) (lat * 1E6), (int) (longi * 1E6));
	
				            	Point aP = new Point();
				    			mapView.getProjection().toPixels(a, aP);
				    			
				    			subElement=elements[1].split(",");
				            	
				    		  	lat=Double.valueOf(subElement[0]);
				                longi=Double.valueOf(subElement[1]);
				            	
				    			GeoPoint b=new GeoPoint((int) (lat * 1E6), (int) (longi * 1E6));
				    			
				    		  	Point bP = new Point();	  	
				    			mapView.getProjection().toPixels(b, bP);
				    		
				    			
				    			subElement=elements[2].split(",");
	
				    		  	lat=Double.valueOf(subElement[0]);
				                longi=Double.valueOf(subElement[1]);
				            	
				    			GeoPoint c=new GeoPoint((int) (lat * 1E6), (int) (longi * 1E6));
				    			
				    		  	Point cP = new Point();
				    			mapView.getProjection().toPixels(c, cP);
				    			
				    			subElement=elements[3].split(",");
	
				    		  	lat=Double.valueOf(subElement[0]);
				                longi=Double.valueOf(subElement[1]);
				            	
				    			GeoPoint d=new GeoPoint((int) (lat * 1E6), (int) (longi * 1E6));
				    			
				    			Point dP = new Point();
				    			mapView.getProjection().toPixels(d, dP);
				    			
				    			drawUTMSquare(canvas, aP, bP, cP, dP);
			    			
			            	}
			            	
			            	break;
			            	
			            }
			            
			        } catch (IOException e) {
			            e.printStackTrace();
			        }
			
		}
		

}
