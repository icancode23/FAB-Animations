package com.example.nipunarora.fabanimations;

import android.animation.AnimatorSet;
import android.animation.ValueAnimator;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    FloatingActionButton locationAccess;
    Button[] buttons;
    Point[] FabmenuButtonVertices;
    int [] buttonicon;
    TextView [] buttonlabels;
    int height, width;//button dimensions
    int ANIMATION_DURATION = 300; //time for which the animation will be played
    int startPositionX = 0;
    int startPositionY = 0;
    int[] enterDelay = {80, 120};
    int[] exitDelay = {80, 40};
    int whichAnimation = 0; //variable to determine whether entry animation is to be played or exit animation
    String[] buttonlabel={"Track My Location","Publish My Location"};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        height = (int) getResources().getDimension(R.dimen.button_height);
        width = (int) getResources().getDimension(R.dimen.button_width);
        buttons=new Button[2];
        buttonlabels=new TextView[2];
        
        buttonicon=new int[2];
        buttonicon[0]=R.drawable.compass;
        buttonicon[1]=R.drawable.placeholder;
        calculateFABMenuVertices();
        setupButtons();

        locationAccess=(FloatingActionButton)findViewById(R.id.fab);
        locationAccess.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (whichAnimation == 0) {
                    /**
                     * Getting the center point of floating action button
                     *  to set start point of buttons
                     */
                    startPositionX = (int) v.getX() + 50;
                    startPositionY = (int) v.getY() + 50;

                    for (Button button : buttons) {
                        button.setX(startPositionX);
                        button.setY(startPositionY);
                        button.setVisibility(View.VISIBLE);
                    }
                    for (TextView t:buttonlabels)
                    {
                        t.setX(startPositionX);
                        t.setY(startPositionY);
                        t.setVisibility(View.VISIBLE);
                        
                    }
                    for (int i = 0; i < buttons.length; i++) {
                        playEnterAnimation(buttons[i], i,buttonlabels[i]);
                    }
                    whichAnimation = 1;
                } else {
                    for (int i = 0; i < buttons.length; i++) {
                        playExitAnimation(buttons[i], i,buttonlabels[i]);
                    }
                    whichAnimation = 0;
                }
            }
        });
    }
    private void calculateFABMenuVertices() {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int centerY = displayMetrics.heightPixels / 2;
        int centerX = displayMetrics.widthPixels / 2;
        FabmenuButtonVertices=new Point[2];
        FabmenuButtonVertices[0]=new Point(centerX-centerX/2,centerY-150);
        FabmenuButtonVertices[1]=new Point(centerX+centerX/2-20,centerY-150);
    }
    private void setupButtons()
    {
        for (int i = 0; i < buttons.length; i++) {

            buttons[i] = new Button(MainActivity.this);
            buttons[i].setLayoutParams(new RelativeLayout.LayoutParams(5, 5));
            buttons[i].setX(0);
            buttons[i].setY(0);
            buttons[i].setTag(i);
            buttons[i].setOnClickListener(this);
            buttons[i].setVisibility(View.INVISIBLE);
            buttons[i].setBackgroundResource(R.drawable.circular_background);
            buttons[i].setBackground(ResourcesCompat.getDrawable(getResources(),buttonicon[i], null));
            /*buttons[i].setTextColor(Color.WHITE);
            buttons[i].setText(String.valueOf(i + 1));
            buttons[i].setTextSize(20);*/
            ((RelativeLayout) findViewById(R.id.activity_main)).addView(buttons[i]);
            buttonlabels[i]=new TextView(this);
            buttonlabels[i].setLayoutParams(new RelativeLayout.LayoutParams(5, 5));
            buttonlabels[i].setX(0);
            buttonlabels[i].setY(0);
            buttonlabels[i].setText(buttonlabel[i]);
            buttonlabels[i].setVisibility(View.INVISIBLE);
            ((RelativeLayout) findViewById(R.id.activity_main)).addView(buttonlabels[i]);


            

        }
    }

    @Override
    public void onClick(View v) {
        switch ((int) v.getTag()) {
            case 0:
                Toast.makeText(this, "Button 1 clicked", Toast.LENGTH_SHORT).show();
                break;
            case 1:
                Toast.makeText(this, "Button 2 clicked", Toast.LENGTH_SHORT).show();
                break;
            default:break;

        }
    }
    private void playEnterAnimation(final Button button, int position, final TextView textView) {

        AnimatorSet buttonAnimator = new AnimatorSet();
    
        //these fractional addition and subtractions have been made to balance the offset due to the size as the
        //the button centre would be assigned to the co ordinate and we need to subtract half its width to balance it

        //******************** CREATING THE X ANIMATOR **************/
        ValueAnimator buttonAnimatorX = ValueAnimator.ofFloat(startPositionX + button.getLayoutParams().width / 2,
                FabmenuButtonVertices[position].x);

        buttonAnimatorX.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                button.setX((float) animation.getAnimatedValue() - button.getLayoutParams().width / 2);
                button.requestLayout();
                textView.setX((float) animation.getAnimatedValue() - (button.getLayoutParams().width / 2)-40);
                textView.requestLayout();
            }
        });
        buttonAnimatorX.setDuration(ANIMATION_DURATION);

        //*************************** CREATING THE Y ANIMATOR *******************/
        ValueAnimator buttonAnimatorY = ValueAnimator.ofFloat(startPositionY + 5,
                FabmenuButtonVertices[position].y);
        buttonAnimatorY.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                button.setY((float) animation.getAnimatedValue());
                button.requestLayout();
                textView.setY((float) animation.getAnimatedValue()+(button.getLayoutParams().height)+20);
                textView.requestLayout();
                
            }

        });
        buttonAnimatorY.setDuration(ANIMATION_DURATION);


        //*********************** CREATING THE SIZE ANIMATOR ********************??
        ValueAnimator buttonSizeAnimator = ValueAnimator.ofInt(5, width); //initially we have the params to 5,5 while creating the buttons now we are animating
        //upto the value given in the dimens file
        buttonSizeAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                button.getLayoutParams().width = (int) animation.getAnimatedValue();
                button.getLayoutParams().height = (int) animation.getAnimatedValue();
                button.requestLayout();
                textView.getLayoutParams().width = (int) animation.getAnimatedValue()+width;
                textView.getLayoutParams().height = (int) animation.getAnimatedValue();
                textView.requestLayout();
            }
        });
        buttonSizeAnimator.setDuration(ANIMATION_DURATION);


        buttonAnimator.play(buttonAnimatorX).with(buttonAnimatorY).with(buttonSizeAnimator);
        buttonAnimator.setStartDelay(enterDelay[position]);
        buttonAnimator.start();
    }
    private void playExitAnimation(final Button button, int position, final TextView textView) {


        AnimatorSet buttonAnimator = new AnimatorSet();

        ValueAnimator buttonAnimatorX = ValueAnimator.ofFloat(FabmenuButtonVertices[position].x - button.getLayoutParams().width / 2,
                startPositionX);
        buttonAnimatorX.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                button.setX((float) animation.getAnimatedValue());
                button.requestLayout();
                textView.setX((float) animation.getAnimatedValue()-40);
                textView.requestLayout();

            }
        });
        buttonAnimatorX.setDuration(ANIMATION_DURATION);

        ValueAnimator buttonAnimatorY = ValueAnimator.ofFloat(FabmenuButtonVertices[position].y,
                startPositionY + 5);
        buttonAnimatorY.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                button.setY((float) animation.getAnimatedValue());
                button.requestLayout();
                textView.setY((float) animation.getAnimatedValue()+((button.getLayoutParams().height)+20));
                textView.requestLayout();
            }
        });
        buttonAnimatorY.setDuration(ANIMATION_DURATION);
        ValueAnimator buttonSizeAnimator = ValueAnimator.ofInt(width, 5);
        buttonSizeAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                button.getLayoutParams().width = (int) animation.getAnimatedValue();
                button.getLayoutParams().height = (int) animation.getAnimatedValue();
                button.requestLayout();
                textView.getLayoutParams().width = (int) animation.getAnimatedValue()+width;
                textView.getLayoutParams().height = (int) animation.getAnimatedValue();
                textView.requestLayout();
            }
        });
        buttonSizeAnimator.setDuration(ANIMATION_DURATION);

        buttonAnimator.play(buttonAnimatorX).with(buttonAnimatorY).with(buttonSizeAnimator);
        buttonAnimator.setStartDelay(exitDelay[position]);
        buttonAnimator.start();
    }
}
