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
 * Simple Attribute database access helper class. 
 */
public class AttributeDbAdapter {

	public static final String KEY_ROWID = "_id";
    public static final String KEY_RS = "idRs";
    public static final String NAME = "name";
    public static final String TYPE = "idType";
    public static final String LABEL = "label";
    public static final String DESC = "desc";
    public static final String CAT = "category";
    public static final String DEF = "def";
    public static final String VISIBLE = "visible";

    public static final String PREVALUE = "preValue";

    private static final String TAG = "AttributeDbAdapter";
    private DatabaseHelper mDbHelper;
    private SQLiteDatabase mDb;
    
    private static final String DATABASE_NAME = "mobileSamplingAtt";
    private static final String DATABASE_TABLE = "Attribute";
    
    /**
     * Database creation sql statement
     */
    private static final String DATABASE_CREATE =
            "create table "+DATABASE_TABLE+" ("
            + KEY_ROWID + " INTEGER PRIMARY KEY,"
            + KEY_RS + " INTEGER,"
            + NAME + " TEXT,"
            + LABEL + " TEXT,"
            + DESC + " TEXT,"
            + PREVALUE + " TEXT,"
            + CAT + " TEXT,"
            + TYPE + " TEXT,"
            + DEF + " BOOLEAN,"
            + VISIBLE + " BOOLEAN"
            + ");";
    
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
    public AttributeDbAdapter(Context ctx) {
        this.mCtx = ctx;
    }

