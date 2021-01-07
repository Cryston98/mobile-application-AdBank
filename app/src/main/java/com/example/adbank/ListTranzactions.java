package com.example.adbank;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class ListTranzactions extends AppCompatActivity {

    String accountAdBank;
    List<ModelTranzaction> listTranz;
    ModelTranzactionAdapter adapter;

    RecyclerView recyclerView;
    Dialog confirmTranzactionDialog;
    //toast config
    LayoutInflater inflater;
    View layout;
    Toast toast;
    TextView toastText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_tranzactions);


        inflater = getLayoutInflater();
        layout = inflater.inflate(R.layout.custom_toast, (ViewGroup)findViewById(R.id.toast_layout));
        toast=new Toast(getApplicationContext());

        toastText=layout.findViewById(R.id.textView2_toast);
        toast.setGravity(Gravity.BOTTOM,0,150);
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.setView(layout);

        SharedPreferences sharedPreferences = getSharedPreferences(String.valueOf(R.string.db_pref_adbank), MODE_PRIVATE);
        accountAdBank = sharedPreferences.getString(String.valueOf(R.string.key_pref_login_email), "null");


        recyclerView = (RecyclerView) findViewById(R.id.recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new ModelTranzactionAdapter(accountAdBank);
        recyclerView.setAdapter(adapter);

        getTranzaction();
    }

    public void getTranzaction() {

        String myURL = "https://lifegame.ro/adBank/getHistoryTranzactionsAll.php";
        String qry = "?user_email=" + accountAdBank.trim();
        class dbprocess extends AsyncTask<String, Void, String> {
            @Override
            protected void onPostExecute(String data) {
                listTranz= new ArrayList<>();
                if (!data.equals("NaN") &&  !data.equals("NoData")) {
                    String[] tranzactions = data.split("!");
                    for (String itemTranz : tranzactions) {
                        String[] modelTranz = itemTranz.split("-");
                        Log.d("TRANZ",modelTranz[1]);
                        listTranz.add(new ModelTranzaction(modelTranz[0], modelTranz[1], modelTranz[2], modelTranz[3], modelTranz[4]));
                    }

                    adapter.setList(listTranz);
                    recyclerView.setAdapter(adapter);
                }else{
                    toastText.setText("There is no transaction recorded!");
                    toast.show();
                    Intent gameIntent=new Intent(ListTranzactions.this,HomeActivity.class);
                    startActivity(gameIntent);
                    finish();
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

        dbprocess obj = new dbprocess();
        obj.execute(myURL + qry);
    }
    @Override
    public void onBackPressed() {
        Intent gameIntent=new Intent(ListTranzactions.this,HomeActivity.class);
        startActivity(gameIntent);
        finish();
    }

    public void ReloadTran(View view){
        getTranzaction();
    }

}