package com.newshak.di.modules;

import android.app.Application;

import com.newshak.data.story.data.StoryInteractor;
import com.newshak.data.story.data.StoryInteractorImpl;
import com.newshak.data.story.presenter.StoryPresenter;
import com.newshak.data.story.presenter.StoryPresenterImpl;

import dagger.Module;
import dagger.Provides;

@Module
public class StoryModule {

    private Application application;

    public StoryModule(Application application) {
        this.application = application;
    }

    @Provides
    public StoryPresenter getStoryPresenter(StoryInteractor storyInteractor) {
        return new StoryPresenterImpl(application, storyInteractor);
    }

    @Provides
    public StoryInteractor provideStoryInteractor() {
        return new StoryInteractorImpl(application);
    }
}
