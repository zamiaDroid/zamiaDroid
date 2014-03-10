package uni.projecte.ui;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import uni.projecte.dataLayer.dataStructures.ImageCache;
import uni.projecte.dataLayer.utils.PhotoUtils;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.media.ExifInterface;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.ImageView;
import android.widget.ViewSwitcher;

public class ImageLoader extends Thread {

	public interface ImageLoadListener {

		void handleImageLoaded(ViewSwitcher aViewSwitcher, ImageView aImageView, Bitmap aBitmap);
	}
	
	private static final String TAG = ImageLoader.class.getSimpleName();
	ImageLoadListener mListener = null;
	private Handler handler;
	private ImageCache imageCache;
	private int thMaxSize;

	/**
	 * Image loader takes an object that extends ImageLoadListener
	 * @param lListener
	 * @param imageCache 
	 * @param thMaxSize 
	 */
	ImageLoader(ImageLoadListener lListener, ImageCache imageCache, int thMaxSize){
		mListener = lListener;
		this.imageCache=imageCache;
		this.thMaxSize=thMaxSize;
	}
	
	@Override
	public void run() {
		try {
			
			// preparing a looper on current thread			
			// the current thread is being detected implicitly
			Looper.prepare();
			
			// Looper gets attached to the current thread by default
			handler = new Handler();
			
			Looper.loop();
			// Thread will start
			
		} catch (Throwable t) {
			Log.e(TAG, "ImageLoader halted due to a error: ", t);
		} 
	}

	/**
	 * Method stops the looper and thus the thread
	 */
	public synchronized void stopThread() {
		
		// Use the handler to schedule a quit on the looper
		handler.post(new Runnable() {
			
			public void run() {
				// This runs on the ImageLoader thread
				Log.i(TAG, "DownloadThread loop quitting by request");
				
				Looper.myLooper().quit();
			}
		});
	}
	
	/**
	 * Method queues the image at path to load
	 * Note that the actual loading takes place in the UI thread
	 * the ImageView and ViewSwitcher are just references for the
	 * UI thread.
	 * @param aPath      - Path where the bitmap is located to load
	 * @param aImageView - The ImageView the UI thread will load 
	 * @param aViewSwitcher - The ViewSwitcher that needs to display the imageview
	 */
	public synchronized void queueImageLoad(
			final String aPath, 
			final ImageView aImageView, 
			final ViewSwitcher aViewSwitcher) {
		
		// Wrap DownloadTask into another Runnable to track the statistics
		
		if(handler!=null){
		
			handler.post(new Runnable() {
				public void run() {
					try {
						
						synchronized (aImageView){
							// make sure this thread is the only one performing activities on this imageview
													
							File originalImage=new File(aPath);
							
							if(originalImage.exists()){
								
								int maxSize;
								
								File thumb=new File(aPath.replace("/Photos/", "/Photos/thumbs/"));
								Bitmap squared=null;
								
								if(thumb.exists()){
									
									Log.i("Images","Loading thumbnail: "+aPath);
									squared=BitmapFactory.decodeFile(aPath.replace("/Photos/", "/Photos/thumbs/"), new BitmapFactory.Options());

								}
								else{
									
									Bitmap lBitmap = PhotoUtils.decodeBitmap(aPath, thMaxSize,false);
																
									if(lBitmap==null){
										
										Log.i("Images","Wrong file: "+aPath);
										squared=createWrongImageBitmap(thMaxSize);
										
									}
									else{
									
										Log.i("Images","Decoding size("+lBitmap.getWidth()+"[w]/"+lBitmap.getHeight()+"[h]): "+aPath);

										if(lBitmap.getWidth()>lBitmap.getHeight()) maxSize=lBitmap.getHeight();
										else maxSize=lBitmap.getWidth();
										
										squared=PhotoUtils.cropBitmap(lBitmap,aPath, maxSize, maxSize);
										
										thumb.createNewFile();
										
										FileOutputStream out = new FileOutputStream(thumb.getAbsoluteFile());
								    	squared.compress(Bitmap.CompressFormat.JPEG, 90, out);
								    
										lBitmap.recycle();

									}
								
								}
								
								// Load the image here
								signalUI(aViewSwitcher, aImageView, squared);
								imageCache.addBitmapToMemoryCache(aPath, squared);
								
							}
							
							else{
								
								Bitmap bm=createWrongImageBitmap(thMaxSize);
								
								signalUI(aViewSwitcher, aImageView, bm);
								
								imageCache.addBitmapToMemoryCache(aPath, bm);
								
							}
						
							
						}
					} 
					catch(Exception e){
						e.printStackTrace();
					}
				}
			});
		
		}
	}
	
	
	

	

	
	private Bitmap createWrongImageBitmap(int width){
		
		Paint paint = new Paint();
		paint.setAntiAlias(true);

		Bitmap bitmap = Bitmap.createBitmap(width, width,Bitmap.Config.ARGB_8888);
		Canvas c = new Canvas(bitmap);
		
	    paint.setColor(Color.argb(0xff, 0x88, 0xaa, 0x00));
		paint.setStyle(Paint.Style.FILL);
        paint.setStrokeWidth(1);
        paint.setTextSize(14);

		c.drawColor(Color.BLACK);
		c.drawText("Foto incorrecta", thMaxSize/4, thMaxSize/2, paint);
		 
		    
       return bitmap;
		
	}


	
	
	/**
	 * Method is called when the bitmap is loaded.  The UI thread adds the bitmap to the imageview.
	 * @param aViewSwitcher - The ViewSwitcher that needs to display the imageview
	 * @param aImageView - The ImageView the UI thread will load 
	 * @param aImage - The Bitmap that gets loaded into the ImageView
	 */
	
	
	public void signalUI( ViewSwitcher aViewSwitcher, ImageView aImageView, Bitmap aImage){
		
		if(mListener != null){
			// we have an object that implements ImageLoadListener
			mListener.handleImageLoaded(aViewSwitcher, aImageView, aImage);
			
			
		}
	}
	
}
