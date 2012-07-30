/*    	This file is part of ZamiaDroid.
*
*	ZamiaDroid is free software: you can redistribute it and/or modify
*	it under the terms of the GNU General Public License as published by
*	the Free Software Foundation, either version 3 of the License, or
*	(at your option) any later version.
*
*    	ZamiaDroid is distributed in the hope that it will be useful,
*    	but WITHOUT ANY WARRANTY; without even the implied warranty of
*    	MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
*    	GNU General Public License for more details.
*
*    	You should have received a copy of the GNU General Public License
*    	along with ZamiaDroid.  If not, see <http://www.gnu.org/licenses/>.
*/


package uni.projecte.Activities.RemoteDBs;

import java.util.ArrayList;

import uni.projecte.R;
import uni.projecte.R.id;
import uni.projecte.R.layout;
import uni.projecte.R.string;
import uni.projecte.controler.PreferencesControler;
import uni.projecte.dataLayer.RemoteDBManager.AbstractDBConnection;
import uni.projecte.dataLayer.RemoteDBManager.DBManager;
import uni.projecte.dataTypes.RemoteCitation;
import uni.projecte.dataTypes.RemoteCitationSet;
import uni.projecte.dataTypes.Utilities;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;


public class TaxonRemoteCitationList extends Activity {
	
	
	private ListView lvRemotetaxonList;
	private TextView tvUTM;
	private TextView tvListType;
	private TextView dataBaseType;
	private TextView tvChoosenTaxon;
	private ImageButton imgSearch;
	private EditText filterList;
	
	private RemoteCitationSet remoteList;
	private RemoteCitationListAdapter remCitationAdapter;

	
	private boolean searchActive=false;
	
	private long projId;
	private String utm;
	private int level;
	private String filum;
	private String locality;
	private String filumType;
	private String dbName;
	private String thName;
	private String codiOrc;
	private String taxonName;
	
	private double latitude;
	private double longitude;
	
	private int workingDB;
	
	private boolean utm1x1;
	
	private AbstractDBConnection dbConn;

	
	private ProgressDialog pd;

	
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    
        setContentView(R.layout.taxonremotelistexplorer);
        
        
        /* Getting arguments */
        
        utm1x1=getIntent().getExtras().getBoolean("utm1x1");
        filum=getIntent().getExtras().getString("filum");
        codiOrc=getIntent().getExtras().getString("codiOrc");
        taxonName=getIntent().getExtras().getString("taxon");
        level=getIntent().getExtras().getInt("level");
        
        latitude=getIntent().getExtras().getDouble("latitude");
        longitude=getIntent().getExtras().getDouble("longitude");

        workingDB=getIntent().getExtras().getInt("workingDB");

        
        /* Getting UI elements */
        
        lvRemotetaxonList= (ListView)findViewById(R.id.taxonCitationListView);
        tvUTM= (TextView)findViewById(R.id.tvDBUTM);
        filterList=(EditText)findViewById(R.id.etFilterTaxonList);
        dataBaseType=(TextView)findViewById(R.id.tvDBServer);
        tvChoosenTaxon=(TextView)findViewById(R.id.tvChoosenTaxon);
        
        PreferencesControler pC=new PreferencesControler(this);


        DBManager dbM=new DBManager(this);
    	dbConn=dbM.getDBInstance(workingDB,filum,pC.getLang());   	
    	dbConn.setLocation(latitude, longitude,utm1x1);
        
        dbName=dbConn.getDbName();
        dataBaseType.setText(dbName);
        tvChoosenTaxon.setText(taxonName);
        tvChoosenTaxon.setTextColor(Color.WHITE);
        
        /* Update interface data */
        
        tvUTM.setText(dbConn.getPrettyLocation());
        tvUTM.setTextColor(Color.GREEN);
        

        
        loadDataThread();

        /*  */
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

       
    }
	

    

	private void loadDataThread() {

		String loaderDescrition=dbName+ " "+getString(R.string.searchingTaxonsUTM)+" "+dbConn.getPrettyLocation();
		
        pd = ProgressDialog.show(this, getString(R.string.connectingDatabase), loaderDescrition, true,
                true);
	          	
	        new Thread(new Runnable() {

				public void run() {
					
			        /* Fetching local and Remote Taxons */ 
			        loadRemoteTaxons();
			        		        
			        handlerEnd.sendEmptyMessage(0);
				}

			
	            	
	    	}).start();

	        
	    	
		}
	
	    
	    
		  private void loadRemoteTaxons(){
			  
			  int error=dbConn.serviceGetTaxonCitations(codiOrc);
			  
			  if(error>=0){
				  
				  remoteList=dbConn.getCitList();
				  
			      remCitationAdapter= new RemoteCitationListAdapter(this,remoteList.getCitationList());
				  
			  }
			  else if(error==AbstractDBConnection.DB_UNAVAILABLE){
				  
				  Utilities.showToast("Database Unavailable. Check Internet Connection", this);
				  
			  }
			  else if(error==AbstractDBConnection.DB_SERVER_ERROR){
				  
				  Utilities.showToast("Wrong retrieved data format", this);

			  }

		       
		  }

	
    private Handler handlerEnd = new Handler() {

        @Override
        public void handleMessage(Message msg) {
 
        	pd.dismiss();
        	lvRemotetaxonList.setAdapter(remCitationAdapter);
            	
	 
	         	
        }
    };
	
    
    private static class RemoteCitationListAdapter extends BaseAdapter implements Filterable{
     	 
    	private LayoutInflater mInflater;
    	ArrayList<RemoteCitation> elements;

        
    	 public RemoteCitationListAdapter(Context context,ArrayList<RemoteCitation> remoteCitation) {
    	 
    		 mInflater = LayoutInflater.from(context);
    	 
    		 this.elements=remoteCitation;
             
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
	    	 
    		 convertView = mInflater.inflate(R.layout.remotecitationrow, null);
    		 
	    	 holder = new ViewHolder();
	    	 holder.text = (TextView) convertView.findViewById(R.id.tvRemoteCitLocality);
	    	 holder.textBib= (TextView) convertView.findViewById(R.id.tvRemoteCitBib);

	    	 convertView.setTag(holder);
	    	 
	    	 
    	 } 
    	 else {
    		 
    		 holder = (ViewHolder) convertView.getTag();
    		 
    	 }
       	 
	    	 holder.text.setText(Html.fromHtml(elements.get(position).getLocality()));
	    	 holder.textBib.setText(elements.get(position).getBib());
	
	    	 return convertView;
    	 
    	 }

    	 static class ViewHolder {
	    	 TextView text;
	    	 TextView textBib;

    	 }



	public Filter getFilter() {
		
		return null;
		
	}
    	 
    	 }    
    
}


