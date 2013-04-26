package uni.projecte.controler;

import uni.projecte.dataLayer.bd.CitacionDbAdapter;
import android.content.Context;
import android.database.Cursor;


public class MapConfigControler{

	private Context baseContext;
	
	public MapConfigControler(Context baseContext) {

		this.baseContext=baseContext;
		
	}

	public boolean setCitationMapMarker(long citationId,String marker_id){
		
		CitacionDbAdapter mDbSample=new CitacionDbAdapter(baseContext);
		mDbSample.open();
		
			boolean success = mDbSample.updateCitationMapMarker(citationId,marker_id);
		
		mDbSample.close();
		
		return success;
		
	}
	
	public boolean setProjectMapMarker(long projId, String marker_id){
		
		ProjectConfigControler projCnf= new ProjectConfigControler(baseContext);

		String newValue=projCnf.changeProjectConfig(projId, ProjectConfigControler.DEFAULT_MARKER, marker_id);
		
		return !newValue.equals("");
		
	}
	
	public String getCitationMapMarker(long citationId){
		
		String marker_id="bubble";
		
		CitacionDbAdapter mDbSample=new CitacionDbAdapter(baseContext);
		mDbSample.open();
		
			Cursor marker = mDbSample.getCitationMapMarker(citationId);
			
			if(marker!=null && marker.getCount()>0) {
				
				marker.moveToFirst();

				if(marker.getString(1)!=null) marker_id=marker.getString(1);

				marker.close();
			}
		
		
		mDbSample.close();
		
		return marker_id;
		
	}
	
	public String getProjectMapMarker(long projId){
		
		ProjectConfigControler projCnf= new ProjectConfigControler(baseContext);

		return projCnf.getProjectConfig(projId,ProjectConfigControler.DEFAULT_MARKER);
		
	}
	

}
