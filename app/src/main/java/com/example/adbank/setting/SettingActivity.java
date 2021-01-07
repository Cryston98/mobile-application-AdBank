package com.example.adbank.setting;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import com.example.adbank.HomeActivity;
import com.example.adbank.MainActivity;
import com.example.adbank.R;

public class SettingActivity extends AppCompatActivity {
        LinearLayout logout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        initialize();
        onLogout();
    }

    void initialize(){
        logout=findViewById(R.id.logoutSide);
    }

    void onLogout(){
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SharedPreferences sharedPreferences= getSharedPreferences(String.valueOf(R.string.db_pref_adbank),MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString(String.valueOf(R.string.key_pref_login_status),"0");
                editor.putString(String.valueOf(R.string.key_pref_login_username),"");
                editor.putString(String.valueOf(R.string.key_pref_login_password),"");
                editor.apply();
                Intent i = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(i);
                finish();
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent i = new Intent(SettingActivity.this,HomeActivity.class);
        finish();
        overridePendingTransition(0, 0);
        startActivity(i);
        overridePendingTransition(0, 0);
    }
}