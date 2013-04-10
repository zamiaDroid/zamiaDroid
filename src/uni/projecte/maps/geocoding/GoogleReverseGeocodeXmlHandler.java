package uni.projecte.maps.geocoding;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class GoogleReverseGeocodeXmlHandler extends DefaultHandler 
{
	private boolean inAddress_component = false;
	private boolean finished = false;
	private StringBuilder builder;
	private String localityName;
	private String country;
	
	private String tmpType;
	private String tmpShortName;
	private String tmpLongName;
	
	
	public String getLocalityName()
	{
		return this.localityName;
	}
	public String getCountry()
	{
		return this.country;
	}
	
	@Override
	public void characters(char[] ch, int start, int length)
	       throws SAXException {
	    super.characters(ch, start, length);
	    if (this.inAddress_component && !this.finished)
	    {
	    	if ((ch[start] != '\n') && (ch[start] != ' '))
	    	{
	    		builder.append(ch, start, length);
	    	}
	    }
	}

	@Override
	public void endElement(String uri, String localName, String name)
	        throws SAXException 
	{
	    super.endElement(uri, localName, name);
	
	    	if (localName.equalsIgnoreCase("long_name"))
	    	{
	    		this.tmpLongName = builder.toString();
	    	}
	    	else if (localName.equalsIgnoreCase("short_name"))
	    	{
	    		this.tmpShortName = builder.toString();
	    		//this.finished = true;
	    	}
	    	else if (localName.equalsIgnoreCase("type"))
	    	{
	    		
	    		String tmp=builder.toString();
	    			
	    		if(!tmp.equals("political")) this.tmpType=tmp;
	    				
	    		
	    		//this.finished = true;
	    	}
	    	else if(localName.equalsIgnoreCase("address_component")){

	    		if(tmpType.equals("locality")) localityName=tmpLongName;
	    		else if(tmpType.equals("country")) country=tmpShortName;
	    		
	    	}
	    	
	    	if (builder != null)
	    	{
	    		builder.setLength(0);
	    	}
	    
    	}

    @Override
    public void startDocument() throws SAXException 
    {
        super.startDocument();
        builder = new StringBuilder();
    }

    @Override
    public void startElement(String uri, String localName, String name, Attributes attributes) throws SAXException
    {
    	super.startElement(uri, localName, name, attributes);
    	
    	if (localName.equalsIgnoreCase("address_component"))
    	{
    		this.inAddress_component = true;
    	}
    
    }
}
