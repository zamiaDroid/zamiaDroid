package uni.projecte.dataLayer.CitationManager.objects;

public class FilterStates {

	private String[] filterTypes;
	private String[] filterValues;
	private long[] fieldIds;
	private String[] comparators;
	
	private int lastFilter=-1;
	private int N=3;
	
	private boolean updateState;
	
	
	public FilterStates(){
		
		filterTypes=new String[N];
		filterValues=new String[N];
		fieldIds=new long[N];
		comparators=new String[N];
		
		updateState=true;
		
	}
	
	public int addTextFilter(String filterType,long fieldId, String filterValue){
		
		if(updateState){
		
			lastFilter++;
			
			filterTypes[lastFilter]=filterType;
			fieldIds[lastFilter]=fieldId;
			filterValues[lastFilter]=filterValue;		
		
		}
		
		return lastFilter;
		
	}
	
	public int addComparatorFilter(String filterType,String comparator, String filterValue){
		
		if(updateState){
		
			lastFilter++;
			
			filterTypes[lastFilter]=filterType;
			comparators[lastFilter]=comparator;
			filterValues[lastFilter]=filterValue;	
		
		}
		
		return lastFilter;
		
	}
	
	public String getFilterType(int filterId){
		
		return filterTypes[filterId];
		
	}
	
	public String getFilterValue(int filterId){
		
		return filterValues[filterId];
		
	}
	
	public String getComparator(int filterId){
		
		return comparators[filterId];
		
	}
	
	public long getFieldId(int filterId){
		
		return fieldIds[filterId];
		
	}
	

	
	public int removeFilter(){

		filterTypes[lastFilter]="";
		filterValues[lastFilter]="";
		fieldIds[lastFilter]=-1;
		comparators[lastFilter]="";
		
		lastFilter--;
		
		return lastFilter;
		
	}

	public int getLastFilter() {
		return lastFilter;
	}

	public boolean isUpdateState() {
		return updateState;
	}

	public void setUpdateState(boolean updateState) {
		this.updateState = updateState;
	}

	
}
