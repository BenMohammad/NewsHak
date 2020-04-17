package com.newshak;

import com.newshak.data.discussion.model.Discussion;
import com.newshak.data.story.model.Story;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.Callback;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface StoryInterface {

    @GET("/{story_type}.json")
    Observable<List<Long>> getStories(@Path("story_type") String storyType);

    @GET("/item/{itemId}.json")
    Observable<Story> getStory(@Path("itemId") String itemId);

    @GET("/item/{itemId}.json")
    Observable<Discussion> getComment(@Path("itemId") long itemId);

    @GET("/{story_type}.json")
    public void getStory2(@Path("story_type") String storyType, Callback<List<Long>> response);

}
