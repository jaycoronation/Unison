package com.unisonpharmaceuticals.databse;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class UnisonDatabaseHelper extends SQLiteOpenHelper
{
	public String TAG = "UNISON DATABASE";
	
	private static final int VER_CRASHLYTICS_BETA_UPDATE_3 = 3;

	private static final int DATABASE_VERSION = VER_CRASHLYTICS_BETA_UPDATE_3;
	
	public static final String DB_NAME = "Unison.db";
	
	public static final String DAILY_ENTRY_TABLE = "DailyEntryTable";
	public static final String DAILY_AREA_TABLE = "DailyAreaTable";
	public static final String Today_AREA_TABLE = "TodayAreaTable";
	public static final String DOCTOR_DETAILS = "DoctorDetails";
	public static final String PRODUCT_DETAILS = "ProductDetails";
	public static final String PRODUCT_DETAILS_NEXT_MONTH = "ProductDetailsNextMonth";
	public static final String REASON_DETAILS = "ReasonDetails";
	public static final String DBC_CODE_DETAILS = "DbcCodeDetails";
	public static final String WORK_WITH_DETAILS = "WorkWithDetails";
	public static final String DOCTOR_BY_TODAY = "DoctorByTodayDetails";
	public static final String ADVICE_FOR_DETAILS = "AdviceForDetails";
	
	public static final String ID = "id";
	public static final String DATE = "date";
	public static final String AREA = "area";
	public static final String CITY = "city";
	public static final String DOCTORBUSINESS = "doctorBusiness";
	public static final String DOCTORTYPE = "doctorType";
	public static final String SPECIALITY = "speciality";
	public static final String DBC = "dbc";
	public static final String DBC_CODE = "dbccode";
	public static final String ADVICE_FOR_ID = "AdviceForId";
	public static final String ADVICE_FOR_NAME = "AdviceForName";
	public static final String WORK_WIH = "workwith";
	public static final String WORK_WIH_CODE = "workwithcode";
	public static final String DOCTOR = "doctor";
	public static final String DOCTOR_CODE = "doctorcode";
	public static final String NEW_DOCTOR_AMOUNT = "newdoctoramount";
	public static final String NEW_DOCTOR_CODE = "newdoctorcode";
	public static final String REMARKS	= "remarks";
	public static final String INTERNEE	= "internee";
	public static final String DDTDATE	= "ddtdate";
	public static final String ISNEWCYCLE = "isnewcycle";
	public static final String PRODUCT	= "product";
	public static final String PRODUCT_TYPE = "Producttype";
	public static final String PRODUCT_ISFORNEXTMONTH = "ProductIsForNextMonth";
	public static final String PRODUCT_ID	= "product_id";
	public static final String REASON = "reason";
	public static final String REASON_CODE = "reasonCode";
	public static final String UNIT = "unit";
	public static final String USER_ID = "userid";
	public static final String API_STRING = "apistring";
	public static final String FOCUS_FOR_STRING = "focusForString";

	public UnisonDatabaseHelper(Context context)
	{
		super(context, DB_NAME, null, DATABASE_VERSION);
	}
	
	@Override
	public void onCreate(SQLiteDatabase sqlDB)
	{
		try
		{
			//default character set utf8mb4
			sqlDB.execSQL("CREATE TABLE IF NOT EXISTS "+UnisonDatabaseHelper.DAILY_ENTRY_TABLE
														+"("+UnisonDatabaseHelper.ID+" INTEGER PRIMARY KEY , "
														    +UnisonDatabaseHelper.DATE+" TEXT , "
									 						+UnisonDatabaseHelper.AREA+" TEXT , "
															+UnisonDatabaseHelper.DBC+" TEXT , "
															+UnisonDatabaseHelper.DBC_CODE+" TEXT , "
															+UnisonDatabaseHelper.WORK_WIH+" TEXT , "
															+UnisonDatabaseHelper.WORK_WIH_CODE+" TEXT , "
															+UnisonDatabaseHelper.ADVICE_FOR_ID+" TEXT , "
															+UnisonDatabaseHelper.ADVICE_FOR_NAME+" TEXT , "
															+UnisonDatabaseHelper.DOCTOR+" TEXT , "
															+UnisonDatabaseHelper.DOCTOR_CODE+" TEXT , "
															+UnisonDatabaseHelper.SPECIALITY+" TEXT , "
															+UnisonDatabaseHelper.NEW_DOCTOR_AMOUNT+" TEXT , "
															+UnisonDatabaseHelper.NEW_DOCTOR_CODE+" TEXT , "
															+UnisonDatabaseHelper.REMARKS+" TEXT , "
															+UnisonDatabaseHelper.INTERNEE+" TEXT , "
															+UnisonDatabaseHelper.DDTDATE+" TEXT , "
															+UnisonDatabaseHelper.ISNEWCYCLE+" TEXT , "
															+UnisonDatabaseHelper.PRODUCT+" TEXT , "
															+UnisonDatabaseHelper.REASON+" TEXT , "
															+UnisonDatabaseHelper.UNIT+" TEXT , "
															+UnisonDatabaseHelper.API_STRING+" TEXT , "
															+UnisonDatabaseHelper.FOCUS_FOR_STRING+" TEXT);");
			
			sqlDB.execSQL("PRAGMA encoding = \"UTF-8MB4\"");
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		
//		Table for Area
		try
		{
			//default character set utf8mb4
			sqlDB.execSQL("CREATE TABLE IF NOT EXISTS "+UnisonDatabaseHelper.DAILY_AREA_TABLE
														+"("+UnisonDatabaseHelper.AREA+" TEXT );");
			
			sqlDB.execSQL("PRAGMA encoding = \"UTF-8MB4\"");
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		
//		Table for TodayArea(Tour Plan)
		try
		{
			//default character set utf8mb4
			sqlDB.execSQL("CREATE TABLE IF NOT EXISTS "+UnisonDatabaseHelper.Today_AREA_TABLE
														+"("+UnisonDatabaseHelper.AREA+" TEXT );");
			
			sqlDB.execSQL("PRAGMA encoding = \"UTF-8MB4\"");
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		
//		Table for DoctorDetails
		try
		{
			//default character set utf8mb4
			sqlDB.execSQL("CREATE TABLE IF NOT EXISTS "+UnisonDatabaseHelper.DOCTOR_DETAILS
													   +"("+UnisonDatabaseHelper.DOCTOR+" TEXT , "
														   +UnisonDatabaseHelper.DOCTOR_CODE+" TEXT , "
														   +UnisonDatabaseHelper.AREA+" TEXT , "
														   +UnisonDatabaseHelper.CITY+" TEXT , "
														   +UnisonDatabaseHelper.DOCTORBUSINESS+" TEXT , "
														   +UnisonDatabaseHelper.DOCTORTYPE+" TEXT , "
													   	   +UnisonDatabaseHelper.SPECIALITY+" TEXT );");
			
			sqlDB.execSQL("PRAGMA encoding = \"UTF-8MB4\"");
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		
//		Table for ProductDetails
		try
		{
			//default character set utf8mb4
			sqlDB.execSQL("CREATE TABLE IF NOT EXISTS "+UnisonDatabaseHelper.PRODUCT_DETAILS
													   +"("+UnisonDatabaseHelper.PRODUCT+" TEXT , "
													       +UnisonDatabaseHelper.PRODUCT_TYPE+" TEXT , "
													       +UnisonDatabaseHelper.PRODUCT_ISFORNEXTMONTH+" TEXT , "
													   	   +UnisonDatabaseHelper.PRODUCT_ID+" TEXT );");
			
			sqlDB.execSQL("PRAGMA encoding = \"UTF-8MB4\"");
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		
//		Table for ProductDetails for next month
		try
		{
			//default character set utf8mb4
			sqlDB.execSQL("CREATE TABLE IF NOT EXISTS "+UnisonDatabaseHelper.PRODUCT_DETAILS_NEXT_MONTH
													   +"("+UnisonDatabaseHelper.PRODUCT+" TEXT , "
													       +UnisonDatabaseHelper.PRODUCT_TYPE+" TEXT , "
													   	   +UnisonDatabaseHelper.PRODUCT_ID+" TEXT );");
			
			sqlDB.execSQL("PRAGMA encoding = \"UTF-8MB4\"");
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		
//		Table for Reason
		try
		{
			//default character set utf8mb4
			sqlDB.execSQL("CREATE TABLE IF NOT EXISTS "+UnisonDatabaseHelper.REASON_DETAILS
													   +"("+UnisonDatabaseHelper.REASON+" TEXT , "
													   	   +UnisonDatabaseHelper.REASON_CODE+" TEXT );");
			
			sqlDB.execSQL("PRAGMA encoding = \"UTF-8MB4\"");
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		
//		Table for DBC Code
		try
		{
			//default character set utf8mb4
			sqlDB.execSQL("CREATE TABLE IF NOT EXISTS "+UnisonDatabaseHelper.DBC_CODE_DETAILS
													   +"("+UnisonDatabaseHelper.DBC+" TEXT , "
													   	   +UnisonDatabaseHelper.DBC_CODE+" TEXT );");
			
			sqlDB.execSQL("PRAGMA encoding = \"UTF-8MB4\"");
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		
//		Table for Work With
		try
		{
			//default character set utf8mb4
			sqlDB.execSQL("CREATE TABLE IF NOT EXISTS "+UnisonDatabaseHelper.WORK_WITH_DETAILS
													   +"("+UnisonDatabaseHelper.WORK_WIH+" TEXT , "
													   	   +UnisonDatabaseHelper.WORK_WIH_CODE+" TEXT );");
			
			sqlDB.execSQL("PRAGMA encoding = \"UTF-8MB4\"");
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		
//		Table for Advice For
		try
		{
			//default character set utf8mb4
			sqlDB.execSQL("CREATE TABLE IF NOT EXISTS "+UnisonDatabaseHelper.ADVICE_FOR_DETAILS
													   +"("+UnisonDatabaseHelper.ADVICE_FOR_ID+" TEXT , "
													   	   +UnisonDatabaseHelper.ADVICE_FOR_NAME+" TEXT );");
			
			sqlDB.execSQL("PRAGMA encoding = \"UTF-8MB4\"");
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		
//		Table for DoctorByToday
		try
		{
			//default character set utf8mb4
			sqlDB.execSQL("CREATE TABLE IF NOT EXISTS "+UnisonDatabaseHelper.DOCTOR_BY_TODAY
													   +"("+UnisonDatabaseHelper.DATE+" TEXT , "
													   	   +UnisonDatabaseHelper.DBC_CODE+" TEXT , "
													   	   +UnisonDatabaseHelper.DOCTOR_CODE+" TEXT );");
			
			sqlDB.execSQL("PRAGMA encoding = \"UTF-8MB4\"");
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}

	/*@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) 
	{
		android.util.Log.v("Constants", "Upgrading Database, which will destory all old Data");
		
		Log.d(TAG, "onUpgrade() from " + oldVersion + " to " + newVersion);

	    int version = oldVersion;

	    Log.d(TAG, "after upgrade logic, at version " + version);
	    if (version != DATABASE_VERSION)
	    {
	        Log.w(TAG, "Destroying old data during upgrade");

			db.execSQL("DROP TABLE IF EXISTS "+DAILY_ENTRY_TABLE);
			db.execSQL("DROP TABLE IF EXISTS "+DAILY_AREA_TABLE);
	        // ... delete all your tables ...

	        onCreate(db);
	     }
	}*/
	
	// Upgrading database
	   @Override
	   public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
	   {
	       db.execSQL("CREATE TABLE DailyEntryTableTemp "
					+"("+UnisonDatabaseHelper.ID+" INTEGER PRIMARY KEY , "
					    +UnisonDatabaseHelper.DATE+" TEXT , "
						+UnisonDatabaseHelper.AREA+" TEXT , "
						+UnisonDatabaseHelper.DBC+" TEXT , "
						+UnisonDatabaseHelper.DBC_CODE+" TEXT , "
						+UnisonDatabaseHelper.WORK_WIH+" TEXT , "
						+UnisonDatabaseHelper.WORK_WIH_CODE+" TEXT , "
						+UnisonDatabaseHelper.ADVICE_FOR_ID+" TEXT , "
						+UnisonDatabaseHelper.ADVICE_FOR_NAME+" TEXT , "
						+UnisonDatabaseHelper.DOCTOR+" TEXT , "
						+UnisonDatabaseHelper.DOCTOR_CODE+" TEXT , "
//						+UnisonDatabaseHelper.SPECIALITY+" TEXT , "
						+UnisonDatabaseHelper.NEW_DOCTOR_AMOUNT+" TEXT , "
						+UnisonDatabaseHelper.NEW_DOCTOR_CODE+" TEXT , "
						+UnisonDatabaseHelper.REMARKS+" TEXT , "
						+UnisonDatabaseHelper.INTERNEE+" TEXT , "
						+UnisonDatabaseHelper.DDTDATE+" TEXT , "
						+UnisonDatabaseHelper.ISNEWCYCLE+" TEXT , "
						+UnisonDatabaseHelper.PRODUCT+" TEXT , "
						+UnisonDatabaseHelper.REASON+" TEXT , "
						+UnisonDatabaseHelper.UNIT+" TEXT , "
						+UnisonDatabaseHelper.API_STRING+" TEXT ,"
				   +UnisonDatabaseHelper.FOCUS_FOR_STRING+" TEXT);");

	       // Create an temporaty table that can store data of older version
	     
	       db.execSQL("INSERT INTO DailyEntryTableTemp SELECT " + ID + ", "
	         +  DATE + ", " +  AREA + ", " +  DBC + ", " +  DBC_CODE + ", " +  WORK_WIH + ", " + WORK_WIH_CODE + ", " + ADVICE_FOR_ID + ", " +  ADVICE_FOR_NAME +
	         ", " +  DOCTOR +
	         ", " +  DOCTOR_CODE +
//	         ", " +  SPECIALITY +
	         ", " +  NEW_DOCTOR_AMOUNT +
	         ", " +  NEW_DOCTOR_CODE +
	         ", " +  REMARKS +
	         ", " +  INTERNEE +
	         ", " +  DDTDATE +
	         ", " +  ISNEWCYCLE +
	         ", " +  PRODUCT +
	         ", " +  REASON +
	         ", " +  UNIT +
	         ", " +  API_STRING +
			", " +  FOCUS_FOR_STRING +" FROM " + DAILY_ENTRY_TABLE);

	       // Insert data into temporary table from existing older version database (that doesn't contains email //column)
	 
	       db.execSQL("DROP TABLE DailyEntryTable");
	       // Remove older version database table
	       
	       db.execSQL("CREATE TABLE " + DAILY_ENTRY_TABLE
					+"("+UnisonDatabaseHelper.ID+" INTEGER PRIMARY KEY , "
					    +UnisonDatabaseHelper.DATE+" TEXT , "
						+UnisonDatabaseHelper.AREA+" TEXT , "
						+UnisonDatabaseHelper.DBC+" TEXT , "
						+UnisonDatabaseHelper.DBC_CODE+" TEXT , "
						+UnisonDatabaseHelper.WORK_WIH+" TEXT , "
						+UnisonDatabaseHelper.WORK_WIH_CODE+" TEXT , "
						+UnisonDatabaseHelper.ADVICE_FOR_ID+" TEXT , "
						+UnisonDatabaseHelper.ADVICE_FOR_NAME+" TEXT , "
						+UnisonDatabaseHelper.DOCTOR+" TEXT , "
						+UnisonDatabaseHelper.DOCTOR_CODE+" TEXT , "
						+UnisonDatabaseHelper.SPECIALITY+" TEXT , "
						+UnisonDatabaseHelper.NEW_DOCTOR_AMOUNT+" TEXT , "
						+UnisonDatabaseHelper.NEW_DOCTOR_CODE+" TEXT , "
						+UnisonDatabaseHelper.REMARKS+" TEXT , "
						+UnisonDatabaseHelper.INTERNEE+" TEXT , "
						+UnisonDatabaseHelper.DDTDATE+" TEXT , "
						+UnisonDatabaseHelper.ISNEWCYCLE+" TEXT , "
						+UnisonDatabaseHelper.PRODUCT+" TEXT , "
						+UnisonDatabaseHelper.REASON+" TEXT , "
						+UnisonDatabaseHelper.UNIT+" TEXT , "
						+UnisonDatabaseHelper.API_STRING+" TEXT,"
				   +UnisonDatabaseHelper.FOCUS_FOR_STRING+" TEXT);");

	       db.execSQL("INSERT INTO " + DAILY_ENTRY_TABLE + " SELECT " +  ID + 
	    		 ", " +  DATE +
	    		 ", " +  AREA +
	    		 ", " +  DBC +
	    		 ", " +  DBC_CODE +
	    		 ", " +  WORK_WIH +
	    		 ", " + WORK_WIH_CODE +
	    		 ", " + ADVICE_FOR_ID +
	    		 ", " +  ADVICE_FOR_NAME +
	  	         ", " +  DOCTOR +
	  	         ", " +  DOCTOR_CODE +
	  	         ", " +  null +
	  	         ", " +  NEW_DOCTOR_AMOUNT +
	  	         ", " +  NEW_DOCTOR_CODE +
	  	         ", " +  REMARKS +
	  	         ", " +  INTERNEE +
	  	         ", " +  DDTDATE +
	  	         ", " +  ISNEWCYCLE +
	  	         ", " +  PRODUCT +
	  	         ", " +  REASON +
	  	         ", " +  UNIT +
	  	         ", " +  API_STRING+
				   ", "+FOCUS_FOR_STRING
	  	         +  " FROM DailyEntryTableTemp");
	       // Insert data ffrom temporary table that doesn't have email column so left it that column name as null.     
	      
	       db.execSQL("DROP TABLE DailyEntryTableTemp");
	   }
}
