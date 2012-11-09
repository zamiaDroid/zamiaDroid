package uni.projecte.Activities.Miscelaneous;

import java.util.HashMap;

import uni.projecte.R;
import uni.projecte.controler.CitationControler;
import uni.projecte.controler.ProjectControler;
import uni.projecte.dataTypes.Utilities;
import uni.projecte.ui.zoomImage.TouchImageView;
import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.TextView;


public class ImageView extends Activity {
	
	private String photoPath;
    double IMAGE_MAX_SIZE= 800;
    private CitationControler citCnt;
    private ProjectControler projCnt;
	private HashMap<String, String> fieldsLabelNames;
	private long projId=-1;

	public void onCreate(Bundle savedInstanceState) {
    	
    	super.onCreate(savedInstanceState);
    	
    	setContentView(R.layout.imageview);

    	LinearLayout ll=(LinearLayout) findViewById(R.id.llTouchImage);
    	TextView thPhoto=(TextView) findViewById(R.id.tvPhotoInfo);
    	TouchImageView tIv=new TouchImageView(this);

    	/* Activity Intents */
        photoPath=getIntent().getExtras().getString("photoPath");
        projId=getIntent().getExtras().getLong("projId");

        /* Data controlers */
        citCnt=new CitationControler(this);
        projCnt=new ProjectControler(this);
        
        projCnt.loadProjectInfoById(projId);
        fieldsLabelNames=projCnt.getProjectFieldsPair(projId);
        
        
      	BitmapFactory.Options options = new BitmapFactory.Options();
   	   	options.inSampleSize = 3;

		Bitmap bm = BitmapFactory.decodeFile(photoPath,options);
		
		if(photoPath!=null && bm!=null){
			
			String[] citationId=citCnt.getCitationInfoByPhoto(photoPath	,fieldsLabelNames);
			if(citationId!=null) thPhoto.setText(citationId[0]);
			tIv.setImageBitmap(bm);
	    	
	    	ll.addView(tIv);

		}
		else{
			
			Utilities.showToast(getString(R.string.photoCantBeFound), this);
			finish();
			
		}
		
        	

	}
	
	
}
