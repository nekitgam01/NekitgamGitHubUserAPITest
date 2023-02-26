package ru.nekitgam.githubusersapi;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatDelegate;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.scalars.ScalarsConverterFactory;
import ru.nekitgam.githubusersapi.api.ApiService;

public class MainActivity extends AppCompatActivity {

    public EditText etSearch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Включение темной темы оформления
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);

        //Привязка кнопки из активности к переменной
        etSearch = findViewById(R.id.etSearch);
    }

    /*
     * Функция добавления пунктов меню в верхнюю панель данной активности
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.userlist_menu, menu);
        return true;
    }

    /*
     * Функция обработки меню верхней панели данной активности
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // При нажатии кнопки авторизации в GitHub
            case R.id.action_auth:
                //Открытие активности авторизации
                Intent intent = new Intent(this,LoginActivity.class);
                this.startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    /*
     * Данная функция отвечает за нажатие кнопки поиска пользователей
     */
    public void buttonSearchClick(View view) {
        GetUserListAData();
    }

    /*
     * Данная функция отвечает за отправку get запроса с поиском пользователя в GitHub
     */
    public void GetUserListAData() {
        Retrofit retrofit = new Retrofit.Builder()
                .addConverterFactory(ScalarsConverterFactory.create())
                .baseUrl("https://api.github.com/")
                .build();

        ApiService service = retrofit.create(ApiService.class);

        Call<String> stringCall = service.getSearchUsers(etSearch.getText().toString());
        // Обрабатываем ответ от сервера тут
        stringCall.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                String responseString = response.body();
                if (responseString!=null) {
                    Toast.makeText(MainActivity.this, responseString, Toast.LENGTH_LONG).show();
                }
                else {
                    Toast.makeText(MainActivity.this, "Никого не найдено", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Toast.makeText(MainActivity.this, call.toString(), Toast.LENGTH_LONG).show();
            }
        });
    }
}