package com.newshak.di.modules;

import android.app.Application;

import com.newshak.data.story.data.StoryInteractor;
import com.newshak.data.story.data.StoryInteractorImpl;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class AppModule {

    Application application;

    public AppModule(Application application) {
        this.application = application;
    }

    @Provides
    @Singleton
    Application provideApplication() {
        return application;
    }

    @Provides
    StoryInteractor provideDataManager(StoryInteractorImpl appDataManager) {
        return appDataManager;
    }
}
