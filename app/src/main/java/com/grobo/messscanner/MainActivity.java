package com.grobo.messscanner;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.grobo.messscanner.show.ShowActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);

        if (preferences.getBoolean("first_run", true)) {
            startActivity(new Intent(MainActivity.this, ChooseMessActivity.class));
        }

        View scan = findViewById(R.id.main_scan);
        scan.setOnClickListener(v -> startActivity(new Intent(MainActivity.this, ScanActivity.class)));

        View data = findViewById(R.id.main_data);
        data.setOnClickListener(v -> startActivity(new Intent(MainActivity.this, ShowActivity.class)));
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate( R.menu.menu_main, menu );
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.menu_action_sync) {
            startActivity(new Intent(MainActivity.this, SyncActivity.class));
            return true;
        }
        return super.onOptionsItemSelected( item );
    }
}
