package com.example.adbank;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    TextView to_register,to_login;
    EditText email,password,IdEmailReg,IdPassReg,IdConfPassReg,IdUsername;
    ScrollView scrollView;
    Button login_btn,register_btn;
    LinearLayout loginPage,registerPage;
    static String resultLogin;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        SharedPreferences sharedPreferences= getSharedPreferences(String.valueOf(R.string.db_pref_adbank),MODE_PRIVATE);
        String isLogin=sharedPreferences.getString(String.valueOf(R.string.key_pref_login_status),"0");
        if (isLogin.equals("1"))
        {
            Intent myIntent=new Intent(MainActivity.this,HomeActivity.class);
            startActivity(myIntent);
            finish();
        }

        initialize();


        OnClickInput();
        onClickLogin();
        createAccount();
        singUpAccount();
        onClickRegister();

    }

    void initialize(){
        email=findViewById(R.id.IdEmail);
        password=findViewById(R.id.IdPassword);
        scrollView=findViewById(R.id.myScrollView);
        login_btn=findViewById(R.id.login_btn);
        register_btn=findViewById(R.id.register_btn);
        to_register=findViewById(R.id.toRegisterPage);
        to_login=findViewById(R.id.toLoginPage);
        loginPage=findViewById(R.id.logSide);
        registerPage=findViewById(R.id.registerSide);
        IdEmailReg=findViewById(R.id.IdEmailReg);
        IdPassReg=findViewById(R.id.IdPasswordReg);
        IdConfPassReg=findViewById(R.id.IdConfPasswordReg);
        IdUsername=findViewById(R.id.username);

    }

    public void createAccount(){
        to_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loginPage.setVisibility(View.GONE);
                registerPage.setVisibility(View.VISIBLE);
            }
        });
    }
    public void singUpAccount(){
        to_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loginPage.setVisibility(View.VISIBLE);
                registerPage.setVisibility(View.GONE);
            }
        });
    }
    public void OnClickInput(){
        email.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                scrollView.scrollBy(0,scrollView.getBottom());
            }
        });
        password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                scrollView.scrollBy(0,scrollView.getBottom());
            }
        });
        IdConfPassReg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                scrollView.scrollBy(0,scrollView.getBottom());
            }
        });
        IdPassReg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                scrollView.scrollBy(0,scrollView.getBottom());
            }
        });
        IdEmailReg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                scrollView.scrollBy(0,scrollView.getBottom());
            }
        });
        IdUsername.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                scrollView.scrollBy(0,scrollView.getBottom());
            }
        });
    }
    public void onClickLogin(){
        login_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String emailString = email.getText().toString();
                String passwordString = password.getText().toString();
                String myURL="https://lifegame.ro/adBank/login.php";
                String qry="?user_email="+emailString.trim()+"&password="+passwordString.trim();
                class dbprocess extends AsyncTask<String,Void,String>
                {
                    @Override
                    protected void onPostExecute(String data){
                        if(data.equals("OK"))
                        {
                            SharedPreferences sharedPreferences = getSharedPreferences(String.valueOf(R.string.db_pref_adbank),MODE_PRIVATE);
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putString(String.valueOf(R.string.key_pref_login_status),"1");
                            editor.putString(String.valueOf(R.string.key_pref_login_email),emailString);
                            editor.putString(String.valueOf(R.string.key_pref_login_password),passwordString);
                            editor.apply();
                            Intent myIntent=new Intent(MainActivity.this,HomeActivity.class);
                            startActivity(myIntent);
                            finish();
                        }else{
                            Toast.makeText(MainActivity.this,"Failed ! "+data,Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    protected String doInBackground(String... params)
                    {
                        String urlText=params[0];
                        try{
                            URL url=new URL(urlText);
                            HttpURLConnection conn = (HttpURLConnection)url.openConnection();
                            BufferedReader br=new BufferedReader(new InputStreamReader(conn.getInputStream()));
                            return br.readLine();
                        }catch(Exception ex){
                            return ex.getMessage();
                        }
                    }
                }

                    dbprocess obj  = new dbprocess();
                    obj.execute(myURL+qry);
            }
        });
    }
    public void onClickRegister(){
        register_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                String emailString = IdEmailReg.getText().toString();
                String passwordString = IdPassReg.getText().toString();
                String confPassString = IdConfPassReg.getText().toString();
                String usernameString = IdUsername.getText().toString();

                if (passwordString.equals(confPassString) == false){
                    Toast.makeText(MainActivity.this, "Confirmarea Parolei este gresita ! ", Toast.LENGTH_SHORT).show();
                    IdConfPassReg.setText("");
                }else{
                    String myURL = "https://lifegame.ro/adBank/register.php";

                    String qry = "?user_email=" + emailString.trim() + "&password=" + passwordString.trim() + "&username=" + usernameString.trim();
                    class dbprocess extends AsyncTask<String, Void, String> {
                        @Override
                        protected void onPostExecute(String data) {
                            if (data.equals("OK")) {
                                SharedPreferences sharedPreferences = getSharedPreferences(String.valueOf(R.string.db_pref_adbank), MODE_PRIVATE);
                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                editor.putString(String.valueOf(R.string.key_pref_login_status), "1");
                                editor.putString(String.valueOf(R.string.key_pref_login_email), emailString);
                                editor.putString(String.valueOf(R.string.key_pref_login_password), passwordString);
                                editor.apply();
                                Intent myIntent = new Intent(MainActivity.this, HomeActivity.class);
                                startActivity(myIntent);
                                finish();
                            } else {
                                Toast.makeText(MainActivity.this, "" + data, Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        protected String doInBackground(String... params) {
                            String urlText = params[0];
                            try {
                                URL url = new URL(urlText);
                                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                                BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                                return br.readLine();
                            } catch (Exception ex) {
                                return ex.getMessage();
                            }
                        }
                    }
                    dbprocess objRegister = new dbprocess();
                    objRegister.execute(myURL + qry);
                }
            }
        });
    }
    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        /*if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT && hasFocus) {
            getWindow().getDecorView().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE |
                            View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION |
                            View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION |
                            View.SYSTEM_UI_FLAG_FULLSCREEN |
                            View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        }*/
    }


}