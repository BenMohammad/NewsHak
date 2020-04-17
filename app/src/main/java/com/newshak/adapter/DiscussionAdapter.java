package com.newshak.adapter;

import android.content.Context;
import android.text.Html;
import android.text.Spanned;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.newshak.R;
import com.newshak.data.discussion.model.Discussion;
import com.newshak.util.Misc;

import org.apache.commons.lang3.StringEscapeUtils;

import java.util.ArrayList;
import java.util.List;

public class DiscussionAdapter extends RecyclerView.Adapter<DiscussionAdapter.ViewHolder> {

    private final TypedValue mTypedValue = new TypedValue();
    private int mBackground;
    private ArrayList<Discussion> mDiscussion;
    Context context;
    RecyclerView recyclerView;

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView mAuthor, mCommentTime, theComment, txtIndent;
        public RelativeLayout relativeLayout;

        public ViewHolder(View view) {
            super(view);

            mView = view;
            mAuthor = view.findViewById(R.id.author);
            mCommentTime = view.findViewById(R.id.time);
            theComment = view.findViewById(R.id.the_comment);
            txtIndent = view.findViewById(R.id.indent);
            relativeLayout = view.findViewById(R.id.relative_comment_list);

        }

        @Override
        public String toString() {
            return super.toString() + " '" + mAuthor.getText();
        }
    }

    public String getValueAt(int position) {
        return String.valueOf(mDiscussion.get(position).getId());
    }


    public DiscussionAdapter(Context context, ArrayList<Discussion> discussions) {
        context.getTheme().resolveAttribute(R.attr.selectableItemBackground, mTypedValue, true);
        this.context = context;
        this.mBackground = mTypedValue.resourceId;
        this.mDiscussion = discussions;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.comment_list, parent, false);
        view.setBackgroundResource(mBackground);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final Discussion model = (Discussion) mDiscussion.get(position);
        String author = "";
        Integer time = 0;
        String comment = "";

        if(model.getBy() != null) author = model.getBy();
        if(model.getTime() != null) time = model.getTime();
        if(model.getText() != null) {
            Spanned commentSpanned = Html.fromHtml(StringEscapeUtils.unescapeHtml4(model.getText()));
            comment =  commentSpanned.toString();
        }

        holder.mAuthor.setText(author);
        holder.mCommentTime.setText(String.valueOf(Misc.formatTime(time)));
        holder.theComment.setText(String.valueOf(comment));

        if(model.getLevel() != 0) {
            RelativeLayout.LayoutParams layoutParams =
                    new RelativeLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

            int level = 0;

            for(int i = 0; i < model.getLevel(); i++) {
                level = level + 10;
            }

            layoutParams.setMargins(level, 0, 0, 0);
            holder.relativeLayout.setLayoutParams(layoutParams);
            holder.relativeLayout.requestLayout();

        }
    }

    @Override
    public int getItemCount() {
        return (null != mDiscussion ? mDiscussion.size() : 0);
    }

    public void addAll(List<Discussion> data) {
        notifyDataSetChanged();
    }
}
