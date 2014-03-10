package uni.projecte.dataLayer.CitationManager.objects;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

import uni.projecte.R;
import uni.projecte.controler.CitationFilteredControler;
import uni.projecte.controler.ThesaurusControler;
import uni.projecte.dataLayer.CitationManager.ListAdapter.CitationListAdapter;
import uni.projecte.dataLayer.bd.CitacionDbAdapter;
import android.content.Context;
import android.database.Cursor;
import android.os.Handler;
import android.text.Html;
import android.util.Log;
import android.widget.TextView;


public class CitationHandler {
	
	private Context baseContext;
	private TextView tvSelected;
	
	private ThesaurusControler thC;

	/* all citations*/
	private ArrayList<Citation> mainCitationList;
	private HashMap<Long, Citation> mainCitationListHash;
	
	/* filtered list */
	private ArrayList<Citation> filteredCitationList;
	private ArrayList<Citation> filteredCitationListAlphaOrdered;
		
	private ArrayList<Citation> secondFilteredCitationList;
	private ArrayList<Citation> secondFilteredCitationListAlphaOrdered;
	
	private ArrayList<Citation> thirdFilteredCitationList;
	private ArrayList<Citation> thirdFilteredCitationListAlphaOrdered;	
	
	private ArrayList<Citation> currentList;
	
	private FilterStates filterStates;
	
	private int filterLevel=0;

	/* selection list */
	private ArrayList<Citation> selectionList;
	private HashMap<Long, Citation> selectionElementsHash;
	
	
	private HashMap<Long, String> surenessValues;
	
	
	/* ListAdapters */
	private CitationFilteredControler citationCnt;
	private CitationListAdapter citationListAdapt;
	
	/* Is alpha ordered? */
	private boolean alphaOrder=false;
	
	/* AlphaASC */
	private boolean alphaAsc=true;	
	
	/* ChronoDesc */
	private boolean chronoAsc=false;
	
	
	private long projId;

	/* remembers list position */
	private int listPosition;
	
	
	/* Sureness exists into the project */
	private long surenessField;

	
	public CitationHandler(Context context){
		
		this.baseContext=context;
		
		citationCnt= new CitationFilteredControler(baseContext);
		filteredCitationList=new ArrayList<Citation>();
		selectionElementsHash= new HashMap<Long, Citation>();
		selectionList=new ArrayList<Citation>();
		
		filterStates=new FilterStates();
	
		
	}
	
	public CitationHandler(Context context, long projId,long fieldId){
		
		this.baseContext=context;
		this.projId=projId;
		this.surenessField=fieldId;
		
		
		citationCnt= new CitationFilteredControler(baseContext);
		filteredCitationList=new ArrayList<Citation>();
		selectionElementsHash= new HashMap<Long, Citation>();
		selectionList=new ArrayList<Citation>();
		
		filterStates=new FilterStates();
		
		loadSurenessValues();
		
	}
	
	public void loadSurenessValues(){
		
		surenessValues=new HashMap<Long, String>();
		
		if(surenessField>-1){ 
			
			Cursor surenessValuesC=citationCnt.getCitationsFilteredBySureness(projId, surenessField);
			
			if(surenessValuesC!=null && surenessValuesC.getCount()>0){
				
				while(!surenessValuesC.isAfterLast()){
				
					surenessValues.put(surenessValuesC.getLong(0), surenessValuesC.getString(1));
					surenessValuesC.moveToNext();
				
				}
				
			}
			
		}
		
	}
	
	
	
	/*
	 * Main method that load all citations from project with @projId
	 * It also creates a hashMap that allows us to have fast access to each citations thanks to citationID 
	 */
	
