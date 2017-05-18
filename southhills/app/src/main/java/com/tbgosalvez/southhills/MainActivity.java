package com.tbgosalvez.southhills;

import android.content.Intent;
import java.text.DecimalFormat;
import java.util.ArrayList;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

public class MainActivity extends AppCompatActivity
{
    // variables && constants
    int nNumTickets=0;
    float fTicketPrice=0f;
    float fSnackPrice=0.75f;
    float fTotalPrice=0f;
    String strBandname;
    java.text.DecimalFormat decFmt = new DecimalFormat("$###.00");

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // variables && constants
        final Button btnSendSpin = (Button)findViewById(R.id.btn_send_spinner);
        final Button btnCheckPrice = (Button)findViewById(R.id.btn_check_price);
        final EditText etNumTickets = (EditText)findViewById(R.id.et_numTickets);
        final TextView tvTotalPrice = (TextView)findViewById(R.id.tv_total_price_value);
        final TextView tvTicketPrice = (TextView)findViewById(R.id.tv_ticket_price_value);
        final TextView tvMain = (TextView)findViewById(R.id.tv_main);

        final Spinner spinBands = (Spinner)findViewById(R.id.spn_band_selection);
        final ArrayAdapter<CharSequence> adaptSpin = ArrayAdapter.createFromResource(this,
                R.array.array_artist_names, android.R.layout.simple_spinner_dropdown_item);

        final ArrayList<CheckBox> arrSnacks = new ArrayList<>();
        arrSnacks.add((CheckBox)findViewById(R.id.cb_chewy));
        arrSnacks.add((CheckBox)findViewById(R.id.cb_crunchy));
        arrSnacks.add((CheckBox)findViewById(R.id.cb_fluffy));
        arrSnacks.add((CheckBox)findViewById(R.id.cb_drink));
        arrSnacks.add((CheckBox)findViewById(R.id.cb_melted));

        final Bundle bData = getIntent().getExtras();

        // assignments
        String strGreeting = "Hallo, "+getIntent().getStringExtra(CONSTANTS.strEmail) +"!";
        tvMain.setText(strGreeting);
        spinBands.setAdapter(adaptSpin);
        fTicketPrice = CONSTANTS.arrPrices.get(spinBands.getSelectedItem().toString());
        tvTicketPrice.setText(decFmt.format(fTicketPrice));

        // Callbacks for views
        spinBands.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l)
            {
                strBandname = spinBands.getSelectedItem().toString();
                Editable strNumTickets = etNumTickets.getText();


                fTicketPrice = CONSTANTS.arrPrices.get(strBandname);
                tvTicketPrice.setText(decFmt.format(fTicketPrice));


                if(strNumTickets != null)
                {
                    nNumTickets = Integer.parseInt(strNumTickets.toString());
                }
                else
                {
                    nNumTickets = 0;
                }


                fTotalPrice = nNumTickets*fTicketPrice;
                tvTotalPrice.setText(decFmt.format(fTotalPrice));
            }

            public void onNothingSelected(AdapterView<?> parent)
            {

            }
        });

        btnCheckPrice.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                //fTicketPrice = CONSTANTS.arrPrices.get(spinBands.getSelectedItem().toString());
                nNumTickets = Integer.parseInt(etNumTickets.getText().toString());

                // tix
                fTotalPrice = nNumTickets*fTicketPrice;

                // display it
                tvTotalPrice.setText(decFmt.format(fTotalPrice));

            }
        });

        btnSendSpin.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Intent iRad = new Intent(MainActivity.this, BillingInfoActivity.class);


                nNumTickets = Integer.parseInt(etNumTickets.getText().toString());
                if(nNumTickets < 1)
                {
                    Toast.makeText(MainActivity.this,
                            "Please Buy At Least One Ticket Or Leave. Thanks.",
                            Toast.LENGTH_LONG).show();
                }
                else
                {
                    // pass data
                    strBandname = spinBands.getSelectedItem().toString();
                    bData.putString(CONSTANTS.strArtist_Choice, strBandname);
                    bData.putInt(CONSTANTS.strNumberOfTickets, nNumTickets);
                    bData.putString(CONSTANTS.strTicketPrice, decFmt.format(fTicketPrice));
                    bData.putString(CONSTANTS.strTotalPrice, decFmt.format(fTotalPrice));


                    // snacks
                    StringBuilder sbSnacks = new StringBuilder();
                    sbSnacks.append("snacks to make a mess with:\n");

                    for(int i=0;i<arrSnacks.size();i++)
                    {
                        if(arrSnacks.get(i).isChecked())
                        {
                            sbSnacks.append("+");
                            sbSnacks.append(arrSnacks.get(i).getText().toString());
                            sbSnacks.append('\n');

                            //update price (all snacks equal)
                            fTicketPrice+=fSnackPrice;
                        }
                    }


                    // update total price
                    fTotalPrice = nNumTickets*fTicketPrice;
                    tvTotalPrice.setText(decFmt.format(fTotalPrice));

                    // bundle it
                    bData.putString(CONSTANTS.strSnackChoices,sbSnacks.toString());

                    iRad.putExtra(CONSTANTS.strBundle, bData);

                    startActivity(iRad);
                }
            }
        });
    }


}
