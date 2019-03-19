package android.example.myapplication;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private Button Login,CreateNew;
    private EditText edit_username;
    private EditText edit_pwd;
    private String email, pwd;
    private DBHelper dbHelper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.layout_register);
        setContentView(R.layout.layout_login);
        //setContentView(R.layout.activity_main);
        Login = (Button)findViewById(R.id.btnLogin);
        CreateNew = (Button)findViewById(R.id.btnLinkToRegisterScreen);
        edit_username = (EditText)findViewById(R.id.login_username);
        edit_pwd = (EditText)findViewById(R.id.login_password);

        dbHelper = new DBHelper(this, "UserData.db", null, 1);

        edit_username.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    edit_username.clearFocus();
                }
                return false;
            }
        });
        edit_pwd.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    edit_pwd.clearFocus();
                    InputMethodManager imm =
                            (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(edit_pwd.getWindowToken(), 0);
                }
                return false;
            }
        });
        Login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (edit_username.getText().toString().trim().equals("") | edit_pwd.getText().
                        toString().trim().equals("")) {
                    Toast.makeText(MainActivity.this, "请输入账号或者注册账号！", Toast.LENGTH_SHORT).show();
                } else {
                    readUserInfo();
                }
                //Intent intent = new Intent(MainActivity.this, LoginSuccessful.class);
                //startActivity(intent);
                //finish();
            }
        });
        CreateNew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, Register.class);
                startActivity(intent);
            }
        });
    }

    /**
     * 读取SharedPreferences存储的键值对
     * */
    /*public void readUsersInfo(){
        SharedPreferences sharedPreferences = getSharedPreferences("UsersInfo",MODE_PRIVATE);
        email = sharedPreferences.getString("username","");
        pwd = sharedPreferences.getString("password","");
    }*/
    /**
     * 读取UserData.db中的用户信息
     * */
    protected void readUserInfo() {
        if (login(edit_username.getText().toString(), edit_pwd.getText().toString())) {
            Toast.makeText(this, "登陆成功！", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(MainActivity.this, LoginSuccessful.class);
            //intent.putExtra("Username",edit_account.getText().toString());
            startActivity(intent);
        } else {
            Toast.makeText(this, "账户或密码错误，请重新输入！！", Toast.LENGTH_SHORT).show();
        }
    }
    /**
     * 验证登录信息
     * */
    public boolean login(String username, String password) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        String sql = "Select * from Userinfo where username=? and userpassword=?";
        Cursor cursor = db.rawQuery(sql, new String[]{username, password});
        if (cursor.moveToFirst()) {
            cursor.close();
            return true;
        }
        return false;
    }
}
