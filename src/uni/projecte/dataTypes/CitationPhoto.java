package uni.projecte.dataTypes;

public class CitationPhoto {
	

	private String photoPath;
	private long citationId;
	private long citationFieldId;
	private long fieldType;
	private String label;
	private String photoType;
	
	
	public CitationPhoto(String photoPath, long citationId, long citationFieldId, long fieldType) {
		super();
		
		this.photoPath = photoPath;
		this.citationId = citationId;
		this.citationFieldId = citationFieldId;
		this.fieldType=fieldType;
		
	}
	
	public CitationPhoto(String photoPath, long citationId, long citationFieldId, String photoType) {
				
		this.photoPath = photoPath;
		this.citationId = citationId;
		this.citationFieldId = citationFieldId;
		this.photoType=photoType;
		
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

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public String getPhotoType() {
		return photoType;
	}

	public void setPhotoType(String photoType) {
		this.photoType = photoType;
	}	

}
