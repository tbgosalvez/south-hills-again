package com.tbgosalvez.southhills;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class BillingInfoActivity extends AppCompatActivity
{

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_billing_info);

        final MediaPlayer bart;
        final ImageView ivArtist = (ImageView)findViewById(R.id.iv_billing_artist);


        final Button btnSubmit = (Button)findViewById(R.id.btn_send_order);
        final Button btnBack = (Button)findViewById(R.id.btn_selection_go_back);
        final Button btnStop = (Button)findViewById(R.id.btn_stop_music);
        final TextView tvEcho = (TextView)findViewById(R.id.tv_disp_selection);
        final Spinner spinPmt = (Spinner)findViewById(R.id.spn_pmt_selection);
        final ArrayList<EditText> arrBillingInfo = new ArrayList<>();

        arrBillingInfo.add ((EditText)findViewById(R.id.et_fname));
        arrBillingInfo.add ((EditText)findViewById(R.id.et_lname));
        arrBillingInfo.add ((EditText)findViewById(R.id.et_city));
        arrBillingInfo.add ((EditText)findViewById(R.id.et_state));
        arrBillingInfo.add ((EditText)findViewById(R.id.et_zip));

        final Intent iRad = getIntent();
        final Bundle bOrderInfo = iRad.getBundleExtra(CONSTANTS.strBundle);


        // change some things
        btnSubmit.setText("Review Order");
        btnBack.setText("Go Back & Change");

        // echo order details

        int numTic = bOrderInfo.getInt(CONSTANTS.strNumberOfTickets, 0);

        StringBuilder strMsg = new StringBuilder();
        strMsg.append("Order Summary for " + bOrderInfo.getString(CONSTANTS.strEmail) + "\n");
        strMsg.append("--------------------------------------\n");
        strMsg.append("\tArtist: \t" +bOrderInfo.getString(CONSTANTS.strArtist_Choice) + "\n");
        strMsg.append("\tTicket Price: \t" + bOrderInfo.getString(CONSTANTS.strTicketPrice) + '\n');
        strMsg.append("\tQuantity: \t" + numTic + '\n');
        strMsg.append("--------------------------------------\n");
        strMsg.append("\tTotal Purchase: \t" + bOrderInfo.getString(CONSTANTS.strTotalPrice)+ '\n');
        strMsg.append("--------------------------------------\n");
        strMsg.append(bOrderInfo.getString(CONSTANTS.strSnackChoices));

        //String strMaybePlural = (numTic > 1) ? " tickets" : " ticket";
        /*strMsg.append("You've Selected " + numTic + strMaybePlural + " for \n");
        strMsg.append(bOrderInfo.getString(CONSTANTS.strArtist_Choice) + ", \n");
        strMsg.append("purchased at " + bOrderInfo.getString(CONSTANTS.strTicketPrice) + " each, \n");
        strMsg.append("for a total of " + bOrderInfo.getString(CONSTANTS.strTotalPrice) + ".");
*/
        // display data from previous activity
        tvEcho.setText(strMsg);

        // also pass it to next activity
        bOrderInfo.putString(CONSTANTS.strOrder_Details, strMsg.toString());



        // payment spinner
        SpinnerAdapter pmtSpinAdapter = ArrayAdapter.createFromResource(
                this,
                R.array.array_payment_types,
                android.R.layout.simple_spinner_dropdown_item);
        spinPmt.setAdapter(pmtSpinAdapter);



        // auto-start background music

        bart = MediaPlayer.create(this, CONSTANTS.arrMP3s.get(bOrderInfo.getString(CONSTANTS.strArtist_Choice)));
        bart.seekTo((1000*60+1000*30));
        bart.start();

        // manually stop the music
        btnStop.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                if(bart != null)
                {
                    bart.pause();
//                    bart.reset();
                    //bart.release();
                }
            }
        });

        // set Artist image
        ivArtist.setImageResource(CONSTANTS.arrImages.get(bOrderInfo.getString(CONSTANTS.strArtist_Choice)));


        // return to order form (MainActivity)
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(bart != null)
                {
                    bart.stop();
                    bart.reset();
                    //bart.release();
                }

                finish();
            }
        });

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Boolean isSet = true;
                EditText etElement;
                String[] arrInfo = new String[5];
                Intent iSummary = new Intent(BillingInfoActivity.this, OrderSummaryActivity.class);


                // validate text fields
                for(int a=0; a<arrBillingInfo.size();a++)
                {
                    if(arrBillingInfo.get(a).getText().toString().length() < 1)
                    {
                        Toast.makeText(BillingInfoActivity.this,
                                        "All fields are required.",
                                        Toast.LENGTH_LONG).show();
                        isSet = false;
                    }
                    else
                    {
                        arrInfo[a] = arrBillingInfo.get(a).getText().toString();
                    }
                }


                // validate spinner
                if(spinPmt.getSelectedItemPosition() == 0)
                {
                    Toast.makeText(BillingInfoActivity.this,
                                    "Please select a payment option.",
                                    Toast.LENGTH_LONG).show();
                    isSet = false;
                }
                else
                {
                    bOrderInfo.putString(CONSTANTS.strPmtType,
                                        spinPmt.getSelectedItem().toString());
                }



                if(isSet)
                {
//                    bart.stop();
//                    bart.reset();

                    bOrderInfo.putStringArray(CONSTANTS.strBillingInfo,arrInfo);

                    iSummary.putExtra(CONSTANTS.strBundle, bOrderInfo);

                    startActivity(iSummary);
                }

            }
        });


    }

    @Override
    protected void onStop()
    {
        super.onStop();
    }

    @Override
    protected void onPause()
    {

        super.onPause();
    }
}
