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

package uni.projecte.Activities.Thesaurus;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

import uni.projecte.R;
import uni.projecte.R.array;
import uni.projecte.R.id;
import uni.projecte.R.layout;
import uni.projecte.R.string;
import uni.projecte.controler.PreferencesControler;
import uni.projecte.controler.ThesaurusControler;
import uni.projecte.dataLayer.ThesaurusManager.ThesaurusDownloader.ThDownloader;
import uni.projecte.dataLayer.ThesaurusManager.ThesaurusDownloader.ThDownloaderBiocat;
import uni.projecte.dataLayer.ThesaurusManager.ThesaurusDownloader.ThDownloaderSivim;
import uni.projecte.dataTypes.Utilities;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.TextView;
import android.widget.Toast;

public class ThesaurusRemote extends Activity {
	

	   private ExpandableListView exListView;
	   
	   private String url;
	   private long thId;
	   
	   private ProgressDialog pd;
	   
	   String urlThFile;
	   String thName;
	   
	    public static final int DIALOG_DOWNLOAD_PROGRESS = 0;
	    private ProgressDialog mProgressDialog;
	   
	
	    @Override
	   public void onCreate(Bundle state) {
	        super.onCreate(state);
	        
	        Utilities.setLocale(this);

	        setContentView(R.layout.thesaurusimport);
	       
	        exListView= (ExpandableListView) findViewById(R.id.thInternetList);
	       
	        MyExpandableListAdapter mAdapter = new MyExpandableListAdapter();
	        exListView.setAdapter(mAdapter);
	        exListView.setOnChildClickListener(remoteThListener);

	        Display newDisplay = getWindowManager().getDefaultDisplay(); 
    	    int width = newDisplay.getWidth();
    	    
    	    exListView.setIndicatorBounds(width-50, width-10);
	        
	   }
	
	    
	    @Override
	    protected void onStop(){
	       super.onStop();
	    
	    
	    }
	   

		private void createThDialog(String fileName,final String filumLetter) {
	        
		  	final Dialog dialog;
	        
	  		        	
	        	//Context mContext = getApplicationContext();
	    	   	dialog = new Dialog(this);
	    	   	
	        	dialog.setContentView(R.layout.thesauruscreation);
	    	   	dialog.setTitle(R.string.thName);
	    	   	
	    	   	Button createProject = (Button)dialog.findViewById(R.id.bCreateTh);
	    		
	    	   	EditText name=(EditText)dialog.findViewById(R.id.etNameItem);
	    	   	name.setText(fileName);
	    	   	
	    	   	
	    	    createProject.setOnClickListener(new Button.OnClickListener(){
	    	    	             
	    	    	
	    	    	public void onClick(View v)
	    	    	              {

	    	    	                 EditText et=(EditText)dialog.findViewById(R.id.etNameItem);
	    	    	                 
	    	    	                 thName=et.getText().toString();
	    	    	                 urlThFile=url;
	    	    	           
	    	    	                 ThesaurusControler thCntr= new ThesaurusControler(v.getContext());
	    	    	                 thId=thCntr.createThesaurus(thName,"",filumLetter,"bdbc","remote");
	    	    	                 
	    	    	                 if(thId>0){
	    	    	                	 
	    	    	                     importTh();
		    	    	                 dialog.dismiss();
		    	    	                 
	    	    	                	 
	    	    	                 }
	    	    	                 else{
	    	    	                	 
		    	    	                String sameTh=getBaseContext().getString(R.string.sameThName);

	    	    	                 	Toast.makeText(getBaseContext(), 
			    	    	   		              sameTh+" "+thName, 
			    	    	   		              Toast.LENGTH_LONG).show();
			    	    	               	
	    	    	                	 
	    	    	                 }
	    	    	                 
	    	    	        
	    	    	              }
	    	    	             
	    	    });
	    	    
	    	    dialog.show();


	    	 
	    } 

	 private void importTh() {
		 
		 pd = ProgressDialog.show(this, getString(R.string.thLoading), getString(R.string.thLoadingTxt), true,false);
		 
		                 Thread thread = new Thread(){
		  	        	   
		  	        	   
			                 @Override
							public void run() {
			               	  
			               	  
			                	importThThread(); 
			               	  
			                 }
			           };
			           
			           
		   thread.start();
		 
		}
	 
	 private void importThThread(){
		 	 
		 
		 ThesaurusControler thCntr= new ThesaurusControler(this);
		 
		 thName=thName.replace(".", "_");

		 boolean error=thCntr.addThItems(thId,thName, url);

		 if(!error) handler.sendEmptyMessage(0);
		 else handler.sendEmptyMessage(1);

		 
	 }
	 
	 
	 
