package uni.projecte.dataLayer.ProjectManager.ListAdapters;

import java.util.ArrayList;

import uni.projecte.R;
import uni.projecte.Activities.Projects.ProjectInfo;
import uni.projecte.controler.ProjectControler;
import uni.projecte.controler.ProjectSecondLevelControler;
import uni.projecte.dataLayer.ProjectManager.objects.Project;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;




public class ProjectBaseListAdapter extends BaseAdapter{

	private LayoutInflater mInflater;
	private Context parentContext;
	private ArrayList<Project> projectList;
	private String defaultProject;
	public String getDefaultProject() {
		return defaultProject;
	}




	private RadioButton rbLastChosenProj;
	private long projId;
	private Handler handUpdatePref;

	
	public ProjectBaseListAdapter(Context baseContext, ArrayList<Project> projectList,long projId, String defaultProject, Handler handUpdatePref) {
	
		this.projectList=projectList;
		this.parentContext=baseContext;
		mInflater = LayoutInflater.from(baseContext);
		this.defaultProject=defaultProject;
		this.projId=projId;
		this.handUpdatePref=handUpdatePref;
	
	}
	
	
	public int getCount() {
		
		return this.projectList.size();
	}

	public Object getItem(int position) {
		
		return projectList.get(position).getProjName();
	}

	public long getItemId(int position) {

		return projectList.get(position).getProjId();
		
	}

