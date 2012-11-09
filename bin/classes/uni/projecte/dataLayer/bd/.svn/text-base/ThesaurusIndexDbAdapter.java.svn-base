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
 * Simple DataType database access helper class. 
 * recommended).
 */
public class ThesaurusIndexDbAdapter {

	public static final String KEY_ROWID = "_id";
    public static final String THNAME = "thName";
    public static final String THREMOTEID = "thRemoteId";
    public static final String DBTABLE = "DBtable";
    public static final String THTYPE = "thType";
    public static final String ITEMS = "items";
    public static final String SOURCE_ID = "sourceId";
    public static final String SOURCE_TYPE = "sourceType";
    public static final String UPDATE_TSP = "updateTimeStamp";
    
  
    private static final String TAG = "DataTypeDbAdapter";
    private DatabaseHelper mDbHelper;
    private SQLiteDatabase mDb;

    
    /**
     * Database creation sql statement
     */
    private static final String DATABASE_CREATE =
            "create table thesaurusIndex ("
            + KEY_ROWID + " INTEGER PRIMARY KEY,"
            + THNAME + " TEXT UNIQUE,"
            + DBTABLE + " TEXT,"
            + ITEMS + " INTEGER,"
            + THTYPE + " TEXT,"
            + SOURCE_ID + " TEXT,"
            + SOURCE_TYPE + " TEXT,"
            + UPDATE_TSP + " TEXT,"
            + THREMOTEID + " TEXT"
            + ");";
    

    private static final String DATABASE_NAME = "thesaurusIndexes";
    private static final String DATABASE_TABLE = "thesaurusIndex";
    private static final int DATABASE_VERSION = 5;
    
    /*
     * 
     * Version 2: main version
     * Version 3: thesaurusType added
     * Version 4: sourceId, sourceType, updateTimestamp
     * Version 5: thId
     * 
     */

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

            	/* Added new Field */
            	