	 private Handler handler = new Handler() {

			@Override
			public void handleMessage(Message msg) {
				
				
				pd.dismiss();
				
				if (msg.what==0){
					
					Intent intent = new Intent();
			    	
					Bundle b = new Bundle();
					b.putString("thName", thName);
								
					intent.putExtras(b);

					setResult(1, intent);
					
				}
				
				else{
					
				  	Toast.makeText(getBaseContext(), 
  	   		              getBaseContext().getString(R.string.thWrongFile), 
  	   		              Toast.LENGTH_LONG).show();
					
				}
				
		
		        finish();


			}
		};
	 
	 

	    
	    private OnChildClickListener remoteThListener = new OnChildClickListener() {
	    	
	    	public boolean onChildClick(ExpandableListView parent, View v, int groupPosition,int childPosition, long id) {
	    		
	    		if(groupPosition==0){
	    			
		            String[] biocatElems = getResources().getStringArray(R.array.thesaurusFilumsLetters);
		            String filumLetter= biocatElems[childPosition];
	    			
		    		//Utilities.showToast("Cliqued "+filumLetter, v.getContext());

		    		startDownload("Biocat",filumLetter);
		            
	    		}
	    		else if(groupPosition==1){
	    			
		    		startDownload("Sivim","F");

	    		}
	    		
	    		
	    		return true;
	    	}

	    };
	   
		private void showConnectionError() {

			Utilities.showToast(this.getString(R.string.noThConnection), this);
			
		}
    
	    
		 private void startDownload(String db, String filumLetter) {
		       	
		    	ThDownloader thD;
		    	
	         PreferencesControler pC= new PreferencesControler(this);
	         String url ="";
		    	
		    	if(db.equals("Biocat")){
		    		    		
		    		thD=new ThDownloaderBiocat();
		    		
		    		url = ((ThDownloaderBiocat) thD).getURL(filumLetter);
		    		

		    		
		    	}
		    	
		    	else if(db.equals("Sivim")){
		    		
		    		thD=new ThDownloaderSivim();
		    		url = ((ThDownloaderSivim) thD).getURL("F");

		    		
		    	}
		    	
		        new DownloadFileAsync().execute(url,Environment.getExternalStorageDirectory()+"/"+pC.getDefaultPath()+"/Thesaurus/",filumLetter);

		           
		       }

		 	@Override
			protected Dialog onCreateDialog(int id) {
		           switch (id) {
		               case DIALOG_DOWNLOAD_PROGRESS:
		                   mProgressDialog = new ProgressDialog(this);
		                   mProgressDialog.setMessage(this.getString(R.string.downloadingTh));
		                   mProgressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
		                   mProgressDialog.setCancelable(false);
		                   mProgressDialog.show();
		                   return mProgressDialog;
		               default:
		                   return null;
		           }
		       }
		       class DownloadFileAsync extends AsyncTask<String, String, String> {
		          
		    	   private String filumLetter;
		    	   
		           @Override
		           protected void onPreExecute() {
		               super.onPreExecute();
		               showDialog(DIALOG_DOWNLOAD_PROGRESS);
		           }

		           @Override
		           protected String doInBackground(String... aurl) {
		               int count;
		               

		               try {
		               	
		                   String dataP = URLEncoder.encode("downloadthesa", "UTF-8") + "=" + URLEncoder.encode("true", "UTF-8");
		                   dataP += "&" + URLEncoder.encode("format", "UTF-8") + "=" + URLEncoder.encode("XML", "UTF-8");
		                   dataP += "&" + URLEncoder.encode("sinonims", "UTF-8") + "=" + URLEncoder.encode("true", "UTF-8");
		                   dataP += "&" + URLEncoder.encode("tesa", "UTF-8") + "=" + URLEncoder.encode("true", "UTF-8");

		    
		                   URL url = new URL(aurl[0]);
		                   HttpURLConnection conexion = (HttpURLConnection) url.openConnection();
		                   
		                   conexion.setUseCaches (false);
		                   conexion.setDoInput(true);
		                   conexion.setDoOutput(true);
		      
		                   filumLetter=aurl[2];
		                   
		                   conexion.setRequestMethod("GET");
		                   
		               /*    DataOutputStream wr = new DataOutputStream (
		                   conexion.getOutputStream ());
		                   wr.writeBytes (dataP);
		                   wr.flush ();
		                   wr.close (); */
		                   
		                   conexion.connect();
		           
		                   
		                   int lenghtOfFile = 1835632;
		                   
		                   String header=conexion.getHeaderField("Content-Disposition");
		                   
		                   int position= header.indexOf("filename=");
		                   
		                   String fileName=header.substring(position+9);
		                   
		                   mProgressDialog.setMax(lenghtOfFile);
		                  

		                   InputStream input = new BufferedInputStream(url.openStream());
		                   
		                   File f=new File(aurl[1]+fileName);
		                   if (!f.exists()){
		                	   
		                   		f.createNewFile();
		                   	
		                   }

		                   
		                   OutputStream output = new FileOutputStream(aurl[1]+fileName);

		                   byte data[] = new byte[1024];

		                   long total = 0;

		                   while ((count = input.read(data)) != -1) {
		                       total += count;
		                   //    publishProgress(""+(int)((total*100)/lenghtOfFile));
		                       publishProgress(""+(int)total);

		                       output.write(data, 0, count);
		                   }

		                   output.flush();
		                   output.close();
		                   input.close();
		                   
		                   return fileName;
		                   
		               } catch (IOException e) {
		               	
		                   Log.d("THdown", "Host Unresearcheable");
		               	
		               	
		               }
		               catch (Exception e) {
		               	
		               	e.printStackTrace();
		               	
		               	
		              
		               }
		               return null;

		           }
		           @Override
				protected void onProgressUpdate(String... progress) {
		                Log.d("ANDRO_ASYNC",progress[0]);
		                mProgressDialog.setProgress(Integer.parseInt(progress[0]));
		           }

