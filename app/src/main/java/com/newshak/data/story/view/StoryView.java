package com.newshak.data.story.view;

import com.newshak.data.story.model.Story;

import java.util.ArrayList;

public interface StoryView {

    public void loadView();
    public void init();
    public void populateRecyclerView();
    public void pullToRefresh();
    public void refresh(String topStories, boolean refresh);
    public void implementScrollListener();
    public void setLayoutVisibility();
    public void displayOfflineSnackBar();
    public void hideOfflineSnackBar();
    public void doAfterFetchStory();
    public void setAdapter(Integer storyLoaded, ArrayList<Story> listArrayList, ArrayList<Story> refreshedArrayList, boolean loadMore, Integer totalNum);

}
