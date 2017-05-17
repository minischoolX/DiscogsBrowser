package bj.discogsbrowser.artist;

import android.support.annotation.NonNull;

import bj.discogsbrowser.network.DiscogsInteractor;
import bj.discogsbrowser.utils.rxmodifiers.RemoveUnwantedLinksFunction;
import bj.discogsbrowser.utils.schedulerprovider.MySchedulerProvider;
import bj.discogsbrowser.wrappers.LogWrapper;

/**
 * Created by Josh Laird on 07/04/2017.
 */
public class ArtistPresenter implements ArtistContract.Presenter
{
    private final String TAG = getClass().getSimpleName();
    private ArtistContract.View view;
    private DiscogsInteractor discogsInteractor;
    private MySchedulerProvider mySchedulerProvider;
    private LogWrapper log;
    private ArtistController artistController;
    private RemoveUnwantedLinksFunction removeUnwantedLinksFunction;

    public ArtistPresenter(@NonNull ArtistContract.View view, @NonNull DiscogsInteractor discogsInteractor,
                           @NonNull MySchedulerProvider mySchedulerProvider, @NonNull LogWrapper log, @NonNull ArtistController artistController,
                           @NonNull RemoveUnwantedLinksFunction removeUnwantedLinksFunction)
    {
        this.view = view;
        this.discogsInteractor = discogsInteractor;
        this.mySchedulerProvider = mySchedulerProvider;
        this.log = log;
        this.artistController = artistController;
        this.removeUnwantedLinksFunction = removeUnwantedLinksFunction;
    }

    /**
     * Fetch the artist's details from Discogs.
     *
     * @param id Artist ID.
     */
    @Override
    public void fetchReleaseDetails(String id)
    {
        discogsInteractor.fetchArtistDetails(id)
                .doOnSubscribe(onSubscribe -> artistController.setLoading(true))
                .subscribeOn(mySchedulerProvider.io())
                .observeOn(mySchedulerProvider.io())
                .map(removeUnwantedLinksFunction)
                .observeOn(mySchedulerProvider.ui())
                .subscribe(artist ->
                {
                    artistController.setArtist(artist);
                    log.e(TAG, artist.getProfile());
                }, error ->
                {
                    log.e(TAG, "onFetchArtistDetailsError");
                    error.printStackTrace();
                    artistController.setError(true);
                });
    }
}
