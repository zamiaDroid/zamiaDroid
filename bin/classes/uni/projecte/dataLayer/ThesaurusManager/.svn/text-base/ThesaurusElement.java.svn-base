package uni.projecte.dataLayer.ThesaurusManager;

import uni.projecte.dataTypes.Utilities;
import android.content.Context;

public class ThesaurusElement {
	
	private long id;
	private String thName;
	private String thType;
	private int thItemCount;
	private String sourceId;
	private String sourceType;
	
	

	public ThesaurusElement(long id,String thName, String thType, int thItemCount, String sourceId, String sourceType) {
		super();
		
		this.id=id;
		this.thName = thName;
		this.thType = thType;
		this.thItemCount = thItemCount;
		this.sourceId = sourceId;
		this.sourceType = sourceType;
		
	}
	
	
	public String getThName() {
		return thName;
	}
	public String getThType() {
		return thType;
	}
	public int getThItemCount() {
		return thItemCount;
	}
	public void setThName(String thName) {
		this.thName = thName;
	}
	public void setThType(String thType) {
		this.thType = thType;
	}
	public void setThItemCount(int thItemCount) {
		this.thItemCount = thItemCount;
	}
	public long getId() {
		return id;
	}


	public void setId(long id) {
		this.id = id;
	}


	public String getSourceId() {
		return sourceId;
	}


	public String getSourceType() {
		return sourceType;
	}


	public void setSourceId(String sourceId) {
		this.sourceId = sourceId;
	}


	public void setSourceType(String sourceType) {
		this.sourceType = sourceType;
	}


	public String getFilumLetter(Context baseContext) {

	  String filum=Utilities.translateThTypeToFilumLetter(baseContext, thType);

	  return filum;
	  
	  
	}
	
	

}
