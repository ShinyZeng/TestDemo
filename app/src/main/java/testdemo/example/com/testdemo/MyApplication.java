package testdemo.example.com.testdemo;

import android.app.Application;

import com.google.gson.Gson;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

/**
 * Created by tset on 16/2/4.
 */
public class MyApplication extends Application{

    @Override
    public void onCreate() {
        super.onCreate();
        RequestWorker.init(this);
        initImageLoader();
    }

    private void initImageLoader(){
        ImageLoaderConfiguration configuration = ImageLoaderConfiguration
                .createDefault(this);
        //Initialize ImageLoader with configuration.
        ImageLoader.getInstance().init(configuration);
    }
}
