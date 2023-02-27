package ru.nekitgam.githubusersapi.dynamic;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.Gravity;
import android.widget.LinearLayout;
import android.widget.TextView;

import ru.nekitgam.githubusersapi.R;


/*
 * Класс для динамического создания списка данных о репозитории
 */
public class RepoClass {
    public LinearLayout line;

    public TextView name;
    public TextView description;
    public TextView date;
    public TextView branch;
    public TextView stars;

    public TextView forks;
    public TextView programmingLanguage;

    @SuppressLint("SetTextI18n")
    public RepoClass(Context context, String rName, String rDescription, String rDate,
                     String rBranch, String rStars, String rForks, String rPL) {
        line = new LinearLayout(context);

        line.setVerticalGravity(Gravity.CENTER);
        line.setHorizontalGravity(Gravity.LEFT);
        line.setBackgroundResource(R.drawable.border_dark_gray);
        line.setOrientation(LinearLayout.VERTICAL);

        name = new TextView(context);
        description = new TextView(context);
        date = new TextView(context);
        branch = new TextView(context);
        stars = new TextView(context);
        programmingLanguage = new TextView(context);
        forks = new TextView(context);

        name.setText("Имя репозитория: "+rName);
        description.setText("Описание: "+rDescription);
        date.setText("Последний commit: "+rDate);
        branch.setText("Основная ветка: "+rBranch);
        forks.setText("Количество форков: "+rForks);
        stars.setText("Количество звезд: "+rStars);
        programmingLanguage.setText("Язык программирования: "+rPL);

        line.addView(name);
        line.addView(date);
        line.addView(description);
        line.addView(branch);
        line.addView(forks);
        line.addView(stars);
        line.addView(programmingLanguage);
    }
}
