package uni.projecte.maps.utils;

import com.google.android.maps.GeoPoint;

public final class LatLonPoint extends GeoPoint {
	
    public LatLonPoint(double latitude, double longitude) {
        super((int) (latitude * 1E6), (int) (longitude * 1E6));
    }
    
    public LatLonPoint(LatLon latLon) {
        super((int) (latLon.latitude * 1E6), (int) (latLon.longitude * 1E6));

    }
    
}

