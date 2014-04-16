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


package uni.projecte.Activities.Miscelaneous;

import java.util.ArrayList;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import uni.projecte.R;
import uni.projecte.dataLayer.CitationManager.Synchro.ProjectSyncListAdapter;
import uni.projecte.dataLayer.CitationManager.Synchro.SyncCitationManager;
import uni.projecte.dataLayer.CitationManager.Synchro.SyncRestApi;
import uni.projecte.dataLayer.CitationManager.Synchro.ZamiaCitation;
import uni.projecte.dataLayer.ProjectManager.objects.Project;
import uni.projecte.dataLayer.ThesaurusManager.RemoteThesaurusListAdapter;
import uni.projecte.dataTypes.Utilities;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;


public class ActivityProvadora extends Activity {
	
	private Spinner sp;
	private SyncCitationManager synchroManager;
	
	
	private TextView tv;
	private ListView lstClientes;
	private Button btUpdateRemote;
	private Button btUpdateLocal;

	  @Override
	  public void onCreate(Bundle savedInstanceState) {
		  
	       super.onCreate(savedInstanceState);
	        
	       Utilities.setLocale(this);
		   
	       setContentView(R.layout.project_sync);
	       
	       synchroManager = new SyncCitationManager(this);
	       
	       lstClientes = (ListView)findViewById(R.id.lvRemoteThPool);
	       btUpdateRemote=(Button)findViewById(R.id.btUpdateRemote);
	       btUpdateLocal=(Button)findViewById(R.id.btUpdateLocal);
	       
	       ArrayList<Project> projectList=synchroManager.getRemoteProjectList();
	       
	       ProjectSyncListAdapter adapter=new ProjectSyncListAdapter(this, projectList, listLocalClick);
	      
	       lstClientes.setAdapter(adapter);	
	       
	       
	       btUpdateRemote.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {

				//synchro.get1ProjectInfo((String)sp.getSelectedItem(),tv);
			    //synchro.getAllCitations((String)sp.getSelectedItem(),lstClientes);
				
				
				
			}
		});

	
	       
		    
	  }
	  
	  public OnClickListener listLocalClick = new OnClickListener() {

			public void onClick(View v) {

				int updated=synchroManager.getOutdatedLocalCitations((String) v.getTag());
				
				Utilities.showToast("Dades remotes actualitzades "+updated, v.getContext());

				updated=synchroManager.getOutdatedRemoteCitations((String) v.getTag());
				
				Utilities.showToast("Dades locals actualitzades "+updated, v.getContext());

				
			}
		  
		};

	  public OnClickListener listClick = new OnClickListener() {
		
		@Override
		public void onClick(View v) {

						
			//TextView counter=(TextView) ((Activity) v.getParent().getParent()).findViewById(R.id.item_counter);
			//counter.setText(""+updated);
			
		//	Utilities.showToast("Dades remotes noves "+updated, v.getContext());

			
		}
	};
}

