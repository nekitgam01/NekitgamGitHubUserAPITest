package ru.nekitgam.githubusersapi;

import android.annotation.SuppressLint;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.Space;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.scalars.ScalarsConverterFactory;
import ru.nekitgam.githubusersapi.api.ApiService;
import ru.nekitgam.githubusersapi.dynamic.RepoClass;
import ru.nekitgam.githubusersapi.dynamic.UserClass;

public class UserInfoActivity extends AppCompatActivity {

    public LinearLayout llDataList;

    public ArrayList<RepoClass> repoList = new ArrayList<>();

    public String userName = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info);

        llDataList = findViewById(R.id.llDataList);

        userName = getIntent().getStringExtra("name");
        GetRepoListData();
    }

    public void GetRepoListData() {
        Retrofit retrofit = new Retrofit.Builder()
                .addConverterFactory(ScalarsConverterFactory.create())
                .baseUrl("https://api.github.com/")
                .build();

        ApiService service = retrofit.create(ApiService.class);

        Call<String> stringCall;
            stringCall = service.getReposForUser(userName);

        // Обрабатываем ответ от сервера тут
        stringCall.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                String responseString = response.body();
                if (responseString!=null) {
                    //Toast.makeText(MainActivity.this, responseString, Toast.LENGTH_LONG).show();
                    try {
                        JSONArray array = new JSONArray(responseString);

                        repoList.clear();
                        llDataList.removeAllViews();

                        for (int i=0;i<array.length();i++)
                        {
                            String name = array.getJSONObject(i).getString("full_name");
                            String description = array.getJSONObject(i).getString("description");
                            if (description.equals("null")) description = "Не указано.";
                            @SuppressLint("SimpleDateFormat") SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
                            Date ddate = format.parse(array.getJSONObject(i).getString("pushed_at"));
                            @SuppressLint("SimpleDateFormat") SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss dd.MM.yyyy");
                            assert ddate != null;
                            String date = dateFormat.format(ddate);
                            String branch = array.getJSONObject(i).getString("default_branch");
                            String stars = array.getJSONObject(i).getString("stargazers_count");
                            String forks = array.getJSONObject(i).getString("forks_count");
                            String pl = array.getJSONObject(i).getString("language");
                            if (pl.equals("null")) pl = "Не указан.";

                            RepoClass uClass = new RepoClass(UserInfoActivity.this, name, description,date,branch,stars,forks,pl);
                           repoList.add(uClass);

                           Space space = new Space(UserInfoActivity.this);
                            LinearLayout.LayoutParams p2 = new LinearLayout.LayoutParams(
                                    LinearLayout.LayoutParams.MATCH_PARENT,
                                    5
                            );
                            space.setLayoutParams(p2);

                            llDataList.addView(repoList.get(repoList.size() - 1).line);
                            llDataList.addView(space);
                        }

                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    } catch (ParseException e) {
                        throw new RuntimeException(e);
                    }
                }
                else {
                    Toast.makeText(UserInfoActivity.this, "Данные не доступны!", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Toast.makeText(UserInfoActivity.this, "Проверьте интернет подключение!", Toast.LENGTH_LONG).show();
            }
        });
    }
}