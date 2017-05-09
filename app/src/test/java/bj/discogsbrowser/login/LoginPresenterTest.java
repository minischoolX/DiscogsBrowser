package bj.discogsbrowser.login;

import com.github.scribejava.core.model.OAuth1AccessToken;
import com.github.scribejava.core.oauth.OAuth10aService;

import org.fuckboilerplate.rx_social_connect.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import bj.discogsbrowser.utils.SharedPrefsManager;
import bj.discogsbrowser.wrappers.RxSocialConnectWrapper;
import io.reactivex.Observable;

import static junit.framework.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

/**
 * Created by Josh Laird on 09/05/2017.
 */
@RunWith(MockitoJUnitRunner.class)
public class LoginPresenterTest
{
    private LoginPresenter presenter;
    @Mock LoginContract.View view;
    @Mock SharedPrefsManager sharedPrefsManager;
    @Mock RxSocialConnectWrapper rxSocialConnectWrapper;
    @Mock OAuth10aService oAuth10aService;
    private OAuthTokenFactory oAuthTokenFactory = new OAuthTokenFactory();

    @Before
    public void setUp()
    {
        presenter = new LoginPresenter(view, sharedPrefsManager, rxSocialConnectWrapper, oAuth10aService);
    }

    @After
    public void tearDown()
    {
        verifyNoMoreInteractions(view, sharedPrefsManager, oAuth10aService);
    }

    @Test
    public void hasUserLoggedInNoToken_returnsFalse()
    {
        OAuth1AccessToken emptyToken = oAuthTokenFactory.getEmptyToken();
        when(sharedPrefsManager.getUserOAuthToken()).thenReturn(emptyToken);

        assertEquals(presenter.hasUserLoggedIn(), false);

        assertEquals(sharedPrefsManager.getUserOAuthToken(), emptyToken);
        verify(sharedPrefsManager, times(2)).getUserOAuthToken();
    }

    @Test
    public void hasUserLoggedInToken_returnsTrue()
    {
        OAuth1AccessToken validToken = oAuthTokenFactory.getValidToken();
        when(sharedPrefsManager.getUserOAuthToken()).thenReturn(validToken);

        assertEquals(presenter.hasUserLoggedIn(), true);

        assertEquals(sharedPrefsManager.getUserOAuthToken(), validToken);
        verify(sharedPrefsManager, times(2)).getUserOAuthToken();
    }

    @Test
    public void startOAuthServiceValid_logsIn()
    {
        LoginActivity mockLoginActivity = mock(LoginActivity.class);
        when(rxSocialConnectWrapper.with(mockLoginActivity, oAuth10aService)).thenReturn(Observable.just(new Response<>(mockLoginActivity, oAuthTokenFactory.getValidToken())));

        presenter.startOAuthService(mockLoginActivity);

        verify(sharedPrefsManager).storeOAuthToken(any(OAuth1AccessToken.class));
        verify(view).finish();
    }

    @Test
    public void startOAuthServiceError_viewDisplaysError()
    {
        LoginActivity mockLoginActivity = mock(LoginActivity.class);
        when(rxSocialConnectWrapper.with(mockLoginActivity, oAuth10aService)).thenReturn(Observable.error(new Throwable()));

        presenter.startOAuthService(mockLoginActivity);

        verify(view).displayErrorDialog();
    }
}
