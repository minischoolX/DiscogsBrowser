package bj.discogsbrowser.search;

import android.support.v7.widget.RecyclerView;

import com.jakewharton.rxbinding2.support.design.widget.TabLayoutSelectionEvent;
import com.jakewharton.rxbinding2.support.v7.widget.SearchViewQueryTextEvent;

import bj.discogsbrowser.common.BasePresenter;
import bj.discogsbrowser.model.search.SearchResult;
import io.reactivex.Observable;

/**
 * Created by Josh Laird on 20/02/2017.
 */

public interface SearchContract
{
    interface View
    {
        Observable<SearchViewQueryTextEvent> searchIntent();

        Observable<TabLayoutSelectionEvent> tabIntent();

        void startDetailedActivity(SearchResult searchResult);

        void fillSearchBox(String searchTerm);

        void retry();
    }

    interface Presenter extends BasePresenter
    {
        void setupRecyclerView(RecyclerView rvResults);

        void setupSearchViewObserver();

        void setupTabObserver();

        void showPastSearches(boolean b);
    }
}
