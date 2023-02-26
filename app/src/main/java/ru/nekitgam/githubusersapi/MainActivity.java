package ru.nekitgam.githubusersapi;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatDelegate;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.Toast;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.scalars.ScalarsConverterFactory;
import ru.nekitgam.githubusersapi.api.ApiService;
import ru.nekitgam.githubusersapi.dynamic.UserClass;

public class MainActivity extends AppCompatActivity {

    public EditText etSearch;

    public Switch swFollow;
    public ArrayList<UserClass> userList = new ArrayList<>();

    public LinearLayout llDataList;

    public long backPressedTime = 0;
    public long backPressedTimeSearch = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Включение темной темы оформления
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);

        //Привязка кнопки из активности к переменной
        etSearch = findViewById(R.id.etSearch);

        //Привязка выключателя из активности к переменной
        swFollow = findViewById(R.id.swFollow);

        //Привязка Линейки за активности к переменной
        llDataList = findViewById(R.id.llDataList);
    }

    /*
     * Функция предостережения от случайного нажатия "назад". Закрывает приложение только при
     * двойном нажатии кнопки "назад"
     */
    @Override
    public void onBackPressed() {
        if (backPressedTime + 3000 > System.currentTimeMillis()) {
            super.onBackPressed();
            finishAffinity();
            System.exit(0);
        } else {
            Toast.makeText(this, "Нажмите еще раз \"назад\" для выхода!", Toast.LENGTH_LONG).show();
        }
        backPressedTime = System.currentTimeMillis();
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
        if (swFollow.isChecked()) {
            if (backPressedTimeSearch + 11000 > System.currentTimeMillis()) {
                Toast.makeText(this, "Следующая попытка только через 10 секунд!", Toast.LENGTH_LONG).show();
            } else {
                GetUserListAData();
                if (!etSearch.getText().toString().equals("")) {
                    backPressedTimeSearch = System.currentTimeMillis();
                }
            }
        }
        else {
            GetUserListAData();
        }

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

        /*
         * Так как гитхаб ограничивает количество запросов к api до 60 в час минут,
         * пришлось ограничить количество пользователей в ответе сервера (до 3), чтобы
         * не запрашивать фолловеров у пользователей больше 3 раз в секунду (при этом
         * огрничение по времени добавлено на кнопку - запрос раз в 11 секунд.).
         * Чтобы запросить до 30 пользователей без фолловеров в любое время -
         * вверху активти добавлена галочка "Запрашивать количество фолловеров". Убрав
         * ее - можно запрашивать чаще, но количества фолловеров не будет показано!
         * Окончание запросов к GitHub (60 в час) приведет к тому, что апи работать не будет,
         * Придется ждать окончания часа. Открытие записи тоже забирает 2 запроса из 60.
         */
        Call<String> stringCall;
        if (swFollow.isChecked()){
            stringCall = service.getSearchUsers(etSearch.getText().toString(), 3);
        }
        else {
            stringCall = service.getSearchUsers(etSearch.getText().toString(), 30);
        }
        // Обрабатываем ответ от сервера тут
        stringCall.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                String responseString = response.body();
                if (responseString!=null) {
                    //Toast.makeText(MainActivity.this, responseString, Toast.LENGTH_LONG).show();
                    try {
                        JSONObject obj = new JSONObject(responseString);

                        JSONArray array = obj.getJSONArray("items");

                        userList.clear();
                        llDataList.removeAllViews();

                        for (int i=0;i<array.length();i++)
                        {
                            String imgLink = array.getJSONObject(i).getString("avatar_url");
                            String name = array.getJSONObject(i).getString("login");

                            UserClass uClass = new UserClass(MainActivity.this, name, imgLink,swFollow.isChecked());
                            userList.add(uClass);

                            llDataList.addView(userList.get(userList.size() - 1).line);
                        }

                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }
                }
                else {
                    Toast.makeText(MainActivity.this, "Пользователь не найден!", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Toast.makeText(MainActivity.this, "Проверьте интернет подключение!", Toast.LENGTH_LONG).show();
            }
        });
    }
}