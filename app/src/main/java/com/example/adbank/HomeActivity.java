package com.example.adbank;

import androidx.annotation.AnimRes;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.etebarian.meowbottomnavigation.MeowBottomNavigation;
import com.example.adbank.setting.SettingActivity;
import com.google.android.material.navigation.NavigationView;
import com.luseen.spacenavigation.SpaceItem;
import com.luseen.spacenavigation.SpaceNavigationView;
import com.luseen.spacenavigation.SpaceOnClickListener;
import com.startapp.sdk.adsbase.Ad;
import com.startapp.sdk.adsbase.StartAppAd;
import com.startapp.sdk.adsbase.StartAppSDK;
import com.startapp.sdk.adsbase.VideoListener;
import com.startapp.sdk.adsbase.adlisteners.AdEventListener;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;

public class HomeActivity extends AppCompatActivity{

    LinearLayout linearLayoutAds,linearLayoutPlay,linearLayoutDaily,linearLayoutNoAds,linearLayoutTranzaction;
    TextView resourceAdBank,timeToNextReward,toastText;

    public static final String StartApp_ID="209901908";
    String todayString;
    ArrayList<Integer> valoareDay;
    ImageView homeMenu,rankMenu,settingMenu;
    Handler timeDailyHandler;
    boolean DailyReward;
    int interval= 1000; //100ms - dupa 100 se repeta runable
    int RewardStandardVal=50;
    int RewardADS=100;

    //toast config
    LayoutInflater inflater;
    View layout;
    Toast toast;


