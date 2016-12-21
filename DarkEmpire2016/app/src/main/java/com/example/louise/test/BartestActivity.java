package com.example.louise.test;

import android.app.Activity;
import android.content.res.Resources;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.RatingBar.OnRatingBarChangeListener;
import android.widget.TextView;
import android.widget.Toast;

public class BartestActivity extends Activity {

    private RatingBar ratingBar;
    private TextView txtRatingValue;
    private Button btnSubmit;
    private Button progress;
    private Button progress_m;

    private ProgressBar myProgressBar;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bartest);

        //addListenerOnRatingBar();

        addListenerOnButton();

        int color = 0xFF00FF00;
//        progressbar.getIndeterminateDrawable().setColorFilter(color, PorterDuff.Mode.SRC_IN);
//        progressbar.getProgressDrawable().setColorFilter(color, PorterDuff.Mode.SRC_IN);



        //定義ProgressBar
        myProgressBar = (ProgressBar) findViewById(R.id.progressbar_updown);

        //ProgressBar通過ID來從XML中獲取
        progress = (Button) findViewById(R.id.progress);
        progress.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                myProgressBar.incrementProgressBy(+10);
                Toast.makeText(BartestActivity.this, String.valueOf((myProgressBar.getProgress())/5), Toast.LENGTH_SHORT).show();
            }

        });
        progress_m = (Button) findViewById(R.id.progress_m);
        progress_m.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {

                myProgressBar.incrementProgressBy(-10);
                Toast.makeText(BartestActivity.this, String.valueOf((myProgressBar.getProgress())/10), Toast.LENGTH_SHORT).show();
                if((myProgressBar.getProgress()/10) == 0) {
                    switch (StoryActivity.party) {
                        case "Sinae": {
                            Resources res = getResources();
                            myProgressBar.setProgressDrawable(res.getDrawable( R.drawable.red_progressbar));
                            break;
                        }
                        default: {
                            Resources res = getResources();
                            myProgressBar.setProgressDrawable(res.getDrawable( R.drawable.green_progressbar));
                            break;
                        }
                    }
                }
            }

        });
    }

    public void addListenerOnRatingBar() {

        ratingBar = (RatingBar) findViewById(R.id.test_ratingBar);
        txtRatingValue = (TextView) findViewById(R.id.txtRatingValue);

        //if rating value is changed,
        //display the current rating value in the result (textview) automatically
        ratingBar.setOnRatingBarChangeListener(new OnRatingBarChangeListener() {
            public void onRatingChanged(RatingBar ratingBar, float rating,
                                        boolean fromUser) {

                txtRatingValue.setText(String.valueOf(rating));

            }
        });
    }

    public void addListenerOnButton() {

        ratingBar = (RatingBar) findViewById(R.id.test_ratingBar);
//        ratingBar.setProgressDrawable(getResources().getDrawable(R.drawable.ratingbar_color));
        btnSubmit = (Button) findViewById(R.id.btnSubmit);

        //if click on me, then display the current rating value.
        btnSubmit.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                ratingBar.setRating(ratingBar.getRating()-1);
                Toast.makeText(BartestActivity.this,
                        String.valueOf(ratingBar.getRating()), Toast.LENGTH_SHORT).show();

            }

        });

    }
}
