package bj.rxjavaexperimentation.discogs;

import android.content.Context;

import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

import javax.inject.Inject;
import javax.inject.Singleton;

import bj.rxjavaexperimentation.R;
import bj.rxjavaexperimentation.discogs.gson.RootSearchResponse;
import bj.rxjavaexperimentation.discogs.gson.release.Release;
import bj.rxjavaexperimentation.main.MainPresenter;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Josh Laird on 18/02/2017.
 * <p>
 * Interactor to interact with the Discogs API.
 */
@Singleton
public class DiscogsInteractor
{
    private Retrofit retrofit;
    private DiscogsService discogsService;
    private Context mContext;

    @Inject
    public DiscogsInteractor(Context context)
    {
        mContext = context;
        retrofit = new Retrofit.Builder()
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(context.getString(R.string.discogs_base))
                .build();

        discogsService = retrofit.create(DiscogsService.class);
    }

    public void searchDiscogs(String searchTerm, MainPresenter mainPresenter)
    {
        discogsService.getSearchResults(searchTerm, mContext.getString(R.string.token))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .flatMapIterable(RootSearchResponse::getSearchResults)
                .subscribeOn(Schedulers.io())
                .map(result -> discogsService.getRelease(result.getId(), mContext.getString(R.string.token)))
                .subscribe(new Observer<Observable<Release>>()
                {
                    @Override
                    public void onSubscribe(Disposable d)
                    {

                    }

                    @Override
                    public void onNext(Observable<Release> value)
                    {
                        mainPresenter.addToRecyclerView(value);
                    }

                    @Override
                    public void onError(Throwable e)
                    {

                    }

                    @Override
                    public void onComplete()
                    {

                    }
                });
    }
}
