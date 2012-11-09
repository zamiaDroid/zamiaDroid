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
 * Simple DataType database access helper class. 
 * recommended).
 */
public class ThesaurusItemsDbAdapter {

	public static final String KEY_ROWID = "_id";   
    public static final String GENUS = "Genus";   			/* Genus */
    public static final String SPECIE = "Specie"; 			/* SpecificEpithet */
    public static final String SUBSPECIE = "Subspecie";		/* InfraspecificEpithet */
    public static final String ICODE = "iCode"; 			/* PrimaryKey */
    public static final String NAMECODE = "NameCode"; 		/* SecondaryKey */
    public static final String AUTHOR = "Author";  			/* SpecificEpithetAuthor */
    public static final String SUBAUTHOR = "SubAuthor"; 	/* InfraspecificEpithetAuthor */

    
    /* New field created when plain thesaurus reader added  */
   
    public static final String INFRA_SPEC_RANK="InfraspecificRank"; /* {"form.", "subsp.", "var."} */

    
    
  
    private static final String TAG = "ThesaurusItemDbAdapter";
    private DatabaseHelper mDbHelper;
    private SQLiteDatabase mDb;

    
    /**
     * Database creation sql statement
     */
  

    private static final String DATABASE_NAME = "Thesaurus";
    
    private static final int DATABASE_VERSION = 3;
    
    /*
     * Version 2: main version
     * Version 3: thesaurusType added
     * 
     */
    
    
	private static String DATABASE_TABLE;

    private final Context mCtx;
    

    private static class DatabaseHelper extends SQLiteOpenHelper {
    	
        private String DATABASE_CREATE;


        DatabaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
		public void onCreate(SQLiteDatabase db) {
        	
        	DATABASE_CREATE =
                "create table if not exists "+ DATABASE_TABLE + " ("
                + KEY_ROWID + " INTEGER PRIMARY KEY,"
                + GENUS + " TEXT,"
                + SPECIE + " TEXT,"
                + SUBSPECIE + " TEXT,"
                + ICODE + " TEXT,"
                + NAMECODE + " TEXT,"
                + AUTHOR + " TEXT,"
                + SUBAUTHOR + " TEXT,"
                + INFRA_SPEC_RANK + " TEXT"
                + ");";

            db.execSQL(DATABASE_CREATE);
           

        }
        