                if (oldVersion < 3) {

                    final String ALTER_TBL = 
                        "ALTER TABLE " + DATABASE_TABLE + 
                        " ADD COLUMN " + THTYPE + " text "
                        + ";";
                    
                    db.execSQL(ALTER_TBL);
                	
                }
                if (oldVersion < 4) {

                   final String ADD_SOURCE_ID = 
                        "ALTER TABLE " + DATABASE_TABLE + 
                        " ADD COLUMN " + SOURCE_ID + " text NOT NULL DEFAULT 'bdbc' "
                        + ";";
                    
                    final String ADD_SOURCE_TYPE = 
                        "ALTER TABLE " + DATABASE_TABLE +
                    " ADD COLUMN " + SOURCE_TYPE + " text NOT NULL DEFAULT 'remote' "
                    + ";";
                    
                    final String ADD_UPDATE_TSP = 
                        "ALTER TABLE " + DATABASE_TABLE +
                        " ADD COLUMN " + UPDATE_TSP + " text "
                        + ";";
                  
                    
                    db.execSQL(ADD_SOURCE_ID);
                    db.execSQL(ADD_SOURCE_TYPE);
                    db.execSQL(ADD_UPDATE_TSP);
                	
                }
                if (oldVersion < 5) {

                    final String ALTER_TBL = 
                        "ALTER TABLE " + DATABASE_TABLE + 
                        " ADD COLUMN " + THREMOTEID + " text "
                        + ";";
                    
                    db.execSQL(ALTER_TBL);
                	
                }
            
        }
    }

    /**
     * Constructor - takes the context to allow the database to be
     * opened/created
     * 
     * @param ctx the Context within which to work
     */
    public ThesaurusIndexDbAdapter(Context ctx) {
        this.mCtx = ctx;
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
     * Open the notes database. If it cannot be opened, try to create a new
     * instance of the database. If it cannot be created, throw an exception to
     * signal the failure
     * 
     * @return this (self reference, allowing this to be chained in an
     *         initialization call)
     * @throws SQLException if the database could be neither opened or created
     */
    public ThesaurusIndexDbAdapter open() throws SQLException {
        mDbHelper = new DatabaseHelper(mCtx);
        mDb = mDbHelper.getWritableDatabase();

        return this;
    }

    public void close() {
        mDbHelper.close();
    }


    /**
     * Create a new DataType using the name, description and type provided. If the DataType is
     * successfully created return the new rowId for that note, otherwise return
     * a -1 to indicate failure.
     * 
     * @param type the type of the DataType
     * @param dtName the name of the DataType
     * @param dtDesc the description of the DataType
     * @return rowId or -1 if failed
     */
    public long createTh (String thName, String dbTable, int numItems) {
        ContentValues initialValues = new ContentValues();
        initialValues.put(THNAME, thName);
        initialValues.put(DBTABLE , dbTable);
        initialValues.put(ITEMS , numItems);  

        return mDb.insert(DATABASE_TABLE, null, initialValues);
    }
    
 
    public long createThWithType (String thName, String dbTable, String thRemoteId,int numItems, String thType, String sourceId,String sourceType) {
        
    	ContentValues initialValues = new ContentValues();
        initialValues.put(THNAME, thName);
        initialValues.put(DBTABLE , dbTable);
        initialValues.put(ITEMS , numItems);  
        initialValues.put(THTYPE , thType);  
        initialValues.put(SOURCE_ID,sourceId);
        initialValues.put(SOURCE_TYPE,sourceType);
        initialValues.put(THREMOTEID,thRemoteId);

        
        Date date = new Date();
        date.getDate();

        initialValues.put(UPDATE_TSP , (String) DateFormat.format("yyyy-MM-dd kk:mm:ss", date));

        return mDb.insert(DATABASE_TABLE, null, initialValues);
    }
 

    /**
     * Delete the DataType with the given dtId
     * 
     * @param dtId id of note to delete
     * @return true if deleted, false otherwise
     */
    public boolean deleteDT(long dtId) {

        return mDb.delete(DATABASE_TABLE, KEY_ROWID + "=" + dtId, null) > 0;
    }
    
    /**
     * Return a Cursor over the list of all DT's in the database
     * 
     * @return Cursor over all DT's
     */
    public Cursor fetchAllTH() {

        return mDb.query(DATABASE_TABLE, new String[] {KEY_ROWID, THNAME,
                DBTABLE,ITEMS,THTYPE,SOURCE_ID,SOURCE_TYPE}, null, null, null, null, null);
    }

    /**
     * Return a Cursor positioned at the DT that matches the given name
     * 
     * @param name of note to retrieve
     * @return Cursor positioned to matching note, if found
     * @throws SQLException if note could not be found/retrieved
     */
    
  public Cursor fetchThbyName(String name) throws SQLException {
    	
 	   return mDb.query(DATABASE_TABLE, new String[] {KEY_ROWID, THNAME,
                DBTABLE,ITEMS,THTYPE,SOURCE_ID,SOURCE_TYPE,UPDATE_TSP,THREMOTEID}, THNAME + "= \"" + name +"\"", null, null, null, null);
 
 }
  
  public Cursor fetchRemoteTh(String thRemoteId) throws SQLException {
  	
	   return mDb.query(DATABASE_TABLE, new String[] {KEY_ROWID, THNAME,
               DBTABLE,ITEMS,THTYPE,SOURCE_ID,SOURCE_TYPE,UPDATE_TSP,THREMOTEID}, THREMOTEID + "= \"" + thRemoteId +"\"", null, null, null, null);

}
  
  	public Cursor fetchRemoteTh() throws SQLException{
  		
  		
  		return mDb.query(DATABASE_TABLE, new String[] {KEY_ROWID, THNAME
               ,UPDATE_TSP,THREMOTEID}, SOURCE_TYPE + "= \"remote\" and "+THREMOTEID + "!=\"\"", null, null, null, null);
  		
  	}
  

	public boolean removeThbyName(String thName) {


        return mDb.delete(DATABASE_TABLE, THNAME + "=\"" + thName+"\"", null) > 0;

		
		
	}

	public boolean updateThCount(long thId, int numElem) {

		ContentValues vals = new ContentValues();
		vals.put(ITEMS,numElem);
		
		return mDb.update(DATABASE_TABLE,vals,KEY_ROWID+" = "+thId,null) >0;
		
		
	}
	
	public boolean unlinkTh(long thId) {

		ContentValues vals = new ContentValues();
		vals.put(THREMOTEID,"");
		
		return mDb.update(DATABASE_TABLE,vals,KEY_ROWID+" = "+thId,null) >0;
		
		
	}
	
	public boolean updateThType(long thId, String thType) {

		ContentValues vals = new ContentValues();
		vals.put(THTYPE,thType);
		
		return mDb.update(DATABASE_TABLE,vals,KEY_ROWID+" = "+thId,null) >0;
		
		
	}
	
    
    /**
     * Return a Cursor positioned at first DT that matches the given Type
     * 
     * @param type of DT's to retrieve
     * @return Cursor positioned to matching note, if found
     * @throws SQLException if note could not be found/retrieved
     */
    
  /*  public Cursor fetchDTbyType(int type) throws SQLException {
    	
    	   return mDb.query(DATABASE_TABLE, new String[] {KEY_ROWID, TYPE,
                   NAME,DESC}, TYPE + "=" + type, null, null, null, null);
    
    } */
    
}
