package uni.projecte.ui;

import java.io.File;
import java.io.FilenameFilter;
import java.util.HashMap;

import uni.projecte.R;
import uni.projecte.dataTypes.Utilities;
import uni.projecte.ui.ImageLoader.ImageLoadListener;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Handler;
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
	
	private File mDirectory;
	
	private String lPath;
	private String projName;
	
	private Uri[] mUrls;  
    private int thMaxSize;
    
    private HashMap<String, Long> selectedPhotos;
	
	/**
	 * Lazy loading image adapter
	 * @param aContext
	 * @param lClickListener click listener to attach to each item
	 * @param lPath the path where the images are located
	 * @param selectedPhotos 
	 * @throws Exception when path can't be read from or is not a valid directory
	 */
	public LazyImageAdapter(
			Context aContext,
			OnClickListener lClickListener,
			String lPath,
			String projName,
			HashMap<String, Long> selectedPhotos, int thMaxSize
		) throws Exception {
		
		mContext = aContext;
		mItemClickListener = lClickListener;
		mDirectory = new File(lPath);
		this.lPath=lPath;
		this.projName=projName;
		this.thMaxSize=thMaxSize;
		this.selectedPhotos=selectedPhotos;
		
		// Do some error checking
		if(!mDirectory.canRead()){
			throw new Exception("Can't read this path");
		}
		else if(!mDirectory.isDirectory()){
			throw new Exception("Path is a not a directory");
		}
		
		createImageList(selectedPhotos!=null);
		
		mImageLoader = new ImageLoader(this);
		mImageLoader.start();
		mHandler = new Handler();

	}

	@Override
	public void finalize() throws Throwable {

		super.finalize();

		// stop the thread we started
		mImageLoader.stopThread();
	}

	public int getCount() {
		
		return mUrls.length;
		
	}

	public Object getItem(int aPosition) {
		
		return mUrls[aPosition].toString();
	}

	public long getItemId(int arg0) {

		return 0;
		
	}
	
	private void createImageList(boolean filtered) {

	    File images = new File(lPath); 

	    
	    File[] imagelist = images.listFiles(new FilenameFilter(){  

	        public boolean accept(File dir, String name){
	        	
	            return ((name.endsWith(".jpg")) || (name.endsWith(".png"))) && (name.startsWith(projName)) ;
	        }  
	    }
	    );  
            
            
	    String[] mFiles = new String[imagelist.length];  
        
        int filteredFiles=0;
  
        for(int i= 0 ; i< imagelist.length; i++){
        	
            mFiles[i] = imagelist[i].getAbsolutePath();  
            
            if(filtered && selectedPhotos.get(mFiles[i])!=null) filteredFiles++;
            
        }  
        
        
        if(!filtered) filteredFiles=mFiles.length;
        
        mUrls = new Uri[filteredFiles];
        
        if(mUrls.length==0){
        	
        	Utilities.showToast(mContext.getString(R.string.noPhotoInProject),mContext);
        	
        	//finish();
        	
        }
        else{
        	
        	filteredFiles=0;

		      for(int i=0; i < mFiles.length; i++){
		    	  
		    	  if(filtered){
		    		  
		    		  if(selectedPhotos.get(mFiles[i])!=null) {
		    			  
		    			  mUrls[filteredFiles] = Uri.parse(mFiles[i]); 
				    	  filteredFiles++;

		    		  }
		    		  
		    	  }
		    	  else{
		    		  
			    	  mUrls[i] = Uri.parse(mFiles[i]);   

		    	  }
		    	  
		    	  
		      }
	
        }

   

	}
	
	public View getView(final int aPosition, View aConvertView, ViewGroup parent) {
		final ViewSwitcher lViewSwitcher;
		
		String lPath = (String)getItem(aPosition);

		// logic for conserving resources see google video on making your ui fast
		// and responsive
		
		//if (null == aConvertView) {
			
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
			
			
	//	} else {
	//		lViewSwitcher = (ViewSwitcher) aConvertView;
	//	}
	

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
			mImageLoader.queueImageLoad(lPath, lImageView, lViewSwitcher);
			

		}

		return lViewSwitcher;
	}
	
	public int size(){
		
		return mUrls.length;
	}

	
	public void handleImageLoaded(
			final ViewSwitcher aViewSwitcher,
			final ImageView aImageView, 
			final Bitmap aBitmap) {
		
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


}

