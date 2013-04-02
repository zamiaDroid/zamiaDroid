package uni.projecte.maps.utils;

import uni.projecte.maps.UTMGrid;
import edu.ub.bio.biogeolib.CoordConverter;
import edu.ub.bio.biogeolib.CoordinateLatLon;
import edu.ub.bio.biogeolib.CoordinateUTM;

public class UTMUtils {
	
	public static final int NORTH = 1;
	public static final int SOUTH = 2;
	
	
	
	public static UTMGrid getUTMBoundingBox(CoordinateUTM utm, boolean hemisNorth, double accuracy){
		
		double xMinus=utm.getX()%10000;
		double yMinus=utm.getY()%10000;
		
		CoordinateUTM utmBox=new CoordinateUTM(hemisNorth,utm.getZone(),utm.getX()-xMinus,utm.getY()-yMinus,accuracy);
		
		/*
         * 	UpperLeft --> 	c******d
         * 	(lat/long)	 	******** 
         * 				 	********
         * 				 	b******a 	<--- LowerRight (lat,long)
         *  
         */
	   
	   
        CoordinateUTM utmLR = new CoordinateUTM(hemisNorth,utmBox.getZone(), utmBox.getX() + utmBox.getAccuracy(), utmBox.getY());

        CoordinateUTM utmLL = new CoordinateUTM(hemisNorth,utmBox.getZone(), utmBox.getX(), utmBox.getY());

        CoordinateUTM utmUL = new CoordinateUTM(hemisNorth,utmBox.getZone(), utmBox.getX(), utmBox.getY() + accuracy);
        
        CoordinateUTM utmUR = new CoordinateUTM(hemisNorth,utmBox.getZone(), utmBox.getX() + utmBox.getAccuracy(), utmBox.getY()+ accuracy);

        CoordConverter conv=CoordConverter.getInstance();
        
        
   		CoordinateLatLon lowerRight=conv.toLatLon(utmLR);
   		CoordinateLatLon lowerLeft=conv.toLatLon(utmLL);
   		CoordinateLatLon upperLeft=conv.toLatLon(utmUL);
   		CoordinateLatLon upperRight=conv.toLatLon(utmUR);

        	        
        return new UTMGrid(lowerRight,lowerLeft,upperLeft,upperRight);
		
		
		
	}
	
	public static int getHemis(boolean north){
		
		if(north) return NORTH;
		else return SOUTH;
		
		
	}
	
	

}