		           @Override
		           protected void onPostExecute(String unused) {
		               dismissDialog(DIALOG_DOWNLOAD_PROGRESS);
		               
		               
		               if(unused==null){
		            	  
		            	 showConnectionError();
		            	   
		               }
		               else{
		            	  
		            	    
			               	File f= new File(Environment.getExternalStorageDirectory()+"/zamiaDroid/Thesaurus/"+unused);
			               
			               	url= f.getAbsolutePath();
				            
				            String name=f.getName();
				            
				            name=name.replace(".xml", "");

				            createThDialog(name,filumLetter);   
		            	   
		               }
		           
		               
		               /*   */
		               
		           }

			
		       }
	    
	    
	    
	    public class MyExpandableListAdapter extends BaseExpandableListAdapter {
	        // Sample data set.  children[i] contains the children (String[]) for groups[i].
	        private String[] groups = {"",""};
	        private String[][] children = {
	                {},{}
	        };
	        
	        public MyExpandableListAdapter(){
	        	
	        	groups[0]=getResources().getString(R.string.dbNameBiocat);
	        	groups[1]=getResources().getString(R.string.dbNameSivim);
	        	
	            String[] biocatElems = getResources().getStringArray(R.array.thesaurusFilums);
	        	children[0]=biocatElems;
		        	
		        	String[] sivim= new String[1];
		        	sivim[0]=biocatElems[0];
		        	
	        	children[1]=sivim;
	        	
	        	
	        }
	        
	        public Object getChild(int groupPosition, int childPosition) {
	            return children[groupPosition][childPosition];
	        }

	        public long getChildId(int groupPosition, int childPosition) {
	            return childPosition;
	        }

	        public int getChildrenCount(int groupPosition) {
	            return children[groupPosition].length;
	        }

	        public TextView getGenericView() {
	            // Layout parameters for the ExpandableListView
	            AbsListView.LayoutParams lp = new AbsListView.LayoutParams(
	                    ViewGroup.LayoutParams.FILL_PARENT, 64);

	            TextView textView = new TextView(ThesaurusRemote.this);
	            textView.setLayoutParams(lp);
	            // Center the text vertically
	            textView.setGravity(Gravity.CENTER_VERTICAL | Gravity.LEFT);
	            // Set the text starting position
	            textView.setPadding(3, 0, 0, 0);
	            return textView;
	        }

	        public View getChildView(int groupPosition, int childPosition, boolean isLastChild,
	                View convertView, ViewGroup parent) {
	            TextView textView = getGenericView();
	            textView.setText(getChild(groupPosition, childPosition).toString());
	            return textView;
	        }

	        public Object getGroup(int groupPosition) {
	            return groups[groupPosition];
	        }

	        public int getGroupCount() {
	            return groups.length;
	        }

	        public long getGroupId(int groupPosition) {
	            return groupPosition;
	        }

	        public View getGroupView(int groupPosition, boolean isExpanded, View convertView,
	                ViewGroup parent) {
	            TextView textView = getGenericView();
	            textView.setText(getGroup(groupPosition).toString());
	            return textView;
	        }

	        public boolean isChildSelectable(int groupPosition, int childPosition) {
	            return true;
	        }

	        public boolean hasStableIds() {
	            return true;
	        }

	    }
	    
	        
}
	    
	        
