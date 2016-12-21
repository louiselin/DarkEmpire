package com.example.louise.test;

import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TabHost;

@SuppressWarnings("deprecation")
public class AwardActivity extends TabActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_award);

        addNewTab(Tab1.class, "專家\n條件");
        addNewTab(Tab2.class, "成就\n解鎖");
        addNewTab(Tab3.class, "玩家\n級別");
        addNewTab(Tab4.class, "樓主\n資格");

        getTabHost().setCurrentTab(0);
        getTabHost().requestFocus();

    }


    public void addNewTab(Class<?> cls, String tabName) {
        Intent intent = new Intent().setClass(AwardActivity.this, cls);
        TabHost.TabSpec spec = getTabHost().newTabSpec(tabName)
                .setIndicator(tabName)
                .setContent(intent);
        getTabHost().addTab(spec);
    }

}
