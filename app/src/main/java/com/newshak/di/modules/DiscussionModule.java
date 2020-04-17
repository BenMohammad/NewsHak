package com.newshak.di.modules;

import android.app.Application;

import com.newshak.data.discussion.data.DiscussionInteractor;
import com.newshak.data.discussion.data.DiscussionInteractorImpl;
import com.newshak.data.discussion.presenter.DiscussionPresenter;
import com.newshak.data.discussion.presenter.DiscussionPresenterImpl;

import dagger.Module;
import dagger.Provides;

@Module
public class DiscussionModule {

    private Application application;

    public DiscussionModule(Application application) {
        this.application = application;
    }

    @Provides
    public DiscussionPresenter getDiscussionPresenter(DiscussionInteractor discussionInteractor) {
        return new DiscussionPresenterImpl(application, discussionInteractor);
    }

    @Provides
    public DiscussionInteractor provideDiscussionInteractor() {
        return new DiscussionInteractorImpl(application);
    }
}
