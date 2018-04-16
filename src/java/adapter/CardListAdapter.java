package adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.toolbox.NetworkImageView;

import java.util.List;

import app.VolleyController;
import model.News;
import news.app.com.newsreader.R;

/**
 * Created by Akshay on 3/8/2018.
 */

public class CardListAdapter extends RecyclerView.Adapter<CardListAdapter.NewsItemViewHolder> {

    List<News> mNewsArticles;
    Context mContext;

    public CardListAdapter(Context context, List<News> newsArticles) {
        this.mNewsArticles = newsArticles;
        this.mContext =context;
    }

    @Override
    public NewsItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.news_item_holder,parent,false);
        return new NewsItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(NewsItemViewHolder holder, int position) {
        News news= mNewsArticles.get(position);
        holder.newsThumnailView.setImageUrl(news.getThumbnailUrl(),VolleyController.getVolleyInstance(mContext).getImageLoader());
        holder.title.setText(news.getTitle());
        holder.source.setText(news.getSource());
        holder.timestamp.setText(news.getTimestamp());
    }

    @Override
    public int getItemCount() {
        return mNewsArticles.size();
    }

    public class NewsItemViewHolder extends RecyclerView.ViewHolder {
        TextView title,source,timestamp;
        NetworkImageView newsThumnailView;

        public NewsItemViewHolder(View newsViewHolder) {
            super(newsViewHolder);
            newsThumnailView=(NetworkImageView)newsViewHolder.findViewById(R.id.thumbnail);
            title=(TextView)newsViewHolder.findViewById(R.id.news_title);
            source=(TextView)newsViewHolder.findViewById(R.id.source);
            timestamp=(TextView)newsViewHolder.findViewById(R.id.timestamp);
        }
    }
}
