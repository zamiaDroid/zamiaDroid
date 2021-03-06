package uni.projecte.controler;

import uni.projecte.dataLayer.bd.ProjectDbAdapter;
import android.content.Context;
import android.database.Cursor;

public class ProjectConfigControler extends ProjectControler{
	
	public static final int DEFAULT_TAB_PROVIDER=1;
	public static final int SEC_STORAGE_ENABLED=2;
	public static final int DEFAULT_MARKER=3;
	
	private String defaultTabProviderValue="bdbc";
	private String defaultSecStorageEnabled="false";
	private String defaultMarker="bubble";
	

	public ProjectConfigControler(Context c) {
		super(c);

	}


	public String changeProjectConfig(long projId,int id, String newValue){
		
		String projConfKey=getConfigKey(id);
	
		if(!projConfKey.equals("")){
			
			projDbAdapter = new ProjectDbAdapter(baseContext);
			projDbAdapter.open();
			
			projDbAdapter.updateProjectConfigValue(projId,projConfKey,newValue);
				
			projDbAdapter.close();
		
		}
		
		return newValue;
		
	}
	
	public String getProjectConfig(long projId,int id){
		
		String projConfKey=getConfigKey(id);
		String confValue="";
	
		if(!projConfKey.equals("")){
			
			projDbAdapter = new ProjectDbAdapter(baseContext);
			projDbAdapter.open();
			
			Cursor c=projDbAdapter.fetchProjectConfigValue(projId,projConfKey);
			c.moveToFirst();
			
			if(c.getCount()>0){
				
				confValue=c.getString(3);
				
			}
			else{
				
				confValue=getConfigValue(id);
				projDbAdapter.insertProjectConfigValue(projId,projConfKey,confValue);
				
			}
	
			c.close();	
			
			projDbAdapter.close();
		
		}
		
		return confValue;

		
	}

	private String getConfigKey(int id){
		
		String projConfKeyName="";
		
		switch (id) {

			case DEFAULT_TAB_PROVIDER:
				
				projConfKeyName="defaultTabProvider";
				
				break;
				
			case SEC_STORAGE_ENABLED:
				
				projConfKeyName="secondaryStorageEnabled";
				
				break;
				
			case DEFAULT_MARKER:
				
				projConfKeyName="defaultMapMarker";
				
				break;
	
			default:
				
				break;
		}
		
		return projConfKeyName;
		
	}
	
	private String getConfigValue(final int id) {

		String projConfKeyValue="";
		
		switch (id) {

			case DEFAULT_TAB_PROVIDER:
				
				projConfKeyValue=defaultTabProviderValue;
				
				break;
				
			case SEC_STORAGE_ENABLED:
				
				projConfKeyValue=defaultSecStorageEnabled;
				
				break;
				
			case DEFAULT_MARKER:
				
				projConfKeyValue=defaultMarker;
				
				break;
	
			default:
				
				break;
		}
		
		return projConfKeyValue;
	}
	
	
	
	
}
