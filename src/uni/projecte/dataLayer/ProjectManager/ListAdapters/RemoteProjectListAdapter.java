package uni.projecte.dataLayer.ProjectManager.ListAdapters;

import java.util.ArrayList;

import uni.projecte.R;
import uni.projecte.dataTypes.ProjectRepositoryType;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;


public class RemoteProjectListAdapter extends BaseAdapter {


	private LayoutInflater mInflater;
	private ArrayList<ProjectRepositoryType> elements;

	public RemoteProjectListAdapter(Context context,ArrayList<ProjectRepositoryType> elements) {

		mInflater = LayoutInflater.from(context);
		this.elements=elements;

	}

	public int getCount() {
		return elements.size();
	}

	public Object getItem(int position) {
		return position;
	}

	public long getItemId(int position) {
		return position;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		
		ViewHolder holder;
		
		if (convertView == null) {

			convertView = mInflater.inflate(R.layout.project_repository_row, null);

			holder = new ViewHolder();

			holder.projRepType = (TextView) convertView.findViewById(R.id.tvProjRepType);
			holder.projRepName = (TextView) convertView.findViewById(R.id.tvProjRepName);
			holder.projrepDesc = (TextView) convertView.findViewById(R.id.tvProjRepDesc);

			convertView.setTag(holder);


		} 
		else {
			
			holder = (ViewHolder) convertView.getTag();
		}

		holder.projRepName.setText(elements.get(position).getProjName());

		holder.projrepDesc.setText(elements.get(position).getProjDescription());
		holder.projrepDesc.setTag(elements.get(position).getThFilum());

		holder.projRepName.setTag(elements.get(position).getProjId());

		holder.projRepType.setText(elements.get(position).getProjType());
		holder.projRepType.setTag(elements.get(position).getThName());
		


		return convertView;
	}

	static class ViewHolder {

		TextView projRepType;
		TextView projRepName;
		TextView projrepDesc;

	}
}    
