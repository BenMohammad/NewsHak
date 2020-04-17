package com.newshak.data.discussion.data;

import android.app.Application;

import com.newshak.StoryInterface;
import com.newshak.data.discussion.model.Discussion;
import com.newshak.data.story.model.Story;
import com.newshak.util.Logger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.inject.Singleton;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;

@Singleton
public class DiscussionInteractorImpl implements DiscussionInteractor {

    private Logger logger = Logger.getLogger(getClass());
    private final Application application;
    private Observable<List<Discussion>> mCommentListObservable;

    public DiscussionInteractorImpl(Application application) {
        this.application = application;
    }


    @Override
    public Observable<List<Discussion>> fetchComment(StoryInterface mInterface, int level, Story story) {
        List<Long> allCommentsIds = story.getKids();
        long descendants = story.getDescendants();

        if(descendants > 15 && allCommentsIds.size() > 3) {
            return Observable.concat(getPartsComment(mInterface, level, allCommentsIds.subList(0, 3)),
                    getAllComments(mInterface, level, allCommentsIds.subList(3, allCommentsIds.size())));
        } else if(descendants / allCommentsIds.size() > 15) {
            return getPartsComment(mInterface, level, allCommentsIds);
        } else {
            return getAllComments(mInterface, level, allCommentsIds);
        }
    }

    @Override
    public Observable<List<Discussion>> getPartsComment(StoryInterface mInterface, int level, List<Long> cmtIds) {
        return Observable.fromIterable(cmtIds)
                .flatMap(cmtId -> getSinglePartComment(mInterface, level, cmtId));
    }

    @Override
    public Observable<List<Discussion>> getSinglePartComment(StoryInterface mInterface, int level, Long cmtId) {
        return mInterface.getComment(cmtId)
                .onErrorReturn(null)
                .filter((discussion -> (discussion != null) && !discussion.getRemoved() && discussion.getText() != null)
                )
                .flatMap(discussion -> getInnerLevelComments(mInterface, level, discussion))
                .toList()
                .map(new Function<List<Discussion>, List<Discussion>>() {
                    @Override
                    public List<Discussion> apply(List<Discussion> discussions) throws Exception {
                        List<Long> listFirstLevelComments = new ArrayList<Long>();
                        listFirstLevelComments.add(cmtId);
                        return sortComments(discussions, listFirstLevelComments);
                    }
                }).toObservable();
    }

    @Override
    public Observable<List<Discussion>> getAllComments(StoryInterface mInterface, int level, List<Long> firstLevelCmtIds) {
        return Observable.fromIterable(firstLevelCmtIds)
                .flatMap(new Function<Long, Observable<Discussion>>() {
                    @Override
                    public Observable<Discussion> apply(Long aLong) throws Exception {
                        return mInterface.getComment(aLong)
                                .onErrorReturn(new Function<Throwable, Discussion>() {
                                    @Override
                                    public Discussion apply(Throwable throwable) throws Exception {
                                        return null;
                                    }
                                });
                    }
                })
                .filter(new Predicate<Discussion>() {
                    @Override
                    public boolean test(Discussion discussion) throws Exception {
                        return (discussion != null) && !discussion.getRemoved() && discussion.getText() != null;
                    }
                })

                .flatMap(new Function<Discussion, Observable<Discussion>>() {
                    @Override
                    public Observable<Discussion> apply(Discussion discussion) throws Exception {
                        return getInnerLevelComments(mInterface, level, discussion);
                    }
                })
                .toList()
                .map(new Function<List<Discussion>, List<Discussion>>() {
                    @Override
                    public List<Discussion> apply(List<Discussion> discussions) throws Exception {
                        return sortComments(discussions, firstLevelCmtIds);
                    }
                }).toObservable();
    }

    @Override
    public Observable<Discussion> getInnerLevelComments(StoryInterface mInterface, int level, Discussion cmt) {
        if(cmt == null || cmt.getRemoved() || cmt.getText() == null) {
            return null;
        }

        cmt.setLevel(level);
        if(cmt.getKids() != null && !cmt.getKids().isEmpty()) {
            return Observable.just(cmt)
                    .mergeWith(Observable.fromIterable(cmt.getKids())
                    .flatMap(new Function<Long, Observable<Discussion>>(){
                        @Override
                        public Observable<Discussion> apply(Long aLong) throws Exception {
                            return mInterface.getComment(aLong)
                                    .onErrorReturn(new Function<Throwable, Discussion>() {
                                        @Override
                                        public Discussion apply(Throwable throwable) throws Exception {
                                            return null;
                                        }
                                    });
                        }
                    })
                    .filter(new Predicate<Discussion>() {
                        @Override
                        public boolean test(Discussion discussion) throws Exception {
                            return (discussion != null) && !discussion.getRemoved() && discussion.getText() != null;
                        }
                    })
                    .flatMap(new Function<Discussion, Observable<Discussion>>() {
                        @Override
                        public Observable<Discussion> apply(Discussion discussion) throws Exception {
                            return getInnerLevelComments(mInterface, level + 1, discussion);
                        }
                    }));
        }
        return Observable.just(cmt);
    }

    @Override
    public List<Discussion> sortComments(List<Discussion> allDiscussions, List<Long> firstLevelCmtIds) {
        HashMap<Long, Discussion> newHashMap = new HashMap<>();
        for(Discussion child : allDiscussions) {
            newHashMap.put(child.getId(), child);
        }
        List<Discussion> newListFirstLevelCmt = new ArrayList<>();
        for(Long id : firstLevelCmtIds) {
            Discussion firstLevelCmt = newHashMap.get(id);
            if(firstLevelCmt != null && !firstLevelCmt.getRemoved() && firstLevelCmt.getText() != null) {
                newListFirstLevelCmt.add(firstLevelCmt);
            }
        }
        return sortAllComments(newHashMap, newListFirstLevelCmt);
    }


    @Override
    public List<Discussion> sortAllComments(HashMap<Long, Discussion> allCommentsHashMap, List<Discussion> listOfDiscussions) {
        List<Discussion> sortedDiscussionList = new ArrayList<>();

        for(Discussion discussion : listOfDiscussions) {
            sortedDiscussionList.add(discussion);
            if(discussion.getKids() != null && discussion.getKids().size() > 0) {
                List<Discussion> innerChildList = new ArrayList<>();
                for(long id : discussion.getKids()) {
                    Discussion child = allCommentsHashMap.get(id);
                    if(child != null && !child.getRemoved() && child.getText() != null) {
                        innerChildList.add(child);
                    }
                }
                sortedDiscussionList.addAll(sortAllComments(allCommentsHashMap, innerChildList));
            }
        }
        return sortedDiscussionList;
    }
}