	public int loadAllCitations(long projId){
		
		this.projId=projId;
		
		mainCitationList= new ArrayList<Citation>();
		mainCitationListHash=new HashMap<Long, Citation>();
	
		Cursor allCitations=citationCnt.getCitationsWithFirstFieldByProjectId(projId,alphaOrder,chronoAsc);
		allCitations.moveToFirst();				
		int i=0;
		
		while(!allCitations.isAfterLast()){
			
			long citId=allCitations.getLong(allCitations.getColumnIndex(CitacionDbAdapter.KEY_ROWID));
			
			Citation tmpCit=new Citation(
					
					allCitations.getLong(allCitations.getColumnIndex(CitacionDbAdapter.KEY_ROWID)), 
					allCitations.getString(allCitations.getColumnIndex(CitacionDbAdapter.VALUE)),
					allCitations.getString(allCitations.getColumnIndex(CitacionDbAdapter.DATE)), 
					allCitations.getDouble(allCitations.getColumnIndex(CitacionDbAdapter.LATITUDE)), 
					allCitations.getDouble(allCitations.getColumnIndex(CitacionDbAdapter.LONGITUDE)),
					i,
					filterLevel
				)
			;
			
			mainCitationListHash.put(citId, tmpCit);
			
			/* citationId, date, latitude, longitude */
			mainCitationList.add(tmpCit);
			
			i++;
			allCitations.moveToNext();
		}
		
		allCitations.close();
		
		if(!alphaAsc) Collections.reverse(mainCitationList);
		
		Log.i("Citations","1) Load All Citations [ALPHA active]= "+alphaOrder+" [chrono New->Old]= "+!chronoAsc+" [alpha A->Z]= "+alphaAsc);
		
		return mainCitationList.size();
				
	}
	

	
	
	public void reloadFilterStructure(long projId) {

		filterLevel=0;
		
		loadAllCitations(projId);
		int filters=filterStates.getLastFilter();
		
		filterStates.setUpdateState(false);

		
		for(int i=0; i<=filters; i++){
			
			String chosenFieldType=filterStates.getFilterType(i);
			String filterValue=filterStates.getFilterValue(i);
			
			if(chosenFieldType.equals("date")){
				
				loadFilteredCitationsByDate(projId, filterStates.getComparator(i), filterValue);
				
			}
			else if(chosenFieldType.equals("genus")){
				
				filterThByGenus(filterValue,null);
				
			}
			else if(chosenFieldType.equals("utm")){
				
				filterByUTM(filterValue);
				
			}
			else if(chosenFieldType.equals("latLong")){
				
				filterByLatLong(filterValue);
				
			}
			
			else if(chosenFieldType.equals("notExists")){
				
				filterThMissing(thC, null);
				
			}
			else if(chosenFieldType.equals("notOk")){
				
				filterNotOk(thC, null);
				
			}
			else if(chosenFieldType.equals("photo") || chosenFieldType.equals("multipPhoto")  ){
				
				loadFilteredCitationsByPhoto(projId, filterStates.getFieldId(i), filterValue);
				
			}
			else{
				
				loadFilteredCitationsByTextField(projId, filterStates.getFieldId(i), filterValue);
				
			}
			
			
		}
		
		filterStates.setUpdateState(true);
		
	}

	
	/*
	 * This method allows us to find citations with photos (filterValue=true) or without them (filterValue=false)
	 * 
	 */
	
	public int loadFilteredCitationsByPhoto(long projId, long photoField,String filterValue) {

		filterStates.addTextFilter("photo", photoField, filterValue);
				
		ArrayList<Citation> filteredWorkingCitationList=provideNextFilter();
				
		Cursor filteredCitations=citationCnt.getCitationsFilteredByPhoto(projId, photoField, filterValue, alphaOrder);
		
		Log.i("Filter", "(Photo) Filter Level: "+filterLevel+" Search: "+filterValue);
		
			ArrayList<String> citationTags= insertCitations(filteredCitations,filteredWorkingCitationList);

		createAlphaOrderedList(citationTags,getFilteredListAlpha());
		
		return filteredWorkingCitationList.size();

		
	}
	
	/*
	 * It loads all citations containing a field (with @fieldId) that's equal to @value
	 * 
	 */
	
