package com.example.louise.test;

import android.app.ListActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;

import java.util.ArrayList;
import java.util.List;

public class PrisonActivity extends ListActivity {
    private String[] mFruits = {
            "Sinae 的 Apple", "Antayen 的 orange", "Antayen 的 bannana", "Sinae 的 pear", "Sinae 的 kiwi", "Antayen 的 grapes", "Sinae 的 strawberry"
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prison);
        
        setListAdapter(new ArrayAdapter<String>(this, R.layout.list_item, R.id.label, mFruits));
    }

}
