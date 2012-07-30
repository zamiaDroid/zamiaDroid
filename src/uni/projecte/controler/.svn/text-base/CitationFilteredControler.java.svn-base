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


package uni.projecte.controler;

import uni.projecte.dataLayer.bd.CitacionDbAdapter;
import android.content.Context;
import android.database.Cursor;

public class CitationFilteredControler extends CitationControler{

	public CitationFilteredControler(Context baseContext) {

		super(baseContext);

	}

	public Cursor getCitationsFilteredByFieldValue(long projId, long fieldId,String value, boolean alphaOrder) {
			
		CitacionDbAdapter mDbAttributes = new CitacionDbAdapter(baseContext);
		
		mDbAttributes.open();
		
			Cursor cursor= mDbAttributes.fetchCitationsByFieldValue(projId,fieldId,value,alphaOrder);
		
		mDbAttributes.close();
		
		return cursor;
		
	}
	
	
	/*public Cursor getCitationsFilteredByNumericalFieldValue(long projId,long fieldId,float value, int comparator) {
		
		String comp="=";
		
		
		CitacionDbAdapter mDbAttributes = new CitacionDbAdapter(baseContext);
		
		mDbAttributes.open();
		
		Cursor cursor= mDbAttributes.fetchCitationsByFieldValue(projId,fieldId,value,comp);
				
		
		mDbAttributes.close();
		
		return cursor;
		
	}*/

	public Cursor getCitationsFilteredByDate(long projId, String value,String comparator, boolean alphaOrder) {
	
		CitacionDbAdapter mDbAttributes = new CitacionDbAdapter(baseContext);
		
		mDbAttributes.open();
		
			Cursor cursor= mDbAttributes.fetchCitationsByDate(projId,comparator,value,alphaOrder);
		
		mDbAttributes.close();
		
		return cursor;
	}

	public Cursor getCitationsFilteredByPhoto(long projId, long photoField, String filterValue, boolean alphaOrder) {
		
		boolean containsPhoto=false;
		
		if(filterValue.equals("true")) containsPhoto=true;
		
		CitacionDbAdapter mDbAttributes = new CitacionDbAdapter(baseContext);
		mDbAttributes.open();
	
			Cursor cursor= mDbAttributes.fetchCitationsByPhoto(projId,photoField,containsPhoto, alphaOrder);
		
		mDbAttributes.close();
		
		return cursor;
	}
	
	public Cursor getCitationsFilteredBySureness(long projId, long surenessField) {
	
		CitacionDbAdapter mDbAttributes = new CitacionDbAdapter(baseContext);
		mDbAttributes.open();
	
			Cursor cursor= mDbAttributes.fetchCitationsBySureness(projId,surenessField);
		
		mDbAttributes.close();
		
		return cursor;
	}

	
	
	
}