	public int loadFilteredCitationsByTextField(long projId,long fieldId,String value){
		
		filterStates.addTextFilter("text", fieldId, value);
		
		ArrayList<Citation> filteredWorkingCitationList=provideNextFilter();
				
		Cursor filteredCitations=citationCnt.getCitationsFilteredByFieldValue(projId,fieldId,value,alphaOrder);
		
		Log.i("Filter", "(TextField) Filter Level: "+filterLevel+" Search: "+value);
		
			ArrayList<String> citationTags= insertCitations(filteredCitations,filteredWorkingCitationList);
		
		createAlphaOrderedList(citationTags,getFilteredListAlpha());
		
		return filteredWorkingCitationList.size();
	}
	
	
	/*
	 * Loads citations with date @comparator = {>,<,=}  with @value
	 *  
	 */
	
	public int loadFilteredCitationsByDate(long projId, String comparator, String value) {
		
		filterStates.addComparatorFilter("date", comparator, value);
		
		ArrayList<Citation> filteredCitation=provideNextFilter();
		
		Cursor filteredCitations=citationCnt.getCitationsFilteredByDate(projId,value,comparator,alphaOrder);
		
		ArrayList<String> citationTags= insertCitations(filteredCitations,filteredCitation);

		createAlphaOrderedList(citationTags,getFilteredListAlpha());
		
		return filteredCitation.size();
		
	}
	
	
	/*
	 * This filter allows us to find citations inside provided @UTM square
	 * 
	 */
	
	public int filterByUTM(String utm){
		
		filterStates.addComparatorFilter("utm", "=", utm);
		
		ArrayList<String> citationTags = new ArrayList<String>();
		
		String x=utm.substring(5,(utm.length()+5)/2);
	    String y=utm.substring((utm.length()+5)/2);
	
		ArrayList<Citation> filteredCitation=provideNextFilter();

		Iterator<Citation> it=mainCitationList.iterator();
		
		while(it.hasNext()){
			
			Citation tmpCitation=it.next();
			
			boolean compareUTM=tmpCitation.belongsToUTM(utm,x,y);
			
			if(compareUTM && citationInPreviousFilter(tmpCitation.getCitationId())) {
				
				tmpCitation.setFilterLevel(filterLevel);
				filteredCitation.add(tmpCitation);
				String citationTag=tmpCitation.getTag()+":"+tmpCitation.getCitationId();
				citationTags.add(citationTag);
				
			}
			
		}
		
		createAlphaOrderedList(citationTags,getFilteredListAlpha());

		return filteredCitation.size();
	}
	
	

	/*
	 * Filter citations inside @latLong provided
	 * 
	 */
	public int filterByLatLong(String latLong){
		
		filterStates.addComparatorFilter("latLong", "=", latLong);

		
		ArrayList<String> citationTags = new ArrayList<String>();
		ArrayList<Citation> filteredCitation=provideNextFilter();

		String[] split=latLong.split(" ");
		
		double latitude=Double.valueOf(split[0]);
		double longitude=Double.valueOf(split[1]);
		
		int precLat=getDecimals(latitude);
		int precLong=getDecimals(longitude);

		
		Iterator<Citation> it=mainCitationList.iterator();
		
		while(it.hasNext()){
			
			Citation tmpCitation=it.next();
			
			boolean compareLatLong=tmpCitation.belongsToLatLong(latitude,precLat,longitude,precLong);
			
			if(compareLatLong && citationInPreviousFilter(tmpCitation.getCitationId())){ 
				
				String citationTag=tmpCitation.getTag()+":"+tmpCitation.getCitationId();
				citationTags.add(citationTag);
				
				tmpCitation.setFilterLevel(filterLevel);
				filteredCitation.add(tmpCitation);
				
			}
			
			
		}
		
		createAlphaOrderedList(citationTags,getFilteredListAlpha());
		
		return filteredCitation.size();
		
	}
	
	
	/*
	 * It finds citations with taxon not belonging to current thesaurus
	 *  
	 */
	
