package uni.projecte.dataLayer.CitationManager.Synchro;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ZamiaCitation {

   @Expose
    private String id="";
    private long internalId;

    @Expose
	private String originalTaxonName="";

	@Expose
    private String observationDate="";
    @Expose
    private String citationNotes="";
    @Expose
    private String altitude="";
    @Expose    
    private double latitude;
    @Expose
    private double longitude;
    @Expose
    private String sureness="";
    
    @Expose
    private String phenology="";
    
    @Expose
    private String natureness="";
    
    @Expose
    private String observationAuthor="";
    
    @Expose   
    private String utm="";
    
    @Expose
    private String locality="";


	public String getLocality() {
		return locality;
	}

	public void setLocality(String locality) {
		this.locality = locality;
	}

	public String getObservationAuthor() {
		return observationAuthor;
	}

	public void setObservationAuthor(String observationAuthor) {
		this.observationAuthor = observationAuthor;
	}

	public long getInternalId() {
		return internalId;
	}

	public void setInternalId(long internalId) {
		this.internalId = internalId;
	}
    
    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    
    public String getSureness() {
        return sureness;
    }

    public void setSureness(String Sureness) {
        this.sureness = Sureness;
    }

    public String getCitationNotes() {
        return citationNotes;
    }

    public void setCitationNotes(String CitationNotes) {
        this.citationNotes = CitationNotes;
    }

    public String getAltitude() {
        return altitude;
    }

    public void setAltitude(String altitude) {
        this.altitude = altitude;
    }

    public ZamiaCitation() {}

        
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getOriginalTaxonName() {
		return originalTaxonName;
	}

	public void setOriginalTaxonName(String originalTaxonName) {
		this.originalTaxonName = originalTaxonName;
	}

    public String getObservationDate() {
        return observationDate;
    }

    public void setObservationDate(String observationDate) {
        this.observationDate = observationDate;
    }
    
    
    public String getPhenology() {
        return phenology;
    }

    public void setPhenology(String phenology) {
        this.phenology = phenology;
    }

    public String getNatureness() {
        return natureness;
    }

    public void setNatureness(String natureness) {
        this.natureness = natureness;
    }
    

    public String getUtm() {
		return utm;
	}

	public void setUtm(String utm) {
		this.utm = utm;
	}
}
