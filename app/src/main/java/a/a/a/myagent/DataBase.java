package a.a.a.myagent;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import java.util.ArrayList;
import java.util.List;

class DataBase extends SQLiteOpenHelper {

    public DataBase(Context context){
        super(context, "users", null, 3);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String create ="CREATE TABLE users  login TEXT, psw TEXT, length INT)";
        db.execSQL(create);
    }

    public DB getUser(String login){
        SQLiteDatabase sql = this.getReadableDatabase();

        Cursor cursor = sql.query("users",new String[]{"login","psw"},"login =?",
                new String[]{login},null,null,null,null);
        if(cursor!=null){
            cursor.moveToFirst();
        }

        DB db = new DB(cursor.getString(1),cursor.getString(2));

        return db;
    }

    public int update(String login,int length) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("length", length);

        return db.update("users", values, "login" + "= ?",new String[]{login});
    }

    public void addLoginAndPassword(DB DB){
        if(checkingLogin(DB.getLogin(),DB.getPass(),DB.getSize())){
            ContentValues contentValues = new ContentValues();
            SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();

            contentValues.put("login", DB.getLogin());
            contentValues.put("psw", DB.getPass());
            contentValues.put("length", DB.getSize());

            sqLiteDatabase.insert("users", null, contentValues);
            sqLiteDatabase.close();
        }
    }

    public boolean checkingLogin(String login, String pass, int length){
        List<DB> list = getAllUsers();

        for (int i = 0; i < list.size(); i++) {
            if(list.get(i).getLogin().equals(login)&&list.get(i).getPass().equals(pass)&&list.get(i).getSize()!=length){
                update(login,length);
                System.out.println("updata");
                return false;
            }
            if(list.get(i).getLogin().equals(login)&&list.get(i).getPass().equals(pass)) return false;
        }

        return true;
    }

    public List<DB> getAllUsers() {
        List<DB> users = new ArrayList<>();
        String selectQuery = "SELECT  * FROM users";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                DB data = new DB();
                data.setLogin(cursor.getString(1));
                data.setPass(cursor.getString(2));
                data.setSize(cursor.getInt(3));
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
