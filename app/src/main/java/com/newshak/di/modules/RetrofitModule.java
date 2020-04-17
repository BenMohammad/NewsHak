package com.newshak.di.modules;

import com.newshak.StoryInterface;

import dagger.Module;
import dagger.Provides;
import retrofit2.Retrofit;

@Module
public class RetrofitModule {

    @Provides
    public StoryInterface provideStoryInterface(Retrofit retrofit) {
        return retrofit.create(StoryInterface.class);
    }
}
