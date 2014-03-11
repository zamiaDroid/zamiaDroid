package uni.projecte.Activities.Projects;

import java.io.File;
import java.io.FilenameFilter;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import uni.projecte.R;
import uni.projecte.controler.BackupControler;
import uni.projecte.controler.PreferencesControler;
import uni.projecte.controler.ProjectControler;
import uni.projecte.dataTypes.ProjectBackup;
import uni.projecte.dataTypes.Utilities;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;



public class ProjectBackupList extends Activity {
	
	
	private PreferencesControler pC;
	private ProgressDialog pd;

	
	private Dialog dialog;
	
	private ListView lV;

	   private ArrayList<ProjectBackup> elements = null;
	
    public static final int DIALOG_DOWNLOAD_PROGRESS = 0;

    private String prType;
    private String prName;
    private long createdProjId;
	
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
       
        Utilities.setLocale(this);
	    setContentView(R.layout.project_backup_list);	
     
	    //filling list with remote and local projects
	    lV= (ListView) findViewById(R.id.backupList);

	    pC= new PreferencesControler(this);
			
		loadLocalProjects();
		
        lV.setOnItemClickListener(theListListener);

        
	}
	
		private void loadLocalProjects() {
			

		     if(isSdPresent()) fillFileList(new File(Environment.getExternalStorageDirectory()+"/"+pC.getDefaultPath()+"/Backups/").listFiles(new ZIPFilter()));
		        else {
		        	
		        	Toast.makeText(getBaseContext(), 
		                    R.string.noSdAlert, 
		                    Toast.LENGTH_SHORT).show();
		        	
		        }
			
		}

		 
		  public static boolean isSdPresent() {

		      return android.os.Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED);
		
		  }
		  
		  private void fillFileList(File[] listFiles) {
			  
			

			
			        elements = new ArrayList<ProjectBackup>();
			      //  elements.add(getString(R.string.root));
			        
			        for( File archivo: listFiles){
			        	
			        	  Date d = new Date(archivo.lastModified());
			              SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
			              String dateString = sdf.format(d);
			        	
			            elements.add(new ProjectBackup(archivo.getName(), dateString));
			            
			        }
			        
					BackupProjectAdapter eA=new BackupProjectAdapter(this,elements);		
					lV.setAdapter(eA);

			    
			
		}
		
		    class ZIPFilter implements FilenameFilter {
		    	  
		    	  
	              public boolean accept(File dir, String name) {

	                return (name.endsWith(".zip"));

	        }
            
	              
	    }
		    
		    private static class BackupProjectAdapter extends BaseAdapter {
		    	 
		    	private LayoutInflater mInflater;
		    	private ArrayList<ProjectBackup> elements;

		    	 public BackupProjectAdapter(Context context,ArrayList<ProjectBackup> elementList) {
		    	 
		    		 mInflater = LayoutInflater.from(context);
		    	 	 	this.elements=elementList;

		    	 }

		    	 public int getCount() {
		    		 return elements.size();
		    	 }

		    	 public Object getItem(int position) {
		    	 return position;
		    	 }

		    	 public long getItemId(int position) {
		    	 return position;
		    	 }

		    	 public View getView(int position, View convertView, ViewGroup parent) {
		    	 ViewHolder holder;
		    	 if (convertView == null) {
			    	 
		    		 convertView = mInflater.inflate(R.layout.backup_list_row, null);
		    		 
			    	 holder = new ViewHolder();
			
			    	 
			    	 holder.text2 = (TextView) convertView
			    	 .findViewById(R.id.tvProjBackName);
			    	 
			    	 holder.text3 = (TextView) convertView
			    	 .findViewById(R.id.tvProjBackDate);
			    	 
			    	 holder.disc = (ImageView) convertView.findViewById(R.id.projBackImg);
		
			    	 convertView.setTag(holder);
			    	 
		    	 } 
		    	 else {
		    		 holder = (ViewHolder) convertView.getTag();
		    	 }
		    	   
		         holder.disc.setImageResource(android.R.drawable.ic_menu_save);
		    	 holder.text2.setText(elements.get(position).getFileName());
		    	 holder.text3.setText(elements.get(position).getDate());
		    	 
		    	 
		    	 holder.text2.setOnLongClickListener(new OnLongClickListener() {
	        		    

						public boolean onLongClick(final View v) {

							
							AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
	        		    	
	        		    	builder.setMessage(R.string.rmFile)
	        		    	       .setCancelable(false)
	        		    	       .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
	        		    	           public void onClick(DialogInterface dialog, int id) {
	        		    
	        		    	      	   	 TextView tv=(TextView)v;
	        		    	    	   	 String projName=(String)tv.getText();
	        		    	    			
	        		    	    	   	 String path = Environment.getExternalStorageDirectory() +"/ZamiaDroid/Backups/"+projName; 
	        		    		        	 
	        		    	    	   	 File f= new File(path);
	        		    	    	   	 f.delete();
	        		    
	        		    	    			
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

		    	 static class ViewHolder {
			    	 TextView text2;
			    	 TextView text3;
			    	 ImageView disc;
		    	 }
		    	 
		    	 	
		    	 
		  
		    	 
		    	 }    
		       

		    
	
	  public OnItemClickListener theListListener = new OnItemClickListener() {
		    
		  public void onItemClick(android.widget.AdapterView<?> parent, View v, int position, long id) {
		        
			  	TextView tv=(TextView) v.findViewById(R.id.tvProjBackName);
			   	 
			  	String projName=(String)tv.getText();
			  	
			  	String niceProjectName=projName.replaceAll("_[0-9][0-9][0-9][0-9]-[0-9][0-9]-[0-9][0-9]-[0-9][0-9]-[0-9][0-9]-[0-9][0-9]", "");
			  	
			 // 	String[] projNameList=projName.split("_");
			  	
			  	
			  	importBackup(niceProjectName,projName);
	
		    }

	
	    };
	    
	    
			    
			 
		
	    private void importBackupDialog(final String fileName,final long projId,final String projName){
	    	
	    	final BackupControler bC= new BackupControler(this);
	    	
			 pd = ProgressDialog.show(this, getBaseContext().getString(R.string.backupLoading),getBaseContext().getString(R.string.backupLoadingTxt),true,false);

             Thread thread = new Thread(){
	        	   
                 @Override
				public void run() {
                	 
                	 createdProjId=bC.importProject(fileName, projId,projName);
     		  		 handler.sendEmptyMessage(0);

                 }
           };
           
           	thread.start();
	    }	

	    
	    
	private void importBackup(final String projName, final String fileName) {

			
			final ProjectControler rC= new ProjectControler(this);
		  	
	  	
		  	
		  	//project with @projName exists
		  	
		  	if(rC.projectExists(projName.replace(".zip", ""))){ 
		  		
		  		
			  	//we show a dialog that gives you two options:
			  		//a)create a project with a different name
			  		//b)import only the citations to the project with @projName
	    	   	dialog = new Dialog(this);
	    	  
	        	dialog.setContentView(R.layout.backup_list);
	    	   	
	    	   	Button createProject = (Button)dialog.findViewById(R.id.btImportBackup);

	    	   	final EditText name=(EditText)dialog.findViewById(R.id.etBackProjName);
    	   		final RadioButton rbBackImportCit = (RadioButton) dialog.findViewById(R.id.rbBackImportCit);
	    	    final RadioButton rbBackNewProj = (RadioButton) dialog.findViewById(R.id.rbBackupNewProject);
	    	    
	    	    rbBackImportCit.setChecked(true);
	    	    name.setVisibility(View.GONE);
	    	    
   	    
    	    
	    	    rbBackImportCit.setOnClickListener(new RadioButton.OnClickListener(){
		             
	    	    	public void onClick(View v){

			    	    name.setVisibility(View.GONE);

	    	    	                
	    	    	    }
	    	    	             
	    	    });
	    	    
	    	    rbBackNewProj.setOnClickListener(new RadioButton.OnClickListener(){
		             
	    	    	public void onClick(View v){

			    	    name.setVisibility(View.VISIBLE);

	    	    	                
	    	    	    }
	    	    	             
	    	    });
	    	    
	    

	    	    createProject.setOnClickListener(new Button.OnClickListener(){
	    	    	             
	    	    	
	    	    	public void onClick(View v)
	    	    	              {

	    	    	                 EditText et=(EditText)dialog.findViewById(R.id.etBackProjName);
	    	     			    	 String proposedName=et.getText().toString();

	    	     			    	 //only citations
	    	    			    	 if(rbBackImportCit.isChecked()){
	    	    			    		 
    	    					  		prName=projName.replace(".zip", "");
    	    					  		importBackupDialog(fileName.replace(".zip", ""), rC.getProjectId(),prName);	  		
	    	    					  	dialog.dismiss();

	    	    					  		    	    			    		 
	    	    			    	 }
	    	    			    	 else if(rbBackNewProj.isChecked()){
	    	    			    	
	    	    			    		if(rC.projectExists(proposedName)) {
	    	    			    			
	    	    			    			Utilities.showToast(getBaseContext().getString(R.string.sameNameProject), getBaseContext());
	    	    			    			
	    	    			    		}
	    	    			    		else{
	    	    			    			
	    	    			    			if(proposedName.equals("")){
	    	    			    				
	    	    			    				Utilities.showToast(getBaseContext().getString(R.string.projNameMissing),getBaseContext());
	    	    			    				
	    	    			    			}
	    	    			    			else{
	    	    			    				
	    	    			    				importBackupDialog(fileName.replace(".zip", ""), -1,proposedName);	 
		    	    			    			prName=proposedName;
			    	    					  	dialog.dismiss();
			    	    				  		
	    	    			    			}


	    	    			    		}
	    	    			    		 
	    	    			    	 }
	    	    			    	 else{
	    	    			    		 
	    	    			    		     	    			    		 
	    	    			    	 }
	    	    			    	 
	    	    	                
	    	    	              }
	    	    	             
	    	    });
	    	       	    
	    	    
	    	    dialog.show();
		  		
		  		
		  		
		  	}
		  	else{
		  		
		  		prName=projName.replace(".zip", "");
		  		importBackupDialog(fileName.replace(".zip", ""), -1,prName);
		  		
		  		
		  	}
		  	
		  	 	
			
		}
	
		 private Handler handler = new Handler() {
	
				@Override
				public void handleMessage(Message msg) {	
					
					pd.dismiss();

		
						
						Toast.makeText(getBaseContext(), 
								 String.format(getString(R.string.projSuccesCreated), prName), 
		   	                    Toast.LENGTH_SHORT).show();	
						
						Intent intent = new Intent();
				    	
						Bundle b = new Bundle();
						b.putLong("projId", createdProjId);
						intent.putExtras(b);
	
						setResult(1, intent);
						
						
				        finish();
			   
					
			
	
				}
			};
	    
		  public OnItemClickListener theLocalListListener = new OnItemClickListener() {
			    
			    public void onItemClick(android.widget.AdapterView<?> parent, View v, int position, long id) {
			        
			/*	   	 TextView tv=(TextView) v
			    	 .findViewById(R.id.tvProjRepName);
				   	 
				   	 TextView tvProjType=(TextView) v
			    	 .findViewById(R.id.tvProjRepType);
				   	 
				   	 String projName=(String)tv.getTag();
				   	 prType=tvProjType.getText().toString();
				   	 
				   	 createProjectDialog(projName,"",false); */
				   	 
				   	
			    	
			    }
		    };
		    
		    

	    	      	 

}
