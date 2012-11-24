package org.personalfinance.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBManager extends SQLiteOpenHelper {
	
	public static final String TABLE_OUTCOME = "OUTCOME";
	public static final String COLUMN_ID = "Id";
	public static final String COLUMN_Gasto = "Gasto";
	public static final String COLUMN_Nota = "Nota";
	public static final String COLUMN_Categoria = "Categoria";
	public static final String COLUMN_LocalizacionValida = "LocalizacionValida";
	public static final String COLUMN_Fecha = "Fecha";
	public static final String COLUMN_Longitud = "Longitud";
	public static final String COLUMN_Latitud = "Latitud";
	

	private static final String DATABASE_NAME = "transactions.db";
	private static final int DATABASE_VERSION = 1;
	
	
	public static final String TABLE_INCOME = "INCOME";
	public static final String COLUMN_GANANCIA = "Ganacia";
	
	// Database creation sql for Outcome table
	  private static final String DATABASE_CREATE_OUTCOME = "CREATE TABLE "
	      + TABLE_OUTCOME + " (" 
		  + COLUMN_ID    + " INTEGER PRIMARY KEY AUTOINCREMENT, "
		  + COLUMN_Nota + " Text NOT NULL,"
		  + COLUMN_Fecha + " Text NOT NULL,"
	      + COLUMN_Categoria + " INTEGER NOT NULL,"
	      + COLUMN_LocalizacionValida + " INTEGER NOT NULL,"
	      + COLUMN_Longitud + " REAL NOT NULL,"
	  	  + COLUMN_Latitud + " REAL NOT NULL,"
	      + COLUMN_Gasto + " REAL NOT NULL);";
	  
	  
	// Database creation sql for Income table
		  private static final String DATABASE_CREATE_INCOME = "CREATE TABLE "
		      + TABLE_INCOME + " (" 
			  + COLUMN_ID    + " INTEGER PRIMARY KEY AUTOINCREMENT, "
			  + COLUMN_Nota + " Text NOT NULL,"
			  + COLUMN_Fecha + " Text NOT NULL,"
		      + COLUMN_GANANCIA + " REAL NOT NULL);";
	 

	public DBManager(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}
	
	@Override
	public void onCreate(SQLiteDatabase database) {
		
		database.execSQL(DATABASE_CREATE_OUTCOME);
		database.execSQL(DATABASE_CREATE_INCOME);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
	
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_INCOME);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_OUTCOME);
		onCreate(db);
	}

}
