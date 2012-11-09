package uni.projecte.dataLayer.dataStructures;

import android.app.ActivityManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.support.v4.util.LruCache;



/*
 *  From developer Android: Caching Bitmaps
 * 		http://developer.android.com/training/displaying-bitmaps/cache-bitmap.html
 * 
 */


public class ImageCache {
	
	
	/* Lruw Cache for caching bitmaps */
	private LruCache<String, Bitmap> mMemoryCache;


	public ImageCache(Context baseContext){ 
		
	    final int memClass = ((ActivityManager) baseContext.getSystemService(Context.ACTIVITY_SERVICE)).getMemoryClass();
		
		// Use 1/8th of the available memory for this memory cache.
	    final int cacheSize = 1024 * 1024 * memClass / 8;

	    mMemoryCache = new LruCache<String, Bitmap>(cacheSize) {

	    	protected int sizeOf(String key, Bitmap bitmap) {
	            // The cache size will be measured in bytes rather than number of items.
	            return bitmap.getHeight() * bitmap.getWidth() * 4;
	        }
	    	
	    };
		
		
	}
	
	
	/* Lte Cache method */
	
	public void addBitmapToMemoryCache(String key, Bitmap bitmap) {
	    if (getBitmapFromMemCache(key) == null) {
	        mMemoryCache.put(key, bitmap);
	    }
	}

	public Bitmap getBitmapFromMemCache(String key) {
	    return mMemoryCache.get(key);
	}
	
	public int getSize(){
		
		return mMemoryCache.size();
		
	}
	
	public int getMaxSize(){
		
		return mMemoryCache.maxSize();

	}
	
	

}
