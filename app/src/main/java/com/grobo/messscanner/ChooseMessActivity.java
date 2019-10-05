package com.grobo.messscanner;

import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

public class ChooseMessActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_mess);

        findViewById(R.id.mess_bh1_1).setOnClickListener(this);
        findViewById(R.id.mess_bh1_2).setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        int mess = -1;
        switch (v.getId()) {
            case R.id.mess_bh1_1:
                mess = 1;
                break;
            case R.id.mess_bh1_2:
                mess = 2;
                break;
        }

        if (mess != -1) {
            PreferenceManager.getDefaultSharedPreferences(this)
                    .edit()
                    .putInt("mess", mess)
                    .putBoolean("first_run", false)
                    .apply();
            finish();
        }
    }
}
