package uni.projecte.dataLayer.CitationManager;

import java.util.ArrayList;

import uni.projecte.R;
import uni.projecte.controler.ProjectControler;
import uni.projecte.dataLayer.CitationManager.ListAdapter.FieldOrderChooserAdapter;
import uni.projecte.dataTypes.ProjectField;
import uni.projecte.dataTypes.Utilities;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.os.Handler;
import android.widget.ListView;



public class FieldOrderDialog {
	
	private static Dialog fieldOrderDialog;
	private static ListView lvOrderFields;
	private static Handler updateCitationsHandler;

	public static Dialog initDialog(Context context, ArrayList<ProjectField> fieldList, ProjectControler projCnt, long projId,final Handler handlerUpdateConf) {
		  
			updateCitationsHandler=handlerUpdateConf;
		
		    fieldOrderDialog = new Dialog(context);
		    fieldOrderDialog.setCancelable(true);
		    fieldOrderDialog.setTitle(R.string.mChangeFields);
		 
		    fieldOrderDialog.setContentView(R.layout.citation_manager_order_dialog);
		    
		    lvOrderFields=(ListView) fieldOrderDialog.findViewById(R.id.lvOrderFields);
		    FieldOrderChooserAdapter adapter=new FieldOrderChooserAdapter(context, fieldList,projCnt,projId);
		    lvOrderFields.setAdapter(adapter);
		    
		    fieldOrderDialog.setOnCancelListener(cancelDialog);
		    
		    fieldOrderDialog.show();
		    		    
		    return fieldOrderDialog;
		    
		  }
	  
	private static OnCancelListener cancelDialog = new OnCancelListener() {

		@Override
		public void onCancel(DialogInterface dialog) {

			updateCitationsHandler.sendEmptyMessage(0);		
			
		}
		
		
	};

	
}
