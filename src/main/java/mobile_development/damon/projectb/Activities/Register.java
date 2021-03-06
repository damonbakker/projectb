package mobile_development.damon.projectb.Activities;

import android.content.Intent;
import android.graphics.Color;
import android.os.CountDownTimer;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import mobile_development.damon.projectb.AppConfig;
import mobile_development.damon.projectb.NetworkHandler;
import mobile_development.damon.projectb.R;


public class Register extends AppCompatActivity
{

    public boolean touch_state = false;
    public boolean bugfix_state = false;
    public String background_color;
    public String new_background_color = "#25ae90";

    public EditText input_email;
    public EditText input_uname;
    public EditText input_password;
    public ProgressBar waiting_response;

    mobile_development.damon.projectb.Models.Login lm = new mobile_development.damon.projectb.Models.Login();

    public RelativeLayout register_view;
    public RelativeLayout register_view_2;
    public RelativeLayout login_view;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        register_view = (RelativeLayout) findViewById(R.id.register_layout);
        register_view_2 = (RelativeLayout) findViewById(R.id.underflow_layout);
        login_view = (RelativeLayout) findViewById(R.id.main_layout);
        // You can be pretty confident that the intent will not be null here.
        Intent intent = getIntent();

        try
        {
            new_background_color = getIntent().getStringExtra("background_color");
            register_view.setBackgroundColor(Color.parseColor(new_background_color));
        }
        catch (Exception e)
        {
            register_view.setBackgroundColor(Color.parseColor("#25ae90"));
        }

        try
        {
            background_color = new_background_color;
        }
        catch (Exception e)
        {
            background_color = "#25ae90";
        }


        input_email = (EditText) findViewById(R.id.userInput_email);
        input_uname = (EditText) findViewById(R.id.userInput_username);
        input_password = (EditText) findViewById(R.id.userInput_password);
        waiting_response = (ProgressBar) findViewById(R.id.progressBar_register);
        Button button_login = (Button) findViewById(R.id.button_register);
        button_login.setOnLongClickListener(onLongClickListener);
        button_login.setOnClickListener(onClickListener);




    }

    public void returnTo_sender()
    {
        Intent intent = new Intent(Register.this, Login.class);
        intent.putExtra("background_color", background_color);
        startActivity(intent);
        overridePendingTransition(R.anim.pull_from_left, R.anim.push_to_right);
    }

    @Override
    public void onBackPressed()
    {
        super.onBackPressed();
        returnTo_sender();

    }

    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v)
        {
            String email = input_email.getText().toString();
            String username = input_uname.getText().toString();
            String password = input_password.getText().toString();

            if (email.trim().length() > 0 && password.trim().length() > 0 && username.trim().length() > 0)
            {

                Register(email,username,password);

            }
            else
            {
                Toast toast =  Toast.makeText(getApplicationContext(), "Fields are missing credentials", Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER,0,0);
                toast.show();
            }

        }
    };




    private View.OnLongClickListener onLongClickListener = new View.OnLongClickListener() {
        @Override
        public boolean onLongClick(View v)
        {

            returnTo_sender();
            return false;
        }
    };



    @Override
    public boolean onTouchEvent(MotionEvent event)
    {

        //workaround animation end bug
        if (!bugfix_state)
        {
            bugfix_state = true;

            new CountDownTimer(700, 1000)
            {

                public void onTick(long millisUntilFinished)
                {

                }

                public void onFinish()
                {
                    bugfix_state = false;
                    Log.i("test", "COUNTDOWN KLAAR");
                    touch_state = false;
                }
            }.start();
        }

        if (!touch_state)
        {
            touch_state = true;
            lm.LoginBackgroundChange(event,register_view,register_view_2);
            background_color = lm.current_background_color;


        }


        return super.onTouchEvent(event);

    }

    private void Register(final String email, final String username, final String password)
    {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, AppConfig.URL_API, new Response.Listener<String>()
        {
            @Override
            public void onResponse(String response)
            {
                waiting_response.setVisibility(View.INVISIBLE);
                Log.i("RESPONSE", "RESPONSE RECIEVED");
                Log.i("RESPONSE",response);

                try
                {
                    JSONObject response_obj = new JSONObject(response);
                    boolean error = response_obj.getBoolean("error");

                    if (!error)
                    {
                        Toast toast =  Toast.makeText(getApplicationContext(), "Registration complete!", Toast.LENGTH_SHORT);
                        toast.setGravity(Gravity.CENTER,0,0);
                        toast.show();
                        returnTo_sender();
                    }
                    else
                    {
                        String error_msg = response_obj.getString("error_msg");
                        Toast toast =  Toast.makeText(getApplicationContext(), error_msg, Toast.LENGTH_SHORT);
                        toast.setGravity(Gravity.CENTER,0,0);
                        toast.show();

                    }

                }
                catch (JSONException e)
                {

                }


            }

        }, new Response.ErrorListener()
        {
            @Override
            public void onErrorResponse(VolleyError error) {

                Log.i("RESPONSE","RESPONSE FAILED");
                Log.i("RESPONSE",error.getMessage().toString());




            }
        })

        {
            @Override
            protected Map<String, String> getParams()
            {
                // Posting parameters to login url
                Map<String, String> params = new HashMap<>();
                params.put("tag", "register");
                params.put("email", email);
                params.put("username",username);
                params.put("password", password);

                return params;
            }

        };

        waiting_response.setVisibility(View.VISIBLE);
        NetworkHandler.getInstance(Register.this).addToRequestQueue(stringRequest);
    }


}
