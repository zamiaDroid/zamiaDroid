package uni.projecte.dataLayer.ProjectManager.ListAdapters;

import java.util.ArrayList;

import uni.projecte.R;
import uni.projecte.dataLayer.ProjectManager.ListAdapters.RemoteProjectListAdapter.ViewHolder;
import uni.projecte.dataLayer.ProjectManager.objects.Project;
import uni.projecte.dataTypes.ProjectRepositoryType;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.RadioButton;
import android.widget.TextView;

public class ProjectListAdapter extends BaseAdapter{
	
	private ArrayList<Project> projList;
	private LayoutInflater mInflater;

	
	public ProjectListAdapter(Context context,ArrayList<Project> projList) {

		mInflater = LayoutInflater.from(context);
		this.projList=projList;

	}

	public int getCount() {
		return projList.size();
	}

	public Object getItem(int position) {
		return projList.get(position);
	}

	public long getItemId(int position) {
		return projList.get(position).getProjId();
		
	}

	public View getView(int position, View convertView, ViewGroup parent) {

		ViewHolder holder;
		
		if (convertView == null) {

			convertView = mInflater.inflate(R.layout.project_repository_row, null);

			holder = new ViewHolder();

			holder.projName = (TextView) convertView.findViewById(R.id.tvProjRepType);
			holder.cbProject = (TextView) convertView.findViewById(R.id.tvProjRepName);

			convertView.setTag(holder);

		} 
		else {
			
			holder = (ViewHolder) convertView.getTag();
		}
		
		
		holder.projName.setText(projList.get(position).getProjName());

		
		
		return convertView;
	}
	
	static class ViewHolder {

		TextView projName;
		TextView cbProject;

	}

}
