package ru.nekitgam.githubusersapi;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.webkit.WebView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.scalars.ScalarsConverterFactory;
import ru.nekitgam.githubusersapi.api.ApiService;
import ru.nekitgam.githubusersapi.dynamic.MetaDataClass;
import ru.nekitgam.githubusersapi.dynamic.UserClass;

public class LoginActivity extends AppCompatActivity {

    public boolean showPassword = false;
    public WebView wvGitHub;
    public EditText etToken;

    public ImageButton btnShowPassword;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //Вызов сайта GitHub для получения токена
        wvGitHub = findViewById(R.id.wvGitHub);
        wvGitHub.loadUrl("https://github.com/settings/apps");

        //Привязка EditText с токеном к переменной
        etToken = findViewById(R.id.etPassword);

        //Привязка кнопки показа токена к переменной
        btnShowPassword = findViewById(R.id.btnShowPassword);

        //Получение информации оранее введеном токине и автоматическое его использование
        MetaDataClass md = new MetaDataClass();
        SharedPreferences mSettings = getSharedPreferences(md.APP_PREFERENCES, Context.MODE_PRIVATE);
        if(mSettings.contains("s")) {
            if (mSettings.getString("s", "").equals("true")) {
                if (mSettings.contains("b")) {

                    String decodedPassword  = md.decodeB64(mSettings.getString("b", ""));

                    etToken.setText(decodedPassword);
                    GetAuth();
                }
            }
        }
        else {
            Toast.makeText(LoginActivity.this, "Для авторизации создайте \"личный токен\" в личном кабинете GitHub и скопируйте в поле ввода!", Toast.LENGTH_LONG).show();
        }
    }

    /*
     * Функция нажатия кнопки "Войти"
     */
    public void onClick(View view){
        GetAuth();
    }

    /*
     * Функция отправки запроса на сервер Github и обработка ответа
     */
    public void GetAuth() {
        Retrofit retrofit = new Retrofit.Builder()
                .addConverterFactory(ScalarsConverterFactory.create())
                .baseUrl("https://api.github.com/")
                .build();

        ApiService service = retrofit.create(ApiService.class);

        Call<String> stringCall;

        stringCall = service.getAuth("Bearer "+etToken.getText().toString());

        // Обрабатываем ответ от сервера тут
        stringCall.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                String responseString = response.body();
                if (responseString != null) {

                    JSONObject obj = null;
                    try {
                        obj = new JSONObject(responseString);


                    MetaDataClass md = new MetaDataClass();


                            Toast.makeText(
                                    LoginActivity.this,
                                    "Авторизация успешна",
                                    Toast.LENGTH_LONG
                            ).show();

                            //Шифруем и сохраняем токен для автовхода
                            String encodedPassword =
                                    md.encodeB64(etToken.getText().toString());

                            SharedPreferences mSettings = getSharedPreferences(
                                    md.APP_PREFERENCES,
                                    Context.MODE_PRIVATE
                            );
                            SharedPreferences.Editor editor = mSettings.edit();
                            editor.putString("b", encodedPassword);
                            editor.putString("s", "true");
                            editor.apply();

                            //Получем данные с JSON ответа
                            String login = obj.getString("login");
                            String bio = obj.getString("bio");
                            if (bio.equals("null")) bio = "Не указано.";
                            String blog = obj.getString("blog");
                            if (blog.equals("null")) blog = "Не указано.";
                            String email = obj.getString("email");
                            if (email.equals("null")) email = "Не указано.";
                            String followers = obj.getString("followers");
                            String following = obj.getString("following");
                            String location = obj.getString("location");
                            if (location.equals("null")) location = "Не указано.";

                            //передаем данные активности AboutAcivity
                            Intent intent = new Intent(LoginActivity.this, AboutActivity.class);
                            intent.putExtra("login", login);
                            intent.putExtra("bio", bio);
                            intent.putExtra("blog", blog);
                            intent.putExtra("email", email);
                            intent.putExtra("followers", followers);
                            intent.putExtra("following", following);
                            intent.putExtra("location", location);
                            startActivity(intent);

                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }
                } else {
                    Toast.makeText(LoginActivity.this, "Авторизация не удалась!", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Toast.makeText(LoginActivity.this, "Проверьте интернет подключение!", Toast.LENGTH_LONG).show();
            }
        });
    }

    /*
     * Функция показа и скрытия токена по нажатии кнопки
     */
    public void buttonShowPasswordClick(View view) {
        if (!showPassword) {
            etToken.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PERSON_NAME);
            showPassword = true;
            btnShowPassword.setImageResource(android.R.drawable.ic_dialog_alert);
        }
        else {
            etToken.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
            showPassword = false;
            btnShowPassword.setImageResource(android.R.drawable.ic_lock_idle_lock);
        }
    }
}