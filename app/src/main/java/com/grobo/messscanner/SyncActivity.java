package com.grobo.messscanner;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;

import com.grobo.messscanner.database.MessModel;
import com.grobo.messscanner.database.MessViewModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class SyncActivity extends AppCompatActivity {

    private ProgressDialog progressDialog;
    private MessViewModel messViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sync);

        messViewModel = ViewModelProviders.of(this).get(MessViewModel.class);
        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.setIndeterminate(true);
        progressDialog.setCanceledOnTouchOutside(false);

        findViewById(R.id.button_sync).setOnClickListener(view -> {
            new AlertDialog.Builder(this)
                    .setTitle("Alert !!")
                    .setMessage("Are you sure you want to sync ??\nYou need to do this only once a week.")
                    .setPositiveButton("OK", (dialogInterface, i) -> {
                        sync();
                    })
                    .setNegativeButton("Cancel", (dialogInterface, i) -> {
                        if (dialogInterface != null) dialogInterface.dismiss();
                    }).show();
        });

    }

    private void sync() {

        progressDialog.setMessage("Loading data... Please wait.");
        progressDialog.show();

        String url = "https://spreadsheets.google.com/tq?key=1wRIRI9XbsD93IvgTk3kK69xjH2SmkISLxVH6I7sPI1w";

        new DownloadWebpageTask(this::processJson).execute(url);
    }

    private void processJson(JSONObject object) {

        if (object != null) {

            messViewModel.deleteAll();

            try {
                JSONArray rows = object.getJSONArray("rows");

                for (int r = 0; r < rows.length(); ++r) {
                    JSONObject row = rows.getJSONObject(r);
                    JSONArray columns = row.getJSONArray("c");

                    String roll = columns.getJSONObject(2).getString("v");
                    String name = columns.getJSONObject(3).getString("v");
                    int mess = columns.getJSONObject(4).getInt("v");
                    String token = columns.getJSONObject(5).getString("v");

                    MessModel current = new MessModel(roll, name, mess, token);
                    messViewModel.insert(current);

                    Log.e(getClass().getSimpleName(), name);
                }

                Toast.makeText(this, "Sync Successful.", Toast.LENGTH_LONG).show();

            } catch (JSONException e) {
                e.printStackTrace();
                Toast.makeText(this, "Sync failed !!!!!", Toast.LENGTH_LONG).show();
            }
        } else {
            Toast.makeText(this, "Sync failed !!!!!", Toast.LENGTH_LONG).show();
        }

        if (progressDialog != null) progressDialog.dismiss();
    }
}