	public int filterThMissing(ThesaurusControler tC, Handler thCheckHandler){
		
		thC=tC;
				
		filterStates.addTextFilter("notExists", -1, "");
		
		ArrayList<String> citationTags = new ArrayList<String>();
		ArrayList<Citation> filteredCitation=provideNextFilter();
		
		Iterator<Citation> it=mainCitationList.iterator();
		
		while(it.hasNext()){
			
			Citation tmpCitation=it.next();
			
			boolean belongsToTh=belongsToThesaurus(tmpCitation,tC);
			
			if(!belongsToTh && citationInPreviousFilter(tmpCitation.getCitationId())){ 
				
				citationTags.add(tmpCitation.getTag()+":"+tmpCitation.getCitationId());
				filteredCitation.add(tmpCitation);
				tmpCitation.setFilterLevel(filterLevel);
				
			}
			
		}
		
		createAlphaOrderedList(citationTags,getFilteredListAlpha());
		
		thCheckHandler.sendEmptyMessage(0);

		return filteredCitation.size();
		
	}

	/*
	 * It finds citations with taxon not belonging to current thesaurus
	 *  
	 */
	
	public int filterNotOk(ThesaurusControler tC, Handler thCheckHandler){
		
		thC=tC;
				
		filterStates.addTextFilter("notOk", -1, "");
		
		ArrayList<String> citationTags = new ArrayList<String>();
		ArrayList<Citation> filteredCitation=provideNextFilter();
		
		Iterator<Citation> it=mainCitationList.iterator();
		
		while(it.hasNext()){
			
			Citation tmpCitation=it.next();
			
			boolean notSure=isNotSure(tmpCitation.getCitationId());
			
			if(notSure && citationInPreviousFilter(tmpCitation.getCitationId())){ 
				
				citationTags.add(tmpCitation.getTag()+":"+tmpCitation.getCitationId());
				filteredCitation.add(tmpCitation);
				tmpCitation.setFilterLevel(filterLevel);
				
			}
			
		}
		
		createAlphaOrderedList(citationTags,getFilteredListAlpha());
		
		if(thCheckHandler!=null) thCheckHandler.sendEmptyMessage(0);

		return filteredCitation.size();
		
	}

	/*
	 * This method finds citations with taxon belonging to provided genus
	 * 
	 */

	public int filterThByGenus(String genus, Handler thCheckHandler){
		
		filterStates.addTextFilter("genus", -1, genus);

		
		ArrayList<String> citationTags = new ArrayList<String>();
		ArrayList<Citation> filteredCitation=provideNextFilter();
		
		Iterator<Citation> it=mainCitationList.iterator();
		
		while(it.hasNext()){
			
			Citation tmpCitation=it.next();
			
			boolean belongsToTh=tmpCitation.getTag().startsWith(genus);
			
			if(belongsToTh && citationInPreviousFilter(tmpCitation.getCitationId())){ 
				
				citationTags.add(tmpCitation.getTag()+":"+tmpCitation.getCitationId());
				filteredCitation.add(tmpCitation);
				tmpCitation.setFilterLevel(filterLevel);
			
			}
			
		}
		
		createAlphaOrderedList(citationTags,getFilteredListAlpha());

		if(thCheckHandler!=null) thCheckHandler.sendEmptyMessage(0);

		return filteredCitation.size();
		
	}
	
	
	private ArrayList<Citation> provideNextFilter() {

		ArrayList<Citation> tempList=null;
		
		
		switch (filterLevel) {
		
			case 3:
				
				thirdFilteredCitationList= new ArrayList<Citation>();
				tempList=thirdFilteredCitationList;
				break;
				
			case 2:
				
				thirdFilteredCitationList= new ArrayList<Citation>();
				tempList=thirdFilteredCitationList;
				filterLevel++;
				break;
				
			case 1:
				
				secondFilteredCitationList = new ArrayList<Citation>();
				tempList=secondFilteredCitationList;
				filterLevel++;
				break;
	
			case 0:
				
				filteredCitationList = new ArrayList<Citation>();
				tempList=filteredCitationList;
				filterLevel++;
				break;
	
			default:
				
				tempList=mainCitationList;
				break;
		}
			
		
		return tempList;
		
		
	}
	
