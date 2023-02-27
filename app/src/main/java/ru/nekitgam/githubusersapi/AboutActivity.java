package ru.nekitgam.githubusersapi;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import ru.nekitgam.githubusersapi.dynamic.MetaDataClass;

public class AboutActivity extends AppCompatActivity {

    public LinearLayout llDatalist;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        //Привязка Линейки к переменной
        llDatalist = findViewById(R.id.llDataList);

        //Создание TextView для отображения информации
        TextView login = new TextView(this);
        TextView bio = new TextView(this);
        TextView blog = new TextView(this);
        TextView email = new TextView(this);
        TextView followers = new TextView(this);
        TextView following = new TextView(this);
        TextView location = new TextView(this);

        //Присвоение данных к TextView, полученных с LoginActivity по intent
        login.setText("Логин: "+getIntent().getStringExtra("login"));
        bio.setText("Биография: "+getIntent().getStringExtra("bio"));
        blog.setText("Блог: "+getIntent().getStringExtra("blog"));
        email.setText("Email: "+getIntent().getStringExtra("email"));
        followers.setText("Подписчики: "+getIntent().getStringExtra("followers"));
        following.setText("Подписки: "+getIntent().getStringExtra("following"));
        location.setText("Расположение: "+getIntent().getStringExtra("location"));

        //Добавление в линейку данных
        llDatalist.addView(login);
        llDatalist.addView(bio);
        llDatalist.addView(blog);
        llDatalist.addView(email);
        llDatalist.addView(followers);
        llDatalist.addView(following);
        llDatalist.addView(location);
    }

    /*
     * Функция для обработки нажатия "назад"
     */
    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this,MainActivity.class);
        this.startActivity(intent);
    }

    /*
     * Функция добавления пунктов меню в верхнюю панель данной активности
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.about_menu, menu);
        return true;
    }

    /*
     * Функция обработки меню верхней панели данной активности
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // При нажатии кнопки авторизации в GitHub
            case R.id.action_unauth:
                MetaDataClass md = new MetaDataClass();
                SharedPreferences mSettings = getSharedPreferences(
                        md.APP_PREFERENCES,
                        Context.MODE_PRIVATE
                );
                SharedPreferences.Editor editor = mSettings.edit();
                editor.putString("b", "");
                editor.putString("s", "false");
                editor.apply();

                //Открытие активности авторизации
                Intent intent = new Intent(this,LoginActivity.class);
                this.startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}