package com.anondev.gaurav.lnotespdfsample;

import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.anondev.gaurav.lnotespdf.LNotesPDFCreator;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(android.R.id.content).post(new Runnable() {
            @Override
            public void run() {
                LNotesPDFCreator lNotesPDFCreator=new LNotesPDFCreator(MainActivity.this);
                lNotesPDFCreator.add(findViewById(android.R.id.content).getRootView());
                lNotesPDFCreator.add(findViewById(android.R.id.content).getRootView());
                lNotesPDFCreator.add(findViewById(android.R.id.content).getRootView());
                lNotesPDFCreator.add(findViewById(android.R.id.content).getRootView());
                lNotesPDFCreator.add(findViewById(android.R.id.content).getRootView());
                lNotesPDFCreator.savePDF(getExternalFilesDir("TEMP").getPath()+"/a.pdf");
            }
        });

    }
}
