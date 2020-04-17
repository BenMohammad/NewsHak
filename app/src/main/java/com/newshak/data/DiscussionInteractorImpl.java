package com.newshak.data;

import com.newshak.StoryInterface;
import com.newshak.data.model.Discussion;
import com.newshak.data.model.Story;

import java.util.HashMap;
import java.util.List;
import java.util.logging.Logger;

import javax.inject.Singleton;

import io.reactivex.Observable;

@Singleton
public class DiscussionInteractorImpl implements DiscussionInteractor {



    @Override
    public Observable<List<Discussion>> fetchComment(StoryInterface mInterface, int level, Story story) {
        return null;
    }

    @Override
    public Observable<List<Discussion>> getPartsComment(StoryInterface mInterface, int level, List<Long> cmtIds) {
        return null;
    }

    @Override
    public Observable<List<Discussion>> getSinglePartComment(StoryInterface mInterface, int level, Long cmtId) {
        return null;
    }

    @Override
    public Observable<List<Discussion>> getAllComments(StoryInterface mInterface, int level, List<Long> firstLevelCmtIds) {
        return null;
    }

    @Override
    public Observable<Discussion> getInnerLevelComments(StoryInterface mInterface, int level, Discussion cmt) {
        return null;
    }

    @Override
    public List<Discussion> sortComments(List<Discussion> allDiscussions, List<Long> firstLevelCmtIds) {
        return null;
    }

    @Override
    public List<Discussion> sortAllComments(HashMap<Long, Discussion> allCommentsHashMap, List<Discussion> listOfDiscussions) {
        return null;
    }
}
