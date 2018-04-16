package app;

import android.app.Application;
import android.content.Context;
import android.graphics.Bitmap;
import android.util.LruCache;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;

/**
 * Created by Akshay on 3/6/2018.
 */

public class VolleyController {
    private static final String TAG=VolleyController.class.getSimpleName();
    private RequestQueue mRequestQueue;
    private ImageLoader mImageLoader;
    private Context mCtx;
    private static VolleyController mVolleyInstance;

    public VolleyController(Context mCtx) {
        this.mCtx = mCtx;
    }

    public static synchronized VolleyController getVolleyInstance(Context ctx){
        if(mVolleyInstance==null)
            mVolleyInstance=new VolleyController(ctx);
        return mVolleyInstance;
    }


    public RequestQueue getRequestQueue(){
        if(mRequestQueue==null)
            mRequestQueue= Volley.newRequestQueue(mCtx.getApplicationContext());

        return  mRequestQueue;
    }


    //get Image loader
    public ImageLoader getImageLoader(){
        getRequestQueue();
        if(mImageLoader==null)
            mImageLoader=new ImageLoader(this.mRequestQueue, new ImageLoader.ImageCache() {

                private  final LruCache<String,Bitmap>cache=new LruCache<>(30);
                @Override
                public Bitmap getBitmap(String url) {
                    return cache.get(url);
                }

                @Override
                public void putBitmap(String url, Bitmap bitmap) {
                    cache.put(url,bitmap);
                }
            });

        return mImageLoader;
    }


    //add to request queue
    public <T> void addToRequestQueue(Request<T> req){
        req.setTag(TAG);
        getRequestQueue().add(req);
    }

    //cancel pending requests
    public void cancelPendingRequests(Object tag) {
        if (mRequestQueue != null) {
            mRequestQueue.cancelAll(tag);
        }
    }

}
