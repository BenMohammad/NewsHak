package com.newshak.data.story.presenter;

import com.newshak.StoryInterface;
import com.newshak.data.story.data.StoryInteractor;
import com.newshak.data.story.view.StoryView;

import io.reactivex.disposables.CompositeDisposable;

public interface StoryPresenter {

    public void setView(StoryView storyView);

    public void updateRecyclerView(StoryInterface storyInterface, CompositeDisposable compositeDisposable, Integer fromIndex, Integer toIndex);

    public void getStoryIds(StoryInterface storyInterface, String storyTypeUrl, CompositeDisposable compositeSubscription, boolean refresh);


}
