package uni.projecte.dataLayer.ThesaurusManager;

import java.util.ArrayList;

import uni.projecte.R;
import uni.projecte.Activities.Thesaurus.ThesaurusInfo;
import uni.projecte.dataTypes.Utilities;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.TextView;



public class ThesaurusListAdapter extends BaseAdapter  {
	
	private LayoutInflater mInflater;
	private ArrayList<ThesaurusElement> thElements;
	private Context parentContext;
	private OnClickListener remoteThListener;
	
	
	private String remoteTh;
	private String plainTh;
	private String xmlLocalTh;
	
	public ThesaurusListAdapter(Context context,ArrayList<ThesaurusElement> thElements,OnClickListener removeThListener){
		
		this.thElements=thElements;
		this.parentContext=context;
		
		this.remoteThListener=removeThListener;
		
		mInflater = LayoutInflater.from(context);
		
		xmlLocalTh=context.getString(R.string.thSourceLocalBVegana);
		plainTh=context.getString(R.string.thSourceLocalPlain);
		remoteTh=context.getString(R.string.thSourceRemote);
		
	}


	public int getCount() {
		
		return thElements.size();
		
	}

	public Object getItem(int position) {
		
		return thElements.get(position);
		
	}

	public long getItemId(int position) {
		
		return thElements.get(position).getId();
		
	}

  	 public View getView(final int position, View convertView, ViewGroup parent) {
  		 
    	 ViewHolder holder;
    	 
    	 if (convertView == null) {
	    	 
    		 convertView = mInflater.inflate(R.layout.thesaurus_list_row, null);
    		 
	    	 holder = new ViewHolder();
	    	 holder.tvThName = (TextView)convertView.findViewById(R.id.tvThInfName);
	    	 holder.tvThType = (TextView)convertView.findViewById(R.id.tvThInfType);
	    	 holder.tvThElemCount = (TextView)convertView.findViewById(R.id.tvThInfItems);
	    	 holder.tvConnectType = (TextView)convertView.findViewById(R.id.tvThTypeConnection);

	    	 holder.rmButton= (ImageButton)convertView.findViewById(R.id.ibRemTh);
	    	 holder.editButton=(ImageButton)convertView.findViewById(R.id.ibEditTh);


	    	 convertView.setTag(holder);
	    	 
    	 }
    	 else {
    		 
    		 holder = (ViewHolder) convertView.getTag();
    		 
    	 }
    	 
    	    holder.rmButton.setBackgroundResource(android.R.drawable.ic_menu_delete);
    	    holder.editButton.setBackgroundResource(android.R.drawable.ic_menu_edit);
 
    	    ThesaurusElement thElem=thElements.get(position);
                       
            holder.tvThName.setText(thElem.getThName());
            if(thElem.getThType()!=null && !thElem.getThType().equals("")) holder.tvThType.setText(Utilities.translateThTypeToCurrentLanguage(parentContext,thElem.getThType()));
            
            holder.tvThElemCount.setText("# "+thElem.getThItemCount()+"");
            
            holder.tvConnectType.setText(translateThConnection(thElem.getSourceType()));

            holder.editButton.setTag(thElem.getThName());
            holder.editButton.setId((int) thElem.getId());
            
            holder.rmButton.setTag(thElem.getThName());
	       
	       holder.rmButton.setOnClickListener(remoteThListener);
	       
            
           holder.editButton.setOnClickListener(new OnClickListener() {  
            	
            	public void onClick(View v) { 
            		
            		Intent intent = new Intent(v.getContext(), ThesaurusInfo.class);
            		
            		Bundle b = new Bundle();
		 			b.putString("thName", (String) v.getTag());
		 			intent.putExtras(b);
		 			b = new Bundle();
		 			b.putLong("thId", v.getId());
		 			intent.putExtras(b);	 		
    	
            		((Activity) v.getContext()).startActivityForResult(intent,0);
            		
            	
             } }
            
            );
    	 
		
		return convertView;
	}

	 private String translateThConnection(String sourceType) {

		 
		 if(sourceType!=null){
			 
			 if(sourceType.equals("remote")) return remoteTh;
			 else if(sourceType.equals("localBvegana")) return xmlLocalTh;
			 else return plainTh;
			 
		 }
		 else{
			 
			 return remoteTh;
			 
		 }
		 
		 

		
	}

	static class ViewHolder {
		 
		   	TextView tvThName;
	        TextView tvThElemCount;
	        TextView tvThType;
	        TextView tvConnectType;
	        ImageButton rmButton;
	        ImageButton editButton;

	 }
	
	 
}
