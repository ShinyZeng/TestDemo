package testdemo.example.com.testdemo;

import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

import java.lang.ref.WeakReference;

public class MainActivity extends AppCompatActivity {

    public static final int SMS_VERIFY_CODE = 1;

    private SmsObserver mSmsObserver;
    private EditText mVerifyEt;
    private Handler mHandler = new MyHandler(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mSmsObserver = new SmsObserver(this,null);

        mVerifyEt = (EditText)findViewById(R.id.et);

        Uri uri = Uri.parse("content://sms");
        getContentResolver().registerContentObserver(uri, true, mSmsObserver);
    }

    @Override
    protected void onPause() {
        super.onPause();
        getContentResolver().unregisterContentObserver(mSmsObserver);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private static class MyHandler extends Handler{

        private WeakReference<MainActivity> ref;

        public MyHandler(MainActivity activity){
            ref = new WeakReference<MainActivity>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            MainActivity act = ref.get();
            if(act == null){
                return;
            }
            switch (msg.what){
                case SMS_VERIFY_CODE:
                    act.mVerifyEt.setText((String) msg.obj);
                    break;
            }
        }
    }
}