    SpaceNavigationView spaceNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);


        spaceNavigationView=findViewById(R.id.space);
        spaceNavigationView.initWithSaveInstanceState(savedInstanceState);

        spaceNavigationView.addSpaceItem(new SpaceItem("Home", R.drawable.home));
        spaceNavigationView.addSpaceItem(new SpaceItem("Setting", R.drawable.setting));

        StartAppSDK.init(HomeActivity.this, StartApp_ID, false);
        StartAppSDK.setTestAdsEnabled(true);
        StartAppAd.disableSplash();
        initialize();
        loadMoney();
        valoareDay=new ArrayList<Integer>();
        valoareDay.add(10);
        valoareDay.add(42);
        valoareDay.add(70);
        valoareDay.add(122);


        btnOpenAds();
        btnOpenGame();
        btnDailyReward();

        btnOpenTranzaction();
        //btnOpenListTranzaction();

       // showSettingPage();

        setNoNavBar();

        timeDailyHandler=new Handler();
        startRepeat();
        navigationListen();

    }

    Runnable repeatShowTime = new Runnable() {
        @Override
        public void run() {
            try {
                Calendar calendar=Calendar.getInstance();
                int year=calendar.get(Calendar.YEAR);
                int month=calendar.get(Calendar.MONTH);
                int day=calendar.get(Calendar.DAY_OF_MONTH);
                int hour =calendar.get(Calendar.HOUR);
                int minute = calendar.get(Calendar.MINUTE);
                int second  =calendar.get(Calendar.SECOND);

                todayString=year+""+month+""+day;
            }finally {
                timeDailyHandler.postDelayed(repeatShowTime,interval);
            }
        }
    };

    void startRepeat(){
        repeatShowTime.run();
    }


    public void initialize(){
        linearLayoutAds=findViewById(R.id.linearAds);
        linearLayoutPlay=findViewById(R.id.linearPlay);
        resourceAdBank=findViewById(R.id.resourceAdBank);
        linearLayoutDaily=findViewById(R.id.linearDaily);

        linearLayoutTranzaction=findViewById(R.id.linearTranzaction);


        inflater = getLayoutInflater();
        layout = inflater.inflate(R.layout.custom_toast, (ViewGroup)findViewById(R.id.toast_layout));
        toast=new Toast(getApplicationContext());

        toastText=layout.findViewById(R.id.textView2_toast);
        toast.setGravity(Gravity.BOTTOM,0,150);
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.setView(layout);

    }

    public void navigationListen(){
        spaceNavigationView.setSpaceOnClickListener(new SpaceOnClickListener() {
            @Override
            public void onCentreButtonClick() {
                Toast.makeText(HomeActivity.this,"History Of Tranzaction", Toast.LENGTH_SHORT).show();
                spaceNavigationView.setCentreButtonSelectable(true);
                Intent listTranz=new Intent(HomeActivity.this,ListTranzactions.class);
                startActivity(listTranz);
                finish();
            }

            @Override
            public void onItemClick(int itemIndex, String itemName) {
                if (itemIndex==1) {
                    startActivity(new Intent(HomeActivity.this, SettingActivity.class));
                    finish();
                }
            }

            @Override
            public void onItemReselected(int itemIndex, String itemName) {
                Toast.makeText(HomeActivity.this,"You are already on the home page!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void btnOpenAds() {
        linearLayoutAds.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               showRewardedVideo();
               //recreate();
            }
        });
    }

    public void btnOpenGame() {
        linearLayoutPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent gameIntent=new Intent(HomeActivity.this,GameActivity.class);
                gameIntent.putExtra("CurentCoins",resourceAdBank.getText().toString());
                startActivity(gameIntent);
                finish();
            }
        });
    }

    public void btnOpenListTranzaction() {
        rankMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent listTranz=new Intent(HomeActivity.this,ListTranzactions.class);
                startActivity(listTranz);
                finish();
            }
        });
    }

    public void btnOpenTranzaction() {
        linearLayoutTranzaction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent gameIntent=new Intent(HomeActivity.this,Tranzaction.class);
                startActivity(gameIntent);
                finish();
            }
        });
    }


    public void btnDailyReward() {
        linearLayoutDaily.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dailyReward();
            }
        });
    }

    public void dailyReward(){
        SharedPreferences preferences=getSharedPreferences(String.valueOf(R.string.db_pref_adbank),0);
        int dayOfAppInstaled=preferences.getInt("dayInstaledApp",0);
        boolean curentDay=preferences.getBoolean(todayString,false);
        if (!curentDay){
            String nextMoney=String.valueOf(Double.parseDouble(resourceAdBank.getText().toString())+RewardStandardVal);
            resourceAdBank.setText(nextMoney);
            saveMoney(nextMoney);
            SharedPreferences.Editor editor=preferences.edit();
            editor.putBoolean(todayString,true);
            editor.putString(String.valueOf(R.string.key_pref_lastDataReward),todayString);
            editor.apply();
            toastText.setText("Congratulations! \n You have won the daily prize!");
            toast.show();

           // Toast.makeText(HomeActivity.this,"You win the daily reward! "+RewardStandardVal+" at : "+todayString,Toast.LENGTH_SHORT).show();
        }else{
            toastText.setText("You have already won the prize today!");
            toast.show();
            //Toast.makeText(HomeActivity.this,"Already achive the daily reward.\nCome back tomorrow!",Toast.LENGTH_SHORT).show();
        }
    }



    public void loadMoney(){
        SharedPreferences sharedPreferences= getSharedPreferences("KEY_SHARED",MODE_PRIVATE);
        resourceAdBank.setText(sharedPreferences.getString("myMoney","0"));
    }
    public void showRewardedVideo(){
        final StartAppAd rewardedVideo=new StartAppAd(HomeActivity.this);

        rewardedVideo.setVideoListener(new VideoListener() {
            @Override
            public void onVideoCompleted() {
                Double curentResource=Double.parseDouble(loadMoneyPref())+RewardADS;
                Log.i("ALPHA"," Complete");
                saveMoney(String.valueOf(curentResource));
                String urlUpload="https://lifegame.ro/adBank/uploadAdsStat.php";
                SharedPreferences sharedPreferences= getSharedPreferences(String.valueOf(R.string.db_pref_adbank),MODE_PRIVATE);
                String emailString=sharedPreferences.getString(String.valueOf(R.string.key_pref_login_email),"0");
                //UPLOAD STATISTICS
                String qry="?user_email="+emailString.trim();
                class dbprocess extends AsyncTask<String,Void,String>
                {
                    @Override
                    protected void onPostExecute(String data){
                        if(data.equals("OK"))
                        {
                            toastText.setText("YOU WIN 100$");
                            toast.show();
                        }else{
                            toastText.setText("Something went wrong...Please Retry!");
                            toast.show();
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
                obj.execute(urlUpload+qry);

                rewardedVideo.close();
                Intent i = new Intent(HomeActivity.this,HomeActivity.class);
                finish();
                overridePendingTransition(0, 0);
                startActivity(i);
                overridePendingTransition(0, 0);
            }
        });

        rewardedVideo.loadAd(StartAppAd.AdMode.REWARDED_VIDEO, new AdEventListener() {
            @Override
            public void onReceiveAd(Ad ad) {
                Log.i("ALPHA"," Loaded");
                rewardedVideo.showAd();
            }

            @Override
            public void onFailedToReceiveAd(Ad ad) {
                Log.i("ALPHA"," Failed");
            }
        });
    }
    public void saveMoney(String numberMoney){
        SharedPreferences sharedPreferences= getSharedPreferences("KEY_SHARED",MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("myMoney",String.valueOf(numberMoney));
        editor.apply();
    }
    public String loadMoneyPref(){
        SharedPreferences sharedPreferences= getSharedPreferences("KEY_SHARED",MODE_PRIVATE);
        return  sharedPreferences.getString("myMoney","0");
    }
    public void setNoNavBar() {

//        HomeActivity.this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
//                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);

        HomeActivity.this.getWindow().getDecorView().setSystemUiVisibility(
                        View.SYSTEM_UI_FLAG_LAYOUT_STABLE|
                        View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY|
                                View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION|
                                View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                );

    }


    public  void logoutHome(View view){
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

    @Override
    public void onBackPressed() {
        finish();
    }
}