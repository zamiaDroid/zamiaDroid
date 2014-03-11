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


package uni.projecte.dataLayer.ThesaurusManager;

import java.util.ArrayList;
import java.util.HashMap;

import uni.projecte.R;
import uni.projecte.dataLayer.RemoteDBManager.objects.DataBasesInfo;
import uni.projecte.dataLayer.utils.DateTimeUtils;
import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.TextView;



public class RemoteThesaurusListAdapter extends BaseAdapter  {
	
	private LayoutInflater mInflater;
	private ArrayList<RemoteThesaurus> thElements;
	private DataBasesInfo dbInfo;
	private OnClickListener actionListener;
	private Context context;
	private String thPool;
	private HashMap<String, String> remoteLocalTh;
	
	
	
	public RemoteThesaurusListAdapter(Context context,String thPool, ArrayList<RemoteThesaurus> thElements, OnClickListener actionListener, HashMap<String, String> remoteLocalTh){
		
		this.thElements=thElements;
		this.actionListener=actionListener;
		this.context=context;
		this.thPool=thPool;
		this.remoteLocalTh=remoteLocalTh;
		
		mInflater = LayoutInflater.from(context);
		dbInfo=new DataBasesInfo(context);
		
	}


	public int getCount() {
		
		return thElements.size();
		
	}

	public Object getItem(int position) {
		
		return thElements.get(position);
		
	}

	public long getItemId(int position) {
		
		return position;
		
	}

  	 public View getView(final int position, View convertView, ViewGroup parent) {
  		 
    	 ViewHolder holder;
    	 
    	 if (convertView == null) {
	    	 
    		 convertView = mInflater.inflate(R.layout.thesaurus_remote_row, null);
    		 
	    	 holder = new ViewHolder();

	    	 holder.tvThDesc = (TextView)convertView.findViewById(R.id.tvRemoteThDesc);
	    	 holder.tvThUpdate= (TextView)convertView.findViewById(R.id.tvRemoteUpdate);
	    	 holder.ibAction=(ImageButton)convertView.findViewById(R.id.ibRemoteThAction);

	    	 convertView.setTag(holder);
	    	 
    	 }
    	 else {
    		 
    		 holder = (ViewHolder) convertView.getTag();
    		 
    	 }
    	 
    	    RemoteThesaurus thElem=thElements.get(position);
    	    
       	 	//holder.tvThSource.setText(thElem.getLastUpdate());
       	 	
       	 	if(thElem.getDesc().equals("")) {
       	 		
       	 		String desc = String.format(context.getResources().getString(R.string.thImportRepDesc), thPool,dbInfo.getDataBaseName(thElem.getThSource()));
       	 		thElem.setDesc(desc);
       	 		
       	 	}
       	 	
       	 	holder.tvThDesc.setText(Html.fromHtml(thElem.getDesc()));
       	 
       	 	
       	 	//if(thElem.getThSource().equals("url")) holder.llThDesc.setBackgroundColor(context.getResources().getColor(R.color.secondaryGreen));
       	 	//else holder.llThDesc.setBackgroundColor(context.getResources().getColor(R.color.background2));
       	 	
       	 	String updated=remoteLocalTh.get(thElem.getThId());
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

       	 		
       	 	}
       	 	
       	 	//if(thElem.getThSource().equals("url")) holder.ibAction.setBackgroundResource(android.R.drawable.ic_popup_sync);
       	 	//else holder.ibAction.setBackgroundResource(android.R.drawable.ic_menu_save);
       	 	
 	       holder.ibAction.setId(position);
                       
		
		return convertView;
	}


	static class ViewHolder {
		 
		   	TextView tvThDesc;
	        TextView tvThUpdate;
	        TextView tvThSource;
	        ImageButton ibAction;

	 }
	
	 
}
