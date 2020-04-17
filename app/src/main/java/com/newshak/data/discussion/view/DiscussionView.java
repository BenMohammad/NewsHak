package com.newshak.data.discussion.view;

import android.view.View;

import com.newshak.data.discussion.model.Discussion;
import com.newshak.data.story.model.Story;

import java.util.ArrayList;

public interface DiscussionView {

    public void init();
    public void loadView();
    public void setCommentHeader(Story story);
    public void setCollapseToolbar(String title);
    public void displayOfflineSnackBar();
    public void setProgressBarVisible();
    public void setProgressBarGone();
    public void sayNoComment();
    public void setAdapter(ArrayList<Discussion> discussionArrayList);
    public void fabButtonSetUp();
    public void fabButtonLink(View v);
    public void shareLink();
}
