package uni.projecte.dataLayer.RemoteDBManager.objects;

import uni.projecte.R;
import uni.projecte.dataTypes.Utilities;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

public class DatabaseListAdapter extends BaseExpandableListAdapter {
  	 
	private LayoutInflater mInflater;
	
    private Context baseContext;
    private DataBaseHandler dbH;
    
    private DataBasesInfo dbInfo;
    


	public DatabaseListAdapter(Context context,DataBaseHandler dbH) {
	 
		 mInflater = LayoutInflater.from(context);
	 
		 this.baseContext=context;
		 
		 this.dbH=dbH;
		 dbInfo=new DataBasesInfo(context);
		 
		 /*
		  * 	Flora
		  *   	+ Biocat
		  * 	+ Sifib
		  * 	+ Orca
		  * 	+ Sivim
		  *	
		  *		Vertebrats
		  *		+ Biocat
		  *
		  */
		 
	

	 }

	 public View getGroupView(int position, boolean isExpanded, View convertView, ViewGroup parent) {
	 
		 ViewHolder holder;
		 
		 
		 if (convertView == null) {
	    	 
			 convertView = mInflater.inflate(R.layout.remotedbmainrow, null);
			 
	    	 holder = new ViewHolder();
	    	 holder.dbFilumName = (TextView) convertView.findViewById(R.id.dbFilumName);

	    	 convertView.setTag(holder);
	    	 
	    	 
		 } 
		 else {
			 
			 holder = (ViewHolder) convertView.getTag();
			 
		 }
	
		 
		 	holder.dbFilumName.setText(Utilities.translateThTypeToCurrentLanguage(baseContext, dbH.getFilumAtPostion(position)));
		 
	  /*  	 holder.citationTag.setText(Html.fromHtml(citations.get(position).getTag()));
	    	 holder.citationTag.setTag(citations.get(position).getCitationId());
	    	 
	    	 holder.citationDate.setText(citations.get(position).getDate());
	    	 
	    	 holder.selected.setTag((int) citations.get(position).getCitationId());
	    	 holder.selected.setId((int) citations.get(position).getPosition());
	    	 
	    	 if(citationsH.isCheckedItem(citations.get(position).getCitationId())) holder.selected.setChecked(true);
	    	 else holder.selected.setChecked(false);
	    	 
	    	 holder.selected.setOnClickListener(new OnClickListener() {  
	         	
	         	public void onClick(View v) { 
	         	
	         		if(((CheckBox)v).isChecked()){
	    		 
	         			citationsH.setCheckedItem((Integer) v.getTag(),v.getId(),true);
	    		 
	    	 		}
	    	 		else{
	    		 
	         			citationsH.setCheckedItem((Integer) v.getTag(),v.getId(),false);
	    	 			
	    	 		}
	         	}
	         	
	    	 }
	         
	         );*/
	    	 
    	
    	 return convertView;
	 
	 }

	 static class ViewHolder {

		 TextView dbFilumName;

	 }
	 



	  public Object getChild(int groupPos, int childPos) 
	  {
	      return dbH.getDataBase(groupPos, childPos);
	  }

	  public long getChildId(int groupPos, int childPos) 
	  {
	      return groupPos;
	  }

	  public int getChildrenCount(int groupPos) 
	  {
	      return dbH.getDataBaseCount(groupPos);
	  }

	  
	  static class ViewChildHolder {
	
		  public TextView tvRemoteDBName;
		  public TextView tvRemoteDBDesc;
		  public ImageButton ibOrderUp;
		  public ImageButton ibOrderDown;
		  public ImageButton ibActive;
		  
	  }


