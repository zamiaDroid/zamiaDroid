package uni.projecte.Activities.Miscelaneous;

import java.util.HashMap;

import uni.projecte.R;
import uni.projecte.controler.PhotoControler;
import uni.projecte.controler.ProjectControler;
import uni.projecte.dataLayer.utils.PhotoUtils;
import uni.projecte.dataTypes.Utilities;
import uni.projecte.ui.LazyImageAdapter;
import uni.projecte.ui.ViewTagInformation;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Html;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.GridView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;




public class GalleryGrid extends Activity{
	
	
	private static final int SECONDARY_STORAGE=Menu.FIRST;
	private static final int ALLOW_SEC_EXTERNAL_STORAGE=Menu.FIRST+1;
	private static final int MOVE_PHOTOS=Menu.FIRST+2;   
	private static final int REMOVE_PHOTOS_CACHE=Menu.FIRST+3;   

	public final static int REFRESH_IMAGE_LIST = 2;
	
	private static final int GRID_COLUMNS=3;

    private ProjectControler projCnt;
    private PhotoControler photoCnt;

	public GridView lLazyGrid;
	public Button btSecondaryStorage;
    private int thWidth;
    
	private long projId;
	private String projectName;
    private String preSettedLoc;
    
    private HashMap<String, Long> selectedPhotos;

    private LazyImageAdapter lLazyAdapter;
    private boolean secondaryStorage=false;
    private String storagePath;
    
    
	private ProgressDialog pdMovePhotos;
	private Dialog movePhotosDialog;
	private ProgressBar progBar;
	private TextView tvGalleryCount;

	
	
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
        
        progBar=(ProgressBar)findViewById(R.id.progsBarGalleryLoading);
        tvGalleryCount=(TextView)findViewById(R.id.tvGalleryCount);
        btSecondaryStorage=(Button)findViewById(R.id.btSecondaryStorage);
        
        btSecondaryStorage.setOnClickListener(btChangeStorageListener);
        
        secondaryStorage=photoCnt.isSecondaryExternalStorageDefault(projId);
        
