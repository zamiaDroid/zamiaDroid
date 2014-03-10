package uni.projecte.controler;

import java.util.ArrayList;

import uni.projecte.dataLayer.CitationManager.CitationExporter;
import uni.projecte.dataLayer.CitationManager.Zamia.ZamiaCitationExporter;
import uni.projecte.dataTypes.LocationCoord;
import uni.projecte.dataTypes.ProjectField;
import uni.projecte.dataTypes.Utilities;
import uni.projecte.maps.utils.LatLon;
import uni.projecte.ui.polygon.PolygonField;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;
import android.widget.SlidingDrawer;

public class PolygonControler {

	public final String FIELD_NAME="polygon";
	
	
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
	

	public void addPolygonList(PolygonField polygonField,long projId,long parentId) {

		ArrayList<LatLon> path=polygonField.getPolygonPath();
        
    	long subFieldId=getPolygonSubFieldId(polygonField.getFieldId());
    	
    	for(LatLon point: path){
   		
			// subProjId (0) || fieldId inside subproject (1)				
	        long citationId=citSLCnt.createCitation(polygonField.getSecondLevelId(), point.latitude,point.longitude, "",projId,FIELD_NAME,parentId);

	        citSLCnt.startTransaction();
	        	citSLCnt.addCitationField(polygonField.getFieldId(),citationId,subFieldId,projField.getName(),(int)point.altitude+"");
	    	citSLCnt.EndTransaction();
	    	
			Log.i("Citation","Action-> created citation[Polygon]Value : Label: polygonAltitude Value: "+point.altitude);

    	}
		
	}
	
	public void updatePolygonList(PolygonField polygonField,long projId,long parentId) {

		citSLCnt.removeCitationsBySLId(polygonField.getSecondLevelId());

		addPolygonList(polygonField,projId,parentId);
		
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


	public ArrayList<ArrayList<LatLon>> getPolygonList(long projId) {

		ArrayList<ArrayList<LatLon>> polygon_list= new ArrayList<ArrayList<LatLon>>();
		
		Cursor polygons=citSLCnt.getPolygonPoints(projId);
		
		if(polygons!=null){
			
			polygons.moveToFirst();
			String lastPolygonId="";
			
			//Single Polygon
			ArrayList<LatLon> tmpPolygon=null;
			
			while(!polygons.isAfterLast()){
				
				String polygonId=polygons.getString(1);
				
				Log.i("Map","PolygonId: "+polygonId);
				
				if(!polygonId.equals(lastPolygonId)){
					
					if(tmpPolygon!=null) polygon_list.add(tmpPolygon);
					
					tmpPolygon=new ArrayList<LatLon>();
					
				}
				
				tmpPolygon.add(new LatLon(polygons.getDouble(2), polygons.getDouble(3), 0.0));
				
				lastPolygonId=polygonId;
				
				polygons.moveToNext();
				
			}
			
			if(tmpPolygon!=null) polygon_list.add(tmpPolygon);
			
			polygons.close();
			
		}
		
		return polygon_list;
		
	}


	public ArrayList<ArrayList<LatLon>> getPolygonList(long projId,
			String preSettedLoc) {

		ArrayList<ArrayList<LatLon>> polygon_list= new ArrayList<ArrayList<LatLon>>();	
		String[] ids=preSettedLoc.split(":");
		
		for(int i=1;i<ids.length;i++){
			
			Cursor polygonPoints=citSLCnt.getPolygonPoints(projId,Long.valueOf(ids[i]));
			
			if(polygonPoints!=null){
				
				polygonPoints.moveToFirst();
				
				//Single Polygon
				ArrayList<LatLon> tmpPolygon=new ArrayList<LatLon>();
				
				while(!polygonPoints.isAfterLast()){
					
					tmpPolygon.add(new LatLon(polygonPoints.getDouble(2), polygonPoints.getDouble(3), 0.0));
					polygonPoints.moveToNext();
					
				}
				
				polygon_list.add(tmpPolygon);
				polygonPoints.close();
				
			}
			
			
			
		}		
    	
		
		
		return polygon_list;
	}


	public void exportSubCitationsZamia(long fieldId, String citationValue,ZamiaCitationExporter zamiaCitExp) {

		Cursor polyPoints=citSLCnt.getFieldValuesBySLId(citationValue);
		
		zamiaCitExp.createPolygon();
		
		if(polyPoints!=null) {
			
			polyPoints.moveToFirst();
			
			while(!polyPoints.isAfterLast()){
				
				zamiaCitExp.createPolygonPoint(polyPoints.getDouble(2),polyPoints.getDouble(3),polyPoints.getString(4),polyPoints.getString(5));
				polyPoints.moveToNext();
			}		
			
			polyPoints.close();
		} 
		zamiaCitExp.closePolygon();
		
		
	}


}
