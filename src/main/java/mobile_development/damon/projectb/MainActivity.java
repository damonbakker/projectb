package mobile_development.damon.projectb;

import android.animation.Animator;
import android.graphics.Color;
import android.os.CountDownTimer;
import android.util.Log;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.widget.RelativeLayout;


public class MainActivity extends AppCompatActivity {


    private int animation_duration = 700;
    private int counter = 0;
    private boolean animation_state = false;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {   //Load the parent activity and any saved instances if any are saved
        super.onCreate(savedInstanceState);
        //Load layout
        setContentView(R.layout.activity_main);




        //start
    }


    @Override
    public boolean onTouchEvent(MotionEvent event)
    {
        if (!animation_state)
        {
            animation_state = true;

            //animation bug workaround, equals duration of animation
            new CountDownTimer(animation_duration, 1000)
            {
                public void onTick(long millisUntilFinished)
                {

                }

                public void onFinish()
                {
                    animation_state = false;
                }
            }.start();


            int x = (int)event.getX();
            int y = (int)event.getY();

            final RelativeLayout mainlayout = (RelativeLayout) findViewById(R.id.main_layout);
            final RelativeLayout registerlayout = (RelativeLayout) findViewById(R.id.register_layout);

            //get the entire field radius of the view
            int finalradius = Math.max(mainlayout.getWidth(), mainlayout.getHeight());


            switch (event.getAction())
            {
                case MotionEvent.ACTION_DOWN:
                    //pressed

                    //get pressed location(coordinates)
                    String stringx = String.valueOf(x);
                    String stringy = String.valueOf(y);

                    //create reveal animation with parameters
                    Animator anim= ViewAnimationUtils.createCircularReveal(registerlayout, x, y, 0, finalradius);
                    anim.setDuration(animation_duration);

                    //listen to the animation
                    anim.addListener(new Animator.AnimatorListener() {
                        @Override
                        public void onAnimationStart(Animator animation)
                        {
                            //animation is started
                            registerlayout.setVisibility(View.VISIBLE);
                        }

                        @Override
                        public void onAnimationEnd(Animator animation)
                        {
                            //animation has ended
                            mainlayout.setBackgroundColor(Color.MAGENTA);
                            counter++;



                        }

                        @Override
                        public void onAnimationCancel(Animator animation)
                        {
                           //animation is cancelled
                        }

                        @Override
                        public void onAnimationRepeat(Animator animation)
                        {
                            //
                        }
                    });

                    if (counter == 1)
                    {
                        registerlayout.setVisibility(View.INVISIBLE);
                        registerlayout.setBackgroundColor(Color.YELLOW);
                    }

                    //start animation
                    anim.start();

                    return true;  //

                case MotionEvent.ACTION_UP:
                    //released
                    return true;
            }
        }

        return super.onTouchEvent(event);

    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
