package com.jch.thesis;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.jch.thesis.AidlClient.AidlClientActivity;


public class MainActivity extends Activity implements View.OnClickListener {

    private Button localBtn = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();
    }

    private void init() {

        localBtn = (Button) findViewById(R.id.local_btn);
        localBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.local_btn: {

                Intent intent = new Intent(MainActivity.this, AidlClientActivity.class);
                startActivity(intent);

                break;
            }
            default: {

            }

        }

    }
}
