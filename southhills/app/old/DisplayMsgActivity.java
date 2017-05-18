package com.tbgosalvez.southhills;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

public class DisplayMsgActivity extends AppCompatActivity
{

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_display_msg);
//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);


        // get a handle to this text view
        TextView tvMsg = (TextView)findViewById(R.id.tv_disp_msg);


        // display user msg from parent activity
        Intent iMsg = getIntent();

        String strMsg = iMsg.getStringExtra(CONSTANTS.strET_Content);
        tvMsg.setText(strMsg);
    }

}