	public ArrayList<Citation> calculateCurrentCitationList(){
		
		if(filterLevel>0) currentList= getFilteredCitationList();
		else currentList= getMainCitationList();
		
		return currentList;
		
	}
	

	
	
	public ArrayList<Citation> getFilteredCitationList() {
		
		ArrayList<Citation> tempFilter=null;
		
		switch (filterLevel) {
		
			case 3:
				
				if(alphaOrder) tempFilter=thirdFilteredCitationListAlphaOrdered;
				else tempFilter=thirdFilteredCitationList;
				break;
				
			case 2:
				
				if(alphaOrder) tempFilter=secondFilteredCitationListAlphaOrdered;
				else tempFilter= secondFilteredCitationList;
				
				break;
				
			case 1:
				
				if(alphaOrder) tempFilter=filteredCitationListAlphaOrdered;
				else tempFilter=filteredCitationList;
				
				break;
	
			default:
				
				break;
				
		}
		
		if(!alphaAsc && alphaOrder) return getReverseArrayList(tempFilter);
		
		return tempFilter;
		
	}
	
	private ArrayList<Citation> getReverseArrayList(ArrayList<Citation> citationList){
		
		ArrayList<Citation> alphaDisorder=new ArrayList<Citation>();
		
		int n=citationList.size();
		
		for(int i=n-1; i>=0; i--){
			
			alphaDisorder.add(citationList.get(i));
			
		}
		
		return alphaDisorder;
	}
	
	
	private ArrayList<Citation> getFilteredListAlpha() {

		ArrayList<Citation> tempList=null;
		
		
		switch (filterLevel) {
		
			case 3:
				
				thirdFilteredCitationListAlphaOrdered= new ArrayList<Citation>();
				tempList=thirdFilteredCitationListAlphaOrdered;
				break;
				
			case 2:
				
				secondFilteredCitationListAlphaOrdered= new ArrayList<Citation>();
				tempList=secondFilteredCitationListAlphaOrdered;
				break;
				
			case 1:
				
				filteredCitationListAlphaOrdered = new ArrayList<Citation>();
				tempList=filteredCitationListAlphaOrdered;
				break;
	
			
			default:
				
				tempList=mainCitationList;
				break;
		}
		
				
		return tempList;
		
		
	}
	

	private ArrayList<String> insertCitations(Cursor filteredCitations, ArrayList<Citation> filtered){
		
		ArrayList<String> citationTags = new ArrayList<String>();
		
		filteredCitations.moveToFirst();				
		
		while(!filteredCitations.isAfterLast()){
			
			/* citationId, date, latitude, longitude */
			
			long citationId=filteredCitations.getLong(filteredCitations.getColumnIndex(CitacionDbAdapter.KEY_ROWID));
			
			if(citationInPreviousFilter(citationId)){			
			
				Citation tmpCit=mainCitationListHash.get(citationId);
				tmpCit.setFilterLevel(filterLevel);
				
				String citationTag=tmpCit.getTag()+":"+citationId;
				citationTags.add(citationTag);
				
				filtered.add(tmpCit);
			
			}
				
			filteredCitations.moveToNext();
			
		}
		
		filteredCitations.close();
		
		return citationTags;
		
	}
	
	
	
	private boolean citationInPreviousFilter(long citationId) {

		Citation tmpCit=mainCitationListHash.get(citationId);
		
		if(tmpCit!=null){
			
			if(tmpCit.getFilterLevel()==filterLevel-1) return true;
		}
		
		return false;
	}
	
	
	
