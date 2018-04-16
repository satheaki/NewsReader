package activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import adapter.CardListAdapter;
import adapter.CustomViewPagerAdapter;
import app.VolleyController;
import listners.ViewListner;
import model.News;
import news.app.com.newsreader.R;

public class MainActivity extends AppCompatActivity {
    TabLayout mTabLayout;
    ViewPager mViewPager;
    CustomViewPagerAdapter mPagerAdapter;
    RecyclerView mRecyclerView;
    List<News> newsArticles=new ArrayList<>();
    CardListAdapter mCardAdapter;
    private ProgressDialog mDialog;
    private SimpleDateFormat dateFormat=new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
    private String currentTimestamp="";
    private Date currentDateTime=null;
    private final String apiUrl="https://newsapi.org/v2/top-headlines?country=us&pageSize=30&apiKey=c1dab8dc05dc40a5898d731a700a605e";
    private final static String TAG=MainActivity.class.getSimpleName();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mViewPager=(ViewPager)findViewById(R.id.main_view_pager);
        mTabLayout =(TabLayout)findViewById(R.id.main_tab_layout);

        mPagerAdapter=new CustomViewPagerAdapter(getSupportFragmentManager(),MainActivity.this);
        mViewPager.setAdapter(mPagerAdapter);
        mTabLayout.setupWithViewPager(mViewPager);

        for(int i = 0; i< mTabLayout.getTabCount(); i++){
            TabLayout.Tab tab= mTabLayout.getTabAt(i);
            tab.setCustomView(mPagerAdapter.getCustomTabView(i));
        }




       /* Calendar calendar=Calendar.getInstance();
        currentTimestamp=dateFormat.format(calendar.getTime());
        Log.i(TAG,"Time in String: "+currentTimestamp);
        try {
            currentDateTime=dateFormat.parse(currentTimestamp);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        Log.i(TAG,"Time in Date: "+currentDateTime);

        mRecyclerView=(RecyclerView)findViewById(R.id.main_recycler_view);
        mCardAdapter=new CardListAdapter(getApplication(),newsArticles);
        RecyclerView.LayoutManager layoutManager=new LinearLayoutManager(getApplicationContext());
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.addOnItemTouchListener(new RecyclerTouchListner(this, mRecyclerView, new ViewListner() {
            @Override
            public void onClick(View view, int position) {
                Toast.makeText(MainActivity.this, ""+newsArticles.get(position).getTitle(), Toast.LENGTH_SHORT).show();
                Intent newsIntent=new Intent(MainActivity.this,NewsWebView.class);
                Bundle newsDataBundle=new Bundle();
                newsDataBundle.putString("newsUrl",newsArticles.get(position).getArticleUrl());
                newsIntent.putExtras(newsDataBundle);
                startActivity(newsIntent);
            }

            @Override
            public void onLongClick(View view, int position) {
                Toast.makeText(MainActivity.this, "Clicked:"+position, Toast.LENGTH_SHORT).show();
            }
        }));
        mRecyclerView.setAdapter(mCardAdapter);

        mDialog=new ProgressDialog(this);
        mDialog.setMessage("Loading...");
        mDialog.show();
        
        fetchRequest();
        
      */
    }



    private void fetchRequest() {

        JsonObjectRequest rootJsonObjReq=new JsonObjectRequest(Request.Method.GET, apiUrl, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.i(TAG,response.toString());
                mDialog.hide();
                try {
                    JSONArray articlesTab = response.getJSONArray("articles");
                    for(int i=0;i<articlesTab.length();i++){
                        JSONObject singleObj=articlesTab.getJSONObject(i);
                        News news=new News();
                        news.setThumbnailUrl(singleObj.getString("urlToImage"));
                        news.setTitle(singleObj.getString("title"));
                        news.setDescription(singleObj.getString("description"));
                        Date d1=dateFormat.parse(singleObj.getString("publishedAt"));
                        long diff=Math.abs(currentDateTime.getTime()-d1.getTime());
                        diff=diff/(60*1000)%60;
                        Log.i(TAG,"Diff: "+diff);
                        if(diff>59) {
                            diff = diff / 60;
                            news.setTimestamp(" - "+diff+"h ago");
                        }else {
                            news.setTimestamp(" - "+diff + "m ago");
                        }
                        news.setArticleUrl(singleObj.getString("url"));
                        news.setSource(singleObj.getJSONObject("source").getString("name"));

                        newsArticles.add(news);
                    }
                    mCardAdapter.notifyDataSetChanged();
                }catch (Exception e){
                    Log.d(TAG,e.toString());
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG,"ERROR:"+error.getMessage());
                mDialog.hide();
            }
        });

        VolleyController.getVolleyInstance(this).addToRequestQueue(rootJsonObjReq);
    }
}






class RecyclerTouchListner implements RecyclerView.OnItemTouchListener{

    private ViewListner listener;
    private GestureDetector gestureDetector;


    public RecyclerTouchListner(Context context,final RecyclerView recyclerView ,final ViewListner listener) {
        this.listener = listener;
        gestureDetector=new GestureDetector(context,new GestureDetector.SimpleOnGestureListener(){
            @Override
            public boolean onSingleTapUp(MotionEvent e) {
                return true;
            }

            @Override
            public void onLongPress(MotionEvent e) {
                View childView=recyclerView.findChildViewUnder(e.getX(),e.getY());
                if(childView!=null && listener!=null){
                    listener.onLongClick(childView,recyclerView.getChildAdapterPosition(childView));
                }
            }
        });
    }

    @Override
    public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
        View childView=rv.findChildViewUnder(e.getX(),e.getY());
        if(childView!=null && listener!=null&& gestureDetector.onTouchEvent(e)){
            listener.onClick(childView,rv.getChildAdapterPosition(childView));
        }
        return false;
    }

    @Override
    public void onTouchEvent(RecyclerView rv, MotionEvent e) {

    }

    @Override
    public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

    }
}