package nerdydog.domoHomeProd.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.util.Log;

class toDoDBOpenHelper extends SQLiteOpenHelper {
	public toDoDBOpenHelper(Context context, String name,
		CursorFactory factory, int version) {
		super(context, name, factory, version);
	}
	
	@Override
	public void onCreate(SQLiteDatabase _db) {
		Log.i("HELPER","CREATE");
		_db.execSQL(ConfDatabase.DATABASE_CREATE_TABLE_ACTUATOR);
		_db.execSQL(ConfDatabase.DATABASE_CREATE_TABLE_ACTION);
		_db.execSQL(ConfDatabase.DATABASE_CREATE_TABLE_COUNTER);
	}
	
	@Override
	public void onUpgrade(SQLiteDatabase _db, int _oldVersion, int _newVersion) {
		Log.w("TaskDBAdapter", "Upgrading from version " +
		_oldVersion + " to " +
		_newVersion + ", which will destroy all old data");
		// Drop the old table.
		_db.execSQL("DROP TABLE IF EXISTS " + ConfDatabase.DATABASE_CREATE_TABLE_ACTUATOR);
		_db.execSQL("DROP TABLE IF EXISTS " + ConfDatabase.DATABASE_CREATE_TABLE_ACTION);
		_db.execSQL("DROP TABLE IF EXISTS " + ConfDatabase.DATABASE_CREATE_TABLE_COUNTER);
		// Create a new one.
		onCreate(_db);
	}
}
