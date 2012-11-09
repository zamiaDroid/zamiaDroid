package uni.projecte.dataLayer.CitationManager.ListAdapter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

import uni.projecte.R;
import uni.projecte.Activities.Citations.CitationEditor;
import uni.projecte.Activities.Citations.Sampling;
import uni.projecte.controler.CitationControler;
import uni.projecte.controler.ProjectControler;
import uni.projecte.dataLayer.CitationManager.objects.Citation;
import uni.projecte.dataLayer.CitationManager.objects.CitationHandler;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.CheckBox;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SectionIndexer;
import android.widget.TextView;



public class CitationListAdapter extends BaseExpandableListAdapter implements SectionIndexer,Filterable{
  	 
	private LayoutInflater mInflater;
	private ArrayList<Citation> citations;
	private CitationHandler citationsH;
    HashMap<String, Integer> alphaIndexer;
    String[] sections;
    private Context baseContext;
    private String[] childrenText;
    private long projId;
    
    private boolean surenessField;
    private int textColor;
    

    /*
     * Main citation list Adapter
     * 	
     * 	@citationsH: is a dataStructure that contains a the list of citations
     * 	@filteredState: current state   
     * 
     */

	public CitationListAdapter(Context context,CitationHandler citationsH,long projId) {
	 
		 mInflater = LayoutInflater.from(context);
	 
		 this.baseContext=context;
		 this.citationsH=citationsH;
		 this.projId=projId;
		 
		 
		 textColor = baseContext.getResources().getColor(android.R.color.secondary_text_dark);

		 
		 this.surenessField=citationsH.getSurenessField()>-1;
			 
			 
		 this.citations=citationsH.getCurrentCitationList();
		 childrenText=new String[citations.size()];
	
		 
		 //when 
		 if(citationsH.isAlphaOrder()){
		 
	         alphaIndexer = new HashMap<String, Integer>();  	 
			 int size = this.citations.size();
			  
	          for (int i = size - 1; i >= 0; i--) {
	                  
	        	  String element = citations.get(i).getTag();
	        	  if(element!=null && !element.equals("")) alphaIndexer.put(element.substring(0, 1), i);

	          } 
	          
	          Set<String> keys = alphaIndexer.keySet(); // set of letters ...sets
	
	          Iterator<String> it = keys.iterator();
	          ArrayList<String> keyList = new ArrayList<String>(); // list can be
	          // sorted
	
	          while (it.hasNext()) {
	         
	        	  String key = it.next();
	              keyList.add(key);
		          Log.i("Sections","Key: "+keys);

	          }
	
	          Collections.sort(keyList);
	
	          sections = new String[keyList.size()];
	          
	          
	          keyList.toArray(sections);
          
		 }

	 }

