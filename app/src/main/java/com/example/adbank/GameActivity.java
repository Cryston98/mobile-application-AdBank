package com.example.adbank;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class GameActivity extends AppCompatActivity {

    //Varianle
    int[] candies ={
            R.drawable.g1,
            R.drawable.g2,
            R.drawable.g3,
            R.drawable.g4,
            R.drawable.g5,
            R.drawable.g6
    };

    int widthOfBlock,noOfBlocks=8,widthOfScreen,heightOfScreen;
    ArrayList<ImageView> candy=new ArrayList<>();
    int candyToBeDragged,candyToBeReplaced;
    int notCandy=R.drawable.ic_launcher_background;
    Handler mHandler ;
    Button btnsaveMoney;
    int interval= 100; //100ms - dupa 100 se repeta runable
    TextView scoreUI,resourceAdBank;
    int scoreVal=0,nextVall;
    MediaPlayer mediaPlayer;
    int startGame=0;
    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        resourceAdBank=findViewById(R.id.resourceAdBank);
        resourceAdBank.setText(loadDataMoney());
        mediaPlayer = MediaPlayer.create(GameActivity.this,R.raw.coins);

        DisplayMetrics displayMetrics=new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        widthOfScreen=displayMetrics.widthPixels - 80;
        heightOfScreen=displayMetrics.heightPixels - 30;

        widthOfBlock=widthOfScreen/noOfBlocks;
        createBoard();
        for (ImageView imageView:candy)
        {
            imageView.setOnTouchListener(new OnSwipeListener(this){
                @Override
                void onSwipeLeft() {
                    startGame=1;
                    super.onSwipeLeft();
                    candyToBeDragged=imageView.getId();
                    candyToBeReplaced=candyToBeDragged-1;
                    candyInterchange();
                }

                @Override
                void onSwipeRight() {
                    startGame=1;
                    super.onSwipeRight();
                    candyToBeDragged=imageView.getId();
                    candyToBeReplaced=candyToBeDragged+1;
                    candyInterchange();
                }

                @Override
                void onSwipeTop() {
                    startGame=1;
                    super.onSwipeTop();
                    candyToBeDragged=imageView.getId();
                    candyToBeReplaced=candyToBeDragged+noOfBlocks;
                    candyInterchange(); }

                @Override
                void onSwipeDown() {
                    startGame=1;
                    super.onSwipeDown();
                    candyToBeDragged=imageView.getId();
                    candyToBeReplaced=candyToBeDragged-noOfBlocks;
                    candyInterchange();}
            });
        }
        mHandler=new Handler();
        startRepeat();
    }

    private void checkRowForThree(){
        if (startGame==1) {
            for (int i = 0; i < 62; i++) {
                int choseCandy = (int) candy.get(i).getTag();
                boolean isBlank = (int) candy.get(i).getTag() == notCandy;
                Integer[] notValid = {6, 7, 14, 15, 22, 23, 30, 31, 38, 39, 46, 47, 55};
                List<Integer> list = Arrays.asList(notValid);
                if (!list.contains(i)) {
                    int x = i;
                    if ((int) candy.get(x++).getTag() == choseCandy && !isBlank &&
                            (int) candy.get(x++).getTag() == choseCandy &&
                            (int) candy.get(x).getTag() == choseCandy) {

                        scoreVal = scoreVal + 3;
                        saveDataMoney();

                        candy.get(x).setImageResource(notCandy);
                        candy.get(x).setTag(notCandy);
                        x--;
                        candy.get(x).setImageResource(notCandy);
                        candy.get(x).setTag(notCandy);
                        x--;
                        candy.get(x).setImageResource(notCandy);
                        candy.get(x).setTag(notCandy);
                    }
                }
            }
            moveDownCandy();
        }

    }

    private void checkCollomForThree(){
        if (startGame==1) {
            for (int i = 0; i < 47; i++) {
                int choseCandy = (int) candy.get(i).getTag();
                boolean isBlank = (int) candy.get(i).getTag() == notCandy;
                int x = i;
                if ((int) candy.get(x).getTag() == choseCandy && !isBlank &&
                        (int) candy.get(x + noOfBlocks).getTag() == choseCandy &&
                        (int) candy.get(x + 2 * noOfBlocks).getTag() == choseCandy) {

                    scoreVal = scoreVal + 3;
                    saveDataMoney();

                    candy.get(x).setImageResource(notCandy);
                    candy.get(x).setTag(notCandy);
                    x = x + noOfBlocks;
                    candy.get(x).setImageResource(notCandy);
                    candy.get(x).setTag(notCandy);
                    x = x + noOfBlocks;
                    candy.get(x).setImageResource(notCandy);
                    candy.get(x).setTag(notCandy);
                }
            }
            moveDownCandy();
        }
    }
    private void moveDownCandy(){
        Integer[] firstRow={0,1,2,3,4,5,6,7};
        List<Integer> list=Arrays.asList(firstRow);
        for (int i=55;i>=0;i--)
        {
            if ((int)candy.get(i+noOfBlocks).getTag()==notCandy)
            {
                candy.get(i+noOfBlocks).setImageResource((int)candy.get(i).getTag());
                candy.get(i+noOfBlocks).setTag(candy.get(i).getTag());
                candy.get(i).setImageResource(notCandy);
                candy.get(i).setTag(notCandy);
                if (list.contains(i) &&(int) candy.get(i).getTag()==notCandy)
                {
                    int randomColor=(int)Math.floor(Math.random()*candies.length);
                    candy.get(i).setImageResource(candies[randomColor]);
                    candy.get(i).setTag(candies[randomColor]);
                }
            }
        }

        for (int i=0;i<8;i++)
        {
            if ((int)candy.get(i).getTag()==notCandy)
            {
                int randomColor=(int)Math.floor(Math.random()*candies.length);
                candy.get(i).setImageResource(candies[randomColor]);
                candy.get(i).setTag(candies[randomColor]);
            }
        }
    }

    Runnable repeatChecker = new Runnable() {
        @Override
        public void run() {
            try {
                checkRowForThree();
                checkCollomForThree();
                moveDownCandy();

            }finally {
                mHandler.postDelayed(repeatChecker,interval);
            }
        }
    };


    void startRepeat(){
        repeatChecker.run();
    }

    private void candyInterchange(){
        int background=(int)candy.get(candyToBeReplaced).getTag();
        int background1=(int)candy.get(candyToBeDragged).getTag();
        candy.get(candyToBeDragged).setImageResource(background);
        candy.get(candyToBeReplaced).setImageResource(background1);
        candy.get(candyToBeDragged).setTag(background);
        candy.get(candyToBeReplaced).setTag(background1);
    }

    private void  createBoard(){
        GridLayout gridLayout= findViewById(R.id.board);
        gridLayout.setRowCount(noOfBlocks);
        gridLayout.setColumnCount(noOfBlocks);
        gridLayout.getLayoutParams().width=widthOfScreen;
        gridLayout.getLayoutParams().height=widthOfScreen;

        for (int i=0;i<noOfBlocks*noOfBlocks;i++)
        {
            ImageView imageView = new ImageView(this);
            imageView.setId(i);
            imageView.setLayoutParams(new ViewGroup.LayoutParams(widthOfBlock,widthOfBlock));
            imageView.setMaxHeight(widthOfBlock);
            imageView.setMaxWidth(widthOfBlock);

            int randomCandy=(int)Math.floor(Math.random()*candies.length);
            imageView.setImageResource(candies[randomCandy]);
            imageView.setTag(candies[randomCandy]);
            candy.add(imageView);
            gridLayout.addView(imageView);
        }
    }


    public void saveDataMoney(){
        Double storeMoney=Double.parseDouble(loadDataMoney());
        Double curentMoney=storeMoney+scoreVal;
         mediaPlayer.start();
        SharedPreferences sharedPreferences= getSharedPreferences("KEY_SHARED",MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("myMoney",String.valueOf(curentMoney));
        editor.apply();
        scoreVal=0;
        resourceAdBank.setText(String.valueOf(curentMoney));
    }

    public String  loadDataMoney(){
        SharedPreferences sharedPreferences= getSharedPreferences("KEY_SHARED",MODE_PRIVATE);
        String myMoney=sharedPreferences.getString("myMoney","0");
        return myMoney;
        //AnimateCounter animateCounter=new AnimateCounter.Builder(resourceAdBank).setCount(curentMoney,Integer.parseInt(myMoney)).setDuration(1000).build();
        //animateCounter.execute();
    }

    @Override
    public void onBackPressed() {
        Intent gameIntent=new Intent(GameActivity.this,HomeActivity.class);
        startActivity(gameIntent);
        finish();
    }
}