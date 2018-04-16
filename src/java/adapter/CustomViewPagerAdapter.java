package adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import activities.MainActivity;
import fragments.HeadLineFragment;
import fragments.IndiaNewsFragment;
import fragments.SportsNewsFragment;
import fragments.TechNewsFragment;
import fragments.USNewsFragment;
import news.app.com.newsreader.R;

/**
 * Created by Akshay on 3/15/2018.
 */

public class CustomViewPagerAdapter extends FragmentStatePagerAdapter {
    Context context;
    String[] tabHeaders={"Trending","US","India","Technology","Sports"};

    public CustomViewPagerAdapter(FragmentManager fragmentManager, MainActivity activity) {
        super(fragmentManager);
     this.context=activity;

    }

    @Override
    public Fragment getItem(int position) {
        switch(position){
            case 0:return new HeadLineFragment(context);
            case 1:return new USNewsFragment(context);
            case 2:return new IndiaNewsFragment(context);
            case 3:return new TechNewsFragment(context);
            case 4:return new SportsNewsFragment(context);
        }
        return null;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return tabHeaders[position];
    }

    @Override
    public int getCount() {
        return tabHeaders.length;
    }

    public View getCustomTabView(int i) {
        View tab = LayoutInflater.from(context).inflate(R.layout.tab,null);
        TextView tab_text_view=(TextView)tab.findViewById(R.id.tab_text_view);
        tab_text_view.setText(tabHeaders[i]);
        return tab;

    }
}
