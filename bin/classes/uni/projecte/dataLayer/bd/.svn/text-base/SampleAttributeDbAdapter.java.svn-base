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


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Simple notes database access helper class. Defines the basic CRUD operations
 * for the notepad example, and gives the ability to list all notes as well as
 * retrieve or modify a specific note.
 * 
 * This has been improved from the first version of this tutorial through the
 * addition of better error handling and also using returning a Cursor instead
 * of using a collection of inner classes (which is less scalable and not
 * recommended).
 */
public class SampleAttributeDbAdapter {

	public static final String KEY_ROWID = "_id";
	public static final String KEY_SAMPLE_ID = "idSample";
    public static final String KEY_TIPUS_ATRIB = "idAttType";
    public static final String VALUE = "value";
   

    private static final String TAG = "SampleAttributeDbAdapter";
    private DatabaseHelper mDbHelper;
    private SQLiteDatabase mDb;
    
    /**
     * Database creation sql statement
     */
    private static final String DATABASE_CREATE =
            "create table SampleAttribute ("
            + KEY_ROWID + " INTEGER PRIMARY KEY,"
            + KEY_SAMPLE_ID + " INTEGER,"
            + KEY_TIPUS_ATRIB + " INTEGER,"
            + VALUE + " TEXT"
            + ");";

    private static final String DATABASE_NAME = "mobileSamplingSA";
    private static final String DATABASE_TABLE = "SampleAttribute";
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
    public SampleAttributeDbAdapter(Context ctx) {
        this.mCtx = ctx;
    }

    /**
     * Open the SampleAttribute database. If it cannot be opened, try to create a new
     * instance of the database. If it cannot be created, throw an exception to
     * signal the failure
     * 
     * @return this (self reference, allowing this to be chained in an
     *         initialization call)
     * @throws SQLException if the database could be neither opened or created
     */
    public SampleAttributeDbAdapter open() throws SQLException {
        mDbHelper = new DatabaseHelper(mCtx);
        mDb = mDbHelper.getWritableDatabase();
        return this;
    }
    
    public void close() {
        mDbHelper.close();
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
    public long createSampleAttribute(long idSample, long idAttribType, String value) {
        ContentValues initialValues = new ContentValues();
        
        initialValues.put(KEY_SAMPLE_ID, idSample);
        initialValues.put(KEY_TIPUS_ATRIB, idAttribType);
        initialValues.put(VALUE , value);

        return mDb.insert(DATABASE_TABLE, null, initialValues);
    }
    

    /**
     * Delete the Sample Attribute with the given rowId
     * 
     * @param rowId id of note to delete
     * @return true if deleted, false otherwise
     */
    public boolean deleteSampleAttribute(long rowId) {

        return mDb.delete(DATABASE_TABLE, KEY_ROWID + "=" + rowId, null) > 0;
    }


    /**
     * Return a Cursor positioned at the SampleAttribute that matches the given rowId
     * 
     * @param rowId id of Sample id to retrieve
     * @return Cursor positioned to matching note, if found
     * @throws SQLException if note could not be found/retrieved
     */
    public Cursor fetchSampleAttributesBySampleId(long sampleId) throws SQLException {

    	
    	   return mDb.query(DATABASE_TABLE, new String[] {KEY_ROWID,KEY_SAMPLE_ID, KEY_TIPUS_ATRIB,
                   VALUE}, KEY_SAMPLE_ID + "=" + sampleId, null, null, null, null);
    
    }
    
    public Cursor fetchSampleAttributeBySampleAttId(long sampleId, long sampleAttId) throws SQLException {

    	
 	   return mDb.query(DATABASE_TABLE, new String[] {KEY_ROWID,KEY_SAMPLE_ID, KEY_TIPUS_ATRIB,
                VALUE}, KEY_TIPUS_ATRIB + "=" + sampleAttId+" and "+ KEY_SAMPLE_ID + "="
                +sampleId , null, null, null, null);
 
 }
    
    
    public boolean updateSampleFieldValue(long sampleId, long sampleAttId,String newValue) {
        
    	ContentValues vals = new ContentValues();
    		vals.put(VALUE,newValue);
    		
    		
          return mDb.update(DATABASE_TABLE, vals,  KEY_TIPUS_ATRIB + "=" + sampleAttId+" and "+ KEY_SAMPLE_ID + "="
                  +sampleId , null) > 0;
    }
    
}
