package uni.projecte.maps;

import uni.projecte.R;
import uni.projecte.controler.PreferencesControler;
import uni.projecte.dataTypes.Utilities;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;



public class MarkerConfigurationDialog extends Dialog{

	//Ensure this Dialog has a Context we can use
	Context mContext ;

	public MarkerConfigurationDialog(Context context) {
	    super(context);
	    mContext=context; //Store the Context as provided from caller
	}

	@Override
	 protected void onCreate(final Bundle savedInstanceState){
		
	  super.onCreate(savedInstanceState);
	  RelativeLayout ll=(RelativeLayout) LayoutInflater.from(mContext).inflate(R.layout.mapchangemarker, null);
	  setContentView(ll);
	  
	  
	  
	 }

	
/*	  public static Dialog initDialog(Context context, long projId) {
		  
		    markerConfigDialog  = new Dialog(context);
		    markerConfigDialog.setCancelable(true);
		    markerConfigDialog.setTitle("Escull un marcador");
		 
		    markerConfigDialog.setContentView(R.layout.mapchangemarker);
		    markerConfigDialog.show();
		    
		    		    
		    //handleEvents(context,pC,compassEnabled,handlerUpdateConf);

		    return markerConfigDialog;
		    
		  }*/
	  
	 /* private static void handleEvents(final Context context, final PreferencesControler pC, boolean compassEnabled, final Handler handlerUpdateConf) {
		  
		  RadioGroup rdgCategory = (RadioGroup)markerConfigDialog.findViewById(R.id.radioGroupUTMPrec);
		  CheckBox cbShowCompass = (CheckBox)markerConfigDialog.findViewById(R.id.cbShowCompass);
		
	  }*/

	  public OnClickListener markerClick = new OnClickListener() {
		

		public void onClick(DialogInterface dialog, int which) {

			Utilities.showToast("Marcador ",mContext);
			
		}
	};

}
