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

package uni.projecte.maps;

import com.google.android.maps.GeoPoint;

/** Class to hold our location information */
public class MapLocation {

	private long citationId; 


	private GeoPoint	point;
	private String		name;
	private boolean		moreInfo;
	private boolean		chosen;
	private String marker_id;

	public MapLocation(long citationId,String name,double latitude, double longitude, String marker_id) {
		
		this.citationId=citationId;
		this.name = name;
		this.moreInfo=false;
		this.marker_id=marker_id;
		point = new GeoPoint((int)(latitude*1e6),(int)(longitude*1e6));
		chosen=false;
		
	}
	
	public MapLocation(long citationId,String name, GeoPoint point){
		
		this.citationId=citationId;
		this.name = name;
		this.moreInfo=false;
		
		this.point=point;
		
	}

	public boolean isMoreInfo() {
		return moreInfo;
	}

	public void setMoreInfo(boolean moreInfo) {
		this.moreInfo = moreInfo;
	}

	public GeoPoint getPoint() {
		return point;
	}

	public String getName() {
		return name;
	}
	
	public long getCitationId() {
		return citationId;
	}

	public void setCitationId(long citationId) {
		this.citationId = citationId;
	}

	public void setPoint(GeoPoint point) {
		this.point = point;
	}

	public MapLocation copy() {

		MapLocation loc=new MapLocation(this.citationId, this.name, this.point);
		
		return loc;
	}

	public void setChosen(boolean chosen) {
		this.chosen = chosen;
	}

	public boolean isChosen() {
		return chosen;
	}

	public String getMarker_id() {
		return marker_id;
	}

	public void setMarker_id(String marker_id) {
		this.marker_id = marker_id;
	}
	
}