	private void createAlphaOrderedList(ArrayList<String> citationTags,ArrayList<Citation> filteredCitationsOrdered) {
		
		int i=0;
			
		 	Collections.sort(citationTags);
		
			Iterator<String> itOrder=citationTags.iterator();
			
			
			while(itOrder.hasNext()){
				
				String[] citationId=itOrder.next().split(":");
				
				Citation tmp=mainCitationListHash.get(Long.valueOf(citationId[1]));
				
				filteredCitationsOrdered.add(
						
						new Citation(
								Long.valueOf(citationId[1]), 
								tmp.getTag(),
								tmp.getDate(), 
								tmp.getLatitude(), 
								tmp.getLongitude(),
							i
						)
					);
					
					i++;
				
			}
					
	}

	
	private boolean belongsToThesaurus(Citation tmpCitation, ThesaurusControler tC) {
		
		Cursor element=tC.fetchThesaurusItembyName(tmpCitation.getTag());
		
		if(element!=null && element.getCount()>0){ 
			
			element.close();
			return true;
			
		}
		else{ 
			
			if(element!=null) element.close();
			return false;
			
		}
		
	}
	
	
	private int getDecimals(double double1 ){
		
		int decimals=0;
		while ((int)double1 % 10 !=0)
		{
			double1*=10;
			decimals++;
		}
		
		return decimals;
		
	}



	public CitationListAdapter getListAdapter(boolean filtered) {

		citationListAdapt= new CitationListAdapter(baseContext,this,projId);
		
		return citationListAdapt;
	}


	public ArrayList<Citation> getMainCitationList() {
		
		return mainCitationList;
		
	}
	
	public int getCitationPosition(long citationId){
		
		return mainCitationListHash.get(citationId).getPosition();
		
	}


	public void setCheckedItem(long id,int position,boolean checked) {

		if(checked){
			
			Citation tmpCit=mainCitationList.get(position);
			selectionList.add(tmpCit);
			selectionElementsHash.put(id, tmpCit);
							
		}
		else{
			
			Citation tmpCit=selectionElementsHash.get(id);
			selectionList.remove(tmpCit);
			selectionElementsHash.remove(id);
		
		}
		
		String textSelected=String.format(baseContext.getString(R.string.countSelected), getSelectionList().size());
		tvSelected.setText(Html.fromHtml(textSelected));
		
	}
	
	public void checkAllItems(boolean filtered){
		
		selectionList= new ArrayList<Citation>();
		selectionElementsHash= new HashMap<Long, Citation>();
		
		ArrayList<Citation> citations=null;
		
		if(filtered) citations=filteredCitationList;
		else citations=mainCitationList;
		
		Iterator<Citation> it=citations.iterator();
		
		while(it.hasNext()){
			
			Citation tmpCit=it.next();
			
			tmpCit.setChecked(true);
			
			selectionList.add(tmpCit);
			selectionElementsHash.put(tmpCit.getCitationId(), tmpCit);
		}
		
	}
	
	public void unCheckAllItems(boolean filtered){
		
		selectionList= new ArrayList<Citation>();
		selectionElementsHash= new HashMap<Long, Citation>();
		
		ArrayList<Citation> citations=null;
		
		if(filtered) citations=filteredCitationList;
		else citations=mainCitationList;
		
		Iterator<Citation> it=citations.iterator();
		
		while(it.hasNext()){
			
			Citation tmpCit=it.next();
			
			tmpCit.setChecked(false);
		
		}
		
	}
	
	public void printSelectedItems(){
		
		Collection<Citation> chosenElem=selectionElementsHash.values();
		
		Iterator<Citation> elemIt= chosenElem.iterator();
		
		while(elemIt.hasNext()){
			
			Citation tmpCit=elemIt.next();
			
			Log.i("NewCit","Selected: "+tmpCit.getTag());
			
		}
		
		
	}
	
	public boolean isCheckedItem(long id){
		
		Citation tmpCit=selectionElementsHash.get(id);
		
		return tmpCit!=null;
		
	}

	public void setMainCitationList(ArrayList<Citation> mainCitationList) {
		
		this.mainCitationList = mainCitationList;
		
	}


	public ArrayList<Citation> getSelectionList() {
		
		return selectionList;
		
	}

	public Set<Long> getSelectedCitationsId(){
		
		return selectionElementsHash.keySet();
		
	}
	

