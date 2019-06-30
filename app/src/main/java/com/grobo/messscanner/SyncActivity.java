package com.grobo.messscanner;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.grobo.messscanner.database.AppDatabase;
import com.grobo.messscanner.database.UserDao;
import com.grobo.messscanner.database.UserModel;
import com.grobo.messscanner.database.Utils;
import com.grobo.messscanner.network.GetDataService;
import com.grobo.messscanner.network.RetrofitClientInstance;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SyncActivity extends AppCompatActivity implements View.OnClickListener {

    private UserDao userDao;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sync);

        findViewById(R.id.button_sync_daily).setOnClickListener(this);
        findViewById(R.id.button_sync_monthly).setOnClickListener(this);

        userDao = AppDatabase.getDatabase(this).userDao();

        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.setIndeterminate(true);
        progressDialog.setCanceledOnTouchOutside(false);
    }

    @Override
    public void onClick(View v) {

        if (v.getId() == R.id.button_sync_daily) {

            progressDialog.setMessage("Loading data... Please wait.");
            progressDialog.show();

            syncDaily();

        } else if (v.getId() == R.id.button_sync_monthly) {

            progressDialog.setMessage("Loading data... Please wait.");
            progressDialog.show();

            syncMonthly();

        }

    }

    private void syncMonthly() {

        int mess = PreferenceManager.getDefaultSharedPreferences(this).getInt("mess", 1);

        GetDataService service = RetrofitClientInstance.getRetrofitInstance().create(GetDataService.class);

        Call<ResponseBody> call = service.getAllUsersOfThisMess(mess);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    String json = null;
                    try {
                        json = response.body().string();
                        parseMonthlyJson(json);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    Log.e("json", json);
                } else {
                    Log.e("json", "failed");
                    if (progressDialog!= null && progressDialog.isShowing()) progressDialog.dismiss();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.e("failure", t.getMessage());
                if (progressDialog!= null && progressDialog.isShowing()) progressDialog.dismiss();
            }
        });
    }

    private void parseMonthlyJson(String json) {
        Utils.InsertUser insertUser = new Utils.InsertUser(userDao);

        try {
            JSONObject jsonObject = new JSONObject(json);

            JSONArray array = jsonObject.getJSONArray("mess");
            for (int j = 0; j < array.length(); j++) {

                JSONObject user = array.getJSONObject(j);

                String studentMongoId = user.getString("studentMongoId");
                int messChoice = user.getInt("messChoice");

                JSONObject stud = user.getJSONObject("student");

                String instituteId = stud.getString("instituteId");
                String name = stud.getString("name");

                UserModel newUser = new UserModel(studentMongoId, instituteId, messChoice, name);

                insertUser.execute(newUser);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        if (progressDialog!= null && progressDialog.isShowing()) progressDialog.dismiss();

    }

    private void syncDaily() {
        int mess = PreferenceManager.getDefaultSharedPreferences(this).getInt("mess", 1);

        GetDataService service = RetrofitClientInstance.getRetrofitInstance().create(GetDataService.class);

        Call<ResponseBody> call = service.getCancelledData(mess);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    String json = null;
                    try {
                        json = response.body().string();
                        parseDailyJson(json);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    Log.e("json", json);
                } else {
                    Log.e("json", "failed");
                    if (progressDialog!= null && progressDialog.isShowing()) progressDialog.dismiss();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.e("failure", t.getMessage());
                if (progressDialog!= null && progressDialog.isShowing()) progressDialog.dismiss();
            }
        });
    }

    private void parseDailyJson(String json) {

        Utils.InsertUser insertUser = new Utils.InsertUser(userDao);
        Utils.LoadUserByMongoId loadUser = new Utils.LoadUserByMongoId(userDao);

        try {
            JSONObject jsonObject = new JSONObject(json);

            JSONArray array = jsonObject.getJSONArray("mess");
            for (int j = 0; j < array.length(); j++) {

                JSONObject user = array.getJSONObject(j);

                String studentMongoId = user.getJSONObject("student").getString("_id");

                JSONArray cancelledMeals = user.getJSONArray("cancelledMeals");
                List<String> foodData = new ArrayList<>();
                for (int k = 0; k < cancelledMeals.length(); k++) {
                    foodData.add(cancelledMeals.getString(k));
                }

                UserModel existingUser = loadUser.execute(studentMongoId).get();
                if (existingUser != null) {
                    existingUser.setFoodData(foodData);
                    insertUser.execute(existingUser);
                }

            }

        } catch (JSONException | ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }

        if (progressDialog!= null && progressDialog.isShowing()) progressDialog.dismiss();

    }
}
