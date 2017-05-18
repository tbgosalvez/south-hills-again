package com.tbgosalvez.southhills;

import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

public class ThanksActivity extends AppCompatActivity {

    private MediaPlayer bart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thanks);

        final Bundle bOrderInfo = getIntent().getBundleExtra(CONSTANTS.strBundle);
        final TextView tvMsg = (TextView)findViewById(R.id.tv_thanks);
        final ImageView ivArtist = (ImageView)findViewById(R.id.iv_thanks_artist);

        tvMsg.setTextSize(26f);
        tvMsg.setText("Thanks for your order, " + bOrderInfo.getString(CONSTANTS.strEmail) + "!");
        ivArtist.setImageResource(CONSTANTS.arrImages.get(bOrderInfo.getString(CONSTANTS.strArtist_Choice)));
        bart = MediaPlayer.create(this, CONSTANTS.arrMP3s.get(bOrderInfo.getString(CONSTANTS.strArtist_Choice)));
        bart.start();
    }

    @Override
    protected void onStop()
    {
        if(bart != null)
            bart.stop();
        super.onStop();
    }
}
