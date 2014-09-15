package uni.projecte.dataLayer.CitationManager.ListAdapter;

import java.util.ArrayList;

import uni.projecte.R;
import uni.projecte.controler.ProjectControler;
import uni.projecte.dataTypes.ProjectField;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.TextView;
import android.view.View.OnClickListener;

public class FieldOrderChooserAdapter extends BaseAdapter {

	private LayoutInflater mInflater;
	private ArrayList<ProjectField> projFields;
	private Context parentContext;
	private long projId;
	private ProjectControler projCnt;
	private int n;

	public FieldOrderChooserAdapter(Context context,ArrayList<ProjectField> projFields, ProjectControler projCnt,long projId) {

		this.projFields = projFields;
		this.parentContext = context;
		this.projCnt = projCnt;
		this.projId=projId;

		n = projFields.size();

		mInflater = LayoutInflater.from(context);

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

			convertView = mInflater.inflate(
					R.layout.citation_manager_order_row, null);

			holder = new ViewHolder();
			
			holder.tvListLabel = (TextView) convertView.findViewById(R.id.tvFieldLabel);
			
			holder.upButton = (ImageButton) convertView.findViewById(R.id.ibArrowUp);
			
			holder.downButton = (ImageButton) convertView.findViewById(R.id.ibArrowDown);
			
			holder.listButton = (ImageButton) convertView.findViewById(R.id.listImgButton);
			
			holder.cbListCheck = (CheckBox) convertView.findViewById(R.id.cBedit);
			

			convertView.setTag(holder);

		} else {

			holder = (ViewHolder) convertView.getTag();

		}

		ProjectField pF = projFields.get(position);

		holder.tvListLabel.setText(pF.getLabel());
		holder.tvListLabel.setTag(pF.getName());
		

		if (position == 0) {

			holder.upButton.setVisibility(View.INVISIBLE);
			holder.downButton.setVisibility(View.VISIBLE);
		} else if (position == n - 1) {

			holder.downButton.setVisibility(View.INVISIBLE);
			holder.upButton.setVisibility(View.VISIBLE);

		} else {

			holder.upButton.setVisibility(View.VISIBLE);
			holder.downButton.setVisibility(View.VISIBLE);

		}

		holder.cbListCheck.setChecked((pF.getVisible() == 0) ? false : true);

		holder.upButton.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {

				ProjectField current = projFields.get(position);
				ProjectField upper = projFields.get(position - 1);
				projFields.remove(current);
				projFields.add(position - 1, current);

				 projCnt.setViewerFieldOrder(current.getId(),position-1);
				 projCnt.setViewerFieldOrder(upper.getId(),position);

				notifyDataSetChanged();

			}

		}

		);

		holder.downButton.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {

				ProjectField current = projFields.get(position);
				ProjectField down = projFields.get(position + 1);
				projFields.remove(current);
				projFields.add(position + 1, current);

				 projCnt.setViewerFieldOrder(current.getId(),position+1);
				 projCnt.setViewerFieldOrder(down.getId(),position);

				notifyDataSetChanged();

			}

		}

		);

		holder.cbListCheck.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {

				CheckBox cBox = (CheckBox) v;
				View e = (View) v.getParent().getParent();

				TextView tv = (TextView) e.findViewById(R.id.tvFieldLabel);
				String attName = tv.getTag().toString();

				if (cBox.isChecked()) {
					projCnt.changeViewerFieldVisibility(projId, attName, true);

				}

				else if (!cBox.isChecked()) {

					projCnt.changeViewerFieldVisibility(projId, attName, false);

				}
			}
		}

		);

		return convertView;

	}

	static class ViewHolder {

		TextView tvListLabel;
		CheckBox cbListCheck;
		ImageButton imgButton;
		ImageButton upButton;
		ImageButton downButton;
		ImageButton listButton;

	}

}
