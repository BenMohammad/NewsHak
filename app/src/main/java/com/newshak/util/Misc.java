package com.newshak.util;

import android.content.Context;
import android.text.format.DateUtils;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.ColorRes;
import androidx.annotation.StringRes;

import com.google.android.material.snackbar.Snackbar;
import com.newshak.R;

public class Misc {

    private Context mContext;

    public Misc(Context context) {
        this.mContext = context;
    }

    public static CharSequence formatTime(long hnTimestamp) {
        hnTimestamp = 1000 * hnTimestamp;
        CharSequence HNTime = DateUtils.getRelativeTimeSpanString(hnTimestamp, System.currentTimeMillis(), DateUtils.MINUTE_IN_MILLIS);
        return HNTime;
    }

    public static void displayLongToast(Context context, CharSequence text) {
        Toast.makeText(context, text, Toast.LENGTH_LONG).show();
    }

    public static void displayLongToast(Context context, @StringRes int ResId) {
        Toast.makeText(context, ResId, Toast.LENGTH_LONG).show();
    }

    public static void setSnackBarTextColor(Snackbar snackBar, Context context, @ColorRes int color) {
        TextView snackBarText = (TextView) snackBar.getView().findViewById(com.google.android.material.R.id.snackbar_text);
        snackBar.setTextColor(context.getResources().getColor(color));

    }}
