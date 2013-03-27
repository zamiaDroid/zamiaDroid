package uni.projecte.maps.utils;

import android.os.Parcel;
import android.os.Parcelable;

public class LatLonParcel implements Parcelable {

	     private LatLon geoPoint;

	     public LatLonParcel(LatLon point) {
	          geoPoint = point;
	     }

	     public LatLon getGeoPoint() {
	          return geoPoint;
	     }

	     public int describeContents() {
	         return 0;
	     }

	     public void writeToParcel(Parcel out, int flags) {
	         out.writeDouble(geoPoint.latitude);
	         out.writeDouble(geoPoint.longitude);
	     }

	     public static final Parcelable.Creator<LatLonParcel> CREATOR
	             = new Parcelable.Creator<LatLonParcel>() {
	         public LatLonParcel createFromParcel(Parcel in) {
	             return new LatLonParcel(in);
	         }

	         public LatLonParcel[] newArray(int size) {
	             return new LatLonParcel[size];
	         }
	     };

	     private LatLonParcel(Parcel in) {
	         double lat = in.readDouble();
	         double lon = in.readDouble();
	         geoPoint = new LatLon(lat, lon,0.0);
	     }

	

		
	 }