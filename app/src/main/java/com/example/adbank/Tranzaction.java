package com.example.adbank;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DecimalFormat;

public class Tranzaction extends AppCompatActivity {
    String accountResources;
    TextView maximTransfer,quantityTransferVal,totalQuantityValue,oldTranzaction,transferValue;
    EditText uid_application,uid_account;
    Button sendRequestTransfer;
    SeekBar selectQuantityBar;

    Dialog confirmTranzactionDialog;
    //toast config
    LayoutInflater inflater;
    View layout;
    Toast toast;
    TextView toastText;
    double tranzactionPrice;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tranzaction);

        SharedPreferences sharedPreferences= getSharedPreferences("KEY_SHARED",MODE_PRIVATE);
        accountResources=sharedPreferences.getString("myMoney","0");
        initialize();
        //initializeHistoryTranzaction();
        tranzactionPrice=5;

        sendRequestTransfer.setEnabled(false);
        selectQuantity();
        sendMoney();

        Double dd=Double.parseDouble(accountResources);
        int ii=(int) Math.round(dd);
        maximTransfer.setText(String.valueOf(ii));
    }

    public void initialize(){
        uid_application=findViewById(R.id.uidAppTransfer);
        uid_account=findViewById(R.id.uidAccount);

        maximTransfer=findViewById(R.id.maximTransfer);
        sendRequestTransfer=findViewById(R.id.sendRequestTransfer);
        selectQuantityBar=findViewById(R.id.selectQuantityBar);
        quantityTransferVal=findViewById(R.id.quantityTransferValue);
        totalQuantityValue=findViewById(R.id.totalQuantityValue);
        //oldTranzaction=findViewById(R.id.waitingTranzactionValue);
        transferValue=findViewById(R.id.TransferValue);
        inflater = getLayoutInflater();
        layout = inflater.inflate(R.layout.custom_toast, (ViewGroup)findViewById(R.id.toast_layout));
        toast=new Toast(getApplicationContext());

        toastText=layout.findViewById(R.id.textView2_toast);
        toast.setGravity(Gravity.BOTTOM,0,150);
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.setView(layout);
    }
/*
    public void  initializeHistoryTranzaction(){
        SharedPreferences sharedPreferences= getSharedPreferences(String.valueOf(R.string.db_pref_adbank),MODE_PRIVATE);
        String emailAccount =sharedPreferences.getString(String.valueOf(R.string.key_pref_login_email),"null");

        String myURL="https://lifegame.ro/adBank/getHistoryTranzactions.php";
        String qry="?user_email="+emailAccount.trim();
        class dbprocess extends AsyncTask<String,Void,String>
        {
            @Override
            protected void onPostExecute(String data){

                if (data.equals("NaN"))
                {
                    toastText.setText("This functionality is invalid at this moment!");
                    toast.show();
                    Intent myIntent = new Intent(Tranzaction.this, HomeActivity.class);
                    startActivity(myIntent);
                    finish();
                }
                oldTranzaction.setText(data);
                Double dd=Double.parseDouble(accountResources);
                int ii=(int) Math.round(dd);
                maximTransfer.setText(String.valueOf(ii));
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
*/
    public void showConfirmTranzaction(){
        Button confBtnSendTranzaction;
        TextView passConfTranzaction;
        confirmTranzactionDialog=new Dialog(this);
        confirmTranzactionDialog.setContentView(R.layout.custom_popup);
        confBtnSendTranzaction=confirmTranzactionDialog.findViewById(R.id.confBtnSendTranzaction);
        passConfTranzaction=confirmTranzactionDialog.findViewById(R.id.passConfTranzaction);
        confirmTranzactionDialog.show();
        confirmTranzactionDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        confBtnSendTranzaction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String passConf=passConfTranzaction.getText().toString();
                SharedPreferences sharedPreferences= getSharedPreferences(String.valueOf(R.string.db_pref_adbank),MODE_PRIVATE);
                String passAccount=sharedPreferences.getString(String.valueOf(R.string.key_pref_login_password),"0");
                String accountAdBank=sharedPreferences.getString(String.valueOf(R.string.key_pref_login_email),"0");

                if (passConf.equals(passAccount))
                {
                    String uid_app = uid_application.getText().toString();
                    String uid_acc = uid_account.getText().toString();

                    String quantityTransfer = totalQuantityValue.getText().toString();

                        String myURL = "https://lifegame.ro/adBank/addNewTranzactions.php";

                        String qry = "?user_email=" + accountAdBank.trim() + "&quantity_transfer=" + quantityTransfer + "&uid_app=" + uid_app.trim()+ "&uid_account=" + uid_acc.trim();
                        class dbprocess extends AsyncTask<String, Void, String> {
                            @Override
                            protected void onPostExecute(String data) {
                                if (data.equals("Your request has been registered!")){
                                    SharedPreferences sharedPreferences= getSharedPreferences("KEY_SHARED",MODE_PRIVATE);
                                    SharedPreferences.Editor editor = sharedPreferences.edit();
                                    Double curentMoney=Double.parseDouble(sharedPreferences.getString("myMoney","0"));
                                    int curentMoneyInt=(int) Math.round(curentMoney);

                                    editor.putString("myMoney",String.valueOf(curentMoneyInt-Double.parseDouble(quantityTransfer)));
                                    editor.apply();
                                }

                                toastText.setText(data);
                                toast.show();
                                confirmTranzactionDialog.dismiss();
                                Intent myIntent = new Intent(Tranzaction.this, HomeActivity.class);
                                startActivity(myIntent);
                                finish();
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
                }else{
                    toastText.setText("Password is wrong !");
                    toast.show();
                    confirmTranzactionDialog.dismiss();
                }
            }
        });
    }

    public void selectQuantity(){
        selectQuantityBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if(progress>0)
                {
                    sendRequestTransfer.setEnabled(true);
                }else{
                    sendRequestTransfer.setEnabled(false);
                }
                quantityTransferVal.setText(String.valueOf(progress));

               // Double  historyTranzaction=Double.parseDouble(oldTranzaction.getText().toString());

                seekBar.setMax(Integer.parseInt(maximTransfer.getText().toString()));
                 DecimalFormat df = new DecimalFormat("0.00");

                tranzactionPrice =(5.5*progress)/100;
                String price2double = df.format(tranzactionPrice);
                tranzactionPrice=Double.parseDouble(price2double);

                totalQuantityValue.setText(String.valueOf(Integer.parseInt(quantityTransferVal.getText().toString())+tranzactionPrice));
                transferValue.setText(String.valueOf(tranzactionPrice));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    public void sendMoney(){
        sendRequestTransfer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showConfirmTranzaction();
            }
        });
    }

    @Override
    public void onBackPressed() {
        Intent gameIntent=new Intent(Tranzaction.this,HomeActivity.class);
        startActivity(gameIntent);
        finish();
    }
}