	public void setSelectionList(ArrayList<Citation> selectionList) {
		
		this.selectionList = selectionList;
		
	}
	
	
	public void removeFilter() {
	
		Log.i("Filter", "(Removing Filter) Filter Level: "+filterLevel);

		switch (filterLevel) {
			
			case 3:
				
				removeCitationFromFilter(thirdFilteredCitationList,2);
				thirdFilteredCitationList= new ArrayList<Citation>();
				thirdFilteredCitationListAlphaOrdered = new ArrayList<Citation>();
				filterLevel--;
				filterStates.removeFilter();
				
				break;
				
			case 2:
				
				
				removeCitationFromFilter(secondFilteredCitationList,1);
				secondFilteredCitationList=new ArrayList<Citation>();
				secondFilteredCitationListAlphaOrdered = new ArrayList<Citation>();
				filterLevel--;
				filterStates.removeFilter();

				
				break;
				
			case 1:
				
				removeCitationFromFilter(filteredCitationList,0);
				filteredCitationList=new ArrayList<Citation>();
				filteredCitationListAlphaOrdered= new ArrayList<Citation>();
				filterLevel--;
				filterStates.removeFilter();

				break;
	
			default:
				
				break;
				
		}
		
		Log.i("Filter", "(Filter removed) Filter Level: "+filterLevel);

	
	}


	private void removeCitationFromFilter(ArrayList<Citation> filteredCitationList, int newFilterLevel) {

		Iterator<Citation> filIterator=filteredCitationList.iterator();
		
		while(filIterator.hasNext()){
			
			Citation tmpCit=filIterator.next();
			tmpCit.setFilterLevel(newFilterLevel);			
			
		}
		
	}
	
	
	/*
	 *  Creates a string of citations ids. Those can be used 
	 *  to transfer a list of chosen ids to other Activities
	 * 
	 */
		
	public String createIdString(){
		
		String ids="";
		
		Set<Long> selectionIds=getSelectedCitationsId();
    	
    	  Iterator<Long> iter = selectionIds.iterator();
	    	   
    	  while (iter.hasNext()) {
	    	   		
    		  	long citationId=iter.next();
    		  	ids=ids+":"+citationId;
		   
    	    }
    	  
    	  return ids;
	}



	public void setFilteredCitationList(ArrayList<Citation> filteredCitationList) {
		this.filteredCitationList = filteredCitationList;
	}


	public boolean isAlphaOrder() {
		return alphaOrder;
	}
	
	public boolean isChronoOrder(){
		
		return !alphaOrder;
		
	}


	public boolean isAlphaAsc() {
		return alphaAsc;
	}

	public boolean isChronoAsc() {
		return chronoAsc;
	}

	public void setChronoAsc(boolean chronoAsc) {
		this.chronoAsc = chronoAsc;
	}

	public void setAlphaAsc(boolean alphaAsc) {
		this.alphaAsc = alphaAsc;
	}

	public void setAlphaOrder(boolean alphaOrder) {
		this.alphaOrder = alphaOrder;
	}


	public TextView getTvSelected() {
		return tvSelected;
	}


	public void setTvSelected(TextView tvSelected) {
		this.tvSelected = tvSelected;
	}


	public int getListPosition() {
		return listPosition;
	}


	public void setListPosition(int listPosition) {
		this.listPosition = listPosition;
	}


	public void setSurenessField(long surenessField) {
		this.surenessField = surenessField;
	}


	public long getSurenessField() {
		return surenessField;
	}

	public boolean isNotSure(long citationId) {
	
		return surenessValues.get(citationId)!=null;
		
	}

	public int getFilterLevel() {
		return filterLevel;
	}

	public void setFilterLevel(int filterLevel) {
		this.filterLevel = filterLevel;
	}

	public ArrayList<Citation> getCurrentList() {
		return currentList;
	}

	public void setCurrentList(ArrayList<Citation> currentList) {
		this.currentList = currentList;
	}

	
}
