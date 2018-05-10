package com.archie.webplugin;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.archie.webplugin.ui.hybrid.SimpleHybridActivity;
import com.archie.webplugin.ui.widget.OnValidClickListener;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        intView();
    }

    private void intView() {
        Button button_one = (Button) findViewById(R.id.start_one);
        button_one.setOnClickListener(mOnValidClickListener);
    }

    private OnValidClickListener mOnValidClickListener = new OnValidClickListener() {
        @Override
        protected void onValidClick(View view) {
            int viewId = view.getId();
            switch (viewId) {
                case R.id.start_one:
                    startActivity(new Intent(MainActivity.this, SimpleHybridActivity.class));
                    break;
            }
        }
    };
}
