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
public class SampleDbAdapter {

	public static final String KEY_ROWID = "_id";
    public static final String KEY_RS = "idRs";
    public static final String DATE = "date";
    public static final String LATITUDE = "latitude";
    public static final String LONGITUDE = "longitude";
    public static final String COMMENT = "comment";
    public static final String SINCRONIZED="sincronized";


    

    private static final String TAG = "SampleDbAdapter";
    private DatabaseHelper mDbHelper;
    private SQLiteDatabase mDb;
    
    /**
     * Database creation sql statement
     */
    private static final String DATABASE_CREATE =
            "create table Sample ("
            + KEY_ROWID + " INTEGER PRIMARY KEY,"
            + KEY_RS + " DOUBLE,"
            + LATITUDE + " DOUBLE,"
            + LONGITUDE + " DOUBLE,"
            + COMMENT + " TEXT,"
            + DATE + " TEXT,"
            + SINCRONIZED + " BOOLEAN"
            + ");";

    private static final String DATABASE_NAME = "mobileSamplingS";
    private static final String DATABASE_TABLE = "Sample";
    private static final int DATABASE_VERSION = 2;

    private final Context mCtx;

    private static class DatabaseHelper extends SQLiteOpenHelper {

        DatabaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
		public void onCreate(SQLiteDatabase db) {

            db.execSQL(DATABASE_CREATE);
            

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
    public SampleDbAdapter(Context ctx) {
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
    public SampleDbAdapter open() throws SQLException {
        mDbHelper = new DatabaseHelper(mCtx);
        mDb = mDbHelper.getWritableDatabase();
        return this;
    }
    
    public void close() {
   
        mDbHelper.close();
    }
    
    
    public long createEmptySample(long rsId) {

    	ContentValues initialValues = new ContentValues();
        initialValues.put(KEY_RS, rsId);        
        initialValues.put(SINCRONIZED , 0);
        

        return mDb.insert(DATABASE_TABLE, null, initialValues);
    }
    
    public boolean updateLocation(long sampleId,double lat, double longitude){
    	
       	ContentValues vals = new ContentValues();
		vals.put(LATITUDE,lat);
		vals.put(LONGITUDE,longitude);
		
      return mDb.update(DATABASE_TABLE, vals, KEY_ROWID + "=" + sampleId, null) > 0;
    	
    	
    }
    
    public boolean updateDate(long sampleId,String date){
    	
       	ContentValues vals = new ContentValues();
		vals.put(DATE,date);
		
      return mDb.update(DATABASE_TABLE, vals, KEY_ROWID + "=" + sampleId, null) > 0;
    	
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
    public long createSample(long rsId, double latitude, double longitude, String comment) {
        

    	ContentValues initialValues = new ContentValues();
        initialValues.put(KEY_RS, rsId);
        initialValues.put(LATITUDE , latitude);
        initialValues.put(LONGITUDE , longitude);
        initialValues.put(COMMENT , comment);
        

        Date date = new Date();
        date.getDate();
 
        initialValues.put(DATE , (String) DateFormat.format("yyyy-MM-dd hh:mm:ss", date));
        
        initialValues.put(SINCRONIZED , 0);
        

        return mDb.insert(DATABASE_TABLE, null, initialValues);
    }

    /**
     * Delete the Sample with the given rowId
     * 
     * @param rowId id of note to delete
     * @return true if deleted, false otherwise
     */
    public boolean deleteSample(long rowId) {

        return mDb.delete(DATABASE_TABLE, KEY_ROWID + "=" + rowId, null) > 0;
    }

    /**
     * Return a Cursor over the list of all Samples in the database
     * 
     * @return Cursor over all notes
     */
    public Cursor fetchAllSamples() {

        return mDb.query(DATABASE_TABLE, new String[] {KEY_ROWID, KEY_RS,
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

        Cursor mCursor =

                mDb.query(true, DATABASE_TABLE, new String[] {KEY_ROWID,
                		KEY_RS,
                        LATITUDE,LONGITUDE,DATE,SINCRONIZED}, KEY_RS + "=" + rowId, null,
                        null, null, null, null);
        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;

    }
    
    
    
    public Cursor fetchSampleBySampleId(long rowId) throws SQLException {

        Cursor mCursor =

                mDb.query(true, DATABASE_TABLE, new String[] {KEY_ROWID,
                		KEY_RS,
                        LATITUDE,LONGITUDE,DATE,SINCRONIZED}, KEY_ROWID + "=" + rowId, null,
                        null, null, null, null);
        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;

    }
    
    public Cursor fetchUnsyncronisedSamples(long rowId) throws SQLException {

         Cursor mCursor =

                mDb.query(true, DATABASE_TABLE, new String[] {KEY_ROWID,
                		KEY_RS,
                        LATITUDE,LONGITUDE,DATE,SINCRONIZED}, KEY_RS + "=" + rowId +" and "+SINCRONIZED+ "= '0'", null,
                        null, null, null, null);
        
        
        if (mCursor != null) {
        	
        	Log.d(TAG,"Cursor diferent de NUll");
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
    		
    		
          return mDb.update(DATABASE_TABLE, vals, KEY_ROWID + "=" + rowId, null) > 0;
    }
}
