package com.newshak.data.story.data;

import com.newshak.StoryInterface;
import com.newshak.data.story.model.Story;

import java.util.List;

import io.reactivex.Observable;

public interface StoryInteractor {

    public void sayHello();

    public Observable<Story> getStory(StoryInterface storyInterface, List<Long> storyIds);

    public Observable<List<Story>> sublistStories(StoryInterface storyInterface, final List<Long> storyIds);

    public List<Story> sortStories(List<Story> storyList, List<Long> storyIds);
}