        loadSelectedPhotos();
        
                
	}
	
	public void onStop(){
		
		super.onStop();
		
		try {
			
			lLazyAdapter.finalize();
			
		} catch (Throwable e) {

			e.printStackTrace();
		}
		
	}
	
	public void onConfigurationChanged(Configuration newConfig) {
		  
		super.onConfigurationChanged(newConfig);

		loadGridView();
	
	}
	
	
	private void updateStorageState(){
		
		
        if(lLazyAdapter!=null) tvGalleryCount.setText("("+lLazyAdapter.getCount()+")");

		
		if(photoCnt.hasSecondaryStorage()) {
			
			btSecondaryStorage.setVisibility(View.VISIBLE);
			
			if(secondaryStorage) btSecondaryStorage.setText(getString(R.string.btStorageSec));
			else btSecondaryStorage.setText(getString(R.string.btStoragePrim));
			
		}
		else btSecondaryStorage.setVisibility(View.GONE);
		
		
	}
	
	public OnClickListener btChangeStorageListener = new OnClickListener() {
		
		public void onClick(View v) {

			if(secondaryStorage) secondaryStorage=false;
			else secondaryStorage=true;

			progBar.setVisibility(View.VISIBLE);
			
			loadGridView();
			updateStorageState();
			
			progBar.setVisibility(View.GONE);
			
		}
	};

	
	private void loadSelectedPhotos() {
		
		final PhotoControler photoCnt=new PhotoControler(this);
		
		progBar.setVisibility(View.VISIBLE);
		
	/* thread in background that load photos */			
		
		new Thread(new Runnable() {
			  public void run() {
				  
				  
				  //subset of selected photos
				  if(preSettedLoc!=null){
		
					  /*  */
																
						selectedPhotos=new HashMap<String,Long>();
						
						String[] ids=preSettedLoc.split(":");
						
						selectedPhotos=photoCnt.getSelectedPhotos(projId, ids);
					
				  }
				  
				  
				  loadImageGridHandler.sendEmptyMessage(0);
		
			  }
			    
				
					}).start();
		
	}
	

	/*
	 * 
	 *  Method handles the logic for setting the adapter for the gridview
	 *  
	 */
	
	    private void loadGridView(){
	    	
    		if(secondaryStorage) storagePath=photoCnt.getSecondayExternalStoragePath();
    		else storagePath=photoCnt.getMainPhotoPath();
    		
	    
			lLazyGrid = (GridView) findViewById(R.id.gridGallery);
					
			try {

				thWidth = (getResources().getDisplayMetrics().widthPixels-10)/GRID_COLUMNS;
				
				Log.i("Photo","Size: "+getResources().getDisplayMetrics().widthPixels+" GRID_COLUMNS"+GRID_COLUMNS+" Photo: "+thWidth);

				lLazyAdapter = new LazyImageAdapter(getApplicationContext(),loadImageSlideListener,storagePath,projectName.replace(" ", "_"),selectedPhotos,thWidth-5);		

				lLazyGrid.setAdapter(lLazyAdapter);
				lLazyGrid.setOnScrollListener(scrollListener);

			} 
			catch (Exception e) {

				e.printStackTrace();
			}
	    
	    }
	    
	  
	    
	    private Handler loadImageGridHandler = new Handler() {

	    	@Override
			public void handleMessage(Message msg) {
	    		
	    		
	    		progBar.setVisibility(View.GONE);
	    		
	            loadGridView();
	            	            
	            updateStorageState();


            }
	    };
	    
	    
	    public OnScrollListener scrollListener = new OnScrollListener() {
			
	    	public void onScrollStateChanged(AbsListView view, int scrollState) {
	    	    
	    		switch (scrollState) {
	    	    
		    	    case OnScrollListener.SCROLL_STATE_IDLE:
		    	        
		    	    	lLazyAdapter.setBusy(false);
		    	        
		    	        try {
		    	        	
		    	        	Log.i("Images","Netejant Cua pendent");
							lLazyAdapter.clearHandler();
							
						} 
		    	        catch (Throwable e) {
	
							e.printStackTrace();
							
						}
		    	        
		    	        lLazyAdapter.notifyDataSetChanged(); 
		    	        		
		    	        break;
		    	        
		    	        
		    	    case OnScrollListener.SCROLL_STATE_TOUCH_SCROLL:
		    	    	
		    	    	lLazyAdapter.setBusy(true);
	
		    	        break;
		    	        
		    	        
		    	    case OnScrollListener.SCROLL_STATE_FLING:
	
		    	    	lLazyAdapter.setBusy(true);
	
		    	        break;
		    	        
	    	    }
	    	}
			
			public void onScroll(AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {

				
			}
		};

    	

	    
	    
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
			
			menu.add(0, REMOVE_PHOTOS_CACHE, 0,R.string.mRemovePhotoCache).setIcon(android.R.drawable.ic_menu_close_clear_cancel);

	    	
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

				loadSelectedPhotos();

				break;
				
			case MOVE_PHOTOS:
				
				movePhotosDialog();
				break;
				
			case REMOVE_PHOTOS_CACHE:
				
				int removed=photoCnt.removeProjectThumbs(projId, projectName);
				
				Utilities.showToast("S'han esborrat "+removed+" fotos", this);
				
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
	    
	    /*
	     * Creates a dialog with all information and options to move
	     * the photos to a secondary storage such as external_sd 
	     * 
	     */
	    
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
				               	  
						photoCnt.movePhotosToSecondaryStorage(projId,storagePath,selectedPhotos,secondaryStorage,copyPhoto,lLazyAdapter.getAvailableImageList(),handlerMove);
				               	  
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
							progBar.setVisibility(View.GONE);
							
							btSecondaryStorage.setVisibility(View.VISIBLE);
							
							if(secondaryStorage) btSecondaryStorage.setText(getString(R.string.btStorageSec));
							else btSecondaryStorage.setText(getString(R.string.btStoragePrim));
							
							loadSelectedPhotos();

							
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
			 			
			 			b = new Bundle();
			 			b.putString("storagePath", storagePath);
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
	         
	         
	         case REFRESH_IMAGE_LIST:
	             
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





