package cn.ingenic.indroidsync.updater;

import java.io.File;
import java.io.IOException;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.RecoverySystem;
import android.util.Log;
import android.widget.TextView;

import com.fox.exercise.R;
import com.fox.exercise.SmartBarUtils;

/**
 * a dialog window  to display some notices or result of some operations.
 *
 * @author dfdun
 */
public class NoticesActivity extends Activity {

    private String update;
    private File file;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        boolean findMethod = SmartBarUtils.findActionBarTabsShowAtBottom();
        if (!findMethod) {
            // 取消ActionBar拆分，换用TabHost
            getWindow().setUiOptions(0);
            getWindow().requestFeature(android.view.Window.FEATURE_NO_TITLE);
        }
        setContentView(R.layout.notices_activity);
        /*Button exit = (Button)this.findViewById(R.id.exit);
        exit.setOnClickListener(new OnClickListener() {
            public void onClick(View v){
                finish();
            }
        });*/
        TextView tv = (TextView) findViewById(R.id.notice_msg);
        Intent intent = getIntent();
        String msg = intent.getStringExtra("msg");
        if (msg != null)
            tv.setText(msg + "\n");
        update = this.getIntent().getStringExtra("update");
        if (update != null)
            file = new File(getIntent().getStringExtra("filename"));
        if (findMethod) {
            getActionBar().setDisplayShowHomeEnabled(false);
            getActionBar().setDisplayShowTitleEnabled(false);
            SmartBarUtils.setActionModeHeaderHidden(getActionBar(), true);
            SmartBarUtils.setActionBarViewCollapsable(getActionBar(), true);
        }
    }

    protected void onStop() {
        super.onStop();
        Log.i("OtaUpdater", "NoticesActivity] - onStop() ,RecoverySystem.installPackage");
        if (update != null) {
            try {
                RecoverySystem.installPackage(this, file);
            } catch (IOException e) {
                Log.i("OtaUpdater", "NoticesActivity] - " + e.toString());
            }
        }
    }
}
