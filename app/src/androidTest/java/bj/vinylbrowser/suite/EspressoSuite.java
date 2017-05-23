package bj.vinylbrowser.suite;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

import bj.vinylbrowser.artist.ArtistActivityMockPresenterTest;
import bj.vinylbrowser.artistreleases.ArtistReleasesMockNetworkTest;
import bj.vinylbrowser.label.LabelActivityMockPresenterTest;
import bj.vinylbrowser.login.LoginActivityMockPresenterTest;
import bj.vinylbrowser.main.MainActivityTest;
import bj.vinylbrowser.marketplace.MarketplaceActivityMockPresenterTest;
import bj.vinylbrowser.master.MasterActivityMockPresenterTest;
import bj.vinylbrowser.order.OrderActivityMockPresenterTest;
import bj.vinylbrowser.release.ReleaseActivityMockPresenterTest;
import bj.vinylbrowser.search.SearchActivityTest;
import bj.vinylbrowser.singlelist.SingleListMockPresenterTest;

/**
 * Created by Josh Laird on 14/05/2017.
 * <p>
 * Runs all espresso tests.
 */
@RunWith(Suite.class)
@Suite.SuiteClasses({ArtistActivityMockPresenterTest.class, ArtistReleasesMockNetworkTest.class, LabelActivityMockPresenterTest.class,
        LoginActivityMockPresenterTest.class, MainActivityTest.class, MarketplaceActivityMockPresenterTest.class,
        MasterActivityMockPresenterTest.class, OrderActivityMockPresenterTest.class, ReleaseActivityMockPresenterTest.class,
        SearchActivityTest.class, SingleListMockPresenterTest.class})
public class EspressoSuite {}