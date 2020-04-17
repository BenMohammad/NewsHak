package com.newshak.data.story.presenter;

import android.app.Application;

import com.newshak.StoryInterface;
import com.newshak.constants.Constants;
import com.newshak.data.story.data.StoryInteractor;
import com.newshak.data.story.model.Story;
import com.newshak.data.story.view.StoryView;
import com.newshak.util.Logger;

import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

public class StoryPresenterImpl implements StoryPresenter {

    private final Logger logger = Logger.getLogger(getClass());
    private final Application application;

    private StoryView storyView;
    private StoryInteractor storyInteractor;

    private int totalNo = 0;
    private List<Long> listStoryId = new ArrayList<>();
    private ArrayList<Story> listArrayList = new ArrayList<>();
    private ArrayList<Story> refreshedArrayList = new ArrayList<>();
    private Observable<Story> mStoryObservable;

    public StoryPresenterImpl(Application application, StoryInteractor storyInteractor) {
        this.application = application;
        this.storyInteractor = storyInteractor;

    }
    @Override
    public void setView(StoryView storyView) {
        this.storyView = storyView;
    }

    @Override
    public void updateRecyclerView(StoryInterface storyInterface, CompositeDisposable compositeDisposable, Integer fromIndex, Integer toIndex) {
        refreshedArrayList.clear();
        storyView.setLayoutVisibility();

        logger.debug(String.valueOf(fromIndex) + " " + String.valueOf(toIndex));
        fetchStories(storyInterface, compositeDisposable, true, true, listStoryId.subList(fromIndex, toIndex));
    }



    @Override
    public void getStoryIds(StoryInterface storyInterface, String storyTypeUrl, CompositeDisposable compositeSubscription, boolean refresh) {
        if(refresh) {
            listArrayList.clear();
            refreshedArrayList.clear();
        }
        if(storyInterface != null) {
            compositeSubscription.add(storyInterface.getStories(storyTypeUrl)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(longs -> {
                listStoryId.clear();
                listStoryId.addAll(longs);
                totalNo = listStoryId.size();

                fetchStories(storyInterface, compositeSubscription, true, false, listStoryId.subList(0, Constants.NO_OF_ITEMS_LOADING));
            },throwable -> logger.error(throwable.getLocalizedMessage())

                    ));
        }
    }

    public void fetchStories(StoryInterface storyInterface, CompositeDisposable compositeDisposable, boolean updateObservable, final boolean loadMore, List<Long> list) {
        if(mStoryObservable == null || updateObservable) {
            mStoryObservable = storyInteractor.getStory(storyInterface, list).cache();
        }

        compositeDisposable.add(mStoryObservable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<Story>() {
                    @Override
                    public void onNext(Story story) {
                        if (story != null) {
                            listArrayList.add(story);
                            refreshedArrayList.add(story);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        logger.debug(e.getLocalizedMessage());
                    }

                    @Override
                    public void onComplete() {
                        logger.debug("completed");
                        storyView.doAfterFetchStory();
                        int storiesLoaded = listArrayList.size();

                        storyView.setAdapter(storiesLoaded, listArrayList, refreshedArrayList, loadMore, totalNo);
                    }
                })
        );
    }
}
