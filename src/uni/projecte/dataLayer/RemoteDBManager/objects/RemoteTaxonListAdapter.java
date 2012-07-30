package uni.projecte.dataLayer.RemoteDBManager.objects;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

import uni.projecte.R;
import uni.projecte.dataLayer.RemoteDBManager.RemoteTaxon;
import uni.projecte.dataTypes.LocalTaxonSet;
import android.content.Context;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SectionIndexer;
import android.widget.TextView;



public class RemoteTaxonListAdapter extends BaseAdapter implements SectionIndexer,Filterable{
  	 
	private LayoutInflater mInflater;
	private ArrayList<RemoteTaxon> elements;
    HashMap<String, Integer> alphaIndexer;
    String[] sections;
    LocalTaxonSet localTaxonList;
    private OnClickListener showTaxonCitations;
    private OnClickListener showTaxonInfo;
    
	 public RemoteTaxonListAdapter(Context context,ArrayList<RemoteTaxon> elements, LocalTaxonSet localTaxonList, OnClickListener showTaxonCitations, OnClickListener showTaxonInfoClick) {
	 
		 mInflater = LayoutInflater.from(context);
	 
		 this.elements=elements;
		 this.localTaxonList=localTaxonList;
         alphaIndexer = new HashMap<String, Integer>(); 
         this.showTaxonCitations=showTaxonCitations;
         this.showTaxonInfo=showTaxonInfoClick;
		 
		  int size = elements.size();
          for (int i = size - 1; i >= 0; i--) {
                  String element = elements.get(i).getTaxon();
                  alphaIndexer.put(element.substring(0, 1), i);
          //We store the first letter of the word, and its index.
          //The Hashmap will replace the value for identical keys are putted in
          } 
          
          Set<String> keys = alphaIndexer.keySet(); // set of letters ...sets
          // cannot be sorted...

          Iterator<String> it = keys.iterator();
          ArrayList<String> keyList = new ArrayList<String>(); // list can be
          // sorted

          while (it.hasNext()) {
                  String key = it.next();
                  keyList.add(key);
          }

          Collections.sort(keyList);

          sections = new String[keyList.size()]; // simple conversion to an
          // array of object
          keyList.toArray(sections);

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
    	 
		 convertView = mInflater.inflate(R.layout.taxonrow, null);
		 
    	 holder = new ViewHolder();
    	 holder.text = (TextView) convertView.findViewById(R.id.tvTaxonList);
    	 holder.image= (ImageView) convertView.findViewById(R.id.existsLocalTaxon);
    	 holder.viewCitations = (ImageButton) convertView.findViewById(R.id.showCitationsButton);

    	 convertView.setTag(holder);
    	 
    	 
	 } 
	 else {
		 
		 holder = (ViewHolder) convertView.getTag();
		 
	 }
	 
    	 holder.text.setText(Html.fromHtml(elements.get(position).getTaxon()));
    	 holder.text.setTag(elements.get(position).getTaxonId());
    	 holder.text.setOnClickListener(showTaxonInfo);
    	 
    	 holder.viewCitations.setTag(elements.get(position).getTaxon());
    	 holder.viewCitations.setOnClickListener(showTaxonCitations);
    	 holder.viewCitations.setVisibility(View.VISIBLE);
    	 holder.viewCitations.setBackgroundResource(R.drawable.list_icon);
    	 
    	     	 
    	 
    	 if(!localTaxonList.existsTaxon(elements.get(position).getCleanTaxon())){ 
    		 
    		 holder.image.setVisibility(View.INVISIBLE);

    		 
    	 }
    	 else {
    		 
    		 holder.image.setVisibility(View.VISIBLE);

    		 
    	 }

    	 return convertView;
	 
	 }

	 static class ViewHolder {
    	 TextView text;
    	 ImageView image;
    	 ImageButton viewCitations;

	 }
	 
	  public int getPositionForSection(int section) {
          // Log.v("getPositionForSection", ""+section);
          String letter = sections[section];

          return alphaIndexer.get(letter);
  }

  public int getSectionForPosition(int position) {

          // you will notice it will be never called (right?)
          Log.v("getSectionForPosition", "called");
          return 0;
  }

  public Object[] getSections() {

          return sections; // to string will be called each object, to display
          // the letter
  }



public Filter getFilter() {
	
	return null;
	
}
	 
	 }    