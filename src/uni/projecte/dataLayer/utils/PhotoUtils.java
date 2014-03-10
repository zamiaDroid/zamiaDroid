package uni.projecte.dataLayer.utils;

import java.io.File;
import java.io.IOException;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.media.ExifInterface;



public class PhotoUtils {
	
	public static String getFileName(String photoPath){
		
		int pos=photoPath.indexOf("/zamiaDroid");
		
		if(pos<=0) return photoPath;
		else return photoPath.substring(pos+19);
				
	}
	
	public static boolean removePhoto(String path){
		
    	File file = new File(path); 
    	boolean deleted = file.delete();

		return deleted;
    	
	}
	
	
	public static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth) {
		
		// Raw height and width of image
		final int height = options.outHeight;
		final int width = options.outWidth;
		int inSampleSize = 1;

		if (width > reqWidth) {

			// Calculate ratios of height and width to requested height and width
			final int heightRatio = Math.round((float) height / (float) reqWidth);
		    final int widthRatio = Math.round((float) width / (float) reqWidth);

		    // Choose the smallest ratio as inSampleSize value, this will guarantee
		    // a final image with both dimensions larger than or equal to the
		    // requested height and width.
		    inSampleSize = heightRatio > widthRatio ? heightRatio : widthRatio;

		}

		return inSampleSize;
		
	}

	public static Bitmap decodeAndResizeBitmap(String photoPath, int inSampleSize, boolean preRotate){
		
		
	    // First decode with inJustDecodeBounds=true to check dimensions
	    final BitmapFactory.Options options = new BitmapFactory.Options();

	    // Calculate inSampleSize
	    options.inSampleSize = inSampleSize;
    
	    // Decode bitmap with inSampleSize set
	    
	    Bitmap resized=BitmapFactory.decodeFile(photoPath, options);
	  
	    		
	    if(resized!=null && preRotate){
	    	
	    	Matrix matrix = getOrientatorMatrix(photoPath);
	    	
	    	return Bitmap.createBitmap(resized, 0, 0, resized.getWidth(), resized.getHeight(), matrix,true);
	
	    }
	    
	    else return resized;
		
	}
	
	
	public static Bitmap decodeBitmap(String photoPath,int reqWidth, boolean preRotate) {

	    // First decode with inJustDecodeBounds=true to check dimensions
	    final BitmapFactory.Options options = new BitmapFactory.Options();
	    options.inJustDecodeBounds = true;
	    BitmapFactory.decodeFile(photoPath, options);

	    // Calculate inSampleSize
	    options.inSampleSize = calculateInSampleSize(options, reqWidth);
    
	    // Decode bitmap with inSampleSize set
	    options.inJustDecodeBounds = false;
	    
	    Bitmap resized=BitmapFactory.decodeFile(photoPath, options);
	  
	    		
	    if(resized!=null && preRotate){
	    	
	    	Matrix matrix = getOrientatorMatrix(photoPath);
	    	
	    	return Bitmap.createBitmap(resized, 0, 0, resized.getWidth(), resized.getHeight(), matrix,true);
	
	    }
	    
	    else return resized;
	    
	}

	public static Matrix getOrientatorMatrix(String filename){

		Matrix matrix = new Matrix();
		ExifInterface exif;

		try {

			exif = new ExifInterface(filename);
			int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, 1);

			switch(orientation){
			
				case 6:
					matrix.postRotate(90);
					break;
				case 3:
					matrix.postRotate(180);
					break;
				case 8:
					matrix.postRotate(270);
					break;
					
			}

		} catch (IOException e) {

			e.printStackTrace();

		}

		return matrix;

	}
	
	public static Bitmap cropBitmap(Bitmap original, String path,int height, int width) {
		
		Matrix matrix = getOrientatorMatrix(path);
		
	    Bitmap croppedImage = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
		
	    Canvas canvas = new Canvas(croppedImage);
	 
	    Rect srcRect = new Rect(0, 0, original.getWidth(), original.getHeight());
	    Rect dstRect = new Rect(0, 0, width, height);
	 
	    int dx = (srcRect.width() - dstRect.width()) / 2;
	    int dy = (srcRect.height() - dstRect.height()) / 2;
	 
	    // If the srcRect is too big, use the center part of it.
	    srcRect.inset(Math.max(0, dx), Math.max(0, dy));
	 
	    // If the dstRect is too big, use the center part of it.
	    dstRect.inset(Math.max(0, -dx), Math.max(0, -dy));
	 
	    // Draw the cropped bitmap in the center
	    canvas.drawBitmap(original, srcRect, dstRect, null);
	 
	    original.recycle();
	 
	    Bitmap rotatedBitmap=Bitmap.createBitmap(croppedImage, 0, 0, width, height, matrix,true);
	    
	    croppedImage.recycle();
	    	    
	    return rotatedBitmap;
	    
	}
	

}
