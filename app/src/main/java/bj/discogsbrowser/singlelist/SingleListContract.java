package bj.discogsbrowser.singlelist;

import android.support.v7.widget.RecyclerView;

import bj.discogsbrowser.common.BasePresenter;
import bj.discogsbrowser.common.SingleListView;
import io.reactivex.Observable;

/**
 * Created by Josh Laird on 16/04/2017.
 */

public interface SingleListContract
{
    interface View extends SingleListView
    {
        Observable<CharSequence> filterIntent();
    }

    interface Presenter extends BasePresenter
    {
        void getData(Integer stringId, String username);

        void setupRecyclerView(SingleListActivity singleListActivity, RecyclerView recyclerView);

        void setupFilterSubscription();
    }
}
