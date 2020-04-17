package com.newshak.adapter;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.newshak.R;
import com.newshak.data.discussion.DiscussionActivity;
import com.newshak.data.story.model.Story;
import com.newshak.util.Logger;
import com.newshak.util.Misc;

import java.util.ArrayList;
import java.util.List;

public class ListingAdapter extends RecyclerView.Adapter<ListingAdapter.ViewHolder> {

    private final Logger logger = Logger.getLogger(getClass());
    private final TypedValue mTypedValue = new TypedValue();
    private int mBackground;
    private ArrayList<Story> mStory;
    Context context;
    ArrayList<String> imageUrlList = new ArrayList<>();
    RecyclerView recyclerView;
    private String aTitle;


    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final ImageView mHotStory;
        public final TextView mStoryComment, mStoryTitle, mStoryPrettyUrl, mStoryPoints, mStoryTime;

        public Typeface typeface;

        public ViewHolder(View view) {
            super(view);

            mView = view;

            mHotStory = view.findViewById(R.id.hot_story);
            mStoryComment = view.findViewById(R.id.story_comments);
            mStoryTitle = view.findViewById(R.id.story_title);
            mStoryPrettyUrl = view.findViewById(R.id.story_pretty_url);
            mStoryPoints = view.findViewById(R.id.story_point);
            mStoryTime = view.findViewById(R.id.story_time);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mStoryTitle.getText();
        }
    }

    public String getValueAt(int position) {
        return String.valueOf(mStory.get(position).getId());
    }

    public ListingAdapter(Context context, ArrayList<Story> story) {
        context.getTheme().resolveAttribute(R.attr.selectableItemBackground, mTypedValue, true);
        this.context = context;
        mBackground = mTypedValue.resourceId;
        mStory = story;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.display_list, parent, false);
        view.setBackgroundResource(mBackground);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        final Story model = (Story) mStory.get(position);

        String title = "";
        String url = "";
        Integer commentsNo = 0;
        Integer points = 0;
        Integer time = 0;

        if(model.getTitle() != null) title = model.getTitle();
        if(model.getUrl() != null) url = model.getUrl();
        if(model.getDescendants() != null) commentsNo = model.getDescendants();
        if(model.getScore() != null) points = model.getScore();
        if(model.getTime() != null) time = model.getTime();

        aTitle = title;
        int tLength = title.length();

        if(tLength >= 80) {
            title = title.substring(0, 80).toLowerCase() + "...";
            title = title.substring(0, 1).toUpperCase() + title.substring(1);
        } else {
            if(tLength > 3) {
                title = title.substring(0, tLength).toLowerCase();
                title = title.substring(0, 1).toUpperCase() + title.substring(1);
            }
        }

        holder.mStoryTitle.setText(title);
        holder.mStoryPrettyUrl.setText(url);
        holder.mStoryComment.setText(String.valueOf(commentsNo));
        holder.mStoryPoints.setText(String.valueOf(points) + context.getString(R.string.story_point_p));
        holder.mStoryTime.setText(String.valueOf(Misc.formatTime(time)));

        if(points > 50) {
            holder.mHotStory.setImageResource(R.drawable.ic_fire);
        } else {
            holder.mHotStory.setImageResource(R.drawable.ic_fire_grey);
        }

        logger.debug(String.valueOf(holder.getAdapterPosition()));

        holder.mView.setOnClickListener(v -> {
            Context context = v.getContext();
            Intent intent = new Intent(context, DiscussionActivity.class);
            intent.putExtra("position", holder.getAdapterPosition());
            intent.putExtra("mStory", mStory);
            intent.putExtra("title", aTitle);
            Activity activity = (Activity) v.getContext();
            activity.startActivityForResult(intent, 500);
            activity.overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        });
    }

    @Override
    public int getItemCount() {
        return (null != mStory ? mStory.size() : 0);
    }

    public void addAll(List<Story> data) {
        notifyDataSetChanged();
    }

    public void clear() {
        mStory.clear();
    }
}
