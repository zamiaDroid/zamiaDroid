package uni.projecte.dataTypes;

public class CitationPhoto {
	

	private String photoPath;
	private long citationId;
	private long citationFieldId;
	private long fieldType;
	
	
	public CitationPhoto(String photoPath, long citationId, long citationFieldId, long fieldType) {
		super();
		
		this.photoPath = photoPath;
		this.citationId = citationId;
		this.citationFieldId = citationFieldId;
		this.fieldType=fieldType;
		
	}
	
	public String getPhotoPath() {
		return photoPath;
	}
	public long getCitationId() {
		return citationId;
	}
	public long getCitationFieldId() {
		return citationFieldId;
	}
	public void setPhotoPath(String photoPath) {
		this.photoPath = photoPath;
	}
	public void setCitationId(long citationId) {
		this.citationId = citationId;
	}
	public void setCitationFieldId(long citationFieldId) {
		this.citationFieldId = citationFieldId;
	}

	public long getFieldType() {
		return fieldType;
	}

	public void setFieldType(long fieldType) {
		this.fieldType = fieldType;
	}	

}
