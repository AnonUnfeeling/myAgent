package a.a.a.myagent;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class DataBase extends SQLiteOpenHelper {

    public DataBase(Context context){
        super(context,"users",null,2);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String create ="CREATE TABLE users (login TEXT, psw TEXT)";
        db.execSQL(create);
    }

    public void addLoginAndPassword(DataDB dataDB){

        ContentValues contentValues = new ContentValues();
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();

        contentValues.put("login",dataDB.getLogin());
        contentValues.put("psw", dataDB.getPass());

        sqLiteDatabase.insert("users", null, contentValues);
        sqLiteDatabase.close();
    }

    public List<DataDB> getAllUsers() {
        List<DataDB> users = new ArrayList<>();
        String selectQuery = "SELECT  * FROM users";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                DataDB data = new DataDB();
                data.setLogin(cursor.getString(0));
                data.setPass(cursor.getString(1));
                users.add(data);
            } while (cursor.moveToNext());
        }
        db.close();
        cursor.close();
        return users;
    }

    public void deleteAll() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete("users", null, null);
        db.close();
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS users");
        onCreate(db);
    }
}
