package com.newshak.data.discussion;

import com.newshak.StoryInterface;
import com.newshak.data.discussion.model.Discussion;
import com.newshak.data.story.model.Story;

import java.util.HashMap;
import java.util.List;

import io.reactivex.Observable;

public interface DiscussionInteractor {

    public Observable<List<Discussion>> fetchComment(StoryInterface mInterface, int level, Story story);

    public Observable<List<Discussion>> getPartsComment(final StoryInterface mInterface, final int level, List<Long> cmtIds);

    public Observable<List<Discussion>> getSinglePartComment(final StoryInterface mInterface, final int level, final Long cmtId);

    public Observable<List<Discussion>> getAllComments(final StoryInterface mInterface, int level, final List<Long> firstLevelCmtIds);

    public Observable<Discussion> getInnerLevelComments(final StoryInterface mInterface, final int level, Discussion cmt);

    public List<Discussion> sortComments(List<Discussion> allDiscussions, List<Long> firstLevelCmtIds);

    public List<Discussion> sortAllComments(HashMap<Long, Discussion> allCommentsHashMap, List<Discussion> listOfDiscussions);
}
