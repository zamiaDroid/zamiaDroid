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
import android.database.sqlite.SQLiteConstraintException;
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
public class ResearchDbAdapter {

	public static final String KEY_ROWID = "_id";
    public static final String NAME = "name";
    public static final String DESCRIPTION = "description";
    public static final String TYPE = "type";


 
    private static final String TAG = "ResearchDbAdapter";
    private DatabaseHelper mDbHelper;
    private SQLiteDatabase mDb;
    
    /**
     * Database creation sql statement
     */
    
    private static final String DATABASE_NAME = "mobileSamplingRs";
    
    private static final String DATABASE_TABLE = "Research";
    private static final int DATABASE_VERSION = 2;

    private static final String DATABASE_CREATE =
            "create table "+DATABASE_TABLE+" ("
            + KEY_ROWID + " INTEGER PRIMARY KEY,"
            + NAME + " TEXT UNIQUE,"
            + DESCRIPTION + " TEXT,"
            + TYPE+ " TEXT"
            + ");";


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
    public ResearchDbAdapter(Context ctx) {
        this.mCtx = ctx;
    }

    /**
     * Open the Research database. If it cannot be opened, try to create a new
     * instance of the database. If it cannot be created, throw an exception to
     * signal the failure
     * 
     * @return this (self reference, allowing this to be chained in an
     *         initialization call)
     * @throws SQLException if the database could be neither opened or created
     */
    public ResearchDbAdapter open() throws SQLException {
        mDbHelper = new DatabaseHelper(mCtx);
        mDb = mDbHelper.getWritableDatabase();
        return this;
    }
 
    
    
    public void close() {
        mDbHelper.close();
    }
    
    
    public void deleteDatabase() {
        mDbHelper.close();
        mDb.close();
        if (mCtx.deleteDatabase(DATABASE_NAME)) {
          Log.d(TAG, "deleteDatabase(): database deleted.");
        } else {
          Log.d(TAG, "deleteDatabase(): database NOT deleted.");
        }
      } 


    /**
     * Create a new Research using the name and description provided. If the Research is
     * successfully created return the new rowId for that Research, otherwise return
     * a -1 to indicate failure.
     * 
     * @param name the name of the Research
     * @param body the description of the Research
     * @return rowId or -1 if failed
     */
    public long createResearch(String name, String description) {
        
    	ContentValues initialValues = new ContentValues();
        initialValues.put(NAME , name);
        initialValues.put(DESCRIPTION , description);

        long result=0;
        
        try{
        	
        	result=mDb.insert(DATABASE_TABLE, null, initialValues);
        }
        catch (SQLiteConstraintException sqle){
        	        	
        	result=-1;
        	
        }
        catch(Exception exception) {

	        int i=0;
	        i++;
  
        }

        
        
        return result;
    }

    /**
     * Delete the Research with the given rowId
     * 
     * @param rowId id of Research to delete
     * @return true if deleted, false otherwise
     */
    public boolean deleteResearch(long rowId) {

        return mDb.delete(DATABASE_TABLE, KEY_ROWID + "=" + rowId, null) > 0;
    }

    /**
     * Return a Cursor over the list of all Researches in the database
     * 
     * @return Cursor over all notes
     */
    public Cursor fetchAllResearches() {

        return mDb.query(DATABASE_TABLE, new String[] {KEY_ROWID, NAME,DESCRIPTION}, null, null, null, null, NAME + " ASC");
    }

    
    
    /**
     * Return a Cursor positioned at the Research that matches the given rowId
     * 
     * @param rowId id of Research to retrieve
     * @return Cursor positioned to matching Research, if found
     * @throws SQLException if Research could not be found/retrieved
     */
    public Cursor fetchResearch(long rowId) throws SQLException {

        Cursor mCursor =

                mDb.query(true, DATABASE_TABLE, new String[] {KEY_ROWID,
                        NAME,DESCRIPTION}, KEY_ROWID + "=" + rowId, null,
                        null, null, null, null);
        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;

    }
    
    /**
     * Return a Cursor positioned at the Research that matches the given rsName
     * 
     * @param rowId id of Research to retrieve
     * @return Cursor positioned to matching Research, if found
     * @throws SQLException if Research could not be found/retrieved
     */
    
    public Cursor fetchResearchByName(String name) throws SQLException {

        Cursor mCursor =

                mDb.query(true, DATABASE_TABLE, new String[] {KEY_ROWID,
                        NAME,DESCRIPTION}, NAME + "='" + name+"'", null,
                        null, null, null, null);
        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;

    }
    
    public Cursor fetchResearchByTh(String thName) throws SQLException {

        Cursor mCursor =

                mDb.query(true, DATABASE_TABLE, new String[] {KEY_ROWID,
                        NAME,DESCRIPTION}, DESCRIPTION + "='" + thName+"'", null,
                        null, null, null, null);
        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;

    }

	public boolean updateRsTh(long rsId, String thName) {
		
		ContentValues vals = new ContentValues();
		vals.put(DESCRIPTION,thName);
		
		
      return mDb.update(DATABASE_TABLE, vals, KEY_ROWID + "=" + rsId, null) > 0;
		
	}
    
   
}
