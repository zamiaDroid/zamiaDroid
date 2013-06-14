package uni.projecte.ui;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.HashMap;

import uni.projecte.R;
import uni.projecte.dataLayer.dataStructures.ImageCache;
import uni.projecte.dataLayer.utils.PhotoUtils;
import uni.projecte.dataTypes.Utilities;
import uni.projecte.ui.ImageLoader.ImageLoadListener;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Handler;
import android.os.storage.StorageManager;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.ViewSwitcher;


public class LazyImageAdapter extends BaseAdapter implements ImageLoadListener {

	private static final int PROGRESSBARINDEX = 0;
	private static final int IMAGEVIEWINDEX = 1;

	
	private Context mContext = null;
	private OnClickListener mItemClickListener;
	private Handler mHandler;
	private ImageLoader mImageLoader = null;
	
	private String projName;
	
	private File imageGalleryDir;
	private String storagePath;

	
	private ArrayList<String> availableImageList;  
    private int thMaxSize;
    
    private HashMap<String, Long> selectedPhotos;
    
    private ImageCache imageCache;
    
    private boolean mBusy;

	
	/**
	 * Lazy loading image adapter
	 * @param aContext
	 * @param lClickListener click listener to attach to each item
	 * @param lPath the path where the images are located
	 * @param selectedPhotos 
	 * @param mBusy 
	 * @throws Exception when path can't be read from or is not a valid directory
	 */
	public LazyImageAdapter(
			Context aContext,
			OnClickListener lClickListener,
			String storagePath,
			String projName,
			HashMap<String, Long> selectedPhotos, int thMaxSize
		) throws Exception {
		
		mContext = aContext;
		mItemClickListener = lClickListener;
		imageGalleryDir = new File(storagePath);
		this.storagePath=storagePath;
		this.projName=projName;
		this.thMaxSize=thMaxSize;
		this.selectedPhotos=selectedPhotos;
		
		this.imageCache = new ImageCache(aContext);
		
		
		// Do some error checking
		if(!imageGalleryDir.canRead()){
			
			throw new Exception("Can't read this path");
			
		}
		else if(!imageGalleryDir.isDirectory()){
			
			throw new Exception("Path is a not a directory");
			
			
		}
		
		createImageList(selectedPhotos!=null);
		
		mImageLoader = new ImageLoader(this,imageCache,thMaxSize);
		mImageLoader.start();
		mHandler = new Handler();

	}

	@Override
	public void finalize() throws Throwable {

		super.finalize();

		// stop the thread we started
		mImageLoader.stopThread();
	}
	

	
	public void clearHandler() throws Throwable {
		
		mImageLoader.interrupt();
		mImageLoader.start();
		
		mHandler = new Handler();

	}

	public int getCount() {
		
		return availableImageList.size();
		
	}

	public Object getItem(int aPosition) {
		
		return availableImageList.get(aPosition).toString();
	}

	public long getItemId(int arg0) {

		return 0;
		
	}
	
	
	/*
	 * 
	 * 
	 * 
	 */
	
	private void createImageList(boolean filtered) {

	    File images = new File(storagePath); 
	    
	    //List of images at lPath
	    File[] imagelist = images.listFiles(new FilenameFilter(){  

	        public boolean accept(File dir, String name){
	        	
	            return ((name.endsWith(".jpg")) || (name.endsWith(".png"))) && (name.startsWith(projName)) ;
	        }  
	    }
	    );  
            
	    
	    availableImageList= new ArrayList<String>();
        
 
        for(int i= 0 ; i< imagelist.length; i++){
        	
            String fileName = imagelist[i].getName();  
            
            if(!filtered || (filtered && selectedPhotos.get(fileName)!=null)){
            	            	
            	availableImageList.add(storagePath+fileName); 
            } 

        }  
        
        if(availableImageList.size()==0){
        	
        	Utilities.showToast(mContext.getString(R.string.noPhotoInProject),mContext);
        	
        }
        
	}
	
	public View getView(final int aPosition, View aConvertView, ViewGroup parent) {
		final ViewSwitcher lViewSwitcher;
		
			String lPath = (String)getItem(aPosition);

			
			lViewSwitcher = new ViewSwitcher(mContext);
			lViewSwitcher.setPadding(4, 4, 4, 4);
			
			ProgressBar lProgress = new ProgressBar(mContext);
			lProgress.setLayoutParams(new ViewSwitcher.LayoutParams(thMaxSize/2, thMaxSize/2));
			lViewSwitcher.addView(lProgress);
			
			ImageView lImage = new ImageView(mContext);
			lImage.setLayoutParams(new ViewSwitcher.LayoutParams(thMaxSize, thMaxSize));
			

			lViewSwitcher.addView(lImage);

			// attach the onclick listener
			lViewSwitcher.setOnClickListener(mItemClickListener);
			

		ViewTagInformation lTagHolder = (ViewTagInformation) lViewSwitcher
				.getTag();

		if (lTagHolder == null || !lTagHolder.aImagePath.equals(lPath)) {
			
			// The Tagholder is null meaning this is a first time load
			// or this view is being recycled with a different image
			
			// Create a ViewTag to store information for later
			ViewTagInformation lNewTag = new ViewTagInformation();
			lNewTag.aImagePath = lPath;
			lNewTag.position=aPosition;
			lViewSwitcher.setTag(lNewTag);

			// Grab the image view
			// Have the progress bar display
			// Then queue the image loading
			ImageView lImageView = (ImageView) lViewSwitcher.getChildAt(1);
			lViewSwitcher.setDisplayedChild(PROGRESSBARINDEX);
			
			if(!mBusy){
			
				Bitmap bitmap=imageCache.getBitmapFromMemCache(lPath);
				
				String cacheInfo=" ("+imageCache.getSize()+"/"+imageCache.getMaxSize()+") --- "+(imageCache.getSize()*100)/imageCache.getMaxSize()+"%";
				
				if(bitmap!=null){
	
					mImageLoader.signalUI(lViewSwitcher, lImageView, bitmap);
					Log.i("Images","	OK - HIT cache ("+PhotoUtils.getFileName(lPath)+") : "+cacheInfo);
					
				}
				else{
				
					Log.i("Images","	KO - MISS cache("+PhotoUtils.getFileName(lPath)+") : "+cacheInfo);
					
					if(!mImageLoader.isAlive()) mImageLoader.start();
					
					mImageLoader.queueImageLoad(lPath, lImageView, lViewSwitcher);
				}
			}
			else{
				
				
			}

		}

		return lViewSwitcher;
	}
	
	

	
	public int size(){
		
		return availableImageList.size();
	}

	

	
	public void handleImageLoaded( final ViewSwitcher aViewSwitcher, final ImageView aImageView, final Bitmap aBitmap) {
		
		// The enqueue the following in the UI thread
		mHandler.post(new Runnable() {
			public void run() {
				
				// set the bitmap in the ImageView
				aImageView.setImageBitmap(aBitmap);
				
				// explicitly tell the view switcher to show the second view
				aViewSwitcher.setDisplayedChild(IMAGEVIEWINDEX);
			}
		});

	}
	
	
	public boolean isBusy() {
		return mBusy;
	}


	public void setBusy(boolean mBusy) {
		this.mBusy = mBusy;
	}

	public ArrayList<String> getAvailableImageList() {
		return availableImageList;
	}

	public void setAvailableImageList(ArrayList<String> availableImageList) {
		this.availableImageList = availableImageList;
	}


}

