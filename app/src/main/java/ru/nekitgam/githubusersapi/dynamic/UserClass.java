package ru.nekitgam.githubusersapi.dynamic;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import org.json.JSONArray;
import org.json.JSONException;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.scalars.ScalarsConverterFactory;
import ru.nekitgam.githubusersapi.R;
import ru.nekitgam.githubusersapi.UserInfoActivity;
import ru.nekitgam.githubusersapi.api.ApiService;

public class UserClass {
    public LinearLayout line;
    public Button userInfo;
    public WebView avatar;


    @SuppressLint("SetTextI18n")
    public UserClass(Context context, String name, String imgLink, boolean onFollowers){
        userInfo = new Button(context);
        avatar = new WebView(context);
        line = new LinearLayout(context);

        if (onFollowers){
            GetFollowers(name,context);
        }
        else {
            userInfo.setText(" "+name);
        }


        line.setVerticalGravity(Gravity.CENTER);
        line.setHorizontalGravity(Gravity.LEFT);
        line.setBackgroundResource(R.drawable.border_dark_gray);



        LinearLayout.LayoutParams p1 = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        p1.weight = 1F;
        userInfo.setLayoutParams(p1);
        userInfo.setBackgroundResource(android.R.color.transparent);

        userInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, UserInfoActivity.class);
                intent.putExtra("name",name);
                context.startActivity(intent);
            }
        });

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



    public void GetFollowers(String name, Context context) {
        Retrofit retrofit = new Retrofit.Builder()
                .addConverterFactory(ScalarsConverterFactory.create())
                .baseUrl("https://api.github.com/")
                .build();

        ApiService service = retrofit.create(ApiService.class);

        Call<String> stringCall = service.getFollowersForUser(name);
        // Обрабатываем ответ от сервера тут
        stringCall.enqueue(new Callback<String>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                String responseString = response.body();
                if (responseString!=null) {
                    try {
                        //Toast.makeText(context, responseString, Toast.LENGTH_LONG).show();
                        JSONArray obj = new JSONArray(responseString);

                        int follows = obj.length();
                        userInfo.setText(" "+name+", Подписч.:"+ follows);

                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }
                }
                else {
                    userInfo.setText(" "+name+", Подписч.:0");
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                userInfo.setText(name+" 0");
            }
        });
    }
}
