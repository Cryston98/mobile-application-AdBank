package com.example.adbank;


import android.content.Context;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

public class OnSwipeListener implements View.OnTouchListener {
    public GestureDetector gestureDetector;

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        return gestureDetector.onTouchEvent(motionEvent);
    }

    public OnSwipeListener(Context context) {
        this.gestureDetector = new GestureDetector(context, new GestureListener());
    }

    private final  class GestureListener extends  GestureDetector.SimpleOnGestureListener{
        public  static  final  int SWIPE_THREESOLD=100;
        public  static  final  int SWIPE_VELOCITY=100;

        @Override
        public boolean onDown(MotionEvent e) {
            return true;
        }

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            boolean result=false;
            float yDiff=e2.getY()-e1.getY();
            float xDiff=e2.getX()-e1.getX();
            if (Math.abs(xDiff)>Math.abs(yDiff))
            {
                //it means that we are either going to left or right direction
                if (Math.abs(xDiff)>SWIPE_THREESOLD && Math.abs(velocityX)>SWIPE_VELOCITY)
                {
                    if (xDiff>0)
                    {
                        onSwipeRight();
                    }else{
                        onSwipeLeft();
                    }
                    result=true;
                }

            }else if(Math.abs(yDiff)>SWIPE_THREESOLD && Math.abs(velocityY)>SWIPE_VELOCITY){
                if (yDiff>0)
                {
                    onSwipeTop();
                }else{
                    onSwipeDown();
                }
                result=true;
            }

            return result;
        }
    }
    void onSwipeLeft(){}
    void onSwipeRight(){}
    void onSwipeTop(){}
    void onSwipeDown(){}


}
