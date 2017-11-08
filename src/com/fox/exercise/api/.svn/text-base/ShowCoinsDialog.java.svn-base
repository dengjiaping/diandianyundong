package com.fox.exercise.api;

import android.app.Activity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.fox.exercise.R;

public class ShowCoinsDialog extends Activity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.coins_dialog);
        TextView msg = (TextView) findViewById(R.id.message);
        if (getIntent() != null)
            msg.setText(getIntent().getStringExtra("message"));
        findViewById(R.id.bt_ok).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                finish();
            }
        });
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        // TODO Auto-generated method stub
        finish();
        return super.onTouchEvent(event);
    }

}
