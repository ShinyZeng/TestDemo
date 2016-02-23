package testdemo.example.com.testdemo;

import android.util.Log;

import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;
import com.google.gson.Gson;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

/**
 * Shiny.zeng
 * Created by tset on 16/2/5.
 */
public class SimpleRequest<T> extends Request<T>{
    private static final String TAG = "SimpleRequest";

    private Class<T> mClass;
    private Response.Listener mListener;
    private Gson mGson;
//    private int mMethod;

    public SimpleRequest(String url, Class pClass,Response.Listener listener,Response.ErrorListener errorListener) {
        super(url, errorListener);
        mListener = listener;
        mGson = new Gson();
        mClass = pClass;
    }

    public SimpleRequest(int method, Class pClass,String url, Response.Listener listener,Response.ErrorListener errorListener) {
        super(method, url, errorListener);
        mListener = listener;
        mGson = new Gson();
        mClass = pClass;
    }


    @Override
    protected Response parseNetworkResponse(NetworkResponse response) {
        try {
            String jsonString = new String(response.data, HttpHeaderParser.parseCharset(response.headers));
            Log.d(TAG,jsonString);
            return Response.success(mGson.fromJson(jsonString,mClass),HttpHeaderParser.parseCacheHeaders(response));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return Response.error(new ParseError(e));
        }
    }

    @Override
    protected void deliverResponse(T response) {
        mListener.onResponse(response);
    }

    @Override
    public int compareTo(Request<T> other) {
        return super.compareTo(other);
    }

    public static class RequestEntry{
        public String url;
        public Map<String,String> paramMap = new HashMap<String,String>();
        public int method = Method.GET;

        public RequestEntry(){
            //TODO:做一些初始化得工作
        }

        public void put(String pKey,String pValue){
            paramMap.put(pKey,pValue);
        }

        @Override
        public String toString() {
            return "RequestEntry{" +
                    "url='" + url + '\'' +
                    ", paramMap=" + paramMap +
                    ", method=" + method +
                    '}';
        }
    }



}