     @Override
	public void onOpen(SQLiteDatabase db) {
        	
        	DATABASE_CREATE =
                "create table if not exists "+ DATABASE_TABLE + " ("
                + KEY_ROWID + " INTEGER PRIMARY KEY,"
                + GENUS + " TEXT,"
                + SPECIE + " TEXT,"
                + SUBSPECIE + " TEXT,"
                + ICODE + " TEXT,"
                + NAMECODE + " TEXT,"
                + AUTHOR + " TEXT,"
                + SUBAUTHOR + " TEXT,"
                + INFRA_SPEC_RANK + " TEXT"
                + ");";

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
                        " ADD COLUMN " + INFRA_SPEC_RANK + " text NOT NULL DEFAULT 'subsp.'"
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
    public ThesaurusItemsDbAdapter(Context ctx) {
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
    public ThesaurusItemsDbAdapter open(String dbName) throws SQLException {
        
    	DATABASE_TABLE=dbName;
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
    public long addThesaurusItem(String genus, String specie, String subspecie, String iCode, String nameCode, String author,String subAuthor, String infraSpecRank) {
 
    	
    	ContentValues initialValues = new ContentValues();
        initialValues.put(GENUS, genus);
        initialValues.put(SPECIE , specie);
        initialValues.put(SUBSPECIE ,subspecie);
        initialValues.put(ICODE , iCode);  
        initialValues.put(NAMECODE , nameCode);  
        initialValues.put(AUTHOR , author);  
        initialValues.put(SUBAUTHOR , subAuthor);  
        
        if(infraSpecRank.equals("")) initialValues.put(INFRA_SPEC_RANK , "subsp."); 
        else initialValues.put(INFRA_SPEC_RANK ,infraSpecRank);  


        return mDb.insert(DATABASE_TABLE, null, initialValues);
        
    

    }
    
    public boolean removeThesaurusItem(long thItemId) {

        return mDb.delete(DATABASE_TABLE, KEY_ROWID + "=" + thItemId, null) > 0;

	}
    
    
    public void startTransaction(){
    	
    	 mDb.beginTransaction();
    	
    	
    	
    }
    public void endTransaction(){
    	
    	mDb.setTransactionSuccessful();
    	mDb.endTransaction();
    	
    	
    }
    
    public long fastInsert(String genus, String specie, String subspecie, String iCode, String nameCode, String author,String subAuthor) {
 
    	
    	ContentValues initialValues = new ContentValues();
        initialValues.put(GENUS, genus);
        initialValues.put(SPECIE , specie);
        initialValues.put(SUBSPECIE ,subspecie);
        initialValues.put(ICODE , iCode);  
        initialValues.put(NAMECODE , nameCode);  
        initialValues.put(AUTHOR , author);  
        initialValues.put(SUBAUTHOR , subAuthor);  

        return mDb.insert(DATABASE_TABLE, null, initialValues);
        
    

    }

    /**
     * Delete the DataType with the given dtId
     * 
     * @param dtId id of note to delete
     * @return true if deleted, false otherwise
     */
/*    public boolean deleteDT(long dtId) {

        return mDb.delete(DATABASE_TABLE, KEY_ROWID + "=" + dtId, null) > 0;
    }
  */  
    /**
     * Return a Cursor over the list of all DT's in the database
     * 
     * @return Cursor over all DT's
     */
  /*  public Cursor fetchAllDTs() {

        return mDb.query(DATABASE_TABLE, new String[] {KEY_ROWID, TYPE,
                NAME,DESC}, null, null, null, null, null);
    }*/

    /**
     * Return a Cursor positioned at the DT that matches the given name
     * 
     * @param name of note to retrieve
     * @return Cursor positioned to matching note, if found
     * @throws SQLException if note could not be found/retrieved
     */
    
    public Cursor fetchTbhItem(String genus,String specie, String subspecie) throws SQLException {
    	
 	   return mDb.query(DATABASE_TABLE, new String[] {KEY_ROWID, GENUS,
               SPECIE,SUBSPECIE,AUTHOR,ICODE,NAMECODE,SUBAUTHOR,INFRA_SPEC_RANK}, GENUS + "= \"" + genus +"\" and "+ SPECIE + "= \"" + specie +"\" and "+ SUBSPECIE + "= \""+ subspecie+"\"", null, null, null, null);
 
 }
    
    /**
     * Return a Cursor positioned at first DT that matches the given Type
     * 
     * @param type of DT's to retrieveORDER BY Genus
     * @return Cursor positioned to matching note, if found
     * @throws SQLException if note could not be found/retrieved
     */
    
   public Cursor fetchAllItems() throws SQLException {
    	
    	   return mDb.query(DATABASE_TABLE, new String[] {KEY_ROWID}, null, null, null, null, null,"0, 10");
    
    }
   
   public Cursor fetchNumAllItems() throws SQLException {
   	
	   return mDb.query(DATABASE_TABLE, new String[] {KEY_ROWID}, null, null, null, null, null);

}
   
   public Cursor fetchSynonymous(String icode) throws SQLException {
   	
	   return mDb.query(DATABASE_TABLE, new String[] {KEY_ROWID, GENUS,
              SPECIE,SUBSPECIE,AUTHOR,SUBAUTHOR,INFRA_SPEC_RANK}, ICODE + "= \"" + icode +"\"", null, null, null, null);

}

	public Cursor fetchNext(String selection) {
		   //" ORDER BY Genus Specie Subspecie ASC"
		
			return mDb.query(DATABASE_TABLE, new String[] {KEY_ROWID,GENUS,
	                  SPECIE,SUBSPECIE,AUTHOR,ICODE,NAMECODE,SUBAUTHOR,INFRA_SPEC_RANK}, selection, null, null,null,GENUS+","+SPECIE+","+SUBSPECIE);
	}
	
	public Cursor fetchGenusNext(String selection) {
			
		
			return mDb.query(DATABASE_TABLE, new String[] {KEY_ROWID,GENUS,
	                  SPECIE,SUBSPECIE,AUTHOR,ICODE,NAMECODE,SUBAUTHOR,INFRA_SPEC_RANK}, selection, null, GENUS,null,GENUS+","+SPECIE+","+SUBSPECIE);
	}
	
	public void dropTable(String tbName){
		
		mDb.execSQL("drop table if exists "+tbName);
		
	}


	



    
}
