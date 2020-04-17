package com.newshak.data.story.data;

import android.app.Application;

import com.newshak.StoryInterface;
import com.newshak.constants.Constants;
import com.newshak.data.story.model.Story;
import com.newshak.data.story.view.StoryView;
import com.newshak.util.Logger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.inject.Singleton;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;

@Singleton
public class StoryInteractorImpl implements StoryInteractor {

    private final Logger logger = Logger.getLogger(getClass());
    private final Application application;
    private StoryView storyView;

    private int totalNo = 0;
    private List<Long>  listStoryId = new ArrayList<>();
    private ArrayList<Story> listArrayList = new ArrayList<>();
    private ArrayList<Story> refreshedArrayList = new ArrayList<>();
    private Observable<Story> mStoryObservable;

    public StoryInteractorImpl(Application application) {
        this.application = application;
    }


    @Override
    public void sayHello() {

    }

    @Override
    public Observable<Story> getStory(StoryInterface storyInterface, List<Long> storyIds) {
        if(storyIds.size() > Constants.NO_OF_SPLIT_ITEMS * 2) {
            return Observable.concat(
                    sublistStories(storyInterface, storyIds.subList(0, Constants.NO_OF_SPLIT_ITEMS)),
                    sublistStories(storyInterface, storyIds.subList(Constants.NO_OF_SPLIT_ITEMS, Constants.NO_OF_SPLIT_ITEMS * 2)),
                    sublistStories(storyInterface, storyIds.subList(Constants.NO_OF_SPLIT_ITEMS * 2, storyIds.size())))
                    .flatMap(posts ->
                        Observable.fromIterable(posts)
                    );
        } else {
            return sublistStories(storyInterface, storyIds)
                    .flatMap(stories -> Observable.fromIterable(stories));
        }
    }

    @Override
    public Observable<List<Story>> sublistStories(StoryInterface storyInterface, List<Long> storyIds) {
        return Observable.fromIterable(storyIds)
                .flatMap(aLong -> storyInterface.getStory(String.valueOf(aLong)))
                .onErrorReturn(throwable -> null)
                .filter(story -> story != null && story.getTitle() != null)
                .toList()
                .map(stories -> sortStories(stories, storyIds)).toObservable();
    }

    @Override
    public List<Story> sortStories(List<Story> storyList, List<Long> storyIds) {
        HashMap<Long, Story> storyHashMap = new HashMap<>();
        List<Story> orderedStoryList = new ArrayList<>();
        for(Story story : storyList) {
            storyHashMap.put(story.getId(), story);
        }
        for(Long id : storyIds) {
            orderedStoryList.add(storyHashMap.get(id));

        }

        return orderedStoryList;
    }
}
