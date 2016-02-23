package testdemo.example.com.testdemo;

import android.content.Context;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Handler;
import android.util.Log;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by tset on 15/10/4.
 */
public class SmsObserver extends ContentObserver{

    private Context mContext;
    private Handler mHandler;

    /**
     * Creates a content observer.
     *
     * @param handler The handler to run {@link #onChange} on, or null if none.
     */
    public SmsObserver(Context context, Handler handler) {
        super(handler);
        mContext = context;
        mHandler = handler;
    }

    @Override
    public void onChange(boolean selfChange, Uri uri) {
        super.onChange(selfChange, uri);

        Log.e("DEBUG","SMS has Change");
        Log.e("DEBUG",uri.toString());

        if(uri.toString().equals("content://sms/raw")){
            return;
        }

        Uri inboxUri = Uri.parse("content://sms/inbox");
        Cursor c = mContext.getContentResolver().query(inboxUri,null,null,null, "date desc");
        if(c != null){
            if(c.moveToNext()){
                String body = c.getString(c.getColumnIndex("body"));
                String address = c.getString(c.getColumnIndex("address"));
                Log.e("DEBUG", body+":"+address);

                String code = "";
                Pattern pattern = Pattern.compile("(//d{6})");
                Matcher matcher = pattern.matcher(body);
                if(matcher.find()){
                    code = matcher.group(0);
                    //TODO：update UI
                    mHandler.obtainMessage(MainActivity.SMS_VERIFY_CODE, code);
                }

            }
            c.close();
        }

    }
}
