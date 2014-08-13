package com.lzz.position;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {

	final String CREAT_TABLE_SQL = 
			"create table position_info(_id integer primary key autoincrement,"
			+ "positionx varchar(10),"
			+ "positiony varchar(10),"
			+ "accuracy varchar(5),"
			+ "time varchar(15),"
			+ "note varchar(50),"
			+ "picPath varchar(100))";
	
	public DBHelper(Context context, String name, int version) {
		super(context, name, null, version);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		db.execSQL(CREAT_TABLE_SQL);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub

	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}