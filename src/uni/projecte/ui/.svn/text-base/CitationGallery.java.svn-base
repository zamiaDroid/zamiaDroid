package uni.projecte.ui;

import android.content.Context;
import android.os.SystemClock;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.Gallery;



public class CitationGallery extends Gallery {

	private long mLastScrollEvent;


	public CitationGallery(Context context) {
		super(context);
	}
	
	
	public CitationGallery(Context context, AttributeSet attrs) {
		super(context, attrs);
	}


	public CitationGallery(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	@Override
	public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
	                       float velocityY) {        
	    return false;
	}
	
	/*@Override
	public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
	        float velocityY) {
	    return super.onFling(e1, e2, velocityX / 4, velocityY);
	}*/
	
	 
	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b){
		
	  long now = SystemClock.uptimeMillis();
	  if (Math.abs(now - mLastScrollEvent) > 250) {
	    super.onLayout(changed, l, t, r, b);
	  }
	  
	}
	 
	@Override
	public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX,
	    float distanceY){
		
	  mLastScrollEvent = SystemClock.uptimeMillis();
	  return super.onScroll(e1, e2, distanceX, distanceY);
	}



}
