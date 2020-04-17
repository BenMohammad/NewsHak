package com.newshak;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AbsListView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.material.snackbar.Snackbar;
import com.newshak.adapter.ListingAdapter;
import com.newshak.constants.Constants;
import com.newshak.data.story.data.StoryInteractor;
import com.newshak.data.story.model.Story;
import com.newshak.data.story.presenter.StoryPresenter;
import com.newshak.data.story.view.StoryView;
import com.newshak.util.NetworkUtil;
import com.newshak.util.ui.MaterialProgressBar;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.disposables.CompositeDisposable;

public class MainActivity extends AppCompatActivity implements StoryView {

    @Inject
    SharedPreferences mSharedPreferences;

    @Inject
    StoryInterface storyInterface;

    @Inject
    StoryPresenter storyPresenter;

    private RelativeLayout storyLayout;
    private RecyclerView listRecyclerView;

    private List<Story> storyList;
    private ListingAdapter adapter;

    private RelativeLayout bottomLayout;
    private LinearLayoutManager manager;

    private boolean userScrolled = true;
    int pastVisibleItems, visibleItemCount, totalItemCount;

    private int storiesLoaded = 0;
    private int page = 1;
    private ArrayList<Long> data = new ArrayList<>();

    private CompositeDisposable compositeDisposable;
    private int totalNo = 0;

    private MaterialProgressBar progressBar;
    private SwipeRefreshLayout swipeRefreshLayout;
    private Snackbar snackBarOffline;
    private int tempNo = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ((BaseApplication) getApplication()).getStoryComponent().inject(this);
        storyPresenter.setView(this);
        storyLayout = findViewById(R.id.layout_story_root);
        compositeDisposable = new CompositeDisposable();

        init();
        loadView();
    }


    @Override
    public void loadView() {
        if(NetworkUtil.isConnected(getApplicationContext())) {
            populateRecyclerView();
            implementScrollListener();
            pullToRefresh();
            hideOfflineSnackBar();
        } else {
            displayOfflineSnackBar();
        }

    }

    @Override
    public void init() {
        bottomLayout = findViewById(R.id.load_more_items);
        progressBar = findViewById(R.id.material_progress_bar);
        manager = new LinearLayoutManager(getApplicationContext());
        listRecyclerView = findViewById(R.id.stories_recyclerview);
        listRecyclerView.setHasFixedSize(true);
        listRecyclerView.setLayoutManager(manager);
    }

    @Override
    public void populateRecyclerView() {
        progressBar.setVisibility(View.VISIBLE);
        storyPresenter.getStoryIds(storyInterface, Constants.TOP_STORIES, compositeDisposable, false);
    }

    @Override
    public void pullToRefresh() {
        if(NetworkUtil.isConnected(getApplicationContext())) {

            swipeRefreshLayout = findViewById(R.id.swipeContainer);
            swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    refresh(Constants.TOP_STORIES, true);
                }
            });

            swipeRefreshLayout.setColorSchemeResources(android.R.color.holo_blue_bright,
                    android.R.color.holo_green_light,
                    android.R.color.holo_orange_light,
                    android.R.color.holo_red_light);

        } else {
            displayOfflineSnackBar();
        }
    }

    @Override
    public void refresh(String topStories, boolean refresh) {
        totalNo = 0;
        storiesLoaded = 0;
        compositeDisposable.clear();
        storyPresenter.getStoryIds(storyInterface, topStories, compositeDisposable, refresh);
    }

    @Override
    public void implementScrollListener() {
        listRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

                if(newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) {
                    userScrolled = true;
                }
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                visibleItemCount = manager.getChildCount();
                totalItemCount = manager.getItemCount();
                pastVisibleItems = manager.findFirstVisibleItemPosition();

                if(userScrolled && (visibleItemCount + pastVisibleItems) == totalItemCount) {
                    userScrolled = false;
                    page = page + 1;

                    int nextNumber = storiesLoaded + 1;
                    int remaining = totalNo - storiesLoaded;

                    if(remaining >= storiesLoaded + Constants.NO_OF_ITEMS_LOADING) {
                        storyPresenter.updateRecyclerView(storyInterface, compositeDisposable, storiesLoaded, storiesLoaded + Constants.NO_OF_ITEMS_LOADING);
                    } else {
                        storyPresenter.updateRecyclerView(storyInterface, compositeDisposable, storiesLoaded, totalNo);
                    }

                }
            }
        });
    }

    @Override
    public void setLayoutVisibility() {
        bottomLayout.setVisibility(View.VISIBLE);
    }

    @Override
    public void displayOfflineSnackBar() {
        snackBarOffline = Snackbar.make(storyLayout, R.string.no_connection_snackbar, Snackbar.LENGTH_INDEFINITE);
        TextView snackBarText = snackBarOffline.getView().findViewById(com.google.android.material.R.id.snackbar_text);
        snackBarText.setTextColor(getApplicationContext().getResources().getColor(android.R.color.white));
        snackBarOffline.setAction(R.string.snackbar_action_retry, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadView();
            }
        });
        snackBarOffline.setActionTextColor(getResources().getColor(R.color.colorPrimary));
        snackBarOffline.show();
    }

    @Override
    public void hideOfflineSnackBar() {
        if(snackBarOffline != null && snackBarOffline.isShown()) {
            snackBarOffline.dismiss();
        }
    }

    @Override
    public void doAfterFetchStory() {
        progressBar.setVisibility(View.GONE);
        bottomLayout.setVisibility(View.GONE);
        swipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void setAdapter(Integer storyLoaded, ArrayList<Story> listArrayList, ArrayList<Story> refreshedArrayList, boolean loadMore, Integer totalNum) {
        storiesLoaded = storyLoaded;
        totalNo = totalNum;

        if(listArrayList.size() != 0) {
            if(!loadMore) {
                adapter = new ListingAdapter(getApplicationContext(),listArrayList);
                listRecyclerView.setAdapter(adapter);
                adapter.notifyDataSetChanged();
            } else {
                adapter.addAll(refreshedArrayList);
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }
}
