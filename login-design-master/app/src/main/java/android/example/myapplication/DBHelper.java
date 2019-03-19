package android.example.myapplication;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

public class DBHelper extends SQLiteOpenHelper {

    public static final String CREATE_USERINFO = "create table Userinfo ("
            + "id integer primary key autoincrement, "
            + "username text, "
            + "useremail text, "
            + "userpassword text)";
    private final static int DATABASE_VERSION = 1;
    private final static String DATABASE_NAME = "UserData.db";
    private Context mContext;
    public DBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        mContext = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //创建表
        //String usersInfo_table = "create table usertable" +
               // "(id integer primary key autoincrement, username text," + "useremail text," + "password text)";
        db.execSQL(CREATE_USERINFO);
        Toast.makeText(mContext,"Create succeeded",Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("alter table usertable add column other string");
    }
}