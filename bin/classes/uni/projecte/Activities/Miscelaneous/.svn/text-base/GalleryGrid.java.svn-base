package uni.projecte.Activities.Miscelaneous;

import java.util.Date;
import java.util.HashMap;

import uni.projecte.R;
import uni.projecte.controler.PhotoControler;
import uni.projecte.controler.ProjectControler;
import uni.projecte.dataTypes.Utilities;
import uni.projecte.ui.LazyImageAdapter;
import uni.projecte.ui.ViewTagInformation;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Html;
import android.text.format.DateFormat;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;



public class GalleryGrid extends Activity{
	
	
	private static final int SECONDARY_STORAGE=Menu.FIRST;
	private static final int ALLOW_SEC_EXTERNAL_STORAGE=Menu.FIRST+1;
	private static final int MOVE_PHOTOS=Menu.FIRST+2;   

	public final static int REFRESH_IMAGE_LIST = 2;

    private ProjectControler projCnt;
    private PhotoControler photoCnt;

	public GridView lLazyGrid;
    private int thWidth;
    
	private long projId;
	private String projectName;
    private String preSettedLoc;
    
    private HashMap<String, Long> selectedPhotos;

    private LazyImageAdapter lLazyAdapter;
    private boolean secondaryStorage=false;
    
	private ProgressDialog pdMovePhotos;
	private Dialog movePhotosDialog;

	
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    
        Utilities.setLocale(this);
	    setContentView(R.layout.gallerygrid);

        projCnt=new ProjectControler(this);
		photoCnt=new PhotoControler(this);

        
        projId=getIntent().getExtras().getLong("id");
        projCnt.loadProjectInfoById(projId);
        projectName=projCnt.getName();

        preSettedLoc=getIntent().getExtras().getString("idSelection");
        
        secondaryStorage=photoCnt.isSecondaryExternalStorageDefault(projId);
        
        if(preSettedLoc!=null){
        	
        	loadSelectedPhotos();
        	
        }
        
