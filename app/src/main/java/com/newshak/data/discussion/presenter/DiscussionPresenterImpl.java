package com.newshak.data.discussion.presenter;

import android.app.Application;
import android.content.Context;

import com.newshak.StoryInterface;
import com.newshak.data.discussion.DiscussionInteractor;
import com.newshak.data.discussion.model.Discussion;
import com.newshak.data.discussion.view.DiscussionView;
import com.newshak.data.story.model.Story;
import com.newshak.util.Logger;
import com.newshak.util.NetworkUtil;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

public class DiscussionPresenterImpl implements DiscussionPresenter {

    private final Logger logger = Logger.getLogger(getClass());
    private final Application application;
    private DiscussionView discussionView;
    private DiscussionInteractor discussionInteractor;

    private Observable<List<Discussion>> mCommentListObservable;
    private ArrayList<Discussion> discussionArrayList;

    public DiscussionPresenterImpl(Application application, DiscussionInteractor discussionInteractor) {
        this.application = application;
        this.discussionInteractor = discussionInteractor;
    }

    @Override
    public void setView(DiscussionView discussionView) {
        this.discussionView = discussionView;
    }

    @Override
    public void getComments(StoryInterface mInterface, CompositeDisposable compositeDisposable, Context context, Story story, boolean updateObservable) {
        if(!NetworkUtil.isConnected(context)) {
            discussionView.displayOfflineSnackBar();
            return;
        }

        if(story.getKids() != null && !story.getKids().isEmpty()) {
            if(mCommentListObservable == null || updateObservable) {
                mCommentListObservable = discussionInteractor.fetchComment(mInterface, 0, story).cache();
            }

            compositeDisposable.add(mCommentListObservable
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeWith(new DisposableObserver <List<Discussion>>() {
                        @Override
                        public void onNext(List<Discussion> discussions) {
                            if(discussions != null) {
                                discussionArrayList = new ArrayList<>(discussions);
                            }
                        }

                        @Override
                        public void onError(Throwable e) {
                            logger.debug(e.getLocalizedMessage());
                            discussionView.setProgressBarGone();
                        }

                        @Override
                        public void onComplete() {
                            discussionView.setProgressBarGone();
                            discussionView.setAdapter(discussionArrayList);
                        }
                    }));
        } else {
            discussionView.sayNoComment();
        }
    }
}
