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
 * 
 * 
 * 
 */
public class FieldItemAdapter {

	public static final String KEY_ROWID = "_id";
    public static final String FIELD_ID ="fieldID";
    public static final String ITEM_VALUE="itemValue";

   
    private static final String TAG = "FieldItemAdapter";
    private DatabaseHelper mDbHelper;
    private SQLiteDatabase mDb;

    
    /**
     * Database creation sql statement
     */
    private static final String DATABASE_CREATE =
            "create table ItemField ("
            + KEY_ROWID + " INTEGER PRIMARY KEY,"
            + FIELD_ID+ " INTEGER,"
            + ITEM_VALUE + " TEXT"
            + ");";
    
    
    /**
     * 
     */
    
    private static final String DATABASE_SECOND_CREATE =
        "create table SecondLevelItemField ("
        + KEY_ROWID + " INTEGER PRIMARY KEY,"
        + FIELD_ID+ " INTEGER,"
        + ITEM_VALUE + " TEXT"
        + ");";
    

    private static final String DATABASE_NAME = "FieldItem";
    private static final String DATABASE_TABLE = "ItemField";
    private static final String DATABASE_TABLE_SECOND = "SecondLevelItemField";

    private static final int DATABASE_VERSION = 2;

    private final Context mCtx;

    private static class DatabaseHelper extends SQLiteOpenHelper {

        DatabaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
		public void onCreate(SQLiteDatabase db) {

            db.execSQL(DATABASE_CREATE);
            db.execSQL(DATABASE_SECOND_CREATE);


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
    public FieldItemAdapter(Context ctx) {
        this.mCtx = ctx;
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
    public FieldItemAdapter open() throws SQLException {
        mDbHelper = new DatabaseHelper(mCtx);
        mDb = mDbHelper.getWritableDatabase();

        return this;
    }

    public void close() {
        mDbHelper.close();
    }


    /**
     * Create a new ItemDT using the name, idDT and the info. If the ItemDT is
     * successfully created return the new rowId for that ItemDT, otherwise return
     * a -1 to indicate failure.
     * 
     * @param itemName the name of the ItemDT
     * @param idDT the DT identificator of the ItemDT
     * @param itemInfo the info of the ItemDT
     * @return rowId or -1 if failed
     */
    public long addFieldItem(long fieldId, String itemValue) {
        
    	ContentValues initialValues = new ContentValues();
        
        initialValues.put(FIELD_ID, fieldId);
        initialValues.put(ITEM_VALUE , itemValue); 

        return mDb.insert(DATABASE_TABLE, null, initialValues);
    }
    
    /**
     * Create a new ItemDT using the name, idDT and the info. If the ItemDT is
     * successfully created return the new rowId for that ItemDT, otherwise return
     * a -1 to indicate failure.
     * 
     * @param itemName the name of the ItemDT
     * @param idDT the DT identificator of the ItemDT
     * @param itemInfo the info of the ItemDT
     * @return rowId or -1 if failed
     */
    public long addSecondLevelFieldItem(long fieldId, String itemValue) {
        
    	ContentValues initialValues = new ContentValues();
        
        initialValues.put(FIELD_ID, fieldId);
        initialValues.put(ITEM_VALUE , itemValue); 

        return mDb.insert(DATABASE_TABLE_SECOND, null, initialValues);
    }

    /**
     * Delete the ItemDT with the given rowId
     * 
     * @param rowId id of ItemDT to delete
     * @return true if deleted, false otherwise
     */
    public boolean deleteItemDT(long rowId) {

        return mDb.delete(DATABASE_TABLE, KEY_ROWID + "=" + rowId, null) > 0;
    }
    
    public boolean deleteItemsFromField(long fieldId) {

        return mDb.delete(DATABASE_TABLE, FIELD_ID + "=" + fieldId, null) > 0;
    }
    
    public boolean deleteItemsFromSecondLevelField(long fieldId) {

        return mDb.delete(DATABASE_TABLE_SECOND, FIELD_ID + "=" + fieldId, null) > 0;
    }

    /**
     * Return a Cursor positioned at the itemDT that matches the given DT id
     * 
     * @param type of note to retrieve
     * @return Cursor positioned to matching note, if found
     * @throws SQLException if note could not be found/retrieved
     */
    
    public Cursor fetchItemsbyFieldId(long fieldId) throws SQLException {
    	
 	   return mDb.query(DATABASE_TABLE, new String[] {KEY_ROWID, FIELD_ID,
                ITEM_VALUE}, FIELD_ID + "=" + fieldId, null, null, null, null);
 
 }
    
    /**
     * Return a Cursor positioned at the itemDT that matches the given DT id
     * 
     * @param type of note to retrieve
     * @return Cursor positioned to matching note, if found
     * @throws SQLException if note could not be found/retrieved
     */
    
    public Cursor fetchItemsbySecondLevelFieldId(long fieldId) throws SQLException {
    	
 	   return mDb.query(DATABASE_TABLE_SECOND, new String[] {KEY_ROWID, FIELD_ID,
                ITEM_VALUE}, FIELD_ID + "=" + fieldId, null, null, null, null);
 
 }
    

    
    
}
