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
 * Remote database access helper class
 * 
 */

public class DataBaseDbAdapter {

	
	/* DataBase main fields */
	public static final String DB_ID = "_id";
    public static final String DB_TAG = "dbTag";
    public static final String ACTIVE_DB = "active";


	/* DataBase Filums  */
    
	public static final String DB_FILUM_ID = "_id";
    public static final String DB_ID_FK = "dbId"; /* Foreign key */
    public static final String FILUM = "filum";
    public static final String ORDER ="ordre";
    public static final String ACTIVE_FILUM ="activeFilum";


    private static final String TAG = "RemoteDataBaseDbAdapter";
    
    
    protected DatabaseHelper mDbHelper;
    protected SQLiteDatabase mDb;
    
    /**
     * Database creation sql statement
     */
    
    
    private static final String REMOTE_DATABASE_DB_CREATE =
            "create table RemoteDataBaseTable ("
            + DB_ID + " INTEGER PRIMARY KEY,"
            + DB_TAG + " TEXT UNIQUE,"
            + ACTIVE_DB + " BOOLEAN"
            + ");";
            
    
    
    private static final String REMOTE_DATABASE_FILUMS_CREATE =
        "create table RemoteDataBaseFilumsTable ("
        + DB_FILUM_ID + " INTEGER PRIMARY KEY,"
        + DB_ID_FK + " INTEGER,"
        + FILUM + " TEXT,"
        + ORDER + " INTEGER,"
        + ACTIVE_FILUM + " BOOLEAN"
        + ");";
    
    

    protected static final String DATABASE_REMOTE_DB = "RemoteDataBaseTable";
    protected static final String DATABASE_REMOTE_DB_FILUMS = "RemoteDataBaseFilumsTable";
    
    private static final String DATABASE_NAME= "RemoteDataBases";
    private static final int DATABASE_VERSION = 2;

    private final Context mCtx;
    

    static class DatabaseHelper extends SQLiteOpenHelper {

        DatabaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
		public void onCreate(SQLiteDatabase db) {

            db.execSQL(REMOTE_DATABASE_DB_CREATE);
            db.execSQL(REMOTE_DATABASE_FILUMS_CREATE);

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
    public DataBaseDbAdapter(Context ctx) {
        this.mCtx = ctx;
    }

    /**
     * Open the RemoteDataBase database. If it cannot be opened, try to create a new
     * instance of the database. If it cannot be created, throw an exception to
     * signal the failure
     * 
     * @return this (self reference, allowing this to be chained in an
     *         initialization call)
     * @throws SQLException if the database could be neither opened or created
     */
    public DataBaseDbAdapter open() throws SQLException {
        mDbHelper = new DatabaseHelper(mCtx);
        mDb = mDbHelper.getWritableDatabase();
        return this;
    }
    
    public void close() {
   
        mDbHelper.close();
    }
    
    
    public long createRemoteDB(String dbTag){
    	
      	ContentValues initialValues = new ContentValues();
        initialValues.put(DB_TAG, dbTag);        
        initialValues.put(ACTIVE_DB , 1);
        
        return mDb.insert(DATABASE_REMOTE_DB, null, initialValues);

    }
    
    
    
    public long createRemoteDBFilum(int rmDBId, String filum,int order){
    	
      	
   	   Cursor list= mDb.query(DATABASE_REMOTE_DB_FILUMS, new String[] {DB_FILUM_ID,DB_ID_FK, FILUM,
                  ORDER}, DB_ID_FK + "=" +rmDBId+" and "+ FILUM +" = \""+filum+"\"", null, null, null, ORDER);
   	   
   	   list.moveToFirst();
   	   
   	   
   	   if(list.getCount()>0){
   		   
   		   list.close();
   		   return -1;
   		 
   	   }
   	   else{
    	
    	
      	ContentValues initialValues = new ContentValues();
        initialValues.put(DB_ID_FK, rmDBId);        
        initialValues.put(FILUM,filum);
        initialValues.put(ORDER,order);
        initialValues.put(ACTIVE_FILUM , 1);
        
               

        return mDb.insert(DATABASE_REMOTE_DB_FILUMS, null, initialValues);
        
   	   }
    
    }

    
    /**
     * 
     * Change dataBase state.
     * 
     * Update dbFilums??¿¿
     * 
     */
        
    public boolean updateDBState(long id,boolean active){
    	
       	ContentValues vals = new ContentValues();
		
		if(active) vals.put(ACTIVE_FILUM,1);
		else vals.put(ACTIVE_FILUM,0);
		
		return mDb.update(DATABASE_REMOTE_DB_FILUMS, vals, DB_FILUM_ID + "=" + id, null) > 0;
    	
    	
    }
    

    public boolean updateDbFilumOrder(int dbFilumId, int order) {

    	ContentValues vals = new ContentValues();
		vals.put(ORDER,order);
		
		return mDb.update(DATABASE_REMOTE_DB_FILUMS, vals, DB_FILUM_ID + "=" + dbFilumId, null) > 0;		
	}


    /**
     * Delete the RemoteDB with the given rmDbId
     * 
     * @param rmDbId id of note to delete
     * @return true if deleted, false otherwise
     */
    
    public boolean deleteRemoteDB(long rmDbId) {

    	boolean success= mDb.delete(DATABASE_REMOTE_DB, DB_ID + "=" + rmDbId, null) > 0;
    	
    	return (mDb.delete(DATABASE_REMOTE_DB, DB_ID_FK + "=" + rmDbId, null) > 0) && success;
    	

    }
    
    
	/*
	 * 
	 * Checks if remoteDB with @dBId has the @dbFilum
	 * 
	 */
    
    public boolean checkDataBaseAvailable(long dbId,String filum) throws SQLException {
    	
    	/*
    	 *  DB_FILUM_ID + DB_ID_FK + FILUM + ORDER
    	 *  
    	 */

    	boolean result=false;
    	
      	
  	   Cursor list= mDb.query(DATABASE_REMOTE_DB_FILUMS, new String[] {DB_FILUM_ID,DB_ID_FK, FILUM,
                 ORDER}, DB_ID_FK + "=" +dbId+" and "+ FILUM +" = \""+filum+"\" and "+ACTIVE_FILUM+ "= '1'", null, null, null, ORDER);
  	   
  	   if(list!=null){ 
  		   
  		   list.moveToFirst();
  	   
  		   result=list.getCount()>0;
  		   
  		   list.close();
  		   
  	   }
  	   
  	  return result;
  	   
  
    }
    
	public Cursor getAllDBList() {
		
	 	   Cursor list= mDb.query(DATABASE_REMOTE_DB, new String[] {DB_ID,DB_TAG},null, null, null, null, DB_ID);
	  	   
	  	   if(list!=null){ 
	  		   
	  		   list.moveToFirst();
	  		   
	  	   }
	  	   
	  	  return list;
	}

    
	public int getFilumsCount(String filum) {

		int count=0;
		
	  	   Cursor list= mDb.query(DATABASE_REMOTE_DB_FILUMS, new String[] {DB_FILUM_ID,DB_ID_FK, FILUM,
	                 ORDER}, FILUM + "=\"" +filum+"\"", null, null, null, ORDER);
	  	   
	  	   if(list!=null){
	  		   
	  		   count=list.getCount();
	  		   list.close();
	  		  
	  	   }
	  	   
	  	   return count;
		
	}

    
    
    public Cursor getDataBaseAvailable(String filum) throws SQLException {

    	
  	   Cursor list= mDb.query(DATABASE_REMOTE_DB_FILUMS, new String[] {DB_FILUM_ID,DB_ID_FK, FILUM,
                 ORDER}, FILUM +" = \""+filum+"\" and "+ACTIVE_FILUM+ "= '1'", null, null, null, ORDER);
  	   
  	   if(list!=null){ 
  		   
  		   list.moveToFirst();
  		   
  	   }
  	   
  	  return list;
  	   
  
    }
    
	public Cursor getAvailableFilumsList(String filum) {
		
		String subQuery="";
		
		if(!filum.equals("")) subQuery=FILUM +" = \""+filum+"\"";
		
		
		   return mDb.query(DATABASE_REMOTE_DB_FILUMS, new String[] {DB_FILUM_ID,DB_ID_FK, FILUM,
	                 ORDER}, subQuery, null, FILUM, null, DB_FILUM_ID +" DESC");

	
	}
	

	public boolean getDBFilumState(int id) {

		boolean result=false;
		
		Cursor c=mDb.query(DATABASE_REMOTE_DB_FILUMS, new String[] {ACTIVE_FILUM},  DB_FILUM_ID + "=" + id, null, null, null, null);
		
		if(c!=null){
			
			c.moveToNext();
			result=c.getInt(0)>0;
		
			c.close();
			
		}
		
		
		
		return result;
		
	}


 
	public Cursor getAvailableDbByFilum(String filumId) {

		//   return mDb.query(DATABASE_REMOTE_DB_FILUMS, new String[] {DB_FILUM_ID,DB_ID_FK, FILUM,
	      //           ORDER,ACTIVE_FILUM}, FILUM+" = \""+filumId+"\"", null, null, null, null);
		   
		   
		   	Cursor mCursor=mDb.rawQuery("SELECT "+DATABASE_REMOTE_DB_FILUMS+"."+DB_FILUM_ID+","+DATABASE_REMOTE_DB_FILUMS+"."+DB_ID_FK+","+FILUM+","+ORDER+","+ACTIVE_FILUM+","+DATABASE_REMOTE_DB+"."+DB_ID+","+DB_TAG+ " FROM " + DATABASE_REMOTE_DB +","+DATABASE_REMOTE_DB_FILUMS
					+ " WHERE "+FILUM+"=\""+filumId+"\" and "+DATABASE_REMOTE_DB+"."+DB_ID+"="+DATABASE_REMOTE_DB_FILUMS+"."+DB_ID_FK+" ORDER BY "+ORDER+";",null);
	    
	
	        return mCursor;
	}

	
    
    /*


    public Cursor fetchAllCitations() {

        return mDb.query(DATABASE_TABLE_CITATION, new String[] {KEY_ROWID, KEY_RS,
                LATITUDE,LONGITUDE,DATE}, null, null, null, null, null);
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
    

    
    public boolean updateToSyncronised(long rowId) {
       
    	ContentValues vals = new ContentValues();
    		vals.put(SINCRONIZED,1);
    		
    		
          return mDb.update(DATABASE_TABLE_CITATION, vals, KEY_ROWID + "=" + rowId, null) > 0;
    }
    
    public Cursor fetchSamplesByFieldId(long sampleAttId) throws SQLException {

      	
 	   return mDb.query(DATABASE_TABLE_FIELD, new String[] {KEY_ROWID,KEY_SAMPLE_ID, KEY_TIPUS_ATRIB,
                VALUE}, KEY_TIPUS_ATRIB + "=" + sampleAttId, null, null, null, null);
 
 }
    
    */
    

    
    public void startTransaction(){
    	
     	 mDb.beginTransaction();
     	
     	
     	
     }
     public void endTransaction(){
     	
     	mDb.setTransactionSuccessful();
     	mDb.endTransaction();
     	
     	
     }

	public boolean removeFilum(long rowId) {

		return mDb.delete(DATABASE_REMOTE_DB_FILUMS, DB_FILUM_ID + ">" + rowId, null)>0;
	
	}




	




  
      


}
