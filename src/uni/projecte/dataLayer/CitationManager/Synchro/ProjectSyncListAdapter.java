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


package uni.projecte.dataLayer.CitationManager.Synchro;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import uni.projecte.R;
import uni.projecte.dataLayer.ProjectManager.objects.Project;
import uni.projecte.dataTypes.Utilities;
import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.TextView;



public class ProjectSyncListAdapter extends BaseAdapter  {
	
	private LayoutInflater mInflater;
	private ArrayList<Project> remoteProjectList;
	private HashMap<String, Project> localProjectMap;
	private OnClickListener actionListener;
	private Context context;
	
	
	
	public ProjectSyncListAdapter(Context context,ArrayList<Project> projectList, HashMap<String, Project> localProjectMap, OnClickListener syncroProjectList){
		
		this.remoteProjectList=projectList;
		this.localProjectMap=localProjectMap;
		this.actionListener=syncroProjectList;
		this.context=context;

		mInflater = LayoutInflater.from(context);
		
	}


	public int getCount() {
		
		return remoteProjectList.size();
		
	}

	public Object getItem(int position) {
		
		return remoteProjectList.get(position);
		
	}

	public long getItemId(int position) {
		
		return position;
		
	}

  	 public View getView(final int position, View convertView, ViewGroup parent) {
  		 
    	 ViewHolder holder;
    	 
    	 if (convertView == null) {
	    	 
    		 convertView = mInflater.inflate(R.layout.project_sync_row, null);
    		 
	    	 holder = new ViewHolder();

	    	 holder.tvThDesc = (TextView)convertView.findViewById(R.id.tvRemoteThDesc);
	    	 holder.tvThUpdate= (TextView)convertView.findViewById(R.id.tvRemoteUpdate);
	    	 holder.tvCounter=(TextView)convertView.findViewById(R.id.item_counter);
	    	 holder.ibAction=(ImageButton)convertView.findViewById(R.id.ibSyncAction);


	    	 convertView.setTag(holder);
	    	 
    	 }
    	 else {
    		 
    		 holder = (ViewHolder) convertView.getTag();
    		 
    	 }
    	 
    	    Project remoteProj=remoteProjectList.get(position);
    	    Project localProj=localProjectMap.get(remoteProj.getProjName());
    	    
       	 	//holder.tvThSource.setText(thElem.getLastUpdate());
       
       	 	holder.tvThDesc.setText(Html.fromHtml(remoteProj.getProjName()));
       	 	
      	 	holder.tvThDesc.setTag(remoteProj.getProjName());

       	 	//holder.ibAction.setBackgroundResource(android.R.drawable.ic_popup_sync);
       	 	
      	 	if(localProj!=null) {
      	 		
          	 	holder.tvThDesc.setOnClickListener(actionListener);

      	 		if(remoteProj.getServer_last_mod().equals("")){
      	 			
          	 		holder.tvThUpdate.setText("New project!");
          	 		holder.ibAction.setVisibility(View.GONE);

      	 		}
      	 		else if(localProj.isUpdated(remoteProj.getServer_last_mod())){
      	 			
          	 		holder.tvThUpdate.setText("Updated");
          	 		holder.ibAction.setVisibility(View.GONE);

      	 		}
      	 		else{
      	 			
          	 		holder.tvThUpdate.setText("Not updated");
          	 		holder.ibAction.setBackgroundResource(android.R.drawable.ic_popup_sync);
          	 		holder.ibAction.setVisibility(View.VISIBLE);

      	 		}
      	 		
      	 		
      	 		
      	 	}
      	 	else {
      	 		
      	 		holder.tvThUpdate.setText(remoteProj.getServer_last_mod());
      	 		holder.ibAction.setVisibility(View.VISIBLE);
      	 		holder.ibAction.setBackgroundResource(R.drawable.cross_green);
     	 		

      	 	}
      	 	
             	 	
       	 	holder.tvCounter.setText(remoteProj.getServer_unsyncro()+"");
       	 	
       	 	//if(thElem.getThSource().equals("url")) holder.llThDesc.setBackgroundColor(context.getResources().getColor(R.color.secondaryGreen));
       	 	//else holder.llThDesc.setBackgroundColor(context.getResources().getColor(R.color.background2));
       	 	
       	 /*	String updated=remoteLocalTh.get(thElem.getThId());
       	 	if(updated!=null){
       	 		
   	 			holder.tvThUpdate.setVisibility(View.VISIBLE);
       	 		
       	 		if(DateTimeUtils.compareDate(updated,thElem.getLastUpdate())<0) {
       	 			
       	       	 	holder.tvThUpdate.setText(String.format(context.getString(R.string.thRemoteNotUpdated),thElem.getLastUpdate()));
       	       	 	holder.ibAction.setBackgroundResource(android.R.drawable.ic_popup_sync);
       	       	 	holder.ibAction.setOnClickListener(actionListener);
       	       	 	holder.ibAction.setTag("update");

       	 		}
       	 		else {
       	 			
       	 			holder.ibAction.setBackgroundResource(R.drawable.tick);
       	       	 	holder.tvThUpdate.setText(context.getString(R.string.thRemoteUpdated));
       	 			
       	 		}
       	 	
       	 	}
       	 	else{
       	 		
       	 		holder.ibAction.setBackgroundResource(android.R.drawable.ic_menu_save);
   	 			holder.tvThUpdate.setVisibility(View.GONE);
   	 			holder.ibAction.setOnClickListener(actionListener);
   	 			holder.ibAction.setTag("new");

       	 		
       	 	}*/
       	 	
       	 	//if(thElem.getThSource().equals("url")) holder.ibAction.setBackgroundResource(android.R.drawable.ic_popup_sync);
       	 	//else holder.ibAction.setBackgroundResource(android.R.drawable.ic_menu_save);
       	 	
 	       //holder.ibAction.setId(position);
                       
		
		return convertView;
	}


	static class ViewHolder {
		 
		   	TextView tvThDesc;
	        TextView tvThUpdate;
	        TextView tvThSource;
	        TextView tvCounter;
	        ImageButton ibAction;

	 }
	
	 
}
