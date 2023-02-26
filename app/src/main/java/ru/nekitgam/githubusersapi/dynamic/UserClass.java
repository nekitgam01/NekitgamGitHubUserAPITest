package ru.nekitgam.githubusersapi.dynamic;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.v4.graphics.drawable.IconCompat;
import android.util.TypedValue;
import android.view.Gravity;
import android.webkit.WebView;
import android.widget.LinearLayout;
import android.widget.TextView;

import ru.nekitgam.githubusersapi.R;

public class UserClass {
    public LinearLayout line;
    public TextView userInfo;
    public WebView avatar;

    @SuppressLint("SetTextI18n")
    public UserClass(Context context, String name, Integer follows , String imgLink){
        userInfo = new TextView(context);
        avatar = new WebView(context);
        line = new LinearLayout(context);

        line.setVerticalGravity(Gravity.CENTER);
        line.setHorizontalGravity(Gravity.LEFT);
        line.setBackgroundResource(R.drawable.border_dark_gray);

        userInfo.setText(name+" "+follows.toString());

        LinearLayout.LayoutParams p1 = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        p1.weight = 1F;
        userInfo.setLayoutParams(p1);
        userInfo.setBackgroundResource(android.R.color.transparent);

        avatar.loadUrl(imgLink);
        avatar.getSettings().setLoadWithOverviewMode(true);
        avatar.getSettings().setUseWideViewPort(true);

        int width = (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                context.getResources().getDimension(R.dimen.image_size),
                context.getResources().getDisplayMetrics()
        );

        LinearLayout.LayoutParams p2 = new LinearLayout.LayoutParams(
                width,
                width/2
        );
        p2.weight = 1F;
        avatar.setLayoutParams(p2);

        line.addView(avatar);
        line.addView(userInfo);
    }
}
