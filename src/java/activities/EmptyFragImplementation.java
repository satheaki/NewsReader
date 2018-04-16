package activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import news.app.com.newsreader.R;

/**
 * Created by Akshay on 3/15/2018.
 */

public class EmptyFragImplementation extends Fragment {

    public  EmptyFragImplementation(){

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    public View onCreateView(LayoutInflater inflater,  ViewGroup container,  Bundle savedInstanceState) {
       View frag_view=inflater.inflate(R.layout.headline_fragment,container,false);
//        TextView tv=(TextView)frag_view.findViewById(R.id.fragment_text_view);
//        tv.setText("Frag Demo Test");

        return frag_view;
    }
}
