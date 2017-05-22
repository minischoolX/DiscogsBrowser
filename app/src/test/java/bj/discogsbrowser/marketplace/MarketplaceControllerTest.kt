package bj.discogsbrowser.marketplace

import android.content.Context
import android.os.Build.VERSION_CODES.LOLLIPOP
import bj.discogsbrowser.model.listing.Listing
import bj.discogsbrowser.model.user.UserDetails
import bj.discogsbrowser.utils.ImageViewAnimator
import bj.discogsbrowser.wrappers.NumberFormatWrapper
import com.nhaarman.mockito_kotlin.mock
import junit.framework.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.MockitoAnnotations
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

/**
 * Created by Josh Laird on 22/05/2017.
 */
@RunWith(RobolectricTestRunner::class)
@Config(sdk = intArrayOf(LOLLIPOP), manifest = Config.NONE)
class MarketplaceControllerTest {
    lateinit var controller: MarketplaceController
    val context: Context = mock()
    val view: MarketplaceContract.View  = mock()
    val imageViewAnimator: ImageViewAnimator = mock()
    val numberFormatWrapper: NumberFormatWrapper = mock()

    @Before
    fun setup() {
        MockitoAnnotations.initMocks(this)
        controller = MarketplaceController(context, view, imageViewAnimator, numberFormatWrapper)
        controller.requestModelBuild()
    }

    @Test
    fun initialLoadingState_correct() {
        val copyOfModels = controller.adapter.copyOfModels
        assertEquals(copyOfModels[0].javaClass.simpleName, "MarketplaceModelTop_")
        assertEquals(copyOfModels[1].javaClass.simpleName, "DividerModel_")
        assertEquals(copyOfModels[2].javaClass.simpleName, "LoadingModel_")
        assertEquals(copyOfModels.size, 3)
    }

    @Test
    fun errorThenValid_displaysValid() {
        controller.setError(true)
        var copyOfModels = controller.adapter.copyOfModels
        assertEquals(copyOfModels[0].javaClass.simpleName, "MarketplaceModelTop_")
        assertEquals(copyOfModels[1].javaClass.simpleName, "DividerModel_")
        assertEquals(copyOfModels[2].javaClass.simpleName, "RetryModel_")
        assertEquals(copyOfModels.size, 3)

        controller.setLoading(true)
        copyOfModels = controller.adapter.copyOfModels
        assertEquals(copyOfModels[0].javaClass.simpleName, "MarketplaceModelTop_")
        assertEquals(copyOfModels[1].javaClass.simpleName, "DividerModel_")
        assertEquals(copyOfModels[2].javaClass.simpleName, "LoadingModel_")
        assertEquals(copyOfModels.size, 3)

        controller.setListing(Listing())
        copyOfModels = controller.adapter.copyOfModels
        assertEquals(copyOfModels[0].javaClass.simpleName, "MarketplaceModelTop_")
        assertEquals(copyOfModels[1].javaClass.simpleName, "DividerModel_")
        assertEquals(copyOfModels[2].javaClass.simpleName, "MarketplaceModelCenter_")
        assertEquals(copyOfModels[3].javaClass.simpleName, "DividerModel_")
        assertEquals(copyOfModels[4].javaClass.simpleName, "WholeLineModel_")
        assertEquals(copyOfModels[5].javaClass.simpleName, "DividerModel_")
        assertEquals(copyOfModels[6].javaClass.simpleName, "WholeLineModel_")
        assertEquals(copyOfModels.size, 7)

        controller.setSellerDetails(UserDetails()) // This doesn't create any new models
        copyOfModels = controller.adapter.copyOfModels
        assertEquals(copyOfModels[0].javaClass.simpleName, "MarketplaceModelTop_")
        assertEquals(copyOfModels[1].javaClass.simpleName, "DividerModel_")
        assertEquals(copyOfModels[2].javaClass.simpleName, "MarketplaceModelCenter_")
        assertEquals(copyOfModels[3].javaClass.simpleName, "DividerModel_")
        assertEquals(copyOfModels[4].javaClass.simpleName, "WholeLineModel_")
        assertEquals(copyOfModels[5].javaClass.simpleName, "DividerModel_")
        assertEquals(copyOfModels[6].javaClass.simpleName, "WholeLineModel_")
        assertEquals(copyOfModels.size, 7)
    }
}