package com.example.diary.app;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class SQLiteHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "user.db";

    public static final String TABLE_NAME="UserTable";
    public static final String Table_Column_ID="id";
    public static final String Table_Column_1_Name="name";
    public static final String Table_Column_2_Email="email";
    public static final String Table_Column_3_Password="password";

    public static final String DIARY = "Diary";
    public static final String DIARY_COL_1 = "id";
    public static final String DIARY_COL_2 = "subject";
    public static final String DIARY_COL_3 = "description";
    public static final String DIARY_COL_4 = "dateTime";
    public static final String DIARY_COL_5 = "userId";

    //this constructor for creating the database
    public SQLiteHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
        SQLiteDatabase db = this.getWritableDatabase();
    }

    //creating table
    @Override
    public void onCreate(SQLiteDatabase database) {
        database.execSQL("CREATE TABLE IF NOT EXISTS "+TABLE_NAME+
                " ("+Table_Column_ID+" INTEGER PRIMARY KEY AUTOINCREMENT, "+Table_Column_1_Name+" VARCHAR, "+
                Table_Column_2_Email+" VARCHAR, "+Table_Column_3_Password+" VARCHAR)");

        database.execSQL("CREATE TABLE " + DIARY +
                " ("+DIARY_COL_1+" INTEGER PRIMARY KEY AUTOINCREMENT," +
                DIARY_COL_2+" TEXT,"+DIARY_COL_3+" TEXT,"+DIARY_COL_4+" Date, "+DIARY_COL_5+" REFERENCES "+TABLE_NAME+"("+Table_Column_ID+"))");

        database.execSQL("INSERT INTO " + TABLE_NAME + " " +
                "("+Table_Column_1_Name+", "+Table_Column_2_Email+", "+Table_Column_3_Password+") " +
                "VALUES('root', 'root@root.com', 'root');");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + DATABASE_NAME);//drop table if exist
        onCreate(sqLiteDatabase); //and create new table
    }

    //function for inserting on sqlite database
    public long insertData(String subject, String description, String dateTime) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();//for accessing database data
        ContentValues contentValues = new ContentValues();
        contentValues.put(DIARY_COL_2,subject);
        contentValues.put(DIARY_COL_3, description);
        contentValues.put(DIARY_COL_4, dateTime);
        contentValues.put(DIARY_COL_5, 1);
        long id = sqLiteDatabase.insert(DIARY, null, contentValues);
        return id;
    }
    public Cursor display(){
        SQLiteDatabase sqliteDatabase = this.getWritableDatabase();//for accessing database data
        Cursor cursor = sqliteDatabase.rawQuery("SELECT * FROM "+ DIARY, null);
        return cursor;
    }
    //for updating database data
    public boolean update(String subject,String description,String dateTime,String id){
        try{
            SQLiteDatabase sqliteDatabase = this.getWritableDatabase();
            ContentValues contentValues = new ContentValues();
            contentValues.put(DIARY_COL_1,id);
            contentValues.put(DIARY_COL_2,subject);
            contentValues.put(DIARY_COL_3, description);
            contentValues.put(DIARY_COL_4, dateTime);
            contentValues.put(DIARY_COL_5, 1);
            sqliteDatabase.update(DIARY,contentValues, DIARY_COL_1 +" =?", new String[]{id});
            return true;
        }
        catch (Exception e){
            return false;
        }
    }
    //for deleting database data
    public boolean delete(String id){
        SQLiteDatabase sqliteDatabase = this.getWritableDatabase();
        sqliteDatabase.delete(DIARY, DIARY_COL_1 +" = ?",new String[]{id});
        return  true;
    }


}
