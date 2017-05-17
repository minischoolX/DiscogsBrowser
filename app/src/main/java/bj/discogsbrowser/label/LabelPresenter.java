package bj.discogsbrowser.label;

import android.support.annotation.NonNull;

import bj.discogsbrowser.utils.schedulerprovider.MySchedulerProvider;

/**
 * Created by Josh Laird on 23/04/2017.
 */
public class LabelPresenter implements LabelContract.Presenter
{
    private LabelController controller;
    private bj.discogsbrowser.network.DiscogsInteractor discogsInteractor;
    private MySchedulerProvider mySchedulerProvider;

    public LabelPresenter(@NonNull LabelController controller, @NonNull bj.discogsbrowser.network.DiscogsInteractor discogsInteractor,
                          @NonNull MySchedulerProvider mySchedulerProvider)
    {
        this.controller = controller;
        this.discogsInteractor = discogsInteractor;
        this.mySchedulerProvider = mySchedulerProvider;
    }

    /**
     * Fetch label details from discogs.
     *
     * @param id Label ID.
     */
    @Override
    public void fetchReleaseDetails(String id)
    {
        discogsInteractor.fetchLabelDetails(id)
                .doOnSubscribe(onSubscribe -> controller.setLoading(true))
                .subscribeOn(mySchedulerProvider.ui())
                .flatMap(label ->
                {
                    controller.setLabel(label);
                    return discogsInteractor.fetchLabelReleases(id)
                            .subscribeOn(mySchedulerProvider.io());
                })
                .observeOn(mySchedulerProvider.ui())
                .subscribe(labelReleases ->
                        controller.setLabelReleases(labelReleases), error ->
                        controller.setError(true));
    }
}
