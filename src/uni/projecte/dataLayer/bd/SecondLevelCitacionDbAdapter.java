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
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.text.format.DateFormat;
import android.util.Log;

/**
 * Simple Sample database access helper class
 */
public class SecondLevelCitacionDbAdapter extends CitacionDbAdapter {

	
	/* citacion main fields */
	public static final String KEY_ROWID = "_id";
    public static final String FIELD_ID = "fieldId";


	/* filled fields */
    
	public static final String KEY_SAMPLE_ID = "idSample";
    public static final String KEY_TIPUS_ATRIB = "idAttType";



    private static final String TAG = "SecondLevelDbAdapter";
    
    protected DatabaseHelperH mDbHelper;
    
   
    /**
     * Database creation sql statement
     */
    
    
    private static final String CITATION_DATABASE_CREATE =
            "create table CitationTable ("
            + KEY_ROWID + " INTEGER PRIMARY KEY,"
            + FIELD_ID + " TEXT,"
            + LATITUDE + " DOUBLE,"
            + LONGITUDE + " DOUBLE,"
            + DATE + " TEXT"
            + ");";
    
    
    private static final String FIELD_DATABASE_CREATE =
        "create table CitationFieldTable ("
        + KEY_ROWID + " INTEGER PRIMARY KEY,"
        + KEY_SAMPLE_ID + " INTEGER,"
        + KEY_TIPUS_ATRIB + " INTEGER,"
        + VALUE + " TEXT,"
        + FIELD_NAME + " TEXT"
        + ");";

    
    private static final String DATABASE_NAME= "SecondLevelCitation";
    private static final int DATABASE_VERSION = 2;

    private final Context mCtx;
    

    private static class DatabaseHelperH extends SQLiteOpenHelper {

        DatabaseHelperH(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
		public void onCreate(SQLiteDatabase db) {

            db.execSQL(CITATION_DATABASE_CREATE);
            db.execSQL(FIELD_DATABASE_CREATE);

        }

        @Override
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
    public SecondLevelCitacionDbAdapter(Context ctx) {
    	super(ctx);
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
    @Override
	public SecondLevelCitacionDbAdapter open() throws SQLException {
        
    	mDbHelper = new DatabaseHelperH(mCtx);
        mDb = mDbHelper.getWritableDatabase();
        return this;
    }
    
    @Override
	public void close() {
   
        mDbHelper.close();
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
    public long createCitation(String secondLevelFieldId, double latitude, double longitude) {
        
    	ContentValues initialValues = new ContentValues();
        
    	initialValues.put(FIELD_ID, secondLevelFieldId);
        initialValues.put(LATITUDE , latitude);
        initialValues.put(LONGITUDE , longitude);

        Date date = new Date();
        date.getDate();
 
        initialValues.put(DATE , (String) DateFormat.format("yyyy-MM-dd kk:mm:ss", date));
        

        return mDb.insert(DATABASE_TABLE_CITATION, null, initialValues);
    }
    
    
    
    
    public int removeCitationsBySLId(String citationId) {

        return mDb.delete(DATABASE_TABLE_CITATION, FIELD_ID + "=\"" + citationId+"\"", null);
    }
    
    
    @Override
	public void startTransaction(){
    	
    	 mDb.beginTransaction();
    	
    	
    	
    }
    @Override
	public void endTransaction(){
    	
    	mDb.setTransactionSuccessful();
    	mDb.endTransaction();
    	
    	
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
    @Override
	public long createCitationField(long idSample, long idAttribType, String value,String fieldName) {
   
    	return super.createCitationField(idSample, idAttribType, value, fieldName);
    	
    }
    
    
    /**
     * Return a Cursor positioned at the Sample that matches the given rowId
     * 
     * @param rowId id of Sample to retrieve
     * @return Cursor positioned to matching note, if found
     * @throws SQLException if note could not be found/retrieved
     */
    @Override
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
    
    public Cursor fetchSecondLevelValuesGrid(String secondLevelId) throws SQLException{
    	
    	
       	Cursor mCursor=mDb.rawQuery("SELECT idSample,fieldId,latitude,longitude,date,group_concat(value,\":\") FROM " + DATABASE_TABLE_CITATION +","+DATABASE_TABLE_FIELD
				+ " WHERE fieldId=\""+secondLevelId+"\" and CitationTable._id="+CitacionDbAdapter.KEY_SAMPLE_ID+" GROUP BY idSample",null);
    	
    	
        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;
    	
    }

    public Cursor fetchSecondLevelFieldValues(String secondLevelId) throws SQLException{
    	
    	
       	Cursor mCursor=mDb.rawQuery("SELECT idSample,fieldId,latitude,longitude,date,group_concat(value,\":\") FROM " + DATABASE_TABLE_CITATION +","+DATABASE_TABLE_FIELD
				+ " WHERE fieldId=\""+secondLevelId+"\" and CitationTable._id="+CitacionDbAdapter.KEY_SAMPLE_ID,null);
    	
    	
        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;
    	
    }

	public Cursor fetchCitationsFromSecondLevel(String projName) {

       	Cursor mCursor=mDb.rawQuery("SELECT * FROM " + DATABASE_TABLE_CITATION
				+ " WHERE fieldId like \""+projName+"%\"",null);
    	
    	
        if (mCursor != null) {
            mCursor.moveToFirst();
        }
		
		return null;
	}
	
	
    public Cursor fetchCitationBySubProjIdValue(String value) throws SQLException {

    	
 	   return mDb.query(DATABASE_TABLE_CITATION, new String[] {KEY_ROWID,FIELD_ID}, FIELD_ID + "=   \"" + value +"\"", null, null, null, null);
 
 }

	public long createEmptyCitation(String secondFieldId) {

	    ContentValues initialValues = new ContentValues();
	    initialValues.put(FIELD_ID, secondFieldId);        
	        

	  return mDb.insert(DATABASE_TABLE_CITATION, null, initialValues);
	   
	}
 

    
    
}