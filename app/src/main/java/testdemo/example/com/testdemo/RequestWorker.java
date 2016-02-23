package testdemo.example.com.testdemo;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import java.util.Iterator;
import java.util.Map;


/**
 * Created by tset on 16/2/5.
 */
public class RequestWorker {
    private static final String TAG = "RequestWorker";

    private static RequestWorker sInstance;
    private static Object mLock = new Object();
    private RequestQueue mRequestQueue;
    private static Context mContext;

    private static boolean sIsInit;

    public static RequestWorker getInstance(){
        if(!sIsInit){
            Log.e(TAG,"Please init first!");
            try {
                throw new UnsupportedOperationException("RequestWorker did not init first!");
            } catch (Throwable throwable) {
                throwable.printStackTrace();
            }

        }
        synchronized (mLock){
            if(sInstance == null){
                sInstance = new RequestWorker();
            }
            return sInstance;
        }
    }

    public static void init(Application mApp){
        mContext = mApp.getApplicationContext();
        sIsInit = true;
    }

    private RequestWorker(){
        mRequestQueue = Volley.newRequestQueue(mContext);
    }

    public RequestQueue getRequestQueue(){
        return mRequestQueue;
    }

    public void doRequest(final SimpleRequest.RequestEntry requestEntry,Response.Listener listener,Response.ErrorListener errorListener,Class clazz){
        Log.d(TAG,requestEntry.toString());
        int method = requestEntry.method;
        StringBuilder builder = new StringBuilder();
        if(requestEntry.url != null){
            builder.append(requestEntry.url);
        }else{
            Log.e(TAG,"requestEntry url is null");
            return;
        }

        SimpleRequest request = null;
        if(method == Request.Method.GET){
            builder.append("?").append(parseMap2String(requestEntry.paramMap));
            request = new SimpleRequest(Request.Method.GET, clazz, builder.toString(), listener, errorListener);
        }else if(method == Request.Method.POST){
            request = new SimpleRequest(Request.Method.POST, clazz, builder.toString(), listener, errorListener){

                @Override
                public int compareTo(Object another) {
                    return 0;
                }

                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    return requestEntry.paramMap;
                }
            };

        }else{
            Log.e(TAG,"not handler this method!");
        }
        if(null != request){
            getRequestQueue().add(request);
        }
    }

    private String parseMap2String(Map<String,String> pMap){
        if(pMap == null || pMap.isEmpty()){
            Log.e(TAG,"map is null or is empty!");
            return "";
        }
        StringBuilder builder = new StringBuilder();
        Iterator<Map.Entry<String,String>> it = pMap.entrySet().iterator();
        while (it.hasNext()){
            Map.Entry<String,String> entry = it.next();
            builder.append(entry.getKey()).append("=").append(entry.getValue()).append("&");
        }
        String mapString = builder.toString();
        if(mapString.endsWith("&")){
            mapString.substring(0,mapString.length()-1);
        }
        Log.d(TAG,mapString);
        return mapString;
    }
}
