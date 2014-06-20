package uni.projecte.dataLayer.ProjectManager.ListAdapters;

import java.util.ArrayList;

import uni.projecte.R;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;

	/*
	 * 	BaseAdapter that includes a list of newFields and a Checkbox that will 
	 * 	allow us to decide if we want to create the newField to the current project
	 * 
	 */

    public class NewFieldsListAdapter extends BaseAdapter {
        
    	private ArrayList<String> newFields;
        private Context mContext;
        
        private LayoutInflater inflater;
        
        // array of items selection
        private boolean[] itemSelection;
        private boolean remoteProj;

    	
    	public NewFieldsListAdapter(Context context,ArrayList<String> newFields, boolean remoteProj){
    		
            mContext = context;
            this.newFields=newFields;
            this.remoteProj=remoteProj;
            
            inflater = (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            
            itemSelection=new boolean[getCount()];
            
        }


        public int getCount() {
        	
            return newFields.size();
            
        }


        public String getItem(int position) {
            return newFields.get(position);
        }

  
        public long getItemId(int position) {
            return position;
        }

      
 
        public View getView(final int position, View convertView, ViewGroup parent) {

        	convertView = inflater.inflate(R.layout.citation_import_zamia_field, null);
        	final ViewHolder holder = new ViewHolder();
        	
        	holder.chkItem = (CheckBox)convertView.findViewById(R.id.cbField);
        	
        	holder.chkItem.setOnCheckedChangeListener(new OnCheckedChangeListener(){

        		public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        			
        			itemSelection[position] = holder.chkItem.isChecked();
        			
        		}
        	});
        	 
        	if(remoteProj) {
        		
        		holder.chkItem.setChecked(false);
        		holder.chkItem.setEnabled(false);
        		
        	}
        	else{
        		
            	holder.chkItem.setChecked(itemSelection[position]);

        		
        	}
        	
        	convertView.setTag(holder);
        	
        	holder.chkItem.setText(getItem(position));
        	
        	return convertView;
        	
        	
        }


		public boolean[] getItemSelection() {
			return itemSelection;
		}
		
	    public static class ViewHolder {
	    	
	    	CheckBox chkItem;
	    
	    }

    }





