package com.tbgosalvez.southhills;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class OrderSummaryActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_summary);

//        Toast.makeText(this, "debug: it works",Toast.LENGTH_LONG).show();

        final Bundle bOrderInfo = getIntent().getBundleExtra(CONSTANTS.strBundle);

        final Button btnConfirm = (Button)findViewById(R.id.btn_summary_confirm_order);
        final Button btnBack = (Button)findViewById(R.id.btn_summary_back);
        final TextView tvOrderSumm = (TextView)findViewById(R.id.tv_summary_purchase);
        final TextView tvBillSumm = (TextView)findViewById(R.id.tv_summary_billing_info);
        final ImageView ivArtist = (ImageView)findViewById(R.id.iv_summary_artist);

        // change button texts
        btnConfirm.setText("Confirm & Place Order-->");
        btnBack.setText("<--Go Back & Change");

        // disp selected artist's image
        // strArtist_Choice |> getStringExtra |> getIntent |> arrImages.get
        ivArtist.setImageResource(CONSTANTS.arrImages.get(bOrderInfo.getString(CONSTANTS.strArtist_Choice)));

        // disp order details
        tvOrderSumm.setText(bOrderInfo.getString(CONSTANTS.strOrder_Details));

        // disp payment info
        StringBuilder sbPmtInfo = new StringBuilder("Payment Info:\n");
        String[] arrBilling = bOrderInfo.getStringArray(CONSTANTS.strBillingInfo);

        // fname lname
        sbPmtInfo.append("=============================================\n");
        sbPmtInfo.append(arrBilling[0] + ' ' + arrBilling[1] + '\n');
        // the rest
        sbPmtInfo.append(arrBilling[2] + ", " + arrBilling[3] + ' ' + arrBilling[4] + '\n');
        sbPmtInfo.append("Payment Type: " + bOrderInfo.getString(CONSTANTS.strPmtType) + '\n');
        sbPmtInfo.append("=============================================\n");
        tvBillSumm.setText(sbPmtInfo.toString());

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent iThankYou = new Intent(OrderSummaryActivity.this, ThanksActivity.class);
                iThankYou.putExtra(CONSTANTS.strBundle, bOrderInfo);

                startActivity(iThankYou);
            }
        });
    }
}