        loadGridView();
        
	}
	
	private void loadSelectedPhotos() {
		
		long photoFieldId=photoCnt.getProjectPhotoFieldId(projId);
		
		selectedPhotos=new HashMap<String,Long>();
		
		String[] ids=preSettedLoc.split(":");
		
		for(int i=1;i<ids.length;i++){
			
			long citationId=Long.valueOf(ids[i]);
			
			String photoPath=photoCnt.getPhotoPathByCitationId(citationId,photoFieldId);
			
			if(!photoPath.equals("")) selectedPhotos.put(photoPath,citationId);
			
		}	
		
		
	}

	/*
	 * 
	 *  Method handles the logic for setting the adapter for the gridview
	 *  
	 */
	
	    private void loadGridView(){
	    
			lLazyGrid = (GridView) findViewById(R.id.gridGallery);
			
			try {
				
		        thWidth = (getResources().getDisplayMetrics().widthPixels-10)/3;
	
		        if(secondaryStorage){
		        	
					lLazyAdapter = new LazyImageAdapter(this.getApplicationContext(),loadImageSlideListener,photoCnt.getSecondayExternalStoragePath(),projectName.replace(" ", "_"),selectedPhotos,thWidth-5);		

		        }
		        else{
		        	
					lLazyAdapter = new LazyImageAdapter(this.getApplicationContext(),loadImageSlideListener,photoCnt.getMainPhotoPath(),projectName.replace(" ", "_"),selectedPhotos,thWidth-5);		

		        }
		        
				lLazyGrid.setAdapter(lLazyAdapter);
				
			} 
			catch (Exception e) {
			
				e.printStackTrace();
			}
	    
	    }
	    
	    
		@Override
		public boolean onCreateOptionsMenu(Menu menu) {

			if(photoCnt.hasSecondaryStorage()){

				if(photoCnt.isSecondaryExternalStorageDefault(projId)) menu.add(0,SECONDARY_STORAGE, 0,R.string.mGalleryShowPrimaryStorage).setIcon(android.R.drawable.ic_menu_gallery);
				else menu.add(0,SECONDARY_STORAGE, 0,R.string.mGalleryShowSecondaryStorage).setIcon(android.R.drawable.ic_menu_gallery);
	    	
				menu.add(0, MOVE_PHOTOS, 0,R.string.mGalleryMovePhotos).setIcon(android.R.drawable.ic_menu_send);
				
				if(photoCnt.isSecondaryExternalStorageDefault(projId)){
					
					menu.add(0, ALLOW_SEC_EXTERNAL_STORAGE, 0,R.string.mUseExternalStorageDisabled).setIcon(android.R.drawable.ic_menu_set_as);
				}
				else{
					
					menu.add(0, ALLOW_SEC_EXTERNAL_STORAGE, 0,R.string.mUseExternalStorage).setIcon(android.R.drawable.ic_menu_set_as);

				}
				
				
			}
	    	
	    	return super.onCreateOptionsMenu(menu);
	    }

		@Override
		public boolean onPrepareOptionsMenu(Menu menu){
			
			if(photoCnt.hasSecondaryStorage()){
			
				if(secondaryStorage)  menu.findItem(SECONDARY_STORAGE).setTitle(R.string.mGalleryShowPrimaryStorage);
		    	else  menu.findItem(SECONDARY_STORAGE).setTitle(R.string.mGalleryShowSecondaryStorage);
				
				if(photoCnt.isSecondaryExternalStorageDefault(projId)){
					
					menu.findItem(ALLOW_SEC_EXTERNAL_STORAGE).setTitle(R.string.mUseExternalStorageDisabled);
				}
				else{
					
					menu.findItem(ALLOW_SEC_EXTERNAL_STORAGE).setTitle(R.string.mUseExternalStorage);

				}
			
			}
	
			return super.onPrepareOptionsMenu(menu);
			
		}

	    
	    @Override
		public boolean onOptionsItemSelected(MenuItem item) {
	    	
	    	
			switch (item.getItemId()) {
		
			case SECONDARY_STORAGE:
				
				if(secondaryStorage) secondaryStorage=false;
				else secondaryStorage=true;

				loadGridView();	

				break;
				
			case MOVE_PHOTOS:
				
				movePhotosDialog();
				break;

				
				
			case ALLOW_SEC_EXTERNAL_STORAGE:

				if(photoCnt.isSecondaryExternalStorageDefault(projId)){
					
					photoCnt.setSecondaryExternalStorageAsDefault(projId, "false");
					secondaryStorage=false;

				}
				
				else {
					
					photoCnt.setSecondaryExternalStorageAsDefault(projId, "true");
					secondaryStorage=true;
				}
				
				loadGridView();	
				break;

		
			default:	
			 			 
				break;
				
			}
		
			return super.onOptionsItemSelected(item);
		}
	    
		private void movePhotosDialog() {
			
			movePhotosDialog=new Dialog(this);
			movePhotosDialog.setContentView(R.layout.movephotosdialog);
			movePhotosDialog.setTitle("Moure fotografies");
				
			    
			final CheckBox cbRemovePhotos=(CheckBox) movePhotosDialog.findViewById(R.id.cbRemovePhotosOnMove);
			final TextView tvMovePhotoInfo=(TextView) movePhotosDialog.findViewById(R.id.tvMovePhotoInfo);
			Button btMovePhoto=(Button) movePhotosDialog.findViewById(R.id.btMovePhotos);
		    final RadioGroup radGrp = (RadioGroup) movePhotosDialog.findViewById(R.id.rgMovePhotoOptions);
			    
		      
			movePhotosDialog.show();
				
			btMovePhoto.setOnClickListener(new OnClickListener() {
					
				public void onClick(View v) {
						
					RadioButton rbMovePhotoToSec=(RadioButton) movePhotosDialog.findViewById(R.id.rbMovePhotoToSec);

					movePhotos(rbMovePhotoToSec.isChecked(),cbRemovePhotos.isChecked());
					movePhotosDialog.dismiss();

					
					}
				});
			
			
			
			    radGrp.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
			      public void onCheckedChanged(RadioGroup arg0, int id) {
			       
			    	  String tvInfo="";
			    	  
			    	  switch (id) {
				        case R.id.rbMovePhotoToSec:
	
						    tvInfo=String.format(getString(R.string.movePhotosInfo),photoCnt.getSecondayExternalStoragePath());
						    tvMovePhotoInfo.setText(Html.fromHtml(tvInfo));
						    
				          break;
				        case R.id.rbMovePhotoToPrim:
	
				    	    tvInfo=String.format(getString(R.string.movePhotosInfo),photoCnt.getMainPhotoPath());
						    tvMovePhotoInfo.setText(Html.fromHtml(tvInfo));
						    
						    
				          break;
				        default:
	
				          break;
			        }
			      }
			    });
			
			   if(!secondaryStorage) radGrp.check(R.id.rbMovePhotoToSec);
			   else radGrp.check(R.id.rbMovePhotoToPrim);
			
		}

		protected void movePhotos(final boolean secondaryStorage, final boolean copyPhoto) {

		    pdMovePhotos = new ProgressDialog(this);
		    pdMovePhotos.setCancelable(true);
		    pdMovePhotos.setTitle(getString(R.string.dialogGalleryMovingPhotos));
		    pdMovePhotos.setMessage("photoName");

		    pdMovePhotos.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
		    pdMovePhotos.setProgress(0);
		    pdMovePhotos.setMax(lLazyAdapter.size());
		    pdMovePhotos.show();
			
			
				Thread thread = new Thread(){
			  	        	   
					@Override
					public void run() {
				               	  
						photoCnt.movePhotosToSecondaryStorage(projId,secondaryStorage,copyPhoto,handlerMove);
				               	  
				    }
				};
				           
				           
			   thread.start();
		
		}	
		
		   /*
		    * This handler handles the result of the move dialog:
		    * 
		    * 	+ Incrementing progress bar and setting fileName
		    * 	+ Ending the process msg.getData().getString("fileName")
		    * 
		    */
		   
			 private Handler handlerMove = new Handler() {

					@Override
					public void handleMessage(Message msg) {	

						if(msg.getData().getString("fileName")==null){
							
							secondaryStorage=msg.getData().getBoolean("secondaryStorage");
							pdMovePhotos.dismiss();
							loadGridView();
							
						}
						else{
							
							pdMovePhotos.setMessage(msg.getData().getString("fileName"));
							pdMovePhotos.incrementProgressBy(1);
							
						}
					}
				};
		

		private OnClickListener loadImageSlideListener = new OnClickListener()
	    {
	        public void onClick(View v)
	        {                        
	        	
	        	ViewTagInformation tagInfo=(ViewTagInformation) v.getTag();
	        	
	        	try {
	        		
	        			//lLazyAdapter.finalize();
						
						Intent intent = new Intent(v.getContext(), ImageGallery.class);
				 	       
			 			Bundle b = new Bundle();
			 			b = new Bundle();
			 			b.putLong("id", projId);
			 			intent.putExtras(b);
			 			
			 			b = new Bundle();
			 			b.putInt("position", tagInfo.position);
			 			intent.putExtras(b);
			 			
			 	        
			 	        if(preSettedLoc!=null){
	
			 	        	b=new Bundle();
			 	        	b.putString("idSelection", preSettedLoc);
			 	        	intent.putExtras(b);
			 	        	
			 	        }

			 	        startActivityForResult(intent,REFRESH_IMAGE_LIST);
		 		
			 	      
					
				} catch (Throwable e) {

					e.printStackTrace();
				}
	        	
	    		
	        	
	        }
	       
	    };	
	    
	    @Override
		protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
	        
	    	 super.onActivityResult(requestCode, resultCode, intent);
	    	

	         switch(requestCode) {
	         
	         
	         case REFRESH_IMAGE_LIST :
	             
     			if(lLazyAdapter!=null){

     				try {
						
						lLazyAdapter.finalize();
						
					} catch (Throwable e) {

						e.printStackTrace();
					}
				
     			}
     		
     			loadGridView();
	             
	             break;
	             
	          
	         	default:
	             
	         }
	         
	    }
	

}





