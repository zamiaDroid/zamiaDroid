/*
 * Copyright (C) 2008 Google Inc.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */

package uni.projecte.dataLayer.bd;

import java.util.Date;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.CursorJoiner;
import android.database.MatrixCursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.text.format.DateFormat;
import android.util.Log;

/**
 * Simple Sample database access helper class
 */
public class CitacionDbAdapter {

	
	/* citacion main fields */
	public static final String KEY_ROWID = "_id";
    public static final String KEY_RS = "idRs";
    public static final String DATE = "date";
    public static final String LATITUDE = "latitude";
    public static final String LONGITUDE = "longitude";
    public static final String COMMENT = "comment";
    public static final String SINCRONIZED="sincronized";

	/* filled fields */
    
	public static final String KEY_SAMPLE_ID = "idSample";
    public static final String KEY_TIPUS_ATRIB = "idAttType";
    public static final String VALUE = "value";
    public static final String FIELD_NAME = "fieldName";


    private static final String TAG = "SampleDbAdapter";
    
    
    protected DatabaseHelper mDbHelper;
    protected SQLiteDatabase mDb;
    
    /**
     * Database creation sql statement
     */
    
    
    private static final String CITATION_DATABASE_CREATE =
            "create table CitationTable ("
            + KEY_ROWID + " INTEGER PRIMARY KEY,"
            + KEY_RS + " INTEGER,"
            + LATITUDE + " DOUBLE,"
            + LONGITUDE + " DOUBLE,"
            + COMMENT + " TEXT,"
            + DATE + " TEXT,"
            + SINCRONIZED + " BOOLEAN"
            + ");";
    
    
    private static final String FIELD_DATABASE_CREATE =
        "create table CitationFieldTable ("
        + KEY_ROWID + " INTEGER PRIMARY KEY,"
        + KEY_SAMPLE_ID + " INTEGER,"
        + KEY_TIPUS_ATRIB + " INTEGER,"
        + VALUE + " TEXT,"
        + FIELD_NAME + " TEXT"
        + ");";

    protected static final String DATABASE_TABLE_FIELD = "CitationFieldTable";
    protected static final String DATABASE_TABLE_CITATION = "CitationTable";
    
    private static final String DATABASE_NAME= "Citation";
    private static final int DATABASE_VERSION = 2;

    private final Context mCtx;
    

    static class DatabaseHelper extends SQLiteOpenHelper {

        DatabaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        public void onCreate(SQLiteDatabase db) {

            db.execSQL(CITATION_DATABASE_CREATE);
            db.execSQL(FIELD_DATABASE_CREATE);

        }

        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            Log.w(TAG, "Upgrading database from version " + oldVersion + " to "
                    + newVersion + ", which will destroy all old data");
            db.execSQL("DROP TABLE IF EXISTS notes");
            onCreate(db);
        }
    }

    /**
     * Constructor - takes the context to allow the database to be
     * opened/created
     * 
     * @param ctx the Context within which to work
     */
    public CitacionDbAdapter(Context ctx) {
        this.mCtx = ctx;
    }

    /**
     * Open the Sample database. If it cannot be opened, try to create a new
     * instance of the database. If it cannot be created, throw an exception to
     * signal the failure
     * 
     * @return this (self reference, allowing this to be chained in an
     *         initialization call)
     * @throws SQLException if the database could be neither opened or created
     */
    public CitacionDbAdapter open() throws SQLException {
        mDbHelper = new DatabaseHelper(mCtx);
        mDb = mDbHelper.getWritableDatabase();
        return this;
    }
    
    public void close() {
   
        mDbHelper.close();
    }
    
    
    public long createEmptyCitation(long rsId) {

    	ContentValues initialValues = new ContentValues();
        initialValues.put(KEY_RS, rsId);        
        initialValues.put(SINCRONIZED , 0);
        

        return mDb.insert(DATABASE_TABLE_CITATION, null, initialValues);
        
    }
    
    public boolean updateLocation(long sampleId,double lat, double longitude){
    	
       	ContentValues vals = new ContentValues();
		vals.put(LATITUDE,lat);
		vals.put(LONGITUDE,longitude);
		
      return mDb.update(DATABASE_TABLE_CITATION, vals, KEY_ROWID + "=" + sampleId, null) > 0;
    	
    	
    }
    
    public boolean updateDate(long sampleId,String date){
    	
       	ContentValues vals = new ContentValues();
		vals.put(DATE,date);
		
      return mDb.update(DATABASE_TABLE_CITATION, vals, KEY_ROWID + "=" + sampleId, null) > 0;
    	
    }

    /**
     * Create a new Sample using the rsID, latitude, longitude and comment. If the Sample is
     * successfully created return the new rowId for that note, otherwise return
     * a -1 to indicate failure.
     * 
     * @param rsId the identifier of the research
     * @param latitude the latitude of the Sample
     * @param longitude the longitude of the Sample
     * @param comment the comment of the Sample
     * @return rowId or -1 if failed
     */
    public long createCitation(long rsId, double latitude, double longitude, String comment) {
        

    	ContentValues initialValues = new ContentValues();
        initialValues.put(KEY_RS, rsId);
        initialValues.put(LATITUDE , latitude);
        initialValues.put(LONGITUDE , longitude);
        initialValues.put(COMMENT , comment);

        Date date = new Date();
        date.getDate();
 
        initialValues.put(DATE , (String) DateFormat.format("yyyy-MM-dd kk:mm:ss", date));
        initialValues.put(SINCRONIZED , 0);
        

        return mDb.insert(DATABASE_TABLE_CITATION, null, initialValues);
    }
    
    
    public long createCitationWithDate(long rsId, double latitude, double longitude, String comment,String date) {
        

    	ContentValues initialValues = new ContentValues();
        initialValues.put(KEY_RS, rsId);
        initialValues.put(LATITUDE , latitude);
        initialValues.put(LONGITUDE , longitude);
        initialValues.put(COMMENT , comment);
        initialValues.put(DATE , date);

        
        initialValues.put(SINCRONIZED , 0);
        

        return mDb.insert(DATABASE_TABLE_CITATION, null, initialValues);
    }

    /**
     * Delete the Sample with the given rowId
     * 
     * @param rowId id of note to delete
     * @return true if deleted, false otherwise
     */
    public boolean deleteCitation(long rowId) {

        return mDb.delete(DATABASE_TABLE_CITATION, KEY_ROWID + "=" + rowId, null) > 0;

    }

    /**
     * Return a Cursor over the list of all Samples in the database
     * 
     * @return Cursor over all notes
     */
    public Cursor fetchAllCitations() {

        return mDb.query(DATABASE_TABLE_CITATION, new String[] {KEY_ROWID, KEY_RS,
                LATITUDE,LONGITUDE,DATE}, null, null, null, null, null);
    }

    /**
     * Return a Cursor positioned at the Sample that matches the given rowId
     * 
     * @param rowId id of Sample to retrieve
     * @return Cursor positioned to matching note, if found
     * @throws SQLException if note could not be found/retrieved
     */
    public Cursor fetchSamplesByResearchId(long rowId) throws SQLException {

     /*   Cursor mCursor =

                mDb.query(true, DATABASE_TABLE_CITATION, new String[] {KEY_ROWID,
                		KEY_RS,
                        LATITUDE,LONGITUDE,DATE,SINCRONIZED}, KEY_RS + "=" + rowId, null,
                        null, null, null, null);
                        
                        
                        */
    	
       	Cursor mCursor=mDb.rawQuery("SELECT DISTINCT latitude,longitude,value,date,fieldName,CitationTable._id FROM " + DATABASE_TABLE_CITATION +","+DATABASE_TABLE_FIELD
				+ " WHERE idRs="+rowId+" and CitationTable._id="+CitacionDbAdapter.KEY_SAMPLE_ID+" ORDER BY CitationFieldTable._id;",null);
    	
    	
    	
        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;

    }
    
    
    public Cursor fetchSamplesByTaxon(long rowId) throws SQLException {

        Cursor mCursor =

                mDb.query(true, DATABASE_TABLE_CITATION, new String[] {KEY_ROWID,
                		KEY_RS,
                        LATITUDE,LONGITUDE,DATE,SINCRONIZED}, KEY_RS + "=" + rowId, null,
                        null, null, null, null);
        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;

    }
    
    
	public boolean isTimestampAvailable(long projId, String presettedDate) {

		 Cursor mCursor =

             mDb.query(true, DATABASE_TABLE_CITATION, new String[] {KEY_ROWID,
             		KEY_RS,
                     LATITUDE,LONGITUDE,DATE,SINCRONIZED}, KEY_RS + "=" + projId +" AND "+ DATE +" = "+"\""+presettedDate+"\"", null,
                     null, null, null, null);
     
		 if (mCursor != null) {
			 
			 mCursor.moveToFirst();
         
		 }
		
		return mCursor.getCount()<1;
	}
    
    
    public Cursor fetchSampleBySampleId(long rowId) throws SQLException {

        Cursor mCursor =

                mDb.query(true, DATABASE_TABLE_CITATION, new String[] {KEY_ROWID,
                		KEY_RS,
                        LATITUDE,LONGITUDE,DATE,SINCRONIZED}, KEY_ROWID + "=" + rowId, null,
                        null, null, null, null);
        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;

    }
    
	public Cursor getNextCitationId(long citationId) {
		
		 Cursor mCursor =

             mDb.query(true, DATABASE_TABLE_CITATION, new String[] {KEY_ROWID,
             		KEY_RS,
                     LATITUDE,LONGITUDE,DATE,SINCRONIZED}, KEY_ROWID + ">" + citationId, null,
                     null, null, null, null);
     
		 if (mCursor != null) {
			 
			 mCursor.moveToFirst();
			 
		 }
     	
     return mCursor;
     
     
	}
	
	public Cursor getPreviousCitationId(long citationId) {
		
		 Cursor mCursor =

            mDb.query(true, DATABASE_TABLE_CITATION, new String[] {KEY_ROWID,
            		KEY_RS,
                    LATITUDE,LONGITUDE,DATE,SINCRONIZED}, KEY_ROWID + "<" + citationId, null ,
                    null, null, KEY_ROWID+" DESC",null);
    
		 if (mCursor != null) {
			 
			 mCursor.moveToFirst();
			 
		 }
    	
    return mCursor;
    
    
	}
    
    public Cursor fetchSampleBySampleIdWithFirstField(long citationId) throws SQLException {

        Cursor 
    		c=mDb.rawQuery("SELECT CitationTable._id as _id,value, date,CitationFieldTable._id as idField, latitude,longitude FROM " + DATABASE_TABLE_FIELD+","+DATABASE_TABLE_CITATION
				+ " WHERE CitationTable._id="+citationId+" and CitationTable._id="+CitacionDbAdapter.KEY_SAMPLE_ID+ " GROUP BY CitationTable._id;",null);

  
        if (c != null) {
            c.moveToFirst();
        }
        return c;

    }
    
    public Cursor fetchCitationByCitationId(long citationId) throws SQLException {

        Cursor mCursor =

               mDb.query(true, DATABASE_TABLE_CITATION, new String[] {KEY_ROWID,
               		KEY_RS,
                       LATITUDE,LONGITUDE,DATE,SINCRONIZED}, KEY_ROWID + "=" + citationId, null,
                       null, null, null, null);
       
       
       if (mCursor != null) {
       	
           mCursor.moveToFirst();
       }
       return mCursor;

   }
    
    public Cursor fetchUnsyncronisedSamples(long rowId) throws SQLException {

         Cursor mCursor =

                mDb.query(true, DATABASE_TABLE_CITATION, new String[] {KEY_ROWID,
                		KEY_RS,
                        LATITUDE,LONGITUDE,DATE,SINCRONIZED}, KEY_RS + "=" + rowId +" and "+SINCRONIZED+ "= '0'", null,
                        null, null, null, null);
        
        
        if (mCursor != null) {
        	
            mCursor.moveToFirst();
        }
        return mCursor;

    }

    /**
     * Update the Sample to sincronized using the details provided.
     * 
     * @return true if the note was successfully updated, false otherwise
     */
    
    
    public boolean updateToSyncronised(long rowId) {
       
    	ContentValues vals = new ContentValues();
    		vals.put(SINCRONIZED,1);
    		
    		
          return mDb.update(DATABASE_TABLE_CITATION, vals, KEY_ROWID + "=" + rowId, null) > 0;
    }
    
    
    /**
     * Create a new SampleAttribute using the idSample, attTypeId and value provided. If the SampleAttribute is
     * successfully created return the new rowId for that note, otherwise return
     * a -1 to indicate failure.
     * 
     * @param idSample the idSample of the SampleAttribute
     * @param idAttribType the idAttribType of the SampleAttribute
     * @param value the value of the SampleAttribute
     * @return rowId or -1 if failed
     */
    public long createCitationField(long idSample, long idAttribType, String value,String fieldName) {
        ContentValues initialValues = new ContentValues();
        
        initialValues.put(KEY_SAMPLE_ID, idSample);
        initialValues.put(KEY_TIPUS_ATRIB, idAttribType);
        initialValues.put(VALUE , value);
        initialValues.put(FIELD_NAME , fieldName);


        return mDb.insert(DATABASE_TABLE_FIELD, null, initialValues);
    }
    

    /**
     * Delete the Sample Attribute with the given rowId
     * 
     * @param rowId id of note to delete
     * @return true if deleted, false otherwise
     */
    public boolean deleteSampleAttribute(long rowId) {

        return mDb.delete(DATABASE_TABLE_FIELD, KEY_ROWID + "=" + rowId, null) > 0;
    }
    
    
    public boolean deleteField(long fieldId) {

        return mDb.delete(DATABASE_TABLE_FIELD, KEY_TIPUS_ATRIB + "=" + fieldId, null) > 0;
    }
    
    public boolean deleteCitationFields(long citationId) {

        return mDb.delete(DATABASE_TABLE_FIELD, KEY_SAMPLE_ID + "=" + citationId, null) > 0;
    }



    /**
     * Return a Cursor positioned at the SampleAttribute that matches the given rowId
     * +
     * @param rowId id of Sample id to retrieve
     * @return Cursor positioned to matching note, if found
     * @throws SQLException if note could not be found/retrieved
     */
    public Cursor fetchSampleAttributesBySampleId(long sampleId) throws SQLException {

    	   return mDb.query(DATABASE_TABLE_FIELD, new String[] {KEY_ROWID,KEY_SAMPLE_ID, KEY_TIPUS_ATRIB,
                   VALUE,FIELD_NAME}, KEY_SAMPLE_ID + "=" + sampleId, null, null, null, null);
    
    }
    
    /// select * from CitationFieldTable,CitationTable where idRs=1 and CitationTable._id=idSample
    
    public Cursor fetchSamples(long projId) throws SQLException {

  
    	
    	Cursor c=mDb.rawQuery("SELECT CitationTable._id as _id,value,date,CitationFieldTable._id as idField FROM " + DATABASE_TABLE_FIELD+","+DATABASE_TABLE_CITATION
				+ " WHERE CitationTable._id="+CitacionDbAdapter.KEY_SAMPLE_ID+" and idRs="+projId+" GROUP BY CitationTable._id ",null);

    	c.moveToFirst();
    	
    	return c;
    	  
    	  
 
    }
    

    
    
   public Cursor fetchSampleByField(long projId, long citationId, String field) throws SQLException {

    	
    	Cursor c;

    	
    		c=mDb.rawQuery("SELECT CitationTable._id as _id,value,date,CitationFieldTable._id as idField,latitude,longitude FROM " + DATABASE_TABLE_FIELD+","+DATABASE_TABLE_CITATION
				+ " WHERE CitationTable._id="+citationId+" and idRs="+projId+" and CitationTable._id="+CitacionDbAdapter.KEY_SAMPLE_ID+ " and fieldName=\""+field+"\" GROUP BY CitationTable._id ORDER BY idField;",null);

  
    	
    	c.moveToFirst();
    	
    	return c;
    	  
    	  
 
    }
   
   public Cursor fetchSamplesByField(long projId, String field) throws SQLException {

   	
   	Cursor c;

   	
   		c=mDb.rawQuery("SELECT CitationTable._id as _id,value,date,CitationFieldTable._id as idField,latitude,longitude FROM " + DATABASE_TABLE_FIELD+","+DATABASE_TABLE_CITATION
				+ " WHERE idRs="+projId+" and CitationTable._id="+CitacionDbAdapter.KEY_SAMPLE_ID+ " and fieldName=\""+field+"\" GROUP BY CitationTable._id ORDER BY date DESC;",null);

 
   	
   	c.moveToFirst();
   	
   	return c;
   	  
   	  

   }
   
   
   public Cursor fetchCitationsByFieldValue(long projId, long fieldId,String value, boolean alphaOrder) throws SQLException {
	   	
	   	Cursor c;
	   	
	   	String orderBy="ORDER BY date DESC";
   		if(alphaOrder) orderBy="ORDER BY value";
   		
	   	
	   		c=mDb.rawQuery("SELECT CitationTable._id as _id,value,date,CitationFieldTable._id as idField,latitude,longitude FROM " + DATABASE_TABLE_FIELD+","+DATABASE_TABLE_CITATION
					+ " WHERE idRs="+projId+" and CitationTable._id="+CitacionDbAdapter.KEY_SAMPLE_ID+" and value=\""+value+"\" and idAttType="+fieldId+" GROUP BY CitationTable._id "+orderBy+";",null);

	   	
	   	c.moveToFirst();
	   	
	   	return c;
	   	  
	   	  

	   }
   
	public Cursor fetchCitationsByPhoto(long projId, long photoFieldId, boolean containsPhoto, boolean alphaOrder) {

		Cursor c;
	   	
	   	String orderBy="ORDER BY date DESC";
	   	String like="like '/mnt%'";
	   	
	   	if(!containsPhoto) like="not "+like;
   		if(alphaOrder) orderBy="ORDER BY value";
   				
	   	c=mDb.rawQuery("SELECT CitationTable._id as _id,value,date,CitationFieldTable._id as idField,latitude,longitude FROM " + DATABASE_TABLE_FIELD+","+DATABASE_TABLE_CITATION
			+ " WHERE idRs="+projId+" and CitationTable._id="+CitacionDbAdapter.KEY_SAMPLE_ID+" and value "+like+" and idAttType="+photoFieldId+" GROUP BY CitationTable._id "+orderBy+";",null);

	   	
	   	c.moveToFirst();
	   	
	   	return c;
		
	}
	
	 public Cursor fetchCitationsBySureness(long projId, long surenessFieldId) throws SQLException {

  
	   Cursor c=mDb.rawQuery("SELECT CitationTable._id as _id,value FROM " + DATABASE_TABLE_FIELD+","+DATABASE_TABLE_CITATION
				+ " WHERE idRs="+projId+" and CitationTable._id="+CitacionDbAdapter.KEY_SAMPLE_ID+" and value != \"OK\" and idAttType="+surenessFieldId+";",null);

		   	
	   c.moveToFirst();
  	   
	   return c;
  	   
  }
   
   
   public Cursor fetchCitationsByDate(long projId, String comparator,String value,boolean alphaOrder) throws SQLException {
	   	
	   	Cursor c;
	   	String orderBy="";
	   	
	   		if(alphaOrder) orderBy="ORDER BY value";
	   	
	   		c=mDb.rawQuery("SELECT CitationTable._id as _id,value,date,CitationFieldTable._id as idField,latitude,longitude FROM " + DATABASE_TABLE_FIELD+","+DATABASE_TABLE_CITATION
					+ " WHERE idRs="+projId+" and CitationTable._id="+CitacionDbAdapter.KEY_SAMPLE_ID+" and strftime('%Y-%m-%d',date)"+comparator+"\'"+value+"\' and fieldName=\"OriginalTaxonName\" GROUP BY CitationTable._id "+orderBy+";",null);

	   	
	   	c.moveToFirst();
	   	
	   	return c;
	   	  
	   	  

	   }
   
  
   
   public Cursor fetchSamplesByFieldOrdered(long projId, String field) throws SQLException {

	   	
	   	Cursor c=mDb.rawQuery("SELECT CitationTable._id as _id,value,date,CitationFieldTable._id as idField,latitude,longitude FROM " + DATABASE_TABLE_FIELD+","+DATABASE_TABLE_CITATION
					+ " WHERE idRs="+projId+" and CitationTable._id="+CitacionDbAdapter.KEY_SAMPLE_ID+ " and fieldName=\""+field+"\" GROUP BY CitationTable._id ORDER BY value;",null);

	   	
	   	c.moveToFirst();
	   	
	   	return c;
	   	  
	   	  

	   }

    
    
    
    public Cursor fetchSSamples(long projId) throws SQLException {

    	MatrixCursor cursor = null;
    	
        Cursor citationCursor = mDb.query(DATABASE_TABLE_CITATION,new String[] {CitacionDbAdapter.KEY_ROWID,
        		CitacionDbAdapter.KEY_RS,CitacionDbAdapter.DATE}, KEY_RS + "=" + projId, null,
                null, null, null, null);
        
        Cursor fieldCursor = mDb.query(DATABASE_TABLE_FIELD,new String[] {CitacionDbAdapter.KEY_ROWID,CitacionDbAdapter.KEY_SAMPLE_ID,
        		CitacionDbAdapter.VALUE,CitacionDbAdapter.FIELD_NAME}, null, null,
                null, null, null, null);
        
        try{
        
	        CursorJoiner joiner = new CursorJoiner(
	                fieldCursor,new String[] {CitacionDbAdapter.KEY_SAMPLE_ID},citationCursor,new String[] {CitacionDbAdapter.KEY_ROWID});
	
	       
		
		        cursor = new MatrixCursor( new String[]
		{"_id","value","date"},2);
		        
		        
		        for (CursorJoiner.Result joinerResult : joiner) {
		                switch (joinerResult) {
		                        case BOTH: // handle case where a row 
		                        	
		                        	
		                       		String _id=citationCursor.getString(0);
	                        		long idD=fieldCursor.getLong(0);
	                
	                                String date = citationCursor.getString(2);
	                                String value = fieldCursor.getString(2);
	                                String name= fieldCursor.getString(3);
	                                
	                        		System.out.println(_id+":"+idD+":"+date+":"+value+":"+name);

	                                
	                                cursor.addRow(new String[] {_id,value,date});
		                        	
		                     
		                             break;
		                             
		                        case RIGHT:
		                        	
		                        	long juju=citationCursor.getLong(0);
		                      
		                        	
		                        	if(juju==15){
		                        		
		                        		_id=citationCursor.getString(0);
		                        		idD=fieldCursor.getLong(0);
		                
		                                date = citationCursor.getString(2);
		                                value = fieldCursor.getString(2);
		                                name= fieldCursor.getString(3);
		                                
		                        		System.out.println(_id+":"+idD+":"+date+":"+value+":"+name);
		                        		
		                        		
		                        		
		                        	}
		                        	
		                        	
		                        	break;
		                
		                }
		        }

        
    	} catch (Exception e) {

    	}

    	return cursor;

  
  }
    
    public void startTransaction(){
    	
     	 mDb.beginTransaction();
     	
     	
     	
     }
     public void endTransaction(){
     	
     	mDb.setTransactionSuccessful();
     	mDb.endTransaction();
     	
     	
     }
     
     public Cursor fetchSamplesByCitationId(long citationId) throws SQLException {
       	
  	   return mDb.query(DATABASE_TABLE_FIELD, new String[] {KEY_ROWID,KEY_SAMPLE_ID, KEY_TIPUS_ATRIB,
                 VALUE}, KEY_SAMPLE_ID + "=" + citationId, null, null, null, null);
  
     }
    
      public Cursor fetchSamplesByFieldId(long sampleAttId) throws SQLException {

      	
    	   return mDb.query(DATABASE_TABLE_FIELD, new String[] {KEY_ROWID,KEY_SAMPLE_ID, KEY_TIPUS_ATRIB,
                   VALUE}, KEY_TIPUS_ATRIB + "=" + sampleAttId, null, null, null, null);
    
    }
      
      public Cursor fetchSamplesByFieldIdAndCitationId(long citationId,long sampleAttId) throws SQLException {

        	
   	   return mDb.query(DATABASE_TABLE_FIELD, new String[] {KEY_ROWID,KEY_SAMPLE_ID, KEY_TIPUS_ATRIB,
                  VALUE}, KEY_TIPUS_ATRIB + "=" + sampleAttId+" and "+ KEY_SAMPLE_ID + " = "+ citationId, null, null, null, null);
   
   }
       
    
    public Cursor fetchSampleAttributeBySampleAttId(long sampleId, long sampleAttId) throws SQLException {

    	
 	   return mDb.query(DATABASE_TABLE_FIELD, new String[] {KEY_ROWID,KEY_SAMPLE_ID, KEY_TIPUS_ATRIB,
                VALUE}, KEY_TIPUS_ATRIB + "=" + sampleAttId+" and "+ KEY_SAMPLE_ID + "="
                +sampleId , null, null, null, null);
 
 }
    
    
    public boolean updateSampleFieldValue(long sampleId, long sampleAttId,String newValue) {
        
    	ContentValues vals = new ContentValues();
    	vals.put(VALUE,newValue);
    		
    		
          return mDb.update(DATABASE_TABLE_FIELD, vals,  KEY_TIPUS_ATRIB + "=" + sampleAttId+" and "+ KEY_SAMPLE_ID + "="
                  +sampleId , null) > 0;
                  
    }
    
   public boolean updateCitationFieldValue(long citationId, long citationFieldId,String newValue) {
        
    	ContentValues vals = new ContentValues();
    	vals.put(VALUE,newValue);
    		
    		
          return mDb.update(DATABASE_TABLE_FIELD, vals,  KEY_SAMPLE_ID + "=" + citationId+" and "+ KEY_ROWID + "="
                  +citationFieldId , null) > 0;
                  
    }
    
    public Cursor fetchCitationIdByPhotoField(String photo) throws SQLException {
    	
    	   return mDb.query(DATABASE_TABLE_FIELD, new String[] {KEY_ROWID,KEY_SAMPLE_ID, KEY_TIPUS_ATRIB,
                   VALUE}, VALUE + "=\""+photo+"\"", null, null, null, null);
    
    }

	public boolean checkRepeated(long projId,long citationId, double latitude, double longitude, String date) {

		
        Cursor mCursor =

                mDb.query(true, DATABASE_TABLE_CITATION, new String[] {KEY_ROWID,KEY_RS,
                LATITUDE,LONGITUDE,DATE,SINCRONIZED}, KEY_RS + "=" + projId + " and " + LATITUDE + "=" + latitude + " and " + LONGITUDE + "=" + longitude + " and " + DATE + " = \"" + date + "\"" , null,
                        null, null, null, null);
            mCursor.moveToFirst();
        
         if(mCursor.getCount()>0){
        	 
        	 mDb.delete(DATABASE_TABLE_CITATION, KEY_ROWID + "=" + citationId, null);
        	 
        	 mCursor.close();
        	 return true;
        	 
         }   
         else{
        	 
        	 mCursor.close();
        	 
        	 return false;
        	 
         }
        

       

	}

	public Cursor fetchSheetField(long sampleId) {
     	
	   	   return mDb.query(DATABASE_TABLE_FIELD, new String[] {KEY_ROWID,KEY_SAMPLE_ID, KEY_TIPUS_ATRIB,
	                  VALUE},"(" + FIELD_NAME + "=\"Sheet\" or "+ FIELD_NAME + "=\"Plec\" ) and " + KEY_SAMPLE_ID + " = "+ sampleId, null, null, null, null);
	   
	   }

	public Cursor fetchTaxonsByProjId(long projId) {


		
		return null;
	}


	public Cursor fetchCitationWithPhoto(long fieldId) {

		   return mDb.query(DATABASE_TABLE_FIELD, new String[] {KEY_ROWID,KEY_SAMPLE_ID, KEY_TIPUS_ATRIB,
                   VALUE}, KEY_TIPUS_ATRIB + " = " +fieldId, null, null, null, null);
		
	}
	
	public Cursor fetchCitationPhotoValue(long citationId,long fieldId) {

		   return mDb.query(DATABASE_TABLE_FIELD, new String[] {KEY_ROWID,KEY_SAMPLE_ID, KEY_TIPUS_ATRIB,
                VALUE}, KEY_TIPUS_ATRIB + " = " +fieldId+" and " + KEY_SAMPLE_ID + " = " +citationId, null, null, null, null);
		
	}






}
