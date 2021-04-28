package com.example.labwebserviceandasynctask;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

public class MainActivity extends AppCompatActivity
{
    private Button buttonCallWebService;
    private TextView textViewWebService;
    private TextView textViewSetup;
    private TextView textViewPunchline;
    private JokeViewModel jokeViewModel;
    private JokeViewModel setupViewModel;
    private JokeViewModel punchlineViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Step 1:
        initializeAttributes();

        // Step 2:
        initializeObserver();

        // Step 3:
        setButton();

    }

    private void initializeAttributes()
    {
        buttonCallWebService = findViewById(R.id.buttonCallWebService);
        textViewWebService = findViewById(R.id.textViewWebService);
        textViewSetup = findViewById(R.id.textViewSetup);
        textViewPunchline = findViewById(R.id.textViewPunchline);

        // Associate the ViewModel and the UI Controller using the .of method in the abstract data
        // type ViewModelProviders. ViewModelProviders helps establish association.
        jokeViewModel =  ViewModelProviders.of(this).get(JokeViewModel.class);
        setupViewModel = ViewModelProviders.of(this).get(JokeViewModel.class);
        punchlineViewModel = ViewModelProviders.of(this).get(JokeViewModel.class);

    }

    private void initializeObserver()
    {
        final Observer<String> jokeObserver = new Observer<String>()
        {
            @Override
            public void onChanged(@Nullable String s)
            {
                textViewWebService.setText(s);
            }
            // this method gets called every time setValue gets called
            // setValue is in onClickListener for buttons in this app
            // This helps maintain consistency with data during configuration
            // change instances such as screen orientation changes
        };

        // Observe the live data
        // .this activity is lifecycle owner and jokeObserver
        jokeViewModel.getResultJSON().observe(this, jokeObserver);


        final Observer<String> setupObserver = new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                if(s == null)
                {
                    textViewSetup.setText("s = null");
                }
                else
                {
                    textViewSetup.setText(s);
                }
            }
        };
        setupViewModel.getSetup().observe(this, setupObserver);



        final Observer<String> punchlineObserver = new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                if (s == null)
                {
                    textViewPunchline.setText("s = null");
                }
                else
                {
                    textViewPunchline.setText(s);
                }
            }
        };
        punchlineViewModel.getPunchLine().observe(this, punchlineObserver);
    }





    private void setButton()
    {
        buttonCallWebService.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                jokeViewModel.loadData();
                //setupViewModel.loadData();
                //punchlineViewModel.loadData();
            }
        });
    }


}