	public View getView(final int position, View convertView, ViewGroup parent) {
		 
		ViewHolder holder;
    	 
    	 if (convertView == null) {
	    	 
    		 convertView = mInflater.inflate(R.layout.project_list_row, null);
    		 
	    	 holder = new ViewHolder();
	    	 holder.tvProjName = (TextView)convertView.findViewById(R.id.tvProjectName);
	    	 holder.cbDefaultProj = (RadioButton)convertView.findViewById(R.id.cBedit);
	    	 holder.ibEditProj = (ImageButton)convertView.findViewById(R.id.ibEditProject);
	    	 holder.ibSyncroProj=(ImageButton)convertView.findViewById(R.id.ibSyncroProject);

	    	 convertView.setTag(holder);
	    	 
    	 }
    	 else {
    		 
    		 holder = (ViewHolder) convertView.getTag();
    		 
    	 }
    	 
    	 
    	 
    	 String projName=projectList.get(position).getProjName();
    	 holder.tvProjName.setText(projName);
    	 
    	 String projType=projectList.get(position).getProjType();
    	 
    	 if(projType.startsWith("remote_")) holder.ibSyncroProj.setVisibility(View.VISIBLE);
    	 

     	boolean isDefault=projName.equals(defaultProject);
     	
     	if(isDefault) rbLastChosenProj=holder.cbDefaultProj;
     	
     	holder.cbDefaultProj.setChecked(isDefault);
         
     	holder.cbDefaultProj.setOnClickListener(new OnClickListener() {  
         	
         	public void onClick(View v) { 
         	
         		RadioButton cBox = (RadioButton) v; 
         		View e=(View) v.getParent().getParent();
         		TextView tv= (TextView) e.findViewById(R.id.tvProjectName);
	  
         		
         		ProjectControler rc=new ProjectControler(v.getContext());
         		rc.loadResearchInfoByName(tv.getText().toString());
         			
         		projId=rc.getProjectId();
         		 	
         		Bundle b=new Bundle();
         		b.putLong("projId",projId);
         		Message msg=new Message();
         		msg.setData(b);
         		
         		handUpdatePref.sendMessage(msg);

         		defaultProject=tv.getText().toString();
         		
         		if(rbLastChosenProj!=null) rbLastChosenProj.setChecked(false);
         	    rbLastChosenProj=cBox;
         	    
         	    
         	} 
         	
         
         }
         
         );
         
     	
     	holder.tvProjName.setOnClickListener(new OnClickListener() {  
         	
         	public void onClick(View v) { 
         	
         		
         		View e=(View) v.getParent().getParent();
         		RadioButton cBox= (RadioButton) e.findViewById(R.id.cBedit);
	  
         		defaultProject=((TextView)v).getText().toString();

        		ProjectControler rc=new ProjectControler(v.getContext());
         		rc.loadResearchInfoByName(defaultProject);
         			
         		projId=rc.getProjectId();
         		 	
         		//insert into Bundle?
         		Bundle b=new Bundle();
         		b.putLong("projId",projId);
         		Message msg=new Message();
         		msg.setData(b);
         		
         		handUpdatePref.sendMessage(msg);
         		
         		if(rbLastChosenProj!=null) rbLastChosenProj.setChecked(false);
         	    rbLastChosenProj=cBox;
         	    cBox.setChecked(true);
           		
         	} 
         	
         
         }
         
         );
         	
     	
     	holder.ibEditProj.setOnClickListener(new OnClickListener() {  
	            	
	            	public void onClick(View v) { 
	            	
	            		View e=(View) v.getParent().getParent();
	            		TextView tv= (TextView) e.findViewById(R.id.tvProjectName);
		  
	            		ProjectControler projCnt=new ProjectControler(v.getContext());
	            		projCnt.loadResearchInfoByName(tv.getText().toString());

		   			    Intent intent = new Intent(v.getContext(), ProjectInfo.class);
		   			       
		   			    Bundle b = new Bundle();
		   			    b.putLong("Id", projCnt.getProjectId());
		   			    intent.putExtras(b);
		   	 			
		   			    b.putString("projName", projCnt.getName());
		   			    intent.putExtras(b);
		   			    intent.putExtras(b);
		   			    
		   			    b = new Bundle();
		   			    b.putString("projDescription", projCnt.getThName());
		   			    intent.putExtras(b);
		   	 		
		   			    ((Activity)v.getContext()).startActivityForResult(intent, 1); 
		   			     	
	            	} 
	            	
	            
	            }
	            
	            );
         	
         	
         	
     		holder.tvProjName.setOnLongClickListener(new OnLongClickListener() {
     		    

					public boolean onLongClick(final View v) {

						
						AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
     		    	
     		    	
     		    	builder.setMessage(R.string.deleteProjQuestion)
     		    	       .setCancelable(false)
     		    	       .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
     		    	           public void onClick(DialogInterface dialog, int id) {
     		    
     		    	      	   	 TextView tv=(TextView)v;
     		    	    	   	 String projName=(String)tv.getText();
     		    	        	 int status=removeProject(projName);
     		    	        	 
     		    	        	 if(status>=0){
     		    	        		 
     		    	        		 if(projName!=null) {
     		    	        		 
	        		    	        		 if(defaultProject.equals(projName)) {
	        		    	        			 
	        		    	        		
	        		    	        			rbLastChosenProj=null;
	        		    	        		
	        		    	        			Bundle b=new Bundle();
	        		    	             		b.putLong("projId",projId);
	        		    	             		b.putBoolean("removeProject",true);
	        		    	             		
	        		    	             		Message msg=new Message();
	        		    	             		msg.setData(b);
	        		    	             		
	        			            	        defaultProject="";
	        			            	        projId=-1;
	        			            	        
	        			            	        
	        		    	             		handUpdatePref.sendMessage(msg);

	        		    	        	 
	        		    	        		 }
     		    	        		 
     		    	        		 }
     		    	        
     		    	           }

     		    	        	   
     		    	           }
     		    	       })
     		    	       .setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
     		    	           public void onClick(DialogInterface dialog, int id) {
     		    	                
     		    	        	   dialog.cancel();
     		    	                
     		    	           }
     		    	       });
     		    	AlertDialog alert = builder.create();
     		    	
     		    	alert.show();
     		    	   
     		    	   return true;
					}
     		    
     		    
     		    }
         	
         	);
    	 
    	 
    	 
		return convertView;
		
	
		 
		 
	}
	
    private int removeProject(String projName){
    	
    	int status=0;
    	
    	ProjectSecondLevelControler projCnt= new ProjectSecondLevelControler(parentContext);
    	projCnt.loadResearchInfoByName(projName);
    	
    	status=projCnt.removeProject(projCnt.getProjectId());
    	
 	   	
 	   	if (status==-1){
 	   		
	 	   	 Toast.makeText(parentContext, 
	 	              R.string.hasCitations, 
	 	              Toast.LENGTH_LONG).show();
	 	   	 
	 	   	 status=-1;
 	   		
 	   	}
 	   	 
 	   	return status;
    	
    	
    }
	
	
	
	
	 static class ViewHolder {
		 
		   	TextView tvProjName;
	        RadioButton cbDefaultProj;
	        ImageButton ibEditProj;
	        ImageButton ibSyncroProj;

	 }




	public void setDefaultRadioButton(RadioButton rb) {

		rbLastChosenProj=rb;
		
	}
	
	
}