	 public View getGroupView(int position, boolean isExpanded, View convertView, ViewGroup parent) {
	 
		 ViewHolder holder;
		 
		 
		 if (convertView == null) {
	    	 
			 convertView = mInflater.inflate(R.layout.complexcitationrow, null);
			 
	    	 holder = new ViewHolder();
	    	 holder.citationDate = (TextView) convertView.findViewById(R.id.citationDate);
	    	 holder.citationTag= (TextView) convertView.findViewById(R.id.citationTag);
	    	 holder.selected= (CheckBox) convertView.findViewById(R.id.cbCitationSelection);
	
	    	 convertView.setTag(holder);
	    	 
	    	 
		 } 
		 else {
			 
			 holder = (ViewHolder) convertView.getTag();
			 
		 }
	
		 
	    	 holder.citationTag.setText(Html.fromHtml(citations.get(position).getTag()));
	    	 
	    	 if(surenessField && citationsH.isNotSure(citations.get(position).getCitationId())) holder.citationTag.setTextColor(Color.RED);
	    	 else holder.citationTag.setTextColor(textColor);
	    	 
	    	 holder.citationTag.setTag(citations.get(position).getCitationId());
	    	 
	    	 if(isExpanded) convertView.setBackgroundColor(baseContext.getResources().getColor(R.color.gris));
	    	 else convertView.setBackgroundColor(baseContext.getResources().getColor(android.R.color.background_dark));
	    	 
	    	 holder.citationDate.setText(citations.get(position).getDate());
	    	 
	    	 holder.selected.setTag((int) citations.get(position).getCitationId());
	    	 holder.selected.setId(citations.get(position).getPosition());
	    	 
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
	         
	         );
	    	 
    	
    	 return convertView;
	 
	 }

	 static class ViewHolder {
    	 TextView citationDate;
    	 TextView citationTag;
    	 ImageView image;
    	 CheckBox selected;

	 }
	 
	public int getPositionForSection(int section) {

			if(sections!=null){
				
				String letter = sections[section];
				return alphaIndexer.get(letter);
				
			}	
			else return 0;
	
			
	  }

	  public int getSectionForPosition(int position) {
	
	          Log.v("getSectionForPosition", "called");
	          return 0;
	  }

	  public Object[] getSections() {
	
	          return sections; // to string will be called each object, to display
	          // the letter
	  }
	  


	public void setChildrenText(String childrenText,int children) {
				this.childrenText[children] = childrenText;
		}


	  public Object getChild(int groupPos, int childPos) 
	  {
	      return childrenText[groupPos];
	  }

	  public long getChildId(int groupPos, int childPos) 
	  {
	      return groupPos;
	  }

	  public int getChildrenCount(int groupPos) 
	  {
	      return 1;
	  }

	  
	  static class ViewChildHolder {
			public ImageButton ibEditButton;
			public ImageButton ibCloneButton;
			public TextView tvCitationName;
		}


	  
		  public View getChildView(final int groupPos, int childPos, boolean isLastChild, View convertView, ViewGroup parent) 
		  {
			  
			  ViewChildHolder holder;
				 			  
		      if (convertView == null){
		    	  
		          LayoutInflater inflater = LayoutInflater.from(baseContext);
		          convertView = inflater.inflate(R.layout.citationfieldrow, null);
		          
		          holder= new ViewChildHolder();
		          holder.tvCitationName = (TextView)convertView.findViewById(R.id.tvCitationFieldName);
			      holder.ibEditButton = (ImageButton)convertView.findViewById(R.id.ibEditCitation);
			      holder.ibCloneButton= (ImageButton)convertView.findViewById(R.id.ibCloneCitation);
		          
		          convertView.setTag(holder);
		      }
		      else{
		    	  
		    	  holder = (ViewChildHolder) convertView.getTag();
		      }
		      
		     
		      
		      
		      if (holder.tvCitationName != null){
			          
		    	  holder.tvCitationName.setText(Html.fromHtml(childrenText[groupPos]));
		    	  
		    	  if(holder.ibEditButton!=null){
			
		    		  holder.ibEditButton.setBackgroundResource(android.R.drawable.ic_menu_edit);
		    		  holder.ibCloneButton.setBackgroundResource(R.drawable.clone_icon);
		    		  
		    		  holder.ibEditButton.setId((int) citations.get(groupPos).getCitationId());
			      
		    		  holder.ibEditButton.setOnClickListener(new OnClickListener() {
						
						public void onClick(View v) {
									
							   	 long id=citations.get(groupPos).getCitationId();
							   	 							   	 						    	 
						    	 ProjectControler rc= new ProjectControler(v.getContext());
						    	 rc.loadProjectInfoById(projId);
						    	 citationsH.setListPosition(groupPos);					    	 
						    	 
								Intent intent = new Intent(v.getContext(), CitationEditor.class);
					       
							 			Bundle b = new Bundle();
							 			b.putString("rsName", rc.getName());
							 			intent.putExtras(b);
							 			b = new Bundle();
							 			b.putLong("id", rc.getProjectId());
							 			intent.putExtras(b);
							 			b = new Bundle();
							 			b.putString("rsDescription", rc.getThName());
							 			intent.putExtras(b);
							 			b = new Bundle();
							 			b.putLong("idSample", id);
							 			intent.putExtras(b);
							 			
							 			b= new Bundle();
							 			b.putInt("count", citationsH.getMainCitationList().size());
							 			intent.putExtras(b);
							 			
							 			b=new Bundle();
							 			b.putInt("position",citationsH.getCitationPosition(id));
							 			intent.putExtras(b);
							 		
							 			
							 		  ((Activity) v.getContext()).startActivityForResult(intent, 2);  
								
							
						}
					});
		    		  
		    		  
		    		  holder.ibCloneButton.setOnClickListener(new OnClickListener() {
							
							public void onClick(final View v) {
								

								AlertDialog.Builder builder = new AlertDialog.Builder(baseContext);
									    	
								LayoutInflater cloneInflater = LayoutInflater.from(baseContext);
					            View eulaLayout = cloneInflater.inflate(R.layout.citationclonedialog, null);
					            builder.setView(eulaLayout);
					            final CheckBox dontShowAgain = (CheckBox)eulaLayout.findViewById(R.id.cbCitationCloneCopyValues);

								
							   	String moveCitationQuestion= baseContext.getString(R.string.citationCloneQuestion);

									    	
									   builder.setMessage(moveCitationQuestion)
									    	       .setCancelable(false)
									    	       .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
									    	           public void onClick(DialogInterface dialog, int id) {
									    
									    	        
									    	        	   Double altitude=getAltitude(groupPos);
															
														   	 long citationId=citations.get(groupPos).getCitationId();
														   	 							   	 						    	 
													    	 CitationControler sC=new CitationControler(v.getContext());
													    	 sC.loadCitation(citationId);
													    	 
															Intent intent = new Intent(v.getContext(), Sampling.class);
												       
												 			intent.putExtra("latitude", sC.getLatitude());
												 			intent.putExtra("longitude", sC.getLongitude());
												 			intent.putExtra("altitude", altitude);
												 			intent.putExtra("timestamp", sC.getDate());
												 			
												 			if(dontShowAgain.isChecked()){
												 			
												 				intent.putExtra("citationId",citationId);
												 			
												 			}
												 			
												 			intent.putExtra("id", sC.getProjId());

													 			
												 		  ((Activity) v.getContext()).startActivityForResult(intent, 2); 
									    	        	   
									    	           }
									    	       })
									    	       .setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
									    	           public void onClick(DialogInterface dialog, int id) {
									    	                
									    	        	  dialog.dismiss();

									    	        	  					    	                
									    	           }
									    	       });
									    	AlertDialog alert = builder.create();
									    	
									    	alert.show();
								
								
										
									 
									
								
							}

						
						});
			      
		    	  }
		    	  
		      
		      
		      }
		      
		      
		      convertView.setFocusableInTouchMode(true);
		      return convertView;
		  }
		  
		private Double getAltitude(int groupPos) {

			Double altitude=0.0;
			
			String altitudeLocale=baseContext.getString(R.string.altitudeLabel);
			String exp="<b>"+altitudeLocale+"</b> : ";
			String[] altitudeString=childrenText[groupPos].split(exp);

			
			if(altitudeString.length>=2) {
				
				String[] altitudealtitude=altitudeString[1].split("<br/>");
				
				if(altitudealtitude.length>0) altitude=Double.valueOf(altitudealtitude[0]);
				else altitude=Double.valueOf(altitudeString[0]);
			
			}
			
			return altitude;
		
		}

		  public Object getGroup(int groupPos) 
		  {
		      return citations.get(groupPos);
		  }
		
		  public int getGroupCount() 
		  {
		      return citations.size();
		  }
		
		  public long getGroupId(int groupPos) 
		  {
			  return citations.get(groupPos).getCitationId();
		  }

		public boolean hasStableIds() {
			
			return false;
		}

		public boolean isChildSelectable(int groupPosition, int childPosition) {
			return true;
		}

		public Filter getFilter() {
			return null;
		}





	 
	 }    