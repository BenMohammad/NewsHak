package com.newshak;

import android.app.Application;

import com.newshak.di.components.DaggerNetComponent;
import com.newshak.di.components.DaggerStoryComponent;
import com.newshak.di.components.NetComponent;
import com.newshak.di.components.StoryComponent;
import com.newshak.di.modules.AppModule;
import com.newshak.di.modules.DiscussionModule;
import com.newshak.di.modules.NetModule;
import com.newshak.di.modules.RetrofitModule;
import com.newshak.di.modules.StoryModule;

public class BaseApplication extends Application {

    private NetComponent mNetComponent;
    private StoryComponent mStoryComponent;

    @Override
    public void onCreate() {
        super.onCreate();

        mNetComponent = DaggerNetComponent.builder()
                .appModule(new AppModule(this))
                .netModule(new NetModule())
                .build();

        mStoryComponent = DaggerStoryComponent.builder()
        .netComponent(mNetComponent)
                .retrofitModule(new RetrofitModule())
                .storyModule(new StoryModule(this))
                .discussionModule(new DiscussionModule(this))
                .build();

    }

    public NetComponent getNetComponent() {
        return mNetComponent;
    }

    public StoryComponent getStoryComponent() {
        return mStoryComponent;
    }
}
