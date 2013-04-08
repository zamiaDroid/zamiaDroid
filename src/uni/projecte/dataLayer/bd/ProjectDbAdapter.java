package uni.projecte.dataLayer.bd;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteConstraintException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class ProjectDbAdapter {
	
	/*Project Table Items*/
	
	public static final String KEY_ROWID = "_id";
    public static final String PROJECTNAME = "name";
    public static final String THESAURUS = "thesaurus";
    public static final String PROJECT_TYPE = "project_type";
    
    /*Project Field Items*/
    public static final String PROJ_ID = "projId";
    public static final String PROJ_NAME = "name";
    public static final String TYPE = "idType";
    public static final String LABEL = "label";
    public static final String DESC = "desc";
    public static final String CAT = "category";
    public static final String DEF = "def";
    public static final String VISIBLE = "visible";
    public static final String PREVALUE = "preValue";
    public static final String ORDER = "ordre";

    /*Project Table Config*/
    public static final String PROJ_ID_FK = "projId";
    public static final String PROJ_CONFIG_KEY="configKey";
    public static final String PROJ_CONFIG_VALUE="configValue";
    
    /*Required fields*/
    private static final String TAG = "ProjectDb";
    private DatabaseHelper mDbHelper;
    private SQLiteDatabase mDb;
    
    /* Second level fields */	    
    public static final String PROJ_FIELD_ID = "projFieldId";

    
    
    /**
     * Database creation sql statement
     */
    
    private static final String DATABASE_NAME = "projects";
    
    private static final String DATABASE_TABLE_PROJECT = "ProjectTable";
    private static final String DATABASE_TABLE_FIELD = "FieldTable";
    private static final String DATABASE_TABLE_PROJECT_CONFIG="ProjectConfigTable";
    
    private static final String DATABASE_TABLE_SECOND_LEVEL_FIELD = "SecondLevelFieldTable";
   
    private static final int DATABASE_VERSION = 4;
    
    /*
     * Version 2: main version
     * Version 3: field Order added
     * Version 4: added Project config table
     * 
     */
    
    /* Strings per a crear les taules */

    private static final String DATABASE_CREATE_TABLE_PROJECT =
            "create table "+DATABASE_TABLE_PROJECT+" ("
            + KEY_ROWID + " INTEGER PRIMARY KEY,"
            + PROJECTNAME + " TEXT UNIQUE,"
            + THESAURUS + " TEXT,"
            + PROJECT_TYPE+ " TEXT"
            + ");";

    private static final String DATABASE_CREATE_TABLE_PROJECT_CONFIG =
        "create table "+DATABASE_TABLE_PROJECT_CONFIG+" ("
        + KEY_ROWID + " INTEGER PRIMARY KEY,"
        + PROJ_ID_FK + " INTEGER,"
        + PROJ_CONFIG_KEY + " TEXT,"
        + PROJ_CONFIG_VALUE + " TEXT"
        + ");";    
    
    private static final String DATABASE_CREATE_FIELD_PROJECT =
        "create table "+DATABASE_TABLE_FIELD+" ("
        + KEY_ROWID + " INTEGER PRIMARY KEY,"
        + PROJ_ID + " INTEGER,"
        + PROJ_NAME + " TEXT,"
        + LABEL + " TEXT,"
        + DESC + " TEXT,"
        + PREVALUE + " TEXT,"
        + CAT + " TEXT,"
        + TYPE + " TEXT,"
        + DEF + " BOOLEAN,"
        + VISIBLE + " BOOLEAN,"
        + ORDER + " INTEGER"
        + ");";
    
    private static final String DATABASE_CREATE_SECOND_LEVEL_FIELD =
        "create table "+DATABASE_TABLE_SECOND_LEVEL_FIELD+" ("
        + KEY_ROWID + " INTEGER PRIMARY KEY,"
        + PROJ_FIELD_ID + " INTEGER,"
        + PROJ_NAME + " TEXT,"
        + LABEL + " TEXT,"
        + DESC + " TEXT,"
        + PREVALUE + " TEXT,"
        + CAT + " TEXT,"
        + TYPE + " TEXT,"
        + DEF + " BOOLEAN,"
        + VISIBLE + " BOOLEAN"
        + ");";
    
    

    private final Context mCtx;

    private static class DatabaseHelper extends SQLiteOpenHelper {

        DatabaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        public void onCreate(SQLiteDatabase db) {

            db.execSQL(DATABASE_CREATE_TABLE_PROJECT);
            db.execSQL(DATABASE_CREATE_FIELD_PROJECT);
            db.execSQL(DATABASE_CREATE_SECOND_LEVEL_FIELD);
            db.execSQL(DATABASE_CREATE_TABLE_PROJECT_CONFIG);



        }

        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            Log.w(TAG, "Upgrading database from version " + oldVersion + " to "
                    + newVersion + ", which will destroy all old data");
     
            if (oldVersion < 3) {

                final String ALTER_TBL = 
                    "ALTER TABLE " + DATABASE_TABLE_FIELD + 
                    " ADD COLUMN " + ORDER + " INTEGER "
                    + ";";
                
                db.execSQL(ALTER_TBL);
            	
            }
            if (oldVersion < 4) {

                db.execSQL(DATABASE_CREATE_TABLE_PROJECT_CONFIG);
            	
            }
            
        }
    }

    /**
     * Constructor - takes the context to allow the database to be
     * opened/created
     * 
     * @param ctx the Context within which to work
     */
    public ProjectDbAdapter(Context ctx) {
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
    public ProjectDbAdapter open() throws SQLException {
        mDbHelper = new DatabaseHelper(mCtx);
        mDb = mDbHelper.getWritableDatabase();
        return this;
    }
    
    public void close() {
        mDbHelper.close();
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
    public long createProject(String name, String description, String projType) {
        
    	ContentValues initialValues = new ContentValues();
        initialValues.put(PROJECTNAME , name);
        initialValues.put(THESAURUS , description);
        initialValues.put(PROJECT_TYPE , projType);


        long result=0;
        
        try{
        	
        	result=mDb.insert(DATABASE_TABLE_PROJECT, null, initialValues);
        }
        catch (SQLiteConstraintException sqle){
        	
            Log.i(TAG, "Project exists "+name+" , desc: "+description);
        	
        	result=-1;
        	
        }
        catch(Exception exception) {

	        int i=0;
	        i++;
  
        }

        Log.i(TAG, "Project Created. "+name+" , desc: "+description);
        
        return result;
    }

    /**
     * Delete the Research with the given rowId
     * 
     * @param rowId id of Research to delete
     * @return true if deleted, false otherwise
     */
    public boolean deleteProject(long rowId) {

        return mDb.delete(DATABASE_TABLE_PROJECT, KEY_ROWID + "=" + rowId, null) > 0;
    }

    /**
     * Return a Cursor over the list of all Researches in the database
     * 
     * @return Cursor over all notes
     */
    public Cursor fetchAllProjects() {

        return mDb.query(DATABASE_TABLE_PROJECT, new String[] {KEY_ROWID, PROJECTNAME,THESAURUS}, null, null, null, null, PROJECTNAME + " ASC");
    }

    
    
    /**
     * Return a Cursor positioned at the Research that matches the given rowId
     * 
     * @param rowId id of Research to retrieve
     * @return Cursor positioned to matching Research, if found
     * @throws SQLException if Research could not be found/retrieved
     */
    public Cursor fetchProjectById(long rowId) throws SQLException {

        Cursor mCursor =

                mDb.query(true, DATABASE_TABLE_PROJECT, new String[] {KEY_ROWID,
                        PROJECTNAME,THESAURUS,PROJECT_TYPE}, KEY_ROWID + "=" + rowId, null,
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
    
    public Cursor fetchProjectByName(String name) throws SQLException {

        Cursor mCursor =

                mDb.query(true, DATABASE_TABLE_PROJECT, new String[] {KEY_ROWID,
                        PROJECTNAME,THESAURUS,PROJECT_TYPE}, PROJECTNAME + "='" + name+"'", null,
                        null, null, null, null);
        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;

    }
    
    public Cursor fetchProjectByTh(String thName) throws SQLException {

        Cursor mCursor =

                mDb.query(true, DATABASE_TABLE_PROJECT, new String[] {KEY_ROWID,
                        PROJECTNAME,THESAURUS,PROJECT_TYPE}, THESAURUS + "='" + thName+"'", null,
                        null, null, null, null);
        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;

    }

	public boolean updateProjectTh(long rsId, String thName) {
		
		ContentValues vals = new ContentValues();
		vals.put(THESAURUS,thName);
		
        Log.i(TAG, "Project Thesaurus Changed "+rsId+" , thName: "+thName);

		
      return mDb.update(DATABASE_TABLE_PROJECT, vals, KEY_ROWID + "=" + rsId, null) > 0;
		
	}
	
	public boolean updateProjectType(long rsId, String projType) {
		
		ContentValues vals = new ContentValues();
		vals.put(PROJECT_TYPE,projType);
		
        Log.i(TAG, "Project Thesaurus Changed "+rsId+" , PROJECT_TYPE: "+projType);

		
      return mDb.update(DATABASE_TABLE_PROJECT, vals, KEY_ROWID + "=" + rsId, null) > 0;
		
	}
	
	
	/* Field operations */
	
	
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
        initialValues.put(PROJ_ID, rsID);
        initialValues.put(PROJ_NAME , name);
        initialValues.put(LABEL , label);
        initialValues.put(DESC , desc);
        initialValues.put(PREVALUE, value);  
        initialValues.put(TYPE , type);
		initialValues.put(DEF,1);
		initialValues.put(CAT,"ECO");
		initialValues.put(VISIBLE,1);

        return mDb.insert(DATABASE_TABLE_FIELD, null, initialValues);

    }
    
    
    public long createNotEdDefField(long rsID,String name, String label,String desc,String value,String type){
    	
        ContentValues initialValues = new ContentValues();
        initialValues.put(PROJ_ID, rsID);
        initialValues.put(PROJ_NAME, name);
        initialValues.put(LABEL , label);
        initialValues.put(DESC , desc);
        initialValues.put(PREVALUE, value);  
        initialValues.put(TYPE , type);
		initialValues.put(DEF,1);
		initialValues.put(CAT,"ADDED");
		initialValues.put(VISIBLE,0);

        return mDb.insert(DATABASE_TABLE_FIELD, null, initialValues);
    	
    }
    
 
    
    public long createField(long rsID, String name, String label, String desc,String value,String type,String cat, boolean visible) {
        
    	
    	ContentValues initialValues = new ContentValues();
        initialValues.put(PROJ_ID, rsID);
        initialValues.put(PROJ_NAME, name);
        initialValues.put(LABEL , label);
        initialValues.put(DESC , desc); 
        initialValues.put(PREVALUE , value);  
        initialValues.put(TYPE , type); 
        initialValues.put(CAT , cat); 
		initialValues.put(DEF,0);

		if(visible) initialValues.put(VISIBLE,1);
		else initialValues.put(VISIBLE,0);
		
		
        return mDb.insert(DATABASE_TABLE_FIELD, null, initialValues);
    }

    /**
     * Delete the Attribute with the given rowId
     * 
     * @param rowId id of Attribute to delete
     * @return true if deleted, false otherwise
     */
    public boolean deleteField(long rowId) {

        return mDb.delete(DATABASE_TABLE_FIELD, KEY_ROWID + "=" + rowId, null) > 0;
    }
    
    
    /**
     * 	Remove attributes from a researchType by ID
     * 
     */
    
    public int deleteFieldsFromProjectId(long rsId) {

        return mDb.delete(DATABASE_TABLE_FIELD, PROJ_ID + "=" + rsId, null);
        
    }
    
    /**
     * Return a Cursor positioned at the Attribute  that belongs to the given attId
     * 
     * @param attId id of Attribute to retrieve
     * @return Cursor positioned to matching note, if found
     * @throws SQLException if note could not be found/retrieved
     */
    
    public Cursor fetchField(long attId) throws SQLException {

    	
  	   return mDb.query(DATABASE_TABLE_FIELD, new String[] {KEY_ROWID, PROJ_ID,
  			 PROJ_NAME,TYPE,LABEL,PREVALUE}, KEY_ROWID + "=" + attId, null, null, null, null);
  
  }
    
    public Cursor fetchFieldNameByLabel(long projId,String label) throws SQLException {

    	
   	   return mDb.query(DATABASE_TABLE_FIELD, new String[] {KEY_ROWID, PROJ_ID,
   			PROJ_NAME,TYPE,LABEL,PREVALUE}, PROJ_ID +"= "+projId+" and "+LABEL + "=\"" + label+"\"", null, null, null, null);
   
   }
    
    public Cursor fetchFieldLabelByName(long projId,String name) throws SQLException {

    	
    	   return mDb.query(DATABASE_TABLE_FIELD, new String[] {KEY_ROWID, PROJ_ID,
    			   PROJ_NAME,TYPE,LABEL,PREVALUE}, PROJ_ID +"= "+projId+" and "+PROJ_NAME + "=\"" + name+"\"", null, null, null, null);
    
    }


    
    public Cursor fetchFieldIdByName(long projId,String name) throws SQLException {

    	
    	   return mDb.query(DATABASE_TABLE_FIELD, new String[] {KEY_ROWID, PROJ_ID,
    			   PROJ_NAME,TYPE,LABEL,PREVALUE,DESC,CAT}, PROJ_ID +"= "+projId+" and "+PROJ_NAME + "=\"" + name+"\"", null, null, null, null);
    
    }


    /**
     * Return a Cursor positioned at the Attributes list that belongs to the given rsID
     * 
     * @param rowId id of Attribute to retrieve
     * @return Cursor positioned to matching note, if found
     * @throws SQLException if note could not be found/retrieved
     */
    
    public Cursor fetchProjectsFromProject(long rsId) throws SQLException {

    	
    	   return mDb.query(DATABASE_TABLE_FIELD, new String[] {KEY_ROWID, PROJ_ID,
    			   PROJ_NAME,TYPE,LABEL,PREVALUE,CAT}, PROJ_ID + "=" + rsId+" and "+VISIBLE+ "= '1'", null, null, null, DEF+" DESC , "+CAT+" ASC");
    
    }
    
    public Cursor fetchOrderedFieldsFromProject(long rsId) throws SQLException {

    	
 	   return mDb.query(DATABASE_TABLE_FIELD, new String[] {KEY_ROWID, PROJ_ID,
 			   PROJ_NAME,TYPE,LABEL,PREVALUE,CAT}, PROJ_ID + "=" + rsId+" and "+VISIBLE+ "= '1'", null, null, null, DEF+" DESC , "+ORDER+" ASC");
 
 }
 
    
    public Cursor fetchProjectsFromProjectOrdered(long rsId) throws SQLException {

    	
 	   return mDb.query(DATABASE_TABLE_FIELD, new String[] {KEY_ROWID, PROJ_ID,
 			   PROJ_NAME,TYPE,LABEL,PREVALUE,CAT,ORDER,VISIBLE}, PROJ_ID + "=" + rsId, null, null, null, DEF+" DESC , "+ORDER+" ASC");
 
 }
    
    public Cursor fetchProjectsFromRsNotOrdered(long rsId) throws SQLException {

    	
 	   return mDb.query(DATABASE_TABLE_FIELD, new String[] {KEY_ROWID,PROJ_ID,
 			  PROJ_NAME,TYPE,LABEL}, PROJ_ID + "=" + rsId, null, null, null, DEF+" DESC , "+CAT+" ASC");
 
 }
    
    public Cursor fetchFieldsFromProject(long rsId, String attName) throws SQLException {

    	
 	   return mDb.query(DATABASE_TABLE_FIELD, new String[] {KEY_ROWID, PROJ_ID,
 			  PROJ_NAME,TYPE,LABEL,PREVALUE,CAT}, PROJ_ID + "=" + rsId+" and "+ PROJ_NAME + "=\"" +attName+"\"", null, null, null, null);
 
 }
    
	public Cursor findSecondLevelField(long projId) {

	 	   return mDb.query(DATABASE_TABLE_FIELD, new String[] {KEY_ROWID, PROJ_ID,
	  			  PROJ_NAME,TYPE,LABEL,PREVALUE,CAT}, PROJ_ID + "=" + projId +" and ("+ TYPE + "=\"secondLevel\" or "+ TYPE + "=\"polygon\"  or "+ TYPE + "=\"multiphoto\")", null, null, null, null);		
	}
	
	public Cursor findThesaurusField(long projId) {

	 	   return mDb.query(DATABASE_TABLE_FIELD, new String[] {KEY_ROWID, PROJ_ID,
	  			  PROJ_NAME,TYPE,LABEL,PREVALUE,CAT}, PROJ_ID + "=" + projId +" and "+ TYPE + "=\"thesaurus\"", null, null, null, null);		
	}
	
	
	public Cursor findComplexFields(long projId) {

	 	   return mDb.query(DATABASE_TABLE_FIELD, new String[] {KEY_ROWID, PROJ_ID,
	  			  PROJ_NAME,TYPE,LABEL,PREVALUE,CAT}, PROJ_ID + "=" + projId +" and "+ TYPE + "=\"complex\"", null, null, null, null);		
	}
	
	public Cursor findComplexSecondLevelFields(long projId) {

	 	   return mDb.query(DATABASE_TABLE_SECOND_LEVEL_FIELD, new String[] {KEY_ROWID, PROJ_FIELD_ID,
	  			  PROJ_NAME,TYPE,LABEL,PREVALUE,CAT}, PROJ_FIELD_ID + "=" + projId +" and "+ TYPE + "=\"complex\"", null, null, null, null);		
	}
    
    public Cursor fetchAllFieldsFromProject(long rsId) throws SQLException {

    	
 	   return mDb.query(DATABASE_TABLE_FIELD, new String[] {KEY_ROWID, PROJ_ID,
 			  PROJ_NAME,TYPE,LABEL,PREVALUE,CAT,VISIBLE}, PROJ_ID + "=" + rsId, null, null, null, DEF+" DESC , "+CAT+" ASC");
 
 }

	public boolean setFieldVisibilty(long idRs, String attName, boolean visible) {

	 	ContentValues vals = new ContentValues();
		if (visible) vals.put(VISIBLE,1);
		else vals.put(VISIBLE, 0);
		
		
      return mDb.update(DATABASE_TABLE_FIELD, vals, PROJ_ID + "=" + idRs +" and "+ PROJ_NAME +"=\""+attName+"\"", null) > 0;
			
		
	}
	
	
	public boolean setFieldOrder(long idRs, long fieldId, int order) {
		
		ContentValues vals = new ContentValues();
		vals.put(ORDER,order);
		
		
		return mDb.update(DATABASE_TABLE_FIELD, vals, PROJ_ID + "=" + idRs +" and "+ KEY_ROWID +"="+fieldId, null) > 0;
		
	}

	public int removeProjectField(long idField) {

        return mDb.delete(DATABASE_TABLE_FIELD, KEY_ROWID + "=" + idField, null);

		
		
	}
	
    
    public void startTransaction(){
    	
      	 mDb.beginTransaction();

      	
      }
      public void endTransaction(){
      	
      	mDb.setTransactionSuccessful();
      	mDb.endTransaction();
      	
      	
      }
      
      
      /* Second level fields */
      
      
      public Cursor fetchSLFieldsFromProject(long projFieldId) throws SQLException {

      	
   	   return mDb.query(DATABASE_TABLE_SECOND_LEVEL_FIELD, new String[] {KEY_ROWID, PROJ_FIELD_ID,
   			   PROJ_NAME,TYPE,LABEL,PREVALUE,CAT,VISIBLE}, PROJ_FIELD_ID + "=" + projFieldId+" and "+VISIBLE+ "= '1'", null, null, null, DEF+" DESC , "+CAT+" ASC");
   
      }
      

      public long createSecondLevelField(long subProjId,String name, String label,String desc,String value,String type){
      	
        ContentValues initialValues = new ContentValues();
        initialValues.put(PROJ_FIELD_ID, subProjId);
        initialValues.put(PROJ_NAME , name);
        initialValues.put(LABEL , label);
        initialValues.put(DESC , desc);
        initialValues.put(PREVALUE, value);  
        initialValues.put(TYPE , type);
  		initialValues.put(DEF,1);
  		initialValues.put(CAT,"ECO");
  		initialValues.put(VISIBLE,1);

          return mDb.insert(DATABASE_TABLE_SECOND_LEVEL_FIELD, null, initialValues);

      }
      
      public Cursor fetchSecondLevelFieldFromProject(long rsId, String attName) throws SQLException {

      	
    	   return mDb.query(DATABASE_TABLE_SECOND_LEVEL_FIELD, new String[] {KEY_ROWID, PROJ_FIELD_ID,
    			  PROJ_NAME,TYPE,LABEL,PREVALUE,CAT}, PROJ_FIELD_ID + "=" + rsId+" and "+ PROJ_NAME + "=\"" +attName+"\"", null, null, null, null);
    
    }
      

  	public boolean setSecondLevelFieldVisibilty(long idRs, String attName, boolean visible) {

  	 	ContentValues vals = new ContentValues();
  		if (visible) vals.put(VISIBLE,1);
  		else vals.put(VISIBLE, 0);
  		
  		
        return mDb.update(DATABASE_TABLE_SECOND_LEVEL_FIELD, vals, PROJ_FIELD_ID + "=" + idRs +" and "+ PROJ_NAME +"=\""+attName+"\"", null) > 0;
  			
  		
  	}
      
  	public boolean updateSecondLevelFieldId(long oldId, long newId) {

	 	ContentValues vals = new ContentValues();
		vals.put(PROJ_FIELD_ID,newId);

		return mDb.update(DATABASE_TABLE_SECOND_LEVEL_FIELD, vals, PROJ_FIELD_ID + "=" + oldId, null) > 0;
			
		
	}
  	
    public Cursor fetchFieldsNoSecondLevelFromProject(long rsId) throws SQLException {

    	
  	   return mDb.query(DATABASE_TABLE_FIELD, new String[] {KEY_ROWID, PROJ_ID,
  			  PROJ_NAME,TYPE,LABEL,PREVALUE,CAT,VISIBLE}, PROJ_ID + "=" + rsId +" and "+ TYPE + "!=\"secondLevel\"" , null, null, null, DEF+" DESC , "+CAT+" ASC");
  
  }
    
    
    
	public long isQuercusExportable(long projId) {

		long retValue=-1;
		
	 	Cursor mCursor=mDb.rawQuery("SELECT SecondLevelFieldTable.projFieldId,ProjectTable.name, FieldTable.name FROM ProjectTable, " +
	 			"FieldTable, SecondLevelFieldTable WHERE FieldTable.idType=\"secondLevel\" and projId=ProjectTable._id and " +
	 			"projFieldId=FieldTable._id and ProjectTable._id="+projId+" and SecondLevelFieldTable.name=\"OriginalTaxonName\"",null);
    	
    	
        if (mCursor != null) {
            mCursor.moveToFirst();
        }
		
        if(mCursor.getCount()<2 && mCursor.getCount()>0){
        	
        	retValue=mCursor.getLong(0);
        	
        }
        
        
		return retValue;
	}

	public Cursor getPhotoFieldsFromProject(long projId) {

		   return mDb.query(DATABASE_TABLE_FIELD, new String[] {KEY_ROWID, PROJ_ID,
		  			  PROJ_NAME,TYPE,LABEL,PREVALUE,CAT,VISIBLE}, PROJ_ID + " = " + projId +" and "+ TYPE + " = \"photo\"" , null, null, null,null);
	}
	
	public Cursor getMultiPhotoFieldsFromProject(long projId) {

		   return mDb.query(DATABASE_TABLE_FIELD, new String[] {KEY_ROWID, PROJ_ID,
		  			  PROJ_NAME,TYPE,LABEL,PREVALUE,CAT,VISIBLE}, PROJ_ID + " = " + projId +" and "+ TYPE + " = \"multiPhoto\"" , null, null, null,null);
	}

	public boolean updateComplexType(long fieldId) {

	 	ContentValues vals = new ContentValues();
		vals.put(TYPE,"complex");
		
		return mDb.update(DATABASE_TABLE_FIELD, vals, KEY_ROWID + "=" + fieldId, null) > 0;

		
	}
    
	public boolean updateSecondLevelComplexType(long fieldId) {

	 	ContentValues vals = new ContentValues();
		vals.put(TYPE,"complex");
		
		return mDb.update(DATABASE_TABLE_SECOND_LEVEL_FIELD, vals, KEY_ROWID + "=" + fieldId, null) > 0;

		
	}
	
    
    public int removeSLFieldsFromSLField(long fieldId) {

        return mDb.delete(DATABASE_TABLE_SECOND_LEVEL_FIELD, PROJ_FIELD_ID + "=" + fieldId, null);
        
    }

	public Cursor fetchProjectConfigValue(long projId,String projConfKeyName) {

		   return mDb.query(DATABASE_TABLE_PROJECT_CONFIG, new String[] {KEY_ROWID,PROJ_ID_FK,PROJ_CONFIG_KEY,
	    			  PROJ_CONFIG_VALUE}, PROJ_ID_FK + "=" + projId+" and "+ PROJ_CONFIG_KEY + "=\"" +projConfKeyName+"\"", null, null, null, null);
	    
	}

	public long insertProjectConfigValue(long projId, String confKey,
			String confValue) {

		   ContentValues initialValues = new ContentValues();
	        initialValues.put(PROJ_ID_FK, projId);
	        initialValues.put(PROJ_CONFIG_KEY , confKey);
	        initialValues.put(PROJ_CONFIG_VALUE , confValue);

          return mDb.insert(DATABASE_TABLE_PROJECT_CONFIG, null, initialValues);
		
	}

	public boolean updateProjectConfigValue(long projId, String projConfKey,
			String newValue) {

	 	ContentValues vals = new ContentValues();
		vals.put(PROJ_CONFIG_VALUE,newValue);

		return mDb.update(DATABASE_TABLE_PROJECT_CONFIG, vals,PROJ_ID_FK + "=" + projId+" and "+ PROJ_CONFIG_KEY + "=\"" +projConfKey+"\"", null) > 0;		
	}




}
