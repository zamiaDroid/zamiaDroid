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

package uni.projecte.Activities.Projects;

import uni.projecte.Main;
import uni.projecte.R;
import uni.projecte.R.id;
import uni.projecte.R.layout;
import uni.projecte.dataLayer.bd.ProjectDbAdapter;
import uni.projecte.dataTypes.Utilities;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

public class ProjecteChooser extends Activity{
	
		
		private ListView projectList;
		private ProjectDbAdapter mDbHelper;
		private Cursor cRsList;
			

	
	  @Override
	public void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        
	        Utilities.setLocale(this);
	        setContentView(R.layout.project_selector);
	        
	        mDbHelper = new ProjectDbAdapter(this);

	        
	        Button bImportProj= (Button)findViewById(R.id.bProjectImport);
	        bImportProj.setOnClickListener(bProjectImport);

	        
	        projectList = (ListView)findViewById(R.id.rsList);
	        projectList.setOnItemClickListener(theProjectListListener);
	        
	  }
	  

	 @Override
	public void onResume(){
		 
		  super.onResume();
		 
	    	mDbHelper.open();
	    	loadResearches();
		 
		 
	 } 
	  
	  @Override
	public void onPause(){
		  
		  super.onDestroy();
		  
		 mDbHelper.close();

		 cRsList.close(); 
		 
		  
	  }
	  
	  
	  public OnItemClickListener theProjectListListener = new OnItemClickListener() {
			 
		  public void onItemClick(android.widget.AdapterView<?> parent, View v, int position, long id) {
		      
			  
			 mDbHelper.open();
			  
		 	 TextView tv=(TextView)v;
		 	 String choosenRs=(String)tv.getText();
		 	 
		 	 Cursor c=mDbHelper.fetchProjectByName(choosenRs);
		 	 c.moveToFirst();
	
		 	
		 	 SharedPreferences preferences = getSharedPreferences(Main.PREF_FILE_NAME, MODE_PRIVATE);
		 	 SharedPreferences.Editor editor = preferences.edit();
		 	 editor.putLong("predProjectId", c.getLong(0));
		 	 editor.putString("predField", null);
		 	 editor.commit();

		 	 finish();
		 	
		  	
		  }
		  };
	  
	private void loadResearches (){
	    	
	    	cRsList = mDbHelper.fetchAllProjects();
	        startManagingCursor(cRsList);

	        String[] from= new String[] { ProjectDbAdapter.PROJECTNAME};
	        int[] to = new int[] { R.id.listItemprojName}; 
	        
	        // Now create an array adapter and set it to display using our row
	        SimpleCursorAdapter mostres = new SimpleCursorAdapter(this, R.layout.project_row, cRsList, from, to);
	        projectList.setAdapter(mostres);
	      
	    	
	    }
	  
	  
	private OnClickListener bProjectImport = new OnClickListener()
	{
	    public void onClick(View v)
	    {                        
	   
	    		Intent myIntent = new Intent(v.getContext(), ProjectImport.class);
	            startActivity(myIntent);

	    }
	    	
	}; 

}

