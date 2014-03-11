package uni.projecte.dataLayer.ProjectManager.ListAdapters;

import java.util.ArrayList;

import uni.projecte.R;
import uni.projecte.dataLayer.ProjectManager.tools.FieldTypeTranslator;
import uni.projecte.dataTypes.ProjectField;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;


public class ProjectFieldCreatorListAdapter extends BaseAdapter  {
	
	private LayoutInflater mInflater;
	private ArrayList<ProjectField> projFields;
	private Context parentContext;
	private FieldTypeTranslator fieldTrans;

	
	public ProjectFieldCreatorListAdapter(Context context,ArrayList<ProjectField> projFields){
		
		this.projFields=projFields;
		this.parentContext=context;
		
		mInflater = LayoutInflater.from(context);
		fieldTrans=new FieldTypeTranslator(parentContext);
		
	}


	public int getCount() {
		
		return projFields.size();
		
	}

	public Object getItem(int position) {
		
		return projFields.get(position);
		
	}

	public long getItemId(int position) {
		
		return projFields.get(position).getId();
		
	}

  	 public View getView(final int position, View convertView, ViewGroup parent) {
  		 
    	 ViewHolder holder;
    	 
    	 if (convertView == null) {
	    	 
    		 convertView = mInflater.inflate(R.layout.project_creator_field_rowlist, null);
    		 
	    	 holder = new ViewHolder();
	    	 holder.tvListText = (TextView)convertView.findViewById(R.id.tvFieldName);
	    	 holder.tvListLabel = (TextView)convertView.findViewById(R.id.tvFieldLabel);
	    	 holder.tvListType = (TextView)convertView.findViewById(R.id.tvFieldType);

	    	 convertView.setTag(holder);
	    	 
    	 }
    	 else {
    		 
    		 holder = (ViewHolder) convertView.getTag();
    		 
    	 }
    	 
    	    ProjectField pF=projFields.get(position);
                       
    	    //holder.tvListText.setText(pF.getName());
            holder.tvListLabel.setText(pF.getLabel());
            holder.tvListType.setText(fieldTrans.getFieldTypeTranslated(pF.getType()));

       
		return convertView;
	}

	 static class ViewHolder {
		 
		   	TextView tvListText;
	        TextView tvListLabel;
	        TextView tvListType;
	 }
		 
	 
}
