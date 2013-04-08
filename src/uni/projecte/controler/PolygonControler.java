package uni.projecte.controler;

import java.util.ArrayList;

import uni.projecte.dataTypes.ProjectField;
import uni.projecte.dataTypes.Utilities;
import uni.projecte.maps.utils.LatLon;
import uni.projecte.ui.polygon.PolygonField;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;
import android.widget.SlidingDrawer;

public class PolygonControler {

	private Context baseContext;
	
	private ProjectSecondLevelControler projSLCnt;
	private CitationSecondLevelControler citSLCnt;
	
	private ProjectField projField;
	
	
	public PolygonControler(Context baseContext) {

		this.baseContext=baseContext;
		citSLCnt=new CitationSecondLevelControler(baseContext);

	}

	
	public long getPolygonSubFieldId(long fieldId){
		
		projSLCnt=new ProjectSecondLevelControler(baseContext);
		
		projField=projSLCnt.getMultiPhotoSubFieldId(fieldId);
	
		return projField.getId();
		
	}
	

	public void addPolygonList(PolygonField polygonField) {

		ArrayList<LatLon> path=polygonField.getPolygonPath();
        
    	long subFieldId=getPolygonSubFieldId(polygonField.getFieldId());
    	
    	for(LatLon point: path){
   		
			// subProjId (0) || fieldId inside subproject (1)				
	        long citationId=citSLCnt.createCitation(polygonField.getSecondLevelId(), point.latitude,point.longitude, "");

	        citSLCnt.startTransaction();
	        	citSLCnt.addCitationField(polygonField.getFieldId(),citationId,subFieldId,projField.getName(),(int)point.altitude+"");
	    	citSLCnt.EndTransaction();
	    	
			Log.i("Citation","Action-> created citation[Polygon]Value : Label: polygonAltitude Value: "+point.altitude);

    	}
		
	}
	
	public void updatePolygonList(PolygonField polygonField) {

		citSLCnt.removeCitationsBySLId(polygonField.getSecondLevelId());

		addPolygonList(polygonField);
		
	}


	public ArrayList<LatLon> getPolygonPath(String secondLevelFieldId) {

		ArrayList<LatLon> path= new ArrayList<LatLon>();
		
		//idSample,fieldId,latitude,longitude,date,group_concat(value,\":\")
		Cursor points=citSLCnt.getFieldValuesBySLId(secondLevelFieldId);
		
		if(points!=null){
			
			points.moveToFirst();
			
			while(!points.isAfterLast()){
				
				LatLon latLonObj= new LatLon(points.getDouble(2), points.getDouble(3),Integer.valueOf(points.getString(5)));
				
				path.add(latLonObj);
				
				points.moveToNext();
			}
			
			points.close();
			
		}
		
		return path;
		
	}


	public String getPolygonString(String subCitId, boolean kmlFormat) {

		String points="";
		
		Cursor polyPoints=citSLCnt.getFieldValuesBySLId(subCitId);
		
		if(polyPoints!=null) {
			
			polyPoints.moveToFirst();
			
			while(!polyPoints.isAfterLast()){
				
				if(!kmlFormat) points=points+"["+polyPoints.getDouble(2)+", "+polyPoints.getDouble(3)+", "+polyPoints.getString(5)+"] ";
				else points=points+polyPoints.getDouble(3)+","+polyPoints.getDouble(2)+","+polyPoints.getString(5)+"\n ";
				polyPoints.moveToNext();
			}		
			
			polyPoints.close();
		} 
		
		return points;
	}



	

}
