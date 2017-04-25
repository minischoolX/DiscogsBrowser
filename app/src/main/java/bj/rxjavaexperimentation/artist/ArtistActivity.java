package bj.rxjavaexperimentation.artist;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import com.thefinestartist.finestwebview.FinestWebView;

import javax.inject.Inject;

import bj.rxjavaexperimentation.AppComponent;
import bj.rxjavaexperimentation.R;
import bj.rxjavaexperimentation.artistreleases.ArtistReleasesActivity;
import bj.rxjavaexperimentation.common.BaseActivity;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Josh Laird on 07/04/2017.
 */
public class ArtistActivity extends BaseActivity implements ArtistContract.View
{
    private static final String TAG = "DetailedActivity";
    @BindView(R.id.recyclerView) RecyclerView rvDetailed;
    @BindView(R.id.toolbar) Toolbar toolbar;
    @Inject ArtistPresenter presenter;

    @Override
    public void setupComponent(AppComponent appComponent)
    {
        ArtistComponent component = DaggerArtistComponent.builder()
                .appComponent(appComponent)
                .artistModule(new ArtistModule(this))
                .build();
        component.inject(this);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recyclerview);
        ButterKnife.bind(this);
        setupToolbar(toolbar);
        presenter.setupRecyclerView(this, rvDetailed, getIntent().getStringExtra("title"));
        presenter.getData(getIntent().getStringExtra("id"));
    }

    @Override
    public void showMemberDetails(String name, String id)
    {
        Intent intent = new Intent(this, ArtistActivity.class);
        intent.putExtra("title", name);
        intent.putExtra("id", id);
        startActivity(intent);
    }

    @Override
    protected void onPause()
    {
        super.onPause();
        presenter.unsubscribe();
    }

    @Override
    public void showLink(String link)
    {
        new FinestWebView.Builder(this).show(link);
    }

    @Override
    public void showArtistReleases(String title, String id)
    {
        Intent intent = new Intent(this, ArtistReleasesActivity.class);
        intent.putExtra("name", title);
        intent.putExtra("id", id);
        startActivity(intent);
    }
}
