package bj.vinylbrowser.release

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import bj.vinylbrowser.App
import bj.vinylbrowser.AppComponent
import bj.vinylbrowser.R
import bj.vinylbrowser.common.BaseController
import bj.vinylbrowser.label.LabelController
import bj.vinylbrowser.marketplace.MarketplaceController
import bj.vinylbrowser.model.listing.ScrapeListing
import bj.vinylbrowser.utils.ArtistsBeautifier
import bj.vinylbrowser.utils.analytics.AnalyticsTracker
import com.bluelinelabs.conductor.RouterTransaction
import com.bluelinelabs.conductor.changehandler.FadeChangeHandler
import kotlinx.android.synthetic.main.content_main.view.*
import java.io.IOException
import javax.inject.Inject

/**
 * Created by Josh Laird on 30/05/2017.
 */
class ReleaseController(val title: String, val id: String) : BaseController(), ReleaseContract.View {
    @Inject lateinit var artistsBeautifier: ArtistsBeautifier
    @Inject lateinit var presenter: ReleasePresenter
    @Inject lateinit var tracker: AnalyticsTracker
    @Inject lateinit var controller: ReleaseEpxController

    constructor(args: Bundle) : this(args.getString("title"), args.getString("id"))

    override fun setupComponent(appComponent: AppComponent) {
        appComponent
                .releaseComponentBuilder()
                .releaseModule(ReleaseModule(this))
                .build()
                .inject(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup): View {
        setupComponent(App.appComponent)
        val view = inflater.inflate(R.layout.activity_recyclerview, container, false)
        setupToolbar(view.toolbar, "")
        setupRecyclerView(view.recyclerView, controller)
        controller.setTitle(title)
        controller.requestModelBuild()
        presenter.fetchArtistDetails(id)
        return view
    }

    override fun onAttach(view: View) {
        tracker.send(applicationContext!!.getString(R.string.release_activity), applicationContext!!.getString(R.string.release_activity), applicationContext!!.getString(R.string.loaded), "onResume", "1")
        super.onAttach(view)
    }

    override fun retry() {
        tracker.send(applicationContext!!.getString(R.string.release_activity), applicationContext!!.getString(R.string.release_activity), applicationContext!!.getString(R.string.clicked), "retryRelease", "1")
        presenter.fetchArtistDetails(id)
    }

    override fun displayListingInformation(title: String?, subtitle: String?, scrapeListing: ScrapeListing?) {
        tracker.send(applicationContext!!.getString(R.string.release_activity), applicationContext!!.getString(R.string.release_activity), applicationContext!!.getString(R.string.clicked), "setListing", "1")
        router.pushController(RouterTransaction.with(MarketplaceController(scrapeListing!!.marketPlaceId, title!!, subtitle!!, scrapeListing.sellerName))
                .popChangeHandler(FadeChangeHandler())
                .pushChangeHandler(FadeChangeHandler()))
    }

    override fun launchYouTube(uri: String?) {
        tracker.send(applicationContext!!.getString(R.string.release_activity), applicationContext!!.getString(R.string.release_activity), applicationContext!!.getString(R.string.clicked), "launchYoutube", "1")
        startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube://" + uri)))
    }

    override fun displayLabel(title: String?, id: String?) {
        tracker.send(applicationContext!!.getString(R.string.release_activity), applicationContext!!.getString(R.string.release_activity), applicationContext!!.getString(R.string.clicked), "displayLabel", "1")
        router.pushController(RouterTransaction.with(LabelController(title!!, id!!))
                .popChangeHandler(FadeChangeHandler())
                .pushChangeHandler(FadeChangeHandler()))
    }

    override fun retryCollectionWantlist() {
        tracker.send(applicationContext!!.getString(R.string.release_activity), applicationContext!!.getString(R.string.release_activity), applicationContext!!.getString(R.string.clicked), "retryWantlistCollection", "1")
        presenter.checkCollectionWantlist()
    }

    override fun retryListings() {
        try {
            tracker.send(applicationContext!!.getString(R.string.release_activity), applicationContext!!.getString(R.string.release_activity), applicationContext!!.getString(R.string.clicked), "retryListings", "1")
            presenter.fetchReleaseListings(id)
        } catch (e: IOException) {
        }
    }
}