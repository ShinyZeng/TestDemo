package testdemo.example.com.testdemo;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.nostra13.universalimageloader.core.ImageLoader;

import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by tset on 16/2/3.
 */
public class RxJavaActivity extends Activity{
    private static final String TAG = "RxJavaActivity";
    private static String mAppKey = "98643d05ee3de747993aaaedbb6272e6";
    private Subscription sb;

    private Subscription sb2;
    private ImageView logoImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rx_java_layout);

        logoImageView = (ImageView) findViewById(R.id.logo_iv);

        String[] strs = {"Hello","world","!"};
        String str2 = "Hello,world,Rx,Java,!";
        sb = Observable.from(strs).subscribe(new Subscriber<String>() {
            @Override
            public void onCompleted() {
                Log.d(TAG,"onCompleted");
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override

            public void onNext(String s) {
                Log.d(TAG, s);
            }
        });


        sb2 = Observable.just(str2).flatMap(new Func1<String, Observable<String>>() {

            @Override
            public Observable<String> call(String s) {
                return Observable.from(s.split(","));
            }
        }).subscribe(new Action1<String>() {
            @Override
            public void call(String s) {
                Log.d(TAG,s);
            }
        });

        requestNBADetial();

        //TODO:RxJava 获取img［］
        getObserableImage().map(new Func1<String, Bitmap>() {
            @Override
            public Bitmap call(String s) {
                //同步获取图片，将这个放在io线程
                Bitmap bitmap = ImageLoader.getInstance().loadImageSync(s);
                return bitmap;
            }
        }).subscribeOn(Schedulers.io()).observeOn(Schedulers.newThread()).map(new Func1<Bitmap, Bitmap>() {
            @Override
            public Bitmap call(Bitmap bitmap) {
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                return bitmap;
            }
        }).observeOn(AndroidSchedulers.mainThread()).subscribe(new Action1<Bitmap>() {
            @Override
            public void call(Bitmap bitmap) {
                Log.d(TAG,"subscribe :"+bitmap.toString());
                logoImageView.setImageBitmap(bitmap);
            }
        });

    }

    private Observable<String> getObserableImage(){
        String[] images = getResources().getStringArray(R.array.images);
        return Observable.from(images);

    }

    private void requestNBADetial() {
        SimpleRequest.RequestEntry requestEntry = new SimpleRequest.RequestEntry();
        requestEntry.url = "http://v.juhe.cn/nba/team_info_byId.php";
        requestEntry.method = Request.Method.GET;
        requestEntry.put("key",mAppKey);
        requestEntry.put("team_id", "1");
        RequestWorker.getInstance().doRequest(requestEntry, new Response.Listener<TeamEntry>() {

            @Override
            public void onResponse(TeamEntry response) {
                Log.d(TAG,response.toString());
                Log.d(TAG,response.result.logoLink);
                ImageLoader.getInstance().displayImage(response.result.logoLink,logoImageView);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d(TAG,"error!");
            }
        },TeamEntry.class);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        sb.unsubscribe();
        sb2.unsubscribe();
    }
}
