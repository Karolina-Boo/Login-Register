package android.example.myapplication;

import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.Spanned;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class Register extends AppCompatActivity implements View.OnClickListener {

    private Button Register,LoginScreen;
    private EditText edit_name,edit_email;
    private EditText edit_pwd;
    private DBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_register);
        init();
        dbHelper = new DBHelper(this, "UserData.db", null, 1);

        /*Register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(Register.this, "Register successful", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(Register.this, MainActivity.class);
                startActivity(intent);
            }
        });
        LoginScreen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Register.this, MainActivity.class);
                startActivity(intent);
            }
        });*/
    }

    protected void init(){
        edit_name = (EditText)findViewById(R.id.register_name);
        edit_name.setFilters(new InputFilter[]{
                new InputFilter() {
                    @Override
                    public CharSequence filter(CharSequence source, int start, int end,
                                               Spanned dest, int dstart, int dend) {
                        for (int i = start; i < end; i++) {
                            if (!Character.isLetterOrDigit(source.charAt(i)) &&
                                    !Character.toString(source.charAt(i)).equals("_")) {
                                Toast.makeText(Register.this, "只能使用'_'、字母、数字、汉字注册！", Toast.LENGTH_SHORT).show();
                                return "";
                            }
                        }
                        return null;
                    }
                }
        });
        edit_name.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    edit_name.clearFocus();
                    InputMethodManager imm =
                            (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(edit_name.getWindowToken(), 0);
                }
                return false;
            }
        });
        edit_email = (EditText)findViewById(R.id.register_email);
        edit_email.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    edit_email.clearFocus();
                    InputMethodManager imm =
                            (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(edit_email.getWindowToken(), 0);
                }
                return false;
            }
        });
        edit_pwd = (EditText)findViewById(R.id.register_password);
        edit_pwd.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    String s = v.getText().toString();
                    //设置密码长度有问题，判断editText的输入长度需要重新理解
                    System.out.println(" v: ****** v :"+ s.length());
                    if (s.length() >= 6) {
                        System.out.println(" ****** s :"+ s.length());
                        edit_pwd.clearFocus();
                        InputMethodManager imm =
                                (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(edit_pwd.getWindowToken(), 0);
                    } else {
                        Toast.makeText(Register.this, "密码设置最少为6位！", Toast.LENGTH_SHORT).show();
                    }
                }
                return false;
            }
        });
        Register = (Button)findViewById(R.id.btnRegister);
        Register.setOnClickListener(this);
        LoginScreen = (Button)findViewById(R.id.btnLinkToLoginScreen);
        LoginScreen.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnRegister:
                if ( CheckIsDataAlreadyInDBorNot(edit_name.getText().toString())) {
                    Toast.makeText(this, "该邮箱已被注册，注册失败", Toast.LENGTH_SHORT).show();
                } else
                    {
                        registerUserInfo(edit_name.getText().toString(),edit_email.getText().toString(),
                                edit_pwd.getText().toString());
                        Toast.makeText(this, "注册成功！", Toast.LENGTH_SHORT).show();
                        Intent register_intent = new Intent(Register.this,
                                MainActivity.class);
                        startActivity(register_intent);
                    }
                break;
            case R.id.btnLinkToLoginScreen:
                Intent login_intent = new Intent(Register.this, MainActivity.class);
                startActivity(login_intent);
                break;
            default:
                break;
        }
    }

        /**
         * 利用sql创建嵌入式数据库进行注册访问
         */
        private void registerUserInfo(String username, String useremail,String userpwd) {
            SQLiteDatabase db = dbHelper.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put("username", username);
            values.put("useremail",useremail);
            values.put("userpassword", userpwd);
            db.insert("Userinfo", null, values);
            db.close();
        }

    /**
     * 检验用户名是否已经注册
     */
    public boolean CheckIsDataAlreadyInDBorNot(String value) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        String Query = "Select * from Userinfo where username =?";
        Cursor cursor = db.rawQuery(Query, new String[]{value});
        if (cursor.getCount() > 0) {
            cursor.close();
            return true;
        }
        cursor.close();
        return false;
    }

    /**
     * 利用SharedPreferences进行默认登陆设置
     */
    /*private void saveUsersInfo() {
        SharedPreferences sharedPreferences = getSharedPreferences("UsersInfo", MODE_APPEND);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("username", edit_name.getText().toString());
        //判断注册时的两次密码是否相同
        if (edit_setpassword.getText().toString().equals(edit_resetpassword.getText().toString())) {
            editor.putString("password", edit_setpassword.getText().toString());
        }
        editor.commit();
    }*/
}
