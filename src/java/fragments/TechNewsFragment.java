package fragments;


import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import activities.NewsWebView;
import adapter.CardListAdapter;
import app.VolleyController;
import listners.RecyclerTouchListner;
import listners.ViewListner;
import model.News;
import news.app.com.newsreader.R;

/**
 * Created by Akshay on 3/17/2018.
 */

public class TechNewsFragment extends Fragment {
    View view;
    Context mContext;
    RecyclerView mRecyclerView;
    List<News> mNewsArticles;
    CardListAdapter mCardAdapter;
    private ProgressDialog mDialog;
    private SimpleDateFormat dateFormat=new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
    private String currentTimestamp="";
    private Date currentDateTime=null;
    private final String apiUrl="https://newsapi.org/v2/top-headlines?category=technology&country=us&pageSize=15&sortBy=popularity&apiKey=c1dab8dc05dc40a5898d731a700a605e";
    private final static String TAG= TechNewsFragment.class.getSimpleName();

    @SuppressLint("ValidFragment")
    public TechNewsFragment(Context context) {
        mContext=context;
    }

    public TechNewsFragment() {
        super();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Calendar calendar=Calendar.getInstance();
        currentTimestamp=dateFormat.format(calendar.getTime());
        Log.i(TAG,"Time in String: "+currentTimestamp);
        try {
            currentDateTime=dateFormat.parse(currentTimestamp);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        mDialog=new ProgressDialog(getContext());
        mDialog.setMessage("Loading...");
        mDialog.show();
        mNewsArticles=new ArrayList<>();
        fetchRequest();
    }



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.tech_news_fragment,container,false);
        mRecyclerView=(RecyclerView)view.findViewById(R.id.main_recycler_view);
        mCardAdapter=new CardListAdapter(getContext(),mNewsArticles);
        RecyclerView.LayoutManager layoutManager=new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), LinearLayoutManager.VERTICAL));
        mRecyclerView.addOnItemTouchListener(new RecyclerTouchListner(getContext(), mRecyclerView, new ViewListner() {
            @Override
            public void onClick(View view, int position) {

                Intent newsIntent=new Intent(getActivity(), NewsWebView.class);
                Bundle newsDataBundle=new Bundle();
                newsDataBundle.putString("newsUrl",mNewsArticles.get(position).getArticleUrl());
                newsIntent.putExtras(newsDataBundle);
                startActivity(newsIntent);
            }
            @Override
            public void onLongClick(View view, int position) {
            }
        }));
        mRecyclerView.setAdapter(mCardAdapter);
        return view;
    }


    private void fetchRequest() {

        JsonObjectRequest rootJsonObjReq=new JsonObjectRequest(Request.Method.GET, apiUrl, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
               // Log.i(TAG,response.toString());
                mDialog.hide();
                try {
                    JSONArray articlesTab = response.getJSONArray("articles");
                    for(int i=0;i<articlesTab.length();i++){
                        JSONObject singleObj=articlesTab.getJSONObject(i);
                        News news=new News();
                        news.setThumbnailUrl(singleObj.getString("urlToImage"));
                        news.setTitle(singleObj.getString("title"));
                        news.setDescription(singleObj.getString("description"));
                        Log.i(TAG,singleObj.getString("description"));
                        Date d1=dateFormat.parse(singleObj.getString("publishedAt"));
                        long diff=Math.abs(currentDateTime.getTime()-d1.getTime());
                        diff=diff/(60*1000)%60;
                        Log.i(TAG,"Diff: "+diff);
                        if(diff>59) {
                            diff = diff / 60;
                            news.setTimestamp(" - "+diff+"h ago");
                        }else {
                            news.setTimestamp(" - "+diff + "m ago");
                        };
                        news.setArticleUrl(singleObj.getString("url"));
                        news.setSource(singleObj.getJSONObject("source").getString("name"));

                        mNewsArticles.add(news);
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

        VolleyController.getVolleyInstance(getContext()).addToRequestQueue(rootJsonObjReq);
    }
}
