package listners;

import android.view.View;

/**
 * Created by Akshay on 3/11/2018.
 */

public interface ViewListner {
    public void onClick(View view, int position);

    public void onLongClick(View view,int position);
}
