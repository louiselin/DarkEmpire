package com.example.louise.test;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class ExplainAvtivity extends AppCompatActivity {

    private String explain = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_explain_avtivity);
        getWindow().setBackgroundDrawableResource(R.drawable.bg);


        explain = "巡邏: \n\n藉由巡邏神殿可以累積馬納值，若對象是友方族人佔有的神殿，則可以為神殿補充馬納值。\n\n淨化: \n\n"
                + "透過淨化消耗馬納值以將暗黑勢力驅離、佔領神殿。\n\n聖水: \n\n"
                + "共有紅、黃、藍三色聖水，可以在執行淨化時選用，藉以強化淨化效果。聖水可經由前一天的活躍程度來獲得，亦可用金幣購買。\n"
                + "前一天巡邏神殿15座未達30座，可得10瓶紅色。\n前一天巡邏神殿30座未達全部，可得10瓶紅色、5瓶黃色。\n前一天巡邏過包含大神殿的所有神殿，可得紅色15瓶、黃色10瓶、藍色5瓶。\n\n";
        TextView textstory = (TextView) findViewById(R.id.explain_game);
        textstory.setText(explain);
    }
}
