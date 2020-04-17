package com.newshak.di.components;

import com.newshak.MainActivity;
import com.newshak.data.discussion.DiscussionActivity;
import com.newshak.data.story.data.StoryInteractor;
import com.newshak.data.story.presenter.StoryPresenter;
import com.newshak.di.modules.DiscussionModule;
import com.newshak.di.modules.NetModule;
import com.newshak.di.modules.RetrofitModule;
import com.newshak.di.modules.StoryModule;
import com.newshak.di.scopes.UserScope;

import java.nio.channels.NetworkChannel;

import dagger.Component;
import retrofit2.Retrofit;

@UserScope
@Component(dependencies = NetComponent.class, modules = {RetrofitModule.class, StoryModule.class, DiscussionModule.class})
public interface StoryComponent {

    void inject(MainActivity activity);
    void inject(DiscussionActivity activity);
    void inject(StoryPresenter storyPresenter);
    void inject(StoryInteractor storyInteractor);
}
