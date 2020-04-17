package com.newshak.data.discussion.presenter;

import android.content.Context;

import com.newshak.StoryInterface;
import com.newshak.data.discussion.view.DiscussionView;
import com.newshak.data.story.model.Story;

import io.reactivex.disposables.CompositeDisposable;

public interface DiscussionPresenter {

    public void setView(DiscussionView discussionView);
    public void getComments(final StoryInterface mInterface, CompositeDisposable compositeDisposable,
                            Context context, final Story story, final boolean updateObservable);
}