    /**
     * Open the Attribute database. If it cannot be opened, try to create a new
     * instance of the database. If it cannot be created, throw an exception to
     * signal the failure
     * 
     * @return this (self reference, allowing this to be chained in an
     *         initialization call)
     * @throws SQLException if the database could be neither opened or created
     */
    public AttributeDbAdapter open() throws SQLException {
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
     * Create a new Attribute using name, rsID and type provided. If the Attribute is
     * successfully created return the new rowId for that note, otherwise return
     * a -1 to indicate failure.
     * 
     * @param rsID the rsID of the Attribute
     * @param name the name of the Attribute
     *  @param type the type of the Attribute
     * @return rowId or -1 if failed
     */
    
    
    public long createDefField(long rsID,String name, String label,String desc,String value,String type){
    	
        ContentValues initialValues = new ContentValues();
        initialValues.put(KEY_RS, rsID);
        initialValues.put(NAME , name);
        initialValues.put(LABEL , label);
        initialValues.put(DESC , desc);
        initialValues.put(PREVALUE, value);  
        initialValues.put(TYPE , type);
		initialValues.put(DEF,1);
		initialValues.put(CAT,"ECO");
		initialValues.put(VISIBLE,1);



        return mDb.insert(DATABASE_TABLE, null, initialValues);
    	
    	
    }
    
    
    public long createNotEdDefField(long rsID,String name, String label,String desc,String value,String type){
    	
        ContentValues initialValues = new ContentValues();
        initialValues.put(KEY_RS, rsID);
        initialValues.put(NAME , name);
        initialValues.put(LABEL , label);
        initialValues.put(DESC , desc);
        initialValues.put(PREVALUE, value);  
        initialValues.put(TYPE , type);
		initialValues.put(DEF,1);
		initialValues.put(CAT,"ADDED");
		initialValues.put(VISIBLE,0);



        return mDb.insert(DATABASE_TABLE, null, initialValues);
    	
    	
    }
    
 
    
    public long createField(long rsID, String name, String label, String desc,String value,String type,String cat, boolean visible) {
        
    	
    	ContentValues initialValues = new ContentValues();
        initialValues.put(KEY_RS, rsID);
        initialValues.put(NAME , name);
        initialValues.put(LABEL , label);
        initialValues.put(DESC , desc); 
        initialValues.put(PREVALUE , value);  
        initialValues.put(TYPE , type); 
        initialValues.put(CAT , cat); 
		initialValues.put(DEF,0);

		if(visible) initialValues.put(VISIBLE,1);
		else initialValues.put(VISIBLE,0);
		
		
        return mDb.insert(DATABASE_TABLE, null, initialValues);
    }

    /**
     * Delete the Attribute with the given rowId
     * 
     * @param rowId id of Attribute to delete
     * @return true if deleted, false otherwise
     */
    public boolean deleteAttribute(long rowId) {

        return mDb.delete(DATABASE_TABLE, KEY_ROWID + "=" + rowId, null) > 0;
    }
    
    
    /**
     * 	Remove attributes from a researchType by ID
     * 
     */
    
    public int deleteAttributes(long rsId) {

        return mDb.delete(DATABASE_TABLE, KEY_RS + "=" + rsId, null);
        
    }
    
    /**
     * Return a Cursor positioned at the Attribute  that belongs to the given attId
     * 
     * @param attId id of Attribute to retrieve
     * @return Cursor positioned to matching note, if found
     * @throws SQLException if note could not be found/retrieved
     */
    
    public Cursor fetchAtribute(long attId) throws SQLException {

    	
  	   return mDb.query(DATABASE_TABLE, new String[] {KEY_ROWID, KEY_RS,
               NAME,TYPE,LABEL,PREVALUE}, KEY_ROWID + "=" + attId, null, null, null, null);
  
  }
    
    public Cursor fetchFieldNameByLabel(long projId,String label) throws SQLException {

    	
   	   return mDb.query(DATABASE_TABLE, new String[] {KEY_ROWID, KEY_RS,
                NAME,TYPE,LABEL,PREVALUE}, KEY_RS +"= "+projId+" and "+LABEL + "=\"" + label+"\"", null, null, null, null);
   
   }
    
    public Cursor fetchFieldLabelByName(long projId,String name) throws SQLException {

    	
    	   return mDb.query(DATABASE_TABLE, new String[] {KEY_ROWID, KEY_RS,
                 NAME,TYPE,LABEL,PREVALUE}, KEY_RS +"= "+projId+" and "+NAME + "=\"" + name+"\"", null, null, null, null);
    
    }


    
    public Cursor fetchFieldIdByName(long projId,String name) throws SQLException {

    	
    	   return mDb.query(DATABASE_TABLE, new String[] {KEY_ROWID, KEY_RS,
                 NAME,TYPE,LABEL,PREVALUE}, KEY_RS +"= "+projId+" and "+NAME + "=\"" + name+"\"", null, null, null, null);
    
    }


    /**
     * Return a Cursor positioned at the Attributes list that belongs to the given rsID
     * 
     * @param rowId id of Attribute to retrieve
     * @return Cursor positioned to matching note, if found
     * @throws SQLException if note could not be found/retrieved
     */
    
    public Cursor fetchAttributesFromRs(long rsId) throws SQLException {

    	
    	   return mDb.query(DATABASE_TABLE, new String[] {KEY_ROWID, KEY_RS,
                   NAME,TYPE,LABEL,PREVALUE,CAT}, KEY_RS + "=" + rsId+" and "+VISIBLE+ "= '1'", null, null, null, DEF+" DESC , "+CAT+" ASC");
    
    }
    
    public Cursor fetchAttributesFromRsNotOrdered(long rsId) throws SQLException {

    	
 	   return mDb.query(DATABASE_TABLE, new String[] {KEY_ROWID,KEY_RS,
                NAME,TYPE,LABEL}, KEY_RS + "=" + rsId, null, null, null, DEF+" DESC , "+CAT+" ASC");
 
 }
    
    public Cursor fetchFieldsFromProject(long rsId, String attName) throws SQLException {

    	
 	   return mDb.query(DATABASE_TABLE, new String[] {KEY_ROWID, KEY_RS,
                NAME,TYPE,LABEL,PREVALUE,CAT}, KEY_RS + "=" + rsId+" and "+ NAME + "=\"" +attName+"\"", null, null, null, null);
 
 }
    
    public Cursor fetchAllFieldsFromProject(long rsId) throws SQLException {

    	
 	   return mDb.query(DATABASE_TABLE, new String[] {KEY_ROWID, KEY_RS,
                NAME,TYPE,LABEL,PREVALUE,CAT,VISIBLE,DESC}, KEY_RS + "=" + rsId, null, null, null, DEF+" DESC , "+CAT+" ASC");
 
 }
    
    public Cursor fetchAllFieldsFromProjectWithDefaultOrder(long rsId) throws SQLException {

    	
  	   return mDb.query(DATABASE_TABLE, new String[] {KEY_ROWID, KEY_RS,
                 NAME,TYPE,LABEL,PREVALUE,CAT,VISIBLE,DESC}, KEY_RS + "=" + rsId, null, null, null, null);
  	   
  
  }


	public boolean setVisibilty(long idRs, String attName, boolean visible) {

	 	ContentValues vals = new ContentValues();
		if (visible) vals.put(VISIBLE,1);
		else vals.put(VISIBLE, 0);
		
		
      return mDb.update(DATABASE_TABLE, vals, KEY_RS + "=" + idRs +" and "+ NAME +"=\""+attName+"\"", null) > 0;
			
		
	}

	public int removeProjecteField(long idField) {

        return mDb.delete(DATABASE_TABLE, KEY_ROWID + "=" + idField, null);

		
		
	}
	
    
    public void startTransaction(){
    	
      	 mDb.beginTransaction();
      	
      	
      	
      }
      public void endTransaction(){
      	
      	mDb.setTransactionSuccessful();
      	mDb.endTransaction();
      	
      	
      }
    

}