		  public View getChildView(final int groupPos, int childPos, boolean isLastChild, View convertView, ViewGroup parent) 
		  {
			  
			  ViewChildHolder holder;
		      RemoteDB remoteDb=dbH.getDataBase(groupPos, childPos);	
	          int n=getChildrenCount(groupPos);
	          String filum=remoteDb.getDbFilum();

				 			  
		      if (convertView == null){
		    	  
		          LayoutInflater inflater = LayoutInflater.from(baseContext);
		          convertView = inflater.inflate(R.layout.remotedbrow, null);
		          
		          
		          holder= new ViewChildHolder();
		          holder.tvRemoteDBName =(TextView)convertView.findViewById(R.id.tvRemoteDBId);
		          holder.tvRemoteDBDesc =(TextView)convertView.findViewById(R.id.tvRemoteDBDesc);
		          holder.ibActive=(ImageButton)convertView.findViewById(R.id.ibRemoteDBActive);
		          holder.ibOrderUp=(ImageButton)convertView.findViewById(R.id.ibRemoteDBArrowUp);
		          holder.ibOrderDown=(ImageButton)convertView.findViewById(R.id.ibRemoteDBArrowDown);
		          
			      //holder.ibEditButton = (ImageButton)convertView.findViewById(R.id.ibEditCitation);
		          
			   
		          
		          convertView.setTag(holder);
		      }
		      else{
		    	  
		    	  holder = (ViewChildHolder) convertView.getTag();
		      }
		      
		     
		      int order=remoteDb.getOrder();
		      
		      	if(n==1){
	          		
	          		holder.ibOrderUp.setVisibility(View.INVISIBLE); 
	            	holder.ibOrderDown.setVisibility(View.INVISIBLE);
	          	}	          	
	          	else if(order==0) {
	            	
	            	holder.ibOrderUp.setVisibility(View.INVISIBLE);
	            	holder.ibOrderDown.setVisibility(View.VISIBLE);
	            }
	            else if(order==n-1) {
	            	
	            	holder.ibOrderDown.setVisibility(View.INVISIBLE);
	            	holder.ibOrderUp.setVisibility(View.VISIBLE);
	            	
	            }
	            else {
	            	
	            	holder.ibOrderUp.setVisibility(View.VISIBLE); 
	            	holder.ibOrderDown.setVisibility(View.VISIBLE);
	            
	            }
		      
		      holder.tvRemoteDBName.setText(dbInfo.getDataBaseName(remoteDb.getDbName())+" ("+remoteDb.getDbName()+")");
		      holder.tvRemoteDBDesc.setText(dbInfo.getDataBaseTerritory(remoteDb.getDbName()));
		      
		      holder.ibActive.setBackgroundResource(android.R.drawable.ic_popup_sync);
		      
		      holder.ibOrderUp.setId(order);
		      holder.ibOrderUp.setTag(filum);
		      holder.ibOrderDown.setId(order);
		      holder.ibOrderDown.setTag(filum);
		      
		      
		      if(!remoteDb.isEnabled()) holder.ibActive.setBackgroundResource(android.R.drawable.ic_delete);
		      else holder.ibActive.setBackgroundResource(android.R.drawable.ic_popup_sync);
		        	  
		    	  if(holder.ibActive!=null){
			
		    		  
		    		  holder.ibActive.setId(remoteDb.getDbId());
			      
		    		  holder.ibActive.setOnClickListener(new OnClickListener() {
						
						public void onClick(View v) {
									
							boolean state=dbH.changeDBFilumState(v.getId());
							if(!state) ((ImageButton) v).setBackgroundResource(android.R.drawable.ic_delete);
							else ((ImageButton) v).setBackgroundResource(android.R.drawable.ic_popup_sync);
							
						}
					}); 

		    	  
		    	 }
		    	  
		    	  if(holder.ibOrderUp!=null){
		    		  
		    		  holder.ibOrderUp.setOnClickListener(new OnClickListener() {
							
							public void onClick(View v) {
										
								dbH.moveUpDBFilum(v.getId(), v.getTag().toString());
					    		notifyDataSetChanged();
				
								
							}
						}); 
		    		  
		    		  
		    	  }
		    	  if(holder.ibOrderDown!=null){
		    		  
		    		  holder.ibOrderDown.setOnClickListener(new OnClickListener() {
							
							public void onClick(View v) {
						
								dbH.moveDownDBFilum(v.getId(), v.getTag().toString());
								notifyDataSetChanged();
								
								
							}
						}); 
		    		  
		    	  }
		      
	
		      
		      
		      convertView.setFocusableInTouchMode(true);
		      return convertView;
		      
		  }
		  


		  public Object getGroup(int groupPos) 
		  {
		      return dbH.getFilumAtPostion(groupPos);
		  }
		
		  public int getGroupCount() 
		  {
		      return dbH.getFilumCount();
		  }
		
		  public long getGroupId(int groupPos) 
		  {
			  return 0; // citations.get(groupPos).getCitationId();
		  }



		public boolean isChildSelectable(int groupPosition, int childPosition) {
			return true;
		}

		public boolean hasStableIds() {
			return false;
		}

	





	 
	 }    